package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.ClassDef;
import gov.va.ehat.model.annotationAdminSchema.ClassRelDef;
import gov.va.ehat.model.annotationAdminSchema.DefaultDef;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class AnalyteDocument
{
	protected ArrayList<AdminAnnotation> adminAnnotations = new ArrayList<AdminAnnotation>();
	protected ArrayList<KnowtatorAnnotation> knowtatorAnnotations = new ArrayList<KnowtatorAnnotation>();
	protected ArrayList<KnowtatorStringSlotMention> knowtatorStringSlotMentions = new ArrayList<KnowtatorStringSlotMention>();
	protected ArrayList<KnowtatorComplexSlotMention> knowtatorComplexSlotMentions = new ArrayList<KnowtatorComplexSlotMention>();
	protected ArrayList<KnowtatorClassMention> knowtatorClassMentions = new ArrayList<KnowtatorClassMention>();

	// These are obsolete...
	private HashMap<String, Annotation> annotationsByAnnotationAdminID = new HashMap<String, Annotation>();
	public HashMap<String, Annotation> getAnnotationsByAnnotAdminID() {
		return annotationsByAnnotationAdminID;
	}
	
	public AdminAnnotation getAdminAnnotationByTempId(String tempId)
	{
		AdminAnnotation adminAnnotation = null;
		for(AdminAnnotation tAdminAnnotation : adminAnnotations)
		{
			String tTempId = "" + tAdminAnnotation.getTempId();
			if(tTempId == tempId)
			{
				adminAnnotation = tAdminAnnotation;
				break;
			}
		}
		return adminAnnotation;
	}

	@SuppressWarnings("unchecked")
	public void populateFromKnowtatorXMLRoot(Element knowtatorDocRoot, String adminAnalyteId, String adminSchemaId, String adminUserId) throws DataConversionException, TranslationException {
		/*
		 * Four element types that I know of. 
		 * 1) Annotation (Becomes annotation?)
		 * 2) Class Mention (Links a class back to an annotation. Can have multiple slots of attributes or other classes.)
		 * 3) Complex Slot Mention (Becomes class annotation)
		 * 4) String Slot Mention (Becomes attribute annotation)
		 * 
		 * My approach will be to first gather all annotations and store them in a map by "mention ID"
		 * If order is important we can put in a parallel ArrayList but for now I'll not worry about that.
		 */
		/*
		 * BRAD's Changes 6/7/12
		 * Four element types that I know of. 
		 * 1) Annotation (Annotation created)
		 * 2) String Slot Mention (Feature of type FEATURE_TYPE_ATTRIBUTE_TEXT - TBD: need FEATURE_TYPE_ATTRIBUTE_NUMERIC also).
		 * 3) Complex Slot Mention (Feature of type FEATURE_TYPE_CLASSIFICATION_RIGHT).  One class relationship
		 *    annotation is created when the first complex slot mention is found.  Other complex slot mentions that reference that relationship
		 *    annotation will find rather than create.
		 * 4) Class Mention (Find created annotation1 by classMention id - same as annotation mention id.  Find features
		 *    by hasSlotMention ids and add to annotation1.  If a feature is found of type FEATURE_TYPE_CLASSIFICATION_RIGHT,
		 *    get the class relationship annotation saved on the right annotation feature.  A feature of type FEATURE_TYPE_CLASSIFICATION_LEFT
		 *    is added to annotation2 which points to annotation1.  Also, feature of type FEATURE_TYPE_CLASSIFICATION_RIGHT is added
		 *    to annotation2.
		 * 
		 * These are listed in this order: annotation, stringSlotMention, complexSlotMention, then classMention.  So the classMention
		 * will always refer to things that have already been read in.
		 * 
		 * My approach will be to first gather all artifacts and store them in a map by mentionId.  I need to store class relationship
		 * annotations by name, because eHost does not create a special annotation for class relationships as Annotation Admin does.  So,
		 * we have to create an class relationship annotation when we find out first complexSlotMention and then look it up by mentionSlotId for
		 * every other complexSlotMention that references that same relationship.
		 * If order is important we can put in a parallel ArrayList but for now I'll not worry about that.
		 */
		HashMap<String, AdminObject> adminObjectsByMentionId = new HashMap<String, AdminObject>();
		HashMap<String, AdminObject> classRelAnnotationsByClassRelDefName = new HashMap<String, AdminObject>();

		// Annotation
		List<Element> annotationElements = knowtatorDocRoot.getChildren("annotation");
		for(Element annotationElement: annotationElements)
		{
			Element mentionElement = annotationElement.getChild("mention");						
			if(mentionElement!=null)
			{
				String mentionId = mentionElement.getAttributeValue("id");
				AdminAnnotation adminAnnotation = new AdminAnnotation(mentionId, AnnotationAdminConst.ANNOTATION_TYPE_CLASSIFICATION,
																		AdminObject.getNextTempId(), adminAnalyteId, adminSchemaId, adminUserId);
				adminAnnotation.populateFromEHostXMLElement(annotationElement);
				adminObjectsByMentionId.put(mentionId, adminAnnotation);
				adminAnnotations.add(adminAnnotation);
			}
		}	

		// Attribute feature
		List<Element> stringSlotMentionElements = knowtatorDocRoot.getChildren("stringSlotMention");
		for(Element slotMentionElement: stringSlotMentionElements)
		{
			String mentionId = slotMentionElement.getAttributeValue("id");
			// TBD - Are FEATURE_TYPE_ATTRIBUTE_NUMERIC AND FEATURE_TYPE_ATTRIBUTE_OPTION also found in stringSlotMentions?
			AdminFeature adminFeature = new AdminFeature(mentionId, AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT,
															AdminObject.getNextTempId(), adminSchemaId);
			adminFeature.populateFromEHostXMLElement(slotMentionElement, adminObjectsByMentionId);
			adminObjectsByMentionId.put(mentionId, adminFeature);
		}	

		// Class relationship right annotation reference feature
		List<Element> complexSlotMentionElements = knowtatorDocRoot.getChildren("complexSlotMention");
		for(Element slotMentionElement: complexSlotMentionElements)
		{
			String mentionId = slotMentionElement.getAttributeValue("id");
			AdminFeature adminFeature = new AdminFeature(mentionId, AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT,
															AdminObject.getNextTempId(), adminSchemaId);
			adminFeature.populateFromEHostXMLElement(slotMentionElement, adminObjectsByMentionId);
			adminObjectsByMentionId.put(mentionId, adminFeature);
			
			Element mentionSlotElement = slotMentionElement.getChild("mentionSlot");
			String schemaObjName = mentionSlotElement.getAttribute("id").getValue();
			String relAdminAnnotationId = schemaObjName + "-" + mentionId;
			
			// Create a class relationship annotation if one does not already exist.
			// Note: There may be multiple complexSlotMentions that add right side annotation reference features to one class relationship annotation.
			AdminAnnotation relAdminAnnotation = (AdminAnnotation)classRelAnnotationsByClassRelDefName.get(relAdminAnnotationId);
			if(relAdminAnnotation == null)
			{
				relAdminAnnotation = new AdminAnnotation(mentionId, AnnotationAdminConst.ANNOTATION_TYPE_CLASSREL,
															AdminObject.getNextTempId(), adminAnalyteId, adminSchemaId, adminUserId);
				ClassRelDef classRelDef = (ClassRelDef)EHostToAnnotAdminTranslator.schema.getClassRelDefsByName().get(schemaObjName);
				relAdminAnnotation.setSchemaRefObj(classRelDef);
				
				// NOTE: No population from eHost XML because there is not class rel annotation xml object, per se.
				// Class Rel Annotations are tracked by schema object id: there is not mentionId for them.
				classRelAnnotationsByClassRelDefName.put(relAdminAnnotationId, relAdminAnnotation);
				adminAnnotations.add(relAdminAnnotation);
			}
			// Note: Do not add the right annotation feature to the relationship annotation yet - we will add it when we process the classMentions.
			// But, save the relationship annotation for when we need to link left and right annotation features as we process the classMentions which
			// reference right annotation features.
			adminFeature.relationshipAdminAnnotation = relAdminAnnotation;
		}	

		// Class mention annotation feature references
		List<Element> classMentionElements = knowtatorDocRoot.getChildren("classMention");
		for(Element classMentionElement: classMentionElements)
		{
			String mentionId = classMentionElement.getAttributeValue("id");
			String adminAnnotationMentionId = mentionId;
			AdminAnnotation adminAnnotation1 = (AdminAnnotation)adminObjectsByMentionId.get(adminAnnotationMentionId);
			
			Element mentionClassElement = classMentionElement.getChild("mentionClass");
			String classDefName = mentionClassElement.getAttributeValue("id");
			ClassDef classDef = EHostToAnnotAdminTranslator.schema.getClassDefsByName().get(classDefName);
			adminAnnotation1.setName(classDef.getName());
			adminAnnotation1.setSchemaRefObj(classDef);
			
			List<Element> hasSlotMentionElements = classMentionElement.getChildren("hasSlotMention");
			for(Element hasSlotMentionElement: hasSlotMentionElements) // Should only be features in here...
			{
				String tMentionId = hasSlotMentionElement.getAttributeValue("id");
				AdminFeature adminFeature = (AdminFeature)adminObjectsByMentionId.get(tMentionId);
				switch(adminFeature.getType())
				{
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT:
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_NUMERIC:
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION:
						adminFeature.setSchemaRefParentObj(adminAnnotation1.getSchemaRefObj());
						adminAnnotation1.getFeatures().add(adminFeature);
						break;
					case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_LEFT:
					case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT:						
						AdminAnnotation relAdminAnnotation = adminFeature.getRelationshipAdminAnnotation();
						DefaultDef rightAnnotationFeatureSchemaRefObj = relAdminAnnotation.getSchemaRefObj();
						relAdminAnnotation.setName(rightAnnotationFeatureSchemaRefObj.getName());
						adminFeature.setSchemaRefParentObj(rightAnnotationFeatureSchemaRefObj);
						
						// Create a left annotation feature
						AdminFeature leftAnnotationFeature = new AdminFeature(null, AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_LEFT,
								AdminObject.getNextTempId(), adminSchemaId);
						leftAnnotationFeature.setSchemaRefParentObj(relAdminAnnotation.getSchemaRefObj());
						DefaultDef leftAnnotationFeatureSchemaRefObj = adminAnnotation1.getSchemaRefObj(); // Same class def schema reference
						leftAnnotationFeature.setName(leftAnnotationFeatureSchemaRefObj.getName());
						leftAnnotationFeature.setSchemaRefObj(leftAnnotationFeatureSchemaRefObj);
						leftAnnotationFeature.setValue("" + adminAnnotation1.getTempId());
						
						relAdminAnnotation.getFeatures().add(leftAnnotationFeature);
						relAdminAnnotation.getFeatures().add(adminFeature); // Right annotation feature
						break;
				}
			}
			
			// Now we need to fix up the objects of type FEATURE_TYPE_CLASSIFICATION_RIGHT.  When they were created the classMentions to which they referred,
			// had not yet been processed.  As we process classMentions that have feature object referring to them by schemaRefObjAnnotationMentionId,
			// update the right annotation feature's schemaRefObj and its name.
			for(String key: adminObjectsByMentionId.keySet())
			{
				Object adminObject = (Object)adminObjectsByMentionId.get(key);
				if(adminObject instanceof AdminFeature)
				{
					AdminFeature adminFeature = (AdminFeature)adminObject;
					if(adminFeature.getType() == AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT &&
							adminFeature.getSchemaRefObjAnnotationMentionId().compareTo(mentionId) == 0)
					{
						adminFeature.setName(classDef.getName());
						adminFeature.setSchemaRefObj(classDef);
					}
				}
			}
		}
	}

	public Element getAnnotationAdminRootElement(String annotAdminAssignmentID)
	{
		Element root = new Element("annotations");
		root.setAttribute("analyteAssignmentId", annotAdminAssignmentID);
		Collection<Element> adds = new ArrayList<Element>();
		for(AdminAnnotation adminAnnotation: adminAnnotations)
		{
			Element annotElement = adminAnnotation.getAnnotationAdminElement();
			adds.add(annotElement);
		}
		root.addContent(adds);
		return root;
	}
	
	@SuppressWarnings("unchecked")
	public void populateFromAnnotationAdminXMLRoot(Element annotationAdminAnnotationsDocRoot) throws TranslationException, ParseException, DataConversionException 
	{            
		List<Element> annotationElements = annotationAdminAnnotationsDocRoot.getChildren("annotation");
		List<KnowtatorObject> allSlotMentions = new ArrayList<KnowtatorObject>();
		List<KnowtatorObject> relAnnotations = new ArrayList<KnowtatorObject>();
		for(Element annotationElement: annotationElements)
		{
			String adminIdStr = annotationElement.getAttributeValue("id");
			Long adminId = new Long(adminIdStr);
			
			String mentionId = KnowtatorObject.getNextMentionId();
			
			// Only if it has left and right classification features, is it a classRel type annotation.
			int type = AnnotationAdminConst.ANNOTATION_TYPE_CLASSIFICATION;
			
			KnowtatorAnnotation annotation = new  KnowtatorAnnotation(mentionId, adminId, type);
			annotation.populateFromAnnotationAdminXMLElement(annotationElement);

			Element featuresElement = annotationElement.getChild("features");
			List<Element> featureElements = featuresElement.getChildren("feature");
			List<KnowtatorObject> slotMentions = new ArrayList<KnowtatorObject>();
			for(Element featureElement: featureElements)
			{
				String featureAdminIdStr = featureElement.getAttributeValue("id");
				Long featureAdminId = new Long(featureAdminIdStr);
				
				String featureMentionId = KnowtatorObject.getNextMentionId();
				
				String featureTypeStr = featureElement.getAttributeValue("type");
				int featureType = new Integer(featureTypeStr).intValue();
				
				switch(featureType)
				{
					case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_LEFT:
						// These do not have a separate knowtator object.  The left annotation is the annotation with the same mention id as the
						// classMention.
						
						// Save the leftAnnotationAdminId on the relationship annotation so that we can add the complexSlotMention to the left
						// annotation's slotMention list after all annotations have been processed.
						Element elementsElement = featureElement.getChild("elements");
						Element elementElement = elementsElement.getChild("element");
						Element valueElement = elementElement.getChild("value");
						String valueStr = valueElement.getValue();
						Long leftAnnotationAdminId = new Long(valueStr);
						annotation.setLeftAnnotationAdminId(leftAnnotationAdminId);
						break;
					case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT:
						annotation.setType(AnnotationAdminConst.ANNOTATION_TYPE_CLASSREL);
						String cMentionId = KnowtatorObject.getNextMentionId();
						KnowtatorComplexSlotMention complexSlotMention = new KnowtatorComplexSlotMention(cMentionId);
						complexSlotMention.populateFromFeatureXMLElement(featureElement);
						complexSlotMention.setRelAnnotationAdminId(annotation.getAdminId());
						complexSlotMention.setMentionSlotId(annotation.getSchemaRefObjName());
						annotation.getSlotMentions().add(complexSlotMention);
						slotMentions.add(complexSlotMention);
						knowtatorComplexSlotMentions.add(complexSlotMention);
						break;
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT:
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_NUMERIC:
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_BLOB:
					case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION:
						// TBD - How do we keep track of these types in the eHost elements and xml so that we can return them to annotation admin WS
						annotation.setType(AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT);
						String sMentionId = KnowtatorObject.getNextMentionId();
						KnowtatorStringSlotMention stringSlotMention = new KnowtatorStringSlotMention(sMentionId);
						stringSlotMention.populateFromFeatureXMLElement(featureElement);
						slotMentions.add(stringSlotMention);
						knowtatorStringSlotMentions.add(stringSlotMention);
						break;
				}
			}
			allSlotMentions.addAll(slotMentions);
			if(annotation.getType() == AnnotationAdminConst.ANNOTATION_TYPE_CLASSREL)
			{
				relAnnotations.add(annotation);
			}
			annotation.getSlotMentions().addAll(slotMentions);
			knowtatorAnnotations.add(annotation);
		}
		
		// After all annotations have been processed, find complex slot mentions and fix up their
		// complexSlotMentionValue by finding the annotation whose adminId is equal to the complex slot's complexSlotMentionValueAdminId.
		// Also, add the complex slot mention to the annotation's slot mentions list.
		for(KnowtatorObject knowtatorObject: allSlotMentions)
		{
			if(knowtatorObject instanceof KnowtatorComplexSlotMention)
			{
				KnowtatorComplexSlotMention complexSlotMention = (KnowtatorComplexSlotMention)knowtatorObject;
				KnowtatorAnnotation rightAnnotation = getKnowtatorAnnotationByAdminId(complexSlotMention.getRightAnnotationAdminId());
				complexSlotMention.setComplexSlotMentionValue(rightAnnotation.getMentionId());
				KnowtatorAnnotation relAnnotation = getKnowtatorAnnotationByAdminId(complexSlotMention.getRelAnnotationAdminId());
				KnowtatorAnnotation leftAnnotation = getKnowtatorAnnotationByAdminId(relAnnotation.getLeftAnnotationAdminId());
				leftAnnotation.getSlotMentions().add(complexSlotMention);
			}
		}
		
		// We used the relAnnotations for complexSlotMention fixup, etc, but we do not want to write these to the knowtator file.
		knowtatorAnnotations.removeAll(relAnnotations);
		
		// Now all annotation slot mention lists have been updated and we can output all slot mentions.
		for(KnowtatorAnnotation annotation: knowtatorAnnotations)
		{
			KnowtatorClassMention classMention = new KnowtatorClassMention(annotation.getMentionId());
			classMention.getHasSlotMentions().addAll(annotation.getSlotMentions());
			classMention.setMentionClassId(annotation.getSchemaRefObjName());
			classMention.setMentionClassValue(annotation.getSpannedText());
			knowtatorClassMentions.add(classMention);
		}
	}
	
	protected KnowtatorAnnotation getKnowtatorAnnotationByAdminId(Long adminId)
	{
		KnowtatorAnnotation annotation = null;
		for(KnowtatorAnnotation knowtatorAnnotation : knowtatorAnnotations)
		{
			if(knowtatorAnnotation.getAdminId().longValue() == adminId.longValue())
			{
				annotation = knowtatorAnnotation;
			}
		}
		return annotation;
	}

	public Element getKnowtatorRootElementAfterBuildingFromAnnotationAdminXML()
	{
		Element root = new Element("annotations");
		root.setAttribute("textSource", "TBD");
		String annotatorName = EHostToAnnotAdminTranslator.getAnnotatorName();
		String annotatorId = EHostToAnnotAdminTranslator.getAnnotatorID();
		List<Element> elements = new ArrayList<Element>();
		for(KnowtatorAnnotation annotation: knowtatorAnnotations)
		{
			Element annotationElement = annotation.getAnnotationElement();
			elements.add(annotationElement);
		}
		for(KnowtatorStringSlotMention stringSlotMention: knowtatorStringSlotMentions)
		{
			Element stringSlotMentionElement = stringSlotMention.getStringSlotMentionElement();
			elements.add(stringSlotMentionElement);
		}
		for(KnowtatorComplexSlotMention complexSlotMention: knowtatorComplexSlotMentions)
		{
			Element complexSlotMentionElement = complexSlotMention.getComplexSlotMentionElement();
			elements.add(complexSlotMentionElement);
		}
		for(KnowtatorClassMention classMention: knowtatorClassMentions)
		{
			Element classMentionElement = classMention.getClassMentionElement();
			elements.add(classMentionElement);
		}
		root.addContent(elements);
		return root;
		/*
		 * 1) For each AnnotationClass:
		 * 		a) Generate a dynamic EHOST_Instance_X id attribute for the class.
		 * 		b) Create an <annotation> element with the attribute generated.
		 * 		c) Create a <classMention> element with the same attribute. (This ties them together)
		 * 2) For each AnnotationAttribute:
		 * 		a) Generate a dynamic EHOST_Instance_X id attribute for the attribute.
		 * 		b) Create a <stringSlotMention> element with the dynamic attribute.
		 * 		c) Find the <classMention> element to which it belongs, and add a <hasSlotMention> element pointing to the dynamic attribute created for the <stringSlotMention>
		 * 3) For each AnnotationComplexSlot:
		 * 		a) Generate a dynamic EHOST_Instance_X id attribute
		 * 		b) Create a <complexSlotMention> element with the dyn. attribute
		 * 		c) Find the <classMention> element to which it belongs, and
		 * 			i) For all classes SUBSEQUENT TO THE FIRST ONE
		 * 			ii) Add <hasSlotMention> to the <classMention> element that belongs to the FIRST one.
		 */
//		for(AnnotationClass a: annotationClasses)
//		{
//			Element el = new Element("annotation");
//			Element mention = new Element("mention");
//			String genEHID = "EHOST_Instance_"+getNextKnowtatorInstanceID();
//			mention.setAttribute("id", genEHID);
//			el.addContent(mention);
//			Element annotator = new Element("annotator");
//			annotator.setAttribute("id",annotatorId);
//			annotator.setText(annotatorName);
//			el.addContent(annotator);
//			ArrayList<Span> spans = a.getSpans();
//			Span mentionClassValue = null; // The redundant value EHost expects in its mentionClass value.
//			if(spans==null || spans.isEmpty())
//			{
//				ArrayList<AnnotationAttribute> attributes = a.getChartReaderAttributes();
//				if(attributes.size()>0)
//				{
//					spans = attributes.get(0).getSpans();
//				}
//			}
//			for(Span span: spans)
//			{
//				if(mentionClassValue == null)
//				{
//					mentionClassValue = span; // Grab the first one we see.
//				}
//				Element spanEl = new Element("span");
//				spanEl.setAttribute("start", span.getStartOffset().toString());
//				spanEl.setAttribute("end", span.getEndOffset().toString());
//				el.addContent(spanEl);
//				Element spannedText = new Element("spannedText");
//				spannedText.setText(span.getSpannedText());
//				el.addContent(spannedText);
//				
//			}
//			Date dt = a.getCreationDate();
//			if(dt==null)
//			{
//				System.err.println("Date was null! Be sure we're pulling in the correct date from Chart Reader's annotation XML.");
//				dt = GregorianCalendar.getInstance().getTime();
//			}
//			Element creationDate = new Element("creationDate");
//			creationDate.setText(KnowtatorAnnotation.getSDF().format(dt));
//			el.addContent(creationDate);
//			root.addContent(el);
//			Element cme = new Element("classMention");
//			cme.setAttribute("id", genEHID);
//			Element mentionClass = new Element("mentionClass");
//			mentionClass.setAttribute("id", a.getAnnotAdminSchemaTextName());
//			if(mentionClassValue!=null)
//			{
//				mentionClass.setText(mentionClassValue.getSpannedText());
//			}
//			cme.addContent(mentionClass);
//			root.addContent(cme);
//			/*
//			 * Set references to these for later use (if necessary)
//			 */
//			a.setGeneratedEHostClassMentionElement(cme);
//			a.setGeneratedEHostXMLElement(el);
//			for(AnnotationAttribute t: a.getChartReaderAttributes())
//			{
//				/*
//				 * Create a stringSlotMention for each attribute
//				 */
//				Element stringSlotMention = new Element("stringSlotMention");
//				String ssmID = "EHOST_Instance_"+getNextKnowtatorInstanceID();
//				stringSlotMention.setAttribute("id", ssmID);
//				Element mentionSlot = new Element("mentionSlot");
//				mentionSlot.setAttribute("id", t.getAnnotAdminSchemaTextName());
//				Element sVal = new Element("stringSlotMentionValue");
//				sVal.setAttribute("value", t.getValue());
//				stringSlotMention.addContent(mentionSlot);
//				stringSlotMention.addContent(sVal);
//				/*
//				 * Add the string slot mention to the class mention as a hasSlot
//				 */
//				Element hasSlotMention = new Element("hasSlotMention");
//				hasSlotMention.setAttribute("id", ssmID);
//				cme.addContent(hasSlotMention);
//				root.addContent(stringSlotMention);
//			}
//		}
//		for(KnowtatorComplexSlotMention a: annotationComplexSlots)
//		{
//			AnnotationClass firstOne = null;
//			ArrayList<Element> complexSlotElements = new ArrayList<Element>();
//			for(KnowtatorAnnotation ca: a.getReferencedAnnotations())
//			{
//				if(ca instanceof AnnotationClass)
//				{
//					/*
//					 * First one gets the hasSlots elements added to its classMention element.
//					 * All subsequent ones get complexSlotMention elements that point to them.
//					 */
//					if(firstOne == null)
//					{
//						firstOne = (AnnotationClass)ca;
//					}
//					else
//					{
//						Element complexSlotMention = new Element("complexSlotMention");
//						complexSlotMention.setAttribute("id", "EHOST_Instance_"+getNextKnowtatorInstanceID());
//						Element mentionSlot = new Element("mentionSlot");
//						mentionSlot.setAttribute("id", a.getAnnotAdminSchemaTextName());
//						complexSlotMention.addContent(mentionSlot);
//						Element complexSlotMentionValue = new Element("complexSlotMentionValue");
//						complexSlotMentionValue.setAttribute("id", ((AnnotationClass) ca).getGeneratedEHostClassMentionElement().getAttributeValue("id"));
//						complexSlotMention.addContent(complexSlotMentionValue);
//						root.addContent(complexSlotMention);
//						complexSlotElements.add(complexSlotMention);
//					}					
//				}
//			}
//			if(firstOne!=null)
//			{
//				Element cme = firstOne.getGeneratedEHostClassMentionElement();
//				if(cme!=null)
//				{
//					for(Element cse: complexSlotElements)
//					{
//						String idVal = cse.getAttributeValue("id");
//						Element hasSlotMention = new Element("hasSlotMention");
//						hasSlotMention.setAttribute("id", idVal);
//						cme.addContent(hasSlotMention);
//					}
//				}
//			}
//		}
		/*
    <annotation>
        <mention id="EHOST_Instance_0" />
        <annotator id="2">Tyler</annotator>
        <span start="30" end="43" />
        <spannedText>Datsun Sentra</spannedText>
        <creationDate>Mon Aug 29 10:33:02 MDT 2011</creationDate>
    </annotation>
    <stringSlotMention id="EHOST_Instance_3">
        <mentionSlot id="Name" />
        <stringSlotMentionValue value="Datsun Sentra" />
    </stringSlotMention>
    <classMention id="EHOST_Instance_0">
        <hasSlotMention id="EHOST_Instance_3" />
        <mentionClass id="Car">Datsun Sentra</mentionClass>
    </classMention>
    <annotation>
        <mention id="EHOST_Instance_1" />
        <annotator id="2">Tyler</annotator>
        <span start="0" end="12" />
        <spannedText>Jim Chandler</spannedText>
        <creationDate>Mon Aug 29 10:42:02 MDT 2011</creationDate>
    </annotation>
    <stringSlotMention id="EHOST_Instance_4">
        <mentionSlot id="Name" />
        <stringSlotMentionValue value="Jim" />
    </stringSlotMention>
    <complexSlotMention id="EHOST_Instance_5">
        <mentionSlot id="WhoAndWhere" />
        <complexSlotMentionValue value="EHOST_Instance_2" />
    </complexSlotMention>
    <classMention id="EHOST_Instance_1">
        <hasSlotMention id="EHOST_Instance_4" />
        <hasSlotMention id="EHOST_Instance_5" />
        <mentionClass id="Person">Jim Chandler</mentionClass>
    </classMention>
    <annotation>
        <mention id="EHOST_Instance_2" />
        <annotator id="2">Tyler</annotator>
        <span start="84" end="95" />
        <spannedText>VA hospital</spannedText>
        <creationDate>Mon Aug 29 10:42:08 MDT 2011</creationDate>
    </annotation>
    <stringSlotMention id="EHOST_Instance_6">
        <mentionSlot id="Name" />
        <stringSlotMentionValue value="VA" />
    </stringSlotMention>
    <classMention id="EHOST_Instance_2">
        <hasSlotMention id="EHOST_Instance_6" />
        <mentionClass id="Place">VA hospital</mentionClass>
    </classMention>
		 */
	}

	public ArrayList<AdminAnnotation> getAdminAnnotations() {
		return adminAnnotations;
	}

	public void setAdminAnnotations(ArrayList<AdminAnnotation> adminAnnotations) {
		this.adminAnnotations = adminAnnotations;
	}

	public ArrayList<KnowtatorAnnotation> getKnowtatorAnnotations() {
		return knowtatorAnnotations;
	}

	public void setKnowtatorAnnotations(
			ArrayList<KnowtatorAnnotation> knowtatorAnnotations) {
		this.knowtatorAnnotations = knowtatorAnnotations;
	}
}

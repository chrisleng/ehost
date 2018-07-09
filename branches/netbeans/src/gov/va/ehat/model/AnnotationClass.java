package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class AnnotationClass extends Annotation {
	/**
	 * eHost annotator
	 */
	private String annotator = "";
	public String getAnnotator() {
		return annotator;
	}
	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}
	
	private ArrayList<AnnotationAttribute> annotationAdminAttributes = new ArrayList<AnnotationAttribute>();
	private String classSimpleValueWhenNoAttributeDefsFound;
	private Element generatedEHostClassMentionElement;
	public ArrayList<AnnotationAttribute> getChartReaderAttributes() {
		return annotationAdminAttributes;
	}
	/**
	 * EHOST XML -- TO -- JAVA OBJECT
	 * 
	 * @param el
	 * @throws DataConversionException
	 */
	public void populateFromEHostXMLElement(Element el) throws DataConversionException {
		Element annotatorElement = el.getChild("annotator");
		Element spanElement = el.getChild("span");
		Element spannedTextElement = el.getChild("spannedText");
		Element creationDateElement = el.getChild("creationDate");
		
		setAnnotator(annotatorElement.getValue());
		Span span = new Span();
		span.setSpannedText(spannedTextElement.getValue());
		span.setStartOffset(spanElement.getAttribute("start").getIntValue());
		span.setEndOffset(spanElement.getAttribute("end").getIntValue());
		getSpans().add(span);
		String creationDateText = creationDateElement.getValue();
		Date creationDate = null;
		try {
			creationDate = DateUtil.getKnowtatorSDF().parse(creationDateText);
		} catch (ParseException e) {
			e.printStackTrace();
			creationDate = GregorianCalendar.getInstance().getTime();
		}
		setCreationDate(creationDate);
	}

	@Override
	public int getAnnotationAdminTypeId() {
		return 2;
	}
	
	/**
	 * JAVA OBJECT -- TO -- CHART READER XML
	 */
	@Override
	public Element getAnnotationAdminElement() {
		Element annotationElement = super.getAnnotationAdminElement();
		
		annotationElement.addContent(getAnnotationAdminFeaturesElement());
		
		return annotationElement;
	}
	
	public Element getAnnotationAdminFeaturesElement() {
		ArrayList<Element> children = new ArrayList<Element>();
		
		if(getClassRefs().size()>0)
		{
			AnnotationClassReference cref = getClassRefs().get(0);
//			Element el = new Element("feature");
//			Attribute tempId = new Attribute("tempid", Annotation.getNextTempID().toString());
//			Attribute type = new Attribute("type", "2");
//			el.setAttribute(tempId);
//			el.setAttribute(type);
//			Element name = new Element("name");
//			name.setText(cref.getClassDefinition().getName());
//			Element schemaRef = new Element("schemaRef");
//			schemaRef.setAttribute("tempid", Annotation.getNextTempID().toString());
			String schemaID = EHostToAnnotAdminTranslator.schema.getId().toString();
//			String classDefId = cref.getClassDefinition().getAnnotationAdminId().toString();
//			String uriValue = "schema:"+schemaID+";classDef:"+classDefId;
//			schemaRef.setAttribute("uri", uriValue);
//			schemaRef.setText("classDef");			
			Element features = new Element("elements");	
			
			if(getReferencedAttributes().size()>0)
			{
				for(AnnotationAttribute att: getReferencedAttributes())
				{
					Element feature = new Element("feature");
					Element classVal = new Element("element");
					classVal.setAttribute("tempid", Annotation.getNextTempID().toString());
					Element valueElement = new Element("value");
					valueElement.setText(att.getEHostTempId().toString());
					Element schemaSubRef = new Element("schemaRef");
					schemaSubRef.setAttribute("tempid", Annotation.getNextTempID().toString());
					String attributeDefId = att.getAttributeDef().getAnnotationAdminId().toString();
					String uriSubValue = "schema:"+schemaID+";attributeDef:"+attributeDefId;
					schemaSubRef.setAttribute("uri", uriSubValue);
					schemaSubRef.setText("attributeDef");
					classVal.addContent(valueElement);
					classVal.addContent(schemaSubRef);
//					classVals.addContent(classVal);
				}
			}
//			else
//			{
//				Element classVal = new Element("element");
//				classVal.setAttribute("tempid", Annotation.getNextTempID().toString());
//				Element valueElement = new Element("value");
//				valueElement.setText(cref.getValue());
//				classVal.addContent(valueElement);
//				classVals.addContent(classVal);
//			}
//			
//			el.addContent(name);
//			el.addContent(schemaRef);
//			el.addContent(classVals);
//			children.add(el);
		}
		
		Element featuresElement = new Element("features");
		featuresElement.addContent(children);
		return featuresElement;
	}
	
	/**
	 * CHART READER XML -- TO -- JAVA OBJECT
	 * @throws TranslationException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void readFromAnnotationAdminFeaturesElement(Element al, AnalyteDocument doc) throws TranslationException 
	{
		Element features = al.getChild("features");
		if(features!=null)
		{
			Element feature = features.getChild("feature");
			if(feature!=null)
			{
				/*
				 * If the class is NOT tied to ANY attributes, it will have a value() which is equal to the span text.
				 * If the class DOES have attribute links, it will NOT have a value() but will instead have one or more attribute refs.
				 */
				Element elements = feature.getChild("elements");
				if(elements!=null)
				{
					List<Element> ells = elements.getChildren("element");
					for(Element el: ells)
					{
						Element val = el.getChild("value");
						String value = "";
						if(val!=null)
						{
							value = val.getValue();
						}
						Element schemaRef = el.getChild("schemaRef");

						if(schemaRef!=null && !(schemaRef.getAttribute("id")==null) && !(schemaRef.getAttribute("uri").getValue().equals("")))
						{
							// Second case (above)
							if(schemaRef.getAttributeValue("uri").contains("attributeDef:"))
							{
								Annotation annot = doc.getAnnotationsByAnnotAdminID().get(value);
								if(annot!=null && annot instanceof AnnotationAttribute)
								{
									annotationAdminAttributes.add((AnnotationAttribute) annot);
								}
								else
								{
									throw new TranslationException("Cannot find annotation ID "+value+" when processing classDef "+annotAdminSchemaTextName);
								}
							}
							else
							{
								throw new TranslationException("Unsupported Field: Schema ref found in classDef element, but was not an attribute!");
							}
						}
						else
						{
							// First case (above)
							classSimpleValueWhenNoAttributeDefsFound = value;
						}
					}
				}
			}
		}
	}
	public String getClassSimpleValueWhenNoAttributeDefsFound() {
		return classSimpleValueWhenNoAttributeDefsFound;
	}

	public void setGeneratedEHostClassMentionElement(Element cme) {
		generatedEHostClassMentionElement = cme;
	}
	
	public Element getGeneratedEHostClassMentionElement()
	{
		return generatedEHostClassMentionElement;
	}
}

package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.ClassRelDef;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

public class AnnotationComplexSlot extends Annotation {
	private ArrayList<Annotation> referencedAnnotations = new ArrayList<Annotation>();
	/*
	 * I don't know whether or not these can have multiple levels .... so ....
	 */
	private ArrayList<ClassRelDef> referencedComplexSlots = new ArrayList<ClassRelDef>();

	public ArrayList<Annotation> getReferencedAnnotations() {
		return referencedAnnotations;
	}
	public void setReferencedAnnotations(ArrayList<Annotation> referencedAnnotations) {
		this.referencedAnnotations = referencedAnnotations;
	}
	@Override
	public int getAnnotationAdminTypeId() {
		return 3;
	}
	@SuppressWarnings("unchecked")
	public void populateFromEHostXMLElement(Element cx,	HashMap<String, Annotation> annotationsByMentionID) {
		List<Element> annotationLinkElements = cx.getChildren("complexSlotMentionValue");
		for(Element lk: annotationLinkElements)
		{
			if(lk.getAttribute("value")!=null)
			{
				String linkVal = lk.getAttribute("value").getValue();
				Annotation linkedAnnotation = annotationsByMentionID.get(linkVal);
				if(linkedAnnotation!=null)
				{
					getReferencedAnnotations().add(linkedAnnotation);
					getClassRefs().addAll(linkedAnnotation.getClassRefs());
				}
				else
				{
					
				}
			}
		}

		List<Element> caseRefElements = cx.getChildren("mentionSlot");
		for(Element caseRefEl: caseRefElements)
		{
			if(caseRefEl.getAttribute("id")!=null)
			{
				String linkVal = caseRefEl.getAttribute("id").getValue();
				ClassRelDef slot = EHostToAnnotAdminTranslator.schema.getClassRelDefsByName().get(linkVal);
				if(slot!=null)
				{
					referencedComplexSlots.add(slot);
				}
				else
				{

				}
			}
		}
	}
	@Override
	public Element getAnnotationAdminFeaturesElement() {

		ArrayList<Element> children = new ArrayList<Element>();

		for(ClassRelDef cslot: referencedComplexSlots)
		{

			Element el = new Element("feature");
			Attribute tempId = new Attribute("tempid", Annotation.getNextTempID().toString());
			Attribute type = new Attribute("type", "3");
			el.setAttribute(tempId);
			el.setAttribute(type);
			Element schemaRef = cslot.getSchemaRefElementForAnnotationAdminXML();
			Element name = new Element("name");
			el.addContent(name);
			el.addContent(schemaRef);
			name.setText(cslot.getName());

			Element featureElements = new Element("elements");

			for(Annotation annot: referencedAnnotations)
			{			
				for(AnnotationClassReference cref: annot.getClassRefs())
				{
					Element featureElement = new Element("element");
					featureElement.setAttribute("tempid", Annotation.getNextTempID().toString());
					Element childSchemaRef = cref.getSchemaRefElementForAnnotationAdminXML();
					Element valueElement = new Element("value");
					valueElement.setText(annot.getEHostTempId().toString());
					featureElement.addContent(valueElement);
					featureElement.addContent(childSchemaRef);
					featureElements.addContent(featureElement);

				}
			}
			el.addContent(featureElements);
			children.add(el);
		}		

		Element featuresElement = new Element("features");
		featuresElement.addContent(children);

		return featuresElement;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void readFromAnnotationAdminFeaturesElement(Element al, AnalyteDocument doc) throws TranslationException {
		Element features = al.getChild("features");
		if(features!=null)
		{
			Element feature = features.getChild("feature");
			if(feature!=null)
			{
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
						if(schemaRef!=null)
						{
							// Second case (above)
							if(schemaRef.getValue().equalsIgnoreCase("classDef"))
							{
								Annotation annot = doc.getAnnotationsByAnnotAdminID().get(value);
								if(annot!=null && annot instanceof AnnotationClass)
								{
									referencedAnnotations.add(annot);
								}
								else
								{
									throw new TranslationException("Cannot find annotation ID "+value+" when processing classRelDef "+annotAdminSchemaTextName);
								}
							}
							else
							{
								throw new TranslationException("Unsupported Field: Schema ref found in classRelDef element, but was not a class!");
							}
						}
						else
						{
							throw new TranslationException("Error processing ClassRelDef: Element found but has no schemaRef when processing classRelDef "+annotAdminSchemaTextName);
						}
					}
				}
			}
		}
/*
 *  <features>
        <feature tempid="53" type="3"> <!-- classRel -->
          <name>HealthCareProviderName_relationship</name>
          <schemaRef tempid="54" uri="schema:1;classRelDef:25">classRelDef</schemaRef>
          <elements>
          <element tempid="55">
            <value>30</value><!-- annotationId -->
            <schemaRef tempid="56" uri="schema:1;classDef:11">classDef</schemaRef>
          </element>
          <element tempid="57">
            <value>40</value><!-- annotationId -->
            <schemaRef tempid="58" uri="schema:1;classDef:11">classDef</schemaRef>
          </element>
        </elements>
        </feature>
      </features>
 */
		
	}
	

	public Date getCreationDate() {
		Date dt = super.getCreationDate();
		
		for(int i = 0; i<referencedAnnotations.size() && dt == null; i++)
		{
			dt = referencedAnnotations.get(i).getCreationDate();
		}
		
		return dt;
	}
}

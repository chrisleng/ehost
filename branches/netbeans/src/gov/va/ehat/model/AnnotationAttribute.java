package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.AnnotationAdminSchema;
import gov.va.ehat.model.annotationAdminSchema.AttributeDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

public class AnnotationAttribute extends Annotation {
	
	private String value = "";

	private AttributeDef attributeDef = null;
	
	@Override
	public int getAnnotationAdminTypeId() {
		return 1;
	}

	/**
	 * CHART READER XML -- TO -- JAVA OBJECT
	 */
	@Override
	public void readFromAnnotationAdminFeaturesElement(Element al, AnalyteDocument doc) 
	{
		Element featuresElement = al.getChild("features");
		if(featuresElement!=null)
		{
			Element featureElement = featuresElement.getChild("feature");
			if(featureElement!=null)
			{				
				/*
				 * Isn't calling an XML element "element" just a bit silly?
				 */
				Element elementsElement = featureElement.getChild("elements");
				if(elementsElement!=null)
				{
					Element elementElement = elementsElement.getChild("element"); // Nails on a chalkboard here.
					if(elementElement!=null)
					{
						Element valueElement = elementElement.getChild("value");
						if(valueElement!=null)
						{
							value = valueElement.getValue();
						}
					}
				}
			}
		}
	}

	public String getValue() 
	{
		return value;
	}

	/**
	 * JAVA OBJECT -- TO -- CHART READER XML
	 */
	@Override
	public Element getAnnotationAdminElement() {
		Element annotationElement = super.getAnnotationAdminElement();
		
		/*
		 * And finally, delegate to the child classes via the abstract method to get their particular flavor of the "features" element.
		 */
		annotationElement.addContent(getAnnotationAdminFeaturesElement());
		
		return annotationElement;
	}
	
	public Element getAnnotationAdminFeaturesElement() {
		AnnotationAdminSchema schema = EHostToAnnotAdminTranslator.schema;
		Element f1 = new Element("features");
		Element f2 = new Element("feature");
		Element n = new Element("name");
		Element s = new Element("schemaRef");
		Element e1 = new Element("elements");
		ArrayList<Element> e2 = new ArrayList<Element>(); // Multiple possible "element" elements.
		
		AttributeDef def = schema.getAttributeDefsByName().get(annotAdminSchemaTextName);
		if(def!=null)
		{
			Attribute tempId = new Attribute("tempid", Annotation.getNextTempID().toString());
			// TBD - This should be either FEATURE_TYPE_ATTRIBUTE_TEXT, FEATURE_TYPE_ATTRIBUTE_NUMERIC, or FEATURE_TYPE_ATTRIBUTE_OPTION - how can we tell numeric in eHost?
			int type = AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT;
			/*
			 * Check for an option ID in the schema attribute definition.
			 * If it exists, this is an option feature.
			 */
			Integer optId = null;
			if(!def.getOptionDefsByName().isEmpty())
			{
				optId = def.getOptionDefsByName().get(value);
			}
			if(optId!=null)
			{
				type = AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION;
			}
			Attribute typeAttr = new Attribute("type", "" + type);
			f2.setAttribute(tempId);
			f2.setAttribute(typeAttr);
			n.setText(def.getName());
			s.setAttribute("tempid", Annotation.getNextTempID().toString());
			String schemaID = EHostToAnnotAdminTranslator.schema.getId().toString();
			String annotationDefId = def.getAnnotationAdminId().toString();
			String uriValue = "schema:"+schemaID+";attributeDef:"+annotationDefId;
			s.setAttribute("uri", uriValue);
			s.setText("attributeDef");
			/*
			 * I'm assuming attributes can only have one value. And only one optionDef reference.
			 * If multiples, loop here later if needed.
			 */
			Element e21 = new Element("element");
			Element v = new Element("value");
			e21.setAttribute(new Attribute("tempid", Annotation.getNextTempID().toString()));
			e21.addContent(v);
			/*
			 * If this is an option feature, we need another schemaRef element here.
			 */
			if(optId!=null)
			{
				String optionUriValue = "schema:"+schemaID+";attributeDefOptionDef:"+optId;
				Element optSchemaElement = new Element("schemaRef");
				optSchemaElement.setAttribute("tempid", Annotation.getNextTempID().toString());
				optSchemaElement.setAttribute("uri", optionUriValue);
				optSchemaElement.setText("attributeDefOptionDef");
				e21.addContent(optSchemaElement);
			}
			v.setText(value);
			e2.add(e21);
			
			f1.addContent(f2);
			f2.addContent(n);
			f2.addContent(s);
			f2.addContent(e1);
			e1.addContent(e2);
			
		}
		
		return f1;
	}

	@SuppressWarnings("unchecked")
	public void populateFromEHostXMLElement(Element sx,	HashMap<String, KnowtatorAnnotation> annotationsByMentionID) throws TranslationException {
		List<Element> annotationLinkElements = sx.getChildren("stringSlotMentionValue");
		for(Element lk: annotationLinkElements)
		{
			if(lk.getAttribute("value")!=null)
			{
				value = lk.getAttribute("value").getValue();
			}
		}

		Element refElement = sx.getChild("mentionSlot");
			if(refElement!=null && refElement.getAttribute("id")!=null)
			{
				String linkVal = refElement.getAttribute("id").getValue();
				AttributeDef ad = EHostToAnnotAdminTranslator.schema.getAttributeDefsByName().get(linkVal);
				if(ad!=null)
				{
					attributeDef = ad;
					annotAdminSchemaTextName = linkVal;
				}
				else
				{
					throw new TranslationException("Chart Reader attribute '"+linkVal+"' could not be found!");
				}
			}
	}

	public AttributeDef getAttributeDef() {
		return attributeDef;
	}
}

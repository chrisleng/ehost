package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.AttributeDef;
import gov.va.ehat.model.annotationAdminSchema.ClassRelDef;
import gov.va.ehat.model.annotationAdminSchema.DefaultDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;

public class AdminFeature extends AdminObject
{
	protected String name;
	protected DefaultDef schemaRefParentObj;
	protected String value;
	protected List<AdminFeature> features = new ArrayList<AdminFeature>();
	
	protected AdminAnnotation relationshipAdminAnnotation;  // A processing hook for features of type FEATURE_TYPE_CLASSIFICATION_RIGHT only.
	protected String schemaRefObjAnnotationMentionId;
	
	public AdminFeature(String mentionId, int type, Integer tempId, String schemaId)
	{
		super(mentionId, type, tempId, schemaId);
	}
	
	public Element getFeatureSchemaRefElement()
	{
		Element schemaRefElement = new Element("schemaRef");
		schemaRefElement.setAttribute("tempid", getNextTempId().toString());
		String uriStr = "schema:" + getSchemaId();
		if(schemaRefObj != null)
		{
			switch(type)
			{
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT:
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_NUMERIC:
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION:
				uriStr += ";classDef:" + schemaRefParentObj.getAnnotationAdminId();
				break;
			case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_LEFT:
			case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT:
				uriStr += ";classRelDef:" + schemaRefParentObj.getAnnotationAdminId();
				break;
			}
		}
		schemaRefElement.setAttribute("uri", uriStr);
		schemaRefElement.setText(getName());
		return schemaRefElement;
	}
	
	public Element getElementSchemaRefElement()
	{
		Element schemaRefElement = new Element("schemaRef");
		schemaRefElement.setAttribute("tempid", getNextTempId().toString());
		String uriStr = "schema:" + getSchemaId();
		if(schemaRefObj != null)
		{
			switch(type)
			{
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT:
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_NUMERIC:
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION:
				uriStr += ";classDef:" + schemaRefParentObj.getAnnotationAdminId() + ";AttributeDef:" + schemaRefObj.getAnnotationAdminId();
				break;
			case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_LEFT:
			case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT:
				uriStr += ";classRelDef:" + schemaRefParentObj.getAnnotationAdminId() + ";ClassDef:" + schemaRefObj.getAnnotationAdminId();
				break;
			}
		}
		schemaRefElement.setAttribute("uri", uriStr);
		schemaRefElement.setText(getName());
		return schemaRefElement;
	}
	
	public void populateFromEHostXMLElement(Element slotMentionElement,	HashMap<String, AdminObject> annotationObjectsMap) throws TranslationException
	{
		Element mentionSlotElement = slotMentionElement.getChild("mentionSlot");
		if(mentionSlotElement != null && mentionSlotElement.getAttribute("id") != null)
		{
			String schemaObjId = mentionSlotElement.getAttribute("id").getValue();
			setName(schemaObjId);
			
			switch(type)
			{
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT: // stringSlotMention
				AttributeDef attributeDef = EHostToAnnotAdminTranslator.schema.getAttributeDefsByName().get(schemaObjId);
				if(attributeDef != null)
				{
					setSchemaRefObj(attributeDef);
				}
				else
				{
					throw new TranslationException("AttributeDef '"+schemaObjId+"' could not be found!");
				}
				break;
			}
		}

		switch(type)
		{
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_TEXT: // stringSlotMention
			// TBD - Are FEATURE_TYPE_ATTRIBUTE_NUMERIC AND FEATURE_TYPE_ATTRIBUTE_OPTION also found in stringSlotMentions?
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_NUMERIC: // stringSlotMention
			case AnnotationAdminConst.FEATURE_TYPE_ATTRIBUTE_OPTION: // stringSlotMention
				Element stringSlotMentionValueElement = slotMentionElement.getChild("stringSlotMentionValue");
				setValue(stringSlotMentionValueElement.getAttributeValue("value"));
				break;
			case AnnotationAdminConst.FEATURE_TYPE_CLASSIFICATION_RIGHT:  // complexSlotMention
				Element complexSlotMentionValueElement = slotMentionElement.getChild("complexSlotMentionValue");
				String rightAnnotationMentionId = complexSlotMentionValueElement.getAttributeValue("value");
				AdminAnnotation adminAnnotation = (AdminAnnotation)annotationObjectsMap.get(rightAnnotationMentionId);
				setValue("" + adminAnnotation.getTempId());
				
				// The parent annotation does not have its schemaObj filled in yet.  The classMention of that annotation needs to be processed
				// before that object is available.  We save the mentionId of the annotation object whose schemaRefObj will fill in the schemaRefObj
				// of this annotation feature.  After classMentions are processed, we will fix up the feature objects of type FEATURE_TYPE_CLASSIFICATION_RIGHT.
				setSchemaRefObjAnnotationMentionId(rightAnnotationMentionId);
				break;
		}
	}

	public Element getAnnotationAdminElement()
	{
		Element featureElement = new Element("feature");
		featureElement.setAttribute("tempid", getTempId().toString());
		featureElement.setAttribute("type", "" + getType());
	
		Element nameElement = new Element("name");
		nameElement.setText(getName());
		featureElement.addContent(nameElement);
	
		Element featureSchemaRefElement = getFeatureSchemaRefElement();
		featureElement.addContent(featureSchemaRefElement);

		Element elementsElement = new Element("elements");
		
		Element elementElement = new Element("element");
		elementElement.setAttribute("tempid", getNextTempId().toString());
		
		Element valueElement = new Element("value");
		valueElement.setText(getValue());
		elementElement.addContent(valueElement);
		
		Element elementSchemaRefElement = getElementSchemaRefElement();
		elementElement.addContent(elementSchemaRefElement);

		elementsElement.addContent(elementElement);
		featureElement.addContent(elementsElement);
		featureElement.addContent(getFeaturesElement());
		
		return featureElement;
	}
	
	public Element getFeaturesElement()
	{
		Element featuresElement = new Element("features");
		for(AdminFeature feature: features)
		{
			Element featureElement = feature.getAnnotationAdminElement();
			featuresElement.addContent(featureElement);
		}

		return featuresElement;
	}
	
	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<AdminFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<AdminFeature> features) {
		this.features = features;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DefaultDef getSchemaRefParentObj() {
		return schemaRefParentObj;
	}

	public void setSchemaRefParentObj(DefaultDef schemaRefParentObj) {
		this.schemaRefParentObj = schemaRefParentObj;
	}

	public AdminAnnotation getRelationshipAdminAnnotation() {
		return relationshipAdminAnnotation;
	}

	public void setRelationshipAdminAnnotation(
			AdminAnnotation relationshipAdminAnnotation) {
		this.relationshipAdminAnnotation = relationshipAdminAnnotation;
	}

	public String getSchemaRefObjAnnotationMentionId() {
		return schemaRefObjAnnotationMentionId;
	}

	public void setSchemaRefObjAnnotationMentionId(
			String schemaRefObjAnnotationMentionId) {
		this.schemaRefObjAnnotationMentionId = schemaRefObjAnnotationMentionId;
	}

}

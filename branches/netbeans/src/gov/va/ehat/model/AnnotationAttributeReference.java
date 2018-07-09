package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.model.annotationAdminSchema.AttributeDef;

import org.jdom.Element;

public class AnnotationAttributeReference {
	private AttributeDef attributeDefinition = null;
	private String value = "";
	public AttributeDef getAttributeDefinition() {
		return attributeDefinition;
	}
	public void setClassDefinition(AttributeDef attributeDefinition) {
		this.attributeDefinition = attributeDefinition;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Element getSchemaRefElementForAnnotationAdminXML() {
		Element schemaRef = new Element("schemaRef");
		schemaRef.setAttribute("tempid", Annotation.getNextTempID().toString());
		String schemaID = EHostToAnnotAdminTranslator.schema.getId().toString();
		String attributeDefId = getAttributeDefinition().getAnnotationAdminId().toString();
		String uriValue = "schema:"+schemaID+";attributeDef:"+attributeDefId;
		schemaRef.setAttribute("uri", uriValue);
		schemaRef.setText("attributeDef");
		return schemaRef;
	}
}

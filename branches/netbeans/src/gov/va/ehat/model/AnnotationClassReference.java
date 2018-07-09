package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.model.annotationAdminSchema.ClassDef;

import org.jdom.Element;

public class AnnotationClassReference {
	private ClassDef classDefinition = null;
	private String value = "";
	public ClassDef getClassDefinition() {
		return classDefinition;
	}
	public void setClassDefinition(ClassDef classDefinition) {
		this.classDefinition = classDefinition;
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
		String classDefId = getClassDefinition().getAnnotationAdminId().toString();
		String uriValue = "schema:"+schemaID+";classDef:"+classDefId;
		schemaRef.setAttribute("uri", uriValue);
		schemaRef.setText("classDef");
		return schemaRef;
	}
}

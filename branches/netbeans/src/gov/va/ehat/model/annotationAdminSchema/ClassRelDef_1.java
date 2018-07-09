package gov.va.ehat.model.annotationAdminSchema;

import gov.va.ehat.model.AdminObject;
import gov.va.ehat.model.KnowtatorAnnotation;

import java.util.ArrayList;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class ClassRelDef_1 extends DefaultDef {
	
	private ArrayList<ClassDef> classes;

	public ArrayList<ClassDef> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<ClassDef> classes) {
		this.classes = classes;
	}

	/**
	 * This expects a "classRel" element
	 * @param l - classRel element
	 * @param schema - Needs the schema doc with populated classDefs to point to the correct classDef objects inside this relationship / complex slot.
	 * @throws DataConversionException 
	 */
	@SuppressWarnings("unchecked")
	public void populateFromAnnotationAdminSchemaXMLElement(Element l, AnnotationAdminSchema schema) throws DataConversionException {
		if(l.getAttribute("id")!=null)
		{
			annotationAdminId = l.getAttribute("id").getIntValue();
		}
		if(l.getChild("name")!=null)
		{
			name = l.getChild("name").getValue();
		}
		classes = new ArrayList<ClassDef>();
		List<Element> cls = l.getChildren();
		for(Element cl: cls)
		{
			String nm = cl.getName();
			if(nm.toUpperCase().startsWith("CLASSDEF"))
			{
				if(cl.getAttribute("id")!=null)
				{
					ClassDef cdef = schema.getClassDefsByID().get(cl.getAttribute("id").getIntValue());
					if(cdef!=null)
					{
						classes.add(cdef);
					}
				}
			}
		}
	}

	public Element getSchemaRefElementForAnnotationAdminXML() {
		Element schemaRef = new Element("schemaRef");
		schemaRef.setAttribute("tempid", AdminObject.getNextTempId().toString());
		String schemaID = gov.va.ehat.EHostToAnnotAdminTranslator.schema.getId().toString();
		String uriValue = "schema:"+schemaID+";classRelDef:"+annotationAdminId;
		schemaRef.setAttribute("uri", uriValue);
		schemaRef.setText("classRelDef");
		return schemaRef;
	}
}

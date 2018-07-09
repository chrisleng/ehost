package gov.va.ehat.model.annotationAdminSchema;

import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class AnnotationAdminSchema {
	
	private Integer id;
	private String name;
	private String description;

	/*
	 * The same attribute definitions organized by two different methods. 
	 */
	private HashMap<Integer, AttributeDef> attributeDefsByID = new HashMap<Integer, AttributeDef>();
	private HashMap<String, AttributeDef> attributeDefsByName = new HashMap<String, AttributeDef>();
	
	/*
	 * The same class definitions organized by two different methods. 
	 */
	private HashMap<Integer, ClassDef> classDefsByID = new HashMap<Integer, ClassDef>();
	private HashMap<String, ClassDef> classDefsByName = new HashMap<String, ClassDef>();

	/*
	 * Used eHost terminology here; Might be more appropriate to use AA's terminology?
	 */	
	private HashMap<Integer, ClassRelDef> classRelDefsByID = new HashMap<Integer, ClassRelDef>();
	private HashMap<String, ClassRelDef> classRelDefsByName = new HashMap<String, ClassRelDef>();
	
	public HashMap<Integer, ClassDef> getClassDefsByID() {
		return classDefsByID;
	}
	public HashMap<String, ClassDef> getClassDefsByName() {
		return classDefsByName;
	}
	public HashMap<Integer, ClassRelDef> getClassRelDefsByID() {
		return classRelDefsByID;
	}
	public HashMap<String, ClassRelDef> getClassRelDefsByName() {
		return classRelDefsByName;
	}
	public HashMap<Integer, AttributeDef> getAttributeDefsByID() {
		return attributeDefsByID;
	}
	public HashMap<String, AttributeDef> getAttributeDefsByName() {
		return attributeDefsByName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@SuppressWarnings("unchecked")
	public void populateFromAnnotationAdminSchemaXMLElement(Element el) throws DataConversionException {
		Attribute att = el.getAttribute("id");
		if(att!=null)
		{
			try {
				id = att.getIntValue();
				if(id==null)
				{
					id = 0;
				}
			} catch (DataConversionException e) {
				e.printStackTrace();
				id = 0;
			}
		}
		Element nel = el.getChild("name");
		if(nel!=null && nel.getValue()!=null)
		{
			name = nel.getValue();
		}
		Element del = el.getChild("description");
		if(del!=null && del.getValue()!=null)
		{
			description = del.getValue();
		}
		
		Element al = el.getChild("attributeDefs");
		if(al!=null)
		{
			List<Element> als = al.getChildren("attributeDef");
			attributeDefsByName = new HashMap<String, AttributeDef>();
			attributeDefsByID = new HashMap<Integer, AttributeDef>();
			for(Element l: als)
			{
				AttributeDef def = new AttributeDef();
				def.populateFromAnnotationAdminSchemaXMLElement(l);
				attributeDefsByName.put(def.getName(), def);
				attributeDefsByID.put(def.getAnnotationAdminId(), def);
			}
		}
		
		Element cdl = el.getChild("classDefs");
		if(cdl!=null)
		{
			List<Element> cdls = cdl.getChildren("classDef");		
			classDefsByName = new HashMap<String, ClassDef>();
			classDefsByID = new HashMap<Integer, ClassDef>();
			addClassDefs(cdls);
		}
		
		/*
		 * NOTE: The order of these code blocks is important. This code snippet will reference the classDefs by their ID value so the class objects themselves can be contained in the relationships (complex slots) established in this snippet.
		 */
		Element crl = el.getChild("classRels");
		if(crl!=null)
		{
			List<Element> crls = crl.getChildren("classRel");
			classRelDefsByID.clear();
			classRelDefsByName.clear();
			for(Element l: crls)
			{
				ClassRelDef slot = new ClassRelDef();
				slot.populateFromAnnotationAdminSchemaXMLElement(l, this);
				classRelDefsByID.put(slot.getAnnotationAdminId(), slot);
				classRelDefsByName.put(slot.getName(), slot);
			}
		}
	}
	
	// Recursive to get class defs of class defs - n level hierarchy.
	private void addClassDefs(List<Element> cdls) throws DataConversionException
	{
		for(Element l: cdls)
		{
			ClassDef def = new ClassDef();
			def.populateFromAnnotationAdminSchemaXMLElement(l);
			classDefsByName.put(def.getName(), def);
			classDefsByID.put(def.getAnnotationAdminId(), def);
			
			Element cdl = l.getChild("classDefs");
			if(cdl!=null)
			{
				List<Element> tcdls = cdl.getChildren("classDef");		
				addClassDefs(tcdls);
			}
		}
	}
	
}

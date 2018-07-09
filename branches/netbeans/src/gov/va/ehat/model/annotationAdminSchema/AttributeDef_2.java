package gov.va.ehat.model.annotationAdminSchema;

import java.util.HashMap;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class AttributeDef_2 extends DefaultDef {
	
	private HashMap<Integer, String> optionDefsByID = new HashMap<Integer, String>();
	private HashMap<String, Integer> optionDefsByName = new HashMap<String, Integer>();
	
	public HashMap<Integer, String> getOptionDefsByID() {
		return optionDefsByID;
	}
	public HashMap<String, Integer> getOptionDefsByName() {
		return optionDefsByName;
	}
	/**
	 * This will populate itself from an attributeDef element.
	 * @param l
	 * @throws DataConversionException 
	 */
	@SuppressWarnings("unchecked")
	public void populateFromAnnotationAdminSchemaXMLElement(Element l) throws DataConversionException {
		Element n = l.getChild("name");
		if(n!=null)
		{
			name = n.getValue();			
		}
		Attribute id = l.getAttribute("id");
		if(id!=null)
		{
			annotationAdminId = id.getIntValue();
		}
		Element opts = l.getChild("attributeDefOptionDefs");
		if(opts!=null)
		{
			List<Element> oels = opts.getChildren("attributeDefOptionDef");
			for(Element o: oels)
			{
				Attribute oid = o.getAttribute("id");
				if(oid!=null)
				{
					String val = o.getValue();
					optionDefsByID.put(oid.getIntValue(), val);
					optionDefsByName.put(val, oid.getIntValue());
				}
			}
		}
	}
	/*
	 * Sample from Chart Reader getSchema() service response
      <attributeDef id='2'>
        <name>annotation_difficulty</name>
        <attributeDefOptionDefs>
          <attributeDefOptionDef id='3'>a_hard_one</attributeDefOptionDef>
        </attributeDefOptionDefs>
      </attributeDef>
	 */
}

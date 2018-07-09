package gov.va.ehat.model.annotationAdminSchema;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class ClassDef_1 extends DefaultDef {
	public void populateFromAnnotationAdminSchemaXMLElement(Element l) throws DataConversionException {
		annotationAdminId = l.getAttribute("id").getIntValue();
		name = l.getChild("name").getValue();
	}
}

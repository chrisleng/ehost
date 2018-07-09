package resultEditor.simpleSchema;

import javax.swing.Icon;
import relationship.simple.dataTypes.AttributeSchemaDef;

/**
 *
 * @author leng
 */
public class AttributeValueObj{


    private String attributevalue;
    private Icon icon;
    private boolean isPublicAttribute = false;
    
    protected AttributeSchemaDef attributeshcema;

    public AttributeValueObj(){
    }

    public AttributeValueObj(String attributevalue, Icon icon, boolean isPublicAttribute, AttributeSchemaDef attributeshcema) {
        this.attributevalue = attributevalue;
        this.icon = icon;
        this.isPublicAttribute = isPublicAttribute;
        this.attributeshcema = attributeshcema;


    }
    
    

    public String getAttributeValue() {
        return attributevalue;
    }



    public void setPublic(boolean isPublicAttribute){
        this.isPublicAttribute = isPublicAttribute;
    }

    public boolean isPublicAttribute(){
        return this.isPublicAttribute;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setAttributeValue(String attributevalue) {
        this.attributevalue = attributevalue;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}


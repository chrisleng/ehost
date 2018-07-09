
package resultEditor.simpleSchema;

import javax.swing.Icon;
import relationship.simple.dataTypes.AttributeSchemaDef;

/**
 *
 * @author leng
 */
public class AttributeListObj{

    private AttributeSchemaDef attribute;
    private String attributename;
    private Icon icon;
    private boolean isPublicAttribute = false;

    public AttributeListObj(){
    }

    public AttributeListObj(String attributename, AttributeSchemaDef attribute, Icon icon, boolean isPublicAttribute) {
        this.attributename = attributename;
        this.icon = icon;
        this.isPublicAttribute = isPublicAttribute;
        this.attribute = attribute;

    }
    
    
    /**Indicator: is this attribute designed for UMLS codes? */
    public boolean isCUICodeNLabel(){
        return this.attribute.isCUICodeNLabel;
    }
    
    boolean isCUICode() {
        return this.attribute.isCUICode;
    }

    boolean isCUILabel() {
        return this.attribute.isCUILabel;
    }
    
    
    public void setUMLSLinkOptions(boolean isCUICode, boolean isCUILabel){
        
        // reset values
        this.attribute.isCUICodeNLabel = false;            
        this.attribute.isCUICode = false;
        this.attribute.isCUILabel = false;
            
        // if two of them are both selected, that mean the attribute is an UMLS attribue
        // and both the CUICode and CUI label will be saved into this label 
        // while user selected an item on UMLS looking up dialog.
        if( isCUICode && isCUILabel ){ 
            this.attribute.isCUICodeNLabel = true;
        }else{ 
            this.attribute.isCUICodeNLabel = false;
                                    
            if(isCUICode)
                this.attribute.isCUICode = true;
            else if(isCUILabel)
                this.attribute.isCUILabel = true;
        }
        
        
        
        
    }

    public String getAttributeName() {
        return attributename;
    }

    public AttributeSchemaDef getAttribute(){
        return attribute;
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

    public void setAttributeName(String attributename) {
        this.attributename = attributename;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;

    
}


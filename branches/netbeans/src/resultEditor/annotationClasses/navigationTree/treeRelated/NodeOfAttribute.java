
package resultEditor.annotationClasses.navigationTree.treeRelated;


import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.navigationTree.dataCache.Att;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class NodeOfAttribute extends DefaultMutableTreeNode {
    public String displayText;
    

    //boolean selected;
    //Color c;
    //int amountofAnnotations, amountofAnnotationTypes;
    //boolean isSelected_toCreateAnnotation = false;
    public int count;
    public boolean isSelected = false;
    public boolean isPublic = false;
    protected Att att;
    
    public String attributeName(){
        return att.attributeName;
    }

    //public NodeOfAttribute(String name, int count) {
    //    super();
    //    this.name = name;
    //    this.count = count;
    //}

    public NodeOfAttribute(String name, Att get) {
        super();
        this.displayText = name;
        //this.count = count;
        this.att = get;
    }

    //public Color getClassColor(){
    //    return c;
    //}

    //public void setFlag_isSelected_toCreateAnnotation( boolean flag ){
    //    isSelected_toCreateAnnotation = flag;
    //}

    //public boolean isSelected_toCreateAnnotation(){
    //    return isSelected_toCreateAnnotation;
    //}

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean newValue) {
        isSelected = newValue;
    }
    
    

    //public int getAnnotationAmount(){
    //    return this.amountofAnnotations;
    //}

    //public int getAnnotationTypeAmount(){
    //    return this.amountofAnnotationTypes;
    //}

    @Override
    public String toString() {
        return this.displayText;
    }

    public void setText(String newValue) {
        this.displayText = newValue;
    }

    /**It's totally same as the getText() method in class of "JAttributeCheckBox"*/
    public String getDisplayText() {
        if(att == null)
             return null;
         if( att.isPublic ){
             if( DepotOfAttributes.isFilterOn() )
                 return "<html><font color=green>" + this.att.attributeName + "</font><font color=gray> (public) ["+ att.count +"]</font></html>";
             else
                 return "<html><font color=gray>" + this.att.attributeName + " (public) ["+ att.count +"]</font></html>";
         }else{
             if( DepotOfAttributes.isFilterOn() )
                 return  "<html><font color=gray>" + this.att.attributeName + " ["+ att.count +"]</font></html>" ;
             else
                 return  "<html><font color=blue>" + this.att.attributeName + " ["+ att.count +"]</font></html>" ;
         }
    }
}


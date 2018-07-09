package resultEditor.annotationClasses.navigationTree.treeRelated;


import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author  Jianwei Chris leng
 * @since   July 17, 2012
 */
 /**tree view node for annotated class*/
public class NodeOfAttValue extends DefaultMutableTreeNode {
    
    public String attvalue;

    
    /**flag that used to indicate whether the checkbox has been selected or not.*/
    public boolean selected;
    
    public int attvaluecount = 0;
    
    /**set the flag to indicate whether the checkbox has been selected or not.*/
    public void setSelectionStatus(boolean isSelected){
        this.selected = isSelected;        
    }
    
    /**get the flag to tell us whether the checkbox has been selected or not.*/
    public boolean getSelectionStatus(){
        return this.selected;
    }
    
    
    /**The name of the attribute of this value.*/
    public String attributeName;

    
    
            
    public NodeOfAttValue(String attname, String attvalue, int attvaluecount) {
        super();
        this.attributeName = attname;
        this.attvalue = attvalue;   
        this.attvaluecount = attvaluecount;
    }

    public NodeOfAttValue() {
        super();        
    }
  
     public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }
    
    

    public String getText() {
        if( DepotOfAttributes.isFilterOn() )
            return  "<html><font color=black>" + attvalue  + "</font> <font color=gray>("+ attvaluecount +")</font></html>" ;
        else
            return  "<html><font color=gray>" + attvalue  + "</font> <font color=gray>("+ attvaluecount +")</font></html>" ;
    }  
    
    public void setText(String text){
        this.attvalue = text;
    }

    @Override
    public String toString() {
        return getText();
    }

}


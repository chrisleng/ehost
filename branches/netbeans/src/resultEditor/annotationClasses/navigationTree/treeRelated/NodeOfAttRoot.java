/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;


import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author  Jianwei Chris leng
 * @since   July 17, 2012
 */
 /**tree view node for annotated class*/
public class NodeOfAttRoot extends DefaultMutableTreeNode {
    
    public String text;
    
    public int count = 0;

    public String classname = null;
    
    /**flag that used to indicate whether the checkbox has been selected or not.*/
    public boolean selected = false;
    
    /**set the flag to indicate whether the checkbox has been selected or not.*/
   
    
    /**get the flag to tell us whether the checkbox has been selected or not.*/
    
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setClassname(String classname){
        this.classname = classname;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }

    
    public NodeOfAttRoot(int count, String classname) {
        super();
        this.count = count;     
        this.classname = classname;
    }

    public NodeOfAttRoot() {
        super();        
    }
  
    
    public void setCount(int count){
        this.count = count;
    }

    

    public String getText() {
        if(DepotOfAttributes.isFilterOn()){
            if ( this.classname == null )
                return "<html><font color=green>"+ count +"</font><font color=black> Public Attributes:</font></html>";
            else
                return "<html><font color=green>"+ count +"</font><font color=black> Attributes of \""+ this.classname +"\":</font></html>";
            }
            
        else{
            if ( this.classname == null )
                return "<html><font color=gray>"+ count +" Public Attributes:</font></html>";
            else{
                return "<html><font color=gray>"+ count +" Attributes of \""+ this.classname +"\":</font></html>";
            }
        }
    }  
    
    public void setText(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return getText();
    }

}


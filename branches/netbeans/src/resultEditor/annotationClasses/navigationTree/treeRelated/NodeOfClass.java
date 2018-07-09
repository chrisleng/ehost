/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class NodeOfClass extends DefaultMutableTreeNode {
    public String classname;
    public String displayText;
    public boolean selected;
    public Color c;
    public int amountofAnnotations, amountofAnnotationTypes;
    boolean isSelected_toCreateAnnotation = false;
    public String shortcomment = null;
    public String des = null;

    public NodeOfClass(String classname, boolean selected, Color c,
            int amountofAnnotations, int amountofAnnotationTypes, 
            String shortcomment, String des) {
        super();
        this.classname = classname;
        this.selected = selected;
        this.c = c;
        this.amountofAnnotations =  amountofAnnotations;
        this.amountofAnnotationTypes = amountofAnnotationTypes;
        this.shortcomment = shortcomment;
        this.des = des;
        
        //if(( DepotOfAttributes.isFilterOn() )&&(DepotOfAttributes.getClassname()!=null)
        //        &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
        //        &&(DepotOfAttributes.getClassname().compareTo( this.classname )!=0)){
        //    this.selected = true;            
        //}
    }
    
    public String getDes(){
        return des;
    }
    
    public String getShortComment(){
        return this.shortcomment;
    }

    public Color getClassColor(){
        return c;
    }

    public void setFlag_isSelected_toCreateAnnotation( boolean flag ){
        isSelected_toCreateAnnotation = flag;
    }

    public boolean isSelected_toCreateAnnotation(){
        return isSelected_toCreateAnnotation;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean newValue) {
        this.selected = newValue;
    }

    /**This method need to be as same as the method in class of "JClassCheckBox.java" */
    public String getDisplayText(){
        if ( DepotOfAttributes.isFilterOn() ){
            if( (DepotOfAttributes.getClassname()!=null)
                &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
                &&(DepotOfAttributes.getClassname().compareTo( this.classname )!=0)){
                
                // only one class is selected while using attribute filter for a 
                // specific class, all others classes will be set as unselected.
                this.setSelected( false );
            }else{
                // only one class is selected while using attribute filter for a 
                // specific class.
                this.setSelected( true ); 
            }
        }
        
        String listing_shortcomment = "";
        if(( shortcomment != null ) && (shortcomment.trim().length()>0))
            listing_shortcomment = " <font color=gray>(" + shortcomment.trim() + ") </font>";
        
            
        
        if(( DepotOfAttributes.isFilterOn() )&&(DepotOfAttributes.getClassname()!=null)
                &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
                &&(DepotOfAttributes.getClassname().compareTo( this.classname )!=0)){
           
            
            
            if ( this.amountofAnnotationTypes > 0 ){
                String text = this.classname;
                text = "<html><font color=d1d1d1>" + text + listing_shortcomment;
                text = text + " [<b>"
                    + this.amountofAnnotationTypes
                    + "/"
                    + this.amountofAnnotations
                    + "</b></font>]</html>";
                this.displayText = text;
                return text;
            }else{
                return "<Html><font color=d1d1d1>"+this.classname + listing_shortcomment +"</font></html>";
            }
            
              
            
        }else{
            
            
            
            
            // set text
            if ( this.amountofAnnotationTypes > 0 ){
                String text = this.classname;
                text = "<html>" + text + listing_shortcomment;
                text = text + " [<b><font color=blue>"
                    + this.amountofAnnotationTypes
                    + "/"
                    + this.amountofAnnotations
                    + "</b></font>]</html>";
                this.displayText = text;
                return text;
            }else{
                return "<html>" + this.classname + listing_shortcomment + "</html>"; 
            }
        }
    }

    public String getText() {
        return this.classname;
    }

    public int getAnnotationAmount(){
        return this.amountofAnnotations;
    }

    public int getAnnotationTypeAmount(){
        return this.amountofAnnotationTypes;
    }

    @Override
    public String toString() {
        return this.classname;
    }

    public void setText(String newValue) {
        this.classname = newValue;
    }
}

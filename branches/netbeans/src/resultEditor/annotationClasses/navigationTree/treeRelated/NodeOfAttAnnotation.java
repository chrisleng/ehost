

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;


import java.awt.Color;
import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotations.Annotation;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class NodeOfAttAnnotation extends DefaultMutableTreeNode {
    //String name;
    //boolean selected;
    //Color c;
    //int amountofAnnotations, amountofAnnotationTypes;
    //boolean isSelected_toCreateAnnotation = false;
    Annotation annotation;
    boolean selected = false;

    public NodeOfAttAnnotation( Annotation annotation ) {
        super();
        this.annotation = annotation;
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
        return selected;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }

    public String getDisplayText(){
        /*if ( this.text.contains("<html>"))
            return this.text;

        if ( this.amountofAnnotationTypes > 0 ){
            String displaytext = this.text;
            displaytext = "<html>" + displaytext;
            displaytext = displaytext + " [<b><font color=blue>"
                + this.amountofAnnotationTypes
                + "/"
                + this.amountofAnnotations
                + "</b></font>]</html>";
            return displaytext;
        }else{
            return this.text;
        }*/
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        //depot.getAnnotationByUnique(  , unique)
        return "<html>" + annotation.annotationText + "</html>";
    }

    public String getText() {
        return getDisplayText();
    }

    //public int getAnnotationAmount(){
    //    return this.amountofAnnotations;
    //}

    //public int getAnnotationTypeAmount(){
    //    return this.amountofAnnotationTypes;
    //}

    @Override
    public String toString() {
        return getDisplayText();
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
    
    public Annotation getAnnotation(){
        return this.annotation;
    }
}



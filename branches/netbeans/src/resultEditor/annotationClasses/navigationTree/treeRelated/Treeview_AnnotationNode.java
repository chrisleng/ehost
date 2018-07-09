/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.file_annotation;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class Treeview_AnnotationNode extends DefaultMutableTreeNode {
    public String text;
    public boolean selected;
    public Color c;

    public String uniqueAnnotationText;
    public int UniqueAunotationAmount = 0;

    public int amountofAnnotations, amountofAnnotationTypes;
    //public boolean isSelected_toCreateAnnotation = false;
    public boolean isNotAppeared_in_CurrentArticle = false;

    public boolean isHighLighted = false;

    Vector<file_annotation> annotations = new Vector<file_annotation>();

    public Treeview_AnnotationNode( String text ) {
        super();
        annotations.clear();
        this.text = text;
    }

    public Treeview_AnnotationNode( String annotationText, int annotationAmount ) {
        super();
        annotations.clear();
        this.setAnnotationDisplay(annotationText, annotationAmount);
    }

    public void setAnnotationDisplay( String annotationText, int annotationAmount ){
        this.uniqueAnnotationText = annotationText;
        this.UniqueAunotationAmount = annotationAmount;

        String lable =  "<html>"  + annotationText;
        if( annotationAmount > 1 )
            lable = lable  + " <font color=blue>[<b>";
        else lable = lable + " <font color=gray>[<b>";
            lable = lable + annotationAmount + "</b>]</font></html>";

        this.text = lable;
    }

    public String getUniqueAnnotationText(){
        return this.uniqueAnnotationText;
    }

    public int getUniqueAnnotationAmount(){
        return this.UniqueAunotationAmount;
    }

    public Vector<file_annotation> getAnnotations(){
        return annotations;
    }


    public void addAnnotations(Vector<file_annotation> annotations){
        this.annotations = annotations;
    }
    
    public void addAnnotations(file_annotation annotation){
        this.annotations.add(annotation);
    }


    public void setClassColor(Color color){
        this.c = color;
    }

    public Color getClassColor(){
        return c;
    }

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
        return this.text;
    }

    public String getText() {
        return this.text;
    }

    public int getAnnotationAmount(){
        return this.amountofAnnotations;
    }

    public int getAnnotationTypeAmount(){
        return this.amountofAnnotationTypes;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public void setText(String newValue) {
        this.text = newValue;
    }
}

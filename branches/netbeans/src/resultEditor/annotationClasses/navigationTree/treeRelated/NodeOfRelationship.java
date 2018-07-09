/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;


import java.awt.Color;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class NodeOfRelationship extends DefaultMutableTreeNode {
    
    
    
    String name;
    //boolean selected;
    //Color c;
    //int amountofAnnotations, amountofAnnotationTypes;
    //boolean isSelected_toCreateAnnotation = false;
    int count;
    boolean selected = false;

    public NodeOfRelationship(String name, int count) {
        super();
        this.name = name;
        this.count = count;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }

    public String getDisplayText(){
        return "<html>" + name + "<font color=grey> [" + count + "]" + "</font></html>";
    }

    public String getText() {
        return "<html>" + name + "<font color=grey> [" + count + "]" + "</font></html>"; //return this.name;
    }

    //public int getAnnotationAmount(){
    //    return this.amountofAnnotations;
    //}

    //public int getAnnotationTypeAmount(){
    //    return this.amountofAnnotationTypes;
    //}

    @Override
    public String toString() {
        return this.name;
    }

    public void setText(String newValue) {
        this.name = newValue;
    }

    /**return the relationship name of this relationship node. */
    public String getRelName() {
        return this.name;
    }
    
    
}



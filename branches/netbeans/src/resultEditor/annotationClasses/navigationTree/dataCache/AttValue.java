/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

/**
 *
 * @author imed
 */
public class AttValue {
    /**the value of this attribute value*/
    public String value;
    
    public boolean isSelected;
    
    /**number of annotation who has this attribute value in its attribute.*/
    public int count = 0;
    
    public AttValue(String value){
        this.value = value;
    }
}

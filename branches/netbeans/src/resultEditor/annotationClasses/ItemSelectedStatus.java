/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses;

 
/**Structure that used to record the selection status of a class, so we can hide 
 * annotations if its class is unselected, OR let annotations to be visible 
 * on screen of their class is selected.
 *
 * @author LENG
 */
public class ItemSelectedStatus {

    String classname;
    boolean isSelected = true;

    public ItemSelectedStatus() {
    }

    public ItemSelectedStatus(String classname, boolean isSelected) {
        this.classname = classname;
        this.isSelected = isSelected;
    }
}
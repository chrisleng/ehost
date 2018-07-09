/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.simpleSchema;

import javax.swing.Icon;

/**
 *
 * @author leng
 */
public class ClassObj{

    private String classname;
    private Icon icon;

    private boolean inheritPublicAttributes = true;

    public ClassObj(){
    }

    public ClassObj(String classname, Icon icon, boolean inheritPublicAttributes) {
        this.classname = classname;
        this.icon = icon;
        this.inheritPublicAttributes = inheritPublicAttributes;
    }

    public void setInherit(boolean flag){
        this.inheritPublicAttributes = flag;
    }

    public boolean getInherit(){
        return this.inheritPublicAttributes;
    }

    public String getClassName() {
        return classname;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public void setClassName(String classname) {
        this.classname = classname;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}

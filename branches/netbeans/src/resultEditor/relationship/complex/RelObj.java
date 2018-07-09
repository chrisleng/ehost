/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.complex;

import javax.swing.Icon;

/**
 *
 * @author Jianwei leng
 */
public class RelObj {

    private String relationship_name;
    private Icon icon;


    public RelObj(){
    }

    public RelObj(String relationship_name, Icon icon) {
        this.relationship_name = relationship_name;
        this.icon = icon;
    }

    public String getRelName() {
        return relationship_name;
    }

    public String getText(){
        return relationship_name;
    }



    public Icon getIcon() {
        return this.icon;
    }

    public void setRelName(String _relationship_name) {
        this.relationship_name = _relationship_name;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}
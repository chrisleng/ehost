/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import resultEditor.annotationClasses.navigationTree.dataCache.Att;
import javax.swing.JCheckBox;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 * New java swing component extend from JCheckBox; it used to display attribute
 * information on the navigation panel.
 *
 * @author Chris Leng
 */
public class JAttValueCheckBox extends JCheckBox {

    //protected NodeOfAttribute attributeNode;
    private boolean hasFocused = false;
    private Att att;
    public String value;
    public int attvaluecount;
    /**
     * The name of the attribute of this value.
     */
    public String attributeName;
    //protected resultEditor.annotationClasses.
    public boolean isSelected;

    public void setSelectionStatus(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getSelectionStatus() {
        return this.isSelected;
    }

    public void setFocused(boolean isHighlighted) {
        this.hasFocused = isHighlighted;
    }

    public boolean hasFocused() {
        return this.hasFocused;
    }

    public void setAtt(Att att) {
        this.att = att;

    }

    public JAttValueCheckBox(String attributename, String attvalue, int attvaluecount) {
        super();
        this.attributeName = attributename;
        this.value = attvalue;
        this.attvaluecount = attvaluecount;
        super.setText(attvalue);
        this.setIconTextGap(2);
        this.setBackground(Color.white);
    }

    public void setText(String attvalue) {
        super.setText(attvalue);
    }

    public String getText() {

        if (DepotOfAttributes.isFilterOn()) {
            return "<html><font color=black>" + value + "</font> <font color=gray>("+ attvaluecount +")</font></html>";
        } else {
            return "<html><font color=gray>" + value + "</font> <font color=gray>("+ attvaluecount +")</font></html>";
        }
    }
}

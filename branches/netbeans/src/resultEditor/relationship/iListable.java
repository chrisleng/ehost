/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.relationship;

import java.util.Vector;

/**
 * This interface will be used for objects that are going to be listed in the Editor
 * located at ResultEditor.ComplexRelationship.  Currently the Editor only supports
 * two columns, and Will have to be edited for each Object that implements this
 * interface.
 * @author Kyle
 */
public interface iListable {
    /**
     * This method will return true if the given column is represented in this
     * object.
     * @param which - the column to check for existence.
     * @return - true if this object needs a column at value which.
     */
    public boolean hasEntry(int which);
    /**
     * Return true if the given column is modifiable
     * @param which - the column to check for modifying
     * @return - true if the column can be modified, false otherwise.
     */
    public boolean isModifiable(int which);
    /**
     * Return true if the given column needs a combo box, By default the
     * Editor assumes the column will use a text box.
     * @param which - the column to check for needing a combo box.
     * @return - true if the column needs a combo box, false otherwise.
     */
    public boolean needsCombo(int which);
    /**
     * Get the Strings that will be put in the Combo Box as choices, if needsCombo(which)
     * returns false, this will *MUST* return a Vector with only one String.
     * @param which - the column to get the values for
     * @return - the choices for a combo box, or the value for a text field
     */
    public Vector<String> getStrings(int which);
    /**
     * Set the selcted value for a column. Should be called when a user selects a
     * different value in a combo box
     * @param which - the column number whose value will be set
     * @param value - the selected value
     */
    public void setString(int which, String value);
    /**
     * Get the selected item for the column
     * @param which - the column to get the selected value for
     * @return - the currently selected value.
     */
    public String getSelectedItem(int which);
    /**
     * Get the write form for this object.  This is used to bypass the fact that
     * this will be used as an iListable, but we will eventually want to write
     * the modified original object.
     * @return - the write form of this iListable.
     */
    public Object getWriteForm();
    /**
     * See if a this row can be deleted
     * @return - true if the row can be deleted, false otherwise.
     */
    public boolean couldDelete();

}

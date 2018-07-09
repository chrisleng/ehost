/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.relationship;

import java.util.Vector;

/**
 *  This class is for list elements in an Editor table.  Look at the iListable class
 * to see documentation for these methods.
 */
public class List implements iListable{
    /**
     * the info string
     */
    String info;
    /**
     * Constructor for a list object
     * @param s - the string
     */
    public List(String s)
    {
        info = s;
    }
    public void setString(int which, String value)
    {
        if(which == 0)
            info = value;
    }
    public boolean needsCombo(int which)
    {
        if(which == 0)
            return false;
        return false;
    }
    public boolean hasEntry(int which)
    {
        if(which == 0)
            return true;
        return false;
    }
    public boolean isModifiable(int which)
    {
        if(which == 0)
            return true;
        return false;
    }

    public Vector<String> getStrings(int which)
    {
        Vector<String> toReturn = new Vector<String>();
        toReturn.add(info);
        return toReturn;
    }
    public String getSelectedItem(int which)
    {
        return info;
    }
    public Object getWriteForm()
    {
        return info;
    }
    public boolean couldDelete()
    {
        return true;
    }

}

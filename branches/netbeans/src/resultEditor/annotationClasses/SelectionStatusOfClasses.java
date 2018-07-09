/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses;

import java.util.Vector;

/**
 *
 * @author imed
 */
/**Depot that used to record the selection status of all annotation classes, 
 * so we can hide annotations if its class is unselected, OR let annotations  
 * to be visible on screen of their class is selected. 
 * 
 * This is a static class.
 */
public class SelectionStatusOfClasses {

    private static Vector<ItemSelectedStatus> depot = new Vector<ItemSelectedStatus>();

    public static void add(String classname, boolean isSelected) {
        if (isExisted(classname)) {
            return;
        }
        ItemSelectedStatus iss = new ItemSelectedStatus(classname, isSelected);
        depot.add(iss);
    }

    public static void changeSelectedStatus(String classname, boolean isSelected) {
        if (!isExisted(classname)) {
            add(classname, isSelected);
        } else {
            for (ItemSelectedStatus iss : depot) {
                if (iss == null) {
                    continue;
                }
                if (iss.classname.trim().compareTo(classname.trim()) == 0) {
                    iss.isSelected = isSelected;
                }
            }
        }
    }

    /**
     * return these classnames which are not selected by user.
     */
    public static Vector<String> getVisibleClasses() {
        Vector<String> toReturn = new Vector<String>();
        if (depot != null) {
            for (ItemSelectedStatus iss : depot) {
                if ((iss == null) || (iss.classname == null)) {
                    continue;
                }
                if (!iss.isSelected) {
                    toReturn.add(iss.classname.trim());
                }

            }
        }
        return toReturn;

    }

    public static boolean getSelectedStatus(String classname) {
        for (ItemSelectedStatus iss : depot) {
            if (iss == null) {
                continue;
            }
            if (iss.classname.trim().compareTo(classname.trim()) == 0) {
                return iss.isSelected;
            }
        }
        return true;
    }

    private static boolean isExisted(String classname) {
        for (ItemSelectedStatus iss : depot) {
            if (iss == null) {
                continue;
            }
            if (iss.classname.trim().compareTo(classname.trim()) == 0) {
                return true;
            }
        }
        return false;
    }
}
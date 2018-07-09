/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.conflicts;

import resultEditor.annotations.Annotation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

/**
 * This class will pull up a pop up menu to resolve class conflicts
 *
 * @author Kyle & Chris
 */
public class classConflictPopUp extends JPanel
{
    //Member variables

    protected javax.swing.JList theList;
    protected JPopupMenu popup;
    protected userInterface.GUI eHOST_GUI;

    /**
     * Constructor
     * @param tc - the jList that is being used to display the classConflicts
     * @param eHOST_GUI - The Main GUI
     */
    public classConflictPopUp(javax.swing.JList tc, userInterface.GUI eHOST_GUI)
    {
        this.eHOST_GUI = eHOST_GUI;
        theList = tc;
    }

    /**
     * This method will display the pop up menu at the given coordinates.
     * @param x
     * @param y
     */
    public void pop(int x, int y)
    {
        //The popup menu
        popup = new JPopupMenu();

        //Make sure the user selected a class conflict before attempting to create a pop up menu
        if (theList.getSelectedIndex() == -1)
        {
            return;
        }

        //Add all available classes to the choices for the pop up menu.
        JMenuItem item;
        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        for (String string : depot.getAnnotationClasssnamesString())
        {
            //Add the new class and position it.
            popup.add(item = new JMenuItem(string));
            item.setHorizontalTextPosition(JMenuItem.RIGHT);

            //Remember the class name.
            final String theClass = string;

            //Add a listener for each class that will be called if it is selected.
            item.addActionListener(new ActionListener()
            {

                public void actionPerformed(ActionEvent event)
                {
                    //Change all classes in the current class conflict to the selected class.
                    changeAllClasses(theClass);
                }
            });

        }

        //Show the pop up menu at the passed in position.
        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        popup.show(theList, x, y);

    }

    /**
     * Change all classes in the selected class conflict to the class chosen in the
     * pop up menu.
     *
     * @param theClass - the class to change annotations to.
     */
    private void changeAllClasses(String theClass)
    {
        classConflict conflicted = (classConflict) theList.getSelectedValue();
        for (Annotation annotation : conflicted.getInvolved())
        {
            annotation.annotationclass = theClass;

        }
    }
}

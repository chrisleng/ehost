/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.PopUp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Depot;
import resultEditor.annotations.suggestion;
import umls.UMLSBrowser;
import userInterface.GUI.spanedittype;

/**
 * This class will pull up a pop up menu to resolve class conflicts
 *
 * @author Kyle & Chris
 */
public class rightClickOnAnnotPopUp extends JPanel
{
    //Member variables
    protected javax.swing.JComponent theList;
    protected JPopupMenu popup;
    protected userInterface.GUI GUI;
    protected Annotation ann;
    
    private ArrayList<Annotation> selectedAnnotaions = null;

    /**
     * Constructor
     * @param tc - the jList that is being used to display the classConflicts
     * @param eHOST_GUI - The Main GUI
     */
    public rightClickOnAnnotPopUp(javax.swing.JComponent tc, ArrayList<Annotation> selectedAnnotaions, userInterface.GUI eHOST_GUI)
    {
        this.GUI = eHOST_GUI;
        this.selectedAnnotaions = selectedAnnotaions;
        theList = tc;
    }

    /**
     * This method will display the pop up menu at the given coordinates.
     * @param x
     *          
     * @param y
     */
    public void pop(final Annotation toUse, int x, int y) {
        try {
            ann = toUse;
            //The popup menu
            popup = new JPopupMenu();

            JMenuItem item;
            resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
            for (suggestion suggested : toUse.verifierFound) {
                //Add the new class and position it.
                popup.add(item = new JMenuItem(suggested.toString()));
                item.setHorizontalTextPosition(JMenuItem.RIGHT);

                //Remember the class name.
                final suggestion toChangeTo = suggested;

                //Add a listener for each class that will be called if it is selected.
                item.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        //Change the annotation to the suggestion
                        GUI.changeAnnotationToSuggestion(toUse, toChangeTo);
                    }
                });

            }             

            // add menu for UMLS lookup items as we may just right clicked on overlapping annotations 
            popup = addUMLSMenu( popup );            
            
            // add break
            JSeparator seperator = new JSeparator();
            popup.add(seperator);
            
            // add delete menu
            popup = addDeleteMenu( popup );
            
            //Show the pop up menu at the passed in position.
            popup.setLabel("Justification");
            popup.setBorder(new BevelBorder(BevelBorder.RAISED));
            popup.show(theList, x, y);


        } catch (Exception e) {
            
        }
    }
    
    private JPopupMenu addDeleteMenu(JPopupMenu popup) {

        if (selectedAnnotaions != null) {
            // go though all selected annotations, and add their texts for UMLS menu
            for (Annotation annotation : selectedAnnotaions) {
                if (annotation == null) {
                    continue;
                }

                String annotationtext = annotation.getTexts();
                if ((annotationtext == null) || (annotationtext.trim().length() < 1)) {
                    continue;
                }

                //annotationtext = annotationtext.replaceAll("\\.", " ");
                annotationtext = annotationtext.trim();
                //annotationtext = annotationtext.replaceAll("( )+", " ");

                final String text = annotationtext;
                JMenuItem item;
                popup.add(item = new JMenuItem("Delete \"" + text + "\" (" + annotation.annotationclass+ ")"));

                item.setHorizontalTextPosition(JMenuItem.RIGHT);

                final Annotation annotation_final = annotation;
                //Add a listener for each class that will be called if it is selected.
                item.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        //Delete the Annotation
                        Depot depot = new Depot();
                        depot.deleteAnnotation( annotation_final );
                        //depot.deleteAnnotation(toUse);
                        // eHOST_GUI.spanEdit(s panedittype.delete);
                        GUI.setModified();
                        GUI.refresh();
                    }
                });
            }
            
        }
        
        return popup;
    }
    
    
    private JPopupMenu addUMLSMenu(JPopupMenu popup) {
        
        if (selectedAnnotaions != null) {
            // go though all selected annotations, and add their texts for UMLS menu
            for (final Annotation annotation : selectedAnnotaions) {
                if (annotation == null) {
                    continue;
                }
                
                String annotationtext = annotation.getTexts();
                if( ( annotationtext == null ) || (annotationtext.trim().length()<1) )
                    continue;
                
                String annotationtext2 = annotationtext.replaceAll("\\.", " ");
                annotationtext2 = annotationtext2.trim();
                annotationtext2 = annotationtext2.replaceAll("( )+", " ");
                        
                final String text = annotationtext;
                final String searchingtext = annotationtext2;

                JMenuItem item;
                
                
                
                final ActionListener menuListener_UMLS = new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    openUMLSBrowser( searchingtext, annotation );
                }
            };

            popup.add(item = new JMenuItem("Search \""+ text + "\" (" + annotation.annotationclass+ ")" +" using UMLS", null));
            item.setHorizontalTextPosition(JMenuItem.RIGHT);
            item.addActionListener(menuListener_UMLS);
            

                
            }
        }
        
        return popup;
    }
            /**Open the UMLS browser to search concepts which may match the selected term(s).*/
    private void openUMLSBrowser(String text, Annotation annotation){
    	//String selectedtext = ann.annotationText;
    	if( ( text == null )||( text.trim().length()<1 ) )
    		return;
    	
        if( this.GUI.umlsbrowser != null ) {
            this.GUI.umlsbrowser.dispose();
        }
        GUI.umlsbrowser = new UMLSBrowser(text, annotation, this.GUI );        
    	GUI.umlsbrowser.setVisible( true );
    }
    
    
    
}

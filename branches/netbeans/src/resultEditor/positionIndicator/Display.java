/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.positionIndicator;

import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author Chris Leng 2010-07-20 12:55pm Division of Epidemiology
 */
public class Display {
    protected userInterface.GUI gui;
    protected JPanel parentPanel;

    public Display(userInterface.GUI gui, JPanel parentPanel){
        this.gui = gui;
        this.parentPanel = parentPanel;

        if ( parentPanel == null )
            return;

        for( Component comp : parentPanel.getComponents()){
            if ( comp instanceof resultEditor.positionIndicator.JPositionIndicator )
                return;
        }

        parentPanel.removeAll();
        JPositionIndicator positionindicator = new JPositionIndicator();
        gui.setPositionIndicator( positionindicator );
        parentPanel.add( positionindicator );
        positionindicator.setVisible(true);
        parentPanel.updateUI();


        
    }



}

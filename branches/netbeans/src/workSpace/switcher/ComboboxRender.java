/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workSpace.switcher;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JList;
import java.io.File;
import javax.swing.ListCellRenderer;

/**
 *
 * @author leng
 */

public class ComboboxRender extends JLabel implements ListCellRenderer
{
    private final int width = 200, height = 40;
    
    public ComboboxRender() {
        setOpaque(true);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
        this.setPreferredSize(new Dimension(width,height));
        
    }

    /*
     * This method finds the image and text corresponding
     * to the selected value and returns the label, set up
     * to display the text and image.
     */
    public Component getListCellRendererComponent(
                                       JList list,
                                       Object value,
                                       int index,
                                       boolean isSelected,
                                       boolean cellHasFocus) {
        //Get the selected index. (The index param isn't
        //always valid, so just use the value.)
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        String text = null;
        if((value!=null)&&(value instanceof File)){
            text = ((File)value).getAbsolutePath();
        }

        if(text!=null)
            text = text.trim();
        
        setText(text);
        this.setToolTipText(text);
        this.setPreferredSize(new Dimension(width,height));
        this.setIconTextGap(1);

        return this;
        /*
        int selectedIndex = ((Integer)value).intValue();

        

        //Set the icon and text.  If icon was null, say so.
        ImageIcon icon = null;//images[selectedIndex];
        String pet = "ss";
        setText(pet);
        setFont(list.getFont());

        if (icon != null) {
            setIcon(icon);
        } else {
            //setUhOhText(pet + " (no image available)",
            //            list.getFont());
        }

        return this;
         * 
         */
    }
    //. . .
}

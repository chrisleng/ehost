/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.structure;

import javax.swing.JLabel;
import java.awt.*;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;


/**
 * A list renderer for us to list files of projects in the list of files of 
 * selected project.
 * 
 * @author C
 */
public class FileRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean hasFocus) {

        JLabel label =
                (JLabel) super.getListCellRendererComponent(list,
                value,
                index,
                isSelected,
                hasFocus);


        label.setBorder(new EmptyBorder(4, 2, 1, 1));

        if (value instanceof FileObj) {
            FileObj item = (FileObj) value;
            String filename = item.getFileName();
            Icon icon = item.getIcon();

            label.setIcon(icon);
            label.setText(filename);

            if (isSelected || item.isSelected) {
                label.setBackground(new Color(63, 132, 230));
            } else {
                label.setBackground(Color.white);
            }


        }
        return label;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.complex;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

/**
**
 *
 * @author Jianwei Leng, 2012 03 20
 */
public class RelRenderer extends DefaultListCellRenderer {

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
        label.setPreferredSize(new Dimension(0,18));
        label.setVerticalTextPosition(CENTER);
        label.setVerticalAlignment(CENTER);

        label.setBorder(new EmptyBorder(1, 1, 1, 1));

        if (value instanceof RelObj) {
            RelObj item = (RelObj) value;            
            Icon icon = item.getIcon();
            String text = item.getText();

            label.setIcon(icon);
            label.setText(text);
            

            if (isSelected || item.isSelected) {                
                label.setBackground(new Color(63, 132, 230));
            } else {                
                label.setBackground(Color.white);
            }

        }
        return label;
    }
}
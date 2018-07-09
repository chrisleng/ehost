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
 *
 * @author leng
 */
public class SpanRenderer extends DefaultListCellRenderer {

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


        label.setBorder(new EmptyBorder(1, 1, 1, 1));

        if (value instanceof SpanObj) {
            SpanObj item = (SpanObj) value;            
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

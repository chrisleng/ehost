/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.simpleSchema;

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
public class ClassRenderer extends DefaultListCellRenderer {

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

        if (value instanceof ClassObj) {
            ClassObj item = (ClassObj) value;
            String classname = item.getClassName();
            Icon icon = item.getIcon();

            label.setIcon(icon);
            label.setText(classname);

            if (index == 0) {
                if(isSelected)
                    label.setForeground(Color.white);
                else
                    label.setForeground(Color.blue);
            } else {
                label.setForeground(Color.BLACK);
            }

            if(index != 0){
                if( !item.getInherit())
                label.setForeground(Color.red);
                else
                    label.setForeground(Color.black);
            }

            if (isSelected || item.isSelected) {
                label.setBackground(new Color(63, 132, 230));
            } else {
                label.setBackground(Color.white);
            }


        }
        return label;
    }
}

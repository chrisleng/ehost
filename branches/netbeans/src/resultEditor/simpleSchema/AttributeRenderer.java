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
public class AttributeRenderer extends DefaultListCellRenderer {

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

        if (value instanceof AttributeListObj) {
            AttributeListObj item = (AttributeListObj) value;
            String attname = item.getAttributeName();
            Icon icon = item.getIcon();

            label.setIcon(icon);
            if( item.getAttribute().hasDefaultValue() )
                label.setText(attname + "  (+) ");
            else
                label.setText(attname);
            
            if( item.getAttribute().isCUICodeNLabel ){
                label.setText( label.getText() + " (linkCUICode&Label) " );
            } else if ( item.getAttribute().isCUICode )
                label.setText( label.getText() + " (linkCUICode) " );
            else if ( item.getAttribute().isCUILabel )
                label.setText( label.getText() + " (linkCUILabel) " );

            if (item.isPublicAttribute()) {
                if(isSelected)
                    label.setForeground(Color.white);
                else
                    label.setForeground(Color.blue);
            } else {
                label.setForeground(Color.BLACK);
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

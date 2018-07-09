package resultEditor.simpleSchema;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;


/**
 *
 * @author leng
 */
public class AttributeValueRenderer extends DefaultListCellRenderer {

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

        if (value instanceof AttributeValueObj) {
            AttributeValueObj item = (AttributeValueObj) value;
            String attributevalue = item.getAttributeValue();
            Icon icon = item.getIcon();

            label.setIcon(icon);
            if( item.attributeshcema.hasDefaultValue( item.getAttributeValue() ) ) 
                label.setText(attributevalue + " (Default)");
            else
                label.setText(attributevalue);

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

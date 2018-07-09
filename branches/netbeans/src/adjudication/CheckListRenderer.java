package adjudication;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

/**
 * This is a list renderer that extended from a SWING JCheckBox; it used to 
 * let user select/unselect annotators and classnames for later process to
 * enter the adjudication mode.
 * 
 * @author  Jianwei Chris Leng
 * @since   JDK 1.6
 * @Date    Oct 10, 2011
 */
public class CheckListRenderer extends JCheckBox implements ListCellRenderer {

    /**Class constructor.*/
    public CheckListRenderer() {
      setBackground( new Color(232,234,229) );
      setForeground(Color.BLACK);
      setBorder(new EmptyBorder(1, 1, 1, 1));
      //setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    }

    /**Implemented method as we implements this class from "ListCellRenderer". */
    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean hasFocus) {
      setEnabled(list.isEnabled());
      setSelected(((CheckableItem) value).isSelected());
      setFont(list.getFont());
      setText(value.toString());
      return this;
    }
  }
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

import java.awt.Color;
import java.awt.Component;
//import java.awt.Cursor;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author leng
 */
public class CheckListRenderer extends JCheckBox implements ListCellRenderer {

    public CheckListRenderer() {
      setBackground( new Color(232,234,229) );
      setForeground(Color.BLACK);
      setBorder(new EmptyBorder(1, 1, 1, 1));
      //setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
    }

    public Component getListCellRendererComponent(JList list, Object value,
        int index, boolean isSelected, boolean hasFocus) {
      setEnabled(list.isEnabled());
      setSelected(((CheckableItem) value).isSelected());
      setFont(list.getFont());
      setText(value.toString());
      return this;
    }
  }
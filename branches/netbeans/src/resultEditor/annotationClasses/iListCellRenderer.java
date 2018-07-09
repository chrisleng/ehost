/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

/**
 *
 * @author Chris
 */

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JList;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class iListCellRenderer extends textPaint implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(50, 50, 128);
  private Color categorycolor;

  public iListCellRenderer() {
    setOpaque(true);
    setIconTextGap(55);
    setPreferredSize(new Dimension(100,24));
    setFont(new Font("Calibri", Font.PLAIN, 13));
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

    iListEntry entry = (iListEntry) value;
    setText( entry.getTitle() );
    setIcon( entry.getImage() );
    setVerticalTextPosition(1);
    categorycolor = entry.getColor();
    setBlockColor( categorycolor );

    if (isSelected) {
      setBackground(HIGHLIGHT_COLOR);
      setForeground(Color.white);
    } else {
      setBackground(Color.white);
      setForeground(Color.black);
    }
    return this;
  }
}



class textPaint extends JLabel {

/**Jianwei Leng 2010-07-08
* @param args
*/
    protected Color color = null;

    public textPaint(){
        super();
    }

    public textPaint(Color color){
        super();
        this.color = color;
    }

    private String text = "CLASS";

    /** rewrote method of paintComponet() */
    @Override
    public void paint(Graphics g){
      super.paint(g);
      Graphics2D g2=(Graphics2D)g;
      // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      // draw color block
      g2.setColor( Color.black );
      g2.drawRect( getX() + 25 , 1, 35 + 4, 20);
      // fill rect
      if (color == null)
        color = Color.CYAN;
      g2.setColor(color);
      g2.fillRect(getX() + 26 , 2, 34 + 4, 19);
      // draw text
      
      
      int r = color.getAlpha();
      int b = color.getBlue();
      int _g = color.getGreen();
      double grayLevel = r * 0.299 + _g * 0.587 + b * 0.114;
      color =  grayLevel > 192 ? Color.BLACK : Color.white;
      g2.setColor(color);
      g2.setFont(new Font("Arial", Font.PLAIN, 11 ));
      g2.drawString(text, getX()+32-5, getHeight()/2 + 4 );
    }

    public void setBlockColor(Color color){
        this.color = color;
    }

}


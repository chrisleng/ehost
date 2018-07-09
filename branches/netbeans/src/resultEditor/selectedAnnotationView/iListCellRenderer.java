/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.selectedAnnotationView;

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


public class iListCellRenderer extends AnnotationEntry implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(50, 50, 128);
  private Color categorycolor;

  private resultEditor.annotations.Annotation annotation;

  public iListCellRenderer() {
    setOpaque(true);
    //setIconTextGap(55);
    //setPreferredSize(new Dimension(0,24));
    setFont(new Font("Calibri", Font.PLAIN, 13));
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

    iListEntry entry = (iListEntry) value;
    //setText( entry.getAnnotation().annotationText );
    super.setAnnotation( entry.getAnnotation() );
    this.annotation = entry.getAnnotation();
    //super.setIconTextGap(4);
    super.setPreferredSize( new Dimension(0, 24 ) );
    super.setVerticalAlignment(TOP);
    //setIcon( entry.getImage() );
    setVerticalTextPosition(1);
    categorycolor = entry.getColor();
    setBlockColor( categorycolor );
    super.setCategory( this.annotation.annotationclass );

    

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



class AnnotationEntry extends JLabel {

/**Jianwei Leng 2010-07-08
* @param args
*/
    protected Color color = null;
    protected String text;
    protected resultEditor.annotations.Annotation annotation;

    
    public void setCategory(String category){
        this.text = category;
    }

    public void setAnnotation(resultEditor.annotations.Annotation annotation){
        this.annotation = annotation;
    }

    public AnnotationEntry(){
        super();
    }

    public AnnotationEntry(Color color){
        super();
        this.color = color;
    }


    /** rewrote method of paintComponet() */
    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            int width = g2.getClipBounds().width - 4;
            int height = 24 - 4; //g2.getClipBounds().height - 4;
            // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // draw color block
            g2.setColor(Color.white);
            g2.drawRect(2, 2, width - 1, height - 1);
            g2.setColor(Color.black);
            g2.drawRect(3, 3, width - 3, height - 3);
            // fill rect
            if (color == null) {
                color = Color.CYAN;
            }
            g2.setColor(color);

            g2.fillRect(4, 4, width - 4, height - 4);

            // draw text
            int r = color.getAlpha();
      int b = color.getBlue();
      int gg = color.getGreen();
        double grayLevel = r * 0.299 + gg * 0.587 + b * 0.114;
            color =  grayLevel > 192 ? Color.BLACK : Color.white;
            g2.setColor(color);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString(annotation.annotationText, 8, 15);
        } catch (Exception ex) {
            System.out.println("1203021619");
        }
    }

    public void setBlockColor(Color color){
        this.color = color;
    }

}


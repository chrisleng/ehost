/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.selectedAnnotationView;

import java.awt.*;
import javax.swing.JLabel;

/**
 *
 * @author imed
 */
public class JAnnotationLabel extends JLabel {

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

    public JAnnotationLabel(){
        super();
        this.setPreferredSize( new Dimension(0, 24 ) );
    }

    public JAnnotationLabel(Color color){
        super();
        this.color = color;
        this.setPreferredSize( new Dimension(0, 24 ) );
    }


    /** rewrote method of paintComponet() */
    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            int width = g2.getClipBounds().width - 4;
            int height = 24 - 4;// g2.getClipBounds().height - 4;
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
            g2.setColor(Color.black); // default font color: black
            if (this.color != null) {
                int rr = color.getRed();
                int gg = color.getGreen();
                int bb = color.getBlue();
                float average = (rr + gg + bb) / 2;
                if (average < 100) // if the class color of this annotation is so dark
                {
                    g2.setColor(Color.WHITE);   // then use while as font color
                }
            }
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

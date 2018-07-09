/*
 * PaperLookPane.java
 *
 * Created on Aug 15, 2011, 14:24:00 PM
 */

package report.iaaReport;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 * This is a customed graphics component which provides a real paper look for
 * us to generate IAA report.
 * 
 *
 * @author Jianwei leng
 * 
 * Created on Aug 15, 2011, 14:24:00 PM
 * 
 */
public class PaperLookPane extends JPanel {

    /**the width or height of the blank space of this panel. This space is 
     * unavilable to children components as we occurred these area as 
     * matte borders.
     */
    public final int topspace = 22;
    public final int leftspace = 22;
    public final int rightspace = 22;
    public final int bottomspace = 12;


    /**constructor*/
    public PaperLookPane(){
        super();
        // use the matteborder to keep a space around the pane and
        // the customed paint() method will use the area to paint out
        // a paper look.
        Border matteBorder = new MatteBorder(topspace, leftspace, bottomspace,
                rightspace, new Color(183,189,196) );
        this.setBorder( matteBorder );
    }

    /**We override the paint() method of JPanel to build a look like a you
     * have a real paper to list information.
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);

        // custom code to build a real paper look

        // pre-define colors
        Color color_topborder = new Color(104,104,104);
        Color color_leftborder = new Color(134,134,134);
        Color color_rightborder = new Color(228,228,228);
        Color color_bottomborder = new Color(175,183,191);

        int width = this.getWidth();
        int height = this.getHeight();

        // draw outside border
        g.setColor(color_topborder);
        g.drawLine( 0, 0, width-1, 0);

        g.setColor(color_leftborder);
        g.drawLine(0, 0, 0, height - 1 );

        g.setColor(color_rightborder);
        g.drawLine(width-1, 1, width-1, height - 1 );

        g.setColor(color_bottomborder);
        g.drawLine(0, height - 1, width-1, height - 1 );


        // pre-define color for the inner borders
        Color color_inner_top = new Color( 148, 153, 159);
        Color color_inner_left = new Color( 127,132,138 );
        Color color_inner_rightAndBottom = new Color( 127, 131, 137 );

        // draw inside border
        // draw inside border: top
        g.setColor(color_inner_top);
        g.drawLine(leftspace-1, topspace-1, width-rightspace-1, topspace-1);
        g.setColor(new Color(170,176,183));
        g.drawLine(leftspace-2, topspace-2, width-rightspace, topspace-2);
        g.setColor(new Color(173,179,187));
        g.drawLine(leftspace-3, topspace-3, width-rightspace+1, topspace-3);
        g.setColor(new Color(177,183,189));
        g.drawLine(leftspace-4, topspace-4, width-rightspace+2, topspace-4);

        // inner line of the left border
        g.setColor(color_inner_left);
        g.drawLine(leftspace-1, topspace-1, leftspace-1, height - bottomspace - 1);
        g.setColor(new Color(158,163,170));
        g.drawLine(leftspace-2, topspace-2, leftspace-2, height - bottomspace );
        g.setColor(new Color(167,173,182));
        g.drawLine(leftspace-3, topspace-3, leftspace-3, height - bottomspace + 1);
        g.setColor(new Color(173,180,188));
        g.drawLine(leftspace-4, topspace-4, leftspace-4, height - bottomspace + 2);

        // inner line of the right border
        g.setColor(color_inner_rightAndBottom);
        g.drawLine(width-rightspace-1, topspace-1, width-rightspace-1, height-bottomspace-1);
        g.setColor(new Color(157,163,170));
        g.drawLine(width-rightspace, topspace-2, width-rightspace, height-bottomspace);
        g.setColor(new Color(169,175,182));
        g.drawLine(width-rightspace+1, topspace-3, width-rightspace+1, height-bottomspace+1);
        g.setColor(new Color(178,184,191));
        g.drawLine(width-rightspace+2, topspace-4, width-rightspace+2, height-bottomspace+2);

        // inner line of the  bottom border
        g.setColor(color_inner_rightAndBottom);
        g.drawLine(leftspace-1, height-bottomspace-1, width-rightspace-1, height-bottomspace-1);
        // draw color chaning effects
        g.setColor(new Color(157,163,170));
        g.drawLine(leftspace-2, height-bottomspace, width-rightspace, height-bottomspace);
        g.setColor(new Color(169,175,182));
        g.drawLine(leftspace-3, height-bottomspace+1, width-rightspace+1, height-bottomspace+1);
        g.setColor(new Color(178,184,191));
        g.drawLine(leftspace-4, height-bottomspace+2, width-rightspace+2, height-bottomspace+2);

        
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.customComponents;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;


/**
 *
 * @author Chris
 */
public class NagivationMenuItem extends JPanel{
    private final int PANELHEIGHT = 40;
    protected boolean isExpaneded = false;

    protected String Title;
    protected Image img;

    protected enum workingstatus { foucsed, selected, normal }
    protected workingstatus currrentWorkingStatus = workingstatus.normal;

    protected boolean isSelected = false;

    public NagivationMenuItem(String title){
        super();

        this.Title = title;
        img = Toolkit.getDefaultToolkit().getImage( getClass().getResource("/ResultEditor/CustomComponents/files2.png") );
    

        this.setfixedSize();
        this.setNormal();

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.addMouseListener( new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                afterMouseEnter();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setMouseReleasedColor();
            }
            /*@Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                afterMousePressed();
            }
            @Override
            public void mouseReleased( java.awt.event.MouseEvent evt ) {
                setMouseReleasedColor();
            }
             * */

        } );

        /*this.addComponentListener( new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                forceupdateUI();
            }
        } );*/
    }

    public void forceupdateUI(){
        if ( this.getGraphics() != null )
            this.print( this.getGraphics() );
        //else
        //    System.out.println("1007270144 this.getGraphics() is null");
    }

    public void setfixedSize(){
        this.setPreferredSize( new Dimension( 0, PANELHEIGHT) );
        this.setMaximumSize(   new Dimension( 0, PANELHEIGHT) );
        this.setMinimumSize(   new Dimension( 0 ,PANELHEIGHT) );
    }


    private void afterMouseEnter(){
        this.currrentWorkingStatus = workingstatus.foucsed;
        this.forceupdateUI();
    }

    public void setSelected(){
        this.isSelected = true;
        this.currrentWorkingStatus = workingstatus.selected;
        this.forceupdateUI();
    }

    public void setActivingColor(){
        
    }

    public void setNormal(){
        this.isSelected = false;
        this.currrentWorkingStatus = workingstatus.normal;
        this.forceupdateUI();
    }

    public void setFocusedColor(){

        this.forceupdateUI();

    }

    public void setMouseReleasedColor(){
        if( this.isSelected ) {
            this.currrentWorkingStatus = workingstatus.selected;
            this.forceupdateUI();
        } else {
            this.currrentWorkingStatus = workingstatus.normal;
            this.forceupdateUI();
        }
    }

    public void afterMousePressed(){
        setFocusedColor();

        if ( this.isExpaneded )
            this.isExpaneded = false;
        else this.isExpaneded = true;

    }

    public void defaultstatus(){
        this.isExpaneded = false;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;

        int width = this.getWidth();
        int height = this.getHeight();

        // background filling
        // GradientPaint paint to fill color in the bar
        Color stage1_startcolor = new Color(227, 239, 255), stage1_endcolor = new Color(173,209, 255);
        if ( currrentWorkingStatus == workingstatus.normal ) {
            stage1_startcolor = new Color(227, 239, 255);
            stage1_endcolor = new Color(173,209, 255);
        } else if ( currrentWorkingStatus == workingstatus.foucsed ) {
            stage1_startcolor = new Color( 255, 252, 222);
            stage1_endcolor = new Color(255,232, 167);
        } else if ( currrentWorkingStatus == workingstatus.selected ) {
            stage1_startcolor = new Color( 255, 214, 164);
            stage1_endcolor = new Color(255,187, 110);
        }
        GradientPaint gp = new GradientPaint(1, 1,  stage1_startcolor, 1, 17, stage1_endcolor, true);     // 创建渐变色对象
        g2d.setPaint(gp);
        g2d.fillRect(1,1, width - 2, 20);
        // stage 2
        Color stage2_startcolor = new Color(173, 209, 255), stage2_endcolor = new Color(192,219, 255);
        if ( currrentWorkingStatus == workingstatus.normal ) {
            stage2_startcolor = new Color(173, 209, 255);
            stage2_endcolor = new Color(192,219, 255);
        }else if( currrentWorkingStatus == workingstatus.foucsed ) {
            stage2_startcolor = new Color( 255, 215, 106);
            stage2_endcolor = new Color(255,230, 158);
        } else if( currrentWorkingStatus == workingstatus.selected ) {
            stage2_startcolor = new Color( 254, 174, 66);
            stage2_endcolor = new Color(254,225, 122);
        }
        GradientPaint gp2 = new GradientPaint(1, 17,  stage2_startcolor, 1, height -2, stage2_endcolor, true);     // 创建渐变色对象
        g2d.setPaint(gp2);
        g2d.fillRect(1,21, width - 2, height - 2);

        // draw out broader
        Color outline = new Color(101,147,207);
        g.setColor(outline);
        g.drawRect(0,0, width-1, height-1);
        
        g2d.setFont( new Font("Arial", Font.BOLD, 15 ) );
        if (!this.isExpaneded)
            g2d.setColor( new Color(22, 57, 107) );
        else
            g2d.setColor( Color.black );
        g2d.drawString(Title, 40, 24);

        // draw icon
        g.drawImage(img, 8, 8, this);

        //System.out.println("font width = " + fontcharwidth);
        // display text title of current panel
        //printVerticalText( "NAVIGATOR", g, (int)(height/2 - fontcharwidth), 10 );


        
    }

    



}

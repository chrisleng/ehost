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
import java.awt.geom.AffineTransform;
import javax.swing.JSplitPane;

/**
 *
 * @author Chris
 */
public class ExpandablePanel extends JPanel{
    private final int PANELWIDTH = 20;
    protected boolean isExpaneded = false;
    protected JSplitPane splitpane;
    protected int containPanelWidth = 260;
    

    public ExpandablePanel(JSplitPane splitpane){
        super();
        this.splitpane = splitpane;

        this.setfixedSize();
        this.setNormalColor();

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
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                afterMousePressed();
            }
            @Override
            public void mouseReleased( java.awt.event.MouseEvent evt ) {
                setMouseReleasedColor();
            }

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
            //System.out.println("1007270144 this.getGraphics() is null");
    }

    public void setfixedSize(){
        this.setPreferredSize( new Dimension( PANELWIDTH,0) );
        this.setMaximumSize(   new Dimension( PANELWIDTH,0) );
        this.setMinimumSize(   new Dimension( PANELWIDTH,0) );
    }


    public void afterMouseEnter(){
        this.setBackground( new Color(255,231,162) );
        this.forceupdateUI();
    }

    public void setActivingColor(){
        //this.setBackground( new Color(255,231,162) );
        this.setBackground(new Color( 213, 228, 242));
        this.forceupdateUI();
    }

    public void setNormalColor(){
        this.setBackground(new Color( 213, 228, 242));
        this.forceupdateUI();
    }

    public void setFocusedColor(){
        this.setBackground(new Color( 241, 140, 60));
        this.forceupdateUI();

    }

    public void setMouseReleasedColor(){
        if( this.isExpaneded )
            this.setActivingColor();
        else
            this.setNormalColor();
    }

    public void afterMousePressed(){
        setFocusedColor();

        if ( this.isExpaneded )
            this.isExpaneded = false;
        else this.isExpaneded = true;

        if( !this.isExpaneded ){
            if ( this.containPanelWidth < PANELWIDTH + 10 )
                this.containPanelWidth = 240;
            else if ( this.containPanelWidth < splitpane.getDividerLocation() )
                this.containPanelWidth = splitpane.getDividerLocation();
            else if ( this.containPanelWidth > splitpane.getDividerLocation() )
                this.containPanelWidth = splitpane.getDividerLocation();


            splitpane.setDividerLocation( this.PANELWIDTH + 2 );
        }else {
            splitpane.setDividerLocation( this.containPanelWidth );
        }
    }

    public void defaultstatus(){
        this.isExpaneded = false;
        if (splitpane !=null)
            splitpane.setDividerLocation( this.PANELWIDTH + 2 );
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        
        int width = this.getWidth();
        int height = this.getHeight();
        if ( this.isExpaneded ) {
            g.setColor( new Color( 241, 140, 60) );
            g.drawRect( 2, 2 , width - 6 , height - 6  );
        }

        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont( new Font("Arial", Font.BOLD, 14 ) );
        int fontcharwidth = g2d.getFontMetrics().stringWidth("NAVIGATOR");
        //System.out.println("font width = " + fontcharwidth);
        // display text title of current panel
        //printVerticalText( "NAVIGATOR", g, (int)(height/2 - fontcharwidth), 10 );
        printVerticalText( "NAVIGATOR", g, -(int)(fontcharwidth+10), 5 );

        
    }


    private void printVerticalText(String text, Graphics g, int x, int y){
        if ( text == null )
            return;
        Graphics2D g2d = (Graphics2D)g;

        AffineTransform ot = g2d.getTransform();
            g2d.setFont( new Font("Arial", Font.BOLD, 14 ) );
            //if(isExpaneded)
                //g2d.setColor(new Color(0,64,128));
                //g2d.setColor(new Color(21,66,139));
            //g2d.setColor(Color.black);
           // else
                g2d.setColor(new Color(21,66,139));;
            AffineTransform at = new AffineTransform();
            //System.out.println(at.toString());
            at.setToRotation( -Math.PI / 2.0 );//, getWidth()/2.0, getHeight()/2.0 );
            

             AffineTransform af = g2d.getTransform();
            af.translate(0.0, 0.0);
            af.concatenate( at );

            g2d.setTransform(af);

        g2d.drawString(text, x, (int)(y + getWidth()/2.0));
        //System.out.println("x= " + x + ", y= "+(int)(y + getHeight()/2.0));
        g2d.setTransform(ot);
    }


}

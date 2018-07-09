/*
 * ExpandablePane_editor.java
 *
 * A custom graphics component button that used on the main GUI of eHOST
 * editor to hide/display the editor/differ panes.
 *
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
import java.util.logging.Level;
import javax.swing.JSplitPane;

/**
 * This is a custom graphics component button that used on the main GUI of eHOST
 * editor to hide/display the editor/differ panes.
 *
 * @author Jianwei Chris Leng
 * 
 */
public class ExpandablePanel_editor extends JPanel{    

    /** the swing component of JSplitPane, which used to give two separated
     * panes between the document viewer and the edit/diff viewer.*/
    protected JSplitPane main_splitpane;
    
    /**the swing component of JPane, which used to contain and manage the 
     * editor pane and the diff pane. */
    protected JPanel diffPanel = null;

    /**Width of this component. It's fixed.*/
    private final int PANELWIDTH = 20;

    
    /**It's a flag for this class to remember whether the panel of
     * annontation editor and differ has been hided or not. If its value is
     * "true", annotation editor area and diff area have been expanded and
     * displayed; if it's "false", that means they is hid. The default status is
     * "hide", value is "fakse";
     */
    protected boolean isExpaneded = false;
    
    protected int containPanelWidth = 400;
    protected JPanel MainPanel;

    /**it's integ*/
    public int width_of_Current_EditorPane = 200;
    public int width_of_Current_DiffPane = 200;

    
    /**before we use this class to display or hide the editor/diff panel
     * on the right side of eHOST main gui, we need to tell this class
     * what is the diffSplitPane.
     *
     * @param   js
     *          The splitpane that used to separate the editor panel and
     *          the diff panel. It can not be null
     */
    public void setDiffSplitPane(JPanel diffPanel) throws Exception{
        
        // the JSplitPane can not be null
        if( diffPanel == null)
            throw new Exception("1109180015::You must define a JPanel to contain and manage the editor pane and diff pane.");
        
        this.diffPanel = diffPanel;
        //diffPanel.setDividerLocation( (Integer)(js.getWidth()/2) );
    }

    public ExpandablePanel_editor(JSplitPane splitpane, JPanel MainPanel){
        super();
        this.main_splitpane = splitpane;
        this.MainPanel = MainPanel;
        // set the size of this custom button
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
        //    System.out.println("1007270144 this.getGraphics() is null");
    }

    /**set the size of this custom button*/
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


    /**While mouse clicked on this button, we need to implement properly
     * 
     */
    public void afterMousePressed(){
        // ##1## reset color of component
        setFocusedColor();

        // ##2## reset indicator flag (give alternative value to the flag "isExpanded")
        if ( this.isExpaneded )
            this.isExpaneded = false;
        else
            this.isExpaneded = true;

        
        
        // ##3## if we need to hide this right part on the splitter
        if( !this.isExpaneded )
        {
            // ##3.1## if current size of right part is TOO Smaller
            // reset the width of containpanel to 300
            if ( this.containPanelWidth < 300 ) {
                this.containPanelWidth = 300;
                log.LoggingToFile.log(Level.INFO, "width1 = "+ this.containPanelWidth);
            } 
            else if ( this.containPanelWidth < ( main_splitpane.getWidth() - main_splitpane.getDividerLocation() ) )
            {
                this.containPanelWidth = main_splitpane.getWidth() - main_splitpane.getDividerLocation();
                log.LoggingToFile.log(Level.INFO, "width2 = "+ this.containPanelWidth);
            } 
            else if ( this.containPanelWidth > ( main_splitpane.getWidth() - main_splitpane.getDividerLocation() ) )
            {
                this.containPanelWidth = main_splitpane.getWidth() - main_splitpane.getDividerLocation();
                log.LoggingToFile.log(Level.INFO, "width3 = "+ this.containPanelWidth);
            }

            // ##3.2## hide the pane by set dividerlocation of split pane:
            // the number after "this.PANELWIDTH" is the width which we left for
            // the button and split pane control bar
            main_splitpane.setDividerLocation(
                    (Integer)(main_splitpane.getWidth()) // total width
                    );    // width of current left pane
        }
        // ##4## if we need to expand right part on the splitter
        else
        {
            
            int width = main_splitpane.getRightComponent().getWidth();
            if(this.diffPanel!=null){
                //diff_splitpane.setDividerLocation(0.5);
                //diff_splitpane.setDividerLocation((Integer)(width/2));
                //splitpane_diff.setDividerLocation(splitpane_diff.getWidth()/2);
            //
            }

            main_splitpane.setDividerLocation( main_splitpane.getWidth() - this.containPanelWidth );
            
            
        }
    }
    
    public boolean isExtended(){
        return this.isExpaneded;
    }
    

    /**Hide the annotation edit and diff area from the right sider on eHOST
     * window, and ask eHOST to remember this status.*/
    public void defaultstatus()
    {
        try{
        this.isExpaneded = false;
        
        // hide the right annotation edit and diff area
        if (main_splitpane !=null)
        {
            main_splitpane.setDividerLocation(
                main_splitpane.getParent().getWidth()   // width of wholesplitter
                - this.main_splitpane.getDividerSize()  // width of divider of splitter
                );
        }}catch(Exception ex){
            System.out.println("error 1206151048");
        }
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
        int fontcharwidth = g2d.getFontMetrics().stringWidth("EDITOR");
        //System.out.println("font width = " + fontcharwidth);
        // display text title of current panel
        //printVerticalText( "NAVIGATOR", g, (int)(height/2 - fontcharwidth), 10 );
        printVerticalText( "EDITOR", g, -(int)(fontcharwidth+10), 5 );

        
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
            g2d.setColor(new Color(21,66,139));
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

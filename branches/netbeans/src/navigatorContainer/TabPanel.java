/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package navigatorContainer;

import eHOSTEvents.EHOSTEventManager;
import eHOSTEvents.NAVListenser;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.event.MouseInputAdapter;

/**
 * This is a graphic component designed to show a tab panel used to build
 * the navigator panel of eHOST.
 * 
 * @author leng - Jan 20, 2011
 *
 */
public class TabPanel extends JPanel{

    private boolean expaneded = false;

    protected userInterface.GUI gui;

    /**size of the matte border to this panel's border*/
    private final int matteTop = 2, matteLeft = 29,
            matteBottom = 4, matteRight = 4;
    private final Color color_tabBackground = new Color(165,165,165);
    private final Color color_selected = new Color(101,101,101);
    private final Color color_MouseOn = new Color(135,135,135);

    private final Color color_outline = new Color(49,49,49);

    private final int minimizeWidth = (matteLeft + matteRight);

    /**width of prvious extended panel.*/
    private int LatestWiderWidth = 0;

    //private String[] tabtexts = {"Projects","Markables"};

    private  int extendedWidth=280;

    private final Font tabtextfont = new Font("Arial", Font.BOLD, 12 );

    /**User to indicate whether we need to show the tab of class. We
     * show the tab of class only while status=1;*/
    private static int status = 0;

    /**
     * @param   status
     *          User to indicate whether we need to show the tab of class. We
     *          show the tab of class only while status=1;
     */
    public TabPanel(userInterface.GUI gui, int status){
        super();

        // load icons from disk
        loadIcons();

        TabPanel.status = status;
        this.gui = gui;

        // tell system that we only have one tab at the begining.
        Tabs.defineOneTab();

        // set the matte border so we can get some place for painting area
        this.setBorder(new MatteBorder(matteTop,
                matteLeft,
                matteBottom,
                matteRight,
                color_tabBackground));

        // setting normal attributes to this tab panel
        panelSetting();

        // add mouse event handler
        addMouseHandling();

        
        
    }

    /**the icon of a project*/
    Image icon_projects, icon_projects_unselected;

    /**load icons from disk*/
    private void loadIcons(){
        try{
            icon_projects = new javax.swing.ImageIcon(
                getClass().getResource("/navigatorContainer/workspace1.png")).getImage();

            icon_projects_unselected = new javax.swing.ImageIcon(
                getClass().getResource("/navigatorContainer/workspace2.png")).getImage();
        }catch(Exception ex){

        }
    }

    /**setting normal attributes to this tab panel*/
    private void panelSetting(){

        this.setBackground(this.color_tabBackground);

        // set mouse sharp
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // set layout type
        this.setLayout(new java.awt.BorderLayout());

        this.setSize( this.minimizeWidth, 200);
        this.setMinimumSize(new Dimension( this.minimizeWidth, 50) );
        this.setPreferredSize(new Dimension( this.minimizeWidth, 50) );

    }

    

    private void printtopbar(Graphics g)
    {
        
        // draw top bar
        Graphics2D g2d = (Graphics2D)g;
        Color startcolor = new Color(62, 62, 62),
              endcolor = new Color(100,100, 100);
        int topbar_height = 12;
        int topbar_width;
        //if(this.expaneded) 
        //    topbar_width = this.getWidth() - 1;
        //else
            topbar_width = this.matteLeft;

        GradientPaint gp = new GradientPaint(0, 0,  startcolor, 0, topbar_height, endcolor, true);
        g2d.setPaint(gp);
        g2d.fillRect(0,0, topbar_width, topbar_height-1);

        // triangle on top gray bar
        painttriangle(g, topbar_height, topbar_width);                

        // bottom black line
        g.setColor(color_outline);
        g.drawLine(0, topbar_height-1, topbar_width-1, topbar_height-1);

        // draw outline
        g.drawLine(0,0,0, this.getHeight()-1);
        g.drawLine(0,0,this.getWidth()-1, 0);
        g.drawLine(this.getWidth()-1, 0,this.getWidth()-1, this.getHeight()-1);
        g.drawLine(0, this.getHeight()-1,this.getWidth()-1, this.getHeight()-1);

        // paint spots bar
        paintSpotsBar(g);

        // paint tab text
        paintTabText(g);
    }

    private void paintTabText(Graphics g){
        
    }

    private void paintSpotsBar(Graphics g){
        g.setColor(new Color(100,100,100));

        int startx=4,starty=17; // count from 0 on axis

        int endx = this.matteLeft - startx;

        for(int x=startx;x<endx;x=x+2){
            g.drawLine(x, starty, x, starty);
            g.drawLine(x, starty+2, x, starty+2);
        }

    }

    private void painttriangle(Graphics g, int topbar_height, int topbar_width){
        
        // first triangle
        g.setColor(new Color(153,153,153));
        int height=5;
        int starty = (topbar_height-5)/2 + 1;
        g.drawLine( 4, starty, 4, starty + height );
        g.drawLine( 5, starty+1, 5, starty + height-1 );        
        g.drawLine( 6, starty+2, 6, starty+2 );

        g.setColor(new Color(201,201,201));
        g.drawLine( 4, starty+1, 4, starty + height-2 );
        g.drawLine( 5, starty+2, 5, starty+2 );


        // second triangle
        g.setColor(new Color(153,153,153));
        g.drawLine( 10, starty, 10, starty + height );
        g.drawLine( 11, starty+1, 11, starty + height-1 );
        g.drawLine( 12, starty+2, 12, starty+2 );

        g.setColor(new Color(201,201,201));
        g.drawLine( 10, starty+1, 10, starty + height-2 );
        g.drawLine( 11, starty+2, 11, starty+2 );
        
    }
    

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        try{
            printtopbar(g);
            // paint tabs
            paintTabs(g, Tabs.tabs);

            // paint rectangle
            paintTabRectangle(g, Tabs.tabs);
            paintTabs(g, Tabs.tabs);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "ERROR 1104201119::" + ex.toString());
        }

    }

    private void paintTabRectangle(Graphics g, Vector<Tab> tabs){
        int size= tabs.size();
        for(int i=0;i<size;i++){
            Tab tab = tabs.get(i);
            if(tab==null)
                continue;

            if(tab.isMouseStoppedOnThisTab){
                // mouse on
                drawRect(g, tab, 2);
            }else{
                // draw rectangle: /**0: unselected; 1: selected; 2 mouse on this tab*/
                if(tab.status==1){
                    // selected
                    drawRect(g, tab, 1);

                }else{
                    // unselected
                    drawRect(g, tab, 0);
                }
            
            }
        }
    }

    private void drawRect(Graphics g, Tab tab, int status){
        //System.out.println("color="+status);
        if(status==1)
            g.setColor(this.color_selected);
        else if(status==2)
            g.setColor(this.color_MouseOn);
        else
            g.setColor(this.color_tabBackground);

        g.fillRect(1, tab.y_rangeStart-1, this.matteLeft-1, tab.y_rangeEnd-tab.y_rangeStart);

        if(status==1){
            g.setColor(Color.BLACK);
            g.drawLine(1, tab.y_rangeStart-1, this.matteLeft-1, tab.y_rangeStart-1);
        }else if(status==2){
            g.setColor(new Color(178,178,178));
            g.drawLine(1, tab.y_rangeStart-1, this.matteLeft-1, tab.y_rangeStart-1);
        }

    }


    
    private void paintTabs(Graphics g, Vector<Tab> tabs){
        
   

        if(tabs==null)
            return;
        if(tabs.size()<1)
            return;

        int i=0;
        for(Tab tab:tabs){
            if(tab==null)   continue;
            if(tab.tabtext==null)   continue;
            if(tab.tabtext.trim().length()<1)   continue;

            if (i==0){
                tab.y_rangeStart = 30;
            }else
                tab.y_rangeStart = tabs.get(i-1).y_rangeEnd;
            
            
            drawVectText(g, tab, i);
            
            i++;
            
        }
    }

    

    private void drawVectText(Graphics g, Tab tab, int i){
        // get text length
        Graphics2D g2d = (Graphics2D)g;
        g2d.setFont( tabtextfont );
        int fontcharheight = g2d.getFontMetrics().stringWidth(tab.tabtext);

        
        int x = 10;
        int y = tab.y_rangeStart + 20+10;
        tab.y_rangeEnd = fontcharheight + tab.y_rangeStart + 60;
        
        //printVerticalText( "NAVIGATOR", g, -(int)(fontcharwidth+10), 5 );
        printVerticalText(tab.tabtext, g, x, y );

        try{
            if(i==0){
                if( tab.status == 1 )
                    g.drawImage( icon_projects, 4, 14 + 20, Color.red, this);
                else
                    g.drawImage( icon_projects_unselected, 4, 14 + 20, Color.red, this);
            }

        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
    }


    private void printVerticalText(String text, Graphics g, int x, int y){
        if ( text == null )
            return;
        Graphics2D g2d = (Graphics2D)g;

        AffineTransform ot = g2d.getTransform();
        g2d.setFont( tabtextfont );
        g2d.setColor(Color.black);

        AffineTransform at = new AffineTransform();        
        at.setToRotation( Math.PI / 2.0 );//, getWidth()/2.0, getHeight()/2.0 );




        AffineTransform af = g2d.getTransform();
        af.translate(x, y);
        af.concatenate( at );

        g2d.setTransform(af);

        g2d.drawString(text, 0, 0);
        //System.out.println("x= " + x + ", y= "+(int)(y + getHeight()/2.0));
        g2d.setTransform(ot);
    }


    

    private int firstx = -1, firsty = -1;
    private int latestx = -1, latesty = -1;

    /**
     * if user clicked on these tab, run respective actions.
     */
    private void addMouseHandling() {
        
        // remove old listener
        MouseListener[] olds = this.getMouseListeners();
        MouseMotionListener[] oldms = this.getMouseMotionListeners();
        if (olds != null) {
            for (MouseListener oldlistener : olds) {
                this.removeMouseListener(oldlistener);
            }
        }
        if (oldms != null) {
            for (MouseMotionListener oldlistener : oldms) {
                this.removeMouseMotionListener(oldlistener);
            }
        }

        
        MouseInputAdapter mia = new MouseInputAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                userMouseClicked(e);
            }
            //@Override
            //public void mouseEntered(MouseEvent e){
            //    userMouseEntered(e);
            //}
        };
        this.addMouseListener(mia);

        
        MouseInputAdapter mp = new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                userPressed(e);
            }
            //@Override
            //public void mouseEntered(MouseEvent e){
            //    userMouseEntered(e);
            //}
        };
        this.addMouseListener(mp);
        
        MouseInputAdapter mr = new MouseInputAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                userMoved(e);
            }
            //@Override
            //public void mouseEntered(MouseEvent e){
            //    userMouseEntered(e);
            //}
        };
        this.addMouseListener(mr);
        
       

                
        MouseMotionAdapter mma = new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                //userMoved(e);
            }
            //@Override
            //public void mouseEntered(MouseEvent e){
            //    userMouseEntered(e);
            //}
        };
        this.addMouseMotionListener(mma);

    }

    /**Move the right border of this panel to the place that the mouse just 
     * released; it calculates the distance that mouse travelled from the 
     * 
     */
    public void userMoved(MouseEvent e) {
        //System.out.println("mousemoved x=" + e.getX() );
        if((firstx == -1)||(firsty == -1))
            return;
        
        int newweight = this.getWidth() + (e.getX()-firstx);
        if( newweight < 280 )
            newweight = 280;
        
        this.extendedWidth = newweight;
        this.setPreferredSize( new Dimension( newweight, this.getHeight() ) );
        
        this.revalidate();
    }
    
    public void userPressed(MouseEvent e) {
        System.out.println( e.getX() + ", " + e.getY() );
        if ((e.getX() > this.getWidth() - 10) && ((e.getX() < this.getWidth())) && (e.getY() > 2) && ((e.getX() < this.getHeight() - 2))) {
            firstx = e.getX();
            firsty = e.getY();
        } else {
            firstx = -1;
            firsty = -1;
        }
    }
        
        
    /*private void userMouseEntered(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        if(Tabs.tabs==null)   return;
        int size=Tabs.tabs.size();

        for(int i=0;i<size;i++){
            Tab tab = Tabs.tabs.get(i);
            if( tab == null)   return;

            if((x<0)||(x>this.matteLeft)){
                Tabs.tabs.get(i).isMouseStoppedOnThisTab = false;
                continue;
            }

            //System.out.println("To range["+tab.y_rangeStart +", "+tab.y_rangeEnd+"]");
            if((tab.y_rangeStart<=y)&&(y<tab.y_rangeEnd)){
                Tabs.tabs.get(i).isMouseStoppedOnThisTab = true;
            }else{
                Tabs.tabs.get(i).isMouseStoppedOnThisTab = false;
            }
        }

        this.paint( this.getGraphics() );
    }*/

    private int whichTabIsSelected(){
        int index=-1;
        if (Tabs.tabs==null)
            return -1;
        int size = Tabs.tabs.size();
        for(int i=0;i<size;i++){
            Tab tab = Tabs.tabs.get(i);
            if(tab==null)
                continue;
            if(tab.status==1)
                return i;
        }
        return index;
    }
    
    private void userMouseClicked(MouseEvent e) {



        int x = e.getX();
        int y = e.getY();
        //System.out.println("x=["+x+"], y=["+y+"]");

        if((x<0)||(x>this.matteLeft))
            return;


        if(Tabs.tabs==null)   return;
        int size=Tabs.tabs.size();
        
        int latestSelectedTab = whichTabIsSelected();
        int selectedTab=-1;
        for(int i=0;i<size;i++){
            Tab tab = Tabs.tabs.get(i);
            if( tab == null)   return;
            //System.out.println("To range["+tab.y_rangeStart +", "+tab.y_rangeEnd+"]");
            if((tab.y_rangeStart<=y)&&(y<tab.y_rangeEnd)){
                 Tabs.tabs.get(i).status = 1;
                 selectedTab = i;
                 //System.out.println("["+i+"]=1");
            }else{                
                Tabs.tabs.get(i).status = 0;
                //System.out.println("["+i+"]=0");
            }
        }

        int t_tab = whichTabIsSelected();

        //　hide/show the panel while there is no tab is selected
        if(t_tab==-1){
            Tabs.tabs.get(latestSelectedTab).status = 1;
            autoexpand();
        }else{
            if(t_tab==latestSelectedTab){
                autoexpand();
                

                    
            }

            if(this.expaneded){
                if(selectedTab==0){
                    EHOSTEventManager emgr = new EHOSTEventManager();
                    emgr.addDoorListener(new NAVListenser());
                    emgr.openTab_workspace(gui);
                }
                if(selectedTab==1){
                    EHOSTEventManager emgr = new EHOSTEventManager();
                    emgr.addDoorListener(new NAVListenser());
                    emgr.openTab_markables(gui);
                }
            }
            
        }

        this.paint( this.getGraphics() );
    }

    /**This method only can be called by the eHOST main GUI to tell thie componemt
     * that GUI want a specific tab is actived.
     * @param   tabname only can be the names of tabs, such as "Markables" or
     *          "WorkSpace". Not case sensitive.
     */
    public void setTabActived(String tabname){
        try{
            Tabs.setTadActived(tabname);
            this.paint( this.getGraphics() );
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "ERROR 1104201139::" + ex.toString() );
        }
    }

    private void autoexpand()
    {
        int width = this.getWidth();
        if(width==this.minimizeWidth){
            expand();
        }else{
            organize();
        }
    }

    public void expand()
    {
        expaneded = true;
        int height = this.getHeight();
        int width = this.getWidth();
        this.setSize(this.extendedWidth, height);
        this.setPreferredSize(new Dimension(this.extendedWidth, height));
        visibleChildrens(true);
    }

    public void organize(){
        expaneded = false;
        int height = this.getHeight();
        int width = this.getWidth();
        visibleChildrens(false);
        this.setSize(minimizeWidth, height);
        this.setPreferredSize(new Dimension(minimizeWidth, height));
    }

    /**set visible or invisible to children components on this panel*/
    private void visibleChildrens(boolean isVisiable){
        int size=this.getComponentCount();
        for(int i=0;i<size;i++){
            Object o = this.getComponent(i);
            if (o instanceof JPanel){
                ((JPanel)o).setVisible(isVisiable);
            }
        }
        
    }


    public void setTab_onlyProject(){
        setStatus(0);
    }

    public void setTab_All(){
        setStatus(1);
    }

    private void setStatus(int _status){
        if( _status == 1 ){
            Tabs.defineAllTabs();
        }else{
            Tabs.defineOneTab();
        }

        // add mouse event handler
        addMouseHandling();

        this.repaint();
    }


    static class Tabs{
        static Vector<Tab> tabs = new Vector<Tab>();

        /**this method can set all tabs into unselected status except the one
         * user designated.
         */
        public static void setTadActived(String tabname){
            try{
                if((tabname==null)||(tabname.trim().length()<1))
                return;
                for(Tab tab:tabs) {
                    tab.status = 0;
                    if(tab.tabtext.trim().toLowerCase()
                            .compareTo(tabname.trim().toLowerCase())==0)
                        tab.status = 1;
                }
            }catch(Exception ex){
                return;
            }
        }

        public static void clear(){
            tabs.clear();
        }

        public static void defineOneTab(){
            //private String[] tabtexts = {"Projects","Markables"};
            Tabs.clear();
            tabs.add(new Tab("Workspace", 1));            
        }

        public static void defineAllTabs(){
            //private String[] tabtexts = {"Projects","Markables"};
            Tabs.clear();
            tabs.add(new Tab("Workspace", 1));
            tabs.add(new Tab("Navigator", 0));
        }
    }

    
    
}

class Tab{

    String tabtext;
    /**0: unselected; 1: selected; */
    int status=0;

    int y_rangeStart=0;
    int y_rangeEnd=0;
    boolean isMouseStoppedOnThisTab = false;

    public Tab(String _tabtext){
            tabtext=_tabtext;
    }

    public Tab(String _tabtext, int _status){
            tabtext=_tabtext;
            status = _status;
    }
}



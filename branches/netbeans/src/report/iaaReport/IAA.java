/*
 * IAA.java
 *
 * Created on Aug 10, 2011, 4:28:09 PM
 */

package report.iaaReport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JLabel;
import javax.swing.JPanel;
import report.iaaReport.genHtml.GenHtml;

/**
 * This is the GUI interface that help us to generate a IAA similar report.
 * 
 *
 * @author Jianwei Chris Leng
 */
public class IAA extends javax.swing.JFrame {

    public static boolean CHECK_ATTRIBUTES = false;
    public static boolean CHECK_RELATIONSHIP = false;
    public static boolean CHECK_OVERLAPPED_SPANS = false;
    public static boolean CHECK_CLASS = true;
    public static boolean CHECK_COMMENT = false;

    /**pointer that used to link current frame to parents.*/
    protected userInterface.GUI __gui= null;

    private boolean __FLAG1 = false;
    private boolean __FLAG2 = false;

    private int __count1 = 0;
    private int __count2 = 0;

    private ArrayList<String> annotatorNames = null;
    private ArrayList<String> classNames = null;


    /** Creates new form for IAA */
    public IAA(userInterface.GUI _gui) {
        
        // get pointer of parents
        this.__gui = _gui;
        this.__gui.setEnabled(false);

        // assemble components for this frame
        initComponents();

        // set the size and position of current window
        setWindowLocation();        



        // list available annotators before preparing data for IAA report
        new Thread(){
            @Override
             public void run(){
                addIndicateText();
            }
        }.start();

        
    }

    /**Collect and record all annotators  found in saved annotations, and then
     * send all found annotators to a public variable "annotatorNames".
     */
    private void extractAnnotators()
    {
        GetAnnotators getAnnotatorNames = new GetAnnotators();
        annotatorNames = getAnnotatorNames.getAnnotators();               
        // log.LoggingToFile.log(Level.INFO, "1108251401:: collecting data for IAA report: found " + annotatorNames.size() + " annotators.");
    }
    
    /**Collect and record all annotators  found in saved annotations, and then
     * send all found annotators to a public variable "annotatorNames".
     */
    private void extractAnnotationClasses()
    {
        GetAnnotationClasses getAnnotationClasses = new GetAnnotationClasses();
        classNames = getAnnotationClasses.getAnnotationClasses();  
        log.LoggingToFile.log(Level.INFO, "1108251401:: collecting data for IAA report: found " + classNames.size() + " records of classes.");
    }


    private void listAnnotatorsAndClasses(ArrayList<String> annotators, ArrayList<String> annotationClasses){
        jPanel_PaperLook.removeAll();
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();
        jPanel_PaperLook.setLayout( new java.awt.BorderLayout() );
        
        SelectAnnotators selector = new SelectAnnotators(annotators, annotationClasses, this);
        jPanel_PaperLook.add(selector, java.awt.BorderLayout.CENTER);
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();
    }




    /**after user clicked button to ask IAA begin to generate IAA reports,
     * this method will load the core of IAA analysis and run in background. It controls the
     * progress to run analysis and generate html reports.
     */
    public void generateIAAReportInBackground(final ArrayList<String> _selectedAnnotators, final ArrayList<String> selectedClasses ){
        log.LoggingToFile.log(Level.INFO, "begin to generate an IAA similar " +
                "report in background. And show indicator on screen to tell" +
                " user that the thread is running.");

        IAA.setClasses(selectedClasses);
        
        if((_selectedAnnotators==null)||(_selectedAnnotators.size()<1))
        {
            commons.Tools.beep();
            commons.Tools.beep();
            commons.Tools.beep();
            commons.Tools.beep();
            log.LoggingToFile.log(Level.SEVERE, "need at least two annotators " +
                    "to impelement the analysis processing to prepare IAA data.");
            return;
        }

        new Thread(){
            @Override
            public void run(){
                addIndicatorofIAAGenerator(_selectedAnnotators, selectedClasses);
            }
        }.start();        
    }
    
    /**names of selected classes in the IAA system.*/
    public static ArrayList<String> __selected_classes = new ArrayList<String>();
    
    /**record what are names of selected classes.*/
    public static void setClasses(ArrayList<String> _classNames){
        if(__selected_classes==null)
            __selected_classes = new ArrayList<String>();
        
        __selected_classes.clear();
        
        if( _classNames != null ){
            for(String classname : _classNames ){
                if( ( classname == null ) || ( classname.trim().length() < 1 ) )
                    continue;
                
                IAA.__selected_classes.add(classname);
            }
        }
    }
    
    /**check whether a class is selected in IAA reported system.*/
    public static boolean isClassSelected (String _classname) {
        if( IAA.__selected_classes==null)
            return false;
        
        if((IAA.__selected_classes==null)||(IAA.__selected_classes.size()<1))
            return false;
        

        for(String classname : IAA.__selected_classes)
        {
            if(classname==null)
                continue;

            if(classname.trim().compareTo(_classname.trim())==0)
            {
                return true;
            }
        }

        return false;

    }
    /**this */
    static Label textcomment = null;
    /**while we are doing the analysis and generating the IAA html report, there
     * maybe have some error got occurred and you want to show some message on 
     * screen to warning or inform the user.
     * This method is to display the warning message or inform message on screen.
     */
    public static void setwarningtext(String warningcontent){
        textcomment.setText(warningcontent);
    }
    
    /**this method displays and flashing the text indicator on the screen which
     * looks like a real paper.
     */
    private void addIndicatorofIAAGenerator( final ArrayList<String> selectedAnnotators, final ArrayList<String> selectedClasses)
    {
        jPanel_PaperLook.removeAll();
        jPanel_PaperLook.setLayout( new java.awt.BorderLayout() );
        JPanel pane = new JPanel();
        pane.setBackground( new Color( 255, 255, 254) );
        org.jdesktop.layout.GroupLayout paneLayout = new org.jdesktop.layout.GroupLayout( pane );
        pane.setLayout( paneLayout );
        jPanel_PaperLook.add(pane, java.awt.BorderLayout.CENTER );
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();


        Label indicatorText = new Label("Extracting and Generating Reports");
        indicatorText.setBackground(new Color(255,255,254));
        indicatorText.setFont( new Font("Georgia",Font.BOLD,28) );
        indicatorText.setLocation( (jPanel_PaperLook.getWidth()-588)/2,
                (jPanel_PaperLook.getHeight() - 39)/2 - 30 );
        pane.add(indicatorText);

        //System.out.println(indicatorText.getWidth()+", "+indicatorText.getHeight());

        Label comments = new Label(""); //go though all annotations by pair comparison, and then generate IAA report in htmls");
        textcomment = comments;

        comments.setBackground(new Color(255,255,254));
        comments.setLocation( (jPanel_PaperLook.getWidth()-588)/2,
                (jPanel_PaperLook.getHeight() - 18)/2 );
        comments.setFont( new Font("Georgia",Font.BOLD,12) );
        pane.add(comments);
        //System.out.println(comments.getWidth()+", "+comments.getHeight());

        __FLAG2 = true;

        new Thread(){
            @Override
            public void run(){

                // #### analysis all annotations and count to collect data
                // compare annotations and collect information for subsequent
                // steps to generate IAA report                
                // most collected data will be stored into the class of 
                // "classAgreementDepot"
                try{
                    report.iaaReport.analysis.Analysis analysiser =  new
                            report.iaaReport.analysis.Analysis( selectedAnnotators, selectedClasses);
                    analysiser.startAnalysis();                    

                    // #### calculate the f-measure and something else after analysis
                    ClassAgreementDepot.completeForms();

                    
                    // check for matches and unmatches
                    report.iaaReport.analysis.detailsNonMatches.Do doDetailedAnalysis
                            = new report.iaaReport.analysis.detailsNonMatches.Do(selectedAnnotators, selectedClasses);
                    doDetailedAnalysis.run(selectedAnnotators);

                    // #### generate htmls
                    GenHtml genhtml = new GenHtml();
                    genhtml.genHTMLs(
                            ClassAgreementDepot.results,
                            PairWiseDepot.depot_SameAll,
                            selectedAnnotators,
                            selectedClasses,
                            env.Parameters.corpus.LIST_ClinicalNotes.size()
                            );

                    // #### load and display html results on current dialog
                    addHTMLViewer();

                }catch(Exception ex){
                    log.LoggingToFile.log(Level.SEVERE,
                            "1109020257::error occurred while running analysis "
                            + "and generating IAA report!!!\nError Details: "
                            + ex.getLocalizedMessage()
                            );
                }

            }
        }.start();
         


        try {

            __count2 = 0;
            
            final String text = "Extracting and Generating Reports";

            int i = 0;
            int length = text.length();


            while( (__FLAG2) || (__count2<(1*(length-1)) )){

                Thread.sleep(40);
                if( i >= (length-1) )
                    i=0;
                if( i < (length-1) )
                    i++;
                char[] str = text.toCharArray();
                str[i] = '-';
                __count2++;

                indicatorText.setText(String.valueOf(str));
                this.repaint();
                //System.out.println(String.valueOf(str));
                indicatorText.repaint();
            }


        } catch (InterruptedException ex) {
            log.LoggingToFile.log(Level.SEVERE, ex.getMessage() );
        }
    }



    private void addHTMLViewer()
    {
        jPanel_PaperLook.removeAll();
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();
        jPanel_PaperLook.setLayout( new java.awt.BorderLayout() );

        HtmlViewer htmlviewer = new HtmlViewer(this);
        jPanel_PaperLook.add(htmlviewer, java.awt.BorderLayout.CENTER);
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();
    }


    /**Show a text in the middle of current window to indicate that eHOST system
     * is going though all document to prepare all data
     */
    private void addIndicateText(){

        jPanel_PaperLook.setLayout( new java.awt.BorderLayout() );
        
        JPanel pane = new JPanel();
        pane.setBackground( new Color(255,255, 254) );
        org.jdesktop.layout.GroupLayout paneLayout = new org.jdesktop.layout.GroupLayout(pane);
        pane.setLayout(paneLayout);
        
        jPanel_PaperLook.add(pane, java.awt.BorderLayout.CENTER );
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();

        
        JLabel indicatorText = new JLabel("Extracting information");
        indicatorText.setForeground(Color.black);
        indicatorText.setSize( new Dimension(326, 39));
        indicatorText.setBackground(new Color(255,255,254));
        indicatorText.setFont( new Font("Georgia",Font.BOLD,28) );
        indicatorText.setLocation( (jPanel_PaperLook.getWidth()-326)/2,
                (jPanel_PaperLook.getHeight() - 39)/2 - 30 );
        pane.add(indicatorText);

        //System.out.println("To label of \"Extracting information\", its width = " + indicatorText.getWidth() +", height = "+indicatorText.getHeight());
        //System.out.println("    It locates on (" + indicatorText.getX() + ", "+ indicatorText.getY() +" ) ");
        
        JLabel comments = new JLabel("check available annotators from current data set");
        
        comments.setBackground(new Color(255,255,254));
        comments.setLocation( (jPanel_PaperLook.getWidth()-277)/2,
                (jPanel_PaperLook.getHeight() - 18)/2 );
        comments.setFont( new Font("Georgia",Font.BOLD,12) );
        pane.add(comments);
        //System.out.println(comments.getWidth()+", "+comments.getHeight());
        jPanel_PaperLook.add(pane, java.awt.BorderLayout.CENTER );
        jPanel_PaperLook.validate();
        jPanel_PaperLook.updateUI();
        
        __FLAG1 = true;
        
        new Thread(){
            @Override
            public void run(){
                extractAnnotators();
                extractAnnotationClasses();
                __FLAG1 = false; 
            }
        }.start();
        
        try {

            final String text = "Extracting information";
            
            int i = 0;
            int length = text.length();
            

            while( (__FLAG1) || (__count1<(1*(length-1)) )){
                
                Thread.sleep(40);
                if( i >= (length-1) )
                    i=0;
                if( i < (length-1) )
                    i++;
                char[] str = text.toCharArray();
                str[i] = '-';
                __count1++;

                indicatorText.setText(String.valueOf(str));
                //System.out.println(String.valueOf(str));
                indicatorText.repaint();
                this.repaint();
            }

        } catch (InterruptedException ex) {
            log.LoggingToFile.log(Level.SEVERE, ex.getMessage() );
        }


        // after the indicator stopped, we need to remove all old components
        // and try to list annotators' names and class names on screen.
        // So user can decide which of them can join following processing 
        // to calculate and generate IAA report.        
        listAnnotatorsAndClasses( this.annotatorNames, this.classNames );
         
         


    }

    public void exitThisWindow(){
        __gui.setEnabled( true );
        this.dispose();
    }


    private void setWindowLocation()
    {

        if(__gui==null)
            log.LoggingToFile.log(Level.WARNING, "1108151316:: fail to get " 
                    + "parameter!!! The given parameter \"_gui\" is \"null\"!!!");

        // try to make current window have same size of the main eHOST window;
        // but if we can not reach the main gui to get size information, we will
        // use default
        int width  = __gui==null? 836 : __gui.getWidth();
        int height = __gui==null? 577 : __gui.getHeight();

        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));

        // get the location of parent window
        int parentX = __gui==null? 0 : __gui.getX();
        int parentY = __gui==null? 0 : __gui.getY();
        
        log.LoggingToFile.log(Level.INFO, "Set visible of the frame of IAA "
                + "report on location ( "
                + parentX
                +", "
                + parentY
                + " ) with size of ( "
                + width
                + ", "
                + height
                + " ).");

        this.setLocation( parentX, parentY );
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_PaperLook = new PaperLookPane(); //javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("IAA Reports");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel_PaperLook.setBackground(new java.awt.Color(255, 255, 254));
        jPanel_PaperLook.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel_PaperLook, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        __FLAG1 = false;
        this.__gui.setEnabled(true);
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        __FLAG1 = false;
        this.__gui.setEnabled(true);
    }//GEN-LAST:event_formWindowClosing

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated

        this.toFront();
        this.requestFocus();
        commons.Tools.beep();
    }//GEN-LAST:event_formWindowDeactivated

    /**
    * THIS WAS MUTED
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IAA().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel_PaperLook;
    // End of variables declaration//GEN-END:variables

}

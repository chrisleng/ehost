/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SimilarAnnotations.java
 *
 * Created by Jianwei Leng on Aug 26, 2010, 1:34:05 PM
 * Modified by Jianwei Leng on Sep 30, 2010 for multiple threads and extending
 * effective range
 */

package resultEditor.annotationBuilder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import userInterface.GUI;



/**
 *
 * @author Chris
 */
public class OracleFunction extends javax.swing.JFrame {

    /**Window handler of main gui window of eHOST*/
    protected userInterface.GUI gui;

    /**The classname/markable's name which just used to create an annotation
     * in operator's perivious operation.
     */
    protected String classname;
    /**The text file operator just used while finding an annowation in his/her
     * perivious operation.
     */
    protected File file;

    /**Temporaty storage space for similar annotations we just found.*/
    protected Vector<TermsLib> depot = new Vector<TermsLib>();

    protected resultEditor.underlinePainting.SelectionHighlighter painter;


    /** Creates new form SimilarAnnotations */
    public OracleFunction(userInterface.GUI gui,
            String classname, File file) {

        // ##1## set value
        this.gui = gui;
        this.file = file;
        this.classname = classname;
        
        
        

        // ##2## initinates components of this window
        initComponents();

        // ##3## set dialog location
        dialogSetting( gui );

        
        
    }

    public void firstload(Vector<Term> terms){
        
        depot.add(new TermsLib(0, terms, file));
        
        // ##4## show newly found similar terms in the first tab page
        listdisplay( terms );

        // ##5## load text content for the first file
        loadfirstfile(file);
    }

    /**Set paramaters for current window.*/
    private void dialogSetting(userInterface.GUI gui) {

        try{
            // force set dialog size
            Dimension dialog_size =  new Dimension(870, 619);
            this.setPreferredSize(dialog_size);
            this.setSize(dialog_size);

            // set dialog location
            int eHOSTx = gui.getX(); int eHOSTy = gui.getY();
            int x = eHOSTx + (int)((gui.getWidth()-this.getWidth())/2);
            int y = eHOSTy + (int)((gui.getHeight()-this.getHeight())/2);
            this.setLocation(x, y);

            // show first tab
            this.jTabbedPane1.setTitleAt(0, file.getName().trim());

            // setting to conponent of textarea
            this.jTextPane1.setEditable(false);
            this.jTextPane1.setText(null);

            // set attributes for test display
            

        } catch(Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201335:: fail to init the dialog for" +
                    " searching similar annoations!");
        }
        
    }


    /**load text content for first file, use new thread for saving 
     * some init time. */
    private void loadfirstfile(final File file){
        new Thread(){
                @Override
                public void run(){
                    loadText(file);
                    highlightAnnotations_inBackground(0, false);
                }
           }.start();
    }

    /**Open designate text file and show found annotations with markable
     * colorful background.
     *
     * Usually this is not for the first test file. For first text file, please
     * use loadfirstfile(File)
     */
    private void loadfile(final int index){
        //new Thread(){
                //@Override
                //public void run(){
                    try{
                        int max=0;
                        if (depot==null)
                            max=0;
                        else max = depot.size();
                        
                        if((index>max-1)||(index<0))
                            return;
                        File afile = depot.get(index).file;

                        loadText(afile);
                        highlightAnnotations_inBackground(index, false);
                    }catch(Exception ex){
                        log.LoggingToFile.log(Level.SEVERE, "error 1010210024:: fail to load " +
                                "text and annotation texts.");
                    }
                //}
           //}.start();
    }


    /**show text content for disignated text file.*/
    private void loadText(File file){
        
        try{
            
            this.jTextPane1.setText(null);
            if (file==null){
                log.LoggingToFile.log(Level.SEVERE, "Error 1010210018:: file == null");
                return;
            }
            if (!file.exists()){
                log.LoggingToFile.log(Level.SEVERE, "Error 1010210017:: file does not exist");
                return;
            }
            
            BufferedReader docFile = new BufferedReader(new FileReader(file));
            String line = docFile.readLine();

            Document doc = this.jTextPane1.getDocument();

            while (line != null) {

                doc.insertString(doc.getLength(), line+"\n", null);
                line = docFile.readLine();
            }

            docFile.close();

            this.jTextPane1.setCaretPosition(0);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201604:: fail to load text content" +
                    " from text source!\n"+ex.toString());
        }

        
    }
    

    /**Highlight these newly found similar annotations in the sub text viewer.*/
    private void highlightAnnotations_inBackground(int index, boolean isRefreshOperation){
        Color color;
        try{
            color = resultEditor.annotationClasses.Depot.getColor(this.classname.trim());
        }catch(Exception ex){
            color =  Color.black;
        }

        try{
            // remove all old highlighter
            //this.jTextPane1.getHighlighter().removeAllHighlights();

            TermsLib terms = depot.get(index);
            if( terms == null )
                return;
            if(terms.terms==null)
                return;

            DefaultStyledDocument doc = (DefaultStyledDocument) this.jTextPane1.getStyledDocument();

            StyleContext sc2 = new StyleContext();
            Style set_white = sc2.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setBackground(set_white, Color.white);

            StyleContext sc = new StyleContext();
            // attribute to mark usual annotation text
            Style set = sc.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setBackground(set, color);            
            if (color == Color.black)
                StyleConstants.setForeground(set, Color.white);
            else
                StyleConstants.setForeground(set, Color.black);
            

            int amount = 0;
            // go over all new annotations belong designated file
            for(Term term : terms.terms){
                if (term == null)
                    continue;
                                
                int x = term.start;
                int y = term.end;
                //System.out.println("x="+x+", y= "+ y );

                // highlight found annotations
                if (term.selected)
                    doc.setCharacterAttributes(x, y-x, set, true);
                else
                    doc.setCharacterAttributes(x, y-x, set_white, true);
                
                if((amount==0)&&(!isRefreshOperation))
                    this.jTextPane1.setCaretPosition(x);
                
                amount++;
            }


        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201652:: fail to highlight newly " +
                    "found annotations with similar text content");
        }
    }


    


    public void showData_onNewTab(Vector<Term> terms, String classname, File file){
        
        if ((terms==null)||(terms.size() < 1))
            return;

        try{
            final JList ilist = new JList();

            ilist.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    OracleFunction.this.mouseClicked_onList(ilist);
                }

                public void mousePressed(MouseEvent e) {}

                public void mouseReleased(MouseEvent e) {}

                public void mouseEntered(MouseEvent e) {}

                public void mouseExited(MouseEvent e) {}
            });

            ilist.addMouseMotionListener(new MouseMotionListener() {

                public void mouseDragged(MouseEvent me) {
                }

                public void mouseMoved(MouseEvent me) {
                    showAnnotationUnderMouse(me, ilist);
                }
            });


            ilist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


            JScrollPane jp = new JScrollPane();
            //jp.add(ilist);
            jp.setViewportView(ilist);


            this.jTabbedPane1.addTab(file.getName().trim(), jp);
            int index=depot.size();
            depot.add(new TermsLib(index, terms, file));
            
            this.listdisplay(terms, ilist);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010201646:: fail to list data on tab!!!");
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_top = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel_top_left = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel_top_center = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel_top_right = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel_center = new javax.swing.JPanel();
        jPanel_content_left1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel_content_splliter = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel_bottom = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
                formWindowLostFocus(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });

        jPanel_top.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_top.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_top.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(245, 245, 244));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel_top_left.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(0, 102, 153));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanel_top_left.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel6.setBackground(new java.awt.Color(254, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(35, 68));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        jPanel_top_left.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel_top_left, java.awt.BorderLayout.WEST);

        jPanel_top_center.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_top_center.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_top_center.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Courier", 0, 13)); // NOI18N
        jLabel2.setText("<html>eHOST found the following matches to this annotation. Please review these and select relevant annotations you wish to keep</html>");
        jPanel_top_center.add(jLabel2, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel_top_center, java.awt.BorderLayout.CENTER);

        jPanel_top_right.setBackground(new java.awt.Color(254, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/metacontact_online.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_top_rightLayout = new javax.swing.GroupLayout(jPanel_top_right);
        jPanel_top_right.setLayout(jPanel_top_rightLayout);
        jPanel_top_rightLayout.setHorizontalGroup(
            jPanel_top_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_top_rightLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
        jPanel_top_rightLayout.setVerticalGroup(
            jPanel_top_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jPanel2.add(jPanel_top_right, java.awt.BorderLayout.EAST);

        jPanel_top.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel_top, java.awt.BorderLayout.NORTH);

        jPanel_center.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_center.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_center.setLayout(new java.awt.BorderLayout());

        jPanel_content_left1.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(254, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(35, 406));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 496, Short.MAX_VALUE)
        );

        jPanel_content_left1.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel_center.add(jPanel_content_left1, java.awt.BorderLayout.WEST);

        jPanel_content_splliter.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setBackground(new java.awt.Color(255, 255, 254));
        jSplitPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 102), 2));
        jSplitPane1.setDividerLocation(420);
        jSplitPane1.setResizeWeight(0.8);

        jPanel1.setBackground(new java.awt.Color(254, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        list.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        list.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                listMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(list);

        jTabbedPane1.addTab("tab1", jScrollPane1);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 254));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 254));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(255, 255, 254));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel7, java.awt.BorderLayout.SOUTH);

        jPanel9.setBackground(new java.awt.Color(255, 255, 254));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel9, java.awt.BorderLayout.EAST);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTextPane1.setBorder(null);
        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        jScrollPane3.setViewportView(jTextPane1);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(jPanel3);

        jPanel_content_splliter.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel_center.add(jPanel_content_splliter, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel_center, java.awt.BorderLayout.CENTER);

        jPanel_bottom.setBackground(new java.awt.Color(240, 240, 241));
        jPanel_bottom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 241), 3));

        jButton1.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton1.setText("Save Changes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Don't show this dialog next time.");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_bottomLayout = new javax.swing.GroupLayout(jPanel_bottom);
        jPanel_bottom.setLayout(jPanel_bottomLayout);
        jPanel_bottomLayout.setHorizontalGroup(
            jPanel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_bottomLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 373, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(3, 3, 3))
        );
        jPanel_bottomLayout.setVerticalGroup(
            jPanel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_bottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton2)
                .addComponent(jButton1)
                .addComponent(jCheckBox1))
        );

        getContentPane().add(jPanel_bottom, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
            int indexmax = list.getModel().getSize();
            int selectedindex = list.getSelectedIndex();
            if( ( selectedindex >= indexmax ) ||(selectedindex < 0))
                return;

            if( depot.get(0).terms == null )
                return;
            if ( selectedindex >= depot.get(0).terms.size()  )
                return;

            Term thisterm = depot.get(0).terms.get( selectedindex );
            if ( thisterm == null )
                return;

            thisterm.selected = !thisterm.selected;


            listdisplay(depot.get(0).terms);

            // refresh all backgroud markers
            highlightAnnotations_inBackground(0, true);
    }//GEN-LAST:event_listMouseClicked


    private void mouseClicked_onList(JList thislist) {
        try{
            int index = this.jTabbedPane1.getModel().getSelectedIndex();


        Vector<Term> thisterms = depot.get(index).terms;
        int indexmax = thislist.getModel().getSize();
            int selectedindex = thislist.getSelectedIndex();
            if( ( selectedindex >= indexmax ) ||(selectedindex < 0))
                return;

            if( thisterms == null )
                return;
            if ( selectedindex >= thisterms.size()  )
                return;

            Term thisterm = thisterms.get( selectedindex );
            if ( thisterm == null )
                return;

            thisterm.selected = !thisterm.selected;


            listdisplay(thisterms, thislist);

            highlightAnnotations_inBackground(index, true);
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202303:: error got after mouse " +
                    "clicked on candidate entries entry of list!");
        }



            
    }


    /**Call method to save newly found annotations to memory.*/
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        save();
    }//GEN-LAST:event_jButton1ActionPerformed


    /**User checked checkbox to turn on/off the function for finding
     * similar annotations.*/
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        try{
            env.Parameters.oracleFunctionEnabled = !jCheckBox1.isSelected();
            if (gui != null)
                gui.setFlag_of_OracleSimilarPhraseSeeker(env.Parameters.oracleFunctionEnabled);
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010111651:: fail to set ");
        }

    }//GEN-LAST:event_jCheckBox1ActionPerformed

    
    private void listMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseMoved
        showAnnotationUnderMouse(evt, this.list);
    }//GEN-LAST:event_listMouseMoved


    
    private void highlight_byUnderline(int spanstart, int spanend){
        try{
            this.jTextPane1.getHighlighter().removeAllHighlights();
            resultEditor.underlinePainting.SelectionHighlighter pa = new
                    resultEditor.underlinePainting.SelectionHighlighter(this.jTextPane1);

            pa.addNewUnderlineHightlight(spanstart, spanend);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202040:: fail to draw underline " +
                    "under focused annotation!");
        }
    }


    private void showAnnotationUnderMouse(java.awt.event.MouseEvent evt, JList thislist){
        try{
            if (evt==null)
                return;
            if (this.list.getModel().getSize()<=0)
                return;

            // get index of entry which our mouse locates on.
            int index = thislist.locationToIndex(evt.getPoint());
            int max = thislist.getModel().getSize();
            if ((index<0)||(index>max-1))
                return;

            // get index of current tab
            int tabindex = this.jTabbedPane1.getSelectedIndex();

            // get current terms list
            TermsLib terms = depot.get(tabindex);
            if (terms==null)
                return;

            if (terms.terms == null)
                return;

            // get current term
            Term t = terms.terms.get(index);
            if (t==null)
                return;

            // get span start of current term
            int position = t.start;


            highlight_byUnderline(t.start, t.end);

            this.jTextPane1.setCaretPosition(position);


        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202111:: fail to show current annotatino"+e.toString());
        }
    }


    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        
    }//GEN-LAST:event_formFocusLost


    /**While user clicked tab to view new found annotations from another
     * text document, open related text content.
     */
    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

        try{
            // check which tab is selected
            int index = this.jTabbedPane1.getSelectedIndex();
            loadfile(index);
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010202147:: fail to switch selected text document!");
        }

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void formWindowLostFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowLostFocus
        this.requestFocus();
        commons.Tools.beep();        
    }//GEN-LAST:event_formWindowLostFocus


    /**save newly found annotations to memory.*/
    private void save(){
        try{

            // validity check
            if (depot == null){
                this.dispose();
                return;
            }

            int count = 0;

            // try to save all found annotations
            for(TermsLib termlib:depot){
                Vector<Term> terms = termlib.terms;
                if ( terms == null ){
                    this.dispose();
                    return;
                }

                for( Term term: terms){
                    if (term == null)
                        continue;
                    if ( term.selected == true ){
                        if(termlib.index==0)
                            count++;
                        saveterm(term, this.classname, termlib.file);
                        log.LoggingToFile.log(Level.INFO, "Oracle Saving: " + term.termtext );
                    }
                }


            }

            // update screen if new annotations were generated
            if (count > 0){
                gui.showAnnotationCategoriesInTreeView_refresh();
                gui.display_repaintHighlighter();
                gui.showValidPositionIndicators();

                String testsourceFilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
                setSelectedDatainDepot( testsourceFilename, depot.get(0).terms.get(0).termtext );
                //gui.showAnnotationDetail(annotation);
                gui.showSelectedAnnotations_inList(0);
            }

        } catch(Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201540:: fail to save newly " +
                    "found similar annotations into memory");
        }

        // exit from this window
        this.dispose();
    }

    private void setSelectedDatainDepot( String filename, String annotationtext ){
        try{
            int index = getAnnotationIndex( filename, annotationtext);
            if ( index < 0)
                return;

            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            depot.setSelectedAnnotationsIndexes(index);
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201543:: fail to set flag.");
        }
     }

    private int getAnnotationIndex(String filename, String annotationtext){
        try{
            if ( annotationtext == null )
                return -1;
            if ( filename == null  )
                return -1;

            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            resultEditor.annotations.Article article = depot.getArticleByFilename(filename.trim());
            if ( article == null )
                return -1;

            if ( article.annotations == null )
                return -1;

            int size = article.annotations.size();
            for(int i=0;i<size;i++){
                resultEditor.annotations.Annotation thisannotation = article.annotations.get(i);
                if ( thisannotation == null )
                    continue;

                if ( thisannotation.annotationText.trim().compareTo( annotationtext.trim()) == 0 )
                    return i;


            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201545:: fail to get index for new annotations!!!");
        }

        return -1;
    }


    /**
     * Build a new annotation and save it in main memroy by 3 given conditions:
     * [1] term text, [2] classname(also called category name, or markable name)
     * [3] file name of the document
     *
     * @param   term
     *          term text
     *
     * @param   classname
     *          classname(also called category name, or markable name)
     *
     * @param   filename
     *          file name of the document
     */
    private void saveterm(Term term, String classname, File file){
        try{
            // validity check
            if (term == null)
                return;
            if((file==null)||(file.getName()==null)||(file.getName().trim().length()<1))
                return;


            // save annotation
            resultEditor.annotations.Depot depotOfAnn = new resultEditor.annotations.Depot();
            depotOfAnn.articleInsurance( file.getName().trim() );
            
            String createdate = commons.OS.getCurrentDate(); // get current time stamp
            String annotator_name = resultEditor.workSpace.WorkSet.current_annotator_name;
            String annotator_id = resultEditor.annotator.Manager.getAnnotatorID_outputOnly();

            // ensure no repetitive
            int repetitiveflag = depotOfAnn.repetitiveCheck(file.getName().trim(), term.termtext.trim(),
                    classname.trim(), term.start, term.end);
            // STOP here if this annotation already existed
            if( repetitiveflag < 0 )
                return;

            // save annotation
            int newuniqueindex = assignMeAUniqueIndex();  
            String testsourceFilename = file.getName().trim();//resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                depotOfAdj.addANewAnnotation_Oracle
                        ( testsourceFilename, //file.getName().trim(),
                    term.termtext,
                    term.start,
                    term.end,
                    // creation date
                    createdate,
                    // annotation classname
                    classname,
                    "Oracle["+annotator_name+"]",
                    annotator_id,
                    null,
                    null,
                    newuniqueindex
                    );
            }else
                depotOfAnn.addANewAnnotation( testsourceFilename, //file.getName().trim(),
                    term.termtext,
                    term.start,
                    term.end,
                    // creation date
                    createdate,
                    // annotation classname
                    classname,
                    "Oracle["+annotator_name+"]",
                    annotator_id,
                    null,
                    null,
                    newuniqueindex
                    );
            
            depotOfAnn.setAttributeDefault( testsourceFilename, newuniqueindex );
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.INFO, "error1205071701: " + ex.getStackTrace().toString() );
            ex.printStackTrace();
            
        }
    }


    /**
     * Ask for a new unique index for a new annotation before saving it.
     *
     * @return  The unique index which allocated for this new annotation
     */
    private int assignMeAUniqueIndex(){
         return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }


    public void listdisplay(Vector<Term> terms) {

        // remove old data
        Vector<Object> listentry = new Vector<Object>();
        list.setListData(listentry);

        if (terms == null)
            return;

        for( Term term : terms){


            iListEntry i;
            i = new iListEntry( term );            
            listentry.add( i );

        }

        list.setListData( listentry );
        list.setCellRenderer(new iListCellRenderer());


    }

    public void listdisplay(Vector<Term> terms, JList thislist) {

        // remove old data
        Vector<Object> listentry = new Vector<Object>();
        thislist.setListData(listentry);

        if (terms == null)
            return;

        for( Term term : terms){


            iListEntry i;
            i = new iListEntry( term );
            listentry.add( i );

        }

        thislist.setListData( listentry );
        thislist.setCellRenderer(new iListCellRenderer());

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_bottom;
    private javax.swing.JPanel jPanel_center;
    private javax.swing.JPanel jPanel_content_left1;
    private javax.swing.JPanel jPanel_content_splliter;
    private javax.swing.JPanel jPanel_top;
    private javax.swing.JPanel jPanel_top_center;
    private javax.swing.JPanel jPanel_top_left;
    private javax.swing.JPanel jPanel_top_right;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables

    class TermsLib{
    public int index;
    public Vector<Term> terms = new Vector<Term>();
    public File file;

    public TermsLib(){}
    public TermsLib(int index, Vector<Term> terms, File file){
        this.index = index;
        this.terms = terms;
        this.file = file;
    }
}
}


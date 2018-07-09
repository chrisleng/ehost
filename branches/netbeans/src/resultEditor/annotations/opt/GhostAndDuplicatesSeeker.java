/*
 * RemoveDuplicates.java
 *
 * Created on Jan 03, 2011, 2:42:59 PM, by Jianwei Chris Leng, in Williams Building, U of U
 * Modified on May 23, 2011 for Ghost annotation and duplicates, by Jianwei Chris Leng, in VA
 */

package resultEditor.annotations.opt;

import resultEditor.annotations.Depot;
import resultEditor.annotations.Depot_DuplicateAndGhosts;
import java.awt.Dimension;
import java.io.File;
import java.util.Vector;
import javax.swing.JList;
import resultEditor.annotations.DupilcateAndGhost;
import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author leng
 */
public class GhostAndDuplicatesSeeker extends javax.swing.JFrame {
    protected userInterface.GUI gui;
    protected int __max=0;

    /** Creates new form RemoveDuplicates */
    public GhostAndDuplicatesSeeker(userInterface.GUI gui) {
        this.gui = gui;

        initComponents();
        // hide the result pane while just launch this dialog
        //jPanel_results.setVisible(false);
        //jButton_alldocs.setVisible(false);

        this.clearResultArea();

        //set size dialog attributes, such as size and position.
        setDialog();
        setComponents_byParameters();

        // set min and max value by return value from
        // resultEditor.annotations.Depot.getSizeofallannotations()
        setProgressBar();

        this.update(this.getGraphics());

        checkcurrentfile();
    }

    /**set size dialog attributes, such as size and position.*/
    private void setDialog(){
        // set dialog size
        int width=925; int height=678;
        this.setSize(new Dimension(width,height));

        // set dialog postion
        int w = gui.getWidth();
        int h = gui.getHeight();
        int guix=gui.getX(), guiy=gui.getY();

        int x=guix+(int)((w-width)/2);
        int y=guiy+(int)((h-height)/2);

        this.setLocation(x, y);

        // remove content in list
        Vector<String> llist = new Vector();
        //jList_msg.setListData(llist);

        //this.jProgressBar1.setValue(0);


        try{
        // show first tab
            this.jTabbedPane1.setTitleAt(0, resultEditor.workSpace.WorkSet.getCurrentFile().getName() );

            // setting to conponent of textarea
            this.jTextPane1.setEditable(false);
            this.jTextPane1.setText(null);

            //loadText(resultEditor.workSpace.WorkSet.getCurrentFile());
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1105271321::" + ex.toString());
        }

        list.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**set min and max value by return value from
     * resultEditor.annotations.Depot.getSizeofallannotations()
     */
    private void setProgressBar(){
        //resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        //int max=depot.getSizeOfAllAnnotations();
        //this.__max = max;

        //this.jProgressBar1.setMinimum(0);
        //this.jProgressBar1.setMaximum(max);

        //this.jLabel_msg.setText("Progress(0/"+max+"):");
    }


    /**get access to current document*/
    private File getCurrentDoc(){
        try{
            return resultEditor.workSpace.WorkSet.getCurrentFile();
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "ERROR 1105231550::" + e.getLocalizedMessage() );
            return null;
        }
    }

    /**search and list all duplicates in the depot of annotations*/
    private void checkAndListDuplicates(File thisfile)
    {
        // ##1## search for duplicates
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        //depot.findAndListDuplicates(this.jProgressBar1, GhostAndDuplicatesSeeker.jList_msg, this.jLabel_msg, __max);
        //if(!env.Parameters.DuplicatesAndGhostSeeker.foralldocs)
        if(thisfile==null)
            thisfile = getCurrentDoc();

        Vector<DupilcateAndGhost> found_duplicates_and_ghosts = depot.searchDuplicates(currentFile);



        // #### 2 ####
        // list all anotation duplicates
        listdisplay(found_duplicates_and_ghosts, list);

        // ##3## refresh screen after removing duplicated annotations
        gui.display_repaintHighlighter();
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.showValidPositionIndicators();
        gui.display_SetStatusInsiable_ToComparisionPanel();
    }

    public void listdisplay(Vector<DupilcateAndGhost> terms, JList thislist) {

        // remove old data
        Vector<Object> listentry = new Vector<Object>();
        thislist.setListData(listentry);

        if (terms == null)
            return;

        for( DupilcateAndGhost term : terms){
            iListEntry i;
            i = new iListEntry( term );
            listentry.add( i );
        }

        thislist.setListData( listentry );
        thislist.setCellRenderer(new iListCellRenderer());

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox_checkDuplicates_forSameSpans = new javax.swing.JCheckBox();
        jCheckBox_checkDuplicates_forSameAttributes = new javax.swing.JCheckBox();
        jCheckBox_checkDuplicates_forSameRelationships = new javax.swing.JCheckBox();
        jLabel_msg1 = new javax.swing.JLabel();
        jLabel_msg2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox_ghost_annotation_spanless = new javax.swing.JCheckBox();
        jCheckBox_ghost_classless = new javax.swing.JCheckBox();
        jCheckBox_ghost_outofrange = new javax.swing.JCheckBox();
        jButton_currentdoc = new javax.swing.JButton();
        jButton_alldocs = new javax.swing.JButton();
        jPanel_results = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jButton_currentdoc1 = new javax.swing.JButton();
        jButton_currentdoc2 = new javax.swing.JButton();
        jLabel_msg4 = new javax.swing.JLabel();
        jCheckBox_checkDuplicates_forSameClass = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);

        jPanel2.setBackground(new java.awt.Color(208, 204, 204));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(208, 204, 204), 3));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(208, 204, 204));
        jPanel4.setLayout(new java.awt.GridLayout(1, 2, 2, 0));

        jButton2.setBackground(new java.awt.Color(208, 204, 204));
        jButton2.setFont(new java.awt.Font("Calibri", 1, 13));
        jButton2.setText("      OK      ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jPanel2.add(jPanel4, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Cambria", 0, 12));
        jLabel1.setText("Check all annotations duplicates by their attributes, relationships, comments and span information:");

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 12));
        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("Please define what kind of annotations are duplicates?");

        jCheckBox_checkDuplicates_forSameSpans.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_checkDuplicates_forSameSpans.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_checkDuplicates_forSameSpans.setSelected(true);
        jCheckBox_checkDuplicates_forSameSpans.setText("duplicates if they have same annotation spans");
        jCheckBox_checkDuplicates_forSameSpans.setEnabled(false);

        jCheckBox_checkDuplicates_forSameAttributes.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_checkDuplicates_forSameAttributes.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_checkDuplicates_forSameAttributes.setText("if they have totally same attributes");

        jCheckBox_checkDuplicates_forSameRelationships.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_checkDuplicates_forSameRelationships.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_checkDuplicates_forSameRelationships.setText("if they have totally same  relationships");

        jLabel_msg1.setFont(new java.awt.Font("Cambria", 0, 12));
        jLabel_msg1.setText("and");

        jLabel_msg2.setFont(new java.awt.Font("Cambria", 0, 12));
        jLabel_msg2.setText("and");

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 12));
        jLabel3.setForeground(new java.awt.Color(0, 0, 153));
        jLabel3.setText("<html>If you want to find ghost annotations, such as spanless annotaitons, classless spans, or out of range annotations, <div>Please check following checkboxes:</html>");

        jCheckBox_ghost_annotation_spanless.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_ghost_annotation_spanless.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_ghost_annotation_spanless.setText("spanless annotations");

        jCheckBox_ghost_classless.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_ghost_classless.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_ghost_classless.setText("classless spans");

        jCheckBox_ghost_outofrange.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_ghost_outofrange.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_ghost_outofrange.setText("out of range annotations");

        jButton_currentdoc.setFont(new java.awt.Font("Cambria", 1, 12));
        jButton_currentdoc.setForeground(new java.awt.Color(102, 0, 0));
        jButton_currentdoc.setText("Check CURRENT document");
        jButton_currentdoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_currentdocActionPerformed(evt);
            }
        });

        jButton_alldocs.setFont(new java.awt.Font("Cambria", 1, 12));
        jButton_alldocs.setForeground(new java.awt.Color(102, 0, 0));
        jButton_alldocs.setText("Delete Selected Items");
        jButton_alldocs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_alldocsActionPerformed(evt);
            }
        });

        jPanel_results.setBackground(new java.awt.Color(240, 240, 242));
        jPanel_results.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204)));
        jPanel_results.setLayout(new java.awt.BorderLayout());

        jSplitPane1.setBackground(new java.awt.Color(238, 238, 237));
        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(400);

        jPanel7.setBackground(new java.awt.Color(238, 238, 237));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(238, 238, 237));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        list.setFont(new java.awt.Font("Calibri", 0, 14));
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
        jScrollPane2.setViewportView(list);

        jTabbedPane1.addTab("tab1", jScrollPane2);

        jPanel7.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jSplitPane1.setLeftComponent(jPanel7);

        jTextPane1.setFont(new java.awt.Font("Courier New", 0, 12));
        jTextPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTextPane1MouseMoved(evt);
            }
        });
        jScrollPane3.setViewportView(jTextPane1);

        jSplitPane1.setRightComponent(jScrollPane3);

        jPanel_results.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jButton_currentdoc1.setFont(new java.awt.Font("Cambria", 1, 12));
        jButton_currentdoc1.setForeground(new java.awt.Color(102, 0, 0));
        jButton_currentdoc1.setText("Previous Doc");
        jButton_currentdoc1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_currentdoc1ActionPerformed(evt);
            }
        });

        jButton_currentdoc2.setFont(new java.awt.Font("Cambria", 1, 12));
        jButton_currentdoc2.setForeground(new java.awt.Color(102, 0, 0));
        jButton_currentdoc2.setText("Next Doc");
        jButton_currentdoc2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_currentdoc2ActionPerformed(evt);
            }
        });

        jLabel_msg4.setFont(new java.awt.Font("Cambria", 0, 12));
        jLabel_msg4.setText("and");

        jCheckBox_checkDuplicates_forSameClass.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_checkDuplicates_forSameClass.setFont(new java.awt.Font("Cambria", 0, 12));
        jCheckBox_checkDuplicates_forSameClass.setText("if they have same class");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel_results, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .add(54, 54, 54)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel_msg1)
                            .add(jLabel_msg2)
                            .add(jLabel_msg4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox_checkDuplicates_forSameClass)
                            .add(jCheckBox_checkDuplicates_forSameRelationships)
                            .add(jCheckBox_checkDuplicates_forSameAttributes)
                            .add(jCheckBox_checkDuplicates_forSameSpans))))
                .addContainerGap())
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .add(jButton_currentdoc1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_currentdoc)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton_currentdoc2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 221, Short.MAX_VALUE)
                        .add(jButton_alldocs))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(70, 70, 70)
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jCheckBox_ghost_outofrange)
                                    .add(jCheckBox_ghost_classless)
                                    .add(jCheckBox_ghost_annotation_spanless))))))
                .add(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .add(18, 18, 18)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_checkDuplicates_forSameSpans)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox_checkDuplicates_forSameAttributes)
                    .add(jLabel_msg1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jCheckBox_checkDuplicates_forSameRelationships)
                    .add(jLabel_msg2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel_msg4)
                    .add(jCheckBox_checkDuplicates_forSameClass))
                .add(18, 18, 18)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_ghost_annotation_spanless)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_ghost_classless)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jCheckBox_ghost_outofrange)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_currentdoc1)
                    .add(jButton_currentdoc)
                    .add(jButton_currentdoc2)
                    .add(jButton_alldocs))
                .add(32, 32, 32)
                .add(jPanel_results, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**user click OK to close this dialog*/
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        // quit and refresh the main GUI for deleting operations before back to
        // the main GUI
        quit_WithMainGUIRefreshed();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**close this window(/dialog) and */
    private void quit_WithMainGUIRefreshed(){
        // ##1## refresh screen after removing duplicated annotations
        gui.display_repaintHighlighter();
        gui.showAnnotationCategoriesInTreeView_refresh();
        gui.showValidPositionIndicators();
        gui.display_SetStatusInsiable_ToComparisionPanel();

        // close this dialog
        this.dispose();
    }

    private File currentFile = null;

    private void jButton_currentdocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_currentdocActionPerformed

        checkcurrentfile();

        
    }//GEN-LAST:event_jButton_currentdocActionPerformed

    private void checkcurrentfile(){
        jPanel_results.setVisible(true);

        // tell parameters that we will only check duplicates/ghosts for
        // current document
        env.Parameters.DuplicatesAndGhostSeeker.foralldocs = false;

        // set flag for duplicates
        setFlag_Duplicates();

        // start to check dulicates
        // starting checking and comparing
        new Thread(){
                @Override
                public void run(){
                    if(currentFile == null)
                        currentFile = resultEditor.workSpace.WorkSet.getCurrentFile();
                    if(currentFile==null)
                        return;

                    checkAndListDuplicates(currentFile);

                    loadfile(currentFile);

                }}.start();
    }

    /**load text content for first file, use new thread for saving
     * some init time. */
    private void loadfile(final File file)
    {
        this.jTabbedPane1.setTitleAt(0, file.getName() );
        loadText(file);
        highlightAnnotations_inBackground(file);
    }

    private void setComponents_byParameters(){
        // duplicates must have same span
        jCheckBox_checkDuplicates_forSameSpans.setSelected( env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samespan );
        // duplicates must have same attributes
        jCheckBox_checkDuplicates_forSameAttributes.setSelected( env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameattributes );
        // duplicates must have same relationships
        jCheckBox_checkDuplicates_forSameRelationships.setSelected( env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samerelationships );
        
        // == DISABLED AS USER DON'T WANT TO COMPARE ANNOTATIONS BY COMMENTS 
        // == JUNE 14, 2011
        // duplicates must have same comments
        //jCheckBox_checkDuplicates_forSameComments.setSelected( env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samecomments );
        // == DISABLED

        // duplicates must have same classes
        jCheckBox_checkDuplicates_forSameClass.setSelected( env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameclasses );

        // #### 2 ####
        // set parameters for ghost annotation
        jCheckBox_ghost_annotation_spanless.setSelected( env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_spanless );
        jCheckBox_ghost_classless.setSelected( env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_classless );
        jCheckBox_ghost_outofrange.setSelected( env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_outofrange );
    }

    private void clearResultArea()
    {
        Vector empty = new Vector();
        list.setListData(empty);

        jTextPane1.setText(null);
    }

    private File gonext()
    {
        try{
            File[] files = env.Parameters.corpus.getFiles();
            if(files==null)
            {
                commons.Tools.beep();
                log.LoggingToFile.log(Level.SEVERE, "error 1105271411::");
                return null;
            }

            int size = files.length;
            if(size < 1)
                return null;

            int location = -1;

            for(int i=0; i<size; i++){
                File f = files[i];
                if(f==null)
                    continue;
                if(f.getName().trim().compareTo(currentFile.getName().trim())==0){
                    location = i;
                    break;
                }
            }

            if((location < 0)||(location >size-1))
                return null;
            if(location == (size-1) )
                return null;

            return files[location + 1];

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1105271412::");
        }
        return null;
    }

    private File goback()
    {
        try{
            File[] files = env.Parameters.corpus.getFiles();
            if(files==null)
            {
                commons.Tools.beep();
                log.LoggingToFile.log(Level.SEVERE, "error 1105271411::");
                return null;
            }

            int size = files.length;
            if(size < 1)
                return null;

            int location = -1;

            for(int i=0; i<size; i++){
                File f = files[i];
                if(f==null)
                    continue;
                if(f.getName().trim().compareTo(currentFile.getName().trim())==0){
                    location = i;
                    break;
                }
            }

            if((location < 0)||(location >size-1))
                return null;
            if(location ==0 )
                return null;

            return files[location - 1];

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1105271412::");
        }
        return null;
    }

    private void jButton_currentdoc1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_currentdoc1ActionPerformed

        

        File file = goback();
        if(file==null){
            commons.Tools.beep();
            return;
        }

        clearResultArea();

        currentFile = file;


        jPanel_results.setVisible(true);

        // tell parameters that we will only check duplicates/ghosts for
        // current document
        env.Parameters.DuplicatesAndGhostSeeker.foralldocs = false;

        // set flag for duplicates
        setFlag_Duplicates();

        // start to check dulicates
        // starting checking and comparing
        new Thread(){
                @Override
                public void run(){
                    if(currentFile==null)
                        return;

                    checkAndListDuplicates(currentFile);

                    loadfile(currentFile);

                }}.start();
    }//GEN-LAST:event_jButton_currentdoc1ActionPerformed

    private void jButton_currentdoc2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_currentdoc2ActionPerformed
        

        File file = gonext();
        if(file==null){
            commons.Tools.beep();
            return;
        }
        
        clearResultArea();

        currentFile = file;


        jPanel_results.setVisible(true);

        // tell parameters that we will only check duplicates/ghosts for
        // current document
        env.Parameters.DuplicatesAndGhostSeeker.foralldocs = false;

        // set flag for duplicates
        setFlag_Duplicates();

        // start to check dulicates
        // starting checking and comparing
        new Thread(){
                @Override
                public void run(){
                    if(currentFile==null)
                        return;

                    checkAndListDuplicates(currentFile);

                    loadfile(currentFile);

                }}.start();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_currentdoc2ActionPerformed

    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
       int indexmax = list.getModel().getSize();
            int selectedindex = list.getSelectedIndex();
            if( ( selectedindex >= indexmax ) ||(selectedindex < 0))
                return;

            if( Depot_DuplicateAndGhosts.get(0) == null )
                return;

            if ( selectedindex >= Depot_DuplicateAndGhosts.get().size()  )
                return;

            DupilcateAndGhost thisterm = Depot_DuplicateAndGhosts.get( selectedindex );
            if ( thisterm == null )
                return;

            thisterm.selected = !thisterm.selected;


            listdisplay(Depot_DuplicateAndGhosts.get(), list);
        
}//GEN-LAST:event_listMouseClicked

    /**Highlight these newly found similar annotations in the sub text viewer.*/
    private void highlightAnnotations_inBackground(File file)
    {
        if((file==null)||(file.getName()==null)||(file.getName().trim().length()<1))
            return;

        

        try{
            // remove all old highlighter
            //this.jTextPane1.getHighlighter().removeAllHighlights();

            Vector<DupilcateAndGhost> duplicates = Depot_DuplicateAndGhosts.get();
            if(( duplicates == null )||(duplicates.size() < 1))
                return;
            
            DefaultStyledDocument doc = (DefaultStyledDocument) this.jTextPane1.getStyledDocument();

            StyleContext sc2 = new StyleContext();
            Style set_white = sc2.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setBackground(set_white, Color.white);

            StyleContext sc = new StyleContext();

            Color color;


            


            int amount = 0;
            // go over all new annotations belong designated file
            for(DupilcateAndGhost du : duplicates)
            {
                if (du == null)
                    continue;

                try{
                color = resultEditor.annotationClasses.Depot.getColor(du.referenceAnnotation.annotationclass.trim());
                }catch(Exception ex){
                color =  Color.blue;
                }
                // attribute to mark usual annotation text
            Style set = sc.getStyle(StyleContext.DEFAULT_STYLE);
            StyleConstants.setBackground(set, color);
            if (color == Color.black)
                StyleConstants.setForeground(set, Color.white);
            else
                StyleConstants.setForeground(set, Color.black);
            

                int x = du.referenceAnnotation.spanstart;
                int y = du.referenceAnnotation.spanend;
                //System.out.println("x="+x+", y= "+ y );

                // highlight found annotations
                if (du.selected)
                    doc.setCharacterAttributes(x, y-x, set, true);
                else
                    doc.setCharacterAttributes(x, y-x, set_white, true);

                if(amount==0)//&&(!isRefreshOperation))
                    this.jTextPane1.setCaretPosition(x);

                amount++;
            }


        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1010201652:: fail to highlight newly " +
                    "found annotations with similar text content");
        }

    }

    private void listMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseMoved
        showAnnotationUnderMouse(evt, this.list);
}//GEN-LAST:event_listMouseMoved

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged

    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jTextPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPane1MouseMoved
        
    }//GEN-LAST:event_jTextPane1MouseMoved

    private void jButton_alldocsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_alldocsActionPerformed
        deleteSelections();
        checkcurrentfile();
        //gui.showAnnotationCategoriesInTreeView_refresh();
        //gui.showValidPositionIndicators();
        gui.refreshResultEditor();
    }//GEN-LAST:event_jButton_alldocsActionPerformed

    private void deleteSelections(){
        deleteDuplicates();
        deleteGhosts();
    }

    private void deleteDuplicates(){
        Depot.deleteDuplicates( this.currentFile.getName(), Depot_DuplicateAndGhosts.get() );
    }

    private void deleteGhosts(){
        Depot.deleteGhosts( this.currentFile.getName(), Depot_DuplicateAndGhosts.get() );
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
            //int tabindex = this.jTabbedPane1.getSelectedIndex();

            // get current terms list
            //Object a =
            //TermsLib terms = jTabbedPane1.getdepot.get(tabindex);
            //if (terms==null)
            //    return;

            //if (terms.terms == null)
            //    return;

            // get current term
            DupilcateAndGhost t = Depot_DuplicateAndGhosts.get(index);
            if (t==null)
                return;

            if((t.type!=1)&&(t.type!=4))
                    return;

            // get span start of current term
            int position = t.referenceAnnotation.spanstart;


            highlight_byUnderline(t.referenceAnnotation.spanstart, t.referenceAnnotation.spanend);

            this.jTextPane1.setCaretPosition(position);


        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "error 1010202111:: fail to show current annotatino"+e.toString());
        }
    }

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


    /**Set parameters for subsequent operation to search duplicates and
      *ghost annotations based on the checkbox used selected on screen
      */
    private void setFlag_Duplicates(){
        // #### 1 ####
        // set parameters for subsequent operation to search duplicates and
        // ghost annotations based on the checkbox used selected on screen

        // duplicates must have same span
        env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samespan = jCheckBox_checkDuplicates_forSameSpans.isSelected();
        // duplicates must have same attributes
        env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameattributes = jCheckBox_checkDuplicates_forSameAttributes.isSelected();
        // duplicates must have same relationships
        env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samerelationships = jCheckBox_checkDuplicates_forSameRelationships.isSelected();
        
        // == DISABLED AS USER DON'T WANT TO COMPARE ANNOTATIONS BY COMMENTS
        // == JUNE 14, 2011
        // duplicates must have same comments
        //env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samecomments = jCheckBox_checkDuplicates_forSameComments.isSelected();
        // == DISABLED END

        // duplicates must have same classes
        env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameclasses = this.jCheckBox_checkDuplicates_forSameClass.isSelected();

        // #### 2 ####
        // set parameters for ghost annotation
        env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_spanless = jCheckBox_ghost_annotation_spanless.isSelected();
        env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_classless = jCheckBox_ghost_classless.isSelected();
        env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_outofrange = jCheckBox_ghost_outofrange.isSelected();
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


    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RemoveDuplicates().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton_alldocs;
    private javax.swing.JButton jButton_currentdoc;
    private javax.swing.JButton jButton_currentdoc1;
    private javax.swing.JButton jButton_currentdoc2;
    private javax.swing.JCheckBox jCheckBox_checkDuplicates_forSameAttributes;
    private javax.swing.JCheckBox jCheckBox_checkDuplicates_forSameClass;
    private javax.swing.JCheckBox jCheckBox_checkDuplicates_forSameRelationships;
    private javax.swing.JCheckBox jCheckBox_checkDuplicates_forSameSpans;
    private javax.swing.JCheckBox jCheckBox_ghost_annotation_spanless;
    private javax.swing.JCheckBox jCheckBox_ghost_classless;
    private javax.swing.JCheckBox jCheckBox_ghost_outofrange;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel_msg1;
    private javax.swing.JLabel jLabel_msg2;
    private javax.swing.JLabel jLabel_msg4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel_results;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables

}

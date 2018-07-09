/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Comparator.java
 *
 * Created on Nov 10, 2010, 3:04:46 PM
 */

package userInterface.annotationCompare;

import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JTextPane;
import resultEditor.annotations.Annotation.AdjudicationStatus;
import resultEditor.annotations.*;
import resultEditor.relationship.iListable;
import resultEditor.selectedAnnotationView.iListCellRenderer;
import resultEditor.selectedAnnotationView.iListEntry;
import resultEditor.workSpace.WorkSet;
import userInterface.GUI;
import userInterface.ListCellRenderer_of_Relationship;
import userInterface.ListEntryofRelationship;

/**
 *
 * @author leng
 */
public class Comparator extends javax.swing.JPanel {

    protected userInterface.GUI gui;
    protected userInterface.annotationCompare.ExpandButton controlButton;
    protected JTextPane textPaneforClinicalNotes;
    private Icon icon_span;

    /** Creates new form Comparator */
    public Comparator(userInterface.GUI gui, 
            userInterface.annotationCompare.ExpandButton controlButton,
            JTextPane textPaneforClinicalNotes) {
        
        STANDARD_TEXTFIELD_BOARD = (new  javax.swing.JTextField()).getBorder();
                
        this.gui = gui;
        this.controlButton = controlButton;
        initComponents();
        //jButton3.setVisible( false );
        loadRes();
        if(env.Parameters.CreateRelationship.where == 2)
            env.Parameters.CreateRelationship.where = 0;
        
        if( gui.reviewmode == GUI.ReviewMode.adjudicationMode )
            jButton4.setVisible( true );
        else
            jButton4.setVisible( false );
        
        jSplitPane5.setDividerLocation( gui.setEditorDividedLocation() );
        //if(GUI.REVIEWMODE == GUI.ReviewMode.adjudicationMode)
        //    jButton3.setVisible(false);

        

        // set cursor
        //jButton4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //jButton5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //jButton10.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //jButton11.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // hide some buttons
        jButton_SelectClass.setEnabled(false);
        jButton_SelectClass.setVisible(false);        
        //jButton_relationships1.setVisible(false);
        this.textPaneforClinicalNotes = textPaneforClinicalNotes;
    }


    /**get current selected annotation from the list of selected annotation. 
     * 
     * @return  the annotation that is current selected in the list 
     *          of "selected annotations.
     */
    public Annotation getSelectedAnnotation(){
        
        Annotation toReturn_annotation = null;

        resultEditor.selectedAnnotationView.iListEntry entry = null;
        try{
            Object selectedAnnotationObj;
            selectedAnnotationObj = jList_selectedAnnotations.getSelectedValue();
            if( selectedAnnotationObj != null){
                entry = (resultEditor.selectedAnnotationView.iListEntry) selectedAnnotationObj;
            }
        }catch(Exception ex){
            entry = null;
        }

        if( entry != null)
            toReturn_annotation = entry.getAnnotation();

        return toReturn_annotation;
    }
    
    
    /**load resources from files, such as pictures, icons, etc.*/
    private void loadRes(){
        try{
            icon_span = new javax.swing.ImageIcon(getClass().getResource("/res/span.jpeg"));
        }catch(Exception ex){
        }
    }

    /**Flags which used to show differences between 2 annotations,
     * one annotation is the primary annotaiton, the other one is
     * the annotation user just selected from the annotations list
     * on comparator panel;
     */
    private static class flags_of_differences{
        static boolean isDifference_inText = false;
        static boolean isDifference_inSpan = false;
        static boolean isDifference_inClass = false;
        //static boolean isDifference_inComment = false;
        static boolean isDifference_inAnnotator = false;
        static boolean isDifference_inNormalRelationship = false;
        static boolean isDifference_inComplexRelationship = false;

        /**set all flags to false: means there is no any difference
         * between these two annotations.
         */
        public static void setAlltoFalse(){
            isDifference_inText = false;
            isDifference_inSpan = false;
            isDifference_inClass = false;
            //isDifference_inComment = false;
            isDifference_inAnnotator = false;
            isDifference_inNormalRelationship = false;
            isDifference_inComplexRelationship = false;
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

        jPanel5 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser5 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanel94 = new javax.swing.JPanel();
        jToolBar_editopanel_comparison = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jToolBar5 = new javax.swing.JToolBar();
        jToolBar4 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton_spaneditor_lefttToLeft = new javax.swing.JButton();
        jButton_spaneditor_leftToRight = new javax.swing.JButton();
        jButton_span_rightToLeft = new javax.swing.JButton();
        jButton4_spanEditor_rightToRight = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();
        jPanel_multipleResultShowList1 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList_selectedAnnotations = new javax.swing.JList();
        jPanel49 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel51 = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel_annotation_details1 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane_Spans = new javax.swing.JScrollPane();
        jList_Spans = new javax.swing.JList();
        jPanel19 = new javax.swing.JPanel();
        jPanel67 = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jTextField_annotationClassnames = new javax.swing.JTextField();
        jButton_SelectClass = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jPanel78 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea_comment1 = new javax.swing.JTextArea();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel48 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jButton_cr = new javax.swing.JToggleButton();
        delete_Relationships = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList_complexrelationships = new javax.swing.JList();
        jPanel71 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList_attributes = new javax.swing.JList();
        jPanel63 = new javax.swing.JPanel();
        jButton_relationships1 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField_creationdate = new javax.swing.JTextField();
        jTextField_annotator = new javax.swing.JTextField();
        jPanel87 = new javax.swing.JPanel();
        jPanel84 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser4 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser7 = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(257, 285));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel43.setBackground(new java.awt.Color(236, 233, 216));
        jPanel43.setPreferredSize(new java.awt.Dimension(390, 180));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jPanel_colorfulTextBar_filebrowser5.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser5.setLayout(new java.awt.BorderLayout());

        jLabel28.setBackground(new java.awt.Color(41, 119, 167));
        jLabel28.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(230, 230, 230));
        jLabel28.setText(" Comparator");
        jLabel28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel28.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel28.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel28.setRequestFocusEnabled(false);
        jLabel28.setVerifyInputWhenFocusTarget(false);
        jPanel_colorfulTextBar_filebrowser5.add(jLabel28, java.awt.BorderLayout.NORTH);

        jPanel94.setLayout(new java.awt.BorderLayout());

        jToolBar_editopanel_comparison.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar_editopanel_comparison.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar_editopanel_comparison.setFloatable(false);
        jToolBar_editopanel_comparison.setBorderPainted(false);
        jToolBar_editopanel_comparison.setFocusable(false);
        jToolBar_editopanel_comparison.setFont(new java.awt.Font("Bangla MN", 0, 13)); // NOI18N
        jToolBar_editopanel_comparison.setRequestFocusEnabled(false);
        jToolBar_editopanel_comparison.setVerifyInputWhenFocusTarget(false);

        jButton1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton1.setText("Accept");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(52, 24));
        jButton1.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton1.setPreferredSize(new java.awt.Dimension(52, 24));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton1);

        jButton2.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton2.setText("Reject");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(52, 24));
        jButton2.setMinimumSize(new java.awt.Dimension(0, 24));
        jButton2.setPreferredSize(new java.awt.Dimension(52, 24));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton2);

        jButton4.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton4.setText("AcceptAll");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setMaximumSize(new java.awt.Dimension(70, 24));
        jButton4.setMinimumSize(new java.awt.Dimension(0, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(52, 24));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton4);

        jPanel94.add(jToolBar_editopanel_comparison, java.awt.BorderLayout.SOUTH);

        jPanel46.setPreferredSize(new java.awt.Dimension(0, 26));
        jPanel46.setLayout(new java.awt.BorderLayout());

        jToolBar5.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar5.setBorderPainted(false);
        jToolBar5.setFocusable(false);
        jToolBar5.setMaximumSize(new java.awt.Dimension(2, 25));
        jToolBar5.setPreferredSize(new java.awt.Dimension(2, 25));
        jToolBar5.setRequestFocusEnabled(false);
        jToolBar5.setVerifyInputWhenFocusTarget(false);

        jToolBar4.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar4.setFloatable(false);
        jToolBar4.setBorderPainted(false);
        jToolBar4.setFocusable(false);
        jToolBar4.setRequestFocusEnabled(false);
        jToolBar4.setVerifyInputWhenFocusTarget(false);
        jToolBar4.add(jSeparator2);

        jButton_spaneditor_lefttToLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span_ll.png"))); // NOI18N
        jButton_spaneditor_lefttToLeft.setToolTipText("<html>Move the start of the annotation<br>One character to the left.<br>Hotkey(Ctrl + Left Arrow)</html>");
        jButton_spaneditor_lefttToLeft.setBorderPainted(false);
        jButton_spaneditor_lefttToLeft.setContentAreaFilled(false);
        jButton_spaneditor_lefttToLeft.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_lefttToLeft.setMinimumSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_lefttToLeft.setPreferredSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_lefttToLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_spaneditor_lefttToLeftActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton_spaneditor_lefttToLeft);

        jButton_spaneditor_leftToRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span_lr.png"))); // NOI18N
        jButton_spaneditor_leftToRight.setToolTipText("<html>Move the start of the annotation<br>One character to the right.<br>Hotkey(Ctrl + Right Arrow)</html>");
        jButton_spaneditor_leftToRight.setBorderPainted(false);
        jButton_spaneditor_leftToRight.setContentAreaFilled(false);
        jButton_spaneditor_leftToRight.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_leftToRight.setMinimumSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_leftToRight.setPreferredSize(new java.awt.Dimension(28, 23));
        jButton_spaneditor_leftToRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_spaneditor_leftToRightActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton_spaneditor_leftToRight);

        jButton_span_rightToLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span_rl.png"))); // NOI18N
        jButton_span_rightToLeft.setToolTipText("<html>Move the end of the annotation<br>One character to the left.</html>");
        jButton_span_rightToLeft.setBorderPainted(false);
        jButton_span_rightToLeft.setContentAreaFilled(false);
        jButton_span_rightToLeft.setFocusable(false);
        jButton_span_rightToLeft.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_span_rightToLeft.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton_span_rightToLeft.setMinimumSize(new java.awt.Dimension(28, 23));
        jButton_span_rightToLeft.setPreferredSize(new java.awt.Dimension(28, 23));
        jButton_span_rightToLeft.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_span_rightToLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_span_rightToLeftActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton_span_rightToLeft);

        jButton4_spanEditor_rightToRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span_rr.png"))); // NOI18N
        jButton4_spanEditor_rightToRight.setToolTipText("<html>Move the end of the annotation<br>One character to the right.<br>Hotkey(Alt + Right Arrow)</html>");
        jButton4_spanEditor_rightToRight.setBorderPainted(false);
        jButton4_spanEditor_rightToRight.setContentAreaFilled(false);
        jButton4_spanEditor_rightToRight.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton4_spanEditor_rightToRight.setMinimumSize(new java.awt.Dimension(28, 23));
        jButton4_spanEditor_rightToRight.setPreferredSize(new java.awt.Dimension(28, 23));
        jButton4_spanEditor_rightToRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4_spanEditor_rightToRightActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton4_spanEditor_rightToRight);

        jToolBar5.add(jToolBar4);

        jPanel46.add(jToolBar5, java.awt.BorderLayout.CENTER);

        jPanel94.add(jPanel46, java.awt.BorderLayout.NORTH);

        jPanel_colorfulTextBar_filebrowser5.add(jPanel94, java.awt.BorderLayout.CENTER);

        jPanel43.add(jPanel_colorfulTextBar_filebrowser5, java.awt.BorderLayout.NORTH);

        jPanel47.setBackground(new java.awt.Color(237, 237, 237));
        jPanel47.setMaximumSize(new java.awt.Dimension(32767, 280));
        jPanel47.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jPanel_multipleResultShowList1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 3));
        jPanel_multipleResultShowList1.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel_multipleResultShowList1.setLayout(new java.awt.BorderLayout());

        jScrollPane6.setBorder(null);
        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane6.setMinimumSize(null);

        jList_selectedAnnotations.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jList_selectedAnnotations.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_selectedAnnotations.setSelectionBackground(new java.awt.Color(74, 136, 218));
        jList_selectedAnnotations.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_selectedAnnotationsValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jList_selectedAnnotations);

        jPanel_multipleResultShowList1.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel49.setBackground(new java.awt.Color(237, 237, 237));

        jLabel13.setBackground(new java.awt.Color(236, 233, 216));
        jLabel13.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setText("Alternative Annotations:");
        jLabel13.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel13.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel13.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel13.setPreferredSize(new java.awt.Dimension(0, 16));
        jLabel13.setRequestFocusEnabled(false);
        jLabel13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        org.jdesktop.layout.GroupLayout jPanel49Layout = new org.jdesktop.layout.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel49Layout.createSequentialGroup()
                .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jPanel_multipleResultShowList1.add(jPanel49, java.awt.BorderLayout.NORTH);

        jPanel51.setBackground(new java.awt.Color(237, 237, 237));
        jPanel51.setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        jPanel77.setLayout(new java.awt.BorderLayout());
        jPanel51.add(jPanel77);

        jPanel_multipleResultShowList1.add(jPanel51, java.awt.BorderLayout.SOUTH);

        jPanel47.add(jPanel_multipleResultShowList1, java.awt.BorderLayout.CENTER);

        jPanel43.add(jPanel47, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(0, 102, 204));
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 1));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 1));

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 460, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel43.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.add(jPanel43, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(237, 237, 237));
        jPanel7.setLayout(new java.awt.GridLayout(1, 2, 3, 0));

        jPanel_annotation_details1.setBackground(new java.awt.Color(240, 240, 240));
        jPanel_annotation_details1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel_annotation_details1.setLayout(new java.awt.BorderLayout());

        jLabel19.setBackground(new java.awt.Color(236, 233, 216));
        jLabel19.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Span:");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel19.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 1, 1, 1, new java.awt.Color(238, 238, 240)));
        jLabel19.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel_annotation_details1.add(jLabel19, java.awt.BorderLayout.NORTH);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(238, 238, 240));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(238, 238, 240));
        jPanel23.setLayout(new java.awt.GridLayout(2, 1, 0, 2));
        jPanel21.add(jPanel23, java.awt.BorderLayout.NORTH);

        jPanel20.add(jPanel21, java.awt.BorderLayout.EAST);

        jScrollPane_Spans.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jList_Spans.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jScrollPane_Spans.setViewportView(jList_Spans);

        jPanel20.add(jScrollPane_Spans, java.awt.BorderLayout.CENTER);

        jPanel_annotation_details1.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel_annotation_details1);

        jPanel19.setBackground(new java.awt.Color(240, 240, 240));
        jPanel19.setLayout(new java.awt.BorderLayout());

        org.jdesktop.layout.GroupLayout jPanel67Layout = new org.jdesktop.layout.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 228, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        jPanel19.add(jPanel67, java.awt.BorderLayout.NORTH);

        jPanel73.setBackground(new java.awt.Color(237, 237, 237));
        jPanel73.setLayout(new java.awt.BorderLayout());

        jPanel56.setBackground(new java.awt.Color(237, 237, 237));
        jPanel56.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        jPanel56.setPreferredSize(new java.awt.Dimension(0, 72));
        jPanel56.setLayout(new java.awt.BorderLayout());

        jPanel52.setBackground(new java.awt.Color(237, 237, 237));
        jPanel52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel52.setMaximumSize(new java.awt.Dimension(32767, 112));
        jPanel52.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel52.setPreferredSize(new java.awt.Dimension(0, 67));
        jPanel52.setLayout(new java.awt.GridLayout(3, 0, 0, 2));

        jLabel18.setBackground(new java.awt.Color(236, 233, 216));
        jLabel18.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Class:");
        jLabel18.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel18.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel18.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel52.add(jLabel18);

        jPanel53.setBackground(new java.awt.Color(237, 237, 237));
        jPanel53.setLayout(new java.awt.BorderLayout());

        jTextField_annotationClassnames.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextField_annotationClassnames.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jTextField_annotationClassnames.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextField_annotationClassnames.setEnabled(false);
        jPanel53.add(jTextField_annotationClassnames, java.awt.BorderLayout.CENTER);

        jButton_SelectClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/instancewatch.png"))); // NOI18N
        jButton_SelectClass.setToolTipText("Change category of current annotation.");
        jButton_SelectClass.setEnabled(false);
        jButton_SelectClass.setFocusable(false);
        jButton_SelectClass.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_SelectClass.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_SelectClass.setPreferredSize(new java.awt.Dimension(20, 20));
        jPanel53.add(jButton_SelectClass, java.awt.BorderLayout.EAST);

        jPanel52.add(jPanel53);

        jLabel42.setBackground(new java.awt.Color(236, 233, 216));
        jLabel42.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(102, 102, 102));
        jLabel42.setText("Annotation Comment:");
        jLabel42.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel42.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel42.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel42.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel52.add(jLabel42);

        jPanel56.add(jPanel52, java.awt.BorderLayout.CENTER);

        jPanel73.add(jPanel56, java.awt.BorderLayout.NORTH);

        jPanel78.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel78.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBorder(null);
        jScrollPane3.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea_comment1.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea_comment1.setColumns(20);
        jTextArea_comment1.setEditable(false);
        jTextArea_comment1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jTextArea_comment1.setLineWrap(true);
        jTextArea_comment1.setRows(5);
        jTextArea_comment1.setBorder(null);
        jTextArea_comment1.setDisabledTextColor(new java.awt.Color(0, 1, 0));
        jScrollPane3.setViewportView(jTextArea_comment1);

        jPanel78.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel73.add(jPanel78, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel73, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel19);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        add(jPanel5, java.awt.BorderLayout.NORTH);

        jSplitPane5.setBackground(new java.awt.Color(237, 237, 237));
        jSplitPane5.setDividerLocation(300);
        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane5.setResizeWeight(0.5);
        jSplitPane5.setMinimumSize(new java.awt.Dimension(0, 0));

        jPanel48.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel48.setPreferredSize(new java.awt.Dimension(439, 200));
        jPanel48.setLayout(new java.awt.GridLayout(2, 0));

        jPanel70.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel70.setLayout(new java.awt.BorderLayout());

        jPanel61.setBackground(new java.awt.Color(237, 237, 237));
        jPanel61.setLayout(new java.awt.BorderLayout());

        jLabel39.setBackground(new java.awt.Color(237, 237, 237));
        jLabel39.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Relationships:");
        jLabel39.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel39.setFocusable(false);
        jLabel39.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel39.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel61.add(jLabel39, java.awt.BorderLayout.CENTER);

        jPanel22.setBackground(new java.awt.Color(238, 238, 239));
        jPanel22.setLayout(new java.awt.GridLayout(1, 2, 1, 0));

        jButton_cr.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jButton_cr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/addtab.png"))); // NOI18N
        jButton_cr.setToolTipText("Enable relationship building");
        jButton_cr.setPreferredSize(new java.awt.Dimension(21, 21));
        jButton_cr.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/res/note.jpeg"))); // NOI18N
        jButton_cr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_crActionPerformed(evt);
            }
        });
        jPanel22.add(jButton_cr);

        delete_Relationships.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/closetab.png"))); // NOI18N
        delete_Relationships.setToolTipText("Delete selected relationship");
        delete_Relationships.setMargin(null);
        delete_Relationships.setMaximumSize(new java.awt.Dimension(23, 23));
        delete_Relationships.setMinimumSize(new java.awt.Dimension(23, 23));
        delete_Relationships.setPreferredSize(new java.awt.Dimension(21, 21));
        delete_Relationships.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_RelationshipsActionPerformed(evt);
            }
        });
        jPanel22.add(delete_Relationships);

        jPanel61.add(jPanel22, java.awt.BorderLayout.EAST);

        jPanel70.add(jPanel61, java.awt.BorderLayout.NORTH);

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane8.setMinimumSize(new java.awt.Dimension(0, 0));

        jList_complexrelationships.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_complexrelationships.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_complexrelationshipsValueChanged(evt);
            }
        });
        jScrollPane8.setViewportView(jList_complexrelationships);

        jPanel70.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel48.add(jPanel70);

        jPanel71.setBackground(new java.awt.Color(237, 237, 237));
        jPanel71.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel71.setLayout(new java.awt.BorderLayout());

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane9.setMinimumSize(new java.awt.Dimension(0, 0));

        jList_attributes.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jScrollPane9.setViewportView(jList_attributes);

        jPanel71.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel63.setBackground(new java.awt.Color(237, 237, 237));
        jPanel63.setLayout(new java.awt.BorderLayout());

        jButton_relationships1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/instancewatch.png"))); // NOI18N
        jButton_relationships1.setFocusable(false);
        jButton_relationships1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_relationships1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_relationships1.setMinimumSize(new java.awt.Dimension(0, 0));
        jButton_relationships1.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton_relationships1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_relationships1ActionPerformed(evt);
            }
        });
        jPanel63.add(jButton_relationships1, java.awt.BorderLayout.EAST);

        jLabel40.setBackground(new java.awt.Color(237, 237, 237));
        jLabel40.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(102, 102, 102));
        jLabel40.setText("Attributes:");
        jLabel40.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel40.setFocusable(false);
        jLabel40.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel40.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel63.add(jLabel40, java.awt.BorderLayout.CENTER);

        jPanel71.add(jPanel63, java.awt.BorderLayout.NORTH);

        jPanel48.add(jPanel71);

        jSplitPane5.setLeftComponent(jPanel48);

        jPanel75.setLayout(new java.awt.BorderLayout());

        jPanel76.setBackground(new java.awt.Color(237, 237, 237));
        jPanel76.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel76.setLayout(new java.awt.GridLayout(2, 2, 2, 2));

        jLabel37.setBackground(new java.awt.Color(237, 237, 237));
        jLabel37.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Creation Date");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel37.setMaximumSize(new java.awt.Dimension(0, 16));
        jLabel37.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel76.add(jLabel37);

        jLabel33.setBackground(new java.awt.Color(236, 233, 216));
        jLabel33.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setText("Annotator");
        jLabel33.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel33.setMaximumSize(new java.awt.Dimension(0, 16));
        jLabel33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel76.add(jLabel33);

        jTextField_creationdate.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextField_creationdate.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextField_creationdate.setMaximumSize(new java.awt.Dimension(0, 16));
        jTextField_creationdate.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel76.add(jTextField_creationdate);

        jTextField_annotator.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextField_annotator.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextField_annotator.setMaximumSize(new java.awt.Dimension(0, 16));
        jTextField_annotator.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel76.add(jTextField_annotator);

        jPanel75.add(jPanel76, java.awt.BorderLayout.NORTH);

        jPanel87.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel87.setLayout(new java.awt.BorderLayout());

        jPanel84.setBackground(new java.awt.Color(238, 238, 237));
        jPanel84.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel84.setLayout(new java.awt.GridLayout(1, 2, 2, 0));

        jPanel68.setBackground(new java.awt.Color(237, 237, 237));
        jPanel68.setLayout(new java.awt.BorderLayout());

        jPanel_colorfulTextBar_filebrowser4.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser4.setLayout(new java.awt.BorderLayout());
        jPanel68.add(jPanel_colorfulTextBar_filebrowser4, java.awt.BorderLayout.NORTH);

        jPanel69.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel69.setLayout(new java.awt.GridLayout(1, 0, 4, 4));
        jPanel68.add(jPanel69, java.awt.BorderLayout.SOUTH);

        jPanel84.add(jPanel68);

        jPanel87.add(jPanel84, java.awt.BorderLayout.CENTER);

        jPanel_colorfulTextBar_filebrowser7.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser7.setLayout(new java.awt.BorderLayout());
        jPanel87.add(jPanel_colorfulTextBar_filebrowser7, java.awt.BorderLayout.NORTH);

        jPanel75.add(jPanel87, java.awt.BorderLayout.CENTER);

        jSplitPane5.setRightComponent(jPanel75);

        add(jSplitPane5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    /**In mode of comparing difference between annotations, all annotations,
     * except the primary annotation (Depot.SelectedAnnotationSet
     * .ui_objectAnnotation), need to be showed in the comparator panel 
     * for comparing. 
     */
    public void display_showAlternativeAnnotations(){

        // ##1## empty the list
        clearlist(this.jList_selectedAnnotations);
        
        int uniqueid_of_primiaryAnnotation
                = Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor;
        //if (uniqueid_of_primiaryAnnotation <0)
        //    return;

        // this is a flag that indicates the first annotation in the
        // list of annotations on the comparator panel.
        boolean isFirstAnnotation = false;
        
        try{
            

            // ##2## load annotations into this list
            
            // get stored selected annotaions
            Vector<Integer> uqids = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if ((uqids==null)||(uqids.size()<1))
                return;
            
            String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();


            // ##2## Get indexes of selected annotations to this mouse position in
            // current text source
            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return;

            Depot depot = new Depot();

            // assemble vector v for show return results in list
            Vector<Object> listentry = new Vector<Object>();
            int size = selectedAnnotationIndexes.size();


            for( int i=0; i<size; i++ ) {

                int this_uniqueindex = selectedAnnotationIndexes.get(i);

                
                resultEditor.annotations.Annotation annotation = null;
                if ( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                    annotation = depot.getAnnotationByUnique( textsourcefilename, this_uniqueindex );
                else{
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    annotation = depotOfAdj.getAnnotationByUnique( textsourcefilename, this_uniqueindex );
                }
                    
                if(annotation == null || !annotation.visible)
                    continue;

                // ignore the primiary annotation which already shoed on
                // editor panel
                if(annotation.uniqueIndex==uniqueid_of_primiaryAnnotation)
                    continue;

                // record current annotation to indicator current annotation
                //ResultEditor.WorkSpace.WorkSet.currentAnnotation = annotation;
                // get term information from query results                

                Color categorycolor = resultEditor.annotationClasses.Depot.getColor( annotation.annotationclass );
                iListEntry le;
                le = new iListEntry( annotation , categorycolor, null );
                listentry.add( le );

                
                // Notice: most of time, we show the annotation(whose i euqal 0)
                //         but if the primiary annotation is on the first postiion
                //         of the vector, we may lost information if we still try
                //         to get the first annotation;
                if(!isFirstAnnotation){
                    display_showAnnotationDetails(annotation);
                    isFirstAnnotation = true;

                    // check differences between primary annotation and this
                    // annotation
                    checkDifferences(annotation);
                    display_setRedRectangle_toMarkDifferences();
                    gui.display_setRedRectangle_toMarkDifferences(
                            flags_of_differences.isDifference_inSpan,
                            flags_of_differences.isDifference_inClass,
                            //flags_of_differences.isDifference_inComment,
                            flags_of_differences.isDifference_inAnnotator,
                            flags_of_differences.isDifference_inComplexRelationship,
                            flags_of_differences.isDifference_inNormalRelationship
                            );
                    
                }
            }


            // show data of selected annotations on list and set selected item
            this.jList_selectedAnnotations.setListData(listentry);
            this.jList_selectedAnnotations.setListData( listentry );
            this.jList_selectedAnnotations.setCellRenderer(new iListCellRenderer());

            this.jList_selectedAnnotations.setSelectedIndex(0);


        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011161349::fail to show annotations in " +
                    "comporator panel!!!");
        }
    }


    /**To a given annotation, show its details in correlated field
     * on this jForm.
     *
     * @param   annotation
     *          annotation whose details will be displayed on current
     *          jForm;
     */
    private void display_showAnnotationDetails(Annotation annotation){
        // ##1## empty all fields
        dispaly_emptyAllAnnotationFileds();

        // ##2## load details to field for current annotation

        if ( annotation == null )
            return;

        try{

            clearSpans(); // remove all spans before displaying new annotation details
            jTextField_annotator.setText(null);


            String annotator = annotation.getFullAnnotator();
            // list spans
            this.listSpans(jList_Spans, annotation);
            
            jTextField_annotationClassnames.setText( annotation.annotationclass );

            // annotator of annotation
            if( (annotator != null )&&( annotator.trim().length() > 0) ){
                
                if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){        
                    if( annotation.adjudicationStatus == AdjudicationStatus.MATCHES_OK ){
                        annotator = "ADJUDICATION";
                    }
                }
                jTextField_annotator.setText( annotator );
            }else
                jTextField_annotator.setText(null);
        
            // create date
            if( (annotation.creationDate != null )&&( annotation.creationDate.trim().length() > 0) )
                jTextField_creationdate.setText( annotation.creationDate);
            else
                jTextField_creationdate.setText( null );

            jTextArea_comment1.setText(annotation.comments);

            // textsourceFilename of current text source
            //String textsourceFilename = ResultEditor.WorkSpace.WorkSet.getCurrentFile().getName();

            // get selected annotations by mouse's positin
//            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
//            Vector<Integer> selectedAnnotationUniqueindexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();

//            int size = selectedAnnotationUniqueindexes.size();
//            String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011161459:: fail to show annotation" +
                    " details in comparator panel" + ex.toString());
        }
    }

    /**empty the span list*/
    private void clearSpans(){
        jList_Spans.setListData( new Vector());
    }

    /**list spans of current annotation in the list of span*/
    private void listSpans(JList list, Annotation ann){
        // empty current list
        list.setListData( new Vector() );

        try{

            if( ann == null )
                return;
            if( ann.spanset == null )
                return;
            if( ann.spanset.size()<1 )
                return;

            Vector<userInterface.structure.SpanObj> entries =
                    new Vector<userInterface.structure.SpanObj>();

            for( int i=0;i<ann.spanset.size(); i++){
                SpanDef span = ann.spanset.getSpanAt(i);
                if( span == null )
                    continue;

                span.text = "";//getText(span.start, span.end);

                userInterface.structure.SpanObj spanObj =
                        new userInterface.structure.SpanObj(
                        ann,
                        span,
                        icon_span
                        );
                entries.add( spanObj );
            }

            list.setListData(entries);
            list.setCellRenderer( new userInterface.structure.SpanRenderer() );
            list.setSelectedIndex(0);

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1101211147:: fail to list corpus on screen"
                    + ex.getMessage());
        }
    }

    /**There are a lot of field on this form are used to display details of
     * designated annotation. This function can empty all contents on them.
     */
    private void dispaly_emptyAllAnnotationFileds(){
        // earse all existing data
        clearSpans(); 
        jTextField_annotationClassnames.setText(null);
        jTextArea_comment1.setText(null);
        clearlist(jList_complexrelationships);
        clearlist(jList_attributes);
        jTextField_creationdate.setText(null);
        jTextField_annotator.setText(null);
        
    }

    /**Empty a designated jlist.
     *
     * @param   thislist
     *          the jList you want to empty;
     */
    private void clearlist( JList thislist){
        Vector object = new Vector();
        object.clear();
        thislist.setListData(object);
    }

    

    /**while user clicked button of "accept" on comparator panel, it means we
     * will only keep this selected annotation in current stored annotation
     * set and show it as usual.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // user click accept button on comparator panel
        int selected = jList_selectedAnnotations.getSelectedIndex();
        if (selected < 0) return;
        if( jList_selectedAnnotations.getModel().getSize() < 1 )
            return;
        // ##2## transform this sequence index to unique index
        int uqid = Depot.SelectedAnnotationSet.getAnnotation_bySequenceIndex_ofComparator(selected);
        if (uqid<0)
            return;

        // user click accept button on comparator panel
        controlButton.acceptreject_acceptAlternativeAnnotation(uqid);
        
        adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter( null, WorkSet.getCurrentFile().getName(), this.gui);
        diffcounter.accepted();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try{
            int selected = jList_selectedAnnotations.getSelectedIndex();

            if (selected < 0) return;
            if( jList_selectedAnnotations.getModel().getSize() < 1 )
            return;
            // ##2## transform this sequence index to unique index
            int uqid = Depot.SelectedAnnotationSet.getAnnotation_bySequenceIndex_ofComparator(selected);
            if (uqid<0)
                return;

            // user click accept button on comparator panel
            controlButton.acceptreject_rejectAlternativeAnnotation(uqid);
            
            adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter( null, WorkSet.getCurrentFile().getName(), this.gui);
            diffcounter.accepted();
        
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1011171423:: fail to reject annotation " +
                    "in comparator panel!!!" + ex.toString());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jList_selectedAnnotationsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_selectedAnnotationsValueChanged
                
        // ####
        int selected = jList_selectedAnnotations.getSelectedIndex();
        if (selected < 0) {
            jList_Spans.setListData( new Vector() );
            return;
        }        

        display_showAnnotationDetails_bySequenceInList(selected);

        
        
        

    }//GEN-LAST:event_jList_selectedAnnotationsValueChanged

    private void jButton_crActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_crActionPerformed
        // jButton10.setSelected(set);
        
        WorkSet.makingRelationships = jButton_cr.isSelected();
        
        // if user clicked and enabled this button, it means we will use 
        // annotation 
        if( jButton_cr.isSelected() ){
           env.Parameters.CreateRelationship.where = 2;
           gui.cr_disableButton();
        }else 
            env.Parameters.CreateRelationship.where = 0;
        
        if(!jButton_cr.isSelected())
        {
            jButton_cr.setToolTipText("Enable relationship building");
            gui.stopRelationshiping();
        }
        else
        {
            jButton_cr.setToolTipText("Disable relationship building");            
        }
        
        
        if(this.jList_complexrelationships.getSelectedIndex() >= 0)
        {                        
            delete_Relationships.setEnabled(true);
        }
        else
        {            
            delete_Relationships.setEnabled(false);
        }
   }//GEN-LAST:event_jButton_crActionPerformed

    public void cr_disableButton(){
        jButton_cr.setSelected( false );
    }
            
    private void delete_RelationshipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_RelationshipsActionPerformed
      try{
          Annotation annotaiton = this.getSelectedAnnotation();
          if( annotaiton == null )
              return;

          int selectedRel = jList_complexrelationships.getSelectedIndex();
          if(selectedRel<0)
              return;
  
                
                annotaiton.relationships.removeElementAt(  selectedRel );
  
            display_listAttributes( annotaiton );
            gui.setModified();            
            gui.display_RelationshipPath_Remove();                       
            recheckDifference();
            
        }catch(Exception ex){
            System.out.println("ERROR 1203010425::");
        }
    }//GEN-LAST:event_delete_RelationshipsActionPerformed

    private void jList_complexrelationshipsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_complexrelationshipsValueChanged
        if(this.jList_complexrelationships.getSelectedIndex() >= 0)
        {                        
            delete_Relationships.setEnabled(true);
        }
        else
        {            
            delete_Relationships.setEnabled(false);
        }
    }//GEN-LAST:event_jList_complexrelationshipsValueChanged

    private void jButton_relationships1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_relationships1ActionPerformed
        openAttributeEditor();
    }//GEN-LAST:event_jButton_relationships1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        this.gui.acceptAll();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton_spaneditor_lefttToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_spaneditor_lefttToLeftActionPerformed
        // TODO add your process code here:
        spanEdit( spanedittype.headExtendtoLeft);
    }//GEN-LAST:event_jButton_spaneditor_lefttToLeftActionPerformed

    private void jButton_spaneditor_leftToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_spaneditor_leftToRightActionPerformed
        spanEdit( spanedittype.headShortentoRight);
    }//GEN-LAST:event_jButton_spaneditor_leftToRightActionPerformed

    private void jButton_span_rightToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_span_rightToLeftActionPerformed
        //((userInterface.annotationCompare.ExpandButton) jPanel60).acceptreject_ignorePrimaryAnnotation();
        spanEdit(spanedittype.tailshortentoLeft);
    }//GEN-LAST:event_jButton_span_rightToLeftActionPerformed

    private void jButton4_spanEditor_rightToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_spanEditor_rightToRightActionPerformed
        // TODO add your process code here:
        spanEdit(spanedittype.tailExtendtoRight);
    }//GEN-LAST:event_jButton4_spanEditor_rightToRightActionPerformed

     /**A dialog that we used to modify the attribute of relationships. */
    private resultEditor.relationship.attributes.Editor editor_attributeOnRelationship;
    
    /**Popup the dialog of the attribute editor for current annotation. If it's
     * already set to visible, bring it into the front of the GUI, so user can
     * see it. 
     * 
     */
    private void openRelAttributeEditor()
    {
        // quit if we didn't allocate an annotation for this attribute editor.
        if ( this.getSelectedAnnotation()== null)        
            return;
        
        // get current selected relationship of current annotation
        AnnotationRelationship relationship = getSelectedRelationship();
        if( relationship == null ){
            return;
        }
               
       
        // get attributes
        Vector<AnnotationAttributeDef> attributes = relationship.attributes;
        
        if(editor_attributeOnRelationship == null || !editor_attributeOnRelationship.isVisible()) {
            editor_attributeOnRelationship = new resultEditor.relationship.attributes.Editor(
                    this.getSelectedAnnotation(),
                    relationship,
                    this.gui,
                    this    
                    );
            editor_attributeOnRelationship.setVisible(true);
        }else{
            editor_attributeOnRelationship.setVisible(true);
            // editor.set
        }
        
    }
    
    /**get the selected relationship from the list if relationship on the editor panel.*/
    private AnnotationRelationship getSelectedRelationship(){
        int index = jList_complexrelationships.getSelectedIndex();
        if( index < 0 )
            return null;
        
        Object selectedObj = jList_complexrelationships.getSelectedValue();
        if( selectedObj == null )
            return null;
        if( !( selectedObj instanceof ListEntryofRelationship ))
            return null;
        
        ListEntryofRelationship rel = (ListEntryofRelationship) selectedObj;
        return rel.getRel();
    }
    
    /** this path depend on operation system type, and it will be set to
     * correct value in output package */
    private resultEditor.relationship.Editor editor = null;
    
    /**Popup the dialog of the attribute editor for current annotation. If it's
     * already set to visible, bring it into the front of the GUI, so user can
     * see it. 
     */
    private void openAttributeEditor()
    {                
        // get current selected annotation fromt the comparator panel
        Annotation annotation = this.getSelectedAnnotation();
        if( annotation == null )
            return;
        
        // prepare the list that will be listed on the editor. It contains the
        // attribute names and their values.
        Vector<iListable> finalList = annotation.getAttributesForShow();

        // if this dialog hasn't been inited yet, we need to create a new one.
        if(editor == null || !editor.isVisible()) {
            editor = new resultEditor.relationship.Editor( 
                    gui,       
                    annotation,
                    finalList,  // the attributes entries for this kind of
                                // annotation.
                                // It includes attribtues and allowed values.
                    resultEditor.relationship.Editor.Type.Attributes, 
                                // tell the dialog that this time it's used
                                // for the 
                    resultEditor.relationship.Editor.Where.ComparatorPanel
                                // Tell this method that where the request is sent 
                                // from, whether from the annotation editor panel 
                                // or the comparator panel.
                    );
            editor.setVisible(true);
        }else{
            editor.setVisible(true);
            // editor.set
        }
    }
    
    /**The */
    public enum spanedittype {
        headExtendtoLeft, 
        headShortentoRight,
        tailExtendtoRight,
        tailshortentoLeft, 
        delete
    };

    
    /**update information about current selected annotation, update their spans.
     * attributes, and relationship on screen.*/
    public void updateAnnotation(){
        Annotation annotaion = this.getSelectedAnnotation();
        
        this.display_showAnnotationDetails( annotaion );

            display_listAttributes(annotaion);

            // #### check for differences between this annotation and primary
            // annotation
            checkDifferences(annotaion);
            display_setRedRectangle_toMarkDifferences();
            gui.display_setRedRectangle_toMarkDifferences(
                            flags_of_differences.isDifference_inSpan,
                            flags_of_differences.isDifference_inClass,
                            //flags_of_differences.isDifference_inComment,
                            flags_of_differences.isDifference_inAnnotator,
                            flags_of_differences.isDifference_inComplexRelationship,
                            flags_of_differences.isDifference_inNormalRelationship
                            );
    }
    
    
    
    
    public void recheckDifference(){
        Annotation annotaion = this.getSelectedAnnotation();
        
        

            // #### check for differences between this annotation and primary
            // annotation
            checkDifferences(annotaion);
            display_setRedRectangle_toMarkDifferences();
            gui.display_setRedRectangle_toMarkDifferences(
                            flags_of_differences.isDifference_inSpan,
                            flags_of_differences.isDifference_inClass,
                            //flags_of_differences.isDifference_inComment,
                            flags_of_differences.isDifference_inAnnotator,
                            flags_of_differences.isDifference_inComplexRelationship,
                            flags_of_differences.isDifference_inNormalRelationship
                            );
    }
    
    /**If you clicked any item in the annotation list on comparator, let use
     * this sequence number of you clicked annotation to approach that
     * annotation, and then show annotation details on fields of
     * comparator panel.
     *
     * @param   sequenceinList
     *          the number counted from 0, it's the index of selected item in
     *          jlist.
     */
    private void display_showAnnotationDetails_bySequenceInList(int sequenceinList){
        try{
            // ##1## earse existing contents
            dispaly_emptyAllAnnotationFileds();

            // validity check
            int max = jList_selectedAnnotations.getModel().getSize();
            if ((sequenceinList <0)||(sequenceinList > max-1))
                return;

            // ##2## transform this sequence index to unique index
            int uqid = Depot.SelectedAnnotationSet.getAnnotation_bySequenceIndex_ofComparator(sequenceinList);
            if (uqid<0)
                return;

            Depot depot = new Depot();
            String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
            Annotation anno = null;
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                anno = depot.getAnnotationByUnique(filename, uqid);
            else{
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                anno = depotOfAdj.getAnnotationByUnique(filename, uqid);
            }
            this.display_showAnnotationDetails( anno );

            display_listAttributes(anno);

            // #### check for differences between this annotation and primary
            // annotation
            checkDifferences(anno);
            display_setRedRectangle_toMarkDifferences();
            gui.display_setRedRectangle_toMarkDifferences(
                            flags_of_differences.isDifference_inSpan,
                            flags_of_differences.isDifference_inClass,
                            //flags_of_differences.isDifference_inComment,
                            flags_of_differences.isDifference_inAnnotator,
                            flags_of_differences.isDifference_inComplexRelationship,
                            flags_of_differences.isDifference_inNormalRelationship
                            );
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1011151534::fail to show annotation " +
                    "details\n" + ex.toString());
        }
    }

    /**standard border of text field*/
    protected javax.swing.border.Border STANDARD_TEXTFIELD_BOARD;
    
    
    /**Use difference information stored in inner class "flags_of_differences"
     * to set the borders of corrlated components. Use red rectangles to indicate
     * difference.
     * 
     * This function only can be called after you have run function of 
     * "checkDifferences(Annotation)". 
     */
    private void display_setRedRectangle_toMarkDifferences(){
        
        // ##1## set boarder if annotation span difference found
        try{
            if (flags_of_differences.isDifference_inSpan){
                jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel20.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::1");}

        // ##3## set boarder if annotation span difference found
        try{
            if (flags_of_differences.isDifference_inClass){
                jTextField_annotationClassnames.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jTextField_annotationClassnames.setBorder(STANDARD_TEXTFIELD_BOARD);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::3");}

        /*// ##4## set boarder if  difference found in annotation comments
        try{
            if (flags_of_differences.isDifference_inComment){
                jPanel78.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel78.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::4");}

        */
        
        
        // ##5## set boarder if  difference found in annotator of annotations
        try{
            if (flags_of_differences.isDifference_inAnnotator){
                jTextField_annotator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::5");}


        // ##6## set boarder if  difference found in complex relationships of annotations
        try{
            if (flags_of_differences.isDifference_inComplexRelationship){
                jPanel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel70.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::6");}

        // ##7## set boarder if  difference found in normal relationships of annotations
        try{
            if (flags_of_differences.isDifference_inNormalRelationship){
                jPanel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel71.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011162346::7");}

    }


    /**Compare primary annotation to a given annotation, try to find 
     * differences. 
     * 
     * @param   annotation
     *          the given annotation for comparing.    
     */
    private void checkDifferences(Annotation annotation){
        try{
            // ##1## reset all flags of difference in inner class of <code>
            // flags_of_differences</code>
            flags_of_differences.setAlltoFalse();

            // ##2## validity check
            if(annotation==null)
                return;
            int uniqueindex_ofPrimaryAnnotation = Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor;
            if (uniqueindex_ofPrimaryAnnotation <0)
                return;
            Depot depot = new Depot();
            String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
            Annotation primaryAnnotation = null;
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                primaryAnnotation = depot.getAnnotationByUnique(filename, uniqueindex_ofPrimaryAnnotation);
            else{
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                primaryAnnotation = depotOfAdj.getAnnotationByUnique(filename, uniqueindex_ofPrimaryAnnotation);
            }
                
            if (primaryAnnotation==null)
                return;

            // ##3## compare 2 annotations
            // ##3.1## compare span
            try{
                if( ! primaryAnnotation.spanset.equals( annotation.spanset ) ){
                    flags_of_differences.isDifference_inSpan = true;
                }
            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::1");}

            // ##3.2## compare annotation text
            try{
                if( (annotation.annotationText == null )&&(primaryAnnotation.annotationText!=null))
                    flags_of_differences.isDifference_inText = true;
                else if( (primaryAnnotation.annotationText==null)&&(annotation.annotationText != null ))
                    flags_of_differences.isDifference_inText = true;
                else if( annotation.annotationText.compareTo(primaryAnnotation.annotationText)!=0  )
                    flags_of_differences.isDifference_inText = true;
                
            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::2");}

            // ##3.3## compare annotation class
            try{
                if( (annotation.annotationclass == null )&&(primaryAnnotation.annotationclass!=null))
                    flags_of_differences.isDifference_inClass = true;
                else if( (primaryAnnotation.annotationclass==null)&&(annotation.annotationclass != null ))
                    flags_of_differences.isDifference_inClass = true;
                else if( annotation.annotationclass.compareTo(primaryAnnotation.annotationclass)!=0  )
                    flags_of_differences.isDifference_inClass = true;

            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::3");}


            // ##3.4## compare annotation comment
            /*try{
                if( (annotation.comments == null )&&(primaryAnnotation.comments==null))
                    flags_of_differences.isDifference_inComment = false;
                else if( (annotation.comments == null ) && (primaryAnnotation.comments.trim().length()>0))
                    flags_of_differences.isDifference_inComment = true;
                else if( (primaryAnnotation.comments==null)&&(annotation.comments.trim().length()>0))
                    flags_of_differences.isDifference_inComment = true;
                else if((primaryAnnotation.comments!=null)&&(annotation.comments != null))
                {
                    if( annotation.comments.trim().compareTo(primaryAnnotation.comments.trim() )!=0  )
                        flags_of_differences.isDifference_inComment = true;
                }

            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::4");}
            */

            // ##3.5## compare annotator of annotations
            try{
                if( (annotation.getAnnotator() == null )&&(primaryAnnotation.getAnnotator() !=null))
                    flags_of_differences.isDifference_inAnnotator = true;
                else if( (primaryAnnotation.getAnnotator()==null)&&(annotation.getAnnotator() != null ))
                    flags_of_differences.isDifference_inAnnotator = true;
                else {
                    if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                        if(( primaryAnnotation.adjudicationStatus == AdjudicationStatus.MATCHES_OK ) && ( annotation.adjudicationStatus == AdjudicationStatus.MATCHES_OK ))
                            flags_of_differences.isDifference_inAnnotator = false ;
                    }else if( annotation.getAnnotator().compareTo(primaryAnnotation.getAnnotator())!=0  ) {
                        flags_of_differences.isDifference_inAnnotator = true; 
                    }
                }

            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::5");}

            // ##3.6## compare complex relationships of annotations
            try{
                
                if( report.iaaReport.analysis.detailsNonMatches.Comparator.equalRelationships(annotation, primaryAnnotation, WorkSet.getCurrentFile().getName()))
                    flags_of_differences.isDifference_inComplexRelationship = false;
                else
                    flags_of_differences.isDifference_inComplexRelationship = true;
              
            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::6");}


            // ##3.7## compare normal relationships of annotations
            try{
                if( report.iaaReport.analysis.detailsNonMatches.Comparator.equalAttributes(annotation, primaryAnnotation))
                    flags_of_differences.isDifference_inNormalRelationship = false;
                else
                    flags_of_differences.isDifference_inNormalRelationship = true;
              
                  
            }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE,"error 1011161704::7");}

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error ");
        }
    }

    
    /** display attribute information of current annotation*/
    public void display_listAttributes( final resultEditor.annotations.Annotation annotation ) {

        //####-1-   show normal relationships
        if ( annotation.attributes == null ){
            Vector empty = new Vector();
            jList_attributes.setListData(empty);
            //jLabel_typeOfRelationship.setText("Type of normalrelationships: ");
        } else {
            //jLabel_typeOfRelationship.setText("Type of normalrelationships: ");
            Vector<String> list = new Vector<String>();
            for( resultEditor.annotations.AnnotationAttributeDef normalrelationship : annotation.attributes ){
                if (normalrelationship == null) continue;
             
                String str = null;
                    if( normalrelationship.value != null ) {
                        str = " \"" + normalrelationship.name + "\" = " + normalrelationship.value;
                    } else {
                        str = " \"" + normalrelationship.name + "\"";
                    }
                    list.add(str);
                
            }
            jList_attributes.setListData( list );
        }


        //####-2-   show complex relationships        
        if ( annotation.relationships == null ){            
            jList_complexrelationships.setListData(new Vector());
        } else {
            //jLabel_typeOfRelationship.setText("Type of normalrelationships: ");
            Vector<ListEntryofRelationship> clist = new Vector<ListEntryofRelationship>();
            for( resultEditor.annotations.AnnotationRelationship relationship : annotation.relationships ){
                if (relationship == null) continue;
                clist.add(new ListEntryofRelationship( annotation, relationship));

            }
            jList_complexrelationships.setListData( clist );
            jList_complexrelationships.setCellRenderer(new ListCellRenderer_of_Relationship());
            jList_complexrelationships.setSelectedIndex(0);
            
            if(clist.size()<0)
                delete_Relationships.setEnabled( false );
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton delete_Relationships;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton4_spanEditor_rightToRight;
    private javax.swing.JButton jButton_SelectClass;
    private javax.swing.JToggleButton jButton_cr;
    private javax.swing.JButton jButton_relationships1;
    private javax.swing.JButton jButton_span_rightToLeft;
    private javax.swing.JButton jButton_spaneditor_leftToRight;
    private javax.swing.JButton jButton_spaneditor_lefttToLeft;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JList jList_Spans;
    private javax.swing.JList jList_attributes;
    private javax.swing.JList jList_complexrelationships;
    private javax.swing.JList jList_selectedAnnotations;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JPanel jPanel_annotation_details1;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser4;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser5;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser7;
    private javax.swing.JPanel jPanel_multipleResultShowList1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPane_Spans;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTextArea jTextArea_comment1;
    private javax.swing.JTextField jTextField_annotationClassnames;
    private javax.swing.JTextField jTextField_annotator;
    private javax.swing.JTextField jTextField_creationdate;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar_editopanel_comparison;
    // End of variables declaration//GEN-END:variables

    /**Modify a specific span. */
    /**Modify a specific span. */
    public void spanEdit(spanedittype _type)
    {
        // remember which span is selected in the span list
        int selectedSpanIndex = this.jList_Spans.getSelectedIndex();
        int selectedAnnIndex = this.jList_selectedAnnotations.getSelectedIndex();
        if(selectedAnnIndex<0){
            if( jList_selectedAnnotations.getModel().getSize() > 0 )
                selectedAnnIndex = 0;
        }
        if(( _type != spanedittype.delete )&&( selectedSpanIndex < 0 ))
            return;
        
        // get the unique index of the selected annotation from the comparator panel
        int uqid = -1;
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
            int selected = jList_selectedAnnotations.getSelectedIndex();

            if (selected < 0) return;
            if( jList_selectedAnnotations.getModel().getSize() < 1 )
            return;
            // ##2## transform this sequence index to unique index
            uqid = Depot.SelectedAnnotationSet.getAnnotation_bySequenceIndex_ofComparator(selected);
            if (uqid<0)
                return;
        }
        
        
        gui.setFlag_allowToAddSpan(false); // cancel possible operation of adding new span
        gui.setModified();
        //boolean refreshScreen = false;

        // get current span and annotation
        Object[] selectedObjs = this.jList_Spans.getSelectedValues();
        if( selectedObjs == null )
            return;
        userInterface.structure.SpanObj spanobj = null;
        try{
            spanobj = (userInterface.structure.SpanObj) selectedObjs[0];
        }catch(Exception ex){
            spanobj= null;                                            
        }
        
         
        
        if(( _type != spanedittype.delete )&&(spanobj==null))
            return;
        
        
        Annotation annotation = null;
        SpanDef span = null; 
        
        // if delete annotation and non span are selected.
        
            
            annotation = spanobj.getAnnotation();
            
            span = spanobj.getSpan();
            if(span==null)
                span = annotation.spanset.getSpanAt(0);
        
        

        // get current file name
        resultEditor.spanEdit.SpanEditor spanEditor  =
                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
        
        // define indicators
        int delete = 0, headdecrease = 1, tailincrease = 2,
                headincrease = 3, taildecrease =4;

        switch(_type){

            case headExtendtoLeft:
                if(spanEditor.editCurrentDisplayedSpan(headdecrease)>0){
                    //refreshScreen = true;
                }
                break;
            case tailExtendtoRight:
                if(spanEditor.editCurrentDisplayedSpan(tailincrease)>0){
                    //refreshScreen = true;
                }
                break;
            case headShortentoRight:
                if(spanEditor.editCurrentDisplayedSpan(headincrease)>0){
                    //refreshScreen = true;
                }
                break;
            case tailshortentoLeft:
                spanEditor.editCurrentDisplayedSpan(taildecrease);                
                break;
        }

        int selectedIndex = this.jList_selectedAnnotations.getSelectedIndex();

        // update after mading modifications
        if (GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE) {
            try {
                //if (refreshScreen) {
                refresh(spanedittype.delete);
                //}
                
                // only need to relist items if it's just changing span range
                if (_type != spanedittype.delete) {
                    jButton_SelectClass.setEnabled(true);
                    // relist all annotations
                    // updateEditorScreen_afterSpanModified(selectedIndex, -1);
                } else {
                    // disable buttons if just deleted an annotations by the button
                    jButton_SelectClass.setEnabled(false);
                    gui.showAnnotationCategoriesInTreeView_refresh();
                    gui.showValidPositionIndicators();
                }

                // remember the latest selected span, so we can pick up and
                // highlight it again.
                if (_type != spanedittype.delete) {
                    this.jList_Spans.setSelectedIndex( selectedSpanIndex );
                }

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1012011438:: fail to refresh screen after "
                        + "span modified!!!");
            }

            //backtoPreviousFocusPoint();

        } else if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
            // if a span is changed in adjudication mode

            int uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get(0);
            Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;
            gui.display_RelationshipPath_Remove();
            // remove_all_underline_highlighter();
            
            
            
            // recheck difference and display on screen            
            this.recheckDifference();
            gui.display_RelationshipPath_Remove();
            gui.remove_all_underline_highlighter();
            
            
            
            // relist current annotation
            //gui.display_showOneAnnotation_inListOnEditorPanel(uniqueindex);  
            // reprint annotations on document viewer
            gui.display_repaintHighlighter();
            // update the navigation trees
            gui.showAnnotationCategoriesInTreeView_refresh();
            //display_showAnnotations_inComparatorList();

            // update position indicators
            gui.showValidPositionIndicators();
            
            display_showAlternativeAnnotations();
            this.jList_selectedAnnotations.setSelectedIndex( selectedAnnIndex );
            
            jList_Spans.setSelectedIndex(selectedSpanIndex);                        
            
            this.updateAnnotation();
            
            
            
            //this.cr_recheckDifference();
            
        }

    }
    
    
    
    /**Refresh after span changed are made on an annotation. 
     */
    private void refresh( spanedittype is_an_span_deleted) {
        // to current modifying annotation, get the its idnex in the annotation list
        //int selectedAnnotationIndex = jList_selectedAnnotations.getSelectedIndex();
        // to current modifying annotation, get the its index in te span list
        //int selectedSpanIndex = jList_Spans.getSelectedIndex();
        gui.display_repaintHighlighter();

        //disableAnnotationDisplay();
        /*
        if ((is_an_span_deleted == null) || (is_an_span_deleted != GUI.spanedittype.delete)) {
            
            // ##1## if is in mode of annotation comparing, this function will be disabled
            if (((userInterface.annotationCompare.ExpandButton) jPanel60).isComparatorPanelExpanded()) {
                
                display_showOneAnnotation_inListOnEditorPanel(
                        resultEditor.workSpace.WorkSet.currentAnnotation.uniqueIndex
                        );
                
            } else {
                showSelectedAnnotations_inList( env.Parameters.latestSelectedInListOfMultipleAnnotions );
            }

            this.enable_AnnotationEditButtons();
            jList_selectedAnnotations.setSelectedIndex( selectedAnnotationIndex );
            jList_Spans.setSelectedIndex(selectedSpanIndex);
        } 
        // after deleting an annotation, show other annotaitons if have;
        else*/ 
        if ( (Depot.SelectedAnnotationSet.getSelectedAnnotationSet() != null)
          && (Depot.SelectedAnnotationSet.getSelectedAnnotationSet().size() > 0)) 
        {
            int index_of_SelectedAnnotaitons = jList_selectedAnnotations.getSelectedIndex();
            if( index_of_SelectedAnnotaitons < 0 )
                return;
            
            this.updateAnnotation();
            jList_selectedAnnotations.setSelectedIndex( index_of_SelectedAnnotaitons );
            
            //showSelectedAnnotations_inList(0);
            //this.enable_AnnotationEditButtons();
        }
    }
     
     
}

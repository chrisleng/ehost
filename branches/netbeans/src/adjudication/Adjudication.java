/*
 * The contents of this file are subject to the GNU GPL v3 (the "License"); 
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at URL http://www.gnu.org/licenses/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is eHOST.
 *
 * The Initial Developer of the Original Code is University of Utah.  
 * Copyright (C) 2009 - 2012.  All Rights Reserved.
 *
 * eHOST was developed by the Division of Epidemiology at the 
 * University of Utah. Current information about eHOST can be located at 
 * http://http://code.google.com/p/ehost/
 * 
 * Categories:
 * Scientific/Engineering
 * 
 * Intended Audience:
 * Science/Research
 * 
 * User Interface:
 * Java Swing, Java AWT, Custom Components.
 * 
 * Programming Language
 * Java
 *
 * Contributor(s): Jianwei "Chris" Leng <Chris.Leng@utah.edu> (Original Author)
 * @ Created on Oct 13, 2011, 9:46:18 PM
 *
 */
package adjudication;

import adjudication.parameters.Paras;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import report.iaaReport.analysis.detailsNonMatches.Comparator;
import resultEditor.annotations.*;
import userInterface.GUI;
import userInterface.GUI.ReviewMode;

/**
 * The dialog which can let user select annotators and classes to initialize the
 * Adjudication mode, it's a wizard that help us run pre-processing before we
 * really go to "adjudication" mode to compare annotation results between
 * different user and build ADJUDICATION ANNOTATIONS.
 *
 * @author Jianwei Chris Leng, 2011-10-13
 * @since JDK 1.6
 */
public class Adjudication extends javax.swing.JFrame {

    /**
     * Serial Version UID of this class.
     */
    private static final long serialVersionUID = 1L;
    /**
     * A list of the class names of all found annotators
     *
     */
    private static Vector<String> __annotators = new Vector<String>();
    /**
     * A list of the class names of all found classes
     *
     */
    private static Vector<String> __classNames = new Vector<String>();

    /**
     * Constructor.
     *
     * @param _gui The caller, usually it's an instance of class
     * "userInterface.GUI".
     *
     */
    public Adjudication(userInterface.GUI _gui) {
        this.__gui = _gui;
        initComponents();
        jButton_next.setEnabled(false);

        __gui.setReviewChangeButtonEnabled(false);

        // set the screen location of this dialog
        // current rule: same center as parents'
        int width = 720, height = 715;

        // set the size of this dialog
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));

        // put this dialog on the middle of the caller.
        int x = __gui.getX() + (int) ((__gui.getWidth() - width) / 2);
        int y = __gui.getY() + (int) ((__gui.getHeight() - height) / 2);
        this.setLocation(x, y);

        // set this dialog to un-resizable
        this.setResizable(false);
        addEvents();

        // show initial card
        currentCard = CARD.card2;
        CardLayout card = (CardLayout) cardContainer_jPanel.getLayout();
        card.show(cardContainer_jPanel, "card2");

        // mouse event to jList; so items can be checked or unchecked by
        // user's clicking.

        reflectParameters(); // set checkboxes for standard of matches using
        // previous

        long currenttime = System.currentTimeMillis();
        extractInfo(currenttime);

    }
    /**
     * Thread that is used to gather informations from annotations in memory,
     * such as all annotators and annotations classes that appear in current
     * annotations.
     */
    Thread gatherInfo = null;

    /**
     * Show corresponding jpanel card by the given index number.
     *
     * @param cardindex The index of the jpanel that we want to load and display
     * it on screen of the dialog of "Adjudication Mode". It can be an integer
     * of '0', '1', '2', '3', '4' or '5'.
     *
     */
    public void showCard(int cardindex) {

        // get control to cardlayout of our card container
        CardLayout card = (CardLayout) cardContainer_jPanel.getLayout();

        // show matched card on the card container by the given index number
        switch (cardindex) {
            case 0:
                currentCard = CARD.card0;
                card.show(cardContainer_jPanel, "card0");
                break;

            case 1:
                currentCard = CARD.card1;
                card.show(cardContainer_jPanel, "card1"); // card1: "card3"
                break;

            case 2:
                currentCard = CARD.card2;
                card.show(cardContainer_jPanel, "card2"); // card1: "card3"
                break;

            case 3:
                currentCard = CARD.card3;
                card.show(cardContainer_jPanel, "card3"); // card1: "card3"
                break;

            case 4:
                currentCard = CARD.card4;
                card.show(cardContainer_jPanel, "card4"); // card1: "card3"
                break;

            case 5:
                currentCard = CARD.card5;
                card.show(cardContainer_jPanel, "card5"); // card1: "card3"
                break;
        }
    }

    /**
     * data format that used to indicate which card is current actived card.
     */
    private enum CARD {

        card0, card1, card2, card3, card4, card5, card6
    };
    /**
     * indicator that tell us which card is current card
     */
    private CARD currentCard;
    /**
     * pointer linked us to the instance of current main window of eHOST
     */
    GUI __gui = null;

    /**
     * This method can be called from the constructor to initialize the dialog.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        cardContainer_jPanel = new javax.swing.JPanel();
        card2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_annotators = new javax.swing.JList();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_classes = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jRadioButton_check_same_spans = new javax.swing.JRadioButton();
        jRadioButton_check_overlapped_spans = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox_sameClasses = new javax.swing.JCheckBox();
        jCheckBox_check_attributes = new javax.swing.JCheckBox();
        jCheckBox_check_relationships = new javax.swing.JCheckBox();
        jCheckBox_check_comments = new javax.swing.JCheckBox();
        card4 = new javax.swing.JPanel();
        jLabel_busySignal1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        card5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton_cancel = new javax.swing.JButton();
        jButton_next = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Adjudication Mode");
        setAlwaysOnTop(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        cardContainer_jPanel.setBackground(new java.awt.Color(254, 254, 255));
        cardContainer_jPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 153)));
        cardContainer_jPanel.setLayout(new java.awt.CardLayout());

        card2.setBackground(new java.awt.Color(255, 255, 254));

        jLabel6.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel6.setText("<html>Adjudication mode will allow you to analyze all annotations across selected  annotators and/or markable classes, attributes, or relationships. In adjudication mode, non-matches are highlighted with a wavy red underline. The final adjudicated set will be saved under a folder called “adjudication” in the workspace associated with your current eHOST project.</html>");

        jLabel7.setFont(new java.awt.Font("Gill Sans MT", 1, 16)); // NOI18N
        jLabel7.setText("1. Select Annotations");

        jPanel1.setBackground(new java.awt.Color(255, 255, 254));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jList_annotators.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList_annotators);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("By Annotator", jPanel1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 254));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jList_classes.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList_classes);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane2.addTab("By Class", jPanel4);

        jLabel9.setFont(new java.awt.Font("Gill Sans MT", 1, 16)); // NOI18N
        jLabel9.setText("2. Define matches and/or non-matches");

        jLabel12.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel12.setText("<html>Use the options below to define what annotations you want to adjudicate. Annotations that meet your criteria will be considered matched annotations (matches), those annotations that do not meet your criteria will be considered non-matches.</html>");

        jLabel13.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel13.setText("<html>Select the annotations  or classes you wish to compare between 2 or more annotators or for specific classes.</html>");

        buttonGroup1.add(jRadioButton_check_same_spans);
        jRadioButton_check_same_spans.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jRadioButton_check_same_spans.setSelected(true);
        jRadioButton_check_same_spans.setText("they have same spans");

        buttonGroup1.add(jRadioButton_check_overlapped_spans);
        jRadioButton_check_overlapped_spans.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jRadioButton_check_overlapped_spans.setText("OR, their spans overlapped");

        jLabel15.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jLabel15.setText("Annotations are matched, if");

        jLabel14.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jLabel14.setText("Define your criteria:");

        jLabel11.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jLabel11.setText("and");

        jCheckBox_sameClasses.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jCheckBox_sameClasses.setSelected(true);
        jCheckBox_sameClasses.setText("they have same classes");

        jCheckBox_check_attributes.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jCheckBox_check_attributes.setSelected(true);
        jCheckBox_check_attributes.setText("they have same attributes");

        jCheckBox_check_relationships.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jCheckBox_check_relationships.setSelected(true);
        jCheckBox_check_relationships.setText("they have same relationships");

        jCheckBox_check_comments.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jCheckBox_check_comments.setSelected(true);
        jCheckBox_check_comments.setText("they have same comments");

        org.jdesktop.layout.GroupLayout card2Layout = new org.jdesktop.layout.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(card2Layout.createSequentialGroup()
                .addContainerGap()
                .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(card2Layout.createSequentialGroup()
                        .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 651, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel7)
                            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(card2Layout.createSequentialGroup()
                                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 324, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 333, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel9)
                            .add(jLabel14)
                            .add(card2Layout.createSequentialGroup()
                                .add(64, 64, 64)
                                .add(jLabel15)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jRadioButton_check_same_spans)
                                .add(18, 18, 18)
                                .add(jRadioButton_check_overlapped_spans))
                            .add(card2Layout.createSequentialGroup()
                                .add(184, 184, 184)
                                .add(jLabel11)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jCheckBox_check_relationships)
                                    .add(jCheckBox_sameClasses)
                                    .add(jCheckBox_check_attributes)
                                    .add(jCheckBox_check_comments))))
                        .add(0, 22, Short.MAX_VALUE)))
                .addContainerGap())
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(card2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jTabbedPane1)
                    .add(jTabbedPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 218, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(30, 30, 30)
                .add(jLabel14)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(jRadioButton_check_same_spans)
                    .add(jRadioButton_check_overlapped_spans))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(card2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(jCheckBox_sameClasses))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_check_relationships)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_check_attributes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_check_comments)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        cardContainer_jPanel.add(card2, "card2");

        card4.setBackground(new java.awt.Color(255, 255, 254));

        jLabel_busySignal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adjudication/busy1.gif"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel17.setText("eHOST is searching for matches and nonmatches ... ... ");

        jLabel18.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel18.setText("1. marking all annotation");

        jLabel19.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel19.setText("2. going though all documents, comparing annotations one by one");

        jLabel20.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel20.setText("- Combined matches");

        jLabel21.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel21.setText("- Found non-matches. eHOST editor will list them on screen with red wavy underlines.");

        jLabel22.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel22.setText("3. end of analysis.");

        jLabel23.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 102));
        jLabel23.setText("PLEASE CLICK \"NEXT\" to complete this preprocessing ... ...");

        org.jdesktop.layout.GroupLayout card4Layout = new org.jdesktop.layout.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(card4Layout.createSequentialGroup()
                .add(card4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(card4Layout.createSequentialGroup()
                        .add(75, 75, 75)
                        .add(card4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel_busySignal1)
                            .add(jLabel18)
                            .add(jLabel17)
                            .add(jLabel19)
                            .add(jLabel22)
                            .add(jLabel23)))
                    .add(card4Layout.createSequentialGroup()
                        .add(90, 90, 90)
                        .add(card4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel21)
                            .add(jLabel20))))
                .addContainerGap(137, Short.MAX_VALUE))
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(card4Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel_busySignal1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel17)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel18)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel19)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel20)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel21)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel22)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 397, Short.MAX_VALUE)
                .add(jLabel23)
                .add(65, 65, 65))
        );

        cardContainer_jPanel.add(card4, "card4");

        card5.setBackground(new java.awt.Color(255, 255, 254));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adjudication/gavel.png"))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Gill Sans MT", 1, 16)); // NOI18N
        jLabel24.setText("After This Wizard... ... ");

        jLabel25.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel25.setText("eHOST Adjudication function has helped you finishing a basic analysis on your selected documents. And there may still have");

        jLabel26.setFont(new java.awt.Font("Gill Sans MT", 0, 13)); // NOI18N
        jLabel26.setText("some non-matches. You need to adjudicate them manually before you export them.");

        org.jdesktop.layout.GroupLayout card5Layout = new org.jdesktop.layout.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(card5Layout.createSequentialGroup()
                .add(card5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(card5Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(card5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel25)
                            .add(jLabel24)
                            .add(jLabel26))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, card5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel24)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel25)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel26)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 458, Short.MAX_VALUE)
                .add(jLabel2))
        );

        cardContainer_jPanel.add(card5, "card5");

        getContentPane().add(cardContainer_jPanel, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(238, 238, 233));

        jButton_cancel.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jButton_cancel.setText("Exit");
        jButton_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cancelActionPerformed(evt);
            }
        });

        jButton_next.setFont(new java.awt.Font("Gill Sans MT", 0, 12)); // NOI18N
        jButton_next.setText("Next >");
        jButton_next.setFocusCycleRoot(true);
        jButton_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_nextActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(533, Short.MAX_VALUE)
                .add(jButton_next)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton_cancel))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton_cancel)
                    .add(jButton_next)))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * mouse clicked on "back" button on this wizard dialog.
     */
    private void jButton_backActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton_backActionPerformed
    }// GEN-LAST:event_jButton_backActionPerformed

    /**
     * add mouse event for user's clicking on jlist of annotators and classes
     */
    private void addEvents() {
        // #### add a listener to the list of annotators
        // so the checkbox of the item can be checked or unchecked after
        // user clicked on a item of annotator
        jList_annotators.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = jList_annotators.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) jList_annotators.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = jList_annotators.getCellBounds(index, index);
                jList_annotators.repaint(rect);

                int countSelectedItem = 0;
                int size = jList_annotators.getModel().getSize();
                for (int i = 0; i < size; i++) {
                    CheckableItem it = (CheckableItem) jList_annotators.getModel().getElementAt(i);
                    if (it.isSelected()) {
                        countSelectedItem++;
                    }
                }

                jButton_next.setEnabled(isGoodToNextStep());

            }
        });

        // #### add a listener to the list of annotators
        // so the checkbox of the item can be checked or unchecked after
        // user clicked on a item of annotator
        this.jList_classes.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int index = jList_classes.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) jList_classes.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = jList_classes.getCellBounds(index, index);
                jList_classes.repaint(rect);

                int countSelectedItem = 0;
                int size = jList_classes.getModel().getSize();
                for (int i = 0; i < size; i++) {
                    CheckableItem it = (CheckableItem) jList_classes.getModel().getElementAt(i);
                    if (it.isSelected()) {
                        countSelectedItem++;
                    }
                }

                jButton_next.setEnabled(isGoodToNextStep());
            }
        });
    }

    /**
     * List gathered information, such as annotators, classnames, into the list.
     * So user can choose some of them to start to enter adjudicatio mode.
     *
     * @param annotatorNames A list of all collected annotators' names. Notice:
     * if an annotator doesn't contain any annotation in current project, its
     * name won't be listed in this list. The reason is these names were
     * collected by go over all existing annotations.
     */
    private void listInformation(Vector<String> annotatorNames,
            Vector<String> classNames) {

        // list all pre-collected annotator names
        listItems(annotatorNames, jList_annotators);

        // list all pre-collected annotation class names
        listItems(classNames, jList_classes);

    }

    /**
     * to given vectors of annotators or classes, list them on the desginated
     * jlist
     *
     * @param items contents we want to display on the list, such as annotators
     * or classes.
     *
     * @param component the designated component of JList.
     */
    private void listItems(Vector<String> items, JList component) {
        Vector<CheckableItem> listdata = new Vector<CheckableItem>();
        if (items == null) {
            component.setListData(listdata);
            return;
        } else {
            for (String name : items) {
                if (name == null) {
                    continue;
                }

                if (name.compareTo("ADJUDICATION") == 0) {
                    continue;
                }

                listdata.add(new CheckableItem(name, true));
            }
            component.setListData(listdata);
            component.setCellRenderer(new CheckListRenderer());
            component.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            component.setBorder(new EmptyBorder(0, 4, 0, 0));
            return;
        }
    }

    private void jButton_cancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton_cancelActionPerformed
        quits();
    }// GEN-LAST:event_jButton_cancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed
    }// GEN-LAST:event_formWindowClosed

    private void jButton_nextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton_nextActionPerformed
        switch (this.currentCard) {
            /*
             * case card0: this.showCard(1); // show second card
             * jButton_back.setVisible(true); jButton_back.setEnabled(true);
             * long currenttime = System.currentTimeMillis();
             *
             * // #### gather information: annotators, annotation classes
             *
             * jLabel_busySignal.setVisible(true);
             * this.jButton_next.setEnabled(false); jLabel3.setVisible(true); //
             * hide text jLabel4.setVisible(false); jLabel5.setVisible(false);
             * // start gather information annotators, annotation classes
             * extractInfo(currenttime);
             *
             * break;
             */
            case card1: // from card "gathing information" to
                // "list all annotators and classes"
                jButton_next.setEnabled(false);

                // set checkboxes based on saved parameters
                reflectParameters();

                listInformation(__annotators, __classNames);

                // check to make sure we selected >= 2 annotators, and >=1 classes

                this.jButton_next.setEnabled( isGoodToNextStep() );

                this.showCard(2); // show second card
                break;

            case card2: // from card "list annotators and classes" to card
                // "define the standard of matches"
                doAnalysis(System.currentTimeMillis());

                break;

            case card3: // from card "list annotators and classes" to card
                // "define the standard of matches"
                this.showCard(4); // show second card
                hideAllComments();

                jButton_next.setEnabled(false);

                // run analysis
                doAnalysis(System.currentTimeMillis());

                break;

            case card4: // from card "list annotators and classes" to card
                // "define the standard of matches"
                this.showCard(5); // show second card

                jButton_next.setVisible(true);
                jButton_next.setText("Close");
                this.jButton_cancel.setVisible(false);

                break;

            case card5: // from card "list annotators and classes" to card
                // "define the standard of matches"

                break;

        }
    }// GEN-LAST:event_jButton_nextActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosing
        if (!completed) {
            quits();
        }
    }// GEN-LAST:event_formWindowClosing
    /**
     * flag used to tell system the exit is started by clicking "completed"
     * button to enter adjudication mode
     */
    private boolean completed = false;
    /**
     * thread that we use to do analysis to check matches and non-matches
     */
    Thread doanalysis = null;

    /**
     * use thread to run analysis to each annotations, to check matches and
     * non-matches.
     */
    private void doAnalysis(final long starttime) {

        // stop the old thread if you had one.
        //if (doanalysis != null) {
        //    doanalysis.interrupt();
        //}

        //doanalysis = new Thread() {

        //    @Override
        //    public void run() {
                try{

                    checkAnnotations( true );

                    // after run analysis, reset the buttons
                    jButton_next.setEnabled( true );

                    completed = true;
                    try{
                        
                        __gui.display_repaintHighlighter();
                        __gui.setReviewChangeButtonEnabled( true );
                        __gui.display_showCategories();
                        __gui.display_removeSymbolIndicators();
                        __gui.display_showSymbolIndicators();
                        
                    }catch( Exception ex ){
                        ex.printStackTrace();
                    }

                    dispose();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        //    }
        //};

        //doanalysis.start();
    }

    /**
     * hide comments on card 4: running analysis
     */
    private void hideAllComments() {
        jLabel17.setVisible(false); // begin analysis
        jLabel18.setVisible(false); // reset adjudication status to all
        // annotations
        jLabel20.setVisible(false);
        jLabel21.setVisible(false);
        jLabel22.setVisible(false);
        jLabel23.setVisible(false);
        jLabel_busySignal1.setVisible(true);
        this.jButton_next.setEnabled(false);

    }
    /**
     * count only used in method "checkannotations()" and "compareAnnotations( ,
     * )"
     */
    public int count = 0;

    /**
     * Go though all annotations to search all matches and non-matches.
     *
     * @param _isInitialCheck Tell the method is that this is our first time to
     * check the annotation differences for adjudication mode. <b> If
     * _isInitialCheck=true, it means it will clear and reset all parameters.
     * </b>
     *
     */
    public void checkAnnotations(boolean _isInitialCheck) throws Exception {

        count = 0; // reset counter

        if (_isInitialCheck) {

            jLabel17.setVisible(true); // begin analysis

            // set flags by these check boxes to define what kind of annotations
            // are
            // matches
            getParameters();

            // get selected annotators and classes
            Paras.removeAll();
            // record anntators
            Paras.setAnnotators(getAnnotators_fromList());
            Paras.addAnnotator("ADJUDICATION");

            // record classes
            Paras.setClasses(getClasses_fromList());
        }

        if (Paras.getAnnotators().size() < 2) {
            log.LoggingToFile.log(Level.SEVERE,
                    "1110140414:: we need at least 2 annotators to run this analysis");
            commons.Tools.beep();
            return;
        }

        if (Paras.getAnnotatorAt(0) == null) {
            throw new Exception("1110140655::fail to get annotator");
        }
        if (Paras.getAnnotatorAt(0).trim().length() < 1) {
            throw new Exception("1110140654::fail to get annotator");
        }

        // count how many article*annotators will be compared in following
        // processing
        // int totalQuery = size * (size - 1 )*Depot.getSize();

        try {

            resultEditor.Differences.IAADifference.Differences.clear();

            // #### set all annotations for adjudication mode, and set some
            // annotation
            // invaliable if their annotator or class isn't in our list.
            jLabel18.setVisible(true);
            jLabel20.setVisible(true);
            jLabel21.setVisible(true);

            // copy annotations from annotation mode to adjudication mode.
            copyAnnotations(Paras.getAnnotators(), Paras.getClasses(), _isInitialCheck);
            
            setAnnotationStatus(Paras.getAnnotators(), Paras.getClasses(), _isInitialCheck);

            // #### to all annotations whose adjudication status is
            // "UNPROCESSED", need to
            // be changed to "NONMATCHES"
            translateAnnotationStatus();

            // ~####~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // build depot to store matched or matched annotaitons for current
            // annotator
            // buildDiffAnalysisDepot( annotator1 );
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // #### following are for PairWise Analysis
            // #### compare this annotator with others
            compareAnnotations(Paras.getAnnotators());

            __gui.display_repaintHighlighter();

            __gui.display_adjudication_setOperationBarStatus(__gui.ENABLED);

            if (_isInitialCheck) {
                jLabel22.setVisible(true);
                jLabel23.setVisible(true);
                jLabel_busySignal1.setVisible(false);

                this.jButton_next.setEnabled(true);

                Paras.__adjudicated = true;

            }

            // }
        } catch (Exception ex) {
            throw new Exception("1110140451::fail to check all "
                    + "annotations.\n - RELATED ERROR:" + ex.getMessage());
        }
    }

    /**
     * check for matched annotations
     */
    private void compareAnnotations(ArrayList<String> annotators)
            throws Exception {
        try {

            String annotator1 = annotators.get(0);

            /*
             * log.LoggingToFile.log(Level.INFO, "Start to compare annotations "
             * + "between annotator: [" + annotator1 + "], and another
             * annotator: [" + annotator2 + "]");
             */

            // #### begin analysis to all annotations
            // get all article
            ArrayList<Article> articles = null;
                    
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE ) {
                resultEditor.annotations.Depot depotOfAnn = new resultEditor.annotations.Depot();
                articles = depotOfAnn.getAllArticles();
            }else{
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                articles = depotOfAdj.getAllArticles();
            }                        

            if (articles == null) {
                throw new Exception(
                        "1110140510::fail to get saved annotations!");
            }

            // #### go though all articles
            for (Article article : articles) {

                // list current progress on screen
                this.count++; // workload++ for one article processed
                jLabel19.setText("2. going though all documents, comparing "
                        + "annotations one by one (" + this.count + "/"
                        + Depot.getSize() + ").");

                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                String file = article.filename;

                // search and record difference in the given article
                searchDifferenceinArticle(article, annotator1);

            }

            log.LoggingToFile.log(Level.INFO,
                    "1108181503:: Completed the IAA analysis.");

        } catch (Exception ex) {

            throw new Exception(
                    "1110140511::fail to compare two annotation!\n - RELATED ERROR: "
                    + ex.getMessage());
        }
    }

    /**
     * To a given file, we go though all its document and find nonmatches
     */
    public static void searchDifferenceinArticle(Article article,
            String annotator1) {
        try {

            // ---- 1 -----
            // erase old searching results by clearing recorded
            // differences of the given article name
            clearRecordNonMatches(article.filename);

            // ---- 2 -----
            // to all its annotaions, search for non-matches
            int size = article.annotations.size();

            // #### go though all annotations of current article, and
            // compare them with the gold standard annotation
            // you just selected.
            for (int i = 0; i < size; i++) {
                Annotation sourceAnnotation = article.annotations.get(i);
                if (sourceAnnotation == null) {
                    continue;
                }

                if (sourceAnnotation.isProcessed()) {
                    continue;
                }

                if ((sourceAnnotation.spanset == null) || (sourceAnnotation.spanset.isEmpty())) {
                    continue;
                }

                // do nothing
                // if [1] current class don't belong to annotator 1 (the gold
                // stand)
                // and if [2] anntator is "ADJUDICATION"
                try{
                if ((sourceAnnotation.getAnnotator().compareTo("ADJUDICATION") != 0)
                        && (!Comparator.checkAnnotator(sourceAnnotation,
                        annotator1))) {

                    continue;
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                // only need status: matches_OK, and nonmatches
                // if ((sourceAnnotation.adjudicationStatus !=
                // Annotation.AdjudicationStatus.MATCHES_OK)
                // && (sourceAnnotation.adjudicationStatus !=
                // Annotation.AdjudicationStatus.NON_MATCHES)) {
                // continue;
                // }
                if ((sourceAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                        && (sourceAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                    continue;
                }

                if (sourceAnnotation.annotationclass == null) {
                    continue;
                }

                // use to store annotation and annotation similar to
                // the annotation and also belong to first annotator
                Vector<Annotation> sames = new Vector<Annotation>();
                // Vector<Annotation> others = new Vector<Annotation>();
                sames.add(sourceAnnotation);

                // -------------------------------------------------------
                // ######## search for similar item to current annotation
                // which came from the first annotator
                for (int ii = 0; ii < size; ii++) {
                    Annotation similarAnnotation = article.annotations.get(ii);
                    if (similarAnnotation == null) {
                        continue;
                    }

                    if (similarAnnotation.uniqueIndex == sourceAnnotation.uniqueIndex) {
                        continue;
                    }

                    if ((similarAnnotation.spanset == null) || (similarAnnotation.spanset.isEmpty())) {
                        continue;
                    }

                    // if the source annotation is a new annnotation created
                    // under the adjudication mode
                    // it need to be compared with all annotations, whatever it
                    // has been processed or not.
                    if (sourceAnnotation.getAnnotator().compareTo("ADJUDICATION") != 0) {
                        if (similarAnnotation.isProcessed()) {
                            continue;
                        }

                        // skip it if this annottation is already analysised
                        if ((similarAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                                && (similarAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                            continue;
                        }
                    }

                    // if ( similarAnnotation.adjudicationStatus ==
                    // Annotation.AdjudicationStatus.NONMATCHES_DLETED)
                    // continue;

                    // annotator can not be compared with his brothers which
                    // have same parents.
                    if ((!Comparator.checkAnnotator(similarAnnotation,
                            annotator1))) {
                        continue;
                    }

                    if (similarAnnotation.annotationclass == null) {
                        continue;
                    }

                    // int ki = 0;
                    // if(sourceAnnotation.annotator.compareTo("ADJUDICATION")==0)
                    // {
                    // if(similarAnnotation.annotator.compareTo("ADJUDICATION")==0)
                    // ki=6;
                    // }

                    // check for span
                    boolean samespan = false;
                    if (Paras.CHECK_OVERLAPPED_SPANS) {
                        if (Comparator.isSpanOverLap(sourceAnnotation,
                                similarAnnotation)) {
                            samespan = true;
                        }


                        if (Comparator.equalSpans(sourceAnnotation,
                                similarAnnotation)) {
                            samespan = true;
                        }
                    } else if (Comparator.equalSpans(sourceAnnotation,
                            similarAnnotation)) {
                        samespan = true;
                    }

                    if (!samespan) {
                        continue; // pass it if they don't have same span
                    }
                    // check for other
                    boolean same = true;
                    if (Paras.CHECK_ATTRIBUTES) {
                        if (!Comparator.equalAttributes(sourceAnnotation,
                                similarAnnotation)) {
                            same = false;
                        }
                    }
                    if (Paras.CHECK_RELATIONSHIP) {
                        if (!Comparator.equalRelationships(sourceAnnotation,
                                similarAnnotation, article.filename)) {
                            same = false;
                        }
                    }
                    if (Paras.CHECK_CLASS) {
                        if (!Comparator.equalClasses(sourceAnnotation,
                                similarAnnotation)) {
                            same = false;
                        }
                    }

                    if (Paras.CHECK_COMMENT) {
                        try {
                            if (!Comparator.equalComments(sourceAnnotation,
                                    similarAnnotation)) {
                                same = false;
                            }

                        } catch (Exception ex) {
                            log.LoggingToFile.log(Level.WARNING,
                                    "1110300841::fail to compare comments for two annotations.");
                        }
                    }

                    //System.out.println("did we find same items = " + same);
                    // decision
                    if (same) {
                        // if(debug)
                        // System.out.println("----> same: (" +
                        // similarAnnotation.spanstart + ", " +
                        // similarAnnotation.spanend + " ) - " +
                        // similarAnnotation.annotationText );
                        sames.add(similarAnnotation);
                    }
                }

                // System.out.println("size of sames = " + sames.size() );

                boolean foundDifference = false;

                // #### compare the objective annotation from gold standard
                for (int s = 0; s < size; s++) {
                    if (i == s) {
                        continue; // don't compare annotation to itself
                    }
                    Annotation objectAnnotation = article.annotations.get(s);


                    if (objectAnnotation == null) {
                        continue;
                    }

                    if (objectAnnotation.uniqueIndex == sourceAnnotation.uniqueIndex) {
                        continue;
                    }

                    if (objectAnnotation.spanset == null) {
                        continue;
                    }
                    if (objectAnnotation.spanset.isEmpty()) {
                        continue;
                    }

                    // only need status: matches_OK, and nonmatches
                    if (sourceAnnotation.getAnnotator().compareTo("ADJUDICATION") != 0) {
                        if (objectAnnotation.isProcessed()) {
                            continue;
                        }
                        if ((objectAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                                && (objectAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                            continue;
                        }
                    }

                    if (objectAnnotation.adjudicationStatus == Annotation.AdjudicationStatus.NONMATCHES_DLETED) {
                        continue;
                    }
                    if (objectAnnotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_DLETED) {
                        continue;
                    }

                    // #### 3.1 #### check for same span
                    boolean samespan = false;
                    if (Comparator.equalSpans(objectAnnotation,
                            sourceAnnotation)) {
                        samespan = true;
                    }
                    if (Paras.CHECK_OVERLAPPED_SPANS) {
                        if (Comparator.isSpanOverLap(objectAnnotation,
                                sourceAnnotation)) {
                            samespan = true;
                        }
                    }

                    // if it has same span as current annotation of gold
                    // standard
                    if (!samespan) {
                        if ((sourceAnnotation.getAnnotator().compareTo("ADJUDICATION") == 0)
                                || (objectAnnotation.getAnnotator().compareTo("ADJUDICATION") == 0)) {
                            if (Comparator.isSpanOverLap(objectAnnotation,
                                    sourceAnnotation)) {
                                Vector<Annotation> oneannotation = new Vector<Annotation>();
                                oneannotation.add(objectAnnotation);
                                oneannotation.add(sourceAnnotation);
                                objectAnnotation.setProcessed();
                                sourceAnnotation.setProcessed();
                                objectAnnotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                                sourceAnnotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                                recordNonMatches(oneannotation,
                                        article.filename);
                            }
                        }
                        continue;
                    }

                    // #### 3.1 #### check to make sure current annotation don't
                    // to annotator 1, and in the list
                    if (Comparator.checkAnnotator(objectAnnotation, annotator1)) {
                        continue;
                    }

                    boolean goodAnnor = false;
                    for (String annor : Paras.getAnnotators()) {
                        if ((annor == null) || (annor.trim().length() < 1)) {
                            continue;
                        }
                        if (annor.trim().compareTo(
                                objectAnnotation.getAnnotator().trim()) == 0) {
                            goodAnnor = true;
                        }
                    }

                    if (goodAnnor == false) {
                        continue;
                    }

                    boolean addtionalConditions = true;

                    // if they have same class
                    if (Paras.CHECK_CLASS) {
                        if (!Comparator.equalClasses(sourceAnnotation,
                                objectAnnotation)) {
                            addtionalConditions = false;
                        }
                    }

                    // this found annotation is matched with the one
                    // of gold standard
                    if (Paras.CHECK_ATTRIBUTES) {
                        if (!Comparator.equalAttributes(sourceAnnotation,
                                objectAnnotation)) {
                            addtionalConditions = false;
                        }
                    }

                    if (Paras.CHECK_RELATIONSHIP) {
                        if (!Comparator.equalRelationships(sourceAnnotation,
                                objectAnnotation, article.filename)) {
                            addtionalConditions = false;

                        }
                    }

                    if (Paras.CHECK_COMMENT) {
                        try {
                            if (!Comparator.equalComments(sourceAnnotation,
                                    objectAnnotation)) {
                                addtionalConditions = false;
                            }

                        } catch (Exception ex) {
                            log.LoggingToFile.log(Level.WARNING,
                                    "1110300842::fail to compare comments for two annotations.");
                        }
                    }

                    if (addtionalConditions) {
                        sames.add(objectAnnotation);
                    } else {
                        foundDifference = true;
                    }

                }

                // System.out.println("diff = " + foundDifference);

                // there are non-matches as we find at least a different one
                if (foundDifference) {
                    for (Annotation ann : sames) {
                        if (ann == null) {
                            continue;
                        }
                        ann.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                        ann.setProcessed();

                        // if(debug) System.out.println("----> diff: (" +
                        // ann.spanstart + ", " + ann.spanend + " ) - " +
                        // ann.annotationText );
                    }
                    recordNonMatches(sames, article.filename);
                } // matches if we found all annotator
                else {
                    // if the source annotation is created under the
                    // adjudication mode,
                    // (they have anntator "ADJUDICATION")
                    if (sourceAnnotation.getAnnotator().compareTo("ADJUDICATION") == 0) {

                        // alone, means it's good
                        if (sames.size() == 1) {
                            sourceAnnotation.adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_OK;
                            sourceAnnotation.setProcessed();

                        } // set one of them are good and semi-delete others by
                        // set their status of adjudication
                        // while there are several same annotation were
                        // created
                        // adjudication mode
                        else if (sames.size() > 1) {

                            for (int t = 0; t < sames.size(); t++) {

                                Annotation ann = sames.get(t);
                                if (ann == null) {
                                    continue;
                                }

                                if (t == 0) {
                                    ann.adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_OK;
                                    ann.setProcessed();
                                    System.out.println("----> adjudicatin same: ("
                                            + ann.annotationText);
                                    continue;
                                }

                                ann.setProcessed();
                                ann.adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_DLETED;
                                System.out.println("----> adjudicatin diff: ("
                                        + ann.annotationText);
                            }

                        }

                    } else if (checkAnnotators(Paras.getAnnotators(),
                            annotator1, sames)) {
                        for (int t = 0; t < sames.size(); t++) {

                            Annotation ann = sames.get(t);
                            if (ann == null) {
                                continue;
                            }
                            String adjudicationAlias = null;
                            if (t == 0) {
                                ann.adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_OK;
                                adjudicationAlias = String.valueOf(ann.uniqueIndex);
                                ann.setProcessed();
                                continue;
                            }

                            ann.setProcessed();
                            ann.adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_DLETED;
                            ann.adjudicationAlias = adjudicationAlias;

                        }
                    } else {
                        // nonmatches
                        for (int t = 0; t < sames.size(); t++) {
                            Annotation ann = sames.get(t);
                            // if(t==1)
                            // ann.adjudicationStatus =
                            // Annotation.AdjudicationStatus.MATCHES_OK;
                            if (ann == null) {
                                continue;
                            }
                            // if(ann.uniqueIndex==18)
                            // {int c=0;}

                            ann.setProcessed();
                            ann.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;

                            ann.adjudicationAlias = String.valueOf(sames.get(0).uniqueIndex);

                            // if(debug) System.out.println("----> diff: (" +
                            // ann.spanstart + ", " + ann.spanend + " ) - " +
                            // ann.annotationText );

                        }
                        recordNonMatches(sames, article.filename);
                    }
                }
                // end processing matches for this annotations
            }
            // end processing matches for all annotatin in current article

            // ------------------------------------------------------------------------------

            // following are all non-matches
            // there are all non-matched annotation, check for annotations
            for (int q = 1; q < Paras.getAnnotators().size(); q++) {

                // get the second annotator and all other annotators one by one
                // from the list of selected annotators
                String qannotator = Paras.getAnnotatorAt(q);

                if ((qannotator == null) || (qannotator.trim().length() < 1)) {
                    throw new Exception(
                            "1110140733::annotator could not be null.");
                }

                // get annotatoions from current article one by one
                for (int i = 0; i < size; i++) {

                    // list that used to record other non-matches compared
                    Vector<Annotation> othernonmatches = new Vector<Annotation>();

                    // get annotatoions from current article one by one
                    Annotation annotation_o1 = article.annotations.get(i);
                    if (annotation_o1 == null) {
                        continue;
                    }

                    if (annotation_o1.isProcessed()) {
                        continue;
                    }

                    if (annotation_o1.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                        continue;
                    }
                    if (annotation_o1.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES) {
                        continue;
                    }

                    if (!Comparator.checkAnnotator(annotation_o1, qannotator)) {
                        continue;
                    }

                    annotation_o1.setProcessed();

                    //String seed = SugarSeeder.getSeed();
                    //annotation_o1.adjudicationAlias = seed;

                    othernonmatches.add(annotation_o1);

                    if (q + 1 < Paras.getAnnotators().size()) {
                        for (int e = q + 1; e < Paras.getAnnotators().size(); e++) {
                            String eannotator = Paras.getAnnotatorAt(e);
                            if ((eannotator == null)
                                    || (eannotator.trim().length() < 1)) {
                                throw new Exception(
                                        "1110140850::annotator could not be null.");
                            }

                            for (int k = 0; k < size; k++) {
                                Annotation annotation_k = article.annotations.get(i);
                                if (annotation_k == null) {
                                    continue;
                                }
                                if (annotation_k.getAnnotator() == null) {
                                    continue;
                                }

                                if (!Comparator.checkAnnotator(annotation_k,
                                        eannotator)) {
                                    continue;
                                }

                                // if(annotation_o1.annotator.compareTo("ADJUDICATION")
                                // != 0) {

                                if (annotation_k.isProcessed()) {
                                    continue;
                                }

                                if (annotation_k.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                                    continue;
                                }
                                if (annotation_k.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES) {
                                    continue;
                                }

                                // }

                                boolean samespan = false;
                                // if(CHECK_OVERLAPPED_SPANS){
                                if (Comparator.isSpanOverLap(annotation_k,
                                        annotation_o1)) // samespan = true;
                                // }else{
                                {
                                    if (Comparator.equalSpans(annotation_k,
                                            annotation_o1)) // samespan = true;
                                    // }
                                    {
                                        if (samespan) {
                                            annotation_k.setProcessed();
                                            othernonmatches.add(annotation_k);
                                        }
                                    }
                                }

                                if (annotation_o1.uniqueIndex != annotation_k.uniqueIndex) {
                                    if ((annotation_o1.getAnnotator().compareTo("ADJUDICATION") == 0)
                                            || (annotation_k.getAnnotator().compareTo("ADJUDICATION") == 0)) {
                                    }

                                    if (Comparator.isSpanOverLap(annotation_k,
                                            annotation_o1)) {
                                        othernonmatches.add(annotation_k);
                                        othernonmatches.add(annotation_o1);
                                        //annotation_k.adjudicationAlias = seed;
                                    }
                                }
                            }
                        }
                    }
                    recordNonMatches(othernonmatches, article.filename);
                }

            }

        } catch (Exception ex) {
            System.out.println("1109151945-B::");
            ex.printStackTrace();
        }
    }

    /**
     * To these annotations, who have same spans, but have different class, or
     * different attributes, or different record non-matches
     */
    private static void recordNonMatches(Vector<Annotation> annotations,
            String filename) throws Exception {

        if ((filename == null) || (filename.trim().length() < 1)) {
            throw new Exception(
                    "1110140714:: filename can not be null or empty.");
        }

        if (annotations == null) {
            throw new Exception(
                    "1110140713:: given vector of anntations can not be null.");
        }

        // a temporary spanset that used to record
        SpanSetDef swap = new SpanSetDef();

        // record all spans from all annotation
        for (Annotation ann : annotations) {
            if (ann == null) {
                continue;
            }
            if (ann.spanset == null) {
                continue;
            }
            if (ann.spanset.size() < 1) {
                continue;
            }

            // get all spans of this annotation
            for (int t = 0; t < ann.spanset.size(); t++) {
                SpanDef span = ann.spanset.getSpanAt(t);
                if (span == null) {
                    continue;
                }
                if ((span.start < 0) || (span.end < 0)
                        || (span.start >= span.end)) {
                    continue;
                }

                // record span
                // before record it, make sure this span is not recorded.
                boolean exists = false;
                for (int k = 0; k < swap.size(); k++) {
                    SpanDef existsSpan = swap.getSpanAt(k);
                    if (existsSpan.isEqual(span)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    swap.addSpan(span.start, span.end);
                }
            }

        }

        for (int k = 0; k < swap.size(); k++) {
            SpanDef span = swap.getSpanAt(k);
            // record for consensus mode
            // System.out.println("diff = " + span.start + ", " + span.end );
            resultEditor.Differences.IAADifference.Differences.add(
                    filename.trim(), span.start, span.end);
        }

    }

    /**
     * clear recorded differences of the given article name
     */
    private static void clearRecordNonMatches(String filename) throws Exception {

        if ((filename == null) || (filename.trim().length() < 1)) {
            throw new Exception(
                    "1110140714:: filename can not be null or empty.");
        }

        resultEditor.Differences.IAADifference.Differences.remove(filename.trim());

    }

    /**
     * this method is used to make sure we found all annotators' matched
     * annotations.
     */
    private static boolean checkAnnotators(ArrayList<String> annotators,
            String annotator, Vector<Annotation> annotations) {

        for (String annor : annotators) {
            if (annor == null) {
                continue;
            }
            if (annor.trim().compareTo(annotator.trim()) == 0) {
                continue;
            }
            if (annor.trim().compareTo("ADJUDICATION") == 0) {
                continue;
            }

            boolean found = false;
            for (Annotation ann : annotations) {
                if (ann == null) {
                    continue;
                }
                if (ann.getAnnotator() == null) {
                    continue;
                }
                if (ann.getAnnotator().trim().compareTo(annor.trim()) == 0) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * to all annotations whose adjudication status is "UNPROCESSED", need to be
     * changed to "MATCHES"
     */
    private void translateAnnotationStatus() throws Exception {
        translateAnnotationStatus(null, false);
    }

    public static void translateAnnotationStatus(String filename,
            boolean onlyCheckSpecificFile) throws Exception {
        try {

            if (onlyCheckSpecificFile) {
                if ((filename == null) || (filename.trim().length() < 1)) {
                    System.out.println("ERROR 1203021530:: empty filename!");
                    return;
                }
            }

            ArrayList<Article> articles = null;
                    
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE ) {
                resultEditor.annotations.Depot depotOfAnn = new resultEditor.annotations.Depot();
                articles = depotOfAnn.getAllArticles();
            }else{
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                articles = depotOfAdj.getAllArticles();
            }
            
            

            if (articles == null) {
                throw new Exception(
                        "1110140439::can not get saved annotations.");
            }

            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                if ((onlyCheckSpecificFile)
                        && (!(article.filename.trim().compareTo(filename) == 0))) {
                    continue;
                }

                int size = article.annotations.size();

                // reset all processed flags
                for (int i = 0; i < size; i++) {
                    Annotation annotation = article.annotations.get(i);
                    if (annotation == null) {
                        continue;
                    }

                    if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.UNPROCESSED) {
                        annotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                    }
                }
            }

        } catch (Exception ex) {
            throw new Exception(
                    "1110140457::fail to change annotations whose adjudication "
                    + "status was \"UNPROCESSED\" to \"MATCHES\".\n - RELATED ERROR:"
                    + ex.getMessage());
        }
    }

    /**
     * set all annotations for adjudication mode, and set some annotation
     * invisible if their annotator or class isn't in our list.
     */
    private static void setAnnotationStatus(
            ArrayList<String> _selectedAnnotators,
            ArrayList<String> selectedClasses,
            boolean _isInitialCheck )  throws Exception {
        resetAnntationStatus(_selectedAnnotators, selectedClasses, null, false, !_isInitialCheck);
    }

    
     /**Copy selected annotations from annotation mode to adjudication mode by 
      * calling method from class of "AdjudicationDepot".
      */    
    private static void copyAnnotations(
            ArrayList<String> _selectedAnnotators,
            ArrayList<String> selectedClasses,
            boolean _isInitialCheck) throws Exception {
        
        System.out.println(_isInitialCheck);
        // call method to copy selected annotations
        adjudication.data.AdjudicationDepot depotOfAdjudication = new adjudication.data.AdjudicationDepot();
            depotOfAdjudication.copyAnnotations( 
                    _selectedAnnotators,
                    selectedClasses, 
                    _isInitialCheck );
    }
            
    public static void resetAnnotations(String filename) {
        try {
            translateAnnotationStatus(filename, true);

            // set annotation's adjudication status of a specific document
            resetAnntationStatus(Paras.getAnnotators(), Paras.getClasses(),
                    filename, true, false);
           
            
        } catch (Exception ex) {
            System.out.println("ERROR 1203021503;\n" + ex.getMessage());
        }
    }

    /**
     * set annotation's adjudication status of a specific document
     *
     *
     * @param isCheckingSpecificFile tell method whether we want to reset
     * annotation's adjudication status or just reset annotation whose status is
     * not OK, MATCHES_DLETED, NONMATCHES_DLETED
     *
     * @param currentFile name of the current document. Method do nothing if
     * it's null.
     *
     * @param isCheckingReloadedData tell us we are going back to previous
     * adjudication work.
     *
     *
     */
    private static void resetAnntationStatus(
            ArrayList<String> _selectedAnnotators,
            ArrayList<String> _selectedClasses, 
            String currentFile,
            boolean isCheckingSpecificFile,
            boolean isCheckReloadedData) throws Exception {
        try {
            
            adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
            depotOfAdj.resetAnntationStatus( 
                    _selectedAnnotators,
                    _selectedClasses, 
                    currentFile,
                    isCheckingSpecificFile,
                    isCheckReloadedData );
        }catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("1110140440::fail to set adjudication "
                    + "status to all annotations.\n - RELATED ERROR:"
                    + ex.getMessage());

        }   

    }

    /**
     * get selected annotators from list
     */
    private ArrayList<String> getAnnotators_fromList() {
        ArrayList<String> selectedAnnotators = new ArrayList<String>();

        for (int i = 0; i < jList_annotators.getModel().getSize(); i++) {
            Object obj = this.jList_annotators.getModel().getElementAt(i);
            if (obj == null) {
                continue;
            }
            CheckableItem item = (CheckableItem) obj;
            if (item.isSelected()) {
                selectedAnnotators.add(item.getAnnotatorName());
            }
        }

        return selectedAnnotators;
    }

    /**
     * get selected annotators from list
     */
    private ArrayList<String> getClasses_fromList() {
        ArrayList<String> selectedClasses = new ArrayList<String>();

        for (int i = 0; i < this.jList_classes.getModel().getSize(); i++) {
            Object obj = this.jList_classes.getModel().getElementAt(i);
            if (obj == null) {
                continue;
            }
            CheckableItem item = (CheckableItem) obj;
            if (item.isSelected()) {
                selectedClasses.add(item.getAnnotatorName());
            }
        }
        return selectedClasses;
    }

    /**
     * Set parameters by these check boxes on this dialog to define what kind of
     * annotations are matches.
     */
    private void getParameters() {
        Paras.CHECK_OVERLAPPED_SPANS = jRadioButton_check_overlapped_spans.isSelected();
        Paras.CHECK_ATTRIBUTES = jCheckBox_check_attributes.isSelected();
        Paras.CHECK_RELATIONSHIP = jCheckBox_check_relationships.isSelected();
        Paras.CHECK_COMMENT = jCheckBox_check_comments.isSelected();
        Paras.CHECK_CLASS = jCheckBox_sameClasses.isSelected();
    }

    /**
     * use static variables to set the checking status of checkboxes for
     * standard of matching.
     */
    private void reflectParameters() {
        jRadioButton_check_overlapped_spans.setSelected(Paras.CHECK_OVERLAPPED_SPANS);
        jCheckBox_check_attributes.setSelected(Paras.CHECK_ATTRIBUTES);
        jCheckBox_check_relationships.setSelected(Paras.CHECK_RELATIONSHIP);
        jCheckBox_check_comments.setSelected(Paras.CHECK_COMMENT);
        jCheckBox_sameClasses.setSelected(Paras.CHECK_CLASS);
    }

    /**
     * check to make sure we selected >= 2 annotators, and >=1 classes
     */
    private boolean isGoodToNextStep() {
        int countSelectedItem = 0;
        int size = jList_annotators.getModel().getSize();
        for (int i = 0; i < size; i++) {
            CheckableItem it = (CheckableItem) jList_annotators.getModel().getElementAt(i);
            if (it.isSelected()) {
                countSelectedItem++;
            }
        }

        if (countSelectedItem < 2) {
            return false;
        }

        countSelectedItem = 0;
        size = jList_classes.getModel().getSize();
        for (int i = 0; i < size; i++) {
            CheckableItem it = (CheckableItem) jList_classes.getModel().getElementAt(i);
            if (it.isSelected()) {
                countSelectedItem++;
            }
        }

        if (countSelectedItem < 1) {
            return false;
        }

        return true;

    }

    /**
     * gather information, such as all annotators, all annotation classes, from
     * annotations in memory.
     */
    private void extractInfo(final long starttime) {
        if (gatherInfo != null) {
            gatherInfo.interrupt();
        }

        gatherInfo = new Thread() {

            @Override
            public void run() {
                try {
                    CollectInfo ci = new CollectInfo();
                    ci.gatherInformation();
                    __annotators.clear();
                    __annotators = ci.getAnnotators();
                    __classNames.clear();
                    __classNames = ci.getClassNames();

                    // do {
                    // this.sleep(200);
                    // } while ((System.currentTimeMillis() - starttime) <
                    // 1300);

                    jButton_next.setEnabled(true);

                    jButton_next.setEnabled(false);
                    listInformation(__annotators, __classNames);

                    // check to make sure we selected >= 2 annotators, and >=1
                    // classes
                    jButton_next.setEnabled(isGoodToNextStep());

                    showCard(2);

                } catch (Exception ex) {
                }
            }
        };

        gatherInfo.start();
    }
    /**
     * a flag that used to avoid a situation that we run quit() more than once
     * after we close this dialog
     */
    boolean donePostProcessing = false;

    /**
     * leave and close current window
     */
    private void quits() {
        // if(donePostProcessing)
        // return;

        try {
            if (gatherInfo != null) {
                gatherInfo.interrupt();
            }
            if (doanalysis != null) {
                doanalysis.interrupt();
            }
        } catch (Exception ex) {
        }

        this.dispose();

        __gui.setReviewMode(ReviewMode.ANNOTATION_MODE);
        __gui.setReviewChangeButtonEnabled(true);

        if (jButton_cancel.getText().compareTo("Exit") == 0) {
            __gui.mode_enterAnnotationMode(true);
        }

        donePostProcessing = true;

    }
    /**
     * @param args the command line arguments
     */
    /*
     * public static void main(String args[]) {
     * java.awt.EventQueue.invokeLater(new Runnable() { public void run() { new
     * Adjudication().setVisible(true); } }); }
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel cardContainer_jPanel;
    private javax.swing.JButton jButton_cancel;
    private javax.swing.JButton jButton_next;
    private javax.swing.JCheckBox jCheckBox_check_attributes;
    private javax.swing.JCheckBox jCheckBox_check_comments;
    private javax.swing.JCheckBox jCheckBox_check_relationships;
    private javax.swing.JCheckBox jCheckBox_sameClasses;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_busySignal1;
    private javax.swing.JList jList_annotators;
    private javax.swing.JList jList_classes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton_check_overlapped_spans;
    private javax.swing.JRadioButton jRadioButton_check_same_spans;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
}

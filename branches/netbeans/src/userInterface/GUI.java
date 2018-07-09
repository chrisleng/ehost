/**Main Graphics User Interface of eHOST.
 *
 * This was first designed as an academic software whose only purpose was for 
 * paper. Later after June 2011, we tried to make it to be a business software. 
 * The first alpha release should be on June 15, 2011.
 *
 */
package userInterface;


import adjudication.SugarSeeder;
import adjudication.parameters.Paras;
import adjudication.statusBar.DiffCounter;
import gov.va.vinci.annotationAdmin.integration.AnnotationAdminComMgrEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import relationship.complex.creation.RelationshipSchemaEditor;
import relationship.complex.dataTypes.ComplexRelImportReturn;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotations.*;
import resultEditor.conflicts.classConflict;
import resultEditor.conflicts.spanOverlaps;
import resultEditor.conflicts.tmp_Conflicts;
import resultEditor.relationship.complex.RelCheckResult;
import resultEditor.relationship.iListable;
import resultEditor.workSpace.WorkSet;
import umls.UMLSBrowser;
import verifier.VerifyChallenge2011;
import webservices.AssignmentsScreen;
import workSpace.switcher.RecentWorkSpace;


/**
 * main GUI of eHOST<p>
 *
 * @author  Jianwei Leng (Chris)
 * @since   Feb 9, 2010, 2:34:52 PM
 * @since   JDK1.5
 */

public class GUI extends javax.swing.JFrame
{
    
    
    
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    protected enum fileInputType{
        importXMLandPin, selectClinicalTextFiles, setXmlOutputforConcepts
    };


    private AnnotationRelationship addingTo = null;

    /**a flag that used to indicate if there is any annotation got modified,
     * deleted, or added into current document*/
    private boolean modified = false;
    
    /** this path depend on operation system type, and it will be set to
     * correct value in output package */
    private resultEditor.relationship.Editor editor = null;
    private resultEditor.simpleSchema.Editor simpleEditor = null;
    private RelationshipSchemaEditor complexEditor = null;
    private resultEditor.annotationBuilder.Popmenu popmenu = null;
    private HotKeys hotkeyDialog = null;
    /**the dialog we used to create/modify/delete classes.*/
    private resultEditor.annotationClasses.Manager classmanager = null;           
    // output founder to screen?
    static boolean sentToScreen = false;
    private static boolean Flag_Want_Shutdown_Program = false;
    
    private AssignmentsScreen assignmentsScreen = null;

    private boolean FLAG_MAYBE_CHANGED = false;
    

    /**latest accessed folder in eHOST.*/
    private File recentlyOpennedFolder;

    /**standard border of text field*/
    protected javax.swing.border.Border STANDARD_TEXTFIELD_BOARD;
    final protected boolean RELEASE = true;

    /**the dialog of UMLS searching function*/
    public UMLSBrowser umlsbrowser;
    
    protected resultEditor.positionIndicator.JPositionIndicator jpositionIndicator;
    private enum infoScreens {
        CLASSCONFLICT,
        SPANCONFLICT,
        ANNOTATIONS,
        CLASSES,
        ANNOTATORS,
        VERIFIER,
        NONE
    }
    
    private infoScreens currentScreen = infoScreens.NONE;
    private HashSet<String> classesList = new HashSet<String>();
    private HashSet<String> annotatorsList = new HashSet<String>();
    private Vector<Annotation> annotationsList = new Vector<Annotation>();
    private Vector<classConflict> conflictWithWorking = new Vector<classConflict>();
    private Vector<spanOverlaps> overlappingAnnotations = new Vector<spanOverlaps>();
    private Vector<Annotation> verifierAnnotations = new Vector<Annotation>();
    private String annotationsText = "Number of Annotations: ";
    private String annotatorsText = "Number of Annotators: ";
    private String classesText = "Number of Markables: ";
    private String conflictsText = "Annotations in Conflict with Working Set: ";
    private String overlappingText = "Overlapping Annotations: ";
    private String verifierText = "Annotations Flagged by Verifier: ";

    /**This is the jPanel contains components and functions of the NLP assistatnt*/
    private annotate.gui.NLPAnnotator NLPAnnotationPanel = null;
    /**This is the jPanel contains components and functions of the NLP assistatnt,
     * it's the gui that show the process of NLP
     */
    private annotate.gui.NLPcpu nlpcpu = null;

    /**icons*/
    private static Icon icon_oracle_disabled, icon_oracle_enabled,
                   icon_graphicpath_disabled, icon_graphicpath_enabled,
                   icon_difference_disabled, icon_difference_enabled,
                   icon_attribute_enabled, icon_attribute_disabled;
    private static Icon icon_note, icon_note2;
    private static Icon icon_span, icon_spanaddingOn, icon_spanaddingOff;

    ///**set it to true while you just pop*/
    //private boolean newSubDialogJustPoped = false;
    /**font size of current document*/
    protected int currentFontSize = 14;


    KeyStroke kc_c= KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK);;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /** Creates new form GUIgate */
    public GUI()
    {


        /*
        try{
            javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName() );
        }catch(Exception ee){}*/
        loadRes();

        // #### init components
        initComponents();
        enableFunctionsByMask();
        // tell system about the Modal Mode of current application
        setModalExclusionType( Dialog.ModalExclusionType.APPLICATION_EXCLUDE); 

        updateComplex();
        
        // add file drop event for text source list
        dropEventOfTextSourceList();

        setHandCursor();
        
        setNAVCurrentTab(1);
        
        // reload panels into the cardlayout
        resetCardLayout();
        //jPanelDesktop.removeAll();
        jLabel_infobar.setText("Welcome to eHOST!");
        // set the width of first column of the table
         
        
        
        // **** end of setting the location of gui window

        // #### designate position on screen to this dialog
        setDialogPosition();
              
        // #### enter tab of result Editor as default start page
        tabDoorman( TabGuard.tabs.resulteditor );

        //((ResultEditor.CustomComponents.ExpandablePanel)NavigationPanel).defaultstatus();
        ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).defaultstatus();



        
        
        STANDARD_TEXTFIELD_BOARD = (new  javax.swing.JTextField()).getBorder();//jTextField_sample.getBorder();

        // subsequence processes after all components got initilized
        componentPostProcessing();

        updateScreen_for_variables();
        
        

        // #### check and force to ask user to set workspace setting while necessary
        display_hideEditor();
        setWorkSpace();
        
    }
    //</editor-fold>

    /**Hide the document viewer, editor and comparator panel by setting their
     * visible values to false.
     */
    public void display_hideEditor(){
        jCardcontainer_interactive.setVisible( false );
    }
    /**This method is let user hide or display the panel of document viewer.
     * @param _on_or_off
     *        true: display the panel of document viewer by setting it to visible;
     *        false: hide the panel of document viewer
     */
    public void display_document_viewer(boolean _on_or_off){
        this.jCardcontainer_interactive.setVisible(_on_or_off);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup_MainButton_startAnalysis = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        confirmExit = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        buttonGroup_Tabs = new javax.swing.ButtonGroup();
        jTextField_sample = new javax.swing.JTextField();
        buttonGroup_treeview = new javax.swing.ButtonGroup();
        jLabel_icon_annotationlist = new javax.swing.JLabel();
        buttonGroup_quicknlp = new javax.swing.ButtonGroup();
        jDialog_Confirm_before_Removing_Annotations = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        buttonGroup_listAnnotationsInSequences = new javax.swing.ButtonGroup();
        jSplitPane_Annotations_Comparator = new javax.swing.JSplitPane();
        buttonGroup1 = new javax.swing.ButtonGroup();
        ToolBar = new javax.swing.JToolBar();
        jToggleButton_ResultEditor = new javax.swing.JToggleButton();
        jToggleButton_CreateAnnotaion = new javax.swing.JToggleButton();
        jToggleButton_PinExtractor = new javax.swing.JToggleButton();
        jToggleButton_DictionaryManager = new javax.swing.JToggleButton();
        jToggleButton_Converter = new javax.swing.JToggleButton();
        jToggleButton_DictionarySetting = new javax.swing.JToggleButton();
        jToggle_AssignmentsScreen = new javax.swing.JToggleButton();
        jToggleButton_exit = new javax.swing.JToggleButton();
        BottomInfoBar = new javax.swing.JPanel();
        jPanel_leftSeparator = new javax.swing.JPanel();
        jPanel_infobar_right_current_annotator = new javax.swing.JPanel();
        jLabel_info_annotator = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel_info_annotatorlabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel_infobar_center = new javax.swing.JPanel();
        jLabel_infobar = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel_infobar_FlagOfOracle = new javax.swing.JLabel();
        jLabel_infobar_FlagOfDiff = new javax.swing.JLabel();
        jLabel_infobar_attributeEditor = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton_annotationMode = new javax.swing.JRadioButton();
        jRadioButton_adjudicationMode = new javax.swing.JRadioButton();
        GUIContainer = new javax.swing.JPanel();
        Editor = new javax.swing.JPanel();
        NavigationPanel1 = new navigatorContainer.TabPanel(this, 0);
        jPanel_NAV_CardContainer = new javax.swing.JPanel();
        jPanel_NAV_Card1 = new javax.swing.JPanel();
        jPanel8_topBar = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jPanel8_bottomBar = new javax.swing.JPanel();
        jButton_AddClinicalTexts1 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jPanel88 = new javax.swing.JPanel();
        jPanel8_currentWorkSpace = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jPanel95 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox_currentworkspace_abspath = new javax.swing.JComboBox();
        jPanel17 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList_NAV_projects = new javax.swing.JList();
        jPanel91 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jPanel_NAV_Card2 = new javax.swing.JPanel();
        jPanel8_topBar1 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel8_bottomBar1 = new javax.swing.JPanel();
        jButton_AddClinicalTexts = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel_corpuslist_outline = new javax.swing.JPanel();
        jPanel_corpuslist_innerline = new javax.swing.JPanel();
        jPanel99 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList_corpus = new javax.swing.JList();
        jPanel_NAV_Card3 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser6 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jToolBar5 = new javax.swing.JToolBar();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton23 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jToggleButton_sequence_inLocation = new javax.swing.JToggleButton();
        jToggleButton_sequence_inCharacters = new javax.swing.JToggleButton();
        jPanel57 = new javax.swing.JPanel();
        jRadioButton_treeview_currentarticle = new javax.swing.JRadioButton();
        jRadioButton_treeview_overall = new javax.swing.JRadioButton();
        jScrollPane_classtree = new javax.swing.JScrollPane();
        jTree_class = new javax.swing.JTree();
        jCardcontainer_interactive = new javax.swing.JPanel();
        jPanel_NLP = new javax.swing.JPanel();
        jSplitPane_between_viewer_and_allatttibutes = new javax.swing.JSplitPane();
        jPanel_interactive_left = new javax.swing.JPanel();
        jPanel73 = new javax.swing.JPanel();
        NavigationPanel_editor = new resultEditor.customComponents.ExpandablePanel_editor(jSplitPane_between_viewer_and_allatttibutes, GUIContainer);
        jPanel12 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        jComboBox_InputFileList = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton_importAnnotations = new javax.swing.JButton();
        jButton_removeduplicates = new javax.swing.JButton();
        jButton_save = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton_removeAllAnnotations = new javax.swing.JButton();
        jButton_save1 = new javax.swing.JButton();
        jButton_importAnnotations1 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton19 = new javax.swing.JButton();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel36 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jLabel_cursor = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jTextField_searchtext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane_textpane = new javax.swing.JScrollPane();
        textPaneforClinicalNotes = new userInterface.txtScreen.TextScreen();
        jPanel_position_indicator_container = new javax.swing.JPanel();
        jPanel77 = new javax.swing.JPanel();
        jPanel78 = new javax.swing.JPanel();
        jPanel79 = new javax.swing.JPanel();
        jPanel80 = new javax.swing.JPanel();
        jPanel82 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        classes = new javax.swing.JLabel();
        annotations = new javax.swing.JLabel();
        annotators = new javax.swing.JLabel();
        overlapping = new javax.swing.JLabel();
        workingConflicts = new javax.swing.JLabel();
        jPanel86 = new javax.swing.JPanel();
        jPanel92 = new javax.swing.JPanel();
        verifierFlagged = new javax.swing.JLabel();
        jPanel90 = new javax.swing.JPanel();
        verifierOnCurrent = new javax.swing.JButton();
        verifierOnAll = new javax.swing.JButton();
        jPanel81 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        infoList = new javax.swing.JList();
        jPanel83 = new javax.swing.JPanel();
        displayCurrent = new javax.swing.JButton();
        refresh = new javax.swing.JButton();
        jPanel_reportFormContainer = new javax.swing.JPanel();
        jPanel_MainFrame_Right = new javax.swing.JPanel();
        jPanel_EditorPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel_colorfulTextBar_filebrowser3 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel94 = new javax.swing.JPanel();
        jToolBar_editopanel_comparison = new javax.swing.JToolBar();
        jButton17 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel42 = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jButton_spaneditor_delete = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton_spaneditor_lefttToLeft = new javax.swing.JButton();
        jButton_spaneditor_leftToRight = new javax.swing.JButton();
        jButton_span_rightToLeft = new javax.swing.JButton();
        jButton4_spanEditor_rightToRight = new javax.swing.JButton();
        jPanel59 = new javax.swing.JPanel();
        jPanel60 = new userInterface.annotationCompare.ExpandButton(
            jPanel_MainFrame_Right,
            this.jSplitPane_between_viewer_and_allatttibutes,
            jPanel_comparator_container,
            jPanel_EditorPanel
        );
        jPanel41 = new javax.swing.JPanel();
        jPanel_multipleResultShowList = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList_selectedAnnotations = new javax.swing.JList();
        jPanel45 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jPanel74 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel_annotation_details = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jButton_SpanAdd = new javax.swing.JButton();
        jButton_SpanRemove = new javax.swing.JButton();
        jScrollPane_Spans = new javax.swing.JScrollPane();
        jList_Spans = new javax.swing.JList();
        jPanel19 = new javax.swing.JPanel();
        jPanel64 = new javax.swing.JPanel();
        jPanel65 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jTextField_annotationClassnames = new javax.swing.JTextField();
        jButton_SelectClass = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_comment = new javax.swing.JTextArea();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel48 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jPanel61 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jButton_cr = new javax.swing.JToggleButton();
        delete_Relationships = new javax.swing.JButton();
        jButton_relationships = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList_complexrelationships = new javax.swing.JList();
        jPanel71 = new javax.swing.JPanel();
        jPanel63 = new javax.swing.JPanel();
        jButton_attribute = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList_normalrelationship = new javax.swing.JList();
        jPanel75 = new javax.swing.JPanel();
        jPanel76 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jTextField_creationdate = new javax.swing.JTextField();
        jTextField_annotator = new javax.swing.JTextField();
        jPanel87 = new javax.swing.JPanel();
        jPanel84 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane_explanations = new javax.swing.JTextPane();
        jPanel_colorfulTextBar_filebrowser4 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jPanel72 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel_colorfulTextBar_filebrowser7 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jPanel_comparator_container = new javax.swing.JPanel();
        jPanel_textPane = new javax.swing.JPanel();
        JPanel_PinsExtractor = new javax.swing.JPanel();
        jPanel_Converter = new javax.swing.JPanel();

        jFileChooser1.setMultiSelectionEnabled(true);

        confirmExit.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        confirmExit.setTitle("Closing?");
        confirmExit.setAlwaysOnTop(true);
        confirmExit.setResizable(false);

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(238, 238, 238), 4));
        jPanel13.setMaximumSize(new java.awt.Dimension(388, 136));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/imac_01.png"))); // NOI18N
        jPanel14.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel14, java.awt.BorderLayout.WEST);

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel6.setText("Are you leaving eHOST?");
        jPanel15.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 26, 232, 22));

        jPanel13.add(jPanel15, java.awt.BorderLayout.CENTER);

        confirmExit.getContentPane().add(jPanel13, java.awt.BorderLayout.NORTH);

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(238, 238, 238), 3));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jButton7.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jButton7.setText("Yes");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jButton6.setText("No");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton7)
                    .add(jButton6))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.add(jPanel22, java.awt.BorderLayout.EAST);

        confirmExit.getContentPane().add(jPanel16, java.awt.BorderLayout.CENTER);

        res_conflictIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/conflictHuman.png"))); // NOI18N
        res_conflictIcon.setText("jLabel38");

        jTextField_sample.setText("jTextField1");

        jLabel_icon_annotationlist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/userInputIcon.png"))); // NOI18N
        jLabel_icon_annotationlist.setText("jLabel9");

        jDialog_Confirm_before_Removing_Annotations.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialog_Confirm_before_Removing_Annotations.setAlwaysOnTop(true);
        jDialog_Confirm_before_Removing_Annotations.setResizable(false);

        jPanel10.setBackground(new java.awt.Color(255, 255, 254));

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/warning_1.png"))); // NOI18N
        jLabel10.setLabelFor(refresh);

        jLabel7.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel7.setText("Are you sure to delete ALL annotations? All annotations will be lost if");

        jLabel15.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel15.setText("you click OK.");

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(43, 43, 43)
                .add(jLabel10)
                .add(18, 18, 18)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel7)
                    .add(jLabel15))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(30, 30, 30)
                        .add(jLabel10))
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(38, 38, 38)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel15)))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jDialog_Confirm_before_Removing_Annotations.getContentPane().add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(242, 240, 240));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(242, 240, 240), 4));

        jButton8.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jButton8.setText("Cancel");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton8);

        jButton11.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jButton11.setText("OK");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton11);

        jDialog_Confirm_before_Removing_Annotations.getContentPane().add(jPanel11, java.awt.BorderLayout.SOUTH);

        jSplitPane_Annotations_Comparator.setBackground(new java.awt.Color(220, 220, 220));
        jSplitPane_Annotations_Comparator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(222, 222, 222)));
        jSplitPane_Annotations_Comparator.setDividerLocation(200);
        jSplitPane_Annotations_Comparator.setDividerSize(7);
        jSplitPane_Annotations_Comparator.setResizeWeight(0.5);
        jSplitPane_Annotations_Comparator.setFocusable(false);
        jSplitPane_Annotations_Comparator.setVerifyInputWhenFocusTarget(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ColorMark/eHOST - MK6@2016.02.19");
        setBackground(new java.awt.Color(132, 122, 122));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        ToolBar.setBackground(new java.awt.Color(200, 200, 200));
        ToolBar.setFloatable(false);
        ToolBar.setRollover(true);
        ToolBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ToolBarMouseReleased(evt);
            }
        });

        buttonGroup_Tabs.add(jToggleButton_ResultEditor);
        jToggleButton_ResultEditor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_ResultEditor.setForeground(new java.awt.Color(41, 41, 41));
        jToggleButton_ResultEditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton_ResultEditor.setSelected(true);
        jToggleButton_ResultEditor.setText("<html>Annotation<br>Panel<html> ");
        jToggleButton_ResultEditor.setFocusable(false);
        jToggleButton_ResultEditor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_ResultEditor.setMaximumSize(new java.awt.Dimension(120, 120));
        jToggleButton_ResultEditor.setMinimumSize(new java.awt.Dimension(90, 0));
        jToggleButton_ResultEditor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_ResultEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_ResultEditorActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_ResultEditor);

        buttonGroup_Tabs.add(jToggleButton_CreateAnnotaion);
        jToggleButton_CreateAnnotaion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_CreateAnnotaion.setForeground(new java.awt.Color(0, 102, 102));
        jToggleButton_CreateAnnotaion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton_CreateAnnotaion.setText("<html>NLP<br>Assisted<html> ");
        jToggleButton_CreateAnnotaion.setFocusable(false);
        jToggleButton_CreateAnnotaion.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_CreateAnnotaion.setMaximumSize(new java.awt.Dimension(106, 100));
        jToggleButton_CreateAnnotaion.setMinimumSize(new java.awt.Dimension(106, 0));
        jToggleButton_CreateAnnotaion.setPreferredSize(new java.awt.Dimension(106, 43));
        jToggleButton_CreateAnnotaion.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_CreateAnnotaion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_CreateAnnotaionActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_CreateAnnotaion);

        buttonGroup_Tabs.add(jToggleButton_PinExtractor);
        jToggleButton_PinExtractor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_PinExtractor.setForeground(new java.awt.Color(41, 41, 41));
        jToggleButton_PinExtractor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/run.png"))); // NOI18N
        jToggleButton_PinExtractor.setText("<html>Pin<br>Extractor<html> ");
        jToggleButton_PinExtractor.setFocusable(false);
        jToggleButton_PinExtractor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_PinExtractor.setMaximumSize(new java.awt.Dimension(90, 100));
        jToggleButton_PinExtractor.setMinimumSize(new java.awt.Dimension(90, 0));
        jToggleButton_PinExtractor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_PinExtractor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_PinExtractorActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_PinExtractor);

        buttonGroup_Tabs.add(jToggleButton_DictionaryManager);
        jToggleButton_DictionaryManager.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_DictionaryManager.setForeground(new java.awt.Color(41, 41, 41));
        jToggleButton_DictionaryManager.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/run.png"))); // NOI18N
        jToggleButton_DictionaryManager.setText("<html>Dictionary<br>Manager<html> ");
        jToggleButton_DictionaryManager.setFocusable(false);
        jToggleButton_DictionaryManager.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_DictionaryManager.setMaximumSize(new java.awt.Dimension(100, 100));
        jToggleButton_DictionaryManager.setMinimumSize(new java.awt.Dimension(100, 0));
        jToggleButton_DictionaryManager.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_DictionaryManager.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_DictionaryManagerActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_DictionaryManager);

        buttonGroup_Tabs.add(jToggleButton_Converter);
        jToggleButton_Converter.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_Converter.setForeground(new java.awt.Color(41, 41, 41));
        jToggleButton_Converter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/configure.png"))); // NOI18N
        jToggleButton_Converter.setText("<html>Converter</html> ");
        jToggleButton_Converter.setFocusable(false);
        jToggleButton_Converter.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_Converter.setMaximumSize(new java.awt.Dimension(110, 43));
        jToggleButton_Converter.setMinimumSize(new java.awt.Dimension(110, 0));
        jToggleButton_Converter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_ConverterActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_Converter);

        buttonGroup_Tabs.add(jToggleButton_DictionarySetting);
        jToggleButton_DictionarySetting.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_DictionarySetting.setForeground(new java.awt.Color(41, 41, 41));
        jToggleButton_DictionarySetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/configure.png"))); // NOI18N
        jToggleButton_DictionarySetting.setText("<html>SYSTEM<br>Setting<html> ");
        jToggleButton_DictionarySetting.setFocusable(false);
        jToggleButton_DictionarySetting.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_DictionarySetting.setMaximumSize(new java.awt.Dimension(110, 100));
        jToggleButton_DictionarySetting.setMinimumSize(new java.awt.Dimension(110, 0));
        jToggleButton_DictionarySetting.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_DictionarySetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_DictionarySettingActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_DictionarySetting);

        buttonGroup_Tabs.add(jToggle_AssignmentsScreen);
        jToggle_AssignmentsScreen.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        jToggle_AssignmentsScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/sync.png"))); // NOI18N
        jToggle_AssignmentsScreen.setText("<html>Sync<p>Assignments<html>");
        jToggle_AssignmentsScreen.setFocusable(false);
        jToggle_AssignmentsScreen.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggle_AssignmentsScreen.setMaximumSize(new java.awt.Dimension(130, 100));
        jToggle_AssignmentsScreen.setMinimumSize(new java.awt.Dimension(100, 28));
        jToggle_AssignmentsScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_AssignmentScreenActionPerformed(evt);
            }
        });
        ToolBar.add(jToggle_AssignmentsScreen);

        buttonGroup_Tabs.add(jToggleButton_exit);
        jToggleButton_exit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jToggleButton_exit.setForeground(new java.awt.Color(102, 0, 51));
        jToggleButton_exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/exit.png"))); // NOI18N
        jToggleButton_exit.setFocusable(false);
        jToggleButton_exit.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToggleButton_exit.setLabel("EXIT ");
        jToggleButton_exit.setMaximumSize(new java.awt.Dimension(100, 100));
        jToggleButton_exit.setMinimumSize(new java.awt.Dimension(100, 0));
        jToggleButton_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_exitActionPerformed(evt);
            }
        });
        ToolBar.add(jToggleButton_exit);

        getContentPane().add(ToolBar, java.awt.BorderLayout.NORTH);

        BottomInfoBar.setBackground(new java.awt.Color(199, 183, 190));
        BottomInfoBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(183, 183, 183), 3));
        BottomInfoBar.setMinimumSize(new java.awt.Dimension(289, 34));
        BottomInfoBar.setPreferredSize(new java.awt.Dimension(500, 24));
        BottomInfoBar.setRequestFocusEnabled(false);
        BottomInfoBar.setVerifyInputWhenFocusTarget(false);
        BottomInfoBar.setLayout(new java.awt.BorderLayout());

        jPanel_leftSeparator.setBackground(new java.awt.Color(183, 183, 183));
        jPanel_leftSeparator.setMinimumSize(new java.awt.Dimension(4, 0));
        jPanel_leftSeparator.setPreferredSize(new java.awt.Dimension(2, 22));

        org.jdesktop.layout.GroupLayout jPanel_leftSeparatorLayout = new org.jdesktop.layout.GroupLayout(jPanel_leftSeparator);
        jPanel_leftSeparator.setLayout(jPanel_leftSeparatorLayout);
        jPanel_leftSeparatorLayout.setHorizontalGroup(
            jPanel_leftSeparatorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 4, Short.MAX_VALUE)
        );
        jPanel_leftSeparatorLayout.setVerticalGroup(
            jPanel_leftSeparatorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 18, Short.MAX_VALUE)
        );

        BottomInfoBar.add(jPanel_leftSeparator, java.awt.BorderLayout.WEST);

        jPanel_infobar_right_current_annotator.setBackground(new java.awt.Color(183, 183, 183));
        jPanel_infobar_right_current_annotator.setForeground(new java.awt.Color(204, 204, 204));
        jPanel_infobar_right_current_annotator.setLayout(new java.awt.BorderLayout());

        jLabel_info_annotator.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel_info_annotator.setText("jLabel18");
        jLabel_info_annotator.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_info_annotatorMouseClicked(evt);
            }
        });
        jPanel_infobar_right_current_annotator.add(jLabel_info_annotator, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(183, 183, 183));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel_info_annotatorlabel.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel_info_annotatorlabel.setForeground(new java.awt.Color(0, 102, 102));
        jLabel_info_annotatorlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/User-2-icon.png"))); // NOI18N
        jLabel_info_annotatorlabel.setText(" ");
        jLabel_info_annotatorlabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_info_annotatorMouseClicked(evt);
            }
        });
        jPanel2.add(jLabel_info_annotatorlabel, java.awt.BorderLayout.CENTER);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, java.awt.BorderLayout.WEST);

        jPanel_infobar_right_current_annotator.add(jPanel2, java.awt.BorderLayout.WEST);

        BottomInfoBar.add(jPanel_infobar_right_current_annotator, java.awt.BorderLayout.EAST);

        jPanel_infobar_center.setBackground(new java.awt.Color(183, 183, 183));
        jPanel_infobar_center.setMinimumSize(new java.awt.Dimension(200, 24));
        jPanel_infobar_center.setPreferredSize(new java.awt.Dimension(525, 24));
        jPanel_infobar_center.setLayout(new java.awt.BorderLayout());

        jLabel_infobar.setBackground(new java.awt.Color(183, 183, 183));
        jLabel_infobar.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel_infobar.setText("jLabel1");
        jLabel_infobar.setMinimumSize(new java.awt.Dimension(400, 16));
        jLabel_infobar.setPreferredSize(new java.awt.Dimension(400, 16));
        jLabel_infobar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_infobarMouseClicked(evt);
            }
        });
        jPanel_infobar_center.add(jLabel_infobar, java.awt.BorderLayout.CENTER);

        jPanel58.setBackground(new java.awt.Color(183, 183, 183));
        jPanel58.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(183, 183, 183));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0, 4, 0));

        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/fire.png"))); // NOI18N
        jLabel22.setToolTipText("Hot keys");
        jLabel22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel22MouseReleased(evt);
            }
        });
        jPanel1.add(jLabel22);

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel1.add(jSeparator6);

        jLabel_infobar_FlagOfOracle.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel_infobar_FlagOfOracle.setForeground(new java.awt.Color(0, 102, 102));
        jLabel_infobar_FlagOfOracle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/oracle_icon_s_disabled.png"))); // NOI18N
        jLabel_infobar_FlagOfOracle.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_infobar_FlagOfOraclejLabel_info_annotatorMouseClicked(evt);
            }
        });
        jPanel1.add(jLabel_infobar_FlagOfOracle);

        jLabel_infobar_FlagOfDiff.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel_infobar_FlagOfDiff.setForeground(new java.awt.Color(0, 102, 102));
        jLabel_infobar_FlagOfDiff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/path-difference.png"))); // NOI18N
        jLabel_infobar_FlagOfDiff.setToolTipText(" Difference Matching");
        jLabel_infobar_FlagOfDiff.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_infobar_FlagOfDiffMouseClicked(evt);
            }
        });
        jPanel1.add(jLabel_infobar_FlagOfDiff);

        jLabel_infobar_attributeEditor.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel_infobar_attributeEditor.setForeground(new java.awt.Color(0, 102, 102));
        jLabel_infobar_attributeEditor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/attribute_disabled.png"))); // NOI18N
        jLabel_infobar_attributeEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_infobar_attributeEditorMouseClicked(evt);
            }
        });
        jPanel1.add(jLabel_infobar_attributeEditor);

        jPanel58.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel3.setBackground(new java.awt.Color(183, 183, 183));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        jRadioButton_annotationMode.setBackground(new java.awt.Color(183, 183, 183));
        buttonGroup1.add(jRadioButton_annotationMode);
        jRadioButton_annotationMode.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jRadioButton_annotationMode.setSelected(true);
        jRadioButton_annotationMode.setText("Annotation Mode");
        jRadioButton_annotationMode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jRadioButton_annotationModeMouseReleased(evt);
            }
        });
        jPanel3.add(jRadioButton_annotationMode);

        jRadioButton_adjudicationMode.setBackground(new java.awt.Color(183, 183, 183));
        buttonGroup1.add(jRadioButton_adjudicationMode);
        jRadioButton_adjudicationMode.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jRadioButton_adjudicationMode.setText("Adjudication Mode");
        jRadioButton_adjudicationMode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jRadioButton_adjudicationModeMouseReleased(evt);
            }
        });
        jPanel3.add(jRadioButton_adjudicationMode);

        jPanel58.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel_infobar_center.add(jPanel58, java.awt.BorderLayout.EAST);

        BottomInfoBar.add(jPanel_infobar_center, java.awt.BorderLayout.CENTER);

        getContentPane().add(BottomInfoBar, java.awt.BorderLayout.SOUTH);

        GUIContainer.setBackground(new java.awt.Color(237, 237, 237));
        GUIContainer.setForeground(new java.awt.Color(204, 204, 204));
        GUIContainer.setPreferredSize(new java.awt.Dimension(890, 440));
        GUIContainer.setRequestFocusEnabled(false);
        GUIContainer.setVerifyInputWhenFocusTarget(false);
        GUIContainer.setLayout(new java.awt.CardLayout());

        Editor.setBackground(new java.awt.Color(102, 102, 102));
        Editor.setLayout(new java.awt.BorderLayout());

        NavigationPanel1.setPreferredSize(new java.awt.Dimension(200, 485));
        NavigationPanel1.setLayout(new java.awt.BorderLayout());

        jPanel_NAV_CardContainer.setBackground(new java.awt.Color(238, 238, 239));
        jPanel_NAV_CardContainer.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 0, 0, new java.awt.Color(153, 153, 153)));
        jPanel_NAV_CardContainer.setLayout(new java.awt.CardLayout());

        jPanel_NAV_Card1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 1, 1, new java.awt.Color(0, 0, 51)));
        jPanel_NAV_Card1.setLayout(new java.awt.BorderLayout());

        jPanel8_topBar.setBackground(new java.awt.Color(0, 51, 102));
        jPanel8_topBar.setLayout(new java.awt.BorderLayout());

        jLabel31.setBackground(new java.awt.Color(41, 119, 167));
        jLabel31.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(204, 204, 204));
        jLabel31.setText("WORKSPACE & PROJECT");
        jLabel31.setMaximumSize(new java.awt.Dimension(5000, 19));
        jLabel31.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel31.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel31.setRequestFocusEnabled(false);
        jLabel31.setVerifyInputWhenFocusTarget(false);
        jPanel8_topBar.add(jLabel31, java.awt.BorderLayout.CENTER);

        jPanel_NAV_Card1.add(jPanel8_topBar, java.awt.BorderLayout.NORTH);

        jPanel8_bottomBar.setBackground(new java.awt.Color(237, 237, 237));
        jPanel8_bottomBar.setLayout(new java.awt.GridLayout(1, 2, 2, 0));

        jButton_AddClinicalTexts1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jButton_AddClinicalTexts1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/add.png"))); // NOI18N
        jButton_AddClinicalTexts1.setText("New");
        jButton_AddClinicalTexts1.setDefaultCapable(false);
        jButton_AddClinicalTexts1.setFocusable(false);
        jButton_AddClinicalTexts1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton_AddClinicalTexts1.setMaximumSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts1.setMinimumSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts1.setPreferredSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton_AddClinicalTexts1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddClinicalTexts1ActionPerformed(evt);
            }
        });
        jPanel8_bottomBar.add(jButton_AddClinicalTexts1);

        jButton26.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/delete.png"))); // NOI18N
        jButton26.setFocusable(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton26.setMinimumSize(new java.awt.Dimension(90, 27));
        jButton26.setPreferredSize(new java.awt.Dimension(77, 30));
        jButton26.setVerifyInputWhenFocusTarget(false);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jPanel8_bottomBar.add(jButton26);

        jPanel_NAV_Card1.add(jPanel8_bottomBar, java.awt.BorderLayout.SOUTH);

        jPanel88.setLayout(new java.awt.BorderLayout());

        jPanel8_currentWorkSpace.setBackground(new java.awt.Color(237, 237, 237));
        jPanel8_currentWorkSpace.setMaximumSize(new java.awt.Dimension(297, 87));
        jPanel8_currentWorkSpace.setPreferredSize(new java.awt.Dimension(197, 87));
        jPanel8_currentWorkSpace.setLayout(new java.awt.BorderLayout());

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/workspace2.png"))); // NOI18N
        jPanel8_currentWorkSpace.add(jLabel18, java.awt.BorderLayout.WEST);

        jPanel95.setBackground(new java.awt.Color(237, 237, 237));
        jPanel95.setLayout(new java.awt.GridLayout(3, 0));

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel9.setText(" My WorkSpace:");
        jPanel95.add(jLabel9);

        jComboBox_currentworkspace_abspath.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_currentworkspace_abspath.setMinimumSize(null);
        jComboBox_currentworkspace_abspath.setPreferredSize(null);
        jComboBox_currentworkspace_abspath.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_currentworkspace_abspathItemStateChanged(evt);
            }
        });
        jPanel95.add(jComboBox_currentworkspace_abspath);

        jPanel17.setBackground(new java.awt.Color(238, 238, 239));

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/back.png"))); // NOI18N
        jButton5.setText("Change");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .add(jButton5)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .add(jButton5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel95.add(jPanel17);

        jPanel8_currentWorkSpace.add(jPanel95, java.awt.BorderLayout.CENTER);

        jPanel88.add(jPanel8_currentWorkSpace, java.awt.BorderLayout.NORTH);

        jPanel47.setBackground(new java.awt.Color(238, 238, 237));
        jPanel47.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(238, 238, 237)));
        jPanel47.setLayout(new java.awt.BorderLayout());

        jScrollPane11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane11.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList_NAV_projects.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_NAV_projects.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_NAV_projectsMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(jList_NAV_projects);

        jPanel47.add(jScrollPane11, java.awt.BorderLayout.CENTER);

        jPanel91.setBackground(new java.awt.Color(238, 238, 239));
        jPanel91.setLayout(new java.awt.BorderLayout());
        jPanel91.add(jSeparator3, java.awt.BorderLayout.NORTH);

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 51, 153));
        jLabel20.setText("Projects in workspace:");
        jPanel91.add(jLabel20, java.awt.BorderLayout.SOUTH);

        jPanel47.add(jPanel91, java.awt.BorderLayout.PAGE_START);

        jPanel88.add(jPanel47, java.awt.BorderLayout.CENTER);

        jPanel_NAV_Card1.add(jPanel88, java.awt.BorderLayout.CENTER);

        jPanel_NAV_CardContainer.add(jPanel_NAV_Card1, "card4");

        jPanel_NAV_Card2.setBackground(new java.awt.Color(0, 51, 102));
        jPanel_NAV_Card2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 1, 1, new java.awt.Color(0, 0, 51)));
        jPanel_NAV_Card2.setPreferredSize(new java.awt.Dimension(0, 0));
        jPanel_NAV_Card2.setLayout(new java.awt.BorderLayout());

        jPanel8_topBar1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel8_topBar1.setLayout(new java.awt.BorderLayout());

        jLabel32.setBackground(new java.awt.Color(0, 51, 102));
        jLabel32.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(204, 204, 204));
        jLabel32.setText("DOCUMENTS");
        jLabel32.setMaximumSize(new java.awt.Dimension(5000, 19));
        jLabel32.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel32.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel32.setRequestFocusEnabled(false);
        jLabel32.setVerifyInputWhenFocusTarget(false);
        jPanel8_topBar1.add(jLabel32, java.awt.BorderLayout.CENTER);

        jPanel_NAV_Card2.add(jPanel8_topBar1, java.awt.BorderLayout.NORTH);

        jPanel8_bottomBar1.setBackground(new java.awt.Color(237, 237, 237));
        jPanel8_bottomBar1.setLayout(new java.awt.GridLayout(1, 0));

        jButton_AddClinicalTexts.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jButton_AddClinicalTexts.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/add.png"))); // NOI18N
        jButton_AddClinicalTexts.setText("Add files");
        jButton_AddClinicalTexts.setDefaultCapable(false);
        jButton_AddClinicalTexts.setFocusable(false);
        jButton_AddClinicalTexts.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton_AddClinicalTexts.setMaximumSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts.setMinimumSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts.setPreferredSize(new java.awt.Dimension(100, 27));
        jButton_AddClinicalTexts.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jButton_AddClinicalTexts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddClinicalTextsActionPerformed(evt);
            }
        });
        jPanel8_bottomBar1.add(jButton_AddClinicalTexts);

        jButton2.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/delete.png"))); // NOI18N
        jButton2.setText("Remove");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton2.setMaximumSize(new java.awt.Dimension(90, 27));
        jButton2.setMinimumSize(new java.awt.Dimension(90, 27));
        jButton2.setPreferredSize(new java.awt.Dimension(77, 30));
        jButton2.setVerifyInputWhenFocusTarget(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel8_bottomBar1.add(jButton2);

        jPanel_NAV_Card2.add(jPanel8_bottomBar1, java.awt.BorderLayout.SOUTH);

        jPanel_corpuslist_outline.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_corpuslist_outline.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(238, 239, 240), 2));
        jPanel_corpuslist_outline.setLayout(new java.awt.BorderLayout());

        jPanel_corpuslist_innerline.setBackground(new java.awt.Color(238, 238, 239));
        jPanel_corpuslist_innerline.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jPanel_corpuslist_innerline.setLayout(new java.awt.BorderLayout());

        jPanel99.setBackground(new java.awt.Color(255, 255, 255));
        jPanel99.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(238, 238, 237)));
        jPanel99.setLayout(new java.awt.BorderLayout());

        jLabel21.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 102));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/return.jpg"))); // NOI18N
        jLabel21.setText("Go back to project list ...");
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });
        jPanel99.add(jLabel21, java.awt.BorderLayout.CENTER);

        jPanel_corpuslist_innerline.add(jPanel99, java.awt.BorderLayout.NORTH);

        jScrollPane12.setBorder(null);
        jScrollPane12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jList_corpus.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jList_corpus.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_corpus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_corpusMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(jList_corpus);

        jPanel_corpuslist_innerline.add(jScrollPane12, java.awt.BorderLayout.CENTER);

        jPanel_corpuslist_outline.add(jPanel_corpuslist_innerline, java.awt.BorderLayout.CENTER);

        jPanel_NAV_Card2.add(jPanel_corpuslist_outline, java.awt.BorderLayout.CENTER);

        jPanel_NAV_CardContainer.add(jPanel_NAV_Card2, "card3");

        jPanel_NAV_Card3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 2, 1, 1, new java.awt.Color(0, 0, 51)));
        jPanel_NAV_Card3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel_NAV_Card3MousePressed(evt);
            }
        });
        jPanel_NAV_Card3.setLayout(new java.awt.BorderLayout());

        jPanel_colorfulTextBar_filebrowser6.setBackground(new java.awt.Color(0, 102, 204));
        jPanel_colorfulTextBar_filebrowser6.setLayout(new java.awt.BorderLayout());

        jLabel28.setBackground(new java.awt.Color(0, 102, 204));
        jLabel28.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText(" NAVIGATOR");
        jLabel28.setMaximumSize(new java.awt.Dimension(5000, 19));
        jLabel28.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel28.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel28.setRequestFocusEnabled(false);
        jLabel28.setVerifyInputWhenFocusTarget(false);
        jPanel_colorfulTextBar_filebrowser6.add(jLabel28, java.awt.BorderLayout.CENTER);

        jToolBar5.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar5.setFloatable(false);
        jToolBar5.setBorderPainted(false);
        jToolBar5.setFocusable(false);
        jToolBar5.setRequestFocusEnabled(false);
        jToolBar5.setVerifyInputWhenFocusTarget(false);
        jToolBar5.add(jSeparator7);

        jButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/classDef.gif"))); // NOI18N
        jButton23.setToolTipText("Annotation Schema Editor");
        jButton23.setBorderPainted(false);
        jButton23.setFocusable(false);
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton23.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton23);

        jButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/attributeDef.gif"))); // NOI18N
        jButton16.setToolTipText("Attribute Schema Editor");
        jButton16.setBorderPainted(false);
        jButton16.setFocusable(false);
        jButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton16);

        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/classRelDef.gif"))); // NOI18N
        jButton15.setToolTipText("Relationship Schema Editor");
        jButton15.setBorderPainted(false);
        jButton15.setContentAreaFilled(false);
        jButton15.setFocusable(false);
        jButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jToolBar5.add(jButton15);
        jToolBar5.add(jSeparator5);

        buttonGroup_listAnnotationsInSequences.add(jToggleButton_sequence_inLocation);
        jToggleButton_sequence_inLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/list_sequence.png"))); // NOI18N
        jToggleButton_sequence_inLocation.setToolTipText("Sort list by order of appearance");
        jToggleButton_sequence_inLocation.setFocusable(false);
        jToggleButton_sequence_inLocation.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton_sequence_inLocation.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_sequence_inLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_sequence_inLocationActionPerformed(evt);
            }
        });
        jToolBar5.add(jToggleButton_sequence_inLocation);

        buttonGroup_listAnnotationsInSequences.add(jToggleButton_sequence_inCharacters);
        jToggleButton_sequence_inCharacters.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/list_chars.png"))); // NOI18N
        jToggleButton_sequence_inCharacters.setToolTipText("Sort list alphabetically");
        jToggleButton_sequence_inCharacters.setFocusable(false);
        jToggleButton_sequence_inCharacters.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton_sequence_inCharacters.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton_sequence_inCharacters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton_sequence_inCharactersActionPerformed(evt);
            }
        });
        jToolBar5.add(jToggleButton_sequence_inCharacters);

        jPanel_colorfulTextBar_filebrowser6.add(jToolBar5, java.awt.BorderLayout.SOUTH);

        jPanel_NAV_Card3.add(jPanel_colorfulTextBar_filebrowser6, java.awt.BorderLayout.NORTH);

        jPanel57.setBackground(new java.awt.Color(237, 237, 237));
        jPanel57.setLayout(new java.awt.GridLayout(1, 0));

        buttonGroup_treeview.add(jRadioButton_treeview_currentarticle);
        jRadioButton_treeview_currentarticle.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jRadioButton_treeview_currentarticle.setSelected(true);
        jRadioButton_treeview_currentarticle.setText("Current Note");
        jRadioButton_treeview_currentarticle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_treeview_currentarticleActionPerformed(evt);
            }
        });
        jPanel57.add(jRadioButton_treeview_currentarticle);

        buttonGroup_treeview.add(jRadioButton_treeview_overall);
        jRadioButton_treeview_overall.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jRadioButton_treeview_overall.setText("All Notes");
        jRadioButton_treeview_overall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_treeview_overallActionPerformed(evt);
            }
        });
        jPanel57.add(jRadioButton_treeview_overall);

        jPanel_NAV_Card3.add(jPanel57, java.awt.BorderLayout.SOUTH);

        jScrollPane_classtree.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(0, 102, 204)));
        jScrollPane_classtree.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTree_class.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTree_class.setToolTipText(getClassNodeHint());
        jTree_class.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
            }
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
                jTree_classTreeWillCollapse(evt);
            }
        });
        jTree_class.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTree_classMousePressed(evt);
            }
        });
        jTree_class.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                jTree_classTreeExpanded(evt);
            }
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
                jTree_classTreeCollapsed(evt);
            }
        });
        jScrollPane_classtree.setViewportView(jTree_class);

        jPanel_NAV_Card3.add(jScrollPane_classtree, java.awt.BorderLayout.CENTER);

        jPanel_NAV_CardContainer.add(jPanel_NAV_Card3, "card2");

        NavigationPanel1.add(jPanel_NAV_CardContainer, java.awt.BorderLayout.CENTER);

        Editor.add(NavigationPanel1, java.awt.BorderLayout.WEST);

        jCardcontainer_interactive.setLayout(new java.awt.CardLayout());

        jPanel_NLP.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_NLP.setForeground(new java.awt.Color(215, 211, 210));
        jPanel_NLP.setLayout(new java.awt.BorderLayout());
        jCardcontainer_interactive.add(jPanel_NLP, "card2");

        jSplitPane_between_viewer_and_allatttibutes.setBackground(new java.awt.Color(220, 220, 220));
        jSplitPane_between_viewer_and_allatttibutes.setBorder(null);
        jSplitPane_between_viewer_and_allatttibutes.setDividerLocation(700);
        jSplitPane_between_viewer_and_allatttibutes.setDividerSize(7);
        jSplitPane_between_viewer_and_allatttibutes.setResizeWeight(1.0);

        jPanel_interactive_left.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel_interactive_left.setPreferredSize(new java.awt.Dimension(200, 166));
        jPanel_interactive_left.setLayout(new java.awt.BorderLayout());

        jPanel73.setBackground(new java.awt.Color(237, 237, 237));
        jPanel73.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(238, 238, 238), 3));
        jPanel73.setLayout(new java.awt.BorderLayout());

        NavigationPanel_editor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        NavigationPanel_editor.setMaximumSize(new java.awt.Dimension(21, 0));
        NavigationPanel_editor.setPreferredSize(new java.awt.Dimension(21, 635));

        org.jdesktop.layout.GroupLayout NavigationPanel_editorLayout = new org.jdesktop.layout.GroupLayout(NavigationPanel_editor);
        NavigationPanel_editor.setLayout(NavigationPanel_editorLayout);
        NavigationPanel_editorLayout.setHorizontalGroup(
            NavigationPanel_editorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 17, Short.MAX_VALUE)
        );
        NavigationPanel_editorLayout.setVerticalGroup(
            NavigationPanel_editorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 786, Short.MAX_VALUE)
        );

        jPanel73.add(NavigationPanel_editor, java.awt.BorderLayout.EAST);

        jPanel12.setBackground(new java.awt.Color(238, 238, 237));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel37.setBackground(new java.awt.Color(236, 233, 216));
        jPanel37.setLayout(new java.awt.BorderLayout());

        jPanel_colorfulTextBar_filebrowser2.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser2.setLayout(new java.awt.BorderLayout());

        jLabel25.setBackground(new java.awt.Color(41, 119, 167));
        jLabel25.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(230, 230, 230));
        jLabel25.setText("  Document Viewer");
        jLabel25.setMaximumSize(new java.awt.Dimension(5000, 19));
        jLabel25.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel25.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel25.setRequestFocusEnabled(false);
        jLabel25.setVerifyInputWhenFocusTarget(false);
        jLabel25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel25MouseClicked(evt);
            }
        });
        jPanel_colorfulTextBar_filebrowser2.add(jLabel25, java.awt.BorderLayout.CENTER);

        jPanel37.add(jPanel_colorfulTextBar_filebrowser2, java.awt.BorderLayout.NORTH);

        jPanel8.setBackground(new java.awt.Color(236, 233, 216));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jToolBar3.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar3.setFloatable(false);
        jToolBar3.setBorderPainted(false);
        jToolBar3.setFocusable(false);
        jToolBar3.setRequestFocusEnabled(false);
        jToolBar3.setVerifyInputWhenFocusTarget(false);

        jComboBox_InputFileList.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jComboBox_InputFileList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_InputFileList.setMaximumSize(new java.awt.Dimension(32767, 22));
        jComboBox_InputFileList.setPreferredSize(new java.awt.Dimension(56, 22));
        jComboBox_InputFileList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_InputFileListActionPerformed(evt);
            }
        });
        jToolBar3.add(jComboBox_InputFileList);

        jLabel3.setBackground(new java.awt.Color(238, 238, 239));
        jLabel3.setOpaque(true);
        jToolBar3.add(jLabel3);

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/nextb.png"))); // NOI18N
        jButton13.setToolTipText("Previous note");
        jButton13.setBorderPainted(false);
        jButton13.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton13.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton13.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton13);

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/back.png"))); // NOI18N
        jButton14.setToolTipText("Next note");
        jButton14.setBorderPainted(false);
        jButton14.setFocusable(false);
        jButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton14.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton14.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton14);
        jToolBar3.add(jSeparator4);

        jButton_importAnnotations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/addtab.png"))); // NOI18N
        jButton_importAnnotations.setToolTipText("<html>Import Annotations from knowtator<br> XML files or PINS files.<html>");
        jButton_importAnnotations.setBorderPainted(false);
        jButton_importAnnotations.setFocusable(false);
        jButton_importAnnotations.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_importAnnotations.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_importAnnotations.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_importAnnotations.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_importAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_importAnnotationsActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_importAnnotations);

        jButton_removeduplicates.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/removeduplicates.png"))); // NOI18N
        jButton_removeduplicates.setToolTipText("Error Checker");
        jButton_removeduplicates.setBorderPainted(false);
        jButton_removeduplicates.setFocusable(false);
        jButton_removeduplicates.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_removeduplicates.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_removeduplicates.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton_removeduplicates.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_removeduplicates.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_removeduplicates.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_removeduplicatesActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_removeduplicates);

        jButton_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/save.png"))); // NOI18N
        jButton_save.setToolTipText("<html>Quick Save</html>");
        jButton_save.setBorderPainted(false);
        jButton_save.setFocusable(false);
        jButton_save.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_save.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_save.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_saveActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_save);

        jButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/saveas.png"))); // NOI18N
        jButton20.setToolTipText("Save As");
        jButton20.setBorderPainted(false);
        jButton20.setFocusable(false);
        jButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton20.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton20.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton20.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton20);

        jButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/font2.png"))); // NOI18N
        jButton21.setToolTipText("Decrease font");
        jButton21.setBorderPainted(false);
        jButton21.setFocusable(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton21.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton21.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton21.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton21);

        jButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/font1.png"))); // NOI18N
        jButton22.setToolTipText("Increase font");
        jButton22.setBorderPainted(false);
        jButton22.setFocusable(false);
        jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton22.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton22.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton22.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton22);

        jButton_removeAllAnnotations.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/clearConsoleButtonGlyph.png"))); // NOI18N
        jButton_removeAllAnnotations.setToolTipText("Delete all annotations.");
        jButton_removeAllAnnotations.setBorderPainted(false);
        jButton_removeAllAnnotations.setFocusable(false);
        jButton_removeAllAnnotations.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_removeAllAnnotations.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_removeAllAnnotations.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton_removeAllAnnotations.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_removeAllAnnotations.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_removeAllAnnotations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_removeAllAnnotationsActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_removeAllAnnotations);

        jButton_save1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/images-preannotated.jpg"))); // NOI18N
        jButton_save1.setToolTipText("<html>Pre-Annotated for Pairs</html>");
        jButton_save1.setBorderPainted(false);
        jButton_save1.setFocusable(false);
        jButton_save1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_save1.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_save1.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton_save1.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_save1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_save1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_save1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_save1);

        jButton_importAnnotations1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/images-preannotated.jpg"))); // NOI18N
        jButton_importAnnotations1.setToolTipText("<html>Import Annotations from knowtator<br> XML files or PINS files.<html>");
        jButton_importAnnotations1.setBorderPainted(false);
        jButton_importAnnotations1.setFocusable(false);
        jButton_importAnnotations1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_importAnnotations1.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_importAnnotations1.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_importAnnotations1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_importAnnotations1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_importAnnotations1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton_importAnnotations1);

        jPanel8.add(jToolBar3, java.awt.BorderLayout.CENTER);

        jPanel37.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel24.setBackground(new java.awt.Color(236, 233, 216));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jToolBar6.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar6.setFloatable(false);
        jToolBar6.setBorderPainted(false);
        jToolBar6.setFocusable(false);
        jToolBar6.setRequestFocusEnabled(false);
        jToolBar6.setVerifyInputWhenFocusTarget(false);
        jToolBar6.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jToolBar6MouseMoved(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(238, 238, 239));
        jLabel4.setOpaque(true);
        jToolBar6.add(jLabel4);

        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/nextb.png"))); // NOI18N
        jButton18.setText("Previous");
        jButton18.setToolTipText("Previous note");
        jButton18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 0)));
        jButton18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton18.setMaximumSize(new java.awt.Dimension(83, 23));
        jButton18.setMinimumSize(new java.awt.Dimension(83, 23));
        jButton18.setPreferredSize(new java.awt.Dimension(83, 23));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton18);

        jLabel24.setText(" ( ");
        jToolBar6.add(jLabel24);

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 13)); // NOI18N
        jLabel23.setText(" ALL SOLVED ");
        jToolBar6.add(jLabel23);

        jLabel30.setText(" ) ");
        jToolBar6.add(jLabel30);

        jButton19.setBackground(new java.awt.Color(204, 204, 255));
        jButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/back.png"))); // NOI18N
        jButton19.setText("Next");
        jButton19.setToolTipText("Next note");
        jButton19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 0)));
        jButton19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton19.setMaximumSize(new java.awt.Dimension(93, 23));
        jButton19.setMinimumSize(new java.awt.Dimension(93, 23));
        jButton19.setPreferredSize(new java.awt.Dimension(93, 23));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });
        jToolBar6.add(jButton19);

        jPanel24.add(jToolBar6, java.awt.BorderLayout.CENTER);

        jPanel37.add(jPanel24, java.awt.BorderLayout.SOUTH);

        jPanel12.add(jPanel37, java.awt.BorderLayout.PAGE_START);

        jTabbedPane3.setBackground(new java.awt.Color(238, 238, 237));
        jTabbedPane3.setForeground(new java.awt.Color(102, 102, 102));
        jTabbedPane3.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jTabbedPane3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane3StateChanged(evt);
            }
        });

        jPanel36.setBackground(new java.awt.Color(236, 233, 216));
        jPanel36.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel36.setPreferredSize(new java.awt.Dimension(200, 122));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jPanel43.setBackground(new java.awt.Color(237, 237, 237));
        jPanel43.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel43.setPreferredSize(new java.awt.Dimension(200, 36));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jLabel_cursor.setForeground(new java.awt.Color(102, 102, 102));
        jPanel43.add(jLabel_cursor, java.awt.BorderLayout.CENTER);

        jPanel38.setBackground(new java.awt.Color(237, 237, 237));
        jPanel38.setLayout(new java.awt.BorderLayout());

        jTextField_searchtext.setBackground(new java.awt.Color(237, 237, 237));
        jTextField_searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_searchtextKeyTyped(evt);
            }
        });
        jPanel38.add(jTextField_searchtext, java.awt.BorderLayout.CENTER);

        jButton1.setBackground(new java.awt.Color(237, 237, 237));
        jButton1.setText("Find");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel38.add(jButton1, java.awt.BorderLayout.EAST);

        jPanel43.add(jPanel38, java.awt.BorderLayout.NORTH);

        jPanel36.add(jPanel43, java.awt.BorderLayout.SOUTH);

        jPanel46.setBackground(new java.awt.Color(237, 237, 237));
        jPanel46.setLayout(new java.awt.BorderLayout());

        jScrollPane_textpane.setBorder(null);

        textPaneforClinicalNotes.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 1, 4, 1, new java.awt.Color(255, 255, 255)));
        textPaneforClinicalNotes.setFont(new java.awt.Font("Courier", 0, 12)); // NOI18N
        textPaneforClinicalNotes.setDoubleBuffered(true);
        textPaneforClinicalNotes.setDropMode(javax.swing.DropMode.INSERT);
        textPaneforClinicalNotes.setMinimumSize(new java.awt.Dimension(1, 1));
        textPaneforClinicalNotes.setPreferredSize(new java.awt.Dimension(200, 20));
        textPaneforClinicalNotes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                textPaneforClinicalNotesMouseReleased(evt);
            }
        });
        textPaneforClinicalNotes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textPaneforClinicalNotesKeyPressed(evt);
            }
        });
        jScrollPane_textpane.setViewportView(textPaneforClinicalNotes);

        jPanel46.add(jScrollPane_textpane, java.awt.BorderLayout.CENTER);

        jPanel_position_indicator_container.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_position_indicator_container.setMinimumSize(new java.awt.Dimension(20, 0));
        jPanel_position_indicator_container.setPreferredSize(new java.awt.Dimension(20, 0));
        jPanel_position_indicator_container.setLayout(new java.awt.BorderLayout());
        jPanel46.add(jPanel_position_indicator_container, java.awt.BorderLayout.EAST);

        jPanel36.add(jPanel46, java.awt.BorderLayout.CENTER);

        jTabbedPane3.addTab("Text Display", jPanel36);

        jPanel77.setLayout(new java.awt.BorderLayout());

        jPanel78.setBackground(new java.awt.Color(237, 237, 237));
        jPanel78.setLayout(new java.awt.BorderLayout());

        jPanel79.setBackground(new java.awt.Color(237, 237, 237));
        jPanel79.setLayout(new java.awt.BorderLayout());

        jPanel80.setBackground(new java.awt.Color(237, 237, 237));
        jPanel80.setLayout(new java.awt.GridLayout(8, 1, 7, 7));

        jPanel82.setBackground(new java.awt.Color(237, 237, 237));
        jPanel82.setLayout(new java.awt.BorderLayout());

        jLabel26.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel26.setText("Current Document Summary");
        jPanel82.add(jLabel26, java.awt.BorderLayout.CENTER);

        jPanel80.add(jPanel82);

        classes.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        classes.setText("Number of Markables:");
        classes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                classesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                classesMouseEntered(evt);
            }
        });
        jPanel80.add(classes);

        annotations.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        annotations.setText("Number of Annotations:");
        annotations.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annotationsMouseClicked(evt);
            }
        });
        jPanel80.add(annotations);

        annotators.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        annotators.setText("Number of Annotators:");
        annotators.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annotatorsMouseClicked(evt);
            }
        });
        jPanel80.add(annotators);

        overlapping.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        overlapping.setText("Overlapping Annotations:");
        overlapping.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                overlappingMouseClicked(evt);
            }
        });
        jPanel80.add(overlapping);

        workingConflicts.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        workingConflicts.setText("Annotations in Conflict with Working Set: ");
        workingConflicts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workingConflictsMouseClicked(evt);
            }
        });
        jPanel80.add(workingConflicts);

        jPanel86.setBackground(new java.awt.Color(237, 237, 237));
        jPanel86.setLayout(new java.awt.GridLayout(1, 0));

        jPanel92.setBackground(new java.awt.Color(237, 237, 237));
        jPanel92.setLayout(new java.awt.BorderLayout());

        verifierFlagged.setText("Annotations Flagged by Verifier:");
        verifierFlagged.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                verifierFlaggedMouseClicked(evt);
            }
        });
        jPanel92.add(verifierFlagged, java.awt.BorderLayout.CENTER);

        jPanel86.add(jPanel92);

        jPanel90.setBackground(new java.awt.Color(237, 237, 237));
        jPanel90.setLayout(new java.awt.GridLayout(1, 0));

        verifierOnCurrent.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        verifierOnCurrent.setText("Run Verifier(Current)");
        verifierOnCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifierOnCurrentActionPerformed(evt);
            }
        });
        jPanel90.add(verifierOnCurrent);

        verifierOnAll.setText("Run Verifier(All)");
        verifierOnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifierOnAllActionPerformed(evt);
            }
        });
        jPanel90.add(verifierOnAll);

        jPanel86.add(jPanel90);

        jPanel80.add(jPanel86);

        jPanel81.setBackground(new java.awt.Color(237, 237, 237));
        jPanel81.setLayout(new java.awt.GridLayout(1, 2));

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Currently Viewing:  ");
        jPanel81.add(jLabel1);

        jLabel29.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jPanel81.add(jLabel29);

        jPanel80.add(jPanel81);

        jPanel79.add(jPanel80, java.awt.BorderLayout.CENTER);

        jPanel78.add(jPanel79, java.awt.BorderLayout.PAGE_START);

        jScrollPane4.setBackground(new java.awt.Color(237, 237, 237));

        infoList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoListMouseClicked(evt);
            }
        });
        infoList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                infoListValueChanged(evt);
            }
        });
        infoList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                infoListMouseMoved(evt);
            }
        });
        jScrollPane4.setViewportView(infoList);

        jPanel78.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel83.setBackground(new java.awt.Color(237, 237, 237));
        jPanel83.setLayout(new java.awt.GridLayout(1, 0));

        displayCurrent.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        displayCurrent.setText("Display Current List In Viewer");
        displayCurrent.setToolTipText("<html>Only show the currently displayed annotations<br> on the clinical notes</html>");
        displayCurrent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayCurrentActionPerformed(evt);
            }
        });
        jPanel83.add(displayCurrent);

        refresh.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });
        jPanel83.add(refresh);

        jPanel78.add(jPanel83, java.awt.BorderLayout.PAGE_END);

        jPanel77.add(jPanel78, java.awt.BorderLayout.CENTER);

        jTabbedPane3.addTab("Annotation Information", jPanel77);

        jPanel_reportFormContainer.setBackground(new java.awt.Color(237, 237, 237));

        org.jdesktop.layout.GroupLayout jPanel_reportFormContainerLayout = new org.jdesktop.layout.GroupLayout(jPanel_reportFormContainer);
        jPanel_reportFormContainer.setLayout(jPanel_reportFormContainerLayout);
        jPanel_reportFormContainerLayout.setHorizontalGroup(
            jPanel_reportFormContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 652, Short.MAX_VALUE)
        );
        jPanel_reportFormContainerLayout.setVerticalGroup(
            jPanel_reportFormContainerLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 675, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Reports", jPanel_reportFormContainer);

        jPanel12.add(jTabbedPane3, java.awt.BorderLayout.CENTER);

        jPanel73.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel_interactive_left.add(jPanel73, java.awt.BorderLayout.CENTER);

        jSplitPane_between_viewer_and_allatttibutes.setLeftComponent(jPanel_interactive_left);

        jPanel_MainFrame_Right.setBackground(new java.awt.Color(238, 238, 237));
        jPanel_MainFrame_Right.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel_EditorPanel.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_EditorPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));
        jPanel_EditorPanel.setMaximumSize(new java.awt.Dimension(4000, 4000));
        jPanel_EditorPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel_EditorPanel.setLayout(new java.awt.BorderLayout());

        jPanel9.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel9.setPreferredSize(new java.awt.Dimension(319, 777));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel5.setPreferredSize(new java.awt.Dimension(257, 285));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel40.setBackground(new java.awt.Color(236, 233, 216));
        jPanel40.setPreferredSize(new java.awt.Dimension(390, 180));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jPanel_colorfulTextBar_filebrowser3.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser3.setLayout(new java.awt.BorderLayout());

        jLabel27.setBackground(new java.awt.Color(41, 119, 167));
        jLabel27.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(230, 230, 230));
        jLabel27.setText(" Annotation Editor");
        jLabel27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel27.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel27.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel27.setRequestFocusEnabled(false);
        jLabel27.setVerifyInputWhenFocusTarget(false);
        jPanel_colorfulTextBar_filebrowser3.add(jLabel27, java.awt.BorderLayout.NORTH);

        jPanel94.setLayout(new java.awt.BorderLayout());

        jToolBar_editopanel_comparison.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar_editopanel_comparison.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar_editopanel_comparison.setFloatable(false);
        jToolBar_editopanel_comparison.setBorderPainted(false);
        jToolBar_editopanel_comparison.setFocusable(false);
        jToolBar_editopanel_comparison.setFont(new java.awt.Font("Bangla MN", 0, 13)); // NOI18N
        jToolBar_editopanel_comparison.setMinimumSize(new java.awt.Dimension(72, 26));
        jToolBar_editopanel_comparison.setPreferredSize(new java.awt.Dimension(0, 26));
        jToolBar_editopanel_comparison.setRequestFocusEnabled(false);
        jToolBar_editopanel_comparison.setVerifyInputWhenFocusTarget(false);

        jButton17.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton17.setText("Accept");
        jButton17.setFocusable(false);
        jButton17.setMaximumSize(new java.awt.Dimension(52, 24));
        jButton17.setMinimumSize(new java.awt.Dimension(0, 24));
        jButton17.setPreferredSize(new java.awt.Dimension(52, 24));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton17);

        jButton24.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton24.setText("Reject");
        jButton24.setFocusable(false);
        jButton24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton24.setMaximumSize(new java.awt.Dimension(52, 24));
        jButton24.setMinimumSize(new java.awt.Dimension(0, 24));
        jButton24.setPreferredSize(new java.awt.Dimension(52, 24));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton24);

        jButton9.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton9.setText("AcceptAll");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setMaximumSize(new java.awt.Dimension(70, 24));
        jButton9.setMinimumSize(new java.awt.Dimension(70, 24));
        jButton9.setPreferredSize(new java.awt.Dimension(70, 24));
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar_editopanel_comparison.add(jButton9);

        jPanel94.add(jToolBar_editopanel_comparison, java.awt.BorderLayout.SOUTH);

        jPanel42.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        jPanel42.setMinimumSize(new java.awt.Dimension(50, 26));
        jPanel42.setPreferredSize(new java.awt.Dimension(138, 26));
        jPanel42.setLayout(new java.awt.BorderLayout());

        jToolBar4.setBackground(new java.awt.Color(237, 237, 237));
        jToolBar4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar4.setFloatable(false);
        jToolBar4.setBorderPainted(false);
        jToolBar4.setFocusable(false);
        jToolBar4.setRequestFocusEnabled(false);
        jToolBar4.setVerifyInputWhenFocusTarget(false);
        jToolBar4.add(jSeparator8);

        jButton_spaneditor_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/closetab.png"))); // NOI18N
        jButton_spaneditor_delete.setToolTipText("Delete selected annotation");
        jButton_spaneditor_delete.setBorderPainted(false);
        jButton_spaneditor_delete.setContentAreaFilled(false);
        jButton_spaneditor_delete.setMaximumSize(new java.awt.Dimension(23, 23));
        jButton_spaneditor_delete.setMinimumSize(new java.awt.Dimension(23, 23));
        jButton_spaneditor_delete.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_spaneditor_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_spaneditor_deleteActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton_spaneditor_delete);
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

        jPanel42.add(jToolBar4, java.awt.BorderLayout.CENTER);

        jPanel59.setBackground(new java.awt.Color(237, 237, 237));
        jPanel59.setLayout(new java.awt.BorderLayout());

        jPanel60.setBackground(new java.awt.Color(237, 237, 237));
        jPanel59.add(jPanel60, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel59, java.awt.BorderLayout.EAST);

        jPanel94.add(jPanel42, java.awt.BorderLayout.NORTH);

        jPanel_colorfulTextBar_filebrowser3.add(jPanel94, java.awt.BorderLayout.CENTER);

        jPanel40.add(jPanel_colorfulTextBar_filebrowser3, java.awt.BorderLayout.NORTH);

        jPanel41.setBackground(new java.awt.Color(237, 237, 237));
        jPanel41.setMaximumSize(new java.awt.Dimension(32767, 280));
        jPanel41.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel_multipleResultShowList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 3));
        jPanel_multipleResultShowList.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel_multipleResultShowList.setLayout(new java.awt.BorderLayout());

        jScrollPane5.setBorder(null);
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane5.setMinimumSize(null);

        jList_selectedAnnotations.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jList_selectedAnnotations.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jList_selectedAnnotations.setSelectionBackground(new java.awt.Color(74, 136, 218));
        jList_selectedAnnotations.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_selectedAnnotationsValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(jList_selectedAnnotations);

        jPanel_multipleResultShowList.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel45.setBackground(new java.awt.Color(237, 237, 237));

        jLabel12.setBackground(new java.awt.Color(236, 233, 216));
        jLabel12.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 102, 102));
        jLabel12.setText("Selected Annotations:");
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel12.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel12.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel12.setPreferredSize(new java.awt.Dimension(0, 16));
        jLabel12.setRequestFocusEnabled(false);
        jLabel12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        org.jdesktop.layout.GroupLayout jPanel45Layout = new org.jdesktop.layout.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel45Layout.createSequentialGroup()
                .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jPanel_multipleResultShowList.add(jPanel45, java.awt.BorderLayout.NORTH);

        jPanel50.setBackground(new java.awt.Color(237, 237, 237));
        jPanel50.setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        jPanel74.setLayout(new java.awt.BorderLayout());
        jPanel50.add(jPanel74);

        jPanel_multipleResultShowList.add(jPanel50, java.awt.BorderLayout.SOUTH);

        jPanel41.add(jPanel_multipleResultShowList, java.awt.BorderLayout.CENTER);

        jPanel40.add(jPanel41, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(165, 198, 230));
        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 1));
        jPanel4.setPreferredSize(new java.awt.Dimension(0, 2));

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 239, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 2, Short.MAX_VALUE)
        );

        jPanel40.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jPanel5.add(jPanel40, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(237, 237, 237));
        jPanel7.setLayout(new java.awt.GridLayout(1, 2, 3, 0));

        jPanel_annotation_details.setBackground(new java.awt.Color(240, 240, 240));
        jPanel_annotation_details.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel_annotation_details.setLayout(new java.awt.BorderLayout());

        jLabel19.setBackground(new java.awt.Color(236, 233, 216));
        jLabel19.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 102, 102));
        jLabel19.setText("Span:");
        jLabel19.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel19.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 1, 1, 1, new java.awt.Color(238, 238, 240)));
        jLabel19.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel_annotation_details.add(jLabel19, java.awt.BorderLayout.NORTH);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(238, 238, 240));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(238, 238, 240));
        jPanel23.setLayout(new java.awt.GridLayout(2, 1, 0, 2));

        jButton_SpanAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span-add.png"))); // NOI18N
        jButton_SpanAdd.setToolTipText("Add a span to this annotation");
        jButton_SpanAdd.setBorderPainted(false);
        jButton_SpanAdd.setContentAreaFilled(false);
        jButton_SpanAdd.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton_SpanAdd.setMinimumSize(new java.awt.Dimension(1, 1));
        jButton_SpanAdd.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_SpanAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SpanAddActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_SpanAdd);

        jButton_SpanRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/span-remove.png"))); // NOI18N
        jButton_SpanRemove.setToolTipText("<html>Delete this span.</html>");
        jButton_SpanRemove.setBorderPainted(false);
        jButton_SpanRemove.setContentAreaFilled(false);
        jButton_SpanRemove.setMaximumSize(new java.awt.Dimension(28, 23));
        jButton_SpanRemove.setMinimumSize(new java.awt.Dimension(1, 1));
        jButton_SpanRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jButton_SpanRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SpanRemoveActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_SpanRemove);

        jPanel21.add(jPanel23, java.awt.BorderLayout.NORTH);

        jPanel20.add(jPanel21, java.awt.BorderLayout.EAST);

        jScrollPane_Spans.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jList_Spans.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_Spans.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_SpansValueChanged(evt);
            }
        });
        jScrollPane_Spans.setViewportView(jList_Spans);

        jPanel20.add(jScrollPane_Spans, java.awt.BorderLayout.CENTER);

        jPanel_annotation_details.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel_annotation_details);

        jPanel19.setBackground(new java.awt.Color(240, 240, 240));
        jPanel19.setLayout(new java.awt.BorderLayout());

        org.jdesktop.layout.GroupLayout jPanel64Layout = new org.jdesktop.layout.GroupLayout(jPanel64);
        jPanel64.setLayout(jPanel64Layout);
        jPanel64Layout.setHorizontalGroup(
            jPanel64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 118, Short.MAX_VALUE)
        );
        jPanel64Layout.setVerticalGroup(
            jPanel64Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        jPanel19.add(jPanel64, java.awt.BorderLayout.NORTH);

        jPanel65.setBackground(new java.awt.Color(237, 237, 237));
        jPanel65.setLayout(new java.awt.BorderLayout());

        jPanel55.setBackground(new java.awt.Color(237, 237, 237));
        jPanel55.setMaximumSize(new java.awt.Dimension(2147483647, 70));
        jPanel55.setPreferredSize(new java.awt.Dimension(0, 72));
        jPanel55.setLayout(new java.awt.BorderLayout());

        jPanel39.setBackground(new java.awt.Color(237, 237, 237));
        jPanel39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel39.setMaximumSize(new java.awt.Dimension(32767, 112));
        jPanel39.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel39.setPreferredSize(new java.awt.Dimension(0, 67));
        jPanel39.setLayout(new java.awt.GridLayout(3, 0, 0, 2));

        jLabel16.setBackground(new java.awt.Color(236, 233, 216));
        jLabel16.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setText("Class:");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel16.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel16.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel39.add(jLabel16);

        jPanel44.setBackground(new java.awt.Color(237, 237, 237));
        jPanel44.setLayout(new java.awt.BorderLayout());

        jTextField_annotationClassnames.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextField_annotationClassnames.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jTextField_annotationClassnames.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextField_annotationClassnames.setEnabled(false);
        jPanel44.add(jTextField_annotationClassnames, java.awt.BorderLayout.CENTER);

        jButton_SelectClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/instancewatch.png"))); // NOI18N
        jButton_SelectClass.setToolTipText("Change category of current annotation.");
        jButton_SelectClass.setFocusable(false);
        jButton_SelectClass.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_SelectClass.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_SelectClass.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton_SelectClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SelectClassActionPerformed(evt);
            }
        });
        jPanel44.add(jButton_SelectClass, java.awt.BorderLayout.EAST);

        jPanel39.add(jPanel44);

        jLabel38.setBackground(new java.awt.Color(236, 233, 216));
        jLabel38.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(102, 102, 102));
        jLabel38.setText("Comment:");
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel38.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel38.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel38.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel39.add(jLabel38);

        jPanel55.add(jPanel39, java.awt.BorderLayout.CENTER);

        jPanel65.add(jPanel55, java.awt.BorderLayout.NORTH);

        jPanel66.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel66.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextArea_comment.setBackground(new java.awt.Color(240, 240, 240));
        jTextArea_comment.setColumns(20);
        jTextArea_comment.setEditable(false);
        jTextArea_comment.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextArea_comment.setLineWrap(true);
        jTextArea_comment.setRows(5);
        jTextArea_comment.setBorder(null);
        jTextArea_comment.setDisabledTextColor(new java.awt.Color(0, 1, 0));
        jTextArea_comment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextArea_commentMouseClicked(evt);
            }
        });
        jTextArea_comment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea_commentFocusLost(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea_comment);

        jPanel66.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel65.add(jPanel66, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel65, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel19);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel5, java.awt.BorderLayout.NORTH);

        jSplitPane5.setBackground(new java.awt.Color(237, 237, 237));
        jSplitPane5.setBorder(null);
        jSplitPane5.setDividerLocation(300);
        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane5.setResizeWeight(0.5);

        jPanel48.setBackground(new java.awt.Color(237, 237, 237));
        jPanel48.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel48.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel48.setPreferredSize(new java.awt.Dimension(439, 200));
        jPanel48.setLayout(new java.awt.GridLayout(2, 0, 0, 2));

        jPanel70.setLayout(new java.awt.BorderLayout());

        jPanel61.setBackground(new java.awt.Color(237, 237, 237));
        jPanel61.setLayout(new java.awt.BorderLayout());

        jLabel39.setBackground(new java.awt.Color(236, 233, 216));
        jLabel39.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(102, 102, 102));
        jLabel39.setText("Relationships:");
        jLabel39.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel39.setFocusable(false);
        jLabel39.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel39.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel39.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel61.add(jLabel39, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(238, 238, 239));
        jPanel18.setLayout(new java.awt.GridLayout(1, 2, 1, 0));

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
        jPanel18.add(jButton_cr);

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
        jPanel18.add(delete_Relationships);

        jButton_relationships.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/instancewatch.png"))); // NOI18N
        jButton_relationships.setFocusable(false);
        jButton_relationships.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_relationships.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_relationships.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton_relationships.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_relationshipsActionPerformed(evt);
            }
        });
        jPanel18.add(jButton_relationships);

        jPanel61.add(jPanel18, java.awt.BorderLayout.EAST);

        jPanel70.add(jPanel61, java.awt.BorderLayout.NORTH);

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane8.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList_complexrelationships.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_complexrelationshipsMouseClicked(evt);
            }
        });
        jList_complexrelationships.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_complexrelationshipsValueChanged(evt);
            }
        });
        jScrollPane8.setViewportView(jList_complexrelationships);

        jPanel70.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jPanel48.add(jPanel70);

        jPanel71.setLayout(new java.awt.BorderLayout());

        jPanel63.setBackground(new java.awt.Color(237, 237, 237));
        jPanel63.setLayout(new java.awt.BorderLayout());

        jButton_attribute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/instancewatch.png"))); // NOI18N
        jButton_attribute.setFocusable(false);
        jButton_attribute.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_attribute.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton_attribute.setPreferredSize(new java.awt.Dimension(20, 20));
        jButton_attribute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_attributeActionPerformed(evt);
            }
        });
        jPanel63.add(jButton_attribute, java.awt.BorderLayout.EAST);

        jLabel40.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(102, 102, 102));
        jLabel40.setText("Attributes:");
        jLabel40.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel40.setFocusable(false);
        jLabel40.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel40.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel63.add(jLabel40, java.awt.BorderLayout.CENTER);

        jPanel71.add(jPanel63, java.awt.BorderLayout.NORTH);

        jScrollPane9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane9.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList_normalrelationship.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_normalrelationship.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_normalrelationshipMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(jList_normalrelationship);

        jPanel71.add(jScrollPane9, java.awt.BorderLayout.CENTER);

        jPanel48.add(jPanel71);

        jSplitPane5.setLeftComponent(jPanel48);

        jPanel75.setBackground(new java.awt.Color(237, 237, 237));
        jPanel75.setLayout(new java.awt.BorderLayout());

        jPanel76.setBackground(new java.awt.Color(237, 237, 237));
        jPanel76.setLayout(new java.awt.GridLayout(2, 2, 2, 2));

        jLabel37.setBackground(new java.awt.Color(236, 233, 216));
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
        jTextField_annotator.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField_annotatorMouseClicked(evt);
            }
        });
        jTextField_annotator.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_annotatorFocusLost(evt);
            }
        });
        jPanel76.add(jTextField_annotator);

        jPanel75.add(jPanel76, java.awt.BorderLayout.NORTH);

        jPanel87.setLayout(new java.awt.BorderLayout());

        jPanel84.setBackground(new java.awt.Color(238, 238, 237));
        jPanel84.setLayout(new java.awt.GridLayout(1, 2, 2, 0));

        jPanel68.setBackground(new java.awt.Color(237, 237, 237));
        jPanel68.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel68.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(236, 233, 216));
        jTabbedPane1.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(32767, 320));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1, 1));

        jPanel6.setLayout(new java.awt.BorderLayout());

        jList3.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jList3.setMaximumSize(new java.awt.Dimension(1, 1));
        jList3.setMinimumSize(new java.awt.Dimension(1, 1));
        jList3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList3ValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(jList3);

        jPanel6.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Suggestions", jPanel6);

        jPanel27.setLayout(new java.awt.BorderLayout());

        jTextPane_explanations.setEditable(false);
        jTextPane_explanations.setMinimumSize(new java.awt.Dimension(1, 1));
        jScrollPane7.setViewportView(jTextPane_explanations);

        jPanel27.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Rule Explanations", jPanel27);

        jPanel68.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel_colorfulTextBar_filebrowser4.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser4.setLayout(new java.awt.BorderLayout());
        jPanel68.add(jPanel_colorfulTextBar_filebrowser4, java.awt.BorderLayout.NORTH);

        jPanel69.setBackground(new java.awt.Color(237, 237, 237));
        jPanel69.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel69.setLayout(new java.awt.GridLayout(1, 0, 4, 4));

        jButton12.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jButton12.setText("Set Annotation To Selected");
        jButton12.setToolTipText("<html>Modify the selected annotation to match<br> the currently selected suggestion</html>");
        jButton12.setEnabled(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel69.add(jButton12);

        jPanel68.add(jPanel69, java.awt.BorderLayout.SOUTH);

        jPanel84.add(jPanel68);

        jPanel72.setBackground(new java.awt.Color(237, 237, 237));
        jPanel72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 240), 2));
        jPanel72.setMinimumSize(new java.awt.Dimension(1, 1));
        jPanel72.setLayout(new java.awt.BorderLayout());

        jLabel41.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(153, 153, 153));
        jLabel41.setText("Saved Verify Suggestion:");
        jLabel41.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel41.setMaximumSize(new java.awt.Dimension(0, 25));
        jLabel41.setMinimumSize(new java.awt.Dimension(0, 25));
        jLabel41.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel72.add(jLabel41, java.awt.BorderLayout.NORTH);

        jPanel84.add(jPanel72);

        jPanel87.add(jPanel84, java.awt.BorderLayout.CENTER);

        jPanel_colorfulTextBar_filebrowser7.setBackground(new java.awt.Color(41, 119, 167));
        jPanel_colorfulTextBar_filebrowser7.setLayout(new java.awt.BorderLayout());

        jLabel42.setBackground(new java.awt.Color(41, 119, 167));
        jLabel42.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("  VERIFIER");
        jLabel42.setMaximumSize(new java.awt.Dimension(5000, 19));
        jLabel42.setMinimumSize(new java.awt.Dimension(1, 1));
        jLabel42.setPreferredSize(new java.awt.Dimension(200, 19));
        jLabel42.setRequestFocusEnabled(false);
        jLabel42.setVerifyInputWhenFocusTarget(false);
        jPanel_colorfulTextBar_filebrowser7.add(jLabel42, java.awt.BorderLayout.CENTER);

        jPanel87.add(jPanel_colorfulTextBar_filebrowser7, java.awt.BorderLayout.NORTH);

        jPanel75.add(jPanel87, java.awt.BorderLayout.CENTER);

        jSplitPane5.setRightComponent(jPanel75);

        jPanel9.add(jSplitPane5, java.awt.BorderLayout.CENTER);

        jPanel_EditorPanel.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel_MainFrame_Right.add(jPanel_EditorPanel);

        jPanel_comparator_container.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_comparator_container.setLayout(new java.awt.BorderLayout());
        jPanel_MainFrame_Right.add(jPanel_comparator_container);

        jSplitPane_between_viewer_and_allatttibutes.setRightComponent(jPanel_MainFrame_Right);

        jCardcontainer_interactive.add(jSplitPane_between_viewer_and_allatttibutes, "card2");

        Editor.add(jCardcontainer_interactive, java.awt.BorderLayout.CENTER);

        GUIContainer.add(Editor, "card6");

        jPanel_textPane.setBackground(new java.awt.Color(237, 237, 237));
        jPanel_textPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200), 3));
        jPanel_textPane.setLayout(new java.awt.BorderLayout());
        GUIContainer.add(jPanel_textPane, "card3");

        JPanel_PinsExtractor.setBackground(new java.awt.Color(237, 237, 237));

        org.jdesktop.layout.GroupLayout JPanel_PinsExtractorLayout = new org.jdesktop.layout.GroupLayout(JPanel_PinsExtractor);
        JPanel_PinsExtractor.setLayout(JPanel_PinsExtractorLayout);
        JPanel_PinsExtractorLayout.setHorizontalGroup(
            JPanel_PinsExtractorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1394, Short.MAX_VALUE)
        );
        JPanel_PinsExtractorLayout.setVerticalGroup(
            JPanel_PinsExtractorLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 796, Short.MAX_VALUE)
        );

        GUIContainer.add(JPanel_PinsExtractor, "card5");

        jPanel_Converter.setBackground(new java.awt.Color(237, 237, 237));

        org.jdesktop.layout.GroupLayout jPanel_ConverterLayout = new org.jdesktop.layout.GroupLayout(jPanel_Converter);
        jPanel_Converter.setLayout(jPanel_ConverterLayout);
        jPanel_ConverterLayout.setHorizontalGroup(
            jPanel_ConverterLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1394, Short.MAX_VALUE)
        );
        jPanel_ConverterLayout.setVerticalGroup(
            jPanel_ConverterLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 796, Short.MAX_VALUE)
        );

        GUIContainer.add(jPanel_Converter, "card6");

        getContentPane().add(GUIContainer, java.awt.BorderLayout.CENTER);

        getAccessibleContext().setAccessibleName("eHOST - 1.137");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //<editor-fold defaultstate="collapsed" desc="Generated Events - 1">
    /**Show dialogs of file chooser and add selected files into memory,<br>
     * then refresh the table of inputed file list.<br>
     */
    private void jButton_AddClinicalTextsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddClinicalTextsActionPerformed

        //#### select new txt file
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setMultiSelectionEnabled(true);
        String[] type = {"txt", "TXT", "Txt"};
        fc.setFileFilter( new commons.SimpleFileFilter(type, "text files") );
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // show the dialog to let user select txt file
        int flag = fc.showDialog(this, "Select Text Files");

        // if user selected some text files
        if(flag==JFileChooser.APPROVE_OPTION){

            File[] selectedfiles = fc.getSelectedFiles();

            if(selectedfiles==null)
                return;

            // raise new dialog to copy these text files to current project
            if(selectedfiles.length>0)
            {
                corpusImport.Copying cp = new corpusImport.Copying(this,
                        selectedfiles, env.Parameters.WorkSpace.CurrentProject);
                cp.setVisible(true);
            }
        }
        
    }//GEN-LAST:event_jButton_AddClinicalTextsActionPerformed


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your process code here:

        int size = jList_corpus.getModel().getSize();

        boolean isCurrentDocumentGotDeleted = false;
        // if row i has beed selected in the table, delete matched records in these arraylist
        int i = size - 1;
        File currentfile = resultEditor.workSpace.WorkSet.getCurrentFile();

        while (i >= 0)
        {
            if (jList_corpus.isSelectedIndex(i))
            {
                File f = env.Parameters.corpus.LIST_ClinicalNotes.get(i).file;

                if (( f != null)||(currentfile != null)){
                    if (f.equals( currentfile ))
                    {
                        
                        isCurrentDocumentGotDeleted = true;
                    }
                }
                f.delete();

                env.Parameters.corpus.LIST_ClinicalNotes.remove(i);

            }            
            i--;
        }

        this.refreshFileList();

        if( isCurrentDocumentGotDeleted )
            enterTab_ResultEditor();

        config.system.SysConf.saveSystemConfigure();

        
        //cleanAndReload_ListOfFileCollection();

        // save changes in configuration file
        

       
            int sizew = env.Parameters.corpus.getSize();
            jComboBox_InputFileList.removeAllItems();
            // show inputted file into the combobox
            for (int qi = 0; qi < sizew; qi++) {
                jComboBox_InputFileList.addItem(env.Parameters.corpus.getFileName(qi));
            }
       


    }//GEN-LAST:event_jButton2ActionPerformed
    // insert text into textpanel with assigned color
    /*public void insertDocument(String _text, Color _textColor,
            int _fontSize, javax.swing.JTextPane _textPanel) {
        
        if (_text == null)  return;
        if (_text.length() < 1) return;
       
        if (_textColor == null)     _textColor = Color.black;
        if (_fontSize <= 1) _fontSize = 12;
        
        try {
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setForeground(set, _textColor);
            StyleConstants.setFontSize(set, _fontSize);
            Document doc = _textPanel.getStyledDocument();

            doc.insertString(doc.getLength(), _text, set);
        } catch (Exception e) {
        }
    }*/

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        Flag_Want_Shutdown_Program = false;
        confirmExit.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        Flag_Want_Shutdown_Program = true;
        // save all configuration information from memory to disk
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                    env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();
        confirmExit.dispose();
}//GEN-LAST:event_jButton7ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        // popup a dialog to ask whether you want to save modificaiton or not
        // if there is any changed have been made on annotations.
        this.saveModification();
        
        // save global setting for eHOST
        config.system.SysConf.saveSystemConfigure();
        // save setting for current project
        config_saveProjectSetting();
    }//GEN-LAST:event_formWindowClosing
  

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span
        // record when is the last time that user clicked next/previous button
        // to go next or previous document
        env.Parameters.lastTimeToCallDocument = System.currentTimeMillis();


        // ##1##
        // exit from annotation comparison mode if editor panel and comparator
        // panel are showed for comparing annotation conflits
        try{
            ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1012011313:: fail to leave annotations compaison mode!!!");
        }

        // temporary mute for speeding on June 22, 2011
        //MUSTMUST resetVerifier();
        goBack_ChoicedFile();
        disableAnnotationDisplay();
        if(jTabbedPane3.getSelectedIndex() != 0)
            jTabbedPane3.getSelectedComponent().repaint();
        
    }//GEN-LAST:event_jButton13ActionPerformed



    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        setFlag_allowToAddSpan(  false ); // cancel possible operation of adding new span
        try{
            // record when is the last time that user clicked next/previous button
            // to go next or previous document
            env.Parameters.lastTimeToCallDocument = System.currentTimeMillis();

            //long start = System.currentTimeMillis(), point;
            //System.out.println("1 start: "+start);

            // ##1##
            // exit from annotation comparison mode if editor panel and comparator
            // panel are showed for comparing annotation conflits
            try{
                ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
            }catch(Exception ex){
                log.LoggingToFile.log(Level.SEVERE, "error 1012011313:: fail to leave annotations compaison mode!!!");
            }

            //point= System.currentTimeMillis();
            //System.out.println("2 set status to jpanel 60: "+ (point- start));
            //start = point;

            // temporary mute for speeding on June 22, 2011
            //MUSTMUST resetVerifier();
            gotoNextChoicedFile();
            //point= System.currentTimeMillis();
            //System.out.println("3 go to next file: "+ (point- start));
            //start = point;


            disableAnnotationDisplay();
            //point= System.currentTimeMillis();
            //System.out.println("4 disable buttons and reset components: "+ (point- start));
            //start = point;

            if(jTabbedPane3.getSelectedIndex() != 0)
                jTabbedPane3.getSelectedComponent().repaint();

            //point= System.currentTimeMillis();
            //System.out.println("5 repaint: "+ (point- start));
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "\n==== ERROR ====::1106111233:: error got "
                    + "thrown out while navigate to next document"
                    + ex.getLocalizedMessage() );
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jComboBox_InputFileListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_InputFileListActionPerformed
        // by the user disignated textsourceFilename, show its text contents in text
        // area

        // check the time interval between user's last clicking on the previous/next
        // document, 
        long now = System.currentTimeMillis();
        long timeinterval = now - env.Parameters.lastTimeToCallDocument;
        //System.out.println("/n&&&&&&&&&&&&&& TIME &&&&&&&&&&&&&&&&&:"+timeinterval+"/n");
        if(timeinterval<100)
            return;

        goUserDesignated();
        if(jTabbedPane3.getSelectedIndex() != 0)
            jTabbedPane3.getSelectedComponent().repaint();
        // log.LoggingToFile.log(Level.INFO, "\n~~~~ DEBUG ~~~~:: open user designated document, called from combobox");
    }//GEN-LAST:event_jComboBox_InputFileListActionPerformed

    private void jTextField_searchtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_searchtextActionPerformed
        // TODO add your process code here:
    }//GEN-LAST:event_jTextField_searchtextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        searchWord();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**get current selected annotation from the list of selected annotation. 
     * 
     * @return  the annotation that is current selected in the list 
     *          of "selected annotations.
     */
    private Annotation getSelectedAnnotation(){
        
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

    private void jList_selectedAnnotationsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_selectedAnnotationsValueChanged
        
        setFlag_allowToAddSpan(  false ); // cancel possible operation of adding new span
        this.display_RelationshipPath_Remove();

        // ##1## if is in mode of annotation comparing, this function will be disabled
        if(((userInterface.annotationCompare.ExpandButton)jPanel60).isComparatorPanelExpanded()){
            return;
        }

        // ####
        int selected = jList_selectedAnnotations.getSelectedIndex();
        if (selected < 0) {
            jButton_SpanAdd.setEnabled(false);
            jButton_SpanRemove.setEnabled(false);
            jList_Spans.setListData( new Vector() );            
            return;
        }else{
            jButton_SpanAdd.setEnabled(true);
        }


        env.Parameters.latestSelectedInListOfMultipleAnnotions = selected;


        // get current selected annotation in the list of "selected annotations"
        Annotation current_selected_annotation = getSelectedAnnotation();
        
        // list annotation details
        showDetails(current_selected_annotation);
        WorkSet.currentAnnotation = current_selected_annotation;

        // enable button of span editor if only one span listed on the span list
        ListModel model = jList_Spans.getModel();
        if (model != null) {
            if (model.getSize() == 1) {
                jList_Spans.setSelectedIndex(0);
                enable_AnnotationEditButtons();
            } else {
                disable_AnnotationEditButtons();
            }

            if(jList_selectedAnnotations.getSelectedIndex() != -1)
                jButton_spaneditor_delete.setEnabled(true);
            else
                jButton_spaneditor_delete.setEnabled(false);
        }

        
    }//GEN-LAST:event_jList_selectedAnnotationsValueChanged

    
    private void jToggleButton_CreateAnnotaionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_CreateAnnotaionActionPerformed

        //GUI: enter tab of creation annotaion
        tabDoorman(TabGuard.tabs.creationAnnotation);

    }//GEN-LAST:event_jToggleButton_CreateAnnotaionActionPerformed

    private void jToggleButton_ResultEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_ResultEditorActionPerformed

        //GUI: enter tab of result editor
        tabDoorman(TabGuard.tabs.resulteditor);
    }//GEN-LAST:event_jToggleButton_ResultEditorActionPerformed

    /**button clicked: show dictionaries setting dialog*/
    private void jToggleButton_DictionarySettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_DictionarySettingActionPerformed
        // show dictionaries setting dialog
        FLAG_MAYBE_CHANGED = true;
        tabDoorman( TabGuard.tabs.dictionariesSetting);
    }//GEN-LAST:event_jToggleButton_DictionarySettingActionPerformed

    /**Show dialog to confirm exiting or not after user just has
     * clicked exit button on menu bar.*/
    /**user clicked button to active tab of pin extractor*/
    private void jToggleButton_PinExtractorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_PinExtractorActionPerformed
        tabDoorman(TabGuard.tabs.pinExtractor);
    }//GEN-LAST:event_jToggleButton_PinExtractorActionPerformed

    /**GUI: enter tab of dictionary manager*/
    private void jToggleButton_DictionaryManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_DictionaryManagerActionPerformed
        tabDoorman(TabGuard.tabs.configuration );
    }//GEN-LAST:event_jToggleButton_DictionaryManagerActionPerformed

    private void jToggleButton_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_exitActionPerformed
        tabDoorman( TabGuard.tabs.exit );
    }//GEN-LAST:event_jToggleButton_exitActionPerformed

    private void jButton_spaneditor_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_spaneditor_deleteActionPerformed

        spanEdit(spanedittype.delete);
    }//GEN-LAST:event_jButton_spaneditor_deleteActionPerformed

    private void jButton_spaneditor_lefttToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_spaneditor_lefttToLeftActionPerformed
        // TODO add your process code here:
        spanEdit(spanedittype.headExtendtoLeft);
    }//GEN-LAST:event_jButton_spaneditor_lefttToLeftActionPerformed

    private void jButton4_spanEditor_rightToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_spanEditor_rightToRightActionPerformed
        // TODO add your process code here:
        spanEdit(spanedittype.tailExtendtoRight);
    }//GEN-LAST:event_jButton4_spanEditor_rightToRightActionPerformed

    private void jButton_spaneditor_leftToRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_spaneditor_leftToRightActionPerformed
        spanEdit(spanedittype.headShortentoRight);
    }//GEN-LAST:event_jButton_spaneditor_leftToRightActionPerformed

    private void jTextField_searchtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_searchtextKeyTyped

        // search word if key "enter" pressed
        int key = Integer.valueOf( evt.getKeyChar() );
        if ( key == 10 ) {
            searchWord();
        }

        // 'esc' key got pressed
        if( key == 27 ){
            jTextField_searchtext.setText("");
        }
    }//GEN-LAST:event_jTextField_searchtextKeyTyped

    ImportSettings settings;
    /**User click button to import annotations from xml or pin files.*/
    private void jButton_importAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_importAnnotationsActionPerformed
        if(settings==null)
            settings = new ImportSettings(this);
        settings.setVisible(true);
        /*
        // import annotations from XML or PINS file
        // and then show on screen(in result ediot)
        //importAnnotations();
        

        // clear all attributes value display and disable those buttons as
        // no annotation are selected just after annotations been imported.
        disableAnnotationDisplay();

        // as we just have extracted and stored annotated classname while
        // importing annotation from xml knowtator files, we should write new
        // found annotated class name into disk.
        resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
        classdepot.saveAnnotationClassnamesToConfigureFile();


        // show all classes in the tree view
        this.showAnnotationCategoriesInTreeView_CurrentArticle();
        this.showValidPositionIndicators_setAll();
        this.showValidPositionIndicators();

        setNAVCurrentTab(3);
        /*
        //Run Verifier on input
        //Make sure verifier dictionaries are loaded.
        Dictionaries.VerifierDictionaries.loadPreAnnotatedConcepts();
        for(int i = 0; i< jComboBox_InputFileList.getItemCount(); i++ )
        {
            verifier.SpanCheck spanCheck = new verifier.SpanCheck(
                    env.ClinicalNoteList.SelectedTxtFile.getAbsoluteFileName(i), env.Parameters.VERIFIER_DICTIONARIES);
            spanCheck.findProblems(-1);
        }
         * 
         */

        //currentScreen = infoScreens.NONE;
        //refreshInfo();

        
 
    }//GEN-LAST:event_jButton_importAnnotationsActionPerformed


    /**This is a flag that used to indicated whether user clicked the button
     * of "continue" or button of "cancel" to remove all annotations.
     */
    private boolean shouldWeContinue = false ;

    /**check whether user clicked the button of "continue" or button of "cancel"
     * to remove all annotations by poping out a dialog (model)*/
    private boolean confirmBeforeDeletingAllAnnotations(){

        jDialog_Confirm_before_Removing_Annotations.setVisible(false);

        // calculate and set the location of this model dialog
        int thisheight = 149, thiswidth = 522;
        int x = this.getX() + (int)((this.getWidth() - thiswidth)/2);
        int y = this.getY() + (int)((this.getHeight() - thisheight)/2);
        jDialog_Confirm_before_Removing_Annotations.setSize(thiswidth, thisheight);
        jDialog_Confirm_before_Removing_Annotations.setLocation(x,y);

        // display this dialog in model mode
        jDialog_Confirm_before_Removing_Annotations.setModal(true);
        jDialog_Confirm_before_Removing_Annotations.setVisible(true);

        return shouldWeContinue;
    }

    /**remove all imported annotations and refresh the display of text contents
     * of processing clinical notes*/
    private void jButton_removeAllAnnotationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_removeAllAnnotationsActionPerformed

        // Check whether user clicked the button of "continue" or button of 
        // "cancel" to remove all annotations by poping out a dialog (model)
        jDialog_Confirm_before_Removing_Annotations.dispose();
        if(!confirmBeforeDeletingAllAnnotations())
            return;

        adjudication.data.AdjudicationDepot.clear();
        
        // remove all imported annotations
        // remove all annotation display and refresh
        cleanImportedXMLBufferAndReload();
        this.setModified();

        totalRefresh();
        ((userInterface.annotationCompare.ExpandButton)jPanel60).defaultStatus();
        ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
    }//GEN-LAST:event_jButton_removeAllAnnotationsActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton12ActionPerformed
    {//GEN-HEADEREND:event_jButton12ActionPerformed
        //Extract the indices of problem annotations
        //callVerifierWithCurrent();

        //Display problem annotations to annotations... might not want to add these directly to our memory
        //model. Probably better to just save them separately and append them to the end of the written
        //XML file.
        //addProblemAnnotations();
        changeSelectedToSuggestion();
        
        //TODO: Write new/old annotations to file that can be used in knowtator
        
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jList3ValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jList3ValueChanged
    {//GEN-HEADEREND:event_jList3ValueChanged
        if(jList3.getSelectedIndex() != -1)
            jButton12.setEnabled(true);
        else
            jButton12.setEnabled(false);
            
    }//GEN-LAST:event_jList3ValueChanged

    private void jButton_SelectClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SelectClassActionPerformed

        /**while user click the icon of change annotation class on result editor.<p>
         * 1. show available annotation classnames for user to select while this label
         *    is enabled.<p>
         * 2. do nothing if this lable is disabled.<p>
         */

        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

        resultEditor.workSpace.WorkSet.latestScrollBarValue = jScrollPane_textpane.getVerticalScrollBar().getValue();
        if(jButton_SelectClass.isEnabled()){
            resultEditor.spanEdit.AnnotationClassChooser popmenu =
                new resultEditor.spanEdit.AnnotationClassChooser(
                    jTextField_annotationClassnames,
                    this,
                    jTextField_annotationClassnames.getText().trim()
                );
            popmenu.popupDialog( resultEditor.workSpace.WorkSet.currentAnnotation.annotationText );
        }else{
            // beep if lable got disabled
            commons.Tools.beep();
        }
    }//GEN-LAST:event_jButton_SelectClassActionPerformed

    
    /**user click button on result editor to save changes to xml or pins files*/
    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        saveAs();
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        if( classmanager == null || !classmanager.isActive()) {
            classmanager = new resultEditor.annotationClasses.Manager(this);
            classmanager.setVisible(true);
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton_attributeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_attributeActionPerformed
        
        setFlag_allowToAddSpan( WANT_NEW_SPAN ); // cancel possible operation of adding new span

        openAttributeEditor( );

    }//GEN-LAST:event_jButton_attributeActionPerformed

    private void jTabbedPane3MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTabbedPane3MouseClicked
    {//GEN-HEADEREND:event_jTabbedPane3MouseClicked
        showValidPositionIndicators();
    }//GEN-LAST:event_jTabbedPane3MouseClicked

    private void classesMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_classesMouseClicked
    {//GEN-HEADEREND:event_classesMouseClicked
        setToClasses();
}//GEN-LAST:event_classesMouseClicked
    

    private void classesMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_classesMouseEntered
    {//GEN-HEADEREND:event_classesMouseEntered
        classes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}//GEN-LAST:event_classesMouseEntered

    private void annotationsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_annotationsMouseClicked
    {//GEN-HEADEREND:event_annotationsMouseClicked
        setToAnnotations();
    }//GEN-LAST:event_annotationsMouseClicked

    private void annotatorsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_annotatorsMouseClicked
    {//GEN-HEADEREND:event_annotatorsMouseClicked
        setToAnnotators();
}//GEN-LAST:event_annotatorsMouseClicked


    /**
     *
     * This will catch any events where a mouse is clicked on the infoList.
     * If left double click then the selction will be expanded if possible.
     * If right click in 'Conflicts' view then a pop up menu will come up to allow
     * you to resolve conflicts.
     *
     * @param evt - the mouse event
     */
    private void infoListMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_infoListMouseClicked
    {//GEN-HEADEREND:event_infoListMouseClicked
        //If right click then do something... based on current screen.
        if(evt.getButton()== MouseEvent.BUTTON3)
        {
            switch(currentScreen)
            {
                //If we're dealing with ClassConflicts then allow the user to 'resolve' the conflict by
                //selecting the appropriate class with a PopUp menu.
                case CLASSCONFLICT:
                    resultEditor.conflicts.classConflictPopUp popmenu = new resultEditor.conflicts.classConflictPopUp(
                    this.infoList, this);
                    popmenu.pop(evt.getX(), evt.getY());
                    break;
                case ANNOTATIONS:
                case VERIFIER:
                    //resultEditor.PopUp.rightClickOnAnnotPopUp annotPopUp = new resultEditor.PopUp.rightClickOnAnnotPopUp(
                    //this.infoList, this);
                    //annotPopUp.pop((Annotation)infoList.getSelectedValue(),evt.getX(), evt.getY());
                    break;
            }
        }
        //If double clicked then move into the entry if possible.
        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1)
        {

            switch(currentScreen)
            {
                //If A Class has been selected then show all Annotations with that class
                case CLASSES: 
                    String selectedClass = (String)infoList.getSelectedValue();
                    selectAClass(selectedClass);
                    currentScreen = infoScreens.ANNOTATIONS;
                    break;
                //If An annotator has been selected then show all Annotations with that Annotator
                case ANNOTATORS: 
                    selectAnAnnotator();
                    currentScreen = infoScreens.ANNOTATIONS;
                    break;
                //If A span conflict has been selected then show all Annotations involved in that span conflict.
                case SPANCONFLICT:
                    spanOverlaps toShow = (spanOverlaps)infoList.getSelectedValue();
                    infoList.setListData(toShow.getInvolved());
                    currentScreen = infoScreens.ANNOTATIONS;
                    break;
                
            }
        }
    }//GEN-LAST:event_infoListMouseClicked

    /*
     * Called when display current list in viewer button is pressed.  This method will
     * display whatever is in the current list, if it is a list of annotations.
     */
    private void displayCurrentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_displayCurrentActionPerformed
    {//GEN-HEADEREND:event_displayCurrentActionPerformed
        //Make sure we're viewing a list of annotations... either from the Verifier or just
        // annotations
        if (currentScreen == infoScreens.ANNOTATIONS || currentScreen == infoScreens.VERIFIER)
        {
            //Get all of the Annotations in the list
            int entries = infoList.getModel().getSize();

            //if there are no entries then return.
            if (entries == 0)
            {
                return;
            }
            //If the first object is not an annotation then return.
            if (!infoList.getModel().getElementAt(0).getClass().isInstance(new Annotation()))
            {
                return;
            }
            //to store results
            Vector<Annotation> currentlyViewing = new Vector<Annotation>();

            //get the list of annotations so we can display them.
            for (int i = 0; i < entries; i++)
            {
                currentlyViewing.add((Annotation) infoList.getModel().getElementAt(i));
            }

            //Draw the selected annotations(highlights)
            repaintNewAnnotations(currentlyViewing);
        }
        //If we're viewing class conflicts then display all annotations involved.
        else if (currentScreen == infoScreens.CLASSCONFLICT)
        {
            //Get the number of class conflicts
            int entries = infoList.getModel().getSize();

            //Return if there are no entries.
            if (entries == 0)
            {
                return;
            }
            //return if the first element is not a class conflict
            if (!infoList.getModel().getElementAt(0).getClass().isInstance(new classConflict()))
            {
                return;
            }
            //Get the curently viewed class conflicts.
            Vector<classConflict> currentlyViewing = new Vector<classConflict>();
            for (int i = 0; i < entries; i++)
            {
                currentlyViewing.add((classConflict) infoList.getModel().getElementAt(i));
            }

            //Get the annotations from each class conflict.
            Vector<Annotation> annotations = new Vector<Annotation>();
            for(classConflict conflict: currentlyViewing)
            {
                annotations.addAll(conflict.getInvolved());
            }
            //Redraw the highlighting
            repaintNewAnnotations(annotations);
        }
        //Display all annotations in a span conflict.
        else if (currentScreen == infoScreens.SPANCONFLICT)
        {
            int entries = infoList.getModel().getSize();
            //return if there are no entries
            if (entries == 0)
            {
                return;
            }
            //return if it's not a list of span overlaps
            if (!infoList.getModel().getElementAt(0).getClass().isInstance(new spanOverlaps()))
            {
                return;
            }
            //get the span overlaps
            Vector<spanOverlaps> currentlyViewing = new Vector<spanOverlaps>();
            for (int i = 0; i < entries; i++)
            {
                currentlyViewing.add((spanOverlaps) infoList.getModel().getElementAt(i));
            }
            //get the annotations
            Vector<Annotation> annotations = new Vector<Annotation>();
            for(spanOverlaps conflict: currentlyViewing)
            {
                annotations.addAll(conflict.getInvolved());
            }
            //redraw the annotations
            repaintNewAnnotations(annotations);
        }
    }//GEN-LAST:event_displayCurrentActionPerformed
    
    private void workingConflictsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_workingConflictsMouseClicked
    {//GEN-HEADEREND:event_workingConflictsMouseClicked
        setToClassConflict();
    }//GEN-LAST:event_workingConflictsMouseClicked

    private void jTree_classMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree_classMousePressed

        // user just made a single click on the treeview of classes and annotations
        resultEditor.workSpace.WorkSet.mouse_clicked_on = 1;
        userMouseClicked_onClassTreeView(evt);
    }//GEN-LAST:event_jTree_classMousePressed

    private void jTree_classTreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree_classTreeCollapsed
        resultEditor.annotationClasses.mouse.CollapsedListener collapsedlistener =
                new resultEditor.annotationClasses.mouse.CollapsedListener( evt, jTree_class );
    }//GEN-LAST:event_jTree_classTreeCollapsed


    /**mouse events: mouse clicked on the text panel. They maybe want to select an
     * annotation; or build a complex relationship*/
    private void textPaneforClinicalNotesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textPaneforClinicalNotesMouseReleased
        //ResultEditor.WorkSpace.WorkSet.mouse_clicked_on = 2;
        mouseClickOnTextPane( evt );
    }//GEN-LAST:event_textPaneforClinicalNotesMouseReleased

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        smallerFontSize();
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        biggerFontSize();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void verifierFlaggedMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_verifierFlaggedMouseClicked
    {//GEN-HEADEREND:event_verifierFlaggedMouseClicked
        setToVerifier();
    }//GEN-LAST:event_verifierFlaggedMouseClicked
    
    private void verifierOnCurrentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_verifierOnCurrentActionPerformed
    {//GEN-HEADEREND:event_verifierOnCurrentActionPerformed
        this.callVerifierWithCurrent();
        currentScreen = infoScreens.VERIFIER;
        refreshInfo();
    }//GEN-LAST:event_verifierOnCurrentActionPerformed

    private void infoListValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_infoListValueChanged
    {//GEN-HEADEREND:event_infoListValueChanged
        switch (currentScreen)
            {

                case ANNOTATIONS:
                case VERIFIER:
                    if(infoList.getSelectedIndex() >= 0)
                    {
                        this.jTabbedPane3.setSelectedIndex(0);
                        displayInfoForAnnotation(infoList.getSelectedValues());
                        Depot depot = new Depot();
                        infoList.setToolTipText(depot.getContext((Annotation)infoList.getSelectedValue()));
                    }
                    break;
            }

    }//GEN-LAST:event_infoListValueChanged
       

    /**ONLY used for method "setNAVonFirstOrSecondPage()"*/
    private int ActivedNVATAB=-1;
    public void setNAVonFirstOrSecondPage()
    {
        if(this.jPanel_NAV_Card2.isVisible())
        {
            return;
        }

        if(this.jPanel_NAV_Card3.isVisible()){
            if( ActivedNVATAB == 1)
                this.setNAVCurrentTab( 1 );
            else if(ActivedNVATAB == 2)
                this.setNAVCurrentTab( 2 );


        }
    }

    public void setNAVonThirdPage(){
        this.setNAVCurrentTab( 3 );
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="GUI Component Declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BottomInfoBar;
    private javax.swing.JPanel Editor;
    private javax.swing.JPanel GUIContainer;
    private javax.swing.JPanel JPanel_PinsExtractor;
    private javax.swing.JPanel NavigationPanel1;
    private javax.swing.JPanel NavigationPanel_editor;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JLabel annotations;
    private javax.swing.JLabel annotators;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup_MainButton_startAnalysis;
    private javax.swing.ButtonGroup buttonGroup_Tabs;
    private javax.swing.ButtonGroup buttonGroup_listAnnotationsInSequences;
    private javax.swing.ButtonGroup buttonGroup_quicknlp;
    private javax.swing.ButtonGroup buttonGroup_treeview;
    private javax.swing.JLabel classes;
    private javax.swing.JDialog confirmExit;
    private javax.swing.JButton delete_Relationships;
    private javax.swing.JButton displayCurrent;
    private javax.swing.JList infoList;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton4_spanEditor_rightToRight;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton_AddClinicalTexts;
    private javax.swing.JButton jButton_AddClinicalTexts1;
    private javax.swing.JButton jButton_SelectClass;
    private javax.swing.JButton jButton_SpanAdd;
    private javax.swing.JButton jButton_SpanRemove;
    private javax.swing.JButton jButton_attribute;
    private javax.swing.JToggleButton jButton_cr;
    private javax.swing.JButton jButton_importAnnotations;
    private javax.swing.JButton jButton_importAnnotations1;
    private javax.swing.JButton jButton_relationships;
    private javax.swing.JButton jButton_removeAllAnnotations;
    private javax.swing.JButton jButton_removeduplicates;
    private javax.swing.JButton jButton_save;
    private javax.swing.JButton jButton_save1;
    private javax.swing.JButton jButton_span_rightToLeft;
    private javax.swing.JButton jButton_spaneditor_delete;
    private javax.swing.JButton jButton_spaneditor_leftToRight;
    private javax.swing.JButton jButton_spaneditor_lefttToLeft;
    private javax.swing.JPanel jCardcontainer_interactive;
    private javax.swing.JComboBox jComboBox_InputFileList;
    private javax.swing.JComboBox jComboBox_currentworkspace_abspath;
    private javax.swing.JDialog jDialog_Confirm_before_Removing_Annotations;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_cursor;
    private javax.swing.JLabel jLabel_icon_annotationlist;
    private javax.swing.JLabel jLabel_info_annotator;
    private javax.swing.JLabel jLabel_info_annotatorlabel;
    private javax.swing.JLabel jLabel_infobar;
    private javax.swing.JLabel jLabel_infobar_FlagOfDiff;
    private javax.swing.JLabel jLabel_infobar_FlagOfOracle;
    private javax.swing.JLabel jLabel_infobar_attributeEditor;
    private javax.swing.JList jList3;
    private javax.swing.JList jList_NAV_projects;
    private javax.swing.JList jList_Spans;
    private javax.swing.JList jList_complexrelationships;
    private javax.swing.JList jList_corpus;
    private javax.swing.JList jList_normalrelationship;
    private javax.swing.JList jList_selectedAnnotations;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel74;
    private javax.swing.JPanel jPanel75;
    private javax.swing.JPanel jPanel76;
    private javax.swing.JPanel jPanel77;
    private javax.swing.JPanel jPanel78;
    private javax.swing.JPanel jPanel79;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel80;
    private javax.swing.JPanel jPanel81;
    private javax.swing.JPanel jPanel82;
    private javax.swing.JPanel jPanel83;
    private javax.swing.JPanel jPanel84;
    private javax.swing.JPanel jPanel86;
    private javax.swing.JPanel jPanel87;
    private javax.swing.JPanel jPanel88;
    private javax.swing.JPanel jPanel8_bottomBar;
    private javax.swing.JPanel jPanel8_bottomBar1;
    private javax.swing.JPanel jPanel8_currentWorkSpace;
    private javax.swing.JPanel jPanel8_topBar;
    private javax.swing.JPanel jPanel8_topBar1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel90;
    private javax.swing.JPanel jPanel91;
    private javax.swing.JPanel jPanel92;
    private javax.swing.JPanel jPanel94;
    private javax.swing.JPanel jPanel95;
    private javax.swing.JPanel jPanel99;
    private javax.swing.JPanel jPanel_Converter;
    private javax.swing.JPanel jPanel_EditorPanel;
    private javax.swing.JPanel jPanel_MainFrame_Right;
    private javax.swing.JPanel jPanel_NAV_Card1;
    private javax.swing.JPanel jPanel_NAV_Card2;
    private javax.swing.JPanel jPanel_NAV_Card3;
    private javax.swing.JPanel jPanel_NAV_CardContainer;
    private javax.swing.JPanel jPanel_NLP;
    private javax.swing.JPanel jPanel_annotation_details;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser2;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser3;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser4;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser6;
    private javax.swing.JPanel jPanel_colorfulTextBar_filebrowser7;
    private javax.swing.JPanel jPanel_comparator_container;
    private javax.swing.JPanel jPanel_corpuslist_innerline;
    private javax.swing.JPanel jPanel_corpuslist_outline;
    private javax.swing.JPanel jPanel_infobar_center;
    private javax.swing.JPanel jPanel_infobar_right_current_annotator;
    private javax.swing.JPanel jPanel_interactive_left;
    private javax.swing.JPanel jPanel_leftSeparator;
    private javax.swing.JPanel jPanel_multipleResultShowList;
    private javax.swing.JPanel jPanel_position_indicator_container;
    private javax.swing.JPanel jPanel_reportFormContainer;
    private javax.swing.JPanel jPanel_textPane;
    private javax.swing.JRadioButton jRadioButton_adjudicationMode;
    private javax.swing.JRadioButton jRadioButton_annotationMode;
    private javax.swing.JRadioButton jRadioButton_treeview_currentarticle;
    private javax.swing.JRadioButton jRadioButton_treeview_overall;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPane_Spans;
    private javax.swing.JScrollPane jScrollPane_classtree;
    private javax.swing.JScrollPane jScrollPane_textpane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JSplitPane jSplitPane_Annotations_Comparator;
    private javax.swing.JSplitPane jSplitPane_between_viewer_and_allatttibutes;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTextArea jTextArea_comment;
    private javax.swing.JTextField jTextField_annotationClassnames;
    private javax.swing.JTextField jTextField_annotator;
    private javax.swing.JTextField jTextField_creationdate;
    private javax.swing.JTextField jTextField_sample;
    private javax.swing.JTextField jTextField_searchtext;
    private javax.swing.JTextPane jTextPane_explanations;
    private javax.swing.JToggleButton jToggleButton_Converter;
    private javax.swing.JToggleButton jToggleButton_CreateAnnotaion;
    private javax.swing.JToggleButton jToggleButton_DictionaryManager;
    private javax.swing.JToggleButton jToggleButton_DictionarySetting;
    private javax.swing.JToggleButton jToggleButton_PinExtractor;
    private javax.swing.JToggleButton jToggleButton_ResultEditor;
    private javax.swing.JToggleButton jToggleButton_exit;
    private javax.swing.JToggleButton jToggleButton_sequence_inCharacters;
    private javax.swing.JToggleButton jToggleButton_sequence_inLocation;
    private javax.swing.JToggleButton jToggle_AssignmentsScreen;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar_editopanel_comparison;
    private javax.swing.JTree jTree_class;
    private javax.swing.JLabel overlapping;
    private javax.swing.JButton refresh;
    public static final javax.swing.JLabel res_conflictIcon = new javax.swing.JLabel();
    private javax.swing.JTextPane textPaneforClinicalNotes;
    private javax.swing.JLabel verifierFlagged;
    private javax.swing.JButton verifierOnAll;
    private javax.swing.JButton verifierOnCurrent;
    private javax.swing.JLabel workingConflicts;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Generated Events - 2">
    private void infoListMouseMoved(java.awt.event.MouseEvent evt)//GEN-FIRST:event_infoListMouseMoved
    {//GEN-HEADEREND:event_infoListMouseMoved
        //Make sure we're viewing Annotations(either Verifier or Annotations Screen).
        if(currentScreen == infoScreens.ANNOTATIONS || currentScreen == infoScreens.VERIFIER)
        {
            //Make sure something is selected.
            if(infoList.getSelectedIndex() >= 0)
            {
                // Get item index
                int index = infoList.locationToIndex(evt.getPoint());

                // Get item
                Annotation item = (Annotation)infoList.getModel().getElementAt(index);

                // Return the tool tip text
                Depot depot = new Depot();

                //Clear out tooltips if the current mouse movement is not over the selected
                //item.
                if(infoList.getSelectedIndex() == index)
                    infoList.setToolTipText(depot.getContext(item));
                else
                    infoList.setToolTipText(null);
            }

        }
    }//GEN-LAST:event_infoListMouseMoved

    /**
     * JTabbedPane containing the Viewer, and the Annotation Info.
     *
     * @param evt - jTabbedPane3 state changed
     */
    private void jTabbedPane3StateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_jTabbedPane3StateChanged
    {//GEN-HEADEREND:event_jTabbedPane3StateChanged
        
        setFlag_allowToAddSpan(false); // cancel possible operation of adding new span

        clear_annotation_display();

        //if we're on the annotation information tab, then refresh the info.
        if(jTabbedPane3.getSelectedIndex() == 0)
            refreshResultEditor();
        else if(jTabbedPane3.getSelectedIndex() == 1)
            refreshInfo();
        else if(jTabbedPane3.getSelectedIndex()==2){
            // load report system to eHOST
            load_ReportSystem();
        }
    }//GEN-LAST:event_jTabbedPane3StateChanged

    /**remove any information about annotation on the panel of annotation editor*/
    public void clear_annotation_display(){
        this.disableAnnotationDisplay();
        this.disable_AnnotationEditButtons();
    }

    /**
     * Button to run Verifier on all documents
     * @param evt - the button pressed event
     */
    private void verifierOnAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_verifierOnAllActionPerformed
    {//GEN-HEADEREND:event_verifierOnAllActionPerformed
        //Call Verifier with all and refresh the info.
        callVerifierWithAll();
        refreshInfo();
    }//GEN-LAST:event_verifierOnAllActionPerformed

    private void textPaneforClinicalNotesKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_textPaneforClinicalNotesKeyPressed
    {//GEN-HEADEREND:event_textPaneforClinicalNotesKeyPressed
        resultEditor.workSpace.WorkSet.mouse_clicked_on = 2;;

        String text = textPaneforClinicalNotes.getText();
        if(WorkSet.currentAnnotation != null)
        {
            //Move the tail of the span
            if(evt.isAltDown() && !evt.isControlDown())
            {
                //Switch on the key
                switch(evt.getKeyCode())
                {
                    //shorten the tail if left key is also pressed
                    case KeyEvent.VK_LEFT:
                        this.spanEdit(spanedittype.tailshortentoLeft);
                        break;
                    //extend tail if the right key is also pressed
                    case KeyEvent.VK_RIGHT:
                        this.spanEdit(spanedittype.tailExtendtoRight);
                        break;
                }
            }
            //Move the head of the span
            else if(!evt.isAltDown() && evt.isControlDown())
            {
                //Switch on the key presed
                switch(evt.getKeyCode())
                {
                    //If left key is pressed then extend the head
                    case KeyEvent.VK_LEFT:
                        this.spanEdit(spanedittype.headExtendtoLeft);
                        break;
                    //If right key is pressed then shorten the span.
                    case KeyEvent.VK_RIGHT:
                        this.spanEdit(spanedittype.headShortentoRight);
                        break;
                    case KeyEvent.VK_Z:
                        restoreDeleted();
                        break;
                }
            }
            else
            {
                //Switch on the key presed
                switch(evt.getKeyCode())
                {
                    //If left key is pressed then extend the head
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_UP:
                        moveToNextAnnotation(false);
                        break;
                    //If right key is pressed then shorten the span.
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_DOWN:
                        moveToNextAnnotation(true);
                        break;
                }
            }
        }

    }//GEN-LAST:event_textPaneforClinicalNotesKeyPressed

    private void formFocusGained(java.awt.event.FocusEvent evt)//GEN-FIRST:event_formFocusGained
    {//GEN-HEADEREND:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowGainedFocus
    {//GEN-HEADEREND:event_formWindowGainedFocus

        //Somehow need to make pop menu keep focus.
        if(popmenu != null && popmenu.isGood())
        {
            log.LoggingToFile.log(Level.INFO, "popmenu open?");
            popmenu.getFocus();
            Toolkit.getDefaultToolkit().beep();
        }
        if(editor != null && editor.isVisible())
        {
            editor.requestFocus();
             Toolkit.getDefaultToolkit().beep();
        }
        if(classmanager != null && classmanager.isVisible())
        {
            log.LoggingToFile.log(Level.INFO, "classmanager open?");
            classmanager.requestFocus();
            Toolkit.getDefaultToolkit().beep();
        }
        if(simpleEditor != null && simpleEditor.isVisible())
        {
            simpleEditor.requestFocus();
            Toolkit.getDefaultToolkit().beep();
        }
    }//GEN-LAST:event_formWindowGainedFocus

    private void jToggleButton_ConverterActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton_ConverterActionPerformed
    {//GEN-HEADEREND:event_jToggleButton_ConverterActionPerformed
         tabDoorman( TabGuard.tabs.converter);
    }//GEN-LAST:event_jToggleButton_ConverterActionPerformed

    private void jTree_classTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_jTree_classTreeExpanded
        resultEditor.annotationClasses.mouse.NodeExpandedListener expanded = new
                resultEditor.annotationClasses.mouse.NodeExpandedListener( evt , jTree_class);
    }//GEN-LAST:event_jTree_classTreeExpanded

    private void jButton_crActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_crActionPerformed
    {//GEN-HEADEREND:event_jButton_crActionPerformed
        setRelationshipingEnabled(jButton_cr.isSelected()); 
    }//GEN-LAST:event_jButton_crActionPerformed
    
    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton15ActionPerformed
    {//GEN-HEADEREND:event_jButton15ActionPerformed
        if(complexEditor == null || !complexEditor.isVisible())
        {
            complexEditor = new RelationshipSchemaEditor(this);
            complexEditor.setVisible(true);
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    
    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton16ActionPerformed
    {//GEN-HEADEREND:event_jButton16ActionPerformed
        if(simpleEditor == null || !simpleEditor.isVisible())
        {
            simpleEditor = new resultEditor.simpleSchema.Editor(this);
            simpleEditor.setVisible(true);
        }else{
            simpleEditor.setVisible(true);
        }
    }//GEN-LAST:event_jButton16ActionPerformed


    /**
     * While user double clicked jTextField_annotatorMouse, set it editable for
     * coming modification to annotator information of current annotation.
     * 
     * jTextField_annotatorMouse is a component of text field used to show 
     * annotator information of current annotation.
     *
     * @param   evt
     *          Caught mouse event by system.
     */
    private void jTextField_annotatorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField_annotatorMouseClicked


        // do NOTHING, while there is no any annotation selected,
        if( ( resultEditor.workSpace.WorkSet.currentAnnotation == null )
                || (jList_selectedAnnotations.getModel().getSize() < 1 ) )
        {
            jTextField_annotator.setEnabled(false);
            jTextField_annotator.setEditable(false);
            return;
        }

        // ##1## if is in mode of annotation comparing, this function will be disabled
        if(((userInterface.annotationCompare.ExpandButton)jPanel60).isComparatorPanelExpanded()){
            return;
        }

        if (evt.getClickCount() == 2){
            jTextField_annotator.setEnabled(true);
            jTextField_annotator.setEditable(true);
        }
        
    }//GEN-LAST:event_jTextField_annotatorMouseClicked


    /**
     * While lost focus from this component which used to show annotator information
     * of current annotation, save changes and reset the component to uneditable.
     */
    private void jTextField_annotatorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField_annotatorFocusLost

        if ( !jTextField_annotator.isEditable() )
            return;
        saveChanges_on_Annotator();

        // set this component is uneditable
        jTextField_annotator.setEnabled(false);
        jTextField_annotator.setEditable(false);
    }//GEN-LAST:event_jTextField_annotatorFocusLost


    /**
     * While user double clicked jTextArea_comment, set it editable for
     * coming modification to annotator comments of current annotation.
     * 
     * jTextArea_comment is a component of textarea used to show 
     * annotation comments of current annotation.
     *
     * @param   evt
     *          Caught mouse event by system.
     */
    private void jTextArea_commentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea_commentMouseClicked

        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

        // do NOTHING, while there is no any annotation selected,
        if( ( resultEditor.workSpace.WorkSet.currentAnnotation == null )
                || (jList_selectedAnnotations.getModel().getSize() < 1 ) )
        {
            jTextArea_comment.setEnabled(false);
            jTextArea_comment.setEditable(false);
            jTextArea_comment.setBackground(new Color(240,240,240));
            return;
        }

        // ##1## if is in mode of annotation comparing, this function will be disabled
        if(((userInterface.annotationCompare.ExpandButton)jPanel60).isComparatorPanelExpanded()){
            return;
        }
        
        if (evt.getClickCount() == 2){

            jTextArea_comment.setBackground(new Color(255,255,255));
            jTextArea_comment.setEditable(true);
            jTextArea_comment.setEnabled(true);
            jTextArea_comment.requestFocus();            
            this.setModified();
        }

    }//GEN-LAST:event_jTextArea_commentMouseClicked


    /**
     * While lost focus from this component which used to show annotation comments
     * of current annotation, save changes and reset the component to uneditable.
     */
    private void jTextArea_commentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea_commentFocusLost
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

        if ( !jTextArea_comment.isEditable() ){
            jTextArea_comment.setEnabled(false);
            jTextArea_comment.setEditable(false);
            jTextArea_comment.setBackground(new Color(240,240,240));
            return;
        }

        saveChanges_on_Comments();

        // set this component is uneditable
        jTextArea_comment.setEnabled(false);
        jTextArea_comment.setEditable(false);
        jTextArea_comment.setBackground(new Color(240,240,240));
    }//GEN-LAST:event_jTextArea_commentFocusLost

    private void jLabel_info_annotatorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_info_annotatorMouseClicked
        if (evt.getClickCount()==2)
            changeAnnotator();
    }//GEN-LAST:event_jLabel_info_annotatorMouseClicked

    private void jLabel_infobar_FlagOfOraclejLabel_info_annotatorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_infobar_FlagOfOraclejLabel_info_annotatorMouseClicked
        setFlag_of_OracleSimilarPhraseSeeker( !env.Parameters.oracleFunctionEnabled );
    }//GEN-LAST:event_jLabel_infobar_FlagOfOraclejLabel_info_annotatorMouseClicked

    private void jLabel_infobar_FlagOfPathjLabel_info_annotatorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_infobar_FlagOfPathjLabel_info_annotatorMouseClicked
        
    }//GEN-LAST:event_jLabel_infobar_FlagOfPathjLabel_info_annotatorMouseClicked

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        this.setModified();                
        
        // user click accept button on editor panel
        ((userInterface.annotationCompare.ExpandButton)jPanel60).acceptreject_acceptPrimaryAnnotation();
        
        //adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);        
        //diffcounter.accepted();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        this.setModified();
        // user click reject button on editor panel
        ((userInterface.annotationCompare.ExpandButton)jPanel60).acceptreject_rejectPrimaryAnnotation();
        //adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
        //diffcounter.accepted(  );
    }//GEN-LAST:event_jButton24ActionPerformed

    
    
    private void jButton_span_rightToLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_span_rightToLeftActionPerformed
        //((userInterface.annotationCompare.ExpandButton) jPanel60).acceptreject_ignorePrimaryAnnotation();
        spanEdit(spanedittype.tailshortentoLeft);
    }//GEN-LAST:event_jButton_span_rightToLeftActionPerformed

    private void jList_complexrelationshipsMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jList_complexrelationshipsMouseClicked
    {//GEN-HEADEREND:event_jList_complexrelationshipsMouseClicked
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span
        // TODO add your handling code here:
        mouseClicked_onList(jList_complexrelationships);
    }//GEN-LAST:event_jList_complexrelationshipsMouseClicked

    private void jButton_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_saveActionPerformed

        // save function
        saveto_originalxml();
        commons.Tools.beep();
        
        this.modified = false;
    }//GEN-LAST:event_jButton_saveActionPerformed

    private void jButton_removeduplicatesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_removeduplicatesActionPerformed
        // call class to pop up dialog to remove duplicates
        resultEditor.annotations.opt.GhostAndDuplicatesSeeker rd = new
                resultEditor.annotations.opt.GhostAndDuplicatesSeeker(this);
        rd.setVisible(true);
    }//GEN-LAST:event_jButton_removeduplicatesActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        
    }//GEN-LAST:event_formWindowClosed

    private void jList_complexrelationshipsValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jList_complexrelationshipsValueChanged
    {//GEN-HEADEREND:event_jList_complexrelationshipsValueChanged
        relationshipStuffChanged();

    }//GEN-LAST:event_jList_complexrelationshipsValueChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        // user click to pop up a dialog to let you input a new path to
        // open as workspace
        dialog_toInputNewWorkspacePath();
    }//GEN-LAST:event_jButton5ActionPerformed

    
    private String getClassNodeHint(){
        return "eHOST Navigation Nodes";
    }

    /**This will pop up a dialog to let you input a new path to open as workspace.*/
    public void dialog_toInputNewWorkspacePath()
    {
        this.setEnabled(false);

        // get coordinates of right working pane
        int x1 = Editor.getLocationOnScreen().x + NavigationPanel1.getWidth();
        int y1 = Editor.getLocationOnScreen().y;
        Point location = new Point(x1, y1); // coordinate of top left point
                                            // to the right working area
        int w = Editor.getWidth() - NavigationPanel1.getWidth();
        int h = Editor.getHeight();
        java.awt.Dimension size = new java.awt.Dimension(w,h);

        workSpace.SetWorkSpace setWS = new workSpace.SetWorkSpace(this,
                "call_from_Nav", location, size);
        setWS.setVisible(true);
        setWS.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
    }
    

    /**While user double clicking a project on the project list on the NAV pane,
     * list all corpus text files under it and show first one on result editor
     * pane;
     */
    private void jList_NAV_projectsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_NAV_projectsMouseClicked

        try{

            // if user double clicking on a project
            if(evt.getClickCount()==2)
            {
                
                 // ##1## empty the corpus list for current project
                env.Parameters.corpus.RemoveAll();
                adjudication.data.AdjudicationDepot.clear();
                
                adjudication.parameters.Paras.__adjudicated = false;
                adjudication.parameters.Paras.removeAll();
                adjudication.parameters.Paras.removeParas();
                env.Parameters.currentMarkables_to_createAnnotation_by1Click = null;

                // ##2## get selected folder
                int size = jList_NAV_projects.getModel().getSize();
                int selected = jList_NAV_projects.getSelectedIndex();
                if((selected<0)||(selected>size-1))
                    return;

                Object o = jList_NAV_projects.getModel().getElementAt(selected);
                if(o==null)
                    return;

                navigatorContainer.ListEntry_Project entry = (navigatorContainer.ListEntry_Project) o;

                if(entry==null) {
                    log.LoggingToFile.log(Level.SEVERE, "#### ERROR #### 1106091554::fail to get a selected item from the list of project!!!");
                    return;
                }

                File f = entry.getFolder();
                if(f==null) {
                    log.LoggingToFile.log(Level.SEVERE,"#### ERROR #### 1102110353:: current project we got from you selected item in the list of project is NULL");
                    return;
                }

                setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

                // move code to a separate function
                selectProject(f);
                
                if ((env.Parameters.OracleStatus.visible == false)
                        || (env.Parameters.OracleStatus.sysvisible == false)) {

                    if((env.Parameters.OracleStatus.sysvisible == false)) {
                        env.Parameters.oracleFunctionEnabled = false;
                    }
                    
                    jLabel_infobar_FlagOfOracle.setVisible(false);
                } else {
                    jLabel_infobar_FlagOfOracle.setVisible(true);
                }
                
                jLabel_infobar.setText("<html><b>Current Project:</b> <font color=blue>"+ f.getAbsolutePath() +"</font>.</html>");
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1106091336::"+ ex.getMessage());
        }

    }//GEN-LAST:event_jList_NAV_projectsMouseClicked

    private void selectProject(File f) {
        try {
            this.setReviewMode(reviewmode.ANNOTATION_MODE);
            // ##3## set current project
            env.Parameters.WorkSpace.CurrentProject = f;

            this.modified = false;

            // #### load configure settings of this project
            config.project.ProjectConf projectconf = new config.project.ProjectConf(f);
            if (projectconf.foundOldConfigureFile()) {
                Object[] options = {"Yes, please", "No"};
                final JOptionPane optionPane = new JOptionPane();
                int xp = NavigationPanel1.getWidth();
                int yp = ToolBar.getHeight();
                optionPane.setSize(500, 300);

                optionPane.setLocation(
                        this.getX() + xp + (int) ((this.getWidth() - xp - 500) / 2),
                        this.getY() + yp + (int) ((this.getHeight() - yp - 300) / 2));
                int answer = optionPane.showOptionDialog(
                        this,
                        "<html>We find an old version of eHOST configure file under<p> your "
                        + "current project. Do you want to load and convert it into new format?<html>",
                        "Older Configure File:",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (answer == JOptionPane.YES_OPTION) {
                    projectconf.loadConfigure();
                    projectconf.rename();
                } else {
                    projectconf.loadXmlConfigure();
                }


            } else {
                projectconf.loadXmlConfigure();
            }

            // open this folder
            setNAVCurrentTab(2);
            showStatusButtons();
            ActivedNVATAB = 2;

            // loading saved annotation classanmes from oonfigure file

            /*
             * new Thread(){ @Override public void run(){
             * System.out.println("#eHOST# Reading saved annotation classnames
             * ..."); resultEditor.annotationClasses.Depot depot = new
             * resultEditor.annotationClasses.Depot(); // load save annotation
             * classnames depot.loadAnnotationClassnamesFromConfigureFile();
             * System.out.println("#eHOST# Reading saved annotation classnames
             * --- end "); } }.start();
             */
            
            //env.ClinicalNoteList.CorpusLib.RemoveAll();
            // list txt files under corpus
            File[] corpus = listCorpus_inProjectFolder(f);
            
            listCorpus(corpus);
            
            

            // add into memory
            if (corpus != null) {
                env.Parameters.corpus.LIST_ClinicalNotes.clear();
                for (File txtfile : corpus) {
                    if (txtfile == null) {
                        continue;
                    }
                    //env.clinicalNoteList.CorpusLib.addTextFile(txtfile);
                    env.clinicalNoteList.CorpusStructure cs = new env.clinicalNoteList.CorpusStructure();
                    cs.file = txtfile;

                    env.Parameters.corpus.LIST_ClinicalNotes.add(cs);

                }

                showTextFiles_inComboBox();

                showFirstFile_of_corpus();
            }

            // update screen
            jCardcontainer_interactive.setVisible(true);

            this.updateScreen_for_variables();

            // load saved annotations from "saved" folder
            resultEditor.reloadSavedAnnotations.Reload.load(this);

            this.showAnnotationCategoriesInTreeView_CurrentArticle();
            this.reavtiveMainPanel();
            
            // display the editor panel as default
            if(!((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).isExtended()){
                ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).afterMousePressed();
                ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).setNormalColor();
            }


            // Update several status buttons on screen by these project parameters.
            // There are several status buttons on the status bar at the button of
            // eHOST window, such as Oracle status button, Diff status button, etc.
            updateGUI_byProjectParameters();
            
            this.setReviewMode(reviewmode.ANNOTATION_MODE);

            ((navigatorContainer.TabPanel) NavigationPanel1).setTab_All();
            
            String annotator_name = resultEditor.annotator.Manager.getAnnotatorName_OutputOnly();
            assignmentsScreen = getAssignmentsScreen(annotator_name);
            //String annotator_id = resultEditor.annotator.Manager.getAnnotatorID_outputOnly(); NOT SAME AS AA USER ID
            //assignmentsScreen.updateAssignments(); THIS DISPLAYS THE ASGS, but then asg selects do not show correctly in the results editor

        } catch (Exception ex) {
            System.out.println("error 1204031721");
            ex.printStackTrace();
        }

    }

    /**read file list from */
    public void refreshFileList(){
        File[] corpus = listCorpus_inProjectFolder(env.Parameters.WorkSpace.CurrentProject);
            listCorpus(corpus);

            // add into memory
            env.Parameters.corpus.RemoveAll();
            if(corpus!=null){
                for(File txtfile:corpus){
                    if(txtfile==null)
                        continue;
                    env.Parameters.corpus.addTextFile(txtfile);
                }
            }

            showTextFiles_inComboBox();
            goUserDesignated();
    }

    private void listCorpus(File[] corpus){
        // empty the list
        this.jList_corpus.setListData(new Vector());

        try{

            Vector<userInterface.structure.FileObj> entries = new Vector<userInterface.structure.FileObj>();
            for(File file:corpus){
                userInterface.structure.FileObj fileobj =
                        new userInterface.structure.FileObj(
                        file.getName(),
                        this.icon_note2 );
                entries.add( fileobj );
            }
            this.jList_corpus.setListData(entries);
            this.jList_corpus.setCellRenderer( new userInterface.structure.FileRenderer() );
            
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1101211147:: fail to list corpus on screen"
                    + ex.getMessage());
        }
    }

    /**List all txt files under the give project folder.*/
    private File[] listCorpus_inProjectFolder(File project){
        File[] txtfiles = null;

        try{
            // get corpus folder
            File corpusfolder=null;
            File[] files = project.listFiles();
            if(files==null)
                return null;
            for(File file:files){
                if(file.getName().toLowerCase().compareTo(env.CONSTANTS.corpus)==0){
                    corpusfolder = file;
                    break;
                }
            }

            if(corpusfolder==null)
                return null;

            File[] allcorpusfiles = corpusfolder.listFiles();
            if(allcorpusfiles==null)
                return null;
            int countfiles = 0;
            for(File file:allcorpusfiles){
                if(file.isFile() && (!file.isHidden())){
                    //if(file.getName().toString().contains(".txt"))
                        countfiles++;
                }
            }

            int point=0;
            txtfiles = new File[countfiles];
            for(File file:allcorpusfiles){
                if(file.isFile()&& (!file.isHidden()) ){
                    //if(file.getName().toString().contains(".txt")) {
                        txtfiles[point] = file;
                        point++;
                    //}
                }
            }


        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1101211133::");
        }

        return txtfiles;
    }

    /**ask user whether to save modification of current document.
     * 
     * @return  true :
     *          if user selected "yes";
     * 
     *          false :
     *          if user selected "no".
     */
    private boolean popDialog_Asking_ChangeSaving() {
        Object[] options = {"Yes, please", "No"};
        final JOptionPane optionPane = new JOptionPane();
        int xp = NavigationPanel1.getWidth();
        int yp = ToolBar.getHeight();
        optionPane.setSize(500, 300);

        optionPane.setLocation(
                this.getX() + xp + (int) ((this.getWidth() - xp - 500) / 2),
                this.getY() + yp + (int) ((this.getHeight() - yp - 300) / 2));
        int answer = optionPane.showOptionDialog(
                this,
                "<html>Do you want to save your modifications?<html>",
                "Save Changes:",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (answer == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span
        // if user modified something of this current, ask user whether they 
        // want to save changes or not.        
        if( this.modified ){
            // get user's decision
            boolean yes_no = popDialog_Asking_ChangeSaving();

            // call saving function if needed.
            if( yes_no ){
                this.saveto_originalxml();
            }
        }

        hideStatusButtons();

        // close protential consensus mode
        this.setReviewMode( reviewmode.OTHERS );

        ((navigatorContainer.TabPanel)NavigationPanel1).setTab_onlyProject();
        
        // go to tab which has the list of project
        setNAVCurrentTab(1);
        jCardcontainer_interactive.setVisible(false);
        
        // save configure setting for current project which you just left
        config_saveProjectSetting();

        // set current project as NULL to indicate that you go back to
        // workspace level and there is no project got selected
        env.Parameters.WorkSpace.CurrentProject = null;

    }//GEN-LAST:event_jLabel21MouseClicked


    /** save configure setting for current project*/
    public void config_saveProjectSetting()
    {
        // get current project
        File currentproject = env.Parameters.WorkSpace.CurrentProject;
        // verify it before using
        if(currentproject==null)
            return;

        // save configure setting for current project
        config.project.ProjectConf projectconf
                = new config.project.ProjectConf(currentproject);
        
        projectconf.saveConfigure();
        
    }


    
    private void jButton_save1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_save1ActionPerformed
        
        preAnnotation.Manager manager = new preAnnotation.Manager(this);

        // make sure class of "person" and "Pronoun" has existed
        manager.confirmClassPerson();

        // search for terms of person
        manager.preannotation_toPairs();
        // marked terms of person with correlated relationship
        manager.MarkPairs();

        preAnnotation.pairs.Dictionary dict = new preAnnotation.pairs.Dictionary();
        dict.close();
        
    }//GEN-LAST:event_jButton_save1ActionPerformed

    private void jLabel22MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jLabel22MouseReleased
    {//GEN-HEADEREND:event_jLabel22MouseReleased
        if(hotkeyDialog == null || !hotkeyDialog.isVisible())
        {
            hotkeyDialog = new HotKeys(this.getX(), this.getY(), this.getWidth(), this.getHeight());
            hotkeyDialog.setVisible(true);
        }
        else
        {
            hotkeyDialog.toFront();
        }
    }//GEN-LAST:event_jLabel22MouseReleased

    private void jList_corpusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_corpusMouseClicked
        
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

        if(evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            goUserDesignatedTable();
        }
    }//GEN-LAST:event_jList_corpusMouseClicked

    private void jButton_AddClinicalTexts1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddClinicalTexts1ActionPerformed
        dialog_toCreateNewProject();
    }//GEN-LAST:event_jButton_AddClinicalTexts1ActionPerformed

    private void dialog_toCreateNewProject(){
        //this.setEnabled(false);

        // get coordinates of right working pane
        int x1 = Editor.getLocationOnScreen().x + NavigationPanel1.getWidth();
        int y1 = Editor.getLocationOnScreen().y;
        Point location = new Point(x1, y1); // coordinate of top left point
                                            // to the right working area
        int w = Editor.getWidth() - NavigationPanel1.getWidth();
        int h = Editor.getHeight();
        java.awt.Dimension size = new java.awt.Dimension(w,h);

        
        workSpace.NewProject newProject = new workSpace.NewProject(this, location, size);
        newProject.setVisible(true);
        newProject.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
    }

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        // user clicked "delete" button on NAV panel (project list)
        // to schedule a deleting operation to a selected projected
        deleteProject();
    }//GEN-LAST:event_jButton26ActionPerformed


    /**user can select a recently used workspace in the combo list and
     * EHOST should bring user into this folder.
     */
    private void jComboBox_currentworkspace_abspathItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_currentworkspace_abspathItemStateChanged

        // this function only works after EHOST system had initialized.
        if(jComboBox_currentworkspace_abspath.getItemCount()!= workSpace.switcher.RecentWorkSpace.getItemCount())
            return;
        
        if(evt.getStateChange()==ItemEvent.SELECTED)
        {
            Object o = evt.getItem();
            if( (o==null) || (!(o instanceof File)) )
                return;

            File workspace = (File)o;
            
            env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath = workspace.getAbsolutePath();
            //if( evt. )
            //    refreshNAVProjects();
            
        }
    }//GEN-LAST:event_jComboBox_currentworkspace_abspathItemStateChanged


    /**There is a combo list on the navigator panel, which can show you recently used
     * workspace. This method is to make sure current workspace is currently displayed.
     */
    public void setCurrentWorkspace_selected(File _workspace){
        try{
            if(_workspace==null)
                return;

            ComboBoxModel cbm = jComboBox_currentworkspace_abspath.getModel();
            if(cbm==null)
                return;

            int size = cbm.getSize();
            if(size<1)
                return;

            for(int i=0; i<size; i++){
                Object o = cbm.getElementAt(i);
                if(o==null)
                    continue;
                String filename = o.toString();
                if(filename==null)
                    continue;
                filename = filename.trim();
                if(filename.length()<1)
                    continue;

                if (filename.compareTo(_workspace.getAbsolutePath())==0){
                    jComboBox_currentworkspace_abspath.setSelectedIndex(i);
                    return;
                }
            }

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "### ERROR ### 1106151410:: failed to show "
                    + "current workspace on the combobox on navigation panel");
        }
    }

    private void delete_RelationshipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_RelationshipsActionPerformed
        try{
            ListEntryofRelationship toDelete = (ListEntryofRelationship)this.jList_complexrelationships.getSelectedValue();
            if(toDelete!=null)
            {
                AnnotationRelationship relToDelete = toDelete.getRel();
                WorkSet.currentAnnotation.relationships.remove(relToDelete);
            }
            this.setModified();
            this.updateComplex();
            ((userInterface.annotationCompare.ExpandButton) jPanel60).cr_recheckDifference();
            display_removeSymbolIndicators();
            display_showSymbolIndicators();
        }catch(Exception ex){
            System.out.println("ERROR 1203010425::");
        }
    }//GEN-LAST:event_delete_RelationshipsActionPerformed

    private void jRadioButton_treeview_overallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_treeview_overallActionPerformed
        display_showCategories();
}//GEN-LAST:event_jRadioButton_treeview_overallActionPerformed

    private void jRadioButton_treeview_currentarticleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_treeview_currentarticleActionPerformed
        display_showCategories();
}//GEN-LAST:event_jRadioButton_treeview_currentarticleActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        shouldWeContinue = false;
        jDialog_Confirm_before_Removing_Annotations.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        shouldWeContinue = true;
        jDialog_Confirm_before_Removing_Annotations.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jToggleButton_sequence_inCharactersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_sequence_inCharactersActionPerformed
        changeDisplaySequence_toAnnotationUniques();
    }//GEN-LAST:event_jToggleButton_sequence_inCharactersActionPerformed

    private void jToggleButton_sequence_inLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_sequence_inLocationActionPerformed
        changeDisplaySequence_toAnnotationUniques();
    }//GEN-LAST:event_jToggleButton_sequence_inLocationActionPerformed



    /**This method is used for test specific methods/classes. It's an action to 
     * the mouse clicking events on the informatin bar on the main gui.
     * 
     * Tested for : import external schema XML files which was pull out 
     *              from the server side.
     */
    private void jLabel_infobarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_infobarMouseClicked

         /**ONLYONLY TESTTEST*/
    
        setFlag_of_DifferenceMatching_Display(env.Parameters.enabled_Diff_Display); // update differences
                    display_repaintHighlighter();
    
        //try{

            // screen info: test begin
    //        log.LoggingToFile.log(Level.INFO,"\nTESTING CLASS: webservices.convertSchema - begin");

            // get the access to the external XML file which we need to convert/extract
            //File externalSchemaFile = new File("/Users/leng/Downloads/schema (1).xml");
            //if((externalSchemaFile==null)||(!externalSchemaFile.exists())||(externalSchemaFile.isDirectory())){
            //    System.out.println("             : fail to get the schema file");
            //    return;
            //}

            
            // webservices.InitProject init = new webservices.InitProject( new File("/Users/leng/"), externalSchemaFile );
            
            

            //webservices.utils.SchemaConvertor schemaConvertor = new webservices.utils.SchemaConvertor();

            // tell the schema convertor which file is the external schema XML file
            //schemaConvertor.setSchemaFile(externalSchemaFile);
            // begin extract/convert schema information
            //schemaConvertor.extractSchema();

            // update the navigate tree as there may have some new classes has
            // be added into current project

          //  webservices.utils.Project.initProject("/Users/leng/Downloads/22", "/Users/leng/Downloads/schema (2).xml");
            

            // screen info: test end
            //log.LoggingToFile.log(Level.INFO,"TESTING CLASS: webservices.convertSchema - end\n");

        //}catch(Exception ex){
//            log.LoggingToFile.log(Level.SEVERE,"\n~~~~ ERROR ~~~~::1107291547::error occurred "
//                + "while we were running the test program to the schema "
//                + "convertor!!!");
//            log.LoggingToFile.log(Level.SEVERE, ex.getMessage() );
//        }
        
    }//GEN-LAST:event_jLabel_infobarMouseClicked

    private void jLabel_infobar_attributeEditorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_infobar_attributeEditorMouseClicked
        setFlag_of_AttributeEditorDisplay_Display(!env.Parameters.enabled_displayAttributeEditor);
    }//GEN-LAST:event_jLabel_infobar_attributeEditorMouseClicked
    
    private void jToggleButton_AssignmentScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_AssignmentScreenActionPerformed
        tabDoorman( TabGuard.tabs.assignmentsScreen );
    }//GEN-LAST:event_jToggleButton_AssignmentScreenActionPerformed

    
    private void jLabel_infobar_FlagOfDiffMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_infobar_FlagOfDiffMouseClicked
        // if left mouse key pressed
        if (evt.getButton() == 1) {
            try {
                this.setFlag_of_DifferenceMatching_Display(!env.Parameters.enabled_Diff_Display);
                display_repaintHighlighter();
            } catch (Exception ex) {
            }
        }
        // if user pressed on the right button, we pop up the dialog to ask
        // user define what's the difference they want.
        else if (evt.getButton() == 3) {
            JPopupMenu popup = new JPopupMenu();
            final ActionListener menuListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    userInterface.differenceMatching.DefineDialog customdlg =
                            new userInterface.differenceMatching.DefineDialog(GUI.this);
                    customdlg.setVisible(true);
                }
            };
            JMenuItem item;
            popup.add(item = new JMenuItem("Define Difference Matching", icon_difference_enabled));
            item.addActionListener(menuListener);
            popup.show(this.jLabel_infobar_FlagOfDiff, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_jLabel_infobar_FlagOfDiffMouseClicked

    private void jRadioButton_adjudicationModeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton_adjudicationModeMouseReleased
        
        setFlag_allowToAddSpan( false );
        
        if(!jRadioButton_adjudicationMode.isEnabled())
            return;

        setReviewChangeButtonEnabled(false);
        
        // if already in adjudication mode, only set the radio button status and
        // enable them
        if (reviewmode == GUI.ReviewMode.adjudicationMode) {
            this.jRadioButton_adjudicationMode.setSelected(true);
            this.jRadioButton_annotationMode.setSelected(false);
            setReviewChangeButtonEnabled(true);
            return;  // quit
        }

        // disable two radio buttons for switching between review modes
        setReviewChangeButtonEnabled(false);
        // set selected status to these two radio button
        jRadioButton_annotationMode.setSelected(false);
        jRadioButton_adjudicationMode.setSelected(true);

        // enter consensus mode from other modes
        this.setReviewMode( ReviewMode.adjudicationMode );

        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        
    }//GEN-LAST:event_jRadioButton_adjudicationModeMouseReleased


    /**user want to active the annotation mode by clicking the radio button
     * of annotation mode;
     */
    private void jRadioButton_annotationModeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton_annotationModeMouseReleased
        mode_enterAnnotationMode(false);
    }//GEN-LAST:event_jRadioButton_annotationModeMouseReleased

    /**entering normal annotation mode. Such as you are leaving the adjudication
     * mode back to the annotation mode, then we need to call this method to
     * redraw annotations and difference indicator.
     * 
     * @param   isForceUpdate
     *          Flag that tell method whether need to update all information on
     *          screen no matter current mode is already in annotation mode.
     */
    public void mode_enterAnnotationMode(boolean isForceUpdate){
        // user want to active the annotation mode by clicking the radio button
        // of annotation mode;
        setFlag_allowToAddSpan( false );
        ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
        ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
        
        
        if( isForceUpdate ){
            jRadioButton_annotationMode.setEnabled(true);
            jRadioButton_adjudicationMode.setEnabled(true);
        }
        else
        {if(!jRadioButton_annotationMode.isEnabled())
            return;
        }

        if( isForceUpdate ){
            this.reviewmode = GUI.ReviewMode.ANNOTATION_MODE;
        }
        // do only while
        // user clicked button to enter annotation review mode{
        else{
            if (this.reviewmode == GUI.ReviewMode.ANNOTATION_MODE) {
                return;
            }
        }

        
                
        jRadioButton_adjudicationMode.setSelected(false);
        this.setReviewMode(reviewmode.ANNOTATION_MODE);

        
        
        
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        refreshInfo();


        resultEditor.conflicts.DifferentMatching diff = new
                        resultEditor.conflicts.DifferentMatching( textPaneforClinicalNotes );
                diff.search_differentMatching( WorkSet.getCurrentFile() );
                // reset details display
        this.setFlag_of_DifferenceMatching_Display(env.Parameters.enabled_Diff_Display);
        display_repaintHighlighter();
                
        this.showAnnotationCategoriesInTreeView_CurrentArticle();
        this.showValidPositionIndicators_setAll();
        this.showValidPositionIndicators();
    }
    
    private void jList_normalrelationshipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_normalrelationshipMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jList_normalrelationshipMouseClicked

    /**EVENT: value changed in jLIST of Spans of current annotation on the 
     * annotation editor panel. */
    private void jList_SpansValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_SpansValueChanged
        setFlag_allowToAddSpan(  false ); // cancel possible operation of adding new span

        /*
        if(((userInterface.annotationCompare.ExpandButton)jPanel60).isDiffPanelWorking){
            jButton10.setEnabled(false);
            jButton3.setEnabled(false);
            return;
        }*/

        if(jList_Spans.getSelectedIndex() <0 ){
            jButton_SpanRemove.setEnabled(false);
            return;
        }

        ListModel model = jList_Spans.getModel();
        if(model==null){
            jButton_SpanRemove.setEnabled(false);
            return;
        }        

        // enable/disable the buttons of "change span"
        if( model.getSize() > 0 ){
            if (jList_Spans.getSelectedIndex() >= 0 ){
                this.enable_AnnotationEditButtons();
            }else{
                this.disable_AnnotationEditButtons();
            }
        }

        // enable/disable the button of "delete a span"
        if( model.getSize() > 1 ){                       
            if (jList_Spans.getSelectedIndex() >= 0 ){
                jButton_SpanRemove.setEnabled(true);
            }else{
                jButton_SpanRemove.setEnabled(false);
            }
        }
    }//GEN-LAST:event_jList_SpansValueChanged


    boolean WANT_NEW_SPAN = false;

    private void addSpan(java.awt.event.MouseEvent evt, boolean requiringFromButton){
        try {

            // check: make sure we get a current annotation.
            if (jList_selectedAnnotations.getModel() == null) {
                return;
            }
            if (jList_selectedAnnotations.getModel().getSize() < 1) {
                return;
            }

            int selectedIndex = jList_selectedAnnotations.getSelectedIndex();

            Annotation currentAnnotation = WorkSet.currentAnnotation;
            if( currentAnnotation == null )
                return;


            // condition: do we have some text selected in the text viewer.
            String selectedText = textPaneforClinicalNotes.getSelectedText();

            if (requiringFromButton) {
                // add the new span

                if (selectedText == null) {
                    setFlag_allowToAddSpan(  true );
                    return;
                } else {

                    int start = textPaneforClinicalNotes.getSelectionStart();
                    int end = textPaneforClinicalNotes.getSelectionEnd();
                    // add span
                    addSpan(currentAnnotation, start, end, selectedText);
                    // update screen
                    updateEditorScreen_afterSpanModified(selectedIndex, start);
                }
            }else{
                int start = textPaneforClinicalNotes.getSelectionStart();
                    int end = textPaneforClinicalNotes.getSelectionEnd();
                    // add span
                    addSpan(currentAnnotation, start, end, selectedText);
                    // update screen
                    updateEditorScreen_afterSpanModified(selectedIndex, start);

                setFlag_allowToAddSpan(  false );
            }
            




        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1202161233::fail to create new span."
                    + ex.getLocalizedMessage());
        }
    }
    private void jButton_SpanAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SpanAddActionPerformed
        
        addSpan(null, true);

    }//GEN-LAST:event_jButton_SpanAddActionPerformed

    private void jButton_SpanRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SpanRemoveActionPerformed
         setFlag_allowToAddSpan(false); // cancel possible operation of adding new span
         jButton_SpanRemove.setEnabled(false);

         int selectedIndex = jList_Spans.getSelectedIndex();
         if( selectedIndex < 0   )
             return;

         Object obj = jList_Spans.getModel().getElementAt(selectedIndex);
         if( obj == null )
             return;
         if( obj instanceof userInterface.structure.SpanObj ){
             userInterface.structure.SpanObj span = (userInterface.structure.SpanObj) obj;
             Annotation ann = span.getAnnotation();
             ann.spanset.removeSpan(selectedIndex);
             ann.annotationText = getText(ann.spanset);
             selectedIndex = this.jList_selectedAnnotations.getSelectedIndex();
             this.updateEditorScreen_afterSpanModified(selectedIndex, -1);
         }
         
         showAnnotationCategoriesInTreeView_refresh();

    }//GEN-LAST:event_jButton_SpanRemoveActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        adjudication.statusBar.DiffCounter differenceCounter = new adjudication.statusBar.DiffCounter( this.jLabel23, WorkSet.getCurrentFile().getName(), this );        
        differenceCounter.goNext();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        adjudication.statusBar.DiffCounter differenceCounter = new adjudication.statusBar.DiffCounter( this.jLabel23, WorkSet.getCurrentFile().getName(), this );
        differenceCounter.goPrevious();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTree_classTreeWillCollapse(javax.swing.event.TreeExpansionEvent evt) throws javax.swing.tree.ExpandVetoException {//GEN-FIRST:event_jTree_classTreeWillCollapse
        resultEditor.annotationClasses.mouse.CollapsedListener collapsedlistener =
                new resultEditor.annotationClasses.mouse.CollapsedListener( evt, jTree_class );
    }//GEN-LAST:event_jTree_classTreeWillCollapse

    private void ToolBarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ToolBarMouseReleased
        showAnnotationCategoriesInTreeView_refresh();        // TODO add your handling code here:
    }//GEN-LAST:event_ToolBarMouseReleased

    private void jPanel_NAV_Card3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel_NAV_Card3MousePressed
        
        
    }//GEN-LAST:event_jPanel_NAV_Card3MousePressed

    private void jToolBar6MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToolBar6MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jToolBar6MouseMoved

    
    bobDict.DictManager dictManager = null;
    /**temp function that used to call the bob dictionary manager*/    
    private void jLabel25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel25MouseClicked
        
        /*if( dictManager != null ){
            dictManager.toFront();
            dictManager.setVisible( true );
        }else{
            dictManager = new bobDict.DictManager( this );
            dictManager.setVisible( true );
        }*/
    }//GEN-LAST:event_jLabel25MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        acceptAll();
    }//GEN-LAST:event_jButton9ActionPerformed

    nlp.tableExtraction.Monitor console;
    private void jButton_importAnnotations1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_importAnnotations1ActionPerformed
        if( console == null )
            console = new nlp.tableExtraction.Monitor( this );
        console.setVisible( true );                
    }//GEN-LAST:event_jButton_importAnnotations1ActionPerformed

    private void jButton_relationshipsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_relationshipsActionPerformed
        //this.WANT_NEW_SPAN = false; // cancel possible operation of adding new span
        setFlag_allowToAddSpan( false );
        openRelAttributeEditor();
    }//GEN-LAST:event_jButton_relationshipsActionPerformed

    public void acceptAll(){
         this.setModified();
        // user click reject button on editor panel
        ((userInterface.annotationCompare.ExpandButton)jPanel60).acceptAll();
        adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
        diffcounter.accepted(  );
    }
    
    /**To an given "spanset" of an annotation, get the combined annotation texts
     * which splitted by " ... ".
     */
    private String getText(SpanSetDef spanset){
        try {
            if (spanset == null)
                return null;

            int size = spanset.size();
            if (size <= 0)
                return null;

            // #### 1 ------------------------------------- //
            // sort spans by their spanstart

            // #### 1.1 copy spans into an array to sort
            SpanDef[] spans = new SpanDef[size];
            for (int i = 0; i < size; i++) {
                SpanDef span = spanset.getSpanAt(i);
                spans[i] = span;
            }

            // #### 1.2 sort spans in the array
            for (int i = 0; i < size; i++) {
                SpanDef span1 = spans[i];
                if (span1 == null) {
                    continue;
                }

                for (int j = i+1; j < size; j++) {
                    if (i == j) {
                        continue;
                    }
                    SpanDef span2 = spans[j];
                    if (span2 == null) {
                        continue;
                    }

                    if (span1.start > span2.start) {
                        //SpanDef tempSpan = span2;
                        SpanDef tempspan = new SpanDef(span1.start, span1.end, span1.text);
                        spans[i] = span2;
                        spans[j] = tempspan;

                        span2 = spans[j];
                        span1 = spans[i];
                    }
                }
            }
            // 1 #### ------------------------------------- //

            // output for debug
            /*
            for (int i = 0; i < size; i++) {
            SpanDef span1 = spans[i];
            if (span1 == null) {
            continue;
            }

            System.out.println("["+i+"]: ( " + span1.start + ", " + span1.end + " )" );
            }
             */

            String annotationText = "";
            boolean isFirstValidRecord = true;
            for (int i = 0; i < size; i++) {
                SpanDef span = spans[i];
                if (span == null) {
                    continue;
                }

                if (isFirstValidRecord) {
                    annotationText = textPaneforClinicalNotes.getText(span.start, span.end - span.start);
                    isFirstValidRecord = false;
                } else {
                    annotationText = annotationText
                            + " ... "
                            + textPaneforClinicalNotes.getText(span.start, span.end - span.start);
                }

            }

            return annotationText;
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1202151405::fail to get all span texts"
                    + ex.getLocalizedMessage());
            return null;
        }
    }

    private void updateEditorScreen_afterSpanModified(int selectedIndex, int start) {
        try {

            int selected = jComboBox_InputFileList.getSelectedIndex();
            //showFileContextInTextPane(selected , 0 );
            this.display_repaintHighlighter();

            disableAnnotationDisplay();

            showSelectedAnnotations_inList( selectedIndex );
            enable_AnnotationEditButtons();

            showAnnotationCategoriesInTreeView_refresh();
            showValidPositionIndicators();

            if(start>=0)            
                textPaneforClinicalNotes.setSelectionEnd(start);

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1012011438:: fail to refresh screen after "
                    + "span modified!!!");
        }


    }

    /**Add a new span into a given annotation*/
    private void addSpan(Annotation annotation, int start, int end, String text){
        if( annotation == null )
            return;
        
        if( text == null )
            return;

        this.modified = true; // tell system that we did a modification.

        annotation.spanset.addSpan(start, end, text);
        annotation.annotationText = this.getText( annotation.spanset );

    }

    /**enable or disable two radio button for changing review mode of ehost
     * between annotation mode and adjudication mode.*/
    public void setReviewChangeButtonEnabled(boolean enabled) {
        try {
            jPanel3.setEnabled(enabled);
            jRadioButton_annotationMode.setEnabled(enabled);
            jRadioButton_adjudicationMode.setEnabled(enabled);
            if (jRadioButton_adjudicationMode.isVisible()) {
                this.repaint();
            }
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1110191538:: fail to enable or "
                    + "disable the two radio button for changing review mode of "
                    + "ehost between annotation mode and adjudication mode."
                    + "\nRELATED ERROR: "
                    + ex.getMessage() );
        }
    }


    /**Change parameters and refresh the tree view of annotation uniques after
     * user clicked on the button
     */
    private void changeDisplaySequence_toAnnotationUniques(){

        // check the button
        env.Parameters.ShowAnnotationUniquesInTree.isLocationSequence 
                = !jToggleButton_sequence_inCharacters.isSelected();

        showAnnotationCategoriesInTreeView_CurrentArticle();
        
    }

    /** user clicked "delete" button on NAV panel (project list) to schedule a
     * deleting operation to a selected project.
     */
    private void deleteProject()
    {

        // get coordinates of right working pane
        int x1 = Editor.getLocationOnScreen().x + NavigationPanel1.getWidth();
        int y1 = Editor.getLocationOnScreen().y;
        Point location = new Point(x1, y1); // coordinate of top left point
                                            // to the right working area
        int w = Editor.getWidth() - NavigationPanel1.getWidth();
        int h = Editor.getHeight();
        java.awt.Dimension size = new java.awt.Dimension(w,h);

        // call class to handle the deleting and confirmation operations
         workSpace.DeleteProject delProject = new  workSpace.DeleteProject(
                this, jList_NAV_projects, location, size);

        delProject.delete();
    }


    private static int widerwidth=0;    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**set flag and corrlated lable's icon and tiptool text.*/
    public void setFlag_of_OracleSimilarPhraseSeeker(boolean isEnabled ){
        try{
            if(( env.Parameters.OracleStatus.sysvisible == false ) || ( env.Parameters.OracleStatus.visible == false )){                
                jLabel_infobar_FlagOfOracle.setVisible( false );
            }else{
                jLabel_infobar_FlagOfOracle.setVisible( true );
            }
            
            env.Parameters.oracleFunctionEnabled = isEnabled;

            if(isEnabled){
                jLabel_infobar_FlagOfOracle.setIcon(icon_oracle_enabled);
                jLabel_infobar_FlagOfOracle.setToolTipText("Oracle Dialog Enabled to find similar annotations.");
            } else {
                jLabel_infobar_FlagOfOracle.setIcon(icon_oracle_disabled);
                jLabel_infobar_FlagOfOracle.setToolTipText("Oracle Dialog DISABLED!");
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1010111612:: fail to set status and icon to indicate oracle ");
        }
    }
    
    /**set flag and corrlated lable's icon and tiptool text.*/
    //public void  setFlag_of_GraphPath_Display(boolean enabled_or_not ){
        //try{
            //env.Parameters.enabled_GraphPath_Display = enabled_or_not;

            //if(enabled_or_not){
            //    jLabel_infobar_FlagOfPath.setIcon(icon_graphicpath_enabled);
            //    jLabel_infobar_FlagOfPath.setToolTipText("View relationships.");
            //} else {
            //    jLabel_infobar_FlagOfPath.setIcon(icon_graphicpath_disabled);
            //    jLabel_infobar_FlagOfPath.setToolTipText("Hided relationships!");
            //}
        //}catch(Exception ex){
        //    log.LoggingToFile.log( Level.SEVERE, "error 1010111612:: fail to set status and icon to graph path ");
        //}
    //}


    public void display_updateDifferenceIndicator(){
        setFlag_of_DifferenceMatching_Display( env.Parameters.enabled_Diff_Display );
    }
            
    /**set flag and corrlated lable's icon and tiptool text.*/
    public void  setFlag_of_DifferenceMatching_Display(boolean isEnabled ){
        try{
            env.Parameters.enabled_Diff_Display = isEnabled;

            if(isEnabled){
                jLabel_infobar_FlagOfDiff.setIcon(icon_difference_enabled);
                try{
                    if( WorkSet.getCurrentFile()!= null ){
                        resultEditor.conflicts.DifferentMatching diff = new
                            resultEditor.conflicts.DifferentMatching(textPaneforClinicalNotes);
                        diff.search_differentMatching( WorkSet.getCurrentFile()  );
                    }
                    
                }catch(Exception ex){
                    
                }

            } else {
                jLabel_infobar_FlagOfDiff.setIcon(icon_difference_disabled);
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1010111612:: fail to set status and icon to graph path ");
        }
    }

    /**set flag and corrlated lable's icon and tiptool text.*/
    public void  setFlag_of_AttributeEditorDisplay_Display(boolean isEnabled ){
        try{
            env.Parameters.enabled_displayAttributeEditor = isEnabled;

            if(isEnabled){
                jLabel_infobar_attributeEditor.setIcon(icon_attribute_enabled);
            } else {
                jLabel_infobar_attributeEditor.setIcon(icon_attribute_disabled);
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1010111612:: fail to set status and icon to graph path ");
        }
    }



    /**remove all highlighted annotation, and only highlight annotations whose
     * texts equal to the given text.
     */
    public void highlightAnnotations(String annotationText ) {
        // highlight manger
        final resultEditor.underlinePainting.SelectionHighlighter highlighter =
            new resultEditor.underlinePainting.SelectionHighlighter(textPaneforClinicalNotes);
        highlighter.RemoveAllUnderlineHighlight();

        if ( annotationText == null )
            return;
        if ( annotationText.trim().length() < 1 )
            return;

        File currentTextSource = resultEditor.workSpace.WorkSet.getCurrentFile();
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        resultEditor.annotations.Article article = depot.getArticleByFilename( currentTextSource.getName() );
        if ( article == null )
            return;
        boolean isCarePositionSet = false;
        for( resultEditor.annotations.Annotation annotation : article.annotations ){
            if ( annotation == null )
                continue;
            
            if ( annotation.annotationText == null )
                continue;
                        
            if ( annotation.annotationText.trim().compareTo( annotationText.trim() ) == 0 ){

                if(annotation.spanset==null)
                    continue;
                int size = annotation.spanset.size();
                for(int t=0; t<size; t++){
                    SpanDef span = annotation.spanset.getSpanAt(t);
                    if(span==null)
                        continue;
                    highlighter.addNewUnderlineHightlight( span.start, span.end );
                }

                
                if( !isCarePositionSet ) {
                    display_setCarpetOn( annotation.spanset.getMinimumStart() );
                    isCarePositionSet = true;
                }
            }
        }

    }
    /**
     * Add text source files into memory. Before adding, check repetitive and
     * only add these files who are new to current text source list.
     *
     * @param   textfiles
     *          List of files we want to add into current list of text sources.
     */
    public void addTextFiles(Vector<File> textfiles){
        if ( textfiles == null )
            return;

        for( File file : textfiles )
            addTextFile( file );
    }

    /**
     * Add just one text source files into memory. Before adding, check repetitive and
     * only add file who is new to current text source list.
     *
     * @param   textfile
     *          The text file we want to add into current list of text sources.
     */
    public void addTextFile(File textfile){
        env.Parameters.corpus.addTextFile( textfile );
    }

    /**
     * Show text information on the bar at the bottom of the GUI.
     *
     * @param   textinfo
     *          The text string you want to show on the bar at the bottom of the GUI.
     */
    public void showText_OnBottomInfoBar(String textinfo){
        this.jLabel_infobar.setText(textinfo);
    }

    /**Set Caret position */
    public void setFocusOn(int position){
        display_setCarpetOn(position);
    }

    public void updateComplex()
    {
        if(WorkSet.currentAnnotation != null)
        {
            this.display_showAnnotationDetail_onEditorPanel(WorkSet.currentAnnotation);
            this.showSelectedAnnotations_inList(0);
        }
    }    
    
    /**
     * Clear and re-list attributes of given annotation. If the given annotation
     * is null, show empty list.
     */
    public void updateAttributes(Annotation a) {

        // ----1---- 
        // empty the list of attribute
        jList_normalrelationship.setListData(new Vector());
        
        if((a==null)||(a.attributes==null)||(a.attributes.size()<1))
            return;
        
        Vector<String> list = new Vector<String>();
        for (resultEditor.annotations.AnnotationAttributeDef attribute : a.attributes) {
            if ((attribute == null)||(attribute.value==null)||(attribute.name==null)) {
                continue;
            }

            String str;
            str = " \"" + attribute.name + "\" = " + attribute.value;
            
            list.add(str);

        }
        
        jList_normalrelationship.setListData(list);
    }

    public void display_diff_checkDifference() {
        ((userInterface.annotationCompare.ExpandButton)jPanel60).cr_recheckDifference();
    }
    
    
    public void display_diff_updateAttributes() {
        ((userInterface.annotationCompare.ExpandButton)jPanel60).cr_updateAnnotation_onComparatorPanel();
        ((userInterface.annotationCompare.ExpandButton)jPanel60).cr_recheckDifference();
    }

    public boolean isShowAnnotations_toAllDoc(){
        if (jRadioButton_treeview_currentarticle.isSelected())
            return false;
        else return true;
    }

    /**Update value to screen while init the GUI of eHOST.*/
    public void updateScreen_for_variables(){
        try{
            // update screen for annotator name
            resultEditor.annotator.Manager annotatormanager = new
                    resultEditor.annotator.Manager(this.jLabel_info_annotator);
            annotatormanager.showCurrentAnnotator();

            setFlag_of_OracleSimilarPhraseSeeker(env.Parameters.oracleFunctionEnabled);
            //setFlag_of_GraphPath_Display(env.Parameters.enabled_GraphPath_Display );
            
            setFlag_of_DifferenceMatching_Display(env.Parameters.enabled_Diff_Display );
            System.out.println("Current Status of the Difference Indicator = " + env.Parameters.enabled_Diff_Display );
            display_repaintHighlighter();
            
            setFlag_of_AttributeEditorDisplay_Display( env.Parameters.enabled_displayAttributeEditor );
            
            if ((env.Parameters.OracleStatus.visible == false)
                || (env.Parameters.OracleStatus.sysvisible == false)) {

            env.Parameters.oracleFunctionEnabled = false;
            jLabel_infobar_FlagOfOracle.setVisible(false);
        } else {
            jLabel_infobar_FlagOfOracle.setVisible(true);
        }
            
        }catch(Exception ex){

        }
    }

    
    public void display_setCarpetOn(int position) {
        try {
            // get visible window of the text panel
            int viewy1 = this.textPaneforClinicalNotes.getVisibleRect().y;
            int viewy2 = this.textPaneforClinicalNotes.getVisibleRect().y
                    + this.textPaneforClinicalNotes.getVisibleRect().height;

            Rectangle rect = this.textPaneforClinicalNotes.getUI().modelToView(this.textPaneforClinicalNotes, position);

            if (rect == null) {
                return;
            }

            if ((rect.y <= viewy2 + 10) && (rect.y >= viewy1 + 10)) {
                return;
            }

            this.textPaneforClinicalNotes.setCaretPosition(position);
        } catch (Exception ex) {
            
        }
    }

    
    /**
     * Called to display or refresh all different matching of current document
     * on text viewer
     */
    public void showDifferentMatching(){
        try{

            // remove all existing wave underlines
            resultEditor.underlinePainting.SelectionHighlighter painter
                    = new resultEditor.underlinePainting.SelectionHighlighter(this.textPaneforClinicalNotes);
            painter.RemoveAllWaveHighlight();

            // get current test source
            File curr_f = resultEditor.workSpace.WorkSet.getCurrentFile();

            

        }catch(Exception ex){
        }
    }


    /**Use difference information stored in inner class "flags_of_differences"
     * to set the borders of corresponding components. Use red rectangles to indicate
     * difference.
     *
     * This function only can be called after you have run function of
     * "checkDifferences(Annotation)".
     */
    public void display_setRedRectangle_toMarkDifferences(
            final boolean isDifference_inSpan,
            final boolean isDifference_inClass,
            //final boolean isDifference_inComment,
            final boolean isDifference_inAnnotator,
            final boolean isDifference_inComplexRelationship,
            final boolean isDifference_inNormalRelationship
            ){

        display_removeRectangle_toPotensionMarkedDifferences();

        // ##1## set boarder if annotation span difference found
        try{
            if ((isDifference_inSpan)&&(env.Parameters.DifferenceMatching.checkatt_forSpan)){
                jScrollPane_Spans.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jScrollPane_Spans.setBorder( javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)) );
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::21");}

        // ##3## set boarder if annotation span difference found
        try{
            if ((isDifference_inClass)&&(env.Parameters.DifferenceMatching.checkatt_forClass)){
                jTextField_annotationClassnames.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jTextField_annotationClassnames.setBorder(STANDARD_TEXTFIELD_BOARD);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::23");}

        // ##4## set boarder if  difference found in annotation comments
        /*try{
            if ((isDifference_inComment)&&(env.Parameters.DifferenceMatching.checkatt_forComment)){
                jPanel66.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel66.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::24");}
        */
        
        // ##5## set boarder if  difference found in annotator of annotations
        try{
            if ((isDifference_inAnnotator)&&(env.Parameters.DifferenceMatching.checkatt_forAnnotator)){
                jTextField_annotator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::25");}


        // ##6## set boarder if  difference found in complex relationships of annotations
        try{
            if ((isDifference_inComplexRelationship)&&(env.Parameters.DifferenceMatching.checkatt_forComplex)){
                jPanel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel70.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::26");}

        // ##7## set boarder if  difference found in normal relationships of annotations
        try{
            if ((isDifference_inNormalRelationship)&&(env.Parameters.DifferenceMatching.checkatt_forNormal)){
                jPanel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2));
            }else{
                jPanel71.setBorder(null);
            }
        }catch(Exception ex){log.LoggingToFile.log( Level.SEVERE, "error 1011162346::27");}

    }


    /**This function will set normal border to these components whose border
     * maybe be set to red rectangle to show differences in annotation
     * comparation mode.
     */
    public void display_removeRectangle_toPotensionMarkedDifferences(){

        // ##1## set boarder if annotation span difference found
        jScrollPane_Spans.setBorder( javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)) );

        // ##3## set boarder if annotation span difference found
        jTextField_annotationClassnames.setBorder(STANDARD_TEXTFIELD_BOARD);


        // ##4## set boarder if  difference found in annotation comments
        jPanel66.setBorder(null);


        // ##5## set boarder if  difference found in annotator of annotations
        jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);



        // ##6## set boarder if  difference found in complex relationships of annotations
        jPanel70.setBorder(null);


        // ##7## set boarder if  difference found in normal relationships of annotations
        jPanel71.setBorder(null);

    }

    /**Forbiding any modification operation while eHOST enter the mode of
     * comparing differences between annotations.
     *
     * It will be called from class "userInterface.annotationCompare.ExpandButton.java"
     */
    public void display_DisabledEditorModification_forCompareMode(){
        // buttons of span editor (change spans or delete an annotation)
        //jButton_spaneditor_lefttToLeft.setVisible(false); 
        //jButton_spaneditor_delete.setVisible(false); // button "delete"        
        //jButton_spaneditor_leftToRight.setVisible(false);
        //jButton_span_rightToLeft.setVisible(false);
        //jButton4_spanEditor_rightToRight.setVisible(false);
        
        jButton_SelectClass.setVisible(false);
        jButton_relationships.setVisible(false);        
        delete_Relationships.setEnabled(false);
        jPanel21.setVisible(false);
        jButton_relationships.setVisible(false);
   
    }

    /**Allow modification operation after eHOST leaving the mode of
     * comparing differences between annotations.
     *
     * It will be called from class "userInterface.annotationCompare.ExpandButton.java"
     */
    public void display_EnabledEditorModification_forLeaveCompareMode(){
        
        // buttons of span editor (change spans or delete an annotation)
        //jButton_spaneditor_lefttToLeft.setVisible(true);
        //jButton_spaneditor_delete.setVisible(true); // button "delete"        
        //jButton_spaneditor_leftToRight.setVisible(true);
        //jButton_span_rightToLeft.setVisible(true);
        //jButton4_spanEditor_rightToRight.setVisible(true);

        jButton_SelectClass.setVisible(true);
        jPanel21.setVisible(true);
        jButton_relationships.setVisible(true);
        if(this.jList_complexrelationships.getSelectedIndex() >= 0)
        {
            delete_Relationships.setEnabled(true);
        }
        

        jButton_relationships.setVisible(true);
    }

    /**show all classes in tree view*/
    public void showAnnotationCategoriesInTreeView_refresh(){



        resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);
        
        if(resultEditor.workSpace.WorkSet.getCurrentFile()==null)
        {
            log.LoggingToFile.log( Level.SEVERE, "### ERROR ### 1106091630:: fail to get current filename");
            sct.removeAllNodehighlight();
            sct.display();
            return;
        }

        sct.refreshTypeMemory_refresh();
        sct.display();
        //System.out.println("^^11");
    }

    /**count and show all counting information splitting by annotation classes
     * to all documents. */
    public void showAnnotationCategoriesInTreeView_All() {

        // new instance of class to show class information
        resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);
        
        // indicated that we are dealing with all documents
        sct.refreshTypeMemory_all();

        sct.display();  // repaint class information
        
    }

    public void showAnnotationCategoriesInTreeView_CurrentArticle() {

        boolean onlyShowCurrentArticle = this.jRadioButton_treeview_currentarticle.isSelected();
        resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);

        File currentfile = resultEditor.workSpace.WorkSet.getCurrentFile();
        if (resultEditor.workSpace.WorkSet.getCurrentFile() == null) {
            sct.removeAllNodehighlight();
            sct.display();
            log.LoggingToFile.log(Level.SEVERE, "### ERROR ### 1106091631:: fail to get current filename");
            return;
        }

        if (onlyShowCurrentArticle) {
            if ((currentfile == null) || (currentfile.getName().trim().length() < 1)) {
                //System.out.println("^^44");
                showAnnotationCategoriesInTreeView_All();
                return;
            } else {

                sct.refreshTypeMemory_currentArticle(currentfile.getName().trim());
                sct.display();
                //System.out.println("^^33");

            }
        } else {
            this.showAnnotationCategoriesInTreeView_All();
            //System.out.println("^^55");
        }

    }

    public void showAnnotationCategoriesInTreeView_selectCategory(String category, String annotation_text){
        resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);
        sct.setCategoryNodeSelected( category, annotation_text );
    }



    public void setAnnotationVisible(){
        resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);
        sct.setAnnotationsVisile();
    }
    public void totalRefresh()
    {
        // clear all attributes value display and disable those buttons as
        // no annotation are selected just after annotations been imported.
        disableAnnotationDisplay();

        this.showAnnotationCategoriesInTreeView_refresh();

        currentScreen = infoScreens.NONE;
        refreshInfo();
        
        display_showSymbolIndicators();
        showValidPositionIndicators();
    }

    /***/
    public void showTextFiles_inComboBox() {
        // add file names to the vector
        int size = env.Parameters.corpus.LIST_ClinicalNotes.size();
        jComboBox_InputFileList.removeAllItems();
        // show inputted file into the combobox
        for (env.clinicalNoteList.CorpusStructure corpusfile:env.Parameters.corpus.LIST_ClinicalNotes) {
            if (corpusfile==null)
                continue;
            if (corpusfile.file == null)
                continue;

            jComboBox_InputFileList.addItem( corpusfile.file.getName() );
        }
    }
    
    /**
     * Show all text sources in the memory onto the screen.
     */
    /*public void showTextFiles(){

        // clean the table
        ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).getDataVector().removeAllElements();
        ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).fireTableDataChanged();

        // show in the table
        for (int i = 0; i < env.Parameters.LIST_ClinicalNotes.size(); i++) {
            Object[] row = {
                (Object)env.Parameters.LIST_ClinicalNotes.get(i).filename,
                (Object)env.Parameters.LIST_ClinicalNotes.get(i).amountOfWords
            };
            ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).addRow(row);

        }
        ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).fireTableDataChanged();

        // show on screen: how many clinical text files get selected
        jLabel_filecollection.setText(" >FILE COLLECTION (" + env.Parameters.LIST_ClinicalNotes.size()+")");

        // attribute of component: not editable
        jComboBox_InputFileList.setEditable(false);
        textPaneforClinicalNotes.setEditable(false);
    }*/

    

    /**This funcation only can be called by dialogs.exit after you had click
     * the yes or no button. If answer is "yes", then close the whole application;
     * O.W, do nothing.
     */
    public void after_click_exit(boolean _userWantToQuit) {
        if ( _userWantToQuit )
            System.exit(-99);
    }

    

    /**Popup a dialog in the middle of current program window. It's designed to
     * reminder user whether they really want to exit.
     * Clicking on the "exit" button leads to shut down the whole eHOST system.
     */
    public void popupExitDialog() {
        
        // program will confirm whether user wants "exit" or not

        // set this dialog in the middle of current window
        confirmExit.setSize(416, 200);
        int x = this.getX() + (int) ((this.getWidth() - confirmExit.getWidth()) / 2);
        int y = this.getY() + (int) ((this.getHeight() - confirmExit.getHeight()) / 2);
        confirmExit.setLocation(x, y);
        // modal window, so all threads will be stopped until this dialog got closed.
        confirmExit.setModal(true);
        confirmExit.setVisible(true);

        // call post-method to handle users' selection ("quit" or "not")
        this.after_click_exit(Flag_Want_Shutdown_Program);
    }

    
    
    /**empty all attributes/text field in result editor, which maybe got
     * used in pervious operation */
    public void disableAnnotationDisplay()
    {
        try{
            display_RelationshipPath_Remove();


            Vector v = new Vector();
            v.clear();
            jList_selectedAnnotations.setListData(v);
            jList_normalrelationship.setListData(v);
            jList_complexrelationships.setListData(v);
            //jLabel_typeOfRelationship.setText("Attributes: ");
            jTextPane_explanations.setText(null);
            jList3.setListData(v);
            jScrollPane_Spans.setBorder( javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)) );
            jTextArea_comment.setText("");
            jList3.setBorder(null);
            jList3.setListData(v);
            jTextField_annotationClassnames.setText(null);

            jTextField_annotator.setText(null);
            jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);

            jTextField_creationdate.setText(null);
            jTextField_creationdate.setBorder(STANDARD_TEXTFIELD_BOARD);

            jButton_SelectClass.setEnabled(false);

            

            // refresh screen only if the text pane is visiable go user;
            // otherwise, do nothing.
            if(textPaneforClinicalNotes.isVisible())
            {
                resultEditor.underlinePainting.SelectionHighlighter painter = new resultEditor.underlinePainting.SelectionHighlighter(
                    this.textPaneforClinicalNotes);
                painter.RemoveAllUnderlineHighlight();
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "\n==== ERROR ====:: 1106101243:: fail to clear screen for new document"
                    + ex.toString() );
        }
    }

    public void remove_all_underline_highlighter(){
        resultEditor.underlinePainting.SelectionHighlighter painter = new resultEditor.underlinePainting.SelectionHighlighter(
                this.textPaneforClinicalNotes);
        painter.RemoveAllUnderlineHighlight();
    }

    public void enable_AnnotationEditButtons(){
        // enable buttons for span and class edit
        jButton_SelectClass.setEnabled(true);
        jButton_spaneditor_lefttToLeft.setEnabled(true);
        jButton_spaneditor_leftToRight.setEnabled(true);
        jButton_span_rightToLeft.setEnabled(true);
        jButton_spaneditor_delete.setEnabled(true);
        jButton4_spanEditor_rightToRight.setEnabled(true);
        //jButton11.setEnabled(true);
        jButton_SelectClass.setEnabled(true);
        jButton_relationships.setEnabled(true);
        if(this.jList_complexrelationships.getSelectedIndex() >= 0)
        {            
            delete_Relationships.setEnabled(true);
        }
        jButton_attribute.setEnabled(true);
    }


    private void showAnnotatorsOfAllSelectedAnnotations()
    {
        Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
        if( selectedAnnotationIndexes == null )
            return;

        Depot depot = new Depot();
        String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

        int size = selectedAnnotationIndexes.size();
        String name = "";
        for(int i =0; i<size; i++ )
        {
            int uniqueindex = selectedAnnotationIndexes.get(i);

            resultEditor.annotations.Annotation annotation1 = depot.getAnnotationByUnique( textsourcefilename, uniqueindex );
            if(annotation1 == null || !annotation1.visible)
               continue;

            if(annotation1.getFullAnnotator() ==null)
                continue;
            if(annotation1.getFullAnnotator().trim().length()<1)
                continue;

            name = name + annotation1.getFullAnnotator() + ", ";

        }

        jTextField_annotator.setText(name);
        jTextField_annotator.updateUI();
    }

    

    /**while we selected some annotations by clicking mouse on text area, and
     * got some annotations selected under this point. We use this method to
     * check wether these annoations are all same or not
     *
     * The default return is "false";
     */
    private boolean isAllSelectedAnnotationSame()
    {
        try{

            Vector<Integer> selectedAnnotationIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if( selectedAnnotationIndexes == null )
                return false;

            Depot depot = new Depot();
            String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

            int size = selectedAnnotationIndexes.size();
            for(int i =0; i<size; i++ )
            {
                int uniqueindex = selectedAnnotationIndexes.get(i);

                resultEditor.annotations.Annotation annotation1 = depot.getAnnotationByUnique( textsourcefilename, uniqueindex );
                if( annotation1 == null || !annotation1.visible )
                   continue;

                if(i==(size-1))
                    continue;
                
                for(int j=i+1; j<size; j++ )
                {
                    int uniqueindex2 = selectedAnnotationIndexes.get(j);
                    resultEditor.annotations.Annotation annotation2 = depot.getAnnotationByUnique( textsourcefilename, uniqueindex2 );
                    if(annotation2 == null || !annotation2.visible)
                        continue;
                    
                    boolean samespan = report.iaaReport.analysis.detailsNonMatches.Comparator.equalSpans(annotation1, annotation2);
                    if(!samespan)
                        return false;
                    boolean sameclass = report.iaaReport.analysis.detailsNonMatches.Comparator.equalClasses(annotation1, annotation2);
                    if(!sameclass)
                        return false;
                    boolean sameatt = report.iaaReport.analysis.detailsNonMatches.Comparator.equalAttributes(annotation1, annotation2);
                    if(!sameatt)
                        return false;
                    boolean samerel = report.iaaReport.analysis.detailsNonMatches.Comparator.equalRelationships(annotation1, annotation2, WorkSet.getCurrentFile().getName());
                    if(!samerel)
                        return false;

                }
            }

            return true;

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1109280007::fail to compare multiple selected annotaitons!");
        }

        return false;
    }

    /**Show your selected annotations in list and show details of first
     * annotaion's details on related field. You you left clicked mouse on
     * a position of a text source. You may select one or more annotaions who
     * span cover the point you just clicked;
     *
     * @param initializeIndex    Index of annotations marked as preselected in the list.
     * This annotation will be highlighted by colorful under line. */
    public void showSelectedAnnotations_inList(int initializeIndex){

        resultEditor.selectedAnnotationView.AnnotationListScreen list
                = new resultEditor.selectedAnnotationView.AnnotationListScreen(
                    this,
                    this.jList_selectedAnnotations,
                    this.jLabel_icon_annotationlist.getIcon());
        list.showAnnotations(initializeIndex);
    }
    
    public void showSelectedAnnotations_inList(Annotation annotation){

        resultEditor.selectedAnnotationView.AnnotationListScreen list
                = new resultEditor.selectedAnnotationView.AnnotationListScreen(
                    this,
                    this.jList_selectedAnnotations,
                    this.jLabel_icon_annotationlist.getIcon());
        list.showAnnotations(annotation);
    }

    public void display_showSelectedAnnotations_inListOnEditorPanel(int uniqueindex){
        
        resultEditor.selectedAnnotationView.AnnotationListScreen list
                = new resultEditor.selectedAnnotationView.AnnotationListScreen(
                    this,
                    this.jList_selectedAnnotations,
                    this.jLabel_icon_annotationlist.getIcon());
        list.showAnnotations_withSpecificUniqueindex(uniqueindex);
    }

    public void display_annotation_inListOnEditorPanel(int uniqueindex){
        
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.onlyOneAnnotationSelected( uniqueindex );
        
        resultEditor.selectedAnnotationView.AnnotationListScreen list
                = new resultEditor.selectedAnnotationView.AnnotationListScreen(
                    this,
                    this.jList_selectedAnnotations,
                    this.jLabel_icon_annotationlist.getIcon());
        
        list.showAnnotations_withSpecificUniqueindex( uniqueindex );
        Annotation ann = depot.getAnnotationByUnique( WorkSet.getCurrentFile().getName(), uniqueindex);
        if( ann != null )
        this.textPaneforClinicalNotes.setCaretPosition( ann.spanset.getMaximumEnd() );
    }
    
    
    /**clean and repaint all background highlight indicator in the text pane */
    public void display_repaintHighlighter(){
        //removeAllBeckgroundHighlight();
        //paintBackgroundHighlight();
        resultEditor.display.Screen screen = new resultEditor.display.Screen(
                textPaneforClinicalNotes,
                WorkSet.getCurrentFile() );
        screen.repaintHighlight( );
        
        this.display_removeSymbolIndicators();
        this.display_showSymbolIndicators();

    }

    
    /**
     * This function is called only from annotations comparator, for accept or
     * reject annotations;
     * It only show one annotation from the selected annotation set; Other
     * annotation will be show on the comparator panel as alternative items.
     * This function is very similar to method "showSelectedAnnotations_inList";
     *
     * @param   uniqueindex */
    public void display_showOneAnnotation_inListOnEditorPanel(int uniqueindex){

        try{
            resultEditor.selectedAnnotationView.AnnotationListScreen list
                = new resultEditor.selectedAnnotationView.AnnotationListScreen(
                    this,
                    this.jList_selectedAnnotations,
                    this.jLabel_icon_annotationlist.getIcon());

            list.onlyshowAnnotation(uniqueindex);



        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1011152253:: fail to show one annotation " +
                    "in the editor panel for annotation accept/reject!!!");
        }
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

                span.text = getText(span.start, span.end);

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

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE,"error 1101211147:: fail to list corpus on screen"
                    + ex.getMessage());
        }        
    }

    /**return the designated text from the document viewer.
     */
    public String getText(int start, int end){
        if((start<0)||(end<0)||(end<=start))
            return null;

        try{
            return textPaneforClinicalNotes.getText(start, end-start);
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "can't get designated span text from current document viewer!");
            return null;
        }
        
    }


    public void display_showAnnotationDetail_onEditorPanel(
            resultEditor.annotations.Annotation annotation){
        display_showAnnotationDetail_onEditorPanel(annotation, false);
    }

    
    /**
     * * load method to draw graphic path of relationships of this annotation
     */
    public void display_showAnnotationDetail_onEditorPanel(
            resultEditor.annotations.Annotation annotation, boolean _onlyshowDetails){
        
        this.display_RelationshipPath_Remove();
        
        //System.out.println("show details: annotation:"+annotation.toString());
        if(!_onlyshowDetails)
            disableAnnotationDisplay();

        if ( annotation == null ) {
            log.LoggingToFile.log(Level.SEVERE, "error 1011170312:: the annotation which you " +
                    "want to show details on screen is empty!!!");
            return;

        } else {

            resultEditor.workSpace.WorkSet.currentAnnotation = annotation;

            // show graph path for complex relationships if have
            if(env.Parameters.enabled_GraphPath_Display)
                display_relationshipPath_setPath( annotation );
            

            
            String annotator = annotation.getFullAnnotator();

            // list the spans of current annotation
            listSpans(jList_Spans, annotation);
            
            jTextField_annotator.setText(null);
            jTextField_annotationClassnames.setText( annotation.annotationclass );

                // mark its classname in the navigator panel
                showAnnotationCategoriesInTreeView_selectCategory( annotation.annotationclass, annotation.annotationText );

                if( ( annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK )
                        && ( GUI.reviewmode == GUI.ReviewMode.adjudicationMode) ) {
                    jTextField_annotator.setText( "ADJUDICATION" );
                    jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
                }else if( (annotator != null )&&( annotator.trim().length() > 0) ) {
                    jTextField_annotator.setText( annotator );
                    jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
                } else {
                    jTextField_annotator.setText(null);
                    setRedBorder( jTextField_annotator );
                }

                if( (annotation.creationDate != null )&&( annotation.creationDate.trim().length() > 0) ) {
                    jTextField_creationdate.setText( annotation.creationDate);
                    jTextField_creationdate.setBorder(STANDARD_TEXTFIELD_BOARD);
                } else {
                    jTextField_creationdate.setText( null );
                    setRedBorder(jTextField_creationdate);
                }

            // textsourceFilename of current text source
            String textsourceFilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

            // get selected annotations by mouse's positin
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            Vector<Integer> selectedAnnotationUniqueindexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();

            int size = selectedAnnotationUniqueindexes.size();
            String textsourcefilename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();
            Vector<suggestion> suggested = new Vector<suggestion>();
            Vector<String> explanations = new Vector<String>();

            /*for (int i = 0; i < size; i++) {
                int index = selectedAnnotationUniqueindexes.get(i);
                resultEditor.annotations.Annotation anAnnotation 
                        = depot.getAnnotationByUnique(
                        textsourcefilename,
                        index
                        );
                for (suggestion suggest : anAnnotation.verifierFound) {
                    if (!explanations.contains(suggest.getExplanation()))
                        explanations.add(suggest.getExplanation());
                    else if (!suggested.contains(suggest))
                        suggested.add(suggest);
                }
            }*/

            String explanationText = "";
            for (String explained : explanations) {
                explanationText += explained + "\n";
            }
            this.jList3.setListData(suggested);
            jList3.setSelectedIndex(0);
            jTextPane_explanations.setText(explanationText);
            jTextArea_comment.setText(annotation.comments);
            setVerifierSuggestion(annotation.verifierSuggestion);
            display_editor_ListRelationships(annotation);

        }
    }

    public void showRelationships_Refresh(){
        // get current annotation
        resultEditor.annotations.Annotation annotation
                = resultEditor.workSpace.WorkSet.currentAnnotation;

        // refresh display of attributes of this annotation in result editor
        display_editor_ListRelationships( annotation );

    }
    

    /**if suggestions of a annotation is not empty, show contents and highlight the rectangle in red lines.*/
    public void setVerifierSuggestion(Vector<String> suggestions){
        if ((suggestions == null)||(suggestions.size() <1)) {
            jList3.setBorder(null);
            Vector v = new Vector();
            jList3.setListData(v);
            return;
        }else{
            int size = suggestions.size();
            jList3.setListData(suggestions);
            setRedBorder();
        }

    }

    /** highlight specific words or phrases */
    public void display_markPhrase( Annotation annot){
        // offset as there might have some conflict button in this textpane
        //int offset = ResultEditor.Conflicts.ConflictButtonPainter.getScreenOffset(start, 1);
        //jLabel1.setText("offset = "+ offset);

        // highlight manger
        final resultEditor.underlinePainting.SelectionHighlighter hightlighter =
            new resultEditor.underlinePainting.SelectionHighlighter(textPaneforClinicalNotes);
        hightlighter.setNewUnderlineHightlight(annot);
    }
    
     public void goUserDesignated(String filename){
        if (( filename == null )||(filename.trim().length() < 1))
            return;
        int size = jComboBox_InputFileList.getModel().getSize();
        for( int i = 0; i<size; i++){
            if ( jComboBox_InputFileList.getItemAt(i).toString().equals(filename) ){
                jComboBox_InputFileList.setSelectedIndex(i);

                resultEditor.workSpace.WorkSet.setCurrentFile(i);
                File f = resultEditor.workSpace.WorkSet.getAllTextFile()[i];
                resultEditor.workSpace.WorkSet.setCurrentFile(f);

                goUserDesignated();
                return;
            }
        }

        log.LoggingToFile.log(Level.SEVERE, "1008190444 - fail to find index of designated text source.");
    }


    public void display_SetStatusInsiable_ToComparisionPanel(){
        ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
    }
    public void paintBackgroundHighlight(){
    }

    public void removeSelection(){
        //textPaneforClinicalNotes.setSelectionStart( 0 );
        textPaneforClinicalNotes.setSelectionEnd( -1 );

    }

    public void addPositionIndicator(){
        resultEditor.positionIndicator.Display assemble = new
                resultEditor.positionIndicator.Display( this, this.jPanel_position_indicator_container );
    }

    /**indicate the position indicator*/
    public void setPositionIndicator( resultEditor.positionIndicator.JPositionIndicator positionindicator ){
        if ( ( positionindicator != null )
                &&( positionindicator instanceof resultEditor.positionIndicator.JPositionIndicator ))
            this.jpositionIndicator = positionindicator;
    }

    /**
     * by users' click, dicide which navigator menu item got selected and then
     * active related panel.
     *
     * @param   i   0 means first panel, 1 means second panel
     */
    public void setNAVCurrentTab( int i ){
        
        CardLayout card = (CardLayout) jPanel_NAV_CardContainer.getLayout();
        switch(i) {
            case 2:
                //((ResultEditor.CustomComponents.NagivationMenuItem)jPanel_SelectFiles).setSelected();
                //((ResultEditor.CustomComponents.NagivationMenuItem)jPanel_AnnotationsAndMarkables).setNormal();
                card.show( jPanel_NAV_CardContainer, "card3");
                this.ActivedNVATAB = 2;
                break;
            case 3:
                //((ResultEditor.CustomComponents.NagivationMenuItem)jPanel_SelectFiles).setNormal();
                //((ResultEditor.CustomComponents.NagivationMenuItem)jPanel_AnnotationsAndMarkables).setSelected();

                card.show( jPanel_NAV_CardContainer, "card2");
                ((navigatorContainer.TabPanel)NavigationPanel1).setTabActived("Navigator");
                break;
            case 1:                
                card.show( jPanel_NAV_CardContainer, "card4");
                this.ActivedNVATAB = 1;
                display_refreshNAV_WorkSpace();
                break;
        }
    }

    //static int etdsei=0;
    /**remove and then redraw all position indicators*/
    public void showValidPositionIndicators(){
        //etdsei++;
        //System.out.println(etdsei);
        
        try{
            if(jTabbedPane3.getSelectedIndex() == 0)
            {
                File file = resultEditor.workSpace.WorkSet.getCurrentFile();

                //System.out.println( "current show position indicators for file:[" + file.getName().trim() + "]" );
                jpositionIndicator.removeAllIndicators();
                if((file!=null)&&(file.exists()))
                        jpositionIndicator.paintArticle( file.getName().trim() );
                jpositionIndicator.forcepaint();
                jpositionIndicator.repaint();
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1103141511:: fail to show valid postion indicators"
                    + ex.getMessage());
        }
    }

    public void showValidPositionIndicators_setAll(){
        this.jpositionIndicator.removeDesignatedAnnotation();
    }

    public void showValidPositionIndicators_setDesignated(String annotationname){
        this.jpositionIndicator.setDesignatedAnnotation(annotationname);
    }
    
    public void checkWorkSpaceDialog(){
        // check results
        String path = env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath;
        boolean success_inWorkspaceSetting = isValidWorkspace(path);

        // ### 1 ### if this path is a valid path for workspace
        if(success_inWorkspaceSetting)
        {

            // record recent accessed workspace
            recordRecentUsedWorkspace(path);
            
            //this.jCardcontainer_interactive.setVisible(true);
            //((ResultEditor.CustomComponents.ExpandablePanel)NavigationPanel).defaultstatus();
            ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).defaultstatus();
            config.system.SysConf.saveSystemConfigure();
            this.refreshNAVProjects();
        }
        // ### 2 ### if this path is NOT a valid path for workspace
        else
        {
            commons.Tools.beep();
            log.LoggingToFile.log(Level.SEVERE, "1102110425::current workspace path is invalid!!!");
            setWorkSpace();
        }

    }

    /**This method will call Workspace.Switcher.RecentWOrkspac to record
     * recent used workspace;
     */
    private void recordRecentUsedWorkspace(String _path)
    {
        File workspace = new File(_path);

        workSpace.switcher.RecentWorkSpace rw = new workSpace.switcher.RecentWorkSpace();
        rw.addWorkspace(workspace);
        
    }


    public void refreshNAVProjects(){
        refreshFileList();
        display_refreshNAV_WorkSpace();
        // if( env.Parameters.WorkSpace.CurrentProject != null )
        config.system.SysConf.saveSystemConfigure();
        
    }

    public void reavtiveMainPanel(){
        this.jCardcontainer_interactive.setVisible(true);
        //((ResultEditor.CustomComponents.ExpandablePanel)NavigationPanel).defaultstatus();
        ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).defaultstatus();
    }


    /**Remove all layer Components from eHOST gui*/
    public void display_RemoveAllLayeredComponents() {
        this.getLayeredPane().removeAll();
    }
    
    /**Subsequence processes after all components got initilized; Only need to
     * be called in the contructor to class GUI
     */
    public void componentPostProcessing()
    {

        try{
            
            jToggleButton_sequence_inLocation.setVisible( false );
            jToggleButton_sequence_inCharacters.setVisible( false );
            // hide the icon of turn on/off the display of relationship path
            //jLabel_infobar_FlagOfPath.setVisible( false );
            
            // PFT
            jButton_importAnnotations1.setVisible( false );
            
            jButton_removeduplicates.setVisible( false );
                    
            // 
            env.Parameters.working_on_file = true;
            
            // workspace list
            jComboBox_currentworkspace_abspath.setPreferredSize(new Dimension(300, 27));

            // no review mode while we just loaded eHOST, components will be hided
            setReviewMode( reviewmode.OTHERS );
            
            // seting the tab panel
            ((navigatorContainer.TabPanel)NavigationPanel1).expand();

            this.setNAVonFirstOrSecondPage();

            ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor).setDiffSplitPane( this.jPanel_MainFrame_Right );
            //((ResultEditor.CustomComponents.ExpandablePanel_editor)NavigationPanel_editor).
            // Import components into custom component, expandable button, for
            // control.
            ((userInterface.annotationCompare.ExpandButton)jPanel60).setContainerPart(
                    this.jPanel_comparator_container,
                    jButton_spaneditor_delete,
                    jSeparator2,
                    //jLabel8,
                    jToolBar_editopanel_comparison,
                    // list for displaying selected annotations in editor panel
                    jList_selectedAnnotations,
                    this,
                    textPaneforClinicalNotes
                 );


            // After all component(no null) have been imported into this expandable
            // button set default status and gui for this custom component of
            // expandable button.
            ((userInterface.annotationCompare.ExpandButton)jPanel60).defaultStatus();



            // Bind a keystroke to the button for act "save".
            jButton_save.setMnemonic(KeyEvent.VK_S);


            nlp.postProcess.ExtractToDocumentViewer.setGUI(this);

            // hide component for Verifier 2010
            jPanel87.setVisible(false);
            verifierOnCurrent.setVisible(false);
            verifierOnAll.setVisible(false);
            jButton18.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
            jButton19.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );

            TreeModel tmodel = jTree_class.getModel();
            if(tmodel!=null){
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tmodel.getRoot();
                rootNode.removeAllChildren();
                jTree_class.setModel(tmodel);
            }

            hideStatusButtons();
            
            // hide the hotkey icon
            jLabel22.setVisible( false );
            
            // This is to hide buttons and notes for the verifyer function 
            // on the seonc
            jPanel86.setVisible( false );

            log.LoggingToFile.log(Level.INFO, "eHOST version: " + this.getTitle() );

        }catch(Exception ex){
            // MUSTMUST add error proceesing codes
        }

        

    }


    
    /**
     * This method will change an annotation to a suggested annotation
     * @param toChange - the annotation to change
     * @param theRightOne - the text to match
     */
    public void changeAnnotationToSuggestion(Annotation toChange, suggestion theRightOne)
    {
/*DISDISDIS
  
        boolean refreshScreen = false;
        //Used to send to span editor.
        int delete = 0, headdecrease = 1, tailincrease = 2,
                headincrease = 3, taildecrease = 4;
        if (theRightOne != null)
        {
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
            if(spanobj==null)
                return;
            Annotation annotation = spanobj.getAnnotation();
            SpanDef span = spanobj.getSpan();
            //Extract the difference between the start spans and end spans of the suggested and current.
                int headChange = toChange.spanstart - theRightOne.getStart();
                int endChange = toChange.spanend - theRightOne.getEnd();
                //If the original head is greater than the suggested then shift left.
                if(headChange > 0)
                {
                    //Keep shifting until we have shifted to the suggested position.
                    for (int shifts = 0; shifts < headChange ; shifts++)
                    {
                        resultEditor.spanEdit.SpanEditor spanEditor =
                                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
                        if (spanEditor.editCurrentDisplayedSpan(headdecrease) > 0)
                        {
                            refreshScreen = true;
                        }
                    }
                }
                //If the original head is less than the suggested head then shift right.
                else if(headChange < 0)
                {
                    //Keep shifting unil we have reached the suggested position.
                    for (int shifts = 0; shifts < Math.abs(headChange) ; shifts++)
                    {
                        resultEditor.spanEdit.SpanEditor spanEditor =
                                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
                        if (spanEditor.editCurrentDisplayedSpan(headincrease) > 0)
                        {
                            refreshScreen = true;
                        }
                    }
                }
                //If the original end is greater than the suggested end, shift left.
                if(endChange > 0)
                {
                    //Shift until we reach the suggested position.
                    for (int shifts = 0; shifts < endChange ; shifts++)
                    {
                        resultEditor.spanEdit.SpanEditor spanEditor =
                                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
                        if (spanEditor.editCurrentDisplayedSpan(taildecrease) > 0)
                        {
                            refreshScreen = true;
                        }
                    }
                }
                //If the original end is less than the suggested end, shift right.
                else if(endChange<0)
                {
                    //Keep shifting until we reach the suggested position.
                    for (int i = 0; i < Math.abs(endChange) ; i++)
                    {
                        resultEditor.spanEdit.SpanEditor spanEditor =
                                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
                        if (spanEditor.editCurrentDisplayedSpan(tailincrease) > 0)
                        {
                            refreshScreen = true;
                        }
                    }
                }
        }
        if (refreshScreen)
        {
            //Get the new verifier result for this annotation.
            checkOneVerifier(WorkSet.indexOfCurrentAnnotation);

            disableAnnotationDisplay();
            this.repaintHighlighter();
            showSelectedAnnotations_inList(env.Parameters.latestSelectedInListOfMultipleAnnotions);

        }
        * */
        
    }
    public enum spanedittype{
        headExtendtoLeft, headShortentoRight,tailExtendtoRight,
        tailshortentoLeft, delete
    };



    /**Modify a specific span. */
    public void spanEdit(spanedittype _type)
    {
        // remember which span is selected in the span list
        int selectedSpanIndex = this.jList_Spans.getSelectedIndex();
        if(( _type != spanedittype.delete )&&( selectedSpanIndex < 0 ))
            return;
        
        
        
        
        setFlag_allowToAddSpan(false); // cancel possible operation of adding new span
        this.setModified();
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
        if(( spanobj == null )&&( _type == spanedittype.delete )) {
            annotation = WorkSet.currentAnnotation;
            span = annotation.spanset.getSpanAt(0);
        }else{
            annotation = spanobj.getAnnotation();
            span = spanobj.getSpan();
        }
        

        // get current file name
        resultEditor.spanEdit.SpanEditor spanEditor  =
                new resultEditor.spanEdit.SpanEditor(annotation, span, textPaneforClinicalNotes);
        
        // define indicators
        int delete = 0, headdecrease = 1, tailincrease = 2,
                headincrease = 3, taildecrease =4;

        switch(_type){

            case delete:
                if(spanEditor.editCurrentDisplayedSpan( delete)>0){
                    //refreshScreen = true;
                    disable_AnnotationEditButtons();
                }
                break;
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
                if(spanEditor.editCurrentDisplayedSpan(taildecrease)>0){
                    //refreshScreen = true;
                }
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
                    updateEditorScreen_afterSpanModified(selectedIndex, -1);
                } else {
                    // disable buttons if just deleted an annotations by the button
                    jButton_SelectClass.setEnabled(false);
                    showAnnotationCategoriesInTreeView_refresh();
                    showValidPositionIndicators();
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

            Vector<Integer> selectedAnnotation = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            int uniqueindex = -1;
            if((selectedAnnotation!=null)&&(selectedAnnotation.size()>0))
                uniqueindex = Depot.SelectedAnnotationSet.getSelectedAnnotationSet().get(0);
            
            
            Depot.SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = uniqueindex;
            display_RelationshipPath_Remove();
            // remove_all_underline_highlighter();
            
            
            
            // recheck difference and display on screen
            ((userInterface.annotationCompare.ExpandButton) jPanel60).recheckUnMatches();
            display_RelationshipPath_Remove();
            remove_all_underline_highlighter();
            
            // relist current annotation
            display_showOneAnnotation_inListOnEditorPanel(uniqueindex);  
            // reprint annotations on document viewer
            display_repaintHighlighter();
            // update the navigation trees
            showAnnotationCategoriesInTreeView_refresh();
            //display_showAnnotations_inComparatorList();

            // update position indicators
            showValidPositionIndicators();
            jList_Spans.setSelectedIndex(selectedSpanIndex);
            
            ((userInterface.annotationCompare.ExpandButton) jPanel60).cr_recheckDifference();
        }

    }
    
    /**refresh after modification made on annotation among several selected annotations*/
    public void refresh(){
        refresh(null);
    }
    
    /**Refresh after span changed are made on an annotation. 
     */
    private void refresh(spanedittype is_an_span_deleted) {
        // to current modifying annotation, get the its idnex in the annotation list
        int selectedAnnotationIndex = jList_selectedAnnotations.getSelectedIndex();
        // to current modifying annotation, get the its index in te span list
        int selectedSpanIndex = jList_Spans.getSelectedIndex();
        this.display_repaintHighlighter();

        disableAnnotationDisplay();
        
        if ((is_an_span_deleted == null) || (is_an_span_deleted != spanedittype.delete)) {
            
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
        else if ((Depot.SelectedAnnotationSet.getSelectedAnnotationSet() != null)
                && (Depot.SelectedAnnotationSet.getSelectedAnnotationSet().size() > 0)) 
        {
            showSelectedAnnotations_inList(0);
            this.enable_AnnotationEditButtons();
        }
    }

    /**This method can help you go back or refresh the result editor. */
    public void refreshResultEditor()
    {
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        

        // latest cursor postion
        // int latestCarePostion = textPaneforClinicalNotes.getCaretPosition();
        // jToggleButton1.setSelected(false);
        File current_raw_document = resultEditor.workSpace.WorkSet.getCurrentFile();
        if(current_raw_document==null)
            showFileContextInTextPane( 0, 0 );
        else


            showFileContextInTextPane( current_raw_document, 0 );
        disableAnnotationDisplay();
        //textPaneforClinicalNotes.setCaretPosition(latestCarePostion);
        showSelectedAnnotations_inList( env.Parameters.latestSelectedInListOfMultipleAnnotions );
        enable_AnnotationEditButtons();

        /*backtoPreviousFocusPoint();*/

    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Misc.">
    /**save function: save annotaitons from memory to their original xml files.
     * The difference between "save" and "save as" is that user are allowed to
     * save annotations belongs to designated files and assigned directionary.
     */
    private void saveto_originalxml(){
        // save annotations to XML
        resultEditor.save.Save saver = new resultEditor.save.Save(this);
        String return_msg = saver.quickXMLSaving();

        // save annotations into excel file        
        io.excel.AnnotationExcelIO aei = new io.excel.AnnotationExcelIO();
        aei.save();
        
        
        // show return msg on buttom bar
        this.showText_OnBottomInfoBar("<html>" + return_msg + " <font color=blue><b>" + env.Parameters.WorkSpace.CurrentProject.getAbsolutePath() + "</b></font></html>");
    }
    private void setDocumentFontSize(int fontsize){

        // 1 change font size in current text panel
        resultEditor.display.Screen screen = new resultEditor.display.Screen(this.textPaneforClinicalNotes);
        screen.changeDisplayFontSize(fontsize);

        // 2 keep parameter of font size into mono text display
        //ResultEditor.Display.Screen.setFontSize(fontsize);

        // 3 keep parameter of font size to background highlight
    }



    public void display_showCategories(){
        if( jRadioButton_treeview_currentarticle.isSelected() ){
            env.Parameters.working_on_file = true;
            showAnnotationCategoriesInTreeView_CurrentArticle( );
        } else if( this.jRadioButton_treeview_overall.isSelected() ){
            env.Parameters.working_on_file = false;
            showAnnotationCategoriesInTreeView_All();
        }
    }
    /**call class to start process of saving annotations and its attributes into xml or pins files.*/
    private void saveAs(){
        resultEditor.save.Save saver = new resultEditor.save.Save(this);
        saver.savechanges();
    }


    /**
     * This method will change the selected annotation to whichever suggestion is currently
     * selected in the suggestions jList.
     */
    private void changeSelectedToSuggestion()
    {
        //int latestScrollBarValue = jScrollPane_textpane.getVerticalScrollBar().getValue();
        resultEditor.annotations.Annotation toChange = WorkSet.currentAnnotation;
        suggestion theRightOne = (suggestion) jList3.getSelectedValue();
        changeAnnotationToSuggestion(toChange, theRightOne);

    }


    /**
     * This method will check a single annotation for potential problems using the
     * verifier.
     * @param originalArrayIndex - The Array Index of the annotation in the static storage space.
     * @return - any potential problems, or null if there are none.
     */
    private void checkOneVerifier(int originalArrayIndex)
    {
    /*
        verifier.SpanCheck spanCheck = new verifier.SpanCheck(
                env.clinicalNoteList.CorpusLib.getAbsoluteFileName(jComboBox_InputFileList.getSelectedIndex()), env.Parameters.VERIFIER_DICTIONARIES);
        spanCheck.findProblems(originalArrayIndex);
     *
     */

    }
    public void launch_dialog_of_configuration()
    {

        // get coordates and size of current window
        int parentX, parentY, parentWidth, parentHeight;
        parentX = this.getX();
        parentY = this.getY();
        parentHeight = this.getHeight();
        parentWidth = this.getWidth();
        
        // launch the dialog of configuration
        Setting jframe_configure = new Setting(parentX, parentY, parentWidth, parentHeight, this);        
        jframe_configure.setVisible(true);
        //jframe_configure.toFront();
    }

    /**This function could be call to show anntotation details while you select
     * one annotation in the annotation list on the document viewer gui.
     *
     * @param   selectedIndex_inList
     *          the index of select items in the jlist of selected annotations
     */
    private void showDetailsByIndex(int selectedIndex_inList){


        // do nothing,
        // if there is no entry show in the list of multiple annotations
        if( jList_selectedAnnotations.getModel().getSize() < 1 )
            return;

        try{
            // filename of current text source
            String currentTextSource = resultEditor.workSpace.WorkSet.getCurrentFile().getName();


            // get indexes of selected annotations in the article in the depot
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            Vector<Integer> selectedannotations = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();
            if (selectedannotations==null)
                return;
            if (selectedannotations.size() < 1)
                return;


            resultEditor.annotations.Annotation annotation = depot.getAnnotationByUnique( currentTextSource, selectedannotations.get( selectedIndex_inList) );
            // record to indicate current annotation and its index in the artile in the depot
            resultEditor.workSpace.WorkSet.currentAnnotation = annotation;
            resultEditor.workSpace.WorkSet.indexOfCurrentAnnotation = selectedannotations.get( selectedIndex_inList );

            // put information on screen
            this.listSpans(jList_Spans, annotation);
            
            jTextField_annotationClassnames.setText( annotation.annotationclass );
            showAnnotationCategoriesInTreeView_selectCategory( annotation.annotationclass, annotation.annotationText );

            // echo for debug
            // System.out.println("User clicked on annotation: Adjudication=" + annotation.adjudicationStatus + " id="+annotation.uniqueIndex);
            
            jTextArea_comment.setText( annotation.comments );

            jTextField_annotator.setText(null);

            if ((annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK)
                    && ( GUI.reviewmode == GUI.ReviewMode.adjudicationMode)) {
                jTextField_annotator.setText("ADJUDICATION");
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else if ((annotation.getFullAnnotator() != null) && (annotation.getFullAnnotator().trim().length() > 0)) {
                jTextField_annotator.setText(annotation.getFullAnnotator());
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else {
                setRedBorder(jTextField_annotator);
            }

            if ((annotation.creationDate != null) && (annotation.creationDate.trim().length() > 0)) {
                jTextField_creationdate.setText(annotation.creationDate);
                jTextField_creationdate.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else {
                setRedBorder(jTextField_creationdate);
            }

            //MUSTMUST111017setVerifierSuggestion( annotation.verifierSuggestion );

            display_editor_ListRelationships( annotation );

            display_markPhrase( annotation);


            display_setCarpetOn( annotation.spanset.getMinimumStart() );

        }catch(Exception e){
            log.LoggingToFile.log( Level.SEVERE, "GUI refresh error - loading result " +
                    "editor - 20311 ! \n"+e.toString() );
            logs.ShowLogs.printWarningLog("GUI refresh error - loading result " +
                    "editor - 20311 ! \n"+e.toString());
        }


    }

    /**This function could be call to show anntotation details while you select
     * one annotation in the annotation list on the document viewer gui.
     *
     * @param   _annotation
     *          a given annotation, and we want to show
     *          
     */
    private void showDetails(Annotation _annotation){


        // if we get an null annotation, we just clean old displaying.

        try{
                        
            // put information on screen
            this.listSpans( jList_Spans, _annotation );

            // display the
            jTextField_annotationClassnames.setText( _annotation.annotationclass );

            // highlight this annotation in the tree view
            showAnnotationCategoriesInTreeView_selectCategory( 
                    _annotation.annotationclass,
                    _annotation.annotationText );

            // echo for debug
            // System.out.println("User clicked on annotation: Adjudication=" + annotation.adjudicationStatus + " id="+annotation.uniqueIndex);

            jTextArea_comment.setText( _annotation.comments );

            jTextField_annotator.setText(null);

            if ((_annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK)
                    && ( GUI.reviewmode == GUI.ReviewMode.adjudicationMode)) {
                jTextField_annotator.setText("ADJUDICATION");
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else if ((_annotation.getFullAnnotator() != null) && (_annotation.getFullAnnotator().trim().length() > 0)) {
                jTextField_annotator.setText(_annotation.getFullAnnotator());
                jTextField_annotator.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else {
                setRedBorder(jTextField_annotator);
            }

            if ((_annotation.creationDate != null) && (_annotation.creationDate.trim().length() > 0)) {
                jTextField_creationdate.setText(_annotation.creationDate);
                jTextField_creationdate.setBorder(STANDARD_TEXTFIELD_BOARD);
            } else {
                setRedBorder(jTextField_creationdate);
            }

            //MUSTMUST111017setVerifierSuggestion( annotation.verifierSuggestion );

            display_editor_ListRelationships( _annotation );
            display_relationshipPath_setPath( _annotation );

//            hilightPhrase( _annotation);

//            display_setCarpetOn( _annotation.spanset.getMinimumStart() );

        }catch(Exception e){
            log.LoggingToFile.log( Level.SEVERE, "GUI refresh error - loading result " +
                    "editor - 20311 ! \n"+e.toString() );
            logs.ShowLogs.printWarningLog("GUI refresh error - loading result " +
                    "editor - 20311 ! \n"+e.toString());
        }


    }

    /**
     * Call this when a mouse click occurs on the Jlist. Invert 'selected' variable
     * for the selected iListEntry.
     * @param thislist - the list that had a mouse click.
     */
    private void mouseClicked_onList(JList thislist) {


            ListEntryofRelationship thisterm = (ListEntryofRelationship)thislist.getSelectedValue();
            if(thisterm == null)
                return;
            int size = thislist.getModel().getSize();
            for(int i = 0; i< size; i++)
            {
                ListEntryofRelationship otherTerm = (ListEntryofRelationship)thislist.getModel().getElementAt(i);
                otherTerm.setSelected(false);
            }
            thisterm.setSelected(true);

    }
    /**load resources from files, such as pictures, icons, etc.*/
    private void loadRes(){
        try{
            icon_oracle_disabled = new javax.swing.ImageIcon(getClass().getResource("/res/oracle_icon_s_disabled.png"));
            icon_oracle_enabled = new javax.swing.ImageIcon(getClass().getResource("/res/oracle_icon_s_enabled.png"));
            icon_graphicpath_enabled = new javax.swing.ImageIcon(getClass().getResource("/res/object-to-path_enabled.png"));
            icon_graphicpath_disabled = new javax.swing.ImageIcon(getClass().getResource("/res/object-to-path_disabled.png"));
            icon_difference_enabled = new javax.swing.ImageIcon(getClass().getResource("/res/path-difference.png"));
            icon_difference_disabled = new javax.swing.ImageIcon(getClass().getResource("/res/path-difference_disabled.png"));
            icon_attribute_disabled = new javax.swing.ImageIcon(getClass().getResource("/res/attribute_disabled.png"));
            icon_attribute_enabled = new javax.swing.ImageIcon(getClass().getResource("/res/attribute_enabled.png"));
            icon_note = new javax.swing.ImageIcon(getClass().getResource("/res/note.jpeg"));
            icon_note2 = new javax.swing.ImageIcon(getClass().getResource("/res/note2.jpeg"));
            icon_span = new javax.swing.ImageIcon(getClass().getResource("/res/span.jpeg"));
            icon_spanaddingOn = new javax.swing.ImageIcon(getClass().getResource("/res/span-add-working.png"));
            icon_spanaddingOff = new javax.swing.ImageIcon(getClass().getResource("/res/span-add.png"));

        }catch(Exception ex){
        }
    }

    /** Clean the table and reload file list into the table.*/
    /*private void cleanAndReload_ListOfFileCollection() {
        // empty the table for list of selected clinical notes
        Vector<String> listdata = new Vector<String>();
        jList_corpus.setListData(listdata);

        // show clinical note textsourceFilename and amount of words in rows
        int size = env.Parameters.LIST_ClinicalNotes.size();
        for (int i = 0; i < size; i++) {
            // buld row
            listdata = env.Parameters.LIST_ClinicalNotes.get(i).filename,
                (Object)env.Parameters.LIST_ClinicalNotes.get(i).amountOfWords
            };
            // add row data into table
            ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).addRow(row);

        }
        ((DefaultTableModel) jTable_SelectedClinicalFiles.getModel()).fireTableDataChanged();


        // show information on GUI: amount of selected clinical notes
        jLabel_filecollection.setText(" FILE COLLECTION ("+size+")");

    }*/
    
    private void smallerFontSize(){
        
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span
        
        if( this.currentFontSize <= 6 ) {
            commons.Tools.beep();
            return;
        }else{
            this.currentFontSize = this.currentFontSize - 1;
            setDocumentFontSize( this.currentFontSize );
        }
    }

    public void setFlag_allowToAddSpan( boolean allowed ){
        this.WANT_NEW_SPAN = allowed; // cancel possible operation of adding new span
        if(allowed)
            jButton_SpanAdd.setIcon( icon_spanaddingOn );
        else
            jButton_SpanAdd.setIcon( icon_spanaddingOff );
    }
    
    private void biggerFontSize(){
        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span
        if( this.currentFontSize >= 50 ) {
            commons.Tools.beep();
            return;
        }else{
            this.currentFontSize = this.currentFontSize + 1;
            setDocumentFontSize( this.currentFontSize );
        }
    }
    /**assign the postion of this dialog.*/
    private void setDialogPosition(){
        // set the perfect size of this window
        int perfectWidth = 1080;
        int perfectHeight = 768;
        this.setSize(perfectWidth, perfectHeight);

        /*
        // **** setting the location of gui window
        Dimension winSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = winSize.width;
        int screenHeight = winSize.height;
        // if screen have enough space for this window, show it within its prefect size
        // otherwise, use the maxsize of the display device
        if ((screenWidth >= perfectWidth) && (screenHeight >= perfectHeight)) {
            int x = (int) (screenWidth - perfectWidth) / 2;
            int y = (int) (screenHeight - perfectHeight) / 2;
            this.setSize(perfectWidth, perfectHeight);
            this.setLocation(x, y);
        } else {
            this.setSize(screenWidth, screenHeight);
            this.setLocation(0, 0);
        }*/
        this.setLocationRelativeTo(null);
    }
    /**
     * Save changes of annotator information to current annotation while
     * component of textfield which used to show annotator information lost
     * its focus.
     */
    private void saveChanges_on_Annotator(){
        String newAnnotator = jTextField_annotator.getText();

        // get current annotation
        resultEditor.annotations.Annotation annotation = null;
        try{
            annotation = resultEditor.workSpace.WorkSet.currentAnnotation;
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231634 : fail to get current while try to save changes of annotator!");
        }

        // validity check
        if (( annotation == null )|| (!(annotation instanceof resultEditor.annotations.Annotation )) ){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231636 : fail to get current while try to save changes of annotator!");
            return;
        }

        // save changes of annotator to current annotation
        try{
            annotation.setAnnotator( newAnnotator );
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231637 : fail to to save changes of annotator!");
            return;
        }

    }

    /**
     * Save changes of comment to current annotation while
     * component of textfield which used to show annotator information lost
     * its focus.
     */
    private void saveChanges_on_Comments(){
        String newComment = jTextArea_comment.getText();

        // get current annotation
        resultEditor.annotations.Annotation annotation = null;
        try{
            annotation = resultEditor.workSpace.WorkSet.currentAnnotation;
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231710 : fail to get current while try to save changes of comments!");
        }

        // validity check
        if (( annotation == null )|| (!(annotation instanceof resultEditor.annotations.Annotation )) ){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231711 : fail to get current while try to save changes of comments!");
            return;
        }

        // save changes of annotator to current annotation
        try{
            annotation.comments = newComment;
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"Error 1009231712 : fail to to save changes of comment!");
            return;
        }

    }


    /** Set hand cursor to these java visilable components */
    private void setHandCursor(){
        jButton13.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton14.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton21.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton22.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_importAnnotations.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_removeAllAnnotations.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton20.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_save.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_removeduplicates.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );

        //Set Annotation Information tab to hand cursors.
        annotations.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        annotators.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        overlapping.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        workingConflicts.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        verifierFlagged.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );

        jButton_spaneditor_delete.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_spaneditor_lefttToLeft.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_spaneditor_leftToRight.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_span_rightToLeft.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        //jButton11.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_SelectClass.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        //jButton11.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_relationships.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton_attribute.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );
        jButton23.setCursor( Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) );

        jLabel_info_annotator.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel_info_annotatorlabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jTextArea_comment.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel_infobar_FlagOfOracle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel_infobar_attributeEditor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //jLabel_infobar_FlagOfPath.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel_infobar_FlagOfDiff.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel22.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    }
    /**add drop event handing process to list of text source, whose type is JTable.*/
    private void dropEventOfTextSourceList(){
        //jTable_SelectedClinicalFiles.setDropMode(DropMode.USE_SELECTION);
       // jTable_SelectedClinicalFiles.setDragEnabled(false);
        //jTable_SelectedClinicalFiles.setDropTarget(null);
    }
    /**pop up a dialog to let u change current annotator.*/
    private void changeAnnotator(){
        try{
            resultEditor.annotator.ChangeAnnotator changeannotator = new
                    resultEditor.annotator.ChangeAnnotator(this, null);
            changeannotator.setVisible(true);

        }catch(Exception ex){

        }
    }

    /**
     * Display: remove all graph path from text panel. These graph paths were
     * used to show links between annotations who have complex relationships.
     */
    public void display_RelationshipPath_Remove()
    {
        if(!jCardcontainer_interactive.isVisible())
            return;
        
        try{
            ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).
                    clearComplexRelationshipCoordinates();
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"\n==== ERROR ====::1106101301::"+ex.toString());
        }

    }
    
    public void display_removeSymbolIndicators(){
        ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).clearAttRels();
    }
    
    public void display_showSymbolIndicators(){
        try{
        
        ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).clearAttRels();
        
        File currentTextSource = resultEditor.workSpace.WorkSet.getCurrentFile();
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        
        resultEditor.annotations.Article article = null;
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
            article = adjudication.data.AdjudicationDepot.getArticleByFilename( currentTextSource.getName() );
        else    
            article = depot.getArticleByFilename( currentTextSource.getName() );
        
        if ( article == null )
            return;
        boolean isCarePositionSet = false;
        for( resultEditor.annotations.Annotation annotation : article.annotations ){
            if ( annotation == null )
                continue;
            
            if( annotation.visible == false )
                continue;
            
            if( this.reviewmode == GUI.ReviewMode.adjudicationMode ){
                if( annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_DLETED )
                    continue;
                if( annotation.adjudicationStatus == Annotation.AdjudicationStatus.NONMATCHES_DLETED )
                    continue;
            }
                
            if( annotation.hasAttribute() )
                ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).addAttAnnotation(annotation);
            if( annotation.hasRelationship() )
                ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).addRelAnnotation(annotation);
            
        }
        
        ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).repaint();
        }catch(Exception ex){
        }
    }


    /** show graph path for complex relationships if have.
     *
     * @param   annotation
     *          The annotation which we will draw out graph path of its complex
     *          relationships if it has.
     */
    private void display_relationshipPath_setPath( resultEditor.annotations.Annotation annotation ){

        // The annotation at the start point can NOT be null or w/o relationships
        if((annotation==null)||(annotation.relationships == null))
            return; 
        
        // The annotation at the start point must have visible spans
        if((annotation.spanset == null)||(annotation.spanset.size()<1))
            return;  

        try{
            
            // preparation
            Depot depot = new Depot();
            if( depot == null )
                return;
            int count=0;
            
            // get the class color of the annotation at the start point
            Color source_color, linked_color;
            source_color = resultEditor.annotationClasses.Depot.getColor(annotation.annotationclass.trim());

            // pull out all relationships of the given annotation 
            for(AnnotationRelationship complex: annotation.relationships) {
                for(resultEditor.annotations.AnnotationRelationshipDef end: complex.getLinkedAnnotations()) {
                    
                    if(end==null)
                        continue;
                    
                    // get the linked annotation
                    Annotation linkedAnnotation = null;
                    
                    if( reviewmode == ReviewMode.adjudicationMode ){
                        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                        linkedAnnotation = depotOfAdj.getAnnotationByUnique(
                            WorkSet.getCurrentFile().getName(),
                            end.linkedAnnotationIndex
                        );
                    }else
                        linkedAnnotation = depot.getAnnotationByUnique(
                            WorkSet.getCurrentFile().getName(),
                            end.linkedAnnotationIndex
                        );
                    
                    // and this linked annotation can not be null
                    if(linkedAnnotation==null)
                        continue;
                    
                    

                    // integer that tell us how many annotation linked to 
                    // the given annotation
                    count++;
                    
                    // if in the adjudication mode, the linked annotation maybe
                    // has adjudicated to another annotation, and we need to 
                    // use the adjudicated annotation as the linked annotation.
                    if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                        linkedAnnotation = getlinkedAdjudicatedAnnotation( linkedAnnotation );
                    }
                    
                    if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                        if( linkedAnnotation.adjudicationStatus == Annotation.AdjudicationStatus.NONMATCHES_DLETED )
                            continue;
                    }

                    // get the spans of the object annotation, which we want to 
                    // create relationship between it and the given annotation 
                    // "_annotation" 
                    SpanSetDef objectiveSpans = linkedAnnotation.spanset;
                    if(objectiveSpans == null )
                        continue;
                        
                    // the color of class of the object annotation
                    linked_color = resultEditor.annotationClasses.Depot.getColor(linkedAnnotation.annotationclass.trim());

                    
                    
                    // set the source annotation and the first linked annotation 
                    if( count==1 ){                        
                        
                        ((userInterface.txtScreen.TextScreen)
                            this.textPaneforClinicalNotes).
                                setComplexRelationsCoordinates(
                                    annotation.spanset, // spanset of the given annotation
                                    source_color,
                                    objectiveSpans,     // spanset of the linked annotation
                                    linked_color
                                );
                    }else{
                        ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).setComplexRelationsCoordinates_extraEnds(
                              objectiveSpans, // spanset of the linked annotation
                              linked_color
                                );
                        //System.out.println("extra x= "+ x +", y = " + y);
                    }

                }
            }
            Graphics g1=((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).getGraphics();
            ((userInterface.txtScreen.TextScreen)this.textPaneforClinicalNotes).paint(g1);

        }catch(Exception ex){
            //ex.printStackTrace();
             log.LoggingToFile.log( Level.SEVERE,"error 1010132130:: fail to set graph path for complex relationship of current annotation!!!");
        }

    }
    
    
    private Annotation getlinkedAdjudicatedAnnotation(Annotation annotation) {
        
         //String textOfLinkedAnnotation = null;
         try {
             if( annotation == null )
                 return null;
             
                     
             //resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
             adjudication.data.AdjudicationDepot depot = new adjudication.data.AdjudicationDepot();
             String path = WorkSet.getCurrentFile().getAbsolutePath();
             int offset = path.lastIndexOf( File.separatorChar );
             if( offset <1 )
                 return annotation;
             
             String filename = path.substring( offset + 1 , path.length() );
             // System.out.println("\n\n\n\n" + filename );

             if ((annotation.adjudicationAlias != null) && (!SugarSeeder.isAdjudicationAlias(
                     annotation.adjudicationAlias))) {
                 
                 String index = annotation.adjudicationAlias;
                 Annotation ann2 = depot.getAnnotationByUnique(filename, Integer.valueOf(index));
                 return ann2;
             }else{
                 return annotation;
             }
             
                          
             //int uniqueindex = linkedAnnotationIndex;
             //Annotation ann = depot.getAnnotationByUnique(filename, uniqueindex);
             //textOfLinkedAnnotation =  ann.annotationText;
             
             
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return null;
     }
    
    /**
     * Search designated text content from current text in the text area of result
     * editor. And highlight results(found terms) with two black bar on them.
     * Give a beep as warnning if nothing got found.
     */
    private void searchWord(){
        resultEditor.wordSearcher.Searcher wordsearcher =
                new resultEditor.wordSearcher.Searcher( textPaneforClinicalNotes, this );
        wordsearcher.find( jTextField_searchtext.getText() );
    }
    private boolean isValidWorkspace(String path){
        boolean success_inWorkspaceSetting=false;

            if(path==null)
                return false;
            if(path.trim().length()<1)
                return false;

            File f = new File(path);
            if(!f.exists())
                return false;
            if(f.isFile())
                return false;

            return true;
    }

    /**check and set workspace setting*/
    private void setWorkSpace()
    {
        String workspace_path = env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath;
        if (( workspace_path == null) || (workspace_path.trim().length()<1))
        {
            this.jCardcontainer_interactive.setVisible(false);
            this.setEnabled(false);

             workSpace.SetWorkSpace setWS = new  workSpace.SetWorkSpace(this);
            setWS.setVisible(true);
            setWS.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        }
        else {
            try {
                File path = new File(env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath);
                if (!path.exists()) {
                    this.jCardcontainer_interactive.setVisible(false);
                    this.setEnabled(false);

                    workSpace.SetWorkSpace setWS = new workSpace.SetWorkSpace(this);
                    setWS.setVisible(true);
                    setWS.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
                }
            } catch (Exception ex) {
                this.jCardcontainer_interactive.setVisible(false);
                this.setEnabled(false);

                workSpace.SetWorkSpace setWS = new workSpace.SetWorkSpace(this);
                setWS.setVisible(true);
                setWS.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
            }

            //this.jCardcontainer_interactive.setVisible(true);
            log.LoggingToFile.log(Level.CONFIG, "current workspace = ["
                    + env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath
                    + "]");

        }


    }
    
    /**
     * Redraw the highlighting and only draw the passed in annotations
     * @param currentlyViewing - the annotations to highlight.
     */
    private void repaintNewAnnotations(Vector<Annotation> currentlyViewing)
    {
        Depot depot = new Depot();
        Article article = depot.getArticleByFilename(WorkSet.getCurrentFile().getName());
        depot.setAnnotationsVisible(currentlyViewing, article);

        //repaint
        jpositionIndicator.removeAllIndicators();
        jpositionIndicator.paintArticle(WorkSet.getCurrentFile().getName().trim());
        jpositionIndicator.forcepaint();
        jpositionIndicator.repaint();

        //go to file viewer to view newly painted annotations
        int selected = jComboBox_InputFileList.getSelectedIndex();
        showFileContextInTextPane(selected, 0);
        jTabbedPane3.setSelectedIndex(0);
    }
    /**
     * Move the focus from the selected annotation to the annotation that is after the selected annotation.
     * @param next - true if you're looking for the next annotation, false if you're looking for the previous annotation
     */
    private void moveToNextAnnotation(boolean next)
    {
        resultEditor.workSpace.WorkSet.latestScrollBarValue = jScrollPane_textpane.getVerticalScrollBar().getValue();

        //Get the current article so we can search for the 'next' annotation
        Depot depot = new Depot();
        Article article = depot.getArticleByFilename(WorkSet.getCurrentFile().getName());

        //Get the current annotation so we can use it to find the next annotation
        Annotation current= WorkSet.currentAnnotation;

        //This will store the current minimum distance between two annotations
        int min = Integer.MAX_VALUE;

        //This will store our current minimum distance annotation
        Annotation currentNext = null;

        //Loop through all annotations to find a minimum distance
        for(Annotation possibleNext: article.annotations)
        {
            if((possibleNext==null)||(possibleNext.spanset.isEmpty()))
                continue;
            
            SpanDef span = possibleNext.spanset.getSpanAt(0);
            if( span == null )
                continue;
            if( (span.start < 0 ) || (span.end < 0) || (span.end <= span.start) )
                continue;
            
            //If the this annotation is closer to the selected annotation then any found so far, and it is not before
            //then set this annotation to be the closes annotation
            if( next 
                    && ((span.start - current.spanset.getSpanAt(0).start) < min) 
                    && ((span.start - current.spanset.getSpanAt(0).start) > 0  ))
            {
                min = span.start- current.spanset.getSpanAt(0).start;
                currentNext = possibleNext;
            }
            //If we're looking for the previous annotation then just do the opposite comparison..
            if( !next 
                    && ( (current.spanset.getSpanAt(0).start - span.start) < min) 
                    && ( (current.spanset.getSpanAt(0).start - span.start) > 0) )
            {
                min = current.spanset.getSpanAt(0).start - span.start;
                currentNext = possibleNext;
            }
        }
        //If we found an annotation after the selected one, then display info for it
        //and jump to it.
        if(currentNext != null)
        {
            displayInfoForAnnotation(new Object[]{currentNext});
            display_setCarpetOn(currentNext.spanset.getMinimumStart());
        }
    }
    
    public final static int ENABLED = 411;
    public final static int HIDEN = 412;
    public final static int DISABLED = 413;

    public void display_adjudication_setOperationBarStatus(int status) {
        switch (status) {
            case ENABLED: {
                jToolBar6.setVisible( true );
                jLabel23.setText( " - / - ");
                jLabel23.setEnabled( true );
                
                adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter( this.jLabel23, WorkSet.getCurrentFile().getName(), this  );
                diffcounter.reset();
            }
                break;
            case HIDEN: {
                jToolBar6.setVisible( false );
            }
                break;
            case DISABLED: {
                jToolBar6.setVisible( true );
                jLabel23.setText( " - / - ");
                jLabel23.setEnabled( false );
            }
                break;
        }
    }
    
    
    /**
     * Display the info for the annotations
     * @param annotations - the annotations to display info for.
     */
    private void displayInfoForAnnotation(Object[] annotations)
    {
        Vector<Annotation> toSet = new Vector<Annotation>();
        for(Object annotation: annotations)
        {
            toSet.add((Annotation)annotation);
        }

        Depot depot = new Depot();
        Depot.SelectedAnnotationSet.setSets(depot.getUniqueIndexByAnnotation(toSet));
        showSelectedAnnotations_inList(0);

    }
    /**
     * Set the list of info to annotations with the selected class. This should
     * use a class using the annotation information format.
     * ***Does not support classes with spaces*****
     *
     * @param selectedClass - the selected class.
     */
    private void selectAClass(String selectedClass)
    {
        //To store the list of annotations with the selected class
        Vector<Annotation> toDisplay = new Vector<Annotation>();

        //Strip out the actual class name.
        selectedClass = selectedClass.split(" ")[2];

        //Loop through all annotations to find those with matching classes.
        for(Annotation annotation: annotationsList)
        {
            if(annotation.annotationclass.equals(selectedClass))
                toDisplay.add(annotation);
        }

        //Alphabetize and display
        Collections.sort(toDisplay);
        infoList.setListData(toDisplay);
        jLabel29.setText(selectedClass);
    }

    private void disable_AnnotationEditButtons(){
        // disable buttons for span and class edit
        jButton_SelectClass.setEnabled(false);
        jButton_spaneditor_lefttToLeft.setEnabled(false);
        jButton_spaneditor_leftToRight.setEnabled(false);
        jButton_span_rightToLeft.setEnabled(false);
        jButton_spaneditor_delete.setEnabled(false);
        jButton4_spanEditor_rightToRight.setEnabled(false);
        //jButton11.setEnabled(false);
        jButton_SelectClass.setEnabled(false);
        jButton_relationships.setEnabled(false);        
        delete_Relationships.setEnabled(false);
        jButton_attribute.setEnabled(false);

         Vector v = new Vector();
         jList3.setListData(v);
         jList3.setBorder(null);

    }

    private void setRedBorder(JTextField textfield ){
        javax.swing.border.Border lines = new javax.swing.border.LineBorder(Color.RED, 3);
        textfield.setBorder(lines);
    }

    private void setRedBorder(){
        javax.swing.border.Border lines = new javax.swing.border.LineBorder(Color.RED, 3);
        jList3.setBorder(lines);
    }
    
    // while clicking button to enter gui of interactive editor, do fellowing things:
    private void enter_Interactive() {

        // show all classes in the tree view
        showAnnotationCategoriesInTreeView_refresh();
        setScreen_toInteractive();
    }
    

    private void setScreen_toInteractive()
    {
        

        // not editable
        jComboBox_InputFileList.setEditable(false);

        textPaneforClinicalNotes.setEditable(false);
        textPaneforClinicalNotes.setText(null);



        // add file names to the vector
        int size = env.Parameters.corpus.getSize();
        jComboBox_InputFileList.removeAllItems();
        if (size<1)
        {
            log.LoggingToFile.log( Level.SEVERE, "~~~~ INFO ~~~~: No document can be list in the combolist on document viewer:");
            return;
        }

        try{
        // show inputted file into the combobox
        for (int i = 0; i < size; i++) {
            jComboBox_InputFileList.addItem(env.Parameters.corpus.LIST_ClinicalNotes.get(i).file.getName());
        }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "~~~~ WARNING ~~~~:: fail to refresh the combolist for ");
        }
        jComboBox_InputFileList.updateUI();
        showFileContextInTextPane(jComboBox_InputFileList.getSelectedIndex(), 0);

 

    }

    private void goBack_ChoicedFile() {
        ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    setStatusInvisible();
            ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    noDiff();
            
        int selected = jComboBox_InputFileList.getSelectedIndex();

        if (selected > 0) {
            jComboBox_InputFileList.setSelectedIndex(selected - 1);
            showFileContextInTextPane(selected - 1, 0);

            this.showAnnotationCategoriesInTreeView_CurrentArticle();
            this.showValidPositionIndicators_setAll();
            this.showValidPositionIndicators();
            display_showSymbolIndicators();

        } //else {
          //  Toolkit.getDefaultToolkit().beep();
        //}
        disable_AnnotationEditButtons();

        //currentScreen = infoScreens.NONE;
        refreshInfo();
        
        if( this.reviewmode == GUI.ReviewMode.adjudicationMode ){
            adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
            diffcounter.reset();
        }
    }


    /**This method is to open next document on the document viewer on screen.*/
    private void gotoNextChoicedFile()
    {
        ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    setStatusInvisible();
            ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    noDiff();
            
        //long p1=System.currentTimeMillis(), p2;

        // numbers of items in the filelist
        int size = jComboBox_InputFileList.getItemCount();

        int selected = jComboBox_InputFileList.getSelectedIndex();

        if (selected < (size - 1))
        {            

            jComboBox_InputFileList.setSelectedIndex(selected + 1);
            
               
                showFileContextInTextPane(selected + 1, 0);
                this.showAnnotationCategoriesInTreeView_CurrentArticle();
                this.showValidPositionIndicators_setAll();
                this.showValidPositionIndicators();
                display_showSymbolIndicators();
            
        }// else {
        //    Toolkit.getDefaultToolkit().beep();
        //}

        disable_AnnotationEditButtons();

        currentScreen = infoScreens.NONE;
        refreshInfo();
        
        if( this.reviewmode == GUI.ReviewMode.adjudicationMode ){
            adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
            diffcounter.reset();
        }
    }

    /**get text content of a given text file by lines*/
    private ArrayList loadFileContents(File _rawTextDocument) {
        return commons.Filesys.ReadFileContents(_rawTextDocument);
    }

    /**Load text content from file and show them in textpanel*/
    private void showFileContextInTextPane(int index, int LatestScrollBarValue )
    {
        //long p1=System.currentTimeMillis(), p2;
        try{
            if (index<0)
                index = 0;
            
            // ##1 validity check to "selectedIndex"
            // numbers of items in the filelist
            int size = jComboBox_InputFileList.getItemCount();
            if (index > (size - 1))
                return;

            // ##2 get the file you want to show in text pane.
            // To this index, get matched absolute textsourceFilename
            //File currentTextSource = env.clinicalNoteList.CorpusLib.getFile(index);
            File currentTextSource = env.Parameters.corpus.LIST_ClinicalNotes.get(index).file;

            // ##2.1 record current operatig file into workset
            WorkSet.setCurrentFile(currentTextSource, index);
            // validity check
            if (currentTextSource == null) return;

            // ##3 Load all text content by lines from this file.
            ArrayList<String> contents = loadFileContents(currentTextSource);            

            setDoclength( contents );
            // ##4 SHOW text content on screen and HIGHLIGHT annotations
            resultEditor.display.Screen display = new resultEditor.display.Screen(
                    textPaneforClinicalNotes,
                    currentTextSource // text source
                    );

            display.ShowTextAndBackgroundHighLight( contents );
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1417");
        }
    }

    /**Load text content from file and show them in textpanel*/
    private void showFileContextInTextPane(File _current_text_file, int LatestScrollBarValue )
    {
        if (_current_text_file == null) {
            log.LoggingToFile.log( Level.SEVERE,"error 1103251356:: fail to find file context");
            return;
        }


        // ##2.1 record current operatig file into workset
        WorkSet.setCurrentFile(_current_text_file);
        // validity check


        // ##3 Load all text content by lines from this file.
        ArrayList<String> contents = loadFileContents(_current_text_file);
        setDoclength( contents );

        // ##4 SHOW text content on screen and HIGHLIGHT annotations
        resultEditor.display.Screen display = new resultEditor.display.Screen(
                textPaneforClinicalNotes,
                _current_text_file // text source
                );
        display.ShowTextAndBackgroundHighLight( contents );
    }

    private void setDoclength(ArrayList<String> contents){
        if ( contents == null )
            return;

        int length = 0;
        for( String paragraph : contents ){
            if ( paragraph == null )
                continue;
            length = length + paragraph.length() + 1;
        }

        if ( jpositionIndicator == null )
            return;
        jpositionIndicator.setDocLength(length);
    }
    /** if the indicator is "ON"(false), check and correct span border by
     * space or symbols, o.w. use exact span user just selected
     *
     * @param   tp
     *          object of jtextpane that a span just got selected on.
     */
    private void operation_checkBorder_whileNeeded(JTextPane tp){
         userInterface.correctSpanBorder.SpanChecker checker
                 = new userInterface.correctSpanBorder.SpanChecker(tp);
         checker.checkAndCorrect_ifHave();
    }    

    /** By the user disignated textsourceFilename, show its text contents in text area
     */
    public void goUserDesignated()
    {
        this.display_removeSymbolIndicators();
        // ##1##
        // exit from annotation comparison mode if editor panel and comparator
        // panel are showed for comparing annotation conflits
        try{
            textPaneforClinicalNotes.setText(null);
            ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1012011313:: fail to leave annotations compaison mode!!!");
        }

        resetVerifier();

        try{
            ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    setStatusInvisible();
            ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    noDiff();
            
            // numbers of items in the filelist
            int size = jComboBox_InputFileList.getItemCount();
            if (size < 1)
                    return;
            int fileListSize = env.Parameters.corpus.getSize();

            // use this to avoid the initial loading side effect
            if(size!=fileListSize)
                return;

            int selected = jComboBox_InputFileList.getSelectedIndex();

            if (selected <= (size-1)) {
                resultEditor.workSpace.WorkSet.latestScrollBarValue = 0;
                //jComboBox_InputFileList.setSelectedIndex(selected + 1);
                showFileContextInTextPane(selected, 0);
                this.showAnnotationCategoriesInTreeView_CurrentArticle();
                this.showValidPositionIndicators_setAll();
                this.showValidPositionIndicators();
                
                this.display_showSymbolIndicators();
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1012011314:: fail to switch to another document!!!");
        }

        try{
            // reset details display
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        //currentScreen = infoScreens.NONE;
        refreshInfo();
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1012011312:: fail to update screen after switched to another document!!!");
        }
        
        if( this.reviewmode == GUI.ReviewMode.adjudicationMode ){
            adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
            diffcounter.reset();
        }
    }


    public void showFirstFile_of_corpus()
    {

        textPaneforClinicalNotes.setText(null);
        // ##1##
        // exit from annotation comparison mode if editor panel and comparator
        // panel are showed for comparing annotation conflits
        try{
            ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
            resetVerifier();
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1012011313-2:: fail to leave annotations compaison mode!!!");
        }

        

        try{
            // numbers of items in the filelist
            int size = jComboBox_InputFileList.getItemCount();
            if (size < 1) {
                return;
            }

            resultEditor.workSpace.WorkSet.latestScrollBarValue = 0;
            showFileContextInTextPane(0, 0);

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1012011314:: fail to switch to another document!!!");
        }

        // reset details display
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        //currentScreen = infoScreens.NONE;
        refreshInfo();
    }


    private void goUserDesignatedTable()
    {
        //resetVerifier();
        ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    setStatusInvisible();
            ((userInterface.annotationCompare.ExpandButton)jPanel60).
                    noDiff();
            
        // numbers of items in the filelist
        int size = this.jList_corpus.getModel().getSize();
        if (size < 1)
                return;
        int fileListSize = env.Parameters.corpus.getSize();

        // use this to avoid the initial loading side effect
        if(size!=fileListSize)
            return;

        int selected = this.jList_corpus.getSelectedIndex();

        if (selected <= (size-1)) {
            jComboBox_InputFileList.setSelectedIndex(selected);
            resultEditor.workSpace.WorkSet.latestScrollBarValue = 0;
            //jComboBox_InputFileList.setSelectedIndex(selected + 1);
            showFileContextInTextPane(selected, 0);
            this.showAnnotationCategoriesInTreeView_CurrentArticle();
            this.showValidPositionIndicators_setAll();
            this.showValidPositionIndicators();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }

        // reset details display
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();
        //currentScreen = infoScreens.NONE;
        refreshInfo();
        
        if( this.reviewmode == GUI.ReviewMode.adjudicationMode ){
            adjudication.statusBar.DiffCounter diffcounter = new adjudication.statusBar.DiffCounter(this.jLabel23, WorkSet.getCurrentFile().getName(), this);
            diffcounter.reset();
        }
    }
    
    private void restoreDeleted()
    {
        if(WorkSet.getLastDeleted() != null && WorkSet.getCurrentFile().getName().equals(WorkSet.getLastDeletedFilename()))
        {
            Depot depot = new Depot();
            depot.addANewAnnotation(WorkSet.getLastDeletedFilename(), WorkSet.getLastDeleted());
            WorkSet.restoredLastDeleted();
            this.display_repaintHighlighter();
        }
    }

    
    /**Load report system form into current tab while user click tab button
     * on eHOST. 
     * This method checks whether any children component is in the tab. And if
     * the tab didn't get any children component yet, it will add the report
     * panel into the tab as set then maximum the components.
     *
     * There already has two report table here: 1st is the report that use bars
     * and short color blocks on these bars to indicate the length of documents,
     * and show the distribution of annotations.
     * 
     */
    private void load_ReportSystem(){
        
        // Check the existing status of the parent component.
        // Quit if the parent component is not existing as we 
        // need to add the panel of the report system into the parent component.
        if (jPanel_reportFormContainer == null)
            return;

        // how many children component in the tab (the parent component)
        int comp_count = jPanel_reportFormContainer.getComponentCount();

        // add report panel on this tab if there is no any children component
        // exists
        if( comp_count == 0 )
        {
            
            // create the report panel
            graphicsReport.Manager manager = new graphicsReport.Manager(this);

            try{
                // add this report panel into the tab (parent panel)
                jPanel_reportFormContainer.setLayout(new BorderLayout());
                jPanel_reportFormContainer.add(manager);
                jPanel_reportFormContainer.setVisible(true);

                // refresh the component to make sure the new component got
                // displayed on screen
                jPanel_reportFormContainer.validate();
                jPanel_reportFormContainer.updateUI();
            } catch ( Exception ex ) {
                log.LoggingToFile.log( Level.SEVERE,"~~~~ ERROR ~~~~::1108091211:: error occurred"
                        + " while eHOST try to load and show the report panel"
                        + " on screen!!!"
                        + "\n"
                        + ex.getMessage()
                    );
            }
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Relationships(Simple and Complex)">
    /**
     * Create a relationship between annotations. While status of "relationship"
     * has been enabled and user has chosed start annotation by left click and
     * closed dest annotation by right clicking, this method will create
     * relationship between these two annotations.
     *
     * @param position The position of the right clicking that user just made.
     */
    private void addRelationship(int position) {
        try {
            if (env.Parameters.CreateRelationship.where == 0) {
                return;
            }

            // ---- step 0.0 ----
            // temporary list used to record the annotations in the start point of 
            // the relationship and the annotations in the dest point of this possible
            // relationship.
            ArrayList<Annotation> sourceAnnotations = new ArrayList<Annotation>();
            ArrayList<Annotation> objectAnnotations = new ArrayList<Annotation>();


            // ---- step 1.1 ----
            // get current annotation; check current annotation is NOT null.
            //Annotation currentAnnotation = WorkSet.currentAnnotation;
            Annotation annotation_left = null;

            if (env.Parameters.CreateRelationship.where == 1) {
                annotation_left = getSelectedAnnotation();
            }
            if (env.Parameters.CreateRelationship.where == 2) {
                annotation_left = ((userInterface.annotationCompare.ExpandButton) jPanel60).getSelectedAnnotation();
            }


            if ((annotation_left == null)
                    || (annotation_left.annotationclass == null)
                    || (annotation_left.annotationclass.trim().length() < 1)) {
                //System.out.println("ERROR 1202291431:: fail to find current annotation.");
                return;
            }

            // ---- step 1.2 ----
            // get all selected annotations (not by current clicking, they selected by previous clicking)
            Depot depotOfAnn = new Depot();
            Vector<Integer> selectedIndexes = Depot.SelectedAnnotationSet.getSelectedAnnotationSet();

            // ---- step 1.3 ----
            // record annotations on the source point of the possible relationship 
            if (env.Parameters.CreateRelationship.where == 2) {
                sourceAnnotations.add(annotation_left);
            } // if the first annotation is selected on the editor panel 
            else if (env.Parameters.CreateRelationship.where == 1) {

                // ignore possible errors
                if ((selectedIndexes == null) || (selectedIndexes.size() < 1)) {
                    sourceAnnotations.add(annotation_left);
                    //System.out.println("no annotation in selected list");
                } else {

                    //if (jList_selectedAnnotations.getModel().getSize() == 1) {
                    //    sourceAnnotations.add(annotation_left);
                    //} else {
                        for (int i = 0; i < selectedIndexes.size(); i++) {
                            
                            Annotation a;
                            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                                a = depotOfAdj.getAnnotationByUnique(WorkSet.getCurrentFile().getName(), selectedIndexes.get(i));
                            }else
                                a = depotOfAnn.getAnnotationByUnique(WorkSet.getCurrentFile().getName(), selectedIndexes.get(i));
                            
                            if (a != null) {
                                sourceAnnotations.add(a);
                                //System.out.println("[REL BUILDER] Source Annotation : " + a.annotationText );
                            }
                        }
                    //}
                }
            }


            // ---- step 2 ----
            // get the object annotations.	        
            //Get all annotations' unique indics at the click position
            Vector<Integer> indices = depotOfAnn.getSelectedAnnotationIndexes(
                    WorkSet.getCurrentFile().getName(), // filename of current document 
                    position, // position on the document viewer
                    false);
            //if nothing was clicked on simply return
            if (indices.isEmpty()) {
                return;
            }
            //If something was clicked on then just use the first one on the list...
            //TODO: Might want to expand on this later to allow user to disambiguate multiple
            //selections
            Annotation destAnnotation = null;
            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                destAnnotation = depotOfAdj.getAnnotation(WorkSet.getCurrentFile().getName(), indices.get(0));
            }
            else
                destAnnotation = depotOfAnn.getAnnotation(WorkSet.getCurrentFile().getName(), indices.get(0));
            if ((destAnnotation == null)
                    || (destAnnotation.annotationclass == null) 
                    || (destAnnotation.annotationclass.trim().length() < 1)) {
                System.out.println("error 1202020450:: fail to find the class of "
                        + "the destination annotation.");
                return;
            }

            //System.out.println();
            for (int i = 0; i < indices.size(); i++) {
                Annotation a = null;
                if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    a = depotOfAdj.getAnnotation(WorkSet.getCurrentFile().getName(), indices.get(i));
                }else{
                    a = depotOfAnn.getAnnotation(WorkSet.getCurrentFile().getName(), indices.get(i));
                }
                if (a != null) {
                    //if( GUI.REVIEWMODE == GUI.ReviewMode.adjudicationMode ){
                    //        if( a.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_DLETED )
                    //            continue;
                    //    }
                    objectAnnotations.add(a);
                    System.out.println("[OBJ BUILDER] Objective Annotation : " + a.annotationText);
                }
            }


            // Until now, WE have got all annotations (source, object) 
            /*if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {

                if (sourceAnnotations != null) {
                    for (int i = 0; i < sourceAnnotations.size(); i++) {
                        Annotation annotation = sourceAnnotations.get(i);
                        //if (annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK) //||( annotation.adjudicationStatus == AdjudicationStatus.NONMATCHES_DLETED ))
                        //{
                            sourceAnnotations.set(i, null);
                        //}
                    }

                    for (int i = 0; i < objectAnnotations.size(); i++) {
                        Annotation annotation = objectAnnotations.get(i);
                        //if (annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK) // ||( annotation.adjudicationStatus == AdjudicationStatus.NONMATCHES_DLETED ))
                        //{
                            objectAnnotations.set(i, null);
                        //}
                    }
                }
            }*/

                
            // check if we need to popup a dialog to handle complex status to create 
            // relationships 
            RelCheckResult relCheckResult = checkRelStatus(sourceAnnotations, objectAnnotations);
            if (relCheckResult.only_one_possible_relationship) {
                System.out.println("\n\n\n\n\n\n\n\n\n\n only one annotation");
                annotation_left = relCheckResult.a;
                destAnnotation = relCheckResult.b;

            } else if (relCheckResult.needpopup) {

                resultEditor.relationship.complex.RelationshipBuilder rbuilder = new resultEditor.relationship.complex.RelationshipBuilder(
                        this,
                        relCheckResult,
                        WorkSet.getCurrentFile().getName());
                rbuilder.setVisible(true);
                return;
            }

            // 2.1 ---------------------------------------------------------
            // get class name of source annotation and linked annotation.
            String sourceClass = annotation_left.annotationclass.trim();
            String destClass = destAnnotation.annotationclass.trim();


            //if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
            //    if ((annotation_left.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
            //            || (destAnnotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)) {
            //        commons.Tools.beep();
            //        return;
            //    }
            //}

            // 3 -----------------------------------------------------------
            // Check to make sure this relationship is allowed or not.        
            boolean health_of_rel = env.Parameters.RelationshipSchemas.checkRegexExists(
                    sourceClass + destClass);

            // exit if this relationship is not allowed.
            if (!health_of_rel) {
                commons.Tools.beep();
                return;
            }


            // 4 -----------------------------------------------------------
            // get the name of the relationship we will create.
            String rulename = env.Parameters.RelationshipSchemas.getRelName(sourceClass, destClass);
            if ((rulename == null) || (rulename.trim().length() < 1)) {
                System.out.println("ERROR 12022915111:: fail to get the relationship"
                        + " name that we want to create.");
                return;
            }


            // 5 -----------------------------------------------------------
            // tell system that we are making modifications. 
            this.setModified();

            // 6 -----------------------------------------------------------
            // begin to record new relationship into annotation        
            //
            // If the relationship already contains an instance of the clicked 
            // on annotation then do nothing, otherwise add it to the list.
            //
            // 6.1 ----------------------------------------------------------
            // just create one relationship
            // if there is no existing relationship in current annotation    
            if (annotation_left.relationships == null) {
                annotation_left.relationships = new Vector<AnnotationRelationship>();
            } else {
                // 6.1 ----------------------------------------------------------
                // this annotation already got some relationships
                // so what we need to do is check whether the one that we want to add is 
                // existing or not. And if not, we add it.    
                //If the Relationship already existed try to remove the Annotation
	            /*
                 * //that was left clicked Vector<eComplex> complexes =
                 * .getLinkedAnnotations(); boolean removed = false; for (int i
                 * = 0; i < complexes.size(); i++) { eComplex complex =
                 * complexes.get(i); if
                 * (complex.mention.equals(destAnnotation.mentionid)) {
                 * complexes.remove(i); removed = true; i--; } }
                 *
                 * // If nothing was removed, then this is new to the
                 * relationship // so add it. if (!removed) {
                 *
                 * addingTo.addLinked(new eComplex(destAnnotation));
	                    }
                 */
            }


            AnnotationRelationship newRelationship = new AnnotationRelationship(rulename);
            newRelationship.addLinked(new AnnotationRelationshipDef(destAnnotation));
            
            
            relationship.complex.dataTypes.RelationshipDef relationshipSchema = env.Parameters.RelationshipSchemas.getRelationshipSchema( rulename );
            if( relationshipSchema != null ){
                relationship.simple.dataTypes.AttributeList attributeDefs = relationshipSchema.getAttributes();
                if( attributeDefs != null ){
                    for( AttributeSchemaDef attdef : attributeDefs.getAttributes() ){
                        if( attdef == null )
                            continue;
                        if( attdef.hasDefaultValue() ){
                            if(newRelationship.attributes == null)
                                newRelationship.attributes = new Vector<AnnotationAttributeDef>();
                            newRelationship.attributes.add( new AnnotationAttributeDef( attdef.getName(), attdef.getDefault()) );    
                        }
                    }
                }
            }
            
            
            annotation_left.relationships.add(newRelationship);

            {
                //See if the selected annotations have markable classes inside of the
                //current relationship schema
                //ArrayList<String> classes = new ArrayList<String>();
                //Build Arraylist of classes to check
	            /*
                 * classes.add(WorkSet.currentAnnotation.annotationclass); for
                 * (eComplex complex : addingTo.linkedAnnotations) {
                 * classes.add(complex.annotationClass); } //Get the name of the
                 * matching relationship(returns null if nothing matches).
                 * String name =
                 * env.Parameters.ComplexRelationshipNames.getNameByRegex(classes);
                 *
                 */
                //If name is non-null then name the relationship, else clear out the name.
                //if (rulename != null) {
                //    addingTo.setMentionSlotID(rulename);
                //} else {
                //    addingTo.setMentionSlotID("");
                //}
            }

            // refresh display after modification
            // showSelectedAnnotations_inList(0);
            // WorkSet.currentAnnotation = annotation_left;

            this.display_RelationshipPath_Remove();
            
            // update these small dots (indicators)
            this.display_removeSymbolIndicators();
            this.display_showSymbolIndicators();

            if (env.Parameters.CreateRelationship.where == 2) {
                ((userInterface.annotationCompare.ExpandButton) jPanel60).cr_updateAnnotation_onComparatorPanel();
                display_relationshipPath_setPath(annotation_left);
            } else {
                // if the comparator panel is working
                if ((jPanel_comparator_container.getComponents() != null)
                        && (jPanel_comparator_container.getComponents().length >= 1)
                        && (jPanel_comparator_container.isVisible())) {
                    this.display_showAnnotationDetail_onEditorPanel(annotation_left, true);
                    ((userInterface.annotationCompare.ExpandButton) jPanel60).cr_recheckDifference();
                } else {
                    this.display_showAnnotationDetail_onEditorPanel(annotation_left);
                    showSelectedAnnotations_inList(0);
                }
            }


            //this.hilightPhrase(WorkSet.currentAnnotation);
        } catch (Exception ex) {
            System.out.println("ERROR 1203201246:: fail to create a relationship!" + "\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }



    

    
    /**Check if we need to popup a dialog to handle complex status to create relationships. 
     * Before really creating a relationship, user usually need to click two times on the 
     * document viewer to select annotations. Two of these annotations will be selected as
     * the start point and end point as a pair annotations, so we can set up relationship 
     * between. We may get serveral annotation pairs and corresponsed relationships. This
     * is considered as a complex situation as we don't which one is user really want.
     * 
     *  @param	sourceAnnotations
     *  		in format of "Vector<Annotation>"; they are all source annotations.
     *  
     *  @param	objectAnnotations
     *  		in format of "Vector<Annotation>"; they are all objective annotations.
     *  
     *  @return true, if eHOST think it need to popup a dialog to deal with this complex 
     *  		situation.
     *  		false, if no needed to popup a dialog.
     *  		
     * */
    private RelCheckResult checkRelStatus(ArrayList<Annotation> sourceAnnotations, ArrayList<Annotation> objectAnnotations) {

        RelCheckResult result = new RelCheckResult();
        result.needpopup = false;

        // no need to create relationship if any one of these annotations 
        // are null
        if ((sourceAnnotations == null) || (objectAnnotations == null)) {
            return result;
        }

        // no need to create relationship if any one of these annotations
        // are empty
        if (sourceAnnotations.size() < 1) {
            return result; // false
        }
        if (objectAnnotations.size() < 1) {
            return result; // false
        }
        // ---- 1 ----
        // count valid annotations in each annotation list
        // source annotation list must have at least one annotation
        int number_of_source_annotation = 0;
        for (int i = 0; i < sourceAnnotations.size(); i++) {
            Annotation a = sourceAnnotations.get(i);
            if (a != null) {
                number_of_source_annotation++;
            }
        }
        if (number_of_source_annotation < 1) {
            return result; // false
        }
        // object annotation list must have at least one annotation
        int number_of_object_annotation = 0;
        for (int i = 0; i < objectAnnotations.size(); i++) {
            Annotation a = objectAnnotations.get(i);
            if (a != null) {
                number_of_object_annotation++;
            }
        }
        if (number_of_object_annotation < 1) {
            return result; // false
        }
        // ---- 2 ---- 
        // begin to remove annotations who can not be linked to other 
        // annotations with any pre-defined relationship rules.
        // ---- 2.1 ----
        // set annotation to null if it can't build a relationship with any annotation in 
        // the objective annotation list.
        for (int i = 0; i < sourceAnnotations.size(); i++) {
            Annotation a = sourceAnnotations.get(i);
            if (a == null) {
                continue;
            }

            boolean flag = false; 	// flag: indicates whether annotation "a" have chance to build
            // relationship with annotation "b".
            for (int j = 0; j < objectAnnotations.size(); j++) {
                Annotation b = objectAnnotations.get(j);
                if (b == null) {
                    continue;
                }

                if (env.Parameters.RelationshipSchemas.getPossibleRels(a, b) > 0) {
                    flag = true;
                    break;
                }
            }

            if (flag == false) {
                sourceAnnotations.set(i, null);
            }
        }

        // ---- 2.2 ----		
        // set annotation to null if it can't build a relationship with any annotation in 
        // the source annotation list.
        for (int i = 0; i < objectAnnotations.size(); i++) {
            Annotation a = objectAnnotations.get(i);
            if (a == null) {
                continue;
            }

            boolean flag = false; 	// flag: indicates whether annotation "a" have chance to build
            // relationship with annotation "b".
            for (int j = 0; j < sourceAnnotations.size(); j++) {
                Annotation b = sourceAnnotations.get(j);
                if (b == null) {
                    continue;
                }

                if (env.Parameters.RelationshipSchemas.getPossibleRels(b, a) > 0) {
                    flag = true;
                    break;
                }
            }

            if (flag == false) {
                objectAnnotations.set(i, null);
            }
        }

        // ---- 3 ----
        // count valid annotations in each annotation list
        // source annotation list must have at least one annotation
        number_of_source_annotation = 0;
        for (int i = 0; i < sourceAnnotations.size(); i++) {
            Annotation a = sourceAnnotations.get(i);
            if (a != null) {
                number_of_source_annotation++;
            }
        }
        if (number_of_source_annotation < 1) {
            return result; // false
        }
        // object annotation list must have at least one annotation
        number_of_object_annotation = 0;
        for (int i = 0; i < objectAnnotations.size(); i++) {
            Annotation a = objectAnnotations.get(i);
            if (a != null) {
                number_of_object_annotation++;
            }
        }
        if (number_of_object_annotation < 1) {
            return result; // false
        }

        // ---- 4 ----
        // check whether we have more than one possible relationship
        // if there is only one pairs
        if ((number_of_source_annotation == 1) && (number_of_object_annotation == 1)) {

            Annotation a = null, b = null;
            for (int i = 0; i < sourceAnnotations.size(); i++) {
                Annotation ann = sourceAnnotations.get(i);
                if (ann != null) {
                    a = ann;
                    break;
                }
            }
            for (int i = 0; i < objectAnnotations.size(); i++) {
                Annotation bnn = objectAnnotations.get(i);
                if (bnn != null) {
                    b = bnn;
                    break;
                }
            }

            // no need the dialog as eHOST can't find the annotation pair
            if ((a == null) || (b == null)) {
                return result; // false
            } else {
                // 
                if (env.Parameters.RelationshipSchemas.getPossibleRels(a, b) > 1) {
                    result.needpopup = true;
                    result.sourceAnnotations = sourceAnnotations;
                    result.objectAnnotations = objectAnnotations;
                    return result;
                } else {
                    result.sourceAnnotations = sourceAnnotations;
                    result.objectAnnotations = objectAnnotations;
                    
                    
                    result.only_one_possible_relationship = true;
                    result.a = a;
                    result.b = b;
                    
                    return result; // false
                }
            }
        } // To multiple annotations, we need to remove annotations that there is no relationship for them 
        else {
            result.needpopup = true;
            result.sourceAnnotations = sourceAnnotations;
            result.objectAnnotations = objectAnnotations;
            return result;
        }

    }

    
    
	public boolean stopRelationshiping()
    {     
            if((addingTo != null)&&(WorkSet.currentAnnotation != null))
            {
                //See if the selected annotations have markable classes inside of the
                //current relationship schema
                ArrayList<String> classes = new ArrayList<String>();
                classes.add(WorkSet.currentAnnotation.annotationclass);
                for(AnnotationRelationshipDef complex : addingTo.linkedAnnotations)
                {
                    classes.add(complex.annotationClass);
                }
                String name = env.Parameters.RelationshipSchemas.getNameByRegex(classes);
                if(name!= null)
                {
                    addingTo.setMentionSlotID(name);
                }
                else
                {
                    Toolkit.getDefaultToolkit().beep();
                    WorkSet.currentAnnotation.relationships.remove(addingTo);
                }
                addingTo=null;
                //We may have modified the relationship so refresh information
                this.display_showAnnotationDetail_onEditorPanel(WorkSet.currentAnnotation);
                showSelectedAnnotations_inList(0);
                return true;
            }
            addingTo = null;
            return false;
    }
    
    
    /**Display relationship information of current annotation on the list of 
     * relationship. 
     * 
     * NOTICE: This one didn't draw the relationship path on screen.
     */
    public void display_editor_ListRelationships( final 
            resultEditor.annotations.Annotation annotation ) {

        //####-1-   show normal relationships
        if ( annotation.attributes == null ){
            Vector empty = new Vector();
            jList_normalrelationship.setListData(empty);
            //jLabel_typeOfRelationship.setText("Type of normalrelationships: ");
        } else {
            //jLabel_typeOfRelationship.setText("Type of normalrelationships: ");
            Vector<String> list = new Vector<String>();
            for( resultEditor.annotations.AnnotationAttributeDef att : annotation.attributes ){
                if ((att == null)||(att.name==null)||(att.value==null))
                    continue;
                
                    String str = " \"" + att.name + "\" = " + att.value;
                    
                    list.add(str);
                
            }
            jList_normalrelationship.setListData( list );
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
        }
    }
    
    /*
    private void openRelationshipViewer()
    {
        Vector<iListable> listable = new Vector<iListable>();
        if(WorkSet.currentAnnotation == null)
            return;
        if(WorkSet.currentAnnotation.ComplexRelationships!= null)
        {
            for(ComplexRelationship f: WorkSet.currentAnnotation.ComplexRelationships)
            {
                listable.add(f);
            }
        }
        if(editor == null || !editor.isVisible())
        {
            editor =  new resultEditor.relationship.Editor(this, listable, resultEditor.relationship.Editor.Type.ComplexRelationships);
            editor.setVisible(true);
        }
    }*/
    
    
    private void relationshipStuffChanged()
    {
        if(this.jList_complexrelationships.getSelectedIndex() >= 0)
        {                        
            delete_Relationships.setEnabled(true);
        }
        else
        {            
            delete_Relationships.setEnabled(false);
        }
    }

    
            
    public void cr_disableButton(){
        jButton_cr.setSelected( false );
    }

    /**Popup the dialog of the attribute editor for current annotation. If it's
     * already set to visible, bring it into the front of the GUI, so user can
     * see it. 
     * 
     */
    private void openAttributeEditor()
    {
        // quit if we didn't allocate an annotation for this attribute editor.
        if (WorkSet.currentAnnotation == null)        
            return;
        
        // prepare the list that will be listed on the editor. It contains the
        // attribute names and their values.
        Vector<iListable> finalList = WorkSet.currentAnnotation.getAttributesForShow();

        // if this dialog hasn't been inited yet, we need to create a new one.
        if(editor == null || !editor.isVisible()) {
            editor = new resultEditor.relationship.Editor( 
                    this,       
                    WorkSet.currentAnnotation,
                    finalList,  // the attributes entries for this kind of
                                // annotation.
                                // It includes attribtues and allowed values.
                    resultEditor.relationship.Editor.Type.Attributes,
                                // tell the dialog that this time it's used
                                // for the 
                    resultEditor.relationship.Editor.Where.EditorPanel
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
        if (WorkSet.currentAnnotation == null)        
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
                    WorkSet.currentAnnotation, 
                    relationship,
                    this,
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Verifier">
    private void setToVerifier()
    {
        infoList.setListData(verifierAnnotations);
        jLabel29.setText("Verifier");
        currentScreen = infoScreens.VERIFIER;
    }
    private void resetVerifier()
    {
        WorkSet.currentlyViewing = new ArrayList<Integer>();
        WorkSet.filteredViewing = false;
    }
    /**
     * This method will fill member variables with the potential problems as flagged by the
     * Verifier using the current verifier dictionaries and settings.
     */
    private void callVerifierWithCurrent()
    {
        BufferedWriter writer = null;
        try
        {
            File file = new File("VerifierOutput.txt");
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file, false));
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        //Make sure verifier dictionaries are loaded.
        dictionaries.VerifierDictionaries.loadPreAnnotatedConcepts();
        FLAG_MAYBE_CHANGED = false;
        verifier.SpanCheck spanCheck = new verifier.SpanCheck(
                env.Parameters.corpus.getAbsoluteFileName(jComboBox_InputFileList.getSelectedIndex()), env.Parameters.VERIFIER_DICTIONARIES, writer);
        spanCheck.findProblems(-1);

        Depot depot = new Depot();
        for(Article art: depot.getAllArticles())
        {
            try {
                writer.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(Annotation annot: art.annotations)
            {
                annot.setVerified(false);
                annot.verifierSuggestion = new Vector<String>();
            }
            if(art.baseArticle == null)
            {
                verifier.MatchOldAndNew.MatchArticleWithDirectory(art, env.Parameters.verifierComparisonSettings.getDirectoryString());
            }
            if(art.baseArticle != null)
            {
            File[] possibleMatches = env.Parameters.corpus.getFiles();
            for(File f: possibleMatches)
            {
                if(f.getName().equals(art.filename))
                {
                    VerifyChallenge2011 verify = new VerifyChallenge2011(art, f, writer);
                    try
                    {
                        ArrayList<Annotation> annots = verify.outputVerify();
                        depot.setAnnotationsVisible(annots, art);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    catch(IOException e)
                    {
                        
                    }
                }
            }
            //File currentTextSource = env.clinicalNoteList.CorpusLib.
            //VerifyChallenge2011 temp = new VerifyChallenge2011(art, );
        }
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void callVerifierWithAll()
    {
        BufferedWriter writer = null;
        try
        {
            File file = new File("VerifierOutput.txt");
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file, false));
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        Depot depot = new Depot();
        for(Article art: depot.getAllArticles())
        {
            try {
                writer.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(Annotation annot: art.annotations)
            {
                annot.verifierSuggestion = new Vector<String>();
            }
            if(art.baseArticle == null)
            {
                verifier.MatchOldAndNew.MatchArticleWithDirectory(art, env.Parameters.verifierComparisonSettings.getDirectoryString());
            }
            if(art.baseArticle != null)
            {
            File[] possibleMatches = env.Parameters.corpus.getFiles();
            for(File f: possibleMatches)
            {
                if(f.getName().equals(art.filename))
                {
                    VerifyChallenge2011 verify = new VerifyChallenge2011(art, f, writer);
                    try
                    {
                        ArrayList<Annotation> annots = verify.outputVerify();
                        depot.setAnnotationsVisible(annots, art);
                    }
                    catch(FileNotFoundException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    catch(IOException e)
                    {

                    }
                    FLAG_MAYBE_CHANGED = false;
                    verifier.SpanCheck spanCheck = new verifier.SpanCheck(
                    f.getAbsolutePath(), env.Parameters.VERIFIER_DICTIONARIES, writer);
                    spanCheck.findProblems(-1);
                }
                
            }
            //File currentTextSource = env.ClinicalNoteList.CorpusLib.
            //VerifyChallenge2011 temp = new VerifyChallenge2011(art, );
        }
        }
        dictionaries.VerifierDictionaries.loadPreAnnotatedConcepts();
        for(File file: WorkSet.getAllTextFile())
        {
            /*
            FLAG_MAYBE_CHANGED = false;
            verifier.SpanCheck spanCheck = new verifier.SpanCheck(
                file.getAbsolutePath(), env.Parameters.VERIFIER_DICTIONARIES, writer);
            spanCheck.findProblems(-1);
             *
             */
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Preannotate Tab">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Information Tab">
    private void setToClassConflict() {
        Vector<classConflict> conflicts = new Vector<classConflict>();
        conflicts.addAll(conflictWithWorking);
        infoList.setListData(conflicts);
        jLabel29.setText("Class Conflicts");
        currentScreen = infoScreens.CLASSCONFLICT;
    }
    private void setToAnnotators()
    {
        //Set to Annotators
        Vector<String> theAnnotators = new Vector<String>();
        theAnnotators.addAll(annotatorsList);
        infoList.setListData(theAnnotators);
        jLabel29.setText("Annotator");
        currentScreen = infoScreens.ANNOTATORS;
    }
    /*
     * This should be called when the user has double clicked on an annotator in
     * the annotations information tab, this will display the Annotations with annotators
     * matching the chosen annotator.
     */
    private void selectAnAnnotator()
    {

        Vector<Annotation> toDisplay = new Vector<Annotation>();

        //Get the selected annotator
        String selectedAnnotator = (String)infoList.getSelectedValue();

        //find annotations with matching annotators.
        for(Annotation annotation: annotationsList)
        {
            if(annotation.getAnnotator().equals(selectedAnnotator))
                toDisplay.add(annotation);
            else if(annotation.getFullAnnotator().equals(selectedAnnotator))
                toDisplay.add(annotation);
        }

        //alphabetize and display
        Collections.sort(toDisplay);
        infoList.setListData(toDisplay);
        jLabel29.setText(selectedAnnotator);
    }
    private JFileChooser setFileChooserAttributes(fileInputType type){
        // if the filechooser was used for
        if (type == fileInputType.importXMLandPin ){
            JFileChooser jFileChooser2 = new JFileChooser();
            jFileChooser2.setDialogType( JFileChooser.OPEN_DIALOG);
            jFileChooser2.setFileSelectionMode(0);
            jFileChooser2.setMultiSelectionEnabled(true);
            jFileChooser2.setDialogTitle("Import From:");
            String[] all = new String[] { "" };
            String[] xmlpin = new String[] { "pins", "xml" };

            int size = jFileChooser2.getChoosableFileFilters().length;
            javax.swing.filechooser.FileFilter[] filefilters = jFileChooser2.getChoosableFileFilters();

            for (int i = 0; i < filefilters.length; i++) {
               jFileChooser2.removeChoosableFileFilter(filefilters[i]);
            }


            jFileChooser2.addChoosableFileFilter(new commons.SimpleFileFilter(all,
                "All files (*.*)"));
            jFileChooser2.addChoosableFileFilter(new commons.SimpleFileFilter(xmlpin,
                "XML and PINS annotation files (*.xml, *.pins)"));

            // set dafault folder is latest accessed folder
            jFileChooser2.setCurrentDirectory( recentlyOpennedFolder );


            return jFileChooser2;

        }else if (type ==  fileInputType.selectClinicalTextFiles ){
            // show file dialog to select clinical notes
            // select file, not directory
            jFileChooser1.setDialogType(1);
            jFileChooser1.setFileSelectionMode(0);
            jFileChooser1.setMultiSelectionEnabled(true);
            jFileChooser1.setDialogTitle("Please Choose Clinical Notes(Text):");
            String[] all = new String[] { "" };
            String[] text = new String[] { "txt" };


            javax.swing.filechooser.FileFilter[] filefilters = jFileChooser1.getChoosableFileFilters();

            for (int i = 0; i < filefilters.length; i++) {
               jFileChooser1.removeChoosableFileFilter(filefilters[i]);
            }


            jFileChooser1.addChoosableFileFilter(new commons.SimpleFileFilter(all,
                "All files (*.*)"));
            jFileChooser1.addChoosableFileFilter(new commons.SimpleFileFilter(text,
                "text file (*.txt)"));
            return null;

        }else if(type ==  fileInputType.setXmlOutputforConcepts){
            // prepare the file chooser
            jFileChooser1.setDialogType(1);

            jFileChooser1.setDialogType(JFileChooser.OPEN_DIALOG);
            jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser1.setSelectedFile(null);
            jFileChooser1.setCurrentDirectory(null);
            File f = jFileChooser1.getSelectedFile();

            // try to remove file name from previous operation, maybe the
            // text file selecting operation.
            try{
                if (f!=null)
                    jFileChooser1.setSelectedFile(new File(f.getPath()));
            }catch(Exception ex){
                System.out.println("Warning 1009091503: fail to set path.");
            }

            jFileChooser1.setMultiSelectionEnabled(false);
            jFileChooser1.setDialogTitle("Chooser folder to ouput annotations:");
            javax.swing.filechooser.FileFilter[] filefilters = jFileChooser1.getChoosableFileFilters();

            for (int i = 0; i < filefilters.length; i++) {
               jFileChooser1.removeChoosableFileFilter(filefilters[i]);
            }
            return null;
        }
        return null;
    }
    /**
     * This function should be called when the classes should be displayed
     * in the list of information in the Annotation information tab.
     */
    private void setToClasses()
    {
        //To store the classes in.
        Vector<String> theClasses = new Vector<String>();

        //Add all of the classes from the classes list.
        theClasses.addAll(classesList);

        //Keep track of how many times each class occurs
        ArrayList<Integer> counts = new ArrayList<Integer>();

        //Start each count at 0.
        for(int i = 0; i < theClasses.size(); i++)
            counts.add(0);

        //Go through all of the annotations, and count the number of classes
        for(Annotation annotation: annotationsList)
        {
            //Check to see which class the Annotation matches and count it.
            for(int i = 0; i< theClasses.size(); i++)
            {
                //If it matches this class then increment the coresponding count
                if(theClasses.get(i).equals(annotation.annotationclass))
                {
                    counts.set(i, counts.get(i)+1);
                }
            }
        }
        //Build the string to display
        for(int i =0 ; i< theClasses.size(); i++)
        {
            theClasses.set(i, "<html>Class Name: " + theClasses.get(i) + " <br><font color = \"gray\">Count in File: " + counts.get(i) + "</font></html>");
        }
        //put it in alphabetical order
        Collections.sort(theClasses);

        //Set the list data
        infoList.setListData(theClasses);

        //Indicate that we're viewing classes
        jLabel29.setText("Class");

        //Set the current screen to classes
        currentScreen = infoScreens.CLASSES;
    }
    /**
     * This function should be called to set the info list in the annotations tab
     * to the list of all annotations.
     */
    private void setToAnnotations()
    {
        //Will contain all of the anotations
        Vector<Annotation> v = new Vector<Annotation>();

        //Loop through all of the annotations to add them to the Vector.
        for(int firstPass = 0; firstPass < annotationsList.size(); firstPass++)
        {
            v.add(annotationsList.get(firstPass));
        }
        //sort the list so it's in alphabetical order
        Collections.sort(v);

        //display the annotation
        infoList.setListData(v);

        //We're viewing annotations
        jLabel29.setText("Annotation");
        currentScreen = infoScreens.ANNOTATIONS;
    }

    public void refreshInfo()
    {
        

        reDrawInfoList();

        new Thread() {

            @Override
            public void run() {
                try{
                //jLabel29.setText("");
                classesList = new HashSet<String>();
                annotatorsList = new HashSet<String>();
                annotationsList = new Vector<Annotation>();
                conflictWithWorking = new Vector<classConflict>();
                verifierAnnotations = new Vector<Annotation>();
                Depot depot = new Depot();
                WorkSet.getCurrentFileIndex();
                File current = WorkSet.getCurrentFile();
                if (current != null) {
                    Article article = depot.getArticleByFilename(current.getName());
                    if (article == null) {
                        //If article is null then just clear out the results
                        annotations.setText("<html>" + annotationsText + "</html>");
                        classes.setText("<html>" + classesText + "</html>");
                        annotators.setText("<html>" + annotatorsText + "</html>");
                        workingConflicts.setText("<html>" + conflictsText + "</html>");
                        overlapping.setText("<html>" + overlappingText + "</html>");
                        verifierFlagged.setText("<html>" + verifierText + "</html>");
                        currentScreen = infoScreens.NONE;
                        reDrawInfoList();
                        return;
                    }

                    //Get all of the annotations
                    annotationsList = article.annotations;
                    //Loop through all annotations to get data
                    for (Annotation annotation : annotationsList) {
                        //Capture all classes and annotators
                        classesList.add( annotation.annotationclass );
                        annotatorsList.add( annotation.getAnnotator() );

                        //If the annotation has Verifier information then capture that as well
                        if ((annotation.verifierFound != null && annotation.verifierFound.size() > 0) || (annotation.verifierSuggestion != null && annotation.verifierSuggestion.size() > 0) || annotation.isVerified()) {
                            verifierAnnotations.add(annotation);
                        }
                    }

                    //Update tab with new information
                    overlappingAnnotations = tmp_Conflicts.getSpanConflicts(current.getName());
                    conflictWithWorking = tmp_Conflicts.getClassConflicts(current.getName());
                    annotations.setText("<html>" + annotationsText + "<font color = \"blue\">" + annotationsList.size() + "</font></html>");
                    classes.setText("<html>" + classesText + "<font color = \"blue\">" + classesList.size() + "</font></html>");
                    annotators.setText("<html>" + annotatorsText + "<font color = \"blue\">" + annotatorsList.size() + "</font></html>");
                    workingConflicts.setText("<html>" + conflictsText + "<font color = \"blue\">" + conflictWithWorking.size() + "</font></html>");
                    overlapping.setText("<html>" + overlappingText + "<font color = \"blue\">" + overlappingAnnotations.size() + "</font></html>");
                    verifierFlagged.setText("<html>" + verifierText + "<font color = \"blue\">" + verifierAnnotations.size() + "</font></html>");
                }
                }catch(Exception ex){
                
                }
            }
            
        }.start();


    }

    /**
     * Should be called whenever the infoList needs to be redrawn.
     * currentScreen variable must be set correctly for this to work.
     */
    private void reDrawInfoList()
    {

        switch(currentScreen)
        {
            case ANNOTATIONS:
                setToAnnotations();
                break;
            case ANNOTATORS:
                setToAnnotators();
                break;
            case CLASSCONFLICT:
                setToClassConflict();
                break;
            case NONE:
                infoList.setListData(new Vector());
                break;
            case SPANCONFLICT:
                setToSpanConflict();
                break;
            case CLASSES:
                setToClasses();
                break;
            case VERIFIER:
                setToVerifier();
                break;
        }
    }

    /**
     * Set annotation information list to view spanConflicts.
     */
    private void setToSpanConflict()
    {
        Vector<spanOverlaps> conflicts = new Vector<spanOverlaps>();
        conflicts.addAll(this.overlappingAnnotations);
        infoList.setListData(conflicts);
        jLabel29.setText("Overlapping Annotations");
        currentScreen = infoScreens.SPANCONFLICT;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Importing Annotations">
    /**Import annotations from XML or PIN files and show on screen.<p>
     * #1 show file chooser to let users select XML or PINS files.<p>
     * #2 (subfunction)extract annotation from pin or xml files.<p>
     * #3 (subfunction)show imported annotation on screen.
     */
    public void importAnnotations() {

        Vector matchedAnnotationFiles = new Vector();
        File[] files = null;
        int loops = 0;
        try{

            // set file chooser
            JFileChooser filechooser = setFileChooserAttributes( fileInputType.importXMLandPin );

            // ## 1.1 ## - show dialog of filechooser
            int re = filechooser.showOpenDialog(this);
            if (re != 0) return;

            // ## 1.2 ## - get selected files (pins and xml files)
            files = filechooser.getSelectedFiles();
            if ( files == null ) return;
            if ( files.length < 1 ) return;

            // if choosed file(s)
            loops = files.length;
            int size_InputedClinicalNotes = env.Parameters.corpus.getSize();


            // to all selected files to import, let's get knowtator xml files whose filenames are
            // appeared in the list of text sources after string ".knowtator.xml" removed
            
            for (int i = 0; i < loops; i++)
            {
                // get each xml file in the list
                String filename = files[i].getName();
                filename = filename.trim().toLowerCase();

                for (int j = 0; j < size_InputedClinicalNotes; j++)
                {

                    // get textsourceFilename of each imported clinical notes
                    env.clinicalNoteList.CorpusStructure corpus
                            = env.Parameters.corpus.LIST_ClinicalNotes.get(j);
                    if (corpus == null)
                        continue;
                    if (corpus.file == null)
                        continue;

                    String inputfilename = corpus.file.getName();
                    String inputfilename1 = (inputfilename + ".knowtator.xml").trim().toLowerCase();
                    // than we can compare them, and record matched annotation xml file
                    if ( filename.compareTo(inputfilename1) == 0 )
                        matchedAnnotationFiles.addElement(files[i]);
                    else{
                        if(inputfilename.length()>4){
                            String inputfilename2 =
                                    inputfilename.substring(0,inputfilename.length()-4)
                                    +
                                    ".knowtator.xml";
                            if ( filename.compareTo(inputfilename2) == 0 )
                                matchedAnnotationFiles.addElement(files[i]);
                        }
                    }
                }
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"### ERROR ###: 1106131510:: " + ex.getMessage() );
        }

        for(Object o:matchedAnnotationFiles){
            log.LoggingToFile.log( Level.SEVERE, ((File)o).getName() );
        }


        // #3.1 extract annotations from XML files
        extractAnnotation_fromXML( matchedAnnotationFiles );

        // #4.1 get all PIN files from just selected files
        // #4.2 extract annotations from PIN files
        int count = extractAnnotation_fromPINS( files );

        //Get the annotations that we just added.
        Depot depot = new Depot();
        Vector<Annotation> newOnes = new Vector<Annotation>();
        for(Annotation annotation: depot.getAllAnnotations()) {
            if(!annotation.isVerified())
                newOnes.add(annotation);
        }

        //Create Sets for new attributes and relationships
        //TreeSet<AttributeSchemaDef> newAttributes = new TreeSet<AttributeSchemaDef>();
        Hashtable<String, Hashtable> unknownAttributes = new Hashtable<String, Hashtable>();
        TreeSet<String> newRelationships = new TreeSet<String>();

        //Find Attributes which are outside our schema
        //newAttributes.addAll( env.Parameters.AttributeSchemas.checkExists(newOnes) );
        unknownAttributes =env.Parameters.AttributeSchemas.getExceptions( newOnes );

        //Create list of unique unknown relationships
        TreeSet<ComplexRelImportReturn> unknowRelationships = new TreeSet<ComplexRelImportReturn>();
        unknowRelationships.addAll(env.Parameters.RelationshipSchemas.checkUniqueRegex());

        //Set annotations visible
        setAnnotationVisible();

        // show clinical notes in color
        int selected = jComboBox_InputFileList.getSelectedIndex();
        showFileContextInTextPane(selected, 0);
        //If unknown attributes or relationships were inputted then open an import schema handler
        if( (env.Parameters.AnnotationImportSetting.needSchemaHandler)
            && ( unknownAttributes.size() >0 || unknowRelationships.size() > 0 )
              )
        {
            relationship.importing.WrapperForBoth wrapper = new relationship.importing.WrapperForBoth( 
                    unknownAttributes, 
                    unknowRelationships, 
                    this, 
                    loops + " file(s) selected: "
                        + (matchedAnnotationFiles.size()+ count)
                        + " file(s) imported for matching text files."
                    );
            wrapper.doImportForBoth();
        }
        //If no unknown attributes/relatinships just show a message indicating how many files inputted.
        else
        {
            // show messagebox to tell how many matched xml file got important
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, loops + " file(s) selected: "
                    + (matchedAnnotationFiles.size()+ count)
                    + " file(s) imported for matching text files.");
            //500 file(s) selected: 3 file(s) imported for matching text files.
        }


    }
    /**
     * Extract annotations from Protege Knowtator pins file.
     *
     * @param   files
     *          the designated PINS files.
     *
     * @return  ***TODOTODO::need recheck the return value***
     */
    private int extractAnnotation_fromPINS(File[] files){
        // ##1 get all PIN files from just selected files
        File[] pinFiles = getPinFiles( files );
        // ##2 extract annotations from PIN files
        int count = 0;
        if( pinFiles != null ){
            resultEditor.io.Extractor extractor = new resultEditor.io.Extractor();
            extractor.fromPINSfiles( pinFiles );
            count = pinFiles.length;
        }
        return count;
    }


    /**Find and return all pin files from a given file array.
     * @return all found pin files which got from param "files".
     * @param  files - all files.
     */
    private File[] getPinFiles(final File[] files){
        Vector<File> resultFiles = new Vector<File>();

        // #1. get files with suffix name ".pins"
        for(int i=0; i< files.length; i++ ) {
            if (files[i] == null) continue; // validity check
            // search for ".pin" suffix
            if ( files[i].getName().toLowerCase().contains(".pins") ) {
                resultFiles.add(files[i]);
            }
        }

        // #2. build return array in format of "File[] "
        int size = resultFiles.size();
        if(size == 0)
            return null;

        File[] results = new File[size];
        for(int i=0;i<size; i++) {
            if ( resultFiles.get(i) instanceof File )
                results[i] = resultFiles.get(i);
        }
        return results;
    }


    /**load XML files and then extract annotations from them.
     * @param   XMLFiles
     */
    private void extractAnnotation_fromXML(Vector XMLFiles) {
        // ##0## record file information about all imported xml files
        try{
            if(XMLFiles==null)
                return;

            for(File f: (Vector<File>) XMLFiles){
                if(f==null)
                    continue;
                if(f instanceof File) {
                    env.Parameters.AnnotationsImportingCorrelated.allImportedXMLs.add(f);
                    //System.out.println("xml annotation file name and path saved! -" + f.toString() );
                }
            }

        }catch(Exception ex){}

        // ##1## start annotation extraction
        resultEditor.annotations.ImportAnnotation imports = new
                resultEditor.annotations.ImportAnnotation();
        imports.XMLImporter(XMLFiles);

    }



    private void cleanImportedXMLBufferAndReload() {
        try{

            // saved file list which contains all file information of imported
            // xml annotation files
            env.Parameters.AnnotationsImportingCorrelated.allImportedXMLs.clear();

            //
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            depot.clear();
            int caretpostion = textPaneforClinicalNotes.getCaretPosition();
            int selected = jComboBox_InputFileList.getSelectedIndex();
            showFileContextInTextPane(selected, 0);
            //textPaneforClinicalNotes.setCaretPosition(caretpostion);
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1101012338::"+ex.toString());
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods - Tab Management">
    /**Enter different tab by user's selection
     * @param _tab  show which tab user is expecting for<p>
     *      type is enum <code>tabs</code>{<p>
     *          creationAnnotation, resulteditor, dictionariesSetting,<p>
     *          pinExtractor, dictionariesManager, exit<p>
     *      };<p>
     */
    public void tabDoorman(TabGuard.tabs _tab)
    {

        // before we start any operation, we hide these buttons and status icons
        // that only correlated to annotation editor panel.
        TabGuard.tabs tab = TabGuard.getLatestActivedTab();
        if( (_tab != TabGuard.tabs.creationAnnotation)
                && ( tab != TabGuard.tabs.resulteditor) )
            hideStatusButtons();

        // set right button selected

        setFlag_allowToAddSpan( false ); // cancel possible operation of adding new span

        // set right button selected        
        if((_tab == TabGuard.tabs.exit)||
                (_tab== TabGuard.tabs.dictionariesSetting) ){
            // selected button matched to previous tab
            setTabButton(tab);
            setButtonSelected(tab);
        }else{
            // set status of buttons
            setTabButton(_tab);
            // this actived tab
            TabGuard.justActivedTab(_tab);
        }



        // enter different tab by user's selection
        switch(_tab){
            // enter tab to annotate documents by running NLP with pre-annotated
            // dictinary, regular expressions, etc
            case creationAnnotation:
                enterTab_createAnnotaion();
                break;

            // enter tab of result editor
            case resulteditor:
                
                enterTab_ResultEditor();
                break;

            // enter tab for dictionary setting
            case dictionariesSetting:
                // show dictionaries setting dialog
                launch_dialog_of_configuration();
                break;

            // enter tab of pin extractor
            case pinExtractor:
                enterTab_pinExtractor();
                break;

            // show dialog of dictionaries manager
            case configuration:
                //preAnnotate.DictionaryGUI.main(null);
                enterTab_createDictionary();
                break;
            //case converter:
            //    enterTab_converter();
            //    break;

            case assignmentsScreen:
                enterTab_assignmentsScreen();
                break;
            // show confirmation window before closing whole application
            case exit:
                
                saveModification();

                //Show dialog of exit confirmation.
                popupExitDialog();

                break;
        }
    }

    /**Save modifications of annotations while user leaving current document
     * or closing eHOST.*/
    private void saveModification() {
        // if user modified something of this current, ask user whether they
        // want to save changes or not.
        if (this.modified) {
            // get user's decision
            boolean yes_no = popDialog_Asking_ChangeSaving();

            // call saving function if needed.
            if (yes_no) {
                this.saveto_originalxml();
            }
        }
    }


    // selected button matched to previous tab
    private void setTabButton(TabGuard.tabs _tab){
        // norma
        Font f = new Font("Arial", Font.BOLD, 12);
        java.awt.Color c = new java.awt.Color(0,102,102);

        // selected
        Font F = new Font("Arial", Font.BOLD, 13);
        java.awt.Color C = new java.awt.Color(41, 41, 41);

        // set font of buttons on tools bar
        jToggleButton_CreateAnnotaion.setForeground(
                _tab == TabGuard.tabs.creationAnnotation ? C:c);
        jToggleButton_CreateAnnotaion.setFont(
                _tab == TabGuard.tabs.creationAnnotation ? F:f);

        jToggleButton_ResultEditor.setForeground(
                _tab == TabGuard.tabs.resulteditor ? C:c);
        jToggleButton_ResultEditor.setFont(
                _tab == TabGuard.tabs.resulteditor ? F:f);

        jToggleButton_PinExtractor.setForeground(
                _tab == TabGuard.tabs.pinExtractor ? C:c);
        jToggleButton_PinExtractor.setFont(
                _tab == TabGuard.tabs.pinExtractor ? F:f);

        jToggleButton_Converter.setForeground(
                _tab == TabGuard.tabs.converter ? C:c);
        jToggleButton_Converter.setFont(
                _tab == TabGuard.tabs.converter ? F:f);

        jToggleButton_DictionaryManager.setForeground(
                _tab == TabGuard.tabs.configuration ? C:c);
        jToggleButton_DictionaryManager.setFont(
                _tab == TabGuard.tabs.configuration ? F:f);

        jToggleButton_DictionarySetting.setForeground(
                _tab == TabGuard.tabs.dictionariesSetting ? C:c);
        jToggleButton_DictionarySetting.setFont(
                _tab == TabGuard.tabs.dictionariesSetting ? F:f);


        jToggleButton_exit.setFont(
                _tab == TabGuard.tabs.exit ? F:f);

    }
    /**reenabled these button we just used before open a new
     * dialog and current frame lost focus.*/
    private void setButtonSelected(TabGuard.tabs _tab ){
        switch(_tab){
            case creationAnnotation:
                jToggleButton_CreateAnnotaion.setSelected(true);
                break;
            case resulteditor:
                jToggleButton_ResultEditor.setSelected(true);
                break;
            case pinExtractor:
                jToggleButton_PinExtractor.setSelected(true);
                break;
            case converter:
                jToggleButton_Converter.setSelected(true);
                break;
            case configuration:
                jToggleButton_DictionaryManager.setSelected(true);
                break;
        }
    }

    /* load all cardlayout panels */
    private void resetCardLayout(){
        GUIContainer.removeAll();
        GUIContainer.add(JPanel_PinsExtractor, "PinsExtractor");
        GUIContainer.add(jPanel_NLP, "CreateAnnotaion");
        GUIContainer.add(Editor, "InteractiveEditor");

    }
    /**user clicked button to active tab of pin extractor*/
    private void enterTab_pinExtractor(){

        if(!pinsExtractorExisted())
            GUIContainer.add(new preAnnotate.integratedPinsExtractor(), "pinsExtractor");
        // load tab of result editor
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "pinsExtractor");
    }

    /**user clicked button to active tab of FileConverterGUI*/
    /*private void enterTab_converter(){

        if(!converterExisted())
            GUIContainer.add(new converter.FileConverterGUI(), "converter");
        // load tab of result editor
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "converter");
    }*/

    private AssignmentsScreen getAssignmentsScreen(String username)
    {
        if (!assignmentsScreenExist()) {
            if(assignmentsScreen == null) {
                try {
					assignmentsScreen = new AssignmentsScreen(this);
					assignmentsScreen.setUserId("", username);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            GUIContainer.add(assignmentsScreen, "assignmentsScreen");
        }
        return assignmentsScreen;
    }

    /**user clicked button to active tab of FileConverterGUI*/
    private void enterTab_assignmentsScreen() {
        assignmentsScreen = getAssignmentsScreen("");
        // load tab of result editor
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "assignmentsScreen");
        
//        commons.Tools.beep();
    }
    
    public void switchTo(String project, String file) {
        File projectFile = new File(project);
        selectProject(projectFile);
        enterTab_ResultEditor();
        int n = jComboBox_InputFileList.getItemCount();
        for (int i=0; i<n; i++) {
            String name = (String) jComboBox_InputFileList.getItemAt(i);
            if (name != null && name.equals(file)) {
                jComboBox_InputFileList.setSelectedIndex(i);
                break;
            }
        }
    }
    
    /**It a methed that only can be called by the class of "AssignmentsScreen" to 
     * carry out a sync operation between eHOST and annotation admin server.*/
    public void verifyAnnotationsWereSaved() {
        saveto_originalxml();
    }
    /**GUI: enter tab of result editor*/
    private void enterTab_ResultEditor()
    {
        showStatusButtons();
        
        //#### 1 #### show result editor tab on screen
        jCardcontainer_interactive.removeAll();
        jCardcontainer_interactive.add(jSplitPane_between_viewer_and_allatttibutes, "resulteditor");
        CardLayout card2 = (CardLayout) jCardcontainer_interactive.getLayout();
        card2.show( jCardcontainer_interactive, "resulteditor");                         
        jCardcontainer_interactive.validate();
        jCardcontainer_interactive.repaint();
        jCardcontainer_interactive.updateUI();

        //#### 2 #### show documents viewer tab on screen
        GUIContainer.removeAll();
        GUIContainer.add("InteractiveEditor", Editor );
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "InteractiveEditor");


        // clear possible contents of terms        

        // set most text filed to uneditable
        //jTextField_spanLocation.setEnabled(false);
        jTextField_annotator.setEnabled(false);
        jTextField_creationdate.setEnabled(false);
        
        // disable buttons for span and class edit
        
        disableAnnotationDisplay();
        disable_AnnotationEditButtons();

        // refresh the list of projects -- Yarden
        refreshNAVProjects();
        
        enter_Interactive();
        addPositionIndicator();         
         
    }



    /**GUI: enter tab of creation annotaion*/
    public void enterTab_createAnnotaion(){

        // read enverionment values       
        config.system.SysConf.loadSystemConfigure();
        
        

        // load tab of creation annotation
        // load tab of result editor
        //if(env.Parameters.WorkSpace.CurrentProject==null){
        //    GUIContainer.setVisible(false);
        //    return;
        //}

        GUIContainer.removeAll();
        GUIContainer.add("InteractiveEditor", Editor );
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "InteractiveEditor");

        // show the nlp panel if a project has been selected
        jCardcontainer_interactive.removeAll();
        //if(env.Parameters.WorkSpace.CurrentProject!=null)
        //{
        if(NLPAnnotationPanel==null){
            NLPAnnotationPanel = new annotate.gui.NLPAnnotator(this);
            
        }
            //jCardcontainer_interactive.add(jPanel_NLP, "CreateAnnotaion");
            jCardcontainer_interactive.add(NLPAnnotationPanel, "CreateAnnotaion");
            CardLayout card2 = (CardLayout) jCardcontainer_interactive.getLayout();
            card2.show( jCardcontainer_interactive, "CreateAnnotaion");
        

        jCardcontainer_interactive.validate();
        jCardcontainer_interactive.repaint();
        jCardcontainer_interactive.updateUI();
        


    }

    public Rectangle getRectofCardHolder(){
        Rectangle rect = new Rectangle();
        rect.x = jCardcontainer_interactive.getLocationOnScreen().x;
        rect.y  = jCardcontainer_interactive.getLocationOnScreen().x;
        rect.width = jCardcontainer_interactive.getWidth();
        rect.height = jCardcontainer_interactive.getHeight();
        
        return rect;

    }

    /**GUI: enter tab of creation annotaion*/
    public void enterTab_createAnnotaion_step2()
    {


        // show the nlp panel if a project has been selected
        jCardcontainer_interactive.removeAll();
        //if(env.Parameters.WorkSpace.CurrentProject!=null)
        //{
        if(nlpcpu==null){
            nlpcpu = new annotate.gui.NLPcpu(this);

        }
            //jCardcontainer_interactive.add(jPanel_NLP, "CreateAnnotaion");
            jCardcontainer_interactive.add(nlpcpu, "beginNLP");
            CardLayout card2 = (CardLayout) jCardcontainer_interactive.getLayout();
            card2.show( jCardcontainer_interactive, "beginNLP");


        jCardcontainer_interactive.validate();
        jCardcontainer_interactive.repaint();
        jCardcontainer_interactive.updateUI();

        env.Parameters.NLPAssistant.STOPSign = false;
        nlpcpu.start();


    }

    private void enterTab_createDictionary()
    {
        if(!isComponentExisted())
            GUIContainer.add(new preAnnotate.integratedDictionary(), "dictionary");
        // load tab of result editor
        CardLayout card = (CardLayout) GUIContainer.getLayout();
        card.show( GUIContainer, "dictionary");
    }

    /**check existing of component in MainPanel.
     * @return  false   if this component has not been added.
     * @return  true    if this component has existed in MainPanel.
     */
    private boolean isComponentExisted(){
        int size = GUIContainer.getComponentCount();
        for(int i=0;i<size;i++){
            Component comp = GUIContainer.getComponent(i);

            if( comp instanceof preAnnotate.integratedDictionary )
                return true;
        }
        return false;
    }

    /**check existing of component in MainPanel.
     * @return  false   if this component has not been added.
     * @return  true    if this component has existed in MainPanel.
     */
        private boolean pinsExtractorExisted(){
        int size = GUIContainer.getComponentCount();
        for(int i=0;i<size;i++){
            Component comp = GUIContainer.getComponent(i);

            if( comp instanceof preAnnotate.integratedPinsExtractor )
                return true;
        }
        return false;
    }
        /**check existing of component in MainPanel.
     * @return  false   if this component has not been added.
     * @return  true    if this component has existed in MainPanel.
     */
        //private boolean converterExisted(){
        //int size = GUIContainer.getComponentCount();
        //for(int i=0;i<size;i++){
        //    Component comp = GUIContainer.getComponent(i);
//
        //    if( comp instanceof converter.FileConverterGUI )
        //        return true;
        //}
        //return false;
    //}

    /**check whether the panel of assignments has been created.*/
    private boolean assignmentsScreenExist() {
        try {
            int size = GUIContainer.getComponentCount();
            for (int i = 0; i < size; i++) {
                Component comp = GUIContainer.getComponent(i);

                if (comp instanceof AssignmentsScreen) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Class TabGuard">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event Handling Methods">
    private void setRelationshipingEnabled(boolean set)
    {
        jButton_cr.setSelected(set);
        WorkSet.makingRelationships = jButton_cr.isSelected();
        
        // if user clicked and enabled this button, it means we will use 
        // annotation.
        
        // 0:  stop building relationship;
        // 1:  build relationship from editor panel;
        // 2:  build relationship from the comparator panel
        if( jButton_cr.isSelected() ){
           env.Parameters.CreateRelationship.where = 1; 
           ((userInterface.annotationCompare.ExpandButton)jPanel60).cr_disableButton();
        }else 
            env.Parameters.CreateRelationship.where = 0;
        
        if(!jButton_cr.isSelected())
        {
            jButton_cr.setToolTipText("Enable relationship building");
            stopRelationshiping( );
        }
        else
        {
            jButton_cr.setToolTipText("Disable relationship building");
        }
        relationshipStuffChanged();
    }

    /**set the flag that current document has been modified*/
    public void setModified(){
        this.modified = true;
    }
    
    /**Set the menu bar and buttons visible by given flag.
     * On the Editor panel, there are two buttons only for ""
     */
    public void setVisible_EditorAdjuducation( boolean isVisible ){
        jToolBar_editopanel_comparison.setVisible(isVisible);
    }
    
    private Annotation attributeAnnotation = null;

    /**
     * Handling mouse clicked event: mouse click on the text area and it may
     * (1)select one annotation or (2)select some text that we can build
     * new annotation.
     *
     * @param   evt
     *          Caught system mouse event.
     */
    private void mouseClickOnTextPane(java.awt.event.MouseEvent evt){

        try{
            
            
            // validity checking
            if(( !this.isEnabled() ) || (!this.isVisible()) || (!this.isActive()) )
                return;
            
            // remove previous selection highlighters
            final resultEditor.underlinePainting.SelectionHighlighter highlighter =
            new resultEditor.underlinePainting.SelectionHighlighter(textPaneforClinicalNotes);
            highlighter.RemoveAllUnderlineHighlight();
            
            
            Annotation previousAnnotation = null; 
            if ( WorkSet.currentAnnotation != null ) {
                previousAnnotation = WorkSet.currentAnnotation;
            }
                
            int clicks = evt.getClickCount();
            
            
            // #### 0.1 #### clicked by left key or right key
            boolean leftClick = evt.getButton() == MouseEvent.BUTTON1;
            boolean rightClick = evt.getButton() == MouseEvent.BUTTON3;
            
            

            if (!rightClick) {
                // #### 0.4 #### preset: leave from comparision mode if it
                //               was in a potential compraision status
                ((userInterface.annotationCompare.ExpandButton) jPanel60).setStatusInvisible();
                ((userInterface.annotationCompare.ExpandButton) jPanel60).noDiff();
            }

            // cancel any possible flag of "create new dis-joint span"
            // after any right click.
            if(rightClick)
                setFlag_allowToAddSpan(  false );
            
            // ---------------------------------------------- //
            // function: add dis-joint span if wanted 
            if( this.WANT_NEW_SPAN ){
                
                // if the indicator is "ON"(false), check and correct span border
                // by space or symbols, o.w. use exact span that user just selected
                operation_checkBorder_whileNeeded(textPaneforClinicalNotes);
                
                addSpan(evt, false);
                setFlag_allowToAddSpan( false );
                return;
            }
            // ---------------------------------------------- //
            

            // #### 0.2 ####  get filename of current document
            String textsourceFilename = resultEditor.workSpace.WorkSet.
                    getCurrentFile().getName();

            // get position of mouse 
            int position = textPaneforClinicalNotes.viewToModel(evt.getPoint());
            
            // #### 0.3 #### try to use current mouse carpet position to select annotations
            ArrayList<Annotation> selectedAnnotaions =
                resultEditor.annotations.Depot.SelectedAnnotationSet.
                    selectAnnotations_ByPosition(
                        textsourceFilename, 
                        position, 
                        false
                    );


                        
             // Catch all Left-Click Mouse Events.  If we are creating a
             // relatinoship this
             // will signal the end of relationship creation.  The Currently
             // active relationship
             // chain will be stopped and this method will return(to avoid
             // activating other
             // mouse events).
             //
             if( WorkSet.makingRelationships && leftClick)
             {
                if(stopRelationshiping())
                    return;
             }

            // ##1## right key to popup Dialog popmenu for select text to
            // build a new annotation
            // ##2## caught similar annotations in all documents
            if( textPaneforClinicalNotes.getSelectedText() != null )
            {
                
                this.setFlag_allowToAddSpan( true );
                
                // if the indicator is "ON"(false), check and correct span border
                // by space or symbols, o.w. use exact span that user just selected
                operation_checkBorder_whileNeeded(textPaneforClinicalNotes);

                if(popmenu == null || !popmenu.isGood()) {
                    popmenu = new resultEditor.annotationBuilder.Popmenu( textPaneforClinicalNotes, this, evt);
                }

                // do not want to build a annotation by method of
                // "ONE-CLICK-TO-BUILD-ANNOTATION"
                if( env.Parameters.currentMarkables_to_createAnnotation_by1Click == null )
                    popmenu.pop(evt.getX(), evt.getY(), this.getX(), this.getY(), this.getWidth(), this.getHeight());
                else
                // "ONE-CLICK-TO-BUILD-ANNOTATION", no popmenu will be pop out in following process
                {
                    if( popmenu != null )
                    {
                        popmenu.setVisible(false);
                        popmenu = new resultEditor.annotationBuilder.Popmenu(textPaneforClinicalNotes, this, evt);
                    }else
                        popmenu = new resultEditor.annotationBuilder.Popmenu(textPaneforClinicalNotes, this, evt);

                    String thismarkablename = env.Parameters.currentMarkables_to_createAnnotation_by1Click;
                    popmenu.oneClicktoCreateAnnotation( thismarkablename );
                } 
                
                return;
            }

            // #============================================================#
            // ##3## build complex relationships between annotaions
            // This occurs when Relationship button is toggled and a right
            // click occurs. This allows users to add Annotations to a
            // relationship and create new relationships.
            // #============================================================#
            else if( (clicks == 1 ) && WorkSet.makingRelationships && rightClick)
            {
                //Simply return if no annotation is currently selected
                if(WorkSet.currentAnnotation == null)
                    return;

                addRelationship(position);

                // reset the flag everytime after adding a relationship
                // WorkSet.makingRelationships = false;
                
                return;
            }


            resultEditor.annotations.Depot.SelectedAnnotationSet.selectAnnotations_ByPosition(
                    textsourceFilename, position, true
            );
            // ##2## left key to get a current position and show found annotations
            // in this position

            // Record for current workset: latest position in text panel
            resultEditor.workSpace.WorkSet.latestClickPosition = position;


            // if no annotation caught
            if (( selectedAnnotaions == null )||(selectedAnnotaions.size()<1)){
                // disable all operation buttons
                // if these is no annotation catched by current cursor position
                WorkSet.currentAnnotation = null;
                disableAnnotationDisplay();
                disable_AnnotationEditButtons();
                
                ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();               
                ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                        
                return;
            }
            

            // if got result in this cursor position
            int amount = selectedAnnotaions.size();
            if ( amount > 0)
            {
                if( reviewmode == GUI.ReviewMode.adjudicationMode ){
                    //jPanel60.setVisible( true );
                    DiffCounter diffposition = new DiffCounter( jLabel23, WorkSet.getCurrentFile().getName(), this);                     
                    //int cursorPosition = textPaneforClinicalNotes.( evt.getPoint() );
                    diffposition.setSelected( position );
                
                }
                    
                // record mouse position
                resultEditor.workSpace.WorkSet.latestValidClickPosition = position;

                
                // show found annotations in list
                showSelectedAnnotations_inList(0);

                // if multiple annotations got selected, we will use diff panel
                // to manage them
                if( amount == 1 )
                {
                    
                    //if (reviewmode == GUI.ReviewMode.ANNOTATION_MODE) {
                        
                        
                        //If same annotation Clicked open Attribute Editor
                        if ((!rightClick)&&(previousAnnotation != null) && (selectedAnnotaions != null))//
                        {
                            boolean contained = false;
                            for (Annotation anno : selectedAnnotaions) {
                                if (anno == null) {
                                    continue;
                                }
                                
                                if (anno.isDuplicate(previousAnnotation, WorkSet.getCurrentFile().getName())) {
                                    
                                    if ((attributeAnnotation != null) && (attributeAnnotation.isDuplicate(previousAnnotation, WorkSet.getCurrentFile().getName()))) {
                                        attributeAnnotation = null;
                                        contained = false;
                                        break;
                                    }else{
                                        attributeAnnotation = previousAnnotation;
                                        contained = true;
                                        break;
                                    }
                                }
                            }
                            
                            

                            if (contained && env.Parameters.enabled_displayAttributeEditor) {
                                     //&& (previousAnnotation.attributes != null)
                                    //&& (previousAnnotation.attributes.size()>0) ) {
                                openAttributeEditor( );
                                return;
                            }
                        }


                    if ( reviewmode == GUI.ReviewMode.adjudicationMode ){
                        
                        boolean showbutton = false;
                        if( checkAnnotator_ADJUDICATION( selectedAnnotaions ) )
                            showbutton = false;
                        else
                            showbutton = true;
                        
                        this.setVisible_EditorAdjuducation( showbutton );
                        
                        if( showbutton ){
                            resultEditor.annotations.Depot.setSingleAnnotation(
                                   selectedAnnotaions.get(0) );
                        }
                        
                        this.display_relationshipPath_setPath(selectedAnnotaions.get(0));
                        
                    }
                } 
                else if( amount >1 ) 
                {
                    // set the diff button to visible
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusVisible();
                        
                    // if these annotations are same
                    //if( isAllSelectedAnnotationSame() ){
                        // right diff panel available if flag is true
                        //((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                        // start diff if flag is true
                        ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();

                        // show all annotations
                        // showAnnotatorsOfAllSelectedAnnotations();
                    //}
                    //else
                    //{                        
                        // start diff if flag is true
                        ((userInterface.annotationCompare.ExpandButton)jPanel60).startDiff();
                    //}


                }else{
                    // right diff panel available if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                }

                enable_AnnotationEditButtons();


                //Check for right click
                if(evt.getModifiers()== java.awt.event.InputEvent.BUTTON3_MASK){
                    Annotation toPopFor = WorkSet.currentAnnotation;
                    resultEditor.PopUp.rightClickOnAnnotPopUp annotPopUp = new resultEditor.PopUp.rightClickOnAnnotPopUp(
                    this.textPaneforClinicalNotes, selectedAnnotaions, this);
                    annotPopUp.pop(toPopFor,evt.getX(), evt.getY());
                }

            } 
            else // if no annotation is selected 
            {
                // set the diff button to visible
                    ((userInterface.annotationCompare.ExpandButton) jPanel60).setStatusVisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton) jPanel60).startDiff();
                    
                // right diff panel available if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                // disable all operation buttons
                // if these is no annotation catched by current cursor position
                disableAnnotationDisplay();
                disable_AnnotationEditButtons();                                
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1106081501::" + ex.getMessage());
            ex.printStackTrace();
        }

    }
    
    
    public void diffjumper(int start, int end){

        try{
            ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
            
            resultEditor.customComponents.ExpandablePanel_editor extension = ((resultEditor.customComponents.ExpandablePanel_editor)NavigationPanel_editor);
            
            if(!extension.isExtended())
                extension.afterMousePressed();
                
            if(( !this.isEnabled() ) || (!this.isVisible()) || (!this.isActive()) )
                return;
            

            //textPaneforClinicalNotes.
            
            // let the position to catch your eyes
            int cursorPosition = start; // + 20;
            if(( this.textPaneforClinicalNotes.getDocument() != null ) 
                && ( cursorPosition >= this.textPaneforClinicalNotes.getDocument().getLength() ) )
                cursorPosition = this.textPaneforClinicalNotes.getDocument().getLength(); 
            
            //if(( this.textPaneforClinicalNotes.getDocument() != null ) 
            //    && ( cursorPosition + 40 < this.textPaneforClinicalNotes.getDocument().getLength() ) )
            //    cursorPosition = cursorPosition + 40; 
          
            textPaneforClinicalNotes.setCaretPosition( cursorPosition );
            System.out.println("focus to [" + cursorPosition + "] of current document");
            //if(( start == -1) && (end==-1)){
            //}


            // cancel any possible flag of "create new dis-joint span"
            // after any right click.
          
                setFlag_allowToAddSpan(false);
            

            // ---------------------------------------------- //
            

            // #### 0.2 ####  get filename of current document
            String textsourceFilename = resultEditor.workSpace.WorkSet.
                    getCurrentFile().getName();

           
            
            // #### 0.3 #### try to use current mouse carpet position to select annotations
            ArrayList<Annotation> selectedAnnotaions =
                resultEditor.annotations.Depot.SelectedAnnotationSet.
                    selectAnnotations_ByPosition(
                        textsourceFilename, 
                        start+1, 
                        false
                    );





            resultEditor.annotations.Depot.SelectedAnnotationSet.selectAnnotations_ByPosition(
                    textsourceFilename, start+1, true
            );
            // ##2## left key to get a current position and show found annotations
            // in this position

            // Record for current workset: latest position in text panel
            resultEditor.workSpace.WorkSet.latestClickPosition = start+1;


            // if no annotation caught
            if (( selectedAnnotaions == null )||(selectedAnnotaions.size()<1)){
                // disable all operation buttons
                // if these is no annotation catched by current cursor position
                WorkSet.currentAnnotation = null;
                disableAnnotationDisplay();
                disable_AnnotationEditButtons();
                
                ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();               
                ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                        
                return;
            }
            

            // if got result in this cursor position
            int amount = selectedAnnotaions.size();
            if ( amount > 0)
            {
                // record mouse position
                resultEditor.workSpace.WorkSet.latestValidClickPosition = start+1;

                
                // show found annotations in list
                showSelectedAnnotations_inList(0);

                // if multiple annotations got selected, we will use diff panel
                // to manage them
                if( amount == 1 )
                {
                    
                    if ( reviewmode == GUI.ReviewMode.adjudicationMode ){
                        
                        boolean showbutton = false;
                        if( checkAnnotator_ADJUDICATION( selectedAnnotaions ) )
                            showbutton = false;
                        else
                            showbutton = true;
                        
                        this.setVisible_EditorAdjuducation( showbutton );
                        
                        if( showbutton ){
                            resultEditor.annotations.Depot.setSingleAnnotation(
                                   selectedAnnotaions.get(0) );
                        }
                    }
                } 
                else if( amount >1 ) 
                {
                    // set the diff button to visible
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusVisible();
                        
                    // if these annotations are same
                    //if( isAllSelectedAnnotationSame() ){
                        // right diff panel available if flag is true
                        //((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                        // start diff if flag is true
                        ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();

                        // show all annotations
                        // showAnnotatorsOfAllSelectedAnnotations();
                    //}
                    //else
                    //{                        
                        // start diff if flag is true
                        ((userInterface.annotationCompare.ExpandButton)jPanel60).startDiff();
                    //}


                }else{
                    // right diff panel available if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                }

                enable_AnnotationEditButtons();


                
            } 
            else // if no annotation is selected 
            {
                // set the diff button to visible
                    ((userInterface.annotationCompare.ExpandButton) jPanel60).setStatusVisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton) jPanel60).startDiff();
                    
                // right diff panel available if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).setStatusInvisible();
                    // start diff if flag is true
                    ((userInterface.annotationCompare.ExpandButton)jPanel60).noDiff();
                // disable all operation buttons
                // if these is no annotation catched by current cursor position
                disableAnnotationDisplay();
                disable_AnnotationEditButtons();                                
            }

            resultEditor.underlinePainting.SelectionHighlighter painter = new
                    resultEditor.underlinePainting.SelectionHighlighter(this.textPaneforClinicalNotes);
            painter.RemoveAllUnderlineHighlight();
            painter.addNewUnderlineHightlight(start, end);
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1106081501xx::" + ex.getMessage());
            ex.printStackTrace();
        }

    }
    
    /**refresh part of screen after adding a new annotation in adjudication mode, it should be call by the class of "popmenu.java" after adding annotation*/
    public void refresh_adjudication(){
          ((userInterface.annotationCompare.ExpandButton)jPanel60).refresh_addingAnnotation();
    }
    
    /**
     * called when mouse clicked on overlapping label
     * @param evt
     */
    private void overlappingMouseClicked(MouseEvent evt) {
        setToSpanConflict();
    }

    /**
     * Refresh Annotation information tab
     * @param evt - button pressed
     */
    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {
        refreshInfo();
    }

    /**event: mouse clicked on class tree view. Only can called by system listener.*/
    private void userMouseClicked_onClassTreeView(java.awt.event.MouseEvent evt){
        resultEditor.annotationClasses.mouse.ClickedListener clicklistener =
                new resultEditor.annotationClasses.mouse.ClickedListener( evt, jTree_class, this, textPaneforClinicalNotes );
        clicklistener.process();
    }

    
    /**if the given vector only contains one annotation and its adjudication 
     * status is "Annotation.AdjudicationStatus.MATCHES_OK", then return true, 
     * otherwise return false;
     * 
     * Hint: 
     * Annotator maybe be showed as "ADJUDICATION" while its adjudication 
     * status is "Annotation.AdjudicationStatus.MATCHES_OK"
     */
    private boolean checkAnnotator_ADJUDICATION(ArrayList<Annotation> selectedAnnotaions){
        
        final String annotator = "ADJUDICATION";
        
        if( ( selectedAnnotaions == null ) || (selectedAnnotaions.size() != 1) )
            return false;
        
        Annotation annotation = selectedAnnotaions.get(0);        
        if ( annotation == null )
            return false;                
                
        if ( annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK )
            return true;                
        else 
            return false;
        
    }
    
    public void display_refreshNAV_WorkSpace()
    {
        
        // show path of current workspace
        String workspacepath = env.Parameters.WorkSpace.WorkSpace_AbsolutelyPath;
        if (workspacepath == null)
        {
            //jLabel_currentworkspace_abspath.setText("<html><b>NO SETTING</b> </html>");
             workSpace.switcher.RecentWorkSpace rw =
                    new  workSpace.switcher.RecentWorkSpace(jComboBox_currentworkspace_abspath);
            rw.emptyCombobox();
                           
        }
        else
        {
             workSpace.switcher.RecentWorkSpace rw =
                    new  workSpace.switcher.RecentWorkSpace(jComboBox_currentworkspace_abspath);
            rw.addWorkspace(new File(workspacepath));
            
            if(RecentWorkSpace.getItemCount()!=jComboBox_currentworkspace_abspath.getItemCount())
                rw.updateWorkspaces_inCombobox();

            //jComboBox1.removeAllItems();
        }

            //jComboBox_currentworkspace_abspath.setSelectedItem("<html><b>NO SETTING</b> </html>");
            

        // list all projects under current workspace
        navigatorContainer.ListProjects lp = new  navigatorContainer.
                ListProjects( this, jList_NAV_projects, workspacepath );
        lp.showProjectsInList();

        this.jList_NAV_projects.updateUI();

    }
    //</editor-fold>

    
    /**The method is gonna to update your GUI component and some values by
     * the parameters that you got from projectschema.cfg of current project.
     *
     * It update several status buttons on screen by these project parameters. 
     * There are several status buttons on the status bar at the button of
     * eHOST window, such as Oracle status button, Diff status button, etc.
     */
    public void updateGUI_byProjectParameters()
    {
        setFlag_of_OracleSimilarPhraseSeeker(env.Parameters.oracleFunctionEnabled);
        //setFlag_of_GraphPath_Display(env.Parameters.enabled_GraphPath_Display );
        setFlag_of_DifferenceMatching_Display(env.Parameters.enabled_Diff_Display );
        
    }

    /**Enable or disable functions by showing or hiding buttons on the tools bar.*/
    private void enableFunctionsByMask(){
        if((env.Parameters.Sysini.functions==null)||(env.Parameters.Sysini.functions.length!=6))
        {
            env.Parameters.Sysini.functions = new char[6];
            for(int i=0;i<6; i++){
                env.Parameters.Sysini.functions[i]=0;
            }
            env.Parameters.Sysini.functions[1]=1;
            env.Parameters.Sysini.functions[5]=1;            
        }
        
        if(env.Parameters.Sysini.functions[0]=='1')
            jToggleButton_ResultEditor.setVisible(true);
        else
            jToggleButton_ResultEditor.setVisible(false);

        if(env.Parameters.Sysini.functions[1]=='1')
            jToggleButton_CreateAnnotaion.setVisible(true);
        else
            jToggleButton_CreateAnnotaion.setVisible(false);

        // following statements are used to enable or disable
        // the function of PIN extractor
        if(env.Parameters.Sysini.functions[2]=='1')
            jToggleButton_PinExtractor.setVisible(true);
        else
            jToggleButton_PinExtractor.setVisible(false);

        if(env.Parameters.Sysini.functions[3]=='1')
            jToggleButton_DictionaryManager.setVisible(true);
        else
            jToggleButton_DictionaryManager.setVisible(false);

        //if(env.Parameters.Sysini.functions[4]=='1')
        //    jToggleButton_Converter.setVisible(true);
        //else
            jToggleButton_Converter.setVisible(false);

        if(env.Parameters.Sysini.functions[5]=='1')
            jToggleButton_DictionarySetting.setVisible(true);
        else
            jToggleButton_DictionarySetting.setVisible(false);

        // hide the button of pre-pairing functions
        this.jButton_save1.setVisible(false);
    }


    /**DATA format used for indicator "REVIEWMODE", which will be used to tell
     * system whether we are in annotation mode, or in consensus mode.
     */
    public enum ReviewMode{
        ANNOTATION_MODE,
        adjudicationMode,   
        OTHERS   // eHOST maybe in other function model
    };

    /**indicator that used to tell system whether we are in annotation mode, or
     * in consensus mode.
     */
    public static ReviewMode reviewmode = ReviewMode.ANNOTATION_MODE;

    /**the dialog of adjudication mode*/
    adjudication.Adjudication dialog_adjudication = null;

    /**Tell system which review mode eHOST will be working in, are set the button
     * status. There are two review modes: annotation mode, and consensus
     * mode.
     */
    public void setReviewMode(ReviewMode thismode) {

        reviewmode = thismode; // set current review mode

        // by given current review mode, select matched togglebutton
        switch (thismode) {
            
            // annotation mode
            case ANNOTATION_MODE:
                
                env.Parameters.enabled_Diff_Display = true;
                
                jRadioButton_annotationMode.setSelected(true);
                jRadioButton_adjudicationMode.setSelected(false);
                jRadioButton_annotationMode.setVisible(true);
                jRadioButton_adjudicationMode.setVisible(true);     
                jButton9.setVisible( false );
                
                //if(  )
                    //jPanel60.setVisible( env.Parameters.EnableDiffButton );
                
                setComponentsVisibleForConsensusMode(true); // show top function buttons
                
                this.display_adjudication_setOperationBarStatus( this.HIDEN );
                
                break;

            // consensus mode
            case adjudicationMode:
                
                //jPanel60.setVisible( true );
                this.display_adjudication_setOperationBarStatus( this.DISABLED );
                
                env.Parameters.enabled_Diff_Display = true; // enable diff flag to show diff
                display_repaintHighlighter();

                jRadioButton_annotationMode.setVisible(true);
                jRadioButton_adjudicationMode.setVisible(true);
                jRadioButton_annotationMode.setSelected(false);
                jRadioButton_adjudicationMode.setSelected(true);
                jButton9.setVisible( true );

                
                setComponentsVisibleForConsensusMode(false); // hide buttons which are not related to consensus mode                
                this.repaint();

                Paras.__adjudicated = adjudication.data.AdjudicationDepot.isReady();
                    
                // let user select what they want
                if ( ( Paras.__adjudicated ) && ( Paras.isReadyForAdjudication() )) {
                    Object[] options = {"Yes, please", "No, Start a new adjudication", "Cancel"};
                    int i = JOptionPane.showOptionDialog( 
                            this, 
                            "Would you like to continue your previous adjudication work?", 
                            "yes", 
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options, 
                            options[0]);
                    
                    // ---- 2.1 ----
                    if( i == 0 ){ // yes, continue latest adjudication work
                        mode_continuePreviousAdjudicationWork();
                        return;
                    }else if ( i == 2) { // cancel
                        // ---- 2.2 ----
                        mode_enterAnnotationMode( true );
                    }
                    
                    if( i != 1 )
                        break;
                }
                
                // ---- 2.3 ----
                // enable the dialog to let user select conditions and enter the 
                // adjudication mode
                
                // enable the dialog to ask user setting what is difference
                if (dialog_adjudication == null) {

                    dialog_adjudication = new adjudication.Adjudication(this);
                    dialog_adjudication.setVisible(true);
                } else {
                    dialog_adjudication.dispose();
                    dialog_adjudication = new adjudication.Adjudication(this);
                    dialog_adjudication.setVisible(true);
                }
                 
                 /**/
                break;
                
            case OTHERS:
                //jPanel60.setVisible( false );
                jRadioButton_annotationMode.setSelected(true);
                jRadioButton_adjudicationMode.setSelected(false);
                jRadioButton_annotationMode.setVisible(false);
                jRadioButton_adjudicationMode.setVisible(false);
                jButton9.setVisible( false );

                setComponentsVisibleForConsensusMode(true); // show top function buttons while out of consense mode
                break;
        }
    }

    
    /**go back to previous adjudication work as user's request.*/
    private void mode_continuePreviousAdjudicationWork(){
        try{
            adjudication.Adjudication dialog_adjudication = new adjudication.Adjudication(this);        
            dialog_adjudication.checkAnnotations( false );
            
            this.display_adjudication_setOperationBarStatus( this.ENABLED );
            resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel sct =
                new resultEditor.annotationClasses.navigationTree.UpDateNavigationPanel(jTree_class, this);
               

        sct.refreshTypeMemory_refresh();        
        sct.refreshTypeMemory_refresh(true);
        sct.display();
            
            jRadioButton_annotationMode.setSelected(false);
            jRadioButton_annotationMode.setVisible(true);
            jRadioButton_annotationMode.setEnabled(true);
            
            jRadioButton_adjudicationMode.setSelected(true);
            jRadioButton_adjudicationMode.setVisible(true);            
            jRadioButton_adjudicationMode.setEnabled(true);
                
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**set components, such as buttons, visible or invisible for the consensus
     * review mode.
     *
     * @param   isVisible
     *          false: hide non-relavent components for consensus review mode;
     *          true: set these components visible to user.
     */
    private void setComponentsVisibleForConsensusMode(boolean isVisible){
        
        //jToggle_AssignmentsScreen.setVisible( isVisible );
        
        if(env.Parameters.Sysini.functions[1]=='1')
            jToggleButton_CreateAnnotaion.setVisible( isVisible );
        else jToggleButton_CreateAnnotaion.setVisible( false );

        if(env.Parameters.Sysini.functions[2]=='1')
            jToggleButton_PinExtractor.setVisible( isVisible );
        else jToggleButton_PinExtractor.setVisible( false );

        if(env.Parameters.Sysini.functions[3]=='1')
            jToggleButton_DictionaryManager.setVisible( isVisible );
        else jToggleButton_DictionaryManager.setVisible( false );
        
        if(env.Parameters.Sysini.functions[5]=='1')
            jToggleButton_DictionarySetting.setVisible( isVisible );
        else jToggleButton_DictionarySetting.setVisible( false );
        
        jToggle_AssignmentsScreen.setVisible( isVisible );
       
        jToggleButton_Converter.setVisible( false );

        // display or hide the button of "import annotations"
        jButton_importAnnotations.setVisible( isVisible );

        // display or hide the button of "remove all current annotations from memory"
        jButton_removeAllAnnotations.setVisible( isVisible );

        // display or hide the separator after above two buttons
        //jLabel_separator02_onViewer.setVisible( isVisible );

        jLabel_infobar_FlagOfDiff.setVisible( isVisible );
        jLabel_infobar_FlagOfOracle.setVisible(isVisible);
        
        //jLabel11.setVisible(isVisible);
        
        
        jButton20.setVisible( isVisible );
        
    }

    /**while we left the tab of editor, we hide all status buttons and icons
     * that only released to editor.
     */
    public void hideStatusButtons(){
        this.jPanel3.setVisible(false);
        this.jPanel1.setVisible(false);
    }

    /**while we enter the tab of editor, we display all status buttons and icons
     * that released to editor.
     */
    public void showStatusButtons(){
        this.jPanel3.setVisible(true);
        this.jPanel1.setVisible(true);
    }
    
    /**return document content in current document viewer*/
    public javax.swing.text.Document getDoc(){
        return textPaneforClinicalNotes.getDocument();
    }
    
    public int setEditorDividedLocation(){
        return jSplitPane5.getDividerLocation();
    }
}



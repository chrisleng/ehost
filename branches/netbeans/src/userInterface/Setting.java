/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Preference.java
 *
 * Created on Mar 31, 2010, 10:13:40 PM
 */
package userInterface;

import commons.Tools;
import dictionaries.exportOrImportVerifier;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import preAnnotate.ExtensionFileFilter;

/**
 *
 * @author Leng
 */
public class Setting extends javax.swing.JFrame implements PropertyChangeListener {

    /**
     * flag of new or edit a regular expression
     */
    private final static int NEW = 1;
    private final static int EDIT = 2;
    private static int FLAG_WORKTYPE = NEW;

    protected userInterface.GUI __gui;
    protected userInterface.addNewDict.WizardConceptLib conceptlibDialog;

    /**
     * Creates new form Preference
     */
    public Setting(int _parentX, int _parentY, int _parentWidth, int _parentHeight, userInterface.GUI _gui) {

        //try {
            //JFrame.setDefaultLookAndFeelDecorated(true);
            //JDialog.setDefaultLookAndFeelDecorated(true);

        //} catch (Exception ex) {
        //    ex.printStackTrace();
        //}

        __gui = _gui;
        initComponents();
        jToggleButton4.setVisible(false);

        // read configure information from project file or configure file
        // so we can find dictionaries of pre-annotated concepts        
        config.system.SysConf.loadSystemConfigure();

        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.loadConfigure();

        

        // load all found dictionaries into the list on the screen
        Show_dictionaries_on_list();
        Show_verified_dictionaries_on_list();

        // set checked status of radio buttons of weight for different
        if (env.Parameters.Pre_Defined_Dictionary_DifferentWeight == true) {
            jRadioButton_diifferentWeight.setSelected(true);
        } else {
            jRadioButton_sameWeight.setSelected(true);
        }

        jButton_down1.setVisible(false);
        jButton_removeRegularExpression.setEnabled(false);
        jButton_up1.setVisible(false);

        // set selection status to check box of enable/disable stop words
        jCheckBox_stopwords.setSelected( env.Parameters.nlp_dictionary_proc.using_StopWords );

        openCard("Others");
        //set initilization value of components on this card by values of flags
        component_setComponent_forCardOther();
        
        // set the screen location of this dialog 
        int width = 952, height = 560;
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        // set winodw not resiable
        setResizable(false);
        // set the location of this dialog        
        int x = _parentX + (int) ((-width + _parentWidth) / 2);
        int y = _parentY + (int) ((-height + _parentHeight) / 2);        
        System.out.println("[INFO] Location of new Configuration window ["+x +", " + y +"]");         
        this.__gui.setEnabled(false);
        x=100;y=100;
        this.setLocation(x, y);
    }

    /**
     * read
     */
    public static void Show_verified_dictionaries_on_list() {
        /**
         * @para filename - file name with absolute path
         * @para weight - if same all will be 0, otherwise big number means
         * heavy weight
         * @para description
         * @para separator
         * @para number_of_valid_entries
         */
        // clean the list before using
        jList_preAnnotated_concept_dictionaries1.removeAll();
        Vector b = new Vector();
        b.clear();
        jList_preAnnotated_concept_dictionaries1.setListData(b);

        // amount of dictionaries
        int amount = env.Parameters.VERIFIER_DICTIONARIES.size();

        //String[] words;
        if (amount > 0) {
            String[] worda = new String[amount];
            for (int i = 0; i < amount; i++) {
                dictionaries.VerifierDictionaryFormat a = env.Parameters.VERIFIER_DICTIONARIES.get(i);
                worda[i] = a.getFileName();
            }
            jList_preAnnotated_concept_dictionaries1.setListData(worda);
        } else {
            Vector a = new Vector();
            a.clear();
            jList_preAnnotated_concept_dictionaries1.setListData(a);
        }

        jButton_add1.setEnabled(true);
        jButton_remove1.setEnabled(false);
        jButton_up1.setEnabled(false);
        jButton_down1.setEnabled(false);

    }

    /**
     * read
     */
    public static void Show_dictionaries_on_list() {
        /**
         * @para filename - file name with absolute path
         * @para weight - if same all will be 0, otherwise big number means
         * heavy weight
         * @para description
         * @para separator
         * @para number_of_valid_entries
         */
        // clean the list before using
        jList_preAnnotated_concept_dictionaries.removeAll();
        Vector b = new Vector();
        b.clear();
        jList_preAnnotated_concept_dictionaries.setListData(b);

        // amount of dictionaries
        int amount = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();

        //String[] words;
        if (amount > 0) {
            String[] worda = new String[amount];
            for (int i = 0; i < amount; i++) {
                Object[] a = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                if ((env.Parameters.Pre_Defined_Dictionary_DifferentWeight) && (i == 0)) {
                    worda[i] = a[1].toString().trim() + "  -  " + a[0].toString().trim() + "  --  [Default Dictionaries]";
                } else {
                    worda[i] = a[1].toString().trim() + "  -  " + a[0].toString().trim();
                }
            }
            jList_preAnnotated_concept_dictionaries.setListData(worda);
        } else {
            Vector a = new Vector();
            a.clear();
            jList_preAnnotated_concept_dictionaries.setListData(a);
        }

        jButton_add.setEnabled(true);
        jButton_remove.setEnabled(false);
        jButton_up.setEnabled(false);
        jButton_down.setEnabled(false);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup_conceptdictionaries_method = new javax.swing.ButtonGroup();
        jFileChooser1 = new javax.swing.JFileChooser();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel_topbar = new javax.swing.JPanel();
        jLabel_background_pic3 = new javax.swing.JLabel();
        jPanel_bar = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jPanel_containor = new javax.swing.JPanel();
        jPanel_conceptDictionaries = new javax.swing.JPanel();
        jPanel_dictionarylist_and_coordination_radio_button = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_preAnnotated_concept_dictionaries = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jPanel_title_dictionaries_list = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jButton_add = new javax.swing.JButton();
        jButton_remove = new javax.swing.JButton();
        jButton_up = new javax.swing.JButton();
        jButton_down = new javax.swing.JButton();
        jButton_InitDict = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel_coordination = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jRadioButton_sameWeight = new javax.swing.JRadioButton();
        jRadioButton_diifferentWeight = new javax.swing.JRadioButton();
        jCheckBox_stopwords = new javax.swing.JCheckBox();
        jPanel_title_dictionaries_list1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jPanel_title_dictionaries_list2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel_separator = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel_CustomRegularExpression = new javax.swing.JPanel();
        jPanel_listOfRegularExpression = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_RegularExpression = new javax.swing.JList();
        jPanel10 = new javax.swing.JPanel();
        jPanel_title_dictionaries_list3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButton_add1 = new javax.swing.JButton();
        jButton_removeRegularExpression = new javax.swing.JButton();
        jButton_up1 = new javax.swing.JButton();
        jButton_down1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel_SubContainer = new javax.swing.JPanel();
        jPanel_detailsOfCustomRegularExpression = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField_RegularExpression = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField_ClassName = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField_Comment = new javax.swing.JTextField();
        jButton_add2 = new javax.swing.JButton();
        jPanel_VerifyRegularExpression = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel_dictionarylist_and_coordination_radio_button1 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList_preAnnotated_concept_dictionaries1 = new javax.swing.JList();
        jPanel_title_dictionaries_list4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jButton_add3 = new javax.swing.JButton();
        jButton_remove1 = new javax.swing.JButton();
        jButton_Edit = new javax.swing.JButton();
        jButton_Edit1 = new javax.swing.JButton();
        jButton_Edit2 = new javax.swing.JButton();
        jButton_Edit3 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel_others = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel42 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jRadioButton_newannotationspan_exact = new javax.swing.JRadioButton();
        jPanel31 = new javax.swing.JPanel();
        jRadioButton_newannotationspan_check = new javax.swing.JRadioButton();
        jPanel35 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jRadioButton_oracle_allmatches = new javax.swing.JRadioButton();
        jPanel34 = new javax.swing.JPanel();
        jRadioButton_oracle_wholeword = new javax.swing.JRadioButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jCheckBox_exportSuggestionsToXML1 = new javax.swing.JCheckBox();
        jPanel_bottom = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preferences");
        setResizable(false);
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
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jPanel_topbar.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_topbar.setLayout(new java.awt.BorderLayout());

        jLabel_background_pic3.setBackground(new java.awt.Color(226, 226, 226));
        jLabel_background_pic3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/bar.png"))); // NOI18N
        jPanel_topbar.add(jLabel_background_pic3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel_topbar, java.awt.BorderLayout.NORTH);

        jPanel_bar.setBackground(new java.awt.Color(200, 200, 200));
        jPanel_bar.setLayout(new java.awt.BorderLayout());

        jToolBar1.setBackground(new java.awt.Color(200, 200, 200));
        jToolBar1.setFloatable(false);
        //jToolBar1.setLayout(new javax.swing.BoxLayout(jToolBar1, javax.swing.BoxLayout.PAGE_AXIS));
        jToolBar1.setLayout(new java.awt.GridLayout(5,1));
        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);
        jToolBar1.setFont(new java.awt.Font("Calibri", 0, 15)); // NOI18N

        jToggleButton5.setBackground(new java.awt.Color(200, 200, 200));
        buttonGroup1.add(jToggleButton5);
        jToggleButton5.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton5.setSelected(true);
        jToggleButton5.setText("<html>Basic<p>Setting</html>");
        jToggleButton5.setToolTipText("");
        jToggleButton5.setFocusable(false);
        jToggleButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton5);

        jToggleButton3.setBackground(new java.awt.Color(200, 200, 200));
        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setLabel("<html>Concept<br>Dictionaries</html>");
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton3);

        jToggleButton2.setBackground(new java.awt.Color(200, 200, 200));
        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setLabel("<html>Custom<br> Regular<br>Expressions</html>");
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        jToggleButton4.setBackground(new java.awt.Color(200, 200, 200));
        buttonGroup1.add(jToggleButton4);
        jToggleButton4.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/annotator.png"))); // NOI18N
        jToggleButton4.setText("<html>Verifier<br> Dictionaries</html>");
        jToggleButton4.setFocusable(false);
        jToggleButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton4);

        jPanel_bar.add(jToolBar1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel_bar, java.awt.BorderLayout.WEST);

        jPanel_containor.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_containor.setLayout(new java.awt.CardLayout());

        jPanel_conceptDictionaries.setOpaque(false);
        jPanel_conceptDictionaries.setLayout(new java.awt.BorderLayout());

        jPanel_dictionarylist_and_coordination_radio_button.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 4));
        jPanel_dictionarylist_and_coordination_radio_button.setFont(new java.awt.Font("Lucida Handwriting", 0, 12)); // NOI18N
        jPanel_dictionarylist_and_coordination_radio_button.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(226, 226, 226));
        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jList_preAnnotated_concept_dictionaries.setBackground(new java.awt.Color(240, 240, 255));
        jList_preAnnotated_concept_dictionaries.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 2));
        jList_preAnnotated_concept_dictionaries.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        jList_preAnnotated_concept_dictionaries.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Empty List" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_preAnnotated_concept_dictionaries.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_preAnnotated_concept_dictionaries.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_preAnnotated_concept_dictionariesMouseClicked(evt);
            }
        });
        jList_preAnnotated_concept_dictionaries.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_preAnnotated_concept_dictionariesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_preAnnotated_concept_dictionaries);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel6.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel_title_dictionaries_list.setBackground(new java.awt.Color(83, 24, 82));
        jPanel_title_dictionaries_list.setForeground(new java.awt.Color(255, 255, 255));
        jPanel_title_dictionaries_list.setPreferredSize(new java.awt.Dimension(233, 26));
        jPanel_title_dictionaries_list.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(83, 24, 82));
        jLabel2.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("> Pre-Annotated Concept Dictionaries");
        jPanel_title_dictionaries_list.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel8.setBackground(new java.awt.Color(226, 226, 226));
        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel8.setMinimumSize(new java.awt.Dimension(100, 5));
        jPanel8.setPreferredSize(new java.awt.Dimension(100, 5));

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 855, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 5, Short.MAX_VALUE)
        );

        jPanel_title_dictionaries_list.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel6.add(jPanel_title_dictionaries_list, java.awt.BorderLayout.NORTH);

        jPanel4.setBackground(new java.awt.Color(226, 226, 226));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));

        jPanel19.setBackground(new java.awt.Color(226, 226, 226));
        jPanel19.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jButton_add.setBackground(new java.awt.Color(226, 226, 226));
        jButton_add.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton_add.setText("Add");
        jButton_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addActionPerformed(evt);
            }
        });
        jPanel19.add(jButton_add);

        jButton_remove.setBackground(new java.awt.Color(226, 226, 226));
        jButton_remove.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton_remove.setText("Remove");
        jButton_remove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_removeActionPerformed(evt);
            }
        });
        jPanel19.add(jButton_remove);

        jButton_up.setBackground(new java.awt.Color(226, 226, 226));
        jButton_up.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton_up.setText("Move Up");
        jButton_up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_upActionPerformed(evt);
            }
        });
        jPanel19.add(jButton_up);

        jButton_down.setBackground(new java.awt.Color(226, 226, 226));
        jButton_down.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton_down.setText("Move Down");
        jButton_down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_downActionPerformed(evt);
            }
        });
        jPanel19.add(jButton_down);

        jButton_InitDict.setBackground(new java.awt.Color(226, 226, 226));
        jButton_InitDict.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton_InitDict.setText("INIT");
        jButton_InitDict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_InitDictActionPerformed(evt);
            }
        });
        jPanel19.add(jButton_InitDict);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(251, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jLabel1.setBackground(new java.awt.Color(204, 204, 204));
        jLabel1.setFont(new java.awt.Font("Calibri", 1, 8)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" ");
        jPanel6.add(jLabel1, java.awt.BorderLayout.LINE_END);

        jPanel_dictionarylist_and_coordination_radio_button.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel_conceptDictionaries.add(jPanel_dictionarylist_and_coordination_radio_button, java.awt.BorderLayout.PAGE_START);

        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel_coordination.setBackground(new java.awt.Color(226, 226, 226));
        jPanel_coordination.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));
        jPanel_coordination.setFont(new java.awt.Font("Handwriting - Dakota", 2, 13)); // NOI18N
        jPanel_coordination.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(226, 226, 226));

        jRadioButton_sameWeight.setBackground(new java.awt.Color(226, 226, 226));
        buttonGroup_conceptdictionaries_method.add(jRadioButton_sameWeight);
        jRadioButton_sameWeight.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_sameWeight.setSelected(true);
        jRadioButton_sameWeight.setText("<html>Treat above dictionaries as same pre-annotated<br>concept source</html>");
        jRadioButton_sameWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_sameWeightActionPerformed(evt);
            }
        });

        jRadioButton_diifferentWeight.setBackground(new java.awt.Color(226, 226, 226));
        buttonGroup_conceptdictionaries_method.add(jRadioButton_diifferentWeight);
        jRadioButton_diifferentWeight.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_diifferentWeight.setText("<html>Dictionaries have different weight<br>(If dictionaries find overlapping concepts,<br> the dictionary with the lowest weight will be<br> chosen over other dictionaries).</html>");
        jRadioButton_diifferentWeight.setActionCommand("<html>Dictionaries have different weight and<br> information found </html>");
        jRadioButton_diifferentWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_diifferentWeightActionPerformed(evt);
            }
        });

        jCheckBox_stopwords.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jCheckBox_stopwords.setText("Enable dictionary of stop words.");
        jCheckBox_stopwords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_stopwordsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioButton_sameWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jRadioButton_diifferentWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCheckBox_stopwords))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jRadioButton_sameWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jRadioButton_diifferentWeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 143, Short.MAX_VALUE)
                .add(jCheckBox_stopwords))
        );

        jPanel_coordination.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel_title_dictionaries_list1.setBackground(new java.awt.Color(83, 24, 82));
        jPanel_title_dictionaries_list1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel_title_dictionaries_list1.setMaximumSize(new java.awt.Dimension(2147483647, 237));
        jPanel_title_dictionaries_list1.setMinimumSize(new java.awt.Dimension(225, 23));
        jPanel_title_dictionaries_list1.setPreferredSize(new java.awt.Dimension(317, 23));
        jPanel_title_dictionaries_list1.setLayout(new java.awt.BorderLayout());

        jLabel3.setBackground(new java.awt.Color(83, 24, 82));
        jLabel3.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("> Multiple Dictionaries Coordination");
        jPanel_title_dictionaries_list1.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel_coordination.add(jPanel_title_dictionaries_list1, java.awt.BorderLayout.NORTH);

        jPanel20.add(jPanel_coordination, java.awt.BorderLayout.WEST);

        jPanel17.setBackground(new java.awt.Color(204, 204, 204));
        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));
        jPanel17.setMaximumSize(new java.awt.Dimension(2147483647, 23));
        jPanel17.setMinimumSize(new java.awt.Dimension(189, 23));
        jPanel17.setPreferredSize(new java.awt.Dimension(574, 23));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel_title_dictionaries_list2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel_title_dictionaries_list2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel_title_dictionaries_list2.setLayout(new java.awt.GridLayout(1, 0));

        jLabel4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel4.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("> Details of Current Dictionary");
        jLabel4.setMaximumSize(new java.awt.Dimension(185, 23));
        jLabel4.setMinimumSize(new java.awt.Dimension(185, 23));
        jLabel4.setPreferredSize(new java.awt.Dimension(185, 23));
        jPanel_title_dictionaries_list2.add(jLabel4);

        jPanel17.add(jPanel_title_dictionaries_list2, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBackground(new java.awt.Color(226, 226, 226));
        jPanel3.setVerifyInputWhenFocusTarget(false);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel16.setBackground(new java.awt.Color(226, 226, 226));
        jPanel16.setFocusable(false);
        jPanel16.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jPanel16.setRequestFocusEnabled(false);
        jPanel16.setVerifyInputWhenFocusTarget(false);
        jPanel16.setLayout(new java.awt.GridLayout(5, 1, 0, 2));

        jLabel5.setBackground(new java.awt.Color(226, 226, 226));
        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setText("<html><bold>Dictionary Name:</bold></html>");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel5.setFocusable(false);
        jLabel5.setPreferredSize(new java.awt.Dimension(200, 18));
        jLabel5.setVerifyInputWhenFocusTarget(false);
        jPanel16.add(jLabel5);

        jLabel6.setBackground(new java.awt.Color(226, 226, 226));
        jLabel6.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel6.setText("Dictionary Weight:");
        jPanel16.add(jLabel6);

        jLabel7.setBackground(new java.awt.Color(226, 226, 226));
        jLabel7.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel7.setText("Description:");
        jPanel16.add(jLabel7);

        jLabel_separator.setBackground(new java.awt.Color(226, 226, 226));
        jLabel_separator.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel_separator.setText("Separator:");
        jPanel16.add(jLabel_separator);

        jLabel9.setBackground(new java.awt.Color(226, 226, 226));
        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel9.setText("Valid Entries:");
        jPanel16.add(jLabel9);

        jPanel3.add(jPanel16, java.awt.BorderLayout.NORTH);

        jPanel17.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel20.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel_conceptDictionaries.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel_containor.add(jPanel_conceptDictionaries, "card2");

        jPanel_CustomRegularExpression.setLayout(new java.awt.BorderLayout());

        jPanel_listOfRegularExpression.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 4));
        jPanel_listOfRegularExpression.setFont(new java.awt.Font("Lucida Handwriting", 0, 12)); // NOI18N
        jPanel_listOfRegularExpression.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(226, 226, 226));
        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);

        jList_RegularExpression.setBackground(new java.awt.Color(240, 240, 255));
        jList_RegularExpression.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 2));
        jList_RegularExpression.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_RegularExpression.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Empty List" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_RegularExpression.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_RegularExpression.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_RegularExpressionMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList_RegularExpression);

        jPanel9.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel9.add(jPanel10, java.awt.BorderLayout.NORTH);

        jPanel_title_dictionaries_list3.setBackground(new java.awt.Color(83, 24, 82));
        jPanel_title_dictionaries_list3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel_title_dictionaries_list3.setPreferredSize(new java.awt.Dimension(233, 26));
        jPanel_title_dictionaries_list3.setLayout(new java.awt.BorderLayout());

        jLabel10.setBackground(new java.awt.Color(83, 24, 82));
        jLabel10.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("> Entries of Custom Regular Expression Lib - \"customre.lib\"");
        jPanel_title_dictionaries_list3.add(jLabel10, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(226, 226, 226));
        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel11.setMinimumSize(new java.awt.Dimension(100, 5));

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 855, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 5, Short.MAX_VALUE)
        );

        jPanel_title_dictionaries_list3.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel9.add(jPanel_title_dictionaries_list3, java.awt.BorderLayout.NORTH);

        jPanel12.setBackground(new java.awt.Color(226, 226, 226));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));

        jPanel21.setBackground(new java.awt.Color(226, 226, 226));
        jPanel21.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jButton_add1.setBackground(new java.awt.Color(226, 226, 226));
        jButton_add1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_add1.setText("New");
        jButton_add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_add1ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton_add1);

        jButton_removeRegularExpression.setBackground(new java.awt.Color(226, 226, 226));
        jButton_removeRegularExpression.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_removeRegularExpression.setText("Remove");
        jButton_removeRegularExpression.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_removeRegularExpressionActionPerformed(evt);
            }
        });
        jPanel21.add(jButton_removeRegularExpression);

        jButton_up1.setBackground(new java.awt.Color(226, 226, 226));
        jButton_up1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_up1.setText("Move Up");
        jButton_up1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_up1ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton_up1);

        jButton_down1.setBackground(new java.awt.Color(226, 226, 226));
        jButton_down1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_down1.setText("Move Down");
        jButton_down1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_down1ActionPerformed(evt);
            }
        });
        jPanel21.add(jButton_down1);

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(jPanel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(413, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jPanel9.add(jPanel12, java.awt.BorderLayout.PAGE_END);

        jLabel11.setBackground(new java.awt.Color(204, 204, 204));
        jLabel11.setFont(new java.awt.Font("Calibri", 1, 8)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText(" ");
        jPanel9.add(jLabel11, java.awt.BorderLayout.LINE_END);

        jPanel_listOfRegularExpression.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel_CustomRegularExpression.add(jPanel_listOfRegularExpression, java.awt.BorderLayout.PAGE_START);

        jPanel_SubContainer.setLayout(new java.awt.CardLayout());

        jPanel_detailsOfCustomRegularExpression.setBackground(new java.awt.Color(235, 235, 235));

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel12.setText("Regular Expression:");

        jTextField_RegularExpression.setBackground(new java.awt.Color(255, 253, 254));
        jTextField_RegularExpression.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel13.setText("Class Name");

        jTextField_ClassName.setBackground(new java.awt.Color(255, 253, 254));
        jTextField_ClassName.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N

        jLabel14.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel14.setText("Comment:");

        jTextField_Comment.setBackground(new java.awt.Color(255, 253, 254));
        jTextField_Comment.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N

        jButton_add2.setBackground(new java.awt.Color(226, 226, 226));
        jButton_add2.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jButton_add2.setLabel("Verify and Save Changes");
        jButton_add2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_add2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel_detailsOfCustomRegularExpressionLayout = new org.jdesktop.layout.GroupLayout(jPanel_detailsOfCustomRegularExpression);
        jPanel_detailsOfCustomRegularExpression.setLayout(jPanel_detailsOfCustomRegularExpressionLayout);
        jPanel_detailsOfCustomRegularExpressionLayout.setHorizontalGroup(
            jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_detailsOfCustomRegularExpressionLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField_RegularExpression, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                    .add(jLabel12)
                    .add(jPanel_detailsOfCustomRegularExpressionLayout.createSequentialGroup()
                        .add(jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel13)
                            .add(jTextField_ClassName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel14)
                            .add(jTextField_Comment, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 425, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jButton_add2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 225, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel_detailsOfCustomRegularExpressionLayout.setVerticalGroup(
            jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_detailsOfCustomRegularExpressionLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel12)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField_RegularExpression, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel_detailsOfCustomRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel_detailsOfCustomRegularExpressionLayout.createSequentialGroup()
                        .add(jLabel13)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField_ClassName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel_detailsOfCustomRegularExpressionLayout.createSequentialGroup()
                        .add(jLabel14)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField_Comment, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton_add2)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        jPanel_SubContainer.add(jPanel_detailsOfCustomRegularExpression, "card2");

        jPanel_VerifyRegularExpression.setBackground(new java.awt.Color(235, 235, 235));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jToggleButton1.setText("Go Back");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel_VerifyRegularExpressionLayout = new org.jdesktop.layout.GroupLayout(jPanel_VerifyRegularExpression);
        jPanel_VerifyRegularExpression.setLayout(jPanel_VerifyRegularExpressionLayout);
        jPanel_VerifyRegularExpressionLayout.setHorizontalGroup(
            jPanel_VerifyRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_VerifyRegularExpressionLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_VerifyRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE)
                    .add(jToggleButton1))
                .addContainerGap())
        );
        jPanel_VerifyRegularExpressionLayout.setVerticalGroup(
            jPanel_VerifyRegularExpressionLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_VerifyRegularExpressionLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 122, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToggleButton1)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        jPanel_SubContainer.add(jPanel_VerifyRegularExpression, "card3");

        jPanel_CustomRegularExpression.add(jPanel_SubContainer, java.awt.BorderLayout.CENTER);

        jPanel_containor.add(jPanel_CustomRegularExpression, "card3");

        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel_dictionarylist_and_coordination_radio_button1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 4));
        jPanel_dictionarylist_and_coordination_radio_button1.setFont(new java.awt.Font("Lucida Handwriting", 0, 12)); // NOI18N
        jPanel_dictionarylist_and_coordination_radio_button1.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(226, 226, 226));
        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setBorder(null);

        jList_preAnnotated_concept_dictionaries1.setBackground(new java.awt.Color(240, 240, 255));
        jList_preAnnotated_concept_dictionaries1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153), 2));
        jList_preAnnotated_concept_dictionaries1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jList_preAnnotated_concept_dictionaries1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Empty List" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_preAnnotated_concept_dictionaries1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList_preAnnotated_concept_dictionaries1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_preAnnotated_concept_dictionaries1MouseClicked(evt);
            }
        });
        jList_preAnnotated_concept_dictionaries1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jList_preAnnotated_concept_dictionaries1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList_preAnnotated_concept_dictionaries1KeyReleased(evt);
            }
        });
        jScrollPane4.setViewportView(jList_preAnnotated_concept_dictionaries1);

        jPanel14.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel_title_dictionaries_list4.setBackground(new java.awt.Color(83, 24, 82));
        jPanel_title_dictionaries_list4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel_title_dictionaries_list4.setPreferredSize(new java.awt.Dimension(233, 26));
        jPanel_title_dictionaries_list4.setLayout(new java.awt.BorderLayout());

        jLabel15.setBackground(new java.awt.Color(83, 24, 82));
        jLabel15.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText(">Verifier Dictionaries");
        jPanel_title_dictionaries_list4.add(jLabel15, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(226, 226, 226));
        jPanel18.setMaximumSize(new java.awt.Dimension(32767, 5));
        jPanel18.setMinimumSize(new java.awt.Dimension(100, 5));

        org.jdesktop.layout.GroupLayout jPanel18Layout = new org.jdesktop.layout.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 855, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 5, Short.MAX_VALUE)
        );

        jPanel_title_dictionaries_list4.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel_title_dictionaries_list4, java.awt.BorderLayout.NORTH);

        jPanel22.setBackground(new java.awt.Color(226, 226, 226));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 2));

        jPanel23.setBackground(new java.awt.Color(226, 226, 226));
        jPanel23.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        jButton_add3.setBackground(new java.awt.Color(226, 226, 226));
        jButton_add3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_add3.setText("Add");
        jButton_add3.setToolTipText("Add a new Verifier Dictionary.");
        jButton_add3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_add3ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_add3);

        jButton_remove1.setBackground(new java.awt.Color(226, 226, 226));
        jButton_remove1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_remove1.setText("Remove");
        jButton_remove1.setToolTipText("Remove selected dictionary from list.");
        jButton_remove1.setEnabled(false);
        jButton_remove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_remove1ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_remove1);

        jButton_Edit.setBackground(new java.awt.Color(226, 226, 226));
        jButton_Edit.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_Edit.setText("Edit");
        jButton_Edit.setToolTipText("Edit Selected dictionary");
        jButton_Edit.setEnabled(false);
        jButton_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EditActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_Edit);

        jButton_Edit1.setBackground(new java.awt.Color(226, 226, 226));
        jButton_Edit1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_Edit1.setText("Export");
        jButton_Edit1.setToolTipText("Export the current settings");
        jButton_Edit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Edit1ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_Edit1);

        jButton_Edit2.setBackground(new java.awt.Color(226, 226, 226));
        jButton_Edit2.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_Edit2.setText("Import");
        jButton_Edit2.setToolTipText("Import an entire set of Verifier Settings");
        jButton_Edit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Edit2ActionPerformed(evt);
            }
        });
        jPanel23.add(jButton_Edit2);

        jButton_Edit3.setBackground(new java.awt.Color(226, 226, 226));
        jButton_Edit3.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jButton_Edit3.setText("Global");
        jButton_Edit3.setToolTipText("Import an entire set of Verifier Settings");
        jButton_Edit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_Edit3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .add(jPanel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 592, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_Edit3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel22Layout.createSequentialGroup()
                .add(jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(jButton_Edit3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.add(jPanel22, java.awt.BorderLayout.PAGE_END);

        jLabel16.setBackground(new java.awt.Color(204, 204, 204));
        jLabel16.setFont(new java.awt.Font("Calibri", 1, 8)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText(" ");
        jPanel14.add(jLabel16, java.awt.BorderLayout.LINE_END);

        jPanel_dictionarylist_and_coordination_radio_button1.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel_dictionarylist_and_coordination_radio_button1, java.awt.BorderLayout.PAGE_START);

        jPanel24.setBackground(new java.awt.Color(226, 226, 226));
        jPanel24.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jPanel25.setBackground(new java.awt.Color(226, 226, 226));
        jPanel25.setLayout(new java.awt.GridLayout(9, 1));

        jPanel27.setBackground(new java.awt.Color(83, 24, 82));
        jPanel27.setLayout(new java.awt.BorderLayout());

        jLabel17.setBackground(new java.awt.Color(0, 102, 102));
        jLabel17.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText(">Current Options");
        jPanel27.add(jLabel17, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel27);

        jLabel19.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel19.setText("Check Around Annotations:");
        jPanel25.add(jLabel19);

        jLabel18.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel18.setText("Number Of Words Before:");
        jPanel25.add(jLabel18);

        jLabel20.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel20.setText("Number Of Words After:");
        jPanel25.add(jLabel20);

        jLabel28.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel28.setText("Characters After(Compared character by character): ");
        jPanel25.add(jLabel28);

        jLabel21.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel21.setText("Check Inside Annotations:");
        jPanel25.add(jLabel21);

        jLabel25.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel25.setText("Check First Word:");
        jPanel25.add(jLabel25);

        jLabel26.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel26.setText("Check Last Word:");
        jPanel25.add(jLabel26);

        jLabel27.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel27.setText("Number Allowed In Span:");
        jPanel25.add(jLabel27);

        jPanel24.add(jPanel25);

        jPanel26.setBackground(new java.awt.Color(226, 226, 226));
        jPanel26.setLayout(new java.awt.GridLayout(9, 0));

        jPanel28.setBackground(new java.awt.Color(0, 102, 102));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jLabel22.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText(">Dictionary Information");
        jPanel28.add(jLabel22, java.awt.BorderLayout.CENTER);

        jPanel26.add(jPanel28);

        jLabel23.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel23.setText("Number of Entries:");
        jPanel26.add(jLabel23);

        jLabel24.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel24.setText("Linked Explanation Filename:");
        jPanel26.add(jLabel24);

        jLabel29.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jPanel26.add(jLabel29);

        jPanel24.add(jPanel26);

        jPanel13.add(jPanel24, java.awt.BorderLayout.CENTER);

        jPanel_containor.add(jPanel13, "card4");

        jPanel_others.setBackground(new java.awt.Color(226, 226, 226));
        jPanel_others.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(226, 226, 226), 4));
        jPanel_others.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(226, 226, 226));
        jPanel15.setLayout(new java.awt.GridLayout(16, 0, 0, 2));

        jPanel39.setBackground(new java.awt.Color(0, 102, 102));
        jPanel39.setPreferredSize(new java.awt.Dimension(850, 20));

        jLabel32.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("  UMLS/UTS Account ");

        org.jdesktop.layout.GroupLayout jPanel39Layout = new org.jdesktop.layout.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel39Layout.createSequentialGroup()
                .add(jLabel32)
                .addContainerGap(752, Short.MAX_VALUE))
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel32, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel39);

        jPanel41.setBackground(new java.awt.Color(226, 226, 226));
        jPanel41.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel41.setRequestFocusEnabled(false);
        jPanel41.setLayout(new java.awt.GridLayout(1, 4, 2, 0));

        jLabel33.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel33.setText("  User Name:");
        jPanel41.add(jLabel33);

        jTextField1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jTextField1.setText("jTextField1");
        jPanel41.add(jTextField1);

        jLabel34.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel34.setText("         Password:");
        jPanel41.add(jLabel34);

        jPasswordField1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jPasswordField1.setText("jPasswordField1");
        jPanel41.add(jPasswordField1);

        jPanel15.add(jPanel41);

        jPanel42.setBackground(new java.awt.Color(226, 226, 226));
        jPanel42.setPreferredSize(new java.awt.Dimension(850, 20));

        org.jdesktop.layout.GroupLayout jPanel42Layout = new org.jdesktop.layout.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 857, Short.MAX_VALUE)
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel42);

        jPanel29.setBackground(new java.awt.Color(0, 102, 102));
        jPanel29.setPreferredSize(new java.awt.Dimension(850, 20));

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("  Annotation Builder Assistant");
        jLabel8.setPreferredSize(null);

        org.jdesktop.layout.GroupLayout jPanel29Layout = new org.jdesktop.layout.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel29Layout.createSequentialGroup()
                .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(711, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel29);

        jPanel30.setBackground(new java.awt.Color(226, 226, 226));
        jPanel30.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel30.setRequestFocusEnabled(false);

        buttonGroup2.add(jRadioButton_newannotationspan_exact);
        jRadioButton_newannotationspan_exact.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_newannotationspan_exact.setForeground(new java.awt.Color(102, 102, 102));
        jRadioButton_newannotationspan_exact.setText("Use exact span matching to build new annotations.");
        jRadioButton_newannotationspan_exact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_newannotationspan_exactActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel30Layout = new org.jdesktop.layout.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel30Layout.createSequentialGroup()
                .add(jRadioButton_newannotationspan_exact)
                .addContainerGap(574, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jRadioButton_newannotationspan_exact, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel30);

        jPanel31.setBackground(new java.awt.Color(226, 226, 226));
        jPanel31.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel31.setRequestFocusEnabled(false);

        buttonGroup2.add(jRadioButton_newannotationspan_check);
        jRadioButton_newannotationspan_check.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_newannotationspan_check.setForeground(new java.awt.Color(102, 102, 102));
        jRadioButton_newannotationspan_check.setText("check on annotation span using existing white space or other special characters.");
        jRadioButton_newannotationspan_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_newannotationspan_checkActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel31Layout = new org.jdesktop.layout.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel31Layout.createSequentialGroup()
                .add(jRadioButton_newannotationspan_check)
                .addContainerGap(433, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jRadioButton_newannotationspan_check, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel31);

        jPanel35.setBackground(new java.awt.Color(226, 226, 226));
        jPanel35.setPreferredSize(new java.awt.Dimension(850, 20));

        org.jdesktop.layout.GroupLayout jPanel35Layout = new org.jdesktop.layout.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 857, Short.MAX_VALUE)
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel35);

        jPanel32.setBackground(new java.awt.Color(0, 102, 102));
        jPanel32.setPreferredSize(new java.awt.Dimension(850, 20));

        jLabel30.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("  Oracle Options - Searching for similar terms in documents:");

        org.jdesktop.layout.GroupLayout jPanel32Layout = new org.jdesktop.layout.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel32Layout.createSequentialGroup()
                .add(jLabel30)
                .addContainerGap(562, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel30, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel32);

        jPanel33.setBackground(new java.awt.Color(226, 226, 226));
        jPanel33.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel33.setRequestFocusEnabled(false);

        buttonGroup3.add(jRadioButton_oracle_allmatches);
        jRadioButton_oracle_allmatches.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_oracle_allmatches.setForeground(new java.awt.Color(102, 102, 102));
        jRadioButton_oracle_allmatches.setText("Find all matching spans, using exact or partial matching.");
        jRadioButton_oracle_allmatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_oracle_allmatchesActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel33Layout = new org.jdesktop.layout.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel33Layout.createSequentialGroup()
                .add(jRadioButton_oracle_allmatches)
                .addContainerGap(548, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jRadioButton_oracle_allmatches, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel33);

        jPanel34.setBackground(new java.awt.Color(226, 226, 226));
        jPanel34.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel34.setRequestFocusEnabled(false);

        buttonGroup3.add(jRadioButton_oracle_wholeword);
        jRadioButton_oracle_wholeword.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jRadioButton_oracle_wholeword.setForeground(new java.awt.Color(102, 102, 102));
        jRadioButton_oracle_wholeword.setText("Wholeword only");
        jRadioButton_oracle_wholeword.setPreferredSize(null);
        jRadioButton_oracle_wholeword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_oracle_wholewordActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel34Layout = new org.jdesktop.layout.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel34Layout.createSequentialGroup()
                .add(jRadioButton_oracle_wholeword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(745, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jRadioButton_oracle_wholeword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel34);

        jPanel36.setBackground(new java.awt.Color(226, 226, 226));
        jPanel36.setPreferredSize(new java.awt.Dimension(850, 20));

        org.jdesktop.layout.GroupLayout jPanel36Layout = new org.jdesktop.layout.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 857, Short.MAX_VALUE)
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel36);

        jPanel43.setBackground(new java.awt.Color(0, 102, 102));
        jPanel43.setPreferredSize(new java.awt.Dimension(850, 20));

        jLabel35.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("  Enable/Disable Diff function on Annotation Mode ");

        org.jdesktop.layout.GroupLayout jPanel43Layout = new org.jdesktop.layout.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel43Layout.createSequentialGroup()
                .add(jLabel35)
                .addContainerGap(605, Short.MAX_VALUE))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel35, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel43);

        jPanel44.setBackground(new java.awt.Color(226, 226, 226));
        jPanel44.setPreferredSize(new java.awt.Dimension(850, 20));
        jPanel44.setRequestFocusEnabled(false);

        jCheckBox_exportSuggestionsToXML1.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jCheckBox_exportSuggestionsToXML1.setText("Enable the \"Diff\" function on the Annotation Mode");
        jCheckBox_exportSuggestionsToXML1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_exportSuggestionsToXML1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel44Layout = new org.jdesktop.layout.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel44Layout.createSequentialGroup()
                .add(jCheckBox_exportSuggestionsToXML1)
                .addContainerGap(577, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel44Layout.createSequentialGroup()
                .add(jCheckBox_exportSuggestionsToXML1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel44);

        jPanel_others.add(jPanel15, java.awt.BorderLayout.NORTH);

        jPanel_containor.add(jPanel_others, "card5");

        getContentPane().add(jPanel_containor, java.awt.BorderLayout.CENTER);

        jPanel_bottom.setBackground(new java.awt.Color(204, 204, 204));
        jPanel_bottom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(167, 167, 167), 2));
        jPanel_bottom.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(167, 167, 167));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 11, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 35, Short.MAX_VALUE)
        );

        jPanel_bottom.add(jPanel1, java.awt.BorderLayout.EAST);

        jPanel2.setBackground(new java.awt.Color(167, 167, 167));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(167, 167, 167), 3));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jButton1.setFont(new java.awt.Font("Calibri", 1, 15)); // NOI18N
        jButton1.setText("      OK      ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, java.awt.BorderLayout.EAST);

        jPanel_bottom.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel_bottom, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * update information before closing this dialog.
     */
    private void Close_this_window() {
        try {

            // get user name and password from the list
            String umls_username = jTextField1.getText();
            String umls_password = null;
            if (jPasswordField1.getPassword() != null) {
                umls_password = String.valueOf(jPasswordField1.getPassword());
            }

            /*boolean changed = false;
            
             // "change" has been made if we have a user name now and didn't have it before.
             if(( env.Parameters.umls_username  == null ) && (umls_username != null) && (umls_username.trim().length()>0 ) ) 
             changed = true;
             // "change" has been made if we have a user password now and didn't have it before.
             if(( env.Parameters.umls_decryptedPassword  == null ) && (umls_password != null) && (umls_password.trim().length()>0 ) ) 
             changed = true;
            
             if(( env.Parameters.umls_username  != null ) && ((umls_username == null) || (umls_username.trim().length()<1 )) ) 
             changed = true;
             if(( env.Parameters.umls_decryptedPassword  != null ) && ((umls_password == null) || (umls_password.trim().length()<1 )) ) 
             changed = true;
            
             if(( umls_username!=null )&&(umls_password!=null)){
             if((env.Parameters.umls_username==null)||(env.Parameters.umls_decryptedPassword==null))
             changed = true;
             else if( ( umls_username.trim().compareTo( env.Parameters.umls_username) != 0 )
             || ( umls_password.trim().compareTo( env.Parameters.umls_decryptedPassword) != 0 ) )
             changed = true;
             }
            
             if( changed ){*/
            env.Parameters.umls_username = ((umls_username == null) || (umls_username.trim().length() < 1)) ? null : umls_username.trim();
            env.Parameters.umls_decryptedPassword = ((umls_password == null) || (umls_password.trim().length() < 1)) ? null : umls_password.trim();
                //System.out.println("\n\n\n\n output umls user name / password [" + env.Parameters.umls_username + "]/["
            //        + env.Parameters.umls_decryptedPassword + "];");
            // save it
            config.system.SysConf.saveSystemConfigure();
            //}
        } catch (Exception ex) {
            System.out.println("fail to saved updated umls account information!!");
            //ex.printStackTrace();
        }

        // save all configuration information from memory to disk
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();

        // cloase current dialog
        this.setVisible(false);
        __gui.setEnabled(true);
        this.dispose();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Close_this_window();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton_upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_upActionPerformed
        // TODO add your handling code here:
        moveup_preannotated_concept_dictionary();
}//GEN-LAST:event_jButton_upActionPerformed

    private void moveup_preannotated_concept_dictionary() {

        // change position of dictioaries in memory
        int selectedIndex = jList_preAnnotated_concept_dictionaries.getSelectedIndex();
        int lastIndex = jList_preAnnotated_concept_dictionaries.getLastVisibleIndex();
        if ((selectedIndex <= 0) || (selectedIndex > lastIndex)) {
            return;
        }
        int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
        if (selectedIndex > (size - 1)) {
            return;
        }
        Object[] o_current = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(selectedIndex);
        Object[] o_previous = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(selectedIndex - 1);

        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(selectedIndex - 1, o_current);
        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(selectedIndex, o_previous);

        if (!jRadioButton_sameWeight.isSelected()) {
            resort_all_weight_as_sequence();
        }

        // show them in screen
        Show_dictionaries_on_list();
        jList_preAnnotated_concept_dictionaries.setSelectedIndex(selectedIndex - 1);

        // set button if needed
        check_and_set_button_status();

    }

    private void movedown_preannotated_concept_dictionary() {

        // change position of dictioaries in memory
        int selectedIndex = jList_preAnnotated_concept_dictionaries.getSelectedIndex();
        int lastIndex = jList_preAnnotated_concept_dictionaries.getLastVisibleIndex();
        if ((selectedIndex < 0) || (selectedIndex >= lastIndex)) {
            return;
        }
        int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
        if ((selectedIndex + 1) > (size - 1)) {
            return;
        }
        Object[] o_current = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(selectedIndex);
        Object[] o_next = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(selectedIndex + 1);

        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(selectedIndex + 1, o_current);
        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(selectedIndex, o_next);

        if (!jRadioButton_sameWeight.isSelected()) {
            resort_all_weight_as_sequence();
        }

        // show them in screen
        Show_dictionaries_on_list();
        jList_preAnnotated_concept_dictionaries.setSelectedIndex(selectedIndex + 1);

        // set button if needed
        check_and_set_button_status();

    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // save all configuration information from memory to disk
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();

        __gui.setEnabled(true);

    }//GEN-LAST:event_formWindowClosing

    private void jRadioButton_sameWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_sameWeightActionPerformed
        check_radiobutton_and_set_flag_of_weight();
    }//GEN-LAST:event_jRadioButton_sameWeightActionPerformed

    private void jRadioButton_diifferentWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_diifferentWeightActionPerformed
        check_radiobutton_and_set_flag_of_weight();
    }//GEN-LAST:event_jRadioButton_diifferentWeightActionPerformed

    // set the flag of different coordination working type (same weight of dictionaries or different)
    // by the selected status of radiobuttons
    private void check_radiobutton_and_set_flag_of_weight() {
        // disable 3 button - remove, move up, move down
        jButton_remove.setEnabled(false);
        jButton_up.setEnabled(false);
        jButton_down.setEnabled(false);

        // check status of radiobutton and set flag of weight of dictonaries
        // and reload dictionaries names in the list
        boolean previous_value = env.Parameters.Pre_Defined_Dictionary_DifferentWeight;
        if (jRadioButton_diifferentWeight.isSelected()) {
            env.Parameters.Pre_Defined_Dictionary_DifferentWeight = true;
            // if status changed to "True"
            if (previous_value == false) {
                resort_all_weight_as_sequence();

                config.project.ProjectConf projectconf = new config.project.ProjectConf(
                        env.Parameters.WorkSpace.CurrentProject);
                projectconf.saveConfigure();

                Show_dictionaries_on_list();
            }
        } else {
            env.Parameters.Pre_Defined_Dictionary_DifferentWeight = false;
            // if status changed to "false"
            if (previous_value == true) {
                resort_all_weight_as_same();

                config.project.ProjectConf projectconf = new config.project.ProjectConf(
                        env.Parameters.WorkSpace.CurrentProject);
                projectconf.saveConfigure();;

                Show_dictionaries_on_list();
            }
        }
    }

    // parameter of weight will not works and all should be set as 0,
    // if we treat all dictionaries on same level
    private void resort_all_weight_as_same() {
        int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Object[] o = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                /**
                 * @para filename - file name with absolute path
                 * @para weight - if same all will be 0, otherwise big number
                 * means heavy weight
                 * @para description
                 * @para separator
                 * @para number_of_valid_entries
                 */
                Object[] e
                        = {
                            o[0], "0", o[2], o[3], o[4]
                        };
                env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(i, e);
            }
        }
    }

    private void resort_all_weight_as_sequence() {
        int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Object[] o = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                /**
                 * @para filename - file name with absolute path
                 * @para weight - if same all will be 0, otherwise big number
                 * means heavy weight
                 * @para description
                 * @para separator
                 * @para number_of_valid_entries
                 */
                Object[] e
                        = {
                            o[0], String.valueOf(i + 1), o[2], o[3], o[4]
                        };
                env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.set(i, e);
            }
        }
    }

    private void jButton_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addActionPerformed
        //open dialogs of wizard of add concept
        //to add a new pre-annotated concept dictionary
        Call_dialog_of_wizard_of_concept_dictionary();
    }//GEN-LAST:event_jButton_addActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        Show_dictionaries_on_list();
        Show_verified_dictionaries_on_list();
    }//GEN-LAST:event_formFocusGained

    private void jList_preAnnotated_concept_dictionariesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_preAnnotated_concept_dictionariesMouseClicked
    }//GEN-LAST:event_jList_preAnnotated_concept_dictionariesMouseClicked

    private void check_and_set_button_status() {
        // TODO add your handling code here:
        int lastIndex = jList_preAnnotated_concept_dictionaries.getLastVisibleIndex();

        int selected = jList_preAnnotated_concept_dictionaries.getSelectedIndex();

        // if we got any row selected in this list, enable these 3 buttons
        if (selected > -1) {
            jButton_remove.setEnabled(true);
            show_details_of_selected_preannotated_concept_dictionary(selected);
        } else {
            jButton_remove.setEnabled(false);
        }

        if ((selected > 0) && (selected <= lastIndex)) {
            jButton_up.setEnabled(true);
        } else {
            jButton_up.setEnabled(false);
        }

        if ((selected >= 0) && (selected < lastIndex)) {
            jButton_down.setEnabled(true);
        } else {
            jButton_down.setEnabled(false);
        }
    }

    private void show_details_of_selected_verifier_dictionary(int _selected) {
        int lastIndex = jList_preAnnotated_concept_dictionaries1.getLastVisibleIndex();

        if ((_selected < 0) || (_selected > lastIndex)) {
            return;
        }

        int size = env.Parameters.VERIFIER_DICTIONARIES.size();
        if (_selected > (size - 1)) {
            return;
        }

        dictionaries.VerifierDictionaryFormat o = env.Parameters.VERIFIER_DICTIONARIES.get(_selected);

        jLabel19.setText("<html>Check Around Annotations:<font color=blue>" + String.valueOf(o.isCheckAround()) + "</font></html>");
        jLabel18.setText("<html>Number Of Words Before: <font color=blue>" + String.valueOf(o.getWordsBefore()) + "</font></html>");
        jLabel20.setText("<html>Number Of Words After: <font color=blue>" + String.valueOf(o.getWordsAfter()) + "</font></html>");
        jLabel28.setText("<html>Characters After(Compared character by character):  <font color=blue>" + String.valueOf(o.getCharByCharAfter()) + "</font></html>");
        jLabel21.setText("<html>Check Inside Annotations:  <font color=blue>" + String.valueOf(o.isCheckInside()) + "</font></html>");
        jLabel23.setText("<html>Number Of Entries:  <font color=blue>" + String.valueOf(o.getNumberOfEntries()) + "</font></html>");
        jLabel29.setText("<html><font color=blue>" + o.getExplanationFileNameAbsolute() + "</font></html>");
        jLabel25.setText("<html>Check First Word:  <font color=blue>" + String.valueOf(o.isFirstOnly()) + "</font></html>");
        jLabel26.setText("<html>Check Last Word:  <font color=blue>" + String.valueOf(o.isLastOnly()) + "</font></html>");
        jLabel27.setText("<html>Number Allowed In Span:  <font color=blue>" + String.valueOf(o.getAllowedInSpan()) + "</font></html>");
    }

    private void show_details_of_selected_preannotated_concept_dictionary(int _selected) {
        int lastIndex = jList_preAnnotated_concept_dictionaries.getLastVisibleIndex();

        if ((_selected < 0) || (_selected > lastIndex)) {
            return;
        }

        int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
        if (_selected > (size - 1)) {
            return;
        }

        Object[] o = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(_selected);

        jLabel5.setText("<html>Dictionary File Name: <font color=blue>" + o[0].toString() + "</font></html>");
        jLabel6.setText("<html>Dictionary Weight: <font color=blue>" + o[1].toString() + "</font></html>");
        jLabel7.setText("<html>Description: <font color=blue>" + o[2].toString() + "</font></html>");

        // display the separator on dialog
        String separator = o[3].toString().trim();
        separator = separator.replaceAll("<", "&#60");
        separator = separator.replaceAll(">", "&#62");
        jLabel_separator.setText("<html>Separator: <font color=blue>" + separator + "</font></html>");

        jLabel9.setText("<html>Valid Entries: <font color=blue>" + o[4].toString() + "</font></html>");

    }

    private void jButton_removeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_removeActionPerformed
        /**
         * remove selected dictionary from memory and list, then update the
         * screen
         */
        delete_selected_preannotated_concept_dictionaries();

        if ((env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size() > 0) && (env.Parameters.Pre_Defined_Dictionary_DifferentWeight)) {
            resort_all_weight_as_sequence();
        }
        // reload on screen
        Show_dictionaries_on_list();
        // update to the configure file
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();
    }//GEN-LAST:event_jButton_removeActionPerformed

    private void jButton_downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_downActionPerformed
        // TODO add your handling code here:
        movedown_preannotated_concept_dictionary();
    }//GEN-LAST:event_jButton_downActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        openCard("CustomRegularExpression");
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        openCard("ConceptDictionaries");
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jButton_add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_add1ActionPerformed
        openPanelOfDetailstoCustomRE(1); //1: new
        FLAG_WORKTYPE = NEW;

    }//GEN-LAST:event_jButton_add1ActionPerformed

    private void jButton_removeRegularExpressionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_removeRegularExpressionActionPerformed
        deleteSelectRegularExpression();
    }//GEN-LAST:event_jButton_removeRegularExpressionActionPerformed

    private void jButton_up1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_up1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_up1ActionPerformed

    private void jButton_down1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_down1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_down1ActionPerformed

    private void jButton_add2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_add2ActionPerformed
        // TODO add your handling code here:
        int selected = this.jList_RegularExpression.getSelectedIndex();
        saveNewRE(selected);
    }//GEN-LAST:event_jButton_add2ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        jPanel_SubContainer.removeAll();
        jPanel_SubContainer.add(jPanel_detailsOfCustomRegularExpression, "2");
        jPanel_SubContainer.validate();
        jPanel_SubContainer.repaint();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jList_RegularExpressionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_RegularExpressionMouseClicked
        // selected regular expression
        int lastIndex = jList_RegularExpression.getLastVisibleIndex();
        int selected = jList_RegularExpression.getSelectedIndex();
        if ((selected < 0) || (selected > lastIndex)) {
            jButton_removeRegularExpression.setEnabled(false);
            openPanelOfDetailstoCustomRE(0);
        } else {
            jButton_removeRegularExpression.setEnabled(true);
            openPanelOfDetailstoCustomRE(2);
            FLAG_WORKTYPE = EDIT;
        }


    }//GEN-LAST:event_jList_RegularExpressionMouseClicked

    private void jList_preAnnotated_concept_dictionaries1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jList_preAnnotated_concept_dictionaries1MouseClicked
    {//GEN-HEADEREND:event_jList_preAnnotated_concept_dictionaries1MouseClicked

        int selected = jList_preAnnotated_concept_dictionaries1.getSelectedIndex();

        // if we got any row selected in this list, enable these 3 buttons
        if (selected > -1) {
            jButton_remove1.setEnabled(true);
            jButton_Edit.setEnabled(true);
            show_details_of_selected_verifier_dictionary(selected);
        } else {
            jButton_Edit.setEnabled(false);
            jButton_remove1.setEnabled(false);
        }


}//GEN-LAST:event_jList_preAnnotated_concept_dictionaries1MouseClicked

    private void jButton_add3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_add3ActionPerformed
    {//GEN-HEADEREND:event_jButton_add3ActionPerformed
        userInterface.WizardVerifierLib conceptlibDialog = new userInterface.WizardVerifierLib(this);

        conceptlibDialog.setLocation(this.getX() + (int) ((this.getWidth() - conceptlibDialog.getHeight()) / 2),
                this.getY() + (int) ((this.getHeight() - conceptlibDialog.getHeight()) / 2));
        //startAnalysis.setVisible(false);
        setEnabled(false);
        //conceptlibDialog.setLo
        conceptlibDialog.setVisible(true);
}//GEN-LAST:event_jButton_add3ActionPerformed

    private void jButton_remove1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_remove1ActionPerformed
    {//GEN-HEADEREND:event_jButton_remove1ActionPerformed
        try {
            int index = jList_preAnnotated_concept_dictionaries1.getSelectedIndex();
            env.Parameters.VERIFIER_DICTIONARIES.remove(index);
        } catch (Exception e) {
            logs.ShowLogs.printErrorLog(e.toString());
        }
        // reload on screen
        Show_verified_dictionaries_on_list();
        // update to the configure file
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();
}//GEN-LAST:event_jButton_remove1ActionPerformed

    private void jButton_EditActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_EditActionPerformed
    {//GEN-HEADEREND:event_jButton_EditActionPerformed
        userInterface.WizardVerifierLib conceptlibDialog = new userInterface.WizardVerifierLib(this, env.Parameters.VERIFIER_DICTIONARIES.get(this.jList_preAnnotated_concept_dictionaries1.getSelectedIndex()), this.jList_preAnnotated_concept_dictionaries1.getSelectedIndex());
        conceptlibDialog.setLocation(this.getX() + (int) ((this.getWidth() - conceptlibDialog.getHeight()) / 2),
                this.getY() + (int) ((this.getHeight() - conceptlibDialog.getHeight()) / 2));
        //startAnalysis.setVisible(false);
        setEnabled(false);
        conceptlibDialog.setVisible(true);
}//GEN-LAST:event_jButton_EditActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton4ActionPerformed
    {//GEN-HEADEREND:event_jToggleButton4ActionPerformed
        openCard("VerifierDictionaries");        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jList_preAnnotated_concept_dictionaries1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jList_preAnnotated_concept_dictionaries1KeyPressed
    {//GEN-HEADEREND:event_jList_preAnnotated_concept_dictionaries1KeyPressed
    }//GEN-LAST:event_jList_preAnnotated_concept_dictionaries1KeyPressed

    private void jList_preAnnotated_concept_dictionaries1KeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jList_preAnnotated_concept_dictionaries1KeyReleased
    {//GEN-HEADEREND:event_jList_preAnnotated_concept_dictionaries1KeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_DOWN: {
                int selected = jList_preAnnotated_concept_dictionaries1.getSelectedIndex();

                // if we got any row selected in this list, enable these 3 buttons
                if (selected > -1) {
                    jButton_remove1.setEnabled(true);
                    jButton_Edit.setEnabled(true);
                    show_details_of_selected_verifier_dictionary(selected);
                } else {
                    jButton_Edit.setEnabled(false);
                    jButton_remove1.setEnabled(false);
                }
                break;
            }
            case KeyEvent.VK_UP: {
                int selected = jList_preAnnotated_concept_dictionaries1.getSelectedIndex();

                // if we got any row selected in this list, enable these 3 buttons
                if (selected > -1) {
                    jButton_remove1.setEnabled(true);
                    jButton_Edit.setEnabled(true);
                    show_details_of_selected_verifier_dictionary(selected);
                } else {
                    jButton_Edit.setEnabled(false);
                    jButton_remove1.setEnabled(false);
                }
                break;
            }
        }
    }//GEN-LAST:event_jList_preAnnotated_concept_dictionaries1KeyReleased

    private void jList_preAnnotated_concept_dictionariesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_preAnnotated_concept_dictionariesValueChanged
        check_and_set_button_status();
    }//GEN-LAST:event_jList_preAnnotated_concept_dictionariesValueChanged

    private void jButton_Edit1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_Edit1ActionPerformed
    {//GEN-HEADEREND:event_jButton_Edit1ActionPerformed

        File theFile = openSingleFileDialog(true, new String[]{".vconfigs"}, "Verifier Configuration Files");
        if (theFile == null) {
            return;
        }
        exportOrImportVerifier.writeFile(theFile);
    }//GEN-LAST:event_jButton_Edit1ActionPerformed

    private void jButton_Edit2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton_Edit2ActionPerformed
    {//GEN-HEADEREND:event_jButton_Edit2ActionPerformed

        File theFile = openSingleFileDialog(true, new String[]{".vconfigs"}, "Verifier Configuration Files");
        if (theFile == null) {
            return;
        }
        exportOrImportVerifier.importFile(theFile);

        Show_verified_dictionaries_on_list();

        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();
    }//GEN-LAST:event_jButton_Edit2ActionPerformed

    private void jButton_InitDictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_InitDictActionPerformed
        nlp.quickNLP.InitDlg initdlg = new nlp.quickNLP.InitDlg(this);
        initdlg.setVisible(true);
    }//GEN-LAST:event_jButton_InitDictActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        openCard("Others");
        //set initilization value of components on this card by values of flags
        component_setComponent_forCardOther();
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jRadioButton_newannotationspan_exactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_newannotationspan_exactActionPerformed
        // Set values of flag to fit changes of selection status of these two
        // radiobuttons.
        operation_setEnvValues_byComponentsStatus();
    }//GEN-LAST:event_jRadioButton_newannotationspan_exactActionPerformed

    private void jRadioButton_newannotationspan_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_newannotationspan_checkActionPerformed
        // Set values of flag to fit changes of selection status of these two
        // radiobuttons.
        operation_setEnvValues_byComponentsStatus();
    }//GEN-LAST:event_jRadioButton_newannotationspan_checkActionPerformed

    private void jRadioButton_oracle_allmatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_oracle_allmatchesActionPerformed
        operation_setEnvValues_toOracle_byComponentsStatus();
    }//GEN-LAST:event_jRadioButton_oracle_allmatchesActionPerformed

    private void jRadioButton_oracle_wholewordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_oracle_wholewordActionPerformed
        operation_setEnvValues_toOracle_byComponentsStatus();
    }//GEN-LAST:event_jRadioButton_oracle_wholewordActionPerformed

    private void jCheckBox_stopwordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_stopwordsActionPerformed
        // by the selection status to this checkbox to set the parameter
        // for enabling/disabling the stop words
        env.Parameters.nlp_dictionary_proc.using_StopWords = jCheckBox_stopwords.isSelected();
    }//GEN-LAST:event_jCheckBox_stopwordsActionPerformed

    private void jButton_Edit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_Edit3ActionPerformed
        userInterface.VerifierCompareSettings conceptlibDialog = new userInterface.VerifierCompareSettings(this);
        
        
        conceptlibDialog.setLocation(
                this.getX() + (int) ((this.getWidth() - conceptlibDialog.getHeight()) / 2),
                this.getY() + (int) ((this.getHeight() - conceptlibDialog.getHeight()) / 2));
        
        //startAnalysis.setVisible(false);
        setEnabled(false);
        conceptlibDialog.setVisible(true);
    }//GEN-LAST:event_jButton_Edit3ActionPerformed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated

        if (conceptlibDialog != null) {
            if (conceptlibDialog.isVisible()) {
                return;
            }
        }
        this.toFront();
        this.requestFocus();

        // beep
        Tools.beep();
    }//GEN-LAST:event_formWindowDeactivated

    private void jCheckBox_exportSuggestionsToXML1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_exportSuggestionsToXML1ActionPerformed
        env.Parameters.EnableDiffButton = jCheckBox_exportSuggestionsToXML1.isSelected();

    }//GEN-LAST:event_jCheckBox_exportSuggestionsToXML1ActionPerformed

    /**
     * Set values of flag to fit changes of selection status of these two
     * radiobuttons.
     */
    private void operation_setEnvValues_byComponentsStatus() {
        if (jRadioButton_newannotationspan_exact.isSelected()) {
            env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = true;
        } else {
            env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = false;
        }
    }

    /**
     * Set values of flag to fit changes of selection status of these two
     * radiobuttons: oracle module.
     */
    private void operation_setEnvValues_toOracle_byComponentsStatus() {
        if (jRadioButton_oracle_wholeword.isSelected()) {
            env.Parameters.Oracle.search_matchWholeWord = true;
        } else {
            env.Parameters.Oracle.search_matchWholeWord = false;
        }
    }

    /**
     * After user clicked button "other" on the vectical tools bar, we need to
     * set initilization value of components on this card by values of flags in
     * class "Parameter"
     */
    private void component_setComponent_forCardOther() {
        if (env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan) {
            jRadioButton_newannotationspan_exact.setSelected(true);
        } else {
            jRadioButton_newannotationspan_check.setSelected(true);
        }

        if (env.Parameters.Oracle.search_matchWholeWord) {
            jRadioButton_oracle_wholeword.setSelected(true);
        } else {
            jRadioButton_oracle_allmatches.setSelected(true);
        }

        // parameter: export verify suggestion to XML files
        //jCheckBox_exportSuggestionsToXML.setSelected(env.Parameters.Output_XML.output_verify_suggestions_toXML);
        jCheckBox_exportSuggestionsToXML1.setSelected(env.Parameters.EnableDiffButton);
    }

    private void deleteSelectRegularExpression() {
        // got index of selected regular expression
        int lastIndex = jList_RegularExpression.getLastVisibleIndex();
        int selected = jList_RegularExpression.getSelectedIndex();
        if ((selected < 0) || (selected > lastIndex)) {
            return;
        }

        nlp.filterCustomDefined.CustomRegexManager.delete(selected);
        nlp.filterCustomDefined.CustomRegexManager.saveTolib();
        showRegularExpression();

        openPanelOfDetailstoCustomRE(0);

        jButton_removeRegularExpression.setEnabled(false);
    }

    private void delete_selected_preannotated_concept_dictionaries() {
        try {
            int index = jList_preAnnotated_concept_dictionaries.getSelectedIndex();
            env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.remove(index);
        } catch (Exception e) {
            logs.ShowLogs.printErrorLog(e.toString());
        }
    }

    private void Call_dialog_of_wizard_of_concept_dictionary() {
        if ((conceptlibDialog != null) && (conceptlibDialog instanceof userInterface.addNewDict.WizardConceptLib)) {
            conceptlibDialog.dispose();
        }
        conceptlibDialog = new userInterface.addNewDict.WizardConceptLib(this, __gui);

        this.setEnabled(false);

        conceptlibDialog.setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("激发该事件");
        if (evt.getPropertyName().equals("Concept_Wizard_End")) {
            setEnabled(true);
            Show_dictionaries_on_list();
            repaint();
        }
    }

    public void reactive_this_dialog_after_finishing_wizard_of_add_concept_dictionary() {
        setEnabled(true);
        Show_dictionaries_on_list();
        repaint();
    }

    /**
     * Show designated panel on card layout
     */
    public void openCard(String _cardname) {
        /**
         * load panel of custom regular expression
         */
        if (_cardname.compareTo("CustomRegularExpression") == 0) {
            jPanel_containor.removeAll();
            jPanel_containor.add(jPanel_CustomRegularExpression, "custom RE");
            jPanel_containor.validate();
            jPanel_containor.repaint();

            //set visible of the details panel for custom regular expression
            openPanelOfDetailstoCustomRE(0);

            jButton_removeRegularExpression.setEnabled(false);

            /**
             * load panel of concept details
             */
        } else if (_cardname.compareTo("ConceptDictionaries") == 0) {
            jPanel_containor.removeAll();
            jPanel_containor.add(jPanel_conceptDictionaries, "concept dictionaries");
            jPanel_containor.validate();
            jPanel_containor.repaint();
            // load all found dictionaries into the list on the screen
            Show_dictionaries_on_list();

            // set checked status of radio buttons of weight for different
            if (env.Parameters.Pre_Defined_Dictionary_DifferentWeight == true) {
                jRadioButton_diifferentWeight.setSelected(true);
            } else {
                jRadioButton_sameWeight.setSelected(true);
            }
        } else if (_cardname.compareTo("Others") == 0) {
            jPanel_containor.removeAll();
            jPanel_containor.add(jPanel_others, "others");
            jPanel_containor.validate();
            jPanel_containor.repaint();
            // load all found dictionaries into the list on the screen
            //Show_dictionaries_on_list();

            // set checked status of radio buttons of weight for different
            if (env.Parameters.Pre_Defined_Dictionary_DifferentWeight == true) {
                jRadioButton_diifferentWeight.setSelected(true);
            } else {
                jRadioButton_sameWeight.setSelected(true);
            }

            jTextField1.setText(env.Parameters.umls_username);
            jPasswordField1.setText(env.Parameters.umls_decryptedPassword);

        } else if (_cardname.compareTo("VerifierDictionaries") == 0) {
            jPanel_containor.removeAll();
            jPanel_containor.add(jPanel13, "verifier dictionaries");
            jPanel_containor.validate();
            jPanel_containor.repaint();
        }
    }

    /**
     * new/modifi details of regular expression
     */
    /**
     * set visible of the details panel for custom regular expression
     */
    private void openPanelOfDetailstoCustomRE(int _workType) {

        /**
         * worktype == 0: mean do nothing
         */
        if (_workType == 0) {
            jPanel_SubContainer.setVisible(false);
            /**
             * clear and reload entries of custom regular expression from lib
             */
            nlp.filterCustomDefined.CustomRegexManager.clear();
            nlp.filterCustomDefined.CustomRegexManager.loadFromLib();
            showRegularExpression();

        } /**
         * worktype == 1: mean "new" - create a new custom regular expression
         */
        else if (_workType == 1) {

            jPanel_SubContainer.setVisible(true);

            // show panel for details of regualr expression on cardlayout
            jPanel_SubContainer.removeAll();
            jPanel_SubContainer.add(jPanel_detailsOfCustomRegularExpression, "hello");
            jPanel_SubContainer.validate();
            jPanel_SubContainer.repaint();

            // clear all text field of regular expression
            jTextField_RegularExpression.setText(null);
            jTextField_ClassName.setText(null);
            jTextField_Comment.setText(null);

            FLAG_WORKTYPE = NEW;

            /**
             * edit exisit regular
             */
        } else if (_workType == 2) {

            int selected = jList_RegularExpression.getSelectedIndex();
            nlp.filterCustomDefined.CustomRegexFormat cre = nlp.filterCustomDefined.CustomRegexManager.get(selected);
            if (cre == null) {
                openPanelOfDetailstoCustomRE(0);
            } else {

                jPanel_SubContainer.setVisible(true);

                // show panel for details of regualr expression on cardlayout
                jPanel_SubContainer.removeAll();
                jPanel_SubContainer.add(jPanel_detailsOfCustomRegularExpression, "hello");
                jPanel_SubContainer.validate();
                jPanel_SubContainer.repaint();

                // clear all text field of regular expression
                jTextField_RegularExpression.setText(cre.customRegularExpression);
                jTextField_ClassName.setText(cre.classname);
                jTextField_Comment.setText(cre.comment);

                FLAG_WORKTYPE = EDIT;
            }
        }
    }

    /**
     * show regular expression information in the list
     */
    private void showRegularExpression() {
        Vector<String> res = nlp.filterCustomDefined.CustomRegexManager.buildVectorForDisplay();
        jList_RegularExpression.setListData(res);
        jList_RegularExpression.repaint();
    }

    /**
     * save newly added regular expression information
     *
     * @param _selected, [integer]; show the selected index of the selected item
     * in list; used for repetitive check
     */
    private void saveNewRE(int _selected) {
        if (jTextField_RegularExpression.getText().trim().length() < 1) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        if (jTextField_ClassName.getText().trim().length() < 1) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        jPanel_SubContainer.removeAll();
        jPanel_SubContainer.add(jPanel_VerifyRegularExpression, "2");
        jPanel_SubContainer.validate();
        jPanel_SubContainer.repaint();

        jTextArea1.setEditable(false);
        jTextArea1.setText(null);

        String regularExpression = jTextField_RegularExpression.getText().trim();
        String classname = jTextField_ClassName.getText().trim();
        String comment = jTextField_Comment.getText().trim();
        if (comment.length() < 1) {
            comment = "null";
        }

        String result = nlp.filterCustomDefined.CustomRegexManager.verifyARegularExpression(regularExpression);
        jToggleButton1.setVisible(false);

        /**
         * check validity of your regular expression
         */
        if (result != null) {
            jTextArea1.setText("1) - failed the validity check!!! Details as following:\n" + result);
            jToggleButton1.setVisible(true);
            Toolkit.getDefaultToolkit().beep();
            return;
        } else {
            jTextArea1.setText("1) - Passed the validity check!");
            jToggleButton1.setVisible(false);

        }

        /**
         * check repetitiveness while you are create a new regular experission
         */
        if (FLAG_WORKTYPE == NEW) {
            boolean repetitiveness = nlp.filterCustomDefined.CustomRegexManager.existing(regularExpression);
            if (repetitiveness) {
                jTextArea1.setText("1) - Passed the validity check!\n"
                        + "2) - failed in repetitiveness check:\n"
                        + "     this regular has exsited in custom regular expression library!!!");
                jToggleButton1.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
                return;
            } else {
                nlp.filterCustomDefined.CustomRegexManager.add(regularExpression, classname, comment);
                jTextArea1.setText("1) - Passed the validity check!\n"
                        + "2) - Passed in repetitiveness check!\n"
                        + "3) - Saved!");
                jToggleButton1.setVisible(false);
            }
        } else {
            boolean repetitiveness = nlp.filterCustomDefined.CustomRegexManager.existing(regularExpression, _selected);
            if (repetitiveness) {
                jTextArea1.setText("1) - Passed the validity check!\n"
                        + "2) - failed in repetitiveness check:\n"
                        + "     this regular has exsited in custom regular expression library!!!");
                jToggleButton1.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
                return;
            } else {
                nlp.filterCustomDefined.CustomRegexManager.modify(_selected, regularExpression, classname, comment);
                jTextArea1.setText("1) - Passed the validity check!\n"
                        + "2) - Passed in repetitiveness check!\n"
                        + "3) - Saved!");
                jToggleButton1.setVisible(false);
            }
        }

        /**
         * save change
         */
        nlp.filterCustomDefined.CustomRegexManager.saveTolib();

        /**
         * reload in list
         */
        showRegularExpression();

    }

    /**
     * Open dialog to allow user to choose a destination
     */
    private File openSingleFileDialog(boolean save, String[] extensions, String description) {
        File directory = jFileChooser1.getCurrentDirectory();
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setCurrentDirectory(directory);
        jFileChooser1.setMultiSelectionEnabled(false);

        // Add file filter
        jFileChooser1.addChoosableFileFilter(new ExtensionFileFilter(
                extensions,
                description));

        // Turn off 'All Files' capability of file chooser,
        // so only our custom filter is used.
        jFileChooser1.setAcceptAllFileFilterUsed(false);

        int result = 0;
        if (save) {
            result = jFileChooser1.showSaveDialog(this);
        } else {
            result = jFileChooser1.showOpenDialog(this);
        }

        if (jFileChooser1.getSelectedFile() != null && result == JFileChooser.APPROVE_OPTION) {
            return jFileChooser1.getSelectedFile();
        }
        jFileChooser1.resetChoosableFileFilters();

        return null;
    }
    /**
     * @param args the command line arguments
     */
    /*
     public static void main(String args[]) {
     java.awt.EventQueue.invokeLater(new Runnable() {
     public void run() {
     new Preference().setVisible(true);
     }
     });
     }
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup_conceptdictionaries_method;
    private javax.swing.JButton jButton1;
    private static javax.swing.JButton jButton_Edit;
    private static javax.swing.JButton jButton_Edit1;
    private static javax.swing.JButton jButton_Edit2;
    private static javax.swing.JButton jButton_Edit3;
    private static javax.swing.JButton jButton_InitDict;
    private static javax.swing.JButton jButton_add;
    private static javax.swing.JButton jButton_add1;
    private static javax.swing.JButton jButton_add2;
    private static javax.swing.JButton jButton_add3;
    private static javax.swing.JButton jButton_down;
    private static javax.swing.JButton jButton_down1;
    private static javax.swing.JButton jButton_remove;
    private static javax.swing.JButton jButton_remove1;
    private static javax.swing.JButton jButton_removeRegularExpression;
    private static javax.swing.JButton jButton_up;
    private static javax.swing.JButton jButton_up1;
    private javax.swing.JCheckBox jCheckBox_exportSuggestionsToXML1;
    private javax.swing.JCheckBox jCheckBox_stopwords;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_background_pic3;
    private javax.swing.JLabel jLabel_separator;
    private static javax.swing.JList jList_RegularExpression;
    private static javax.swing.JList jList_preAnnotated_concept_dictionaries;
    private static javax.swing.JList jList_preAnnotated_concept_dictionaries1;
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
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_CustomRegularExpression;
    private javax.swing.JPanel jPanel_SubContainer;
    private javax.swing.JPanel jPanel_VerifyRegularExpression;
    private javax.swing.JPanel jPanel_bar;
    private javax.swing.JPanel jPanel_bottom;
    private javax.swing.JPanel jPanel_conceptDictionaries;
    private javax.swing.JPanel jPanel_containor;
    private javax.swing.JPanel jPanel_coordination;
    private javax.swing.JPanel jPanel_detailsOfCustomRegularExpression;
    private javax.swing.JPanel jPanel_dictionarylist_and_coordination_radio_button;
    private javax.swing.JPanel jPanel_dictionarylist_and_coordination_radio_button1;
    private javax.swing.JPanel jPanel_listOfRegularExpression;
    private javax.swing.JPanel jPanel_others;
    private javax.swing.JPanel jPanel_title_dictionaries_list;
    private javax.swing.JPanel jPanel_title_dictionaries_list1;
    private javax.swing.JPanel jPanel_title_dictionaries_list2;
    private javax.swing.JPanel jPanel_title_dictionaries_list3;
    private javax.swing.JPanel jPanel_title_dictionaries_list4;
    private javax.swing.JPanel jPanel_topbar;
    private javax.swing.JPasswordField jPasswordField1;
    private static javax.swing.JRadioButton jRadioButton_diifferentWeight;
    private javax.swing.JRadioButton jRadioButton_newannotationspan_check;
    private javax.swing.JRadioButton jRadioButton_newannotationspan_exact;
    private javax.swing.JRadioButton jRadioButton_oracle_allmatches;
    private javax.swing.JRadioButton jRadioButton_oracle_wholeword;
    private javax.swing.JRadioButton jRadioButton_sameWeight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField_ClassName;
    private javax.swing.JTextField jTextField_Comment;
    private javax.swing.JTextField jTextField_RegularExpression;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}

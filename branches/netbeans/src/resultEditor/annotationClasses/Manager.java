/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Manager.java
 *
 * Created on Jul 8, 2010, 10:57:09 AM
 */

package resultEditor.annotationClasses;

import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 * modified on Nov 8, 2010 to add multiple deleting at one time
 */
public class Manager extends javax.swing.JFrame {
    
    protected userInterface.GUI __gui;
    protected String currentAnnotatedClassName;
    protected String newAnnoatatedClassName;
    protected Color  currentcolor;
    protected Color  newcolor;
    protected javax.swing.Icon icon;

    /**Dialog which will be poped up to confirm operation of deleting.*/
    protected DeleteConfirm deleteconfirm_dialog;

    /** Creates new form Manager */
    public Manager(userInterface.GUI _gui) {
        
        this.__gui = _gui;
        
        _gui.setEnabled(false);

        // init
        initComponents();
        this.icon = jLabel_notdone.getIcon();
        jLabel_warning.setVisible(false);
        //initialize the color combo box
        initColorCombo();
        this.jPanel_buttonHolder.setVisible( false );
        this.jTextField_shortcomment.setText( null );
        this.jTextPane1.setText( null );

        // set dialog location
        setDialogLocation();
        // show annotated classes on screen
        listdisplay();

        // hide components
        setComponentsEnabled( false );

    }

    private void setComponentsEnabled(boolean flag){
        jTextField_classname.setEnabled( flag );
        if (!flag) jTextField_classname.setText(null);
        jComboBox_color.setEnabled(flag);
        jButton_delete.setEnabled(flag);

        if( !flag ){
            jComboBox_color.removeAllItems();
            jComboBox_color.addItem( Color.white );
            jComboBox_color.setSelectedIndex(0);
        }
    }

    /**put this dialog in the middle of eHOST's main window*/
    private void setDialogLocation(){
        if ( __gui == null ) return;

        int parentX = __gui.getX(), parentY = __gui.getY();
        int parentWidth = __gui.getWidth(), parentHeight = __gui.getHeight();
        int width = this.getWidth(), height =  this.getHeight();

        int x = parentX + (int)((parentWidth-width)/2);
        int y = parentY + (int)((parentHeight-height)/2);

        this.setLocation(x, y);
    }


    /**We already pre-defined some color in the eHOST system for using by
     * class as their highlight background color. This method will return all
     * pre-defined colors to you.
     *
     * @return  return all pre-defined colors to you in a vector.
     */
    private static Vector<Color> getPreDefinedColor(){
        // vector to return
        Vector<Color> listedColor = new Vector<Color>();

        // pre-define color
        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r = 0; r < values.length; r++) {
          for (int g = 0; g < values.length; g++)
            for (int b = 0; b < values.length; b++) {
              if ((r==0)&&(g==0)&&(b==0))
                  continue;
              Color c = new Color(values[r], values[g], values[b]);
              listedColor.add(c);
            }
        }

        return listedColor;
    }


    /**This one returns a un-used color for class to highlight their
     * annotations. It goes throught all pre-defined colors and get a unused
     * color.
     *
     * @return  a unused pre-defined class background color
     * 
     */
    public static Color allocateColor(){
        try{
            Vector<Color> predefinedColors = getPreDefinedColor();
            if(predefinedColors==null)
                return Color.black;

            // go though all pre-defined colors to try to find a un-used color            
            for(Color colorcandidate:predefinedColors){

                if(colorcandidate==null)
                    continue;

                //ResultEditor.AnnotationClasses.Depot classdepot = new ResultEditor.AnnotationClasses.Depot();
                if(Depot.isColorUsedInClass(colorcandidate))
                    continue;
                
                
                // make sure it's a light color
                int r = colorcandidate.getAlpha();
                int b = colorcandidate.getBlue();
                int g = colorcandidate.getGreen();
                double grayLevel = r * 0.299 + g * 0.587 + b * 0.114;
                if(grayLevel <= 192)
                    continue;

                return colorcandidate;
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "~~~~ WARNING ~~~~::1107201438:: fail to "
                    + "allocate color to highlight background for class");
        }

        return Color.black;
    }

    /**This method will check */
    private static boolean colorUsed(Color _color){
        // return true if it's NULL
        if(_color==null)
            return true;

        // return true if it's not a instance of class "Class"
        if(!(_color instanceof Color))
            return true;

        // get all used colors for classes in
        //ResultEditor.AnnotationClasses.Depot classdepot = new ResultEditor.AnnotationClasses.Depot();
        return Depot.isColorUsedInClass(_color);

    }

    /**initialize the color combo box*/
    private void initColorCombo(){

        jComboBox_color.removeAllItems();

        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r = 0; r < values.length; r++) {
          for (int g = 0; g < values.length; g++)
            for (int b = 0; b < values.length; b++) {
              if ((r==0)&&(g==0)&&(b==0))
                  continue;
              Color c = new Color(values[r], values[g], values[b]);
              jComboBox_color.addItem(c);
            }
        }

        jComboBox_color.setRenderer(new resultEditor.newClass.ColorComboRenderer());

    }

    private void listdisplay() {        

        Depot depot = new Depot();
        depot.sort();
        Vector<AnnotationClass> classes = depot.getAll();
        if ( classes == null )
            return;

        //Object [] array = classes.toArray();
        //Arrays.sort(array);

        Vector<Object> listentry = new Vector<Object>();
        for( AnnotationClass thisclass : classes ){
                if ( thisclass == null )
                    continue;

                iListEntry le;
                le = new iListEntry( thisclass.annotatedClassName , thisclass.backgroundColor, icon );
                listentry.add( le );
        }
        jList_color.setListData( listentry );
        jList_color.setCellRenderer(new iListCellRenderer());

    }

    private void saveAndExit(){
        __gui.setEnabled( true );
        //Depot depot =  new Depot();
        config.project.ProjectConf projectconf = new config.project.ProjectConf(
                    env.Parameters.WorkSpace.CurrentProject);
        projectconf.saveConfigure();
        this.dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_notdone = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_color = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jButton_delete = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel_unmodifiedClassName = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jTextField_classname = new javax.swing.JTextField();
        jPanel_buttonHolder = new javax.swing.JPanel();
        jButton_apply = new javax.swing.JButton();
        jButton_cancel = new javax.swing.JButton();
        jComboBox_color = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel_warning = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField_shortcomment = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();

        jLabel_notdone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resultEditor/annotationClasses/classDef.gif"))); // NOI18N
        jLabel_notdone.setText("<html>jLabel3------<br>dsd<br>dsdf</html>");
        jLabel_notdone.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Class Manager");
        setMinimumSize(new java.awt.Dimension(786, 599));
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

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Annotation Classes"));
        jPanel1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jList_color.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jList_color.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_color.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_colorValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_color);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton_delete.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton_delete.setText("Delete");
        jButton_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_deleteActionPerformed(evt);
            }
        });
        jPanel5.add(jButton_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, -1, -1));

        jButton3.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton3.setText("New");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(609, 0, -1, -1));

        jPanel4.add(jPanel5, java.awt.BorderLayout.SOUTH);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 774, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel1.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel7.setBackground(new java.awt.Color(222, 222, 222));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new java.awt.GridLayout(5, 0, 0, 2));

        jPanel8.setBackground(new java.awt.Color(222, 222, 222));

        jLabel2.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel2.setText("Details:");

        jLabel_unmodifiedClassName.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel_unmodifiedClassName.setText("classname");

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel_unmodifiedClassName)
                .addContainerGap(641, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel_unmodifiedClassName))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel8);

        jPanel9.setBackground(new java.awt.Color(222, 222, 222));
        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("Name:");
        jPanel9.add(jLabel1);

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 102));
        jLabel3.setText("Color:");
        jPanel9.add(jLabel3);

        jPanel7.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(222, 222, 222));
        jPanel10.setLayout(new java.awt.GridLayout(1, 2, 20, 0));

        jPanel14.setBackground(new java.awt.Color(222, 222, 222));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jTextField_classname.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField_classnameInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTextField_classname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField_classnameKeyReleased(evt);
            }
        });
        jPanel14.add(jTextField_classname, java.awt.BorderLayout.CENTER);

        jPanel_buttonHolder.setBackground(new java.awt.Color(222, 222, 222));
        jPanel_buttonHolder.setLayout(new java.awt.GridLayout(1, 2));

        jButton_apply.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton_apply.setText("Apply");
        jButton_apply.setMaximumSize(new java.awt.Dimension(60, 29));
        jButton_apply.setMinimumSize(new java.awt.Dimension(60, 29));
        jButton_apply.setPreferredSize(new java.awt.Dimension(60, 29));
        jButton_apply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_applyActionPerformed(evt);
            }
        });
        jPanel_buttonHolder.add(jButton_apply);

        jButton_cancel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton_cancel.setText("Cancel");
        jButton_cancel.setMaximumSize(new java.awt.Dimension(63, 29));
        jButton_cancel.setMinimumSize(new java.awt.Dimension(63, 29));
        jButton_cancel.setPreferredSize(new java.awt.Dimension(63, 29));
        jButton_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_cancelActionPerformed(evt);
            }
        });
        jPanel_buttonHolder.add(jButton_cancel);

        jPanel14.add(jPanel_buttonHolder, java.awt.BorderLayout.EAST);

        jPanel10.add(jPanel14);

        jComboBox_color.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_colorActionPerformed(evt);
            }
        });
        jPanel10.add(jComboBox_color);

        jPanel7.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(222, 222, 222));

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 770, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 31, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(222, 222, 222));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jLabel_warning.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel_warning.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/markerror.png"))); // NOI18N
        jLabel_warning.setText("jLabel2");
        jPanel12.add(jLabel_warning, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel12);

        jPanel1.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel15.setBackground(new java.awt.Color(222, 222, 222));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 102));
        jLabel4.setText("Short Comment：");

        jTextField_shortcomment.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField_shortcommentInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTextField_shortcomment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField_shortcommentKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_shortcommentKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 102));
        jLabel5.setText("Description Doc：");

        jTextPane1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextPane1InputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTextPane1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPane1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTextPane1);

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField_shortcomment, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jTextField_shortcomment, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel15Layout.createSequentialGroup()
                        .add(jLabel5)
                        .addContainerGap(115, Short.MAX_VALUE))
                    .add(jScrollPane2)))
        );

        jPanel1.add(jPanel15, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jButton1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(702, Short.MAX_VALUE)
                .add(jButton1)
                .add(9, 9, 9))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 786, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel13, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        __gui.setEnabled(true);
        saveAndExit();
        __gui.display_repaintHighlighter();
        __gui.showAnnotationCategoriesInTreeView_refresh();
        __gui.display_repaintHighlighter();
        __gui.showValidPositionIndicators_setAll();
        __gui.showValidPositionIndicators();
        __gui.remove_all_underline_highlighter();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList_colorValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_colorValueChanged
        this.jPanel_buttonHolder.setVisible( false );
        this.jLabel_unmodifiedClassName.setText( null );
        this.jTextPane1.setText( null );
        
        jLabel_warning.setVisible(false);
        if( jList_color == null )   return;
        if( jList_color.isSelectionEmpty() )   return;

        
        
        // only works after component got seatle down
        if ( jList_color.getModel().getSize() == Depot.size() ) {
            //|| ((jList_color.getLastVisibleIndex() + 2) == Depot.size()) ) {

            
            this.setComponentsEnabled(true);

            // get current annotated class name
            this.currentAnnotatedClassName = jList_color.getSelectedValue().toString().trim();
            this.jLabel_unmodifiedClassName.setText( this.currentAnnotatedClassName );
            jTextField_classname.setText( this.currentAnnotatedClassName );
            // value sync
            this.newAnnoatatedClassName = this.currentAnnotatedClassName;
            
            try{
                Depot depot = new Depot();
                AnnotationClass aclass = depot.getAnnotatedClass( this.currentAnnotatedClassName );
                this.jTextField_shortcomment.setText( aclass.shortComment  );
                this.jTextPane1.setText( aclass.des );
            }catch(Exception ex){
            }

            // get background color to current annotated class
            Color color = Depot.getColor( this.currentAnnotatedClassName );
            this.currentcolor = color;
            this.newcolor = color;
            initColorCombo( color );
             
            
             
        }
    }//GEN-LAST:event_jList_colorValueChanged

    private void jComboBox_colorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_colorActionPerformed
        if( (jComboBox_color.getItemCount() == 64 )||(jComboBox_color.getItemCount() == 65 )){
            if( jComboBox_color.getSelectedItem() == null)
                return;
            Color color = (Color)jComboBox_color.getSelectedItem();
            if (this.currentcolor == color)
                return;

            int selectedindex = jList_color.getSelectedIndex();
            this.newcolor = color;
            Depot depot = new Depot();
            depot.changeColor(this.currentAnnotatedClassName, this.newcolor);

            initColorCombo( this.newcolor );
            listdisplay();
            jList_color.setSelectedIndex( selectedindex );

            this.currentcolor = this.newcolor;
        }
    }//GEN-LAST:event_jComboBox_colorActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        saveAndExit();
    }//GEN-LAST:event_formWindowClosing


    /**
     * Call dialog to confirm deleting type while user clicked the button of
     * "delete" on class dialog.
     */
    private void jButton_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_deleteActionPerformed

        // validity check
        if( jList_color.isSelectionEmpty() )
            return;

        // show a dialog and let user confirm mass delete or not
        if( deleteconfirm_dialog == null || !deleteconfirm_dialog.isActive()) {
            deleteconfirm_dialog = new DeleteConfirm(this);
            deleteconfirm_dialog.setVisible(true);
        }else{
            deleteconfirm_dialog.dispose();
            deleteconfirm_dialog = new DeleteConfirm(this);
            deleteconfirm_dialog.setVisible(true);
        }
        
       
    }//GEN-LAST:event_jButton_deleteActionPerformed


    public void deleteClass(){
         // begin delete class
        String classname = jList_color.getSelectedValue().toString().trim();
        Depot depot = new Depot();
        depot.delete( classname );

        listdisplay();
        this.setComponentsEnabled(false);
    }
    public void deleteClass(String classname){
        try{
            // begin delete class
            Depot depot = new Depot();
            depot.delete( classname );
        } catch(Exception ex){
        }

        listdisplay();
        this.setComponentsEnabled(false);
    }

    /**To any selected classes in the list, delete all annotations belongs to them.*/
    public void deleteClass_MASS(){

        if (jList_color.isSelectionEmpty())
            return;
        try{
            int[] indexes = jList_color.getSelectedIndices();

            int size = indexes.length;

            for(int i=size-1; i>=0; i--){
                // begin delete class
                String classname = jList_color.getModel().getElementAt(indexes[i]).toString().trim();
                deleteClass(classname);

                // delete annotaitons
                resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
                depot.deleteAnnotation_belongToClass( classname );
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1011081644:: fail to do mass delete to " +
                    "class(es);" + ex.toString());
        }

        __gui.display_repaintHighlighter();
        __gui.showAnnotationCategoriesInTreeView_refresh();
        __gui.display_repaintHighlighter();
        __gui.showValidPositionIndicators_setAll();
        __gui.showValidPositionIndicators();
        __gui.remove_all_underline_highlighter();
        
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jLabel_warning.setVisible(false);
        
        this.jPanel_buttonHolder.setVisible(true);
        this.jButton_apply.setEnabled(true);
        this.jButton_cancel.setEnabled(true);

        // class "NEW_ANNOTATED_CLASS" already existed
        if( checkExisting() ){
            this.jButton_apply.setEnabled(false);
            setComponentsEnabled(true);
            listdisplay();
            jTextField_classname.setText("NEW_ANNOTATED_CLASS");
            this.jLabel_unmodifiedClassName.setText( "NEW_ANNOTATED_CLASS" );
            this.currentAnnotatedClassName = "NEW_ANNOTATED_CLASS";
            //this.currentcolor = 
            this.currentcolor = Depot.getColor("NEW_ANNOTATED_CLASS");
            initColorCombo( this.currentcolor );
            highlightSpecificEntry( this.currentAnnotatedClassName );
            jLabel_warning.setText("New annotated class\"NEW_ANNOTATED_CLASS\" has existed.");
            commons.Tools.beep();
            return;
        }

        // build a new class "NEW_ANNOTATED_CLASS"
        addNewClass();
        listdisplay();
        setComponentsEnabled( true );
        jTextField_classname.setText("NEW_ANNOTATED_CLASS");
        this.currentAnnotatedClassName = "NEW_ANNOTATED_CLASS";
        this.currentcolor = Depot.getColor("NEW_ANNOTATED_CLASS");
        initColorCombo( this.currentcolor );
        highlightSpecificEntry( this.currentAnnotatedClassName );
        return;
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField_classnameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_classnameKeyReleased
        jLabel_warning.setVisible( false );
        this.jPanel_buttonHolder.setVisible( false );

        if (( jTextField_classname.getText() != null )&&(jTextField_classname.getText().trim().length() > 0)){
            //System.out.println("  + " + jTextField_classname.getText().trim() );
            String typed = jTextField_classname.getText().trim();// + evt.getKeyChar();
            //System.out.println("  - " + typed);

            if( typed.trim().length() < 1 ) return;
            if( typed.trim().compareTo( this.currentAnnotatedClassName.trim() ) == 0 )
                return;

            if ( repetitiveCheck( typed.trim() )) {
                
                this.jPanel_buttonHolder.setVisible( true );
                this.jButton_apply.setEnabled( false );
                this.jButton_cancel.setEnabled( true );

                jLabel_warning.setText(" Class \"" + typed.trim()  + "\" has existed!!!");
                jLabel_warning.setVisible(true);
                //this.jTextField_classname.setText( this.currentAnnotatedClassName );
                commons.Tools.beep();

            } else {
                
                this.jPanel_buttonHolder.setVisible( true );
                this.jButton_apply.setEnabled( true );
                this.jButton_cancel.setEnabled( true );
                jLabel_warning.setVisible(false);
                                                
                
                return;

            }

        } else {
            log.LoggingToFile.log(Level.SEVERE, "The class name could NOT be empty or black space!!!");
            jLabel_warning.setVisible(true);
            commons.Tools.beep();
        }
        
    }//GEN-LAST:event_jTextField_classnameKeyReleased

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        if( (deleteconfirm_dialog != null ) && ( deleteconfirm_dialog.isVisible() ) )
            return;
        
        this.toFront();
        this.requestFocus();
        commons.Tools.beep();
    }//GEN-LAST:event_formWindowDeactivated

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        __gui.setEnabled( true );
    }//GEN-LAST:event_formWindowClosed

    private void jButton_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_cancelActionPerformed
        jTextField_classname.setText( this.jLabel_unmodifiedClassName.getText() );
        this.jPanel_buttonHolder.setVisible( false );
    }//GEN-LAST:event_jButton_cancelActionPerformed

    private void jButton_applyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_applyActionPerformed
        
        String typed = jTextField_classname.getText().trim();// + evt.getKeyChar();
            //System.out.println("  - " + typed);

            if( typed.trim().length() < 1 ) return;
            if( typed.trim().compareTo( this.currentAnnotatedClassName.trim() ) == 0 )
                return;
            
        this.newAnnoatatedClassName = typed.trim();
                Depot depot = new Depot();
                //System.out.println("["+this.currentAnnotatedClassName+"]-["+this.newAnnoatatedClassName + "]");
                depot.changeClassname( this.currentAnnotatedClassName, this.newAnnoatatedClassName);


                this.jPanel_buttonHolder.setVisible( false );
                this.jButton_apply.setEnabled( false );
                this.jButton_cancel.setEnabled( true );

                this.currentAnnotatedClassName = this.newAnnoatatedClassName;
                
                resultEditor.annotations.Depot.renClassInAnnotations( this.jLabel_unmodifiedClassName.getText(), currentAnnotatedClassName );

                
                jLabel_warning.setVisible(false);
                

                listdisplay();
                highlightSpecificEntry( this.currentAnnotatedClassName );
                
                
    }//GEN-LAST:event_jButton_applyActionPerformed

    private void jTextField_classnameInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField_classnameInputMethodTextChanged
        jLabel_warning.setVisible( false );
        this.jPanel_buttonHolder.setVisible( false );

        if (( jTextField_classname.getText() != null )&&(jTextField_classname.getText().trim().length() > 0)){
            //System.out.println("  + " + jTextField_classname.getText().trim() );
            String typed = jTextField_classname.getText().trim();// + evt.getKeyChar();
            //System.out.println("  - " + typed);

            if( typed.trim().length() < 1 ) return;
            if( typed.trim().compareTo( this.currentAnnotatedClassName.trim() ) == 0 )
                return;

            if ( repetitiveCheck( typed.trim() )) {
                
                this.jPanel_buttonHolder.setVisible( true );
                this.jButton_apply.setEnabled( false );
                this.jButton_cancel.setEnabled( true );

                jLabel_warning.setText(" Class \"" + typed.trim()  + "\" has existed!!!");
                jLabel_warning.setVisible(true);
                //this.jTextField_classname.setText( this.currentAnnotatedClassName );
                commons.Tools.beep();

            } else {
                
                this.jPanel_buttonHolder.setVisible( true );
                this.jButton_apply.setEnabled( true );
                this.jButton_cancel.setEnabled( true );
                jLabel_warning.setVisible(false);
                                                
                
                return;

            }

        } else {
            log.LoggingToFile.log(Level.SEVERE, "The class name could NOT be empty or black space!!!");
            jLabel_warning.setVisible(true);
            commons.Tools.beep();
        }
    }//GEN-LAST:event_jTextField_classnameInputMethodTextChanged

    private void jTextPane1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPane1KeyReleased
        updateDes( String.valueOf( evt.getKeyChar() ) );
    }//GEN-LAST:event_jTextPane1KeyReleased

    private void jTextPane1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextPane1InputMethodTextChanged
        updateDes();
    }//GEN-LAST:event_jTextPane1InputMethodTextChanged

    private void jTextField_shortcommentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_shortcommentKeyPressed
        
    }//GEN-LAST:event_jTextField_shortcommentKeyPressed

    private void jTextField_shortcommentInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField_shortcommentInputMethodTextChanged
        updateShortComment();
    }//GEN-LAST:event_jTextField_shortcommentInputMethodTextChanged

    private void jTextField_shortcommentKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_shortcommentKeyTyped
        updateShortComment( String.valueOf( evt.getKeyChar() ) );
    }//GEN-LAST:event_jTextField_shortcommentKeyTyped

     private void updateDes() {
        updateDes( null );
    }
     
    private void updateDes(String c) {
        String classname = this.jLabel_unmodifiedClassName.getText().trim();// + evt.getKeyChar();
        //System.out.println("  - " + typed);

        if ((classname == null) || (classname.trim().length() < 1)) {
            return;
        }


        String text =  this.jTextPane1.getText();
        int caret = this.jTextPane1.getCaretPosition();
        String des = text.subSequence(0, caret) + c + text.subSequence( caret, text.length() );
        

        Depot depot = new Depot();
        //System.out.println("["+this.currentAnnotatedClassName+"]-["+this.newAnnoatatedClassName + "]");
        depot.setDes(classname, des);

        jLabel_warning.setVisible(false);


        //listdisplay();
        //highlightSpecificEntry( this.currentAnnotatedClassName );

    }
    
    private void updateShortComment() {
        updateShortComment( null );
    }
    private void updateShortComment(String c) {
        
        String classname = this.jLabel_unmodifiedClassName.getText().trim();// + evt.getKeyChar();
        //System.out.println("  - " + typed);

        if ((classname == null) || (classname.trim().length() < 1)) {
            return;
        }


        
        String text =  this.jTextField_shortcomment.getText();
        int caret = this.jTextField_shortcomment.getCaretPosition();
        String newComment = text.subSequence(0, caret) + c + text.subSequence( caret, text.length() );

        //String newComment = this.jTextField_shortcomment.getText() + c;
        
        Depot depot = new Depot();
        //System.out.println("["+this.currentAnnotatedClassName+"]-["+this.newAnnoatatedClassName + "]");
        depot.setShortComment(classname, newComment);

        jLabel_warning.setVisible(false);


        //listdisplay();
        //highlightSpecificEntry( this.currentAnnotatedClassName );

    }
    /**To a given string name of an entry of list, found its index and use
     * color to indicate this entry has been selected.
     */
    private void highlightSpecificEntry(String classname){
        if ( classname == null )
            return;
        
        int size = this.jList_color.getVisibleRowCount();
        for( int i=0; i<size;i++ ) {
            String thisclassname = jList_color.getModel().getElementAt(i).toString().trim();
            if ( thisclassname.compareTo( classname.trim() ) == 0 ) {
                jList_color.setSelectedIndex(i);
                return;
            }
        }
    }

    /**check existing of class "NEW_ANNOTATED_CLASS" */
    private boolean checkExisting(){
        final String str = "NEW_ANNOTATED_CLASS";
        return repetitiveCheck( str );

    }
    private void addNewClass(){
        if ( repetitiveCheck( "NEW_ANNOTATED_CLASS" ) )
            return;
        
        Depot.addElement("NEW_ANNOTATED_CLASS", "eHOST", allocateColor(), true, false );

    }


    /**check whether an annotated classname has */
    private boolean repetitiveCheck(String newClassname ){
        if(( newClassname == null)||(newClassname.trim().length() < 1 ))
            return false;
        //ResultEditor.AnnotationClasses.Depot classdepot = new ResultEditor.AnnotationClasses.Depot();
        if( Depot.isExisting(newClassname) )
            return true;
        else
            return false;
    }


    private void initColorCombo( Color color  ){
        if (color == null)  color = Color.CYAN;        
        jComboBox_color.removeAllItems();
        jComboBox_color.addItem( color );

        int[] values = new int[] { 0, 128, 192, 255 };
        for (int r = 0; r < values.length; r++) {
          for (int g = 0; g < values.length; g++)
            for (int b = 0; b < values.length; b++) {
              if ((r==0)&&(g==0)&&(b==0))
                  continue;
              Color c = new Color(values[r], values[g], values[b]);
              jComboBox_color.addItem(c);
            }
        }
        jComboBox_color.setRenderer(new resultEditor.newClass.ColorComboRenderer());
        jComboBox_color.setSelectedIndex(0);
    }
    /**
    * @param args the command line arguments
    */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_apply;
    private javax.swing.JButton jButton_cancel;
    private javax.swing.JButton jButton_delete;
    private javax.swing.JComboBox jComboBox_color;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel_notdone;
    private javax.swing.JLabel jLabel_unmodifiedClassName;
    private javax.swing.JLabel jLabel_warning;
    private javax.swing.JList jList_color;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_buttonHolder;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField_classname;
    private javax.swing.JTextField jTextField_shortcomment;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Editor.java
 *
 * Created on Sep 7, 2010, 3:40:12 PM
 */
package relationship.complex.creation;

import java.awt.Dimension;
import java.util.HashMap;
import relationship.complex.dataTypes.RelationshipDef;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JList;
import relationship.simple.dataTypes.AttributeSchemaDef;
import relationship.simple.dataTypes.AttributeList;
import resultEditor.simpleSchema.AttributeListObj;
import resultEditor.simpleSchema.AttributeRenderer;
import resultEditor.simpleSchema.AttributeValueObj;
import resultEditor.simpleSchema.AttributeValueRenderer;

/**
 * This Class will be used to Edit the Complex Relationships within the current schema.
 * A Complex Relationship in the Schema consists of a name and a Regular expression that
 * defines the Classes that it accepts.
 * <pre>
 * For Example:
 *      Name: Test_Revealed, Regex: (Test)(Problem)+
 *
 *      This Complex Relationship will allow users to associate one Test class annotation
 *      with one or more Problem class annotations.
 * <pre>
 * This GUI will display all Complex Relationships in the schema and provide tools
 * for modifying the schema.
 *
 * @see Relationsihp.Complex.DataTypes.ComplexRel
 * @see Relationship.Complex.DataTypes.RelSchemaList
 * @author Chris
 */
public class RelationshipSchemaEditor extends javax.swing.JFrame
{
    private userInterface.GUI __gui;    
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Constructor
     * @param caller - the userInterface.GUI that is creating this editor.
     */
    public RelationshipSchemaEditor(userInterface.GUI caller)
    {
        __gui = caller;
        __gui.setEnabled( false );        
        //Initialize Components... DO NOT USE COMPONENTS BEFORE THIS LINE!!!!
        initComponents();
        
        
        
        postProcess();
    }
    //</editor-fold>

    /**A hash table that used to manager icon resource.*/
    private HashMap<String, Icon> iconTable = new HashMap<String, Icon>();

    /**post processing before displaying the dialog of initial function.*/
    private void postProcess() {
        
        //Set the window size
        this.setSize(1024, 617);
        this.setPreferredSize( new Dimension(1024, 617) );
        
        iconTable.clear();
        iconTable.put("attribute", icon_1.getIcon());
        iconTable.put("attributevalue", icon_2.getIcon());
        
        //Initialize Relationship List data
        updateRelationships();
        this.clearListOfAttributes();
        this.clearListOfValues();
        this.setAttributeButtons( false );
        this.setAttributeValueButtons( false );
        
        this.up.setVisible( false );
        this.down.setVisible( false );
        this.upButton2.setVisible( false );
        this.downButton2.setVisible( false );
        
        //Keep Track of GUI that created this gui
        
        __gui.setEnabled( false );
        jCheckBox1.setEnabled( false );
        jCheckBox1.setSelected( false );

        //Center dialog in caller dialog
        setDialogLocation();
        
    }
    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Update the list of relationship names
     */
    public void updateRelationships()
    {
        Vector<RelationshipDef> listInfo = new Vector<RelationshipDef>();
        for (RelationshipDef rel : env.Parameters.RelationshipSchemas.getRelationships())
        {
            listInfo.add(rel);
        }
        jList_of_relationshipSchemas.setListData(listInfo);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Remove the currently selected ComplexRelationship from the schema, and update
     * the relationshipList.
     *
     * @param list - the list to remove values from.
     */
    private void removeSelectedFromSchema(JList list)
    {
        Object[] att = list.getSelectedValues();
        if (att == null || att.length == 0)
        {
            return;
        }
        for (Object obj : att)
        {
            env.Parameters.RelationshipSchemas.remove((RelationshipDef) obj);
        }
        list.setListData(new Vector());
        updateRelationships();
        
        this.clearListOfAttributes();
        this.clearListOfValues();
        this.setAttributeButtons( false );
        this.setAttributeValueButtons( false );
    }
    
    /**only can be called by the RegexEditor to update changes*/
    protected void config_saveProjectSetting(){
        __gui.config_saveProjectSetting();
    }
    /**
     * Place this dialog in the center of the Caller Dialog.
     */
    private void setDialogLocation()
    {
        this.setLocationRelativeTo( __gui );
        
        /*//If null was passed into the constructor then just return
        if (__gui == null)
        {
            return;
        }

        //If non null gui then center the gui in the Callers window.
        int parentX = gui.getX(), parentY = gui.getY();
        int parentWidth = gui.getWidth(), parentHeight = gui.getHeight();
        int width = this.getWidth(), height = this.getHeight();

        int x = parentX + (int) ((parentWidth - width) / 2);
        int y = parentY + (int) ((parentHeight - height) / 2);

        this.setLocation(x, y);*/
    }
    //</editor-fold>
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        icon_1 = new javax.swing.JLabel();
        icon_2 = new javax.swing.JLabel();
        jPanel_mainContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        removeName = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        addName = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_of_relationshipSchemas = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        removeName1 = new javax.swing.JButton();
        up = new javax.swing.JButton();
        down = new javax.swing.JButton();
        sortButton1 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        nameText = new javax.swing.JTextField();
        addName1 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_of_attributeNames = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        deleteButton2 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        upButton2 = new javax.swing.JButton();
        downButton2 = new javax.swing.JButton();
        sortButton2 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        valueText = new javax.swing.JTextField();
        addValue = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_of_attributeValues = new javax.swing.JList();
        jPanel24 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        exit = new javax.swing.JButton();

        icon_1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/relationship/complex/creation/attribute.png"))); // NOI18N
        icon_1.setText("jLabel4");

        icon_2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/relationship/complex/creation/attributevalue.png"))); // NOI18N
        icon_2.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relationship Schema Editor");
        setMinimumSize(new java.awt.Dimension(500, 400));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel_mainContainer.setLayout(new java.awt.GridLayout(1, 3, 4, 0));

        jPanel1.setLayout(new java.awt.GridLayout(1, 2, 10, 10));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel10.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel10.setLayout(new java.awt.GridLayout(1, 0));

        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        removeName.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        removeName.setText("Remove Selected");
        removeName.setToolTipText("<html>Remove the relationship that is selected in the list. All to/from information<br> associated with the relationship will also be deleted.</html>");
        removeName.setEnabled(false);
        removeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeNameActionPerformed(evt);
            }
        });
        jPanel11.add(removeName);

        edit.setText("Edit");
        edit.setEnabled(false);
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        jPanel11.add(edit);

        addName.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        addName.setText("Add");
        addName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNameActionPerformed(evt);
            }
        });
        jPanel11.add(addName);

        jPanel10.add(jPanel11);

        jPanel9.add(jPanel10, java.awt.BorderLayout.PAGE_END);

        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(0, 51, 153));
        jPanel14.setMinimumSize(new java.awt.Dimension(85, 16));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(0, 51, 153));
        jLabel2.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Relationship");
        jPanel14.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel14, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jList_of_relationshipSchemas.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jList_of_relationshipSchemas.setToolTipText("<html>The list of relationships that are allowed within this schema.</html>");
        jList_of_relationshipSchemas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList_of_relationshipSchemasMouseReleased(evt);
            }
        });
        jList_of_relationshipSchemas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_of_relationshipSchemasValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_of_relationshipSchemas);

        jPanel13.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        jPanel_mainContainer.add(jPanel1);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(102, 153, 255));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Book Antiqua", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Attribute Types");
        jPanel15.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204), 2));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel16.setLayout(new java.awt.GridLayout(2, 0));

        jPanel17.setLayout(new java.awt.GridLayout(1, 4, 1, 0));

        removeName1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        removeName1.setText("Delete");
        removeName1.setToolTipText("<html>Remove the attribute that is selected in the list. Any allowable values<br> associated with the annotation will also be deleted.</html>");
        removeName1.setEnabled(false);
        removeName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeName1ActionPerformed(evt);
            }
        });
        jPanel17.add(removeName1);

        up.setText("<-");
        up.setEnabled(false);
        up.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upActionPerformed(evt);
            }
        });
        jPanel17.add(up);

        down.setText("->");
        down.setEnabled(false);
        down.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downActionPerformed(evt);
            }
        });
        jPanel17.add(down);

        sortButton1.setText("Sort");
        sortButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortButton1ActionPerformed(evt);
            }
        });
        jPanel17.add(sortButton1);

        jPanel16.add(jPanel17);

        jPanel18.setLayout(new java.awt.BorderLayout());

        nameText.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        nameText.setToolTipText("<html>Type the name of a new Attribute here.<br> Add to the list by pressing enter or the 'Add' button.</html>");
        nameText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextKeyPressed(evt);
            }
        });
        jPanel18.add(nameText, java.awt.BorderLayout.CENTER);

        addName1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        addName1.setText("Add");
        addName1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addName1ActionPerformed(evt);
            }
        });
        jPanel18.add(addName1, java.awt.BorderLayout.LINE_END);

        jPanel16.add(jPanel18);

        jPanel12.add(jPanel16, java.awt.BorderLayout.PAGE_END);

        jPanel19.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);

        list_of_attributeNames.setFont(new java.awt.Font("American Typewriter", 0, 12)); // NOI18N
        list_of_attributeNames.setToolTipText("<html>The list of attributes that are allowed within this schema.</html>");
        list_of_attributeNames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                list_of_attributeNamesMouseReleased(evt);
            }
        });
        list_of_attributeNames.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_of_attributeNamesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list_of_attributeNames);

        jPanel19.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel12.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel_mainContainer.add(jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(51, 153, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Book Antiqua", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Allowable Values");
        jPanel8.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel8, java.awt.BorderLayout.NORTH);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 204), 2));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(2, 0));

        jPanel20.setLayout(new java.awt.BorderLayout());

        deleteButton2.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        deleteButton2.setText("Delete");
        deleteButton2.setToolTipText("<html>Remove the allowable value that is selected in the list.</html>");
        deleteButton2.setEnabled(false);
        deleteButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton2ActionPerformed(evt);
            }
        });
        jPanel20.add(deleteButton2, java.awt.BorderLayout.CENTER);

        jPanel21.setLayout(new java.awt.GridLayout(1, 0));

        upButton2.setText("<-");
        upButton2.setEnabled(false);
        upButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButton2ActionPerformed(evt);
            }
        });
        jPanel21.add(upButton2);

        downButton2.setText("->");
        downButton2.setEnabled(false);
        downButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButton2ActionPerformed(evt);
            }
        });
        jPanel21.add(downButton2);

        sortButton2.setText("Sort");
        sortButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortButton2ActionPerformed(evt);
            }
        });
        jPanel21.add(sortButton2);

        jPanel20.add(jPanel21, java.awt.BorderLayout.EAST);

        jPanel7.add(jPanel20);

        jPanel22.setLayout(new java.awt.BorderLayout());

        valueText.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        valueText.setToolTipText("<html>Type an allowable value for the currently selected attribute type.<br>  Add to the list by pressing enter or the \"Add\" button.");
        valueText.setEnabled(false);
        valueText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                valueTextKeyPressed(evt);
            }
        });
        jPanel22.add(valueText, java.awt.BorderLayout.CENTER);

        addValue.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        addValue.setText("Add");
        addValue.setToolTipText("<html>Add a value to the currently selected Attribute</html>");
        addValue.setEnabled(false);
        addValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addValueActionPerformed(evt);
            }
        });
        jPanel22.add(addValue, java.awt.BorderLayout.LINE_END);

        jPanel7.add(jPanel22);

        jPanel5.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel23.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBorder(null);

        list_of_attributeValues.setFont(new java.awt.Font("American Typewriter", 0, 12)); // NOI18N
        list_of_attributeValues.setToolTipText("<html> The list of values that are allowed for the selected attribute.</html>");
        list_of_attributeValues.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_of_attributeValuesValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(list_of_attributeValues);

        jPanel23.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel24.setLayout(new java.awt.BorderLayout());

        jCheckBox1.setFont(new java.awt.Font("American Typewriter", 0, 11)); // NOI18N
        jCheckBox1.setText("Default Value");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel24.add(jCheckBox1, java.awt.BorderLayout.CENTER);

        jPanel23.add(jPanel24, java.awt.BorderLayout.SOUTH);

        jPanel5.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel_mainContainer.add(jPanel4);

        getContentPane().add(jPanel_mainContainer, java.awt.BorderLayout.CENTER);

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel6.setLayout(new java.awt.BorderLayout());

        exit.setText("Done");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jPanel6.add(exit, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel6, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //<editor-fold defaultstate="collapsed" desc="Event Handlers">
    /**
     * Call this whenever the currently selected attribute is changed.  If an
     * attribute name is selected enable deletion, and enable addition of Attribute
     * values.  If nothing is selected then disable this actions.
     * @param evt
     */
    private void jList_of_relationshipSchemasValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jList_of_relationshipSchemasValueChanged
    {//GEN-HEADEREND:event_jList_of_relationshipSchemasValueChanged
        
        
        Object[] values = jList_of_relationshipSchemas.getSelectedValues();
        //If only one Relationship is selected then it can be edited and removed
        if (values.length == 1)
        {
            removeName.setEnabled(true);
            edit.setEnabled(true);
        }
        //If more than one Relationship is selected.. they can be mass removed but not mass edited
        else if (values.length > 1)
        {
            removeName.setEnabled(true);
            edit.setEnabled(false);
        }
        //If no Relationships are selected then disable deleting/editing
        else if (values.length == 0)
        {
            //Disable deletion of Attributes
            removeName.setEnabled(false);
            edit.setEnabled(false);
        }

    }//GEN-LAST:event_jList_of_relationshipSchemasValueChanged

    RegexEditor editor;
    /**
     * When the 'Add' button is pressed pull up new Editor to create a new
     * Relationship.
     * @param evt - the event that caused this function to be called.
     */
    private void addNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addNameActionPerformed
    {//GEN-HEADEREND:event_addNameActionPerformed
        this.clearListOfAttributes();
        this.clearListOfValues();
        this.setAttributeButtons( false );
        this.setAttributeValueButtons( false );
        
        if(editor!=null)
            editor.dispose();               
        
        editor = new RegexEditor(null, this);
        editor.setVisible(true);
        
    }//GEN-LAST:event_addNameActionPerformed

    /**
     * When the remove button under the Attribute list is pressed remove the currently
     * selected attribute name.
     * @param evt
     */
    private void removeNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeNameActionPerformed
    {//GEN-HEADEREND:event_removeNameActionPerformed
        removeSelectedFromSchema(jList_of_relationshipSchemas);
    }//GEN-LAST:event_removeNameActionPerformed

    /**
     * Update the complex choices in the main GUI when this GUI closes
     * TODO: See if this is still necessary with refactored Relationship building.
     * @param evt
     */
    /**
     * Close the gui when the 'Done'/exit button is pressed.
     * @param evt
     */
    private void exitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitActionPerformed
    {//GEN-HEADEREND:event_exitActionPerformed
        __gui.config_saveProjectSetting();
        __gui.setEnabled( true );
        
        this.dispose();
    }//GEN-LAST:event_exitActionPerformed

    /**
     * Open up a new Complex Relationship Editor with the selected Relationship's
     * info already filled in.  Users can then modify existing relationships
     * @param evt
     */
    private void editActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editActionPerformed
    {//GEN-HEADEREND:event_editActionPerformed
        RelationshipDef rel = (RelationshipDef) jList_of_relationshipSchemas.getSelectedValue();
        removeSelectedFromSchema(jList_of_relationshipSchemas);
        RegexEditor editor = new RegexEditor(rel, this);
        this.editor = editor;
        editor.setVisible(true);
    }//GEN-LAST:event_editActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(editor!=null){
            __gui.config_saveProjectSetting();
            editor.dispose();
        }
        
        __gui.setEnabled( true );
        
    }//GEN-LAST:event_formWindowClosing

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        if( editor != null ) 
            return;
        
        {
            this.toFront(); 
            this.requestFocus();
            commons.Tools.beep();
        }
    }//GEN-LAST:event_formWindowDeactivated

    private void removeName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeName1ActionPerformed
        this.clearListOfValues();
        this.setAttributeValueButtons( false );
        
        int selectedIndex = this.list_of_attributeNames.getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        

        AttributeSchemaDef att = this.getSelectedAttribute();
        

        if (att == null) {
            return;
        }
        
        
        
        
        RelationshipDef relationship = this.getSelectedRelationshipSchema();
        if( relationship == null )
            return;
        
        
            relationship.getAttributes().Remove( att );        
            
        this.listAttributes( this.getSelectedRelationshipSchema().getAttributes() );
        
    }//GEN-LAST:event_removeName1ActionPerformed

    private void upActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upActionPerformed

    }//GEN-LAST:event_upActionPerformed

    private void downActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downActionPerformed

  
    }//GEN-LAST:event_downActionPerformed

    private void sortButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sortButton1ActionPerformed

    private void nameTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextKeyPressed
        if (evt.getKeyChar() == '\n') {
            addNewRelationshipFromTextBox();
        }
    }//GEN-LAST:event_nameTextKeyPressed

    /**user clicked on the button "add" under the list of attributes to add new attribtues*/
    private void addName1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addName1ActionPerformed
        addNewRelationshipFromTextBox();
    }//GEN-LAST:event_addName1ActionPerformed

    
    /**add new attributes while user clicked the button of "add" under the list of attributes.*/
    private void addNewRelationshipFromTextBox(){
        
        if ((nameText.getText() == null) || (nameText.getText().trim().length() < 1)) {
            return;
        }
        
        RelationshipDef selectedRelationship = this.getSelectedRelationshipSchema();
        if( selectedRelationship == null ) 
            return;
        AttributeSchemaDef toAdd = new AttributeSchemaDef(nameText.getText(), new Vector<String>());
        selectedRelationship.getAttributes().Add( toAdd );
        
        // reset buttons
        this.clearListOfAttributes();
        this.clearListOfValues();
        this.setAttributeButtons( false );
        this.setAttributeValueButtons( false );
        
        // relist attributes for this relationship schema 
        selectedRelationship = this.getSelectedRelationshipSchema();        
        this.listAttributes( selectedRelationship.getAttributes() );     
        
        nameText.requestFocus();
    }
    
    
    private void list_of_attributeNamesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_of_attributeNamesMouseReleased

    }//GEN-LAST:event_list_of_attributeNamesMouseReleased

    private void list_of_attributeNamesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_of_attributeNamesValueChanged
        
        // return if the component is adjusting
        if (evt.getValueIsAdjusting()) {
            return;
        }
      
        this.clearListOfValues();
        this.setAttributeButtons( false );
        this.setAttributeValueButtons( false );
        
        // get the selection index
        int selectedIndex = this.list_of_attributeNames.getSelectedIndex();

        if (selectedIndex < 0) { // non item is selected in the list of attribtues
            return;
        }
        
        //Enable deletion of Attributes
        removeName1.setEnabled(true);
        nameText.setText( null );
        nameText.setEditable( true );
        addName1.setEnabled( true );
        
        
        //Enable Editing of Allowable Values
        valueText.setEnabled(true);
        valueText.setText( null );
        valueText.requestFocus();
        addValue.setEnabled(true);
        
        //Update list of allowable values for this attribute
        AttributeListObj ao;
        try {
            ao = (AttributeListObj) this.list_of_attributeNames.getModel().getElementAt(selectedIndex);
        } catch (Exception ex) {
            ao = null;
        }

        if (ao == null) {
            return;
        }
        
        listValuess( ao.getAttribute() );

    }//GEN-LAST:event_list_of_attributeNamesValueChanged

    /**list definitions of attribute values on the list of attributes for 
     * current relationship definition. 
     */
    private void listValuess(AttributeSchemaDef attribute){
        
        this.clearListOfValues();
        
        list_of_attributeValues.setListData(new Vector());

        if (attribute == null) {
            return;
        }
        Vector<AttributeValueObj> listData = new Vector<AttributeValueObj>();
        for (String value_str : attribute.getAllowedEntries()) {
            AttributeValueObj avo = new AttributeValueObj(value_str, this.iconTable.get("attributevalue"), false , attribute);
            listData.add(avo);
        }
        list_of_attributeValues.setListData(listData);
        list_of_attributeValues.setCellRenderer(new AttributeValueRenderer());
                                        
        // give a way for user to add new attributes 
        nameText.setEnabled( true );       
        nameText.setText( null );
        addName1.setEnabled( true );
        
    }
    
    private void list_of_attributeValuesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_of_attributeValuesValueChanged

        if (list_of_attributeValues.getSelectedIndex() == -1) {
            jCheckBox1.setEnabled( false );
            jCheckBox1.setSelected( false );
            return;
        }

        if (list_of_attributeValues.getSelectedIndex() >= 0) {
            deleteButton2.setEnabled(true);
            jCheckBox1.setEnabled( true );
            
            if (list_of_attributeValues.getSelectedIndex() == 0) {
                upButton2.setEnabled(false);
            } else {
                upButton2.setEnabled(true);
            }

            if (list_of_attributeValues.getSelectedIndex() == list_of_attributeValues.getModel().getSize() - 1) {
                downButton2.setEnabled(false);
            } else {
                downButton2.setEnabled(true);
            }
            
            // get current selected value
            AttributeValueObj attvalueobj = (AttributeValueObj) list_of_attributeValues.getSelectedValue();
            String value = attvalueobj.getAttributeValue();
            AttributeListObj attribute = (AttributeListObj) this.list_of_attributeNames.getSelectedValue();
            String defaultvalue = attribute.getAttribute().getDefault();
            if( defaultvalue == null   ){
                this.jCheckBox1.setSelected( false);
            }else{
                if( defaultvalue.compareTo(value) == 0 )
                    this.jCheckBox1.setSelected( true );
                else
                    this.jCheckBox1.setSelected( false);
            }

        } else {
            jCheckBox1.setEnabled( false );
            jCheckBox1.setSelected( false );
            deleteButton2.setEnabled(false);
            upButton2.setEnabled(false);
            downButton2.setEnabled(false);
        }
    }//GEN-LAST:event_list_of_attributeValuesValueChanged

    private void upButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButton2ActionPerformed
        
    }//GEN-LAST:event_upButton2ActionPerformed

    private void downButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButton2ActionPerformed
        
    }//GEN-LAST:event_downButton2ActionPerformed

    private void sortButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortButton2ActionPerformed
        
    }//GEN-LAST:event_sortButton2ActionPerformed

    private void valueTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valueTextKeyPressed
        if (evt.getKeyChar() == '\n') {
            addAttValue();
        }
    }//GEN-LAST:event_valueTextKeyPressed

    private void addValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addValueActionPerformed
        addAttValue();
    }//GEN-LAST:event_addValueActionPerformed

    /**add a new attribute value into the definition for current relationship.*/
    private void addAttValue(){
        
         if( valueText.getText() == null )
            return;
        if( valueText.getText().trim().length() < 1 )
            return;

        if (this.list_of_attributeNames.getSelectedIndex() == -1) {
            return;
        }

        // get current attribute
        AttributeListObj ao = null;
        try {
            ao = (AttributeListObj) this.list_of_attributeNames.getSelectedValue();
        } catch (Exception ex) {
            ao = null;
        }

        if (ao == null) {
            return;
        }

        AttributeSchemaDef currentAtt = getSelectedAttribute();
        if( currentAtt == null  )
            return;

        String text = this.valueText.getText().trim();
        for ( String existingValue : currentAtt.getAllAllowedEntries() ){
            if( existingValue == null )
                continue;
            if ( existingValue.compareTo( text.trim() ) == 0 ){
                commons.Tools.beep();
                valueText.setText(null);
                return;
            }
        }
        currentAtt.put(text);

        this.listValuess( getSelectedAttribute() );
        // list_of_attributeNames.setSelectedValue(text, true);
        valueText.setText("");
    }
    
    
    private AttributeSchemaDef getSelectedAttribute(){
         int selectedIndex = this.list_of_attributeNames.getSelectedIndex();        

        // end, if no item is selected
        if (selectedIndex < 0 ) {
            return null;
        }

        this.list_of_attributeValues.setDragEnabled(false);
       
        // get current relationship schema
        Object selecteditem = list_of_attributeNames.getSelectedValue();
        if( selecteditem == null )
            return null;
        
        AttributeListObj selectedRelationship = (AttributeListObj) selecteditem;
        if( selectedRelationship == null )
            return null;
        
        return selectedRelationship.getAttribute();
    }
    
    private void jList_of_relationshipSchemasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_of_relationshipSchemasMouseReleased
                
        // get current selected relationship schema
        RelationshipDef selectedRelationship = getSelectedRelationshipSchema(); 
        if( selectedRelationship == null )
            return;
        
        AttributeList attributes = selectedRelationship.getAttributes(); 
        listAttributes( attributes );        
        
    }//GEN-LAST:event_jList_of_relationshipSchemasMouseReleased

    private void deleteButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton2ActionPerformed
        AttributeSchemaDef currentAttribute = this.getSelectedAttribute();
        if( currentAttribute == null )
            return;
        
        // get the value that we want to remove
        
        AttributeValueObj avo = getSelectedValue();
        if( avo == null )
            return;
        
        currentAttribute.removeValue( avo.getAttributeValue()  );   
        
        if( currentAttribute.hasDefaultValue() ){
            if( currentAttribute.hasDefaultValue( avo.getAttributeValue()  ) )
                currentAttribute.removeDefaultValue();
        }
        
        // refresh the screen
        this.clearListOfValues();
        this.setAttributeValueButtons( false );
        
        
        int index1 = list_of_attributeNames.getSelectedIndex();
        int index2 = list_of_attributeValues.getSelectedIndex();        
        
        //listAttributeValues(ao.getAttribute(), ao.isPublicAttribute());
        this.listAttributes( this.getSelectedRelationshipSchema().getAttributes() );
        this.listValuess( this.getSelectedAttribute());
        
        list_of_attributeNames.setSelectedIndex( index1 );
        
        int size = list_of_attributeValues.getModel().getSize();        
        if(size>0){
            if(index2>=size)
                index2 = size - 1;
            list_of_attributeValues.setSelectedIndex( index2 );
        }
        
    }//GEN-LAST:event_deleteButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // get current selected attribute schema
        if( list_of_attributeNames.getSelectedIndex() < 0 )
            return;
        if( this.list_of_attributeValues.getSelectedIndex() < 0 )
            return;
        AttributeListObj att = (AttributeListObj) this.list_of_attributeNames.getSelectedValue();
        AttributeValueObj value = (AttributeValueObj) this.list_of_attributeValues.getSelectedValue();
        
        if( jCheckBox1.isSelected() )
            att.getAttribute().setDefaultValue( value.getAttributeValue() );
        
        else if( !jCheckBox1.isSelected()  ){
            att.getAttribute().removeDefaultValue();
        }
        
        int index1 = list_of_attributeNames.getSelectedIndex();
        int index2 = list_of_attributeValues.getSelectedIndex();        
        this.listAttributes( this.getSelectedRelationshipSchema().getAttributes() );        
        list_of_attributeNames.setSelectedIndex( index1 );
        
        list_of_attributeValues.setSelectedIndex( index2 );
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    
    
    /**get current selected value.*/
    private AttributeValueObj getSelectedValue() {
        try {
            int index = list_of_attributeValues.getSelectedIndex();
            if (index < 0) {
                return null;
            }

            Object selectedObj = this.list_of_attributeValues.getSelectedValue();
            if (selectedObj == null) {
                return null;
            }
            
            return (AttributeValueObj)selectedObj;
            
        } catch (Exception ex) {
            return null;
        }
    }


    
    /**get the current selected relationship schema*/
    private RelationshipDef getSelectedRelationshipSchema(){
        int selectedIndex = jList_of_relationshipSchemas.getSelectedIndex();        

        // end, if no item is selected
        if (selectedIndex < 0 ) {
            return null;
        }

        this.list_of_attributeNames.setDragEnabled(false);
       
        // get current relationship schema
        Object selecteditem = jList_of_relationshipSchemas.getSelectedValue();
        if( selecteditem == null )
            return null;
        
        RelationshipDef selectedRelationship = (RelationshipDef) selecteditem;
        
        return selectedRelationship;
        
    }
    
    /**get the current selected relationship schema*/
    private RelationshipDef getSelectedAttributeName(){
        int selectedIndex = this.list_of_attributeNames.getSelectedIndex();        

        // end, if no item is selected
        if (selectedIndex < 0 ) {
            return null;
        }

        
       
        // get current relationship schema
        Object selecteditem = jList_of_relationshipSchemas.getSelectedValue();
        if( selecteditem == null )
            return null;
        
        RelationshipDef selectedRelationship = (RelationshipDef) selecteditem;
        
        return selectedRelationship;
        
    }
    
    
    /**list attribute items on the list of attributes for current relationship definition. */
    private void listAttributes(AttributeList attributes){
        this.clearListOfAttributes();
        
        Vector<AttributeListObj> listInfo = new Vector<AttributeListObj>();
        for (AttributeSchemaDef att : attributes.getAttributes() ) {
            AttributeListObj ao = new AttributeListObj(att.getName(), att, this.iconTable.get("attribute"), true);
            listInfo.add(ao);
        }
        this.list_of_attributeNames.setListData(listInfo);
        this.list_of_attributeNames.setCellRenderer(new AttributeRenderer());
        
        // give a way for user to add new attributes 
        nameText.setEnabled( true );       
        nameText.setText( null );
        addName1.setEnabled( true );
        
    }
    
    /**remove contents in the list of attributes*/
    private void clearListOfAttributes(){
        list_of_attributeNames.setListData( new Vector());
    }
    
    /**remove contents in the list of attribute values*/
    private void clearListOfValues(){
        list_of_attributeValues.setListData( new Vector());
    }
    
    /**disable or enable buttons under the list of attributes.*/
    private void setAttributeButtons(boolean isEnabled ){
        removeName1.setEnabled( isEnabled );
        up.setEnabled( isEnabled );
        down.setEnabled( isEnabled );
        sortButton1.setEnabled( isEnabled );
        addName1.setEnabled( isEnabled );
        nameText.setText( null );
        nameText.setEnabled( isEnabled );
    }
    
    /**disable or enable buttons under the list of attribute values.*/
    private void setAttributeValueButtons(boolean isEnabled ){
        deleteButton2.setEnabled( isEnabled );
        upButton2.setEnabled( isEnabled );
        downButton2.setEnabled( isEnabled );
        sortButton2.setEnabled( isEnabled );
        addValue.setEnabled( isEnabled );
        valueText.setText( null );
        valueText.setEnabled( isEnabled );
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addName;
    private javax.swing.JButton addName1;
    private javax.swing.JButton addValue;
    private javax.swing.JButton deleteButton2;
    private javax.swing.JButton down;
    private javax.swing.JButton downButton2;
    private javax.swing.JButton edit;
    private javax.swing.JButton exit;
    private javax.swing.JLabel icon_1;
    private javax.swing.JLabel icon_2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList_of_relationshipSchemas;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_mainContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList list_of_attributeNames;
    private javax.swing.JList list_of_attributeValues;
    private javax.swing.JTextField nameText;
    private javax.swing.JButton removeName;
    private javax.swing.JButton removeName1;
    private javax.swing.JButton sortButton1;
    private javax.swing.JButton sortButton2;
    private javax.swing.JButton up;
    private javax.swing.JButton upButton2;
    private javax.swing.JTextField valueText;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}

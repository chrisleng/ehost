/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Manager.java
 *
 * Created on Jul 8, 2010, 10:57:09 AM
 */
package resultEditor.relationship;

import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;

/**
 * This Class is an Editor used to edit Attributes/Relationships for Annotations
 * and to edit the Relationship Schema.
 * @author leng
 */
public class Editor extends javax.swing.JFrame
{

    /**
     * The gui that called this Editor
     */
    protected userInterface.GUI __gui;
    /**
     * The list of entries to display
     */
    protected Vector<iListable> entries;
    /**
     * The type of this editor
     */
    protected Type type;

    /**The annotation that we are modifying.*/
    protected Annotation __annotation;
    
    /**Tell this method that where the request is sent from, 
     * whether from the annotation editor panel or the comparator panel.
     */
    protected Where __where;
     
    /**
     * The enum for the type of Info a ResultEditor.ComplexRelationship.Editor can
     * edit.
     */
    public enum Type
    {
        //ComplexRelationships,
        //ComplexSchema,
        Attributes        
    }
    
    public enum Where
    {
        EditorPanel,
        ComparatorPanel
    }

    /**
     * Constructor for an Editor.
     * @param gui - the gui that called this
     * @param entries - the entries to edit
     * @param type - the type of info to edit
     * @param   _where  Tell this method that where the request is sent from, 
     * whether from the annotation editor panel or the comparator panel.
     * 
     */
    public Editor(
            userInterface.GUI _gui, 
            Annotation _annotation,
            Vector<iListable> entries, 
            Type type,
            Where _where
            )
    {
        //Set member variables
        this.type = type;
        this.__gui = _gui;
        __gui.setEnabled( false );
        this.entries = entries;
        this.__annotation = _annotation;
        this.__where = _where;
        // init
        initComponents();

        // set dialog location
        setDialogLocation();

        // show passed in Listables on the list
        listdisplay();

        //Create border and SetTitle
        TitledBorder border = (TitledBorder)this.jPanel1.getBorder();
        this.setTitle("Editor");

        //Set border title for relationship schema editor
//        if(type == Type.ComplexSchema)
//        {
//            border.setTitle("Relationship Schema Editor");
//        }
        //Set border title and button state for Relationship editing
//        if(type == Type.ComplexRelationships)
//        {
            border.setTitle("Relationship Editor");
//            jButton3.setVisible(false);
//        }
        //Set border title and button state for Attribute editing
        if(type == Type.Attributes)
        {
            border.setTitle("Annotation Attributes");
            jButton3.setVisible(false);
            jButton_delete.setVisible(false);
        }

    }

    /**
     * Set the dialog location so that it is in the center of th eHost window.
     */
    private void setDialogLocation()
    {
        //If the gui is null then just return cause we can't set the dialog
        //location
        if (__gui == null)
        {
            return;
        }

        //Calculate the value to place the dialog at
        int parentX = __gui.getX(), parentY = __gui.getY();
        int parentWidth = __gui.getWidth(), parentHeight = __gui.getHeight();
        int width = this.getWidth(), height = this.getHeight();

        int x = parentX + (int) ((parentWidth - width) / 2);
        int y = parentY + (int) ((parentHeight - height) / 2);

        this.setLocation(x, y);
    }
    /**
     *  Display the Attributes in the table
     */
    private void listdisplay()
    {
        //Build the model
        PropertyTableModel tableDataModel = build_Table_Data_Model();

        //If the table model is non null then use it.
        if (tableDataModel != null)
        {
            table.setModel(tableDataModel);
            set_PropertiesTable_Attributes();
        }

        // imply table for its changes
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
    }
    /**
     * Build the table model with all of the entries that were passed in to our editor.
     * @return
     */
    private PropertyTableModel build_Table_Data_Model()
    {
        // empty the jList
        clearList();

        //Create a new PropertyTableModel
        PropertyTableModel propertytablemodel = new PropertyTableModel(type);

        //PropertyTable is null then return
        if (propertytablemodel == null)
        {
            log.LoggingToFile.log(Level.SEVERE, " 1008191259 - worng table data model returned! ");
            return null;
        }

        //Empty out the Property Table Model
        propertytablemodel.removeAllRows();

        //Add the entries to the list
        for (iListable entry : this.entries)
        {
            propertytablemodel.addrow(entry);
            
        }

        //return
        return propertytablemodel;

    }
    /**
     * Set up the table properties
     */
    private void set_PropertiesTable_Attributes()
    {
        //Set the renderers and cell editors for each column
        for(int i = 0; i< table.getColumnCount(); i++)
        {
            TableColumn column_left = table.getColumnModel().getColumn(i);
            //Set the cell renderer
            PropertyCellRenderer_AttributeNames renderer_left = new PropertyCellRenderer_AttributeNames();
            column_left.setCellRenderer(renderer_left);
            //Set the cell editor
            TableCellEditor editor = new PropertyCellEditor(this);
            column_left.setCellEditor(editor);
        }
        //Set row height and margin
        table.setRowHeight(24);
        table.setRowMargin(2);

    }

    /**empty the jlist component*/
    private void clearList()
    {
        ((DefaultTableModel) table.getModel()).getDataVector().removeAllElements();
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
    }
   
    /**
     * Save attributes and dispose this editor.
     */
    private void saveAndExit()
    {
       //try{
            saveAttributes();
       //}
       //catch(Exception e)
       {
           //JOptionPane.showMessageDialog(e.getMessage() + " "  + e.getLocalizedMessage());
       }
       __gui.setEnabled( true );
       __gui.display_removeSymbolIndicators();
       __gui.display_showSymbolIndicators();
        this.dispose();
    }

    /**
     * Save changes of attributes to memory of annotations. All operations we
     * did are based on the local data model of the JTable in this dialog. The
     * data model need to be converted and saved before you close this dialog.
     *
     */
    private void saveAttributes()
    {
        __gui.setModified();

        try
        {
             //String f = null;
             //f.equals("hi");
            //Get all of the rows from the table model
            Vector<iListable> rows = PropertyTableModel.rowdata;

            //If the list of rows is null just return
            if (rows == null)
            {
                return;
            }

            //If we're editing out complex schema save the table informatino back to
            //static memory
//            else if (type == Type.ComplexSchema)
//            {
                /*
                env.Parameters.ComplexRelationshipNames = new TreeSet<String>();
                for (iListable row : rows)
                {
                    env.Parameters.ComplexRelationshipNames.add((String) row.getWriteForm());
                }
                gui.updateComplex();
                 * 
                 */
//            }
            //If we're editing relationships for a single Annotation than save them back
            //to that annotation
//            else if (type == Type.ComplexRelationships)
//            {
//                Annotation annotation = WorkSet.currentAnnotation;
//                annotation.relationships = new Vector<AnnotationRelationship>();
//                for (iListable row : rows)
//                {
//                    annotation.relationships.add((AnnotationRelationship) row.getWriteForm());
//                }
//                __gui.updateComplex();
//            }
            //If we're editing Attributes for a single Annotation than save all of the
            //Attributes back to the annotation.
//            else 
                if(type == Type.Attributes)
            {
                Annotation annotation = __annotation;
                if(annotation == null)
                    return;
                annotation.attributes= new Vector<AnnotationAttributeDef>();
                for (iListable row : rows)
                {
                    AnnotationAttributeDef toAdd = (AnnotationAttributeDef) row.getWriteForm();
                    if(toAdd == null || toAdd.name == null || toAdd.value == null)
                        continue;
                    if(toAdd.name.equals("") || toAdd.value.trim().length() < 1)
                        continue;
                    annotation.attributes.add(toAdd);
                }
                
                if( __where == Where.EditorPanel ){
                    __gui.updateAttributes(annotation );
                    __gui.display_diff_checkDifference();
                }else
                    __gui.display_diff_updateAttributes();
            }
        }
        catch(Exception e)
        {
            String s = "";
            StackTraceElement[] trace = e.getStackTrace();
            for(StackTraceElement elt: trace)
            {
                s+= elt.toString() + "\n";
            }
            log.LoggingToFile.log(Level.SEVERE, "Error Saving: " + s);
            JOptionPane.showMessageDialog(__gui, "Error Saving: \n" + e.getMessage() + "\n" + s);

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

        jLabel_notdone = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jButton_delete = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();

        jLabel_notdone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/radiobutton_unchecked_pressed.png"))); // NOI18N
        jLabel_notdone.setText("<html>jLabel3------<br>dsd<br>dsdf</html>");
        jLabel_notdone.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Complex Relationships:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jButton_delete.setText("Delete");
        jButton_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_deleteActionPerformed(evt);
            }
        });

        jButton3.setText("New");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(426, Short.MAX_VALUE)
                .add(jButton3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_delete))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jButton_delete)
                .add(jButton3))
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel6.setLayout(new java.awt.BorderLayout());

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Attribute Name", "Attribute Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(table);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel1.getAccessibleContext().setAccessibleName("Relationship Types:");

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jButton1.setText("Done");
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
                .addContainerGap(517, Short.MAX_VALUE)
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
            .add(0, 603, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 10, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel13, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        saveAndExit();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        saveAndExit();
    }//GEN-LAST:event_formWindowClosing

    /**
     * Delete the selected row from the table.
     * @param evt
     */
    private void jButton_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_deleteActionPerformed

        int selectedRowIndex = table.getSelectedRow();
        PropertyTableModel model = (PropertyTableModel) table.getModel();
        if (model == null)
        {
            return;
        }
        TableCellEditor editor = table.getCellEditor();
        //Must stop editing or it will be saved into the following row when this
        //row is deleted
        if(editor != null)
        {
            editor.stopCellEditing();
        }

        __gui.setModified();

        model.deleteRow(selectedRowIndex);
        table.updateUI();

    }//GEN-LAST:event_jButton_deleteActionPerformed
    /**
     * Add a new value to the list of data
     * @param evt
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        __gui.setModified();
        PropertyTableModel model = (PropertyTableModel) table.getModel();
        if (model == null)
        {
            return;
        }
        model.addNewValue();

        table.updateUI();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        this.requestFocus();
        this.toFront();
        commons.Tools.beep();
    }//GEN-LAST:event_formWindowDeactivated

    /**
     * Doesn't do anything... Not sure how to make netbeans delete it...
     * @param evt
     */    /**
     * Used to enable the delete button from the outside(or disable).
     * @param set
     */
    public void setDeleteEnabled(Boolean set)
    {
        jButton_delete.setEnabled(set);
        jButton_delete.setVisible(set);
    }
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton_delete;
    private javax.swing.JLabel jLabel_notdone;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}

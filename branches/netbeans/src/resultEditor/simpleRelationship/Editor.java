/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Manager.java
 *
 * Created on Jul 8, 2010, 10:57:09 AM
 */

package resultEditor.simpleRelationship;

import resultEditor.annotations.AnnotationAttributeDef;
import resultEditor.workSpace.WorkSet;
import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author leng
 */
public class Editor extends javax.swing.JFrame {

    private static Vector<sTableData> tabledata = new Vector<sTableData>();
    
    protected userInterface.GUI gui;
    protected String currentAnnotatedClassName;
    protected String newAnnoatatedClassName;
    protected Color  currentcolor;
    protected Color  newcolor;
    protected javax.swing.Icon icon;

    protected resultEditor.annotations.Annotation currentannotation;

    public static sTableData getRowData(int rowindex){
        return tabledata.get(rowindex);
    }
    /** Creates new form Manager */
    public Editor(userInterface.GUI gui) {
        
        this.gui = gui;

        // init
        initComponents();

        // set dialog location
        setDialogLocation();
        // show annotated classes on screen
        listdisplay();
              
    }


    /**put this dialog in the middle of eHOST's main window*/
    private void setDialogLocation(){
        if ( gui == null ) return;

        int parentX = gui.getX(), parentY = gui.getY();
        int parentWidth = gui.getWidth(), parentHeight = gui.getHeight();
        int width = this.getWidth(), height =  this.getHeight();

        int x = parentX + (int)((parentWidth-width)/2);
        int y = parentY + (int)((parentHeight-height)/2);

        this.setLocation(x, y);
    }

 

    private void listdisplay(){
        PropertyTableModel tableDataModel = build_Table_Data_Model();
        if ( tableDataModel != null )
            table.setModel( tableDataModel );


        if ( tableDataModel != null )
            set_PropertiesTable_Attributes();
        
        // imply table for its changes
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
    }

    private PropertyTableModel build_Table_Data_Model() {
        // empty the jList
        clearList();

        PropertyTableModel propertytablemodel = new PropertyTableModel();

        if (propertytablemodel == null) {
            log.LoggingToFile.log(Level.SEVERE," 1008191259 - worng table data model returned! ");
            return null;
        }
        // ge current annotation
        currentannotation = resultEditor.workSpace.WorkSet.currentAnnotation;
        if ( currentannotation == null )               
            return propertytablemodel;

        if ( currentannotation.attributes == null )    
            return propertytablemodel;

        // get all normaltionships belongs to current annotation
        Vector<resultEditor.annotations.AnnotationAttributeDef> normalrelationships
                = currentannotation.attributes;

        Vector<String> entries = new Vector<String>();

        // try to show details of each normal relationship
        int size = normalrelationships.size();
        //Object [] nrs = normalrelationships.toArray();
        //Arrays.sort(nrs);

        
        propertytablemodel.removeAllRows();

        //int location_row = -1;
        for( resultEditor.annotations.AnnotationAttributeDef normalrelationship : normalrelationships  ){
            if ( normalrelationship == null )
                    continue;
            if( normalrelationship.name == null )
                continue;
            if( normalrelationship.value == null )   {
                continue;
            }

            
                // if slot value equal to null
                if ( normalrelationship.value  == null ) {
                    propertytablemodel.addrow(
                        //new sTableData("Markable", null,
                        new sTableData("attributes", null,
                            
                            normalrelationship.name,
                            null
                            )
                        );
                }
                else
                // other attributes, such as attributes to a complex relationship
                {
                    propertytablemodel.addrow(
                        new sTableData( "attributes", null,
                            normalrelationship.name, normalrelationship.value  ) );
                }
                // set editor to value of class/markables
                //if ( str == null ){
                //    TableColumn column = table.getColumnModel().getColumn(1);
                //    String[] values = new String[] {"hello1", "hello2"};
                //    column.setCellEditor(new PropertyCellEditor(values));
                //}

            

        }

        return propertytablemodel;

    }

    private void set_PropertiesTable_Attributes(){

        TableColumn column = table.getColumnModel().getColumn(1);
        PropertyCellRenderer renderer = new PropertyCellRenderer();
        column.setCellRenderer(renderer);

        TableCellEditor editor = new PropertyCellEditor();
        column.setCellEditor(editor);


        TableColumn column_left = table.getColumnModel().getColumn(0);
        PropertyCellRenderer_AttributeNames renderer_left = new PropertyCellRenderer_AttributeNames();
        column_left.setCellRenderer(renderer_left);

        TableCellEditor editor_leftColumn = new PropertyCellEditor_leftColumn();
        column_left.setCellEditor(editor_leftColumn);


        table.setRowHeight(22);
        table.setRowMargin(2);

    }

    /**empty the jlist component*/
    private void clearList(){
        tabledata.clear();
        
        ((DefaultTableModel) table.getModel()).getDataVector().removeAllElements();
        ((DefaultTableModel) table.getModel()).fireTableDataChanged();
        //Vector empty = new Vector();
        //jList_color.setListData( empty );
    }

    private void saveAndExit(){
        saveAttributes();
        this.dispose();
    }


    /**
     * Save changes of attributes to memory of annotations. All operations we
     * did are based on the local data model of the JTable in this dialog. The
     * data model need to be convert and saved before you close this dialog.
     *
     */
    private void saveAttributes(){
        PropertyTableModel model = (PropertyTableModel)table.getModel();
        if (PropertyTableModel.rowdata == null)
            return;

        clearAttributes_inDepot();

        Vector<sTableData> rows = PropertyTableModel.rowdata;

        for(sTableData row: rows){
            //if(  row.type.equals("Markable"))
            //    addAttributes_inDepot( row.attributeValue, null);
            //else
                addAttributes_inDepot( row.attributeName, row.attributeValue);
        }
        gui.display_showAnnotationDetail_onEditorPanel(WorkSet.currentAnnotation);
        gui.showSelectedAnnotations_inList(0);
        gui.showRelationships_Refresh();
    }


    private void clearAttributes_inDepot(){
        // ge current annotation
        currentannotation = resultEditor.workSpace.WorkSet.currentAnnotation;
        if ( currentannotation == null )
            return;

        if ( currentannotation.attributes == null )
            return;

        // get all normaltionships belongs to current annotation
        Vector<resultEditor.annotations.AnnotationAttributeDef> normalrelationships
                = currentannotation.attributes;

        normalrelationships.clear();
    }

    private void addAttributes_inDepot(String attributeName, String attributeValue){
        // ge current annotation
        currentannotation = resultEditor.workSpace.WorkSet.currentAnnotation;
        if ( currentannotation == null )
            return;

        //if ( currentannotation.normalrelationships == null )
            //return;

        // get all normaltionships belongs to current annotation
        Vector<resultEditor.annotations.AnnotationAttributeDef> normalrelationships
                = currentannotation.attributes;
        AnnotationAttributeDef realship = new AnnotationAttributeDef();
        realship.name = attributeName;
        if (attributeValue!=null){
            if(attributeValue.trim().length() < 1)
                attributeValue = null;
        }
        realship.value = attributeValue;
        normalrelationships.add( realship );
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
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Attributes:"));
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
                .addContainerGap(432, Short.MAX_VALUE)
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
                .addContainerGap(500, Short.MAX_VALUE)
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
            .add(0, 566, Short.MAX_VALUE)
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
        
    }//GEN-LAST:event_formWindowClosing

    private void jButton_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_deleteActionPerformed
        int selectedRowIndex = table.getSelectedRow();
        PropertyTableModel model = (PropertyTableModel) table.getModel();
        if ( model == null )
            return;

        model.deleteRow( selectedRowIndex );

        table.updateUI();

    }//GEN-LAST:event_jButton_deleteActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        PropertyTableModel model = (PropertyTableModel) table.getModel();
        if ( model == null )
            return;

        model.addNewValue();

        table.updateUI();
    }//GEN-LAST:event_jButton3ActionPerformed

    /**To a given string name of an entry of list, found its index and use
     * color to indicate this entry has been selected.
     */


    /**check whether an annotated classname has */
    private boolean repetitiveCheck(String newClassname ){
        if(( newClassname == null)||(newClassname.trim().length() < 1 ))
            return false;
        resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
        if( classdepot.isExisting(newClassname) )
            return true;
        else
            return false;
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

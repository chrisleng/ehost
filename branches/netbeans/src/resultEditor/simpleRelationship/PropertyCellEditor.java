/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.simpleRelationship;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JComboBox;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Chris
 */
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor {
    private String editvalue = null;
    protected int row;
    protected JTable table;



    public PropertyCellEditor() {
        super();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {

        this.row = row;
        this.table = table;

        // record original value before modification
        //if (value !=null)
        //    this.setCellEditorValue(value.toString());
        //else this.setCellEditorValue(null);

        // get data of this row
        sTableData thisrow = ((PropertyTableModel)table.getModel()).getRowData_inFormatOf_STableData(row);

        if ( thisrow != null ){
            // record original value before modification
            //if ( thisrow.attributeValue !=null)
            //    this.setCellEditorValue( thisrow.attributeValue );
            //else this.setCellEditorValue( null);

            if ( thisrow.type.equals("Markable")){
                MarkableValueDepot markablevaluedepot = new MarkableValueDepot();
                String[] values = markablevaluedepot.getValues( thisrow.attributeValue );
                //for(String s : values){
                //    System.out.println("str - " + s);
                //}
                final JComboBox box;
                if ( values == null ) 
                    box = new JComboBox();
                else  box = new JComboBox(values);
                //box.addItem(thisrow.attributeValue);
                if (isSelected) {
                    box.setForeground(table.getSelectionForeground());
                    box.setBackground(table.getSelectionBackground());
                } else {
                    box.setForeground(table.getForeground());
                    box.setBackground(table.getBackground());
                }

                // Select the current value
                box.setSelectedItem( thisrow.attributeValue );


                box.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        PropertyCellEditor.this.setCellEditorValue(  box.getSelectedItem().toString() );
                    }
                });
                return box;
            }else{

                // build a jtextfiled as return component for default cell editor
                final JTextField jtextfield;
                if (thisrow.attributeValue != null )
                    jtextfield = new JTextField( thisrow.attributeValue );
                 else jtextfield = new JTextField( null );

                jtextfield.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        PropertyCellEditor.this.setCellEditorValue(  jtextfield.getText() );
                    }
                });
                jtextfield.addPropertyChangeListener(new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent evt) {
                        PropertyCellEditor.this.setCellEditorValue(  jtextfield.getText() );
                    }
                });
                jtextfield.addKeyListener(new KeyListener() {

                    public void keyTyped(KeyEvent e) {
                        PropertyCellEditor.this.setCellEditorValue(  (jtextfield.getText() + e.getKeyChar()).trim() );
                    }

                    public void keyPressed(KeyEvent e) {

                    }

                    public void keyReleased(KeyEvent e) {

                    }
                });

                return jtextfield;
            }
        }



        // build a jtextfiled as return component for default cell editor
        final JTextField jtextfield2;
        if (value != null )
            jtextfield2 = new JTextField( value.toString() );
        else jtextfield2 = new JTextField( null );

        jtextfield2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PropertyCellEditor.this.setCellEditorValue(  jtextfield2.getText() );
            }
        });
        jtextfield2.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                PropertyCellEditor.this.setCellEditorValue(  jtextfield2.getText() );
            }
        });
        jtextfield2.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
                PropertyCellEditor.this.setCellEditorValue(  (jtextfield2.getText() + e.getKeyChar()).trim() );
            }

            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {

            }
        });

        return jtextfield2;
        //configureRenderer(tableRenderer, value);
        //return null;
 
    }

    public Object getCellEditorValue() {
        return this.editvalue;
    }

    private void setCellEditorValue(String value){
        this.editvalue = value;
        //System.out.println("current value = " + value);
        //sTableData thisrow = Editor.getRowData(row);
        //
        PropertyTableModel model = ((PropertyTableModel)table.getModel());
        model.setValueAt(value, row, 1);
    }
}

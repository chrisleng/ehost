/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.attributes;

import java.awt.Component;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

/**
 * A custom cell editor for a JTable Cell
 * @author Chris
 */
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor
{

    private String editvalue = null;
    private int row;
    private JTable table;
    private int column;
    private Editor father;

    /**
     * The Constructor for a PropertyCellEditor
     * @param father - the editor
     */
    public PropertyCellEditor(Editor father)
    {

        super();
        this.father = father;
    }

    /**Remove html tags from an html string.
     * Another topical example is:
     *  <html><font color = "red">faslse</font></html>
     * @return  pure text from the given html string.
     */
    public String removeHtmlTags(final String htmlstring){
        //System.out.println("脱壳："+htmlstring);
        String html = htmlstring;

        final String REMOVE_TAGS1 = "\\<html\\>";
        final String REMOVE_TAGS2 = "\\</html\\>";
        final String REMOVE_TAGS3 = "\\</font\\>";
        final String REMOVE_TAGS4 = "\\<font color = \"red\"\\>";

        if( html == null )
            return html;

        if( ! html.toLowerCase().contains( "<html>" ) )
            return html;
        
        try{
            html = html.replaceAll(REMOVE_TAGS1, "");
            html = html.replaceAll(REMOVE_TAGS2, "");
            html = html.replaceAll(REMOVE_TAGS3, "");
            html = html.replaceAll(REMOVE_TAGS4, "");
            
            return html;
            /*while(matcher.find()){

                int start = matcher.start();
                int end   = matcher.end();
                String text = html.substring(start, end);
                System.out.println("x="+start+", y="+end+"; "+"text=["+text+"]");

                Matcher submatcher = REMOVE_TAGS.matcher( text );
                if(!submatcher.find()){
                    
                }
            }*/
        
        }catch(Exception ex){
            return html;
        }
    }


    /**This is a string that used to record the latest display attribtue value
     * on the edit field on the combox while editing, the string only can
     * be update by the method in the listener.
     */
    private String latestString = null;

    /**
     * This will return the component for cell editing for each row and column
     * @param table - table for editing
     * @param value - value to edit
     * @param isSelected - true if the cell is selected
     * @param row - row of this cell
     * @param column - column of this cell
     * @return
     */
    public Component getTableCellEditorComponent(final JTable table, Object value,
            boolean isSelected, final int row, final int column)
    {
        //Set member variables
        this.row = row;
        this.table = table;
        this.column = column;

        // get data of this row
        final Row thisrow = ((TableModel) table.getModel()).getRowData_inFormatOf_STableData(row);

        //If this column needs a combo box then build one.
        //if (thisrow.needsCombo(column))
        if( column == 1 )
        {
            //Get values for this combo box and put them in an array
            String[] values = null;
            Vector<String> toUse = null;
            if (thisrow.allowValues != null) {
                toUse = thisrow.allowValues; //.getStrings(column);
            }
            if (toUse != null) {
                values = new String[toUse.size()];
                for (int i = 0; i < toUse.size(); i++) {
                    values[i] = toUse.get(i);
                }
            }
            //Build combo box
            final JComboBox box;
            //if values are null then bulid empty combo box
            if (values == null) {
                box = new JComboBox();
            } //if values are non null then build combo box with those values
            else {
                box = new JComboBox(values);
            }

            box.setEditable(true);

            //If the cell is selected then set colors differently
            if (isSelected) {
                box.setForeground(table.getSelectionForeground());
                box.setBackground(table.getSelectionBackground());
            } //If not selected then set unselected colors
            else {
                box.setForeground(table.getForeground());
                box.setBackground(table.getBackground());
            }

            // Select the current value
            String selected = thisrow.currentValue;//.getSelectedItem(column);
            box.setSelectedItem(removeHtmlTags(selected));


            //Allow for change of selection for Combo box items
            box.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    String selectedString = box.getSelectedItem().toString();
                    //selectedString = removeHtmlTags( selectedString );
                    PropertyCellEditor.this.setCellEditorValue(selectedString);
                    //thisrow.setString(column, selectedString );
                    thisrow.currentValue = selectedString;
                    table.update(table.getGraphics());


                }
            });

            box.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    //System.out.println("处理1");
                    //String item = e.getItem().toString();
                    Object[] selected = e.getItemSelectable().getSelectedObjects();
                    if (selected == null) {
                        return;
                    }
                    if (selected.length < 1) {
                        return;
                    }
                    String item = selected[0].toString();
                    //System.out.println("text = " + text);

                    if (item != null) {
                        if (item.length() <= 6) {
                            return;
                        }
                        if (item.substring(0, 6).compareTo("<html>") == 0) {

                            item = removeHtmlTags(item);


                            //if( (latestString!=null)
                            //   && ( item.compareTo(latestString) == 0 ))
                            //    return;

                            latestString = item;
                            box.setSelectedItem(item);

                            //System.out.println("处理2");
                        }
                    }
                }
            });
            /*
             * box.addMouseListener(new java.awt.event.MouseAdapter() {
             *
             * public void mousePressed(java.awt.event.MouseEvent evt) { pop();
             * } });
             *
             */
            return box;
        } //If this row/column does not need a combo box then just build a textfield
        //that can be edited
        else {
            //Create a textfield for the editor
            final JTextField jtextfield;
            jtextfield = new JTextField(thisrow.attributename); //.getSelectedItem(column));

            //If it can't be modified then set enabledToFalse
            //if (!thisrow.isModifiable(column))
            if (column == 0) {
                jtextfield.setEnabled(false);
            }
            jtextfield.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    pop();
                    PropertyCellEditor.this.setCellEditorValue(jtextfield.getText());
                }
            });
            jtextfield.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    pop();
                    PropertyCellEditor.this.setCellEditorValue(jtextfield.getText());
                }
            });
            //Allow for editing by allowing keys to be typed.
            jtextfield.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent e) {
                    pop();
                    PropertyCellEditor.this.setCellEditorValue((jtextfield.getText() + e.getKeyChar()).trim());
                }

                public void keyPressed(KeyEvent e) {
                    pop();
                }

                public void keyReleased(KeyEvent e) {
                }
            });

            return jtextfield;

        }

    }
    /**
     * Get the value in the cell editor
     * @return
     */
    public Object getCellEditorValue()
    {
        return this.editvalue;
    }
    /**
     * Set the value in the cell editor
     * @param value - the value to set the editor value to.
     */
    private void setCellEditorValue(String value)
    {
        //this.editvalue = value;
        TableModel model = ((TableModel) table.getModel());
        model.setValueAt(value, row, column);

    }

    private void pop()
    {
        //final iListable thisrow = ((TableModel) table.getModel()).getRowData_inFormatOf_STableData(row);
        //if(thisrow == null)
        //    return;
        //father.setDeleteEnabled(thisrow.couldDelete());
    }
}

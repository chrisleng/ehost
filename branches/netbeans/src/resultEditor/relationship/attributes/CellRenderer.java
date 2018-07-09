/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.attributes;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import resultEditor.relationship.iListable;

/**
 * Cell renderer that use to put components on table cells for user 
 * to edit attribute of relationship.
 * 
 * @author Chris 2012-06-12
 */
public class CellRenderer implements TableCellRenderer
{

    /**
     * Constructor for a property cell renderer
     */
    public CellRenderer()
    {
        super();
    }
    /**
     * Get the renderer for any given cell
     * @param table - the table containing the cells
     * @param value - the value in
     * @param isSelected - true if this cell is selected
     * @param hasFocus - true if this cell has focus
     * @param row - the row of the cell
     * @param column - the column of the cell
     * @return
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        //Get the property table model
        Row thisrow = ((TableModel) table.getModel()).getRowData_inFormatOf_STableData(row);

        //If the row needs a combo box then build one to be rendered
        //if (thisrow.needsCombo(column))
        if( column == 1 )
        {
            //Build a new combo box
            JComboBox box = new JComboBox();

            
            
            //Add all of the entries for this cell
            if (thisrow.allowValues != null) {
                for (String s : thisrow.allowValues) // getStrings(column))
                {
                    box.addItem(s);
                }
            }else{
                box.addItem(thisrow.currentValue );
            }

            //Set the selected item in the combo box to the selected value in the iListable
            if(thisrow.allowValues != null){
                box.setSelectedItem( thisrow.currentValue );
            }else{
                box.setSelectedItem( thisrow.currentValue );
            }//thisrow.getSelectedItem(column));

            //Set colors based on selection
            if (isSelected)
            {
                box.setForeground(table.getSelectionForeground());
                box.setBackground(table.getSelectionBackground());
            }
            else
            {
                box.setForeground(table.getForeground());
                box.setBackground(table.getBackground());
            }
            
            return box;
        }
        //Return a default table renderer
        else
        {
            DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
                tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(table,
                //thisrow.getSelectedItem(column), 
                        thisrow.attributename,
                isSelected,
                hasFocus, row, column);
                return tableRenderer;
        }
        

        

    }
  
}

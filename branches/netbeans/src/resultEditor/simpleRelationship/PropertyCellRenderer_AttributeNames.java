/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.simpleRelationship;

import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Chris
 */
public class PropertyCellRenderer_AttributeNames implements TableCellRenderer {

    public PropertyCellRenderer_AttributeNames() {
        super();
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        sTableData thisrow = ((PropertyTableModel)table.getModel()).getRowData_inFormatOf_STableData(row);

        if ( thisrow != null ){

                DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer();
                tableRenderer = (DefaultTableCellRenderer) tableRenderer.getTableCellRendererComponent(table,
                thisrow.attributeName, isSelected,
                hasFocus, row, column);
                //configureRenderer(tableRenderer, value);
                return tableRenderer;
        }

        DefaultTableCellRenderer tableRenderer2 = new DefaultTableCellRenderer();
        tableRenderer2 = (DefaultTableCellRenderer) tableRenderer2.getTableCellRendererComponent(table,
                value, isSelected,
            hasFocus, row, column);
        //configureRenderer(tableRenderer, value);
        return tableRenderer2;

    }

    public String[] getClassnames(String classname){

        try{
            resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
            String[] allclassnames = depot.getAnnotationClasssnamesString();
            
        } catch(Exception e){
            return null;
        }

        return null;
    }
}

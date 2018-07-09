package resultEditor.relationship;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import resultEditor.annotations.AnnotationAttributeDef;
import resultEditor.relationship.Editor.Type;

/**
 * A PropertyTableModel for a JTable
 *
 * @author Chris
 */
class PropertyTableModel extends DefaultTableModel {

    /**
     * The data in the table
     */
    static Vector<iListable> rowdata = new Vector<iListable>();
    /**
     * The type of data
     */
    private Type type;
    private String columnNames[];

    public PropertyTableModel(Type s) {
        //Set the column names based on the Type of propertytablemodel
        type = s;
//        if(type == Type.ComplexSchema)
//        {
//            columnNames = new String[]{"Relationship Type", "Allowable Values"};
//        }
//        if(type == Type.ComplexRelationships)
//        {
//            columnNames = new String[]{"Type Of Relationship", "Involved Annotations(Not editable)"};
//        }
        if (type == Type.Attributes) {
            columnNames = new String[]{"Attribute Name", "Attribute Value"};
        }
    }

    public void setrow(int row, iListable thisrow ){
        rowdata.set(row, thisrow);
    }
    
    /**
     * Get the column count for the table
     *
     * @return - the column count
     */
    @Override
    public int getColumnCount() {
//        if(type == Type.ComplexSchema)
//            return 1;
//        if(type == Type.ComplexRelationships)
//            return 2;
        if (type == Type.Attributes) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * Get the row data for a given row
     *
     * @param row - the row to get the data from
     * @return - the row data
     */
    public iListable getRowData_inFormatOf_STableData(int row) {
        if ((row < 0) || (row >= rowdata.size())) {
            return null;
        }
        return rowdata.get(row);
    }

    /**
     * Get the name of a column.
     *
     * @param column - the column number
     * @return - the name of the column
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Get the row count
     *
     * @return - the number of rows
     */
    @Override
    public int getRowCount() {
        return rowdata.size();
    }

    /**
     * Add a row data
     *
     * @param row_data
     */
    public void addrow(iListable row_data) {
        rowdata.add(row_data);
    }

    /**
     * Remove all the rows from the property table
     */
    public void removeAllRows() {
        rowdata.clear();
    }

    /**
     * Get the value at a Cell
     *
     * @param row - the row of the cell
     * @param column - the column of the cel
     * @return - the cell data
     */
    @Override
    public Object getValueAt(int row, int column) {
        return rowdata.get(row).getStrings(column);
    }

    /**
     * Get the class of a column
     *
     * @param column
     * @return
     */
    @Override
    public Class getColumnClass(int column) {
        //return (getValueAt(0, column).getClass());
        return String.class;
    }

    /**
     * Add a new Value to the list
     */
    public void addNewValue() {
        //Add a new value based on the type
//        if(type == Type.ComplexSchema)
//        {
//            rowdata.add(new List("new"));
//        }
        if (type == Type.Attributes) {
            if (env.Parameters.AttributeSchemas.getAttributes().size() > 0) {
                AnnotationAttributeDef normal = new AnnotationAttributeDef(env.Parameters.AttributeSchemas.getAttributes().get(0).getName());
                rowdata.add(normal);
            }
        }
        //If we're editing ComplexRelationships then we can't add new ones
//        else if(type == Type.ComplexRelationships)
//        {
        //Do nothing?
//        }
    }

    /**
     * Delete a row from the table
     *
     * @param rowIndex - the row to delete
     */
    public void deleteRow(int rowIndex) {
        //rowIndex--;
        if (this.rowdata == null) {
            return;
        }
        int size = this.rowdata.size();

        if ((rowIndex < 0) || (rowIndex >= size)) {
            return;
        }

        rowdata.removeElementAt(rowIndex);
    }

    /**
     * Set the value at a position in the JTable
     *
     * @param value - the value to set the cell to
     * @param row - the row of the cell
     * @param column - the column of the cell
     */
    public void setValueAt(Object value, int row, int column) {
        //rowData[row][column] = value;
        int size = rowdata.size();
        if ((row >= size) || ((row < 0))) {
            log.LoggingToFile.log(Level.SEVERE, "1008121323 - data index out of range!!!");
            return;
        }
        if (value != null) {
            rowdata.get(row).setString(column, value.toString());
        }


    }

    /**
     * See if a cell value can be changed
     *
     * @param row - row of the cell
     * @param column - column of the cell
     * @return - true if the value can be changed, false otherwise
     */
    public boolean isCellEditable(int row, int column) {
        return true;
    }
}

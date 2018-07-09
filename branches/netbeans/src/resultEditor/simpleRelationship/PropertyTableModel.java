package resultEditor.simpleRelationship;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Vector;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Chris
 */
class PropertyTableModel extends DefaultTableModel  {

  static Vector<sTableData> rowdata = new Vector<sTableData>();

  String columnNames[] = { "Attribute Name", "Attribute Value" };

  public int getColumnCount() {
      return 2;
    //return columnNames.length;
  }

  public sTableData getRowData_inFormatOf_STableData(int row){
      if (( row < 0)||(row >= rowdata.size() ))
          return null;
      return rowdata.get(row);
  }

  public String getColumnName(int column) {
    return columnNames[column];
  }

  public int getRowCount() {
    //return rowData.length;
    return rowdata.size();
  }

  public void addrow(sTableData row_data){
      rowdata.add(row_data);
  }

  public void removeAllRows(){
      rowdata.clear();
  }

  public Object getValueAt(int row, int column) {
      if (( column != 0)||( column != 1))
          return null;

      if ( column == 0 )
          return rowdata.get(row).attributeName;

      if ( column == 1 )
          return rowdata.get(row).attributeValue;
        
              //return rowData[row][column];
      return null;
  }

    @Override
    public Class getColumnClass(int column) {
        //return (getValueAt(0, column).getClass());
        return String.class;
    }

    public void addNewValue(){
        sTableData newrow = new sTableData();
        newrow.type = "new";
        newrow.typevalue = "";
        newrow.attributeName = "New_Property_Item";
        newrow.attributeValue = "Value";
        rowdata.add( newrow );
    }

    public void deleteRow(int rowIndex){
        if ( this.rowdata == null )
            return;
        int size = this.rowdata.size();

        if (( rowIndex < 0 )||(rowIndex >= size))
            return;

        rowdata.removeElementAt(rowIndex);
    }

    public void setValueAt(Object value, int row, int column) {
        //rowData[row][column] = value;
        int size = rowdata.size();
        if (( row >= size  )&&(( row < 0  ))){
            log.LoggingToFile.log(Level.SEVERE, "1008121323 - data index out of range!!!");
            return;
        }
        if( column == 0 ){
            if (value != null)
                rowdata.get(row).attributeName = value.toString();
            else rowdata.get(row).attributeName = null;
        } else if( column == 1){
            if (value != null)
                rowdata.get(row).attributeValue = value.toString();
            else rowdata.get(row).attributeValue = null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        //if(( column == 0 )&&( !this.rowdata.get(row).type.equals("new") ))
        //    return false;
        
        return true;
    }
}
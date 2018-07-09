package resultEditor.relationship.attributes;

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import relationship.complex.dataTypes.RelationshipDef;
import relationship.simple.dataTypes.AttributeList;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotations.AnnotationAttributeDef;

/**
 * Table model that list attributes of relationship on the table.
 *
 * @author Chris 2012-06-15
 */
public class TableModel extends DefaultTableModel {

    
    //private Vector<AnnotationAttributeDef> attributes = new Vector<AnnotationAttributeDef>();
    
    /**attribute schema for current relationship*/
    //private Vector<AttributeSchemaDef> attributeDefs = new Vector<AttributeSchemaDef>();
    
    /**
     * The data in the table
     */        
    private ArrayList<Row> attributedatas = new ArrayList<Row>();
    
    
    
    /**
     * The type of data
     */   
    private String columnNames[];

    
    public ArrayList<Row> getData(){
        return attributedatas;
    }
    
    
    public TableModel(Vector<AnnotationAttributeDef> attributes, RelationshipDef relationshipSchema) {
        
        columnNames = new String[]{"Attribute Name", "Attribute Value"};
        
        
        builddata( attributes, relationshipSchema);
                               
    }

    
    private void builddata(Vector<AnnotationAttributeDef> attributes, RelationshipDef relationshipSchema){
        
        attributedatas.clear();
                
        //ArrayList<Row> toReturn = new ArrayList<Row>();
        
        if( relationshipSchema == null )
            return ;
        
        AttributeList attributeSchemas = relationshipSchema.getAttributes();
        if( attributeSchemas == null )
            return ;
        
        for( AttributeSchemaDef attDef : attributeSchemas.getAttributes() ) {
            if( attDef == null )
                continue;
            
            if(( attDef.getName() == null )||(attDef.getName().trim().length()<1))
                continue;
            
            Row record = new Row();
            record.attributename = attDef.getName().trim();
            record.allowValues = attDef.getAllAllowedEntries();
            record.currentValue = getAttributeValue( record.attributename, attributes );
            attributedatas.add( record );
        }
        
        if(attributes==null)
            return;
        
        
        for( AnnotationAttributeDef att : attributes ){
            if(( att == null )||(att.name==null)||(att.name.trim().length()<1))
                continue;
            
            boolean hasMatchedSchema = false;
            for( AttributeSchemaDef attDef : attributeSchemas.getAttributes() ) {
                if(( attDef == null )||(attDef.getName()==null)||(attDef.getName().trim().length()<1))
                    continue;
                
                if( att.name.trim().compareTo( attDef.getName().trim()) == 0 )
                    hasMatchedSchema = true;
            }
            
            if( hasMatchedSchema == false ){
                 Row record = new Row();
                record.attributename = att.name.trim();
                record.allowValues = null;
                record.currentValue = att.value;
                attributedatas.add( record );
            }
            
            
        }
        
        
        return ;
        
    }
    
    /**
     * Get the column count for the table
     *
     * @return - the column count
     */
    @Override
    public int getColumnCount() {
        
        return columnNames.length;
        

        //if (type == Editor.Type.Attributes) {
        //    return 2;
        //} else {
        //    return 0;
        //}
    }

    
    private int countValidSchema(AttributeList attributeSchemas){
        if( attributeSchemas == null )
            return 0;
        
        int count = 0;
        for(AttributeSchemaDef attSchema : attributeSchemas.getAttributes()){
            if( attSchema == null )
                continue;
            if( attSchema.getName() == null )
                continue;
            if( attSchema.getName().trim().length() < 1 )
                continue;
            
            count++;
            
        }
        
        return count;
    }
    /**
     * Get the row data for a given row
     *
     * @param row - the row to get the data from
     * @return - the row data
     */
    public Row getRowData_inFormatOf_STableData(int row) {
        if ((row < 0) || (row >= this.attributedatas.size())) {
            return null;
        }
        return attributedatas.get(row);
    }
    
    //public void setAttributes(Vector<AnnotationAttributeDef> attributes){
    //    this.attributes = attributes;
    //}

    /**
     * Get the name of a column.
     *
     * @param column - the column number
     * @return - the name of the column
     */
    @Override
    public String getColumnName(int column) {
        if( columnNames == null )
            return null;
        else
            return columnNames[column];
    }

    /**
     * Get the row count
     *
     * @return - the number of rows
     */
    @Override
    public int getRowCount() {
        if( attributedatas == null )
            return 0;
        else
            return attributedatas.size();
    }

    /**
     * Add a row data
     *
     * @param row_data
     */
    public void addrow(Row row_data) {
        attributedatas.add(row_data);
    }

    /**
     * Remove all the rows from the property table
     */
    public void removeAllRows() {
        attributedatas.clear();
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
        if( column == 0)
            return attributedatas.get(row).attributename;
        else 
            return attributedatas.get(row).currentValue;
    }

    /**get the current value to an given attribute name*/
    private String getAttributeValue(String attributeName, Vector<AnnotationAttributeDef> attributes){
        if( attributeName == null )
            return null;
        if( attributeName.trim().length() < 1 )
            return null;
        
        if( attributes == null )
            return null;
        
        for( AnnotationAttributeDef aad : attributes ){
            if(( aad == null )||(aad.name == null) ||(aad.name.trim().length()<1))
                continue;   
            if( aad.name.trim().compareTo(attributeName.trim()) == 0 )
                return aad.value;
        }
        
        return null;
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
    //public void addNewValue() {

        /*if (type == Editor.Type.Attributes) {
            if (env.Parameters.SimpleRelationshipNames.getAttributes().size() > 0) {
                AnnotationAttributeDef normal = new AnnotationAttributeDef(env.Parameters.SimpleRelationshipNames.getAttributes().get(0).getName());
                rowdata.add(normal);
            }
        }*/
    //}

    /**
     * Delete a row from the table
     *
     * @param rowIndex - the row to delete
     */
    public void deleteRow(int rowIndex) {
        //rowIndex--;
        if (this.attributedatas == null) {
            return;
        }
        int size = attributedatas.size();

        if ((rowIndex < 0) || (rowIndex >= size)) {
            return;
        }

        attributedatas.remove(rowIndex);
    }

    /**
     * Set the value at a position in the JTable
     *
     * @param value - the value to set the cell to
     * @param row - the row of the cell
     * @param column - the column of the cell
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        //rowData[row][column] = value;
        int size = attributedatas.size();
        if ((row >= size) || ((row < 0))) {
            
            return;
        }
        if ((value != null)&&(column==1)) {
            Row thisline = attributedatas.get( row );
            thisline.currentValue = (String) value ;
            attributedatas.set( row, thisline);
        }


    }

    /**
     * See if a cell value can be changed
     *
     * @param row - row of the cell
     * @param column - column of the cell
     * @return - true if the value can be changed, false otherwise
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    
    
}

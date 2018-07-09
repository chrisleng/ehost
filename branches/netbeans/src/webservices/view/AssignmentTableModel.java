/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.view;

import gov.va.vinci.annotationAdmin.integration.model.Analyte;
import gov.va.vinci.annotationAdmin.integration.model.AnalyteAssignment;
import gov.va.vinci.annotationAdmin.integration.model.Assignment;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author yarden
 */
public class AssignmentTableModel extends AbstractTableModel {
    private String[] columnNames = {"Name", "Assignment", "Project", "Assigned", "Modified", "Comment"};
    private Class[]  columnClasses = { String.class, String.class, String.class, String.class, String.class, String.class};
    private List<AnalyteAssignment> items = new ArrayList<AnalyteAssignment>();   
    private Assignment.Status status;
    
    public AssignmentTableModel(Assignment.Status status) {
        this.status = status;
    }
    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public int getRowCount() {
        return items == null ? 0 : items.size();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0:
                return items.get(row).getName();
            case 1:
                return items.get(row).getAssignment().getName();
            case 2:
                return items.get(row).getAssignment().getProject();
            case 3:
                return items.get(row).getAssignment().getAssigned();
            case 4:
                return items.get(row).getModified();
            default: 
                return items.get(row).getComment();
        }
    }
     
    @Override
    public void setValueAt(Object value, int row, int col) {
        if (col != columnClasses.length-1) return; // should never happen
        items.get(row).setComment(value.toString());
        fireTableCellUpdated(row, col);
    }   
    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 4;
    }

    public void insertAssignmentItem(int row, AnalyteAssignment item) {
        items.add(row, item);
        item.setStatus(status);
        fireTableDataChanged();
    }
    
    public void setAssignments(List<AnalyteAssignment> items) {
        this.items = items;
        for (AnalyteAssignment i : items) {
            i.setStatus(status);
        }
        fireTableDataChanged();
    }
    
    public List<AnalyteAssignment> getAssignments() {
        return items;
    }
    
    public AnalyteAssignment getRow(int row) {
        return items.get(row);
    }
    
    public void removeRow(int row) {
        items.remove(row);
        fireTableDataChanged();
    }
    
}

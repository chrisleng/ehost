/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.view;


import gov.va.vinci.annotationAdmin.integration.model.AnalyteAssignment;
import gov.va.vinci.annotationAdmin.integration.model.Assignment;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;


/**
 *
 * @author yarden
 */
public class TableTransferHandler extends TransferHandler {
    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(Object[].class, DataFlavor.javaJVMLocalObjectMimeType, "Array of items");

    private JTable source = null;
    private int[] rows = null;
    private Object[] transferedObjects = null;

	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    
    
    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JTable)c;
        AssignmentTableModel model = (AssignmentTableModel) source.getModel();
        ArrayList< Object > list = new ArrayList< Object >();
        for(int i: rows = source.getSelectedRows()) {
            AnalyteAssignment item = model.getRow(i);
            if (item.getServerStatus() != Assignment.Status.Done)
                list.add(item);
        }
            
        transferedObjects = list.toArray();
        return new DataHandler(transferedObjects,localObjectFlavor.getMimeType());
    }
    
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        // support only drops. No clipboard paste.
        boolean reply = support.isDrop() && support.isDataFlavorSupported(localObjectFlavor);
        JTable table = (JTable)support.getComponent();

        table.setCursor(reply?DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        
        return reply;
    }
    
    @Override 
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        
        JTable target = (JTable) support.getComponent();
        JTable.DropLocation location = (JTable.DropLocation) support.getDropLocation();
        AssignmentTableModel model = (AssignmentTableModel) target.getModel();
        
        int at = location.getRow();
        int max = model.getRowCount();
        
        if (at < 0 || at > max)
            at = max;
        
        target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
 
        // fetch the data
        try {
            Object[] values = (Object[])support.getTransferable().getTransferData(localObjectFlavor);
            
            AssignmentTableModel srcModel = (AssignmentTableModel) source.getModel();
            for(int i=rows.length-1;i>=0;i--) {
                if (source == target && rows[i] < at) at--;
                srcModel.removeRow(rows[i]);
            }
           
            for(int i=0; i < rows.length;i ++) {
                model.insertAssignmentItem(at++, (AnalyteAssignment)values[i]);
                target.getSelectionModel().addSelectionInterval(at, at);
            }
            
            rows = null;
            
            /*
             * Hook in an action event so the change in analyte state can be persisted.
             */
            for(ActionListener al: actionListeners)
            {
            	al.actionPerformed(null);
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

	public void addActionListener(ActionListener actionListener) {
		actionListeners.add(actionListener);
	}
    
}

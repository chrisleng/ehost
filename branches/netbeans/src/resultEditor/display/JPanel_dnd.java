/*
 * Custom java component for building a panel component which will support
 * DnD functions.
 */

package resultEditor.display;

import userInterface.addNewDict.WizardConceptLib;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.util.logging.Level;
import javax.swing.JPanel;

/**
 * This is a new component extends from JPanel which will be used to implement
 * DnD function on dialog of chosing a new pre-annotated concept dictionary.
 *
 * @author Jianwei Chris Leng, created at  Sep 10, 2010
 */
public class JPanel_dnd extends JPanel implements java.awt.dnd.DropTargetListener{

    protected WizardConceptLib frame;

    public JPanel_dnd(WizardConceptLib frame){
        super();
        this.frame = frame;
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragOver(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragExit(DropTargetEvent dte) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drop(DropTargetDropEvent dtde) {
        System.out.println("User try to drop objects into list of text sources.");
        try{
            java.awt.datatransfer.Transferable tr = dtde.getTransferable();
            if( dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ){
                //Vector<File> files = new Vector<File>();
                dtde.acceptDrop( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
                Object fileobject = (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));

                System.out.println("-----  " + fileobject.toString());

                // add files and show them on eHOST GUI


                dtde.dropComplete(true);
            }else{
                dtde.rejectDrop();
            }
        }catch(Exception ep){
            log.LoggingToFile.log(Level.SEVERE,  "1007291100::" + ep);
        }
    }

}

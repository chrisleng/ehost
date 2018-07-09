/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.customComponents;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;

/**
 *
 * @author Chris
 */
public class JTable_dropable extends JTable implements java.awt.dnd.DropTargetListener {

    protected userInterface.GUI gui;

    public JTable_dropable(userInterface.GUI gui){
        super();

        this.gui = gui;
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

    }

    public void dragEnter(DropTargetDragEvent dtde) {
        // Ignore 
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragOver(DropTargetDragEvent dtde) {
        // Ignore 
        //System.out.println( dtde.getSource().toString() );
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Ignore 
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void dragExit(DropTargetEvent dte) {
        // Ignore
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drop(DropTargetDropEvent dtde) {

        log.LoggingToFile.log(Level.INFO, "User try to drop objects into list of text sources.");
        try{
            java.awt.datatransfer.Transferable tr = dtde.getTransferable();
            if( dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ){
                Vector<File> files = new Vector<File>();
                dtde.acceptDrop( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);                
                List list = (List)(dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                int size = list.size();
                for( int i=0; i< size; i++ ){
                    File f = null;
                    try{ f = (File)list.get(i);
                    }catch(Exception e){}
                    if ( f == null) continue;
                    files.add(f);
                    // System.out.println( ((File)list.get(i)).getName() );
                }

                // add files and show them on eHOST GUI
                gui.addTextFiles(files);
                //&&&&gui.showTextFiles();
                gui.showTextFiles_inComboBox();
                
                dtde.dropComplete(true);
            }else{
                dtde.rejectDrop();
            }
        }catch(Exception ep){
            log.LoggingToFile.log(Level.SEVERE,  "1007291100::" + ep);
        }
    }

}

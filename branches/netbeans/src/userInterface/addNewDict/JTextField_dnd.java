/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.addNewDict;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import javax.swing.JTextField;

/**
 * This is a new component extends from JTextField which will be used to implement
 * DnD function on dialog of chosing a new pre-annotated concept dictionary.
 *
 * @author Jianwei Chris Leng, created at  Sep 10, 2010
 */
public class JTextField_dnd extends JTextField implements java.awt.dnd.DropTargetListener{

    protected WizardConceptLib frame;

    public JTextField_dnd(WizardConceptLib frame){
        super();
        this.frame = frame;
        new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
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
        log.LoggingToFile.log( Level.INFO,"User try to drop objects into list of text sources.");
        try{
            java.awt.datatransfer.Transferable tr = dtde.getTransferable();
            if( dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ){
                List files;
                dtde.acceptDrop( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE);
                Object fileobject = (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));

                //System.out.println("-----  " + fileobject.toString() + " / " + fileobject.getClass().toString());

                if( (fileobject != null)&&(fileobject instanceof List) ){
                    try{
                        files = (List) fileobject;
                        if (files != null){
                            if (files.size() < 1 )
                                return;
                            File f = (File)files.get(0);
                            frame.setDictionaryName( (File)f );
                        }
                    }catch(Exception ex){
                        log.LoggingToFile.log( Level.SEVERE, "error 1009101355 - fail to get dnd file name.");
                    }
                }
                


                dtde.dropComplete(true);
            }else{
                dtde.rejectDrop();
            }
        }catch(Exception ep){
            log.LoggingToFile.log( Level.SEVERE, "1007291100::" + ep);
        }
    }

}

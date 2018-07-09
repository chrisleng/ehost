/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workSpace;


import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JList;

/**
 *
 * @author leng
 */
public class DeleteProject {

    /**The coordinates of the top left corner to the parent window.*/
    protected Point __parentWindowCoordinates = null;
    /**The size of the parent window.*/
    protected Dimension __parentWindowSize = null;

    /**point to main window*/
    protected userInterface.GUI __gui = null;

    /**the project that you selected from project list for deleting.*/
    private File __projectfolder = null;
    
    /**the jlist which contains projects, which locates on the NAV pane*/
    protected JList __jlist = null;

    public DeleteProject(userInterface.GUI _gui, JList _jlist, Point _parentWindowCoordinates, Dimension _parentWindowSize)
    {
        // get values of imported variables
        this.__jlist = _jlist;
        this.__gui = _gui;
        this.__parentWindowCoordinates = _parentWindowCoordinates;
        this.__parentWindowSize = _parentWindowSize;

    }

    public void delete()
    {

        //####  Get selected project
        File folder = getProject(this.__jlist);
        if(folder==null)
            return;
        else
            __projectfolder = folder;

                
        //####  Show the confimation pane
        ConfirmDeleting confirmdeleting = new ConfirmDeleting(this, __gui, __parentWindowCoordinates, __parentWindowSize);
        confirmdeleting.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
        confirmdeleting.setVisible(true);
    }


    private void deleteFolder(File folder){
        if(folder!=null){
            File[] belongs = folder.listFiles();
            if((belongs!=null)&&(belongs.length>0))
            {
                for(File f: belongs)
                {
                    if (f==null)
                        continue;

                    if(f.isFile())
                        f.delete();
                    else if (f.isDirectory())
                        deleteFolder(f);
                }
            }

            folder.delete();
        }
    }
    /**This method only should be called from class Confirmdeleting. After you
     * clicked "OK" button, confirmdeleteing class will call this method to
     * really erase the selected project folder physically.
     */
    public void physicalDeleting()
    {                
        try{
            if(__projectfolder==null)
                return;

            if(!__projectfolder.exists())
                return;

            if(__projectfolder.isDirectory())
            {
                File[] subfiles = __projectfolder.listFiles();
                if((subfiles!=null)&&(subfiles.length>0))
                {
                    for(File f:subfiles)
                    {
                        if (f==null)
                            continue;

                        if (f.isFile())
                            f.delete();
                        else if (f.isDirectory())
                            deleteFolder(f);
                    }
                }

            }
            
            __projectfolder.delete();

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1102041447:: fail to physical erase these folders and files"
                    + ex.toString());
        }
    }

    private File getProject(JList _jlist)
    {
        try{
            if(_jlist==null)
                return null;

            int size = _jlist.getModel().getSize();
            if(size<1)
                return null;

            int seletedindex = _jlist.getSelectedIndex();
            if(seletedindex<0)
                return null;

            Object o = _jlist.getModel().getElementAt(seletedindex);
            if(o==null)
                return null;

            navigatorContainer.ListEntry_Project entry = (navigatorContainer.ListEntry_Project) o;

            File f = entry.getFolder();
            return f;
            
        } catch(Exception ex) {
            log.LoggingToFile.log( Level.SEVERE,"error 1102041228:: could not get selected project in project list(for deleting)");
        }

        return null;
        
    }
}

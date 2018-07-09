/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package workSpace.switcher;

import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JComboBox;

/**
 *
 * @author leng
 */
public class RecentWorkSpace {

    /**The JCombobox used to show recent selected workspaces on the NAV pane*/
    private JComboBox __jcombobox = null;


    public void updateWorkspaces_inCombobox(){
        if(__jcombobox==null)
            return;

        File selectedpath = null;
        Object selectobject = __jcombobox.getSelectedItem();
        if((selectobject!=null)&&(selectobject instanceof File))
        {
            selectedpath = (File)selectobject;
        }


        emptyCombobox();

        Vector<File> workspaces = RecentWorkspaces.get();
        if((workspaces==null)||(workspaces.size()<1))
            return;
        
        //File currentworkspace = __jcombobox.
        showInCombobox(workspaces, __jcombobox, selectedpath);
    }

    private void showInCombobox(Vector<File> _files, JComboBox _jcombobox, File _latestSelectedWorkspace)
    {
        if((_files==null)||(_jcombobox==null))
            return;

        int i=0, j=-1;
        try{
            for(File path:_files)
            {
                if(path==null)
                    continue;

                _jcombobox.addItem(path);


                if(_latestSelectedWorkspace!=null){
                    if(_latestSelectedWorkspace.compareTo(path)==0)
                        j=i;
                }


                i++;
            }

            _jcombobox.setEditable(false);
            _jcombobox.setRenderer(new ComboboxRender());
            if(j>0)
                _jcombobox.setSelectedIndex(j);
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1106030929::");
        }

        
    }

    public void emptyCombobox(){
        if(__jcombobox!=null)
            __jcombobox.removeAllItems();
    }
    

    /**this contstruct method as the the jcombobox that you used to show
     * recent selected workspaces on the NAV pane. There have another construction
     * method with out parameter is only used to let you handle workspace issues
     * which do not related to display.
     */
    public RecentWorkSpace(JComboBox _jcombobox){
        this.__jcombobox = _jcombobox;
    }

    /**There are two contruction method in this class. This one is only used
     * for you to access recent used workspaces.
     */
    public RecentWorkSpace(){
    }

    public void removeRecordWorkspaces(){
        RecentWorkspaces.clear();
    }

    public static int getItemCount(){
        return RecentWorkspaces.size();
    }
    
    public void addWorkspace(File _workspace){
        RecentWorkspaces.add(_workspace);
    }



    /**This static inner class is used to manage recent used workspace*/
    static class RecentWorkspaces{
        /**static variable of vector which is uesed to store all 5 recent
         * used workspace path; format in "File".
         */
        private static Vector<File> RECENTWORKSPACES = new Vector<File>();

        static void clear(){
            RECENTWORKSPACES.clear();
        }

        static Vector<File> get(){
            return RECENTWORKSPACES;
        }

        static void add(File _workspace){

            // check repetitive
            if(isExists(_workspace))
                return;

            // remove items more than 5
            removeItemsMoreThan5();

            // add
            RECENTWORKSPACES.add(_workspace);
                        
        }

        static void removeItemsMoreThan5(){
            if(size()>=5) {
                RECENTWORKSPACES.removeElementAt(0);
            }
        }

        static int size(){
            return RECENTWORKSPACES.size();
        }
        
        static boolean isExists(File _workspace){
            if(size()<1)
                return false;

            for(File f:RECENTWORKSPACES)
            {
                if(f==null)
                    continue;
                if(f.compareTo(_workspace)==0)
                    return true;
            }

            return false;
        }
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package navigatorContainer;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.JList;


/**
 *
 * @author leng
 */
public class ListProjects {

    protected userInterface.GUI gui;
    protected JList list;
    protected String path;
    protected Image image_good=null;
    protected Image image_error=null;
      
    
    public ListProjects(userInterface.GUI _gui, JList _list, String _path){
        this.gui = _gui;
        this.list = _list;
        this.path = _path;

        try{
            //image = new javax.swing.ImageIcon(getClass().getResource("projects.jpg")).getImage();
            if(image_good==null)
                //image_good = Toolkit.getDefaultToolkit().getImage( getClass().getResource("/NavigatorContainer/projects_box.png") );
                 
                image_good = ImageIO.read(getClass().getResource("/navigatorContainer/archive.png").toURI().toURL());
                image_error = ImageIO.read(getClass().getResource("/navigatorContainer/error.png").toURI().toURL());
                
            //image_warning = Toolkit.getDefaultToolkit().getImage( getClass().getResource("/NavigatorContainer/projects-warning.png") );
            //image_wrong = Toolkit.getDefaultToolkit().getImage( getClass().getResource("/NavigatorContainer/projects-wrong.png") );
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1102021334:: fail to load resourece:: image file /navigatorContainer/projects_box.png");
        }
    }


    /**This method is allowed to be called from outside, so eHOST can refresh
     * the list of projects under current workspace;
     */
    public void showProjectsInList()
    {        

        //System.out.println("img = "+image.toString());
        
        Vector nulldisplay = new Vector();
        list.setListData(nulldisplay);

        if(path==null)
            return;
        if(path.trim().length()<1)
            return;

        File f = new File(path);
        if (!f.exists())
            return;
        if(!f.isDirectory())
            return;

        File[] subfiles = f.listFiles();
        Vector<Object> listentry = new Vector<Object>();

        Image image=null;
        if((subfiles!=null)&&(subfiles.length>=0))
        {
            for(File subfile:subfiles)
            {
                if (subfile==null)
                    continue;

                if (subfile.isDirectory())
                {
                    String foldername = subfile.getName();
                    nulldisplay.add(foldername);
                    // get number of txt files under this project
                    int numberOfCorpus = getNumberOfCorpus(subfile);
                    if(numberOfCorpus<0)
                        continue;
                    //System.out.println("number="+numberOfCorpus);
                    image = (numberOfCorpus>=0? image_good:image_error);
                    ListEntry_Project entry =  new ListEntry_Project(subfile, 
                            image, numberOfCorpus);
                    listentry.add(entry);
                }
            }
        }
        
        list.setListData(listentry);
        list.setCellRenderer( new ProjectListCellRander() );
        list.updateUI();
    }

    /**Return the number of corpus to a given project folder. Count *.txt
     * file under the project
     * directory.
     *
     * @return
     *    -2          : means this vaiable didn't get initilized;
     *    -1          : means error(s) occurred while trying to count
     *                  the ".txt" files;
     *    0-infinite  : means the number of corpus.
     */
    private int getNumberOfCorpus(File projectFolder){
        int number=-2;
        try{
            if(projectFolder==null)
                return -1;
            if(projectFolder.isFile())
                return -1;

            File[] folders = projectFolder.listFiles();
            if(folders==null)
                return -1;
            for(File f : folders)
            {
                if(f==null)
                    continue;
                
                // if this is the folder "saved"
                if( (f.isDirectory())
                && (f.getName().compareTo( env.CONSTANTS.corpus)==0) )
                {
                    int size=0;
                    
                    // filter to only select ".txt" files                    
                    File[] txtfiles = f.listFiles();
                    
                    for(File corpus:txtfiles){
                        //System.out.println("Filename:[ " + corpus.getName() + " ]");
                        //System.out.println("extension name:[ " + getExtension(corpus.getName()) + " ]");
                        int i = corpus.getName().lastIndexOf('.');
                        if(  i == -1){
                            size++;
                            continue;
                        }
                        if(corpus.getName().toLowerCase().endsWith(".txt"))
                            size++;
                    }
                    
                    return size;
                }
            }

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1101241125:: file to count txt files in "
                    + "folder of corpus\n"
                    + ex.getMessage());
        }
        
        return number;
    }
    
    public static String getExtension(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int i = filename.lastIndexOf('.'); 

            if ((i >-1) && (i < (filename.length() - 1))) { 
                return filename.substring(i + 1); 
            } 
        } 
        return null; 
    } 
}

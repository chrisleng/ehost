/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.workSpace;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * This is the workset which record current operating annotation and current
 * displayed text files.
 *
 * @author Jianwei Leng, Tue Jun 22 14:03:14 MDT 2010
 */
public class WorkSet {

    //---------------------------------------------------//
    /**the text file which being showed on screen.*/
    protected static File currentTextFile;

    /**the index of the file which is showed on the textpanel. 
     * The index is its position in the file list. File list stored in 
     * env.Parameters.LIST_ClinicalNotes. You can used method 
     * <code>getAllTextFile()</code> in this class or other methods 
     * in SelectedTxtFile.java to do operation.
     */
    protected static int currentTextFileIndex;

    /**return the File which being showed on screen.*/
    public static File getCurrentFile(){
        return currentTextFile;
    }
    public static int getCurrentFileIndex(){
        return currentTextFileIndex;
    }


    /**set the File which being showed on screen.*/
    public static void setCurrentFile(File currentTextFile){
        WorkSet.currentTextFile = currentTextFile;
    }

    public static void setCurrentFile(File currentTextFile, int currentTextFileIndex){
        WorkSet.currentTextFile = currentTextFile;
        WorkSet.currentTextFileIndex = currentTextFileIndex;
    }

    public static void setCurrentFile_WithIndex(File currentTextFile){
        WorkSet.currentTextFile = currentTextFile;
        WorkSet.currentTextFileIndex = getIndex(currentTextFile);
    }

    /**To a given text source file, find the index from the lib. Because
     * sometimes, you need it to set which item is seleted in your combobox.*/
    public static int getIndex(File file){
        int index=-1;

        if(file==null)
            return index;

        File[] files = env.Parameters.corpus.getFiles();
        if(files==null)
            return index;

        int size = files.length;
        for(int i=0;i<size;i++){
            File f = files[i];
            if(f==null)
                continue;
            if(f.getAbsoluteFile().compareTo(file.getAbsoluteFile())==0)
                return i;
        }

        return index;
    }

    public static void setCurrentFile(int currentTextFileIndex){
        WorkSet.currentTextFileIndex = currentTextFileIndex;
    }

    /**
     * Used to indicate where the mouse just clicked. 0 means default value;
     * 1 means clicked on navigator panel, 2 means clicked on text area; 3 means
     * that the mouse clicked on the list of selected annotations.
     */
    public static int mouse_clicked_on = 0;


    /**Get all clinical text files in an array of "File[]".
     * @return all clinical text files in an array of "File[]"
     */
    public static File[] getAllTextFile(){
        if( env.Parameters.corpus.LIST_ClinicalNotes == null )
            return null;

        int size = env.Parameters.corpus.LIST_ClinicalNotes.size();
        File[] files = new File[size];
        for(int i=0;i<size;i++){
            files[i] = env.Parameters.corpus.LIST_ClinicalNotes.get(i).file;
        }

        return files;
    }



    //---------------------------------------------------//
    /**latest cursor position of a document in text panel*/
    protected static int latestCursorPosition;

    /**record latest cursor position of a document in textpane*/
    public static void setLatestCursorPosition(int position){
        WorkSet.latestCursorPosition = position;
    }


    /**Get latest cursor position of a document in textpane*/
    public static int getLatestCursorPosition(){
        return WorkSet.latestCursorPosition;
    }


    /**The latest mouse left click position which successed selected one or
     * more annotations.
     */
    public static int latestValidClickPosition;

    /**Latest mouse click position. If compare this with
     * "atestValidClickPosition", this one record latest mouse click position
     * even if it didn't select any annotation*/
    public static int latestClickPosition;

    /**current operating annotation*/
    public static resultEditor.annotations.Annotation currentAnnotation;

    /**index of current annotation in its article in the depot  */
    public static int indexOfCurrentAnnotation;

    /**Last deleted annotation*/
    private static ArrayList<resultEditor.annotations.Annotation> lastDeleted = new ArrayList<resultEditor.annotations.Annotation>();

    /** filename of last deleted annotation*/
    private static ArrayList<String> lastDeletedFilenames= new ArrayList<String>();

    public static void addLastDeleted(String fileName, resultEditor.annotations.Annotation deleted)
    {
        lastDeletedFilenames.add(fileName);
        lastDeleted.add(deleted);
    }
    public static String getLastDeletedFilename() {
        if(lastDeletedFilenames.size() == 0)        
            return null;        
        else
            return lastDeletedFilenames.get((lastDeletedFilenames.size()-1));
    }

    
    // --------------------------- annotator and authorization information ------ //
    /**Name of current annotator*/
    public static String current_annotator_name;
    /**ID of current annotator, no space is allowed in it.*/
    public static String current_annotator_id;
    
    public static String password = null;
    /**user number id in the Annotation Admin Server (CharterReader server)*/
    public static String uid = null;
    
    public static boolean authorized = false;
    
    

    
    public static resultEditor.annotations.Annotation getLastDeleted() {
        if(lastDeleted.size() == 0)       
            return null;        
        else        
            return lastDeleted.get(lastDeleted.size()-1);
        
    }

    public static void restoredLastDeleted()
    {
        if(lastDeleted.size() == 0)
            return;
        else
        {
            lastDeleted.remove(lastDeleted.size()-1);
            lastDeletedFilenames.remove(lastDeletedFilenames.size()-1);
        }
    }


    public static ArrayList<Integer> currentlyViewing = new ArrayList<Integer>();

    public static boolean filteredViewing = false;
    public static boolean makingRelationships = false;
    //public static Annotation addingRelationships = null;

    /**latest scroll bar value for text pane of result ediot*/
    public static int latestScrollBarValue = -1;
    
    /**To each imported pins file, remember all text source name of it.*/
    protected static Vector<PINSFileSet> pinsFileSet = new Vector<PINSFileSet>();

    public static void pinsFileSet_add(String pinsFilename, String textsource){
        if(( pinsFilename == null )||(textsource == null) )    return;
        if(pinsFilename.trim().length() < 1 )    return;
        if(textsource.trim().length() < 1 )    return;

        PINSFileSet pinsfileset = getFileSet( pinsFilename );
        if ( pinsfileset == null ) {
            PINSFileSet newset = new PINSFileSet();
            newset.PINSFileName = pinsFilename.trim();
            newset.textSourceFilename.add( textsource.trim() );
            pinsFileSet.add(newset);
        }else{
            pinsfileset.textSourceFilename.add( textsource.trim() );
        }
        //System.out.println("    - record textsource["+ textsource.trim() + "] for PINS file:["
        //        + pinsFilename.trim() + "]");
    }

    private static PINSFileSet getFileSet( String pinFilename ){
        for(PINSFileSet pinsfile: pinsFileSet) {
            if( pinsfile.PINSFileName.trim().compareTo(pinFilename.trim()) == 0 )
                return pinsfile;
        }
        return null;
    }
}

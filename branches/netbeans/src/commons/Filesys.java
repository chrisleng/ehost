/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class is designned to help eHOST programmer to handle file process, such
 * as checking whether a file is existing or not, or checking whether a file is
 * a folder or not, etc.
 *
 * @author Jianwei "Chris" Leng 
 * @location: Williams Building, University of Utah,
 */
public class Filesys {

    /** find whether current file exists or not. */
    public static boolean checkFilesExists(String _fileNameWithFullPath){
            boolean re = false;
            try{
                    File f = new File( _fileNameWithFullPath );
                    re = f.exists();
            }catch(Exception e){
                    re = false;
            }
            return re;
    }

    /** Is current file a file or a directory */
    public static boolean isFileOrDirectory( String _fileNameWithFullPath ){
            boolean re = false;
            File f = new File( _fileNameWithFullPath );
            re = f.isDirectory();
            return re;
    }

    // MOVED to class dialogs.wazirdconceptlib
    // use assigned separator to split an assignment file
    // return is the amount of all valid records after being separated
    /* public static int ValidityByAssignedSeparator(String _fileNameWithFullPath, String _separator ){
        int count = 0;
        String[] str;
        try{
            BufferedReader f = new BufferedReader( new FileReader( _fileNameWithFullPath ));
            String line = f.readLine();
            while( line != null ){
                str = line.split( _separator , 2 );
                if ( str.length == 2 ){
                    count++;
                }
                line = f.readLine();
            }
            f.close();
        }catch(Exception e){
            System.out.println(e);
        }
        return count;
    }*/


    // ***************** get the clean file name ************ //
    // clean: no path, no suffix name
    public static String cleanFileName(String _filename_with_fullpath){

            String cleanFileName = null;
            int lastSlash;
            int lastPoint = 0;

            // ***STEP 1: remove the path
            // if OS is Mac OS X, use '/' as path separator
            // if OS is windows,  use '\' as path separator
            if( env.Parameters.isUnixOS ){
                    lastSlash = _filename_with_fullpath.lastIndexOf("/");
            }else{
                    lastSlash = _filename_with_fullpath.lastIndexOf("\\");
            }

            // ***STEP 2: remove the suffix name
            // if no path in front of the file name
            if ( lastSlash < 0 ) {
                    cleanFileName = _filename_with_fullpath;

                    lastPoint = cleanFileName.lastIndexOf(".");

                    // if has a suffix name, remove it
                    if (lastPoint >= 0) {
                            cleanFileName = cleanFileName.substring( 0, lastPoint );
                    }
            // if has a path in front of the file name, remove it before you remove the suffix name
            }else{
                    // the input file should have a suffix, (usually should be .txt)
                    cleanFileName = _filename_with_fullpath.substring( lastSlash + 1, _filename_with_fullpath.length() );
                    lastPoint = cleanFileName.lastIndexOf(".");
                    if (lastPoint >= 0) {
                            cleanFileName = cleanFileName.substring( 0, lastPoint ); }
                                            }
            return cleanFileName;
    }


    /** get contents from a designated text file*/
    public static ArrayList ReadFileContents(File _RAWTextfile){

        // verify the file before loading text contents
        if (_RAWTextfile == null ){
            log.LoggingToFile.log( Level.SEVERE, "### WARNING ### 11061010018:: eHOST is trying to access an non-existing raw document!!!");
            return null;
        }

        // verify the file before loading text contents
        if (!(_RAWTextfile.exists())){
            log.LoggingToFile.log( Level.SEVERE, "### WARNING ### 11061010019:: eHOST is trying to access an non-existing raw document ["
                    + _RAWTextfile.getAbsolutePath()
                    +"]!!!");
            return null;
        }


        ArrayList<String> contents = new ArrayList();
        try{
                // print info on screen
                log.LoggingToFile.log( Level.INFO, "~~~~ INFO ~~~~ ::11061010017:: start loading content of file \""
                        + _RAWTextfile.getName() + "\" ......");

                // load contents from document
                BufferedReader f = new BufferedReader( new FileReader( _RAWTextfile ));
                String line = f.readLine();



                while( line != null ){

                    // #### following code is designed to remove special
                    // #### characters form strings
                    // #### WARNING #### These codes will delete a blank line
                    //                   from your document to display
                    /*if(line==null){
                        line = f.readLine();
                        continue;
                    }

                    //int specialcharacternumbers = 0;
                    char[] linetoCharArray = line.toCharArray();
                    if((linetoCharArray==null)||(linetoCharArray.length<1)){
                        line = f.readLine();
                        continue;
                     }

                    int size = linetoCharArray.length;
                    
                    for(int i=0;i<size;i++) {
                        if(Integer.valueOf( linetoCharArray[i] ) > 127 ){
                            System.out.println("~~~~ INFO ~~~~::found special character=["
                                    +Integer.valueOf( linetoCharArray[i] )
                                    +"] in file ["
                                    + _RAWTextfile.getName() 
                                    + "]");
                            // linetoCharArray[i]= '\u0000';
                            linetoCharArray[i]= '\u0020';
                            //specialcharacternumbers++;
                        }
                    }*/


                        /*String filteredLine = "";
                        for(int j=0;j<size;j++){
                            if(linetoCharArray[j]!='\u0000')
                                filteredLine = filteredLine + linetoCharArray[j];
                        }*/

                    

                    contents.add( line );
                    //contents.add( line );
                    line = f.readLine();
                }


                //#### DEBUGDEBUG:: use to print out whole document that you are
                //#### trying to load and display onto the screen
                //for(int i=0;i<contents.size();i++){
                //    System.out.println("["+i+"] "+contents.get(i));
                //}
                //#### ENDDEBUG


                // close opened file
                f.close();
                log.LoggingToFile.log( Level.INFO,"### INFO ### 11061010017:: start loading content of file \""
                        + _RAWTextfile 
                        + "\" ------ COMPLETED"
                        );
                
        }catch(Exception e){
                log.LoggingToFile.log( Level.SEVERE, "error 1106081621::"+ e.getMessage());
        }

        return contents;
    }

    /**Count how many words in a file.<p>
     * 1. replace all "( +)" to "( )" by regular expression
     * 2. use " " as separator to split all lines in this file into
     * <code>string[]</code><p>
     *
     * @return int  amount of words in this file.
     */
    public static int countWordsInFile(File _file){
        // define the return variable
        int amount = 0;

        String[] str;
        try{
            // read each line from file pre time
            BufferedReader f = new BufferedReader( new FileReader( _file ));
            String line = f.readLine();

            // go over all lines in this the file
            while( line != null ){

                // if line is not empty
                if( line.trim().length() > 0 ) {
                    line = line.replaceAll("( +)", " ");
                    amount += line.split(" ").length;
                }
                line = f.readLine();
            }

            // close source
            f.close();

        } catch (Exception e) {
            // while file not existing or deleted
            return 0;
        }

        return amount;
    }
        
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;

/**
 *
 * @author Jianwei Chris Leng
 *         03:57am Jan 26, 2011
 */
public class Reader {

    public String readContents(File corpus){

        String contents = "";
        
        if(!checkFile(corpus)){
            return null;
        }
        
        try{
        
            BufferedReader corpusFile = new BufferedReader(new FileReader(corpus));
            String line = corpusFile.readLine();

            while (line != null) {
                contents = contents + line + " ";
                line = corpusFile.readLine();
            }

            // end
            corpusFile.close();
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1101260421::fail to read contents from corpus!!!");
            return null;
        }

        return contents;
    }

    /**This function should be removed to commons package.
     */
    public boolean checkFile(File corpus){
        try{
            if(!corpus.exists())
                return false;
            if(corpus.isDirectory())
                return false;
        }catch(Exception ex){
            return false;
        }

        return true;
    }

    
}

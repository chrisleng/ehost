/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.io.File;

/**
 * Extract annotation from XML files or PINS files.
 *
 * @author Jianwei Leng 9:41pm 6-18-2010 Friday
 */
public class Extractor {

    /**Extract annotations from pins files and get analysised information
     * into memory
     * @param   files   a list of PINS files
     */
    public void fromPINSfiles(File[] files){

        // validity check
        if (files == null) return;

        // validity check
        int size = files.length;
        if(size < 1) return;

        // #1 extract annotations from each PINs file from the list
        for(int i=0;i<size; i++) {            
            // if exists and is a file
            if(( files[i] != null )&&( files[i] instanceof File )){                
                // run parser to PIN file
                resultEditor.io.PinFile pinfile = new resultEditor.io.PinFile( files[i] );
                pinfile.startParsing(true);
            }
        }
    }
}

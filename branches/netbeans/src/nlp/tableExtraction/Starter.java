/*
 *  nlp.tableExtraction.Starter.java
 */ 
package nlp.tableExtraction;

import env.clinicalNoteList.CorpusStructure;
import java.io.File;
import java.util.Vector;
import userInterface.GUI;

/**
 * This module is designed for Brian Sauer's NLP project to extract dose 
 * information from unformatted text table, and this class is the starter.
 * 
 * @author  Chris Jianwei Leng
 * @since   Java 1.6
 * @since   Nov 14, 2012, MST, 15:03 WED
 */
public class Starter {
    
    // public GUI gui;
    
    /**Start the starter.*/
    public void fire( File[] files, GUI gui ){
        
        if( files == null )
            return;
            
        for( File file : files ){
            // analysis
            Analyst analyst = new Analyst( file, gui  );
            // results = analyst.analyse();
            analyst.analyse();
            //display( results );
        }
    }
    
    /**Start the starter.*/
    public void fire( File file, GUI gui ){
        
            // analysis
            Analyst analyst = new Analyst( file, gui );
            // results = analyst.analyse();
            analyst.analyse();
            //display( results );
        
    }
    
    public void fire( GUI gui){
        
        // get current files
        Vector<CorpusStructure> corpuses = env.Parameters.corpus.LIST_ClinicalNotes;
        if(( corpuses == null )||(corpuses.size()<1)){
            System.out.println("no files in the waiting list!");
            return;
        }
        
        // start analyse and display result
        for( int i=0; i<corpuses.size(); i++ ){
         CorpusStructure file = corpuses.get(i);
            if( file != null )                
                fire( file.file, gui );
        }
    }

    void fixfile(GUI gui) {
        Vector<CorpusStructure> corpuses = env.Parameters.corpus.LIST_ClinicalNotes;
        if(( corpuses == null )||(corpuses.size()<1)){
            System.out.println("no files in the waiting list!");
            return;
        }
        
        // start analyse and display result
        for( int i=0; i<corpuses.size(); i++ ){
            CorpusStructure file = corpuses.get(i);
            if( file != null )                
            {
                // analysis
                Analyst analyst = new Analyst( file.file, gui );
                // results = analyst.analyse();
                analyst.preprocess();
            //display( results );
            }
        }
    }
}

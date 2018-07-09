/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.analysis;

import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author Chris
 */
public class DiffAnalysisResult {
    
    private static Vector<DiffAnnotator> diffResults = new Vector<DiffAnnotator>();
    
    /**remove all items of results*/
    public static void removeAll(){
        diffResults.clear();
    }
    
    /**Structure that used to store all analysis results for each Annotator. To 
     * each annotator, it has a list of all articles; and each article has a 
     * list of annotation of the annotator; to each annotation, we have 
     * 
     */
    public Vector<DiffAnnotator> getAll(){
        return diffResults;
    }
    
    
    
    public static void initAnnotator(String annotatorName ) throws Exception{
        if( (annotatorName==null)||(annotatorName.trim().length() <1) )
            throw new Exception("1108261253::you can not init an analysis "
                    + "result with a null or empty annotation name.");
        
        if(inited(annotatorName))
            throw new Exception("1108261256::result space for this annotator "
                    + "has been inited yet, and do not need to be inited again.");
        
        diffResults.add( new DiffAnnotator(annotatorName));
        
        log.LoggingToFile.log(Level.FINEST, "1108261317::inited space for annotator:" + annotatorName);
    }
    
    /***/
    public static boolean inited(String annotatorName) throws Exception{
        try{
            // return true if found
            for(DiffAnnotator record : diffResults){
                if(record.Annotator.trim().compareTo(annotatorName.trim())==0)
                    return true;
            }            
            // return false if this annotator didn't get inited
            return false;
            
        }catch(Exception ex){
            throw new Exception( "1108261304: error get thrown out while "
                    + "checking whether the analysis result to "
                    + "an annotator has been inited to record all matched and "
                    + "unmatched comparsion details" 
                    + "\nException details: ["+ ex.getMessage() + "]");
        }
    }
    
    public static void addDiffedArticle(String annotator, DiffedArticle diffedArticle) throws Exception{
        try{
            // check all diffed annotator records to find this annotator
            for(DiffAnnotator record: diffResults)
            {
                if(record==null)
                    continue;
                
                // if found this diffed annotator, record newly built diffedArticle
                if(record.Annotator.compareTo(annotator.trim())==0)
                {
                    record.add(diffedArticle);
                }
            }
        }catch(Exception ex){
            throw new Exception("1108261407::" + ex.getMessage());
        }
    }

    
    
}

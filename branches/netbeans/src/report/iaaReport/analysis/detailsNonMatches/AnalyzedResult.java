/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author leng
 */
public class AnalyzedResult {

    /**result depot of IAA analysis*/
    private static Vector<AnalyzedAnnotator> results = new Vector<AnalyzedAnnotator>();
    private static ArrayList<String> selectedAnnotators = null;


    /**This one is used to print out all contents in the class of
     * "AnalyzedResult" for programmer to debug*/
    static void printResults() {
        System.out.println("==========> Total has " 
                + results.size()
                + " annotators has children in this diff operation.");
        
        for( int i=0; i<results.size(); i++) {
            System.out.println("==========> we are handing " + i + " data of " + results.size() );
            AnalyzedAnnotator aator = results.get(i);
            if(aator==null){
                System.out.println("==========> this one has no data " );
                continue;
            }else
                System.out.println("==========> this one has " +aator.analyzedArticles.size() + " articles" );

            
        }
    }


    public Vector<AnalyzedAnnotator> getAll(){
        return results;
    }

    public static boolean rowHeadsInited(String annotator, String articleFilename) throws Exception{
        if(results==null){
            results = new Vector<AnalyzedAnnotator>();
            return false;
        }



        if(annotator==null)
            throw new Exception("1109020146::annotator name can not be null!");

        if(annotator.trim().length()<1)
            throw new Exception("1109020144::annotator name can not be empty string!");

        if(articleFilename==null)
            throw new Exception("1109020157::file name of current article can not be null!");

        if(articleFilename.trim().length()<1)
            throw new Exception("1109020156::file name of current article can not be empty string!");

        for(AnalyzedAnnotator analyzedAnnotatorRecord : results)
        {
            if(analyzedAnnotatorRecord==null)
                continue;

            if(analyzedAnnotatorRecord.mainAnnotator==null)
                throw new Exception("1109020145::wrong data value: result.mainAnnotator=null!");

            if(annotator.trim().compareTo( analyzedAnnotatorRecord.mainAnnotator.trim() )==0)
            {
                if(analyzedAnnotatorRecord.analyzedArticles == null)
                    return false;

                // #### if record of this annotator have this article
                if(analyzedAnnotatorRecord.articleExists(articleFilename))
                    return true;                                
            }
        }

        return false;

    }
    
    public static void clear(){
        results.clear();
        
        if(selectedAnnotators!=null)
            selectedAnnotators.clear();
    }

    /**tell system which annotators have been selected for feature comparsion.*/
    public static void setAnnotators(ArrayList<String> selectedAnnotators){
        AnalyzedResult.selectedAnnotators = selectedAnnotators;
    }

    public static void addAnalyzedAnnotator(AnalyzedAnnotator aa){
        if(exists(aa)){

            for(AnalyzedAnnotator analyzedannotator : results){
                if(analyzedannotator.mainAnnotator.trim().compareTo(aa.mainAnnotator.trim())==0)
                {
                    
                }
            }

        }else{
           results.add(aa);
        }
    }

    /**check whether the store space of one annotator has been allocated.
     */
    public static boolean exists(AnalyzedAnnotator aa){
        for(AnalyzedAnnotator analyzedAnnotator : results)
        {
            if(analyzedAnnotator.mainAnnotator.trim().compareTo(aa.mainAnnotator.trim())==0)
                return true;
        }

        return false;
    }
    
    
}

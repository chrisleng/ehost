/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;

/**
 *
 * @author leng
 */
public class GetAnnotators {

    /**the vector which we used to store all found annotators*/
    public ArrayList<String> annotators = new ArrayList<String>();

    public void clear(){
        annotators.clear();
    }
    
    
    /**This method will go though all annotations in memory and collect all
     * annotators we found. And then return a vector of string for these 
     * annotators.
     * 
     * @return  all annotators' name in a vector of strings
     */
    public ArrayList<String> getAnnotators(){
        
        // #### clear old data: annotators and classes
        clear();
        
        log.LoggingToFile.log(Level.FINE, "Begin collecting all annotators from annotations which saved in memory.");

        try{
            // get all articles
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            ArrayList<Article> articles = depot.getAllArticles();
            if((articles==null)||(articles.size()<1))
            {
                log.LoggingToFile.log(Level.WARNING, "1108170321:: no annotation-article can be found in current dataset!!!");
                return null;
            }

            for(Article article : articles )
            {
                if(article==null)
                    continue;
                if(article.annotations == null)
                    continue;

                for(Annotation annotation: article.annotations)
                {
                    if(annotation==null)
                        continue;
                    if(annotation.getAnnotator() ==null)
                        continue;
                    
                    // call another method to record this annotator
                    recordAAnnotator(annotation.getAnnotator() );                                                                       
                                
                }
            }
       
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1108170348:: fail to collect all annotators' names!!!");
        }

        return annotators;
    }
    
    private void recordAAnnotator(String annotatorname){
        if(annotatorname==null)
            return;
        if(annotatorname.trim().length()<1)
            return;

        if(annotatorExists(annotatorname))
            return;

        this.annotators.add(annotatorname.trim());

        
    }

    private boolean annotatorExists(String annotatorname){
        if(annotatorname==null)
            return true;
        if(annotatorname.trim().length()<1)
            return true;

        for(String annotator: this.annotators)
        {
            if(annotator==null)
                continue;
            if(annotator.trim().length()<1)
                continue;

            if(annotator.trim().compareTo(annotatorname.trim())==0)
                return true;
        }

        return false;
    } 

}

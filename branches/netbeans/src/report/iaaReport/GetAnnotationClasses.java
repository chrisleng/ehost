/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport;

import java.util.ArrayList;
import java.util.logging.Level;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;

/**
 *
 * @author Chris Jianwei Leng, 2011-08-25
 */
public class GetAnnotationClasses {
    
    /**the vector which we used to store all found annotation class*/
    public ArrayList<String> classes = new ArrayList<String>();

    public void clear(){
        classes.clear();
    }
    
    public ArrayList<String> getAnnotationClasses(){
        
        // #### clear old data: annotators and classes
        clear();
        
        log.LoggingToFile.log(Level.FINE, "Begin collecting all annotation class names from annotations which saved in memory.");

        try{
            // get all articles
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            ArrayList<Article> articles = depot.getAllArticles();
            if((articles==null)||(articles.size()<1)){
                log.LoggingToFile.log(Level.WARNING, "1108250321:: no annotation-article can be found in current dataset!!!");
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
                    
                    if(annotation.annotationclass == null)
                        continue;
                    if(annotation.annotationclass.trim().length()<1)
                        continue;
                    
                    // call another method to record this class
                    recordAClass( annotation.annotationclass );                                                            
                                
                }
            }
       
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1108250348:: fail to collect all annotators' names!!!");
        }

        return classes;
    }

    /**To a class we found in stored annotations, we should record it in the 
     * a vector here in this class if it hasn't got recorded yet.
     * 
     * @param   annotationClassName
     *          the name of the class
     */
    private void recordAClass( String annotationClassName ){
        
        // check validity
        if( annotationClassName == null )
            return;
        if( annotationClassName.trim().length() < 1 )
            return;
        
        // do nothing if this annotation class name already got recorded 
        if( classExists( annotationClassName ) )
            return;
        
        this.classes.add(annotationClassName.trim() );
        
    }
    
    
    private boolean classExists(String annotationClassName){
        if(annotationClassName==null)
            return true;
        if(annotationClassName.trim().length()<1)
            return true;

        for(String annotation_class_name: this.classes)
        {
            if(annotation_class_name==null)
                continue;
            if(annotation_class_name.trim().length()<1)
                continue;

            if(annotation_class_name.trim().compareTo (annotationClassName.trim() )==0)
                return true;
        }

        return false;
    }

    

}

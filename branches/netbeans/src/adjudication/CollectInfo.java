/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package adjudication;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;

/**
 * Collect all annotators and annotation classes that appeared in current
 * loaded or generated annotations.
 *
 * @author Jianwei Chris leng, 2011-11-14 01:13am
 * 
 */
public class CollectInfo {
    
    /**the vector which we used to store all found annotators*/
    public Vector<String> annotators = new Vector<String>();

    /**the vector which we used to store all found annotation class*/
    public Vector<String> classes = new Vector<String>();

    /**remove all saved information in vectors in this class*/
    public void clear(){
        annotators.clear();
        classes.clear();
    }

    /**get found annotators in vector of string.
     *
     * @return  all annotator's name in a vector of strings
     */
    public Vector<String> getAnnotators(){
        return annotators;
    }

    /**get found class names in vector of string.
     *
     * @return  all names of classes in a vector of strings
     */
    public Vector<String> getClassNames(){
        return classes;
    }


    /**This method will go though all annotations in memory and collect all
     * annotators we found. And then return a vector of string for these
     * annotators.
     *
     */
    public void gatherInformation(){

        // #### clear old data: annotators and classes
        clear();

        log.LoggingToFile.log(Level.FINE, "begin collect annotators and annotation classes for adjudication mode");

        try{
            // get all articles
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            ArrayList<Article> articles = depot.getAllArticles();
            if((articles==null)||(articles.size()<1))
            {
                log.LoggingToFile.log(Level.WARNING, "1108170321:: no annotation-article can be found in current dataset!!!");
                return;
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

                    String annotator = annotation.getAnnotator();
                    //System.out.println("***> "+annotator);
                    
                    // call another method to record this annotator
                    recordAAnnotator( annotator );

                    if(annotation.annotationclass == null)
                        continue;
                    if(annotation.annotationclass.trim().length()<1)
                        continue;

                    // call another method to record this class
                    recordAClass( annotation.annotationclass );

                }
            }

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1108170348:: fail to collect all annotators' names!!!");
        }

        log.LoggingToFile.log(Level.FINE, "END to collect annotators and annotation classes for adjudication mode");

        return;
    }

    /**to an given annotator, record it if it hasn't been recorded yet*/
    private void recordAAnnotator(String annotatorname){
        if(annotatorname==null)
            return;
        if(annotatorname.trim().length()<1)
            return;

        if(annotatorExists(annotatorname))
            return;

        this.annotators.add(annotatorname.trim());
    }

    /**check whether an annotator is already found or not*/
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

    /**check whether a given classname is existing or not*/
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

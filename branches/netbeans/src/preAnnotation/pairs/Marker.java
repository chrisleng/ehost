/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation.pairs;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.awt.Color;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class Marker {

    public final static String specificclass = "Person";

    public void MarkerPairs(){
        
        // ##1## remove repetitive or overlapped term which has a small span
        //       than others
        clean();

        // ##2## sort annotations belongs to class "person"
        //sort();

        // ##3## try to build pairs
        searchPairs();
        
    }

    

    private String searchPairs(){
        String return_msg=null;
        try{
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            Vector<Article> articles = depot.getDepot();
            if(articles==null)
                return "[WARNING] No annotation found!!!";

            // #### to each article
            for(Article article: articles)
            {
                // #### sort annotaions in article
//                searchPairs(article);

            }
        }catch(Exception ex){
            String error_msg = "[error 1101261439]:: fail to search annotations " +
                    "in pairs!!!"
                    + ex.getMessage();
            log.LoggingToFile.log(Level.SEVERE, error_msg);
            return error_msg;
        }
        
        return return_msg;
    }

    /*private String searchPairs(Article article){
        
    
        if(article==null)
            return "[Warning 1101261443]:: this article is null";

        log.LoggingToFile.log(Level.SEVERE, "Searching paris in file " + article.filename );
        
        if((article.annotations==null)||(article.annotations.size()<1))
        {
            String msg = "[WARNING 1101261444]:: no annotations in this article";
            log.LoggingToFile.log(Level.SEVERE, msg);
            return msg;
        }



        Dictionary dict = new Dictionary();

        try{
            
            int size = article.annotations.size();
            log.LoggingToFile.log(Level.SEVERE, "  + number of annotationa: " + size );

            for( int i=size-1; i>=0; i-- )
            {

                Annotation annotation1 = article.annotations.get(i);

                if(annotation1==null)
                    continue;

                // only handle annotations belong to class "person"
                if(!this.isClassPerson(annotation1))
                    continue;

                String term1 = annotation1.annotationText.trim().toLowerCase();

                String subtype1 = dict.getSubType(term1);
                if(subtype1==null)
                    continue;

                // compare this other with others to make sure this one is the
                // smallest one
                Annotation closest = null;
                for(int j=0; j<size; j++ )
                {
                    if(i==j)
                        continue;
                    
                    Annotation annotation2 = article.annotations.get(j);                    

                    if(annotation2==null)
                        continue;

                    // only handle annotations belong to class "person"
                    if(!this.isClassPerson(annotation2))
                        continue;

                    String term2 = annotation2.annotationText.trim().toLowerCase();

                    String subtype2 = dict.getSubType(term2);
                    if(subtype2==null)
                        continue;

                    //#### begin to search cloest persion, forward
                    //#### if these two annotations have SAME subtype
                    if(subtype1.compareTo(subtype2)==0){

                        if((annotation2.spanstart>=annotation1.spanend)&&(annotation2.spanend>=annotation1.spanend)){
                            continue;
                        }
                            
                        if(closest==null)
                            closest = annotation2;

                        // annotation2 occurred in front of recorded annotation "cloest"
                        if((closest.spanend<=annotation2.spanstart)&&(closest.spanstart<annotation2.spanstart)){
                            closest = annotation2;
                        }
                            

                        
                    }
                    
                   
                    
                }

                // build and save this complex relationship of "pairs of person"
                if(closest!=null)
                {
                    resultEditor.annotations.eComplex eComplex
                            = new resultEditor.annotations.eComplex(closest);
                    resultEditor.annotations.ComplexRelationship cr = new
                            resultEditor.annotations.ComplexRelationship("Coref Person");
                    cr.addLinked(eComplex);
                    if(annotation1.ComplexRelationships==null)
                        annotation1.ComplexRelationships = new Vector<resultEditor.annotations.ComplexRelationship>();

                    annotation1.ComplexRelationships.add(cr);
*/
                    /*
                     System.out.println("[==PAIRS==]: " + annotation1.annotationText + " @"
                            + annotation1.spanstart + ", " + annotation1.spanend
                            + " linked to \""+closest.annotationText+"\"("+annotation1.uniqueIndex+")"
                            + " (" + closest.spanstart
                            + ", " + closest.spanend
                            + ") to subclass ::"  );
                     */
/*    
                    article.annotations.setElementAt(annotation1, i);
                }
            }
        }
        catch(Exception ex)
        {
            log.LoggingToFile.log(Level.SEVERE, "[ERROR 1101261737]:: fail to search or set up paris\n"+
                    ex.toString());
        }
        return null;
    }
*/

    /**This method is used to sort annotations in the memeory.*/
    private String sort(){
        try{
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            Vector<Article> articles = depot.getDepot();
            if(articles==null)
                return "[WARNING] No annotation found!!!";

            // #### to each article
            for(Article article: articles)
            {
                
                // #### sort annotaions in article
                sort(article);
                
            }
        }catch(Exception ex){
            String return_msg = "[error 1101261406]:: fail to sort annotations"
                    + ex.getMessage();
            log.LoggingToFile.log(Level.SEVERE,  return_msg);
        }

        return null;
    }

    /**sort original annotations in memory to a specific article.*/
    private String sort(Article article){
/*
        if(article==null)
            return "[Warning 1101261407]:: this article is null";

        if((article.annotations==null)||(article.annotations.size()<1))
            return "[WARNING 1101261408]:: no annotations in this article";

        for(int i=0;i<article.annotations.size();i++){

            Annotation annotation1 = article.annotations.get(i);

            if(annotation1==null)
                continue;

            // only handle annotations belong to class "person"
            if(!this.isClassPerson(annotation1))
                continue;

            int start1=annotation1.spanstart, end1 = annotation1.spanend;

            // compare this other with others to make sure this one is the
            // smallest one
            for(int j=i+1;j<article.annotations.size();j++)
            {
                Annotation annotation2 = article.annotations.get(j);

                if(annotation2==null)
                    continue;

                // only handle annotations belong to class "person"
                if(!this.isClassPerson(annotation2))
                    continue;
                

                int start2 = annotation2.spanstart, end2 = annotation2.spanend;

                // comp them to make sure annotaion with smaller coordinates
                // will occurred in head of the article
                if((start2<end1)&&(end2<end1)){
                    // switch these two annotations
                    Annotation tmp = annotation1;
                    annotation1=annotation2;
                    annotation2=tmp;

                    article.annotations.setElementAt(annotation1, i);
                    article.annotations.setElementAt(annotation2, j);

                }
                
            }
        }
*/
        return null;

    }

    
    /** (1). remove repetitive annotations
     *      (remove the second one, if we found two annotations have same spans. )
     *  (2). remove overlap annotations
     *      (remove the annotation with the smaller span, this smaller span should
     *       be included in the bigger span.)
     */
    private String clean(){
 /*       try{
            // get all annotations
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            Vector<Article> articles = depot.getDepot();
            if(articles==null)
                return "[WARNING] No annotation found!!!";

            // #### to each article
            for(Article article: articles)
            {
                if(article==null)
                    continue;
                if((article.annotations==null)||(article.annotations.size()<1))
                    continue;


                int size = article.annotations.size();

                // #### go over each annotaions to this article                
                for( int i=size-1; i>=0; i-- )
                {
                    // current annotation in this article
                    Annotation annotation1 = article.annotations.get(i);
                    if ( annotation1 == null )
                        continue;
                    if(!isClassPerson(annotation1))
                        continue;
                        
                    int start1=annotation1.spanstart, end1=annotation1.spanend;
                    
                    // #### (1). remove repetitive annotations
                    for( int j=size-1; j>=0; j-- )
                    {
                        if(i==j)
                            continue;
                        
                        // comparable annotation
                        Annotation annotation2 = article.annotations.get(j);
                        if(annotation2==null)
                            continue;
                        if(!isClassPerson(annotation2))
                            continue;
                        

                        int start2=annotation2.spanstart, end2=annotation2.spanend;

                        
                        // #### if they have same spans
                        int overlap_flag = 0;
                        overlap_flag = checkOverlapType(start1, end1, start2, end2);
                        //  0:  no overlapping or repetitive
                        //  1:  they have exact same spans
                        //  2:  span of annotation 2 is included in the span of annotaiton1
                        //      so, please delete annotation 2
                        //  3:  span of annotation 1 is included in the span of annotation2
                        //      so delete annotation 1

                        // 1: delete second annotation, continue
                        // 2: delete second annotation, continue
                        if((overlap_flag==1)||(overlap_flag==2))
                        {
                            article.annotations.setElementAt(null,j);
                        }
                        // delete first annotation, break this loop
                        else if(overlap_flag==3)
                        {
                            article.annotations.setElementAt(null,i);
                            break;
                        }

                    }                   
                } // end cleaning opertation to this article


                // #### erase null entry from the vector of annotations
                for( int z=size-1; z>=0; z-- )
                {
                    Annotation annotation3 = article.annotations.get(z);
                    if(annotation3==null) {
                        article.annotations.removeElementAt(z);
                    }
                } // end of erasing
                
            } // end this article, go next one in the loop
            
        }
        catch(Exception ex)
        {
            String err = "[error 1101261645]:: fail to clean annotations for "
                    + "marking pairs (pre-annotation for pairs)."
                    + ex.getMessage();
            log.LoggingToFile.log(Level.SEVERE,  err);
            return err;
        }
*/
        return null;
    }


    /**This method is used to check whehter an annotation belongs to class 
     * "Person"; In this class, String "person" is defined in static variable
     * "specificclass".
     *
     * @return  true:   this annotation belongs to class "person";
     *          false:  this annotation does NOT belong to class "person".
     */
    private boolean isClassPerson(Annotation annotation){
        boolean to_return = false;

        if(annotation==null)
            return false;

        if(annotation.annotationclass==null)
            return false;

        if(annotation.annotationclass.trim().length()<1)
            return false;

        if(annotation.annotationclass.trim().toLowerCase().compareTo(specificclass.toLowerCase())==0)
            return true;

        return to_return;
    }


    /**
     * This method is used to compare two annotation spans. This method reports
     * what kind of overlapping occurred between them. Return 0, if there is no
     * any overlapping.
     *
     * @param start1    span start of the first annotation
     * @param end1      span end of the first annotation
     * @param start2    span start of the second annotation
     * @param end2      span end of the second annotation
     *
     * @return
     *  0:  no overlapping or repetitive
     *  1:  they have exact same spans
     *  2:  span of annotation 2 is included in the span of annotaiton1
     *      so, please delete annotation 2
     *  3:  span of annotation 1 is included in the span of annotation2
     *      so delete annotation 1
     */
    private int checkOverlapType(int start1, int end1, int start2, int end2)
    {
        int to_return = 0;
        // exact same span
        if((start1==start2)&&(end1==end2))
        {
            to_return  = 1;
        }
        // annotation2's span is included in annotation #1 's span
        else if ((start1<start2)&&(end2<=end1))
        {
            to_return = 2;
        }
        else if ((start1<=start2)&&(end2<end1))
        {
            to_return = 2;
        }
        // annotation1's span is included in annotation #1 's span
        else if ((start2<start1)&&(end1<=end2))
        {
            to_return = 3;
        }
        else if ((start2<=start1)&&(end1<end2))
        {
            to_return = 3;
        }

        return to_return;
    }
}

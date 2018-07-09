/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

import java.util.ArrayList;
import java.util.Vector;
import report.iaaReport.analysis.detailsNonMatches.Comparator;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;

/**
 *
 * @author leng
 */
public class ClassAgreementDepot {

    

    
    public static Vector<ClassAgreementAnnotators> results = new Vector<ClassAgreementAnnotators>();
    

    public static void clear(){
        results.clear();
    }

    
    public static void record(String annotator1, String annotator2) throws Exception{

        ClassAgreementAnnotators ca = new ClassAgreementAnnotators(annotator1, annotator2);

        if((annotator1==null)||(annotator2==null))
            throw new Exception("1109152100");

        if((annotator1.trim().length()<1)||(annotator2.trim().length()<1))
            throw new Exception("1109152101");

        try{
            Depot depot = new Depot();

            ArrayList<Article> articles = depot.getAllArticles();

            if(articles==null)
            {
                // error process
            }

            // #### go though all articles
            for(Article article:articles)
            {
                if(article==null)
                    continue;
                if(article.annotations==null)
                    continue;

                for(Annotation ann:article.annotations)
                {
                    if(ann==null)
                        continue;
                    if((ann.annotationclass==null)||(ann.annotationclass.trim().length()<1))
                        continue;
                    
                    if( !IAA.isClassSelected(ann.annotationclass ) )
                        continue;

                    if( ( Comparator.checkAnnotator( ann, annotator1 ) ) ||
                        ( Comparator.checkAnnotator( ann, annotator2 ) ) )
                    {
                        ca.recordMatchedStatus( ann.annotationclass, ann.isComparedAsMatched );
                    }
                }
            }

            results.add(ca);

        }catch(Exception ex){
            throw new Exception("1109182342");
        }
    }

    public static void record() throws Exception{

        ClassAgreementAnnotators ca = new ClassAgreementAnnotators();       

        try{
            Depot depot = new Depot();

            ArrayList<Article> articles = depot.getAllArticles();

            if(articles==null)
            {
                // error process
            }

            // #### go though all articles
            for(Article article:articles)
            {
                if(article==null)
                    continue;
                if(article.annotations==null)
                    continue;

                for(Annotation ann:article.annotations)
                {
                    if(ann==null)
                        continue;
                    if((ann.annotationclass==null)||(ann.annotationclass.trim().length()<1))
                        continue;

                    
                    ca.recordMatchedStatus(ann.annotationclass, ann.is3WayMatched);
                    
                }
            }

            results.add(ca);

        }catch(Exception ex){
            throw new Exception("1109182343");
        }
    }

    static void completeForms() {
        for(ClassAgreementAnnotators ca : results)
        {
            if(ca==null)
                continue;

            ca.completeForms();
            
        }

    }
    


    

    

    /*public static void init(String classname) throws Exception{
        if((classname==null)||(classname.trim().length()<1))
            throw new Exception("--------> error 11081903336:: the given classname which you want to be inited is NULL!!!");

        for(ClassAgreementRecord car: AllClassAgreementRecords){
            if( car.classname.trim().compareTo(classname.trim())==0){
                
            }
        }
        
    }*/
    
}

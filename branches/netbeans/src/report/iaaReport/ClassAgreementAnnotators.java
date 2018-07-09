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
public class ClassAgreementAnnotators {

    public String annotator1, annotator2 = null;

    /**contructor for 2 way table*/
    public ClassAgreementAnnotators(String annotator1, String annotator2){
        this.annotator1 = annotator1;
        this.annotator2 = annotator2;
    }

    /**contructor for 3 way table*/
    public ClassAgreementAnnotators(){
        this.annotator1 = "3way--------";
        this.annotator2 = "3way--------";
    }

    public Vector<ClassAgreementRecord> AllClassAgreementRecords = new Vector<ClassAgreementRecord>();

    public  void clear(){
        AllClassAgreementRecords.clear();
    }

    public  void completeForms()
    {
        for(ClassAgreementRecord car : AllClassAgreementRecords )
        {
            if(car==null)
                continue;

            car.completeRecord();
        }
    }

    public void record(){
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

                    recordMatchedStatus(ann.annotationclass, ann.isComparedAsMatched);
                }
            }

        }catch(Exception ex){
        }
    }

    public  void record(String annotator1, String annotator2) throws Exception{

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

                    if( ( Comparator.checkAnnotator( ann, annotator1 ) ) ||
                        ( Comparator.checkAnnotator( ann, annotator2 ) ) )
                    {
                        recordMatchedStatus(ann.annotationclass, ann.isComparedAsMatched);
                    }
                }
            }

        }catch(Exception ex){
        }
    }

    protected   void recordMatchedStatus(String classname, boolean isMatched) throws Exception{
        if((classname==null)||(classname.trim().length()<1))
            throw new Exception("--------> 1108190358::");

        boolean found = false;

        for(ClassAgreementRecord car : AllClassAgreementRecords )
        {
            if(car.classname.trim().compareTo(classname.trim())==0)
            {
                if(isMatched)
                    car.matches = car.matches + 1;
                else
                    car.nonmatches = car.nonmatches + 1;

                found = true;
            }


        }

        if(!found){
                AllClassAgreementRecords.add( new ClassAgreementRecord(classname.trim(), isMatched));
            }
    }
}

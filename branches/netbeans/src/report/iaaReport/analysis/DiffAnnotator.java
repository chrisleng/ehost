/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.analysis;

import java.util.Vector;

/**
 * Data Structure that we used to store analysis result of IAA analysis for a
 * specific annotator.
 * 
 * 
 * @author Chris    2011-08-26
 */
public class DiffAnnotator {
    
    /**the annotator. 
     * we compare all annotations between this annotator and others, and we save
     * all matched information and unmatched information in memory.
     */
    public String Annotator = null;

    
    private Vector<DiffedArticle> articles = new Vector<DiffedArticle>();

    /**return all diffed/analysised results to a specific annotator, and the
     * results is Non-modifiable.*/
    public final Vector<DiffedArticle> getAll(){
        return articles;
    }

    /**add a article*/
    public void add(DiffedArticle article) throws Exception{
        try{
            
            // #### if this article (by the name) already can be find in the list
            //      of articles, just extract ezch records of the given parameter
            //      , and then put them into the existed article.
            if( articleExists(article) )
            {
                for(DiffedArticle diffedarticle : articles)
                {
                    if(diffedarticle==null)
                        continue;
                    // if the article is existing here yet
                    if(diffedarticle.article.trim().compareTo(article.article.trim())==0)
                    {
                        // add eached diffed annotation into existing article
                        for(DiffedAnnotationDef diffedannotation: article.DiffedAnnotations)
                        {
                            if(diffedannotation==null)
                                continue;
                            diffedarticle.DiffedAnnotations.add(diffedannotation);
                        }
                        return;
                    }
                }
            }
            // #### 2 directly  add this article into vector if it isn't in 
            // "Vector<DiffedArticle> articles" in this class now
            else
            {
                articles.add(article);
            }
        }catch(Exception ex){
            throw new Exception("1108261336::" + ex.getMessage());
        }
    }
    
    
    
    public DiffAnnotator(){
        
    }
    
    public DiffAnnotator(String annotator) throws Exception{
        if((annotator==null)||(annotator.trim().length()<1))
            throw new Exception("1108261311::"+ this.getClass().getName().toString() 
                    + ":: class can not completed " );
        
        this.Annotator = annotator;
                
    }
    
    public boolean articleExists(String articlename) throws Exception{
        if((articlename==null)||(articlename.trim().length()<1))
            throw new Exception("1108261523::");
        
        try{
            for(DiffedArticle article: articles)
            {
                if(article==null)
                    continue;
                if((article.article==null)||(article.article.trim().length()<1))
                    throw new Exception("1108261525::");
                
                if(article.article.trim().compareTo(articlename.trim())==0)
                    return true;
                else 
                    return false;
                    
            }
        }catch(Exception ex){
            throw new Exception("1108261524::");            
        }
        
        return false;
        
    }
    
    public boolean articleExists(DiffedArticle diffedArticle) throws Exception{
        if(diffedArticle==null)
            throw new Exception("1108261522::");
        
        return articleExists( diffedArticle.article );
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import java.util.Vector;

/**
 * 纯粹某个user和其他用户annotations的比较结果
 *
 * @author leng
 */
public class AnalyzedAnnotator {

    /**主annoatator*/
    public String mainAnnotator = null;

    public String[] annotators = null;

    public Vector<AnalyzedArticle> analyzedArticles = new Vector<AnalyzedArticle>();

    public boolean articleExists(String filename) throws Exception{
        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1109020201::given filename of the article can not be null or empty!");

        if(analyzedArticles==null)
        {
            analyzedArticles = new Vector<AnalyzedArticle>();
            return false;
        }

        for(AnalyzedArticle analyzedArticle: analyzedArticles)
        {
            if(analyzedArticle==null)
                continue;
            
            if((analyzedArticle.filename==null)||(analyzedArticle.filename.trim().length()<1))
                throw new Exception("1109020202::one instance of " +
                        "\"AnalyzedArticle\" in \"Vector<AnalyzedArticle> " +
                        "analyzedArticles\" has a null filename!");

            if(analyzedArticle.filename.trim().compareTo(filename.trim())==0)
                return true;
        }
        return false;
    }

    public AnalyzedAnnotator(String mainAnnotator){
        this.mainAnnotator = mainAnnotator;
    }

    AnalyzedArticle getArticle(String filename) throws Exception {
        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1109020245::given filename of the article can not be null or empty!");

        if(analyzedArticles==null)
        {
            analyzedArticles = new Vector<AnalyzedArticle>();
            return null;
        }

        for(AnalyzedArticle analyzedArticle: analyzedArticles)
        {
            if(analyzedArticle==null)
                continue;

            if((analyzedArticle.filename==null)||(analyzedArticle.filename.trim().length()<1))
                throw new Exception("1109020202::one instance of " +
                        "\"AnalyzedArticle\" in \"Vector<AnalyzedArticle> " +
                        "analyzedArticles\" has a null filename!");

            if(analyzedArticle.filename.trim().compareTo(filename.trim())==0)
                return analyzedArticle;
        }
        
        return null;
    }


     public void setArticle(String filename, AnalyzedArticle article) throws Exception {
        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1109090245::given filename of the article can not be null or empty!");

        if(analyzedArticles==null)
        {
            analyzedArticles = new Vector<AnalyzedArticle>();
            throw new Exception("1109090457::fail to get saved data.");
        }

        int size = this.analyzedArticles.size();
        if(size<=0)
        {
            analyzedArticles.add(article);
            return;
        }

        for(int i=size-1; i>=0; i--)
        {
            AnalyzedArticle analyzedArticle = analyzedArticles.get(i);

            if(analyzedArticle==null)
                continue;

            if((analyzedArticle.filename==null)||(analyzedArticle.filename.trim().length()<1))
                throw new Exception("1109090202::one instance of " +
                        "\"AnalyzedArticle\" in \"Vector<AnalyzedArticle> " +
                        "analyzedArticles\" has a null filename!");

            if(analyzedArticle.filename.trim().compareTo(filename.trim())==0){
                this.analyzedArticles.setElementAt(article, i);
                return;
            }
        }

        analyzedArticles.add(article);
        //throw new Exception("1109090245::given filename of the article can not be null or empty!");

    }

}

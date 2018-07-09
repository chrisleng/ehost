/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package verifier;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.util.ArrayList;

/**
 *
 * @author Kyle
 */
public class Output
{
    private ArrayList<Article> mArticles;
    private ArrayList<Annotation> mAnnotations;
    public Output(ArrayList<Article> arts, ArrayList<Annotation> annots)
    {
        mArticles = arts;
        mAnnotations = annots;
    }
    public Output()
    {
        mArticles = new ArrayList<Article>();
        mAnnotations = new ArrayList<Annotation>();
    }

    /**
     * @return the mArticles
     */
    public ArrayList<Article> getArticles() {
        return mArticles;
    }

    /**
     * @return the mAnnotations
     */
    public ArrayList<Annotation> getAnnotations() {
        return mAnnotations;
    }

    /**
     * @param mArticles the mArticles to set
     */
    public void setArticles(ArrayList<Article> mArticles) {
        this.mArticles = mArticles;
    }

    /**
     * @param mAnnotations the mAnnotations to set
     */
    public void setAnnotations(ArrayList<Annotation> mAnnotations) {
        this.mAnnotations = mAnnotations;
    }

    public void addAnnotations(ArrayList<Annotation> annots)
    {
        this.mAnnotations.addAll(annots);
    }

    public void addArticle(Article art)
    {
        mArticles.add(art);
    }
}

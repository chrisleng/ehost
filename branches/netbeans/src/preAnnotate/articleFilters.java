/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.util.ArrayList;
import java.util.Vector;
import preAnnotate.format.Annotator;

/**
 *
 * @author Kyle
 */
public class articleFilters
{
    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This function will take the Articles from toFilter, and filter out any that don't have
     * a matching name to the entries within fileNames. It will also combine Annotations
     * from articles with matching names.  Then it will look at all of the annotations
     * in the remaining articles and will filter out any that either don't match one of the passed
     * in annotators or don't match one of the passed in annotators.
     *
     * @param toFilter - The Articles to clean up.
     * @param fileNames - The filenames to use to filter articles with.
     * @param annotators - The annotators to filter annotations with.
     * @param classes - The classes to filter annotations with.
     * @return - A cleaned up ArrayList of Articles that only contains Articles with passed in filenames,
     * and annotations with matching classes and annotators.
     */
    public static ArrayList<Article> filter(ArrayList<Article> toFilter, ArrayList<String> fileNames, ArrayList<Annotator> annotators, ArrayList<String> classes)
    {
        ArrayList<Article> temp = new ArrayList<Article>();
        temp = combineSameArticles(toFilter);
        ArrayList<Article> toReturn = new ArrayList<Article>();
        // check to make sure the annotation is valid(has all the information)
        for (Article article : temp)
        {
            //If it is not from a chosen file just continue.
            if (fileNames != null && !fileNames.contains(article.filename))
            {
                continue;
            }
            Article toAdd = new Article(article.filename);
            for (Annotation anAnnotation : article.annotations)
            {
                //Filter by class
                if (classes != null && !classes.contains(anAnnotation.annotationclass))
                {
                    continue;
                }
                //Filter by annotator
                if (annotators != null && !annotators.contains(new Annotator(anAnnotation.getAnnotator(), anAnnotation.annotatorid, null)))
                {
                    continue;
                }
                toAdd.addAnnotation(anAnnotation);

            }
            toReturn.add(toAdd);
        }
        return toReturn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * This private method will combine Articles that have the same file names.
     *
     * @param passed - The list of articles to combine duplicates.
     *
     * @return - The same list of articles with the duplicates combined.
     */
    private static ArrayList<Article> combineSameArticles(ArrayList<Article> passed)
    {
        ArrayList<Article> toReturn = new ArrayList<Article>();
        ArrayList<Article> toCombine = (ArrayList<Article>)passed.clone();
        for (int i = 0; i < toCombine.size(); i++)
        {
            Vector<Annotation> forThisName = new Vector<Annotation>();
            forThisName.addAll(toCombine.get(i).annotations);
            for (int j = i + 1; j < toCombine.size(); j++)
            {
                if (toCombine.get(i).filename.equals(toCombine.get(j).filename))
                {
                    forThisName.addAll(toCombine.get(j).annotations);
                    toCombine.remove(j);
                    j--;
                }
            }
            Article toAdd = new Article(toCombine.get(i).filename);
            toAdd.annotations = forThisName;
            toReturn.add(toAdd);
        }
        return toReturn;
    }
}
//</editor-fold>

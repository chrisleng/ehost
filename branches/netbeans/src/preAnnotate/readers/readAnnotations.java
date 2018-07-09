/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate.readers;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *  This class will extract annotations from a .annotations file format.
 * @author Kyle
 */
public class readAnnotations
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private String fileName;
    private ArrayList<Article> articles;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     * @param fileName - the name of the .annotations file to extract annotations
     * from.
     */
    public readAnnotations(String fileName)
    {
        this.fileName = fileName;
        articles = new ArrayList<Article>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Read in annotations from a .annotations file and return them in Article
     * form.
     * @return - The annotations organized by article(file of origin)
     */
    public ArrayList<Article> extractAnnotations()
    {
        try
        {
            Scanner s = new Scanner(new File(fileName));
            while (s.hasNext())
            {
                String entry = s.nextLine();
                String[] atts = entry.split("\t");
                addAnnotation(atts[0], atts[1], atts[2], atts[3], atts[4]);
            }
        }
        catch (IOException e)
        {
            System.out.println("Exception");
        }
        return articles;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Add an Annotation to the correct Article, or create a new Article if one does not already
     * exist.
     * @param text - the text of the annotation
     * @param textClass - the class of the annotation
     * @param source - the file the annotation originated from
     * @param span - the character span of the annotation
     * @param date - the creation date of the annotation
     */
    private void addAnnotation(String text, String textClass, String source, String span, String date)
    {
        Article toAdd = null;
        boolean newOne = true;
        for (Article article : articles)
        {
            if (article.filename.equals(source))
            {
                newOne = false;
                toAdd = article;
            }
        }
        if (newOne)
        {
            toAdd = new Article(source);
            articles.add(toAdd);
        }
        String[] startnEnd = span.split("\\|");
        int start = 0;
        int end = 0;
        try
        {
            start = Integer.parseInt(startnEnd[0]);
            end = Integer.parseInt(startnEnd[1]);
        }
        catch (NumberFormatException e)
        {
            return;
        }
        Annotation annotation = new Annotation(null, text, start, end, null, null, textClass, date);
        toAdd.addAnnotation(annotation);
    }
    //</editor-fold>
}

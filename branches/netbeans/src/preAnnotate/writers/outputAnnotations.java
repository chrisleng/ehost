/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotate.writers;

import resultEditor.annotations.Article;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import resultEditor.annotations.Annotation;
import preAnnotate.format.Annotator;
import resultEditor.annotations.SpanDef;

/**
 *
 * @author Kyle
 */
public class outputAnnotations
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private ArrayList<Article> allEntries;
    private String theFile;
    private boolean overwrite;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     * @param articles - the Articles to output to a .annotations file
     * @param fileName - the .annotations filename
     * @param overWrite - true to overwrite the file if it exists, false to append to it.
     */
    public outputAnnotations(ArrayList<Article> articles, String fileName, boolean overWrite)
    {
        theFile = fileName;
        allEntries = articles;
        overwrite = overWrite;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Write the files to a .annotations file.
     * @param chosenFiles - the files that were chosen to output
     * @param chosenAnnotators - the annotators that were chosen to output.
     * @param chosenClasses - the classes that were chosen to output.
     * @return - A brief summary of the process
     */
    public String write(ArrayList<String> chosenFiles, ArrayList<Annotator> chosenAnnotators, ArrayList<String> chosenClasses)
    {
        try
        {
            int count = 0;
            //Make file writer
            BufferedWriter out = new BufferedWriter(new FileWriter(theFile + ".annotations", !overwrite));
            // check to make sure the annotation is valid(has all the information)
            for (Article article : allEntries)
            {
                //If it is not from a chosen file just continue.
                if(chosenFiles != null && !chosenFiles.contains(article.filename))
                    continue;
                for(Annotation anAnnotation: article.annotations)
                {
                    //Filter by class
                    if(chosenClasses != null && !chosenClasses.contains(anAnnotation.annotationclass))
                        continue;
                    //Filter by annotator
                    if(chosenAnnotators != null && !chosenAnnotators.contains(new Annotator(anAnnotation.getAnnotator(), anAnnotation.annotatorid, null)))
                        continue;

                    //Write the line
                    String toWrite = anAnnotation.annotationText + "\t" + anAnnotation.annotationclass +"\t"+
                            article.filename + "\t";
                    if(( anAnnotation.spanset == null )||(anAnnotation.spanset.isEmpty()) )
                        continue;
                    if( anAnnotation.spanset.size() < 1 )
                        continue;
                    
                    for( int t=0; t<anAnnotation.spanset.size(); t++){
                        SpanDef span = anAnnotation.spanset.getSpanAt(t);
                        if(span == null)
                            continue;
                        toWrite = toWrite + span.start+"|" + span.end + "\t"; 
                    }
                    
                    toWrite = toWrite + anAnnotation.creationDate + "\n";
                    out.write(toWrite);
                    count++;
                }
            }
            out.close();
            return count + " Annotations written to file succesfully!\n";
        }
        catch (IOException e)
        {
            return "Failed to write annotations file!\n";
        }
    }
    //</editor-fold>
}

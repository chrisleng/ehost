/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package verifier;

import imports.importedXML.eXMLFile;
import resultEditor.annotations.Article;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 *
 * @author Kyle
 */
public class MatchOldAndNew
{
    public static void MatchArticleWithDirectory(Article art, String dir)
    {
        if((dir==null)||(dir.trim().length()<1))
            return;
        
        //dir = dir.replaceAll("\\(", "\\\\(");
        //dir = dir.replaceAll("\\)", "\\\\)");
        File dirFile = new File(dir);
        log.LoggingToFile.log( Level.SEVERE, "--------------------dir=["+dir+"]");
        log.LoggingToFile.log( Level.SEVERE,"--------------------dir=["+dirFile+"]");


        ArrayList<File> files = new ArrayList<File>();
        File[] subfiles = dirFile.listFiles();
        if((subfiles!=null)&&(subfiles.length>0))
            files.addAll(Arrays.asList(subfiles));
        ArrayList<File> oldFiles = FileTools.matchFileWithFiles(new File(art.filename), files);
        art.baseArticle = readInAll(oldFiles);
    }

    private static Article readInAll(ArrayList<File> files)
    {
        //Depot depot = new Depot();
        //depot.clear();
        for(File f: files)
        {
            eXMLFile XMLfile = imports.ImportXML.readXMLContents( f );
            resultEditor.annotations.ImportAnnotation imports = new resultEditor.annotations.ImportAnnotation();
            imports.assignateAnnotationIndex(XMLfile);
            return imports.XMLExtractor( XMLfile , false);
        }

        return null;
        //return depot.getArticleByFilename((files.get(0).getName()).replace(".xml", "").replace(".knowtator", ""));
    }
}

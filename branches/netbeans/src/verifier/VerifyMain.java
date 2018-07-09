/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package verifier;

import imports.importedXML.eXMLFile;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 *
 * @author Kyle
 */
public class VerifyMain
{
    private static File mNewDir, mOldDir, mTxtDir;
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        if(parseArgs(args))
        {
            ArrayList<File> oldDirFiles = new ArrayList<File>();
            oldDirFiles.addAll(Arrays.asList(mOldDir.listFiles()));

            ArrayList<File> newDirFiles = new ArrayList<File>();
            newDirFiles.addAll(Arrays.asList(mNewDir.listFiles()));

            ArrayList<File> txtDirFiles = new ArrayList<File>();
            txtDirFiles.addAll(Arrays.asList(mTxtDir.listFiles()));
            ArrayList<Article> articles = new ArrayList<Article>();
            ArrayList<ArrayList<Annotation>> theAnnots = new ArrayList<ArrayList<Annotation>>();
            while(txtDirFiles.size() > 0)
            {
                Depot depot = new Depot();
                ArrayList<File> sameFiles = FileTools.matchFileWithFiles(txtDirFiles.get(0), newDirFiles);
                ArrayList<File> oldFiles = FileTools.matchFileWithFiles(txtDirFiles.get(0), oldDirFiles);
                ArrayList<File> matchingText = FileTools.matchFileWithFiles(txtDirFiles.get(0), txtDirFiles);
                Article old = readInAll(oldFiles);
                Article theNew = readInAll(sameFiles);
                BufferedWriter writer = null;
                try
                {
                    File file = new File("VerifierOutput.txt");
                    file.createNewFile();
                    writer = new BufferedWriter(new FileWriter(file));
                }
                catch(IOException e)
                {
                    System.out.println(e.getMessage());
                }
                VerifyChallenge2011 verify = new VerifyChallenge2011(theNew,old, matchingText.get(0), writer);
                ArrayList<Annotation> annots = new ArrayList<Annotation>();
                annots.addAll(verify.outputVerify());
                theAnnots.add(annots);
                txtDirFiles.removeAll(matchingText);
                depot.add(theNew);
                articles.add(theNew);
                //toReturn.addArticle(theNew);
                depot.setAnnotationsVisible(annots, theNew);
            }
            Depot depot = new Depot();
            depot.clear();
            for(int i = 0; i<articles.size(); i++)
            {
                
                depot.add(articles.get(i));
                depot.setAnnotationsVisible( theAnnots.get(i),articles.get(i));
            }
        }
//        for(File f: newFile.listFiles())
//        {
//            Depot depot = new Depot();
//            depot.clear();
//            //Read XML file into memory
//            eXMLFile XMLfile = Import.ImportXML.readXMLContents( f );
//            ResultEditor.Annotations.ImportAnnotation imports = new ResultEditor.Annotations.ImportAnnotation();
//            imports.assignateAnnotationIndex(XMLfile);
//            Article r = imports.XMLExtractor( XMLfile , true);
//            r = depot.getArticleByFilename((XMLfile.filename).replace(".xml", "").replace(".knowtator", ""));
//
//            ArrayList<File> matchingFile = FileTools.matchFileWithFiles(f, oldDirFiles);
//            depot.clear();
//            //Read XML file into memory
//            XMLfile = Import.ImportXML.readXMLContents( f );
//            imports = new ResultEditor.Annotations.ImportAnnotation();
//            imports.assignateAnnotationIndex(XMLfile);
//            Article r = imports.XMLExtractor( XMLfile , true);
//        }
    }
    private static Article readInAll(ArrayList<File> files)
    {
        Depot depot = new Depot();
        depot.clear();
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
    private static boolean parseArgs(String[] args)
    {
        if(args.length != 3)
        {
            System.out.println("Invalid number of arguments!");
            return false;
        }
        mNewDir = new File(args[0]);
        mOldDir = new File(args[1]);
        mTxtDir = new File(args[2]);

        if(!mNewDir.isDirectory())
        {
            log.LoggingToFile.log( Level.SEVERE,"Invalid directory path for 2011 .xml files");
            return false;
        }

        if(!mOldDir.isDirectory())
        {
            log.LoggingToFile.log( Level.SEVERE,"Invalid directory path for 2010 .xml files");
            return false;
        }

        if(!(mTxtDir.isDirectory()))
        {
            log.LoggingToFile.log( Level.SEVERE,"Invalid directory for matching clinical notes");
            return false;
        }

        return true;
    }
}

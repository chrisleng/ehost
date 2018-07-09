/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate.readers;

import imports.importedXML.eXMLFile;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import resultEditor.annotations.ImportAnnotation;
import java.io.File;
import java.util.ArrayList;
import resultEditor.annotations.Annotation;
import resultEditor.io.PinFile;
import java.util.TreeSet;
import preAnnotate.format.Annotator;
import preAnnotate.integratedDictionary;
import preAnnotate.writers.InternalToDictionary;

/**
 * This class is used to extract annotations, class, and annotators from various file formats.
 *
 * @author Kyle
 */
public class FileToAnnotations
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    //Member variables
    private integratedDictionary toUse;
    private ArrayList<String> chosenClasses = null;
    private ArrayList<Annotator> chosenAnnotators = null;
    private ArrayList<String> chosenFiles = null;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     * @param dict - the GUI that is using this annotations Extractor.
     */
    public FileToAnnotations(integratedDictionary dict)
    {
        toUse = dict;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * This method is used to set the chosen filter classes.
     * @param choices - the chosen classes.
     */
    public void setChosenClasses(ArrayList<String> choices)
    {
        chosenClasses = choices;
    }

    /**
     * This method is used to set the chosen annotators.
     * @param choices - the chosen annotators.
     */
    public void setChosenAnnotators(ArrayList<Annotator> choices)
    {
        chosenAnnotators = choices;
    }

    public void setChosenFiles(ArrayList<String> choices)
    {
        chosenFiles = choices;
    }

    /**
     * This method is called after annotators and classes have been chosen and will perform the
     * actual extraction and writing of the dictionary.
     * @param destination - the destination for the dictionary
     * @param overwrite - true if we want to append to the destination dictionary, false otherwise.
     * @param articles - all of the Articles for the extraction.
     */
    public void createDict(String destination, boolean overwrite, ArrayList<Article> articles)
    {
        //ArrayList<Article> allAnnotations = readAll(fileNames);
        InternalToDictionary writer = new InternalToDictionary(articles, chosenClasses, chosenAnnotators, destination);
        writer.setListener(toUse);
        writer.addToDictionary(overwrite);
        toUse.output("Dictionary created!\n");
    }

    /**
     * This method will extract the articles from the working set.
     *
     * @return - the Articles from the working set.
     */
    public ArrayList<Article> extractWorkingSet()
    {
        ArrayList<Article> articles = new ArrayList<Article>();

        //Extract all articles from working set.
        Depot depot = new Depot();
        ArrayList<Article> workingArticles = depot.getAllArticles();
        articles.addAll(workingArticles);

        //Return the articles
        return articles;
    }

    /**
     * This function is used to read in all of the annotations from all of the different
     * file types.
     * @param fileNames - the absolute paths for all of the files
     * @return
     */
    public ArrayList<Article> readAll(ArrayList<String> fileNames)
    {
        ArrayList<Article> articles = new ArrayList<Article>();

        //Loop through all inputted fileNames to try to match the extensions and extract info.
        for (int i = 0; i < fileNames.size(); i++)
        {
            //If this process has been cancelled then just return null.
            if (toUse.killInput())
            {
                //Set Progress Bar to 100 percent
                toUse.setProgressPercentage(100, "Cancelled");

                //Notify GUI that we recieved the message
                toUse.setKillInput(false);

                //Return null
                return null;

            }
            //Get the name of this file
            String name = fileNames.get(i);

            //Create a file from the fileName
            File toRead = new File(name);

            //Set progress percentage
            toUse.setProgressPercentage((i) * 100 / fileNames.size(), "Loading File: " + toRead.getName());

            //Make sure it is an actual file.
            if (toRead.isFile())
            {

                //Get the extension so we can check it.
                String extension = getExtension(toRead);

                //If it is a .pins file then use a pinsFile object to do the extraction.
                if (extension.toLowerCase().equals(".pins"))
                {
                    PinFile toAdd = new PinFile(toRead);
                    toAdd.startParsing(false);
                    articles.addAll(toAdd.articles);
                    
                    //OLD PINS EXTRACTION METHOD... SHOULD WORK FINE STILL.
                    /*
                    //Create a new pins file object and extract the annotations.
                    pinsFile toExtract = new pinsFile(toRead);
                    ArrayList<Article> newOnes = toExtract.extract(null, null, null);
                    if (newOnes != null)
                    {
                        articles.addAll(newOnes);
                    }
                    //Show summary of pins extraction.
                    toUse.output(toExtract.getExtractionSummary());
                     *
                     */
                }
                //If it is a .preannotate extension use a dictionaryToInternal object to extract the
                //annotations 
                else if (extension.toLowerCase().equals(".preannotate"))
                {
                    //Extract annotations and output to GUI
                    DictionaryToInternal reader = new DictionaryToInternal(toRead.getAbsolutePath());
                    articles.add(reader.ReadIn());
                    toUse.output("DICTIONARY extracted! " + toRead.getAbsolutePath() + "\n");
                }
                //If it is a .xml extension then create an eXMLFile and extract the annotations.
                else if (extension.toLowerCase().equals(".xml"))
                {
                    //Create a new eXML object and extract the annotations
                    eXMLFile aNewOne = imports.ImportXML.readXMLContents(toRead);
                    ImportAnnotation extractor = new ImportAnnotation();
                    articles.add(extractor.XMLExtractor(aNewOne, false));
                }
                //If it is a .annotations extension then extract the annotations from it
                else if (extension.toLowerCase().equals(".annotations"))
                {
                    readAnnotations extractor = new readAnnotations(toRead.getAbsolutePath());
                    articles.addAll(extractor.extractAnnotations());
                }
            }

        }
        //Return the annotations
        toUse.setProgressPercentage(100, "Files Loaded");
        return articles;
    }

    /**
     * This method will extract all of the annotators from the various file types.
     * @param fileNames - the absolute paths for the files.
     */
    public TreeSet<Annotator> getAllAnnotators(ArrayList<Article> articles)
    {
        //Used to hold unique annotators.
        TreeSet<Annotator> annotators = new TreeSet<Annotator>();

        //Loop through all articles and find all annotations
        for (Article article : articles)
        {
            //Extract the annotators from each annotation
            for (Annotation annotation : article.annotations)
            {
                //If the annotations does not have an annotatorID then just continue.
                if (annotation.annotatorid == null )//|| annotation.annotatorid.equals(""))
                {
                    annotation.annotatorid = "";
                    //continue;
                }

                //If the annotation does not have an annotator name than just continue.
                if (annotation.getAnnotator() == null || annotation.getAnnotator().equals(""))
                {
                    continue;
                }

                //Add the new annotator.
                Annotator newOne = new Annotator(annotation.getAnnotator(), annotation.annotatorid, null);
                annotators.add(newOne);
            }
        }
        //Return the annotations.
        return annotators;

    }

    /**
     * This class will extract all of the class information from the various filetypes.
     * @param fileNames
     */
    public TreeSet<String> getAllClasses(ArrayList<Article> articles)
    {
        //Used to hold unique classes.
        TreeSet<String> uniqueClasses = new TreeSet<String>();

        //Loop through all articles and find all annotations
        for (Article article : articles)
        {
            //For all annotations extract the class.
            for (Annotation annotation : article.annotations)
            {
                uniqueClasses.add(annotation.annotationclass);
            }
        }
        //Return the unique classes.
        return uniqueClasses;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    /**
     * This method will take a file object and extract the file extension from it.
     * @param file - file to extract extension from
     * @return - the file extension or an empty string if it has no file extension
     */
    public static String getExtension(File file)
    {
        String toReturn = file.getAbsolutePath();

        //If the path contains a . then get the text after the last one.
        if (toReturn.contains("."))
        {
            toReturn = toReturn.substring(toReturn.lastIndexOf("."));
        }
        return toReturn;
    }
    //</editor-fold>
}

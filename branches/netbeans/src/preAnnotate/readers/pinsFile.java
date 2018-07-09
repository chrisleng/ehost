/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package preAnnotate.readers;

import resultEditor.annotations.Article;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import preAnnotate.format.Annotation;
import preAnnotate.format.Annotator;

/**
 *This class represents a .pins file and contains the methods necessary to extract the
 * annotations contained it as well as extract annotations of a specific class or from
 * a specific annotator.
 * @author Kyle
 *
 */
public class pinsFile
{
    //<editor-fold defaultstate="collapsed" desc="Public Properties">
    public File file;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private ArrayList<Annotator> humanAnnotators = null;
    private ArrayList<Annotator> setAnnotators = null;
    private ArrayList<String> foundClasses = null;
    private ArrayList<String> sourceFileNames = null;
    private String errorMessages = "";
    private Map<String, String> wordsToClass;
    private int lineNum = 0;
    private InformationExtractor infoExtractor;
    private String extractionSummary;
    private ArrayList<imports.importedXML.eXMLFile> foundFiles = new ArrayList<imports.importedXML.eXMLFile>();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * The constructor for a pinsFile object
     *
     * @param source - The .pins file.
     */
    public pinsFile(File source)
    {
        infoExtractor = new InformationExtractor(this);
        file = source;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /** This method will extract all annotations from the chosenAnnotators and chosen classes.
     *
     * @param chosenAnnotators - The list of Annotators that annotations will be extracted from, or null for no filter.
     * @param chosenClasses - The list of classes that annotations will be extracted from, or null for no filter.
     * @return All Annotations in the pinsFile
     */
    public ArrayList<Article> extract(ArrayList<Annotator> chosenAnnotators, ArrayList<String> chosenClasses, ArrayList<String> chosenFiles)
    {
        this.extractionSummary = null;
        if (humanAnnotators == null)
        {
            processAnnotators();
        }
        System.out.println("Extracting" + this.file.getAbsolutePath());
        if (getWordsToClass() == null)
        {
            this.createMap();
        }
        return (createAnnotations(chosenAnnotators, chosenClasses, chosenFiles));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * This method will find all of the annotators in this pinsFile and add them to the lists
     * of setAnnotators and humanAnnotators.
     */
    private void processAnnotators()
    {
        //Initialize variables
        humanAnnotators = new ArrayList<Annotator>();
        setAnnotators = new ArrayList<Annotator>();

        //Create scanner through this file.
        Scanner s = createScanner(file);
        if (s == null)
        {
            return;
        }
        //Patterns for finding annotators
        Pattern human = Pattern.compile("knowtator\\+human\\+annotator", Pattern.CASE_INSENSITIVE);
        Pattern consensusLine = Pattern.compile("knowtator\\+consensus\\+set", Pattern.CASE_INSENSITIVE);

        //Search through every line to find the annotators
        while (s.hasNext())
        {
            setLineNum(getLineNum() + 1);
            String searchLine = s.nextLine();
            //Matchers for this line.

            Matcher findHumanLine = human.matcher(searchLine);
            Matcher findConsensusLine = consensusLine.matcher(searchLine);

            //If a human annotation is found eat up the information and attempt to create an Annotator object
            if (findHumanLine.find())
            {
                //Eat up the next line which should be empty.
                if (s.hasNext())
                {
                    setLineNum(getLineNum() + 1);
                    s.nextLine();
                }

                //The information from this annotator object will be stored in here
                ArrayList<String> objectInformation = new ArrayList<String>();
                objectInformation = eatLines(s);
                objectInformation.add(searchLine);

                //Create Annotator object
                Annotator anAnnotator = this.infoExtractor.annotatorExtractor(objectInformation);

                //If returned properly add it to the list if it is unique otherwise just add the inFileName to
                //an existing annotator object
                if (anAnnotator != null)
                {
                    //Assume it is unique
                    boolean unique = true;
                    //Loop through all known annotators to try to prove it wrong.
                    for (Annotator annotator : getHumanAnnotators())
                    {
                        //If the objects match then it's not unique.
                        if (annotator.equals(anAnnotator))
                        {
                            unique = false;
                            //just add the InFileName to the existing Annotator
                            annotator.addInFileName(anAnnotator.getAnnotatorFileNames().get(0));
                            break;
                        }
                    }
                    if (unique)
                    {
                        getHumanAnnotators().add(anAnnotator);
                    }
                }
            }
            //If a consensus line is found than eat up the information and create an Annotator object
            else if (findConsensusLine.find())
            {

                //Eat up the next line which should be empty.
                if (s.hasNext())
                {
                    setLineNum(getLineNum() + 1);
                    s.nextLine();
                }

                ArrayList<String> objectInformation = new ArrayList<String>();
                objectInformation = eatLines(s);
                objectInformation.add(searchLine);


                //Create Annotator object
                Annotator anAnnotator = this.infoExtractor.consensusExtractor(objectInformation);
                if (anAnnotator != null)
                {
                    getSetAnnotators().add(anAnnotator);
                }
            }

        }
        //Reset line number, so it will be correct when we do another pass.
        setLineNum(0);
    }

    /**
     * This method will find all of the classes in this pinsFile object and add them to
     * the foundClasses member variable.
     */
    private void processClasses()
    {
        foundClasses = new ArrayList<String>();

        //Create scanner through this file.
        Scanner s = createScanner(file);
        if (s == null)
        {
            return;
        }
        Pattern aClassLine = Pattern.compile("knowtator_mention_class");

        while (s.hasNext())
        {
            setLineNum(getLineNum() + 1);
            String searchLine = s.nextLine();
            //Matchers for this line.

            Matcher findClassLine = aClassLine.matcher(searchLine);

            //If a human annotation is found eat up the information and attempt to create an Annotator object
            if (findClassLine.find())
            {
                String[] temp = searchLine.split(" ");
                String possibleNewClass = temp[1].substring(0, temp[1].indexOf(")"));

                //Check for these two classes that are left over from an old version of knowtator
                if (possibleNewClass.equalsIgnoreCase("tests"))
                {
                    possibleNewClass = "Test";
                }
                else if (possibleNewClass.equalsIgnoreCase("treatments"))
                {
                    possibleNewClass = "Treatment";
                }
                boolean unique = true;
                //If it is unique then add it to the list.
                for (String aFoundClass : foundClasses)
                {
                    if (aFoundClass.equals(possibleNewClass))
                    {
                        unique = false;
                    }
                }
                if (unique)
                {
                    getFoundClasses().add(possibleNewClass);
                }
                /*
                if (!foundClasses.contains(possibleNewClass));
                {
                getFoundClasses().add(possibleNewClass);
                }
                 *
                 */
            }
        }
        Collections.sort(getFoundClasses());
        //Reset line number, so it will be correct when we do another pass.
        setLineNum(0);

    }

    /** This method will extract all of the instanceNumbers for annotations and the Class associated with that annotation
     * and put it into a map so that a key(instanceNum) will map to the Class it is associated with.
     *
     * @param s - The Scanner for a .pins file
     * @return - The Map containing the (instanceNum, Class) pairs
     */
    private void createMap()
    {
        Scanner s = createScanner(file);
        //The map that will store the (instanceNum, Class) pairs
        wordsToClass = new HashMap<String, String>();
        //Scan through the .pins file line by line.
        while (s.hasNext())
        {
            //Store next token
            String nextLine = s.nextLine();
            setLineNum(getLineNum() + 1);
            String[] tokens = nextLine.split(" ");

            //If it is not length of three then it is not something we care about.
            if (tokens.length != 4)
            {
                continue;
            }

            //If the token is the one we're looking for extract the Class
            // and the Instance # associated with it.(found in the next 4 tokens).
            if (tokens[3].equals("knowtator+class+mention"))
            {
                //Get next line which is irrelevant
                //should be blank line
                if (s.hasNext())
                {
                    setLineNum(getLineNum() + 1);
                    s.nextLine();
                }

                //Variable to store the lines that constitute this knowtator+class+mention object
                ArrayList<String> objectInfo = new ArrayList<String>();
                objectInfo.add(nextLine);
                //Loop until we hit an empty line.
                while (s.hasNext())
                {
                    String requiredLine = s.nextLine();
                    setLineNum(getLineNum() + 1);
                    if (!requiredLine.equals(""))
                    {
                        objectInfo.add(requiredLine);
                    }
                    //Break when we hit an empty line
                    else
                    {
                        break;
                    }
                }
                //Send the data to our informationExtractor
                ArrayList<String> anEntry = this.infoExtractor.dictionaryExtractor(objectInfo);
                //If returned properly use it.
                if (anEntry != null)
                {
                    getWordsToClass().put(anEntry.get(0), anEntry.get(1));
                }
            }
        }
        //Reset line number, so it will be correct when we do another pass.
        setLineNum(0);
    }

    /**
     * Add some things to our end Summary to make it easier to understand.
     * @param endSummary
     * @return
     */
    private String packResult(String endSummary)
    {
        String toReturn = "\n****Extraction Start****" + this.file.getAbsolutePath() + "\n"
                + endSummary + "****Extraction End****" + this.file.getAbsolutePath() + "\n";
        return toReturn;
    }

    /**
     * Check to see if this pinsFile contains any of the chosen annotators.
     *
     * @return true if this pinsFile contains a chosen Annotator, false otherwise.
     */
    private boolean checkAnnotators(ArrayList<Annotator> chosenAnnotators)
    {
        //Assume the chosen annotator is not in this file.
        boolean chosenInHere = false;
        //Search through all chosenAnnotators to try to prove it wrong.
        if (chosenAnnotators != null)
        {
            for (Annotator annotator : chosenAnnotators)
            {
                for (Annotator existing : getAllAnnotators())
                {
                    //If they have the same ID then they are the same annotator
                    if (existing.equals(annotator))
                    {
                        chosenInHere = true;
                        break;
                    }
                }
            }
        }
        //If chosen Annotators = null then they don't want to filter by annotator.
        else
        {
            chosenInHere = true;
        }
        return chosenInHere;
    }

    /**
     * This method will extract annotations from the pinsFile if they have a chosen annotator
     * and a chosen class.
     *
     * @param chosenAnnotators
     * @param chosenClasses
     * @return
     */
    private ArrayList<Article> extractFromPins(ArrayList<Annotator> chosenAnnotators, ArrayList<String> chosenClasses, ArrayList<String> chosenFiles)
    {
        ArrayList<Annotation> goodEntries = new ArrayList<Annotation>();
        ArrayList<Article> toReturn = new ArrayList<Article>();
        Scanner s = createScanner(file);
        String endSummary = "";
        //The number of bad annotations
        int countBad = 0;
        //The number of valid(using the expected format) annotations in the .pins file
        int countGood = 0;

        //Pattern matcher for an annotation
        Pattern annotation = Pattern.compile("knowtator\\+annotation");

        //Search every line for an annotation
        while (s.hasNext())
        {
            //Store the next token to see if it is an annotation
            setLineNum(getLineNum() + 1);
            String next = s.nextLine();

            //Looking for lines following this format(where the one can be any integer):
            //"([filename_Instance_1] of  knowtator+annotation"

            //Matcher for this line
            Matcher annotationFinder = annotation.matcher(next);

            //This will store the lines of an annotation if one is found.
            ArrayList<String> objectInfo = new ArrayList<String>();

            Article anArticle = null;
            //Assume each annotation is valid to begin with.
            boolean valid = true;

            //If an annotation is found eat up the lines and send them to the information extractor
            if (annotationFinder.find())
            {
                //Should be two empty lines
                if (s.hasNext())
                {
                    setLineNum(getLineNum() + 1);
                    s.nextLine();
                }
                if (s.hasNext())
                {
                    setLineNum(getLineNum() + 1);
                    s.nextLine();
                }

                //Eat up all lines until we hit an empty line
                objectInfo = eatLines(s);

                //Add starting line as well
                objectInfo.add(next);

                //Extract the annotation
                anArticle = this.infoExtractor.annotationsExtractor(objectInfo, chosenAnnotators, chosenClasses, chosenFiles);

                //If not returned properly then this is not a valid entry
                if (anArticle == null)
                {
                    valid = false;
                }

                // If an entry wasn't valid increment countBad
                if (!valid)
                {
                    countBad++;
                }

                //If it returned properly then it is valid
                if (valid)
                {
                    boolean added = false;
                    for (Article article : toReturn)
                    {
                        if (article.filename.equals(anArticle.filename))
                        {
                            for (resultEditor.annotations.Annotation newOne : anArticle.annotations)
                            {
                                article.addAnnotation(newOne);
                                added = true;
                            }
                        }
                    }
                    if (!added)
                    {
                        toReturn.add(anArticle);
                    }
                    //toReturn.addAnnotation(anAnnotation);
                    //Increment countGood if the lines were valid.
                    countGood++;
                    /*
                    //Attempt to append to a file
                    try
                    {

                    //APPEND TO THE FILE
                    BufferedWriter out = new BufferedWriter(new FileWriter(destination + ".annotations", true));
                    // check to make sure the annotation is valid(has all the information)
                    out.write(anAnnotation);
                    out.close();
                    }
                    catch (IOException e)
                    {
                    return (endSummary + "[ERROR]Unable to append to : " + destination + ": " + e.getMessage());
                    }
                     *
                     */
                }
            }
        }
        //writeToAnnotations(goodEntries);
        //Output the number of .pins annotations that were not in the expected format.
        endSummary += ("Number of annotations added: " + countGood + "\n");
        endSummary += ("Number of filtered annotations: " + countBad + "\n");
        endSummary += getErrorMessages();
        extractionSummary = packResult(endSummary);
        //Reset line number, so it will be correct when we do another pass.
        setLineNum(0);
        return toReturn;
    }
    /*
    private void writeToXML(ArrayList<Annotation> entries)
    {
    String summary = "";
    AnnotationsToXML aTest = new AnnotationsToXML(this.XMLDestination,entries);
    while(aTest.hasNext())
    summary += aTest.extractNext();
    }
     *
     */
    /*
    private boolean writeToAnnotations(ArrayList<Annotation> entries)
    {
    try
    {

    //APPEND TO THE FILE
    BufferedWriter out = new BufferedWriter(new FileWriter(destination + ".annotations", true));
    // check to make sure the annotation is valid(has all the information)
    for (Annotation anAnnotation : entries)
    {
    String toWrite = anAnnotation.spanText + "\t" + anAnnotation.theClass +"\t"+
    anAnnotation.source + "\t" +anAnnotation.span + "\t" + anAnnotation.creationDate + "\n";
    out.write(toWrite);
    }
    out.close();
    return true;
    }
    catch (IOException e)
    {
    return false;
    }
    }

     *
     */

    /** This method will write extract information from the Scanner s(which should contain information from a
     * .pins file) and append the annotations to the destination directory.
     *
     * @return - A string with the summary for this process
     */
    private ArrayList<Article> createAnnotations(ArrayList<Annotator> chosenAnnotators, ArrayList<String> chosenClasses, ArrayList<String> chosenFiles)
    {
        String endSummary = "";

        boolean chosenInHere = checkAnnotators(chosenAnnotators);
        if (!chosenInHere)
        {
            return null;
        }
        return (this.extractFromPins(chosenAnnotators, chosenClasses, chosenFiles));

    }
    /**
     * This method will add lines from a scanner to an ArrayList until it reaches an empty line.
     *
     * @param s - a scanner.
     * @return All lines from the starting point in the scanner to a blank line.
     */
    private ArrayList<String> eatLines(Scanner s)
    {
        ArrayList<String> objectInformation = new ArrayList<String>();
        while (s.hasNext())
        {
            setLineNum(getLineNum() + 1);
            String requiredLine = s.nextLine();
            if (!requiredLine.equals(""))
            {
                objectInformation.add(requiredLine);
            }
            //Break when we hit an empty line
            else
            {
                break;
            }
        }
        return objectInformation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">

    /**
     * @return the errorMessages
     */
    public String getErrorMessages()
    {
        return errorMessages;
    }

    /**
     * @param errorMessages the errorMessages to set
     */
    public void setErrorMessages(String errorMessages)
    {
        this.errorMessages = errorMessages;
    }

    /**
     * @return the lineNum
     */
    public int getLineNum()
    {
        return lineNum;
    }

    /**
     * @param lineNum the lineNum to set
     */
    public void setLineNum(int lineNum)
    {
        this.lineNum = lineNum;
    }

    /**
     * @return the wordsToClass
     */
    public Map<String, String> getWordsToClass()
    {
        if (wordsToClass == null)
        {
            this.createMap();
        }
        return wordsToClass;
    }

    /**
     * @return the humanAnnotators
     */
    public ArrayList<Annotator> getHumanAnnotators()
    {
        if (humanAnnotators == null)
        {
            processAnnotators();
        }
        return humanAnnotators;
    }

    /**
     * @return the setAnnotators
     */
    public ArrayList<Annotator> getSetAnnotators()
    {
        if (setAnnotators == null)
        {
            processAnnotators();
        }
        return setAnnotators;
    }

    /**
     * @return the foundClasses
     */
    public ArrayList<String> getFoundClasses()
    {
        if (foundClasses == null)
        {
            processClasses();
        }
        return foundClasses;
    }

    /**
     * This method will return all of the annotators from this pinsFile.
     *
     * @return ArrayList containing all annotators
     */
    public ArrayList<Annotator> getAllAnnotators()
    {
        ArrayList<Annotator> toReturn = new ArrayList<Annotator>();

        //If either is null process annotators
        if (getHumanAnnotators() == null || getSetAnnotators() == null)
        {
            this.processAnnotators();
        }

        //Add the human and set annotators
        toReturn.addAll(getHumanAnnotators());
        toReturn.addAll(getSetAnnotators());
        return toReturn;
    }

    /**
     * This method will process the classes if necessary and return them.
     * @return All of the classes in this pinsFile
     */
    public ArrayList<String> getAllClasses()
    {
        if (getFoundClasses() == null)
        {
            this.processClasses();
        }
        return getFoundClasses();
    }

    /**
     * @return the extractionSummary
     */
    public String getExtractionSummary()
    {
        if (extractionSummary == null)
        {
            return "Nothing extracted from: " + file.getAbsolutePath() + "(probably didn't contain one or more filters)\n";
        }
        return extractionSummary;
    }

    /**
     * This method will return all of the filenames from this .pins file.
     * @return All fileNames contained in this .pins file.
     */
    public ArrayList<String> getAllFileNames()
    {
        int count = 0;
        if (sourceFileNames == null)
        {
            sourceFileNames = new ArrayList<String>();
            Pattern fileName = Pattern.compile("file\\+text\\+source", Pattern.CASE_INSENSITIVE);
            Scanner s = this.createScanner(file);
            while (s.hasNext())
            {
                count++;
                String searchLine = s.nextLine();
                Matcher findFile = fileName.matcher(searchLine);
                if (findFile.find())
                {
                    count = 0;
                    sourceFileNames.add(this.infoExtractor.fileExtractor(searchLine));
                }
                if(count == 50 && sourceFileNames.size() >= 1)
                    break;
            }
        }
        return sourceFileNames;
    }

    /**
     * Create a string representation of this object.
     * @return - string representation.
     */
    public String toString()
    {
        return this.file.getAbsolutePath();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Functions">
    /**
     * This function will create a scanner for the given file.
     * @param file - the file to scan through.
     * @return - A scanner through the file if one could be created, null otherwise.
     */
    public static Scanner createScanner(File file)
    {
        //Attempt to create a scanner from the file
        Scanner s = null;
        try
        {
            s = new Scanner(file);
        }
        // If file cannot be found throw an exception and return null
        catch (FileNotFoundException e)
        {
            return null;
        }
        return s;
    }
    //</editor-fold>
}

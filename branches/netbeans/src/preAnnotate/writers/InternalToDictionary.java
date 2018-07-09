package preAnnotate.writers;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import preAnnotate.format.Annotator;
import preAnnotate.format.dictEntry;
import preAnnotate.integratedDictionary;

/** This class will add text of the form text <---> classOfText
 * to the destination file. Any text that has multiple classOfText
 * associated with it will not be added to the file. Instead, the
 * text will be added to a .conflicts file of the same name, so we
 * can avoid adding these words to the .preannotate file in the future.
 *
 * The .preannotate file will be in alphabetical order.
 *
 * @author Kyle
 *
 */
public class InternalToDictionary
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    //This represents the Allowable ClassOfText that is allowed to be in the second
    // column of the dictionary. To add classes, simply add the desired class to
    // this String[]. No other changes are necessary(besides the javaDoc).
    final String[] allowedClasses = new String[]
    {
        "Problem", "Treatment", "Test"
    };
    final boolean checkClass = false;
    //Member Variables
    private ArrayList<Article> info;
    private ArrayList<String> chosenClasses;
    private ArrayList<Annotator> chosenAnnotators;
    private String Destination;
    private String Conflicts;
    //This will hold All of the entries before they are actually written to the dictionary
    private ArrayList<String> wordsTabClass = new ArrayList<String>();
    private integratedDictionary listener = null;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /** This is a constructor for a Dictionary Extractor object.  When the constructor is called all of the
     * .annotations or .con files found in the string for the source pathName will be added to an ArrayList of
     * sourceFile objects so they can be used in future computations.
     *
     * @param source - The path name for the folder/file containing annotations.
     * @param destination - The path name for the file that the dictionary will be put in.
     * @param extension - The file extensions that will be accepted... must be .annotation, .con, or both.
     */
    public InternalToDictionary(ArrayList<Article> toRead, ArrayList<String> chosenClasses, ArrayList<Annotator> chosenAnnotators, String destination)
    {

        this.chosenClasses = chosenClasses;
        this.chosenAnnotators = chosenAnnotators;

        //Initialize variables.
        info = toRead;
        String destinationName = destination;
        String conflicts = "";
        String extensionType = "";

        //Check to see if the extension could have been included for the destination
        if (destinationName.length() >= 12)
        {
            extensionType = destinationName.substring(destination.length() - 12);
        }
        //If the extension was included then don't add it again, and create the conflicts file name without it
        if (extensionType.equals(".preannotate"))
        {
            conflicts = destinationName.substring(0, destination.length() - 12) + ".conflicts";
        }
        //If the extension wasn't included then add it.
        else
        {
            conflicts = destinationName + ".conflicts";
            destinationName = destinationName + ".preannotate";
        }
        //Initialize Member Variables
        Destination = destinationName;
        Conflicts = conflicts;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public void setListener(integratedDictionary listen)
    {
        listener = listen;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /** This function will take a scanner containing the information held in
     * a .annotations file type and add it to a file(or create a file
     * if one does not already exist) that contains a dictionary
     * with two columns text <---> ClassOfText.  Only allows The Classes "problem",
     * "treatment", and "test".
     */
    public String addToDictionary(boolean overWrite)
    {

        //Create the dictionary file if it does not exist
        try
        {
            //See if file already exists
            //If it does not exist, create it and inform user.
            if (new File(Destination).createNewFile())
            {
                log.LoggingToFile.log( Level.INFO, "Dictionary File Created");
            }
            // if it does exist inform user that it is being appended to.
            else
            {
                log.LoggingToFile.log( Level.INFO, "Dictionary File Already Exists");

            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "Problem creating dictionary file";
        }
        //Create the conflicts file if it does not exist
        try
        {
            //See if file already exists
            //If it does not exist, create it and inform user.
            if (new File(Conflicts).createNewFile())
            {
                log.LoggingToFile.log( Level.INFO, "Conflicts file created");
            }
            // if it does exist inform user that it is being appended to.
            else
            {
                log.LoggingToFile.log( Level.INFO, "Conflicts File Already Exists: Attempting to Append");
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "Problem creating conflicts file";
        }
        int startNumber = 0;
        //Count the number of entries in the dictionary to begin with.
        try
        {
            Scanner s = new Scanner(new File(Destination));
            while (s.hasNext())
            {
                s.nextLine();
                startNumber++;
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "Problem reading from dictionary.";
        }

        //The number of items in the .annotations file that were invalid
        int invalidClassAttempts = 0;

        //The percentages of the total read time that i'm assuming the articles will take.
        int articleProgress = 0;

        //The percentage of the total process time that i'm assuming the dictionary will take.
        int dictProgress = 0;

        //If we're overwriting the dictionary then it isn't going to take any time.
        if (overWrite)
        {
            articleProgress = 50;
            dictProgress = 0;
        }
        //Otherwise assume it's going to take equal time.
        else
        {
            articleProgress = 25;
            dictProgress = 25;
        }

        //Read in all of the articles and filter using chosen classes and annotators
        for (int i = 0; i < info.size(); i++)
        {
            //Extract the article
            Article article = info.get(i);

            //If we have an integratedDictoinaryGUI listening then update it on the progress
            //And check to make sure this process hasn't been cancelled.
            if (listener != null)
            {
                listener.setProgressPercentage((i * articleProgress) / info.size(), "Extracting: " + article.filename);
                if (listener.stopThread == true)
                {
                    listener.killThread();
                    return "Cancelled";
                }
            }
            //Loop through all annotations in this article
            for (Annotation annotation : article.annotations)
            {
                //Assume it's the wrong class
                boolean wrongClass = false;

                //See if it is a right class
                if (chosenClasses != null)
                {
                    if (!chosenClasses.contains(annotation.annotationclass.trim()))
                    {
                        wrongClass = true;
                    }
                }

                //Assume it's the wrong annotator
                boolean wrongAnnotator = false;

                //See if it is a right annotator
                if (chosenAnnotators != null)
                {
                    //Create a test annotator object from the annotation
                    Annotator test = new Annotator(annotation.getAnnotator(), annotation.annotatorid, null);

                    //See if chosenAnnotators contains the test annotator
                    if (!chosenAnnotators.contains(test))
                    {
                        wrongAnnotator = true;
                    }
                }
                //If it was both wrongClass and wrongAnnotator then notify the listener and continue
                if (wrongClass && wrongAnnotator)
                {
                    if (listener != null)
                    {
                        listener.addDoubleFiltered(new dictEntry(annotation.annotationText, annotation.annotationclass));
                    }
                    continue;
                }
                //If it was just the wrongClass notify the listener and continue
                if (wrongClass)
                {
                    if (listener != null)
                    {
                        listener.addFilteredClass(new dictEntry(annotation.annotationText, annotation.annotationclass));
                    }
                    continue;
                }
                //If it was just the wrongAnnotator notify the listener and continue
                if (wrongAnnotator)
                {
                    if (listener != null)
                    {
                        listener.addFilteredAnnotator(new dictEntry(annotation.annotationText, annotation.annotationclass));
                    }
                    continue;
                }
                //Make sure text and class variables are good and then add the annotation to wordsTabClass
                if (annotation.annotationText != null && !annotation.annotationText.equals("") && annotation.annotationclass != null && !annotation.annotationclass.equals(""))
                {
                    
                    wordsTabClass.add(annotation.annotationText.toLowerCase().trim() + "\t" + annotation.annotationclass.trim());

                }
            }

            //invalidClassAttempts += file.annotationsExtract();
            //wordsTabClass.add(line.getSpanText().toLowerCase().trim() + "\t" + line.getTheClass());
        }
        //Add existing dictionary entries to wordsTabClass
        ArrayList<String> original = new ArrayList<String>();

        //If we're not overwriting the original dictionary, then read it in.
        if (!overWrite)
        {
            //Notify the listener that we're extracting the old dictionary
            if (listener != null)
            {
                listener.setProgressPercentage(articleProgress, "Extracting old dictionary");
            }
            //Read in the dictionary entries
            original = dictExtract();

            //Add all of the dictionary entries
            wordsTabClass.addAll(original);
            //Check for cancelled thread and notify listener that we're moving to processing new dictionary
            if (listener != null)
            {
                if (listener.stopThread == true)
                {
                    listener.killThread();
                    return "Cancelled";
                }

                listener.setProgressPercentage(articleProgress + dictProgress, "Processing new dictionary");
            }
        }

        //Put wordsTabClass in alphabetical order so that text that is the same will be next to each other
        Collections.sort(wordsTabClass);

        //UPdate percentage and check for cancelled thread
        if (listener != null)
        {
            if (listener.stopThread == true)
            {
                listener.killThread();
                return "Cancelled";
            }
            listener.setProgressPercentage(55, "Processing new dictionary");
        }
        //Find and remove any conflict words.
        int newConflictsCount = 0;
        newConflictsCount = findConflicts(overWrite);

        //Check for cancelled thread and update percentage
        if (listener != null)
        {
            if (listener.stopThread == true)
            {
                listener.killThread();
                return "Cancelled";
            }
            listener.setProgressPercentage(70, "Processing new dictionary");
        }

        //count the number of entries added
        int count = 0;
        //Attempt to append to the dictionary file
        try
        {
            //Clear out the file and write all of the new words.
            BufferedWriter out = new BufferedWriter(new FileWriter(Destination, false));
            for (int i = 0; i < wordsTabClass.size(); i++)
            {
                //Get an entry from wordsTabClass
                String toCheck = wordsTabClass.get(i);

                String[] theSplit = toCheck.split("\t");

                //Get entry information and write it to a file.
                String theText = theSplit[0];
                String theClass = theSplit[1];
                //Notify listener of a new annotation being added
                if (!original.contains(toCheck) && listener != null)
                {
                    listener.addAnnotation(new dictEntry(theText, theClass));
                }

                //Write new dictionary entry to file.
                out.write(theText + " <---> " + theClass + "\n");
                count++;

                //Update progress percentage and check for cancelled thread
                if (listener != null)
                {
                    if (listener.stopThread == true)
                    {
                        listener.killThread();
                        return "Cancelled";
                    }
                    //set the total number of entries in the dictionary(so far).
                    listener.setTotalEntries(count);
                    //add a new class to listener(listener responsible for removing duplicates).
                    listener.addclasses(theClass);
                    //Update progress percentage
                    listener.setProgressPercentage(70 + (count * 30) / wordsTabClass.size(), "Writing dictionary...");

                }
            }
            //out.write(toWrite);
            out.close();
        }
        catch (IOException e)
        {
            //If unable to append print out an error.
            log.LoggingToFile.log( Level.SEVERE, "Unable to append to : " + Destination + ": " + e.getMessage());
        }

        //Return information about the process.
        return "Number of conflicting words added: " + newConflictsCount + "\n"
                + "Number of invalid entries extracted from file: " + invalidClassAttempts + "\n"
                + "Current dictionary size: " + count + "\n";
    }

    /** This function will see if there is text in wordsTabClass that matches words in conflictWords and if it does
     * then it will be removed. It also finds new conflicts within wordsTabClass and adds them to conflictWords
     * and the conflicts file.
     *
     * @param conflictWords - The text that has already had conflicting classes
     * @return The number of conflict words that were found.
     */
    public int findConflicts(boolean overWrite)
    {
        //To hold all of the conflict words
        ArrayList<String> conflictWords = new ArrayList<String>();

        //Create a scanner through conflictsFile
        File conflictsFile = new File(Conflicts);
        Scanner conflictsScanner = createScanner(conflictsFile);

        //Scan through all text in the conflicts file line by line.
        if (!overWrite)
        {
            while (conflictsScanner.hasNext())
            {
                String line = conflictsScanner.nextLine();
                //Add each line to the conflictWords ArrayList
                conflictWords.add(line);
            }
        }
        //Clear out conflicts file if we're overwriting this dictionary.
        else
        {

            //Attempt to append to the conflicts file file
            try
            {

                //Append to the File
                BufferedWriter out = new BufferedWriter(new FileWriter(Conflicts, false));
                out.write("");
                out.close();

            }
            catch (IOException e)
            {
                //If unable to append print out an error.
                log.LoggingToFile.log( Level.SEVERE, "Unable to append to : " + Conflicts + ": " + e.getMessage());
            }
        }
        int conflictCount = 0;

        //initialize looping variables
        String previousClass = "";
        String previousText = "";
        String thisLine = "";

        //Loop through all words in wordsTabClass to find any new conflicts(where text maps to multiple classes)
        //and delete duplicates.
        for (int entryIndex = 0; entryIndex < wordsTabClass.size(); entryIndex++)
        {
            //Get a line from wordsTabClass
            thisLine = wordsTabClass.get(entryIndex);

            //Split using the tab
            String[] thisLineSplit = thisLine.split("\t");

            String theText = "";
            String theClass = "";
            //Store text and class of the text
            if (thisLineSplit.length == 2)
            {
                theText = thisLineSplit[0];
                theClass = thisLineSplit[1];
            }
            else
            {
                log.LoggingToFile.log( Level.WARNING, "Uh oh! badLine " + thisLine);
                continue;
            }

            //If the previousText is not the same is this text just set the previous text and class
            //equal to this class and text and move on
            if (!previousText.equals(theText))
            {
                previousClass = theClass;
                previousText = theText;
                //Continue loop with new values
                continue;
            }
            //If this line has the same text as the last line then check
            // to make sure they have the same class.  If they do not, then add them to the conflicts
            // list and set previous(text, class) = this(class,text). If they do then remove the duplicate.
            else if (previousText.equals(theText))
            {
                //Check to make sure class is the same.
                if (theClass.equals(previousClass))
                {
                    //If class is the same remove this word(because it is a duplicate).
                    wordsTabClass.remove(entryIndex);

                    //Decrement counter so we don't miss the word at i+1
                    entryIndex--;
                    continue;
                }
                //If they do not have the same class then add theText to conflict words (ArrayList and File)
                else if (!conflictWords.contains(theText))
                {
                    conflictWords.add(theText);
                    
                    //Attempt to append to the conflicts file file
                    try
                    {
                        //Append to the File
                        BufferedWriter out = new BufferedWriter(new FileWriter(Conflicts, true));
                        out.write(theText + "\n");
                        out.close();
                        if (listener != null)
                        {
                            listener.addConflict(new dictEntry(theText, theClass));
                            if (listener.stopThread)
                            {
                                return 0;
                            }
                        }
                        conflictCount++;
                    }
                    catch (IOException e)
                    {
                        //If unable to append print out an error.
                        log.LoggingToFile.log( Level.SEVERE, "Unable to append to : " + Conflicts + ": " + e.getMessage());
                    }
                }
            }
        }
        if (listener != null)
        {
            listener.setDictConflicts(conflictWords.size());
        }
        //One more pass to remove new conflict words.
        for (int entryIndex = 0; entryIndex < wordsTabClass.size(); entryIndex++)
        {
            //Get a word and split it using the tab
            String toCheck = wordsTabClass.get(entryIndex);
            String[] theSplit = toCheck.split("\t");
            //Set theText equal to the first index of the split array.
            String theText = theSplit[0];
            //If it is a conflict word remove it.
            if (conflictWords.contains(theText))
            {
                wordsTabClass.remove(entryIndex);
                //Decrement index so we don't miss words
                entryIndex--;
            }
        }
        return conflictCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**  This function attempts to create a Scanner object
     * for a file object
     *
     * @param file - Creates a scanner from the given file
     * @return the Scanner for the file, null if unable to open file
     */
    private Scanner createScanner(File file)
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
            log.LoggingToFile.log( Level.SEVERE, "file not found: " + e.getMessage());
            return null;
        }
        return s;
    }
    /** This function will extract all of the Text <---> ClassOfText out of a dictionary file and put
     * them in wordsTabClass
     *
     * @param wordsTabClass - The ArrayList to be added to
     * @param dictionary - a dictionary file to get the information from
     */
    private ArrayList<String> dictExtract()
    {
        ArrayList<String> toReturn = new ArrayList<String>();
        File dictionary = new File(Destination);
        //Create a scanner through the dictionary and conflicts
        Scanner dictionaryScanner = createScanner(dictionary);
        //Keep track of what line we are on in the dictionary
        int lineNum = 0;
        //Loop through the dictionary line by line.
        while (dictionaryScanner.hasNext())
        {
            //Increment the line that we are on
            lineNum++;
            //Extract an entire line.
            String line = dictionaryScanner.nextLine();
            //Split the text into two sections (text, classOfText)
            String[] fields = line.split(" <---> ");
            //create strings to hold the text, classOfText
            String text = "";
            String classOfText = "";
            //Ensure that the line was of the correct format
            if (fields.length == 2)
            {
                //Store the values for the text and classOfText
                text = fields[0].toLowerCase();
                classOfText = fields[1];
                boolean valid = false;
                if (!checkClass)
                {
                    valid = true;
                }
                //Check to see if the classOfText meets our requirements
                for (String aClass : allowedClasses)
                {
                    if (checkClass)
                    {
                        if (classOfText.equals(aClass))
                        {
                            valid = true;
                        }
                    }
                }
                // The Class of text is not valid, so the entry is invalid and will
                // not be added to our dictionary.
                if (!valid)
                {
                    log.LoggingToFile.log( Level.SEVERE, "Your dictionary has an invalid classOfText at line: " + lineNum);
                }
                //Make sure the text is not already in the arrayList
                if (valid)
                {
                    toReturn.add(text + "\t" + classOfText);
                }
                //If the text is already in the arrayList inform the user that
                //the dictionary contains duplicates but add it anyways.
                //duplicates will be removed from the dictionary by the end.
                /*
                else
                {
                wordsTabClass.add(text + "\t" + classOfText);
                System.out.println("Your dictionary contains a duplicate entry at line: " + lineNum);
                }
                 *
                 */
            }
            //This will be printed out if the dictionary had a line of text that did not
            //divide into two parts when split using " <---> "
            else
            {
                log.LoggingToFile.log( Level.WARNING, "Invalid dictionary line encountered at line: " + lineNum);
                log.LoggingToFile.log( Level.WARNING, "BadLine: " + line);
            }
        }
        return toReturn;
    }
    //</editor-fold>
}

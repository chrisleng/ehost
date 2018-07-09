package preAnnotate.readers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;

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
public class DictionaryExtractor
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

    private boolean debugging = true;
    //Member Variables
    private String Source;
    private String Destination;
    private String Conflicts;
    private int CursorPosition;
    //This will hold All of the entries before they are actually written to the dictionary
    private ArrayList<String> wordsTabClass = new ArrayList<String>();
    //The list of .annotation or .con files in the Source File.
    private ArrayList<sourceFile> sourceFiles = new ArrayList<sourceFile>();
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
    public DictionaryExtractor(String source, String destination, String extension)
    {
        CursorPosition = 0;
        //Initialize variables.
        String sourceName = source;
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
        Source = sourceName;
        Destination = destinationName;
        Conflicts = conflicts;

        //Recursively find all .con/.annotations files
        File sourceFile = new File(Source);
        fileFinder(sourceFile, extension);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Recursively opens directories searching for files that have the extension found in extensions.
     * The choices for extensions are in the set {.con, .annotations, both}.
     *
     * @param fileObject - The file/directory to open in search of .con/.annotation files.
     * @param extensions - The file extensions we are looking for(".con", ".annotation", or "both").
     */
    private void fileFinder(File fileObject, String extensions)
    {
        //If the File is a Folder than open it up and recursively call
        // this function on everything inside it.
        if (fileObject.isDirectory())
        {
            File allFiles[] = fileObject.listFiles();
            for (File aFile : allFiles)
            {
                fileFinder(aFile, extensions);
            }
        }
        //If the file is a .con file then open it up and sort it.
        else if (fileObject.isFile())
        {

            //Get the absolute path name for this file
            String source = fileObject.getAbsolutePath();
            String conExtension = "";
            String annotationsExtension = "";

            //If the filename is long enough to have a .con extension then get the extension
            if (source.length() > 4)
            {
                conExtension = source.substring(source.length() - 4);
            }
            if (source.length() >= 12)
            {
                annotationsExtension = source.substring(source.length() - 12);
            }

            //Make sure the extension is .con
            if (conExtension.equals(".con") && (extensions.equals(".con") || extensions.equals("both")))
            {
                if (debugging)
                {
                    log.LoggingToFile.log( Level.SEVERE, ".CON file found: " + source);
                }
                //Add the file to the list
                sourceFiles.add(new sourceFile(fileObject, conExtension));
                return;
            }
            //If the file is an annotations file then extract using the annotations format.
            else if (annotationsExtension.equals(".annotations") && (extensions.equals(".annotations") || extensions.equals("both")))
            {
                if (debugging)
                {
                    log.LoggingToFile.log( Level.SEVERE,".ANNOTATIONS file found " + source);
                }
                //Add the file to the list
                sourceFiles.add(new sourceFile(fileObject, annotationsExtension));
                return;
            }
        }
    }

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
            System.err.println("file not found: " + e.getMessage());
            return null;
        }
        return s;
    }

    /** This function will take a scanner containing the information held in
     * a .annotations file type and add it to a file(or create a file
     * if one does not already exist) that contains a dictionary
     * with two columns text <---> ClassOfText.  Only allows The Classes "problem",
     * "treatment", and "test".
     */
    public String addToDictionary()
    {

        //Create the dictionary file if it does not exist
        try
        {
            //See if file already exists
            //If it does not exist, create it and inform user.
            if (new File(Destination).createNewFile())
            {
                System.out.println("Dictionary File Created");
            }
            // if it does exist inform user that it is being appended to.
            else
            {
                System.out.println("Dictionary File Already Exists: Attempting to Append");
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
                System.out.println("Conflicts file created");
            }
            // if it does exist inform user that it is being appended to.
            else
            {
                System.out.println("Conflicts File Already Exists: Attempting to Append");
            }
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            return "Problem creating conflicts file";
        }

        //The number of items in the .annotations file that were invalid
        int invalidClassAttempts = 0;

        //Add new dictionary entries to wordsTabClass
        for (sourceFile file : sourceFiles)
        {
            invalidClassAttempts += file.annotationsExtract();
        }
        //Add existing dictionary entries to wordsTabClass
        dictExtract();

        //Put wordsTabClass in alphabetical order so that text that is the same will be next to each other
        Collections.sort(wordsTabClass);

        //Find and remove any conflict words.
        int newConflictsCount = 0;
        newConflictsCount = findConflicts();

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
                out.write(theText + " <---> " + theClass + "\n");
                count++;
            }
            out.close();
        }
        catch (IOException e)
        {
            //If unable to append print out an error.
            System.err.println("Unable to append to : " + Destination + ": " + e.getMessage());
        }

        //Return information about the process.
        return "Number of conflicting words added: " + newConflictsCount + "\n"
                + "Number of invalid entries extracted from .annotations file: " + invalidClassAttempts + "\n"
                + "Current dictionary size: " + count + "\n";
    }
    /** This function will extract all of the Text <---> ClassOfText out of a dictionary file and put
     * them in wordsTabClass
     *
     * @param wordsTabClass - The ArrayList to be added to
     * @param dictionary - a dictionary file to get the information from
     */
    private void dictExtract()
    {
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
                    log.LoggingToFile.log( Level.WARNING, "Your dictionary has an invalid classOfText at line: " + lineNum);
                }
                //Make sure the text is not already in the arrayList
                if (valid)
                {
                    wordsTabClass.add(text + "\t" + classOfText);
                }
                //If the text is already in the arrayList inform the user that
                //the dictionary contains duplicates but add it anyways.
                //duplicates will be removed from the dictionary by the end.
                else
                {
                    wordsTabClass.add(text + "\t" + classOfText);
                    log.LoggingToFile.log( Level.INFO, "Your dictionary contains a duplicate entry at line: " + lineNum);
                }
            }
            //This will be printed out if the dictionary had a line of text that did not
            //divide into two parts when split using " <---> "
            else
            {
                log.LoggingToFile.log( Level.SEVERE, "Invalid dictionary line encountered at line: " + lineNum);
                log.LoggingToFile.log( Level.SEVERE, "BadLine: " + line);
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /** This function will see if there is text in wordsTabClass that matches words in conflictWords and if it does
     * then it will be removed. It also finds new conflicts within wordsTabClass and adds them to conflictWords
     * and the conflicts file.
     *
     * @param conflictWords - The text that has already had conflicting classes
     * @return The number of conflict words that were found.
     */
    public int findConflicts()
    {
        //To hold all of the conflict words
        ArrayList<String> conflictWords = new ArrayList<String>();

        //Create a scanner through conflictsFile
        File conflictsFile = new File(Conflicts);
        Scanner conflictsScanner = createScanner(conflictsFile);

        //Scan through all text in the conflicts file line by line.
        while (conflictsScanner.hasNext())
        {
            String line = conflictsScanner.nextLine();
            //Add each line to the conflictWords ArrayList
            conflictWords.add(line);
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

            //Store text and class of the text
            String theText = thisLineSplit[0];
            String theClass = thisLineSplit[1];

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
                        conflictCount++;
                    }
                    catch (IOException e)
                    {
                        //If unable to append print out an error.
                        log.LoggingToFile.log( Level.SEVERE,"Unable to append to : " + Conflicts + ": " + e.getMessage());
                    }
                }
            }
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
    
    //<editor-fold defaultstate="collapsed" desc="Private Internal Class - Source File">
    /**
     * A class that represents a .annotations or .con file that will contain
     * the information needed for dictionary entries.  This class will provide
     * the methods required to extract dictionary entries from a .con/.annotations
     * file.
     *
     * @author Kyle
     *
     */
    private class sourceFile
    {
        //Member Variables

        File Source;
        String Extension;

        /**
         * Constructor for a sourceFile object
         *
         * @param source - The File object for this sourceFile
         * @param extension - The File extension of this file
         */
        private sourceFile(File source, String extension)
        {
            Source = source;
            Extension = extension;
        }

        /**
         * This class will extract all of the annotations from this sourceFile object and add them to
         * wordsTabClass.
         *
         * @return The number of invalid annotations extracted.
         */
        private int annotationsExtract()
        {
            //The count of bad lines
            int badLines = 0;
            Scanner s = createScanner(this.Source);
            while (s.hasNext())
            {
                //Extract the next line
                String line = s.nextLine();
                //Create strings to hold the information
                String text = "";
                String classOfText = "";
                String[] fields = null;
                //split based on the tabs
                if (Extension.equals(".con"))
                {
                    fields = line.split("\"");
                }
                else if (Extension.equals(".annotations"))
                {
                    fields = line.split("\t");
                }
                else
                {
                    log.LoggingToFile.log( Level.SEVERE,"Invalid extension passed to function: annotationsExtract");
                    return 0;
                }
                //Ensure that the line has the correct format.
                if ((fields.length == 6 && Extension.equals(".con")) || (fields.length == 5 && Extension.equals(".annotations")))
                {
                    //Extract the information we want.
                    if (Extension.equals(".con"))
                    {
                        text = fields[1].toLowerCase();
                        classOfText = fields[3];
                    }
                    else
                    {
                        text = fields[0].toLowerCase();
                        classOfText = fields[1];
                    }
                    //Assume that the classOfText is invalid
                    boolean valid = false;

                    //Check to see if the classOfText meets our requirements
                    //change in .pins file format from Tests to Test and Treatments to Treatment.
                    //The dictionary only accepts entries with Class Test, Treatment, and Problem
                    //so the old format must be changed to be in line with these standards
                    if (classOfText.equalsIgnoreCase("tests"))
                    {
                        classOfText = "Test";
                    }
                    if (classOfText.equalsIgnoreCase("treatments"))
                    {
                        classOfText = "Treatment";
                    }
                    if (classOfText.equalsIgnoreCase("problems"))
                    {
                        classOfText = "Problem";
                    }
                    if (!checkClass)
                    {
                        valid = true;
                    }
                    //Make sure the classOfText is in our allowedClasses
                    for (String aClass : allowedClasses)
                    {
                        if (checkClass)
                        {
                            if (classOfText.equalsIgnoreCase(aClass))
                            {
                                valid = true;
                                classOfText = aClass;
                            }
                        }
                    }
                    //If not a valid class increment counter
                    if (!valid)
                    {
                        badLines++;
                        System.out.println(classOfText);
                    }
                    //If valid add to wordsTabClass
                    if (valid)
                    {
                        wordsTabClass.add(text + "\t" + classOfText);
                    }

                }
                //Not a valid line
                else
                {
                    badLines++;
                }

            }

            return badLines;
        }
    }
    //</editor-fold>
}

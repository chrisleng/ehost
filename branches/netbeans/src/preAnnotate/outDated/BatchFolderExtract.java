
package preAnnotate.outDated;

import preAnnotate.readers.pinsFile;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import preAnnotate.format.Annotation;
import preAnnotate.format.Annotator;

/** ********************THIS CLASS IS BROKEN*********************
 * ****MUST BE MODIFIED TO USE ARTICLES BEFORE USE*******
 *
 * This class will create a .annotations file from .pins files
 * the format for a .annotations is as follows:
 * Text	classOfText	sourceFile	characterSpan	annotationCreationDate
 * (with tab delimiters)
 *
 * @author Kyle Anderson
 *
 */
public class BatchFolderExtract
{
    //Member Variables
    private ArrayList<pinsFile> files = null;
    private ArrayList<String> chosenClasses = null;
    private ArrayList<Annotator> chosenAnnotators = null;
    private int cursorPosition;

    /** The constructor for a BatchFolderExtract object. The caller is responsible for making sure
     * that the source and destination paths are valid.
     *
     * @param source - The path for the folder containing .pins files.
     * @param destination - The path for the destination file that the annotations will be written to.
     */
    public BatchFolderExtract(String source)
    {
        cursorPosition = -1;

        files = new ArrayList<pinsFile>();
        recursiveFileFinder(new File(source));

    }

    /** This method Adds all of the .pins files contained in this fileObject to the ArrayList of files
     * for this object.
     *
     * @param fileObject - The root directory
     */
    private void recursiveFileFinder(File fileObject)
    {
        //If the File is a Folder than open it up and recursively call
        // this function on everything inside it.
        if (fileObject.isDirectory())
        {
            File allFiles[] = fileObject.listFiles();
            for (File aFile : allFiles)
            {
                recursiveFileFinder(aFile);
            }
        }
        //If the file is a .pins file then open it up and extract the
        //annotations from it.
        else if (fileObject.isFile())
        {
            //Get the absolute path name for this file
            String source = fileObject.getAbsolutePath();
            //String used to ensure that this file is a .pins file.
            String extension = "";
            //If the filename is long enough to have a .pins extension then get the extension
            if (source.length() > 5)
            {
                extension = source.substring(source.length() - 5);
            }
            //Make sure the extension is .pins
            if (extension.equals(".pins"))
            {
                this.files.add(new pinsFile(fileObject));

                //Notify user that .pins file has been located.
                System.out.println(".pins file found: " + source);

            }
        }
    }

    /**
     * This method will clear the destination file.
     *
     * @return - true if successful, false otherwise.
     */
    /*
    public boolean clearDestinationFile()
    {

        File destinationFile = new File(destination + ".annotations");
        boolean exists = destinationFile.isFile();

        //The user wants to overwrite the destination file so overwrite it.
        if (exists)
        {
            try
            {
                //Overwrite
                BufferedWriter out = new BufferedWriter(new FileWriter(destination + ".annotations", false));
                out.write("");
                out.close();
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
        //The user wants to create the destination file, so create it.
        else
        {
            try
            {
                destinationFile.createNewFile();
                return true;
            }
            catch (IOException e)
            {
                return false;
            }
        }
    }
    */
    /**
     * This method will extract the next .pins file.
     * @return - A summary of the process.
     */
    public ArrayList<Annotation> extractNext()
    {
        cursorPosition++;
        String summary = "";
        //return files.get(cursorPosition).extract(chosenAnnotators, chosenClasses, null);
        return null;
    }
    public String getLastExtractionSummary()
    {
        if(cursorPosition >= 0 && cursorPosition < files.size())
        {
            return files.get(cursorPosition).getExtractionSummary();
        }
        else
            return "";
    }

    /**
     * This method will fetch all of the annotators from all of the .pins files in the source directory.
     *
     * @return - All of the Annotators.
     */
    public ArrayList<Annotator> getAllAnnotators()
    {
        //Initialize
        ArrayList<Annotator> allAnnotators = new ArrayList<Annotator>();

        //Get and add all human and set annotators
        allAnnotators.addAll(getHumanAnnotators());
        allAnnotators.addAll(getSetAnnotators());

        return allAnnotators;
    }

    /**
     * This method will fetch all of the classes from all of the .pins files in the source directory.
     *
     * @return - The strings for all of the classes.
     */
    public ArrayList<String> getAllClasses()
    {
        ArrayList<String> allClasses = new ArrayList<String>();

        //Loop through all of the .pins files to get all of the classes.
        for (pinsFile file : files)
        {
            //Add all unique classes to foundClasses
            for (String aClass : file.getFoundClasses())
            {
                boolean unique = true;
                for (String existing : allClasses)
                {
                    if (existing.equals(aClass))
                    {
                        unique = false;
                    }
                }
                if (unique)
                {
                    allClasses.add(aClass);
                }
            }
        }

        return allClasses;
    }

    /**
     * This method will extract all human annotators from all of the .pins files in the source directory
     * and return an ArrayList of annotator objects.
     *
     * @return - ArrayList of all human annotators
     */
    public ArrayList<Annotator> getHumanAnnotators()
    {
        ArrayList<Annotator> allAnnotators = new ArrayList<Annotator>();

        //Loop through all files and extract the human annotators
        for (pinsFile file : files)
        {

            //Loop through all the human annotators from this .pins file.
            for (Annotator annotator : file.getHumanAnnotators())
            {
                //Assume that we have not already found someone with this ID
                boolean unique = true;

                //Loop through all of the annotators that we've already found to see if the new one is unique.
                for (Annotator existing : allAnnotators)
                {
                    //If the ID equals an existing ID then it is not unique so just add the name that would appear
                    //in that file to the existing annotator filenames and break.
                    if (existing.getAnnotatorID().equals(annotator.getAnnotatorID()))
                    {
                        existing.getAnnotatorFileNames().addAll(annotator.getAnnotatorFileNames());
                        unique = false;
                        break;
                    }
                }
                //If it is unique then just add it to the ArrayList that will be returned.
                if (unique)
                {
                    allAnnotators.add(annotator);
                }
            }
        }
        return allAnnotators;
    }

    /**
     * This method will extract all set annotators from all of the .pins files in the source directory
     * and return an ArrayList of annotator objects.
     *
     * @return - ArrayList of all set annotators
     */
    public ArrayList<Annotator> getSetAnnotators()
    {
        ArrayList<Annotator> allAnnotators = new ArrayList<Annotator>();

        //Loop through all of the files
        for (pinsFile file : files)
        {
            //Add all of the set annotators from each file.
            allAnnotators.addAll(file.getSetAnnotators());
        }
        return allAnnotators;
    }

    /**
     * This method will return some basic information about the process, it shouldn't be called until after
     * all annotations have been extracted.
     * @return - a String with information about the process.
     */
    public String getEndSummary()
    {
        /**
        String summary = "";
        //Count the number of annotations in the .annotations file
        File annotations = new File(destination + ".annotations");
        Scanner s = createScanner(annotations);
        int size = 0;
        if (s != null)
        {
            //Loop through all lines of the annotations file and count them.
            while (s.hasNext())
            {
                s.nextLine();
                size++;
            }
        }
         */
        String summary = "";
        //print out the total number of annotations in the .annotations file
        summary += "\n****************EXTRACTION SUMMARY***************\n";
        summary += "Total number of .pins files found: " + files.size() + "\n";
        //summary += "Total size of .annotations file: " + size + "\n";
        summary += "****************END EXTRACTION SUMARY****************\n";
        return summary;
    }

    /**
     * This method should be called before extractNext() to ensure that there is another
     * file to extract.
     *
     * @return - true this instance has another file.
     */
    public boolean hasNext()
    {
        if (cursorPosition + 1 < files.size())
        {
            return true;
        }
        return false;
    }

    /**
     * This calculates how close to completion this process is.
     * @return - a float representing the completion value(between 0 and 1).
     */
    public float getCompletePercentage()
    {
        return (float) (cursorPosition + 1) / (float) files.size();
    }

    /**
     * @return - int the total number of .pins files in this BatchFolderExtract object.
     */
    public int numberOfFiles()
    {
        return files.size();
    }

    /**
     * Set the annotators to filter by.
     *
     * @param chosenAnnotators
     */
    public void setChosenAnnotators(ArrayList<Annotator> chosenAnnotators)
    {
        this.chosenAnnotators = chosenAnnotators;
    }

    /** Set the classes to filter by.
     *
     * @param chosenClasses
     */
    public void setChosenClasses(ArrayList<String> chosenClasses)
    {
        this.chosenClasses = chosenClasses;
    }

    /**  This function attempts to create a Scanner object
     * for a file object
     *
     * @param file - Creates a scanner from the given file
     * @return the Scanner for the file, null if unable to open file
     */
    private static Scanner createScanner(File file)
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
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dictionaries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Kyle
 */
public class exportOrImportVerifier {
    /**
     * This function will import settings from a file written from the export function in this
     * class.
     * @param file - the file we're reading
     * @return - true if read successfully, false otherwise.
     */
    public static boolean importFile(File file)
    {
        File writeName = file;
        try
        {
            Scanner configFile = new Scanner(writeName);
            while(configFile.hasNext())
            {
                String line = configFile.nextLine();
                //Start of an entry
                if(line.startsWith("<entry"))
                {
                    String start = line.substring(line.indexOf("=")+1, line.lastIndexOf(">"));
                    //Read in entry details
                    VerifierDictionaryFormat newDict = new VerifierDictionaryFormat(start);
                    String name = new File(newDict.getFileName()).getName();
                    File extractedWords = extractWords(configFile, name);
                    File extractedExplanation = extractExplanation(configFile, name);
                    Scanner s = new Scanner(extractedWords);

                    ArrayList<String> someWords = new ArrayList<String>();
                    while(s.hasNext())
                    {
                        someWords.add(s.nextLine());
                    }
                    newDict.setWords(someWords);
                    if(extractedExplanation != null)
                        newDict.setExplanationFileNameAbsolute(extractedExplanation.getAbsolutePath());
                    if(extractedWords != null)
                        newDict.setFileName(extractedWords.getAbsolutePath());
                    env.Parameters.VERIFIER_DICTIONARIES.add(newDict);
                }
            }

            // save setting related to verifier dictionary for current project
            config.project.ProjectConf projectconf = new config.project.ProjectConf(
                    env.Parameters.WorkSpace.CurrentProject);
            projectconf.saveConfigure();

            return true;
        }
        catch(Exception e)
        {
            config.project.ProjectConf projectconf = new config.project.ProjectConf(
                    env.Parameters.WorkSpace.CurrentProject);
            projectconf.saveConfigure();
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * This function will write a file containing all of the information for the current Verifier Settings
     * from static memory.
     * @param file - The file we're writing to.
     * @return - true if it wrote successfully false otherwise.
     */
    public static boolean writeFile(File file)
    {
        String absoluteName = file.getAbsolutePath();
        String extension = "";
        int index = absoluteName.lastIndexOf(".");

        //Give it a vconfigs extension if it doesn't have it.
        if(index >= 0)
        {
            extension = absoluteName.substring(index+1);
        }
        if(!extension.toLowerCase().equals("vconfigs"))
        {
            absoluteName+= ".vconfigs";
        }
        File writeName = new File(absoluteName);
        
        try
        {
            //Create the file if it did not already exist
            writeName.createNewFile();

            //Create afile writer
            BufferedWriter out = new BufferedWriter(new FileWriter(writeName, false));

            //Write all of the Verifier Dictionaries
            for (VerifierDictionaryFormat dict : env.Parameters.VERIFIER_DICTIONARIES)
            {
                //Get some information to write this verifier dictionary
                String getFileName = dict.getFileName();
                String getExplanation = dict.getExplanationFileNameAbsolute();
                File dictWords = new File(getFileName);
                File dictExplanation = new File(getExplanation);

                //Set scanner to null originally
                Scanner scanFile = null;

                //if the file containing the entries exists then create a scanner from it.
                if(dictWords.exists())
                    scanFile = new Scanner(dictWords);

                //Set scaner to null originally
                Scanner explanation = null;

                //if the explanation file exists then create a scanner from it.
                if(dictExplanation.exists())
                    explanation = new Scanner(dictExplanation);


                out.write("<entry =" + dict.toString() + ">\n");

                //Write the file containing the dictionary words
                out.write("<fileText>\n");
                while(scanFile != null && scanFile.hasNext())
                {
                    out.write(scanFile.nextLine() + "\n");
                }
                out.write("</fileText>\n");

                //Write the file containing the explanation
                out.write("<fileExplanation>\n");
                while(explanation != null && explanation.hasNext())
                {
                    out.write(explanation.nextLine() +"\n");
                }
                out.write("</fileExplanation>\n");
                out.write("</entry =" + dict.toString() + ">\n");
            }
            out.close();
            return true;
        }
        //Catch any exceptions and return false.
        catch (Exception e)
        {
            return false;
        }
    }
    /**
     * This function will extract a dictionary file from a config file.
     * @param configFile - the scanner for the config file
     * @param name - the name of the file
     * @return - the file that was written
     */
    private static File extractWords(Scanner configFile,String name )
    {
        return readFromFile("<fileText>", "</fileText>", configFile, name, ".entries");
    }
    /**
     * Check to see if the text from a file matches the passed in text.
     * @param file - the file to see if the text matches
     * @param text - the text
     * @return - return true if the file and text are equal, false otherwise.
     */
    private static boolean checkEqualText(File file, String text)
    {

        //Read in the file
        String checkAgainst = "";
        try
        {
            Scanner s = new Scanner(file);
            while(s.hasNext())
            {
               checkAgainst += s.nextLine() + "\n";
            }
        }
        catch(Exception e)
        {

        }

        //Return the string comparison
        return(checkAgainst.equals(text));
    }
    /**
     * This function will extract the text from an explanation file, and write it
     * to it's own file.
     * @param configFile - the file to write from
     * @param name - the name of the file to write
     * @return - the file that was written
     */
    private static File extractExplanation(Scanner configFile, String name)
    {
        return readFromFile("<fileExplanation>", "</fileExplanation>", configFile, name, ".explained");
    }
    /**
     * Create a file from the config file using the text between the startTag and endTag
     * @param startTag - the tag indicating the start of the textblock
     * @param endTag - the tag indicating the end of the textblock
     * @param configFile - the scaner for the config file
     * @param name - the name of the file we're writing.
     * @return
     */
    private static File readFromFile(String startTag, String endTag, Scanner configFile, String name, String extension)
    {
        //Get the path separator based on OS.
        String separator = env.Parameters.isUnixOS ? "/":"\\";

        //To store the text from the config file
        String forResults = "";

        //Write the directories if they don't already exist.
        String fileName = "dictionaries" + separator + "verifierDictionaries";
        File toWrite = new File(fileName);
        toWrite.mkdirs();

        //Check to see if it has the right extension
        int index = name.lastIndexOf(".");

        boolean needsExtension = false;
        String existingExtension = "";
        //Give it an extension if it doesn't already have it.
        if(index >= 0)
        {
            existingExtension = name.substring(index);
        }
        if(!extension.toLowerCase().equals(existingExtension.toLowerCase()))
        {
            needsExtension = true;
        }
        //Make a file object for this Dictionary
        fileName += separator + name;
        if(needsExtension)
            fileName += extension;
        toWrite = new File(fileName);

        //Used to find when the scanner has hit the start of the text
        boolean foundStart = false;

        //Read in all lines that are part of the text
        while(configFile.hasNext())
        {
            //Read in next line
            String line = configFile.nextLine();

            //This is the start of the text
            if(line.startsWith(startTag))
            {
                foundStart = true;
                continue;
            }
            //If we've hit the end then break this loop
            else if(line.startsWith(endTag))
            {
                break;
            }
            //If we've found the start and it's not the end then read this in.
            if(foundStart)
            {
                forResults += line +"\n";
            }
        }
        if(forResults.equals(""))
            return null;
        //If the file already exists in our directory, then see if they are equal...
        //if not then change to a new filename
        if(toWrite.exists())
        {
            //The name we're using for this file
            String tempName= fileName;

            //Keep track of what attempt we're on so we can append a number to our attempt
            int attempt = 0;

            //Keep trying until we have found a unique filename
            while(toWrite.exists())
            {
                attempt ++;

                //Break if we already have an exact copy of this file
                if(checkEqualText(toWrite, forResults))
                {
                    break;
                }

                //The files are not equal so move to the next attempt
                else
                {
                    //Get the index of the last .
                    int dotindex = fileName.lastIndexOf(".");

                    // if the . exists then append the (#)
                    //before the prefix.
                    if(dotindex >= 0)
                    {
                        String beforeExt = fileName.substring(0, dotindex);
                        String afterExt = fileName.substring(dotindex+1);
                        tempName = beforeExt + "(" + attempt + ")" + afterExt;
                    }
                    //if there is now . then just append the (#) after the name.
                    else
                    {
                        tempName = fileName + "(" + attempt + ")";
                    }
                    //Create a new file for the next loop
                    toWrite = new File(tempName);
                }
            }
        }
        //Try to write the new file
        try
        {
            toWrite.createNewFile();
            //Create afile writer
            BufferedWriter out = new BufferedWriter(new FileWriter(toWrite, false));
            out.write(forResults);
            out.close();
            return toWrite;
        }
        //Catch any exceptions
        catch(Exception e)
        {
            return null;
        }
    }

}

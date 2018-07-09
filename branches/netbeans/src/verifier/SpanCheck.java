package verifier;

/**
 * This class is used to match words within dictionaries to annotations from clinical notes.
 * Depending on the settings of each individual dictionary, this class will check around, before and
 * inside every annotation span to find matches.
 *
 * @author Timothy Ryan Beavers
 * date - March 15th 2010
 *
 */
import resultEditor.annotations.Depot;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.suggestion;
import resultEditor.annotations.Article;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpanCheck
{
    //The name of the clinical note that is being checked.

    private String input;
    private BufferedWriter writer;
    //All of the dictionaries that will be used with this run of SpanCheck.
    private ArrayList<dictionaries.VerifierDictionaryFormat> configs;

    /**
     * The constructor for a SpanCheck object.
     * @param input - the name of the input file.
     * @param config - all of the verifier dictionaries that will be used.  ****These must
     * already be processed before being passed in.****
     */
    public SpanCheck(String input, ArrayList<dictionaries.VerifierDictionaryFormat> config, BufferedWriter forOutput)
    {
        this.input = input;
        configs = config;
        writer = forOutput;
    }

    /**
     * This method will first extract all annotations that match the inputted fileName from
     * the data pool of annotations.  For each of these annotations this method will attempt
     * to find matches with each dictionary, using the user settings for each of the dictionaries.
     * This method will populate all of the annotations with suggestion objects.
     *
     * @param onlyCheckThisOne - If you only want to check a single annotation for this file then pass
     * in the arrayIndex of the annotation you wish to check otherwise pass in -1.
     */
    public void findProblems(int onlyCheckThisOne)
    {

        Vector<Annotation> theObjects = new Vector<Annotation>();
        int objectSize = 0;
        //Make sure the input file is a file.
        if (new File(input).isFile())
        {
            //Extract the annotation information for this file.
            Depot check = new Depot();
            if (check==null)
                log.LoggingToFile.log( Level.SEVERE, "1106141324");

            Article anArticle = check.getArticleByFilename(new File(input).getName());
            

            //If there isn't an article then just return
            if (anArticle == null)
            {
                return;
            }

            //If the article doesn't contain any annotations just return.
            if (anArticle.annotations == null)
            {
                return;
            }

            //Add all annotations that match this file.
            theObjects.addAll(anArticle.annotations);

            //If we only want to check a single annotation... clear our all of the others.
            if (onlyCheckThisOne > 0)
            {
                Annotation toAdd = theObjects.get(onlyCheckThisOne);
                theObjects.clear();
                theObjects.add(toAdd);
            }

            objectSize = theObjects.size();
        }
        //If the input file isn't a file just return.
        else
        {
            return;
        }
        //For each annotation check for matches with all current dictionaries.
        for (int index = 0; index < objectSize; index++)
        {
            theObjects.get(index).verifierFound.clear();
            //Extract the start and end of the original annotation.
            String startSpan = String.valueOf(theObjects.get(index).spanstart);
            String endSpan = String.valueOf(theObjects.get(index).spanend);
            int endOfBefore = Integer.parseInt(startSpan); // first number in the char span
            int startOfAfter = Integer.parseInt(endSpan); // second number in the char span

            //Extract the text from the clinical notes and put it into a String.
            String fileInString = "";
            try
            {
                fileInString = FileString.readFile(input); // turn the new file into a string
            }
            catch (IOException e)
            {
                //e.printStackTrace();
                log.LoggingToFile.log( Level.SEVERE, "File to read in Character span from was not found.");
            }

            Boolean cont = true; // loop condition
            String newLine = "";
            int start = endOfBefore;  // used to modify from the character index to get the whole line out
            int end = startOfAfter;
            boolean badSpans = false;

            while (cont)
            {
                if (start < 0 || end < 0 || end >= fileInString.length() || start >= fileInString.length())
                {
                    log.LoggingToFile.log( Level.SEVERE,"Bad start or end in verifier.");
                    log.LoggingToFile.log( Level.SEVERE,theObjects.get(index) + " Start: " + start + " End: " + end);
                    log.LoggingToFile.log( Level.SEVERE,"MAX: " + fileInString.length());
                    badSpans = true;
                    break;
                }
                newLine = fileInString.substring(start, end);
                if (!(newLine.startsWith("\n")))
                {
                    start--;	// if your not at the start of the line go back
                }
                if (!(newLine.endsWith("\n")))
                {
                    end++; //if your not at the end of the same line go forward
                }
                if (newLine.startsWith("\n") && newLine.endsWith("\n"))
                {
                    cont = false;  // break the loop if your there
                }

            }
            if (badSpans)
            {
                continue;
            }

            String beforeCharSpan = "";
            String afterCharSpan = "";

            Boolean wordsBefore = false; // see if there are words before * assume there isn't*
            Boolean wordsAfter = false; // see if there are words after *assume there isn't*

            if ((endOfBefore - 1) - (start + 1) > 0) // if there is a difference greater than 0 from the start of the character span to the first of the line
            {
                beforeCharSpan = fileInString.substring(start + 1, endOfBefore - 1); //create the before substring
                wordsBefore = true; // set the bool to say there are words before
            }

            if ((end - 1) - (startOfAfter) > 0) // if there is a difference great than 0 from the end of the char span to the end of the line
            {
                afterCharSpan = fileInString.substring(startOfAfter, end - 1); //create the after substring
                wordsAfter = true; // set the bool to say there are words after
            }

            Boolean shouldPrintBefore = false; // * assume your not going to print  the new string at all *

            if (wordsBefore)  // if you have words before your char span
            {
                String[] before = new String[0];// create the array to hold the words
                before = beforeCharSpan.split(" ");  // load the words in

                //Check for matches with each dictionary.
                for (int dictionaryIndex = 0; dictionaryIndex < configs.size(); dictionaryIndex++)
                {
                    //Only do the check if the dictionary is set to check around the span.
                    if (configs.get(dictionaryIndex).isCheckAround())
                    {
                        int maxWords = Math.min(configs.get(dictionaryIndex).getWordsBefore(), before.length);
                        String[] words = new String[maxWords];
                        for (int word = before.length - 1; word >= before.length - maxWords; word--)
                        {
                            words[word - (before.length - maxWords)] = before[word];
                        }

                        int wordNum = QualityCheck.checkPhrase(words, configs.get(dictionaryIndex).getWords(), 0);
                        if (wordNum >= 0)
                        {
                            shouldPrintBefore = true;
                            int startDifference = 0;

                            //Add up the characters before, so we can suggest a new start for the span.
                            for (int j = wordNum; j < words.length; j++)
                            {
                                if (before.length - j >= 0)
                                {
                                    startDifference += words[j].length() + 1;
                                }
                            }

                            //Extract information for the creation of the suggestion object.
                            String suggestion = fileInString.substring(endOfBefore - startDifference, startOfAfter);
                            String explanationFileName = configs.get(dictionaryIndex).getExplanationFileNameAbsolute();
                            String explained = "";

                            //Read in explanation file.
                            explained = readDict(explanationFileName);

                            theObjects.get(index).verifierFound.add(new suggestion(suggestion, endOfBefore - startDifference, startOfAfter, explained));
                            try {
                            this.writer.write(explained.replaceAll("\n", "") + " "+ identify(theObjects.get(index)) + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(SpanCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }

                    }
                }
            }

            Boolean shouldPrintAfter = false; // * assume your not going to print  the new string at all *


            if (wordsAfter) // if you have any words after your char span
            {
                

                String[] after = new String[0]; // create the array to hold the words
                after = afterCharSpan.split(" "); // load the words in

                

                //Check each dictionary for matches
                for (int dictionaryIndex = 0; dictionaryIndex < configs.size(); dictionaryIndex++)
                {
                    //If the dictionary is checking around the span then do the check.
                    if (configs.get(dictionaryIndex).isCheckAround())
                    {
                        int maxWords = Math.min(configs.get(dictionaryIndex).getWordsAfter(), after.length);
                        String[] words = new String[maxWords];
                        for (int word = 0; word < maxWords; word++)
                        {
                            words[word] = after[word];
                        }

                        int wordNum = QualityCheck.checkPhrase(words, configs.get(dictionaryIndex).getWords(), 0);
                        if (wordNum >= 0)
                        {
                            shouldPrintAfter = true;
                            int startDifference = 0;

                            //****This assumes one space between words which may not necessarily be true****
                            //Count the characters up to the word that was flagged so we can suggest a new start.
                            for (int j = 0; j <= wordNum; j++)
                            {
                                startDifference += after[j].length() + 1;
                            }

                            //Extract information for creation of a suggestion object.
                            String suggestion = fileInString.substring(endOfBefore, startOfAfter + startDifference -1);
                            String explanationFileName = configs.get(dictionaryIndex).getExplanationFileNameAbsolute();
                            String explained = "";

                            //Read in the explanation file
                            explained = readDict(explanationFileName);

                            //alreadyExportedFiles.add(explanationFileName);
                            theObjects.get(index).verifierFound.add(new suggestion(suggestion, endOfBefore, startOfAfter + startDifference-1, explained));
                            try {
                            this.writer.write(explained + " "+ identify(theObjects.get(index)) + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(SpanCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            //theObjects.get(index).comments = theObjects.get(index).comments + explained;


                        }
                        
                        String chars = "";
                        
                        //Keep adding characters while wee need more.
                        for(int i = 0; i< configs.get(dictionaryIndex).getCharByCharAfter(); i++)
                        {
                            if(afterCharSpan.length() > i)
                                chars+= afterCharSpan.charAt(i);
                            if (chars.length() == configs.get(dictionaryIndex).getCharByCharAfter())
                                {
                                    
                                    break;
                                }

                        }
                        int add = QualityCheck.matchCharByChar(chars, configs.get(dictionaryIndex).getWords());
                        if (add >= 0)
                        {
                            //****This assumes one space between words which may not necessarily be true****
                            //Count the characters up to the word that was flagged so we can suggest a new start.
                            int startDifference = add + 1;

                            //Extract information for creation of a suggestion object.
                            String suggestion = fileInString.substring(endOfBefore, startOfAfter + startDifference);
                            String explanationFileName = configs.get(dictionaryIndex).getExplanationFileNameAbsolute();
                            String explained = "";

                            //Read in the explanation file
                            explained = readDict(explanationFileName);

                            theObjects.get(index).verifierFound.add(new suggestion(suggestion, endOfBefore, startOfAfter + startDifference, explained));
                            //theObjects.get(index).comments = theObjects.get(index).comments + explained;

                            try {
                            this.writer.write(explained + " "+ identify(theObjects.get(index)) + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(SpanCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }
                    }
                }


            }

            Boolean shouldPrintSpan = false; // assume by default your not going to print
           

            //Get the original span by doing a substring on the file.
            String originalSpan = fileInString.substring(theObjects.get(index).spanstart, theObjects.get(index).spanend);

            //Split the original span in to individual words.
            String[] spanned = new String[0];
            spanned = originalSpan.split(" ");

            //For each dictionary try to find a match.
            for (int dictionaryIndex = 0; dictionaryIndex < configs.size(); dictionaryIndex++)
            {
                //Only do the test if the dictionary is set to check inside of the span.
                if (configs.get(dictionaryIndex).isCheckInside())
                {
                    //Create a temporary array for this computation
                    String[] temp = new String[0];

                    //If the user only wants to check the first and last words then only add those words
                    //to the temp array
                    if (configs.get(dictionaryIndex).isLastOnly() && configs.get(dictionaryIndex).isFirstOnly())
                    {
                        temp = new String[2];
                        temp[0] = spanned[0];
                        temp[1] = spanned[spanned.length - 1];
                    }
                    //If the user only wants to check the last word then only add that word to the temp array.
                    else if (configs.get(dictionaryIndex).isLastOnly())
                    {
                        temp = new String[1];
                        temp[0] = spanned[spanned.length - 1];
                    }
                    //If the uesr only wants to check the first word then only add that word to the temp array.
                    else if (configs.get(dictionaryIndex).isFirstOnly())
                    {
                        temp = new String[1];
                        temp[0] = spanned[0];
                    }
                    //If the user hasn't selected lastOnly or firstOnly then just add everything.
                    else
                    {
                        temp = new String[spanned.length];
                        temp = spanned;
                    }

                    //If a match is found then add a new suggestion to the annotation that matched.
                    if (QualityCheck.checkPhrase(temp, configs.get(dictionaryIndex).getWords(), configs.get(dictionaryIndex).getAllowedInSpan()) >= 0)
                    {
                        //Extract information for use in the suggestion object.
                        String suggestion = fileInString.substring(endOfBefore, startOfAfter);
                        String explanationFileName = configs.get(dictionaryIndex).getExplanationFileNameAbsolute();
                        String explained = "";

                        //Read in the explanation file... might not want to do this over and over again.
                        //Just read in all dictionary explanations at the beginning of the call?
                        shouldPrintSpan = true;
                        explained = readDict(explanationFileName);

                        //Add the suggestion to the annotation.
                        theObjects.get(index).verifierFound.add(new suggestion(suggestion, endOfBefore, startOfAfter, explained));
                        try {
                            this.writer.write(explained + " "+ identify(theObjects.get(index)) + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(SpanCheck.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
    }

    private String identify(Annotation annot)
    {
        String toReturn = "";
        toReturn += (annot.spanstart + "|" + annot.spanend + " " +annot.annotationText);
        return toReturn;
    }
    private String readDict(String name)
    {
        File tryToExtract = new File(name);
        String explanationText = "";
        try
        {
            Scanner s = new Scanner(tryToExtract);
            while (s.hasNext())
            {
                explanationText += s.nextLine() + "\n";
            }
        }
        catch (IOException e)
        {
            //explanationText += name + "\n couldn't be opened!\n";
        }
        return explanationText;
    }
}





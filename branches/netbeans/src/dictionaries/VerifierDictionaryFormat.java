/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the information required for a verifier dictionary including
 * the options, source for the words, and explanation of the rules governing this dictionary.
 * @author Kyle
 */
public class VerifierDictionaryFormat
{

    private ArrayList<String> words = new ArrayList<String>();
    private String fileName;
    private boolean checkAround;
    private int wordsBefore;
    private int wordsAfter;
    private int charByCharAfter;
    private boolean checkInside;
    private String explanationFileNameAbsolute;
    private int numberOfEntries;
    private boolean firstOnly;
    private boolean lastOnly;
    private int allowedInSpan;

    /**
     * Parameter by parameter constructor
     * @param file - the absolute path of the file
     * @param checkAround - true if this dictionary should be used to check around the span
     * @param before - the number of words before the span to check
     * @param after - the number of words after the span to check
     * @param checkInside - true if this dictoinary should be used to check within a span
     * @param first - true if this dictionary should only check the first word within a span
     * @param last - true if this dictionary should only check the last word within a span
     * @param allowed - the number of allowed entries from this dictionary within a span
     * @param explanations - the path name for the explanation of the rules governing this dictionary.
     * @param numberOfEntries - the number of entries
     */
    public VerifierDictionaryFormat(String file, boolean checkAround, int before, int after,int charByCharAfter, boolean checkInside, boolean first, boolean last, int allowed, String explanations, int numberOfEntries)
    {
        this.numberOfEntries = numberOfEntries;
        fileName = file;
        this.checkAround = checkAround;
        wordsBefore = before;
        wordsAfter = after;
        this.charByCharAfter = charByCharAfter;
        this.checkInside = checkInside;
        firstOnly = first;
        lastOnly = last;
        allowedInSpan = allowed;
        explanationFileNameAbsolute = explanations;
        words = new ArrayList<String>();
        
    }
    /**
     * Constructor using a string from the configure file... This should be able to read in a string from
     * this objects toString method and create a VerifierDictionaryFormat object from it.
     * @param fromConfig - A line from the configure file(written using this objects toString() method).
     */
    public VerifierDictionaryFormat(String fromConfig)
    {
        String[] parameters = fromConfig.split("@@@@");
        if(parameters.length == 11)
        {
            fileName = parameters[0].toString();
            checkAround = Boolean.parseBoolean(parameters[1]);
            wordsBefore = Integer.parseInt(parameters[2]);
            wordsAfter = Integer.parseInt(parameters[3]);
            charByCharAfter = Integer.parseInt(parameters[4]);
            checkInside = Boolean.parseBoolean(parameters[5]);
            firstOnly = Boolean.parseBoolean(parameters[6]);
            lastOnly = Boolean.parseBoolean(parameters[7]);
            allowedInSpan = Integer.parseInt(parameters[8]);
            explanationFileNameAbsolute = parameters[9].toString();
            numberOfEntries = Integer.parseInt(parameters[10]);

        }
    }

    public String getExplanationText()
    {
        String toReturn = "";
        if(explanationFileNameAbsolute == null)
            return "";
        try {
            Scanner s = new Scanner(new File(explanationFileNameAbsolute));
            while(s.hasNext())
                toReturn+=s.nextLine();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerifierDictionaryFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toReturn;
    }
    /**
     * The toString method for this object, this should be called when you want to write
     * this object to a config file.
     * @return
     */
    @Override
    public String toString()
    {
        String temp = fileName + "@@@@"
                + checkAround + "@@@@"
                + wordsBefore + "@@@@"
                + wordsAfter + "@@@@"
                + getCharByCharAfter() + "@@@@"
                + checkInside + "@@@@"
                + isFirstOnly() + "@@@@"
                + isLastOnly() + "@@@@" 
                + getAllowedInSpan() + "@@@@"
                + explanationFileNameAbsolute + "@@@@"
                + numberOfEntries;
        return temp;
    }
    /**
     * 
     * @return
     */
    public boolean checkGood()
    {
        if(fileName != null)
            return true;
        return false;
    }
    public void addAnEntry(String entry)
    {
        getWords().add(entry);
    }

    /**
     * @return the words
     */
    public ArrayList<String> getWords()
    {
        return words;
    }

    /**
     * @return the wordsBefore
     */
    public int getWordsBefore()
    {
        return wordsBefore;
    }

    /**
     * @return the wordsAfter
     */
    public int getWordsAfter()
    {
        return wordsAfter;
    }

    /**
     * @return the checkAround
     */
    public boolean isCheckAround()
    {
        return checkAround;
    }

    /**
     * @return the checkInside
     */
    public boolean isCheckInside()
    {
        return checkInside;
    }

    /**
     * @return the explanationFileNameAbsolute
     */
    public String getExplanationFileNameAbsolute()
    {
        return explanationFileNameAbsolute;
    }

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param words the words to set
     */
    public void setWords(ArrayList<String> words)
    {
        this.words = words;
    }

    /**
     * @return the numberOfEntries
     */
    public int getNumberOfEntries()
    {
        return numberOfEntries;
    }

    /**
     * @return the firstOnly
     */
    public boolean isFirstOnly()
    {
        return firstOnly;
    }

    /**
     * @return the lastOnly
     */
    public boolean isLastOnly()
    {
        return lastOnly;
    }

    /**
     * @return the allowedInSpan
     */
    public int getAllowedInSpan()
    {
        return allowedInSpan;
    }

    /**
     * @return the charByCharAfter
     */
    public int getCharByCharAfter()
    {
        return charByCharAfter;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @param explanationFileNameAbsolute the explanationFileNameAbsolute to set
     */
    public void setExplanationFileNameAbsolute(String explanationFileNameAbsolute)
    {
        this.explanationFileNameAbsolute = explanationFileNameAbsolute;
    }
}

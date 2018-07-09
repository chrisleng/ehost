/* SynonymCleaner.java
 * 
 * This method expands acronyms, abbrevations and truncations, including
 * those containing punctuation. This is adapted from the EMT-P version 2.2.
 * This file incorporates the functions of synonyms.pl and synonyms_punc.pl.
 *  
 * Created: February 05, 2008
 */
package algorithmNegex;

/**
 *
 * @author Shuying Shen
 */
import java.io.*;
import java.util.logging.Level;
import java.util.regex.*;

public class SynonymCleaner {

    private BufferedReader synonymRefReader;
    private String inLine;
    private String[] rawString,  cleanString,  inLineSplit;
    private int length = 0;

    // Constructor
    public SynonymCleaner() {

        try {
            synonymRefReader = new BufferedReader(new FileReader("synonyms_ref.txt"));

            // Read reference list of synonyms and the replacements into array
            while ((inLine = synonymRefReader.readLine()) != null) {
                inLineSplit = inLine.split("\t", 2);
                rawString[length] = inLineSplit[0].toLowerCase();
                cleanString[length] = inLineSplit[1].toLowerCase();
                length++;
            }

            synonymRefReader.close();
        } catch (Exception e) {
            log.LoggingToFile.log(Level.SEVERE, e + "Couldn't open file");
        }
    }

    public String cleanSynonym(String currentNote) {
        Pattern regex;
        Matcher m;
        for (int i = 0; i < length; i++) {
            regex = Pattern.compile(" \\p{Punct}*" + rawString[i] + "\\p{Punct}^ ");
            currentNote = regex.matcher(currentNote).replaceAll(" "+cleanString+" ");
        }
        
        return currentNote;
    }
}

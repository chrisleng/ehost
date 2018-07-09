/*
 * dig out experiencer related phrase in a specific sentence by
 * ConText Algorithm.
 */

package algorithmConText;

import nlp.storageSpaceDraftLevel.StorageSpace;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * dig out experiencer related phrase in a specific sentence by
 * ConText Algorithm.<p>
 *
 * @author  Jianwei Leng, University Of Utah
 * @since   Friday, June 4, 2010, 02:10:00 PM MST
 * @since   JDK1.5
 */
public class Experiencer {
    
    /**1. find all experiencer words in a specific sentence.<p>
     * 2. record all founds into storage memory in format.<p>
     */
    public void experiencerDigger(String _sentenceText, // text of sentence
            // start point of the sentence in paragraph
            /**parameter _sentenceEnd - <code>String<code> - end point
             * of the sentence in paragraph<p>*/
            int _offsetInFile,
            int _offsetInParagraph,
            int _overallIndex
            ){

        // experiencer trigger terms
        String[] experiencerTriggers = {
            "Father", "Father's",
            "Mother", "Mother's",
            "Dad", "Dad's",
            "Mom", "Mom's",
            "Sister", "Sister's",
            "Brother", "Brother's",
            "Aunt", "Aunt's",
            "Uncle", "Uncle's",
            "Grandfather", "Grandfather's",
            "Grandmother", "Grandmother's",
            "Family( +)history"
        };

        // assemble regex
        int size = experiencerTriggers.length;
        String[] regex = new String[size];
        String possibleSymbolHead = "(( +)|(:)|(;)|(\\.)|(\")|(~)|(#)|(@)"+
                "|($)|(%)|(\\*)|(-)|(!)|(^)|(&)|(\\()|(,)|(_))";
        String possibleSymbolTail = "(( +)|(;)|(:)|(,)|(\\.)|(;)|(:)|(@)|(#)|(_))";
        for(int i=0;i<size;i++){
            regex[i] = possibleSymbolHead
                     + "(" + experiencerTriggers[i] + ")"
                     + possibleSymbolTail;
        }

        // use to correct the offset while you only have
        // 
        int offsetInParagraph = 0;

        // start regular expression
        for(int i=0;i<size;i++){
            // results of regular expression in vector
            Vector<Table_Experiencer> experiencers;
            experiencers = experiencerRegex( regex[i], _sentenceText );

            // validity check
            if (experiencers == null)
                continue;

            // record found experiencer
            int size_experiencers = experiencers.size();

            for(int j=0;j<size_experiencers;j++){
                
                // span start and end in file of your found experiencer word
                // ****TESTED and got CONFIRMED in result editor on June 6, 2010
                int jstart = experiencers.get(j).spanStart + _offsetInParagraph
                           + _offsetInFile + 1;
                int jend   = experiencers.get(j).spanEnd + _offsetInParagraph
                           + _offsetInFile - 1;

                // sentence start and end of this sentence
                // use to indicate the range of this experiencer
                // ****TESTED and got CONFIRMED in result editor on June 6, 2010
                int jsstart = _offsetInParagraph + _offsetInFile;
                int jsend   = _offsetInParagraph + _offsetInFile
                            + _sentenceText.length();

                // log on screen
                logs.ShowLogs.printResults("== experiencer == ", "- ["
                        + experiencers.get(j).Experiencer + "], located on ["
                        + jstart + ", " + jend + " with sentence on ["
                        + jsstart + ", " + jsend + "].");

                //record this into storage space
                StorageSpace.addExperiencer(_overallIndex,
                        experiencers.get(j).Experiencer,
                        // span start and end in file of your found experiencer word
                        jstart,jend,
                        // sentence start and end of this sentence
                        jsstart, jsend);
            }
        }


    }

    /**Use given regex to find experiencer in a sentence.<p>
     * @return Vector<<code>SF_Experiencer</code>>
     * <p>
     */
    private Vector<Table_Experiencer> experiencerRegex(String _regex, String _sentence){

        // variable for return
        Vector<Table_Experiencer> exper = new Vector<Table_Experiencer>();
        // sentence
        String sentence = " "+ _sentence + " ";

        // regular expression
        try{
            Matcher matcher;
            matcher = Pattern.compile( _regex, Pattern.CASE_INSENSITIVE ).matcher( sentence );

            boolean match_found = matcher.find();

            int start, end; String text;

            while(match_found){
                // get matched details
                start = matcher.start();
                end = matcher.end();
                text = sentence.substring(start, end);

                //assemble data in structure of experiencer
                Table_Experiencer e = new Table_Experiencer();
                e.Experiencer = text.substring(1,(text.length() - 1));
                e.spanEnd = end - 1;
                e.spanStart = start - 1;
                // add in vector
                exper.add(e);

                // go next record if existing
                match_found = matcher.find();
            }

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, e.toString());
            return exper;
        }

        // return results
        return exper;

    }
}

/*
 * temporality check by ConText algorithm
 */

package algorithmConText;

import nlp.storageSpaceDraftLevel.DraftLevelFormat;
import nlp.storageSpaceDraftLevel.StorageSpace;
import nlp.storageSpaceDraftLevel.Table_Negation;
import commons.Constant.temporality;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * temporality check of ConText Algorithm.<p>
 *
 * @author  Jianwei Leng, University Of Utah
 * @since   Monday, June 7, 2010, 04:50:42 AM MST
 * @since   JDK1.5
 */
public class Temporality {

    /**to a given sentence, check its temporality status*/
    public temporality temporalityDector(String _sentence,
            int _offsetInFile,
            int _offsetInParagraph,
            int _overallIndex){
        
        // <<1>> start from default value on "recent"
        // t: status of temporality in this sentences
        // default is recent
        temporality t = temporality.recent;

        // <<2>> check hypotherical trigger term
        boolean found_hypotherical_trigger = false;
        found_hypotherical_trigger = HypotheticalPatternCheck( _sentence );

        // <<3>> if found
        if (found_hypotherical_trigger){
            log.LoggingToFile.log( Level.FINE,"found hypotherical trigger");
            
            // check for any negation in this sentence
            int sentenceStartPoint = _offsetInFile;
            int sentenceEndPoint   = _offsetInFile + _sentence.length();
            boolean found_negation_in_sentence = 
                    checkNegation(sentenceStartPoint, sentenceEndPoint,
                    _overallIndex);

            // if NO negation found in this sentence
            if(!found_negation_in_sentence){
                return temporality.hypothetical;
            }else{
                t = historicalPathway(_sentence);
            }


            // if not found, it's hypotherical, mark it in its range
        }else{
            t = historicalPathway(_sentence);
        }
        
        return t;
    }

    /**regular expression operation to find historical trigger*/
    private temporality historicalPathway(String _sentence){
        temporality t = temporality.recent;
        // check historical trigger exsiting or not
        boolean found_historical_trigger = HistoricalTriggerCheck(_sentence);

        // found any historical trigger in sentence
        if(found_historical_trigger){
            // regular expression operation 
            // to find pseudo-historical triggers
            boolean found_PSEUDO_historical_trigger;
            found_PSEUDO_historical_trigger = pseudoHistoricalCheck(_sentence);
            
            // if found pseudo historical trigger, 'recent'
            if(found_PSEUDO_historical_trigger){
                t = temporality.recent;
                return t;
                
            }else{
            // if no pseudo historical trigger, "historical"
                t = temporality.historical;
                return t;

            }

        }else{
        // do NOT found any historical trigger
            t = temporality.recent;
            return t;
        }
    }

    /**to a given range, check negation in it.<p>
     * @return  true    if found any negative phrase in specific range
     *          false   if not<p>
     */
    private boolean checkNegation(int _rangeStartPoint,
            int _rangeEndPoint, int _overallIndex){
        try{
            DraftLevelFormat dlf = StorageSpace.getParagraphInFormat(_overallIndex);
            // get all negative phrases in this paragraph
            ArrayList<Table_Negation> negations = dlf.Negations;

            // amount of negations in this paragraph
            int negationAmount = negations.size();

            // go over all negation in this paragraph 
            for(int i=0;i<negationAmount;i++){
                Table_Negation negation = negations.get(i);
                int effectiveRange_LowerSide = negation.effectiveRange_start;
                int effectiveRange_UpperSide = negation.effectiveRange_end;

                // if found, return true
                if((_rangeStartPoint <= effectiveRange_LowerSide)
                        &&(_rangeEndPoint >= effectiveRange_UpperSide)){
                    return true;
                }
            }


        }catch(Exception e){
            return false;
        }
        return false;
    }

    /**regular expression operation to find hypothetical triggers.<p>
     * @return  true    if found hypothetical triggers in this sentence.<p>
     *          false   if not.<p>
     */
    private boolean HistoricalTriggerCheck(String _current_sentence ){
        String[] Historical_Triggers = {
            "([0123456789]+)(-|( *))((days?)|(hours?)|(weeks?)|(months?)|(years?))",
            "(for|over)( +)(the)( +)(last|past)( +)([0123456789]+)",
            "(since|last)( +)"
        };

        // preprocessing: add space in two endpoint for Regular Expression Operation
        String current_sentence = " " + _current_sentence + " ";

        boolean found_historical_triggers = false;
        // search hypothetical triggers in current sentence
        int size = Historical_Triggers.length;
        for( int i=0; i<size; i++){
            Historical_Triggers[i] = "( )" + Historical_Triggers[i] + "( )";

            // check hypothetical triggers
            boolean result = Find_Keywords(
                    Historical_Triggers[i].toString(), _current_sentence);

            if (result) {
                found_historical_triggers = true;
                return found_historical_triggers;
            }
        }

        return found_historical_triggers;
    }

    /**regular expression operation to find PSEUDO hypothetical triggers.<p>
     * @return  true    if found PSEUDO hypothetical triggers in sentence.<p>
     *          false   No found.<p>
     */
    private boolean pseudoHistoricalCheck(String _current_sentence ){
        String[] pseudo_Historical_Triggers = {
            "History( +)and( +)physical",
            "History( +)physical",
            "History( +)taking",
            "Poor( +)history",
            "History( +)and( +)examination",
            "History( +)and( +)chief( +)complaint",
            "history( +)for",
            "history( +)and",
            "history( +)of( +)present( +)illness",
            "cocial( +)history",
            "family( +)history"
        };

        // preprocessing: add space in two endpoint for Regular Expression Operation
        String current_sentence = " " + _current_sentence + " ";

        boolean found_pseudo_historical_triggers = false;
        // search pseudo hypothetical triggers in current sentence
        int size = pseudo_Historical_Triggers.length;
        for( int i=0; i<size; i++){
            pseudo_Historical_Triggers[i] = "( )" + pseudo_Historical_Triggers[i] + "( )";

            // check pseudo hypothetical triggers
            boolean result = Find_Keywords(
                    pseudo_Historical_Triggers[i].toString(), _current_sentence);

            if (result) {
                found_pseudo_historical_triggers = true;
                return found_pseudo_historical_triggers;
            }
        }

        return found_pseudo_historical_triggers;
    }

    /**regular expression operation to find hypothetical triggers.<p>
     * @return  true    if found hypothetical triggers in this sentence.<p>
     *          false   if not.<p>
     */
    private boolean HypotheticalPatternCheck(String _current_sentence ){
        String[] Hypothetical_Triggers = {"if",
            "return",
            "(should)( +)(he|she)",
            "(should)( +)(there)",
            "(should)( +)(the)( patient)",
            "(as)( +)(needed)",
            "(come)( +)(back)( +)(for|to)"
        };

        // preprocessing: add space in two endpoint for Regular Expression Operation
        String current_sentence = " " + _current_sentence + " ";

        boolean found_hypothetical_triggers = false;
        // search hypothetical triggers in current sentence
        int size = Hypothetical_Triggers.length;
        for( int i=0; i<size; i++){
            Hypothetical_Triggers[i] = "( )" + Hypothetical_Triggers[i] + "( )";

            // check hypothetical triggers
            boolean result = Find_Keywords(
                    Hypothetical_Triggers[i].toString(), _current_sentence);

            if (result) {
                found_hypothetical_triggers = true;
                return found_hypothetical_triggers;
            }
        }

        return found_hypothetical_triggers;
    }

    // find keywords in a sentence by specific regex
    private boolean Find_Keywords(String _regex, String _sentence ){
        Matcher matcher;
        matcher = Pattern.compile( _regex, Pattern.CASE_INSENSITIVE ).matcher( _sentence );

        boolean match_found = matcher.find();

        return match_found;
    }

    
}

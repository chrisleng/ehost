/* building negative relationshop for concepts and negations
 */

package nlp.postProcess.relationships;

import nlp.storageSpaceDraftLevel.DraftLevelFormat;
import nlp.storageSpaceDraftLevel.StorageSpace;
import nlp.storageSpaceDraftLevel.Table_Concept;
import nlp.storageSpaceDraftLevel.Table_ConceptAttributes;
import nlp.storageSpaceDraftLevel.Table_Negation;
import commons.Constant.attributeType;
import java.util.ArrayList;

/**
 * 1. To a concept in a paragraph, found and marked its nagative relationship<p>
 * Concept terms always have two attributes: <tt>spanstart</tt> and
 * <tt>spanend</tt>. Those are coordinates of the concepts. <p>
 * And you can found negation phrases in this paragraph, and all negations
 * have their own effective ranges. Task of method <code>BuildRelationship</code>
 * are used to find concepts appeared in effective ranges of negations.<p>
 *
 * 2. to the attributes(slotmention), assign the mention id to them.<p>
 *
 * 3. this method MUST be run AFTER class <code>AssignMentionID</code>
 * because of No. 2.<p>
 *
 * @author Jianwei Leng
 * @since Monday 06-07-2010, 12:43 am MST
 */
public class Negation {

    /**1.find concepts appeared in effective ranges of negations.<p>
     * 2.buid relationship.<p>
     */
    public void BuildRelationship(){

        // find how many paragraph had been processed
        int amount_of_all_paragraphs = StorageSpace.Length();
        // if no paragraph got processed, exit from this function
        if(amount_of_all_paragraphs <= 0)
            return;

        // **** to all records in the storage space, if they belong to the specific file
        // apply the concept result filter to it
        for(int i=0;i<amount_of_all_paragraphs;i++){

            // <<1>> get specified record
            DraftLevelFormat cef
                = (DraftLevelFormat) StorageSpace.getParagraphInFormat(i);

            // <<2>> get all concepts in this paragraph
            ArrayList<Table_Concept> concepts = cef.ConceptDetails;
            // get all negations in this paragraph
            ArrayList<Table_Negation> negations = cef.Negations;

            // get amount of concepts and negations
            int conceptAmount = concepts.size();
            int negationAmount = negations.size();

            // if amount of concepts found in this paragraph is less than 1
            // continue to process next paragraph
            // (same results as for loops in <<3>> )
            if( conceptAmount < 1)
                continue;

            // <<3>> go over all concepts to find their negative relationship
            for(int j=0;j<conceptAmount; j++){
                Table_Concept concept = concepts.get(j);
                int start = concept.span_start_in_file;
                int end = concept.span_end_in_file;

                for(int k=0;k<negationAmount;k++){
                    Table_Negation negation = negations.get(k);
                    int effectiveRange_lowerSide = negation.effectiveRange_start;
                    int effectiveRange_upperSide = negation.effectiveRange_end;
                    
                    // build relationship
                    // if concept(j) is in effective range of negation(k)
                    if( (start>= effectiveRange_lowerSide )
                            &&(end <= effectiveRange_upperSide) ){

                        // get value of latest used mention id
                        int latestUsedMentionID = getLatestUsedMentionID();

                        Table_ConceptAttributes attribute = new Table_ConceptAttributes();

                        attribute.hasClassMentionID = latestUsedMentionID + 1;
                        
                        // type of attributes
                        attribute.type = attributeType.negation;

                        // == details of this attribute ==
                        attribute.mentionSlotID = "negation";

                        // set slotmentionvalue of negation
                        if (negation.type == commons.Constant.NEGATED_NEGATION )
                            attribute.slotMentionValue = "negated";
                        else if (negation.type == commons.Constant.POSSIBLE_NEGATION )
                            attribute.slotMentionValue = "possible";
                        else attribute.slotMentionValue = "negation";

                        

                        concept.attributes.add(attribute);

                        // write latest used mention id in env
                        updateLatestUsedMentionID( latestUsedMentionID + 1 );
                    }

                }
            }
            
        }
    }

    /** get and update newly used mention id number in integer
     */
    private int getLatestUsedMentionID(){
        int latestUsedMentionID = env.Parameters.getLatestUsedMentionID();
        if (latestUsedMentionID < 10000)
            latestUsedMentionID = 10000;
        
        return latestUsedMentionID;
    }

    private void updateLatestUsedMentionID( int _newLatestUsedMemtionID ){
        env.Parameters.updateLatestUsedMentionID( _newLatestUsedMemtionID );
    }
}

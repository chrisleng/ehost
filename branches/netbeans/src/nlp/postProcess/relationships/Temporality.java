/* indicate which experiencer in this sentencer/concept.
 */

package nlp.postProcess.relationships;

import nlp.storageSpaceDraftLevel.DraftLevelFormat;
import nlp.storageSpaceDraftLevel.StorageSpace;
import nlp.storageSpaceDraftLevel.Table_Concept;
import nlp.storageSpaceDraftLevel.Table_ConceptAttributes;
import nlp.storageSpaceDraftLevel.Table_Temporality;
import commons.Constant.attributeType;
import java.util.ArrayList;

/**
 * 1. desicript temporality of conepts in sentences.<p>
 *
 * @author Jianwei Leng
 * @since Monday 06-07-2010, 06:39 am MST
 */
public class Temporality {

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
            // get all temporalities information in this paragraph
            ArrayList<Table_Temporality> temporalities = cef.Temporality;

            // get amount of concepts and temporalities info
            int conceptAmount = concepts.size();
            int temporalitiesAmount = temporalities.size();

            // if amount of concepts found in this paragraph is less than 1
            // continue to process next paragraph
            // (same results as for loops in <<3>> )
            if( conceptAmount < 1)
                continue;

            // <<3>> go over all concepts to find their temporalities
            for(int j=0;j<conceptAmount; j++){
                Table_Concept concept = concepts.get(j);
                int start = concept.span_start_in_file;
                int end = concept.span_end_in_file;

                for(int k=0; k < temporalitiesAmount; k++){
                    Table_Temporality thisTemporality = temporalities.get(k);
                    int sentence_startpoint = thisTemporality.effectRangeStartPoint;
                    int sentence_endpoint = thisTemporality.effectRangeEndPoint;

                    // build relationship
                    // if concept(j) is in effective range of Temporality(k)
                    if( (start>= sentence_startpoint )
                            &&(end <= sentence_endpoint) ){


                        // get value of latest used mention id
                        int latestUsedMentionID = getLatestUsedMentionID();

                        Table_ConceptAttributes attribute = new Table_ConceptAttributes();

                        attribute.hasClassMentionID = latestUsedMentionID + 1;

                        // type of attributes
                        attribute.type = attributeType.temporality;

                        // == details of this attribute ==
                        attribute.mentionSlotID = "temporality";

                        // set slotmentionvalue of stringSlot MentionValue
                        attribute.slotMentionValue = thisTemporality.temporalityStatus;

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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.postProcess;

import nlp.storageSpaceDraftLevel.Table_Experiencer;
import nlp.storageSpaceDraftLevel.Table_CustomRegex;
import nlp.storageSpaceDraftLevel.Table_Negation;
import java.util.ArrayList;

/**
 *
 * @author Jianwei Leng
 */
public class AssignMentionID {

    public void assignMentionID(){

        // find how many paragraph had been processed
        int number_of_paragraphs = nlp.storageSpaceDraftLevel.StorageSpace.Length();
        // if no paragraph got processed, exit from this function
        if(number_of_paragraphs <= 0)
            return;

        // **** to all records in the storage space, if they belong to the specific file
        // apply the concept result filter to it
        for(int i=0;i<number_of_paragraphs;i++)
        {
            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                = (nlp.storageSpaceDraftLevel.DraftLevelFormat)nlp.storageSpaceDraftLevel.StorageSpace.getParagraphInFormat(i);

            /** [1] assign mention id to concepts*/
            // if information belong to current file
            // apply the concept result filter
            ArrayList conceptsdetails = cef.ConceptDetails;
            // assign id for these concept results
            ArrayList modifiedConceptArrayList = assignAndRecordMentionID(conceptsdetails);
            // update these modified concept results to the paragraph
            // and update the paragraph in the storagespace in draft level
            updateConcepts_into_StorageSpaceDraftLevel(i, modifiedConceptArrayList);


            

            /** [2] assign mention id to SSN
             */
            if(env.Parameters.NLPAssistant.OUTPUTSSN){
                ArrayList SSNDs = cef.SSNDetails;
                // assign id for these concept results
                ArrayList modifiedSSNArrayList = assignAndRecordMentionID_toSSN(SSNDs);
                updateSSNs_into_StorageSpaceDraftLevel(i, modifiedSSNArrayList );
            }

            /** [3] assign mention id to Date and time terms
             */
            if(env.Parameters.NLPAssistant.OUTPUTDATE){
                ArrayList dates = cef.Dates;
                // assign id for these concept results
                ArrayList modifiedSSNArrayList = assignAndRecordMentionID_toDates(dates);
                updateDates_into_StorageSpaceDraftLevel(i, modifiedSSNArrayList );
            }

            /** [4] assign mention id to terms found by filter of
             * custom regular expression
             */
            if(env.Parameters.NLPAssistant.OUTPUTCUSTOMREGEX){
                ArrayList<Table_CustomRegex> customRegexs = cef.CustomRegex;
                // assign id for these concept results
                ArrayList<Table_CustomRegex> modifiedCustomRegexArrayList
                        = assignAndRecordMentionID_toCustomRegexs(customRegexs);
                updateCustomRegexs_into_StorageSpaceDraftLevel(i, modifiedCustomRegexArrayList );
            }

            /** [5] assign mention id to negation phrases
             */
            if(env.Parameters.NLPAssistant.OUTPUTNEGATION){

                ArrayList<Table_Negation> negations = cef.Negations;
                // assign mention id for these negations
                assignMentionID_toNegations(negations);

            }

            /** [6] assign mention id to experiencer
             */
            if(env.Parameters.NLPAssistant.OUTPUTEXPERIENCER){
                ArrayList<Table_Experiencer> experiencer = cef.Experiencer;
                assignMentionID_toExperiencer(experiencer);
            }


        }

        return;
    }


    /**designate a mention id to all experiencer in this paragraph*/
    private void assignMentionID_toExperiencer(
            ArrayList<Table_Experiencer> _experiencersInArrayList ){

        // amount of all negation terms in this paragraph
        int size = _experiencersInArrayList.size();

        // get value of latest used mention id
        int latestUsedMentionID = getLatestUsedMentionID();

        // go over all records in the arraylist and assign a mention id to them
        for(int i=0; i<size;i++){
            // *** get experiencer details
            Table_Experiencer experiencer
                    = _experiencersInArrayList.get(i);

            experiencer.mentionID = latestUsedMentionID + 1;
            latestUsedMentionID++;
        }

        // write latest used mention id in env
        updateLatestUsedMentionID( latestUsedMentionID );

    }

    /**designate a mention id to all negation terms in this paragraph*/
    private void assignMentionID_toNegations(
            ArrayList<Table_Negation> _negationsInArrayList ){

        // amount of all negation terms in this paragraph
        int size = _negationsInArrayList.size();

        // get value of latest used mention id
        int latestUsedMentionID = getLatestUsedMentionID();

        // go over all records in the arraylist and assign a mention id to them
        for(int i=0; i<size;i++){
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_Negation negation
                        = _negationsInArrayList.get(i);

                negation.mentionID = latestUsedMentionID + 1;
                latestUsedMentionID++;


        }

        // write latest used mention id in env
        updateLatestUsedMentionID( latestUsedMentionID );

    }

    // assign mention id to all found SSN
    private ArrayList assignAndRecordMentionID_toSSN( ArrayList _SSNDetailsInArrayList ){
            ArrayList modifiedResults = _SSNDetailsInArrayList;
            int size = _SSNDetailsInArrayList.size();

            int latestUsedMentionID = getLatestUsedMentionID();

            // compare a entry result to others
            for(int i=0; i<size;i++){
                    // *** get current concept result
                    nlp.storageSpaceDraftLevel.Table_SSN SSN
                            = (nlp.storageSpaceDraftLevel.Table_SSN) modifiedResults.get(i);

                    SSN.mentionID = latestUsedMentionID + 1;
                    latestUsedMentionID++;

                    modifiedResults.set(i, SSN);
            }

            updateLatestUsedMentionID( latestUsedMentionID );

            return modifiedResults;
    }

    // assign mention id to all found date and time terms
    private ArrayList assignAndRecordMentionID_toDates( ArrayList _dateDetailsInArrayList ){
        ArrayList modifiedResults = _dateDetailsInArrayList;
        int size = _dateDetailsInArrayList.size();

        int latestUsedMentionID = getLatestUsedMentionID();

        // compare a entry result to others
        for(int i=0; i<size;i++){
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_DateTime dda
                        = (nlp.storageSpaceDraftLevel.Table_DateTime) modifiedResults.get(i);

                dda.mentionID = latestUsedMentionID + 1;
                latestUsedMentionID++;

                modifiedResults.set(i, dda);
        }

        updateLatestUsedMentionID( latestUsedMentionID );

        return modifiedResults;
    }


    // assign mention id to all found concepts
    private ArrayList assignAndRecordMentionID( ArrayList _conceptDetailsInArrayList )
    {
            ArrayList modifiedResults = _conceptDetailsInArrayList;
            int size = _conceptDetailsInArrayList.size();

            int latestUsedMentionID = getLatestUsedMentionID();

            // compare a entry result to others
            for(int i=0; i<size;i++){
                    // *** get current concept result
                    nlp.storageSpaceDraftLevel.Table_Concept cd
                            = (nlp.storageSpaceDraftLevel.Table_Concept) modifiedResults.get(i);

                    cd.mentionID = latestUsedMentionID + 1;
                    latestUsedMentionID++;

                    modifiedResults.set(i, cd);
            }

            updateLatestUsedMentionID( latestUsedMentionID );

            return modifiedResults;
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


    /** update modified data in the storage space on draft level
     */
    private void updateConcepts_into_StorageSpaceDraftLevel(int _index, ArrayList _modifiedResults){
            nlp.storageSpaceDraftLevel.StorageSpace.UpdateConcepsDetails_toAParagraph(_index, _modifiedResults);
    }

    private void updateSSNs_into_StorageSpaceDraftLevel(int _index, ArrayList _modifiedResults){
            nlp.storageSpaceDraftLevel.StorageSpace.UpdateSSNDetails_toAParagraph(_index, _modifiedResults);
    }

    private void updateDates_into_StorageSpaceDraftLevel(int _index, ArrayList _modifiedResults){
            nlp.storageSpaceDraftLevel.StorageSpace.UpdateDateDetails_toAParagraph(_index, _modifiedResults);
    }

    private ArrayList<Table_CustomRegex> assignAndRecordMentionID_toCustomRegexs(
        ArrayList<Table_CustomRegex> _customRegexsInArrayList) {
        ArrayList<Table_CustomRegex> modifiedResults = _customRegexsInArrayList;
            int size = _customRegexsInArrayList.size();

            int latestUsedMentionID = getLatestUsedMentionID();

            // compare a entry result to others
            for(int i=0; i<size;i++){
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_CustomRegex cr
                        = (nlp.storageSpaceDraftLevel.Table_CustomRegex) modifiedResults.get(i);

                cr.mentionID = latestUsedMentionID + 1;
                latestUsedMentionID++;

                modifiedResults.set(i, cr);
            }

            updateLatestUsedMentionID( latestUsedMentionID );

            return modifiedResults;
    }

    private void updateCustomRegexs_into_StorageSpaceDraftLevel(int _index, ArrayList<Table_CustomRegex> _modifiedCustomRegexArrayList) {
        nlp.storageSpaceDraftLevel.StorageSpace.UpdateCustomRegexResults_toDesignatedParagraph(_index, _modifiedCustomRegexArrayList);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.postProcess;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class ConceptResultFilter {
        private String __filename__;
        public ConceptResultFilter(String _filename ){
                __filename__ = _filename;
        }


    public void conceptResultsFilter(){
        // show log on screen
        String infostr = "Begin to REMOVE concepts who got " +
                "part of whole repetitive!n  - to file -" + __filename__;
        log.LoggingToFile.log( Level.INFO, infostr );
        logs.ShowLogs.printImportantInfoLog( infostr );
        if( __filename__ == null )
                return;
        // find how many paragraph had been processed
        int amount_of_all_paragraphs = nlp.storageSpaceDraftLevel.StorageSpace.Length();
        // if no paragraph got processed, exit from this function
        if(amount_of_all_paragraphs <= 0)
            return;

        // **** to all records in the storage space, if they belong to the specific file
        // apply the concept result filter to it
        for(int i=0;i<amount_of_all_paragraphs;i++){
            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)nlp.storageSpaceDraftLevel.StorageSpace.getParagraphInFormat(i);

            //System.out.println( "filename = " + cef.filename + ",  __filename__ = "
            //       + __filename__ );
            // if information belong to current file
            // apply the concept result filter
            File file = new File(cef.filename.trim());
            if( file.getName().compareTo(__filename__.trim()) == 0 ){
                ArrayList conceptsResults = cef.ConceptDetails;
                // filter these concept results
                ArrayList modifiedResults = conceptFilter_ByRange(conceptsResults);
                // update these modified concept results to the paragraph
                // and update the paragraph in the storagespace in draft level
                UpdateStorageSpaceDraftLevel(i, modifiedResults);
            }
        }

        return;
    }

        private void UpdateStorageSpaceDraftLevel(int _index, ArrayList _modifiedResults){
                nlp.storageSpaceDraftLevel.StorageSpace.UpdateConcepsDetails_toAParagraph(_index, _modifiedResults);
        }

        //
        private ArrayList conceptFilter_ByRange( ArrayList _conceptResults ){
                ArrayList modifiedResults = _conceptResults;
                int size = _conceptResults.size();
                // compare a entry result to others
                for(int i=0; i<size;i++){
                        // *** get current concept result
                        nlp.storageSpaceDraftLevel.Table_Concept i_cd
                                = (nlp.storageSpaceDraftLevel.Table_Concept) modifiedResults.get(i);

                        if( i_cd.concept_text == null)
                                continue;

                        int i_start = i_cd.span_start_in_file;
                        int i_end = i_cd.span_end_in_file;
                        int i_weight = i_cd.weight;

                        // *** compare to all others
                        for(int j=0; j<size;j++){
                                // make sure they do not compare to themself
                                if (i == j) continue;

                                nlp.storageSpaceDraftLevel.Table_Concept j_cd
                                    = (nlp.storageSpaceDraftLevel.Table_Concept) modifiedResults.get(j);

                                if( j_cd.concept_text == null)
                                        continue;

                                int j_start = j_cd.span_start_in_file;
                                int j_end = j_cd.span_end_in_file;
                                int j_weight = i_cd.weight;

                                // ******** core of comparation ******
                                // if - they have same range
                                if((i_start == j_start)&&( i_end == j_end )){

                                        // same weight
                                        if( !env.Parameters.Pre_Defined_Dictionary_DifferentWeight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + j_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + j_start
                                                    + ", "
                                                    + j_end
                                                    + "]");
                                            j_cd.concept_text = null;
                                            modifiedResults.set( j , j_cd);
                                        }
                                        // following are for different weight
                                        // if i has more weight than j (small means more weight)
                                        else if( i_weight <= j_weight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + j_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + j_start
                                                    + ", "
                                                    + j_end
                                                    + "]");
                                            j_cd.concept_text = null;
                                            modifiedResults.set( j , j_cd);
                                        }else{
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + i_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + i_start
                                                    + ", "
                                                    + i_end
                                                    + "]");
                                            i_cd.concept_text = null;
                                            modifiedResults.set( i , i_cd);
                                        }

                                }else if((i_start <= j_start)&&( i_end >= j_end )){
                                // if - i has bigger ranger

                                        // same weight
                                        if( !env.Parameters.Pre_Defined_Dictionary_DifferentWeight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + j_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + j_start
                                                    + ", "
                                                    + j_end
                                                    + "]");
                                            j_cd.concept_text = null;
                                            modifiedResults.set( j , j_cd);
                                        }
                                        // following are for different weight
                                        // if i has more weight than j (small means more weight)
                                        else if( i_weight <= j_weight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + j_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + j_start
                                                    + ", "
                                                    + j_end
                                                    + "]");
                                            j_cd.concept_text = null;
                                            modifiedResults.set( j , j_cd);
                                        }

                                }else if((i_start >= j_start)&&( i_end <= j_end )){
                                // if - j has bigger ranger

                                        // same weight
                                        if( !env.Parameters.Pre_Defined_Dictionary_DifferentWeight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + i_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + i_start
                                                    + ", "
                                                    + i_end
                                                    + "]");
                                            i_cd.concept_text = null;
                                            modifiedResults.set( i , i_cd);
                                            continue;
                                        }
                                        // following are for different weight
                                        // j have heavy weight here
                                        else if( i_weight >= j_weight ){
                                            logs.ShowLogs.printInfoLog("filter of concept result removed concept - ["
                                                    + i_cd.concept_text
                                                    + "] - with coordinates[ "
                                                    + i_start
                                                    + ", "
                                                    + i_end
                                                    + "]");
                                            i_cd.concept_text = null;
                                            modifiedResults.set( i , i_cd);
                                        }
                                }


                        }

                }
                
                return modifiedResults;
        }

}

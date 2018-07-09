/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmNegex;
/**
 *
 * @author Chris
 */
public class ConceptUtilities {

        public static String getConceptAnnouncedName(String _Concept){
                String conceptAnnouncedName = "Concept";
                /*try{
                        String concept = null;
                        int amount_of_dictionaries = Dictionaries.ConceptDictionaries.ConceptArray.size();
                        for(int j=0; j<amount_of_dictionaries;j++){
                                Dictionaries.ConceptDictionaryFormat cf = Dictionaries.ConceptDictionaries.ConceptArray.get(j);

                                int size = cf.original_preannotated_concept.size();
                                int index = 0;

                                // check if there are enough concepts in the lib
                                if( size < 1 ){
                                        Logs.ShowLogs.printErrorLog("ConceptUtilities.java - empty arraylist of original pre-annotated concept list!");
                                        return null;
                                }

                                // go over the array and compare all strings
                                for( int i = 0; i < size; i++) {
                                        concept = cf.original_preannotated_concept.get( i ).trim().toLowerCase();
                                        if ( _Concept.compareTo( concept.trim() ) == 0){
                                                cf.Comment.get(i).trim();
                                                return conceptAnnouncedName;
                                        }
                                }
                        }
                        
                        return null;

                }catch(Exception e){
                       conceptAnnouncedName = null;
                       Logs.ShowLogs.printErrorLog("error find while try to find the announced name for concept:[" + _Concept + "]");
                }*/
                return conceptAnnouncedName;
        }
}

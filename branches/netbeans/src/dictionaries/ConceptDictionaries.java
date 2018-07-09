/*****************************************************************
 * @author       Jianwei 'Chris' Leng
 * @created      on Januray 30, 2010, 4:11 AM
 * @Organization University of Utah, Veteran Affairs Salt Lake City
 * @purpose:     load pre-annotated concepts from dictionary to the static arraylist in the memory 
 * @History:     Feb 2, 2010, modified for refining the speed, from 41 seconds to 21 seconds for a 2600k dictionary loop
 *****************************************************************/
package dictionaries;

import java.io.BufferedReader;
import java.io.FileReader;
import logs.ShowLogs;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class ConceptDictionaries {
        public static ArrayList <ConceptDictionaryFormat> ConceptArray
                = new ArrayList<ConceptDictionaryFormat>();
        private static int amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;
        private static int total_amount_of_valid_preannotated_concepts = 0;
        //private int amount_of_valid_preannotated_concepts = 0;
        private final static boolean isStopWordsEnabled = true;

        /**empty all loaded pre-annotated concept dictionaries.*/
        public static void clear(){
            ConceptArray.clear();
            amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;
            total_amount_of_valid_preannotated_concepts = 0;
        }

        public static int size(){
            return total_amount_of_valid_preannotated_concepts;
        }

        
        /* 1.load all valid pre annotated concepts from file
         * 2.store to the static common data pool
         * the returned integer is the amount of valid concepts,
         * if return a '0', means fail to get the valid preannotated concept lib
         */
	public static int loadPreAnnotatedConcepts(){
                //reset counters
		
                int total_preannotated_concepts = 0;
                String comment;

                // amount of concept dictionaries
                int amount_of_concept_dictionaries = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
                // if no dictionaries, Exit
                if ( amount_of_concept_dictionaries <= 0 )
                        return -1;

                // **** <1> empty arraLyists before using
                ConceptArray.clear();
                
                
                // **** <2> load stop words //
                // stopwords: means the pre-annotated concept words will not be
                // stored into the memory if it appreared in the stopwords list

                StopWords.LoadingStopWords();

                // **** <3> load pre-annotated concepts from dictionaries one by one
                for(int i=0; i<amount_of_concept_dictionaries; i++)
                {
                        if(env.Parameters.NLPAssistant.STOPSign)
                        return -1;

                        dictionaries.ConceptDictionaryFormat CF = new dictionaries.ConceptDictionaryFormat();

                        String info = "Openning " + (i+1) + " file of total"
                                + amount_of_concept_dictionaries + " pre-annotated concept dictionary";
                        log.LoggingToFile.log(Level.INFO, info );
                        ShowLogs.printImportantInfoLog( info );
                        amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;
                        // **** <3.1> get filename and separator and weight
                        /** @para filename - file name with absolute path
                         * @para weight - if same all will be 0, otherwise big number
                         * means heavy weight
                         * @para description
                         * @para separator
                         * @para number_of_valid_entries
                         */
                        Object[] o = (Object[])env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                        String filename = o[0].toString().trim();
                        int weight = Integer.valueOf( o[1].toString().trim() );
                        String separator = o[3].toString().trim();

                        CF.weight = weight;

                        //int total_valid_preannotated_concepts = 0;
                        // **** <3.2> loading concepts from current dictionary
                        try{
                                
                                CF.Comment.clear();
                                //CF.original_preannotated_concept.clear();
                                CF.preprocessed_preannotated_Concept.clear();
                                CF.size = 0;
                                CF.weight = weight;

                                // **** <3.2.1> open the file
                                // preannotatedConcepts lib files includes 2 columns, which can be
                                // splitted by specific separator
                                BufferedReader thisDictionary
                                        = new BufferedReader( new FileReader( filename ));

                                log.LoggingToFile.log( Level.INFO, "Loading pre-annotated concept dictionary ......");
                                logs.ShowLogs.printImportantInfoLog( "Loading pre-annotated concept dictionary ......");
                                log.LoggingToFile.log( Level.INFO, "each point means 1000 entries(80K/line):" );
                                logs.ShowLogs.printImportantInfoLog( "each point means 1000 entries(80K/line):\n" );

                                // **** <3.2.2> go over the directionary and load entires into array
                                String st = thisDictionary.readLine();
                                
                                String[] sts; String regex; boolean validity;
                                
                                while (st != null) {
                                    
                                    // list current line and the line #
                                    // System.out.println(amount_of_valid_preannotated_concepts_in_a_specific_dictionary + "/ st="+st);
                                    
                                    //if( amount_of_valid_preannotated_concepts_in_a_specific_dictionary > 4850 )
                                    //    System.out.println("1");

                                    if(env.Parameters.NLPAssistant.STOPSign)
                                        return -1;

                                    validity = true;
                                    // split lines to 2 strings,
                                    // the concept and the comment(announced name of concept)
                                    sts = st.split( separator);
                                    
                                    if ( sts[0].trim().length() > 0 ) {

                                        //check the validity of current concept entry
                                        if( isStopWordsEnabled ) validity = ConceptValidity( sts[0] );
                                        

                                        if(!validity) {
                                                st = thisDictionary.readLine();
                                                continue;
                                        }
                                        
                                        // count how many entires in this directory
                                        amount_of_valid_preannotated_concepts_in_a_specific_dictionary++;

                                        if(amount_of_valid_preannotated_concepts_in_a_specific_dictionary%1000 == 0)
                                                ShowLogs.LogPrint(".", Color.blue);
                                                //System.out.print('.');
                                        if(amount_of_valid_preannotated_concepts_in_a_specific_dictionary%80000 == 0){
                                            log.LoggingToFile.log(Level.SEVERE, amount_of_valid_preannotated_concepts_in_a_specific_dictionary + " LOADED");
                                            ShowLogs.LogPrint( " - (" + String.valueOf(amount_of_valid_preannotated_concepts_in_a_specific_dictionary) + ") \n", Color.blue);
                                        }

                                        
                                        regex = sts[0].trim();
                                        //System.out.println("NO." + ConceptLib.len + "  -[" + sts[0] +"] - [" + sts[1]+"]");

                                        if (sts.length > 1)
                                                comment = sts[1];
                                        else{
                                            st = thisDictionary.readLine();
                                            continue;
                                            //comment = "UNINDICATED_IN_PRE_ANNOTATED_CONCEPT_DICTIONARIES";
                                        }
                                        
                                        CF.Comment.add( comment );

                                        //CF.original_preannotated_concept.add( regex );
                                        // pre processing for regular expression
                                        regex = regex.replaceAll("\\[", "\\\\[");
                                        regex = regex.replaceAll("\\]", "\\\\]");
                                        regex = regex.replaceAll("\\,", "\\\\,");
                                        regex = regex.replaceAll("\\:", "\\\\:");
                                        regex = regex.replaceAll("\\.", "\\\\.");
                                        regex = regex.replaceAll("\\-", "\\\\-");
                                        regex = regex.replaceAll("\\=", "\\\\=");
                                        regex = regex.replaceAll("\\*", "\\\\*");
                                        regex = regex.replaceAll("\\@", "\\\\@");
                                        regex = regex.replaceAll("\\&", "\\\\&");
                                        regex = regex.replaceAll("\\+", "\\\\+");
                                        regex = regex.replaceAll("\\%", "\\\\%");
                                        //regex = regex.replaceAll(" ", " " );
                                        regex = "[ |\\:|[\\p{Punct}&&[^_]]]" + regex + "[ |\\:|[\\p{Punct}&&[^_]]]";
                                        CF.preprocessed_preannotated_Concept.add( regex );
                                        
                                        

                                    }

                                    //read next line in this concept directionary
                                    st = thisDictionary.readLine();
                                }
                                

                                thisDictionary.close();

                                
                                CF.size = amount_of_valid_preannotated_concepts_in_a_specific_dictionary;
                                ConceptArray.add(CF);


                                total_amount_of_valid_preannotated_concepts
                                        += amount_of_valid_preannotated_concepts_in_a_specific_dictionary;
                                
                                
                                ShowLogs.LogPrint("\n", Color.blue);
                                // print out how many valid pre-annotated concepts we got in this directionary
                                info = "Loaded pre-annotated concepts from file "
                                        + filename + " successfully";
                                log.LoggingToFile.log(Level.INFO, info );
                                ShowLogs.printImportantInfoLog(info);

                                info = String.valueOf( amount_of_valid_preannotated_concepts_in_a_specific_dictionary )
                                     + " pre-annotatored concpet(s)";
                                log.LoggingToFile.log(Level.INFO, info );
                                ShowLogs.LogPrint( "[eHOST Info] - ", Color.green );
                                ShowLogs.LogPrint( String.valueOf( amount_of_valid_preannotated_concepts_in_a_specific_dictionary ), Color.red);
                                ShowLogs.LogPrint( " pre-annotatored concpet(s) .\n", Color.blue);
                                

                        }catch(Exception e){
                            String errorstr = "Failed to load the PreAnnotated Concept Lib: ["
                                        + filename +"]!!!"
                                        + "with separator: [" + separator + "]"
                                        + "with current step: [" + amount_of_valid_preannotated_concepts_in_a_specific_dictionary + "]"
                                        + "error details - " + e + "\n"
                                        ;
                            ShowLogs.LogPrint("\n", Color.yellow);
                            log.LoggingToFile.log(Level.SEVERE, errorstr );
                            ShowLogs.printErrorLog( errorstr );

                        }
                        
                        

                } // loop end for going over all dictionaries

                ShowLogs.LogPrint( "[eHOST Info] - ", Color.green );
                ShowLogs.LogPrint( "There are totally ", Color.blue);
                ShowLogs.LogPrint( String.valueOf( total_amount_of_valid_preannotated_concepts ), Color.red);
                ShowLogs.LogPrint( " pre-annotatored concpet(s) got input.\n", Color.blue);

                log.LoggingToFile.log(Level.SEVERE, "There are totally "
                        + String.valueOf( total_amount_of_valid_preannotated_concepts )
                        + " pre-annotatored concpet(s) got input.");

                return amount_of_valid_preannotated_concepts_in_a_specific_dictionary;
	}


    /**If a given word is exsited in the list of "Stop words", return false, otherwise return true.*/
    private static boolean ConceptValidity(String _rawConcept ){
        if ( _rawConcept == null )  return false;
        try{
            String invalid_concept, raw;
            raw = _rawConcept.trim().toLowerCase();
            int size = StopWords.size();
            for ( int i = 0; i < size; i++ ){
                if(StopWords.STOPWORDs.get(i) == null) continue;
                invalid_concept = StopWords.STOPWORDs.get(i).trim().toLowerCase();
                if( raw.compareTo( invalid_concept ) == 0 )
                        return false;
            }
            return true;
        } catch(Exception e) {
            log.LoggingToFile.log(Level.SEVERE,"4001: error: " + e.toString());
            return true;
        }

    }

        public static boolean addNewAnnotationIntoDictionary(String _dictionaryFileName,
                String _spanText, String _annotatedClass ){
            int size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
            String absoluteFileName = null, separator = null;
            for( int i = 0; i < size; i++){
                Object[] o = (Object[])env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                File f = new File( o[0].toString() );
                if (f.getName().toLowerCase().trim().compareTo( _dictionaryFileName.trim().toLowerCase() ) == 0){
                    absoluteFileName = o[0].toString().trim();
                    separator = o[3].toString().trim();
                    break;
                }
            }

            if(addOneLine(absoluteFileName, _spanText  + " " + separator + " " + _annotatedClass )){
                return true;
            }else{ return false;}
        }

        private static boolean addOneLine(String _absoluteDictionaryFileName, String _content){
            //new PrintWriter(File , true)
            try{
                PrintWriter pw  = new PrintWriter(
                        new BufferedWriter(
                        new FileWriter(_absoluteDictionaryFileName,true)),
                        true);
                pw.println("\n " + _content);
                pw.close();
                return true;
            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE,  "ConceptDictionaries - fail to add one line into dictionary." );
                log.LoggingToFile.log(Level.SEVERE, e.getMessage() );
                logs.ShowLogs.printErrorLog("ConceptDictionaries - fail to add one line into dictionary.");
                logs.ShowLogs.printErrorLog(e.toString());
                return false;
            }

        }
}

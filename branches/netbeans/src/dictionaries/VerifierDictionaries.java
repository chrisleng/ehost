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
import java.util.ArrayList;
import java.util.logging.Level;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class VerifierDictionaries
{

    public static ArrayList<VerifierDictionaryFormat> ConceptArray = new ArrayList<VerifierDictionaryFormat>();
    private static int amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;

    static
    {
    }

    /* 1.load all valid pre annotated concepts from file
     * 2.store to the static common data pool
     * the returned integer is the amount of valid concepts,
     * if return a '0', means fail to get the valid preannotated concept lib
     */
    public static int loadPreAnnotatedConcepts()
    {
        //reset counters

        // amount of concept dictionaries
        int amount_of_concept_dictionaries = env.Parameters.VERIFIER_DICTIONARIES.size();
        // if no dictionaries, Exit
        if (amount_of_concept_dictionaries <= 0)
        {
            return -1;
        }

        // **** <1> empty arraLyists before using
        ConceptArray.clear();


        // **** <2> load stop words //
        // stopwords: means the pre-annotated concept words will not be
        // stored into the memory if it appreared in the stopwords list

        //StopWords.LoadingStopWords();

        // **** <3> load pre-annotated concepts from dictionaries one by one
        for (int i = 0; i<amount_of_concept_dictionaries; i++)
        {
            VerifierDictionaryFormat CF = env.Parameters.VERIFIER_DICTIONARIES.get(i);
            String info = "Openning " + (i + 1) + " file of total"
                    + amount_of_concept_dictionaries + " pre-annotated concept dictionary";
            log.LoggingToFile.log(Level.INFO, info );
            ShowLogs.printImportantInfoLog( info );
            amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;

            //int total_valid_preannotated_concepts = 0;
            // **** <3.2> loading concepts from current dictionary
            try
            {
                CF.setWords(new ArrayList<String>());

                // **** <3.2.1> open the file
                // preannotatedConcepts lib files includes 2 columns, which can be
                // splitted by specific separator
                BufferedReader thisDictionary = new BufferedReader(new FileReader(CF.getFileName()));

                log.LoggingToFile.log(Level.INFO, "Loading pre-annotated concept dictionary ......");
                logs.ShowLogs.printImportantInfoLog("Loading pre-annotated concept dictionary ......");
                log.LoggingToFile.log(Level.INFO, "each point means 1000 entries(80K/line):");
                logs.ShowLogs.printImportantInfoLog("each point means 1000 entries(80K/line):\n");

                // **** <3.2.2> go over the directionary and load entires into array
                String st = thisDictionary.readLine();

                while (st != null)
                {

                    CF.addAnEntry(st);
                    //read next line in this concept directionary
                    st = thisDictionary.readLine();
                }

                thisDictionary.close();
                ConceptArray.add(CF);

                ShowLogs.LogPrint("\n", Color.blue);
                // print out how many valid pre-annotated concepts we got in this directionary
                ShowLogs.printImportantInfoLog("Loaded Verifier words from file "
                        + CF.getFileName() + " successfully");
                
                ShowLogs.LogPrint("[eHOST Info] - ", Color.green);
                log.LoggingToFile.log(Level.INFO, "Loaded Verifier words from file "
                        + CF.getFileName() + " successfully" );
            }
            catch (Exception e)
            {
                ShowLogs.LogPrint("\n", Color.yellow);
                ShowLogs.printErrorLog("Failed to load the Verifier Concept Lib ["
                        + CF.getFileName() + "]!!!");
                log.LoggingToFile.log( Level.SEVERE, "Failed to load the Verifier Concept Lib ["
                        + CF.getFileName() + "]!!!" );
                ShowLogs.printErrorLog("error details - " + e + "\n");
                log.LoggingToFile.log( Level.SEVERE, "error details - " + e );

            }
        } // loop end for going over all dictionaries



        return amount_of_valid_preannotated_concepts_in_a_specific_dictionary;


    }







}

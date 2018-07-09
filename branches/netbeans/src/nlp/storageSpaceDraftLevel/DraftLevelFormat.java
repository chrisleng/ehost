/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.storageSpaceDraftLevel;

import java.util.ArrayList;

/**
 *
 * @author Leng
 */
public class DraftLevelFormat {

        // paragraph index in whole memory, start from 1
        public int paragraphOverallIndex;

        // the text of this paragraph
        public String text_of_current_paragraph;
        // show which file does this paragraph belong to
        public String filename;

        // file index: which file is this paragraph belong to, start from 0
        public int fileindex;

        // show which paragraph in current file is current one
        public int index;

        // length of all previous paragraph before this one
        // (length included fullstop symbols)
        public int length_of_processed_paragraph;

        // flag for concept phrases
        // drafult value is                    '-99'
        // first character in this phrases is  '10'
        // other characters in this phrases is '11'
        public int[] flag_of_concept;

        // flag for negation
        // drafult value is                    '-99'
        // first character in this phrases is  '20'
        // other characters in this phrases is '21'
        public int[] flag_of_negation;

        public ArrayList ConceptDetails;
        public ArrayList SSNDetails;

        // all found negation words and their ranger information
        public ArrayList<Table_Negation> Negations = new ArrayList<Table_Negation>();
        public ArrayList<Table_DateTime> Dates = new ArrayList<Table_DateTime>();

        // array list of found terms by custom regular expression to this paragraph
        public ArrayList<Table_CustomRegex> CustomRegex
                = new ArrayList<Table_CustomRegex>();

        // arraylist of all experiencer found by ConText Algorithm
        public ArrayList<Table_Experiencer> Experiencer
                = new ArrayList<Table_Experiencer>();

        // arraylist of all temporality information to each sentence by ConText Algorithm
        public ArrayList<Table_Temporality> Temporality
                = new ArrayList<Table_Temporality>();


        

}

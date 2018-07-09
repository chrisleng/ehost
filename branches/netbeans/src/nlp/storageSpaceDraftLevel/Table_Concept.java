/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.storageSpaceDraftLevel;

import java.util.Vector;

/**
 *
 * @author Leng
 */
public class Table_Concept {
        public String concept_text;
        //int span_start_in_the_sentence;
        //int span_end_in_the_sentence;
        public int span_start_in_the_paragraph;
        public int span_end_in_the_paragraph;
        public int span_start_in_file;
        public int span_end_in_file;
        public int weight;

        // for searching the announced name of the concept
        // use these index to find the comment in the array in Dictionary of concept
        public int found_by_which_dictionary = -1; // start from 0
        public int found_by_which_entry = -1; // start from 0

        //** for output *//
        // announced name
        public String comment;
        // mentionID, usually got assignated in postprocess.assignMentionID.java
        public int mentionID = 0;

        public int uniqueindex = -1;


        // following two variables are only for QNLP to record annotation's classname
        // if already found concept class
        public boolean foundclassname = false;
        public String classname = null;
        
        // attributes of this concept, such as negation, temporality, experiencer
        public Vector<Table_ConceptAttributes> attributes
                = new Vector<Table_ConceptAttributes>();

}


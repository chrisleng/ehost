/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaries;

import java.util.ArrayList;

/**
 *
 * @author Chris
 */

public class ConceptDictionaryFormat {

        public ArrayList<String> preprocessed_preannotated_Concept
                = new ArrayList<String>(); // pre-processed for regular expression
        public ArrayList<String> Comment
                = new ArrayList<String>();
        public int weight = -1;
	public int size = -1;
}

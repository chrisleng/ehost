/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.algorithm.QNLP;

/**
 *
 * @author Chris
 */
public class DictionaryFormat {
    String term;
    String category;

    public DictionaryFormat(){}

    public DictionaryFormat(String term, String category){
        this.term = term;
        this.category = category;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation.pairs;

/**
 *
 * @author leng
 */
public class Entry {
    /**term: such as "he", "she", "his", "their", "the patient"*/
    public String term;
    /**class type of this term, such as term "he" is belong to class "person".*/
    public String type;

    public String subtype;

    /**constructure*/
    public Entry(String _term, String _type, String _subtype){
        this.term = _term;
        this.type = _type;
        this.subtype = _subtype;
    }

    @Override
    public String toString(){
        return term;
    }
}
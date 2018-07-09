/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package commons;

/**
 *
 * @author Jianwei Leng
 */
public class Constant {
    public final static int POSSIBLE_NEGATION = 1;
    public final static int NEGATED_NEGATION = 2;

    // to a concept, it may have several kind of attributes
    // such as negation, experiencer, tempotality
    public enum attributeType{
        negation, experiencer, temporality
    };

    // attributes of temporality
    // 1st created for ContextAlgorithm.java on June 7, 2010
    public enum temporality{
        recent, hypothetical, historical
    };
}



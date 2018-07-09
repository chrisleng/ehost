/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationBuilder;

/**
 *
 * @author Chris
 */
public class Term {
    public String termtext;
    /**30 characts before and after the term.*/
    public String surroundtext;
    public int start;
    public int end;

    public int start_in_surroundtext;
    public int end_in_surroundtext;

    public boolean selected = true;
    
    /**to each term we found by oracle function, we need to check whether it 
     * has been annotated yet or not. If this annotation is annotated, then
     * this flag should be "true", "false" for others. 
     * 
     * And if this flag is "false", it should have an unchecked checkbox while 
     * we list this term in the list.
     */
    public boolean isExists = false;
}

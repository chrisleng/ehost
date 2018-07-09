/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations;

/**
 * To each annotation you imported or created, an unique integer is assignated
 * for annotation indentification.
 *
 * @author Jianwei Leng 2010-06-28
 */
public class AnnotationIndex {

    /**unique annotation index*/
    protected static int annotaitonindex = 1;

    /**assignate me a new annotation index number*/
    public static int newAnnotationIndex(){
        annotaitonindex++;
        return annotaitonindex;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.conflicts;

/**
 *
 * @author leng
 */
public class Diff{

    public int spanstart;
    public int spanend;
    public String annotationtext;
    public int difftype;

    public Diff(){

    }

    public Diff(String annotationtext, int spanstart, int spanend, int difftype){
        this.spanstart = spanstart;
        this.spanend = spanend;
        this.annotationtext = annotationtext;

        /**
         * difference type:
         * 1: same span 2: crossed span
         */
        this.difftype = difftype;
    }

}

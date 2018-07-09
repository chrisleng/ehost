/**
 * this class is a structure used for remove duplicate annotations by method of
 * "removeDuplicates" in class of "Depot".
 */

package resultEditor.annotations;

import java.util.Vector;


/**
 * This is the format for single item 
 *
 * @author Chris Leng, 5/25/2010
 */
public class DupilcateAndGhost {

    /**indicate where is this duplicate annotaiton or ghost annotation*/
    public String filename;
    /**the unique index of this annotation*/
    public int uniqueindex;
    
    /**the marked annotation; This is the first annotation if there are multiple
     * annotation duplicates on one span.
     */
    public Annotation referenceAnnotation = null;
    // public int spanstart, spanend;
    public SpanSetDef spanset;
    /**duplicates which are same to the reference Annotation*/
    public Vector<Annotation> duplicates = new Vector<Annotation>();

    /**this is a flag indicates whether current */
    public boolean selected = true;

    /**indicate what is this record: duplicates, or ghost annotation; and what
     * kind of ghost annotation it is.
     *
     * type=1   :   duplicates
     * type=4   :   classless
     * type=5   :   spanless
     * type=6   :   out of range
     */
    public int type=0;


    /**constructor method*/
    public DupilcateAndGhost(){}
    /*public DupilcateAndGhost(String filename, int uniqueindex){
        this.filename = filename;
        this.uniqueindex = uniqueindex;
    }*/

    /**constructor method*/
    public void addDuplicate(String filename, Annotation _referenceAnnotation, Annotation _duplicate){
        if((_referenceAnnotation==null)||(_duplicate==null)||(filename==null)||(filename.trim().length()<1))
            return;

        this.filename = filename;
        this.uniqueindex = _referenceAnnotation.uniqueIndex;
        referenceAnnotation = _referenceAnnotation;
        spanset = _referenceAnnotation.spanset;
        type = 1; // duplicates
        duplicates.add(_duplicate);
    }

    void addClassless(String _filename,Annotation ann) {
        if(ann==null)
            return;
        this.referenceAnnotation = ann;
        this.filename = _filename;
        type = 4;
    }

    void addSpanless(String _filename, Annotation ann) {
        if(ann==null)
            return;
        this.referenceAnnotation = ann;
        this.filename = _filename;
        type = 5;
    }

    void addOutOfRange(String _filename, Annotation ann) {
        if(ann==null)
            return;
        this.referenceAnnotation = ann;
        this.filename = _filename;
        type = 6;
    }
}

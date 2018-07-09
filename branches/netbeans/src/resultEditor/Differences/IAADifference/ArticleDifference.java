/*
 * ArticleDifference.java
 * 2011-10-10 16:12
 *
 */

package resultEditor.Differences.IAADifference;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * This is used to record all annotation differences to a specific document.
 * To a designated document, we record intervals which cover all these
 * annotations difference.
 *
 * @author Jianwei Chris leng, 2011-10-10 16:12
 */
public class ArticleDifference {

    /**file name of current document. We will record intervals to cover all
     * annotations differences for this specific document later.*/
    public String filename = null;

    /**space that used to store all interval that covered all annotation differences*/
    public Vector<Difference> differences = new Vector<Difference>();


    /**constructor
     *
     * @param   filename
     *          file name of current document. We will record intervals to
     *          cover all annotations differences for this specific document
     *          later.
     */
    public ArticleDifference(String filename) throws Exception{

        // check validity
        if((filename==null)||(filename.trim().length()<1))
            throw new Exception("1110101612::null or empty file name!");

        // after checking validity, start to create the instance
        this.filename = filename.trim();
    }

    public void add(int spanStart, int spanEnd) throws Exception{
        if((spanStart==0)&&(spanEnd==0))
            throw new Exception("1110132051:: invalid span startpoint and endpoint: (0,0) !");

        if((spanStart<0)||(spanEnd<0))
            throw new Exception("1110101617:: invalid span startpoint or endpoint: <0 !");

        // a flag used to tell system whether the span is existing or included
        // yet
        boolean found = false;

        for( Difference difference : differences ){
            
            if(difference==null)
                throw new Exception("1110101945:: found a invalid(null) value");

            // if this difference already included this span, we do nothing;
            // Otherwise, we add this interval
            if( compare2Spans(spanStart, spanEnd, difference) ){
                found = true;
                break;
            }
            // Otherwise, we add this interval                        
        }

        // if no existing interval can cover this new span, it should be
        // recorded.
        if( found == false ){
            differences.add( new Difference( spanStart, spanEnd) );
        }
    }
    
    /**sort these differences by their spans*/
    public void sort(){
        Comparator comparator = new SpanComparator();
        Collections.sort( this.differences, comparator );
    }

    
    /**this function is used to compare 2 span intervals and check whether the
     * first interval has been included into the second span.
     *
     * @param   difference
     *          it's a span, a instance of class "difference", which also
     *          contains a span start and end in formation of integer.
     *
     * @param   spanstart2
     *          the start point of the first span
     *
     * @param   spanend2
     *          the end point of the first span
     *
     * @return  true: if the span has been inclued in the given difference
     */
    public boolean compare2Spans(int spanstart2, int spanend2, Difference difference) throws Exception{
        try{
            
            if((difference.differenceStart<=spanstart2)&&(difference.differenceEnd>=spanend2))
                return true;
            
            if((difference.differenceStart>=spanstart2)&&(difference.differenceEnd<=spanend2))
                return true;
            
            
                return false;
                
        }catch(Exception ex){
            throw new Exception("1110110001::fail to compare two spans.");
        }       
    }


}

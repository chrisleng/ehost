/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.Differences.IAADifference;

/**
 * The structure of a recorded annotation differences. In this position, there
 * have at least 2 annotations and they have some differences in their 
 * annotation classes, attributes, or relationships. We record the span which
 * covered these annotations.
 *
 *
 * @author Jianwei Chris Leng, Oct-10, 2011
 */
public class Difference implements Comparable{
    
    /**span start of the interval which covered these different annotations.
     * the default "-1" means it's a wrong start point.*/
    public int differenceStart = -1;

    /**span end of the interval which covered these different annotations.
     * the default "-1" means it's a wrong end point.*/
    public int differenceEnd = -1;


    /**constructor*/
    public Difference(int differenceStart, int differenceEnd) throws Exception{
        if((differenceStart<0)||(differenceEnd<0))
            throw new Exception("1110101607::wrong span start/end point: value<0");

        if(differenceEnd<differenceStart)
            throw new Exception("1110101608::wrong span start/end point: end<start");

        // after checking these values, we start to create instance of this class
        this.differenceEnd = differenceEnd;
        this.differenceStart = differenceStart;
    }

    @Override
    public int compareTo(Object o) {
        Difference anotherDifference = (Difference) o;
        int result = differenceStart < anotherDifference.differenceStart? 1 : 0;
        if( differenceStart == anotherDifference.differenceStart  )
            result = differenceEnd < anotherDifference.differenceEnd? 1 : 0;
        return result;
        
    }
    
}

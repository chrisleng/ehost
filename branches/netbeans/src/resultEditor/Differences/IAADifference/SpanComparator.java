package resultEditor.Differences.IAADifference;

import java.util.Comparator;

/**
 * Comparator that can compare two instance of "difference", and found which one 
 * is in front of the other one.
 * 
 * @author Chris Leng @ June 26, 2012
 */
public class SpanComparator implements Comparator{
    @Override
    public int compare(Object o1,Object o2) {        
            
        Difference ac1 = (Difference) o1;
        Difference ac2 = (Difference) o2;

        if( ac1.differenceStart < ac2.differenceStart )
            return 0;
        else if( ac1.differenceStart > ac2.differenceStart )
            return 1;
        
        if( ac1.differenceEnd < ac2.differenceEnd )
            return 1;
        else if( ac1.differenceEnd > ac2.differenceEnd )
            return 0;
        
        return 1;
       
    }
}

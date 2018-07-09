/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.conflicts;

import java.util.Vector;
import resultEditor.annotations.Annotation;

/**
 *
 * @author Kyle
 */
public class spanOverlaps
{

    private Vector<Annotation> overlapping = new Vector<Annotation>();

    /**
     * Constructor
     * @param overlaps - All annotations involved in the overlappinp conflict
     */
    public spanOverlaps(Vector<Annotation> overlaps)
    {
        overlapping.addAll(overlaps);
    }

    /**
     * Default constructor
     */
    public spanOverlaps(){};

    public Vector<Annotation> getInvolved()
    {
        return overlapping;
    }
    /**
     * String representation of this object.
     * @return
     */
    @Override
    public String toString()
    {
        return overlapping.get(0).annotationText;
        /*//GET THE MINIMUM START AND MAXIMUM END OF ALL ANNOTATIONS
        //Start out assuming the first annotation has the minimum start and maximum end.
        int min = overlapping.get(0).spanstart;
        int max = overlapping.get(0).spanend;

        //Loop through all annotations finding the minimum start and maximum end.
        for (Annotation annotation : overlapping)
        {
            min = Math.min(min, annotation.spanstart);
            max = Math.max(max, annotation.spanend);
        }

        //Build a string for the entire span(from minimum start to maximum end)
        String startToEnd = "";
        boolean notDone = true;

        //Keep looping until we've reached the maximum end.
        while(notDone)
        {
            //Loop through all Annotations to pull out parts of the final string
            for(Annotation annotation: overlapping)
            {
                //If this annotatoin starts before(or equal) to the current min and ends after the current min.
                //Then it has part of the string that we need so add it to our return string.
                if(annotation.spanstart <= min && annotation.spanend > min)
                {
                    startToEnd += annotation.annotationText.substring(min-annotation.spanstart);

                    //Our new min is the end of this annotation
                    min = annotation.spanend;

                    //Check to see if we're done.
                    if(min == max)
                        notDone = false;
                }
            }
        }
        //Add the total number of annotations in this span.
        startToEnd += "(" + overlapping.size() + " Annotations)";

        //Return the string representation.
        return startToEnd;
        *
        */
         
    }

    /**
     * Check to see if this actually is a span overlap
     * @return
     */
    public boolean isGood()
    {
        return(ensureRealOverlap());
    }

    /**
     * Make sure that there are annotations that don't have the same start and end.
     * @return
     */
    private boolean ensureRealOverlap()
    {
        for(Annotation annotation: overlapping)
        {
            for(Annotation compareTo: overlapping)
            {
                if(compareTo.spanset.isDuplicates( annotation.spanset ))
                    return true;
            }
        }
        return false;
    }
}

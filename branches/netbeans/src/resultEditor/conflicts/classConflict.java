/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.conflicts;

import resultEditor.annotations.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**  This class represents a conflict object.  A conflict consists of two or more Annotations which
 *  have different classes but the same text.  This class should contain ALL ANNOTATIONS that are involved
 * in the conflict.  So if there are 12 annotations with the same text marked as test and one marked
 * as treatment then all 13 annotations should be added to the 'conflict' object.
 *
 * @author Kyle
 */
public class classConflict
{
    //Annotations from the file we are currently working with that are involved with this conflict
    private ArrayList<Annotation> inSource = new ArrayList<Annotation>();

    //Annotations from other files in the workset that are involved with this conflict
    private ArrayList<Annotation> notSource = new ArrayList<Annotation>();

    //The text for this conflict
    private String text;
    /**
     * Constructor
     * @param fromSource - the first Annotation from the source file that is in conflict.
     */
    public classConflict(Annotation fromSource)
    {
        inSource.add(fromSource);
        text = fromSource.annotationText;
    }
    public classConflict(){};
    /**
     * Add an Annotation that belongs to this conflict that is from the source file.
     * @param fromSource
     */
    public void addSource(Annotation fromSource)
    {
        if(!inSource.contains(fromSource))
            inSource.add(fromSource);
    }
    /**
     * Add an annotation that belongs to this conflict that is not from the source file.
     * @param notFromSource
     */
    public void addNotSource(Annotation notFromSource)
    {
        if(!notSource.contains(notFromSource))
            notSource.add(notFromSource);
    }

    /**
     * Get all Annotations involved in this class conflict.
     * @return - involved Annotations
     */
    public Vector<Annotation> getInvolved()
    {
        Vector<Annotation> toReturn = new Vector<Annotation>();
        toReturn.addAll(inSource);
        toReturn.addAll(notSource);
        return toReturn;
    }
    /**
     * Output a string representation of this object.
     * @return - A string containing key information about this object.
     */
    public String toString()
    {
        HashMap<String, Integer> uniqueClasses = new HashMap<String, Integer>();
        ArrayList<Annotation> all = new ArrayList<Annotation>();
        all.addAll(inSource);
        all.addAll(this.notSource);
        for (Annotation annotation : all)
        {
            if (!uniqueClasses.containsKey(annotation.annotationclass))
            {
                uniqueClasses.put(annotation.annotationclass, 1);
            }
            else
            {
                Integer original = uniqueClasses.get(annotation.annotationclass);
                uniqueClasses.remove(annotation.annotationclass);
                original++;
                uniqueClasses.put(annotation.annotationclass, original);
            }
        }
        String toReturn = "<html>" + inSource.get(0).annotationText;
        toReturn += "<br><font color = \"gray\">";
        for (String key: uniqueClasses.keySet())
        {
            toReturn += key;
            toReturn += "(" + uniqueClasses.get(key) + ") ";
        }
        toReturn += "</font></html>";
        return toReturn;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }
}

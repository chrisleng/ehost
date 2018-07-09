/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotate.format;

/**
 *
 * @author Kyle
 */
public class dictEntry implements Comparable
{
    String theText;
    String theClass;
    public dictEntry(String text, String aClass)
    {
        theText = text;
        theClass = aClass;
    }
    public String toString()
    {
        return theText;
    }
    public boolean equals(Object c)
    {
        if(!c.getClass().isInstance(this))
            return false;
        dictEntry passed = (dictEntry)c;
        if(!this.theText.equals(passed.theText))
            return false;
        if(!this.theClass.equals(passed.theClass))
            return false;
        return true;

    }
    public int compareTo(Object f)
    {
        if(!f.getClass().isInstance(this))
            return -1;
        if(this.equals(f))
            return 0;
        dictEntry passed = (dictEntry)f;
        String original = this.theText + this.theClass;
        String passedIn = passed.theText + passed.theClass;
        return(original.compareTo(passedIn));
    }

}

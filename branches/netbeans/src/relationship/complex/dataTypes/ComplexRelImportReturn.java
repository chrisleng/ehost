/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relationship.complex.dataTypes;

import java.util.TreeSet;

/**
 * This class will be used when Importing Relationships from an exisiting source
 * of annotations.  This Class is similar to ComplexRel, but allows multiple regular
 * expressions to be stored within one Object.
 * @author Kyle
 */
public class ComplexRelImportReturn implements Comparable
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private String name;
    private TreeSet<String> regexes;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor for a relationship for an import return
     * @param relName - the name of the relationship
     */
    public ComplexRelImportReturn(String relName)
    {
        name = relName;
        regexes = new TreeSet<String>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    /**
     * Add the allowed regular expressions to the list of regular expressions.
     * @param toAdd - the allowed regular expressions
     */
    public void addRegex(String toAdd)
    {
        regexes.add(toAdd);
    }
    /**
     * Get the allowed regular expresions
     * @return - allowed regex
     */
    public TreeSet<String> getRegex()
    {
        return regexes;
    }
    /**
     * Get all of the Class Strings that are in this complex import.
     * so if the relationship: (Test)->(Problem)->(Treatment) is in the 
     * schema the return value will include:TestProblemTreatment, This will
     * be used to see if a regular expression matches the class
     * @return - String throw up
     */
    public TreeSet<String> getClassesConcatenated()
    {
        TreeSet<String> toReturn = new TreeSet<String>();
        for(String s: regexes)
        {
            String temp = s.replaceAll("\\(", "");
            temp = temp.replaceAll("\\)","");
            toReturn.add(temp);
        }
        return toReturn;
    }
    /**
     * Get the name of this relationship
     * @return - the name
     */
    public String getName()
    {
        return name;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="For Comparable Interface">
    /**
     * Compare this object with another object.
     * @param toCompare - the object to compare to
     * @return - 0 if equal, >0 if the argument is greater than this, <0 if the argument
     * is less than this.
     */
    public int compareTo(Object toCompare)
    {
        String thisCompare = "";
        String otherCompare = "";
        thisCompare +=name;
        for(String s: regexes)
            thisCompare += s;
        if(!toCompare.getClass().isInstance(this))
            return -1;

        ComplexRelImportReturn compareAgainstRel = (ComplexRelImportReturn)toCompare;

        otherCompare += compareAgainstRel.name;
        for(String s: compareAgainstRel.getRegex())
            otherCompare += s;
        return(thisCompare.compareTo(otherCompare));

    }
    //</editor-fold>
}

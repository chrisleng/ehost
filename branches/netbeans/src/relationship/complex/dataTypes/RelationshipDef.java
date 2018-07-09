package relationship.complex.dataTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import relationship.simple.dataTypes.AttributeList;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationRelationshipDef;

/**
 * This class represents a Complex Relationship in the Schema.  A Complex Relationship
 * is defined by a name and a regular expression that describes what classes it
 * allows to be in it.
 *
 * <pre>
 * For Example:
 *      A relationship that would show all of the Problems that a Test revealed
 *      might be:
 *          regular expression: (Test)(Problem)+
 *          name: Reveals
 *
 * </pre>
 *
 * @author Kyle
 */
public class RelationshipDef implements Comparable
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    private String name;
    private String allowed;
    private boolean unique = true;
    
    /**attributes on this relationship.*/
    private AttributeList attributes = new AttributeList();
    
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Regular constructor.
     * @param name - Name of the relationship
     * @param allowed - Regular Expression that describes the allowed classes
     * in this ComplexRelationship type.
     */
    public RelationshipDef(String name, String allowed)
    {
        this.name = name;
        this.allowed = allowed;
    }
    /**
     * Constructor for an Relationship that does not have an allowed regular expresion'
     * yet.
     *
     * @param name - Name of the relationship
     * @param wasted - whatever you want
     */
    public RelationshipDef(String name, boolean wasted)
    {
        this.name = name;
    }
    /**
     * Constructor to use when reading from the configure file.  The Line that is
     * read in should be sent to this constructor.
     * @param fromGetWriteString - the String from the configure file.
     */
    public RelationshipDef(String fromGetWriteString)
    {
        String[] firstSplit = fromGetWriteString.split("----");
        this.name = firstSplit[0];
        this.allowed = firstSplit[1];
    }

    //</editor-fold>

    public AttributeList getAttributes(){
        return attributes;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Check that the annotations fit within the given Chain
     * @param annots
     * @return - True if the Annotations are contained in this Complex Relationship,
     * false otherwise.
     */
    public boolean checkContains(ArrayList<Annotation> annots)
    {
        String aString = allowed;
        {
            Pattern pattern = Pattern.compile(aString);
            String s = "";
            for(Annotation annot: annots)
            {
                s += annot.annotationclass;
            }
            Matcher matcher = pattern.matcher(s);

            if(matcher.matches())
                return true;
        }

        return false;
    }
    /**
     * Check to see if a given ArrayList of classes can be represented with the
     * allowed regular expression for this Complex Relationship.
     * @param strings - The list of classes from the Annotations.
     * @return - True if the list of classes is contained in this Complex Relationship,
     * false otherwise.
     */
    public boolean checkContainsClasses(ArrayList<String> strings)
    {
        String toTest = "";
        for(String s: strings)
        {
            toTest+=s;
        }
        Pattern pattern = Pattern.compile(allowed);
        Matcher matcher = pattern.matcher(toTest);
        if(matcher.matches())
            return true;
        return false;
    }
    /**
     * Check that the annotations fit within the given Chain, ONLY USE THIS IF YOU
     * ARE SURE THAT THE ANNOTATION CLASS HAS BEEN INITIALIZED PROPERLY FOR eCOMPLEX
     * OBJECTS.
     *
     * @param source - the source of the relationship
     * @param annots  - the annotations that the source annotation is linked to.
     * @return - True if the given source, and other annotations are contianed
     * in this Complex Relationship.
     */
    public boolean checkContainsComplex(Annotation source, Collection<AnnotationRelationshipDef> annots)
    {
        String aString = allowed;
        {
            Pattern pattern = Pattern.compile(aString);
            String s = "";
            s += source.annotationclass;
            for(AnnotationRelationshipDef annot: annots)
            {
                //Annotation linkAnnot = article.getAnnotationByMention(link.mention);
                s += annot.annotationClass;
            }
            Matcher matcher = pattern.matcher(s);

            if(matcher.matches())
                return true;
        }

        return false;
    }
    /**
     * Return a string representation for this object
     * @return - a string representation of this object.
     */
    public String toString()
    {
        return name + " <" + allowed + ">";
    }
    /**
     * Return the string as written to the configure file
     * @return - the string to be written to a configure file.
     */
    public String getWriteString()
    {
        String toReturn = "";
        toReturn += this.name;
        toReturn += "----";
        toReturn += allowed;
        toReturn += "----";
        return toReturn;

    }
    //</editor-fold>

    
    
    

    //<editor-fold defaultstate="collapsed" desc="Getters And Setters">
    /**
     * Get the allowed regular expression. 
     * @return - return the allowed classes in the form of a regular expression.
     */
    public String getAllowed()
    {
        return allowed;
    }
    /**
     * Set the allowed regular expressions
     * @param name
     */
    public void setAllowed(String name)
    {
        this.allowed = name;
    }
    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return the unique
     */
    public boolean isUnique()
    {
        return unique;
    }

    /**
     * @param unique the unique to set
     */
    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }
    //</editor-fold>

    /**
     * Compare this object with another object to check for less than greater than
     * or equality.
     * @param obj - the object to compare against
     * @return - 1 if obj is greater, 0 if it's equal, -1 if it is less.
     */
    @Override
    public int compareTo(Object obj) {
        if(obj == null)
            return -1;
        
        if( obj instanceof RelationshipDef ){               
            return this.getWriteString().compareTo( ((RelationshipDef)obj).getWriteString());
        }
        
        return -1;
    }

    
    
}

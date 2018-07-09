/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations;

import relationship.complex.dataTypes.RelationshipDef;
import resultEditor.relationship.iListable;
import java.util.Vector;

/**
 *
 * @author Jianwei Leng, 2010-06-28
 */
public class AnnotationRelationship implements iListable{

    /** The name of the relationship */
    public String   mentionSlotID;
    /**
     * The Annotations that are in the relationship(the 'source' annotation will
     * not be in this list).
     */
    public Vector<AnnotationRelationshipDef> linkedAnnotations = new Vector<AnnotationRelationshipDef>();
    
    
    /**Attributes of this annotation*/
    public Vector<AnnotationAttributeDef> attributes = new Vector<AnnotationAttributeDef>();
    /**
     * The currently selected Relationship name (mentionSlotID)
     */
    private String current;
    /**
     * Constructor for a Complex Relationship - which is a relationship between at
     * least two annotations.
     * @param name - the name of the relationship
     */
    public AnnotationRelationship(String name) {
        mentionSlotID = name;
    }

    public AnnotationRelationship copy() {
        AnnotationRelationship toReturn = new AnnotationRelationship(mentionSlotID);
        for(AnnotationRelationshipDef complex: linkedAnnotations)
            toReturn.addLinked(complex);
        return toReturn;
    }
    /**
     * Add a linked annotation in the form of an eComplex object.
     * @param complex - the eComplex object to link with this relationship
     */
    public void addLinked(AnnotationRelationshipDef complex) {
        getLinkedAnnotations().add(complex);
        if(getLinkedAnnotations().size() == 1) {
            current =linkedAnnotations.get(0).linkedAnnotationText;
        }
    }
    /**
     * Get the write form for this object; Make sure to do any changes done for displaying
     * the annotation name/ involved attributes.
     * @return - the write form of this object.
     */
    public Object getWriteForm() {
        mentionSlotID = getMentionSlotID().replace("<html><font color = \"red\">", "");
        mentionSlotID = getMentionSlotID().replace("</font></html>", "");
        return this;
    }
    /**
     * Get the selected String at a given column
     * @param which - the column
     * @return - the selected string
     */
    public String getSelectedItem(int which) {
        if(which == 0)
            return getMentionSlotID();
        if(which == 1) {
            return current;
        }
        return null;
    }
    /**
     * Set the selected string for a given column.
     * @param which - the column number to set
     * @param value - the value to set the string to.
     */
    public void setString(int which, String value) {
        if(which ==0)
            mentionSlotID = value;
        if(which == 1)
            current = value;

    }

    /**
     * Get the allowable strings for a given column
     * @param which - the number of the column
     * @return - the allowable strings for a given column
     */
    public Vector<String> getStrings(int which)
    {
        Vector<String> toReturn = new Vector<String>();
        if(which == 0)
        {
            toReturn.add(getMentionSlotID());
            boolean regular = false;
            for(RelationshipDef rel: env.Parameters.RelationshipSchemas.getRelationships())
            {
                String s = rel.getName();
                if(!toReturn.contains(s))               
                    toReturn.add(s);                
                else
                    regular = true;
                
            }

            if(!regular) {
                String test = toReturn.get(0);
                test = "<html><font color = \"red\">" + test +"</font></html>";
                toReturn.set(0, test);
            }

            return toReturn;
        }

        if(which == 1) {
            for(AnnotationRelationshipDef complex: getLinkedAnnotations())
            {
                toReturn.add(complex.linkedAnnotationText);
            }
            return toReturn;
        }
        return toReturn;

    }
    /**
     * Return true if the given column needs a combo box.
     * @param which - the column number
     * @return - return true if the given column needs a combo box.
     */
    public boolean needsCombo(int which) {
        return true;
    }
    /**
     * Return true if the given column can be modified
     * @param which - the column number
     * @return - return true if the given column can be modified.
     */
    public boolean isModifiable(int which) {
        return true;
    }
    /**
     * Return true if the given column exists in this listable object
     * @param which - the column
     * @return - true if the column exists in this listable.
     */
    public boolean hasEntry(int which) {
        if(which == 0 || which == 1)
            return true;
        return false;
    }
    public boolean couldDelete() {
        return true;
    }

    /**
     * @return the mentionSlotID
     */
    public String getMentionSlotID() {
        return mentionSlotID;
    }
    public void setMentionSlotID(String name)
    {
        mentionSlotID = name;
    }

    /**
     * @return the linkedAnnotations
     */
    public Vector<AnnotationRelationshipDef> getLinkedAnnotations() {
        return linkedAnnotations;
    }
    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        result.append(" \"" + this.getMentionSlotID() + "\"" + NEW_LINE);
        for(AnnotationRelationshipDef complex:linkedAnnotations)
        {
            result.append( "         " + complex.linkedAnnotationText + NEW_LINE);
        }
        return result.toString();
        
    }

    /**get a deep copy/clone of current instance of annotation relationship.*/
    public AnnotationRelationship getCopy() {
        AnnotationRelationship toReturn = new AnnotationRelationship(mentionSlotID);
        
        // copy attributes of this relationship
        {
            if( this.attributes != null ) {
                for( AnnotationAttributeDef att : this.attributes ){
                    if( att == null )
                        continue;
                    AnnotationAttributeDef newatt = new AnnotationAttributeDef();
                    newatt.name = att.name;
                    newatt.value = att.value;
                    toReturn.attributes.add( newatt );
                }
            }
        }
        
        // copy relationship
        for(AnnotationRelationshipDef complex: linkedAnnotations){
            if( complex == null )
                continue;
            AnnotationRelationshipDef newcomplex = new AnnotationRelationshipDef();
            newcomplex.annotationClass = complex.annotationClass;
            newcomplex.linkedAnnotationIndex = complex.linkedAnnotationIndex;
            newcomplex.linkedAnnotationText = complex.linkedAnnotationText;
            newcomplex.mention = complex.mention;
            
            toReturn.linkedAnnotations.add( newcomplex );
        }
        
        return toReturn;
    }
}

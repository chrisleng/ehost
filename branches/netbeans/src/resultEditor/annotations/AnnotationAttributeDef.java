/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotations;

import resultEditor.relationship.iListable;
import relationship.simple.dataTypes.AttributeSchemaDef;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Chris
 */
/**normal relationship of this annotation, normal is compare to complex relationship. */
public class AnnotationAttributeDef implements iListable
{
    /**
     * The name of the annotation attribute.
     */
    public String name;
    /**
     * The values that this annotation attribute currently has
     */
    public String value;
    /**
     * The currently selected string in the List of Allowable Strings from
     * the Simple Schema.
     */
    private String currentlySelected = "";
    /**
     * If this is a repeat of a former attribute
     */
    public boolean deadName = false;
    /**
     * Default constructor
     */
    public AnnotationAttributeDef(){};
    /**
     * Constructor
     * @param name - the name of the relationship
     */
    public AnnotationAttributeDef(String name)
    {
        this.name = name;
        ArrayList<String> allowed = env.Parameters.AttributeSchemas.getAllowed(name);
        if(allowed.size() != 0)
            currentlySelected = allowed.get(0);
    }
    /**
     * Constructor
     * @param name - name of the relationship
     * @param value - value of the relationship
     */
    public AnnotationAttributeDef(String name, String value)
    {
        this.name = name;
        this.value = value;
    }
    /**
     * Copy this object.
     * @return - a copy of this object that doesn't share any references.
     */
    public AnnotationAttributeDef copy()
    {
        AnnotationAttributeDef toReturn = new AnnotationAttributeDef();
        toReturn.name = name;
        toReturn.value = value;
            
        return toReturn;

    }
    /**
     * Get a copy of this object that can be written back to an Annotation.  This will
     * be called after the Annotation Attributes have been edited so it can be written
     * back to the annotation.
     * @return - A normal relationship ready to be written to an Annotation
     */
    public Object getWriteForm()
    {
        value = currentlySelected;
        return this;
    }
    /**
     * The currently selected String(used for columns in a JTable).
     * @param which - the column
     * @return - the currently selected item
     */
    public String getSelectedItem(int which)
    {
        if(currentlySelected.equals(""))
            currentlySelected = value;
        if (which == 0)
        {
            return name;
        }
        return currentlySelected;
    }
    /**
     * Set the string at a given column.(Used for editing within a JTable)
     * @param which - the column
     * @param value - the new value
     */
    public void setString(int which, String value)
    {
        if (which == 0)
        {
            if(!name.equals(value))
            {
                ArrayList<String> test = env.Parameters.AttributeSchemas.getAllowed(value);
                if(test.size() != 0)
                {
                    currentlySelected = test.get(0);
                    value = test.get(0);
                }
            }
            name = value;
        }
        if (which == 1)
        {
            value = value.replace("<html><font color = \"red\">", "");
            value = value.replace("</font></html>", "");
            currentlySelected = value;
        }
    }
    /**
     * Get the Allowed Strings for a given column.
     * @param which - the column.
     * @return - Return any Strings which can be in a given column.  If needsCombo(which)
     * returns false then only one String will be returned.
     */
    
    public Vector<String> getStrings(int which)
    {
        Vector<String> toReturn = new Vector<String>();

        if (which == 0)
        {
            boolean returnAll = false;
            for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes())
            {
                if (att.getName().equals(name))
                {
                    returnAll = true;
                }

            }
            if(returnAll)
            {
                for(AttributeSchemaDef att: env.Parameters.AttributeSchemas.getAttributes())
                    toReturn.add(att.getName());
            }
            else
            {
                toReturn.add(name);
            }
            return toReturn;
        }
        for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes())
        {
            if (att.getName().equals(name))
            {
                
                    if(!att.getAllowedEntries().contains( value))
                    {
                        toReturn.add("<html><font color = \"red\">" + value + "</font></html>");
                    }
                
                for (String allowed : att.getAllowedEntries())
                {
                    toReturn.add(allowed);
                }
                
                return toReturn;
            }

        }
        return toReturn;

    }
    /**
     * Used for editing within a JTable, If the given column needs a combo box
     * then return true, otherwise return false.
     * @param which- the column
     * @return  - true if needs a combo box, false otherwise.
     */
    public boolean needsCombo(int which)
    {
        for(AttributeSchemaDef att: env.Parameters.AttributeSchemas.getAttributes())
        {
            if(att.getName().equals(name))
                return true;
        }
        return false;
    }
    /**
     * 
     * @param which
     * @return
     */
    public boolean isModifiable(int which)
    {
        if(needsCombo(which))
            return true;
        return false;
    }
    public boolean hasEntry(int which)
    {
        if(which >1 || which< 0)
            return false;
        return true;
    }
    public boolean couldDelete()
    {
        return true;
    }
    
    /*
    /**Prepare the list that will be listed on the editor. It contains the
     * attribute names and their values.
    public Vector<iListable> getAttributesForShow() {

        //Get attributes of this annotation
        Vector<iListable> list = new Vector<iListable>();
        if (attributes != null) {
            for (AnnotationAttributeDef rel : attributes) {
                list.add(rel);
            }
        }

        // Go through each attribute... if it is in the Annotation then get the
        // current value, and set that as the selected value else just add it
        // with no selected value.
        Vector<iListable> finalList = new Vector<iListable>();
        String classname = this.annotationclass;

        if ((classname == null) || (classname.trim().length() < 1)) {
            return null;
        }

        // get class of current annotation
        resultEditor.annotationClasses.Depot classdepot =
                new resultEditor.annotationClasses.Depot();
        AnnotationClass currentClass =
                classdepot.getAnnotatedClass(annotationclass);

        // if the class of this annotation is allowed to use public attributes:
        if (currentClass.inheritsPublicAttributes) {
            
            for (relationship.simple.dataTypes.AttributeSchemaDef attribute : env.Parameters.SimpleRelationshipNames.getAttributes()) {
                boolean accountedFor = false;
                for (iListable listEntry : list) {
                    if ((((AnnotationAttributeDef) listEntry).name == null)
                            || (((AnnotationAttributeDef) listEntry).value == null)) {
                        continue;
                    }
                    if (listEntry.getSelectedItem(0).equals(attribute.getName())) {
                        accountedFor = true;
                        attribute.setString(1, listEntry.getSelectedItem(1));
                        finalList.add(attribute);
                    }
                }
                if (!accountedFor) {
                    finalList.add(attribute);
                }
            }

        }

        for (relationship.simple.dataTypes.AttributeSchemaDef attribute : currentClass.privateAttributes) {
            boolean accountedFor = false;
            for (iListable listEntry : list) {
                if ((((AnnotationAttributeDef) listEntry).name == null)
                        || (((AnnotationAttributeDef) listEntry).value == null)) {
                    continue;
                }
                if (listEntry.getSelectedItem(0).equals(attribute.getName())) {
                    accountedFor = true;
                    attribute.setString(1, listEntry.getSelectedItem(1));
                    finalList.add(attribute);
                }
            }
            if (!accountedFor) {
                finalList.add(attribute);
            }
        }



        //Get all Normal Relationships that Aren't in the current Schema
        if (attributes != null) {

            for (AnnotationAttributeDef attribute : attributes) {
                if (((AnnotationAttributeDef) attribute).name == null
                        || ((AnnotationAttributeDef) attribute).value == null) {
                    continue;
                }
                boolean accountedFor = false;
                for (relationship.simple.dataTypes.AttributeSchemaDef attributedef : currentClass.privateAttributes) {
                    if (attributedef.getName().equals(attribute.name)) {
                        accountedFor = true;
                    }
                }

                for (relationship.simple.dataTypes.AttributeSchemaDef attributedef : env.Parameters.SimpleRelationshipNames.getAttributes()) {
                    if (attributedef.getName().equals( attribute.name)) {
                        accountedFor = true;
                    }
                }

                if (currentClass.inheritsPublicAttributes) {
                }

                if (!accountedFor) {
                    finalList.add(new relationship.simple.dataTypes.AttributeSchemaDef( attribute.name, attribute.value));
                }
            }
        }


        //Get all Normal Relationships that Aren't in the current Schema
        if (attributes != null) {
        }



        return finalList;
    }*/

}

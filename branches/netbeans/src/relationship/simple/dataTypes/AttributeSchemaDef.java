/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relationship.simple.dataTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
 * This Class is used to represent an Attribute which has a name and a Set of allowable
 * values. Currently this class has two uses: it holds all Attributes and allowable
 * values within our schema, as well as for representing normalrelationships within
 * an Annotation while they are being edited.  Annotations DO NOT hold Attribute
 * objects, they hold a related object of normalrelationships.
 *
 * @author Chris Jianwei Leng
 */
public class AttributeSchemaDef implements resultEditor.relationship.iListable, java.lang.Comparable
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    /**
     * If this name is a repeat of a attribute in or schema this will be true.
     */
    private String name = "";
    /**
     * List of allowed values
     */
    private Vector<String> allowedEntries = new Vector<String>();
    /**
     * True if this is a repeat attribute.
     */
    private boolean repeatName = false;
    
    /**Is this attribute is UMLS code.*/
    public boolean isCUICodeNLabel  = false;
    public boolean isCUICode        = false;
    public boolean isCUILabel       = false;
    
    private String defaultValue = null;
    
    
    public void removeDefaultValue(){
        this.defaultValue = null;
    }
    
    public boolean hasDefaultValue(String value){
        if(this.defaultValue == null)
            return false;
        
        if((value==null)||(value.trim().length()<1))
            return false;
        
        // if(this.defaultValue != null){
            if( defaultValue.trim().length() < 1 )
                return false;
            else 
            {
                if( defaultValue.trim().compareTo( value.trim()) == 0 )
                    return true;
                else 
                    return false;
            }
        //}
        
    }
    
    public boolean hasDefaultValue(){
        if(this.defaultValue != null){
            if( defaultValue.trim().length() < 1 )
                return false;
            else 
                return true;
        }
        return false;
    }
    public void setDefaultValue(String value){
        if( value == null )
            this.defaultValue = null;
        else{
            if( value.trim().length() <1 )  // Set it to "null" if it's empty or all space characters
                this.defaultValue = null;
            else
                this.defaultValue = value.trim();
        }
    }
    
    
    public String getDefault(){
        
        if( defaultValue != null ){
            if( defaultValue.trim().length() < 1 )
                return null;
            else
                return defaultValue.trim();
        }
        
        return defaultValue;
    }
    /**
     * The currently selected value. Usually this will be a value from 'allowedEntries',
     * but it not always necessarily to be true.
     */
    private String current = "";
    /*
     * If we read in an Attribute name that is not in our Schema than this will be set to
     * false.  This denotes whether this Attribute can be modifed from our Attribute editor.
     */
    private boolean isUsable = true;
    //</editor-fold>

    
    public void removeValue(String value){
        if(( value == null  ) ||(value.trim().length()<1 ))
            return;
        
        this.allowedEntries.remove( value.trim() );
    }

    public void rename(String newname){
        if((newname!=null)&&(newname.trim().length()>0))
            this.name = newname;
    }

    public final Vector<String> getAllAllowedEntries(){
        return allowedEntries;
    }

    /**Add a new option. */
    public void put(String value){
        if( ( value == null ) || (value.trim().length() < 1) )
            return;

        if( allowedEntries.size() == 0 )
            allowedEntries.add(value);

        for( String existingvalue : this.allowedEntries ){
            if ( existingvalue == null )
                continue;
            if ( existingvalue.trim().compareTo( value.trim() ) == 0 )
                return;
        }

        this.allowedEntries.insertElementAt( value, 0 );
    }

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * Regular Constructor for an Attribute, This Constructor is for Attributes within
     * the Schema.
     * @param name - The name of the Attribute
     * @param allowed - The Allowable Entries
     */
    public AttributeSchemaDef(String name, Vector<String> allowed)
    {
        this.name = name;
        this.allowedEntries.clear();
        this.isCUICode = false;
        this.isCUICodeNLabel = false;
        this.isCUILabel = false;
        
        
        if(allowed!=null){
            for(int i=allowed.size()-1; i>0; i--){
                String currentvalue = allowed.get(i);
                // valid checking
                if(( currentvalue == null ) || (currentvalue.trim().length()<1)){
                    allowed.removeElementAt(i);
                    continue;
                }
                
                boolean foundOneRepetitive = false;
                // check if this value (currentvalue) already exists
                for(int j=i-1;j>=0; j--){
                    String eachvalue  = allowed.get(j);
                    if(( eachvalue == null ) || (eachvalue.trim().length()<1)){                    
                        continue;
                    }
                    
                    if( currentvalue.trim().compareTo( eachvalue.trim() ) == 0 ){
                        foundOneRepetitive = true;
                        break;
                    }
                }
                
                if(foundOneRepetitive){
                    allowed.removeElementAt(i);
                    continue;
                }
            }
        }
        
        this.allowedEntries.addAll(allowed);
    }
    /**
     * The Constructor for an Attribute which is read in whose Attribute name is not
     * within our Schema.  These Attributes will not be Editable. (except for deletion *TODO)
     * @param name - the name of the attribute
     * @param onlyValue - the value of the attribute
     */
    public AttributeSchemaDef(String name, String onlyValue)
    {
        this.allowedEntries.clear();
        isUsable = false;
        this.name = name;
        this.allowedEntries.add(onlyValue);
        current = onlyValue;
    }
    
    public AttributeSchemaDef()
    {
        this.allowedEntries.clear();
        isUsable = false;        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="iListable Interface Methods">
    /**
     * Get The write form for this object, this will return a NormalRelationship object
     * that can be added to an annotation, it will use the currently selected value
     * and the name of this Attribute to create a normal relationship.
     * @return - A alternate representation of this object
     */
    public Object getWriteForm()
    {
        //If the current string is empty return null.
        if(getCurrent().equals(""))
            return null;

        //Make sure the string is more than just spaces
        boolean moreThanSpaces = false;
        for(char c: getCurrent().toCharArray())
        {
            if(c != (' '))
                moreThanSpaces = true;
        }
        //If the string does not have more than spaces return null
        if(!moreThanSpaces)
            return null;

        //Remove any sort of html tagging
        String temp = getCurrent().replace("<html><font color = \"red\">", "");
        temp = temp.replace("</font></html>", "");

        //If the string is empty when the html tagging is removed then return null
        if(temp.equals(""))
            return null;

        //Clear out the current string
        setCurrent("");
        return new resultEditor.annotations.AnnotationAttributeDef(getName(),temp);
    }
    /**
     * Get the currently Selected item
     * @param which - the column to get the selected value from
     * @return - the selected value in the given column
     */
    public String getSelectedItem(int which)
    {
        if(which == 0)
            return getName();
        return getCurrent();
    }
    /**
     * Set the selected String(value) at a given column(position).
     * @param position - The Column to set the selected value
     * @param value - The selected value
     */
    public void setString(int position, String value)
    {
        if(position == 0)
            setName(value);
        if(position == 1)
        {
            if(!allowedEntries.contains(value) && !value.equals(""))
            {
                String s ="<html><font color = \"red\">" + value + "</font></html>";
                setCurrent(s);
            }
            else
                setCurrent(value);
        }
    }
    /**
     * Get The Available Strings for a given column
     * @param which - The column to return the available string choices for
     * @return - available String choices, if needsCombo(which) returns true there
     * may be multiple values but if needsCombo(which) returns false then there
     * will only be on string in the Return list.
     */
    public Vector<String> getStrings(int which)
    {
        Vector<String> toReturn = new Vector<String>();
        if(which == 0)
        {
            toReturn.add(getName());
        }
        if(which == 1)
        {
            if(isIsUsable())
                toReturn.add("");
            boolean accountedFor = false;
            if(getCurrent().equals(""))
                accountedFor = true;
            for(String allowed: getAllowedEntries())
            {
                if(allowed.equals(getCurrent()))
                    accountedFor = true;
                toReturn.add(allowed);
            }
            if(!accountedFor)
            {
                toReturn.add(getCurrent());
            }
        }
        return toReturn;
    }
    /**
     * Returns true if the given Column needs a combo box, false otherwise.
     * @param which - the column
     * @return
     */
    public boolean needsCombo(int which)
    {
        if(!isIsUsable())
            return false;
        if(which == 0)
            return false;
        return true;
    }
    /**
     *
     * @param which - the column to check for modifiable
     * @return - Return true if the given column can be modified
     */
    public boolean isModifiable(int which)
    {
        if(!isIsUsable())
            return false;
        if(which == 1)
            return true;
        return false;
    }
    /**
     *
     * @param which - the column
     * @return - true if this column has an entry in this data type, false otherwise.
     * This object only returns true for 0,1.
     */
    public boolean hasEntry(int which)
    {
        if(which < 2)
            return true;
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * For Internal Comparing.
     * @return - a string representation of this object, used for comparisons
     */
    private String stringToCompare()
    {
        String toReturn = "";
        toReturn += getName();
        for(String s: getAllowedEntries())
            toReturn += "\t" + s;
        return toReturn;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    public String toString()
    {
        String toReturn = "";
        toReturn += getName();
        return toReturn;
    }
    public boolean couldDelete()
    {
        if(!isIsUsable())
            return true;
        return false;
    }
    /**
     * Compare to objects to order them.  Currently using the string representation of
     * the object for comparisons.
     * @param f - the object
     * @return - -1 if less than 0 if equal 1 if greater than
     */
    public int compareTo(Object f)
    {
        if(!f.getClass().isInstance(this))
            return -1;
        AttributeSchemaDef temp = (AttributeSchemaDef)f;
        return(this.stringToCompare().compareTo(temp.stringToCompare()));
    }
    /**
     * See if the objects are equal.  Two attribute objects are considered equal if
     * the names are the same and all of the attribute values match up.
     * @param f
     * @return true if they are equal false otherwise.
     */
    public boolean equals(Object f)
    {
        if(this == null && f == null)
            return true;
        if(this == null || f == null)
            return false;
        if(!f.getClass().isInstance(this))
            return false;
        AttributeSchemaDef att = (AttributeSchemaDef)f;
        if(!att.name.equals(this.name))
            return false;

        //Sort 'this' objects allowed entries
        ArrayList<String> sortedAllowed = new ArrayList<String>();
        sortedAllowed.addAll(getAllowedEntries());
        Collections.sort(sortedAllowed);
        
        //sort other objects allowed entries
        ArrayList<String> sortedAllowedOutside = new ArrayList<String>();
        sortedAllowedOutside.addAll(att.getAllowedEntries());
        Collections.sort(sortedAllowedOutside);
        if(sortedAllowedOutside.size() != sortedAllowed.size())
            return false;
        for(int i = 0; i< sortedAllowedOutside.size(); i++)
        {
            if(!sortedAllowed.get(i).equals(sortedAllowedOutside.get(i)))
                return false;
        }
        return true;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
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
     * @return the allowedEntries
     */
    public Vector<String> getAllowedEntries()
    {
        return allowedEntries;
    }

    /**
     * @param allowedEntries the allowedEntries to set
     */
    public void setAllowedEntries(Vector<String> _newAttributeValues)
    {
        // this one is to remove repetitive items to make sure no
        // repetitive attribue values for this attribute
        if(_newAttributeValues!=null){
            for(int i=_newAttributeValues.size()-1; i>0; i--){
                String currentvalue = _newAttributeValues.get(i);
                // valid checking
                if(( currentvalue == null ) || (currentvalue.trim().length()<1)){
                    _newAttributeValues.removeElementAt(i);
                    continue;
                }
                
                boolean foundOneRepetitive = false;
                // check if this value (currentvalue) already exists
                for(int j=i-1;j>=0; j--){
                    String eachvalue  = _newAttributeValues.get(j);
                    if(( eachvalue == null ) || (eachvalue.trim().length()<1)){                    
                        continue;
                    }
                    
                    if( currentvalue.trim().compareTo( eachvalue.trim() ) == 0 ){
                        foundOneRepetitive = true;
                        break;
                    }
                }
                
                if(foundOneRepetitive){
                    _newAttributeValues.removeElementAt(i);
                    continue;
                }
            }
        }
                
        this.allowedEntries = new Vector<String>();
        if( _newAttributeValues != null ){
            
                this.allowedEntries.addAll(_newAttributeValues);
            
        }else{
            this.allowedEntries.clear();
        }
            
    }

    /**
     * @return the repeatName
     */
    public boolean isRepeatName()
    {
        return repeatName;
    }

    /**
     * @param repeatName the repeatName to set
     */
    public void setRepeatName(boolean repeatName)
    {
        this.repeatName = repeatName;
    }

    /**
     * @return the current
     */
    public String getCurrent()
    {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(String current)
    {
        this.current = current;
    }

    /**
     * @return the isUsable
     */
    public boolean isIsUsable()
    {
        return isUsable;
    }

    /**
     * @param isUsable the isUsable to set
     */
    public void setIsUsable(boolean isUsable)
    {
        this.isUsable = isUsable;
    }
    //</editor-fold>
}

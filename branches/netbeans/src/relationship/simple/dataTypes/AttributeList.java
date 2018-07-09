/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relationship.simple.dataTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;

/**
 * This Class is used to save all Attributes that are currently in the Schema.
 * Methods for accessing and modifying these Attributes are also found in this class.
 *
 * @see env.Attribute
 * @see ResultEditor.SimpleSchema.Editor
 * @author Chris
 */
public class AttributeList
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    /**
     * The list of attributes in the schema.
     */
    private TreeSet<AttributeSchemaDef> attSet = new TreeSet<AttributeSchemaDef>();
    //</editor-fold>

    /**get the public attribute by its name.*/
    public AttributeSchemaDef getAttribute(String attributename){
        if(( attSet == null )||(attSet.size()<1))
            return null;
        
        if( ( attributename == null ) ||( attributename.trim().length()<1))
            return null;
        
        for( AttributeSchemaDef att : attSet ){
            if( att == null )
                continue;
            
            if( att.getName() == null )
                continue;
            
            if( att.getName().compareTo( attributename.trim() ) == 0 )
                return att;
        }
        
        return null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Default constructor
     */
    public AttributeList(){}
    //</editor-fold>

    public void clear(){
        attSet.clear();
    }

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    /**
     * Add an attribute to the Schema
     * @param toAdd - the Attribute to add
     */
    public void Add(AttributeSchemaDef toAdd)
    {
        if(toAdd.getName().equals(""))
            return;
        attSet.add(toAdd);
    }
    /**
     * Remove an attribute from the schema
     * @param toRemove - the attribute to remove.
     */
    public void Remove(AttributeSchemaDef toRemove)
    {
        attSet.remove(toRemove);
    }
    /**
     * Get all Attributes in the current schema.
     * @return - All Attributes in the current schema.
     */
    public Vector<AttributeSchemaDef> getAttributes()
    {
        Vector<AttributeSchemaDef> toReturn = new Vector<AttributeSchemaDef>();
        for(AttributeSchemaDef att: attSet)
            toReturn.add(att);
        return toReturn;
    }

    public void rename(String oldname, String newname) {

        if((oldname==null)||(oldname.trim().length()<1))
            return;
        if((newname==null)||(newname.trim().length()<1))
            return;

        if(attSet==null)
            return;

        if(exists(newname)){
            return;
        }


        for(AttributeSchemaDef att: attSet){
            if(att==null)
                continue;
            if(att.getName()==null)
                continue;
            if(att.getName().equals(oldname)){


                AttributeSchemaDef newatt = new AttributeSchemaDef( newname, att.getAllAllowedEntries() );
                this.Remove(att);
                this.Add(newatt);
                return;
            }
        }

    }

    private boolean exists(String attributeName){
        if((attributeName==null)||(attributeName.trim().length()<1))
            return true;
        
        for(AttributeSchemaDef att0: attSet){
            if(att0==null)
                continue;
            if(att0.getName()==null)
                continue;

            if(att0.getName().equals(attributeName.trim()))
                return true;
        }

        return false;
    }

    /**
     * Get the allowed Values for a given attribute name
     * @param s - the name of the attribute
     * @return - an ArrayList of allowed values
     */
    public ArrayList<String> getAllowed(String s)
    {
        ArrayList<String> toReturn = new ArrayList<String>();
        for(AttributeSchemaDef att: attSet)
        {
            if(att.getName().equals(s))
            {
                for(String allowed: att.getAllowedEntries())
                    toReturn.add(allowed);
            }
        }
        return toReturn;
    }
    
    
    public Hashtable<String, Hashtable> getExceptions(Vector<Annotation> toCheck){
        
        // classname : depot of attributes
        Hashtable<String, Hashtable> byclass = new Hashtable<String, Hashtable>();
        // attribute " depot of values
        //Hashtable<String, HashSet> byatt = new Hashtable<String, HashSet>();
        // values
        //HashSet<String> values = new HashSet<String>();

        for (Annotation annotation : toCheck) {
            if (annotation == null) {
                continue;
            }
            if ((annotation.annotationclass == null)||(annotation.annotationclass.trim().length()<1)) {
                continue;
            }
            
            // GET ATTRIBUTES OF CLASS OF THIS ANNOTATION
            resultEditor.annotationClasses.Depot classdepot =
                    new resultEditor.annotationClasses.Depot();
            AnnotationClass annotationClass = classdepot.getAnnotatedClass(annotation.annotationclass.trim());
            if (annotationClass == null) {
                continue;
            }
            if( annotationClass.privateAttributes == null ) {
                annotationClass.privateAttributes = new Vector<AttributeSchemaDef>();                
            }
                  
            // GO THOUGH ALL ATTRIBUTE OF THIS ANNOTATION
            try {
                //Loop through each normal relationship and compare it against our current schema
                //to see if our schema can represent it.
                for (AnnotationAttributeDef annotationAttribute : annotation.attributes) {
                    
                    // make sure this annotation attribute is valid,
                    // otherwise, goto the next attribute of this annotation
                    if ((annotationAttribute.name == null)||(annotationAttribute.value == null)) {
                        continue;
                    }                    
                    if ((annotationAttribute.name.trim().length()<1)
                            ||(annotationAttribute.value.trim().length()<1)) {
                        continue;
                    }
                    
                    // System.out.println("att["+annotationAttribute.name+"] = " + annotationAttribute.value);
                    //Make a copy because we will be modifying it.. and we don't want the changes to
                    //affect the actual annotation.
                    //attribute = attribute.copy();
                        
                    // TO ATTRIBUTES OF CLASS OF THIS ANNOTATION
                    
                    // RECORD THIS ATTRIBUTE
                    // if this class didn't have any attribute yet
                    if((annotationClass.privateAttributes==null)
                            ||(annotationClass.privateAttributes.size()<1)){
                        Hashtable<String, HashSet> _byAtt;
                        // get atts of this class by its name                        
                        if( byclass.get( annotation.annotationclass.trim() ) != null ){                            
                            _byAtt = (Hashtable<String, HashSet>) byclass.get(annotation.annotationclass.trim());
                        }else{
                            _byAtt = new Hashtable<String, HashSet>();                            
                        }
                        
                        HashSet<String> _values; // = new HashSet<String>();
                        // get the values
                        if( _byAtt.get(annotationAttribute.name.trim()) != null ){
                            _values = _byAtt.get( annotationAttribute.name.trim() );
                            
                        }else{
                            _values = new HashSet<String>();                            
                        }
                        // add the value
                        _values.add( annotationAttribute.value.trim()  );                        
                        // put it back to attribute: attributename:values
                        _byAtt.put( annotationAttribute.name.trim(), _values );
                        // put it back to class
                        byclass.put(annotation.annotationclass.trim(), _byAtt);
                        
                    }else{
                    
                    
                    
                    boolean found = false;
                    //annotationClass.privateAttributes    
                    for (AttributeSchemaDef aAttributeSchema : annotationClass.privateAttributes) {
                        
                        // continue to next if attribute name are different                        
                        if (aAttributeSchema.getName().compareTo(annotationAttribute.name) != 0) {                            
                            continue;
                        }
                        
                        // So, the attribute of the annotation is found now

                        //Remove any already accounted for values... need to check for same att name first
                        if (aAttributeSchema.getAllowedEntries().contains(annotationAttribute.value)) {
                            continue;
                        }
                        
                        // so, attribute with now value is found now;
                        found = true;
                        // let's record it
                        
                        
                        Hashtable<String, HashSet> _byAtt;
                        
                        // get atts of this class by its name                        
                        if( byclass.get( annotation.annotationclass.trim() ) != null  ){                            
                            _byAtt = (Hashtable<String, HashSet>) byclass.get(annotation.annotationclass.trim());
                        }else{
                            _byAtt = new Hashtable<String, HashSet>();                            
                        }
                        
                        HashSet<String> _values; // = new HashSet<String>();
                        // get the values
                        if( _byAtt.get(annotationAttribute.name.trim()) != null ){
                            _values = _byAtt.get( annotationAttribute.name.trim() );
                            
                        }else{
                            _values = new HashSet<String>();                            
                        }
                        // add the value
                        _values.add( annotationAttribute.value.trim()  );                        
                        // put it back to attribute: attributename:values
                        _byAtt.put( annotationAttribute.name.trim(), _values );
                        // put it back to class
                        byclass.put(annotation.annotationclass.trim(), _byAtt);
                        
                        
                    }
                    
                    if(!found){
                        Hashtable<String, HashSet> _byAtt;
                        
                        // get atts of this class by its name                        
                        if( byclass.get( annotation.annotationclass.trim() ) != null ){                            
                            _byAtt = (Hashtable<String, HashSet>) byclass.get(annotation.annotationclass.trim());
                        }else{
                            _byAtt = new Hashtable<String, HashSet>();                            
                        }
                        
                        HashSet<String> _values; // = new HashSet<String>();
                        // get the values
                        if( _byAtt.get(annotationAttribute.name.trim()) != null ){
                            _values = _byAtt.get( annotationAttribute.name.trim() );
                            
                        }else{
                            _values = new HashSet<String>();                            
                        }
                        // add the value
                        _values.add( annotationAttribute.value.trim()  );                        
                        // put it back to attribute: attributename:values
                        _byAtt.put( annotationAttribute.name.trim(), _values );
                        // put it back to class
                        byclass.put(annotation.annotationclass.trim(), _byAtt);
                        //System.out.println("_______>3["+ annotation.annotationclass.trim() +"]" + 
                        //        "["+ annotationAttribute.name.trim() +"]" + 
                        //        "["+ annotationAttribute.value.trim()  +"]");
                    }
                    }
                    
                    
                    
                    /*
                    //If we end up with a relationship with values in this list than we have an unknown attribute
                    //value.
                    if ((attribute.value != null) && (attribute.value.trim().length() > 0)) {
                        AttributeSchemaDef att = new AttributeSchemaDef(attribute.name, attribute.value);
                        // System.out.println("%%" + attribute.name + "; " + attribute.value);
                        att.setRepeatName(!attribute.deadName);
                        //toReturn.add(att);
                        
                        

                    }*/
                }


            } catch (Exception ex) {
                System.out.println("\n!!! fail to record attribtue information for attribute filter");
            }
        }
        
        return byclass;

    }
    /**
     * Check a collection of Annotations to see if there are any with Attributes outside
     * of the current Schema.
     * @param toCheck - the annotations to check
     * @return - all Attributes outside the current schema.
     */
    public TreeSet<AttributeSchemaDef> checkExists(Vector<Annotation> toCheck) {
        //Create a treeset to return all uniquely new attributes
        TreeSet<AttributeSchemaDef> toReturn = new TreeSet<AttributeSchemaDef>();

        //Loop through each annotation to find Attributes outside of our schema.
        for (Annotation annotation : toCheck) {
            if (annotation.attributes == null) {
                continue;
            }

            //Loop through each normal relationship and compare it against our current schema
            //to see if our schema can represent it.
            for (AnnotationAttributeDef attribute : annotation.attributes) {
                if (attribute.name == null) {
                    continue;
                }
                if (attribute.value == null) {
                    continue;
                }
                //Make a copy because we will be modifying it.. and we don't want the changes to
                //affect the actual annotation.
                attribute = attribute.copy();
                for (AttributeSchemaDef savedAttribute : attSet) {
                    if ( savedAttribute.getName().compareTo( attribute.name) != 0 ) {                        
                        continue;
                    }

                    //Remove any already accounted for values... need to check for same att name first

                    if (savedAttribute.getAllowedEntries().contains(attribute.value)) {
                        continue;
                    }

                    //Need to somehow indicate that the name is accounted for
                    if (savedAttribute.getName().equals(attribute.name)) {
                        attribute.deadName = true;
                    }
                }
                //If we end up with a relationship with values in this list than we have an unknown attribute
                //value.
                if ((attribute.value != null) && (attribute.value.trim().length() > 0)) {
                    AttributeSchemaDef att = new AttributeSchemaDef(attribute.name, attribute.value);
                    // System.out.println("%%" + attribute.name + "; "+ attribute.value);
                    att.setRepeatName(!attribute.deadName);
                    toReturn.add(att);
                }
            }

        }
        toReturn = combineSameName(toReturn);
        return toReturn;

    }
    /**
     * Add All attributes to the Schema.
     * @param toAdd
     */
    public void addAll(Collection<AttributeSchemaDef> toAdd)
    {
        //Copy to ArrayList... TODO probably more efficient way to do this... look into
        //that if this ends up being too slow.
        ArrayList<AttributeSchemaDef> initial = new ArrayList<AttributeSchemaDef>();
        ArrayList<AttributeSchemaDef> needToAdd = new ArrayList<AttributeSchemaDef>();
        initial.addAll(attSet);
        needToAdd.addAll(toAdd);
        //Loop through all attributes that we are trying to add to see if the Attribute already exists
        // and only the values are needed.
        for(AttributeSchemaDef att: needToAdd)
        {
            AttributeSchemaDef matching = null;
            for(AttributeSchemaDef init: initial)
            {
                if(init.getName().equals(att.getName()))
                    matching = init;
            }
            if(matching != null)
                matching.getAllowedEntries().addAll(att.getAllowedEntries());
            else
            {
                attSet.add(att);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Combine a set of attributes so that any attributes that have the same name are combined into
     * Attribute object.  This is done by adding the allowedEntries of the repeat item to the
     * allowedEntries of the existing item(the one before it in the list).
     * @param atts - the attributes to retur
     * @return - the Attributes combined so that each one has a unique name and contains
     * all allowedEntries from before.
     */
    private TreeSet<AttributeSchemaDef> combineSameName(TreeSet<AttributeSchemaDef> atts)
    {
        //The Set to Return
        TreeSet<AttributeSchemaDef> toReturn = new TreeSet<AttributeSchemaDef>();

        //Will copy the passed in hashSet into an ArrayList so it is easier to use.
        ArrayList<AttributeSchemaDef> temp = new ArrayList<AttributeSchemaDef>();
        temp.addAll(atts);

        //Loop through each element finding all repeat elements farther in the list
        for(int i = 0; i< temp.size(); i++)
        {
            //Loop ahead of the current index to find any repeat names
            for(int j = i + 1; j< temp.size(); j++)
            {
                //If the names match than the item at position j is a repeat so...
                //add all of the allowed entries in at position j to position i.
                if(temp.get(i).getName().equals(temp.get(j).getName()))
                {
                    temp.get(i).getAllowedEntries().addAll(temp.get(j).getAllowedEntries());
                    temp.remove(j);
                    j--;
                }
            }
        }
        //Put the results back into a TreeSet and return
        toReturn.addAll(temp);
        return toReturn;
    }
    //</editor-fold>
}

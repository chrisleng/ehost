/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import relationship.simple.dataTypes.AttributeList;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;

/**
 *
 * @author imed
 */
public class DepotOfAttributes {

    /**
     * the flag that tell system whether do we want to use attribute conditions
     * to filter out annotations.
     *
     * True : filter on; False : filter off;
     */
    private static boolean isFilterOn = false;

    /**
     * Set the flag to tell system whether do we want to use attribute
     * conditions to filter out annotations.
     *
     * @param isFilterOn True : filter on; False : filter off;
     */
    public static void setFilterOn(boolean isFilterOn) {
        DepotOfAttributes.isFilterOn = isFilterOn;
    }

    /**
     * Tell us whether do we are filtering out annotations by their attribute
     * conditions.
     *
     * @param isFilterOn True : filter on; False : filter off;
     */
    public static boolean isFilterOn() {
        return isFilterOn;
    }
    /**
     * Current class that we list its attributes on the Navigation Tree, and it
     * will be 'null' if no class is selected.
     */
    private static String classname = null;

    /**
     * Return the name of current selected class that we use its attributes to
     * filter out annotations; and it will return null while no class is
     * selected.
     */
    public static String getClassname() {
        return classname;
    }
        
    /**
     * Record name of current class, and this class will use attributes of this
     * class to filter out annotations.
     *
     * @param classname The name of the new selected class; it could be "null"
     * if nothing is selected.
     */
    public static void setClassname(String classname) {
        DepotOfAttributes.classname = classname;
    }
    /**
     * class and its attributes, index by "class"
     */
    public static HashMap<String, AttributesOfAClass> attributeDepot=  new HashMap<String, AttributesOfAClass>();

    public static AttributesOfAClass getAttributes() {
        if ((classname == null) || (classname.trim().length() < 1)) {
            return attributeDepot.get("NULL_NULL_NULL");
        } else {
            classname = classname.trim();
            return attributeDepot.get(classname);
        }
    }

    public static AttributesOfAClass getAttributes(String classname) {
        DepotOfAttributes.classname = classname;
        return getAttributes();
    }

    
    private static String currentproject = null;
    
    public static void smartReset(String classname, String projectname) {
        smartReset( classname, projectname, false);
    }
    
    public static void smartReset(String classname, String projectname, boolean realreset) {

        boolean needRealReset = false;
        if( currentproject == null ){
            needRealReset = true;
        }else{
            if(projectname!=null){
                if(currentproject.trim().compareTo(projectname.trim()) != 0 ){
                    needRealReset = true;
                }
            }
        }
        currentproject = projectname;
        
        if(realreset||needRealReset){
            attributeDepot.clear();
            DepotOfAttributes.classname = null;
            //DepotOfAttributes.
        }
        
        
        // #### 1 ####        
        // register all public attributes, if no designated class

        // flag: do we need public attributes
        boolean needsPublic = false;
        if ((classname == null) || (classname.trim().length() <= 0)) {
            needsPublic = true;
        } else if ((classname != null) && (classname.trim().length() > 0)) {
            resultEditor.annotationClasses.Depot classdepot =
                    new resultEditor.annotationClasses.Depot();            
            AnnotationClass ac = classdepot.getAnnotatedClass(classname);
            
            if(ac == null)
                return;

            // #### 1.2 
            // inherited public attributes
            if (ac.inheritsPublicAttributes) {
                needsPublic = true;
            }
        }


        // #### 2
        // get or set the attribute list ("attributes") for this class
        if (classname == null) {
            classname = "NULL_NULL_NULL";
        }

        classname = classname.trim();
        if (classname.length() < 1) {
            classname = "NULL_NULL_NULL";
        }

        AttributesOfAClass attributes = attributeDepot.get(classname);
        if (attributes == null) {
            attributes = new AttributesOfAClass(classname);
        }


        // #### 3
        // record public attributes if needed
        if (needsPublic) {
            // get public attributes
            AttributeList publicAtts = env.Parameters.AttributeSchemas;

            // put public attributes into a new structure
            if (publicAtts != null) {
                Vector<AttributeSchemaDef> publicAttributes = publicAtts.getAttributes();
                for (AttributeSchemaDef publicAttribute : publicAttributes) {
                    if (publicAttribute == null) {
                        continue;
                    }

                    // get the name of a public attribute 
                    String attname = publicAttribute.getName();

                    // get or create an att structure for this attributes and its attValue
                    Att att = attributes.attributes.get(attname);
                    if (att == null) {
                        att = new Att(attname);
                        att.isSelected = true;
                    }

                    att.count = 0;
                    att.isPublic = true;

                    if (publicAttribute.getAllowedEntries() != null) {
                        for (String attValue : publicAttribute.getAllowedEntries()) {
                            if ((attValue == null) || (attValue.trim().length() < 1)) {
                                continue;
                            }
                            if (!att.values.containsKey(attValue)) {
                                AttValue av = new AttValue(attValue); 
                                av.count = 0;
                                att.values.put(attValue, av);
                                av.isSelected = true;
                            }else{
                                AttValue av = att.values.get( attValue);
                                av.count = 0;
                                att.values.put(attValue, av);
                            }
                        }
                    }

                    attributes.attributes.put(attname, att);
                    //.put(publicAttribute.getName(), att);
                }

            }
        }

        // #### 4 ####
        // Otherwise, for a specific class, add public attribute if it has, and 
        // its private attribute


        // #### 4.2
        // private attributes
        if ((classname != null) && (classname.trim().length() > 0) && (classname.compareTo("NULL_NULL_NULL") != 0)) {

            resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
            AnnotationClass ac = classdepot.getAnnotatedClass(classname);
            Vector<AttributeSchemaDef> privateAttributes = ac.privateAttributes;
            for (AttributeSchemaDef privateAttribute : privateAttributes) {
                if (privateAttribute == null) {
                    continue;
                }

                String name = privateAttribute.getName();
                Att att = attributes.attributes.get(name);
                if (att == null) {
                    att = new Att(name);
                    att.isSelected = true;
                }

                att.count = 0;
                att.isPublic = false;

                for (String value : privateAttribute.getAllowedEntries()) {
                    if ((value == null) || (value.trim().length() < 1)) {
                        continue;
                    }

                    value = value.trim();

                    if (!att.values.containsKey(value)) {
                        AttValue av = new AttValue(value);
                        av.count = 0;
                        av.isSelected = true;
                        att.values.put(value, av);
                    } else {
                        AttValue av = att.values.get(value);
                        av.count = 0;
                        att.values.put(value, av);
                    }
                }

                attributes.attributes.put(name, att);
            }

        }

        attributeDepot.put(classname, attributes);


    }

    /**
     * tell the DepotOfAttributes that a attribute attValue node is selected or not.
     */
    public static void setValueStatus(String attname, String valuename, boolean isSelected) {

        // get all attributes of this class
        AttributesOfAClass attributesOfAClass = DepotOfAttributes.getAttributes();


        HashMap<String, Att> currentAttributes = attributesOfAClass.attributes;
        if (currentAttributes == null) {
            return;
        }

        Att att = currentAttributes.get(attname);
        if (att == null) {
            return;
        }

        HashMap<String, AttValue> values = att.values;
        if (values == null) {
            return;
        }

        AttValue av = values.get(valuename);
        if (av == null) {
            return;
        }

        av.isSelected = isSelected;


        values.put(valuename, av);

        currentAttributes.put(attname, att);

        DepotOfAttributes.attributeDepot.put(DepotOfAttributes.classname, attributesOfAClass);

    }

    public static void setAttStatus(String attname, boolean selected) {
        // get all attributes of this class
        AttributesOfAClass attributesOfAClass = DepotOfAttributes.getAttributes();


        HashMap<String, Att> currentAttributes = attributesOfAClass.attributes;
        if (currentAttributes == null) {
            return;
        }

        Att att = currentAttributes.get(attname);
        if (att == null) {
            return;
        }

        att.isSelected = selected;
        
        HashMap<String, AttValue> valueset = att.values;
        
        
        if( valueset !=  null ){
            
            ArrayList<String> valuenames = new ArrayList<String>();
            
            for(String valuename : valueset.keySet()){
                valuenames.add(valuename); 
            }
            
            for(String valuename : valuenames ){
                AttValue av = valueset.get( valuename );
                if( av != null )
                    av.isSelected = selected;
                att.values.put(valuename, av);
            } 
        }        
        currentAttributes.put(attname, att);

        DepotOfAttributes.attributeDepot.put(DepotOfAttributes.classname, attributesOfAClass);
    }

    public static void setAttExpStatus(String attname, boolean expanded) {
        // get all attributes of this class
        AttributesOfAClass attributesOfAClass = DepotOfAttributes.getAttributes();


        HashMap<String, Att> currentAttributes = attributesOfAClass.attributes;
        if (currentAttributes == null) {
            return;
        }

        Att att = currentAttributes.get(attname);
        if (att == null) {
            return;
        }

        att.isExpanded = expanded;

        currentAttributes.put(attname, att);

        DepotOfAttributes.attributeDepot.put(DepotOfAttributes.classname, attributesOfAClass);

        // System.out.println( attname + " is " + expanded );
    }
    public static boolean isExpanded = false;

    
    /**true == dropoff */
    public static boolean filterByAttribute(Annotation annotation) {
        if (annotation.annotationclass == null) {
            return false;
        }

        if (DepotOfAttributes.isFilterOn()) {
            String filterclassname = DepotOfAttributes.getClassname();
            
            // If compare it with a specific class
            if ((filterclassname != null) && (filterclassname.compareTo("NULL_NULL_NULL") != 0)) {

                // drop off if its class is not selected.
                if (annotation.annotationclass.compareTo(filterclassname) != 0) {
                    return true;
                }

                if (annotation.attributes == null) {
                    return true; // not fit the request as it doesn't have any attribute
                }
                if (annotation.attributes.size() < 1) {
                    return true;
                }

                if(filterit( annotation, classname ))
                    return false;
            }
            else{
                // if compare it with public attribtues
                if(filterit( annotation, "NULL_NULL_NULL" ))
                    return false;
            }
            
            return true;    
        }
        
    return false;
    }
    
    /**return "true" means this annotation will NOT be dropped off, false means 
     * drop off .*/
    private static boolean filterit( Annotation annotation, String tclassname) {
        if(annotation.attributes == null)
            return false;
        
        for (AnnotationAttributeDef attribute_of_thisAnnotation : annotation.attributes) {
            if (attribute_of_thisAnnotation == null) {
                continue;
            }
            if ((attribute_of_thisAnnotation.name == null) || (attribute_of_thisAnnotation.name.trim().length() < 1)) {
                continue;
            }
            
            AttributesOfAClass attributeOfAAttribute = attributeDepot.get(tclassname);
            if ((attributeOfAAttribute == null) || (attributeOfAAttribute.attributes == null)) {
                return false;  // drop off
            }

            for (String attributename : attributeOfAAttribute.attributes.keySet()) {
                if (attributename == null) {
                    continue;
                }
                Att aattribute = attributeOfAAttribute.attributes.get(attributename);
                if (!aattribute.isSelected) {
                    continue;
                }
                if ((aattribute == null) || (aattribute.values == null)) {
                    continue;
                }


                // compare annotation's attribute attValue to the selected values of the filter
                // if they have same attribute names
                if (aattribute.attributeName.compareTo(attribute_of_thisAnnotation.name) == 0) {

                    if (aattribute.values == null) {
                        continue;
                    } else {

                        // compare their values
                        for (String valuename : aattribute.values.keySet()) {
                            AttValue av = aattribute.values.get(valuename);
                            if (!av.isSelected) {
                                continue;
                            }

                            if (av.value.compareTo(attribute_of_thisAnnotation.value) == 0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        
        return false; // drop off
    }
}

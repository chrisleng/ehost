/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import resultEditor.annotations.Annotation;

/**
 *
 * @author imed
 */
 /**structure used to store attributes information for nodes on eHOST Navigation 
 * Panel.*/
public class Att{
    
    /**Name of the attribute.*/
    public String attributeName;
    
    
    public int count;
    
    /**Is this attribute is a public attribute. */
    public boolean isPublic = false;
    
    /**set of all possible values of this attribute: attribute value name : 
     * value's own attribute.*/
    public HashMap<String, AttValue> values = new HashMap<String, AttValue>();
    
    public boolean isExpanded = false;
    
    public boolean isSelected = false;
    
    
    //public ArrayList<Annotation> annotations = new ArrayList<Annotation>();
    
    public Att(String name){
        this.attributeName = name;
        //annotations.clear();;
        count = 0;
    }
    
    public Att(String name, Annotation annotation ){
        this.attributeName = name;
        this.count = 1;
        //this.annotations.add(annotation);
    }
    
    //public void addAnnotation(Annotation annotation){
    //    if( annotation != null )
    //        annotations.add( annotation );
    //}
}

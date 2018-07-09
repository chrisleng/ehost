/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import java.util.HashMap;

/**
 *
 * @author imed
 */
public class AttributesOfAClass {
    
    /**counted attributes details: attribute name : attribute details */
    public HashMap<String, Att> attributes = new HashMap<String, Att>();
    
    /**name of the class who owns these attribtues.*/
    public String classname = null;
    
    public AttributesOfAClass(String classname){
        this.classname = classname;
    }
    
    /**report how many attributes this attributes set have, it counts number of 
     * public attribtues plus private attributes.*/
    public int size(){
        return attributes.size();
    }
    
     
}

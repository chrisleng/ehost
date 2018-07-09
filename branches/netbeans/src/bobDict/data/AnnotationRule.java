/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.data;

import java.util.HashMap;

/**
 *
 * @author imed
 */
public class AnnotationRule {
    public String id;
    public String type;
    public String groupbegin;
    public String groupend;
    
    public AnnotationRule(String id, String type, String groupbegin, String groupend  ){
        this.id = id;
        this.type = type;
        this.groupbegin = groupbegin;
        this.groupend = groupend;
    }
    
    // name, type
    public HashMap<String, String> setFeatures = new HashMap<String, String>();
    
    public String featuresToString(){
        String toReturn = "";
        
        for( String name : setFeatures.keySet() ){
            String value = setFeatures.get( name );
            toReturn = toReturn + "\""+name + " = " + value + "\"; ";            
        }
        
        return toReturn;
        
    }
    public void clear(){
        setFeatures.clear();
    }
    
    public void addSetFeatures(String name, String type){
        setFeatures.put(name, type);
    }
}

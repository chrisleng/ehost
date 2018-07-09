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
public class Variables {
    public HashMap<String, String> variables = new HashMap<String, String>();
    
    public void clear(){
        variables.clear();
    }
    
    public void add(String name, String value){
        if(( name == null )||(name.trim().length()<1))
            return;
        if(( value == null )||(value.trim().length()<1))
            return;
        
        variables.put( name.trim() , value.trim());
            
    }

    public int size() {
        return variables.size();
    }
    
    
}

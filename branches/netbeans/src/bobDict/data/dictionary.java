/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.data;

/**
 *
 * @author imed
 */
public class dictionary {
    
    public static Variables variables = new Variables();
          
    public static Concepts concepts = new Concepts();
    
    public static void clear(){
        variables.clear();        
        concepts.clear();
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.tableExtraction;

/**
 *
 * @author imed
 */
public class Light {
    
    /**string content of the found table contents*/
    public String line = null;
    
    public String variablename = null;
    
    /** 0 : first one
     *  1 : second one
     */    
    public int index = -1;
    
    /**start, end of this string insider of this block*/
    public int start, end;
    
    
    public int repeat = 0;
}

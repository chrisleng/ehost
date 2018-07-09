/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.tableExtraction;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author imed
 */
public class Block {
    
    /**The file that current block belongs to.*/
    public File file;
    
    public Block(File file){
        this.file = file;
    }
    
    /**the index that we can use to find a specific block, it's in a (0-n) order.*/
    public int index = -1;
    
    /**the start and end of this block in the original document.*/
    public int blockStart = -1, blockEnd = -1;
    
    /**string contents of this block*/
    public ArrayList<String> blockcontents = new ArrayList<String>();
    
    /** smaller than 0  : unclassified yet
     *  equal to 0 : can't be identified
     *  1   : standard table
     *  2   : standard table, ( more than one table )
     *  
     */
    public int type = 0;
    
    /**array list that used to store table variable+values that we found from 
     * current block.
     */
    public ArrayList<Light> lights = new ArrayList<Light>();
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Chris
 */
public class SubRootOfRelationship extends DefaultMutableTreeNode {
    
    /**the number of relationships we have for this file or for all files*/
    public int size;
    
    /**the number of type of relationship*/
    public int sizeOfType;
    
    /**construction method */
    public SubRootOfRelationship( int size, int sizeOfType){
        this.size = size;
        this.sizeOfType = sizeOfType;
    }
    
    public String toString(){
        return "<html><font color=blue><b>Relationships:</b>"
                    + "<font color=gray>["
                    + size + "/" + sizeOfType
                    + "]</font></html>";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotationClasses.Depot;

/**
 *
 * @author imed
 */
public class NodeOfClassRoot extends DefaultMutableTreeNode {
    
    public int totalClassType = 0;
    public int totalAnnotationType = 0;
    public int totalAnnotation = 0;
    
    public String displayText;
    
    public NodeOfClassRoot(int totalClassType, int totalAnnotationType, int totalAnnotation ){        
        this.totalClassType = totalClassType;
        this.totalAnnotationType = totalAnnotationType;
        this.totalAnnotation = totalAnnotation;
    }
    
    public String getText(){
        displayText = "<html> <font color=green>"
                    + totalAnnotationType
                    + "</font> Classes: <font color=gray>["
                    + totalAnnotationType
                    + "/"
                    + totalAnnotation
                    + " annotations]<font><html>";
        
        return this.displayText;
    }
}

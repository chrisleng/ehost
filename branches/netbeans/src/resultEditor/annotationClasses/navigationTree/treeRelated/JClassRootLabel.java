/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import javax.swing.JLabel;

/**
 *
 * @author imed
 */
public class JClassRootLabel extends JLabel {
    
    

    public int totalClassType = 0;
    public int totalAnnotationType = 0;
    public int totalAnnotation = 0;
    
    public String displayText;
    
    public String getText(){
        displayText = "<html> <font color=green>"
                    + totalClassType
                    + "</font> Classes: <font color=gray>["
                    + totalAnnotationType
                    + "/"
                    + totalAnnotation
                    + " annotations]<font><html>";
        super.setText(displayText);
        return this.displayText;
    }
    public JClassRootLabel(int totalClassType, int totalAnnotationType, int totalAnnotation ){
        super();
        this.setOpaque( false );
        this.totalClassType = totalClassType;
        this.totalAnnotationType = totalAnnotationType;
        this.totalAnnotation = totalAnnotation;
    }
    
    public JClassRootLabel( ){
        super();
        this.setOpaque( false );
    }
    
    @Override
    public void setText(String  displayText){
        this.displayText = displayText;
        super.setText(displayText);        
    }
    
    public String toString(){
        return this.getText();
    }
}
    
  

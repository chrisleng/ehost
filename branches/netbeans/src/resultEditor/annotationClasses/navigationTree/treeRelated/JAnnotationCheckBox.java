/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import javax.swing.JLabel;
//import javax.swing.UIManager;

/**
 *
 * @author leng 
 */
/**New java swing conponent extend from JCheckBox.*/
public class JAnnotationCheckBox extends JLabel{

    protected String uniqueAnnotationText;
    protected int uniqueAnnotationAmount;

    public boolean isNotAppeared_in_CurrentArticle = false;

    public Color iconcolor;

    private boolean isHighlighted = false;

    /*Color selectionBorderColor, selectionForeground, selectionBackground,
        textForeground, textBackground;
*/

    public JAnnotationCheckBox(){
        super();
        this.setOpaque(true);
        /*//this.setVerticalTextPosition(0);
        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");

        if (isHighlighted){

            this.setForeground(selectionForeground);
            this.setBackground(selectionBackground);
        } else {
            this.setForeground(textForeground);
            this.setBackground(textBackground);
        }*/

    }

    /*
    public void setHighlight(boolean isHighlighted){
         if (isHighlighted){

            this.setForeground(selectionForeground);
            this.setBackground(selectionBackground);
        } else {
            this.setForeground(textForeground);
            this.setBackground(textBackground);
        }
    }*/

    public void setUniqueAnnotationText(String text){
        this.uniqueAnnotationText = text;
    }

    public String getUniqueAnnotationText(){
        return this.uniqueAnnotationText;
    }

    public void setUniqueAnnotationAmount(int uniqueAnnotationAmount){
        this.uniqueAnnotationAmount = uniqueAnnotationAmount;
    }

    public int getUniqueAnnotationAmount(){
        return this.uniqueAnnotationAmount;
    }




}

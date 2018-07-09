/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JCheckBox;
import resultEditor.annotationClasses.navigationTree.dataCache.Att;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 * New java swing component extend from JCheckBox; it used to display attribute
 * information on the navigation panel.
 * 
 * @author  Chris Leng
 */
public class JAttributeCheckBox extends JCheckBox {
    
    //protected NodeOfAttribute attributeNode;
    private boolean hasFocused = false;
    private Att att;
    public String displayText;
    //protected resultEditor.annotationClasses.
    
    
    public void setFocused(boolean isHighlighted){
        this.hasFocused = isHighlighted;
    }
    
    public boolean hasFocused(){
        return this.hasFocused;
    }
    
    //@Override
    //public void setText(String attname){
    //    super.setText(attname);
    //}
    
    public Att getAtt(){
        return att;
    }
    
    public void setAtt(Att att){
        this.att = att;
    }
    
     public JAttributeCheckBox(Att att){
        super();
        this.att = att;
        this.setIconTextGap(24);  
        //this.setIcon( new javax.swing.ImageIcon(getClass().getResource("/res/attributeDef.gif")) );
        
        
            
    }
             
     public String getText(){
         if(att == null)
             return null;
         if( att.isPublic ){
             if( DepotOfAttributes.isFilterOn() )
                 return "<html><font color=green>" + this.att.attributeName + "</font><font color=gray> (public) ["+ att.count +"]</font></html>";
             else
                 return "<html><font color=gray>" + this.att.attributeName + " (public) ["+ att.count +"]</font></html>";
         }else{
             if( DepotOfAttributes.isFilterOn() )
                 return  "<html><font color=blue>" + this.att.attributeName + " ["+ att.count +"]</font></html>" ;
             else
                 return  "<html><font color=gray>" + this.att.attributeName + " ["+ att.count +"]</font></html>" ;
         }
     }
     
     public String getDisplayText(){
         return this.getText();
     }
     
     public boolean isPublic(){
         return att.isPublic;
     }
     
     
    static Image image = null; 
    
    @Override
     public void paint(Graphics g){
         super.paint(g);
         if( image == null ){
             image =  new javax.swing.ImageIcon(getClass().getResource("/res/attributeDef.gif")).getImage();
         }
         g.drawImage(  image, 26, 2, this);
         
     }


}

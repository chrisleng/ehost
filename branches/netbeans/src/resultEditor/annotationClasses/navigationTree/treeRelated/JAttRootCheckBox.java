package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JCheckBox;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 * This is the modified jcheckbox component used to show the root node of all
 * attributes on the navigation tree view.
 *
 * @author Jianwei Chris Leng
 * @since July 18,2012
 */
public class JAttRootCheckBox extends JCheckBox {

    /**
     * the text that will be display on this "checkbox.
     */
    public String text;  
    
    public int count = 0;
    
    public String classname = null;

    /**flag that used to indicate whether the checkbox has been selected or not.*/
    public boolean isSelected;
    
    /**set the flag to indicate whether the checkbox has been selected or not.*/
    public void setSelectionStatus(boolean isSelected){
        this.isSelected = isSelected;
        super.setSelected( isSelected );
    }
    
    /**get the flag to tell us whether the checkbox has been selected or not.*/
    public boolean getSelectionStatus(){
        return this.isSelected;
    }
    

    public void setCount(int count){
        this.count = count;
    }
    
    public void setClassname(String classname){
        this.classname = classname;
    }
    
     
    /**
     * constructor
     */
    public JAttRootCheckBox() {
        //super( icon, true );                
        super();
        super.setBackground(Color.white);
        super.setIconTextGap(24);
        //this.set
        //this.setIcon( new javax.swing.ImageIcon(getClass().getResource("/res/attributeDef.gif")) );
        //this.setSelectedIcon(icon_attribute);
        
        
        
    }
    
    @Override
    public void setText(String text){
        this.text = text;
        super.setText(text);
    }
    
    

    /**
     * This is the default text that will be displayed on the screen if it isn't 
     * overwrote by the matched cell renderer function.
     * 
     * ##### IMPORTANT ####
     * 
     */
    @Override
    public String getText() {
        if(DepotOfAttributes.isFilterOn()){
            if ( this.classname == null )
                return "<html><font color=green>"+ count +"</font><font color=black> Public Attributes:</font></html>";
            else
                return "<html><font color=green>"+ count +"</font><font color=black> Attributes of \""+ this.classname +"\":</font></html>";
            }
            
        else{
            if ( this.classname == null )
                return "<html><font color=gray>"+ count +" Public Attributes:</font></html>";
            else{
                return "<html><font color=gray>"+ count +" Attributes of \""+ this.classname +"\":</font></html>";
            }
        }
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

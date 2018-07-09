/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JCheckBox;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;

/**
 *
 * @author leng
 */
/**
 * New java swing component extend from JCheckBox.
 */
public class JClassCheckBox extends JCheckBox {

    public Color c;
    public int annotationAmount;
    public int annotationTypeAmount;
    public String classname;
    public String displayText;
    public String shortcomment;
    private String des;
    protected boolean isSelected_toCreateAnnotation = false;
    protected boolean isHighlighted = false;

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public boolean setHighlighted() {
        return this.isHighlighted;
    }

    public JClassCheckBox() {
        super();
        this.setIconTextGap(27);
        //if(( des != null ) && (des.trim().length()>0))
            this.setToolTipText( des );
        
    }
    
    
    public void setDes(String des){
        this.des = des;
        this.setToolTipText( des );
    }
    
    
    public String getDes(){
        return des;
    }

    public void setflag_isSelected_toCreateAnnotation(boolean flag) {
        isSelected_toCreateAnnotation = flag;
    }

    public boolean isSelected_toCreateAnnotation() {
        return isSelected_toCreateAnnotation;
    }

    public void setClassColor(Color c) {
        this.c = c;
    }

    public Color getClassColor() {
        return c;
    }
    
    

    public void setAnnotationAmount(int amount) {
        this.annotationAmount = amount;
    }

    public int getAnnotationAmount() {
        return this.annotationAmount;
    }

    public String getDisplayText(){
        if(( DepotOfAttributes.isFilterOn() )&&(DepotOfAttributes.getClassname()!=null)
                &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
                &&(DepotOfAttributes.getClassname().compareTo( this.classname )!=0)){
            
            if ( this.annotationTypeAmount > 0 ){
                String text = this.classname;
                text = "<html><font color=d1d1d1>" + text;
                text = text + " [<b>"
                    + this.annotationTypeAmount
                    + "/"
                    + this.annotationAmount
                    + "</b></font>]</html>";
                this.displayText = text;
                return text;
            }else{
                return "<Html><font color=d1d1d1>"+this.classname+"</font></html>";
            }
        }else{
            if ( this.annotationTypeAmount > 0 ){
                String text = this.classname;
                text = "<html>" + text;
                text = text + " [<b><font color=blue>"
                    + this.annotationTypeAmount
                    + "/"
                    + this.annotationAmount
                    + "</b></font>]</html>";
                this.displayText = text;
                return text;
            }else{
                return this.classname;
            }
        }
    }
    
    public void setAnnotationTypeAmount(int amount) {
        this.annotationTypeAmount = amount;
    }

    public int getAnnotationTypeAmount() {
        return this.annotationTypeAmount;
    }

    public void setClassname(String classname) {
        this.classname = classname.trim();
        
        //if(( DepotOfAttributes.isFilterOn() )&&(DepotOfAttributes.getClassname()!=null)
        //        &&(DepotOfAttributes.getClassname().compareTo("NULL_NULL_NULL")!=0)
        //        &&(DepotOfAttributes.getClassname().compareTo( this.classname )!=0)){
        //    this.setSelected( true );
        //    this.isHighlighted = true;
        //}
    }

    public String getClassname() {
        return this.classname;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw color block
        //g2.setColor( new Color(150,150,150) );
        g2.setColor(new Color(30, 30, 30));
        g2.drawRect(getX() - 14, 2, 14, 14);
        // fill rect
        if (c == null) {
            c = Color.CYAN;
        }

        g2.setColor(c);
        g2.fillRect(getX() - 13, 3, 13, 13);


        /* 
         if (DepotOfAttributes.isFilterOn()) {
            if ((DepotOfAttributes.getClassname() != null)
                    && (DepotOfAttributes.getClassname() != "NULL_NULL_NULL")) {
                if (DepotOfAttributes.getClassname().compareTo(classname) != 0) {
                    g2.setColor(Color.gray);
                    g2.drawLine(45, 10, this.getWidth(), 10);
                }
            }
        }
        */

        // following codes are used to draw the cross lines to indicate that 
        // this class is set to be invisible or visible.
        if (!isSelected_toCreateAnnotation) {
            return;
        }

        // set color of corss line, which indicates selected annotatin class
        Color crossLineColor = Color.black;
        if ((c.getRed() + c.getBlue() + c.getGreen()) / 3 < 50) {
            crossLineColor = Color.white;
        }
        g2.setColor(crossLineColor);

        // draw cross line
        g2.drawLine(getX() - 14, 3, getX() - 14 + 13, 3 + 13);
        g2.drawLine(getX() - 13, 3 + 13, getX() - 13 + 13, 3);
    }
}

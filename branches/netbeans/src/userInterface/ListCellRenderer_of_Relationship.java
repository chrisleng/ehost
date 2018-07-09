/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface;

/**
 *
 * @author Chris
 */

import adjudication.SugarSeeder;
import java.awt.*;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;
import resultEditor.annotations.AnnotationRelationship;
import resultEditor.workSpace.WorkSet;


public class ListCellRenderer_of_Relationship extends RelationshipComponent implements ListCellRenderer {

    private static final long serialVersionUID = -2575287177726702542L;
    private static final Color HIGHLIGHT_COLOR = new Color(180, 180, 180);
    
    protected ListEntryofRelationship entry;

    public ListCellRenderer_of_Relationship() {        
    }

    public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

        entry = (ListEntryofRelationship) value;

        super.setRelationship( entry.getAnnotation() , entry.getRel() );                        
        
        //this.setIcon( entry.getImage() );
        //this.setVerticalTextPosition(1);

        // System.out.println("选择状态：" + entry.isSelected());        
        entry.setSelected(isSelected);
        
        if (entry.isSelected()) {
            super.setBackground( new Color(0,51,100) );            
        } else {
            super.setBackground(Color.white);            
        }

     
        
        return this;
        
    }

   
}


 class RelationshipComponent extends JLabel {

    /**Jianwei Leng 2012-06-14
    * @param args
    */    
    protected AnnotationRelationship relationship;
    protected Annotation annotation;

    
    public void setRelationship(Annotation annotation, AnnotationRelationship relationship){
        this.relationship = relationship;     
        this.annotation = annotation;
        int height = calculateHeight();       
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(0, height));
    }

    public AnnotationRelationship getRelationship(resultEditor.annotations.Annotation annotation){
        return this.relationship;
    }

    public RelationshipComponent(){
        super();        
    }  


    /**count and return how many valid attributes on this relationship*/
    private int sizeOfAttributes(){
        if( this.relationship == null )
            return 0;
        if( this.relationship.attributes == null )
            return 0;
        
        int count = 0;
        for( AnnotationAttributeDef attribute : this.relationship.attributes ){
            if(( attribute == null )||(attribute.name==null)||(attribute.value==null))
                continue;
            if( (attribute.name.trim().length() < 1)||(attribute.value.trim().length() < 1))
                continue;
            
            count++;
                
        }
        return count;
    }
    
    private int calculateHeight(){
        return (15 + 14*(1 + sizeOfAttributes()) ); 
    }
    /** rewrote method of paintComponet() */
    @Override
    public void paint(Graphics g) {
        try {
            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            int width = g2.getClipBounds().width - 4;
            int height = this.calculateHeight();
            //g2.getClipBounds().height - 4;
            // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // draw color block
            g2.setColor(Color.white);
            g2.drawRect(4, 1, width - 2, height - 3); 
            g2.setColor(Color.black);
            //g2.drawRect(5, 3, width - 5, height - 5);
            
            // fill rect
            //if (color == null) {
            //        color = Color.CYAN;
            //}
            g2.setColor(new Color(200,200,200));

            g2.fillRect(6, 3, width - 5, height - 6);

            
            
            
            
            //g2.setColor(Color.white); // default font color: black
            //g2.fillRect(6, 1, width - 5 , 30);
            
            
            // #### 2 ####
            // draw the path that another annotation linked to current annotation
            
            
            // draw relationship name on the center
            int componentWidth = this.getWidth();
            
            /*
            Font   TEXT_FONT   =   new   Font( "Calibri",   java.awt.Font.PLAIN,   10); 
            FontMetrics   fm   =   g.getFontMetrics(TEXT_FONT); 
            int   strWidth   =   fm.stringWidth( relationship.getMentionSlotID() );
            int startx = (int)((componentWidth - strWidth)/2);
            // draw text
            g2.setFont(new Font("Calibri", Font.BOLD, 11));            
            g2.setColor(Color.blue); // default font color: black
            g2.drawString( "(" + relationship.getMentionSlotID() + ")", startx,6+10 );            
            */
            
            /*
            // draw the path line
            Color linecolor1 = new Color(213,221,224);
            Color linecolor2 = new Color(197,205,208);
            Color linecolor3 = new Color(174,184,186);
            Color linecolor4 = new Color(201,211,213);
            
            
            // draw the line
            g2.setColor(linecolor1);           
            g2.drawLine( 28 , 18, componentWidth -28, 18);
            g2.setColor(linecolor3);
            g2.drawLine( 28 , 19, componentWidth -28, 19);
            g2.setColor(linecolor4);
            g2.drawLine( 28 , 20, componentWidth -28, 20);
          
            // draw the start point
            g2.setColor( Color.black );
            g2.fillOval( 22, 16, 8, 8);
            g2.setColor(linecolor3);
            g2.fillOval( 24, 14, 8, 8);
            g2.setColor( Color.blue );
            g2.fillOval( 26, 16, 4, 4);
            
            // draw the end arrow
            g2.setColor(new Color(122,19,20));
            g2.drawLine( componentWidth - 32, 19, componentWidth -28, 19);
            g2.drawLine( componentWidth - 32, 18, componentWidth -28, 18);
            g2.drawLine( componentWidth - 32, 20, componentWidth -28, 20);
            
            g2.drawLine( componentWidth - 27, 19, componentWidth - 27, 19 );
            g2.drawLine( componentWidth - 29, 18, componentWidth - 29, 21);
            g2.drawLine( componentWidth - 30, 17, componentWidth - 30, 22);
            g2.drawLine( componentWidth - 31, 16, componentWidth - 31, 23);
            g2.drawLine( componentWidth - 31, 15, componentWidth - 31, 24);
            
            
            // draw the annotation of the start point
            g2.setColor( new Color(158, 28, 30) );
            g2.setFont(new Font("Calibri", Font.BOLD, 10));
            String firstAnnotation = getSubStr( annotation.annotationText, 16 );
            int   widthOfFirstAnnotation   =   fm.stringWidth( firstAnnotation );
            int xOfFirstAnnotation = (int)(componentWidth - widthOfFirstAnnotation - 16);            
            g2.drawString( firstAnnotation, xOfFirstAnnotation , 32);
            
            
            // draw text content of linked annotation
            g2.setColor( new Color(158, 28, 30) );
            g2.setFont(new Font("Calibri", Font.BOLD, 10));
            g2.drawString( getSubStr( relationship.linkedAnnotations.get(0).linkedAnnotationText, 16 ) , 14, 32 );
             
            */
            
            /*
            
            g2.setColor(Color.black); // default font color: black
            g2.setFont(new Font("Calibri", Font.BOLD, 10));
            g2.drawString( "Linked to Annotation: " , 14,19+10 );
            g2.setColor(Color.BLUE); // default font color: black
            g2.setFont(new Font("Calibri", Font.PLAIN, 10));
            g2.drawString( "\"" +relationship.linkedAnnotations.get(0).linkedAnnotationText + "\"" , 14+90,19+10 );
            */
            String linkedAnnotationText = this.getlinkedAnnotationText( relationship.linkedAnnotations.get(0).linkedAnnotationIndex );
            g2.setFont(new Font("Calibri", Font.BOLD, 12));            
            g2.setColor(Color.blue); // default font color: black
            g2.drawString( "--- (" + relationship.getMentionSlotID() 
                    + ")  --->  \"" + linkedAnnotationText + "\""
                    , 20 ,6+10 );            
            
            //g2.setColor(Color.black);
            //g2.drawLine( 14, 17, 100, 17);
            //g2.drawLine( 97, 15, 100, 17);
            //g2.drawLine( 97, 19, 100, 17);
            
            //g2.drawString( "\"" +relationship.linkedAnnotations.get(0).linkedAnnotationText + "\"" , 100, 22 );
            
            int size = sizeOfAttributes();
            int count = 0;
            
            for( AnnotationAttributeDef attribute :  relationship.attributes){
                
                if( attribute == null )
                    continue;
                if(( attribute.name == null )||(( attribute.name.trim().length()<1 )))
                    continue;
                if(( attribute.value == null )||(( attribute.value.trim().length()<1 )))
                    continue;
                
                g2.setColor(Color.black);
                g2.setFont(new Font("Calibri", Font.PLAIN, 11));
                //String name = getSubStr( attribute.name , 30 );
                // String value = "[ " + getSubStr( attribute.value , 30 ) + " ]";
                g2.drawString( attribute.name + " = " + attribute.value  , 19  ,19+10 + 4 + count*12 );                
                //g2.drawString( value  , (int)(componentWidth/2) + 10  ,19+10 + 4 + count*12 );                
                //g2.setColor( Color.black );
                //g2.drawString( "="  , (int)(componentWidth/2)  ,19+10 + 4 + count*12 );
                count++;
            }
            
        } catch (Exception ex) {
            System.out.println("1203021619");
            ex.printStackTrace();
        }
        
        
    }

     private String getlinkedAnnotationText(int linkedAnnotationIndex) {
         String textOfLinkedAnnotation = null;
         try {
             resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
             String filename = WorkSet.getCurrentFile().getName();
             int uniqueindex = linkedAnnotationIndex;
             
             Annotation ann = null;
             if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                 adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                 ann = depotOfAdj.getAnnotationByUnique(filename, uniqueindex);
             } else {
                 ann = depot.getAnnotationByUnique(filename, uniqueindex);
             }
             if (ann == null) {
                 return null;
             }
             
             textOfLinkedAnnotation =  ann.annotationText;
             
             if ((ann.adjudicationAlias != null) && (!SugarSeeder.isAdjudicationAlias(
                     ann.adjudicationAlias))) {
                 String index = ann.adjudicationAlias;
                 Annotation ann2 = depot.getAnnotationByUnique(filename, Integer.valueOf(index));
                 if (ann2 != null) {
                     textOfLinkedAnnotation = ann2.annotationText;
                 }
             }
         } catch (Exception ex) {
             ex.printStackTrace();
         }
         return textOfLinkedAnnotation;
     }
    /**get first n character of this */
    private String getSubStr(String str, int maxLength){
        if(str==null)
            return "";
        if( str.length() <= maxLength )
            return str;
        
        return str.substring(0, maxLength) + " ...";
    }
 }
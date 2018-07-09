/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package navigatorContainer;

/**
 *
 * @author leng
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Chris
 */

import java.awt.Color;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JList;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JPanel;




public class ProjectListCellRander extends RowPainter implements ListCellRenderer {
  private static final Color HIGHLIGHT_COLOR = new Color(50, 50, 128);
  private Color categorycolor;

  private resultEditor.annotations.Annotation annotation;

  public ProjectListCellRander() {
    setOpaque(true);
    //setIconTextGap(55);
    //setPreferredSize(new Dimension(0,24));
    setFont(new Font("Calibri", Font.PLAIN, 13));
  }

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected, boolean cellHasFocus) {

    ListEntry_Project entry = (ListEntry_Project) value;
    //setText( entry.getAnnotation().annotationText );
    super.setFolder( entry.getFolder() );
    super.setImage( entry.getImage() );
    super.setNumberOfCorpus( entry.getNumberOfCorpus() );
    //super.setIconTextGap(4);
    super.setPreferredSize( new Dimension(0, HEIGHT ) );
    //super.setVerticalAlignment(TOP);
    //setIcon( entry.getImage() );
    //setVerticalTextPosition(1);
    //categorycolor = entry.getColor();
    //setBlockColor( categorycolor );
    //super.setCategory( this.annotation.annotationclass );

    if (isSelected) {
      setBackground(Color.blue);
      setForeground(Color.white);
    } else {
      setBackground(Color.white);
      setForeground(Color.black);
    }
    return this;
  }



}



class RowPainter extends JPanel {

/**Jianwei Leng 2010-07-08
* @param args
*/

    protected final int HEIGHT = 55;
    protected Color color = null;
    protected Image image;
    protected File folder;
    protected int numberOfCorpus=-2;


    public void setFolder(File folder){
        this.folder = folder;
    }
    public void setImage(Image image){
        this.image = image;
    }
    

    public RowPainter(){
        super();
    }

    public RowPainter(File folder, Image image){
        super();
        this.folder = folder;
        this.image = image;
    }

    public void setNumberOfCorpus(int number){
        this.numberOfCorpus = number;
    }

    
    /** rewrote method of paintComponet() */
    @Override
    public void paint(Graphics g){
      super.paint(g);
      Graphics2D g2=(Graphics2D)g;
      //int width = g2.getClipBounds().width - 4;
      int width = 220 - 4;
      int height = 50;//g2.getClipBounds().height - 4;
      // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // draw color block
      g2.setColor( Color.white);
      g2.fillRect( 2, 2, width-1, height-3 );
      g2.setColor( new Color(40,53,111) );
      g2.fillRect( 3 , 3, width - 3, height - 5);


      g2.setColor(Color.white);

      g2.fillRect( 9 , 4, width - 10, height - 7);
      // draw text

      String capital = folder.getName();
      Graphics2D g2d = (Graphics2D)g;
      Font tabtextfont = new Font("Arial", Font.BOLD, 11 );
      g2d.setFont( tabtextfont );
      int textwidth = g2d.getFontMetrics().stringWidth(capital);
      int validwidth = this.getWidth() - 60 - 4;
      int size=capital.length();
      String str1=null, str2=null;
      boolean toolong = false;
      for(int i=0;i<size;i++){
          String str = capital.substring(0, i);
          if(g2d.getFontMetrics().stringWidth(str)>validwidth){
              str1=capital.substring(0, i-1);
              toolong = true;
          }
      }

      if(toolong){
            for(int i=str1.length();i<size;i++){
                String str = capital.substring(str1.length(), i);
                if(g2d.getFontMetrics().stringWidth(str)>validwidth){
                    str2=capital.substring(str1.length(), i-1);
                }
            }
      }


      g2.setColor( Color.black );
      g2.setFont(tabtextfont);
      if(!toolong)
        g2.drawString( capital , 60, 15 );
      else{
        g2.drawString( str1 , 60, 15 );
        if((str2!=null)&&(str2.length()>0))
            g2.drawString( str2 , 60, 29 );
      }
      //g2.drawString( "Line.txt" , 60, 29 );
      

      // draw number of corpus
      g2.setFont( new Font("Arial", Font.PLAIN, 11 )) ;
        if(this.numberOfCorpus>=0)
            g2.drawString( "Corpus: " + this.numberOfCorpus, 60, 41);
        else
            g2.drawString( "Corpus: UNKNOWN", 60, 41);


      // draw the image
      if(image==null)
          log.LoggingToFile.log(Level.SEVERE, "error 1102081103:: lost the image resource!!!");
      g2.drawImage(image, 19, 4, this);


      
    }

    public void setBlockColor(Color color)
    {
        this.color = color;
    }

}

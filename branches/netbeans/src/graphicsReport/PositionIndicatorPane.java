/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphicsReport;

import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.logging.Level;
import javax.swing.JPanel;
import resultEditor.annotations.SpanDef;

/**
 *
 * @author Chris
 */
public class PositionIndicatorPane extends JPanel{
    private Graphics2D offScreenGraphics;
    private Image offScreenImage = null;
    final int fixed_width = 20;
    final int fixed_seperator_width = 9;
    protected static int h,w;

    final int fixed_title_height = 90;

    protected long max_length_of_document = 0;

    public PositionIndicatorPane(){
        super();
        this.setDoubleBuffered(true);

        // set the papaer size before painting
        setPaperSize();

        // paint position indicators for all documents
        //paintAll();
    }

    public void paintAll(){
        max_length_of_document = getmaxsize();
        renderOffScreenGraphics(offScreenGraphics);
    }

    
    /**overrided paint() method of the extended JPanel component. We used this
     * to generate a picture of all annotatin position map in memory and then
     * load it into the screen for display. This can be considered as "double
     * buffered".
     */
    @Override
    public void paint(Graphics g){
        super.paint(g);

        if(offScreenImage==null){
            if(this.isVisible()){
                if((w>0)&&(h>0)){
                    offScreenImage = createImage(w, h);
                    offScreenGraphics = (Graphics2D)offScreenImage.getGraphics();
                    offScreenGraphics.setClip(0, 0, getWidth(), getHeight());
                    paintAll();
                }
            }
        }

        if(offScreenImage!=null)
            g.drawImage(offScreenImage, 0, 0, null);
    }


    /**Change the size of canvas, and then refresh the screen by reruning the
     * paint() method. */
    public void sizeChanged(int _w, int _h){
        if(_w>0) w = _w;
        if(_h>0) h = _h;
        
        offScreenImage = null; // the paint() will not refresh the screen
                               // without setting offscreenimage to null.
        this.paint(this.getGraphics());
    }

    private static int iii=0;

    /**The enterance method which can call paintIndicator_of_OneFile() to
     * paint all annotation positions to build a annotation map.
     */
    protected void renderOffScreenGraphics(Graphics2D _og){
        try{
            File[] corpus = env.Parameters.corpus.getFiles();
            int count=0;
            for(File file: corpus)
            {

                int x = count*fixed_width + count*fixed_seperator_width;
                int y = 0;
                if((file==null)||(!(file.exists())))
                    continue;
                paintIndicator_of_OneFile(file, _og, x, y);
                count++;
            }            
        }catch(Exception ex){
        }
    }

    /**Find the bigest size of all text source files in current project.*/
    private long getmaxsize()
    {
        long max=0;
        
        try{
            File[] corpus = env.Parameters.corpus.getFiles();
            for(File file: corpus)
            {
                if((file==null)||(!file.exists()))
                    continue;
                long length = file.length();
                if(length>max)
                    max=length;

                //System.out.println("max=" + max);
            }
        }catch(Exception ex){
            return 0;
        }
        
        return max;
    }

    /**Draw annotation indicators */
    private void paintIndicator_of_OneFile(File file, Graphics2D _og, int x, int y)
    {
        try{
            

            // got all annotaions
            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            File[] corpus = env.Parameters.corpus.getFiles();
            if(corpus == null)
                return;

           
                String filename = file.getName();

                // draw bounder
                _og.setColor(new Color(10,100,200));
                long filelength = file.length();
                int newheight = (int)((h-this.fixed_title_height)*((float)filelength/this.max_length_of_document));
                _og.drawRect(x, y, this.fixed_width, newheight);
                
                printVerticalText(file.getName(), _og, x+15, h - this.fixed_title_height+20);
                
                //iii++;
                //System.out.println("[============== " + iii + " ===============" );
                //System.out.println("h="+h);
                //System.out.println("filelength="+file.length());
                //System.out.println("max="+this.max_length_of_document);
                //System.out.println("rate="+(file.length()/this.max_length_of_document));


                Article article = Depot.getArticleByFilename(filename);
                if(article==null)
                    return;
                if(article.annotations==null)
                    return;

                for(Annotation annotation: article.annotations)
                {
                    if(annotation==null)
                        continue;

                    // indicator color
                    Color color = resultEditor.annotationClasses.Depot.getColor( annotation.annotationclass );
                    if(color==null) color = Color.black;                    
                    _og.setColor(color);

/*
                    int start = annotation.spanstart, end = annotation.spanend;
                    int i_height = (int)(newheight*(start/(float)filelength));
                    int i_endheight = (int)(newheight*(end/(float)filelength));
                    int rectheight = i_endheight - i_height;
                    if(rectheight<1)
                        rectheight=1;
                    _og.fillRect(x, i_height, fixed_width, rectheight);
*/
                if ((annotation.spanset != null) && (annotation.spanset.size_nonNull() > 0)) {
                    for (int i=0; i<annotation.spanset.size();i++) {
                        SpanDef span = annotation.spanset.getSpanAt(i);
                        if(span==null)
                            continue;
                        int start = span.start;
                        int end = span.end;
                        int i_height = (int) (newheight * (start / (float) filelength));
                        int i_endheight = (int) (newheight * (end / (float) filelength));
                        int rectheight = i_endheight - i_height;
                        if (rectheight < 1) {
                            rectheight = 1;
                        }
                        _og.fillRect(x, i_height, fixed_width, rectheight);
                    }
                }
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1103250416::" + ex.toString() );
        }
    }

    /**Paint a vertical text string on screen with deisgned coordinates.
     * @param x     coordinate x on the canvas
     * @param x     coordinate x on the canvas
     */
    private void printVerticalText(String text, Graphics2D g2d, int x, int y)
    {
        if ( text == null )
            return;

        AffineTransform ot = g2d.getTransform();
        g2d.setFont( new Font("Arial", Font.BOLD, 10 ) );
        g2d.setColor(new Color(21,66,139));
            AffineTransform at = new AffineTransform();
            //System.out.println(at.toString());
            at.setToRotation( Math.PI / 2.0 );//, getWidth()/2.0, getHeight()/2.0 );


            AffineTransform af = g2d.getTransform();
            af.translate(x, y);
            af.concatenate( at );

            g2d.setTransform(af);

        g2d.drawString(text, 1-10, 1+10);
        //System.out.println("x= " + x + ", y= "+y);
        g2d.setTransform(ot);


    }

    /**Design the screen size or this component based on the number of raw text
     * documents in current project.*/
    private void setPaperSize(){
        int documentnumbers = numberOfDocumnets();
        if(documentnumbers<1)
            return;

        // h = int height;
        if (this.getHeight()<526)
            h=526;
        else h = this.getHeight();



        w = this.getWidth();
        int miniwidth = fixed_width*documentnumbers + (documentnumbers-1)*fixed_seperator_width+1;

        if(miniwidth > w)
            w = miniwidth;


        this.setPreferredSize(new Dimension(w, h));                
    }

    /**Get number of raw text files of current project.
     * @return  number of text files in current project.
     */
    private int numberOfDocumnets(){
        int numbers = 0;
        File[] corpus = env.Parameters.corpus.getFiles();
        if(corpus==null)
            return 0;
        else
            numbers = corpus.length;

        //System.out.println("==============total files:" + numbers);

        return numbers;
    }

}

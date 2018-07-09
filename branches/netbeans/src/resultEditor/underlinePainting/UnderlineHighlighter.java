/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.underlinePainting;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;
/**
 *
 * @author Jianwei Leng Tuesday, June 8, 2010, 11:19 am MST
 */
public class UnderlineHighlighter extends DefaultHighlighter{

    // Shared painter used for default highlighting
    protected static final Highlighter.HighlightPainter sharedPainter
            = new SelectionHighlightPainter(null);

    // Painter used for this highlighter
    protected Highlighter.HighlightPainter painter;

    // colors defined to draw wave line
    protected static final Color red = new Color(255, 19, 19);
    protected static final Color pink = new Color(255, 192, 192);
    protected static final Color white = Color.white;


    // c is the color of underline
    public UnderlineHighlighter(Color c) {
        painter = (c == null ? sharedPainter : new SelectionHighlightPainter(c));
    }

    // Convenience method to add a highlight with
    // the default painter.
    public Object addHighlight(int p0, int p1) throws BadLocationException {
        return addHighlight(p0, p1, painter);
    }

    @Override
    public void setDrawsLayeredHighlights(boolean newValue) {
        // Illegal if false - we only support layered highlights
        if (newValue == false) {
        throw new IllegalArgumentException(
            "UnderlineHighlighter only draws layered highlights");
        }
        super.setDrawsLayeredHighlights(true);
    }
    

    /** Painter for underlined highlights.
     *
     */
    public static class SelectionHighlightPainter extends
        LayeredHighlighter.LayerPainter {

        protected Color color; // The color for the underline

        // color to paint underline
        public SelectionHighlightPainter(Color c) {
            color = c;
        }

        public void paint(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c) {
            // Do nothing: this method will never be called
        }

        // triditional double lines highlighter
        
        /*public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c, View view) {

            g.setColor(color == null ? c.getSelectionColor() : color);

            Rectangle alloc = null;
            if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
                if (bounds instanceof Rectangle) {
                    alloc = (Rectangle) bounds;
                } else {
                    alloc = bounds.getBounds();
                }
            } else {
                try {
                    Shape shape = view.modelToView(offs0,
                    Position.Bias.Forward, offs1,
                    Position.Bias.Backward, bounds);
                    alloc = (shape instanceof Rectangle) ? (Rectangle) shape
                    : shape.getBounds();
                } catch (BadLocationException e) {
                    return null;
                }
            }

            FontMetrics fm = c.getFontMetrics(c.getFont());
            int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
            for(int i=0; i<=2;i++){
                g.setColor( i == 0 ?  Color.white : color );
                g.drawLine(alloc.x, baseline+i, alloc.x + alloc.width, baseline+i);
            }
            baseline = alloc.y + alloc.height + 1;
            for(int i=0; i<=2;i++){
                g.setColor( i == 2 ?  Color.white : color );
                g.drawLine(alloc.x, alloc.y + i, alloc.x + alloc.width, alloc.y+i);
            }
            

            return alloc;
        }*/
        
        public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c, View view) {

            //System.out.println( offs0 + ", " + offs1);
            //g.setColor(color == null ? c.getSelectionColor() : color);

            Rectangle alloc = null;
            if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
                if (bounds instanceof Rectangle) {
                    alloc = (Rectangle) bounds;
                } else {
                    alloc = bounds.getBounds();
                }
            } else {
                try {
                    Shape shape = view.modelToView(
                            offs0,
                            Position.Bias.Forward, 
                            offs1,
                            Position.Bias.Backward, 
                            bounds
                            );
                    alloc = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            FontMetrics fm = c.getFontMetrics(c.getFont());
            //int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
            /*for(int i=0; i<=2;i++){
                g.setColor( i == 0 ?  Color.white : color );
                g.drawLine(alloc.x, baseline+i, alloc.x + alloc.width, baseline+i);
            }
            baseline = alloc.y + alloc.height + 1;
            for(int i=0; i<=2;i++){
                g.setColor( i == 2 ?  Color.white : color );
                g.drawLine(alloc.x, alloc.y + i, alloc.x + alloc.width, alloc.y+i);
            }*/
            
            /*
            // gave us a totally white paper
            g.setColor( Color.white );
            g.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
            
            //
            Color c1 = new Color(65,65,65);
            g.setColor( c1 );
            g.fillRoundRect(alloc.x, alloc.y-2, alloc.width, alloc.height+3, 10, 10);
            
            //Color c2 = new Color(163,163,163);
            //g.setColor( c2 );
            //g.fillRoundRect(alloc.x+1, alloc.y-1, alloc.width-2, alloc.height+1, 10, 10);
            
            if(color==null)
                color = Color.YELLOW;
            g.setColor( color );
            //g.drawRoundRect(alloc.x, alloc.y, alloc.width, alloc.height, 10, 10);
            
            g.fillRoundRect(alloc.x+2, alloc.y, alloc.width-4, alloc.height-1, 8, 8);
            */
            g.setColor( Color.BLACK );
            if(( alloc.y<=4 )&&(alloc.y>=0)){
                g.drawLine( alloc.x, 4, alloc.x + alloc.width, 4 );
                g.drawLine( alloc.x, 5, alloc.x + alloc.width, 5 );
            }else{
                g.drawLine( alloc.x, alloc.y-3, alloc.x + alloc.width, alloc.y-3 );
                g.drawLine( alloc.x, alloc.y-2, alloc.x + alloc.width, alloc.y-2 );
            }
            g.setColor( Color.WHITE );
            if(( alloc.y<=4 )&&(alloc.y>=0)){
                g.drawLine( alloc.x, 6, alloc.x + alloc.width, 6 );
            }else{
                g.drawLine( alloc.x, alloc.y-1, alloc.x + alloc.width, alloc.y-1 );                
            }
            g.drawLine( alloc.x, alloc.y + alloc.height + 0, alloc.x + alloc.width, alloc.y + alloc.height + 0 );
            g.setColor( Color.BLACK );
            g.drawLine( alloc.x, alloc.y + alloc.height + 1, alloc.x + alloc.width, alloc.y + alloc.height + 1 );
            g.drawLine( alloc.x, alloc.y + alloc.height + 2, alloc.x + alloc.width, alloc.y + alloc.height + 2 );
            /*try{
                int r = color.getAlpha();
                int b = color.getBlue();
                int gg = color.getGreen();
                double grayLevel = r * 0.299 + gg * 0.587 + b * 0.114;
                Color textcolor =  grayLevel > 192 ? Color.BLACK : Color.white;
                
                //String str = c.getDocument().getText(offs0, offs1-offs0);
                //if(str!=null){                    
                //    g.setColor( textcolor );                
                //    g.drawString( str, alloc.x, alloc.y+alloc.height);
                //}
            }catch(Exception ex){
                //ex.printStackTrace();
                System.out.println("error occurred while highlight a selection!" + ex.getMessage() );
            }*/

            return alloc;
        }

        
        
      }

    /** Painter for wave underlined highlights.
     *
     */
    public static class WaveHighlightPainter extends
        LayeredHighlighter.LayerPainter {

        protected Color color; // The color for the underline

        // color to paint underline
        public WaveHighlightPainter() {
            //color = c;
        }

        public void paint(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c) {
            // Do nothing: this method will never be called
        }

        public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds,
            JTextComponent c, View view) {

            g.setColor(color == null ? c.getSelectionColor() : color);

            Rectangle alloc = null;
            if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
                if (bounds instanceof Rectangle) {
                    alloc = (Rectangle) bounds;
                } else {
                    alloc = bounds.getBounds();
                }
            } else {
                try {
                    Shape shape = view.modelToView(offs0,
                    Position.Bias.Forward, offs1,
                    Position.Bias.Backward, bounds);
                    alloc = (shape instanceof Rectangle) ? (Rectangle) shape
                    : shape.getBounds();
                } catch (BadLocationException e) {
                    return null;
                }
            }

            FontMetrics fm = c.getFontMetrics(c.getFont());

             int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
            /*for(int i=0; i<=2;i++){
                g.setColor( i == 0 ?  Color.white : color );
                g.drawLine(alloc.x, baseline+i, alloc.x + alloc.width, baseline+i);
            }
            */
            baseline = alloc.y + alloc.height + 1;
            int basex = alloc.x, basey=alloc.y+alloc.height+1;

            // draw top white line
            g.setColor(white);
            g.drawLine(basex, basey, basex + alloc.width, basey);


            g.setColor(pink);
            g.drawLine(basex, basey+1, basex+alloc.width, basey+1);
            g.drawLine(basex, basey+2, basex+alloc.width, basey+2);

            g.setColor(red);
            
            for(int ii=0; ii<Math.floor(alloc.width/4); ii++) {
                g.drawLine(basex+ii*4, basey+1, basex+ii*4+1, basey+1);
            }
            for(int ii=0; ii<Math.floor(alloc.width/4); ii++) {
                g.drawLine(basex+ii*4+2, basey+2, basex+ii*4+3, basey+2);
            }

            g.setColor(white);
            //Create Point2D.Double
            g.drawLine(alloc.x, alloc.y+1, alloc.x, alloc.y+1);


            // draw ave line by integrating mixed red and pink short line

            // draw botton white line
            //for(int i=0; i<=2;i++){
            //    g.setColor( i == 2 ?  Color.white : color );
            //    g.drawLine(alloc.x, alloc.y + i, alloc.x + alloc.width, alloc.y+i);
            //}


            return alloc;
        }



      }
}

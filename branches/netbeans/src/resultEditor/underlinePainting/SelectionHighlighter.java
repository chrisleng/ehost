/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.underlinePainting;

import resultEditor.annotations.Annotation;
import java.awt.Color;
import java.util.logging.Level;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import resultEditor.annotations.SpanDef;

/**
 * 1.Bind custom highlightpainter with a given text component.<p>
 * 
 * @author Jianwei Leng
 * @since 06-08-2010 11:40am tuesday
 */
public class SelectionHighlighter {
    
    protected JTextComponent comp;
    protected Highlighter.HighlightPainter underline_painter;
    //protected Highlighter.HighlightPainter conflict_painter;
    protected Highlighter.HighlightPainter wave_painter;
    


    /**initial method of class <code>HighlightPainter</code>.*/
    public SelectionHighlighter(JTextComponent comp){
        this.comp = comp;
        

        this.wave_painter = new UnderlineHighlighter.WaveHighlightPainter();
        
        // color 'red' only used to indicate which word is current active
        // whatever underline or a color block
    }


    /**
     * set new underline highlight by annotation constructure.
     */
    public void setNewUnderlineHightlight(Annotation annotation) {
        try {
            // get all hightlighter from current text component
            Highlighter highlighter = comp.getHighlighter();

            // Remove any existing underline highlights for last word
            Highlighter.Highlight[] highlights = highlighter.getHighlights();
            for (int i = 0; i < highlights.length; i++) {
                Highlighter.Highlight h = highlights[i];
                if (h.getPainter() instanceof UnderlineHighlighter.SelectionHighlightPainter) {                    
                    highlighter.removeHighlight(h);
                }
            }
            
            

            // add span for highlighting
            if (annotation.spanset != null) {
                if (annotation.spanset.size() > 0) {
                    for (int spanindex = 0; spanindex < annotation.spanset.size(); spanindex++) {
                        try {
                            SpanDef span = annotation.spanset.getSpanAt(spanindex);

                            if (annotation.annotationclass == null) {
                                continue;
                            }
                            String classname = annotation.annotationclass;
                            Color color = resultEditor.annotationClasses.Depot.getColor(classname);
                            if (color == null) {
                                color = Color.yellow;
                            }

                            if (span == null) {
                                continue;
                            }

                            this.underline_painter = new UnderlineHighlighter.SelectionHighlightPainter(color);



                            //System.out.println(span.start + ", " + span.end + " -- " + color.toString());


                            boolean shouldAdd = true;
                            for (int i = highlights.length-1; i >= 0 ; i--) {
                                Highlighter.Highlight h = highlights[i];
                                if (h.getPainter() instanceof UnderlineHighlighter.SelectionHighlightPainter) {
                                    int starto = h.getStartOffset();
                                    int endo = h.getEndOffset();
                                    
                                    
                                    
                                    if(( starto <= span.start ) && ( endo>= span.end )){
                                        shouldAdd = false;
                                        break;
                                    }
                                    if(( starto > span.start ) && ( endo < span.end )
                                            || ( starto >= span.start ) && ( endo < span.end )
                                            || ( starto > span.start ) && ( endo <= span.end )
                                            )
                                    {
                                        highlighter.removeHighlight(h);                                        
                                        continue;
                                    }
                                        
                                        
                                    
                                }
                                
                                shouldAdd = true;
                            }
                            
                            if(shouldAdd) {
                                highlighter.addHighlight(
                                    span.start,
                                    span.end,
                                    underline_painter);
                            }
                            
                        } catch (Exception ex) {
                            System.out.println("fail to add a selection highlighter: error msg: " + ex.getMessage());
                        }
                    }
                }
            }
            //8888highlighter.addHighlight(annotation.spanstart, annotation.spanend, wave_painter);

        } catch (Exception ex) {
        }
    }

    
    public void removeSelectionHighlighter( int start, int end ){
        
        Highlighter highlighter = comp.getHighlighter();
            // Remove any existing underline highlights for last word
            Highlighter.Highlight[] highlights = highlighter.getHighlights();
            for (int i = highlights.length - 1; i >= 0 ; i--) {
                Highlighter.Highlight h = highlights[i];
                if( h==null )
                    continue;
                 if (h.getPainter() instanceof UnderlineHighlighter.SelectionHighlightPainter) {
                     if(( h.getStartOffset() == start ) && (h.getEndOffset() == end) )                
                        highlighter.removeHighlight(h);
              }
            }
    }
         

    /**
     * Draw underline highlight by positions
     */
    public void addNewUnderlineHightlight(int _startpoint, int _endpoint){

        // get all hightlighter from current text component
        if( comp == null ) {
            System.out.println("fail to add a new selection highlighter.");
            return;
        }
        
        Highlighter highlighter = comp.getHighlighter();


        // Remove any existing underline highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        
        try {
            System.out.println("adding a new selection highlighter "+ _startpoint + ", " +  _endpoint);
            this.underline_painter = new UnderlineHighlighter.SelectionHighlightPainter( Color.BLACK );
            highlighter.addHighlight(_startpoint, _endpoint, underline_painter);
            
            /*boolean shouldAdd = true;
            for (int i = highlights.length - 1; i >= 0; i--) {
                Highlighter.Highlight h = highlights[i];
                if (h.getPainter() instanceof UnderlineHighlighter.SelectionHighlightPainter) {
                    int starto = h.getStartOffset();
                    int endo = h.getEndOffset();
                    


                    if ((starto <= _startpoint) && (endo >= _endpoint)) {
                        shouldAdd = false;
                        break;
                    }
                    if ((starto > _startpoint) && (endo < _endpoint)
                            || (starto >= _startpoint) && (endo < _endpoint)
                            || (starto > _startpoint) && (endo <= _endpoint) ) {
                        highlighter.removeHighlight(h);
                        continue;
                    }
                }

            }


            if (shouldAdd) {
                highlighter.addHighlight(_startpoint, _endpoint, underline_painter);
            }
            
            
               */ 
            //8888highlighter.addHighlight(_startpoint, _endpoint, wave_painter);
        } catch (BadLocationException e) {
            System.out.println("fail to add a new selection high lighter!" + e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     * add Draw underline highlight by positions
     */
    public void addNewWavelineHightlight(int _startpoint, int _endpoint){

        // get all hightlighter from current text component
        Highlighter highlighter = comp.getHighlighter();

        try {
            highlighter.addHighlight(_startpoint, _endpoint, wave_painter);
        } catch (BadLocationException e) {
            System.out.println("error 2201");
            //e.printStackTrace();
        }
    }

    public void RemoveAllWaveHighlight(){
        // get all hightlighter from current text component
        Highlighter highlighter = comp.getHighlighter();
        
        if(highlighter==null)
            return;

        // Remove any existing highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        if(highlights==null)
            return;
        
        for (int i = highlights.length - 1 ; i >= 0 ; i--) {
            Highlighter.Highlight h = highlights[i];
            if( h == null )
                continue;
            
            if (h.getPainter() instanceof UnderlineHighlighter.WaveHighlightPainter) {
                highlighter.removeHighlight(h);
          }
        }
    }


    /**
     * Call this to clear all underline highlight from Screen of document viewer,
     * including colorful straight underline and red wave underline.
     */
    public void RemoveAllUnderlineHighlight(){
        try{
        // get all hightlighter from current text component
        Highlighter highlighter = comp.getHighlighter();
        if(highlighter==null)
            return;

        // Remove any existing highlights for last word
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof UnderlineHighlighter.SelectionHighlightPainter) {
                highlighter.removeHighlight(h);
            }         
        }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 110224");
        }
    }

}

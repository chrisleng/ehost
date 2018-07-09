/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.display;

import resultEditor.annotations.Depot;
import resultEditor.workSpace.WorkSet;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.text.*;
import resultEditor.annotations.SpanDef;
import userInterface.GUI;

/**
 *
 * @author Jianwei Chris Leng 
 */
public class Screen {

    protected JTextPane __jtextpane;
    protected int latestScrollBarValue;
    protected JScrollBar scollbar;
    protected File textsource;
    protected static int fontsize = 13;

    protected static Vector<Integer> paragraphstartpoints = new Vector<Integer>();

    public Screen(JTextPane comp, File textsource,
            JScrollBar scollbar ){
        this.__jtextpane = comp;
        this.scollbar = scollbar;
        this.textsource = textsource;
        // text pane should be set to uneditable
        comp.setEditable(false);  
    }
    
    public Screen(JTextPane comp, File textsource ){
        this.__jtextpane = comp;
        this.textsource = textsource;
        // text pane should be set to uneditable
        comp.setEditable(false);  
    }

    public void clearParagraphStartPoints(){
        Screen.paragraphstartpoints.clear();
    }
    
    public void removeAllBackgroundHighLight(){
        AnnotationHighlighter highlighter =  new AnnotationHighlighter(__jtextpane);
        highlighter.removeAllPaint();
    }

    /**constractor, only for change font size of document*/
    public Screen(JTextPane comp ){
        this.__jtextpane = comp;
        // text pane should be set to uneditable
        comp.setEditable(false);
    }

    /**Show text content and then highlight all these annotations in colorful background.
     * @param contents  An arraylist contains text of each line.
     */
    public void ShowTextAndBackgroundHighLight( final ArrayList<String> contents ){
        //new Thread(){
        //    @Override
        //    public void run(){
                clearParagraphStartPoints();

                showMonoText( contents );

                highlightAnnotations( __jtextpane, textsource );


                resultEditor.conflicts.DifferentMatching diff = new
                        resultEditor.conflicts.DifferentMatching(__jtextpane);
                diff.search_differentMatching(textsource);

                __jtextpane.setCaretPosition(0);
        //    };
        //}.start();
    }


    /**Clear and Repaint all high lighter on the text panel for all annotated 
     * concepts, it contains the color blocks for each visible annotation and 
     * wavy underline.
     */
    public void repaintHighlight()
    {
        try{
            // remove all high lights
            removeAllBackgroundHighLight();
            
            // high light annotations
            highlightAnnotations( __jtextpane, textsource );

            // find difference and highlight diffences
            resultEditor.conflicts.DifferentMatching diff = new
                resultEditor.conflicts.DifferentMatching(__jtextpane);
            diff.search_differentMatching(textsource);
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1102241045:: fail to repaint high lighter"
                    + ex.toString());
        }
    }


    private void setDoclength(ArrayList<String> contents){
        if ( contents == null )
            return;

        int length = 0;
        for( String paragraph : contents ){
            if ( paragraph == null )
                continue;
            length = length + paragraph.length() + 1;
        }
    }

    /**highlight all annotations for this text source*/
    public void highlightAnnotations( JTextPane comp, File textsource )
    {
        try{
            if((comp==null)||(!(comp instanceof JTextPane)))
            {
                log.LoggingToFile.log(Level.SEVERE, "error 1102241047-00:: fail to high light annotations.");
                return;
            }

            AnnotationHighlighter highlighter =  new AnnotationHighlighter(comp);
            highlighter.removeAllPaint();

            if (textsource == null)
                return;

            String textsourceFilename = textsource.getName();

            if ((textsourceFilename == null)||(textsourceFilename.trim().length()<1))
                return;

            ArrayList<Colorblock>  painttasks = paintObjects( textsourceFilename.trim() );

            if ( painttasks == null )   
                return;

            int size = painttasks.size();
            
            
            
            for(int i=0;i<size;i++){
                Colorblock colorblock = painttasks.get(i);
                if (colorblock == null) 
                    continue;                
                highlighter.paintColor( colorblock.start, colorblock.end , colorblock.color);
            }
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1102241047:: fail to high light annotations."
                    + ex.toString());
        }
    }

    /*private void showMonoText(ArrayList<String> contents){
        clear();
        if ( contents == null)
            return;

        int size = contents.size();
        int length = 0;
        paragraphstartpoints.add(length);
        for(int i=0;i<size;i++){
            paragraphstartpoints.add(length);
            String line = contents.get(i) + "\n";
            showInTextPane( line );
            setTextStyle(length);
            length = length + line.length();
            
        }
        
        setTextStyle(0);
        
    }*/

    
    private void showMonoText(ArrayList<String> contents){
        try {
            if (__jtextpane == null) {
                return;
            }

            clear();
            if (contents == null) {
                return;
            }

            int size = contents.size();
            int length = 0;
            paragraphstartpoints.add(length);

            // get document of current Java text component
            Document doc = __jtextpane.getDocument();
            int doclength = 0;
            AttributeSet testattribute = __jtextpane.getInputAttributes();
            if (doc == null) {
                return;
            }

            for (int i = 0; i < size; i++) {
                paragraphstartpoints.add(length);
                String line = contents.get(i);
                if (line == null) {
                    continue;
                }
                
                line = line + "\n";
                
                // add the content(String) at the end of current Java Text component
                //doc.insertString( doc.getLength(), line, testattribute);//instert text
                doc.insertString( length, line, testattribute);//instert text

                setTextStyle(length);
                length = length + line.length();

            }

            setTextStyle(0);
            
            
            
        } catch (Exception e) {
            System.out.println("\n\n\n\nerror 1203291118" + e.getMessage() );
                    // e.printStackTrace();
        }
        
    }

    /**This method is used to clear all old content from current java text
     * component, usually this component is a jTextPane.*/
    private void clear(){
        try{
            if(__jtextpane==null)
                return;
            else
                __jtextpane.setText("");
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "~~~~ ERROR ~~~~::1108041434::fail to remove old content from screen.");
            log.LoggingToFile.log(Level.SEVERE, ex.getMessage());
        }
    }

    

    /**This method is used to add and display a string on current text pane,
     * the document viewer of eHOST. It will get document of current text pane
     * component and add this new string at the end of the text pane.
     *
     * @param   _test
     *          the string content that you want to add and display it at the
     *          end of current text pane. If it equals to null, this method will
     *          do nothing.
     */
    private void addIntoTextPane(String _text)  {

        // quit if parameter(the string that you want to display on screen)
        // is 'null'
        if( _text == null )
           return;
        
        if(__jtextpane == null)
            return;
        

        try{
            // get document of current Java text component
            Document doc = __jtextpane.getDocument();
            // add the content(String) at the end of current Java Text component
            //doc.insertString(doc.getLength(), _text, __jtextpane.getInputAttributes());//instert text
            doc.insertString(doc.getLength(), _text, null );//instert text
            
        } catch (Exception e) {
            System.out.println("\n\n\n\nerror 1203291118" + e.getMessage() );
                    // e.printStackTrace();
        }
    }


     
     public static void setFontSize(int size){
         Screen.fontsize = size;
     }

     private void setTextStyle(int position){
            DefaultStyledDocument doc = (DefaultStyledDocument)__jtextpane.getStyledDocument();

            // add this string to end of logs on the Text Pane
            //MutableAttributeSet set = new SimpleAttributeSet();
            // Create and add the main document style
            StyleContext sc = new StyleContext();
            Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

            final Style set = sc.addStyle("set", defaultStyle);
            //StyleConstants.setLineSpacing(set, 2f);
            StyleConstants.setForeground(set, Color.black);
            StyleConstants.setFontFamily(set, "Courier New");
            StyleConstants.setFontSize(set, fontsize );//set size of font
            //set linespace
            StyleConstants.setLineSpacing(set,0.3f);

            doc.setLogicalStyle(position, set);
     }

     public void changeDisplayFontSize(int fontsize){

         setFontSize(fontsize);

         DefaultStyledDocument doc = (DefaultStyledDocument)__jtextpane.getStyledDocument();

            // add this string to end of logs on the Text Pane
            //MutableAttributeSet set = new SimpleAttributeSet();
            // Create and add the main document style
            StyleContext sc = new StyleContext();
            Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

            final Style set = sc.addStyle("set", defaultStyle);
            //StyleConstants.setLineSpacing(set, 2f);
            StyleConstants.setFontFamily(set, "Courier New");
            StyleConstants.setFontSize(set, fontsize );//set size of font
            //set linespace
            StyleConstants.setLineSpacing(set,0.3f);

         for( Integer i : Screen.paragraphstartpoints ){
         //this.comp.setFont(new Font( "Courier New", Font.PLAIN, fontsize ));
            doc.setLogicalStyle(i, set);
         }

     }

     class Colorblock{
         int start;
         int end;
         Color color = Color.white; // white is default color
     }

     private ArrayList<Colorblock> paintObjects(String filename) {
        
         resultEditor.annotations.Article article = null;
        //resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        //resultEditor.annotations.Article articleOfAnn = Depot.getArticleByFilename( filename );
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
            article = adjudication.data.AdjudicationDepot.getArticleByFilename( filename );
        else{ // if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
            article = resultEditor.annotations.Depot.getArticleByFilename( filename );
        }
        
        //if(  )
        
        //final resultEditor.annotations.Article article = Depot.getArticleByFilename( filename );
        
        if ( article == null )  return null;

        Vector<resultEditor.annotations.Annotation> annotations = article.annotations;
        if ( annotations == null )  return null;

        ArrayList<Colorblock> colorblocks = new ArrayList<Colorblock>();

        int size = annotations.size();

        // to each imported annotation, check and pick out these who are
        // in range of current paragraph
        try {
            for (int i = 0; i < size; i++) {
                if(WorkSet.filteredViewing) {
                    if(!WorkSet.currentlyViewing.contains(i))
                        continue;
                }
                resultEditor.annotations.Annotation annotation = annotations.get(i);

                // ON ANNOTATION MODE
                if (GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE) {
                    if (!annotation.visible) {
                        continue;
                    }
                    
                    /*if ( (annotation.annotator != null)  && ( annotation.annotator.compareTo("ADJUDICATION") == 0 ) ) {
                        annotation.setVisible( false );
                        continue;
                    }*/
                }


                // only list matched_ok and non-matches on ADJUDICATION MODE
                if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                    annotation.setVisible( true );
                    
                    if ((annotation.adjudicationStatus != resultEditor.annotations.Annotation.AdjudicationStatus.MATCHES_OK)
                            && (annotation.adjudicationStatus != resultEditor.annotations.Annotation.AdjudicationStatus.NON_MATCHES)) {
                        continue;
                    }
                }



                
                String className = annotation.annotationclass.trim();
                //System.out.println(" - classname = [" + className + "]");
                Color c = resultEditor.annotationClasses.Depot.getColor(className);
                if (c == null) {
                    c = Color.BLACK;
                }


                if (annotation.spanset != null) {
                    for (int x = 0; x < annotation.spanset.size(); x++) {
                        Colorblock colorblock = new Colorblock();
                        SpanDef span = annotation.spanset.getSpanAt(x);
                        if (span == null) {
                            continue;
                        }
                        if ((span.start < 0) || (span.end < 0)) {
                            continue;
                        }
                        
                        //System.out.println("HIGHLIGHT - " + span.start+ ", " + span.end );
                        colorblock.start = span.start;
                        colorblock.end = span.end;
                        colorblock.color = c;




                        colorblocks.add(colorblock);
                    }
                }

            }
        } catch (Exception e) {
            log.LoggingToFile.log(Level.SEVERE, "gui.java - paintline() - error2752" + e.toString());
        }

        return colorblocks;
    }
}

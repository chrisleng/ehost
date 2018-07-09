/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.display;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Chris
 */
public class AnnotationHighlighter {
    protected JTextPane comp;

    public AnnotationHighlighter(JTextPane comp){
        this.comp = comp;
    }

    public void paintColor(int start, int end, Color color){
        
        int max = docLength();
        if ((start < 0)||(end>max))
            return;
        
        //Document doc = comp.getDocument();
       DefaultStyledDocument doc = (DefaultStyledDocument)comp.getStyledDocument();
       StyleContext sc = new StyleContext();
       Style set = sc.getStyle(StyleContext.DEFAULT_STYLE);

       StyleConstants.setBackground(set, color);

       if (color == Color.black)
            StyleConstants.setForeground(set, Color.white);
       else
            StyleConstants.setForeground(set, Color.black);
       //StyleConstants.setBackground(set, Color.white);
       //final Style set = sc.addStyle("set", defaultStyle);
       doc.setCharacterAttributes(start, end-start, set, true);

    }

    public void removeAllPaint(){
        int max = docLength();
        paintColor(0, max-1, Color.white);
    }

    private int docLength(){
        Document doc = comp.getDocument();
        if (doc == null)
            return 0;
        else return doc.getLength();
    }
}

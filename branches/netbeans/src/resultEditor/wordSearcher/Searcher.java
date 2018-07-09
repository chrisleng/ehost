/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.wordSearcher;

import java.util.Vector;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * Search for a word in textcomponent and hightlight all occurrences
 * of that word.
 *
 * @author Jianwei Leng 2010-6-15 3:06 pm Williams Building, University of Utah
 */
public class Searcher {

    protected JTextComponent textcomponent;
    protected userInterface.GUI gui;
    protected String objectWord;

    public Searcher(JTextComponent textcomponent, userInterface.GUI gui){
        this.textcomponent = textcomponent;
        this.gui = gui;
    }

    /**search a word and highlight all occurrences of this word.*/
    public int find(String word){

        this.objectWord = word;

        // amount of found words
        int count=0;

        if (word == null || word.equals("")) {
            commons.Tools.beep();
            return -1;
        }

        // get text content of the component
        String article;
        try{
            Document doc = textcomponent.getDocument();
            article = doc.getText(0, doc.getLength()).toLowerCase();
            Vector<Span> results = searchText(word, article);
            count = results.size();

            // set echo text information on the info bar at the bottom of GUI to
            // tell user how many results found.
            setEchoText_OnInfoBar( count );

            // get the highlight painter
            resultEditor.underlinePainting.SelectionHighlighter painter = new
                    resultEditor.underlinePainting.SelectionHighlighter(textcomponent);
            painter.RemoveAllUnderlineHighlight();
            
            // paint all found words in the text component
            for(int i=0;i<count;i++){
                int start = results.get(i).start;
                int end = results.get(i).end;

                if( i==0 ){
                    textcomponent.setCaretPosition(start);
                }

                painter.addNewUnderlineHightlight(start, end);
            }




        } catch( Exception e ) {
            logs.ShowLogs.printWarningLog(e.toString());
        }
        
        commons.Tools.beep();
        return count;
    }

    /**
     * set echo text information on the info bar at the bottom of GUI to
     * tell user how many results found.
     *
     * @param   amount_of_results
     *          Amount of found words in current document.
     */
    private void setEchoText_OnInfoBar(int amount_of_results){
        String str;
        if ( amount_of_results < 1 ) {
            str = "No phrase containing \""+ this.objectWord +"\" :found!";
            gui.showText_OnBottomInfoBar(str);
        } else if ( amount_of_results == 1 ){
            str = "Just " + amount_of_results + " word of \""+ this.objectWord +"\" found.";
            gui.showText_OnBottomInfoBar(str);
        } else {
            str = amount_of_results + " words of \""+ this.objectWord +"\" found.";
            gui.showText_OnBottomInfoBar(str);
        }
    }
    
    private Vector<Span> searchText(String word, String article){
        Vector<Span> founds = new Vector<Span>();
        int startpoint = 0, endpoint =0;
        int wordsize = word.length();
        while( (startpoint = article.indexOf(word, startpoint)) != -1 ){
            endpoint = startpoint + wordsize;
            Span span = new Span();
            span.start = startpoint;
            span.end = endpoint;
            founds.add(span);

            startpoint = endpoint;
        }
        return founds;
    }


    class Span {
        int start;
        int end;
    }
}

/*
 * if the indicator is "ON"(false), check and correct span border by
 * space or symbols, o.w. use exact span user just selected.
 */

package userInterface.correctSpanBorder;

import java.util.logging.Level;
import javax.swing.JTextPane;
import javax.swing.text.Document;

/**
 *
 * @author leng 2010-11-24 12:36am
 */
public class SpanChecker {

    protected JTextPane tp;

    /** Constructor
     *
     * @param   tp
     *          object of jtextpane that a span just got selected on.
     */
    public SpanChecker(JTextPane tp){
        this.tp = tp;
    }

     /** if the indicator is "ON"(false), check and correct span border by
     * space or symbols, o.w. use exact span user just selected
     *
     * @param   tp
     *          object of jtextpane that a span just got selected on.
     */
    public void checkAndCorrect_ifHave(){
        try{

            // ##1## validity check
            if (tp==null)
                return;
            if (env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan)
                return;

            if (tp.getSelectedText()==null)
                return;
            if (tp.getSelectedText().trim().length() < 1)
                return;

            String selectedText = tp.getSelectedText();

            // do nothing if just one character selected and it's a symbol
            if (selectedText.length() == 1){
                if (isBorderSymbol(selectedText.charAt(0)))
                    return;
            }

            // ##2## detect new border of span
            int start, end;
            start = tp.getSelectionStart();
            end = tp.getSelectionEnd();

            Document doc = tp.getDocument();
            // ##2.1## detect left border
            
            do{
                start--;
                if(start<=0){
                    start=0;
                    break;
                }


                String str = doc.getText(start, end-start);
                if(str!=null){
                    char c = str.charAt(0);
                    if ((c == ' ')||(isBorderSymbol(c))){
                        start++;
                        break;
                    }
                }


            }while(start>=0);
            


            // ##2.1## detect left border
            do{
                end++;
                if(end >= doc.getLength()-1 ){
                    end = doc.getLength()-1;
                    break;
                }

                String str = doc.getText(start, end-start);
                if(str!=null){
                    char c = str.charAt(str.length()-1);
                    //System.out.println("["+c+"]");
                    if ((c == ' ')||(isBorderSymbol(c))){
                        end--;
                        break;
                    }
                }

            }while(end <= doc.getLength()-1);


            // ##3## reset border of span and update the GUI
            tp.setSelectionStart(start);
            tp.setSelectionEnd(end);
            //System.out.println("start="+start+", end="+end);
            //tp.updateUI();
            
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, "error 1011240041:: fail to check and correct" +
                    "span border that user just clicked to selected.");
        }
    }

    /**check type of a given char.*/
    private boolean isBorderSymbol(char c){
        if (c == ' ')
            return false;
        if ((c >= 'a' )&&(c<='z'))
            return false;
        if ((c >= 'A' )&&(c<='Z'))
            return false;
        if ((c >= '0' )&&(c<='9'))
            return false;

        return true;
    }
}

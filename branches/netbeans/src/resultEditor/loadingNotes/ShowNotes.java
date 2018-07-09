/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.loadingNotes;

import annotate.gui.NLPcpu;
import java.awt.Color;
import java.util.logging.Level;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Leng
 */
public class ShowNotes {

        // we difine this to get the handler of Text Panel
        private static javax.swing.JTextPane _TextPane_;
        private static userInterface.GUI _gui_;
        //private static dialogs.Logs _logWindows_;

        static{
                _TextPane_ = NLPcpu.getLogTextPane();
        }

        public static void setGUIHandler(Object _gui){
                _gui_ = (userInterface.GUI)_gui;
        }


        // **** get information from draft level storage space and convet into colored format
        // which is friendly for showing on screen
        @SuppressWarnings("static-access")
        public static void ShowNote(String _absoluteFilename){

                ColoringSpace.clean();
                ColoringSpace.notesLoading();
                _TextPane_.setDocument(new DefaultStyledDocument());
                //_gui_.cleanTextPaneOfResults();
                ShowOnScreen(_absoluteFilename);
        }


    private static void ShowOnScreen(final String _absoluteFilename){

        if (_absoluteFilename == null)
                return;

        int size = resultEditor.loadingNotes.ColoringSpace.getSize();

        //GUI.cleanTextPaneOfResults();

        // show file on screen
        // go over all paragraph to find whose filename are same as requested
        for(int i = 0; i < size; i++){

            // a) get paragraph with marked color info
            String paragraph = resultEditor.loadingNotes.ColoringSpace.getParagraphText(i);

            if ((paragraph.trim().length() < 1)||(paragraph == null)){
                    PrintToScreen("\n", null);
            }

            Color[] marks = resultEditor.loadingNotes.ColoringSpace.getColorMarks(i);
            char[] ch = paragraph.toCharArray();
            String absoluteFilename = resultEditor.loadingNotes.ColoringSpace.getFileName(i);

            // b) print all characters in this paragraph
            if (absoluteFilename.compareTo(_absoluteFilename)==0)
            {
                //_gui_.setCurrentResult( _absoluteFilename, i );

                for(int j=0;j<ch.length;j++){
                        if (j<marks.length-1)
                                PrintToScreen( String.valueOf(ch[j]), marks[j] );
                        else
                                PrintToScreen( String.valueOf(ch[j]), Color.BLACK );
                }
                PrintToScreen( "\n", null );
            }

        }
    }

    public static void PrintToScreen(String _log, Color _color){
            insertDocument( _log , _color, 0, _TextPane_ );
    }

    /** insert text into textpanel with assigned color
     * @param _text        the text need to be show on the text pane
     * @param _textColor   font color, default is gray
     * @param _fontSize    font size, default value is 12
     * @return null
     **/
    private static void insertDocument(String _text , Color _textColor, int _fontSize, javax.swing.JTextPane _textPanel) {
        if( _text == null )
                return;
        if( _text.length() < 1)
                return;

        if( _textColor == null )
                _textColor = Color.gray;
        if( _fontSize <= 1 )
                _fontSize = 12;

        try
        {
            // add this string to end of logs on the Text Pane
            SimpleAttributeSet set = new SimpleAttributeSet();
            StyleConstants.setForeground(set, _textColor);//设置文字颜色
            StyleConstants.setFontSize(set, _fontSize );//设置字体大小
            Document doc = _textPanel.getStyledDocument();
            doc.insertString(doc.getLength(), _text, set);//插入文字
            _textPanel.validate();

            // automatically focus on last line of the text pane
            //_textPanel.setCaretPosition(doc.getLength());
            _textPanel.requestFocus();
            _textPanel.validate();

            // folk a new thread to validate the window of log
            //_logWindows_.forceRepaint();

        }catch (Exception e){
            log.LoggingToFile.log(Level.SEVERE, " 1010 - ShowNotes.java"+e.toString());
            logs.ShowLogs.printErrorLog(" 1010 - ShowNotes.java"+e.toString());
        }
    }
}

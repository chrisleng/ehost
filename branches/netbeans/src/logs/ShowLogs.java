/*
 * @(#)ShowLog.java	1.00 03/21/2010
 * first created        03/19/2010
 *
 * opensource project of eHOST, clinical nature language processing.
 */

package logs;
/**
 * Class <code>ShowLogs</code> is a class which provide some static methods for
 * other class to show log text in the log windows in color.
 *
 * @author  Jianwei Leng (Chris)
 * @version 1.00, 03/19/2010
 * @see     eHOST.dialogsShowLogs.ShowLogs
 * @since   JDK1.5
 */

import annotate.gui.NLPcpu;
import java.awt.Color;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ShowLogs {

        private static int position=0;

        // we difine this to get the handler of Text Panel
        private static javax.swing.JTextPane _TextPane_;
        //private static dialogs.Log _logWindows_;

        static{
                //System.out.println("----------------"+_TextPane_.toString());
                _TextPane_ = NLPcpu.getLogTextPane();
                if(_TextPane_ != null)
                    _TextPane_.setEditable(false);
                //System.out.println("----------------"+_TextPane_.toString());

        }
        
       // public static void setTextPane(Object _this){
        //        _logWindows_ = (dialogs.Log)_this;
        //}


        private static void LogPrint(String _log){
                insertDocument( _log , Color.GRAY, 0, _TextPane_ );
        }

        public static void LogPrint(String _log, Color _color){
                insertDocument( _log , _color, 0, _TextPane_ );
        }


        private static void LogPrintln(String _log){
                insertDocument( _log , Color.GRAY, 0, _TextPane_ );
                LogPrintFullstop();
        }

        private static void LogPrintln(String _log, Color _color){
                insertDocument( _log , _color, 0, _TextPane_ );
                System.out.print(_log);
                LogPrintFullstop();
                
        }

        // insert a fullstop symbol into the text pane after previous strings
        private static void LogPrintFullstop(){
                insertDocument("\n" , Color.GRAY, 0, _TextPane_ );
                System.out.print("\n");
        }

        /**
         * @param log_level_0   important error, need to interrupt system,
         * in red, <code>printErrorLog</code>
         * @param log_level_1   normal error, in Orange color, <code>printErrorLog</code>
         * @param log_level_2   normal information or parameter that got from
         * user input or notes, in gray color, <code>printInfoLog</code>
         * @param log_level_3   similar as above, but more important, or easy to cause error
         * or some notice, in light gray color, <code>printImportantInfoLog</code>
         * param log_level_4   similar as above, but more important, or easy to cause error
         * , in orange color, <code>printImportantInfoLog</code>
         * param log_level_5   show notice, in green, <code>printNotice</code>
         * param log_level_6   others logs, in grey color, <code>printLog</code>
         **/
        public static void printErrorLog(String _errorLog){
                LogPrint( "[eHOST] - ", Color.RED);
                LogPrintln( _errorLog, Color.RED );
        }

        public static void printWarningLog( String _errorLog ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrintln( _errorLog, Color.orange );
        }

        public static void printImportantInfoLog( String _log ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrintln( _log, Color.blue );
        }

        public static void printInfoLog( String _log ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrintln( _log, Color.lightGray );
        }

        public static void printNotice( String _log ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrintln( _log, Color.black );
        }

        public static void printLog( String _log ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrintln( _log, Color.gray );
        }

        public static void printResults( String _type, String _log ){
                LogPrint( "[eHOST] - ", Color.black);
                LogPrint( _type, Color.BLUE);
                LogPrintln( _log, Color.ORANGE );
        }

        public static void cleanTextArea(){
            if(_TextPane_ == null)
                return;
            position = 0;
            _TextPane_.setText(null);
        }

        /** insert text into textpanel with assigned color
         * @param _text        the text need to be show on the text pane
         * @param _textColor   font color, default is gray
         * @param _fontSize    font size, default value is 12
         * @return null
         **/
        private static void insertDocument(String _text , Color _textColor, int _fontSize, javax.swing.JTextPane _textPanel) {
                if( _text == null || _textPanel == null)
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
                        StyleConstants.setForeground(set, _textColor);//set color of font
                        StyleConstants.setFontSize(set, _fontSize );//set size of font
                        Document doc = _textPanel.getStyledDocument();
                        //doc.insertString(doc.getLength(), _text, set);//insert text
                        doc.insertString(position, _text, set);//instert text
                        position+=_text.length();
                        // automatically focus on last line of the text pane
                        //_textPanel.setCaretPosition(doc.getLength());
                        _textPanel.setCaretPosition(position);
                        //dialogs.GUI.getLogTextPane().setCaretPosition(position);

                        //_textPanel.requestFocus();

                        // folk a new thread to validate the window of log
                        //_logWindows_.forceRepaint();
                        
                }catch (Exception e){
                        e.printStackTrace();
                }
        }

}

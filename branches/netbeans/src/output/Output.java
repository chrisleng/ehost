/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package output;

/**
 *
 * @author Leng
 */
public class Output {

        // the folder you want to output your xml results
        private String _xml_output_path_;
        // this show how many files processed in previous nlp processing
        private int  _amount_of_processed_files_;
        private String  _original_filename_;



        // init method
        public Output(String _xml_output_path, int _amount_of_processed_files, String _filename) {
                _xml_output_path_ = _xml_output_path;
                _amount_of_processed_files_ = _amount_of_processed_files;
                _original_filename_ = _filename;

        }

        /** export found clinical terms in XML format, and then these xml files
         * can be import by third-party software, such as potoge, to do some
         * annotation research.
         **/
        public void toXML(){
                output.xmloutput.WriteToXML WTX = new output.xmloutput.WriteToXML(_xml_output_path_, _original_filename_);
                WTX.writeToDisk();
        }

}

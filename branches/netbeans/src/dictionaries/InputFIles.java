/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dictionaries;

import java.util.ArrayList;

/**
 * @author Leng, Jianwei (Chris)
 * @version 1
 * @since Feb 15, 2010
 */
public class InputFIles {
        public static ArrayList INPUT_FILES;
        public static ArrayList FullINFO_INPUT_FILES;

        static{ INPUT_FILES = new ArrayList(); };

        static{ FullINFO_INPUT_FILES = new ArrayList(); };

        public static void AddInputFiles(String _filename, String _filesize, String _absolutely_path){
                // first charact of the file name is . or ~, do nothing
                if (_filename.charAt(0) == '.')
                        return;
                if (_filename.charAt(0) == '~')
                        return;

                Object[] files = { _filename, _filesize };
                INPUT_FILES.add(files);

                Object[] fullfiles = { _filename, _filesize, _absolutely_path };
                FullINFO_INPUT_FILES.add(fullfiles);
        }

        public static void RemoveInputFiles( int index ){
                INPUT_FILES.remove(index);
                FullINFO_INPUT_FILES.remove(index);
        }

        public static void RemoveAll(){
                INPUT_FILES.clear();
                FullINFO_INPUT_FILES.clear();
        }
}

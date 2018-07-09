/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.loadingNotes;

import java.util.ArrayList;
import java.awt.Color;
import java.util.logging.Level;
/**
 *
 * @author Leng
 */
public class ColoringSpace {

        private static ArrayList _Spaces_;

        static{ _Spaces_ = new ArrayList();
        }

        public static void clean(){
                _Spaces_.clear();
        }

        public static int getSize(){
                return _Spaces_.size();
        }

        public static String getParagraphText(int _index){
                try{

                        //dialogsShowlogs.ShowLogs.printErrorLog("size of colored.spaces = "+_Spaces_.size());
                        //dialogsShowlogs.ShowLogs.printErrorLog("------------index = " + _index);
                        resultEditor.loadingNotes.ColoredFormat s
                                = ( resultEditor.loadingNotes.ColoredFormat ) _Spaces_.get(_index);
                        String getParagraphText = s.paragraphText;
                        return getParagraphText;
                }catch(Exception e){
                    log.LoggingToFile.log(Level.SEVERE, " 901 - ColoringSpace.java "+e.toString());
                        logs.ShowLogs.printErrorLog(" 901 - ColoringSpace.java "+e.toString());
                }
                return null;
        }

        public static String getFileName(int _index){
                try{
                        resultEditor.loadingNotes.ColoredFormat s
                                = ( resultEditor.loadingNotes.ColoredFormat )_Spaces_.get(_index);
                        String filename = s.filename;
                        return filename;
                }catch(Exception e){
                        log.LoggingToFile.log(Level.SEVERE, " 903 - ColoringSpace.java "+e.toString());
                        logs.ShowLogs.printErrorLog(" 903 - ColoringSpace.java "+e.toString());
                }
                return null;
        }

        public static Color[] getColorMarks(int _index){
                try{
                        Color[] getColorMark;
                        resultEditor.loadingNotes.ColoredFormat s
                                = ( resultEditor.loadingNotes.ColoredFormat )_Spaces_.get(_index);
                        getColorMark = s.marks;
                        return getColorMark;
                }catch(Exception e){
                        log.LoggingToFile.log(Level.SEVERE, " 902 - ColoringSpace.java "+e.toString());
                        logs.ShowLogs.printErrorLog(" 902 - ColoringSpace.java "+e.toString());
                }
                return null;
        }

        // check the available of results
        // return true, if found results in the storage crude memory
        // return false, o.w.
        boolean checkStatus(){
                int size = nlp.storageSpaceDraftLevel.StorageSpace.Length();
                
                if (size < 1)
                        return false;
                else
                        return true;

        }

        // loading notes and

        static void notesLoading(){
                try{
                        
                        // get how many paragraph in the storage memory of draft level
                        int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();

                        // if filelist or memory is empty, do nothing
                        if( amount_of_paragraph == 0 )
                                return;

                        // **** 1) empty the memory before adding new data
                        _Spaces_.clear();

                        
                        // **** 2) get all paragraph and found informations from algorithms
                        // and convert to format of
                        for(int i = 0; i< amount_of_paragraph; i++){

                                // ****3.1) prepare a instant in format of coloredFormat
                                resultEditor.loadingNotes.ColoredFormat thisColoringParagraph = new resultEditor.loadingNotes.ColoredFormat();

                                // ****3.2) load paragraph and filename and set the marks array
                                thisColoringParagraph.paragraphText =
                                        nlp.storageSpaceDraftLevel.StorageSpace.getParagraphText(i);
                                thisColoringParagraph.filename
                                        = nlp.storageSpaceDraftLevel.StorageSpace.getFilename(i);
                                int size = thisColoringParagraph.paragraphText.length();
                                thisColoringParagraph.marks = new Color[size];
                                for(int j=0;j<size;j++){
                                        thisColoringParagraph.marks[j] = Color.BLACK;
                                }

                                // ****4 get found concept details and mark color array
                                // by coordinates of concepts
                                ArrayList conceptDetail = nlp.storageSpaceDraftLevel.StorageSpace.getConceptDetails(i);

                                int cdSize = conceptDetail.size();

                                for(int j=0; j<cdSize; j++){
                                        nlp.storageSpaceDraftLevel.Table_Concept concepts
                                                = (nlp.storageSpaceDraftLevel.Table_Concept)conceptDetail.get(j);
                                        int start = concepts.span_start_in_the_paragraph;
                                        int end = concepts.span_end_in_the_paragraph;
                                        //System.out.println("------------ start = " +start+ ";  end = " + end + ";");

                                        // mark integer array of color
                                        for(int s=start; s<end; s++){
                                                
                                                if ((s-1)< (thisColoringParagraph.marks.length - 1) )
                                                        thisColoringParagraph.marks[s-2]=Color.green;
                                        }
                                }
                                

                                _Spaces_.add(thisColoringParagraph);
                        }

                        //int size=_Spaces_.size();
                        //for(int i=0;i<size;i++){
                        //        dialogsShowNotes.ColoredFormat th = (dialogsShowNotes.ColoredFormat)_Spaces_.get(i);
                        //         System.out.println(i+"-----=========----------" + th.paragraphText );
                        //}

                        return;
                }catch(Exception e){
                    log.LoggingToFile.log(Level.SEVERE, e.getMessage() );
                    logs.ShowLogs.printErrorLog(e.toString());
                }
                return;
        }

        //dialogsShowNotes.ColoredFormat notesColoring(int i){
        //        dialogsShowNotes.ColoredFormat c;


        //}

        public void formatNote(){
                // format notes for text panel, set color, font size....
                if(checkStatus()){
                        // load processed notes into memory and ready for format
                        //notesPreProcessing();
                }
        }
}

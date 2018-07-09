/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.spanEdit;

import resultEditor.annotations.Depot;
import java.util.logging.Level;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.SpanDef;
import resultEditor.annotations.SpanSetDef;
import userInterface.GUI;

/**
 *
 * @author leng 2010-6-14 2:23am
 */
public class SpanEditor {
    private final JTextComponent __tc;
    private Annotation __annotation;
    private SpanDef __span;

    public SpanEditor(Annotation _annotation, SpanDef _span, JTextComponent _tc){
        this.__tc = _tc;
        this.__span = _span;
        this.__annotation = _annotation;
    }

    final private int delete = 0, headextendtoLeft = 1, tailextendtoRight = 2,
            headShortentoRight = 3, tailShortentoLeft =4;



    
    
    public int editCurrentDisplayedSpan(int _type){
                
        resultEditor.annotations.Depot depotOfAnn = new  resultEditor.annotations.Depot();
        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
        
        if (( depotOfAnn == null )|| (depotOfAdj == null) )
                return 0;

        if(__span == null)
            return 0;
        if(__annotation == null)
            return 0;

        int modifiedEntries = -1;
        // get latest accessed annotation in the query
        
        // get latest annotation

        // the text of the modified span
        String newSpanText;

        int start = __span.start;
        int end = __span.end;

        switch(_type){
            case headextendtoLeft:
                if (start == 0 ){
                    commons.Tools.beep();
                    return 0;
                }

                break;

            case tailextendtoRight:
                if (end >= __tc.getDocument().getLength() ){
                    commons.Tools.beep();
                    return 0;
                }
                break;

            case headShortentoRight:
                if ( start == end - 1 ){
                    commons.Tools.beep();
                    return 0;
                }

                break;

            case tailShortentoLeft:
                if ( end == start + 1 ){
                    commons.Tools.beep();
                    return 0;
                }
                break;
        }

        // filename of current text source
        String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

        // index: annotation index in vector of anntation of this article in depot
        int uniqueindex = __annotation.uniqueIndex;

        switch (_type) {
            case delete:

                if (GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE) {
                    modifiedEntries = depotOfAnn.deleteAnnotation_byUID(filename, uniqueindex);
                    Depot.SelectedAnnotationSet.removeAnnotationUniqueIndex(uniqueindex);
                } else if (GUI.reviewmode == GUI.reviewmode.adjudicationMode) {
                    //adjudication.data.AdjudicationDepot  = new adjudication.data.AdjudicationDepot();                    
                    modifiedEntries = depotOfAdj.deleteAnnotation_byUID(filename, uniqueindex);
                    Depot.SelectedAnnotationSet.removeAnnotationUniqueIndex(uniqueindex, true);
                }
                break;
            case headextendtoLeft:
                newSpanText = getspan(start - 1, end);
                if (GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE) {
                    modifiedEntries = depotOfAnn.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, headextendtoLeft);
                } else if (GUI.reviewmode == GUI.reviewmode.adjudicationMode) {
                    modifiedEntries = depotOfAdj.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, headextendtoLeft);
                }
                break;
            case tailextendtoRight:
                newSpanText = getspan(start, end + 1);
                if (GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE) {
                    modifiedEntries = depotOfAnn.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, tailextendtoRight);
                }else{
                    modifiedEntries = depotOfAdj.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, tailextendtoRight);
                }
                
                break;
            case headShortentoRight:
                newSpanText = getspan(start + 1, end);
                if (GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE) {
                    modifiedEntries = depotOfAnn.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, headShortentoRight);
                }else{
                    modifiedEntries = depotOfAdj.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, headShortentoRight);
                }
                

                break;
            case tailShortentoLeft:
                newSpanText = getspan(start, end - 1);
                if (GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE) {
                    modifiedEntries = depotOfAnn.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, tailShortentoLeft);
                }else{
                    modifiedEntries = depotOfAdj.AnnotationRangeSet(__tc, filename, uniqueindex, start, end, tailShortentoLeft);
                }
                break;

                

        }
        
        return modifiedEntries;
    }

    
    /*


    public int editCurrentDisplayedSpan(String documentfilename, int annotation_uid, int _type){
        resultEditor.annotations.Depot depot = new  resultEditor.annotations.Depot();
        if ( depot == null ) return 0;

        String span;
        int modifiedEntries = -1;
        // get latest accessed annotation in the query

        // get latest annotation
        resultEditor.annotations.Annotation annotation =
                depot.getAnnotationByUnique(documentfilename, annotation_uid);

        if (annotation == null){
            log.LoggingToFile.log(Level.SEVERE,"error 1012011558:: fail to find annotation!!");
            return 0;
        }

        int start = annotation.spanstart;
        int end = annotation.spanend;

        switch(_type){
            case headextendtoLeft:
                if (start == 0 ){
                    commons.Tools.beep();
                    return 0;
                }

                break;

            case tailextendtoRight:
                if (end >= __tc.getDocument().getLength() ){
                    commons.Tools.beep();
                    return 0;
                }
                break;

            case headShortentoRight:
                if ( start == end - 1 ){
                    commons.Tools.beep();
                    return 0;
                }

                break;

            case tailShortentoLeft:
                if ( end == start + 1 ){
                    commons.Tools.beep();
                    return 0;
                }
                break;
        }

        // filename of current text source
        //String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName();

        // index: annotation index in vector of anntation of this article in depot
        

        switch(_type){
            case delete:

                modifiedEntries = depot.annotation_uid( documentfilename, annotation_uid );
                Depot.SelectedAnnotationSet.removeAnnotationUniqueIndex(annotation_uid);
                break;
            case headextendtoLeft:
                span = getspan(start-1, end);
                modifiedEntries = depot.AnnotationRangeSet( documentfilename, annotation_uid, span, start, end, headextendtoLeft);
                break;
            case tailextendtoRight:
                span = getspan(start, end+1);
                modifiedEntries = depot.AnnotationRangeSet( documentfilename, annotation_uid, span, start, end, tailextendtoRight);
                break;
            case headShortentoRight:
                span = getspan(start+1, end);
                modifiedEntries = depot.AnnotationRangeSet( documentfilename, annotation_uid, span, start, end, headShortentoRight);

                break;
            case tailShortentoLeft:
                span = getspan(start, end-1);
                modifiedEntries = depot.AnnotationRangeSet( documentfilename, annotation_uid, span, start, end, tailShortentoLeft);
                break;



        }

        return modifiedEntries;
    }
*/

    /**Return the text of designated range on text viewer on screen for a given 
     * span, such as to (1,4), pick up the text from 1st character to 4th 
     * character on screen of current document, and return this string. 
     */
    public String getspan(int start, int end){
        String spantext;
        try {
            int offset = resultEditor.conflicts.ConflictButtonPainter.getScreenOffset(start, 0);
            spantext = __tc.getDocument().getText(start + offset, end - start);
            return spantext;
        } catch (BadLocationException ex) {
            logs.ShowLogs.printErrorLog( ex.toString() );
            return null;
        }
    }



}

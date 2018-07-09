/*
 * This class is designed to search difference matching to annotations with
 * same or crossed spans; Differences in markable category(class), complex
 * relationships or simple attributes will be figure out and marked with
 * read wavy underline.
 */

package resultEditor.conflicts;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.swing.JTextPane;
import resultEditor.Differences.IAADifference.ArticleDifference;
import resultEditor.Differences.IAADifference.Difference;
import resultEditor.Differences.IAADifference.Differences;
import resultEditor.annotations.*;
import userInterface.GUI;
import userInterface.GUI.ReviewMode;


/**
 * This class is designed to search difference matching to annotations with
 * same or crossed spans; Differences in markable category(class), complex
 * relationships or simple attributes will be figure out and marked with
 * read wavy underline.
 *
 *
 * @author  Jianwei Chris Leng,
 *          Nov 2, 2010,
 *          Division of Epidemiolofy, School of Medicine, University of Utah.
 */
public class DifferentMatching {

    /**a list that records all diff-matchings*/
    public static ArrayList<Diff> __depot_DifferentMatching = new ArrayList<Diff>();
    
    protected JTextPane textcomp;
    Thread thisthread = null;

    public DifferentMatching(JTextPane comp){
        this.textcomp = comp;
    }

    public void clear(){
        __depot_DifferentMatching.clear();
        try{
            if(textcomp!=null){
                // remove all existing wave underlines
                resultEditor.underlinePainting.SelectionHighlighter painter
                    = new resultEditor.underlinePainting.SelectionHighlighter(this.textcomp);
                painter.RemoveAllWaveHighlight();
            }
        }catch(Exception ex){}
    }

    /**
     * Primary method of class "DifferentMatching". This method is used to search
     * and find difference matching: cross overlap spans, or exact overlap spans.
     */
    public void search_differentMatching(final File _currentDocument) {

        try {
            if (thisthread != null) {
                //thisthread.getAllStackTraces().clear();
                thisthread.interrupt();
            }
        } catch (Exception ex) {
            
        }

        /*
         * thisthread = new Thread() {
         *
         * @Override public void run() {
         */
        clear();

        if ((_currentDocument == null) || (_currentDocument.getName() == null)) {
            return;
        }

        if (!env.Parameters.enabled_Diff_Display) {
            return;

        }

        if (GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE) {
            
            findRepetitive(_currentDocument);

            checkFounds_forUnique();

            drawDiff_inWaveLine();

        } else if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
            underlineNonMatches(_currentDocument.getName());
        }


        /*
         * }; thisthread.start();
        *
         */

    }

    

    /**
     * Check for difference matching to these annotations with same overlapped
     * or cross-spans. Propoal is to find difference in their class, complex
     * relationships or normal attributes.
     *
     * @param   _document
     *          File of Current handling text source. It can be used to indentfy
     *          which "article" contains annotations for current document in
     *          depot of annotations.
     */
    public void findRepetitive(File _document){
        try{

            __depot_DifferentMatching.clear();

            // ##1## get the depot of all annotations
            //resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            //adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
            

            Article article = null;
            // ##2## get annotations of current document
            if( GUI.reviewmode == ReviewMode.ANNOTATION_MODE ){                
                article = resultEditor.annotations.Depot.getArticleByFilename(_document.getName().trim());
            }else{
                article = adjudication.data.AdjudicationDepot.getArticleByFilename(_document.getName().trim());
            }
            
            if (article ==  null)   return;
            if( article.annotations == null ) return;
            int size = article.annotations.size();


            // ##3## go over all annotations in current document
            for(int i=0; i< size; i++) {
                
                Annotation annotation = article.annotations.get(i);

                // check validity of the annotation in the first loop
                if (annotation==null)
                    continue;     
                
                if ( annotation.visible == false)
                    continue;
                
                if( ( annotation.spanset == null ) || (annotation.spanset.size()<1 ) )
                    continue;
               

                for( int j=0; j<size; j++ ){

                    if (i==j)
                        continue;

                    Annotation compare_annotation = article.annotations.get(j);
                    if(compare_annotation == null)
                        continue;
                    
                    if( compare_annotation.visible == false )
                        continue;
                    
                    if( ( compare_annotation.spanset == null )
                    || (compare_annotation.spanset.size()<1 ) )
                    continue;

                    /**check for exact same spans*/
                    if (env.Parameters.DifferenceMatching.checkSameOverlappingSpan) {
                        
                        if (compare_annotation.spanset.isDuplicates(annotation.spanset)) {
                            int size_dup = compare_annotation.spanset.size();
                            for (int idx_dup = 0; idx_dup < size_dup; idx_dup++) {
                                SpanDef span = compare_annotation.spanset.getSpanAt(idx_dup);
                                if( span != null ){
                                    __depot_DifferentMatching.add(
                                        new Diff(
                                            span.text,
                                            span.start,
                                            span.end,
                                            1 )
                                        );
                                }
                            }
                            continue;
                        }
                    }


                    // check for overlap spans                    
                     if( env.Parameters.DifferenceMatching.checkCrossSpan ){

                        // one big span contains a smaller span
                        SpanSetDef overlaps = compare_annotation.spanset.getOverlapping( annotation.spanset );

                        if( ( overlaps != null ) && ( overlaps.size() > 0 ) ){
                            for(int idx_overlap = 0; idx_overlap< overlaps.size(); idx_overlap++ ){
                                SpanDef span = overlaps.getSpanAt(idx_overlap);
                                if(span != null)
                                    __depot_DifferentMatching.add(new Diff( "" , span.start, span.end, 3));
                            }
                            
                        }

                    }
                    


                }
            }
            
        }catch(Exception ex){
        }
    }

    

    /**make sure only unique diff can be stored after pervious draft searching.*/
    private void checkFounds_forUnique(){
        try{
            
            if (__depot_DifferentMatching==null)
                return;
            
            if ( __depot_DifferentMatching.size() < 1 )
                return;
            
            
            for(int i = 1; i< __depot_DifferentMatching.size() ; i++ ){
                Diff diff = __depot_DifferentMatching.get(i);
                if((diff==null)||(diff.spanstart<0)||(diff.spanend<0))
                    continue;
                
                for(int j=1; j< __depot_DifferentMatching.size(); j++ ){
                    if( i==j )
                        continue;
                    Diff compare_diff = __depot_DifferentMatching.get(j);
                    if((compare_diff==null)||(compare_diff.spanstart<0)||(compare_diff.spanend<0))
                        continue;

                    if ((diff.spanstart == compare_diff.spanstart) && (diff.spanend == compare_diff.spanend)) {
                        compare_diff.spanstart = -1;                       
                        compare_diff.spanend = -1;
                    }
                }
            }
            
            
            for(int t = __depot_DifferentMatching.size() - 1; t >= 0; t-- ){
                Diff diff = __depot_DifferentMatching.get(t);
                if((diff==null)||(diff.spanstart<0)||(diff.spanend<0))
                    __depot_DifferentMatching.remove(t);                                
            }
            

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1010281728::"+ex.toString());
            ex.printStackTrace();
        }
    }

    private void drawDiff_inWaveLine(){
        try{
            // remove all existing wave underlines
            resultEditor.underlinePainting.SelectionHighlighter painter
                = new resultEditor.underlinePainting.SelectionHighlighter(this.textcomp);
            painter.RemoveAllWaveHighlight();

            if( __depot_DifferentMatching == null )
                return;
            if( __depot_DifferentMatching.size()<1 )
                return;
            
            for( int i=0; i < __depot_DifferentMatching.size(); i++ ){
                
                Diff diff = __depot_DifferentMatching.get(i);
                if(diff==null)
                    continue;
                
                //System.out.println("\nstart: ");
                //System.out.print(diff.spanstart + "/ end: "+ diff.spanend);
                painter.addNewWavelineHightlight(diff.spanstart, diff.spanend);
                //System.out.println("end\n");
                
                
            }

            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1011011451A: fail to show wave underline" +
                    " to indicate these different matching annotations.\nRelated Error:" + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**use red wavy underline to highlight all nonmatches (non-matched 
     * annotations) which we got after running the IAA analysis module. 
     * This will be used to indicate where are diffs for adjudication mode.
     */
    private void underlineNonMatches(String filename) {
        try {
            // #### 1 #### remove all existing wave underlines
            resultEditor.underlinePainting.SelectionHighlighter painter = new resultEditor.underlinePainting.SelectionHighlighter(this.textcomp);
            painter.RemoveAllWaveHighlight();

            //
            ArticleDifference ad = Differences.getArticle(filename);

            // do nothing if no saved non-matches to this document
            if (ad == null) {
                log.LoggingToFile.log(Level.INFO, "111010131648:: didn't find saved non-matches to current document:" + filename);
                return;
            }

            if (ad.differences == null) {
                log.LoggingToFile.log(Level.SEVERE, "111010131649:: didn't find any saved non-matches to current document:" + filename);
                return;
            }

            for (Difference difference : ad.differences) {
                if(difference==null)
                    continue;

                painter.addNewWavelineHightlight( difference.differenceStart, difference.differenceEnd );
//                System.out.println("highlight start: " + difference.differenceStart + "/ end: "+ difference.differenceEnd);
            }            

        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error 1011011451: fail to show wave underline" +
                    " to indicate these different matching annotations.");
        }

    }

}



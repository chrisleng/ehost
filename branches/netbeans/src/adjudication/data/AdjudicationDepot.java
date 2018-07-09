/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adjudication.data;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.text.JTextComponent;
import resultEditor.annotations.*;
import resultEditor.annotations.Annotation.AdjudicationStatus;
import resultEditor.workSpace.WorkSet;
import userInterface.GUI;
/**
 *
 * @author imed
 */
public class AdjudicationDepot {
    
    /**
     * A static vector memory, used to record all documents and their 
     * annotations only for the adjudication mode. So we can isolate 
     * operations on annotations between adjudication mode and 
     * annotation mode.
     * 
     */
    protected static Vector<Article> depotOfAdj = new Vector<Article>();

    /**
     * get article by the filename of the text source.
     *
     * @param filename This is the filename of the text source, it will be used
     * to located where is the article space in the depot.
     */
    public static Article getArticleByFilename(String filename) {
        // validity check
        if (filename == null) {
            return null;
        }

        // check space exists
        if (!articleExists(filename)) {
            return null;
        }

        int size = depotOfAdj.size();
        // go over each article in depot and compare article name with given
        // filename
        filename = filename.trim();
        for (int i = 0; i < size; i++) {
            String articlefilename = depotOfAdj.get(i).filename.trim();
            if (articlefilename.compareTo(filename) == 0) {
                return depotOfAdj.get(i);
            }
        }

        return null;

    }
    
    public static boolean isReady(){
        if((depotOfAdj==null)||(depotOfAdj.size()<1))
            return false;
        else
            return true;
    }
    /**
     * Check whether depot space(article) for a specific filename exists or not.
     *
     * @param filename The file name of the text file for the depot space which
     * you want to check.
     * @return false, if depot space for this file name doesn't exist<p> true,
     * if exists.<p>
     */
    public static boolean articleExists(String filename) {
        // validity check
        if (filename == null) {
            return false;
        }

        int size = depotOfAdj.size();
        // go over each article in depot and compare article name with given
        // filename
        //boolean exists = false;
        filename = filename.trim().toLowerCase();
        for (int i = 0; i < size; i++) {
            String articlefilename = depotOfAdj.get(i).filename.trim().toLowerCase();
            if (articlefilename.compareTo(filename) == 0) {
                return true;
            }
        }

        return false;
    }
    
    /**remove all annotations under adjudication mode.*/
    public static void clear(){
        depotOfAdj.clear();
    }
    
    

    /**
     * This method is to build a designate relationship between two give
     * annotations: a and b in specific document.
     *
     * @param filename file name of current reviewing document on the document
     * viewer.
     *
     * @param a the annotation in the start point of this pair / relationship.
     *
     * @param b the annotation in the end point of this pair / relationship.
     *
     * @param relationshipname the name of the relationship. it's simple string.
     *
     */
    public static void buildRelationship(String filename, String relationshipname, Annotation a, Annotation b) {
        if ((filename == null) || (a == null) || (b == null)) {
            return;
        }

        if ((relationshipname == null) || (relationshipname.trim().length() < 1)) {
            return;
        }

        Article article = getArticleByFilename(filename);
        if (article == null) {
            return;
        }
        
        if (article.annotations == null) {
            return;
        }

        for (Annotation ann : article.annotations) {
            if (ann == null) {
                continue;
            }

            if (ann.uniqueIndex == a.uniqueIndex) {
                if (a.relationships == null) {
                    a.relationships = new Vector<AnnotationRelationship>();
                }

                AnnotationRelationship newRelationship = new AnnotationRelationship(relationshipname);
                newRelationship.addLinked(new AnnotationRelationshipDef(b));
                a.relationships.add(newRelationship);

                break;
            }
        }
    }
    
    
    public void copyDepotOfAnn(){
        clear();
        
        resultEditor.annotations.Depot depotOfAnn = new resultEditor.annotations.Depot();
        if(( depotOfAnn != null )||(depotOfAnn.getDepot() == null))
            return;
            
        for( Article article : depotOfAnn.getDepot() ) {
            if( article != null )
                depotOfAdj.add(article.getCopy());
        }
    }

    public ArrayList<Article> getAllArticles() {
        ArrayList<Article> toReturn = new ArrayList<Article>();
        toReturn.addAll( depotOfAdj );
        return toReturn;
    }

    
    /**
     * Prepare annotations by copy specific annotations from resulteditor.annotations.depot 
     * to adjudication.data.depotOfAdj, set annotation's adjudication status of 
     * a specific document
     *
     *
     * @param isCheckingSpecificFile tell method whether we want to reset
     * annotation's adjudication status or just reset annotation whose status is
     * not OK, MATCHES_DLETED, NONMATCHES_DLETED
     *
     * @param currentFile name of the current document. Method do nothing if
     * it's null.
     *
     * @param isCheckingReloadedData tell us we are going back to previous
     * adjudication work.
     *
     *
     */
    public void resetAnntationStatus( ArrayList<String> _selectedAnnotators,
            ArrayList<String> _selectedClasses, 
            String currentFile,  
                // if this one is not full, then we will only deal with this 
                // file, otherwise we need to deal with all files in this project.
            boolean isCheckingSpecificFile,
            boolean isCheckReloadedData) throws Exception 
    {        
        try{
            
            if ((isCheckingSpecificFile) && (currentFile == null)) {
                return;
            }
            if ((isCheckingSpecificFile) && (currentFile.trim().length() < 1)) {
                return;
            }

            // remove old datas if we are deal with all annotations.
            //if( currentFile == null )
            //    adjudication.data.AdjudicationDepot.clear();
            

            ArrayList<Article> articles = this.getAllArticles();

            if (articles == null) {
                throw new Exception(
                        "1110140439::can not get saved annotations.");
            }

            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                if ((isCheckingSpecificFile)
                        && (!(currentFile.trim().compareTo(article.filename) == 0))) {
                    continue;
                }

                int size = article.annotations.size();

                // reset all processed flags
                for (int i = 0; i < size; i++) {
                    Annotation annotation = article.annotations.get(i);
                    if (annotation == null) {
                        continue;
                    }

                    //if(!annotation.visible)
                    //    continue;

                    if ((isCheckingSpecificFile) || (isCheckReloadedData)) {
                        // if( annotation. )
                        if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                            continue;
                        }
                        if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_DLETED) {
                            continue;
                        }
                        if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.NONMATCHES_DLETED) {
                            continue;
                        }
                    }

                    annotation.adjudicationAlias = null;

                    //annotation.adjudicationStatus = Annotation.AdjudicationStatus.EXCLUDED;

                    //boolean included = false;
                    //// only deal with annotations whose annotator is in our list
                    //if ((annotation.getAnnotator() != null)
                    //        && (annotation.getAnnotator().trim().length() > 0)) {
                    //    for (String annotator : _selectedAnnotators) {
                    //        if (annotator == null) {
                    //            continue;
                    //        }
                    //        if (annotation.getAnnotator().trim().compareTo(
                     //               annotator.trim()) == 0) {
                                annotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                                annotation.setUnProcessed();
                    //            included = true;
                    //            break;
                    //        }

                    //    }

                   //     if (!included) {
                   //         //annotation.setVisible( false );
                   //         continue;
                   //     }
                   // }

                    // only deal with annotations whose class is in our list.
                    //if (included) {
                    //    boolean class_is_selected = false;
                    //    if ((annotation.annotationclass != null)
                    //           && (annotation.annotationclass.trim().length() > 0)) {
                    //        for (String annotationclass : _selectedClasses) {
                    //            if (annotationclass == null) {
                    //                continue;
                    //            }
                                //if (annotation.annotationclass.trim().compareTo(annotationclass.trim()) == 0) {
                                    annotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                                    annotation.setUnProcessed();
                                //    class_is_selected = true;
                                //    break;
                                //}

                            //}
                        //}
                        //if (!class_is_selected) {
                        //    annotation.adjudicationStatus = Annotation.AdjudicationStatus.EXCLUDED;
                        //    continue;
                        //}
                    //}

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("failed to prepare annotations for adjudication mode."
                    + ex.getMessage());

        }
    }

    public void copyAnnotations(
            ArrayList<String> _selectedAnnotators, 
            ArrayList<String> selectedClasses, 
            boolean _isInitialCheck) {
        
        if( !_isInitialCheck )
            return;
            
        
        clear();
        
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if(( depot == null )||(depot.getDepot() == null))
            return;
            
        for( Article article : depot.getDepot() ) {
            if( article != null ){
                
                Article newarticle = getCopyOfAtricle( article, 
                        _selectedAnnotators, selectedClasses );
                
                if(newarticle!=null)
                    depotOfAdj.add( newarticle );
            }
        }    
    }

    private Article getCopyOfAtricle(Article article,  ArrayList<String> _selectedAnnotators, 
            ArrayList<String> selectedClasses ) {
        if(article == null)
            return article;
        
        Article newarticle = new Article(article.filename);
        
        if (article != null) {
            for (Annotation annotation : article.annotations) {
                if (annotation == null) {
                    continue;
                }
                //int i = 0;
                //if (i < i + 1) {
                //    continue;
                //}
                boolean included = false;

                // only deal with annotations whose annotator is in our list
                if ((annotation.getAnnotator() != null)
                        && (annotation.getAnnotator().trim().length() > 0)) {
                    
                    for (String annotator : _selectedAnnotators) {
                        if (annotator == null) {
                            continue;
                        }
                        if (annotation.getAnnotator().trim().compareTo(
                                annotator.trim()) == 0) {
                            
                            included = true;
                            break;
                        }

                    }

                    if (!included) {
                        //annotation.setVisible( false );
                        continue;
                    }
                } else {
                    continue;
                }
                
                if (included) {
                        boolean class_is_selected = false;
                        if ((annotation.annotationclass != null)
                                && (annotation.annotationclass.trim().length() > 0)) {
                            for (String annotationclass : selectedClasses) {
                                if (annotationclass == null) {
                                    continue;
                                }
                                if (annotation.annotationclass.trim().compareTo(annotationclass.trim()) == 0) {
                                    annotation.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                                    annotation.setUnProcessed();
                                    class_is_selected = true;
                                    break;
                                }

                            }
                        }
                        if (!class_is_selected) {
                            annotation.adjudicationStatus = Annotation.AdjudicationStatus.EXCLUDED;
                            continue;
                        }
                    }
                
                

                // copy this annotation
                Annotation newann = annotation.getCopy();
                newann.adjudicationStatus = Annotation.AdjudicationStatus.NON_MATCHES;
                newann.setUnProcessed();
                newarticle.annotations.add( newann );
            }
        }

        return newarticle;
    }
    
    /**
     * Get annotation by its unique index.
     *
     * @param unique unique index of this annotation which you want to find.
     *
     * @return null if don't find; Or return instance of class "Annotation"
     */
    public Annotation getAnnotationByUnique(String filename, int unique) {
        try {
            Article article = getArticleByFilename(filename);
            if ((article == null) || (article.annotations == null)) {
                return null;
            }
            for (Annotation annotation : article.annotations) {
                if (annotation.uniqueIndex == unique) {
                    return annotation;
                }
            }

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1101032047::fail to find annotation by "
                    + "filename and unique index;\n" + ex.toString());
        }
        return null;
    }
    
    
    /**
     * Delete an annotation from depot by its UID (Unique Index ID, a integer).
     *
     * @param _UID The unique ID for annotation. An annotation have and only
     * have one unique integer number as its ID.
     *
     * @return Report how many annotations have been deleted. It should be 1.
     */
    public static int deleteAnnotation_byUID(String filename, int _UID) {

        try {
            if (filename == null) {
                return 0;
            }
            if (filename.trim().length() < 1) {
                return 0;
            }

            Article article = getArticleByFilename(filename.trim());
            if ((article == null) || (article.annotations == null)) {
                return 0;
            }

            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.uniqueIndex == _UID) {
                    //if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                        article.annotations.get(i).adjudicationStatus = Annotation.AdjudicationStatus.NONMATCHES_DLETED;
                    //} else {
                        WorkSet.addLastDeleted(article.filename, article.annotations.get(i));
                        article.annotations.removeElementAt(i);
                        
                        //Article article = articles.get(i);
                        

                        if (article.annotations == null) {
                            continue;
                        }

                        int annotation_size = article.annotations.size();
                        for (int iu = annotation_size - 1; iu >= 0; iu--) {
                            Annotation annotation2 = article.annotations.get(iu);
                            if (annotation2 == null) {
                                continue;
                            }
                            if (annotation2.relationships == null) {
                                continue;
                            }

                            Vector<resultEditor.annotations.AnnotationRelationship> relationships = annotation2.relationships;
                            int size_relationships = relationships.size();
                            for (int cr_index = size_relationships - 1; cr_index >= 0; cr_index--) {
                                AnnotationRelationship cr = relationships.get(cr_index);
                                if (cr == null) {
                                    continue;
                                }
                                if (cr.linkedAnnotations == null) {
                                    continue;
                                }
                                Vector<AnnotationRelationshipDef> linkeds = cr.linkedAnnotations;
                                int sizea = linkeds.size();

                                for (int m = sizea - 1; m >= 0; m--) {
                                    boolean found = false;
                                    AnnotationRelationshipDef linedAnnotation = linkeds.get(m);
                                    if (linedAnnotation == null) {
                                        continue;
                                    }

                                    if ((linedAnnotation.linkedAnnotationIndex == _UID)
                                            && (_UID > 0)) {
                                        relationships.removeElementAt(cr_index);

                                        log.LoggingToFile.log(Level.INFO, "We just delete a relationship after deleted an annotation.");
                                        found = true;
                                        break;
                                    }
                                    if (found) {
                                        break;
                                    }
                                }
                            }



                        }
                        break;
                    //}
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }

        return 1;
    }

    
    /**
     * Get annotation by its index in the array. It's not safe after you
     * deleting or adding some annotations. **Suggest** using unique index of
     * annotation to identify them.
     */
    public Annotation getAnnotation(String filename, int index) {
        Article article = getArticleByFilename(filename);
        if (article == null) {
            return null;
        }
        if (index >= article.annotations.size()) {
            return null;
        }
        return article.annotations.get(index);
    }
    
    public int AnnotationRangeSet(
            JTextComponent _tc,
            String filename,
            int uniqueindex,
            int start,
            int end,
            int type) {

        try {
            if (filename == null) {
                return 0;
            }
            if (filename.trim().length() < 1) {
                return 0;
            }

            Article article = null;
            if( GUI.reviewmode == GUI.reviewmode.ANNOTATION_MODE ){
                article = getArticleByFilename(filename.trim());
            }else if( GUI.reviewmode == GUI.reviewmode.adjudicationMode ) {
                article = adjudication.data.AdjudicationDepot.getArticleByFilename( filename.trim() );
            }
            
            if ((article == null) || (article.annotations == null)) {
                return 0;
            }

            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.spanset == null) {
                    continue;
                }

                if (annotation.uniqueIndex == uniqueindex) {

                    int size = annotation.spanset.size();
                    for (int t = 0; t < size; t++) {
                        SpanDef span = annotation.spanset.getSpanAt(t);
                        if (span == null) {
                            continue;
                        }
                        if ((span.start == start) && (span.end == end)) {
                            start = (type == 1 ? start - 1 : start);
                            start = (type == 3 ? start + 1 : start);
                            end = (type == 2 ? end + 1 : end);
                            end = (type == 4 ? end - 1 : end);


                            span.start = start;
                            span.end = end;
                            String newspan = getText(_tc, annotation.spanset);
                            annotation.annotationText = newspan;
                            annotation.creationDate = commons.OS.getCurrentDate();
                            break;
                        }
                    }
                    break;
                    
                }
                
            }




        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }
        return 1;

    }
    
    
    /**
     * To an given spanset of an annotation, get the combined annotation texts
     * which splitted by " ... ".
     */
    private String getText(JTextComponent _tc, SpanSetDef spanset) {
        try {
            if (spanset == null) {
                return null;
            }

            int size = spanset.size();
            if (size <= 0) {
                return null;
            }

            // #### 1 ------------------------------------- //
            // sort spans by their spanstart

            // #### 1.1 copy spans into an array to sort
            SpanDef[] spans = new SpanDef[size];
            for (int i = 0; i < size; i++) {
                SpanDef span = spanset.getSpanAt(i);
                spans[i] = span;
            }

            // #### 1.2 sort spans in the array
            for (int i = 0; i < size; i++) {
                SpanDef span1 = spans[i];
                if (span1 == null) {
                    continue;
                }

                for (int j = i + 1; j < size; j++) {
                    if (i == j) {
                        continue;
                    }
                    SpanDef span2 = spans[j];
                    if (span2 == null) {
                        continue;
                    }

                    if (span1.start > span2.start) {
                        //SpanDef tempSpan = span2;
                        SpanDef tempspan = new SpanDef(span1.start, span1.end, span1.text);
                        spans[i] = span2;
                        spans[j] = tempspan;

                        span2 = spans[j];
                        span1 = spans[i];
                    }
                }
            }
            // 1 #### ------------------------------------- //

            // output for debug
            /*
             * for (int i = 0; i < size; i++) { SpanDef span1 = spans[i]; if
             * (span1 == null) { continue; }
             *
             * System.out.println("["+i+"]: ( " + span1.start + ", " + span1.end
             * + " )" ); }
             */

            String annotationText = "";
            boolean isFirstValidRecord = true;
            for (int i = 0; i < size; i++) {
                SpanDef span = spans[i];
                if (span == null) {
                    continue;
                }

                if (isFirstValidRecord) {
                    annotationText = _tc.getText(span.start, span.end - span.start);
                    isFirstValidRecord = false;
                } else {
                    annotationText = annotationText
                            + " ... "
                            + _tc.getText(span.start, span.end - span.start);
                }

            }

            return annotationText;
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1202151405::fail to get all span texts"
                    + ex.getLocalizedMessage());
            return null;
        }
    }
    
    public void addANewAnnotation(String filename,
            String annotationText, SpanSetDef spanset,
            String creationDate,
            String classname,
            String annotatorname, String annotatorid,
            Vector<AnnotationAttributeDef> normalrelstionships,
            Vector<AnnotationRelationship> complexrelationships,
            int uniqueIndex) {

        
        //System.out.println(" + try to record to file: [" + filename.trim() + "]");
        articleInsurance(filename.trim());
        Article article = this.getArticleByFilename(filename.trim());
        if (article == null) {
            return;
        }

        

        String mentionid = "eHOST_" + resultEditor.annotations.Depot.newMentionID();
        //String annotator = "eHOST_User_hard_Code";        

        Annotation annotation = new Annotation();
        
        annotation.annotationText = annotationText;
        annotation.annotationclass = classname;
        annotation.setAnnotator( annotatorname );
        annotation.annotatorid = annotatorid;

        
        annotation.comments = "";
        annotation.creationDate = creationDate;
        annotation.mentionid = mentionid;
        annotation.spanset = spanset;
        
        annotation.attributes = normalrelstionships;
        annotation.relationships = complexrelationships;
        
        annotation.uniqueIndex = uniqueIndex;

        
        if ((annotation.getAnnotator() !=null)&&(annotation.getAnnotator().compareTo("ADJUDICATION") == 0)) {
            annotation.setUnProcessed();
        }

        
        
        article.annotations.add(annotation);

        // set current workset
        int size = article.annotations.size();
        resultEditor.workSpace.WorkSet.currentAnnotation = article.annotations.get(size - 1);
        resultEditor.workSpace.WorkSet.indexOfCurrentAnnotation = size - 1;




    }
    
    /**
     * check existing of depot space for a specific text file by the filename of
     * the text source. And build a new space if space is not available.
     *
     * @param files The filename of a specific text source.
     */
    public void articleInsurance(String filename) {
        // validity check
        if (filename == null) {
            return;
        }
        if (filename.trim().length() < 1) {
            return;
        }

        // build the article/space if it does not exist
        if (!articleExists(filename)) {
            Article article = new Article(filename.trim());
            // add article into depot
            add(article);
        }

    }
    
    /**
     * add a new article into the depot
     */
    public void add(Article article) {
        AdjudicationDepot.depotOfAdj.add(article);
    }
    
    /**
     * Add a new annotation into exsiting annotations to a txt file.
     *
     * @param file indicate this annotation is extracted from which txt file;
     * @param annotationText the text content of the new annotation.
     * @param start the start position of the annotation.
     * @param end the end position of the annotation.
     */
    public void addANewAnnotation(String filename,
            String annotationText, int start, int end,
            String creationDate,
            String classname,
            String annotatorname, String annotatorid,
            Vector<AnnotationAttributeDef> normalrelstionships,
            Vector<AnnotationRelationship> complexrelationships,
            int uniqueIndex) {

        SpanSetDef spanset = new SpanSetDef();
        spanset.addSpan(start, end);

        addANewAnnotation(filename,
                annotationText, spanset,
                creationDate,
                classname,
                annotatorname, annotatorid,
                normalrelstionships,
                complexrelationships,
                uniqueIndex);




    }
    
    public void addANewAnnotation_Oracle(String filename,
            String annotationText, int start, int end,
            String creationDate,
            String classname,
            String annotatorname, String annotatorid,
            Vector<AnnotationAttributeDef> normalrelstionships,
            Vector<AnnotationRelationship> complexrelationships,
            int uniqueIndex) {

        SpanSetDef spanset = new SpanSetDef();
        spanset.addSpan(start, end);

        articleInsurance(filename.trim());
        Article article = this.getArticleByFilename(filename.trim());
        if (article == null) {
            return;
        }

        

        String mentionid = "eHOST_" + resultEditor.annotations.Depot.newMentionID();
        //String annotator = "eHOST_User_hard_Code";        

        Annotation annotation = new Annotation();
        
        annotation.annotationText = annotationText;
        annotation.annotationclass = classname;
        annotation.setAnnotator( "ADJUDICATION" );
        annotation.annotatorid = "0000";

        
        annotation.comments = "";
        annotation.creationDate = creationDate;
        annotation.mentionid = mentionid;
        annotation.spanset = spanset;
        
        annotation.attributes = normalrelstionships;
        annotation.relationships = complexrelationships;
        
        annotation.uniqueIndex = uniqueIndex;

        
        
            annotation.setProcessed();
        annotation.adjudicationStatus = AdjudicationStatus.MATCHES_OK;

        
        
        article.annotations.add(annotation);

        // set current workset
        int size = article.annotations.size();
        resultEditor.workSpace.WorkSet.currentAnnotation = article.annotations.get(size - 1);
        resultEditor.workSpace.WorkSet.indexOfCurrentAnnotation = size - 1;





    }
    
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            SpanSetDef spanset, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            boolean isProcessed,
            String _adjudicationStatus,
            int uniqueindex) {

        // get entrance
        Article article = getArticleByFilename(filename);

        // validity check
        if (article == null) {
            log.LoggingToFile.log(Level.SEVERE, "Depot.java: can not get entrance to depot!!!");
            return;
        }

        // assemble an annotation for storage
        Annotation annotation = new Annotation();
        annotation.annotationText = annotationText;
        annotation.mentionid = mentionid;
        annotation.spanset = spanset;
        annotation.annotationclass = annotationClass;
        annotation.setAnnotator( annotator );
        annotation.annotatorid = annotatorid;
        annotation.creationDate = creationdate;
        annotation.comments = comment;
        annotation.verifierSuggestion = verifierSuggestion;
        annotation.attributes = normalRelationships;
        annotation.relationships = complexRelationships;
        annotation.uniqueIndex = uniqueindex;
       
        if( isProcessed )
            annotation.setProcessed(); 
        else
            annotation.setUnProcessed();

        /**
         * data type that use to indicate the adjudication status of an
         * annotation public enum AdjudicationStatus { NOBODY, MATCHES_OK,
         * MATCHES_DLETED, NON_MATCHES, UNPROCESSED, NONMATCHES_DLETED };
         */
        /**
         * variable that use to indicate the adjudication status of an
         * annotation, default is "NOBODY", it means that this annotation will
         * not be count into subsequent adjudication processing;
         *
         * public AdjudicationStatus adjudicationStatus =
         * AdjudicationStatus.NOBODY;
         */
        
        annotation.adjudicationStatus = AdjudicationStatus.EXCLUDED;
    
        if( _adjudicationStatus.trim().equals( "MATCHES_OK" ) )
            annotation.adjudicationStatus = AdjudicationStatus.MATCHES_OK;
        else if( _adjudicationStatus.trim().equals( "MATCHES_DLETED" ) )
            annotation.adjudicationStatus = AdjudicationStatus.MATCHES_DLETED;
        else if( _adjudicationStatus.trim().equals( "NON_MATCHES" ) )
            annotation.adjudicationStatus = AdjudicationStatus.NON_MATCHES;
        else if( _adjudicationStatus.trim().equals( "UNPROCESSED" ) )
            annotation.adjudicationStatus = AdjudicationStatus.UNPROCESSED;
        else if( _adjudicationStatus.trim().equals( "NONMATCHES_DLETED" ) )
            annotation.adjudicationStatus = AdjudicationStatus.NONMATCHES_DLETED;                  
    
        //System.out.println( "annotation {" + annotation.annotationText + " } = " + annotation.adjudicationStatus + "/" + _adjudicationStatus );
        
        article.annotations.add(annotation);


        // record annotated class name from XML imported
        resultEditor.annotationClasses.Depot.addElement(annotationClass, "XML",
                resultEditor.annotationClasses.Manager.allocateColor(),
                true, false);

        //System.out.println(" + record annotation: [" + annotationText
        //        + "] into article: [" + filename + "] at [" + spanstart + ", "
        //       + spanend + " ]; class:"+annotationClass+";" );

    }
    
    
}

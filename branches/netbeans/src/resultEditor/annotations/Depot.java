/*
 * Depot.java 
 * 
 * Jianwei Leng at June 22, 2010 3:30pm MST
 * 
 */
package resultEditor.annotations;

import adjudication.statusBar.DiffCounter;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.text.JTextComponent;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.annotationClasses.navigationTree.dataCache.DepotOfAttributes;
import resultEditor.annotations.Annotation.AdjudicationStatus;
import resultEditor.workSpace.WorkSet;
import userInterface.GUI;

/**
 * This is a data bank/depot to store all annotations for current eHOST project.
 *
 * This is a class with a static vector which used to store all annotations of
 * current eHOST project. All files and their annotations will be stored here
 * one by one, and each file and their annotations will be treated as an
 * instance of class "Article". Each article has a list of its annotations, and
 * each annotation might has its attributes and relationships.
 *
 * Methods in this class can used to add/modify/remove annotations, their
 * attributes, and relationship, or even the whole article.
 *
 *
 *
 * @author Jianwei Leng at June 22, 2010 3:30pm MST
 *
 */
public class Depot {

    /**
     * A static vector used to record all documents and their annotations
     */
    protected static Vector<Article> depot = new Vector<Article>();

    /**
     * Delete the given duplicate annotations. Information of the annotations for
     * deleting can be found in the given parameter "_duplicateAnnotations". That
     * parameter is a structured data that contains a reference annotation and 
     * a list of the duplicate annotations. Empty the duplicate annotations 
     * can delete all duplicates of the annotation and only left the reference 
     * one in memory.
     * 
     * @param   _filenam    
     *          The string of the file name of a file. It's the basic file name 
     *          of the document which contains this annotation.

     * 
     * 
     * @param   _duplicateAnnotations
     *          A list of duplicate annotations. There is 
     * 
     */
    public static void deleteDuplicates(String _filename, Vector<DupilcateAndGhost> _duplicateAnnotations) {
        try {
            
            // do nothing if get empty list of duplicate annotations
            if ((_duplicateAnnotations == null) || (_duplicateAnnotations.size() < 1)) {
                return;
            }
            
            // go over all duplicate annotations and delete them
            for (DupilcateAndGhost duplicate : _duplicateAnnotations) {

                if (duplicate == null) {
                    continue;
                }


                // List of types
                // type=1   :   duplicates
                // type=4   :   classless
                // type=5   :   spanless
                // type=6   :   out of range             
                if ((duplicate.type == 1) && (duplicate.selected)) {
                    if ((duplicate.duplicates == null) || (duplicate.duplicates.size() < 1)) {
                        continue;
                    }
                    
                    // go over all given annotation duplicates, and delete them one by one
                    for (Annotation ann : duplicate.duplicates) {
                        
                        if( ann == null )
                            continue;
                        
                        // delete the annotation by its UID
                        deleteAnnotation_byUID(_filename, ann.uniqueIndex);
                    }
                }
            }
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1105310238::");
        }
    }

    
    /**This method is used to delete the given ghost annotations. An annotation
     * is a ghost annotation if it didn't have an annotation-class-name, or any 
     * one of its spans is out of range of corresponding document, or the length 
     * of its span is Zero.
     *  
     * @param   _filenam    
     *          The string of the file name of a file. It's the basic file name 
     *          of the document which contains this annotation.
     * 
     * @param   _ghostAnnotations
     *          Previous found ghost annotations.
     */
    public static void deleteGhosts(String _filename, Vector<DupilcateAndGhost> _ghostAnnotations) {
        try {
            if ((_ghostAnnotations == null) || (_ghostAnnotations.size() < 1)) {
                return;
            }
            for (DupilcateAndGhost ghost : _ghostAnnotations) {
                if (ghost == null) {
                    continue;
                }

                if (ghost.selected) {
                    if ((ghost.type == 4) || (ghost.type == 5) || (ghost.type == 6)) {
                        if (ghost.referenceAnnotation == null) {
                            continue;
                        }

                        deleteAnnotation_byUID(_filename, ghost.referenceAnnotation.uniqueIndex);

                    }
                }
            }
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1105310239::");
        }
    }

    /**change class names in all annotations. */
    public static void renClassInAnnotations(String oldclassname, String newclassname) {
        if( ( oldclassname == null )||( oldclassname.trim().length() < 1 ))
            return;
        
        if( ( newclassname == null )||( newclassname.trim().length() < 1 ))
            return;
        
        newclassname = newclassname.trim();
        
        
        for( Article article : depot ){
            if(article==null) 
                continue;
            if(article.annotations==null) 
                continue;
            
            for(Annotation annotation : article.annotations ){
                if( annotation == null )
                    continue;
                if( annotation.annotationclass == null )
                    continue;
                
                if( annotation.annotationclass.trim().compareTo( oldclassname.trim() ) == 0  )
                    annotation.annotationclass = newclassname;
                
            }
        }
        
    }

    /**
     * Remove all articles and its annotations from memory.
     */
    public void clear() {
        depot.clear();
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

        int size = depot.size();
        // go over each article in depot and compare article name with given
        // filename
        //boolean exists = false;
        filename = filename.trim().toLowerCase();
        for (int i = 0; i < size; i++) {
            String articlefilename = depot.get(i).filename.trim().toLowerCase();
            if (articlefilename.compareTo(filename) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * add a new article into the depot
     */
    public void add(Article article) {
        Depot.depot.add(article);
    }

    /**
     * save annotation to annotation depot in ResultEditor.Annotations.Depot.
     *
     * @param filename This is the filename of the text source, it will be used
     * to located which article space in the depot the annotation will be saved
     * into.
     */
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            SpanSetDef spanset, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            int uniqueindex) {
        
        recordAnnotation( filename,  mentionid,  annotationText,
             spanset,  annotationClass,
             annotator,  annotatorid,
             creationdate,  comment,  verifierSuggestion,
             normalRelationships,
             complexRelationships,
             uniqueindex);
        
    }
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            SpanSetDef spanset, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            int uniqueindex, boolean isVerified) {

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
        annotation.setVerified( false );

        article.annotations.add(annotation);


        // record annotated class name from XML imported
        resultEditor.annotationClasses.Depot.addElement(annotationClass, "XML",
                resultEditor.annotationClasses.Manager.allocateColor(),
                true, false);

        //System.out.println(" + record annotation: [" + annotationText
        //        + "] into article: [" + filename + "] at [" + spanstart + ", "
        //       + spanend + " ]; class:"+annotationClass+";" );

    }
    
    
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            SpanSetDef spanset, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            boolean isProcessed,
            String _adjudicationStatus,
            int uniqueindex ) {
        
        recordAnnotation( filename,  mentionid,  annotationText,
             spanset,  annotationClass,
             annotator,  annotatorid,
             creationdate,  comment,  verifierSuggestion,
             normalRelationships,
             complexRelationships,
            isProcessed,
            _adjudicationStatus,
            uniqueindex, true);
    }

    
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            SpanSetDef spanset, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            boolean isProcessed,
            String _adjudicationStatus,
            int uniqueindex, boolean isVerified) {

        try{
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
        annotation.setVerified( false );
       
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
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * count all annotations in depot. It was designed for clas
     * "removeduplicates" at first time.
     */
    public int getSizeOfAllAnnotations() {
        int amount = 0;
        for (Article article : depot) {
            if ((article == null) || (article.annotations == null)) {
                continue;
            }
            amount = amount + article.annotations.size();
        }

        return amount;
    }

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

        int size = depot.size();
        // go over each article in depot and compare article name with given
        // filename
        filename = filename.trim();
        for (int i = 0; i < size; i++) {
            String articlefilename = depot.get(i).filename.trim();
            if (articlefilename.compareTo(filename) == 0) {
                return depot.get(i);
            }
        }

        return null;

    }
    /**
     * index of selected annotations in its storage space in this class
     */
    protected static Vector<Integer> selectedAnnotations = new Vector<Integer>();

    /**
     * add index of selected annotation into
     */
    private void saveSelectedAnnotation(int index) {
        if (selectedAnnotations == null) {
            return;
        }

        // check exists or not
        int size = selectedAnnotations.size();
        boolean exists = false;
        for (int i = 0; i < size; i++) {
            if (selectedAnnotations.get(i) == index) {
                exists = true;
                break;
            }
        }

        // save if it's not exist
        if (!exists) {
            selectedAnnotations.add(index);
        }
    }
    
    public void onlyOneAnnotationSelected(int index){
        SelectedAnnotationSet.sets.clear();
        SelectedAnnotationSet.sets.add(index);
    }

    /**
     * To a given position in an article, search and return annotations it
     * selected
     */
    /**
     * THIS METHOD IS ABANDONED!!!!!! replaced by "selectAnnotations_ByPosition"
     * THIS METHOD IS ABANDONED!!!!!! replaced by "selectAnnotations_ByPosition"
     * THIS METHOD IS ABANDONED!!!!!! replaced by "selectAnnotations_ByPosition"
     */
    public Vector<Integer> getSelectedAnnotationIndexes(
            String filename, int position, boolean save) {
        Vector<Integer> oldSelected = null;
        // clear re
        if (!save) {
            oldSelected = (Vector<Integer>) selectedAnnotations.clone();
        }
        selectedAnnotations.clear();

        // validity check: to text source filename
        if (filename == null) {
            return null;
        }
        if (filename.trim().length() < 1) {
            return null;
        }

        // get annotations related to this text source
        Article article = null;
        
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
            adjudication.data.AdjudicationDepot depotOfAdjudication = new 
                    adjudication.data.AdjudicationDepot();
            article = depotOfAdjudication.getArticleByFilename(filename);
        }else 
            article = getArticleByFilename(filename);
        if (article == null) {
            return null;
        }
        Vector<Annotation> annotations = article.annotations;
        if (annotations == null) {
            return null;
        }

        // amout of all annotations in this article
        int size = annotations.size();

        Annotation annotation;

        // go over all annotaions of this article and filter out annotations
        // related to this position
        //int rangeleft = position, rangeright = position;
        for (int i = 0; i < size; i++) {

            annotation = annotations.get(i);
            if (annotation == null) {
                continue;
            }
            if (!annotation.visible) {
                continue;
            }

            if (annotation.spanset == null) {
                continue;
            }
            if (annotation.spanset.isEmpty()) {
                continue;
            }

            for (int t = 0; t < annotation.spanset.size(); t++) {
                SpanDef span = annotation.spanset.getSpanAt(t);
                if (span == null) {
                    continue;
                }

                int start = span.start;
                int end = span.end;

                // found annotation whose span cover this position
                if ((start <= position) && (end >= position)) {
                    // save it
                    saveSelectedAnnotation(i);
                    continue;
                    //rangeleft = ( start < rangeleft ? start : rangeleft);
                    //rangeright = ( end > rangeright ? end : rangeright);
                }
            }
        }

        /*
         * // get other annotations whose span get overlapped with selected
         * annotations for(int i=0; i<size; i++) {
         *
         * annotation = annotations.get(i); if( annotation == null ) continue;
         * if(!annotation.visible) continue; int start = annotation.spanstart;
         * int end = annotation.spanend; * // found annotation whose span cover
         * this position if(( start >= rangeleft )&&( start <= rangeright )&&(
         * end > rangeright ) ) { // save it saveSelectedAnnotation( i ); } else
         * if(( end >= rangeleft )&&( end <= rangeright )&&( start < rangeleft )
         * ) { // save it saveSelectedAnnotation( i ); } }
         */

        // for verifier
        //for(Annotation thisannotation : annotations) {
            /*
         * if(Indices == null || Indices.contains(i)){ eAnnotationNode ea =
         * EAs.get(j); String mentionId = ea.mention_id.trim(); int start =
         * Integer.valueOf( ea.span_start ); int end = Integer.valueOf(
         * ea.span_end ); if((_position>=start)&&(_position<=end)){ for(int k=0;
         * k<size_ECMs ; k++ ){ eClassMention ecm = ECMs.get(k); String
         * classMentionId = ecm.classMentionID.trim();
         *
         * QueryReturns.add(qr);
         *
         * }
         * }
         * }
         *
         */
        //}
        /*
         * // 3. to each annotation, get their classname for(int j =0 ;
         * j<size_EAs;j++){ if(Indices == null || Indices.contains(j)){
         * eAnnotationNode ea = EAs.get(j); String mentionId =
         * ea.mention_id.trim(); int start = Integer.valueOf( ea.span_start );
         * int end = Integer.valueOf( ea.span_end );
         * if((_position>=start)&&(_position<=end)){ for(int k=0; k<size_ECMs ;
         * k++ ){ eClassMention ecm = ECMs.get(k); String classMentionId =
         * ecm.classMentionID.trim();
         *
         * QueryReturns.add(qr);
         *
         * }
         * }
         * }
         *
         * }
         * }
         * }
         */
        Vector<Integer> result = selectedAnnotations;
        if (!save) {
            selectedAnnotations = oldSelected;
        }

        return result;
    }

    /**
     * Return the index of selected Annotations in a vector structure. To a
     * given position in an article, search and return annotations it selected.
     */
    public Vector<Integer> getSelectedAnnotationIndexes() {
        return selectedAnnotations;
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

        Article article = Depot.getArticleByFilename(filename);
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
     * Return the number of articles in the static vector in class of Depot.
     *
     * @return The number of articles.
     */
    public static int getSize() {
        if (depot == null) {
            return 0;
        } else {
            return depot.size();
        }
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

            Article article = Depot.getArticleByFilename(filename.trim());
            if ((article == null) || (article.annotations == null)) {
                return 0;
            }

            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.uniqueIndex == _UID) {
                    if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                        article.annotations.get(i).adjudicationStatus = Annotation.AdjudicationStatus.NONMATCHES_DLETED;
                    } else {
                        WorkSet.addLastDeleted(article.filename, article.annotations.get(i));
                        article.annotations.removeElementAt(i);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }

        return 1;
    }

    /**
     * set the adjudication_alias for an annotation's adjudication.
     */
    public static void setAliasName(String filename, int uniqueindex) {
        try {
            if (filename == null) {
                return;
            }
            if (filename.trim().length() < 1) {
                return;
            }
            
            Article article = null;
            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
                article = adjudication.data.AdjudicationDepot.getArticleByFilename(filename.trim());
            else
                article = Depot.getArticleByFilename(filename.trim());
            
            if ((article == null) || (article.annotations == null)) {
                return;
            }
           
            String alias = null;
            
            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.uniqueIndex == uniqueindex) {
                    alias = annotation.adjudicationAlias;
                    break;
                }
            }
            
            if( alias != null ){
                
                for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                
                if( annotation.adjudicationAlias == null )
                    continue;
                
                if (annotation.adjudicationAlias.compareTo(alias) == 0 ) {
                    annotation.adjudicationAlias = String.valueOf( uniqueindex );                    
                }
            }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return;
    }
    
    /**
     * set an annotation's adjudication status to "matches_OK".
     */
    public static void setAnnotationToMatchedOK_byUID(String filename, int uniqueindex) {
        try {            
            
            if (filename == null) {
                return;
            }
            if (filename.trim().length() < 1) {
                return;
            }
            
            Article article = null;
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                article = Depot.getArticleByFilename(filename.trim());
            else {
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                article = depotOfAdj.getArticleByFilename(filename.trim());
            }
                
            if ((article == null) || (article.annotations == null)) {
                return;
            }

            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.uniqueIndex == uniqueindex) {
                    article.annotations.get(i).adjudicationStatus = Annotation.AdjudicationStatus.MATCHES_OK;
                    if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
                        article.annotations.get(i).setAnnotator("ADJUDICATION");
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return;
    }

    /**
     * In adjudication mode, delete a annotation from depot is to hide this
     * annotation by set its "adjudication status" to nobody
     */
    public static int deleteAnnotation_byUID_onAdjudicationMode(String filename, int uniqueindex) {
        int count = 0;

        try {
            if (filename == null) {
                return 0;
            }
            if (filename.trim().length() < 1) {
                return 0;
            }

            Article article = adjudication.data.AdjudicationDepot.getArticleByFilename(filename.trim());
            if ((article == null) || (article.annotations == null)) {
                return 0;
            }

            for (int i = article.annotations.size() - 1; i >= 0 ; i--) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.uniqueIndex == uniqueindex) {
                    //WorkSet.addLastDeleted( article.filename, article.annotations.get(i) );
                    //article.annotations.get(i).adjudicationStatus = Annotation.AdjudicationStatus.NONMATCHES_DLETED;
                    article.annotations.removeElementAt(i);
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

    /**
     * change span for span editor in result editor
     *
     * @param type headextendtoLeft = 1, tailextendtoRight = 2,
     * headShortentoRight = 3, tailShortentoLeft =4;
     */
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
                article = Depot.getArticleByFilename(filename.trim());
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
                        }
                    }
                }
            }




        } catch (Exception e) {
            System.out.println(e.toString());
            return 0;
        }
        return 1;

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

        

        String mentionid = "eHOST_" + newMentionID();
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

        

        String mentionid = "eHOST_" + newMentionID();
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
     * maximum number of all imported Mention ID
     */
    private static int maxImportedMentionID;

    /**
     * get a new mention id for storage of new annotation.
     */
    public static int newMentionID() {
        maxImportedMentionID++;
        return maxImportedMentionID - 1;
    }

    /**
     * add a new annotation to current article for verifier
     */
    public void addANewAnnotation(String textSourcefilename, Annotation annotation) {
        Article article = this.getArticleByFilename(textSourcefilename);
        if (article == null) {
            return;
        }

        annotation.mentionid = annotation.mentionid + newMentionID();

        article.annotations.add(annotation);

    }

    public ArrayList<Annotation> getAllAnnotations() {
        ArrayList<Annotation> toReturn = new ArrayList<Annotation>();
        for (Article article : depot) {
            toReturn.addAll(article.annotations);
        }
        return toReturn;
    }

    public ArrayList<Article> getAllArticles() {
        ArrayList<Article> toReturn = new ArrayList<Article>();
        toReturn.addAll(depot);
        return toReturn;
    }

    public void setAnnotationVisible(Vector<String> forbiddenClassnames) {
        if (depot == null) {
            return;
        }
        for (Article article : depot) {
            if (article == null) {
                continue;
            }
            if (article.annotations == null) {
                continue;
            }
            for (Annotation annotation : article.annotations) {
                if (annotation.annotationclass == null) {
                    annotation.setVisible(true);
                } else {
                    
                    boolean allow = true;
                    
                    
                                        
                    for (String forbiddenClass : forbiddenClassnames) {
                        if (forbiddenClass.trim().compareTo(annotation.annotationclass.trim()) == 0) {
                            allow = false;
                            break;
                        }
                    }
                    
                    if( allow != false )
                        if( annotation.attributes != null ){
                            allow = !DepotOfAttributes.filterByAttribute(annotation);                            
                        }
                    
                    annotation.setVisible(allow);
                }
            }
        }
    }

    public void setAnnotationsVisible(Collection<Annotation> visible, Article article) {
        if (depot == null) {
            return;
        }

        for (Annotation annotation : article.annotations) {

            boolean allow = false;
            for (Annotation toSee : visible) {
                if (toSee.equals(annotation)) {
                    allow = true;
                    break;
                }
            }
            annotation.setVisible(allow);

        }

    }

    public Vector<Integer> getUniqueIndexByAnnotation(Vector<Annotation> annotations) {
        Vector<Integer> toReturn = new Vector<Integer>();
        for (Article article : this.getAllArticles()) {
            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation reference = article.annotations.get(i);
                for (Annotation annotation : annotations) {
                    if (reference.equals(annotation)) {
                        toReturn.add(reference.uniqueIndex);
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * If you want to show only one annotation in the list of selected
     * annotations, clear the vector and add the index of the annotation you
     * want to display.
     *
     * @param index The index of the annotation in vector of annotations in its
     * related article.
     */
    public void setSelectedAnnotationsIndexes(int index) {
        selectedAnnotations.clear();
        selectedAnnotations.add(index);
    }

    public Vector<Integer> getSelectedAnnotationIndices() {
        return selectedAnnotations;
    }

    /**
     * Get the text surrounding the annotation. Just one sentence(from period to
     * period).
     *
     * @param annotation
     *
     * @return
     */
    public String getContext(Annotation annotation) {
        //Make sure the working article contains this annotation
        Article working = this.getArticleByFilename(WorkSet.getCurrentFile().getName());
        if (!working.annotations.contains(annotation)) {
            return "";
        }
        if (annotation.spanset == null) {
            return "";
        }
        if (annotation.spanset.size() < 1) {
            return "";
        }
        if (annotation.spanset.getSpanAt(0) == null) {
            return "";
        }

        //Read in the current file.
        File f = WorkSet.getCurrentFile();
        String theText = " ";
        try {
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                theText += s.nextLine() + "\n";
            }
        } catch (IOException e) {
        }

        //Calculate start and end of the sentence
        int startIndex = Math.max(theText.lastIndexOf('.', annotation.spanset.getSpanAt(0).start) + 1, 0);
        int startDifference = annotation.spanset.getSpanAt(0).start - startIndex;
        int endIndex = Math.min(theText.length() - 1, theText.indexOf('.', annotation.spanset.getSpanAt(0).end) + 1);
        if (endIndex < 0) {
            endIndex = theText.length() - 1;
        }
        int endDifference = endIndex - annotation.spanset.getSpanAt(0).end;
        String temp = theText.substring(startIndex, endIndex);
        Scanner s = new Scanner(temp);
        String toReturn = "<html>";
        int charsAdded = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (i == startDifference) {
                toReturn += "<Font color=\"red\">";
            }
            char toAdd = temp.charAt(i);
            toReturn += toAdd;

            charsAdded += 1;
            if (charsAdded > 60 && toAdd == ' ') {
                toReturn += "<br>";
                charsAdded = 0;
            }
            if (i == temp.length() - endDifference) {
                toReturn += "</Font>";
            }
        }
        toReturn += "</html>";
        return toReturn;
    }

    public void deleteAnnotation(Annotation annotation) {
        for (Article article : depot) {
            for (int i = 0; i < article.annotations.size(); i++) {
                Annotation possibleDeletion = article.annotations.get(i);
                if (possibleDeletion == annotation) {
                    
                    // remove this annotation
                    article.annotations.remove(i);    
                    
                    // remove all relationships that linked to this annotations
                    for( Annotation rel_annotation : article.annotations  ){
                        if( rel_annotation == null )
                            continue;
                        
                        if( rel_annotation.relationships == null )
                            continue;
                        
                        for( int j = rel_annotation.relationships.size() - 1; j>=0; j-- ){
                            AnnotationRelationship ar = rel_annotation.relationships.get(j);
                            if( ar == null )
                                continue;
                            
                            if( ar.linkedAnnotations == null ){
                                rel_annotation.relationships.removeElementAt(j);
                                continue;
                            }
                            
                            Vector<AnnotationRelationshipDef> complex = ar.linkedAnnotations;
                            for( int k=ar.linkedAnnotations.size()-1; k>=0; k-- ){
                                AnnotationRelationshipDef e = ar.linkedAnnotations.get(k);
                                if(e==null)
                                    continue;
                                if(e.linkedAnnotationIndex == annotation.uniqueIndex && annotation.uniqueIndex > 0 ){
                                    rel_annotation.relationships.removeElementAt(j);
                                    continue;
                                }
                            }
                        }
                            
                    }
                    
                    return;
                    
                }

            }
        }
    }

    public void deleteAnnotation_belongToClass(String classname) {
        if (classname == null) {
            return;
        }
        if (classname.trim().length() < 1) {
            return;
        }

        if (depot == null) {
            return;
        }

        for (Article article : depot) {
            if (article == null) {
                continue;
            }
            if (article.annotations == null) {
                continue;
            }

            int size_annotations = article.annotations.size();
            for (int i = size_annotations - 1; i >= 0; i--) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.annotationclass == null) {
                    continue;
                }
                if (annotation.annotationclass.trim().length() < 1) {
                    continue;
                }
                try {
                    if (annotation.annotationclass.trim().compareTo(classname.trim()) == 0) {
                        article = removeRel( article, article.annotations.get(i) );
                        article.annotations.removeElementAt(i);
                    }
                } catch (Exception e) {
                    log.LoggingToFile.log(Level.SEVERE, " 1008121710 - i=" + i + "; of " + size_annotations);
                }
            }
        }
    }
    
    private Article removeRel(Article article, Annotation annotation){
        try{
            int linkedindex = annotation.uniqueIndex;
            if(article.annotations==null)
                return article;
            for( int i=(article.annotations.size()-1); i>=0; i-- ){
                Annotation otherannotation = article.annotations.get(i);
                if(otherannotation == null)
                    continue;
                if(otherannotation.relationships==null)
                    continue;
                Vector<AnnotationRelationship> rels = otherannotation.relationships;
                if( ( rels == null ) || (rels.size() < 1))
                    continue;
                
                
                for(int j=rels.size()-1; j>=0;j--){
                    
                    AnnotationRelationship rel = rels.get(j);
                    
                    // this relationship can't be empty,
                    // otherwise we delete to remove the trash
                    if(rel==null){
                        rels.removeElementAt(j);
                        continue;
                    }
                    
                    // relationship must be linked to someone, otherwise we delete
                    // to remove the trash
                    if(rel.linkedAnnotations==null){
                        rels.removeElementAt(j);
                        continue;
                    }
                    
                    AnnotationRelationshipDef reldef = rel.linkedAnnotations.get(0);
                    if(reldef==null){
                        rels.removeElementAt(j);
                        continue;
                    }
                    
                    if( reldef.linkedAnnotationIndex == linkedindex ) {
                        rels.removeElementAt(j);                        
                        break;
                    }
                    
                }
                
            }
            
        }catch(Exception ex){
            System.out.print("fail to delete related relationship while doing a mass deleting on ");
        }
        
        return article;
    }

    /**
     * delete all annotations in all documents if their text are same kind of
     * text we designated.
     */
    public int deleteAnnotations_byUniqueText(String filename, String annotationText) {
        int deletecount = 0;

        if (annotationText == null) {
            return -1;
        }
        if (annotationText.trim().length() < 1) {
            return -1;
        }

        for (Article article : depot) {
            if (article == null) {
                continue;
            }
            if (filename != null) {
                if (!article.filename.equals(filename)) {
                    continue;
                }
            }

            if (article.annotations == null) {
                continue;
            }

            int size = article.annotations.size();
            for (int i = size - 1; i >= 0; i--) {
                Annotation annotation = article.annotations.get(i);
                if (annotation == null) {
                    continue;
                }
                if (annotation.annotationText.equals(annotationText)) {
                    article.annotations.remove(i);
                    deletecount++;
                }

            }

        }

        return deletecount;
    }

    /**
     * annotation repetitive check
     *
     * @return 0 means normal error happened;
     *
     * @return -1 can not found related article
     *
     * @return 1 no repetitive, and there is no any annotations in current
     * article
     */
    public int repetitiveCheck(String articlename, final String termtext, final String classname,
            final int spanstart, final int spanend) {

        int returnflag = 0; // 0: default flag to return

        try {
            SpanSetDef spanset = new SpanSetDef();
            spanset.addSpan(spanstart, spanend);

            Article article = this.getArticleByFilename(articlename.trim());

            if (article == null) {
                return 1;  // no repetitive, if this article did not existed
            }
            Vector<Annotation> annotations = article.annotations;
            if ((annotations == null) || (annotations.size() < 1)) {
                return 2;  // no repetitive, article is empty
            }
            for (Annotation annotation : annotations) {
                if ((annotation.spanset.isDuplicates(spanset))) {
                    if (annotation.annotationclass.equals(classname)) {
                        return -1;
                    }
                }

            }

            returnflag = 0;

        } catch (Exception ex) {
        }

        return returnflag;
    }

    /**
     * apply designated annotator information to all existing annotations
     */
    public void changeAnnotator_toAll(String annotatorname, String annotatorid) {
        try {
            if ((annotatorname == null) || (annotatorid == null)) {
                return;
            }
            if ((annotatorname.trim().length() < 1) || (annotatorid.trim().length() < 1l)) {
                return;
            }

            if (depot == null) {
                return;
            }

            for (Article article : depot) {
                if (article == null) {
                    continue;
                }
                for (Annotation annotation : article.annotations) {
                    if (annotation == null) {
                        continue;
                    }
                    annotation.setAnnotator( annotatorname.trim() );
                    annotation.annotatorid = annotatorid.trim();
                }
            }

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1010012311:: error while try to apply "
                    + "an annotator to all existing annotations in memory!!!");
        }
    }

    /**
     * apply designated annotator information to all related annotations.
     */
    public void changeAnnotator_toRelated(String annotatorname, String annotatorid, String oldAnnotatorName, String oldAnnotatorId) {
        try {
            if ((oldAnnotatorName == null) || (oldAnnotatorId == null)) {
                return;
            }
            if ((oldAnnotatorName.trim().length() < 1) || (oldAnnotatorId.trim().length() < 1l)) {
                return;
            }
            if ((annotatorname == null) || (annotatorid == null)) {
                return;
            }
            if ((annotatorname.trim().length() < 1) || (annotatorid.trim().length() < 1l)) {
                return;
            }

            if (depot == null) {
                return;
            }

            for (Article article : depot) {
                if (article == null) {
                    continue;
                }
                for (Annotation annotation : article.annotations) {
                    if (annotation == null) {
                        continue;
                    }
                    if ((!annotation.getAnnotator().equals(oldAnnotatorName.trim()))) {
                        continue;
                    }
                    annotation.setAnnotator( annotatorname.trim() );
                    annotation.annotatorid = annotatorid.trim();
                }
            }

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1010012311:: error while try to apply "
                    + "an annotator to all existing annotations in memory!!!");
        }
    }

    public void checkGhosts(File doc) {
        if (doc == null) {
            return;
        }
        if ((!doc.exists()) || (doc.isDirectory())) {
            return;
        }

        try {
            long doclength = doc.length() - 1;

            // go over all article to find article for current document
            for (Article article : depot) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                // if found information for current document
                if (isSameDoc(doc, article.filename)) {
                    if ((article.annotations == null) || (article.annotations.size() < 1)) {
                        return;
                    }

                    for (Annotation ann : article.annotations) {
                        if (ann == null) {
                            continue;
                        }

                        if (env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_classless) {
                            // found ghost annotation: spanless
                            if ((ann.annotationclass == null) || (ann.annotationclass.trim().length() < 1)) {
                                Depot_DuplicateAndGhosts.addClassless(doc.getName(), ann);
                            }
                        }

                        // found ghost annotation: out of range
                        if (env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_classless) {
                            boolean isGhost = false;
                            if (ann.spanset == null) {
                                isGhost = true;
                            } else if (ann.spanset.isEmpty()) {
                                isGhost = true;
                            } else if (ann.spanset.size_nonNull() < 1) {
                                isGhost = true;
                            }

                            if (isGhost) {
                                Depot_DuplicateAndGhosts.addOutOfRange(doc.getName(), ann);
                            }
                        }

                        // found ghost annotation: out of range
                        if (env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_spanless) {
                            if ((ann.spanset != null) && (ann.spanset.size() > 1)) {
                                int size = ann.spanset.size();
                                for (int t = 0; t < size; t++) {
                                    SpanDef span = ann.spanset.getSpanAt(t);
                                    if (span == null) {
                                        continue;
                                    }
                                    long start = span.start, end = span.end;
                                    if ((start == end) || (ann.annotationText == null) || (ann.annotationText.trim().length() < 1)) {
                                        Depot_DuplicateAndGhosts.addSpanless(doc.getName(), ann);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println();
        }
    }

    /**
     * This method only can be called by class "check and list duplicates.jar",
     * It can help you find and list out all annotations duplicates.
     *
     * These found duplicate annotations will be kept in vector for subsequent
     * usage, such for eHOST to delete these duplicates, etc.
     */
    public Vector<DupilcateAndGhost> searchDuplicates(File doc) {
        // start with an empty return
        Depot_DuplicateAndGhosts.clear();

        try {
            if ((doc == null) || (!doc.exists())) {
                return null;
            }

            // go over all article to find article for current document
            for (Article article : depot) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                // if found information for current document
                if (isSameDoc(doc, article.filename)) {
                    // go over all annotations
                    int size = article.annotations.size();
                    for (int i = 0; i < size; i++) {
                        // get this annotation
                        Annotation ann1 = article.annotations.get(i);
                        if (ann1 == null) {
                            continue;
                        }

                        // go to next one if this one already recorded
                        if (Depot_DuplicateAndGhosts.isAnnotationInList_asReference(ann1, article.filename)) {
                            continue;
                        }

                        // compare with others
                        for (int j = 0; j < size; j++) {
                            if (i == j) {
                                continue;
                            }

                            Annotation ann2 = article.annotations.get(j);
                            if (ann2 == null) {
                                continue;
                            }

                            /*
                             * if((ann1.spanstart==ann2.spanstart)&&(ann1.spanend==ann2.spanend))
                             * System.out.println("- 发现重复[" +ann1.annotationText
                             * + "] at ("+ann1.uniqueIndex + ") @(" + " (" +
                             * ann1.spanstart + ", " + ann1.spanend + ")" +", 和
                             * [" + ann2.annotationText + "(" +
                             * ann2.spanstart+","+ann2.spanend+")]" );
                             */
                            // remove it if these two annotations are exactly same
                            if (ann1.isDuplicate(ann2, article.filename)) {
                                Depot_DuplicateAndGhosts.addDuplicates(article.filename, ann1, ann2);
                                /*
                                 * System.out.println("发现重复["
                                 * +ann1.annotationText + "]::"+ann1.uniqueIndex
                                 * +", 和 [" + ann2.annotationText + "(" +
                                 * ann2.spanstart+","+")]" );
                                 *
                                 */
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            log.LoggingToFile.log(Level.SEVERE, "ERROR 1105231556::" + e.getLocalizedMessage());
        }

        if ((env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_classless)
                || (env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_spanless)
                || (env.Parameters.DuplicatesAndGhostSeeker.ghostannotation_outofrange)) {
            checkGhosts(doc);
        }

        return Depot_DuplicateAndGhosts.get();
    }

    /**
     * whether is these two files are same one.
     */
    private boolean isSameDoc(File _docfile, String _articlefilename) {
        if ((_docfile == null) || (!_docfile.exists()) || (_articlefilename == null) || (_articlefilename.trim().length() < 1)) {
            return false;
        }

        try {
            String articlefilename = _articlefilename.trim();
            String filename = _docfile.getName();

            if (articlefilename.trim().compareTo(filename.trim()) == 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }
    
    public void recordAnnotation(String filename, String mentionid, String annotationText,
            int spanstart, int spanend, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            int uniqueindex) {

        recordAnnotation( filename,  mentionid,  annotationText,
             spanstart,  spanend,  annotationClass,
             annotator,  annotatorid,
             creationdate,  comment, verifierSuggestion,
             normalRelationships,
             complexRelationships,
             uniqueindex );

    }

    public void recordAnnotation(String filename, String mentionid, String annotationText,
            int spanstart, int spanend, String annotationClass,
            String annotator, String annotatorid,
            String creationdate, String comment, Vector<String> verifierSuggestion,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            int uniqueindex, boolean isVerified) {

        SpanSetDef spanset = new SpanSetDef();
        spanset.addSpan(spanstart, spanend);

        recordAnnotation(
                filename,
                mentionid,
                annotationText,
                spanset,
                annotationClass,
                annotator,
                annotatorid,
                creationdate,
                comment,
                verifierSuggestion,
                normalRelationships,
                complexRelationships,
                uniqueindex, isVerified);



    }

    /**
     * On adjudicate mode, if there is only one annotation and it isn't an
     * agreement annotation, we only show the buttons of "accept" and "reject"
     * for user to do decision. At that time, we didn't need to show the whole
     * part of the comparator, we only need put some data to here.
     *
     * @return the error message in format of string; Or just null if there is
     * no any error.
     */
    public static String setSingleAnnotation(Annotation annotation) {

        if (annotation == null) {
            return "ERROR 1203021131:: annotation is null!";
        }

        SelectedAnnotationSet.sets.clear();
        SelectedAnnotationSet.sets.add(annotation.uniqueIndex);

        SelectedAnnotationSet.uniqueIndex_of_annotationOnEditor = annotation.uniqueIndex;

        return null;
    }

    
    /**check and set the default attributes to a new annotation*/
    public void setAttributeDefault(String testsourceFilename, int newuniqueindex) {
        
        // get the annotation
        Annotation annotation = getAnnotationByUnique(testsourceFilename, newuniqueindex);
        if( annotation == null )
            return;
        
        // get the classname
        String classname =annotation.annotationclass;
        if( classname == null )
            return;
    
        // get the class
        resultEditor.annotationClasses.Depot classDepot = new resultEditor.annotationClasses.Depot();
        AnnotationClass classDef = classDepot.getAnnotatedClass(classname);
        
        // Dealing with Private attributes of this class
        Vector<AttributeSchemaDef> atts = classDef.privateAttributes;
        if( atts !=null ){
            for(AttributeSchemaDef attdef : atts ){
                if( attdef.hasDefaultValue() ){
                    if(annotation.attributes == null)
                        annotation.attributes = new Vector<AnnotationAttributeDef>();
                    //annotation.attributes.add( new AnnotationAttributeDef( attdef.getName(), attdef.getDefault()));
                    annotation = setAttribute(annotation, attdef.getName(), attdef.getDefault() );                    
                }
            }
        }
        
        // if have, dealing with public attributes of this class
        if (classDef.inheritsPublicAttributes) {
            if (env.Parameters.AttributeSchemas != null) {
                Vector<AttributeSchemaDef> public_atts = env.Parameters.AttributeSchemas.getAttributes();
                if (atts != null) {
                    for (AttributeSchemaDef attdef : public_atts) {
                        if(attdef==null)
                            continue;
                        if (attdef.hasDefaultValue()) {
                            if((attdef.getName()!=null)&&(attdef.getDefault()!=null)){
                                //AnnotationAttributeDef attitem = new AnnotationAttributeDef(attdef.getName(), attdef.getDefault());
                                if(annotation.attributes == null)
                                    annotation.attributes = new Vector<AnnotationAttributeDef>();
                                //annotation.attributes.add( attitem );
                                annotation = setAttribute(annotation, attdef.getName(), attdef.getDefault() );                    
                            }
                        }
                    }
                }

            }
        }
    }
    
    
    /**Set the default attribute value if needed after changing the class of 
     * current annotation.
     */
    public Annotation setDefaultAttValue(Annotation currentAnnotation, String classname ) {
        
        // validity checking
        if((classname==null)||(classname.trim().length()<1))
            return currentAnnotation;
        
        if(currentAnnotation == null)
            return currentAnnotation;
        
        // get the class
        resultEditor.annotationClasses.Depot classDepot = new resultEditor.annotationClasses.Depot();
        AnnotationClass classDef = classDepot.getAnnotatedClass(classname);
        if( classDef == null )
            return currentAnnotation;
        
        // make sure att list of this annotation is not NULL
        if(currentAnnotation.attributes == null)
            currentAnnotation.attributes = new Vector<AnnotationAttributeDef>();
        
        // Dealing with Private attributes of this class
        Vector<AttributeSchemaDef> atts = classDef.privateAttributes;  // get 
        if( atts !=null ){
            for(AttributeSchemaDef attdef : atts ){
                // to attribute who has default value of this class in schema 
                if( attdef.hasDefaultValue() ){
                    currentAnnotation = setAttribute(currentAnnotation, attdef.getName(), attdef.getDefault() );                    
                }
            }
        }
        
        // if have, dealing with public attributes of this class
        if (classDef.inheritsPublicAttributes) {
            if (env.Parameters.AttributeSchemas != null) {
                Vector<AttributeSchemaDef> public_atts = env.Parameters.AttributeSchemas.getAttributes();
                if (atts != null) {
                    for (AttributeSchemaDef attdef : public_atts) {
                        if(attdef==null)
                            continue;
                        if (attdef.hasDefaultValue()) {
                            if((attdef.getName()!=null)&&(attdef.getDefault()!=null)){                                                            
                                currentAnnotation = setAttribute(currentAnnotation, attdef.getName(), attdef.getDefault() );                    
                            }
                        }
                    }
                }

            }
        }
        
        return currentAnnotation;
    }
    
    /**To assign an attribute with a given value to the annotation, this will 
     * add a new attribute, or just change the value if this attribute is 
     * already there.
     * 
     * @param   annotation
     *          The annotation that we want to add an attribute.
     * 
     * @param   defaultValue
     *          The value that we want to add.
     * 
     * @return  The annotation with the given attribute.
     */
    private Annotation setAttribute(Annotation annotation, String attname, String defaultValue ){
        // make sure att list of this annotation is not NULL
        if(annotation.attributes == null)
            annotation.attributes = new Vector<AnnotationAttributeDef>();
        
        // If this annotation already has some attribtue, we need to check to 
        // avoid adding same attribtues.
        if( annotation.attributes.size() > 0 ){
            
            boolean found = false;  // flag: this attribute is already added(true) or not(false)
            
            for( int i = 0;  i < annotation.attributes.size(); i++ ){
                // get each existing attribute of the annotation
                AnnotationAttributeDef att = annotation.attributes.get(i);
                if( att == null )
                    continue;
                
                if( att.name == null )
                    continue;
                
                if( att.name.trim().compareTo( attname.trim() ) == 0 )
                {
                    annotation.attributes.set( i, new AnnotationAttributeDef( attname, defaultValue) );
                    found = true;
                }
                
            }
            
            if( !found ){
                annotation.attributes.add( new AnnotationAttributeDef( attname, defaultValue));
            }
        }
        // if this annotation doesn't have any attribtue until now, we can 
        // add the new one directly without considering other attribtues.
        else{
            annotation.attributes.add( new AnnotationAttributeDef( attname, defaultValue));
        }
        
        return annotation;
    }

    /**
     * ***********************************************************************
     * A static class to integrate all operation for selecting annotations from
     * a given position, usually this position is where you mouse just clicked
     * on.
     * ***********************************************************************
     */
    public static class SelectedAnnotationSet {

        /**
         * Stored recently selected annotations. Such as annotations whose spans
         * overlapped on a given position, usually this given position is came
         * from the carpet postion where you mouse just clicked on.
         */
        protected static Vector<Integer> sets = new Vector<Integer>();
        /**
         * unique index of the object annotation showed in the editor panel;
         */
        public static int uniqueIndex_of_annotationOnEditor;

        public static void setSets(Vector<Integer> set) {
            sets = set;
        }

        /**
         * If you want to show some annotations in the list of selected
         * annotations, and all annotations have same annotation text. Clear the
         * vector and add the index of these annotations you want to display.
         *
         * @param filename file name of current article, pure filename w/o path.
         *
         * @param annotationText These annotations have same annotation text
         * content.
         */
        public static void selectedAnnotations_byTheirText(String filename, String annotationText) {

            sets.clear();

            if ((filename == null) || (annotationText == null)) {
                log.LoggingToFile.log(Level.SEVERE, "error 1101041438:: fail to find designated text source!");
                return;
            }
            if ((filename.trim().length() < 1) || (annotationText.trim().length() < 1)) {
                return;
            }
            
            Article article = null;
            if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                article = Depot.getArticleByFilename(filename.trim());
            else if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                article = depotOfAdj.getArticleByFilename(filename.trim());
            }
            if (article == null) {
                return;
            }

            if (article.annotations == null) {
                return;
            }

            int size = article.annotations.size();

            for (int i = 0; i < size; i++) {
                Annotation annotation = article.annotations.get(i);

                if (annotation == null) {
                    continue;
                }

                if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                    if ((annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                            && (annotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                        continue;
                    }
                }

                if (annotation.annotationText == null) {
                    continue;
                }

                if (annotation.annotationText.trim().compareTo(annotationText.trim()) == 0) {
                    sets.add(annotation.uniqueIndex);
                }
            }

        }

        /**
         * return amount of selected annotations in "sets"
         *
         * @return amount of all selected annotations
         */
        public static int size() {
            if (sets == null) {
                return 0;
            }

            int size = sets.size();

            return size;
        }

        /**
         * Return unique indexes of latest selected annotations in Vector of
         * "integer";
         */
        public static Vector<Integer> getSelectedAnnotationSet() {
            return sets;
        }

        
        public static void removeAnnotationUniqueIndex(final int uniqueindex ) {
            removeAnnotationUniqueIndex( uniqueindex, false );
        }
        /**
         * To an given unique index number, delete the annotation which has same
         * unique index# in current set; just after this annotation got deleted,
         * we also will check it's relationships and deleted any relationship
         * who is linking to current annotation.
         *
         * @param uniqueindex the index of the annotation which need to be
         * removed from list of selected annotations.
         * 
         * @param   isMirrorDepot
         *          While isMirrorDepot is true, we go to the adjudication depot
         *          to delete the annotation, not in the annotation depot of normal
         *          annotation.
         */
        public static void removeAnnotationUniqueIndex(final int uniqueindex, final boolean isMirrorDepot) {
            try {


                if ((sets == null) || (sets.size() < 1)) {
                    return;
                }
                int size = sets.size();
                for (int i = 0; i < size; i++) {
                    if (sets.get(i) == null) {
                        continue;
                    }
                    if (sets.get(i) == uniqueindex) {
                        sets.removeElementAt(i);
                        break;
                    }
                }

                //new Thread(){
                //    @Override
                //    public void run()
                //    {

                Depot depot = new Depot();
                ArrayList<Article> articles;
                if(isMirrorDepot){
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    articles = depotOfAdj.getAllArticles();
                }else
                    articles = depot.getAllArticles();
                int size_article = articles.size();
                for (int i = size_article - 1; i >= 0; i--) {
                    Article article = articles.get(i);
                    if (article == null) {
                        continue;
                    }

                    if (article.annotations == null) {
                        continue;
                    }

                    int annotation_size = article.annotations.size();
                    for (int iu = annotation_size - 1; iu >= 0; iu--) {
                        Annotation annotation = article.annotations.get(iu);
                        if (annotation == null) {
                            continue;
                        }
                        if (annotation.relationships == null) {
                            continue;
                        }

                        Vector<resultEditor.annotations.AnnotationRelationship> relationships = annotation.relationships;
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

                                if ((linedAnnotation.linkedAnnotationIndex == uniqueindex)
                                        && (uniqueindex > 0)) {
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

                }
                //   }
                //}.start();


            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011152110:: failed to delete an"
                        + "unique index # from the selected annotation set.");
            }
        }

        /**
         * Select Annotations in designed document with a given positions,
         * usually this position is the carpet position you just clicked on the
         * text panel via your mouse.
         *
         * @param filename simple file name without path and suffix name
         *
         * @param position given position, integer. All annotation who span
         * covered this range should be selected.
         *
         * @return a vector contains unique indeces of all selected annotations.
         */
        public static ArrayList<Annotation> selectAnnotations_ByPosition(String filename, int position, boolean save) {
            // define return value
            ArrayList<Annotation> selectedAnnotationsUniqueID = new ArrayList<Annotation>();
            selectedAnnotationsUniqueID.clear();
            if ((filename == null) || (filename.trim().length() < 1)) {
                return null;
            }

//            System.out.println("One click:");
            try {
                Article article = null;
                if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
                    article = adjudication.data.AdjudicationDepot.getArticleByFilename(filename);
                else
                // ##1## get annotation set to this document
                    article = getArticleByFilename(filename);
                if ((article == null) || (article.annotations == null)) {
                    return null;
                }
                // ##2## get all annotations of this document
                Vector<Annotation> annotations = article.annotations;
                if (annotations == null) {
                    return null;
                }

                // amout of all annotations in this article
                int size = annotations.size();
                Annotation annotation;

                // ##3## go over all annotaions of this article and filter out
                // annotations related to this position directly;
                for (int i = 0; i < size; i++) {

                    annotation = annotations.get(i);
                    if (annotation == null) {
                        continue;
                    }
                    if (!annotation.visible) {
                        continue;
                    }

                    if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                        if ((annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                                && (annotation.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                            continue;
                        }
                    }
                    // found annotation whose span cover this position
                    if (annotation.spanset == null) {
                        continue;
                    }

                    int number_of_spans = annotation.spanset.size();
                    if (number_of_spans < 1) {
                        continue;
                    }

                    for (int n = 0; n < number_of_spans; n++) {
                        SpanDef span = annotation.spanset.getSpanAt(n);
                        if (span == null) {
                            continue;
                        }

                        if ((span.start <= position) && (span.end >= position)) {
                            selectedAnnotationsUniqueID.add(annotation);

//                            System.out.println("unique id = [" + annotation.uniqueIndex + "]");
                            break;
                        }
                    }



                }

                // ##4## try to find overspan annotaiton to these found annotations
                if (selectedAnnotationsUniqueID != null) {
                    int size2 = selectedAnnotationsUniqueID.size();
                    for (int j = 0; j < size2; j++) {
                        Annotation previous_selected_annotation = selectedAnnotationsUniqueID.get(j);
                        if (previous_selected_annotation == null) {
                            continue;
                        }
//                        int start = previous_selected_annotation.spanstart;
//                        int end = previous_selected_annotation.spanend;

                        // check overlap matching
                        for (Annotation ann : annotations) {
                            if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                                if ((ann.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                                        && (ann.adjudicationStatus != Annotation.AdjudicationStatus.NON_MATCHES)) {
                                    continue;
                                }
                            }
                            if (ann.spanset.isOverlapping(previous_selected_annotation.spanset)) {
                                selectedAnnotationsUniqueID.add(ann);
                                continue;
                            }

                        }
                    }
                }

                // ##5## remove repetitive items
                int amount = selectedAnnotationsUniqueID.size();
                for (int m = amount - 1; m >= 0; m--) {
                    Annotation ann = selectedAnnotationsUniqueID.get(m);
                    if (ann == null) {
                        continue;
                    }

                    for (int s = m - 1; s >= 0; s--) {
                        Annotation s_ann = selectedAnnotationsUniqueID.get(s);
                        if (s_ann == null) {
                            continue;
                        }
                        if (s_ann.uniqueIndex == ann.uniqueIndex) {
                            selectedAnnotationsUniqueID.remove(m);
                            break;
                        }

                    }
                }

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011151615:: fail to select annotations"
                        + " by a given cursor position.");
            }

            if (save) {
                // ##6## save unique indexes of selected annotations into a static
                // vector<Integer>
                try {
                    clearsets();
                    for (Annotation annot : selectedAnnotationsUniqueID) {
                        if ((annot == null) || (annot.uniqueIndex < 0)) {
                            continue;
                        }
                        saveSelectedAnnotations(annot.uniqueIndex);
                    }


                } catch (Exception ex) {
                    log.LoggingToFile.log(Level.SEVERE, "error 1011151717:: fail to save selected"
                            + " annotations by a given cursor position.");
                }
            }
//            System.out.println("selected annotations: ["
//                    + selectedAnnotationsUniqueID.size()
//                    + "] :: " + selectedAnnotationsUniqueID.toString() );
            // sent out return value
            return selectedAnnotationsUniqueID;
        }

        /**
         * just record ONE annotation into the selected annotation sets.
         *
         * @param uniqueindex unique index of a newly selected annotation.
         */
        public static void selectJustOneAnnotation(int uniqueindex) {
            clearsets();
            sets.add(uniqueindex);
        }

        /**
         * To annotations found by method "selectAnnotations_ByPosition", we
         * store them into static vector of "sets"; It's a vector of integer and
         * values in the vector is the unique index of our selected annotations.
         *
         * @param uniqueindex unique index of a newly selected annotation.
         */
        private static void saveSelectedAnnotations(int uniqueindex) {
            if (sets == null) {
                return;
            }

            // check exists or not
            int size = sets.size();
            boolean exists = false;
            for (int i = 0; i < size; i++) {
                if (sets.get(i) == uniqueindex) {
                    exists = true;
                    break;
                }
            }

            // save if it's not exist
            if (!exists) {
                sets.add(uniqueindex);
            }
        }

        private static void clearsets() {
            sets.clear();
        }

        /**
         * Find the annotation and return its uniqueindex by the sequence index
         * number which we got from the annotation list on comparator panel 
         * (==== Automatically switch data sources between annotation mode and 
         * adjudication mode ====).
         *
         * @return unique id of annotation you want to find -1 : fail others:
         * real unique index of annotation
         */
        public static int getAnnotation_bySequenceIndex_ofComparator(int sequenceindex) {
            if (sets == null) {
                return -1;
            }

            try {
                int size = sets.size();
                if ((sequenceindex < 0) || (sequenceindex > size - 1)) {
                    return -1;
                }

                int count = -1;
                for (Integer index : sets) {
                    if (index != uniqueIndex_of_annotationOnEditor) {
                        count++;
                    }

                    if (count == sequenceindex) {
                        return index;
                    }

                }

                return -1;

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011161602::fail to find annotation"
                        + " by given sequence index in list of annotations on "
                        + "comparator panel\n" + ex.toString());
                return -1;
            }

        }

        /**
         * Only keep primary annotation and delte others.
         */
        public static void data_onlyKeepPrimaryAnnotation() {

            // reset the pointer 
            DiffCounter.accept();            
            
            // get unique index of primary annotation
            int uniqueIndex_toPrimaryAnnotation = uniqueIndex_of_annotationOnEditor;
            if (uniqueIndex_toPrimaryAnnotation <= 0) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171245::fail to delete annotations"
                        + " while you try to accept some annotation in compare mode!!!");
                return;
            }

            // delete annotations in this set and depot, except the primary annotation
            try {


                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
                Dimension span = getSpanRange();
                if (span != null) {
                    resultEditor.Differences.IAADifference.Differences.remove(filename, span); // remove the relvant records of nonmatches
                }
                // keep the primary annotation, but set its adjudication status as "matches_OK"
                setAnnotationToMatchedOK_byUID(filename, uniqueIndex_toPrimaryAnnotation);
                
                setAliasName( filename, uniqueIndex_toPrimaryAnnotation );

                for (Integer uid : sets) {
                    if (uid != uniqueIndex_toPrimaryAnnotation) {
                        
                        Depot depot = new Depot();

                        if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                            Depot.deleteAnnotation_byUID_onAdjudicationMode(filename, uid);
                        } else {
                            Depot.deleteAnnotation_byUID(filename, uid);
                        }

                    }
                }

                int size = sets.size();
                for (int i = size - 1; i >= 0; i--) {
                    int uniqueindex = sets.get(i);
                    if (uniqueindex != uniqueIndex_toPrimaryAnnotation) {
                        // delete annotations from the "set"
                        sets.removeElementAt(i);
                    }
                }
                
                checkAnnotationRelationship(filename, uniqueIndex_toPrimaryAnnotation);

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171250::fail to delete "
                        + "annotations while you try to accept some annotation "
                        + "in compare mode!!!");
            }

        }
        
        public static void data_acceptAll() {

            // get unique index of primary annotation
            

            // delete annotations in this set and depot, except the primary annotation
            try {
                // reset the pointer 
                DiffCounter.accept();
                System.out.println("3");

                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();
                Dimension span = getSpanRange();
                if (span != null) {
                    resultEditor.Differences.IAADifference.Differences.remove(filename, span); // remove the relvant records of nonmatches
                }
                // keep the primary annotation, but set its adjudication status as "matches_OK"
                //setAnnotationToMatchedOK_byUID(filename, sets);
                
                
                //setAliasName( filename, uniqueIndex_toPrimaryAnnotation );

                for (Integer uid : sets) {
                    
                        Depot depot = new Depot();

                        if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                            
                            setAnnotationToMatchedOK_byUID(filename, uid);
                        } else {
                            Depot.deleteAnnotation_byUID(filename, uid);
                        }

                    
                }

                /*int size = sets.size();
                for (int i = size - 1; i >= 0; i--) {
                    int uniqueindex = sets.get(i);
                    if (uniqueindex != uniqueIndex_toPrimaryAnnotation) {
                        // delete annotations from the "set"
                        sets.removeElementAt(i);
                    }
                }*/

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171250::fail to delete "
                        + "annotations while you try to accept some annotation "
                        + "in compare mode!!!");
            }

        }

        /**
         * to a selected set, get the span range.
         */
        public static Dimension getSpanRange() {
            Dimension toReturn = new Dimension(-1, -1);

            // delete annotations in this set and depot, except the primary annotation
            try {
                if (sets == null) {
                    return null;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

                for (Integer uid : sets) {
                    Depot depot = new Depot();
                    Annotation annotation = depot.getAnnotationByUnique(filename, uid);
                    if (annotation == null) {
                        continue;
                    }

                    // init to record first span
                    if ((toReturn.height == -1) && (toReturn.width == -1)) {
                        toReturn.height = annotation.spanstart;
                        toReturn.width = annotation.spanend;
                        continue;
                    }

                    // deal with others
                    if ((toReturn.height > annotation.spanstart)) {
                        toReturn.height = annotation.spanstart;
                    }
                    if ((toReturn.width < annotation.spanend)) {
                        toReturn.width = annotation.spanend;
                    }
                }

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1110141250::fail "
                        + "to find span range to a selected \"set\";"
                        + "\n - RELATED ERROR: " + ex.getMessage());
            }

            return toReturn;
        }

        /**
         * Only keep a disignated annotation and delte others from the stored
         * selected, while user.
         */
        public static void data_onlyKeepAnnotation(int uniqueindex) {
            // delete annotations in this set and depot, except the primary annotation
            try {
                // reset the pointer 
                DiffCounter.accept();
            
                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

                Dimension span = getSpanRange();
                if (span != null) {
                    resultEditor.Differences.IAADifference.Differences.remove(filename, span); // remove the relvant records of nonmatches
                }
                // keep the primary annotation, but set its adjudication status as "matches_OK"
                setAnnotationToMatchedOK_byUID(filename, uniqueindex);

                setAliasName( filename, uniqueindex );

                for (Integer uid : sets) {
                    if (uid != uniqueindex) {
                        Depot depot = new Depot();
                        if (GUI.reviewmode == GUI.ReviewMode.adjudicationMode) {
                            Depot.deleteAnnotation_byUID_onAdjudicationMode(filename, uid);                            
                        } else {
                            Depot.deleteAnnotation_byUID(filename, uid);
                        }
                    }
                }

                int size = sets.size();
                for (int i = size - 1; i >= 0; i--) {
                    int unid = sets.get(i);
                    if (uniqueindex != unid) {
                        sets.removeElementAt(i);
                    }
                }
                
                checkAnnotationRelationship( filename, uniqueindex );

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171401::fail to delete "
                        + "annotations while you try to accept some annotation "
                        + "in compare mode!!!");
            }

        }

        /**
         * Sometimes, these is one or more annotation(s) appreared in the
         * annotations set which you selected for compare. But these annotations
         * do not belong this set. We click the "ignore" button in comparasion
         * mode to kick this/these annotation(s) out of our annotation set and
         * then we can continue our accept/reject operation to solve our
         * conflicts.
         */
        public static void data_onlyIgnorePrimaryAnnotation() {
            // get unique index of primary annotation
            int uniqueIndex_toPrimaryAnnotation = uniqueIndex_of_annotationOnEditor;
            if (uniqueIndex_toPrimaryAnnotation <= 0) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171316::fail to delete annotations"
                        + " while you try to accept some annotation in compare mode!!!");
                return;
            }

            // delete annotations in this set, except the primary annotation
            try {
                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

                int size = sets.size();
                for (int i = 0; i < size; i++) {
                    if (sets.get(i) == uniqueIndex_toPrimaryAnnotation) {
                        sets.removeElementAt(i);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011221333::fail to kick out "
                        + "annotations while you try to ignore some annotation "
                        + "on editor panel in compare mode!!!");
            }
        }

        /**
         * Only delete primary annotation and keep others as user clicked
         * "reject" button on editor panel while current mode is "annotation
         * comparation mode".
         */
        public static void data_onlyDeletePrimaryAnnotation() {

            // get unique index of primary annotation
            int uniqueIndex_toPrimaryAnnotation = uniqueIndex_of_annotationOnEditor;
            if (uniqueIndex_toPrimaryAnnotation <= 0) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171316::fail to delete annotations"
                        + " while you try to accept some annotation in compare mode!!!");
                return;
            }

            // delete annotations in this set, except the primary annotation
            try {
                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();


                //Depot depot = new Depot();
                if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE )
                    Depot.deleteAnnotation_byUID(filename, uniqueIndex_toPrimaryAnnotation);
                else{
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    depotOfAdj.deleteAnnotation_byUID(filename, uniqueIndex_toPrimaryAnnotation);
                }
                    

                int size = sets.size();
                for (int i = 0; i < size; i++) {
                    if (sets.get(i) == uniqueIndex_toPrimaryAnnotation) {
                        sets.removeElementAt(i);
                        break;
                    }
                }

                /*if (Depot.SelectedAnnotationSet.size() == 1) {
                    // Because we only have one now, se we need to consider this one
                    // as its adjudication status should be "matches_OK"
                    Depot.setAnnotationToMatchedOK_byUID(filename, Depot.SelectedAnnotationSet.sets.get(0));

                    setAliasName( filename, Depot.SelectedAnnotationSet.sets.get(0) );
                    
                    Dimension span = getSpanRange();
                    if (span != null) {
                        resultEditor.Differences.IAADifference.Differences.remove(filename, span); // remove the relvant records of nonmatches
                    }
                }*/


            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171317::fail to delete "
                        + "annotations while you try to accept some annotation "
                        + "in compare mode!!!");
            }



        }

        /**
         * Only delete the designated annotation and keep others as user clicked
         * "reject" button on comparator panel while current mode is "annotation
         * comparation mode".
         *
         * @param uniqueindex unique index of annotation which you want to
         * reject from selected annotation set.
         */
        public static void data_onlyDeleteSpcificAnnotation(int uniqueindex) {

            // delete annotations in this set, except the primary annotation
            try {
                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

                if( GUI.reviewmode == GUI.ReviewMode.ANNOTATION_MODE ){
                    Depot depot = new Depot();
                    depot.deleteAnnotation_byUID(filename, uniqueindex);
                }else{
                    adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                    depotOfAdj.deleteAnnotation_byUID(filename, uniqueindex);
                }

                int size = sets.size();
                for (int i = 0; i < size; i++) {
                    if (sets.get(i) == uniqueindex) {
                        sets.removeElementAt(i);
                        break;
                    }
                }

                /*if (Depot.SelectedAnnotationSet.size() == 1) {

                    Depot.setAnnotationToMatchedOK_byUID(filename, Depot.SelectedAnnotationSet.sets.get(0));

                    setAliasName( filename, Depot.SelectedAnnotationSet.sets.get(0) );
                    
                    Dimension span = getSpanRange();
                    if (span != null) {
                        resultEditor.Differences.IAADifference.Differences.remove(filename, span); // remove the relvant records of nonmatches
                    }
                }*/

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171421::fail to delete "
                        + "annotations while you try to reject some annotation "
                        + "in compare mode!!!");
            }

        }

        /**
         * Only kick out designated annotation from selected annotation set and
         * keep others as user clicked "ignore" button on comparator panel while
         * current mode is "annotation comparation mode". Notice: here we just
         * need to kick out this annotation from set but not to really delete it
         * from depot of annotations.
         *
         * @param uniqueindex unique index of annotation which you want to
         * ignore from selected annotation set.
         */
        public static void data_onlyIgnoreSpcificAnnotation(int uniqueindex) {

            // delete annotations in this set, except the primary annotation
            try {
                if (sets == null) {
                    return;
                }
                String filename = resultEditor.workSpace.WorkSet.getCurrentFile().getName().trim();

                int size = sets.size();
                for (int i = 0; i < size; i++) {
                    if (sets.get(i) == uniqueindex) {
                        sets.removeElementAt(i);
                        return;
                    }
                }
            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1011171421::fail to delete "
                        + "annotations while you try to reject some annotation "
                        + "in compare mode!!!");
            }

        }

        /**check and remove null relationship after "accept" an annotation. */
        private static void checkAnnotationRelationship(String filename, int uniqueIndex_toPrimaryAnnotation) {
            Article article = null; 
            if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ) {
                article = adjudication.data.AdjudicationDepot.getArticleByFilename(filename);
            } else {
                article = Depot.getArticleByFilename(filename);
            }
            
            if( article != null ){
                if( article.annotations != null ){
                    for(int i=article.annotations.size()-1; i>=0; i--){
                        Annotation annotation = article.annotations.get(i);
                        if( annotation == null )
                            continue;
                        if( annotation.relationships != null ){
                            for( int j=annotation.relationships.size()-1; j>=0; j-- ){
                                AnnotationRelationship ar = annotation.relationships.get(j);
                                if( ar == null )
                                    continue;
                                if( ( ar.linkedAnnotations == null ) // if vector<annotationRelationship> is null
                                        || ( ar.linkedAnnotations.get(0) == null) )// or first relationship is null
                                {
                                    annotation.relationships.removeElementAt(j);
                                    article.annotations.setElementAt(annotation, i);
                                    continue;
                                }
                                
                                AnnotationRelationshipDef arf = ar.linkedAnnotations.get(0);
                                if( arf == null ){
                                    annotation.relationships.removeElementAt(j);
                                    article.annotations.setElementAt(annotation, i);
                                    continue;
                                }
                                
                                int uniqueindex = arf.linkedAnnotationIndex;
                                
                                boolean found = false;
                                for(Annotation eachannotation : article.annotations ){
                                    if( eachannotation == null )
                                        continue;
                                    if( uniqueindex == eachannotation.uniqueIndex ){
                                        found = true;
                                        break;
                                    }
                                }
                                
                                if( !found ){
                                    annotation.relationships.removeElementAt(j);
                                    article.annotations.setElementAt(annotation, i);
                                    continue;
                                }                                                                                                            
                            }
                        }
                    }
                }
            }
            
        }
    }

    public final Vector<Article> getDepot() {
        return depot;
    }
}

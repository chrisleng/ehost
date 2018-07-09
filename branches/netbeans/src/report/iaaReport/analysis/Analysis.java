/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.analysis;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.ClassAgreementDepot;
import report.iaaReport.IAA;
import report.iaaReport.PairWiseDepot;
import report.iaaReport.analysis.detailsNonMatches.Comparator;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;

/**
 * One of cores of the IAA report system. It analysis all annotations we have to
 * find matched annotations and unmatched annotations. All store results in
 * several different static classes, such as the class of "PairWiseDepot", etc.
 *
 * @author Chris Jianwei Leng
 *
 */
public class Analysis {

    private ArrayList<String> __selectedAnnotators;
    private ArrayList<String> __selectedClasses;

    public Analysis(ArrayList<String> _selectedAnnotators, ArrayList<String> _selectedClasses) {
        this.__selectedAnnotators = _selectedAnnotators;
        this.__selectedClasses = _selectedClasses;
    }
    
    

    /**
     * This method is one of cores of the IAA report system. It analysis all
     * annotations we have to find matched annotations and unmatched
     * annotations. All store results in several different static classes, such
     * as the class of "PairWiseDepot", etc.
     */
    public void startAnalysis() throws Exception {
        // ---- 1 ----
        // clear static memory 
        PairWiseDepot.removeAll();  // PairWise table used to generate the
        // pricine table between each 2 of these
        // annotators
        DiffAnalysisResult.removeAll();
        ClassAgreementDepot.clear();

        // for 3 way table
        setAllAnnotationUnMatched_3Way();
        compareAnnotations_multipleWay( __selectedAnnotators, __selectedClasses );
        // record for 3 way table based flags set by previous comparation work in "compareAnnotations(,)"
        ClassAgreementDepot.record();


        // #### 2 ####
        // to compare annotations belong to different annotator, you at least
        // have two or more annotators
        if (__selectedAnnotators.size() < 2) {
            String info = "To generate the IAA report, we need two data set of annotations belong to at least two annotators.";
            IAA.setwarningtext(info);
            log.LoggingToFile.log(Level.SEVERE, info);
            commons.Tools.beep();
            return;
        }

        int size = this.__selectedAnnotators.size();

        // #### check annotations for annotators one by one
        for (int i = 0; i < size; i++) {
            // (1) set all annotation unprocessed(). 
            // (2) set all annotation isComparedAsMatched = false
            setAllAnnotationUnMatched();


            String annotator1 = this.__selectedAnnotators.get(i);
            if ((annotator1 == null) || ((annotator1.trim().length() < 1))) {
                continue;
            }

            //~####~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            // build depot to store matched or matched annotaitons for current
            // annotator
            //buildDiffAnalysisDepot( annotator1 );
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // #### following are for PairWise Analysis
            // #### compare this annotator with others
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }

                String annotator2 = this.__selectedAnnotators.get(j);
                if ((annotator2 == null) || (annotator2.trim().length() < 1)) {
                    continue;
                }

                if (annotator1.trim().compareTo(annotator2.trim()) == 0) {
                    continue;
                }


                compareAnnotations(annotator1, annotator2);

                // after checked all pairs, we have labeled all matched annotations,
                // then we use following method to generate a matching table by classes.
                ClassAgreementDepot.record(annotator1, annotator2); // 2 way iaa table
            }
        }



        PairWiseDepot.completeForms();


        //ClassAgreementDepot.completeForms();

        log.LoggingToFile.log(Level.INFO, "1108181557::finished preparing iaa data.");
    }

    private void setAllAnnotationUnMatched() {
        try {
            Depot depot = new Depot();

            ArrayList<Article> articles = depot.getAllArticles();

            if (articles == null) {
                throw new Exception("1110140439::can not get saved annotations!");
            }

            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                int size = article.annotations.size();

                // reset all processed flags
                for (int i = 0; i < size; i++) {
                    Annotation annotation0 = article.annotations.get(i);
                    if (annotation0 == null) {
                        continue;
                    }
                    


                    
                    
                    /*
                     * if(( annotation0.annotationclass != null ) && (
                     * annotation0.annotationclass.trim().length() > 0 ) ){ if(
                     * !isClassSelected( annotation0.annotationclass ) ){
                     * annotation0.setUnProcessed(); } }else{
                     * annotation0.setProcessed(); }
                     */

                    annotation0.isComparedAsMatched = false;
                    annotation0.setUnProcessed();

                }
            }

        } catch (Exception ex) {
        }
    }

    /**
     * check whether a class is selected in IAA reported system.
     */
    private boolean isClassSelected(String _classname) {
        if (this.__selectedClasses == null) {
            return false;
        }

        if ((__selectedClasses == null) || (__selectedClasses.size() < 1)) {
            return false;
        }


        for (String classname : __selectedClasses) {
            if (classname == null) {
                continue;
            }

            if (classname.trim().compareTo(_classname.trim()) == 0) {
                return true;
            }
        }

        return false;

    }

    private void setAllAnnotationUnMatched_3Way() {
        try {
            Depot depot = new Depot();

            ArrayList<Article> articles = depot.getAllArticles();

            if (articles == null) {
                // error process
            }

            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                int size = article.annotations.size();

                // reset all processed flags
                for (int i = 0; i < size; i++) {
                    Annotation annotation0 = article.annotations.get(i);
                    if (annotation0 == null) {
                        continue;
                    }
                    


                    /*
                     * if(( annotation0.annotationclass != null ) && (
                     * annotation0.annotationclass.trim().length() > 0 ) ){ if(
                     * !isClassSelected( annotation0.annotationclass ) ){
                     * annotation0.setUnProcessed(); } }else{
                     * annotation0.setProcessed(); }
                     */

                    //annotation0.setUnProcessed();
                    //annotation0.matchedFor3WayIAATable = true;
                }
            }

        } catch (Exception ex) {
        }
    }

    private String[] resortAnnotators(Vector<String> annotators, String designatedAnnotator) throws Exception {
        if (annotators == null) {
            throw new Exception("1109011434::null value is given!");
        }

        int size = annotators.size();

        String[] toReturn = new String[size];
        toReturn[0] = designatedAnnotator.trim();

        int j = 1;
        for (int i = 0; i < size; i++) {

            if (annotators.get(i).trim().compareTo(designatedAnnotator.trim()) == 0) {
                continue;
            }

            toReturn[j] = annotators.get(i).trim();
            j++;
        }



        return toReturn;
    }

    /**
     * check for matched annotations
     */
    private void compareAnnotations(String annotator1, String annotator2) {
        try {

            log.LoggingToFile.log(Level.INFO, "Start to compare annotations "
                    + "between annotator: ["
                    + annotator1
                    + "], and another annotator: ["
                    + annotator2
                    + "]");

            // init the depot space for feature selected information
            PairWiseDepot.initAPairWiseRecord(annotator1, annotator2);


            // #### begin analysis to all annotations 
            // get all article
            Depot depot = new Depot();
            ArrayList<Article> articles = depot.getAllArticles();

            if (articles == null) {                
                // error process
                return;
            }

            // count how many documents we used to do analysis,
            // or how many documents are in current date set.



            // #### go though all articles
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                String file = article.filename;

                int size = article.annotations.size();




                try {

                    // ####0#### -------------------------------------------- //
                    // 1. set flag of all annotations as "unprocessed"
                    // 2. count annotation numbers of goldstandard set and
                    //    comparation set (you also can call it first annotator
                    //    set and second annotator set).


                    for (int i = 0; i < size; i++) {
                        Annotation annotation = article.annotations.get(i);
                        if (annotation == null) {
                            continue;
                        }
                        
                         if((  annotation.spanset == null ) ||(annotation.spanset.isEmpty()))
                                continue;

                        // #### 0.1 #### ------------------------------------ //
                        // set flag of all annotations in current article as
                        // "unprocessed"
                        annotation.setUnProcessed();
                        annotation.isComparedAsMatched = false;
                        
                        
                        if (!isClassSelected(annotation.annotationclass)) {
                            annotation.setProcessed();
                            continue;
                        }

                        // #### 0.2 #### ------------------------------------ //
                        // set the count plus 1 if we found an annotation whose 
                        // annotator is "annotator1" or "annotator2".
                        //
                        // so we can count annotation numbers for these two 
                        // annotators, and count for how many annotations of 
                        // in the gold standard set, or in the comparing set
                        if (annotation.getAnnotator() != null) {
                            if (Comparator.checkAnnotator(annotation, annotator1)) {
                                PairWiseDepot.countForGoldStandardSet(annotator1, annotator2);
                            } else if (Comparator.checkAnnotator(annotation, annotator2)) {
                                PairWiseDepot.countForCompareSet(annotator1, annotator2);
                            }

                        }

                    }


                    // set one gold standard annotation as object
                    //Vector<Integer> matchedCompare = new Vector<Integer>();

                    // #### go though all annotations of current article, and
                    //      compare them with the gold standard annotation
                    //      you just selected.
                    for (int i = 0; i < size; i++) {
                        Annotation leftAnnotation = article.annotations.get(i);
                        if (leftAnnotation == null) {
                            continue;
                        }
                        

                        if (leftAnnotation.isProcessed()) {
                            continue;
                        }
                        
                        if((  leftAnnotation.spanset == null ) ||(leftAnnotation.spanset.isEmpty()))
                                continue;

                        if (!isClassSelected(leftAnnotation.annotationclass)) {
                            leftAnnotation.setProcessed();
                            continue;
                        }

                        // do nothing if current class don't belong to annotator 1
                        // (the gold stand)                        
                        if (!Comparator.checkAnnotator(leftAnnotation, annotator1)) {
                            continue;
                        }

                        if (leftAnnotation.annotationclass == null) {
                            continue;
                        }


                        leftAnnotation.setProcessed();


                        /*
                         * // #### after we got a valid annotation // #### we
                         * check whether there is some annotations which // may
                         * same as the one we already selected as // the gold
                         * standard. for(int j=0; j<size; j++ ) { if(i==j)
                         * continue; Annotation annotation2 =
                         * article.annotations.get(j); if(annotation2==null)
                         * continue; if(annotation2.isProcessed()) continue;
                         *
                         * // same span position and annotator as if(
                         * (annotation2.spanstart == annotation1.spanstart) &&
                         * (annotation2.spanend == annotation1.spanend) ) {
                         * if(annotation2.annotator==null) continue;
                         * if((annotation2.annotationclass ==
                         * null)||(annotation1.annotationclass == null))
                         * continue;
                         *
                         * // same annotator and same class
                         * if((annotation2.annotator.trim().compareTo(annotator1.trim())==0)
                         * &&(annotation2.annotationclass.trim().compareTo(annotation1.annotationclass.trim())==0))
                         * {
                         *
                         * matchedGoldStandard.add(i); } } }
                         *
                         */



                        boolean foundMatchItem = false;

                        // #### compare the objective annotation from gold standard
                        //      
                        for (int s = 0; s < size; s++) {
                            if (i == s) {
                                continue;  // don't compare annotation to itself
                            }
                            Annotation rightAnnotation = article.annotations.get(s);
                            
                            if (rightAnnotation == null) {
                                continue;
                            }
                            
                            if((  rightAnnotation.spanset == null ) ||(rightAnnotation.spanset.isEmpty()))
                                continue;
                                

                            if (rightAnnotation.isProcessed()) {
                                continue;
                            }

                            // if this annotation belong to annotator 2
                            if (Comparator.checkAnnotator(rightAnnotation, annotator2)) {
                                boolean samespan = false;
                                if (Comparator.equalSpans(rightAnnotation, leftAnnotation)) {
                                    samespan = true;
                                }
                                if (IAA.CHECK_OVERLAPPED_SPANS) {
                                    if (Comparator.isSpanOverLap(rightAnnotation, leftAnnotation)) {
                                        samespan = true;
                                        System.out.println("overlapping: " + leftAnnotation.annotationText +"/" + rightAnnotation.annotationText);
                                    }
                                }

                                // if it has same span as current annotation of gold standard
                                if (samespan) {
                                    boolean addtionalConditions = true;

                                    // if they have same class
                                    if (IAA.CHECK_CLASS) {
                                        if (!Comparator.equalClasses(leftAnnotation, rightAnnotation)) {
                                            addtionalConditions = false;
                                        }
                                    }

                                    // this found annotation is matched with the one
                                    // of gold standard
                                    if (IAA.CHECK_ATTRIBUTES) {
                                        if (!Comparator.equalAttributes(leftAnnotation, rightAnnotation)) {
                                            addtionalConditions = false;
                                        }
                                    }

                                    if (IAA.CHECK_RELATIONSHIP) {
                                        if (!Comparator.equalRelationships(leftAnnotation, rightAnnotation, article.filename)) {
                                            addtionalConditions = false;

                                        }
                                    }

                                    /*
                                     * if(IAA.CHECK_COMMENT){ if(
                                     * !Comparator.equalComments(annotation1,
                                     * annotation3) ) { addtionalConditions =
                                     * false; } }
                                     */

                                    if (addtionalConditions) {
                                        //foundMatchItem = true;
                                        leftAnnotation.isComparedAsMatched = true;
                                        rightAnnotation.isComparedAsMatched = true;
                                        rightAnnotation.setProcessed();
                                        PairWiseDepot.truepositivePlusOne(annotator1, annotator2);

                                        foundMatchItem = true;

                                        // break current loop
                                        break;
                                    } // if addtionalConditions == false
                                    else {
                                        //leftAnnotation.matchedFor3WayIAATable = false;
                                        //rightAnnotation.matchedFor3WayIAATable = false;
                                    }
                                }
                            }
                        }

                        //if (!foundMatchItem) {
                        //    leftAnnotation.matchedFor3WayIAATable = false;
                        //}
                    }
                } catch (Exception ex) {
                    System.out.println("1109151945-A::");
                    ex.printStackTrace();
                }

            }

            log.LoggingToFile.log(Level.INFO, "1108181503:: Completed the IAA analysis.");

        } catch (Exception ex) {
            IAA.setwarningtext("error occurred while comparing annotations between two annotators!");
            log.LoggingToFile.log(Level.SEVERE, "Error occurred while comparing a");
        }
    }

    /**
     * check for matched annotations
     */
    private void compareAnnotations_multipleWay(ArrayList<String> _selectedAnnotators, ArrayList<String> _selectedClasses) {
        try {

            log.LoggingToFile.log(Level.INFO, "Start to compare annotations in multiple ways");


            // ~~~~ 1 ~~~~
            // Reset all annotations' 3wayIAA flags to "false", only annotations 
            // that matched in all ways can be marked as "true".
            Depot depot = new Depot();
            ArrayList<Article> articles = depot.getAllArticles();
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                for (Annotation annotation : article.annotations) {
                    annotation.is3WayMatched = false;
                }
            }

            // ~~~~ 2 ~~~~
            // to each annotation, if they are matches in all the way, then it can
            // be marked as matches, 6 of 6 same 

            /*
             * if you consider 6 annotations the same to be 6 matches, then you
             * don't multiply with anything and your original "all annotations =
             * matches + non-matches, and IAA = matches / all annotations" is
             * correct Shuying Shen: if you consider 5 out of 6 the same, it is
             * a completely different definition.
             */
            for (Article article : articles) {
                if (article == null) {
                    continue;
                }
                if (article.annotations == null) {
                    continue;
                }

                int size = article.annotations.size();

                for (int i = 0; i < size; i++) {
                    Annotation annotation = article.annotations.get(i);
                    //if (annotation.annotationText.compareTo("1996") == 0) {
                    //    System.out.println(" ------------------>[" + annotation.annotationText + "]" + annotation.uniqueIndex);
                    //}
                    if (annotation == null) {
                        continue;
                    }
                    
                    if((  annotation.spanset == null ) ||(annotation.spanset.isEmpty()))
                                continue;
                    
                    if (annotation.is3WayMatched == true) {
                        continue;
                    }


                    // check whether we can find annotations that same to this one
                    // and belong to other annotators.
                    // If we have n annotators, to diclear n annotations are matches, 
                    // we need to make sure 6 of 6 are same. Otherwise, even 5 of 6
                    // are same they are still non-matches.
                    ArrayList<Annotation> matches = getMatches(
                            annotation, // current annotation
                            _selectedAnnotators, // selected annotations
                            _selectedClasses,
                            article);

                    // avoid possible error
                    if (matches == null) {
                        continue;
                    }

                    // if n of n are same, we will change their flag to true.
                    if (matches.size() + 1 == _selectedAnnotators.size()) {
                        annotation.is3WayMatched = true;
                        //System.out.println("\n" + annotation.getTexts() + ", <--" + annotation.annotator);
                        for (Annotation match : matches) {
                            match.is3WayMatched = true;
                            //System.out.println(match.getTexts() + ", <--" + match.annotator);
                        }
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("1204260312::" + ex.getMessage());
        }
    }

    /**
     * Check whether a specific annotator is appeared in the list of given
     * annotations.
     */
    private boolean isAnnotatorIncluded(String annotator, ArrayList<String> annotators) {
        if ((annotator == null) || (annotators == null)) {
            return false;
        }

        for (String name : annotators) {
            if (name == null) {
                continue;
            }
            if (name.trim().compareTo(annotator.trim()) == 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Find matched annotations for the given annotation.
     *
     * @param annotation The given annotation
     *
     * @return A list of matched annotations.
     */
    private ArrayList<Annotation> getMatches(
            Annotation _leftAnnotation,
            ArrayList<String> _selectedAnnotators, // selected annotations
            ArrayList<String> _selectedClasses,
            Article _article) {

        if ((_leftAnnotation == null) || (_selectedAnnotators == null)) {
            return null;
        }

        // list for return
        ArrayList<Annotation> matches = new ArrayList<Annotation>();

        // list for annotators that we found matched annotations for them in this method
        ArrayList<String> annotators = new ArrayList<String>();
        annotators.add(_leftAnnotation.getAnnotator() );

        // 
        try {

            if (_article == null) {
                return null;
            }
            if (_article.annotations == null) {
                return null;
            }

            String file = _article.filename;
            int size = _article.annotations.size();

            for (int i = 0; i < size; i++) {
                Annotation rightannotation = _article.annotations.get(i);
                if( rightannotation.uniqueIndex == _leftAnnotation.uniqueIndex )
                    continue;
                
                if (rightannotation == null) {
                    continue;
                }
                
                if((  rightannotation.spanset == null ) ||(rightannotation.spanset.isEmpty()))
                                continue;
                
                
                if (rightannotation.is3WayMatched) {
                    continue;
                }

                // this annotation must be appeared be in the list of selected classes
                if (!isClassSelected(rightannotation.annotationclass)) {
                    continue;
                }
                if (!isAnnotatorIncluded(rightannotation.getAnnotator(), _selectedAnnotators)) {
                    continue;
                }

                if (isAnnotatorIncluded(rightannotation.getAnnotator(), annotators)) {
                    continue;
                }
                
                if( !this.isClassSelected( rightannotation.annotationclass ) )
                    continue;
                


                boolean samespan = false;
                if (Comparator.equalSpans(rightannotation, _leftAnnotation)) {
                    samespan = true;
                }
                if (IAA.CHECK_OVERLAPPED_SPANS) {
                    if (Comparator.isSpanOverLap(rightannotation, _leftAnnotation)) {
                        samespan = true;
                    }
                }

                // if it has same span as current annotation of gold standard
                if (samespan) {
                    boolean addtionalConditions = true;

                    // if they have same class
                    if (IAA.CHECK_CLASS) {
                        if (!Comparator.equalClasses(_leftAnnotation, rightannotation)) {
                            addtionalConditions = false;
                        }
                    }

                    // this found annotation is matched with the one
                    // of gold standard
                    if (IAA.CHECK_ATTRIBUTES) {
                        if (!Comparator.equalAttributes(_leftAnnotation, rightannotation)) {
                            addtionalConditions = false;
                        }
                    }

                    if (IAA.CHECK_RELATIONSHIP) {
                        if (!Comparator.equalRelationships(_leftAnnotation, rightannotation, _article.filename)) {
                            addtionalConditions = false;

                        }
                    }

                    /*
                     * if(IAA.CHECK_COMMENT){ if(
                     * !Comparator.equalComments(annotation1, annotation3) ) {
                     * addtionalConditions = false; } }
                     */

                    if (addtionalConditions) {
                        matches.add(rightannotation);
                        annotators.add( rightannotation.getAnnotator() );
                    }

                }
            }
        } catch (Exception ex) {
            System.out.println("1204260329::" + ex.getMessage());
        }

        return matches;
    }
}

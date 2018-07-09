/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import report.iaaReport.IAA;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;

/**
 *
 * @author leng
 */
public class Do {

    private final ArrayList<String> selectedClasses;

    public Do(final ArrayList<String> selectedAnnotators, final ArrayList<String> selectedClasses){
        this.selectedClasses = selectedClasses;
    }

    /**main method of this class*/
    public void run( ArrayList<String> selectedAnnotators ) throws Exception{

        

        try{

            // remove data of last analyst
            AnalyzedResult.clear();

            // tell system that which annotator will be selected to implement
            // the feature compasion.
            // It will ask the class of "AnalyzedResult" record a string array
            // which contains all selected annotators, and the annotator using
            // as currret main annotator is on the first slot.
            AnalyzedResult.setAnnotators(selectedAnnotators);

            // compare annotations of any two different annators
            int size = selectedAnnotators.size();
            for(int i=0; i<size; i++)
            {
                String annotator1 = selectedAnnotators.get(i).trim();

                AnalyzedAnnotator analyzedAnnotator = new AnalyzedAnnotator(annotator1);

                String[] annotators = resortAnnotators(selectedAnnotators, annotator1);
                analyzedAnnotator.annotators = annotators;

                for(int j=0; j<size; j++)
                {
                    if(i==j)
                        continue;

                    String annotator2 = selectedAnnotators.get(j);

                    // compare annotations of these two different annotators
                    log.LoggingToFile.log(Level.INFO, "comparing "
                            + "annotations between two annotators: ( "
                            + annotator1
                            +", "
                            + annotator2
                            + " ). begin......");

                    // start comparate annotations between two annotators
                    analyzedAnnotator = compare(annotator1, annotator2, analyzedAnnotator, annotators);
                    log.LoggingToFile.log(Level.INFO, "comparing "
                            + "annotations between two annotators: ( "
                            + annotator1
                            +", "
                            + annotator2
                            + " ). END <<~~~~");
                }

                // save results
                AnalyzedResult.addAnalyzedAnnotator(analyzedAnnotator);

            }

            //printResults(); // print all result for debugging

        }catch(Exception ex){
            throw new Exception("1109020113::"+ex.getMessage());
        }
    }
    
    /**This one is used to print out all contents in the class of 
     * "AnalyzedResult" for programmer to debug*/
    private void printResults(){
        AnalyzedResult.printResults();
    }


    /**compare two annotators, only can be called by method "Do(Vector<String>)"
     * in this class.
     *
     * @param   annotator1
     *          the annotator name of the first annotator. Usually, we will use
     *          this one to init rows to record all compare results between this
     *          annotator and others.
     *
     * @param   annotator2
     *          The second annotator's name.
     */
    private AnalyzedAnnotator compare(
            String annotator1,
            String annotator2,
            AnalyzedAnnotator analyzedAnnotator,
            String[] annotators) throws Exception
    {
        try{
           
            Depot depot = new Depot();
      
            ArrayList<Article> articles = depot.getAllArticles();
            for(Article article : articles )
            {
                setAllUnProcessed(article);

                // 对于这个annotator，遍历他所有annotation，纪录在这个里面，并且
                // record any total repetitive annotations
                AnalyzedArticle analyzedarticle =
                        initMainAnnotator(analyzedAnnotator, annotator1, article, annotators);
                //#### 1 #### add analyzed data into memory if we don't have
                if(analyzedarticle!=null)
                    analyzedAnnotator.setArticle( article.filename.trim(), analyzedarticle);

                analyzedAnnotator = checkSameAnnotations(annotator1, article, analyzedAnnotator);
                // #### ####
                // user annotations belong to annotator1 as gold standard
                // and then to compare them with annotations that belong to
                // annotation2
                analyzedAnnotator = checkMatchedAnnotations( annotator1, annotator2, article, analyzedAnnotator);

                //######## check for non-matched annotations
                analyzedAnnotator = checkNonMatchedAnnotations(
                        annotator1,
                        annotator2,
                        article,
                        analyzedAnnotator);

                //######## after checking for non-matched annotations, there
                //         still some annotations have not been processed
                //         as their belong to other annotations and no
                //         others' annotation have same or overlapped annotation
                //         to them.
                analyzedAnnotator = checkNonMatchedAnnotations_forUnprocessed(
                        annotator1,
                        annotator2,
                        article,
                        analyzedAnnotator,
                        annotators);
            }

            return analyzedAnnotator;
            
        }catch(Exception ex){
            throw new Exception(
                    "1109020240::error occurred while compare annotations between two different annotator: ("
                    + annotator1
                    + ", "
                    + annotator2
                    + " ).\nError Details:"
                    + ex.getMessage() );
        }

        
    }

    private boolean isInClassesList(Annotation annotation) throws Exception{
        if(annotation==null)
            throw new Exception("1109091041::you can not check a null annotation's class category!");

        if( (annotation.annotationclass==null)|| (annotation.annotationclass.trim().length()<1) )
            throw new Exception("1109091043::you can not check an annotation's class category with empty classname!");

        if ((this.selectedClasses == null)||(this.selectedClasses.size()<1))
            throw new Exception("1109091042::lost data!");

        for(String classname : this.selectedClasses)
        {
            if(classname==null)
                continue;

            if(classname.trim().compareTo(annotation.annotationclass.trim())==0)
            {
                return true;
            }
        }

        return false;

    }


    private AnalyzedAnnotator checkSameAnnotations(String annotator1,
            Article article,
            AnalyzedAnnotator analyzedAnnotator ) throws Exception
    {

        try{
            AnalyzedArticle analyzedarticle = analyzedAnnotator.getArticle(article.filename);

            // as all annotations belong to annotator1 has be saved in the format
            // of "AnalyzedAnnotator", we will list each row and get one annotator from
            // each row, and compare it with every annotations belong to annotator2
            // in the same article
            for(AnalyzedAnnotation analyzedAnnotation: analyzedarticle.rows)
            {
                if((analyzedAnnotation==null)||(analyzedAnnotation.mainAnnotations==null))
                {
                    // MUSTMUST show warning log
                    continue;
                }

                Annotation annotation_rowhead = null;
                if(analyzedAnnotation.mainAnnotations.size()>=1)
                    annotation_rowhead = analyzedAnnotation.mainAnnotations.get(0);
                if(annotation_rowhead==null){
                    continue;
                }

                // compare the annotation of annotation1 to annotations of
                // annotator2
                for(Annotation annotation: article.annotations)
                {
                    if(annotation==null)
                        //MUSTMUST need to show warnings
                        continue;

                    if(annotation.isProcessed())
                        continue;

                    if(Comparator.checkAnnotator(annotation, annotator1))
                    {
                        boolean sameSpan = false;
                        boolean sameClass = false;
                        boolean sameNormalRelationship = false;
                        boolean sameComplexRelationship = false;
                        boolean sameComment = false;

                        boolean overlapped = Comparator.isSpanOverLap(annotation_rowhead, annotation);
                        
                        sameSpan = Comparator.equalSpans(annotation_rowhead, annotation);
                        //sameClass = Comparator.equalClasses(annotation_rowhead, annotation);
                        // System.out.println("same class = " + sameClass + ":  " + annotation_rowhead.annotationText + " || " + annotation.annotationText);
                        sameNormalRelationship = Comparator.equalAttributes(annotation_rowhead, annotation);
                        sameComplexRelationship = Comparator.equalRelationships(annotation_rowhead, annotation, article.filename );
                        sameComment = Comparator.equalComments(annotation_rowhead, annotation);

                        // #### check spans
                        // consider overlapped annotations same as exact same span annotations
                        if( IAA.CHECK_OVERLAPPED_SPANS ){ // if IAA wants overlapped annotations
                            if( (!sameSpan) && (!overlapped) )                                
                                continue;
                        }
                        // consider overlapped annotations are different
                        else{
                            if( !sameSpan )
                                continue;
                            
                            if( overlapped )
                                continue; // there are different while spans are overlapped while IAA.CHECK_OVERLAPPED_SPANS=true;
                        }
                        
                        boolean same = true;
                        
                        if(IAA.CHECK_ATTRIBUTES){
                            if(!sameNormalRelationship)
                                same = false;                            
                        }
                        
                        if( IAA.CHECK_CLASS ){
                            if(!sameClass)
                                same = false;
                        }
                        
                        if( IAA.CHECK_RELATIONSHIP ){
                            if(!sameComplexRelationship)
                                same = false;
                        }
                        
                        if( IAA.CHECK_COMMENT ){
                            if(!sameComment)
                                same = false;
                        }
                        
                        if(same)
                        {
                            analyzedAnnotation.addmain(annotation);

                            annotation.setProcessed();
                        }
                    }
                }
            }

            return analyzedAnnotator;

        }catch(Exception ex){
            throw new Exception("1109020347a::error while we are comparing annotations.\nError Details: " + ex.getMessage());
        }
    }



    /**as all annotations belong to annotator1 has be saved in the format of
     * "AnalyzedAnnotator", we will list each row and get one annotator from
     * each row, and compare it with every annotations belong to annotator2
     * in the same article
     */
    private AnalyzedAnnotator checkMatchedAnnotations(String annotator1,
            String annotator2,
            Article article,
            AnalyzedAnnotator analyzedAnnotator ) throws Exception
    {

        try{
            AnalyzedArticle analyzedarticle = analyzedAnnotator.getArticle(article.filename);

            if(analyzedarticle==null){
                analyzedarticle = new AnalyzedArticle(article.filename);
            }


            // as all annotations belong to annotator1 has be saved in the format
            // of "AnalyzedAnnotator", we will list each row and get one annotator from
            // each row, and compare it with every annotations belong to annotator2
            // in the same article
            for(AnalyzedAnnotation analyzedAnnotation: analyzedarticle.rows)
            {

                if((analyzedAnnotation==null)||(analyzedAnnotation.mainAnnotations==null))
                        
                {
                    // MUSTMUST show warning log
                    continue;
                }
                
                

                Annotation annotation_rowhead;
                if(analyzedAnnotation.mainAnnotations.size()>=1)
                    annotation_rowhead = analyzedAnnotation.mainAnnotations.get(0);
                else
                    annotation_rowhead = null;
                if(annotation_rowhead==null){
                    continue;
                }


                // compare the annotation of annotation1 to annotations of
                // annotator2
                for(Annotation annotation: article.annotations)
                {
                    if(annotation==null)
                        //MUSTMUST need to show warnings
                        continue;

                    if(annotation.isProcessed())
                        continue;

                    if(!isInClassesList(annotation))
                        continue;

                    if(Comparator.checkAnnotator(annotation, annotator2))
                    {
                        boolean sameSpan = false;
                        boolean sameClass = false;
                        boolean sameNormalRelationship = false;
                        boolean sameComplexRelationship = false;
                        boolean sameComment = false;
                        
                        boolean overlapped = Comparator.isSpanOverLap(annotation_rowhead, annotation);

                        sameSpan = Comparator.equalSpans(annotation_rowhead, annotation);
                        sameClass = Comparator.equalClasses(annotation_rowhead, annotation);
                        sameNormalRelationship = Comparator.equalAttributes(annotation_rowhead, annotation);
                        sameComplexRelationship = Comparator.equalRelationships(annotation_rowhead, annotation, article.filename);
                        sameComment = Comparator.equalComments(annotation_rowhead, annotation);

                        if( IAA.CHECK_OVERLAPPED_SPANS ){ // if IAA wants overlapped annotations
                            
                            // spans aren't same if not exact same span
                            if( ( !sameSpan) && (!overlapped) )
                                continue;
                                
                        }
                        // consider overlapped annotations are different
                        else{
                            // 如果不重叠，就不是same span
                            if( !sameSpan )
                                continue;
                            
                            // not same span if they are overlapping
                            if( overlapped )
                                continue; // there are different while spans are overlapped while IAA.CHECK_OVERLAPPED_SPANS=true;
                        }
                        
                        boolean same = true;
                        
                        if(IAA.CHECK_CLASS){
                            if(!sameClass)
                                same = false;
                        }else
                            sameClass = true;
                        
                        if(IAA.CHECK_ATTRIBUTES){
                            if(!sameNormalRelationship)
                                same = false;
                    }else
                                sameNormalRelationship = true;
                        
                        if(IAA.CHECK_RELATIONSHIP)
                        {
                            if(!sameComplexRelationship)
                                same = false;
                        } else sameComplexRelationship = true;
                        
                        if(IAA.CHECK_COMMENT)
                        {    if(!sameComment)
                                same = false;
                        }else{
                            sameComment = true;
                        }
                        
                        
                        if ( same )
                        {
                            analyzedAnnotation.add(annotator2, annotation,
                                    false, //!sameSpan,
                                    !sameClass,
                                    !sameNormalRelationship,
                                    !sameComplexRelationship,
                                    !sameComment
                                    );
                            
                            annotation.setProcessed();
                        }
                    }
                }
            }
            
            return analyzedAnnotator;

        }catch(Exception ex){
            throw new Exception("1109020347V::error while we are comparing annotations.\nError Details: " + ex.getMessage());
        }
    }

    private AnalyzedAnnotator checkNonMatchedAnnotations(
            String annotator1,
            String annotator2,
            Article article,
            AnalyzedAnnotator analyzedAnnotator ) throws Exception{

        try{
            AnalyzedArticle analyzedarticle = analyzedAnnotator.getArticle(article.filename);

            for(AnalyzedAnnotation analyzedAnnotation: analyzedarticle.rows)
            {
                if((analyzedAnnotation==null)||(analyzedAnnotation.mainAnnotations==null))
                {
                    // MUSTMUST show warning log
                    continue;
                }
                
                if(analyzedAnnotation.mainAnnotations.size()<1){
                    continue;
                }                
                
                Annotation annotation_rowhead = analyzedAnnotation.mainAnnotations.get(0);
                if(annotation_rowhead==null){
                    continue;
                }

                // compare the annotation of annotation1 to annotations of
                // annotator2
                for(Annotation annotation: article.annotations)
                {

                    if(annotation==null)
                        //MUSTMUST need to show warnings
                        continue;

                    if(annotation.isProcessed())
                        continue;
                    
                    if( (annotation.spanset == null)||(annotation.spanset.isEmpty()) )
                        continue;

                    //if(!isInClassesList(annotation))
                    //    continue;

                    if(Comparator.checkAnnotator(annotation, annotator2))
                    {
                        boolean sameSpan = false;
                        boolean sameClass = false;
                        boolean sameNormalRelationship = false;
                        boolean sameComplexRelationship = false;
                        boolean overlap = false;
                        boolean sameComment = false;
                        
                        //boolean overlapped = Comparator.isSpanOverLap(annotation_rowhead, annotation);

                        sameSpan = Comparator.equalSpans(annotation_rowhead, annotation);
                        sameClass = Comparator.equalClasses(annotation_rowhead, annotation);
                        sameNormalRelationship = Comparator.equalAttributes(annotation_rowhead, annotation);
                        sameComplexRelationship = Comparator.equalRelationships(annotation_rowhead, annotation, article.filename);
                        sameComment = Comparator.equalComments(annotation_rowhead, annotation);
                        overlap = Comparator.isSpanOverLap(annotation_rowhead, annotation);

                        
                        
                        // overlapped spans are one kind of non-matches while the flag is off.
                        if((IAA.CHECK_OVERLAPPED_SPANS==false)&&(overlap))
                        {
                            analyzedAnnotation.add(annotator2, annotation,
                                    false, // !overlap, now overlap=true, false means no difference in span
                                    !sameClass,
                                    !sameNormalRelationship,
                                    !sameComplexRelationship,
                                    !sameComment
                                    );

                            annotation.setProcessed();
                        }
                        // same span
                        else if( (sameSpan) || ((IAA.CHECK_OVERLAPPED_SPANS)&&(overlap==true)) )
                        {
                            boolean diffFound = false;
                            if((sameClass==false)&&(IAA.CHECK_CLASS))
                                diffFound = true;
                            if((IAA.CHECK_ATTRIBUTES)&&(sameNormalRelationship==false))
                                diffFound = true;
                            if((IAA.CHECK_RELATIONSHIP)&&(sameComplexRelationship==false))
                                diffFound = true;
                            if((IAA.CHECK_COMMENT)&&(sameComment==false))
                                diffFound = true;

                            if(diffFound)
                            {
                                analyzedAnnotation.add(annotator2, annotation,
                                    false, //!sameSpan,
                                    !sameClass,
                                    !sameNormalRelationship,
                                    !sameComplexRelationship,
                                    !sameComment
                                    );

                                annotation.setProcessed();
                            }
                        }
                    }
                }
            }

            return analyzedAnnotator;

        }catch(Exception ex){
            throw new Exception("1109020347b::error while we are comparing annotations.\nError Details: " + ex.getMessage());
        }
    }

    private AnalyzedAnnotator dealUnProcessed(
            Annotation annotation, // annotation whose annotator is not "annotator0"
            AnalyzedAnnotator analyzedAnnotator,
            Article article,
            String[] annotators)
    {
        try{

            // #### 1 ####
            // If there are some annoation have similar spans to give annotation,
            // compare and save it into current "analyzedarticle"
            boolean foundsimilar = false;

            AnalyzedArticle analyzedarticle = analyzedAnnotator.getArticle(article.filename);
            if(analyzedarticle==null)
            {
                analyzedarticle = new AnalyzedArticle(article.filename);
                analyzedAnnotator.analyzedArticles.add(analyzedarticle);
            }

            // #### 1.1 #### go over all saved/processed annotations
            int numberOfRows = analyzedarticle.rows.size();
            for(int i = 0; i< numberOfRows; i++ )
            {
                AnalyzedAnnotation aannotation = analyzedarticle.rows.get(i);
                if(aannotation==null)
                    continue;

                OthersAnnotations[] oas = aannotation.othersAnnotations;
                if(oas==null)
                    continue;
                
                int length = oas.length;
                for(int j=0;j<length;j++)
                {
                    OthersAnnotations oa = oas[j];
                    if(oa==null)
                        continue;

                    Vector<AnalyzedAnnotationDifference> aads = oa.annotationsDiffs;
                    if(aads==null)
                        continue;
                    for(AnalyzedAnnotationDifference aad : aads)
                    {
                        if(aad==null)
                            continue;
                        boolean samespan = Comparator.equalSpans(aad.annotation, annotation);
                        boolean overlapped = Comparator.isSpanOverLap(aad.annotation, annotation);

                        // ensure that we need to
                        //if(IAA.CHECK_OVERLAPPED_SPANS){
                            if((!overlapped)||(!samespan))
                                continue;
                        //}else{
                        //    if(!samespan)
                        //        continue;
                        //}

                        foundsimilar = true;
                        // ########
                        // compare and save diffed results
                        for(int s=0;s<length;s++)
                        {
                            OthersAnnotations oa_modified = oas[s];
                            if(oa_modified==null)
                                continue;
                            if(Comparator.checkAnnotator(annotation, oa_modified.annotator)){
                                oas[s].annotationsDiffs.add(new AnalyzedAnnotationDifference(annotation, false, true, true, true, true));
                                aannotation.othersAnnotations = oas;
                                analyzedarticle.rows.set(i, aannotation);
                                analyzedAnnotator.setArticle(article.filename, analyzedarticle);
                                return analyzedAnnotator;
                            }
                        }                                                                        
                    }                    
                }
            }

            // #### 2 ####
            // if flag "foundsimilar" indicated we didnt found any existing
            // similar annotations in saved results of analyzedannotator,
            // we need to create a new item and save it into analyzedannotator
            // for this annotation
            if (foundsimilar==false){
                AnalyzedAnnotation aa = record( annotation, annotators );
                analyzedarticle.rows.addElement( aa );
                //analyzedAnnotator.setArticle(article.filename, analyzedarticle);
            }            
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "11101331::\n" + ex.getMessage() );
        }

        return analyzedAnnotator;
    }

    private AnalyzedAnnotation record(Annotation annotation, String[] annotators) throws Exception{
        if(annotation==null)
            throw new Exception("1110041719:: A given annotation is NULL!!!");

        AnalyzedAnnotation aa = new AnalyzedAnnotation(annotators);
        if( aa == null )
            throw new Exception("1110041718:: NULL Value!");
        
        OthersAnnotations[] oas = aa.othersAnnotations;
        if( oas == null )
            throw new Exception("1110041717:: NULL Value!");
        if( oas.length < 1 )
            throw new Exception("1110041716:: Invalid Value!");

        try{
            for(int i=0; i< oas.length; i++)
            {
                if(oas[i]==null)
                    throw new Exception("1110041748:: NULL Value!");

                OthersAnnotations oa = oas[i];
                if(oa==null)
                    throw new Exception("1110041749:: NULL Value!");

                if(Comparator.checkAnnotator(annotation, oa.annotator)){
                    oa.annotationsDiffs.add(new AnalyzedAnnotationDifference(annotation, true, true, true, true, true));
                    oas[i]=oa;
                    aa.othersAnnotations = oas;
                }
            }
        }catch(Exception ex){
            throw new Exception("1110041943::");
        }

        return aa;
    }

    private AnalyzedAnnotator checkNonMatchedAnnotations_forUnprocessed(
            String annotator1,
            String annotator2,
            Article article,
            AnalyzedAnnotator analyzedAnnotator,
            String[] annotators) throws Exception{

        try{

            for(Annotation annotation: article.annotations)
            {
                // annotation can not be fill
                if(annotation==null)
                    continue;
                
                if( (annotation.spanset == null)||(annotation.spanset.isEmpty()) )
                    continue;
                
                // annotation can not belong to annotator1
                if(Comparator.checkAnnotator( annotation, annotator1))
                    continue;
                // annotation must belong to annotation2
                if(!Comparator.checkAnnotator( annotation, annotator2))
                    continue;

                // annotation can not be processed yet
                if( annotation.isProcessed() )
                    continue;

                if( ! isInClassesList(annotation))
                    continue;
                
                // record into analyzedAnnotator
                analyzedAnnotator = dealUnProcessed(annotation, analyzedAnnotator, article, annotators );


                // set as processed
                annotation.setProcessed();
                
            }

            

            return analyzedAnnotator;

        }catch(Exception ex){
            throw new Exception("1109020347c::error while we are comparing annotations.\nError Details: " + ex.getMessage());
        }
    }



    /**set all annotations in this article is unprocessed. */
    private void setAllUnProcessed(Article article) {
        for(Annotation annotation:article.annotations){
            annotation.setUnProcessed();
        }
    }

    static int iii = 0;

    private AnalyzedArticle initMainAnnotator(AnalyzedAnnotator _analyzedAnnotator, String annotator, Article article, String[] annotators) throws Exception {

        try{

            AnalyzedArticle analyzedArticle = null;

            //#### 1 #### get/allocate a space for analyzedArticle of current annotator
            //#### 1.1 #### try to get it if it already got inited.

            if(_analyzedAnnotator.articleExists(article.filename))
            {
                analyzedArticle =  _analyzedAnnotator.getArticle(article.filename);;
            }
            //#### 1.2 #### create a new analyzed article if we don't have it yet.
            else
            {
                analyzedArticle = new AnalyzedArticle(article.filename);
            }

            //for(AnalyzedAnnotation row:analyzedArticle.rows){
            //    Annotation a1 = row.mainAnnotations.get(0);
            //    System.out.println("1----------------> " + a1.annotationText + ": " +a1.spanstart + " :" +a1.annotator);
            //}

            if(analyzedArticle == null)
                throw new Exception("error 1109081615::fail to init annotation space for main annotator:["+annotator+"].");


            for(Annotation annotation:article.annotations)
            {
                if(annotation==null)
                    continue;
                
                if( (annotation.spanset == null)||(annotation.spanset.isEmpty()) )
                    continue;
                
                

                if(annotation.getAnnotator() ==null)
                    continue;

                if(annotation.getAnnotator().trim().compareTo(annotator.trim())!=0)
                    continue;

                if(!isInClassesList(annotation))
                    continue;

                

                if(analyzedArticle.isInited(annotation)==false)
                {
                    analyzedArticle.initRow(annotation, annotators);
                    //System.out.println("INITing - " + annotation.annotationText + " - [ " + annotation.spanstart + ", " + annotation.spanstart + "]: "+annotation.annotator);
                }//else{
                //   System.out.println("inited - " + annotation.annotationText + " - [ " + annotation.spanstart + ", " + annotation.spanstart + "]: "+annotation.annotator + "---------");
                //}
            }

            //for(AnalyzedAnnotation row:analyzedArticle.rows){
            //    Annotation a1 = row.mainAnnotations.get(0);
            //    System.out.println("2----------------> " + a1.annotationText + ": " +a1.spanstart + " :" +a1.annotator);
            //}

            return analyzedArticle;

        }catch(Exception ex){
            throw new Exception("1109020153::error occurred while init space " 
                    + "for a annotator in the process of analysting matched "
                    + "and non-matched details. \nError Details: "
                    + ex.getMessage());
        }
        

    }

    /**return an array of strings which contains all selected annotators, and
     * current annotator is in the first slot of this array.
     */
    private String[] resortAnnotators(ArrayList<String> annotators, String designatedAnnotator ) throws Exception{
        if(annotators==null)
            throw new Exception("1109011434::null value is given!");

        int size = annotators.size();

        String[] toReturn = new String[size];
        toReturn[0] = designatedAnnotator.trim();

        int j=1;
        for(int i=0;i<size;i++){

            if( annotators.get(i).trim().compareTo(designatedAnnotator.trim())==0 )
                continue;

            toReturn[j] = annotators.get(i).trim();
            j++;
        }
        
        return toReturn;
    }
}

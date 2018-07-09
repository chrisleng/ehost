/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import java.util.Vector;
import resultEditor.annotations.Annotation;

/**
 *
 * @author leng
 */
public class AnalyzedAnnotation {

    /**main annotation or annotations*/
    public Vector<Annotation> mainAnnotations = new Vector<Annotation>();

    /**After comparing the main annotation/annotations to other annotators'
     * annotation, we collect matched or non-matched annotations of others.
     */
    public OthersAnnotations[] othersAnnotations = null;

    /**@param   annotators
     *          We will use this to init the array of the "otherAnnotaions".
     *          In the array of "annotators", the first one should be the same
     *          annotator as what annotator the main annotation has.
     */
    public AnalyzedAnnotation(Vector<Annotation> annotations, String[] annotators ){
        mainAnnotations.clear();
        for(Annotation annotation: annotations){
            mainAnnotations.add(annotation);
        }

        OthersAnnotations[] thisothersAnnotations = new OthersAnnotations[annotators.length -1];

        for(int i=1; i<annotators.length; i++){
            OthersAnnotations oa = new OthersAnnotations();
            oa.annotator = annotators[i].trim();
            thisothersAnnotations[i-1] = oa;
        }
        this.othersAnnotations = thisothersAnnotations;
        
    }

    public AnalyzedAnnotation(Annotation annotation, String[] annotators ){
        
        mainAnnotations.clear();
        mainAnnotations.add(annotation);

        OthersAnnotations[] thisothersAnnotations = new OthersAnnotations[annotators.length -1];
        
        for(int i=1; i<annotators.length; i++){
            OthersAnnotations oa = new OthersAnnotations();
            oa.annotator = annotators[i].trim();
            thisothersAnnotations[i-1] = oa;
        }

        this.othersAnnotations = thisothersAnnotations;
    }

    public AnalyzedAnnotation(String[] annotators ){

        mainAnnotations.clear();

        OthersAnnotations[] thisothersAnnotations = new OthersAnnotations[annotators.length -1];

        for(int i=1; i<annotators.length; i++){
            OthersAnnotations oa = new OthersAnnotations();
            oa.annotator = annotators[i].trim();
            thisothersAnnotations[i-1] = oa;
        }

        this.othersAnnotations = thisothersAnnotations;
    }

    void addmain( Annotation annotation  ) throws Exception {
        if(this.mainAnnotations ==null){
            throw new Exception("1109090410");
        }

        int size=mainAnnotations.size();
        for(int i=0; i<size; i++ )
        {
            Annotation ann = this.mainAnnotations.get(i);
            if(ann.uniqueIndex==annotation.uniqueIndex)
                return;
            
        }
        this.mainAnnotations.add(annotation);
    }

    void add(String annotator2, Annotation annotation, 
            boolean diffInSpan,
            boolean diffInClass,
            boolean diffInAttribute,
            boolean diffInRelationship,
            boolean difInComment) throws Exception {
        if(this.othersAnnotations==null){
            throw new Exception("1109020410");
        }

        int size=othersAnnotations.length;
        for(int i=0; i<size; i++ )
        {
            // if this column of current row is for annotator2
            if(annotator2.trim().compareTo( othersAnnotations[i].annotator.trim() )==0)
            {
                AnalyzedAnnotationDifference anaDiffs = 
                        new AnalyzedAnnotationDifference(
                        annotation,
                        diffInSpan,
                        diffInClass,
                        diffInAttribute,
                        diffInRelationship,
                        difInComment
                        );
                othersAnnotations[i].annotationsDiffs.add(anaDiffs);
            }
        }
    }

    
            
}

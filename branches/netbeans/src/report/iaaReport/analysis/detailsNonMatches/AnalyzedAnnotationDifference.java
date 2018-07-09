/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import resultEditor.annotations.Annotation;


/**
 *
 * @author leng
 */
public class AnalyzedAnnotationDifference {

    public String annotator;
    public Annotation annotation = null;

    public boolean diffInClass = false;
    public boolean diffInSpan = false;
    public boolean diffInAttribute = false;
    public boolean diffInRelationship = false;
    public boolean diffInComment = false;
    

    public AnalyzedAnnotationDifference(String annotator) throws Exception
    {
        if(annotator==null)
            throw new Exception("110109011503::annotator string is null");

        if(annotator.trim().length()<1)
            throw new Exception("110109011504::annotator string is empty.");

        this.annotator = annotator.trim();
    }

    public String getAnnotator() throws Exception{
        if(annotation==null)
            throw new Exception("1109020415");
        
        return annotation.getAnnotator();
    }

    /**
     * @param   diffInClass
     *          a boolean value used to indicate whether there are difference
     *          between the class of this annotation and its compared objective
     *          annotation
     */
    public AnalyzedAnnotationDifference(Annotation annotation,
            boolean diffInSpan, boolean diffInClass,
            boolean diffInAttribute,
            boolean diffInRelationship,
            boolean diffInComment)
    {
        this.annotation = annotation;
        this.diffInSpan = diffInSpan;
        this.diffInClass = diffInClass;
        this.diffInAttribute = diffInAttribute;
        this.diffInRelationship = diffInRelationship;
        this.diffInComment = diffInComment;

    }

    
}
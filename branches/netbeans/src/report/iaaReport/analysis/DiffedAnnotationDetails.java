/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package report.iaaReport.analysis;

import resultEditor.annotations.Annotation;

/**
 *
 * @author Chris
 */
public class DiffedAnnotationDetails {
    public String annotator;
    public Annotation annotation = null;

    public DiffedAnnotationDetails(String annotator) throws Exception
    {
        if(annotator==null)
            throw new Exception("110109011503::annotator string is null");
        
        if(annotator.trim().length()<1)
            throw new Exception("110109011504::annotator string is empty.");

        this.annotator = annotator.trim();
    }

    public String getAnnotator(){
        return annotation.getAnnotator();
    }

    /**
     * @param   diffInClass
     *          a boolean value used to indicate whether there are difference
     *          between the class of this annotation and its compared objective
     *          annotation
     */
    public DiffedAnnotationDetails(Annotation annotation,
            boolean diffInSpan, boolean diffInClass,
            boolean diffInAttribute,
            boolean diffInRelationship)
    {
        this.annotation = annotation;
        this.diffInSpan = diffInSpan;
        this.diffInClass = diffInClass;
        this.diffInAttribute = diffInAttribute;
        this.diffInRelationship = diffInRelationship;

    }
    
    public boolean diffInClass = false;
    public boolean diffInSpan = false;
    public boolean diffInAttribute = false;
    public boolean diffInRelationship = false;
    
}

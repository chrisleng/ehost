/*
 *  
 */
package report.iaaReport.analysis;

import resultEditor.annotations.Annotation;

/**
 * This is the definition of Non-Match-Annotations, it records and manages
 * all non-matched annotation we found.
 * 
 * @author Chris
 */
public class DiffedAnnotationDef {
    public Annotation objectAnnotation = null;

    /**To the given annotation, the public value "objectAnnotation" of this
     * class, we record all non-matched annotations in this vector "unmatched"
     * if they has different span position, classes, attributes, or
     * relationships.
     */
    private DiffedAnnotationDetails[] unmatched = null;


    /**class construction, 1/2
     * @param   annotation
     *          The annotation that we consider as the objective annotation for
     *          comparsion.
     */
    public DiffedAnnotationDef(Annotation annotation, String[] annotators) throws Exception{

        this.objectAnnotation = annotation;

        if(annotators==null)
            throw new Exception("1109011455::selected annotators is null.");
        
        if(annotators.length < 2 )
            throw new Exception("1109011456::you need to select at least 2 annotators to do this analysis.");

        unmatched = new DiffedAnnotationDetails[ annotators.length ];
    }


    /**return all different annotations to this annotation
     */
    public DiffedAnnotationDetails[] getAllNonMatched(){
        return unmatched;
    }


    /**tell you whether current annotation is a non-matched annotation after
     * comparing it with annotations belong to other annotators. it return
     * true of if it found other annotators annotations have same or overlapped
     * span as the object annotation, but have different class, attributes or
     * relationships.
     */
    public boolean isNonMatched(){
        if (unmatched==null)
            return false;

        if (unmatched.length<1)
            return false;

        return true;
    }

    public void addNonMatchedAnnotation(Annotation annotation,
            boolean diffInSpan, boolean diffInClass,
            boolean diffInAttribute,
            boolean diffInRelationship) throws Exception
    {
        if(unmatched==null)
            throw new Exception("1109011454:: you need to allocate space for \"unmatched\" to initlize it before use it.");

        /*unmatched.add(
                new DiffedAnnotationDetails(
                    annotation,
                    diffInSpan,
                    diffInClass,
                    diffInAttribute,
                    diffInRelationship)
                );*/
    }
    
}

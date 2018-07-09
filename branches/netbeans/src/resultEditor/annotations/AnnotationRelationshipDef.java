/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations;

/**
 *
 * @author Chris
 */
public class AnnotationRelationshipDef {
    public int linkedAnnotationIndex;
    public String linkedAnnotationText;
    public String mention;
    public String annotationClass;
    
    /**The 1st class constructor*/
    public AnnotationRelationshipDef(){};
    
    /**The second class constructor.*/
    public AnnotationRelationshipDef(Annotation e)
    {
        linkedAnnotationIndex = e.uniqueIndex;
        linkedAnnotationText = e.annotationText;
        mention = e.mentionid;
        annotationClass = e.annotationclass;
    }
}

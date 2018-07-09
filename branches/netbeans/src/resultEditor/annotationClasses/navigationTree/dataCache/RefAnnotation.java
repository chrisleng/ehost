/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import resultEditor.annotations.Annotation;

/**
 *
 * @author imed
 */
public class RefAnnotation {
    public Annotation annotation = null;
    public String filename = null;
    
    public RefAnnotation(Annotation annotation, String filename){
        this.annotation = annotation;
        this.filename = filename;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import resultEditor.annotations.Annotation;
import java.util.Vector;

/**
 *
 * @author Chris
 */
public class AnnotationType {
    public String name;
    public int amount;

    /**record the span start postion of the first annotation*/
    public int firstspanstart = -1;

    public Vector<file_annotation> subAnnotations = new Vector<file_annotation>();

    public AnnotationType(){
        subAnnotations.clear();
    }

    public AnnotationType(String name, int amount, int _firstspanstart){
        subAnnotations.clear();
        this.name = name;
        this.amount = amount;
        this.firstspanstart = _firstspanstart;
    }

    public void addAnnotation( String filename, Annotation annotation ){
        file_annotation fa = new file_annotation();
        fa.filename = filename;
        fa.annotation = annotation;

        subAnnotations.add(fa);
    }

}



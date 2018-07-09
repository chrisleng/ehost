/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import java.util.Comparator;


/**
 *
 * @author Chris
 */
public class AnnotationTypeCOmparator_location implements Comparator{
    public int compare(Object o1,Object o2) {
        AnnotationType ac1 = (AnnotationType) o1;
        AnnotationType ac2 = (AnnotationType) o2;

        int ac1_firstspanstart = ac1.firstspanstart;
        int ac2_firstspanstart = ac2.firstspanstart;

        if(ac1_firstspanstart>=ac2_firstspanstart)
            return 1;
        else
            return 0;


    }
}

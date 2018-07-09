/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import java.util.*;

/**
 *
 * @author Chris
 */
public class AnnotatedClassComparator implements Comparator{
    public int compare(Object o1,Object o2) {
        AnnotationClass ac1 = (AnnotationClass) o1;
        AnnotationClass ac2 = (AnnotationClass) o2;

        String ac1_classname = ac1.annotatedClassName.trim();
        String ac2_classname = ac2.annotatedClassName.trim();

        int size1 = ac1_classname.length();
        int size2 = ac2_classname.length();

        int size = ( size1 < size2 ? size1 : size2);

        char[] chars1 = ac1_classname.toCharArray();
        char[] chars2 = ac2_classname.toCharArray();

        char[] chars1_lowercase = ac1_classname.toLowerCase().toCharArray();
        char[] chars2_lowercase = ac2_classname.toLowerCase().toCharArray();

        for( int i = 0; i<size; i++ ){
            if ( chars1_lowercase[i] > chars2_lowercase[i] )
                return 1;
            else if ( chars1_lowercase[i] < chars2_lowercase[i] )
                return 0;
            else if ( chars1_lowercase[i] == chars2_lowercase[i] ){
                if( chars1[i] > chars2[i] )
                    return 1;
                else if( chars1[i] < chars2[i] )
                    return 0;

            }
        }

        if( size1 > size2 )
            return 1;
        else return 0;
    }
}

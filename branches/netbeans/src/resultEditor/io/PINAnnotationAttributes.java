/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.util.Vector;

/**
 *
 * @author Chris
 * /**temporary storage space of found PINS annotation information in the pins extraction.
 */
public
class PINAnnotationAttributes{
    protected static Vector<PINAnnotationAttribute> annotations = new Vector<PINAnnotationAttribute>();

    public static void clear(){
        annotations.clear();
    }

    public static int size(){
        return annotations.size();
    }

    /**
     * Return all annotations in static vector of "annotations" in this class.
     *
     * @return  All annotation which stored in static vector of "annotations" in this class.
     */
    static Vector<PINAnnotationAttribute> getAll(){
        return annotations;
    }

    /**
     * @param   annotationclass
     *          'Annotation class' / or called ' markables '.
     *
     */
    public static void add(String id, String annotatedclass, String INDEX_knowtator_mention_annotation,
            Vector<String> INDEXES_knowtator_slot_mention ){
        PINAnnotationAttribute pinannotation = get(id);
        if ( pinannotation == null ){
            PINAnnotationAttribute newpinannotation = new PINAnnotationAttribute();
            newpinannotation.id = id;
            newpinannotation.annotationclass = annotatedclass;
            newpinannotation.INDEX_knowtator_mention_annotation = INDEX_knowtator_mention_annotation;
            newpinannotation.INDEXES_knowtator_slot_mention = INDEXES_knowtator_slot_mention;
            annotations.add( newpinannotation );
        } else {
            pinannotation.annotationclass = annotatedclass;
            pinannotation.INDEX_knowtator_mention_annotation = INDEX_knowtator_mention_annotation;
            pinannotation.INDEXES_knowtator_slot_mention = INDEXES_knowtator_slot_mention;
        }
    }

    /**Get the annotation by given id*/
    public static PINAnnotationAttribute get(String id){
        if ( annotations == null )
            return null;

        for(PINAnnotationAttribute annotation: annotations ){
            if( annotation.id.trim().compareTo( id.trim() ) == 0 )
                return annotation;
        }

        return null;
    }
}
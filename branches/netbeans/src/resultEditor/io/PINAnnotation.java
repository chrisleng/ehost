/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.util.Vector;

/**
 *
 * @author Chris
 */
public /**details and example see document #2 @ LENG-4001 */
class PINAnnotation{
    public int uniqueIndex;
    public String id;

    // annotator related information
    public String index_to_knowtator_annotation_annotator; // old variable
    public String annotator_first_name, annotator_last_name;
    public String annotator_id;

    public String knowtator_annotation_creation_date;
    public int span_start, span_end;
    public String annotationtext, textsource;
    public String annotated_class;
    public String knowtator_mention_annotation;

    public String interSingle;
    public Vector<String> myLinks_byInterSingle = new Vector<String>();

    public Vector<resultEditor.annotations.AnnotationAttributeDef> simpleRelationships
            = new Vector<resultEditor.annotations.AnnotationAttributeDef>();
    public Vector<resultEditor.annotations.AnnotationRelationship> complexRelationships
            = new Vector<resultEditor.annotations.AnnotationRelationship>();

    /**
     * Constructor method
     */
    public PINAnnotation( String id, String index_to_knowtator_annotation_annotator,
            String knowtator_annotation_creation_date,
            int span_start, int span_end,
            String annotationtext, String textsource, String knowtator_mention_annotation){

        this.uniqueIndex = assignMeAUniqueIndex();

        this.id = id;
        this.index_to_knowtator_annotation_annotator = index_to_knowtator_annotation_annotator;
        this.knowtator_annotation_creation_date = knowtator_annotation_creation_date;
        this.span_start = span_start; this.span_end = span_end;
        this.annotationtext = annotationtext;
        this.textsource = textsource;
        this.knowtator_mention_annotation = knowtator_mention_annotation;
        //System.out.println(" 0 - index_to_knowtator_annotation_annotator = "  + index_to_knowtator_annotation_annotator );
    }

    private int assignMeAUniqueIndex(){
         return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }

    /**
     * Set the class name of this annotation.
     *
     * @param   classname
     *          The class name of current annotation.
     */
    void setclassname(String classname ){
        this.annotated_class = classname;
    }

    void setAnnotationMention(String knowtator_mention_annotation ){
        this.knowtator_mention_annotation = knowtator_mention_annotation;
    }

    /**
     * Tell this annotation that it has a new slot mention.
     *
     * @param   simplerelationship
     *          A simple relationship to this annotation
     */
    void setAnnotationSimpleSlots(resultEditor.annotations.AnnotationAttributeDef simplerelationship){
        simpleRelationships.add(simplerelationship);
    }

    void setAnnotationComplexSlot(String linkedAnnotationText, String slottype, int uniqueindex){
        resultEditor.annotations.AnnotationRelationship complexrelationship = new resultEditor.annotations.AnnotationRelationship(slottype);
        resultEditor.annotations.AnnotationRelationshipDef mycomplex = new resultEditor.annotations.AnnotationRelationshipDef();
        // the linked annotation text
        mycomplex.linkedAnnotationText = linkedAnnotationText ;
        mycomplex.linkedAnnotationIndex = uniqueindex;
        complexrelationship.addLinked( mycomplex );
        complexRelationships.add(complexrelationship);
    }


    void setAnnotationSimpleSlot(String slottype, String slotvalue){
        resultEditor.annotations.AnnotationAttributeDef attribute = new resultEditor.annotations.AnnotationAttributeDef();
        attribute.name = slottype;
        attribute.value = slotvalue;
        simpleRelationships.add(attribute);
    }


}
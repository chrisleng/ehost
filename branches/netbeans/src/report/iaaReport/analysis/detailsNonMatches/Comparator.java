/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.analysis.detailsNonMatches;

import resultEditor.annotations.*;
import userInterface.GUI;

/**
 *
 * @author leng
 */
public class Comparator {

    /**compare whether two annotations are same. It will compare their spans,
     * classes, attributes, and relationships. But will NOT compare their
     * annotators.
     *
     * @return  true
     *          if they have same spans, attributes, classes, and relationships.
     *          (IT WILL NOT CONSIDER THEIR ANNOTATORS)
     *
     *          false
     *          If they have different in spans, classes, attributes, or
     *          relationships.
     * 
     */
    public static boolean equalAnnotations(Annotation ann1, Annotation ann2) throws Exception{
        if( ! equalSpans(ann1, ann2) )
            return false;

        if( ! Comparator.equalAnnotator(ann1, ann2) )
            return false;

        if( ! Comparator.equalClasses(ann1, ann2) )
            return false;

        return true;
        
    }

    /***/
    public static boolean isSpanOverLap(Annotation annotation1,
            Annotation annotation2) throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            return false;
//            throw new Exception("~~~~error~~~~::1109011345::can not " +
//                    "compare span positions of two null annotations!");
        
        if( (annotation1.spanset.isEmpty()) || (annotation2.spanset.isEmpty()))
//            throw new Exception("~~~~error~~~~::1109011345::can not " +
//                    "compare span positions of two null annotations!");
            return false;
        
        return annotation1.spanset.isOverlapping( annotation2.spanset );

        
    }

    /**purpose is to compare comments between two annotations. Return true if we
     * didn't find any difference on comments; return false for finding different
     * comments.
     */
    public static boolean equalComments(Annotation annotation1, Annotation annotation2) throws Exception{
        if((annotation1==null)&&(annotation2==null))
            throw new Exception("1110300743::you can not compare two null annotations.");
        
        if((annotation1==null)&&(annotation2!=null))
            return false;
        if((annotation1!=null)&&(annotation2==null))
            return false;
        
        if((annotation1.comments==null)&&(annotation2.comments==null)){
            return true;
        }else if ((annotation1.comments==null)&&(annotation2.comments!=null)){
            if(annotation2.comments.trim().length()<1)
                return true; // equal if one is null and another one is zero length
            else 
                return false;
        }else if ((annotation1.comments!=null)&&(annotation2.comments==null)){
            if(annotation1.comments.trim().length()<1)
                return true; // equal if one is null and another one is zero length
            else 
                return false;
        }
        
        // if none of them are null, compare comment text directly
        int result = annotation1.comments.trim().compareTo(annotation2.comments.trim());
        if(result==0)
            return true;
        else 
            return false;
        
    }

    public static boolean equalRelationships(Annotation annotation1,
            Annotation annotation2, String filename) throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            throw new Exception("~~~~error~~~~::1109011613::can not " +
                    "compare complex relationships of two null annotations!");        


        // both complex relationships of these two annotations are null
        if( (annotation1.relationships == null)
           &&(annotation2.relationships == null))
            return true;

        // if only one of them doesn't have complex relationship
        if( (annotation1.relationships == null)
           &&(annotation2.relationships != null))
        {
            // if attributes of annoataion 1 is "NULL"
            // and attributes of annotation2 is "empty"
            //  they are same
            if(annotation2.relationships.size()<1)
                return true;
            else
                return false;
        }

        if( (annotation1.relationships != null)
           &&(annotation2.relationships == null))
        {
            // if attributes of annoataion 1 is "NULL"
            // and attributes of annotation2 is "empty"
            //  they are same
            if(annotation1.relationships.size()<1)
                return true;
            else
                return false;
        }

        // consider they are NOT same if the numbers of their normal relationship
        // items are NOT equal
        if ( annotation1.relationships.size()
                != annotation2.relationships.size() )
            return false;

        // compare each item of them
        for( AnnotationRelationship cr1: annotation1.relationships )
        {
            if( cr1==null)
                continue;

            boolean foundMatchedNR = false;
            for( AnnotationRelationship cr2: annotation2.relationships )
            {
                if( cr2==null)
                    continue;

                if(compare2ComplexRelationship(cr1, cr2, filename))
                {
                    foundMatchedNR = true;
                    break;
                }

            }

            if(foundMatchedNR == false)
                return false;
        }


        return true;

    }

    /**check whether two given annotation terms have same atributes.
     */
    public static boolean equalAttributes(Annotation annotation1,
            Annotation annotation2) throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            throw new Exception("~~~~error~~~~::1108261613::fail " +
                    "to comnpare attributes of two annoation");

        // both attributes of these two annotations are null
        if( (annotation1.attributes == null)
           &&(annotation2.attributes == null))
            return true;

        // if only one of them doesn't have attribute
        if( (annotation1.attributes == null)
           &&(annotation2.attributes != null))
        {
            // if attributes of annoataion 1 is "NULL"
            // and attributes of annotation2 is "empty"
            //  they are same
            if(annotation2.attributes.size()<1)
                return true;
            else
                return false;
        }

        if( (annotation1.attributes != null)
           &&(annotation2.attributes == null))
        {
            // if attributes of annoataion 1 is "NULL"
            // and attributes of annotation2 is "empty"
            //  they are same
            if(annotation1.attributes.size()<1)
                return true;
            else
                return false;
        }

        annotation1.cleanAttributes();
        annotation2.cleanAttributes();

        // consider they are NOT same if the numbers of their normal relationship
        // items are NOT equal
        if ( annotation1.attributes.size() !=
                annotation2.attributes.size() ){
                //System.out.println("miss matching length of vectors:" + annotation1.normalrelationships.size() + ", " + annotation2.normalrelationships.size());
            return false;
        }

        // compare each item of them
        for( AnnotationAttributeDef nr1: annotation1.attributes )
        {
            if( nr1==null)
                continue;

            boolean foundMatchedNR = false;
            for( AnnotationAttributeDef nr2: annotation2.attributes )
            {
                if( nr2==null)
                    continue;

                if(compare2NormalRelationship(nr1, nr2))
                {
                    foundMatchedNR = true;
                    break;
                }

            }

            if(foundMatchedNR == false)
                return false;
        }

        return true;
    }


    private static boolean compare2ComplexRelationship(
            AnnotationRelationship cr1,
            AnnotationRelationship cr2,
            String filename)
            throws Exception
    {

        // same if both of them are null
        if((cr1==null)&&(cr2==null))
            return true;

        // different if only one of them are null
        if((cr1==null)&&(cr2!=null)){
            //System.out.println("cr2==null");
            return false;
        }

        if((cr1!=null)&&(cr2==null)){
            //System.out.println("cr2==null");
            return false;
        }


        if((cr1.mentionSlotID==null)||(cr2.mentionSlotID==null))
            throw new Exception("1108301123::relationship name can not be null.");

        // not equal if they have different relationship name
        if(cr1.mentionSlotID.trim().compareTo(cr2.mentionSlotID.trim())!=0)
            return false;

        // #### until now, above operations indicates these two normal relationships
        //      have same attributes name, so what we need to continue is to
        //      compare their values to detemine whether they are same or not.

        // return true(same) if they all have null values
        if((cr1.linkedAnnotations == null)&&(cr2.linkedAnnotations==null))
            return true;

        // return false(they are different if only one of them is null vales)
        if((cr1.linkedAnnotations == null)||(cr2.linkedAnnotations==null))
            return false;

        // they are different if have different numbers of values
        if((cr1.linkedAnnotations.size() != cr2.linkedAnnotations.size()))
            return false;

        
        Depot depot = new Depot();
        //Depot depot = new Depot();
        // to each option value in this relationship, we compare it to
        // all option values in another relationship. we return false
        // only if one option value in the source relationship could not found
        // matched value in another one's.
        try{
            for(AnnotationRelationshipDef value1: cr1.linkedAnnotations)
            {
                if(value1==null)
                    continue;

                boolean flag = false;
                for(AnnotationRelationshipDef value2: cr2.linkedAnnotations)
                {
                    if(value2==null)
                       continue;

                    if(value1.linkedAnnotationIndex == value2.linkedAnnotationIndex )
                    {
                        flag = true;
                        break;
                    }else{
                        
                        Annotation a1 = null;
                        Annotation a2 = null;
                        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                            adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
                            a1 = depotOfAdj.getAnnotationByUnique(filename, value1.linkedAnnotationIndex );
                            a2 = depotOfAdj.getAnnotationByUnique(filename, value2.linkedAnnotationIndex );
                        }else{
                            a1 = depot.getAnnotationByUnique(filename, value1.linkedAnnotationIndex );
                            a2 = depot.getAnnotationByUnique(filename, value2.linkedAnnotationIndex );
                        }
                        
                        
                        if(( a1.spanset.isDuplicates( a2.spanset ) )
                        &&( a1.annotationclass.compareTo( a2.annotationclass ) == 0 )){
                            flag = true;
                            break;
                        }
                    }
                }

                if(flag==false)
                    return false;
            }
        }catch(Exception ex){
            throw new Exception(
                    "1109011057::fail to compare two normal relationship Values!" +
                    "\nError Details: " + ex.getMessage());
        }

        return true;
    }

    /***/
    private static boolean compare2NormalRelationship(
            AnnotationAttributeDef nr1,
            AnnotationAttributeDef nr2)
            throws Exception
    {

        // same if both of them are null
        if((nr1==null)&&(nr2==null))
            return true;

        // different if only one of them are null
        if((nr1==null)&&(nr2!=null))
            return false;

        if((nr1!=null)&&(nr2==null))
            return false;


        if((nr1.name==null)||(nr2.name==null))
            throw new Exception("1108301123::relationship name can not be null.");

        // not equal if they have different relationship name
        if(nr1.name.trim().compareTo(nr2.name.trim())!=0)
            return false;

        // #### until now, above operations indicates these two normal relationships
        //      have same attributes name, so what we need to continue is to
        //      compare their values to detemine whether they are same or not.

        // return true(same) if they all have null values
        if((nr1.value == null)&&(nr2.value==null))
            return true;

        // return false(they are different if only one of them is null vales)
        if((nr1.value == null)||(nr2.value==null))
            return false;

        // they are different if have different numbers of values
        if( nr1.value.trim().compareTo( nr2.value.trim()) != 0 )
            return false;
        

        return true;
    }


    /**if two annotation have same annotation span, return true*/
    public static boolean equalSpans(
            Annotation annotation1, Annotation annotation2)
            throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            return false;

        if( (annotation1.spanset.isEmpty()) || (annotation2.spanset.isEmpty()))
            return false;
        
        return annotation1.spanset.isDuplicates( annotation2.spanset );
        
    }

    /**if two annotation have same annotation classes, return true*/
    public static boolean equalClasses(
            Annotation annotation1, Annotation annotation2)
            throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            throw new Exception("~~~~error~~~~::1108301532::fail to " +
                    "compare classes of two annotations");

        if( (annotation1.annotationclass==null) || (annotation2.annotationclass==null))
            throw new Exception("~~~~error~~~~::1108301533::fail to " +
                    "compare classes of two annotations");

        if( annotation1.annotationclass.trim().compareTo(
                annotation2.annotationclass.trim() ) ==0 )
            return true;
        else
            return false;
    }

    /**check whether two annotations belong to same annotator*/
    public static boolean equalAnnotator(
            Annotation annotation1, Annotation annotation2)
            throws Exception
    {
        if( (annotation1==null) || (annotation2==null))
            throw new Exception("~~~~error~~~~::1108301609::fail to " +
                    "determin whether two annotations belong to same annotator.");

        if( (annotation1.getAnnotator()==null) || (annotation2.getAnnotator()==null))
            throw new Exception("~~~~error~~~~::1108301610::fail to determin " +
                    "whether two annotations belong to same annotator.");

        if( annotation1.getAnnotator().trim().compareTo( (annotation2.getAnnotator().trim() ))==0 )
            return true;
        else
            return false;
    }

    public static boolean checkAnnotator(final Annotation annotation, final String annotator) throws Exception{
        if(annotation==null)
            throw new Exception("1109020359");
        if(annotation.getAnnotator() ==null)
            throw new Exception("1109020358");
        if(annotator==null)
            throw new Exception("1109020357");
        if(annotator.trim().length()<1)
            throw new Exception("1109020356");

        if(annotation.getAnnotator().trim().compareTo(annotator.trim())==0)
            return true;
        else
            return false;

    }


}

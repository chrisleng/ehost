/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author Chris
 */
public class PINSAnnotationDepot{

    protected static Vector<PINAnnotation> details = new Vector<PINAnnotation>();
    static int size(){
        return details.size();
    }

    static Vector<PINAnnotation> getAll(){
        return details;
    }

    static void clear(){
        details.clear();
    }

    static void add(String id, String index_to_knowtator_annotation_annotator,
            String knowtator_annotation_creation_date,
            int span_start, int span_end,
            String annotationtext,
            // two types: 1: filename.txt 2: filename without extension name
            final String textsource,

            String knowtator_mention_annotation){

        // specifical processing
        String textsource_filename = textsource.trim();
        if(!textsource.contains("."))
            textsource_filename = textsource_filename + ".txt";
        

        PINAnnotation pa = new PINAnnotation(
                id,
                index_to_knowtator_annotation_annotator,
                knowtator_annotation_creation_date,
                span_start, span_end,
                annotationtext,
                textsource_filename,
                knowtator_mention_annotation);

        pa.interSingle = "PIN"+ PinFile.givemeLatestIndex();
        
        details.add( pa );
    }

    public static String linkToAnotherAnnotation( String StringIndex, String friendsUniqueStringIndex ){
        for( PINAnnotation annotation : details ){
            //System.out.print("\n\n    + " + annotation.id.trim() + "]");
            //System.out.print("\n    + " + StringIndex.trim() + "]");
            if( annotation.knowtator_mention_annotation.trim().compareTo( StringIndex.trim() ) ==0 ){
                annotation.myLinks_byInterSingle.add( friendsUniqueStringIndex );
                return annotation.annotationtext;
            }
        }
        log.LoggingToFile.log(Level.SEVERE, "1008021826:: fail to store complex relationships"
                + "\n    - from annotation [" + StringIndex + "]"
                + "\n   - to annotatoin " + friendsUniqueStringIndex + "(unique string index) " 
                + "\n   - while doing a PINS input!!!");

        return null;
    }

    public static int linkToAnotherAnnotation( String StringIndex){
        for( PINAnnotation annotation : details ){
            //System.out.print("\n\n    + " + annotation.id.trim() + "]");
            //System.out.print("\n    + " + StringIndex.trim() + "]");
            if( annotation.knowtator_mention_annotation.trim().compareTo( StringIndex.trim() ) ==0 ){
                return annotation.uniqueIndex;
            }
        }
        log.LoggingToFile.log(Level.SEVERE, "1008021826:: fail to store complex relationships"
                + "\n    - from annotation [" + StringIndex + "]"
                + "\n   - while doing a PINS input!!!");

        return -1;
    }


}
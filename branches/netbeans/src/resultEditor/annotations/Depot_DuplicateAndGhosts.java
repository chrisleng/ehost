/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations;

import java.util.Vector;

/**
 *This is a memory space used to record annotations, which are found and
 * will be deleted by method of "removeDuplicates."
 *
 * This structure is only used in method "removeDuplicates".
 * @author Chris
 */
public class Depot_DuplicateAndGhosts {
    /**This is a memory space used to record annotations, which are found and
     * will be deleted by method of "removeDuplicates."
     *
     * This structure is only used in method "removeDuplicates".
     */
    private static Vector<DupilcateAndGhost> caughtDuplicatesAndGhosts = new Vector<DupilcateAndGhost>();

    public static void clear(){
        caughtDuplicatesAndGhosts.clear();
    }

    /**return all found duplicates*/
    public static final Vector<DupilcateAndGhost> get(){
        return caughtDuplicatesAndGhosts;
    }

    public static final DupilcateAndGhost get(int index){
        if((index<0)||(index>=caughtDuplicatesAndGhosts.size()))
            return null;

        return caughtDuplicatesAndGhosts.get(index);

    }

    /**check whether an annotation has been recorded as reference annotation*/
    public static boolean isAnnotationInList_asReference(Annotation _ann, String _filename){

        if((_ann==null)||(_filename==null)||(_filename.trim().length()<1))
            return true;


        if(_ann.annotationText==null)
            return true;
        if( _ann.spanset == null )
            return true;

        for(DupilcateAndGhost record : caughtDuplicatesAndGhosts)
        {
            if (record==null)
                continue;

            if(record.filename.trim().compareTo(_filename.trim())==0){
                if( _ann.spanset.isDuplicates( record.spanset ) )
                    return true;
            }
        }

        return false;
    }

    /**add new duplicates*/
    public static void addDuplicates(String _filename, Annotation _referenceAnnotation, Annotation _duplicate)
    {

        if((_filename==null)||(_filename.trim().length()<1))
            return;

        if((_referenceAnnotation==null)||(_duplicate==null))
        {
            return;
        }

        boolean found = false;

        for(DupilcateAndGhost record : caughtDuplicatesAndGhosts)
        {
            if(record==null)
                continue;

            if( (record.referenceAnnotation.spanset.isDuplicates( _referenceAnnotation.spanset ) )                
                &&(record.filename.trim().compareTo( _filename.trim() )==0))
            {


                found = true;

                if(record.duplicates.size()<1)
                {
                    // record
                    record.duplicates.add(_duplicate);
                }else{
                    record.duplicates.add(_duplicate);
                }
            }
        }
        
        if(!found){
            DupilcateAndGhost record = new DupilcateAndGhost();
            record.addDuplicate(_filename, _referenceAnnotation, _duplicate);
            caughtDuplicatesAndGhosts.add(record);
        }


    }

    public static void addClassless(String _filename, Annotation ann) {
        if(ann==null)
            return;
        DupilcateAndGhost record = new DupilcateAndGhost();
        record.addClassless(_filename, ann);
        caughtDuplicatesAndGhosts.add(record);

    }

    public static void addOutOfRange(String _filename, Annotation ann) {
        if(ann==null)
            return;
        DupilcateAndGhost record = new DupilcateAndGhost();
        record.addOutOfRange(_filename, ann);
        caughtDuplicatesAndGhosts.add(record);
    }

    public static void addSpanless(String _filename, Annotation ann) {
        if(ann==null)
            return;
        DupilcateAndGhost record = new DupilcateAndGhost();
        record.addSpanless(_filename, ann);
        caughtDuplicatesAndGhosts.add(record);
    }




}

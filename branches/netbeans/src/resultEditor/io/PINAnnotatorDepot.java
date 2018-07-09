/*
 * Temporary storage space of found annotator information in pins extractor.
 */

package resultEditor.io;

import java.util.Vector;


/**
 * Temporary storage space of found annotator information in pins extractor.
 *
 * @author  Jianwei ‘Chris’ Leng, @ Sat Oct 09 16:47:27 MDT 2010
 *          Division of Epidemiology, School of Medicine, University of Utah
 */
public class PINAnnotatorDepot {

    protected static Vector<PINAnnotator> PIN_annotators_depot = new Vector<PINAnnotator>();

    public static void clear(){
        PIN_annotators_depot.clear();
    }

    /**
     * Save found annotator information for imported annotations from PINS file.
     * 
     * @param   id
     *          the identification string which called "title" of the block from
     *          the pins extractor. It MUST appreared here. But to other 3 parameters,
     *          at least one of them need to be NOT NULL.
     * 
     *
     */
    public static void add(
            String id,
            String annotator_first_name,
            String annotator_last_name,
            String annotatorid )
    {

        if ( id == null )
            return;

        
        PIN_annotators_depot.add(
                new PINAnnotator( id, annotator_first_name,
                    annotator_last_name, annotatorid )
                );
    }
}

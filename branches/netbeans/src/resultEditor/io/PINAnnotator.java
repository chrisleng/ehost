/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

/**
 *
 * @author Chris
 */
/**structure of annotator in PINS extractor.*/
public class PINAnnotator{

    public String id;
    public String annotator_first_name;
    public String annotator_last_name;
    public String annotatorid;

    /**Class Contructor*/
    public PINAnnotator( String id, String annotator_first_name, String annotator_last_name, String annotatorid ){
        this.id = id;
        this.annotator_first_name = annotator_first_name;
        this.annotator_last_name  = annotator_last_name;
        this.annotatorid = annotatorid;
    }
}

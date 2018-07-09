/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imports.importedXML;

import java.util.ArrayList;
import java.util.Vector;
import resultEditor.annotations.AnnotationAttributeDef;

/**
 *
 * @author Chris
 */
public class eComplexSlotMention {
    public String complexSlotMention;
    public String mentionSLot_id;
    public Vector<String> complexSlotMentionValue_value = new Vector<String>();
    
    /**attributes on relationship*/
    public ArrayList<AnnotationAttributeDef> attributes = new ArrayList<AnnotationAttributeDef>();
}

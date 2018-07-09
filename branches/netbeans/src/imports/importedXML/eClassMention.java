/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package imports.importedXML;

import java.util.Vector;

/**
 *
 * @author Chris
 */
public class eClassMention {
    public String classMentionID;
    public String mentionClassID;
    public String mentionClassText;
    public Vector<String> hasSlotMention_id
            = new Vector<String>();
    public eClassMention(){}
    public eClassMention(String classMentionID, String mentionClassID, String mentionClassText)
    {
        this.classMentionID = classMentionID;
        this.mentionClassID = mentionClassID;
        this.mentionClassText = mentionClassText;
    }
}

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
public class ComplexSlots{
    public static Vector<ComplexSlot> slots = new Vector<ComplexSlot>();

    public static void addSlot(String id,
            String knowtator_mention_slot,
            Vector<String> knowtator_mention_slot_value,  // index to other annotation
            String knowtator_mentioned_in){

        ComplexSlot slot = new ComplexSlot();
        // validity check
        if (( id  == null )||(id.trim().length() < 1))
            return;
        if ( (knowtator_mention_slot==null)||(knowtator_mention_slot.trim().length() < 1) )
            return;

        slot.id = id.trim();
        slot.knowtator_mention_slot = knowtator_mention_slot.trim();
        slot.knowtator_mention_slot_value = knowtator_mention_slot_value;
        slot.knowtator_mentioned_in = knowtator_mentioned_in;

        slots.add(slot);
    }

    public static ComplexSlot getSlot(String id){
        if ( id == null )
            return null;
        if ( id.trim().length() < 1 )
            return null;

        for( ComplexSlot thisslot : slots ){
            if (thisslot == null)
                continue;
            if ( thisslot.id.trim().compareTo( id.trim() ) == 0 )
                return thisslot;
        }

        return null;
    }

    public static void clear(){
        slots.clear();
    }
}
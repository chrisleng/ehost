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
public class SimpleSlotDepot{
    public static Vector<SimpleSlot> slots = new Vector<SimpleSlot>();

    public static void addSlot(String id,
            String knowtator_mention_slot,
            String knowtator_mention_slot_value,
            String knowtator_mentioned_in){

        SimpleSlot slot = new SimpleSlot();
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

    public static SimpleSlot getSlot(String id){
        if ( id == null )
            return null;
        if ( id.trim().length() < 1 )
            return null;

        for( SimpleSlot thisslot : slots ){
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
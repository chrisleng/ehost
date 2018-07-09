/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eHOSTEvents;

import java.util.EventObject;
/**
 *
 * @author leng
 */



public class EHOSTEVENT extends EventObject {
    
    private int EventNo = 0;

    public EHOSTEVENT(Object source, int event) {
        super(source);
        this.EventNo = event;
    }

    public void setEvent(int event) {
        this.EventNo = event;
    }

    public int getEvent() {
        return this.EventNo;
    }
}


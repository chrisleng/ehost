/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eHOSTEvents;

import java.util.EventListener;
/**
 *
 * @author leng
 */
public interface EHOSTEventListener extends EventListener {
    public void doorEvent(EHOSTEVENT event, userInterface.GUI gui);
}



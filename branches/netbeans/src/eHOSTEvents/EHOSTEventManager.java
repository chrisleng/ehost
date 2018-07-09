/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eHOSTEvents;
import java.util.*;
/**
 *
 * @author leng
 */
public class EHOSTEventManager
{
    private Collection listeners;
    /**
     * 添加事件
     * @param listener DoorListener
     */
    public void addDoorListener(EHOSTEventListener listener) {
        if (listeners == null) {
            listeners = new HashSet();
        }
        listeners.add(listener);
    }

    /**
     * 移除事件
     * @param listener DoorListener
     */
    public void removeDoorListener(EHOSTEventListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }

    /**
     * 触发开门事件
     */
    public void openTab_workspace(userInterface.GUI gui) {
        if (listeners == null)
            return;
        EHOSTEVENT event = new EHOSTEVENT(this, 1);
        notifyListeners(event, gui);
    }

    /**
     * 触发关门事件
     */
    public void openTab_markables(userInterface.GUI gui) {
        if (listeners == null)
            return;
        EHOSTEVENT event = new EHOSTEVENT(this, 2);
        notifyListeners(event, gui);
    }

    /**
     * 通知所有的DoorListener
     */
    private void notifyListeners(EHOSTEVENT event, userInterface.GUI gui) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            EHOSTEventListener listener = (EHOSTEventListener) iter.next();
            listener.doorEvent(event, gui);
        }
    }

}


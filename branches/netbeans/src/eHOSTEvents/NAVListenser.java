/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eHOSTEvents;

/**
 *
 * @author leng
 */
public class NAVListenser implements EHOSTEventListener
{

    public void doorEvent(EHOSTEVENT event, userInterface.GUI gui )
    {
        if(event.getEvent()==1)
        {
                gui.setNAVonFirstOrSecondPage();
        }
        else if(event.getEvent()==2)
        {
              gui.setNAVonThirdPage();
        }
    }
}


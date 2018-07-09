/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package commons;

import java.awt.Toolkit;

/**
 * This class is designed to offer some fresequencely used tool methods to help
 * eHOST programming. For example, let PC speaker beeping, or get current time
 * stamp.
 * 
 * @author Jianwei Chris Leng, Jan 4, 2010
 */
public class Tools {

    /**Let your PC computer beeping once.*/
    public static void beep(){
        Toolkit.getDefaultToolkit().beep();
    }

    /**Get current time stamp from system.*/
    public static String getTimeStamper(){
        return String.valueOf( System.currentTimeMillis() );
    }

}

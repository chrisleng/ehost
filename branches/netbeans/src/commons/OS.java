/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package commons;

import java.util.Calendar;

/**
 *
 * @author Leng
 */
public class OS {
        public static boolean isMacOS(){
                // if its Mac os, it show "Mac OS X"
                boolean os = getOSType().startsWith("Mac");
                return os;
        }

        private static String getOSType(){
                String OS;
                OS = System.getProperty("os.name");
                return OS;
        }
            // get current date and time
    public static String getCurrentDate(){
            Calendar rightnow = Calendar.getInstance();
            String CurrentDate = rightnow.getTime().toString();
            //System.out.println(CurrentDate);
            return CurrentDate;
    }
}

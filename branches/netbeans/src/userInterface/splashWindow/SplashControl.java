/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.splashWindow;

/**
 *
 * @author leng
 */
public class SplashControl {
    private static GSplashWindow splashwindow = null;

    public static void start_splashWindow(){
        splashwindow = new GSplashWindow();
        splashwindow.start();
    }

    public static void end_splashWindow(){
        if (splashwindow != null){
            try {
                Thread.sleep(12000);
            } catch (Exception ex) {

            }
            splashwindow.stop();
        }
    }

}

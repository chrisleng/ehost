/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.splashWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 *
 * @author leng
 */
public class SplashController {

    static SplashScreen splash =null;
    static Graphics2D m_splash_g = null;
    static java.net.URL url_of_bgimg = null;

    /*public static void initSplash() {
        try {
            //url_of_bgimg = new URL("/UserInterface/splashWindow/splash.png");
            //getClass().getResource("/UserInterface/splashWindow/splash.png").toURI().toURL();
            //m_splash.setImageURL(url_of_bgimg);
            m_splash = SplashScreen.getSplashScreen();

            System.out.println("##" + m_splash.getImageURL());
            if (m_splash != null) {

                System.out.println("##" + m_splash.getImageURL());
                m_splash_g = m_splash.createGraphics();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public static void closeSplash() {

        if (m_splash!=null){

            m_splash.close();

        }

    }

    private static void renderSplashFrame(Graphics2D g, String msg) {

        //g.setComposite(AlphaComposite.Clear);

        g.fillRect(40, 240, 200,40);

        g.setPaintMode();

        g.setColor(Color.BLACK);

        g.drawString(msg, 40, 250);

    }

    public static void setSplashMessage(String msg) {

        if ( m_splash_g!=null ) {

            renderSplashFrame(m_splash_g, msg);

        }

        if ( m_splash!=null ) {

            m_splash.update();

        }

    }
     *
     */

    public static void start(){
        splash = SplashScreen.getSplashScreen();
        if( SplashScreen.getSplashScreen() == null ) {
            return;
        }

        FileInputStream fis;
        Scanner scanner;
        final Graphics2D g2 = splash.createGraphics();
        final Dimension size = splash.getSize();

          
        try {
            drawInfo("Launching eHOST program ...", g2, size);
            //initAndRecLog();
            fis = new FileInputStream("eHOST_start.log");

            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                    str="launching info::"+str;
                    drawInfo(str, g2, size);
                    Thread.sleep(800);
            }
            //fis.close();
            scanner.close();

            Thread.sleep(3000); // SplashScreen delay time.
        }catch(Exception e){}
    }

    public static void showtext(String text){
        try {
            if (splash == null) {
                return;
            }
            final Graphics2D g2 = splash.createGraphics();
            final Dimension size = splash.getSize();
            drawInfo(text, g2, size);
            Thread.sleep(800);
        } catch (Exception ex) {

        }
    }

    private static void drawInfo(String info, Graphics2D g2, Dimension size) {
        g2.setColor(new Color(223,224,224));
        g2.fillRect(10, size.height - 30, size.width - 30, 20);
        g2.setColor(new Color(132,132,132));
        g2.setFont(new Font("Courier", Font.PLAIN, 13));
        g2.drawString(info, 20, size.height - 15);
        splash.update();
    }
}

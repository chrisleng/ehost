/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package userInterface.splashWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;


/**
 * Splash Window
 *
 * @author Jianwei 'chris' Leng, Division of Epidemiology, School of Medicine, U o U
 */
public class GSplashWindow extends JWindow {

    /** label component used to show current status information */
    JLabel status = null;

    /** background image */
    private Image bgImage = null;

    /**
     * Construct the start window
     */
    public GSplashWindow() {
        
        try{
            this.bgImage = new ImageIcon( getClass().getResource("/UserInterface/splashWindow/splash.png") ).getImage();
        }catch(Exception ex){
            // Ignore errors here
        }
        initComponents();
    }

    /**
     * 通过图像对象构造启动画面
     *
     * @param bgImage 背景图像对象
     */
    public GSplashWindow(Image bgImage) {
        this.bgImage = bgImage;
        initComponents();
    }

    private void initComponents() {
        // 获取图片尺寸
        int imgWidth = bgImage.getWidth(this);
        int imgHeight = bgImage.getHeight(this);

        // 设置窗口大小
        setSize(imgWidth, imgHeight);

        // 设置窗口背景
        JPanel background = new JPanel() {
            protected void paintChildren(Graphics g) {
                g.drawImage(bgImage, 0, 0, this);
                super.paintChildren(g);
            }
        };
        background.setLayout(new BorderLayout());
        setContentPane(background);

        // 设置窗口位置
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scmSize = toolkit.getScreenSize();
        setLocation(scmSize.width / 2 - imgWidth / 2,
                scmSize.height / 2 - imgHeight / 2);

        // 加入状态条
        status = new JLabel("状态条..........", JLabel.RIGHT);
        status.setForeground(Color.WHITE);
        status.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        getContentPane().add(status, "South");
    }

    public void start() {
        setVisible(true);
        toFront();
    }

    /**
     * 设置状态信息
     *
     * @param statusText 状态信息
     */
    public void setStatus(String statusText) {
        status.setText(statusText);
    }

    public void stop() {
        setVisible(false);
        dispose();
    }
}






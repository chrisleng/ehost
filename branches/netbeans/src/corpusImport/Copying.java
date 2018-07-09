/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Copying.java
 *
 * Created on Jan 28, 2011, 9:43:26 AM
 */

package corpusImport;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author leng
 */
public class Copying extends javax.swing.JFrame {

    private userInterface.GUI __gui;
    protected File[] selectedfiles;
    protected File toproject;

    /** Creates new form Copying */
    public Copying(userInterface.GUI gui, final File[] _selectedfiles, final File _toproject) {
        this.__gui = gui;
        this.selectedfiles = _selectedfiles;
        this.toproject = _toproject;
        initComponents();

        setDialogPosition();

        new Thread(){
                @Override
                public void run(){
                    copyfiles(_selectedfiles, _toproject);
                }
        }.start();

        
    }

    /**copy files to current project*/
    private void copyfiles(File[] selected, File _toProject ){

        if( _toProject == null ){
            this.dispose();
            return;
        }

        File[] subfiles = _toProject.listFiles();
        if(subfiles==null)
        {
            this.dispose();
            return;
        }

        // init dialog text information
        jLabel6.setText("Copying:");
        jLabel9.setText("0 files completed,");
        int number_of_object_files = selected.length;        
        jLabel10.setText( number_of_object_files + " files left ... ...");
        jLabel10.repaint();

        // check for the folder of "corpus"
        boolean foundFolderOfCorpus=false;
        File corpus=null;
        for(File subfile:subfiles){
            if(subfile==null)
                continue;
            if(subfile.isDirectory()){
                if(subfile.getName().compareTo(env.CONSTANTS.corpus)==0)
                {
                    corpus = subfile;
                    foundFolderOfCorpus = true;
                    break;
                }
            }
        }

        if(!foundFolderOfCorpus)
        {
            String projectpath = _toProject.getAbsolutePath();
            projectpath = projectpath + File.separator + "corpus";
            corpus = new File(projectpath);
            corpus.mkdirs();            
        }

        if(selected==null)
        {
            this.dispose();
        }

        jProgressBar1.setMaximum(selected.length);
        jProgressBar1.setMinimum(0);
        int i=0;
        jProgressBar1.setValue(i);

        // copy each file in the give list to "corpus" folder of current project
        for(File file :selected )
        {

            try{
                jLabel10.setText( String.valueOf(number_of_object_files -i) + " files left ... ...");
                jLabel10.repaint();
                jLabel6.setText("Copying: " + file.getAbsolutePath());
                jLabel6.repaint();
                jLabel9.setText( i + " files completed,");
                jLabel9.repaint();
                jProgressBar1.setValue(i);
                jProgressBar1.repaint();
                jProgressBar1.updateUI();
            }catch(Exception ex){
                
            }

           
            
            //try {
            //    Thread.sleep(400);
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(Copying.class.getName()).log(Level.SEVERE, null, ex);
            //}

            String name = file.getName();
            

            // remove stange character from name of document
            /*int nameLength = name.length();
            char[] nameCharArray = name.toCharArray();
            for(int j=0;j<nameLength;j++){
                
                // ASCII 45: - hyphen
                // ASCII 46: . period
                if(( name.charAt(j)  == 45 )||( name.charAt(j)  == 46 )) {
                    continue;
                }

                if(( name.charAt(j)  >= 48 )&&( name.charAt(j)<=57)){
                    continue;
                }

                if(( name.charAt(j)  >= 65 )&&( name.charAt(j)<=122)){
                    continue;
                }

                nameCharArray[j] = '_';
                
            }
            
            name = String.copyValueOf(nameCharArray, 0, nameLength);
            */name = corpus.getAbsolutePath() + File.separator + name;
            System.out.println("copy to ---- "+name);

            File output = new File(name);
            forTransfer(file, output);
            

            //DEBUG
            //System.out.println("filter out special character - begin");
            removeSpecialCharacters(output);
            //DEBUG//
            System.out.println("filter out special character - end");
            i++;
        }

        jLabel6.setText("Completed!");
        jLabel8.setText(" - ");
        jLabel10.setText( "0 file left ... ...");
        jProgressBar1.setValue(selected.length);
        jLabel9.setText( i + " files completed,");
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Copying.class.getName()).log(Level.SEVERE, null, ex);
        }

        __gui.refreshFileList();


        if(jCheckBox1.isSelected())
            this.dispose();

        
    }

    private void setDialogPosition(){
        int width = 580, height = 311;
        this.setPreferredSize(new Dimension(width, height));
        int parentX = __gui.getX(), parentY = __gui.getY();
        int parentWidth = __gui.getWidth(), parentHeight = __gui.getHeight();
        int x = parentX + (int)(parentWidth-width)/2;
        int y = parentY + (int)(parentHeight-height)/2;
        this.setLocation(x, y);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jCheckBox1 = new javax.swing.JCheckBox();
        Close = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Copy selected corpus to your project");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(238, 238, 239));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(238, 238, 240));

        jProgressBar1.setBackground(new java.awt.Color(238, 238, 240));

        jCheckBox1.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Close dialog after these files are added");

        Close.setText("Close");
        Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 356, Short.MAX_VALUE)
                .add(Close))
            .add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(2, 2, 2)
                        .add(jCheckBox1)
                        .addContainerGap(31, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(Close))))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(238, 238, 239));

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 12));
        jLabel6.setForeground(new java.awt.Color(0, 51, 102));
        jLabel6.setText("Copying:");

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 12));
        jLabel7.setForeground(new java.awt.Color(0, 51, 102));
        jLabel7.setText("Status:");

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 12));
        jLabel8.setText("Copys 1 files,");

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 12));
        jLabel9.setText("0 files completed,");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 12));
        jLabel10.setText("1 files left ... ... ");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jLabel9)
                            .add(jLabel10))))
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel8)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_CloseActionPerformed

    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Copying().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Close;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    protected static javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables


    public static void removeSpecialCharacters(File file){
        if(file==null){
            log.LoggingToFile.log(Level.WARNING, "~~~~ WARNING ~~~~:: 1107201124: can not copy a "
                    + "NULL file to your current project.");            
            return;
        }

        if(!file.exists()){
            log.LoggingToFile.log(Level.WARNING,"~~~~ WARNING ~~~~:: 1107201125: can not found "
                    + "document:" + file.getAbsolutePath());
            return;
        }
        
        // print info on screen
        try{


                // load contents from document
                BufferedReader infile = new BufferedReader( new FileReader( file ));
                String inline = infile.readLine();

                String tmp = file.getAbsolutePath()+".tmp";
                FileOutputStream outputstream = new FileOutputStream(tmp);
                PrintStream p = new PrintStream(outputstream);




                while( inline != null ){
                    //if(inline.contains("On this echocardiogram , he was again"))
                    //    System.out.println("hello");

                        
                    //System.out.println("inline~~~~"+ inline);

                    if(inline.length()<1){
                        p.println(inline);
                        inline = infile.readLine();
                        continue;
                    }

                    if(inline.trim().length()<1){
                        p.println(inline);
                        inline = infile.readLine();
                        continue;
                    }

                    //int specialcharacternumbers = 0;
                    char[] linetoCharArray = inline.toCharArray();
                    if((linetoCharArray==null)||(linetoCharArray.length<1)){
                        
                        p.println(inline);
                        inline = infile.readLine();
                        continue;
                     }

                    int size = linetoCharArray.length;

                    for(int i=0;i<size;i++)
                    {

                        int thischar = Integer.valueOf( linetoCharArray[i] );
                        if (thischar <= 4)
                        {
                            continue;
                        }

                        if( (thischar <= 126 )&&(thischar>=32))
                        {
                            continue;
                        }

                        if( (thischar >= 1 )&&(thischar>=31))
                        {
                            continue;
                        }

                        log.LoggingToFile.log(Level.WARNING,"~~~~ INFO ~~~~::found special character=["
                                    +Integer.valueOf( linetoCharArray[i] )
                                    +"] in file ["
                                    + file.getName()
                                    + "]");
                            // linetoCharArray[i]= '\u0000';
                        linetoCharArray[i]= '\u0020';
                            //specialcharacternumbers++;
                        
                    }
                    
                    p.println(String.valueOf(linetoCharArray, 0, size));
                    inline = infile.readLine();
                }

                infile.close();
                p.close();

                file.delete();
                File tmpfile = new File(tmp);
                tmpfile.renameTo(new File(tmp.substring(0, (tmp.length()-4))));
                //outputstream.close();

        }catch(Exception ex){
            log.LoggingToFile.log(Level.WARNING, "~~~~ WARNING ~~~~:: 1107201123: fail to remove special characters from document.");
            log.LoggingToFile.log(Level.WARNING, "~~~~ WARNING ~~~~:: 1107201123: "
                    + ex.getLocalizedMessage());
        }

    }

    public static long forTransfer(File f1,File f2){
        try{

            long time=new Date().getTime();
            int length=2097152;
            
            // define file input stream and file channel for subsequent file
            // copying
            FileInputStream  in  = new FileInputStream(f1);
            FileOutputStream out = new FileOutputStream(f2);
            FileChannel inC  = in.getChannel();
            FileChannel outC = out.getChannel();
            int i=0;

            // start loop to read file
            while(true)
            {
                // for
                if(inC.position()==inC.size()){
                    inC.close();
                    outC.close();
                    return new Date().getTime()- time;
                }


                if((inC.size()-inC.position())<20971520)
                    length=(int)(inC.size()-inC.position());
                else
                    length=20971520;

                inC.transferTo(inC.position(),length,outC);
                inC.position(inC.position()+length);
                i++;
            }


        
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, ex.toString());
            return 0;
        }
    }

}

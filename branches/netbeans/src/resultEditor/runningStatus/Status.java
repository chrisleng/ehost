/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.runningStatus;

import errors.ErrorPane;
import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 *
 * @author leng 2010-6-12
 */
public class Status {
    
    protected static JLabel jLabel_processedfiles;
    protected static JLabel jLabel_totalfiles;
    protected static JLabel jLabel_concepts;
    protected static JLabel jLabel_allconcepts;
    protected static JLabel jLabel_dates;
    protected static JLabel jLabel_totaldates;
    protected static JLabel jLabel_ssn;
    protected static JLabel jLabel_allssn;
    protected static JLabel jLabel_customregex;
    protected static JLabel jLabel_allcustomregex;
    protected static JLabel jLabel_file;
    protected static JComboBox jComboBox_processedfilelist;

    protected static JButton __jButton_stop;
    protected static JButton __jButton_back;

    protected static userInterface.GUI __gui;

    protected static JLabel jLabel_status;
    protected static JLabel __jLabel_taskdescription;

    protected static Vector<Counter> counter = new Vector<Counter>();

    public Status(JComboBox _jComboBox_processedfilelist,
            JLabel _jLabel_totalfiles,
            JLabel _jLabel_processedfiles,
            JLabel _jLabel_concepts,
            JLabel _jLabel_allconcepts,
            JLabel _jLabel_dates,
            JLabel _jLabel_totaldates,
            JLabel _jLabel_ssn,
            JLabel _jLabel_totalssn,
            JLabel _jLabel_customregex,
            JLabel _jLabel_allcustomregex,
            //JLabel _jLabel_currentProcessing_filename
            JLabel jLabel_status,
            JLabel jLabel_file,
            JLabel _jLabel_taskdescription,
            JButton _jButton_stop,
            JButton _jButton_back,
            userInterface.GUI _gui
            )
    {
        //this._gui_ = _gui;
        jLabel_processedfiles = _jLabel_processedfiles;
        jLabel_totalfiles = _jLabel_totalfiles;
        jLabel_concepts = _jLabel_concepts;
        jLabel_allconcepts = _jLabel_allconcepts;
        jLabel_dates = _jLabel_dates;
        jLabel_totaldates = _jLabel_totaldates;
        jLabel_ssn = _jLabel_ssn;
        jLabel_allssn = _jLabel_totalssn;
        jLabel_customregex = _jLabel_customregex;
        jLabel_allcustomregex = _jLabel_allcustomregex;
        __jLabel_taskdescription = _jLabel_taskdescription;
        __jButton_stop = _jButton_stop;
        __jButton_back = _jButton_back;
        Status.jLabel_file = jLabel_file;

        //jLabel_currentProcessing_filename = _jLabel_currentProcessing_filename;
        jComboBox_processedfilelist = _jComboBox_processedfilelist;
        Status.jLabel_status = jLabel_status;

        Status.__gui = _gui;
        startStatusFlash();
    }


    private static JLayeredPane lp = null; // 我们要用到的层
    private static JPanel b_c = null;
    private static ErrorPane dictionaryDialog = null;


    /**This contruction method is only for you to use while calling showErrorMsg()*/
    public Status(){}
    public void showErrorMsg(String msg){
        lp = __gui.getLayeredPane(); // 获取JLayeredPane
        dictionaryDialog = new ErrorPane(__gui, msg);
        lp.add(dictionaryDialog, new Integer(500));

        //dictionaryDialog.setBounds(100, 100, 400, 400);
        dictionaryDialog.setVisible(true);
        dictionaryDialog.updateUI();


    }

    /**set the after the NLP processing successfully completed */
    public static void afterCompleteAllProcessing()
    {
        __jLabel_taskdescription.setText("<html><font color=red><b>Task Completed<b></font></html>");
        __jButton_back.setEnabled(true);
        __jButton_stop.setText("Go to result Editor");
    }

    public static void listCurrentCorpusFileName(String filename){
        jLabel_file.setText(filename);
        jLabel_file.repaint();
    }
    /** set text: total processing files */
    public static void setTotalFiles(String _fileNumbers){
        //_gui_.jLabel_totalprocessingfiles.setText(_fileNumbers);
                jLabel_totalfiles.setText("<html><Font color=\"blue\">" + _fileNumbers
                + "</Font></html>");
    }
    /** set text: total processing files */
    public static void setTotalFiles(int _fileNumbers){
        jLabel_totalfiles.setText("<html><Font color=\"blue\">" + String.valueOf(_fileNumbers)
                + "</Font></html>");
    }

    static Thread flash = null;
    static int status=0;
    private static void startStatusFlash(){
        flash = new Thread()
        {
            @Override
            public void run() {
                String text;
                try{
                    while((status==1)||(status==2)||(status==3)||(status==4)||(status==5))
                    {
                        if (status==1){
                            Thread.sleep(200);
                            text="<html><font color=blue>" +
                                    "Initialization</font> --> Loading " +
                                    "Dictionary --> Processing Article Contents" +
                                    " --> Post Process --> End</html>";
                            jLabel_status.setText(text);
                            jLabel_status.repaint();
                        }else if (status==2 ){
                            Thread.sleep(200);
                            text="<html>Initialization --> <font color=blue>Loading " +
                                    "Dictionary</font> --> Processing Article Contents" +
                                    " --> Post Process --> End</html>";
                            jLabel_status.setText(text);
                            jLabel_status.repaint();
                        }else if (status==3 ){
                            Thread.sleep(200);
                            text="<html>Initialization --> Loading " +
                                    "Dictionary --> <font color=blue>Processing Article Contents</font>" +
                                    " --> Post Process --> End</html>";
                            jLabel_status.setText(text);
                            jLabel_status.repaint();
                        }
                        else if (status==4 ){
                            Thread.sleep(200);
                            text="<html>Initialization --> Loading " +
                                    "Dictionary --> Processing Article Contents" +
                                    " --> <font color=blue>Post Process</font> --> End</html>";
                            jLabel_status.setText(text);
                            jLabel_status.repaint();
                        }
                        if(status==888)                            
                            break;

                        Thread.sleep(200);
                        text="<html><font color=black>Initialization --> Loading Dictionary --> Processing Article Contents --> Post Process --> End</font></html>";
                        jLabel_status.setText(text);
                        jLabel_status.repaint();
                        
                    }
                    
                
                }catch(Exception ex){
                        log.LoggingToFile.log(Level.SEVERE, "error 1103111002:: fail to set text flashing");
                }
                    
                }
            };
            flash.start();
               
    }

    public static void  setStatus(int status){
            Status.status=status;
        }

    public static void setProcessedFiles(String _fileNumbers){
        //_gui_.jLabel_totalprocessingfiles.setText(_fileNumbers);
        jLabel_processedfiles.setText("<html><Font color=\"blue\">"
                + String.valueOf(_fileNumbers)
                + "</Font></html>");
    }
    public static void setProcessedFiles(int _fileNumbers){
        //_gui_.jLabel_totalprocessingfiles.setText(_fileNumbers);
        jLabel_processedfiles.setText("<html><Font color=\"blue\">"
                + String.valueOf(_fileNumbers)
                + "</Font></html>");
    }

    // new file
    public static void addProcessedFilename(File _file){
        Counter c = new Counter();
        counter.add(c);
        jComboBox_processedfilelist.addItem(_file.getName());
        int index = jComboBox_processedfilelist.getItemCount() - 1;
        jComboBox_processedfilelist.setSelectedIndex(index);
        jLabel_concepts.setText("<html><font color=blue>0<font></html>");
        jLabel_dates.setText("<html><font color=blue>0<font></html>");
        jLabel_ssn.setText("<html><font color=blue>0<font></html>");
        jLabel_customregex.setText("<html><font color=blue>0<font></html>");

    }

    public static void found_a_term_bycustomregex(){
        int size = counter.size();
        counter.get(size-1).customregex_ammount++;
        showamount_found_bycustomregex( counter.get(size-1).customregex_ammount,
                countamount_of_allfounds_bycustomregex() );
    }

    public static void found_a_ssn(){
        int size = counter.size();
        counter.get(size-1).SSN_ammount++;
        showamount_ofssn( counter.get(size-1).SSN_ammount, countamount_of_allssns() );
    }

    public static void conceptfound(){
        int size = counter.size();
        counter.get(size-1).concept_ammount++;
        showamount_ofConcepts( counter.get(size-1).concept_ammount, countamount_of_allconcepts() );
    }

    public static void datefound(){
        int size = counter.size();
        counter.get(size-1).date_ammount++;
        showamount_ofdate( counter.get(size-1).date_ammount, countamount_of_allDate() );
    }

    public static void showamount_ofConcepts(int i, int total){
        jLabel_concepts.setText("<html><font color=blue>"+i+"<font></html>");
        jLabel_allconcepts.setText("<html><font color=blue>"+total+"<font></html>");
    }

    public static void showamount_found_bycustomregex(int i, int total){
        jLabel_customregex.setText("<html><font color=blue>"+i+"<font></html>");
        jLabel_allcustomregex.setText("<html><font color=blue>"+total+"<font></html>");
    }

    public static void showamount_ofssn(int i, int total){
        jLabel_ssn.setText("<html><font color=blue>"+i+"<font></html>");
        jLabel_allssn.setText("<html><font color=blue>"+total+"<font></html>");
    }

    public static void showamount_ofdate(int i, int total){
        jLabel_dates.setText("<html><font color=blue>"+i+"<font></html>");
        jLabel_totaldates.setText("<html><font color=blue>"+total+"<font></html>");
    }

    private static int countamount_of_allssns()
    {
        int count=0;
        int size = counter.size();
        for(int i=0;i<size;i++){
            count+= counter.get(i).SSN_ammount;
        }
        return count;
    }

    private static int countamount_of_allfounds_bycustomregex(){
        int count=0;
        int size = counter.size();
        for(int i=0;i<size;i++){
            count+= counter.get(i).customregex_ammount;
        }
        return count;
    }

    private static int countamount_of_allconcepts(){
        int count=0;
        int size = counter.size();
        for(int i=0;i<size;i++){
            count+= counter.get(i).concept_ammount;
        }
        return count;
    }

    private static int countamount_of_allDate(){
        int count=0;
        int size = counter.size();
        for(int i=0;i<size;i++){
            count+= counter.get(i).date_ammount;
        }
        return count;
    }
    
    public static void reset(){
        counter.clear();
        jComboBox_processedfilelist.removeAllItems();
        setProcessedFiles(" ");
        setTotalFiles(env.Parameters.corpus.LIST_ClinicalNotes.size());
        __jButton_stop.setText("Stop");
    }

    public static void show(int index){
        int size = counter.size();
        int concept=0, customregex=0, ssn=0, date=0;
        if ((index <= (size -1))&&(index >= 0)){
            concept = counter.get(index).concept_ammount;
            customregex = counter.get(index).customregex_ammount;
            ssn = counter.get(index).SSN_ammount;
            date = counter.get(index).date_ammount;

        jLabel_concepts.setText("<html><font color=blue>"+concept+"<font></html>");
        jLabel_customregex.setText("<html><font color=blue>"+customregex+"<font></html>");
        jLabel_ssn.setText("<html><font color=blue>"+ssn+"<font></html>");
        jLabel_dates.setText("<html><font color=blue>"+date+"<font></html>");
        }
    }

    public static void enablecombolist_counter(){
        jComboBox_processedfilelist.setEnabled(true);
    }
}

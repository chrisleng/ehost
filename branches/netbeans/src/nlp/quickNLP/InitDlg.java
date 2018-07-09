/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InitDlg.java
 *
 * Build indexed concept dictionary and routemap.
 * Created on Sep 1, 2010, 8:09:13 PM
 */

package nlp.quickNLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Chris
 */
public class InitDlg extends javax.swing.JFrame {

    protected userInterface.Setting settingDlg;
    protected Vector<Concept> concepts = new Vector<Concept>();
    protected int amount_of_valid_preannotated_concepts_in_a_specific_dictionary = 0;


    /** Creates new form InitDlg */
    public InitDlg(userInterface.Setting settingDlg) {
        this.settingDlg = settingDlg;
        initComponents();
        setDlgLocation();

        new Thread(){
                @Override
                public void run(){
                    ArrayList<ConceptDictionary> files = getDictionaryList();
                    if ( files == null )
                        return;
                    integrateDicts( files );
                    //sortDict1();
                    //sortDict2();
                    
                    buildDict();

                    buildRouteMap();
                    
                    releaseRES();
         }}.start();

    }

    private void releaseRES(){
        concepts.clear();
    }

    private void buildRouteMap(){
        this.showInfo("\nBuilding route map for indexed dictionary ... ");
        RouteMap routemap = new RouteMap( concepts, this );
        this.showInfo("Complete.");
        this.showInfo("\nSaving route map ... ");
        routemap.saveRouteMap();
        this.showInfo("Complete.");
    }

    private void sortDict2(){
        this.showInfo("Begin sort integrated entries (" + concepts.size() + ")... " );
        QuickSorter sorter = new QuickSorter();
        sorter.QuickSort(concepts);
        this.showInfo("Sort end. ");
    }

    
    private void sortDict1(){
        this.showInfo("\n ------------------------");
        this.showInfo("Begin sort integrated entries ... " );
        if ( concepts == null ) {
            this.showInfo("ERROR 1009021053 - no entry in memory!!!");
            return;
        }
        int size = concepts.size();
        for( int i=0; i<size;i++ ){
            //showInfo("sorting " + i + "/" + size );
            if(i%1000 == 0)
                showInfo_noenter(".");
                        //System.out.print('.');
            if(i%80000 == 0)
                showInfo_noenter( ".\n");

            Concept thisconcept = concepts.get(i);
            Concept compareconcept, smallerconcept;
            int smallest = -1;
            smallerconcept = thisconcept;
            
            for(int j=i+1; j<size; j++){
                compareconcept = concepts.get(j);
                if( compare( smallerconcept, compareconcept ) ){
                    smallest = j;
                    smallerconcept = compareconcept;
                }
            }
            if( smallest != -1 ){
                concepts.setElementAt(smallerconcept, i);
                concepts.setElementAt(thisconcept, smallest);
            }
        }
        this.showInfo("Sort end. ");
    }

    private void buildDict(){
        if ( concepts == null ) {
            this.showInfo("ERROR 1009021152 - no data in memory waiting for output!!!");
            return;
        }

        this.showInfo("\nBeing out put indexed dictionary.");
        FileOutputStream output;
        try {
            output = new FileOutputStream("eHOST.idict");
            PrintStream p = new PrintStream(output);

            for( Concept concept: concepts ){
                p.println( concept.term + "<---->" + concept.category );
            }
            p.close();

        } catch (Exception e) {
            this.showInfo("ERROR 1009021159 - " + e.toString());
            return;
        }

        this.showInfo("Complete.");

    }

    private boolean compare(Concept o1,Concept o2) {
        String ac1_classname = o1.term.trim();
        String ac2_classname = o2.term.trim();

        int size1 = ac1_classname.length();
        int size2 = ac2_classname.length();

        int size = ( size1 < size2 ? size1 : size2);

        char[] chars1 = ac1_classname.toCharArray();
        char[] chars2 = ac2_classname.toCharArray();

        char[] chars1_lowercase = ac1_classname.toLowerCase().toCharArray();
        char[] chars2_lowercase = ac2_classname.toLowerCase().toCharArray();

        for( int i = 0; i<size; i++ ){
            if ( chars1_lowercase[i] > chars2_lowercase[i] )
                return true;
            else if ( chars1_lowercase[i] < chars2_lowercase[i] )
                return false;
            else if ( chars1_lowercase[i] == chars2_lowercase[i] ){
                if( chars1[i] > chars2[i] )
                    return true;
                else if( chars1[i] < chars2[i] )
                    return false;

            }
        }

        if( size1 > size2 )
            return true;
        else return false;
    }

    private void integrateDicts( ArrayList<ConceptDictionary> files ){
        if (  files == null )
            return;

        int size = files.size();
        for(int i=0; i<size; i++){
            this.showInfo("Handling " + (i+1) + " dictionary now...");
            if ( files.get(i) == null )
                continue;
            File f = null;
            String separator = null;
            try{
                //System.out.println("0000 "+files.get(i).filename);
                f = new File( files.get(i).filename );
                
                separator = files.get(i).separator;
            }catch(Exception e){
                this.showInfo("Failure to open this concept dictionary!!!" + e.toString());
                continue;
            }

            if(( f == null )||( separator == null )){
                this.showInfo("Failure to get file or matched separator!!!");
                continue;
            }

            if (!f.exists()){
                this.showInfo("File do not exist!!!");
                continue;
            }

            if (( separator == null )||(separator.trim().length() < 1)) {
                this.showInfo("Invalid separator!!!");
                continue;
            }
            
            integrate(f, separator );


        }

    }

    private void integrate(File f, String separator ){
        try {
            showInfo("Dictionary ["+f.toString() +"] opened:\n ");
            int count = 0;
            BufferedReader thisDictionary = new BufferedReader(new FileReader(f));
            String st = thisDictionary.readLine();
            String[] sts;

            while (st != null) {
                sts = st.split( separator);

                if ( sts.length == 2 ){
                    amount_of_valid_preannotated_concepts_in_a_specific_dictionary++;

                    if(amount_of_valid_preannotated_concepts_in_a_specific_dictionary%1000 == 0)
                        showInfo_noenter(".");
                        //System.out.print('.');
                    if(amount_of_valid_preannotated_concepts_in_a_specific_dictionary%80000 == 0)
                        showInfo_noenter( ".\n");
                    
                    recordConcept(sts[0], sts[1]);
                    count++;
                }

                st = thisDictionary.readLine();
            }
            thisDictionary.close();

            showInfo("Read "+ count + " pre-annotated concepts from dictionary [" + f.toString() +"]");
        } catch (Exception e) {
            this.showInfo("error: e");
        }
    }

    private void recordConcept(String concept, String category){
        if (( concept == null )||(category == null ))
            return;
        if(( concept.trim().length() < 1 )||(category.trim().length() < 1 ))
            return;

        Concept c = new Concept();
        c.term = concept.trim();
        c.category = category.trim();
        
        this.concepts.add(c);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextArea1.setBackground(new java.awt.Color(253, 253, 253));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel1.setText("Building dictionaries index and routemap");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setDlgLocation(){
        int parentx = settingDlg.getX(), parenty = settingDlg.getY();
        int parentwidth = settingDlg.getWidth(), parentheight = settingDlg.getHeight();
        int width = this.getWidth(), height = this.getHeight();
        int x = parentx + (int)((parentwidth-width)/2);
        int y = parenty + (int)((parentheight-height)/2);
        this.setLocation(x, y);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private ArrayList<ConceptDictionary> getDictionaryList(){
        try{
            ArrayList files = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES;

            int size = 0;

            if( files == null )
                size = 0;
            size = files.size();
            if ( size > 1 )
                this.jTextArea1.setText("There are " + size + " dictonaries in the waiting list...");
            else
                this.jTextArea1.setText("There is " + size + " dictonary in the waiting list...");

            ArrayList<ConceptDictionary> dicts = new ArrayList<ConceptDictionary>();
            for(Object o : files) {
                if ( o == null ) {
                    continue;
                }

                Object[] Os = (Object[]) o;
                ConceptDictionary cd = new ConceptDictionary();
                cd.filename = (String) Os[0];
                cd.weight = (String)Os[1];
                cd.describe = (String)Os[2];
                cd.separator = (String)Os[3];
                cd.amount = (String)Os[4];
                dicts.add(cd);
            }

            return dicts;
        }catch(Exception e){
            showInfo(e.toString());
            return null;
        }
    }

    private void showInfo(String info ){
        if ( info == null )
            return;
        String text = jTextArea1.getText();
        text = text + "\n" + info;
        this.jTextArea1.setText(text);
        this.jTextArea1.setCaretPosition( text.length() );
    }
    
    public void showInfo_noenter(String info ){
        if ( info == null )
            return;
        String text = jTextArea1.getText();
        text = text + info;

        this.jTextArea1.setText(text);
        this.jTextArea1.setCaretPosition( text.length() );
    }
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}

class ConceptDictionary{
    /**@para filename - file name with absolute path */
    String filename;
    /** @para weight - if same all will be 0, otherwise big number means heavy weight*/
    String weight;
    /** @para description*/
    String describe;
    /** @para separator */
    String separator;
    /** @para number_of_valid_entries */
    String amount;
}
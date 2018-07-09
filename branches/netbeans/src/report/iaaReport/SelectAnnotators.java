/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SelectAnnotators.java
 *
 * Created on Aug 17, 2011, 4:05:28 AM
 */

package report.iaaReport;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author leng
 */
public class SelectAnnotators extends javax.swing.JPanel {

    

    private static ArrayList<String> __annotatorNames = null;
    private static ArrayList<String> __annotationClasses = null;
    
    private IAA __iaa;
    
    /** constructor of current class: Creates new form SelectAnnotators */
    public SelectAnnotators(ArrayList<String> annotatorNames, ArrayList<String> annotationClasses, IAA _iaa) {
        __iaa = _iaa;
        __annotatorNames = annotatorNames;
        __annotationClasses = annotationClasses;
        initComponents();
        jPanel7.setVisible(false);
        jPanel8.setVisible(false);

        if(IAA.CHECK_OVERLAPPED_SPANS)
            jRadioButton_check_overlapped_spans.setSelected(true);
        else this.jRadioButton_check_same_spans.setSelected(true);        
        jCheckBox_check_attributes.setSelected(IAA.CHECK_ATTRIBUTES);
        jCheckBox_check_relationships.setSelected(IAA.CHECK_RELATIONSHIP);
        jCheckBox_check_comments.setSelected(IAA.CHECK_COMMENT);
        jCheckBox_sameClasses.setSelected(IAA.CHECK_CLASS);


        jButton_goGenReports.setEnabled(false);

        //this.jLabel_warning.setVisible(false);
        jList_annotators.setSelectionBackground( new Color(188,198,170) );
        jList_annotators.setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
        
        // list all pre-collected annotator names
        listAnnotators();
        // list all pre-collected annotation class names
        listAnnotationClasses();

        if (this.jList_annotators.getModel().getSize()>=2)
            jButton_goGenReports.setEnabled(true);
        
        this.updateUI();

        // #### add a listener to the list of annotators
        //      so the checkbox of the item can be checked or unchecked after 
        //      user clicked on a item of annotator
        jList_annotators.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = jList_annotators.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) jList_annotators.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = jList_annotators.getCellBounds(index, index);
                jList_annotators.repaint(rect);

                int countSelectedItem = 0;
                int size = jList_annotators.getModel().getSize();
                for(int i=0; i< size;i++)
                {
                    CheckableItem it = (CheckableItem) jList_annotators.getModel().getElementAt(i);
                    if (it.isSelected() )
                        countSelectedItem++;
                }

                if( countSelectedItem >= 2 )
                    jButton_goGenReports.setEnabled(true);
                else
                    jButton_goGenReports.setEnabled(false);
            }
        });
        
        // #### add a listener to the list of annotators
        //      so the checkbox of the item can be checked or unchecked after 
        //      user clicked on a item of annotator
        this.jList_annotationClasses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = jList_annotationClasses.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) jList_annotationClasses.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = jList_annotationClasses.getCellBounds(index, index);
                jList_annotationClasses.repaint(rect);

                int countSelectedItem = 0;
                int size = jList_annotationClasses.getModel().getSize();
                for(int i=0; i< size;i++)
                {
                    CheckableItem it = (CheckableItem) jList_annotationClasses.getModel().getElementAt(i);
                    if (it.isSelected() )
                        countSelectedItem++;
                }

                if( countSelectedItem >= 1 )
                    jButton_goGenReports.setEnabled(true);
                else
                    jButton_goGenReports.setEnabled(false);
            }
        });
    }



    /**in previous steps, especially in the IAA class, we already go though all
     * annotation articles and record all annotation class names. Here, we will 
     * list them on screen.
     */
    private void listAnnotationClasses() {
        Vector<CheckableItem> listdata = new Vector<CheckableItem>();
        if(__annotationClasses==null){
            this.jList_annotationClasses.setListData(listdata);
            return;
        }else{
            for(String name: __annotationClasses){
                if(name==null)
                    continue;
                listdata.add(new CheckableItem(name, true));
            }
            jList_annotationClasses.setListData(listdata);
            jList_annotationClasses.setCellRenderer( new CheckListRenderer() );
            jList_annotationClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList_annotationClasses.setBorder(new EmptyBorder(0, 4, 0, 0));
            return;
        }
    }

    private void listAnnotators(){
        Vector<CheckableItem> listdata = new Vector<CheckableItem>();
        if(__annotatorNames==null){
            this.jList_annotators.setListData(listdata);
            return;
        }else{
            for(String name: __annotatorNames){
                if(name==null)
                    continue;
                listdata.add(new CheckableItem(name, true));
            }
            this.jList_annotators.setListData(listdata);
            jList_annotators.setCellRenderer( new CheckListRenderer() );
            jList_annotators.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList_annotators.setBorder(new EmptyBorder(0, 4, 0, 0));
            return;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton_goGenReports = new javax.swing.JButton();
        jPanel_center = new javax.swing.JPanel();
        jPanel_selectClasses = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane_annotator = new javax.swing.JScrollPane();
        jList_annotators = new javax.swing.JList();
        jLabel_warning = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel_warning2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox_sameClasses = new javax.swing.JCheckBox();
        jCheckBox_check_attributes = new javax.swing.JCheckBox();
        jCheckBox_check_relationships = new javax.swing.JCheckBox();
        jRadioButton_check_same_spans = new javax.swing.JRadioButton();
        jRadioButton_check_overlapped_spans = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox_check_comments = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane_annotator1 = new javax.swing.JScrollPane();
        jList_annotationClasses = new javax.swing.JList();
        jLabel_warning1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane_annotator2 = new javax.swing.JScrollPane();
        jList_annotationClasses1 = new javax.swing.JList();
        jLabel_warning3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane_annotator3 = new javax.swing.JScrollPane();
        jList_annotationClasses2 = new javax.swing.JList();
        jLabel_warning4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 254));
        setBorder(javax.swing.BorderFactory.createMatteBorder(20, 10, 5, 10, new java.awt.Color(255, 255, 254)));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 254));

        jLabel1.setFont(new java.awt.Font("Georgia", 1, 15)); // NOI18N
        jLabel1.setText("Please Select Annotators to Compare ... ...");

        jLabel2.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel2.setText("To generate an IAA report at least 2 annotators need to be selected from the following list:");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 357, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addContainerGap(1087, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 254));

        jButton1.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton_goGenReports.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jButton_goGenReports.setText("Generate Reports");
        jButton_goGenReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_goGenReportsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(1315, Short.MAX_VALUE)
                .add(jButton_goGenReports)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton1)
                    .add(jButton_goGenReports))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel_center.setBackground(new java.awt.Color(238, 238, 237));
        jPanel_center.setLayout(new java.awt.GridLayout(2, 2, 3, 0));

        jPanel_selectClasses.setBackground(new java.awt.Color(255, 255, 254));
        jPanel_selectClasses.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel9.setBackground(new java.awt.Color(255, 254, 255));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jScrollPane_annotator.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jList_annotators.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane_annotator.setViewportView(jList_annotators);

        jPanel9.add(jScrollPane_annotator, java.awt.BorderLayout.CENTER);

        jLabel_warning.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel_warning.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_warning.setText("* You need to select at least 2 annotators ... ... ");
        jPanel9.add(jLabel_warning, java.awt.BorderLayout.PAGE_START);

        jPanel_selectClasses.add(jPanel9);

        jPanel5.setBackground(new java.awt.Color(255, 255, 254));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jLabel_warning2.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel_warning2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_warning2.setText("Define criteria for differences:");
        jPanel5.add(jLabel_warning2, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 254));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102)));

        jLabel3.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel3.setText("eHOST will check annotations fitting the following conditions: ");

        jCheckBox_sameClasses.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_sameClasses.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jCheckBox_sameClasses.setSelected(true);
        jCheckBox_sameClasses.setText("they have same classes");

        jCheckBox_check_attributes.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_check_attributes.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jCheckBox_check_attributes.setSelected(true);
        jCheckBox_check_attributes.setText("they have same attributes");

        jCheckBox_check_relationships.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_check_relationships.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jCheckBox_check_relationships.setSelected(true);
        jCheckBox_check_relationships.setText("they have same relationships");

        jRadioButton_check_same_spans.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton_check_same_spans);
        jRadioButton_check_same_spans.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jRadioButton_check_same_spans.setSelected(true);
        jRadioButton_check_same_spans.setText("they have exact same spans");

        buttonGroup1.add(jRadioButton_check_overlapped_spans);
        jRadioButton_check_overlapped_spans.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jRadioButton_check_overlapped_spans.setText("or they also have their spans overlapped");

        jLabel4.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jLabel4.setText("and");

        jCheckBox_check_comments.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_check_comments.setFont(new java.awt.Font("Georgia", 0, 12)); // NOI18N
        jCheckBox_check_comments.setText("they have same comments");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(8, 8, 8)
                        .add(jLabel3))
                    .add(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel4)
                        .add(18, 18, 18)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jCheckBox_sameClasses)
                            .add(jCheckBox_check_attributes)
                            .add(jCheckBox_check_relationships)
                            .add(jPanel6Layout.createSequentialGroup()
                                .add(jRadioButton_check_same_spans)
                                .add(18, 18, 18)
                                .add(jRadioButton_check_overlapped_spans))
                            .add(jCheckBox_check_comments))))
                .addContainerGap(293, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(jLabel3)
                .add(3, 3, 3)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButton_check_same_spans)
                    .add(jRadioButton_check_overlapped_spans))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jCheckBox_sameClasses)
                        .add(1, 1, 1)
                        .add(jCheckBox_check_attributes)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jCheckBox_check_relationships)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jCheckBox_check_comments)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel_selectClasses.add(jPanel5);

        jPanel_center.add(jPanel_selectClasses);

        jPanel3.setBackground(new java.awt.Color(255, 255, 254));
        jPanel3.setLayout(new java.awt.GridLayout(1, 3, 2, 0));

        jPanel4.setBackground(new java.awt.Color(255, 255, 254));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane_annotator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jList_annotationClasses.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane_annotator1.setViewportView(jList_annotationClasses);

        jPanel4.add(jScrollPane_annotator1, java.awt.BorderLayout.CENTER);

        jLabel_warning1.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel_warning1.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_warning1.setText("Select Classes:");
        jPanel4.add(jLabel_warning1, java.awt.BorderLayout.PAGE_START);

        jPanel3.add(jPanel4);

        jPanel7.setBackground(new java.awt.Color(255, 255, 254));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jScrollPane_annotator2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jList_annotationClasses1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane_annotator2.setViewportView(jList_annotationClasses1);

        jPanel7.add(jScrollPane_annotator2, java.awt.BorderLayout.CENTER);

        jLabel_warning3.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel_warning3.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_warning3.setText("Select Attributes:");
        jPanel7.add(jLabel_warning3, java.awt.BorderLayout.PAGE_START);

        jPanel3.add(jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 254));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jScrollPane_annotator3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jList_annotationClasses2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane_annotator3.setViewportView(jList_annotationClasses2);

        jPanel8.add(jScrollPane_annotator3, java.awt.BorderLayout.CENTER);

        jLabel_warning4.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N
        jLabel_warning4.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_warning4.setText("Select Relationships:");
        jPanel8.add(jLabel_warning4, java.awt.BorderLayout.PAGE_START);

        jPanel3.add(jPanel8);

        jPanel_center.add(jPanel3);

        add(jPanel_center, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        __iaa.exitThisWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton_goGenReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_goGenReportsActionPerformed
        try{

            IAA.CHECK_OVERLAPPED_SPANS = jRadioButton_check_overlapped_spans.isSelected();
            IAA.CHECK_ATTRIBUTES = jCheckBox_check_attributes.isSelected();
            IAA.CHECK_RELATIONSHIP = jCheckBox_check_relationships.isSelected();
            IAA.CHECK_COMMENT = jCheckBox_check_comments.isSelected();
            IAA.CHECK_CLASS = jCheckBox_sameClasses.isSelected();

            ArrayList<String> selectedAnnotators = new ArrayList<String>();

            for(int i=0;i<jList_annotators.getModel().getSize(); i++)
            {
                Object obj = this.jList_annotators.getModel().getElementAt(i);
                if(obj==null)
                    continue;
                CheckableItem item = (CheckableItem)obj;
                if(item.isSelected())
                    selectedAnnotators.add( item.getAnnotatorName() );
            }
            
            if(selectedAnnotators.size()<2)
            {
                commons.Tools.beep();
                commons.Tools.beep();
                log.LoggingToFile.log(Level.SEVERE, "no enough annotators are selected to do pair-comparation.");
                __iaa.dispose();
            }

            ArrayList<String> selectedClasses = new ArrayList<String>();

            for(int i=0;i<jList_annotationClasses.getModel().getSize(); i++)
            {
                Object obj = this.jList_annotationClasses.getModel().getElementAt(i);
                if(obj==null)
                    continue;
                CheckableItem item = (CheckableItem)obj;
                if(item.isSelected())
                    selectedClasses.add( item.getAnnotatorName() );
            }

            if(selectedClasses.size()<1)
            {
                commons.Tools.beep();
                commons.Tools.beep();
                log.LoggingToFile.log(Level.SEVERE, "At least one class should be selected!");
                __iaa.dispose();
            }


            __iaa.generateIAAReportInBackground( selectedAnnotators, selectedClasses );
            
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error occurred while try to send selected annotator names to analysis module!");
        }



        
    }//GEN-LAST:event_jButton_goGenReportsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_goGenReports;
    private javax.swing.JCheckBox jCheckBox_check_attributes;
    private javax.swing.JCheckBox jCheckBox_check_comments;
    private javax.swing.JCheckBox jCheckBox_check_relationships;
    private javax.swing.JCheckBox jCheckBox_sameClasses;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel_warning;
    private javax.swing.JLabel jLabel_warning1;
    private javax.swing.JLabel jLabel_warning2;
    private javax.swing.JLabel jLabel_warning3;
    private javax.swing.JLabel jLabel_warning4;
    private javax.swing.JList jList_annotationClasses;
    private javax.swing.JList jList_annotationClasses1;
    private javax.swing.JList jList_annotationClasses2;
    private javax.swing.JList jList_annotators;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_center;
    private javax.swing.JPanel jPanel_selectClasses;
    private javax.swing.JRadioButton jRadioButton_check_overlapped_spans;
    private javax.swing.JRadioButton jRadioButton_check_same_spans;
    private javax.swing.JScrollPane jScrollPane_annotator;
    private javax.swing.JScrollPane jScrollPane_annotator1;
    private javax.swing.JScrollPane jScrollPane_annotator2;
    private javax.swing.JScrollPane jScrollPane_annotator3;
    // End of variables declaration//GEN-END:variables

}

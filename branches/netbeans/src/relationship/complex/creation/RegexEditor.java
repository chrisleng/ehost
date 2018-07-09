/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RegexEditor.java
 *
 * Created on Nov 22, 2010, 12:23:59 PM
 */
package relationship.complex.creation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import relationship.complex.dataTypes.RelationshipDef;
import resultEditor.annotationClasses.Depot;

/**
 * This Graphical User Interface will provide a way for users to create Complex
 * Relationship entries for the schema.
 * <pre>
 * Rules for allowing Complex Relationships into the Schema.
 *  1)Relationship name must be unique in the schema
 *  2)Regular expression must be unique in the schema
 *  3)Regular expression must compile
 *  4)Regular Expression meets the requirements specified in the 'help' section.
 *      a)Only uses class names within the schema and regex symbols
 *      b)All classes are in parantheses (Test) not Test
 * <pre>
 *
 * @author Kyle
 */
public class RegexEditor extends javax.swing.JFrame
{
    //<editor-fold defaultstate="collapsed" desc="Member Variables">

    //Member Variables
    private RelationshipDef rel;
    private final RelationshipDef original;
    /*
     * The GUI that called the Regex Editor
     * TODO: Might want to replace this with an interface so this class can be called
     * from other classes...
     */
    private RelationshipSchemaEditor caller;
    //</editor-fold>

    String[] classNames;
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    /** Creates new form RegexEditor */
    public RegexEditor(RelationshipDef existing, relationship.complex.creation.RelationshipSchemaEditor editor)
    {
        //Keep track of the GUI that called this one
        caller = editor;
        if(caller != null) caller.setEnabled( false );
        
        //Initialize components
        initComponents();

        

        this.jLabel_message.setVisible( false );
        original = existing;

        

        //Get Classes to show user
        Depot depot = new Depot();
        classNames = depot.getAnnotationClasssnamesString();

        //Make the Classes regex safe(add a forward slash before any regex symbol)
        classNames = VerifyRegexWithClasses.Regexerate(classNames, "\\\\");

        //Display Classes so users know what they can use
        //this.jList_from.setListData(classNames);
        //this.jList_to.setListData(classNames);
        jList_from.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jList_to.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        //If they passed in a non-null Complex Relationship place its' info in the text boxes.
        if (existing != null)
        {
            regex.setText(existing.getAllowed());
            name.setText(existing.getName());            
            markItemsByRelationRegex( existing.getAllowed() );
            
        }else{
            regex.setText(null);
            name.setText( null );            
        }
        
        // update
            listClassesOnFrom();

        //Initial set up of buttons/ set background color of text boxes
        this.updateBasedOnParamValidity();
        
        setDialogSizeNLocation();
    }
    //</editor-fold>

   
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * This method should be called whenever the name or regular expression change.
     * This method will enable the save button if the name and regular expression are valid
     * and disable otherwise.
     */
    private void updateBasedOnParamValidity()
    {
        //Verify that the regular expression and name that the user entered is valid
        boolean goodRegex = VerifyRegexWithClasses.CheckValidRegex(regex.getText()) && !regex.getText().equals("");
        boolean goodName = !name.getText().equals("");

        //Verify that the relationship name and regular expression are unique to the schema.
        boolean nameExists = env.Parameters.RelationshipSchemas.checkNameExists(name.getText());
        boolean regexExists = env.Parameters.RelationshipSchemas.checkRegexExists(regex.getText());

        //Concatenate boolean results to be more useful
        boolean regexReady = goodRegex; // && !regexExists;
        boolean nameReady = goodName && !nameExists;
        boolean everythingReady = nameReady & regexReady;

        //If everything is good enable the 'next' button and set background color
        //of regex bar to green.
        if(everythingReady)
        {
            
            regex.setBackground(new Color(255,255,255));
            name.setBackground( new Color(255,255,255));
            save.setEnabled(true);
        }

        //Regex is bad or already exists in schema...
        else
        {
            
            if(!regexReady)
                regex.setBackground(new Color(255,150,150));
            else
                regex.setBackground(new Color(255,255,255));
            if(!nameReady)
                name.setBackground(new Color(255, 150, 150));
            else
                name.setBackground(new Color(255,255,255));
             
            save.setEnabled(false);
        }
    }
    /**
     * Center this dialog in callers window.
     */
    private void setDialogSizeNLocation()
    {
        // set dialog size
        Dimension dialogSize = new Dimension(710, 630); 
        this.setPreferredSize( dialogSize );
        this.setSize( dialogSize );
                
        // set the on-screen position
        if( caller != null )
            this.setLocationRelativeTo( caller );        
    }
    
    //</editor-fold>
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        regex = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        valid = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        help = new javax.swing.JButton();
        save = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jLabel_message = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel_topbar = new javax.swing.JPanel();
        jPanel_holder_description = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel_holder_fromto = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel_holder_fromto1 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        name = new javax.swing.JTextField();
        jPanel_holder_fromto2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel_holder_fromto3 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_from = new javax.swing.JList();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_to = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relationship Builder");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 0, 10, 10));

        jPanel3.setLayout(new java.awt.BorderLayout());

        regex.setEnabled(false);
        regex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regexActionPerformed(evt);
            }
        });
        regex.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                regexInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        regex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                regexKeyTyped(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                regexKeyReleased(evt);
            }
        });
        jPanel3.add(regex, java.awt.BorderLayout.CENTER);

        jLabel1.setText("Regular Expression:");
        jLabel1.setEnabled(false);
        jPanel3.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jPanel2.add(jPanel3);

        jPanel5.setLayout(new java.awt.GridLayout(0, 8));

        jPanel6.setLayout(new java.awt.BorderLayout());
        jPanel5.add(jPanel6);

        jPanel7.setLayout(new java.awt.BorderLayout());
        jPanel5.add(jPanel7);

        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel8.add(valid, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel8);

        jPanel9.setLayout(new java.awt.BorderLayout());
        jPanel5.add(jPanel9);

        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel5.add(jPanel10);

        jPanel11.setLayout(new java.awt.BorderLayout());

        help.setText("Help");
        help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpActionPerformed(evt);
            }
        });
        jPanel11.add(help, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel11);

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jPanel5.add(save);

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        jPanel5.add(cancel);

        jPanel2.add(jPanel5);

        jLabel_message.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel_message.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_message.setText("jLabel3");
        jPanel2.add(jLabel_message);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel4.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel_topbar.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 0, 5, new java.awt.Color(255, 254, 253)));
        jPanel_topbar.setLayout(new java.awt.BorderLayout());

        jPanel_holder_description.setBackground(new java.awt.Color(255, 253, 252));
        jPanel_holder_description.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 253)));
        jPanel_holder_description.setLayout(new java.awt.BorderLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/relationship/complex/creation/Relationship_cursor.png"))); // NOI18N
        jPanel_holder_description.add(jLabel2, java.awt.BorderLayout.LINE_START);

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jLabel6.setText("<html><b>Define a relationship</b><br> </html>");
        jPanel_holder_description.add(jLabel6, java.awt.BorderLayout.CENTER);

        jPanel_topbar.add(jPanel_holder_description, java.awt.BorderLayout.CENTER);

        jPanel12.setLayout(new java.awt.GridLayout(3, 0, 0, 2));

        jPanel_holder_fromto.setBackground(new java.awt.Color(240, 241, 242));
        jPanel_holder_fromto.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel_holder_fromto.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel14.setBackground(new java.awt.Color(0, 51, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102), 3));
        jPanel14.setForeground(new java.awt.Color(0, 102, 153));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("  Relationship Name:");
        jPanel14.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto.add(jPanel14);

        jPanel12.add(jPanel_holder_fromto);

        jPanel_holder_fromto1.setBackground(new java.awt.Color(240, 241, 242));
        jPanel_holder_fromto1.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel_holder_fromto1.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel15.setBackground(new java.awt.Color(0, 51, 102));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        jPanel15.setForeground(new java.awt.Color(0, 102, 153));
        jPanel15.setLayout(new java.awt.BorderLayout());

        name.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        name.setForeground(new java.awt.Color(0, 51, 204));
        name.setBorder(null);
        name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameActionPerformed(evt);
            }
        });
        name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameKeyReleased(evt);
            }
        });
        jPanel15.add(name, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto1.add(jPanel15);

        jPanel12.add(jPanel_holder_fromto1);

        jPanel_holder_fromto2.setBackground(new java.awt.Color(240, 241, 242));
        jPanel_holder_fromto2.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel_holder_fromto2.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel16.setBackground(new java.awt.Color(0, 51, 102));
        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102), 3));
        jPanel16.setForeground(new java.awt.Color(0, 102, 153));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 204, 204));
        jLabel9.setText("  Start From Class:");
        jPanel16.add(jLabel9, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto2.add(jPanel16);

        jPanel19.setBackground(new java.awt.Color(0, 51, 102));
        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102), 3));
        jPanel19.setForeground(new java.awt.Color(0, 102, 153));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("  To Class:");
        jPanel19.add(jLabel10, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto2.add(jPanel19);

        jPanel12.add(jPanel_holder_fromto2);

        jPanel_topbar.add(jPanel12, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel_topbar, java.awt.BorderLayout.NORTH);

        jPanel13.setBackground(new java.awt.Color(255, 254, 253));
        jPanel13.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 5, new java.awt.Color(255, 254, 253)));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel_holder_fromto3.setBackground(new java.awt.Color(240, 241, 242));
        jPanel_holder_fromto3.setPreferredSize(new java.awt.Dimension(0, 20));
        jPanel_holder_fromto3.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        jPanel17.setBackground(new java.awt.Color(0, 51, 102));
        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 51, 102)));
        jPanel17.setForeground(new java.awt.Color(0, 102, 153));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);

        jList_from.setBackground(new java.awt.Color(204, 204, 204));
        jList_from.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList_fromMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_fromMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList_from);

        jPanel17.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto3.add(jPanel17);

        jPanel18.setBackground(new java.awt.Color(0, 51, 102));
        jPanel18.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(0, 51, 102)));
        jPanel18.setForeground(new java.awt.Color(0, 102, 153));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);

        jList_to.setBackground(new java.awt.Color(204, 204, 204));
        jList_to.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList_toMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jList_toMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jList_to);

        jPanel18.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel_holder_fromto3.add(jPanel18);

        jPanel13.add(jPanel_holder_fromto3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel13, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Events">
    /**
     * 'Save' button pressed, save the relationship and dispose of this GUI.
     * @param evt
     */
    private void saveActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_saveActionPerformed
    {//GEN-HEADEREND:event_saveActionPerformed
        if( ( name.getText() == null ) 
                || ( name.getText().trim().length()<1 ) ) {
            this.jLabel_message.setText("Please type a relationship name!");
            this.jLabel_message.setVisible( true );
            commons.Tools.beep();
            return;
        }else
            this.jLabel_message.setVisible( false );
        
        caller.setEnabled( true );
        
        rel = new RelationshipDef(name.getText(), regex.getText());
        env.Parameters.RelationshipSchemas.add(rel);
        caller.updateRelationships();
        caller.config_saveProjectSetting();
        this.dispose();
        caller.toFront();
    }//GEN-LAST:event_saveActionPerformed

    /**
     * 'Help' button pressed, show help information.
     * @param evt
     */
    private void helpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpActionPerformed
    {//GEN-HEADEREND:event_helpActionPerformed
        ComplexRelationshipInfo info = new ComplexRelationshipInfo(this);
        info.setVisible(true);
    }//GEN-LAST:event_helpActionPerformed

    private void regexActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_regexActionPerformed
    {//GEN-HEADEREND:event_regexActionPerformed
    }//GEN-LAST:event_regexActionPerformed

    /**
     * User changed regular expression.. update gui based on validity of new
     * regular expression.
     * @param evt
     */
    private void regexKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_regexKeyReleased
    {//GEN-HEADEREND:event_regexKeyReleased
        this.updateBasedOnParamValidity();;
    }//GEN-LAST:event_regexKeyReleased


    private void regexKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_regexKeyTyped
    {//GEN-HEADEREND:event_regexKeyTyped
    }//GEN-LAST:event_regexKeyTyped
    /**
     * User canceled this action, dispose gui.
     * @param evt
     */
    private void cancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelActionPerformed
    {//GEN-HEADEREND:event_cancelActionPerformed

        caller.setEnabled( true );        
        if(original!=null)
            env.Parameters.RelationshipSchemas.add( original );
        caller.updateRelationships();
        this.dispose();
        caller.toFront();
    }//GEN-LAST:event_cancelActionPerformed

    private void regexInputMethodTextChanged(java.awt.event.InputMethodEvent evt)//GEN-FIRST:event_regexInputMethodTextChanged
    {//GEN-HEADEREND:event_regexInputMethodTextChanged
    }//GEN-LAST:event_regexInputMethodTextChanged

    private void nameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nameActionPerformed
    {//GEN-HEADEREND:event_nameActionPerformed

    }//GEN-LAST:event_nameActionPerformed

    /**
     * User changed name, update gui based on validity of new name
     * @param evt
     */
    private void nameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_nameKeyReleased
    {//GEN-HEADEREND:event_nameKeyReleased
        this.updateBasedOnParamValidity();
    }//GEN-LAST:event_nameKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        caller.setEnabled( true );
        
        env.Parameters.RelationshipSchemas.add( original );
        caller.updateRelationships();
        caller.toFront();
    }//GEN-LAST:event_formWindowClosing

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        this.toFront();
        this.requestFocus();
        commons.Tools.beep();
    }//GEN-LAST:event_formWindowDeactivated

    ArrayList<Integer> indices_from = new ArrayList<Integer>();
    ArrayList<Integer> indices_to = new ArrayList<Integer>();
    private void jList_toMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_toMousePressed
        
    }//GEN-LAST:event_jList_toMousePressed

    private void jList_fromMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_fromMousePressed
        
    }//GEN-LAST:event_jList_fromMousePressed

    private void jList_fromMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_fromMouseClicked
        jList_MousePressed( evt,  this.jList_from, indices_from );
        regex.setText( getRegex() );
    }//GEN-LAST:event_jList_fromMouseClicked

    private void jList_toMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_toMouseReleased
        jList_MousePressed( evt,  this.jList_to, indices_to );
        regex.setText( getRegex() );
    }//GEN-LAST:event_jList_toMouseReleased
    //</editor-fold>

    
    private void jList_MousePressed(java.awt.event.MouseEvent evt, JList jlist, ArrayList<Integer> indices) {                                        

        //find the item which was clicked on
        int newSelected = jlist.locationToIndex(evt.getPoint());
        if( newSelected < 0 )
            return;
        
        if( indices == null )
            indices = new ArrayList<Integer>();
        
        Integer indexObj = new Integer(newSelected);

        //就是把选好的如果以前选过的项从indices里面删掉
        //is this selected? if so remove it.
        if (indices.contains(indexObj)) {
            indices.remove(indexObj);
            //System.out.println("removed " + indexObj);
        } //如果是没选过的就存起来
        //otherwise add it to our list
        else {
            indices.add(indexObj);
            //System.out.println("added " + indexObj);
        }

        //copy to an int array
        int[] listData = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            listData[i] = indices.get(i);
        }
        //最后通过setSelectedIndices把JList设置好
        //set selected indices
        //if( listData.length == 1 )
        //    System.out.println("dddde");
        if( listData.length > 0 ){
            for( int i = 0; i < listData.length; i++){
                int x = listData[i];
                
                for( int j = 0; j < listData.length; j++){
                    int y = listData[j];
                    if( x > y){
                        listData[i] = y;
                        listData[j] = x;
                        x = listData[i];
                        y = listData[j];
                    }
                        
                }                                            
            }
        }
        
        jlist.setSelectedIndices( listData ); 
        
        
        
        System.out.println("\n");
        for(int i : listData){
          System.out.print(i + " ");  
        }
       
        //jlist.updateUI();

        // show button and color bar on component of the textfield to indicate 
        // whether current regex is good
        if((jList_from.getSelectedValues() != null)
                &&(jList_to.getSelectedValues() != null)){
            // reset buttons
            save.setEnabled( true );
            regex.setBackground(new Color(255,255,255));
        }else{
            save.setEnabled( false );
            regex.setBackground(new Color(255,150,150));
        }
        

    }  
    
    //<editor-fold defaultstate="collapsed" desc="GUI Component Declaration">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancel;
    private javax.swing.JButton help;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_message;
    private javax.swing.JList jList_from;
    private javax.swing.JList jList_to;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_holder_description;
    private javax.swing.JPanel jPanel_holder_fromto;
    private javax.swing.JPanel jPanel_holder_fromto1;
    private javax.swing.JPanel jPanel_holder_fromto2;
    private javax.swing.JPanel jPanel_holder_fromto3;
    private javax.swing.JPanel jPanel_topbar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField name;
    private javax.swing.JTextField regex;
    private javax.swing.JButton save;
    private javax.swing.JLabel valid;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    
    
    HashMap<String, Boolean> classes_OnFromList = null;
    HashMap<String, Boolean> classes_OnToList = null;
    
    /**Add a boolean flag for each class, so we can let whether this class 
     * has been selected or not by using this structure in later processing.
     */
    private HashMap<String, Boolean> getClasses(String[] _classnames ){
        HashMap<String, Boolean> classes = new HashMap<String, Boolean> ();
        
        if( _classnames != null ){
            
            // record all classes and give each of them a boolean tag 
            for(String classname : _classnames ){
                if( classname == null )
                    continue;
                if( classname.trim().length() < 1 )
                    continue;
                
                classname = classname.trim();
                
                classes.put( classname, false );                
            }
        }
        
        return classes;
    }
    
    private void listClassesOnFrom(){                
        listClasses( jList_from,  classes_OnFromList, indices_from);
        listClasses( jList_to,  classes_OnToList, indices_to);
    }
    
    private void listClasses(JList list, HashMap<String, Boolean> classes, ArrayList<Integer> indices ){
        
        // clean
        list.setListData(new Vector());
        indices.clear();
        
        if( classes == null )
            classes = getClasses( this.classNames );
        
        int count = 0;
        if( classes != null ){
            Vector<String> listdata = new Vector<String>();
            for( String key : classes.keySet() ){
                boolean highlighted = (boolean) classes.get(key);
                if( highlighted ){
                    listdata.add(key);
                    indices.add(count);
                    count++;
                }
            }
            
            for( String key : classes.keySet() ){
                boolean highlighted = (boolean) classes.get(key);
                if( !highlighted ){
                    listdata.add(key);
                    count++;
                }
            }
            
            list.setListData( listdata );
        }
        
        if( indices.size() > 0 ){
            int[] ints = new int[indices.size()];
            for(int i=0;i<indices.size();i++)
                ints[i]=indices.get(i);    
            list.setSelectedIndices(ints);
        }
        
        
    }
    

    private void markItemsByRelationRegex(String allowed) {
        if( allowed == null )
            return;
        
        String[] parts = allowed.split("\\)\\(");
        if( parts.length != 2)
            return;
        
        String fromStr = parts[0], toStr = parts[1];
        if(( fromStr ==null )||(toStr==null)||(fromStr.trim().length()<1)||(toStr.trim().length()<1))
            return;
        
        toStr = toStr.replaceAll("\\(", " ");
        toStr = toStr.replaceAll("\\)", " ");
        
        
        fromStr = fromStr.replaceAll("\\(", " ");
        fromStr = fromStr.replaceAll("\\)", " ");        
        
        String[] fromclasses = fromStr.split("\\|");
        if( classes_OnFromList == null )
            classes_OnFromList = getClasses( this.classNames );
        if( fromclasses != null ){
            for( String classname_onfromlist : fromclasses){
                if( classname_onfromlist == null )
                    continue;
                
                classname_onfromlist = classname_onfromlist.trim();                
                if( classname_onfromlist.length() < 1 )
                    continue;
                
                // System.out.println(classname_onfromlist);                
                regHighlightedClass( this.classes_OnFromList, classname_onfromlist, true);                
                                               
            }
        }
        
        String[] toclasses = toStr.split("\\|");    
        if( classes_OnToList == null )
            classes_OnToList = getClasses( this.classNames );
        if( toclasses != null ){
            for( String classname_ontolist : toclasses){
                if( classname_ontolist == null )
                    continue;
                
                classname_ontolist = classname_ontolist.trim();                
                if( classname_ontolist.length() < 1 )
                    continue;
                
                // System.out.println(classname_onfromlist);                
                regHighlightedClass( this.classes_OnToList, classname_ontolist, true);                
                                               
            }
        }
        
        
        
        
        //highlightSelections();
    }
    
    private void regHighlightedClass( HashMap<String, Boolean> classes, String classname, boolean flag ) {
        if(( classname == null ) || (classname.trim().length() <1))
            return;
        if( classes != null)
            classes.put( classname, flag );
    }
    
    private String getRegex(){
        
        // check to make sure we can make the regex
        if( ( this.indices_from == null ) 
                || ( this.indices_from.size() < 1 )
                || ( this.indices_to == null )
                || ( this.indices_to.size() < 1 )
        ) {
            commons.Tools.beep();
            commons.Tools.beep();
            return null;
        }
        
        String left = "", right = "";
        for( int i=0;i<indices_from.size();i++ ){
            int index = indices_from.get(i);
            if ( i == 0 )
                left = "(" + (String) jList_from.getModel().getElementAt( index ) +")";
            else
                left = left + "|(" + (String) jList_from.getModel().getElementAt( index ) +")";
        }
        
        for( int i=0;i<indices_to.size();i++ ){
            int index = indices_to.get(i);
            if ( i == 0 )
                right = "(" + (String) jList_to.getModel().getElementAt( index ) +")";
            else
                right = right + "|(" + (String) jList_to.getModel().getElementAt( index ) +")";
        }
        
        String regex = "(" + left + ")(" + right + ")";
        
        return regex;
                
    }
    
}

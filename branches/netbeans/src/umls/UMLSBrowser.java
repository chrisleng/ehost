/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package umls;

import UtsMetathesaurusContent.ConceptDTO;
import UtsMetathesaurusFinder.UiLabel;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.Depot;
import resultEditor.annotations.Annotation;

/**
 *
 * @author imedls
 */
public class UMLSBrowser extends javax.swing.JFrame {

    
    
    /**previous searched terms, they are listed on the combobox. */
    public static HashSet<String> searchedterms = new HashSet<String>(); 
    
    public String terms = null;
    
    private userInterface.GUI __gui;
    
    private Annotation __annotation;
    
    private final int dialogheight = 768;
    
    /**
     * Creates new form UMLSBrowser
     */
    public UMLSBrowser() {
        initComponents();
        
        this.jLabel_errmsg.setVisible( false );
        
        // set dialog size        
        this.setPreferredSize( new Dimension(598, dialogheight));
        this.setResizable( true );
        
        setTableAttribute();
    }
    
    
    /**Get latest UMLS Parameter from the static memory and update them on the 
     * tab of "UMLS Server Info" on this dialog.
     */
    private void updateUMLSParametersOnTab(){
        // umls user name
        this.jLabel_UMLSUserName.setText( env.Parameters.umls_username );
        // umls language that we use to filter the result.
        this.JLabel_UMLSSearchingLanguage.setText(  "ENGLISH (ENG)" );
        // umls password: show '*' 
        if( ( env.Parameters.umls_encryptorPassword==null ) 
                || (env.Parameters.umls_encryptorPassword.trim().length() < 0)
                || (env.Parameters.umls_decryptedPassword == null )
                || (env.Parameters.umls_encryptorPassword.trim().length() < 0)
                
               
                )
            this.jLabel_UMLSUserPassw0rd.setText(null);
        else{
            int length_of_password = env.Parameters.umls_decryptedPassword.length();
            String markedPassword = "";
            for( int i=0;i<length_of_password; i++ ){
                markedPassword = markedPassword + "*";
            }
            this.jLabel_UMLSUserPassw0rd.setText( markedPassword );
        }
    }
    
    /**create new form UMLSBrowser.*/
    public UMLSBrowser(final String terms, Annotation _annotation, userInterface.GUI _gui) {
        initComponents();
        
        this.jLabel_errmsg.setVisible( false );
        
        this.__gui = _gui;
        this.__annotation = _annotation;
        
        // set dialog size
        this.setSize(new Dimension(598, dialogheight));
        this.setPreferredSize( new Dimension(598, dialogheight));
        this.setResizable( true );
        
        this.setLocationRelativeTo( _gui );
        
        this.terms = terms;
        if( this.terms != null ){
        	this.terms = this.terms.trim();
        }
        
        UMLSBrowser.searchedterms.add( this.terms ); // recorded it
        
        
        // this.jComboBox1.setModel( new ComboBoxModel(searchedterms) );
        for(String term: searchedterms ){
        	this.jComboBox1.addItem( term );
        }
        
        // set the display text
        this.jComboBox1.getModel().setSelectedItem( this.terms );
        
        
        
        // start searching 
        new Thread() {
			@Override
			public void run() {	
                            // list umls setting information of eHOST on the tab "info"
                            updateUMLSParametersOnTab();
                            
			    findConcepts( terms );                  
			}
		}.start();
        
    }
    
    private void getConcept(String cuinumber){
        try{
            
        } catch( Exception ex ){
            
        }
    }
    
    /**start searching concepts for the given term(s).*/
    private void findConcepts(String terms){
        try{
            
        
        this.jLabel2.setIcon( jLabel3.getIcon() );
    	if(( terms == null ) || (terms.trim().length()<1))
    		return;    	
        
        String ticketGrantingTicket = null;
        
        // init and handshake
        GetCUI getcui = new GetCUI();               
        try{
            ticketGrantingTicket = getcui.getGrantingTicket();        
        }catch(Exception ex){
            String errstr = ex.getMessage();
            if(( errstr != null )&&( errstr.trim().length()>15 )){
                String codestr = errstr.substring( 5, 15);
                if(codestr.compareTo("1212181357")==0){
                    this.jLabel_errmsg.setText( "<html><font color=red>ERROR:  Please setup your UMLS/UTS account!</font></html>" );
                    this.jLabel_errmsg.setVisible( true );
                }else if(codestr.compareTo("1212181359")==0){
                    this.jLabel_errmsg.setText( "<html><font color=red>ERROR:  Can't connect to UMLS/UTS server!</font></html>" );
                    this.jLabel_errmsg.setVisible( true );
                }else if(codestr.compareTo("1212181355")==0){
                    this.jLabel_errmsg.setText( "<html><font color=red>ERROR:  incorrect UTS user name/password!</font></html>" );
                    this.jLabel_errmsg.setVisible( true );
                }else {
                    this.jLabel_errmsg.setText( "<html><font color=red>" + ex.getMessage() + "</font></html>" );
                    this.jLabel_errmsg.setVisible( true );
                }
            }
        }
        
        
        // log.LoggingToFile.log(Level.SEVERE, "UMLS: single user ticket : result = [" + singleUseTicket + "]" );
        
        List<UiLabel> results = getcui.findConcepts(ticketGrantingTicket, terms);
        
        //Document doc = this.jTextPane1.getDocument();
        //String strings = new String();
        
        // clear old rows
        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
        tableModel.setRowCount(0);
        
        if(results!=null){
            for (int i = 0; i < results.size(); i++) {
                UiLabel myUiLabel = results.get(i);
                String ui = myUiLabel.getUi();
                String label = myUiLabel.getLabel();
                //String source = myUiLabel.getRootSource();
            
//                strings = strings + "CUI: " + ui + "\n";
                // System.out.println("CUI: " + ui);
//                strings = strings + "Label: " + label + "\n\n";
                // System.out.println("Name: " + label);                        
                //strings = strings + "Source: " + source + "\n\n";

                // 填充数据

                Object[] arr = new Object[3];
                arr[0] = ui;
                arr[1] = label;
                //if( __annotation != null )
                //    arr[2] = isThisSaved(ui);
                //else
                //    arr[2] = false;
                arr[2] = false;
                tableModel.addRow(arr);

            }
            
        }
        // update table
                jTable1.invalidate();
        
        
        
            //doc.remove(0, doc.getLength());
            //doc.insertString( doc.getLength() , strings, null);
        }catch(Exception ex){
        	System.out.println("fail to list found concepts from UMLS server." + ex.toString() );
        	ex.printStackTrace();
        }
        
        this.jLabel2.setIcon( null );

        setTableAttribute();
        
        //ButtonColumn   buttonColumn   =   new   ButtonColumn(jTable1,   2); 
    }
    
    
    private boolean isThisSaved(String cui){
        if((__annotation==null)||(__annotation.annotationclass==null))
            return false;
        
        String classname = __annotation.annotationclass;

        //for ( : allClasses) {
        try {// record class names
            Depot classdepot = new Depot();
            

            
                resultEditor.annotationClasses.AnnotationClass thisclass = classdepot.getAnnotatedClass(classname);

                if (thisclass == null) {
                    return false;
                }

                boolean found = false;
                for (AttributeSchemaDef att : thisclass.getAttributes()) {
                    if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                        continue;
                    }
                    if (att.isCUICodeNLabel) {
                        if(att.getAllAllowedEntries()==null)
                            continue;
                        else{
                            for(String value : att.getAllAllowedEntries() ){
                        String regex = "CUI: \\[" + cui + "\\];" ;
                        Matcher matcher;
                         matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( value );
                        found = matcher.find();
                        if( found )
                            return true;
                        }
                        }
                    }
                }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
        //}



        
    }
    
    /**Set basic table attribute of the table of CUI results, such as width of 
     * each column, some column is sorted, etc.
     */
    private void setTableAttribute(){
        TableColumn firsetColumn = jTable1.getColumnModel().getColumn(0);
        firsetColumn.setPreferredWidth(80);
        firsetColumn.setWidth( 80 );
        firsetColumn.setMaxWidth(80);
        firsetColumn.setMinWidth(80);
        
        //TableColumn thirdColumn = jTable1.getColumnModel().getColumn(2);
        //thirdColumn.setPreferredWidth(90);
        //thirdColumn.setWidth( 90 );
        //thirdColumn.setMaxWidth(90);
        //thirdColumn.setMinWidth(90);
        
        //TableColumn fourthColumn = jTable1.getColumnModel().getColumn(3);
        //fourthColumn.setPreferredWidth(90);
        //fourthColumn.setWidth( 90 );
        //fourthColumn.setMaxWidth(90);
        //fourthColumn.setMinWidth(90);
        TableColumn fourthColumn = jTable1.getColumnModel().getColumn(2);
        fourthColumn.setPreferredWidth(90);
        fourthColumn.setWidth( 90 );
        fourthColumn.setMaxWidth(90);
        fourthColumn.setMinWidth(90);
        
        
        //Set row height and margin
        jTable1.setRowHeight(26);
        jTable1.setRowMargin(2);
        
        //for(int i = 0; i< table.getColumnCount(); i++)
        //{
        TableColumn column_Button = jTable1.getColumnModel().getColumn(2);
        
        ColumnButton cbutton =  new ColumnButton(jTable1, this.__annotation, this.__gui);
        cbutton.setRendererNEditor( column_Button );
        
        //TableColumn column_xButton = jTable1.getColumnModel().getColumn(2);
        //ColumnCheckbox xbutton =  new ColumnCheckbox(jTable1, this.__annotation, this.__gui);
        //xbutton.setRendererNEditor( column_xButton );
        
        //Set the cell renderer
        //PropertyCellRenderer_jButton renderer_jbutton = new PropertyCellRenderer_jButton();
        
        //TableCellEditor editor = new cellEditor_jbutton();
        //column_Button.setCellEditor(editor);
        //column_left.setCellEditor( new ButtonColumn( jTable1, 2 ) );
        //Set the cell editor
        //TableCellEditor editor = new PropertyCellEditor(this);
         //column_left.setCellEditor(editor);
        
        //}
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel12 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username = new javax.swing.JPanel();
        jLabel_UMLSUserName = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_password = new javax.swing.JPanel();
        jLabel_UMLSUserPassw0rd = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel88 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_password1 = new javax.swing.JPanel();
        JLabel_UMLSSearchingLanguage = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel89 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_password2 = new javax.swing.JPanel();
        JLabel_UMLSSearchingLanguage1 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username1 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username2 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jCheckBox_CPT = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username3 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jCheckBox_HCPCS = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username4 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jCheckBox_ICD10CM = new javax.swing.JCheckBox();
        jLabel15 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username5 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jCheckBox_ICD10PCS = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username6 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jCheckBox_LNC = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username7 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jCheckBox_MedDRA = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username8 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jCheckBox_MEDLINEPLUS = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username9 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jCheckBox_MeSH = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username10 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jCheckBox_RXNORM = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username11 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jCheckBox_SCTSPA = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username12 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jCheckBox_SCTUSX = new javax.swing.JCheckBox();
        jLabel23 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username13 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jCheckBox_SNOMEDCT = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username14 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jCheckBox_UMD = new javax.swing.JCheckBox();
        jLabel25 = new javax.swing.JLabel();
        jPanel_UnderLineUnit_username15 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel_errmsg = new javax.swing.JLabel();

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/umls/busy1.gif"))); // NOI18N
        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(240, 240, 240));

        jTabbedPane1.setBackground(new java.awt.Color(240, 240, 240));
        jTabbedPane1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(234, 234, 234));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(234, 234, 234)));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(234, 234, 234));

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel1.setText("Term(s):");

        jComboBox1.setEditable(true);
        jComboBox1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(0, 0, 204));
        jComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox1KeyReleased(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel2.setText("Results:");

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 432, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1))
            .add(jLabel2)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel2)
                .addContainerGap())
        );

        jPanel2.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel11.setBackground(new java.awt.Color(234, 234, 234));
        jPanel11.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jPanel11, java.awt.BorderLayout.PAGE_END);

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 153)));

        jTable1.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "CUI#", "Concept", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoCreateRowSorter(true);
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setRowHeight(14);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setShowGrid(false);
        jScrollPane3.setViewportView(jTable1);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Search Concepts", jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 6, 0, 6, new java.awt.Color(234, 234, 234)));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 102)));

        jTextPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        jTextPane2.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jScrollPane2.setViewportView(jTextPane2);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel12.setBackground(new java.awt.Color(234, 234, 234));

        jLabel7.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel7.setText("CUI#");

        jButton3.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jButton3.setText("Search");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        jLabel8.setText("Results:");

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .add(jLabel8)
                .addContainerGap())
            .add(jPanel12Layout.createSequentialGroup()
                .add(jLabel7)
                .add(18, 18, 18)
                .add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(jButton3))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(jButton3)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel8)
                .addContainerGap())
        );

        jPanel1.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setBackground(new java.awt.Color(234, 234, 234));
        jPanel13.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("Search by CUI code", jPanel1);

        jPanel8.setBackground(new java.awt.Color(234, 234, 234));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 12, 0, 12, new java.awt.Color(234, 234, 234)));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(234, 234, 234));
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 12, 0, 12, new java.awt.Color(234, 234, 234)));
        jPanel14.setLayout(new java.awt.GridLayout(19, 2, 2, 2));

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel9.setText("UMLA/UTS User Name");
        jPanel14.add(jLabel9);

        jPanel_UnderLineUnit_username.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username.setLayout(new java.awt.BorderLayout());

        jLabel_UMLSUserName.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel_UMLSUserName.setText("ENGLISH");
        jPanel_UnderLineUnit_username.add(jLabel_UMLSUserName, java.awt.BorderLayout.CENTER);

        jPanel16.setBackground(new java.awt.Color(0, 51, 102));
        jPanel16.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel16.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel16Layout = new org.jdesktop.layout.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username.add(jPanel16, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel_UnderLineUnit_username);

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel11.setText("Password");
        jPanel14.add(jLabel11);

        jPanel_UnderLineUnit_password.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_password.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_password.setLayout(new java.awt.BorderLayout());

        jLabel_UMLSUserPassw0rd.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jLabel_UMLSUserPassw0rd.setText("ENGLISH");
        jPanel_UnderLineUnit_password.add(jLabel_UMLSUserPassw0rd, java.awt.BorderLayout.CENTER);

        jPanel17.setBackground(new java.awt.Color(0, 51, 102));
        jPanel17.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_password.add(jPanel17, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel_UnderLineUnit_password);

        jLabel88.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel88.setText("Searching Language");
        jPanel14.add(jLabel88);

        jPanel_UnderLineUnit_password1.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_password1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_password1.setLayout(new java.awt.BorderLayout());

        JLabel_UMLSSearchingLanguage.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        JLabel_UMLSSearchingLanguage.setText("ENGLISH");
        jPanel_UnderLineUnit_password1.add(JLabel_UMLSSearchingLanguage, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(0, 51, 102));
        jPanel18.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel18Layout = new org.jdesktop.layout.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_password1.add(jPanel18, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel_UnderLineUnit_password1);

        jLabel89.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel89.setText("UTS Version");
        jPanel14.add(jLabel89);

        jPanel_UnderLineUnit_password2.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_password2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_password2.setLayout(new java.awt.BorderLayout());

        JLabel_UMLSSearchingLanguage1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        JLabel_UMLSSearchingLanguage1.setText("2012AB");
        jPanel_UnderLineUnit_password2.add(JLabel_UMLSSearchingLanguage1, java.awt.BorderLayout.CENTER);

        jPanel19.setBackground(new java.awt.Color(0, 51, 102));
        jPanel19.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel19Layout = new org.jdesktop.layout.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_password2.add(jPanel19, java.awt.BorderLayout.SOUTH);

        jPanel14.add(jPanel_UnderLineUnit_password2);

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel10.setText("Resource");
        jPanel14.add(jLabel10);

        jPanel_UnderLineUnit_username1.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username1.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username1.setLayout(new java.awt.BorderLayout());

        jPanel20.setBackground(new java.awt.Color(0, 51, 102));
        jPanel20.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel20.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel20Layout = new org.jdesktop.layout.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username1.add(jPanel20, java.awt.BorderLayout.SOUTH);

        jCheckBox5.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox5.setText("<html><b>Enable filter on resources</b></html>");
        jCheckBox5.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox5.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox5.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username1.add(jCheckBox5, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username1);

        jLabel12.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel12.setText("    ");
        jPanel14.add(jLabel12);

        jPanel_UnderLineUnit_username2.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username2.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username2.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(0, 51, 102));
        jPanel21.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel21Layout = new org.jdesktop.layout.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username2.add(jPanel21, java.awt.BorderLayout.SOUTH);

        jCheckBox_CPT.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_CPT.setText("CPT (Current Procedural Terminology) ");
        jCheckBox_CPT.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_CPT.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_CPT.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_CPT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_CPTActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username2.add(jCheckBox_CPT, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username2);

        jLabel13.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel13.setText("    ");
        jPanel14.add(jLabel13);

        jPanel_UnderLineUnit_username3.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username3.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username3.setLayout(new java.awt.BorderLayout());

        jPanel22.setBackground(new java.awt.Color(0, 51, 102));
        jPanel22.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel22Layout = new org.jdesktop.layout.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username3.add(jPanel22, java.awt.BorderLayout.SOUTH);

        jCheckBox_HCPCS.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_HCPCS.setText("HCPCS (Healthcare Common  Procedure Coding System) ");
        jCheckBox_HCPCS.setToolTipText("HCPCS (Healthcare Common  Procedure Coding System) ");
        jCheckBox_HCPCS.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_HCPCS.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_HCPCS.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_HCPCS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_HCPCSActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username3.add(jCheckBox_HCPCS, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username3);

        jLabel14.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel14.setText("    ");
        jPanel14.add(jLabel14);

        jPanel_UnderLineUnit_username4.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username4.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username4.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(0, 51, 102));
        jPanel23.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel23Layout = new org.jdesktop.layout.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username4.add(jPanel23, java.awt.BorderLayout.SOUTH);

        jCheckBox_ICD10CM.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_ICD10CM.setText("ICD10CM (International Classification  of Diseases, 10th Revision, Clinical  Modification) ");
        jCheckBox_ICD10CM.setToolTipText("ICD10CM (International Classification  of Diseases, 10th Revision, Clinical  Modification) ");
        jCheckBox_ICD10CM.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_ICD10CM.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_ICD10CM.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_ICD10CM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_ICD10CMActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username4.add(jCheckBox_ICD10CM, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username4);

        jLabel15.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel15.setText("    ");
        jPanel14.add(jLabel15);

        jPanel_UnderLineUnit_username5.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username5.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username5.setLayout(new java.awt.BorderLayout());

        jPanel24.setBackground(new java.awt.Color(0, 51, 102));
        jPanel24.setPreferredSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel24Layout = new org.jdesktop.layout.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username5.add(jPanel24, java.awt.BorderLayout.SOUTH);

        jCheckBox_ICD10PCS.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_ICD10PCS.setText("ICD10PCS (International Classification  of Diseases, 10th Revision, Procedure  Coding System) ");
        jCheckBox_ICD10PCS.setToolTipText("ICD10PCS (International Classification  of Diseases, 10th Revision, Procedure  Coding System) ");
        jCheckBox_ICD10PCS.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_ICD10PCS.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_ICD10PCS.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_ICD10PCS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_ICD10PCSActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username5.add(jCheckBox_ICD10PCS, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username5);

        jLabel16.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel16.setText("    ");
        jPanel14.add(jLabel16);

        jPanel_UnderLineUnit_username6.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username6.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username6.setLayout(new java.awt.BorderLayout());

        jPanel25.setBackground(new java.awt.Color(0, 51, 102));
        jPanel25.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel25.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel25Layout = new org.jdesktop.layout.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username6.add(jPanel25, java.awt.BorderLayout.SOUTH);

        jCheckBox_LNC.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_LNC.setText("LNC (Logical Observation Identifier  Names and Codes)");
        jCheckBox_LNC.setToolTipText("LNC (Logical Observation Identifier  Names and Codes)");
        jCheckBox_LNC.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_LNC.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_LNC.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_LNC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_LNCActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username6.add(jCheckBox_LNC, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username6);

        jLabel17.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Medicine");
        jPanel14.add(jLabel17);

        jPanel_UnderLineUnit_username7.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username7.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username7.setLayout(new java.awt.BorderLayout());

        jPanel26.setBackground(new java.awt.Color(0, 51, 102));
        jPanel26.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel26.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel26Layout = new org.jdesktop.layout.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username7.add(jPanel26, java.awt.BorderLayout.SOUTH);

        jCheckBox_MedDRA.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_MedDRA.setText("MedDRA (Medical Dictionary for  Regulatory Activities Terminology) and Translations ");
        jCheckBox_MedDRA.setToolTipText("MedDRA (Medical Dictionary for  Regulatory Activities Terminology) and Translations ");
        jCheckBox_MedDRA.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_MedDRA.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_MedDRA.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_MedDRA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_MedDRAActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username7.add(jCheckBox_MedDRA, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username7);

        jLabel18.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel18.setText("    ");
        jPanel14.add(jLabel18);

        jPanel_UnderLineUnit_username8.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username8.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username8.setLayout(new java.awt.BorderLayout());

        jPanel27.setBackground(new java.awt.Color(0, 51, 102));
        jPanel27.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel27.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel27Layout = new org.jdesktop.layout.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username8.add(jPanel27, java.awt.BorderLayout.SOUTH);

        jCheckBox_MEDLINEPLUS.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_MEDLINEPLUS.setText("MEDLINEPLUS (MedlinePlus Health  Topics) ");
        jCheckBox_MEDLINEPLUS.setToolTipText("MEDLINEPLUS (MedlinePlus Health  Topics) ");
        jCheckBox_MEDLINEPLUS.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_MEDLINEPLUS.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_MEDLINEPLUS.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_MEDLINEPLUS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_MEDLINEPLUSActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username8.add(jCheckBox_MEDLINEPLUS, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username8);

        jLabel19.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel19.setText("    ");
        jPanel14.add(jLabel19);

        jPanel_UnderLineUnit_username9.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username9.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username9.setLayout(new java.awt.BorderLayout());

        jPanel28.setBackground(new java.awt.Color(0, 51, 102));
        jPanel28.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel28.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel28Layout = new org.jdesktop.layout.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username9.add(jPanel28, java.awt.BorderLayout.SOUTH);

        jCheckBox_MeSH.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_MeSH.setText("MeSH (Medical Subject Headings) and  Translations ");
        jCheckBox_MeSH.setToolTipText("MeSH (Medical Subject Headings) and  Translations ");
        jCheckBox_MeSH.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_MeSH.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_MeSH.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_MeSH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_MeSHActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username9.add(jCheckBox_MeSH, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username9);

        jLabel20.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel20.setText("    ");
        jPanel14.add(jLabel20);

        jPanel_UnderLineUnit_username10.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username10.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username10.setLayout(new java.awt.BorderLayout());

        jPanel29.setBackground(new java.awt.Color(0, 51, 102));
        jPanel29.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel29.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel29Layout = new org.jdesktop.layout.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username10.add(jPanel29, java.awt.BorderLayout.SOUTH);

        jCheckBox_RXNORM.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_RXNORM.setText("RXNORM (RXNORM Vocabulary) ");
        jCheckBox_RXNORM.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_RXNORM.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_RXNORM.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_RXNORM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_RXNORMActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username10.add(jCheckBox_RXNORM, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username10);

        jLabel21.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel21.setText("    ");
        jPanel14.add(jLabel21);

        jPanel_UnderLineUnit_username11.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username11.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username11.setLayout(new java.awt.BorderLayout());

        jPanel30.setBackground(new java.awt.Color(0, 51, 102));
        jPanel30.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel30.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel30Layout = new org.jdesktop.layout.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username11.add(jPanel30, java.awt.BorderLayout.SOUTH);

        jCheckBox_SCTSPA.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_SCTSPA.setText("SCTSPA (SNOMED Clinical Terms, Spanish Language Edition)");
        jCheckBox_SCTSPA.setToolTipText("SCTSPA (SNOMED Clinical Terms, Spanish Language Edition)");
        jCheckBox_SCTSPA.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_SCTSPA.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_SCTSPA.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_SCTSPA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_SCTSPAActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username11.add(jCheckBox_SCTSPA, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username11);

        jLabel22.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel22.setText("    ");
        jPanel14.add(jLabel22);

        jPanel_UnderLineUnit_username12.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username12.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username12.setLayout(new java.awt.BorderLayout());

        jPanel31.setBackground(new java.awt.Color(0, 51, 102));
        jPanel31.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel31.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel31Layout = new org.jdesktop.layout.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username12.add(jPanel31, java.awt.BorderLayout.SOUTH);

        jCheckBox_SCTUSX.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_SCTUSX.setText("SCTUSX (US Extension to SNOMED CT)");
        jCheckBox_SCTUSX.setToolTipText("SCTUSX (US Extension to SNOMED CT)");
        jCheckBox_SCTUSX.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_SCTUSX.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_SCTUSX.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_SCTUSX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_SCTUSXActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username12.add(jCheckBox_SCTUSX, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username12);

        jLabel23.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel23.setText("    ");
        jPanel14.add(jLabel23);

        jPanel_UnderLineUnit_username13.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username13.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username13.setLayout(new java.awt.BorderLayout());

        jPanel32.setBackground(new java.awt.Color(0, 51, 102));
        jPanel32.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel32.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel32Layout = new org.jdesktop.layout.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username13.add(jPanel32, java.awt.BorderLayout.SOUTH);

        jCheckBox_SNOMEDCT.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_SNOMEDCT.setText("SNOMEDCT (SNOMED Clinical Terms) ");
        jCheckBox_SNOMEDCT.setToolTipText("SNOMEDCT (SNOMED Clinical Terms) ");
        jCheckBox_SNOMEDCT.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_SNOMEDCT.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_SNOMEDCT.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_SNOMEDCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_SNOMEDCTActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username13.add(jCheckBox_SNOMEDCT, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username13);

        jLabel24.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel24.setText("    ");
        jPanel14.add(jLabel24);

        jPanel_UnderLineUnit_username14.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username14.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username14.setLayout(new java.awt.BorderLayout());

        jPanel33.setBackground(new java.awt.Color(0, 51, 102));
        jPanel33.setPreferredSize(new java.awt.Dimension(100, 1));
        jPanel33.setSize(new java.awt.Dimension(100, 1));

        org.jdesktop.layout.GroupLayout jPanel33Layout = new org.jdesktop.layout.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 430, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1, Short.MAX_VALUE)
        );

        jPanel_UnderLineUnit_username14.add(jPanel33, java.awt.BorderLayout.SOUTH);

        jCheckBox_UMD.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jCheckBox_UMD.setText("UMD (UMDNS: product category thesaurus) ");
        jCheckBox_UMD.setToolTipText("UMD (UMDNS: product category thesaurus) ");
        jCheckBox_UMD.setMaximumSize(new java.awt.Dimension(400, 20));
        jCheckBox_UMD.setMinimumSize(new java.awt.Dimension(1, 20));
        jCheckBox_UMD.setPreferredSize(new java.awt.Dimension(100, 20));
        jCheckBox_UMD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox_UMDActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username14.add(jCheckBox_UMD, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username14);

        jLabel25.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        jLabel25.setText("    ");
        jPanel14.add(jLabel25);

        jPanel_UnderLineUnit_username15.setBackground(new java.awt.Color(234, 234, 234));
        jPanel_UnderLineUnit_username15.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        jPanel_UnderLineUnit_username15.setLayout(new java.awt.BorderLayout());

        jButton4.setText("save this setting");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel_UnderLineUnit_username15.add(jButton4, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel_UnderLineUnit_username15);

        jPanel8.add(jPanel14, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab(" Setting & Info", jPanel8);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(250, 250, 250));
        jPanel4.setMaximumSize(new java.awt.Dimension(2147483647, 55));
        jPanel4.setPreferredSize(new java.awt.Dimension(560, 55));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/umls1.gif"))); // NOI18N
        jLabel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 3, new java.awt.Color(250, 250, 250)));
        jPanel4.add(jLabel4, java.awt.BorderLayout.EAST);

        jPanel6.setBackground(new java.awt.Color(250, 250, 250));

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("<html>Return a list UMLS CUI codes for this span</html>");

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Searching on UMLS Server");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel6))
                .addContainerGap(664, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(0, 51, 102));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 2));

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 932, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 2, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(234, 234, 234));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(234, 234, 234));

        jButton2.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                .add(0, 829, Short.MAX_VALUE)
                .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(jButton2))
        );

        jPanel7.add(jPanel9, java.awt.BorderLayout.PAGE_END);

        jLabel_errmsg.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLabel_errmsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_errmsg.setText("ERROR: Please setup your UMLS/UTS account!");
        jPanel7.add(jLabel_errmsg, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel7, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    	searchterm();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyReleased
        
    }//GEN-LAST:event_jComboBox1KeyReleased

    /**close the dialog if use clicked on the button of "close". */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox1KeyPressed
        if( evt.getKeyChar() == '\n' )
            searchterm();
        
        System.out.println("["+ evt.getKeyChar() + "]");// TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        getConcept();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        boolean isenable = false;
        if( this.jCheckBox5.isSelected() ){
            isenable = true;
        }else{
            isenable = false;
        }
        
        env.Parameters.UMLSSetting.UMLSfilterOn = isenable;
        
        this.jCheckBox_CPT.setEnabled( isenable );
        this.jCheckBox_HCPCS.setEnabled( isenable );
        this.jCheckBox_ICD10CM.setEnabled( isenable );
        this.jCheckBox_ICD10PCS.setEnabled( isenable );
        this.jCheckBox_LNC.setEnabled( isenable );
        this.jCheckBox_MEDLINEPLUS.setEnabled( isenable );
        this.jCheckBox_MeSH.setEnabled( isenable );
        this.jCheckBox_MedDRA.setEnabled( isenable );
        this.jCheckBox_RXNORM.setEnabled( isenable );
        this.jCheckBox_SCTSPA.setEnabled( isenable );
        this.jCheckBox_SCTUSX.setEnabled( isenable );
        this.jCheckBox_SNOMEDCT.setEnabled( isenable );
        this.jCheckBox_UMD.setEnabled( isenable );
        
        
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        config.system.SysConf.saveSystemConfigure();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (this.jTabbedPane1.isDisplayable()) {
            
            if (jTabbedPane1.getSelectedIndex() == 2) {

                boolean selected = env.Parameters.UMLSSetting.UMLSfilterOn;
                this.jCheckBox5.setSelected(selected);

                this.jCheckBox_CPT.setEnabled(selected);
                this.jCheckBox_HCPCS.setEnabled(selected);
                this.jCheckBox_ICD10CM.setEnabled(selected);
                this.jCheckBox_ICD10PCS.setEnabled(selected);
                this.jCheckBox_LNC.setEnabled(selected);
                this.jCheckBox_MEDLINEPLUS.setEnabled(selected);
                this.jCheckBox_MeSH.setEnabled(selected);
                this.jCheckBox_MedDRA.setEnabled(selected);
                this.jCheckBox_RXNORM.setEnabled(selected);
                this.jCheckBox_SCTSPA.setEnabled(selected);
                this.jCheckBox_SCTUSX.setEnabled(selected);
                this.jCheckBox_SNOMEDCT.setEnabled(selected);
                this.jCheckBox_UMD.setEnabled(selected);
                
                if( env.Parameters.UMLSSetting.CPT ){
                    this.jCheckBox_CPT.setSelected(true);
                }else{
                    this.jCheckBox_CPT.setSelected(false);
                }
                
                if( env.Parameters.UMLSSetting.HCPCS ){
                    this.jCheckBox_HCPCS.setSelected(true);
                }else{
                    this.jCheckBox_HCPCS.setSelected(false);
                }
                
                
                if( env.Parameters.UMLSSetting.ICD10CM ){
                    this.jCheckBox_ICD10CM.setSelected(true);
                }else{
                    this.jCheckBox_ICD10CM.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.ICD10PCS ){
                    this.jCheckBox_ICD10PCS.setSelected(true);
                }else{
                    this.jCheckBox_ICD10PCS.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.LNC ){
                    this.jCheckBox_LNC.setSelected(true);
                }else{
                    this.jCheckBox_LNC.setSelected(false);
                }
                
                                
                
                if( env.Parameters.UMLSSetting.MEDLINEPLUS ){
                    this.jCheckBox_MEDLINEPLUS.setSelected(true);
                }else{
                    this.jCheckBox_MEDLINEPLUS.setSelected(false);
                }
                
                
                if( env.Parameters.UMLSSetting.MeSH ){
                    this.jCheckBox_MeSH.setSelected(true);
                }else{
                    this.jCheckBox_MeSH.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.MedDRA ){
                    this.jCheckBox_MedDRA.setSelected(true);
                }else{
                    this.jCheckBox_MedDRA.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.RXNORM ){
                    this.jCheckBox_RXNORM.setSelected(true);
                }else{
                    this.jCheckBox_RXNORM.setSelected(false);
                }
                
                
                
                
                if( env.Parameters.UMLSSetting.SCTSPA ){
                    this.jCheckBox_SCTSPA.setSelected(true);
                }else{
                    this.jCheckBox_SCTSPA.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.SCTUSX ){
                    this.jCheckBox_SCTUSX.setSelected(true);
                }else{
                    this.jCheckBox_SCTUSX.setSelected(false);
                }
                
                
                
                if( env.Parameters.UMLSSetting.SNOMEDCT ){
                    this.jCheckBox_SNOMEDCT.setSelected(true);
                }else{
                    this.jCheckBox_SNOMEDCT.setSelected(false);
                }

                
                if( env.Parameters.UMLSSetting.UMD ){
                    this.jCheckBox_UMD.setSelected(true);
                }else{
                    this.jCheckBox_UMD.setSelected(false);
                }
                
            }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jCheckBox_CPTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_CPTActionPerformed
        env.Parameters.UMLSSetting.CPT = jCheckBox_CPT.isSelected();
    }//GEN-LAST:event_jCheckBox_CPTActionPerformed

    private void jCheckBox_HCPCSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_HCPCSActionPerformed
        env.Parameters.UMLSSetting.HCPCS = jCheckBox_HCPCS.isSelected();
    }//GEN-LAST:event_jCheckBox_HCPCSActionPerformed

    private void jCheckBox_ICD10CMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_ICD10CMActionPerformed
        env.Parameters.UMLSSetting.ICD10CM = jCheckBox_ICD10CM.isSelected();
    }//GEN-LAST:event_jCheckBox_ICD10CMActionPerformed

    private void jCheckBox_ICD10PCSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_ICD10PCSActionPerformed
        env.Parameters.UMLSSetting.ICD10PCS = jCheckBox_ICD10PCS.isSelected();
    }//GEN-LAST:event_jCheckBox_ICD10PCSActionPerformed

    private void jCheckBox_LNCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_LNCActionPerformed
        env.Parameters.UMLSSetting.LNC = jCheckBox_LNC.isSelected();
    }//GEN-LAST:event_jCheckBox_LNCActionPerformed

    private void jCheckBox_MedDRAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_MedDRAActionPerformed
        env.Parameters.UMLSSetting.MedDRA = jCheckBox_MedDRA.isSelected();
    }//GEN-LAST:event_jCheckBox_MedDRAActionPerformed

    private void jCheckBox_MEDLINEPLUSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_MEDLINEPLUSActionPerformed
        env.Parameters.UMLSSetting.MEDLINEPLUS = jCheckBox_MEDLINEPLUS.isSelected();  
    }//GEN-LAST:event_jCheckBox_MEDLINEPLUSActionPerformed

    private void jCheckBox_MeSHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_MeSHActionPerformed
        env.Parameters.UMLSSetting.MeSH = jCheckBox_MeSH.isSelected();   
    }//GEN-LAST:event_jCheckBox_MeSHActionPerformed

    private void jCheckBox_RXNORMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_RXNORMActionPerformed
        env.Parameters.UMLSSetting.RXNORM = jCheckBox_RXNORM.isSelected();  
    }//GEN-LAST:event_jCheckBox_RXNORMActionPerformed

    private void jCheckBox_SCTSPAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_SCTSPAActionPerformed
        env.Parameters.UMLSSetting.SCTSPA = jCheckBox_SCTSPA.isSelected();  
    }//GEN-LAST:event_jCheckBox_SCTSPAActionPerformed

    private void jCheckBox_SCTUSXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_SCTUSXActionPerformed
        env.Parameters.UMLSSetting.SCTUSX = jCheckBox_SCTUSX.isSelected(); 
    }//GEN-LAST:event_jCheckBox_SCTUSXActionPerformed

    private void jCheckBox_SNOMEDCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_SNOMEDCTActionPerformed
        env.Parameters.UMLSSetting.SNOMEDCT = jCheckBox_SNOMEDCT.isSelected(); 
    }//GEN-LAST:event_jCheckBox_SNOMEDCTActionPerformed

    private void jCheckBox_UMDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox_UMDActionPerformed
        env.Parameters.UMLSSetting.UMD = jCheckBox_UMD.isSelected();
    }//GEN-LAST:event_jCheckBox_UMDActionPerformed

    
    private void getConcept() {
        final String cui = jTextField1.getText();
        if ((cui == null) || (cui.trim().length() < 1)) {
            return;
        }
        
        jTextField1.setText( cui.toUpperCase() );

        jTextPane2.setText(null);


        new Thread() {

            @Override
            public void run() {
                getConcepts(cui.trim().toUpperCase());
                jLabel8.setIcon( null  );
                jLabel8.updateUI();
                repaint();
            }}.start();
           

    }
    
    
    private void getConcepts(String cui) {
        try {


            this.jLabel8.setIcon(jLabel3.getIcon());
            if ((cui == null) || (cui.trim().length() < 1)) {
                return;
            }

            // init and handshake
            GetCUI getcui = new GetCUI();
            String singleUseTicket = getcui.getGrantingTicket();
            // log.LoggingToFile.log(Level.SEVERE, "UMLS: single user ticket : result = [" + singleUseTicket + "]" );

            ConceptDTO results = getcui.getConcept(singleUseTicket, cui);

            Document doc = this.jTextPane2.getDocument();
            String strings = new String();



            if (results != null) {


                strings = strings + "CUI: " + results.getUi() + "\n";
                // System.out.println("CUI: " + ui);
                strings = strings + "Label: " + results.getDefaultPreferredName() + "\n\n";
                // System.out.println("Name: " + label);


            }



            doc.remove(0, doc.getLength());
            doc.insertString(doc.getLength(), strings, null);
        } catch (Exception ex) {
            System.out.println("fail to list found concepts from UMLS server." + ex.toString());
            ex.printStackTrace();
        }

        this.jLabel8.setIcon(null);
    }
    

     
     
    private void searchterm(){
        this.jLabel_errmsg.setVisible( false );
        this.jLabel2.setIcon( jLabel3.getIcon() );
        this.jLabel2.updateUI();
        this.repaint();
        
    	Object selectobj = this.jComboBox1.getSelectedItem();
        String term = null;
        try{
        	term = (String) selectobj;
        }catch(Exception ex){
        	term = null;
        }
        
        if(( term == null )||( term.trim().length()<1 )){
            DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();
            tableModel.setRowCount(0);
            return;
        }
        
        searchedterms.add( term.trim() );
        
        final String  t = term.trim();
        new Thread() {

            @Override
            public void run() {
                findConcepts( t );
                jLabel2.setIcon( null );
            jLabel2.updateUI();
            repaint();
            }

            
        }.start();
                        
        
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel_UMLSSearchingLanguage;
    private javax.swing.JLabel JLabel_UMLSSearchingLanguage1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox_CPT;
    private javax.swing.JCheckBox jCheckBox_HCPCS;
    private javax.swing.JCheckBox jCheckBox_ICD10CM;
    private javax.swing.JCheckBox jCheckBox_ICD10PCS;
    private javax.swing.JCheckBox jCheckBox_LNC;
    private javax.swing.JCheckBox jCheckBox_MEDLINEPLUS;
    private javax.swing.JCheckBox jCheckBox_MeSH;
    private javax.swing.JCheckBox jCheckBox_MedDRA;
    private javax.swing.JCheckBox jCheckBox_RXNORM;
    private javax.swing.JCheckBox jCheckBox_SCTSPA;
    private javax.swing.JCheckBox jCheckBox_SCTUSX;
    private javax.swing.JCheckBox jCheckBox_SNOMEDCT;
    private javax.swing.JCheckBox jCheckBox_UMD;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_UMLSUserName;
    private javax.swing.JLabel jLabel_UMLSUserPassw0rd;
    private javax.swing.JLabel jLabel_errmsg;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_UnderLineUnit_password;
    private javax.swing.JPanel jPanel_UnderLineUnit_password1;
    private javax.swing.JPanel jPanel_UnderLineUnit_password2;
    private javax.swing.JPanel jPanel_UnderLineUnit_username;
    private javax.swing.JPanel jPanel_UnderLineUnit_username1;
    private javax.swing.JPanel jPanel_UnderLineUnit_username10;
    private javax.swing.JPanel jPanel_UnderLineUnit_username11;
    private javax.swing.JPanel jPanel_UnderLineUnit_username12;
    private javax.swing.JPanel jPanel_UnderLineUnit_username13;
    private javax.swing.JPanel jPanel_UnderLineUnit_username14;
    private javax.swing.JPanel jPanel_UnderLineUnit_username15;
    private javax.swing.JPanel jPanel_UnderLineUnit_username2;
    private javax.swing.JPanel jPanel_UnderLineUnit_username3;
    private javax.swing.JPanel jPanel_UnderLineUnit_username4;
    private javax.swing.JPanel jPanel_UnderLineUnit_username5;
    private javax.swing.JPanel jPanel_UnderLineUnit_username6;
    private javax.swing.JPanel jPanel_UnderLineUnit_username7;
    private javax.swing.JPanel jPanel_UnderLineUnit_username8;
    private javax.swing.JPanel jPanel_UnderLineUnit_username9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
}


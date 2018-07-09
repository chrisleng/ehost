package webservices;

import env.Parameters;
import gov.va.vinci.annotationAdmin.integration.AnnotationAdminComMgr;
import gov.va.vinci.annotationAdmin.integration.AnnotationAdminComMgrEvent;
import gov.va.vinci.annotationAdmin.integration.AnnotationAdminComMgrEventListener;
import gov.va.vinci.annotationAdmin.integration.authentication.AuthenticationManager;
import gov.va.vinci.annotationAdmin.integration.io.Repository;
import gov.va.vinci.annotationAdmin.integration.model.Analyte;
import gov.va.vinci.annotationAdmin.integration.model.AnalyteAssignment;
import gov.va.vinci.annotationAdmin.integration.model.Assignment;
import gov.va.vinci.annotationAdmin.integration.model.Catalog;
import gov.va.vinci.annotationAdmin.integration.model.ServerInfo;
import gov.va.vinci.annotationAdmin.integration.model.Assignment.Status;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.jdesktop.swingx.MultiSplitLayout;
import userInterface.GUI;
import webservices.view.AssignmentTableModel;
import webservices.view.TableTransferHandler;

/**
 *
 * @author Yarden
 */
public class AssignmentsScreen extends javax.swing.JPanel implements AnnotationAdminComMgrEventListener {

    private javax.swing.Box.Filler filler;
    // private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox jServer;
    private javax.swing.JLabel jStatus;
    // private javax.swing.JTextField jUsername;
    private org.jdesktop.swingx.MultiSplitPane multiSplitPane;
    private javax.swing.JButton syncButton;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private AssignmentTableModel inProgressModel = new AssignmentTableModel(Assignment.Status.InProgress);
    private AssignmentTableModel doneModel = new AssignmentTableModel(Assignment.Status.Done);
    private AssignmentTableModel onHoldModel = new AssignmentTableModel(Assignment.Status.OnHold);    
    private String workDir = "/";
  
    
//    private SyncManager syncManager = new SyncManager(this);
    private AnnotationAdminComMgr syncManager = null;
    private GUI gui = null;
        private Catalog catalog;
    
    /** Creates new form AssignmentsScreen 
     * @throws IOException */
    public AssignmentsScreen(GUI gui) throws IOException {
        this.gui = gui;
        this.workDir = getWorkDirPath();
        syncManager = new AnnotationAdminComMgr(workDir);
        syncManager.setAnnotationAdminComMgrEventListener(this);
        init();
    }
   
        @Override
        public void sendEvent(AnnotationAdminComMgrEvent event) {
        log.LoggingToFile.log(Level.INFO, "actionPerformed="+event.getCmd());
        if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.CATALOG_CHANGED))
        {
                setCatalog(syncManager.getCatalog()); // set parameters
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.WORK_DIR_CANNOT_BE_CREATED))
        {
                JOptionPane.showMessageDialog(null, "Work directory cannot be created: " + this.workDir);
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.ASSIGNMENTS_CHANGED))
        {
                showAssignments(syncManager.getCatalog().getUserInfo().getAssignments());  // show fetched results
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.CANNOT_CLEAN_UP_OLD_PROJECT))
        {
                JOptionPane.showMessageDialog(null, "Cannot clean up old project. (Project folder could not be removed)");
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.LAST_SYNC_CHANGED))
        {
                setLastSync(syncManager.getLastSync());  // set string about when did we finish the laest syncing.
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.STATUS_CHANGED))
        {
                setStatus(syncManager.getStatus());
        }
        else if(event.getCmd().equalsIgnoreCase(AnnotationAdminComMgrEvent.SYNC_IN_PROGRESS_STATUS_CHANGED))
        {
                enableSync(!syncManager.isSyncInProgressStatus());
        }
        }

    private String getWorkDirPath() {
        String path = new File(Parameters.WorkSpace.WorkSpace_AbsolutelyPath).getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return path;
    }
    
    public void setAssignments( List<AnalyteAssignment> inProgress, List<AnalyteAssignment> done, List<AnalyteAssignment> onHold) {
        inProgressModel.setAssignments(inProgress);
        doneModel.setAssignments(done);
        onHoldModel.setAssignments(onHold);
        syncButton.setEnabled(true);
    }
    
    public List<AnalyteAssignment> getAssignments(Assignment.Status status) {
        if (status == Assignment.Status.InProgress) 
            return inProgressModel.getAssignments();
        else if (status == Assignment.Status.Done)
            return doneModel.getAssignments();
        else
            return onHoldModel.getAssignments();
    }
    
    public void setLastSync(String date) {
        if (date.equals(""))
            setStatus("Never synced");
        else
            setStatus("Last synced on "+date);
    }
    
    public void setStatus(String msg) {
        jStatus.setText(msg);
    }
    
    private void setTableHeight(JTable table, int rows) {
        int width = table.getPreferredSize().width;
        int height = rows*table.getRowHeight();
        table.setPreferredScrollableViewportSize(new Dimension(width, height));
    }
    
    private void init() throws IOException {
        GridBagConstraints gridBagConstraints;
        
        setLayout(new GridBagLayout());
        
        // top 

        JPanel jInfo = new JPanel();
        jInfo.setMaximumSize(new Dimension(500, 50));
        jInfo.setLayout(new GridBagLayout());

        //jLabel1 = new JLabel();
        //jLabel1.setText("Username:");
        //jInfo.add(jLabel1, new GridBagConstraints());

        //jUsername = new JTextField();
        //jUsername.setDragEnabled(false);
        //jUsername.setMinimumSize(new Dimension(100, 28));
        //jUsername.setPreferredSize(new Dimension(100, 28));
        //jUsername.addKeyListener( new KeyListener() {

        //    @Override
        //    public void keyTyped(KeyEvent ke) {
        //        // ignore
        //    }

        //    @Override
        //    public void keyPressed(KeyEvent ke) {
        //        // ignore
        //    }

        //    @Override
        //    public void keyReleased(KeyEvent ke) {
        //        syncManager.setUserId(jUsername.getText());
        //    }
        //});
        
        //jInfo.add(jUsername, new java.awt.GridBagConstraints());

        jLabel2 = new JLabel();
        jLabel2.setText("Server:");
        jInfo.add(jLabel2, new GridBagConstraints());
        
        String [] options = {"localhost" };
        jServer = new JComboBox(options);
        jServer.setSelectedIndex(0);
        jServer.setEditable(true);
        jServer.setMinimumSize(new Dimension(100, 30));
        jServer.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!ae.getActionCommand().equals("comboBoxEdited")) {
                    JComboBox cb = (JComboBox)ae.getSource();
                    if (cb.getSelectedIndex() == -1) 
                        cb.addItem(cb.getSelectedItem());
                    syncManager.setAnnotationAdminServer((String)cb.getSelectedItem());
                }
            }
        } );
        
        catalog = syncManager.getCatalog();
        ServerInfo info = catalog.getServerInfo();
        if (!info.url.equals("localhost") || info.port != 8080) {
            jServer.addItem(info.url+":"+Integer.toString(info.port));
            jServer.setSelectedIndex(1);
        }
        
        jInfo.add(jServer, new GridBagConstraints());
        
        jStatus = new JLabel();
        
        syncButton = new JButton();
        syncButton.setEnabled(true);
        syncButton.setText("Sync");
        syncButton.setAlignmentX(1.0F);
        syncButton.setHorizontalAlignment(SwingConstants.LEFT);
        syncButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syncButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jInfo.add(syncButton, gridBagConstraints);

        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jInfo.add(jStatus, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        filler = new Box.Filler(new Dimension(30, 0), new Dimension(30, 0), new Dimension(30, 0));
        jInfo.add(filler, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill =  GridBagConstraints.HORIZONTAL;
        add(jInfo, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        filler = new Box.Filler(new Dimension(30, 0), new Dimension(30, 0), new Dimension(30, 0));
        add(filler, gridBagConstraints);
        
        /*
         * Tables
         */
        
        TableTransferHandler handler = new TableTransferHandler();
        handler.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
        	{
        		for(AnalyteAssignment asg : inProgressModel.getAssignments())
        		{
        			asg.setStatus(Status.InProgress);
        		}
        		for(AnalyteAssignment asg : doneModel.getAssignments())
        		{
        			asg.setStatus(Status.Done);
        		}
        		for(AnalyteAssignment asg : onHoldModel.getAssignments())
        		{
        			asg.setStatus(Status.OnHold);
        		}
        		Catalog catalog = AssignmentsScreen.this.catalog;
        		if(catalog!=null)
        		{
        			catalog.updateCatalog();
        		}
        	}        	
        });
        jPanel1 = createTable("In Progress", inProgressModel, handler, 10);
        jPanel2 = createTable("Done", doneModel, handler, 10);
        jPanel3 = createTable("On Hold", onHoldModel, handler, 1);

        // Organize tablels
        
        String layoutDef = "(COLUMN first second third)";
        MultiSplitLayout.Node model = MultiSplitLayout.parseModel(layoutDef);

        multiSplitPane = new org.jdesktop.swingx.MultiSplitPane();
        multiSplitPane.getMultiSplitLayout().setModel(model);
        
        multiSplitPane.add(jPanel1, "first");
        multiSplitPane.add(jPanel2, "second");
        multiSplitPane.add(jPanel3, "third");
        
        //multiSplitPane.setPreferredSize(model.getBounds().getSize());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        add(multiSplitPane, gridBagConstraints);
    }
    
    public String getUserId()
    {
    	return syncManager.getUserId();
    }
    
    public void setUserId(String id, String username)
    {
    	syncManager.setUserId(id );
    }
    
    public void updateAssignments() {
		showAssignments(syncManager.getCatalog().getUserInfo().getAssignments());
    }
   
        private void showAssignments(List<Assignment> assignments) {

                List<AnalyteAssignment> inProgress = new ArrayList<AnalyteAssignment>();
                List<AnalyteAssignment> onHold = new ArrayList<AnalyteAssignment>();
                List<AnalyteAssignment> done = new ArrayList<AnalyteAssignment>();

                for (Assignment assignment :  assignments ) {
                        for (AnalyteAssignment item : assignment.getAnalyteAssignments()) {
                                if (item.getStatus() == Status.InProgress) {
                                        inProgress.add(item);
                                } else if (item.getStatus() == Status.OnHold) {
                                        onHold.add(item);
                                } else { 
                                        done.add(item);
                                }
                        }
                }

                // sort
                Collections.sort(inProgress, compareAssignments);
                Collections.sort(done, compareAssignments);
                Collections.sort(onHold, compareAssignments);

                setAssignments( inProgress, done, onHold);
        }
        
        private Comparator compareAssignments = new Comparator() {

                @Override
                public int compare(Object o1, Object o2) {
                        AnalyteAssignment a1 = (AnalyteAssignment) o1;
                        AnalyteAssignment a2 = (AnalyteAssignment) o2;

                        int v = a1.getAssignment().getId().compareTo(a2.getAssignment().getId());
                        if (v == 0)
                                v = a1.getAnalyteId().compareTo(a2.getAnalyteId());
                        return v;
                }
        };

        private JPanel createTable(String title, AssignmentTableModel model, TransferHandler handler, int rows) {
       
//        model.addTableModelListener(new TableModelListener() {
//
//            @Override
//            public void tableChanged(TableModelEvent tme) {
//                syncButton.setEnabled(/*inProgressModel.getAssignments().isEmpty() && */ onHoldModel.getAssignments().size() <= 1 );              
//            }
//        });
        
        JPanel jPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel.add(new JLabel(title), gridBagConstraints);
        
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill =  GridBagConstraints.HORIZONTAL;
        jPanel.add( new JSeparator(SwingConstants.HORIZONTAL), gridBagConstraints);
        
        
        JTable table = new JTable();
        table.setModel(model);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION/*SINGLE_SELECTION*/);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(handler);
        table.setShowGrid(true);
        setTableHeight(table, rows);
        table.addMouseListener( new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2 && me.getComponent().isEnabled()) 
                    selectAnalyte((JTable) me.getSource());
            }

            @Override
            public void mousePressed(MouseEvent me) {
                // ignore
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                // ignore
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                // ignore
            }

            @Override
            public void mouseExited(MouseEvent me) {
                // ignore
            }
         });
            
         
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(table);
 
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        jPanel.add(jScrollPane, gridBagConstraints);
        
        return jPanel;
    }
        
    private void syncButtonActionPerformed(java.awt.event.ActionEvent evt) {

        sync();

    }
    
    public void sync(){
        try {
            // get and set the server URL
            String serverURL = jServer.getSelectedItem().toString();
            syncManager.setAnnotationAdminServer(serverURL);
            System.out.println("URL : " + serverURL);
            
            boolean islogining = true;

            // get possible password and username
            String username = resultEditor.workSpace.WorkSet.current_annotator_name;
            String password = resultEditor.workSpace.WorkSet.password;
            String userid   = resultEditor.workSpace.WorkSet.uid;
            boolean authorized = resultEditor.workSpace.WorkSet.authorized;
            
            // ask them to type username or password if they aren't typed
            if (( username == null) || (password == null) || (username.trim().length() < 1)
                    || (password.trim().length()<1)) {
                resultEditor.annotator.ChangeAnnotator changeannotator = new resultEditor.annotator.ChangeAnnotator(gui, this);
                changeannotator.setVisible(true);
                return;
            }

            if ((authorized == false)||(userid==null)||(userid.trim().length()<1)||userid.compareToIgnoreCase("null") == 0) {    
                
                // try to login and get the numberized id of this user
                        AuthenticationManager authMgr = new AuthenticationManager("http://"+serverURL+"/annotationAdmin-ws/");
                        Long userId = authMgr.authenticate(username, password, true);
                String getid = "" + userId;
                if(( getid != null )&&(getid.trim().length()>0)){
                    resultEditor.workSpace.WorkSet.uid = getid;
                    userid = getid;                    
                    resultEditor.workSpace.WorkSet.authorized = true;
                }else{                    
                    resultEditor.workSpace.WorkSet.authorized = false;
                    resultEditor.workSpace.WorkSet.uid = null;
                    resultEditor.annotator.ChangeAnnotator changeannotator = new resultEditor.annotator.ChangeAnnotator(gui, this);
                    changeannotator.setVisible(true);                    
                    return;
                }
            }
                                  
            boolean isSubmittingAdjudications = false;
            if(env.Parameters.WorkSpace.CurrentProject!=null)
            {
            	gui.verifyAnnotationsWereSaved();
                try
                {
        	        String[] names = env.Parameters.WorkSpace.CurrentProject.list();
        	        for(String name : names)
        	        {
        	        	File file = new File(env.Parameters.WorkSpace.CurrentProject.getAbsolutePath() +  File.separator + name);
        	            if (file.isDirectory() && file.getName().compareToIgnoreCase("adjudication") == 0)
        	            {
        	                isSubmittingAdjudications = true;
        	                break;
        	            }
        	        }
        	    } catch (SecurityException e)
                {
                }
            }
            
            if( isSubmittingAdjudications ){
                int n = JOptionPane.showConfirmDialog(null, 
                        "Submit adjudications (adjudicated annotations)?", "What to sync?", JOptionPane.YES_NO_OPTION); 
                if (n == JOptionPane.YES_OPTION) { 
                    isSubmittingAdjudications = true;
                } else if (n == JOptionPane.NO_OPTION) { 
                    isSubmittingAdjudications = false;
                } 
            }
            
            syncManager.setUserId(userid );
            syncManager.setFetchPreAnnotations(true);
            
            if( !isSubmittingAdjudications  )
                syncManager.doSync(true);
            else
                syncManager.doSync(true, true);
            
            // save all fetched pre annotation
            if(env.Parameters.WorkSpace.CurrentProject!=null)
            {
                gui.verifyAnnotationsWereSaved();
            }
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1201280136::fail to save current project.");
        }
    }
    
    public void enableSync(boolean enable) {
        syncButton.setEnabled(enable);
        setEnabledAll(jPanel1, enable);
        setEnabledAll(jPanel2, enable);
        setEnabledAll(jPanel3, enable);
    }
    
    private void setEnabledAll(JComponent component, boolean set)
    {
        component.setEnabled(set);
        Component[] aList = component.getComponents();
        for (Component aComponent : aList)
        {
            aComponent.setEnabled(set);
            try
            {
                JComponent casted = (JComponent) aComponent;
                setEnabledAll(casted, set);
            }
            catch (Exception e)
            {
            }
        }
    }
   
    private void selectAnalyte(JTable table) {
        AssignmentTableModel model = (AssignmentTableModel) table.getModel();
        AnalyteAssignment analyte = model.getRow(table.getSelectedRow());
               
        Repository repository = new Repository(workDir);
        String path = repository.getAssignmentPath(analyte.getAssignment());
        gui.switchTo(path, analyte.getName());
        
    }

        public void setCatalog(Catalog catalog) {
                this.catalog = catalog;         
        }
}
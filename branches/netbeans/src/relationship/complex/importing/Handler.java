/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Handler.java
 *
 * Created by Jianwei Leng on Aug 26, 2010, 1:34:05 PM
 * Modified by Jianwei Leng on Sep 30, 2010 for multiple threads and extending effective range
 * Modified by Kyle Anderson on Oct. 4, 2010 to be used for schema modifications on import
 */

package relationship.complex.importing;

import relationship.importing.iListener;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationRelationship;
import resultEditor.annotations.Depot;
import resultEditor.annotations.AnnotationAttributeDef;
import relationship.simple.dataTypes.AttributeSchemaDef;
import relationship.complex.dataTypes.RelationshipDef;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JList;
import resultEditor.annotationClasses.AnnotationClass;

/**
 * This class will handle the import of unknown attributes/relationships into the
 * schema.
 * @author Kyle
 */
public class Handler extends javax.swing.JFrame {
    //<editor-fold defaultstate="collapsed" desc="Member Variables">
    /**
     * The attributes that were imported that are not in the current schema.
     */
    private Hashtable<String, Hashtable> unknownAtts = new Hashtable<String, Hashtable>();
    /**
     * The relationships that were imported that are not in the current schema.
     */
    private Vector<RelationshipDef> unknownRels = new Vector<RelationshipDef>();
    /**
     * The relationships and attributes will be placed in here so that they can be
     * displayed on the list more easily.
     */
    private Vector<iListEntry> listentry = new Vector<iListEntry>();
    /**
     * The attributes that are currently being modified will be stored here.
     */
    private Hashtable<String, Hashtable> actionAttsByClass = new Hashtable<String, Hashtable>();
    /**
     * The relationshps that are currently being modified will be stored here.
     */
    private Vector<RelationshipDef> actionRels = new Vector<RelationshipDef>();
    /**
     * The gui that called this class, will be used to refresh the screen when this
     * class exits.
     */
    private iListener gui;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Constructor
     * @param gui - the gui that called this class
     * @param terms - the attributes that were imported that are not in the current schema.
     * @param relationships - the relationships that were imported that are not in the current schema.
     */
    public Handler(iListener gui, int guiX, int guiY, int width, int height,
            Hashtable<String, Hashtable> atts, Collection<RelationshipDef> relationships, String summary) {
        //Extract params
        unknownAtts.putAll(atts);
        unknownRels.addAll(relationships);
        this.gui = gui;

        //Create list entry objects for all unkown rels/atts
        for( RelationshipDef term : unknownRels)
        {
            iListEntry i;
            Vector<String> allowed = new Vector<String>();
            allowed.add(term.getAllowed());
            i = new iListEntry( term.getName(), allowed,null/*term.getTo()*/,true, !term.isUnique() );
            listentry.add( i );
        }
        
        for(String classname: unknownAtts.keySet())
        {
            Hashtable<String, HashSet> atts_of_class = atts.get( classname );
            
            for( String attname : atts_of_class.keySet() ){
                
                HashSet values = atts_of_class.get(attname);
                Vector<String> valuetexts = new Vector<String>();
                if( values == null ){
                    continue;
                }else{
                    for(Object value : values.toArray()){
                        valuetexts.add( (String)value );
                        
                    }
                }
                
                iListEntry i;
                
                // public iListEntry(String term, Collection subTerm, Collection subTerm2, boolean relationship, boolean nameKnown)
                i = new iListEntry( 
                        attname, 
                        valuetexts,
                        null, 
                        false, 
                        true );
                i.setClass( classname );
                listentry.add( i );
            }
        }
        //initialize gui
        initComponents();
        String temp = this.jLabel2.getText();
        this.jLabel2.setText("<html>" + summary + " " + temp + "</html>");


        //set size of gui
        this.setSize(700,600);

        //set dialog location
        int x = 0;
        int y = 0;

        int eHOSTx = guiX; int eHOSTy = guiY;
        x = eHOSTx + (int)((width-this.getWidth())/2);
        y = eHOSTy + (int)((height-this.getHeight())/2);
        
        this.setLocation(x, y);
        
        //Display unknown atts/rels on list.
        listdisplay();
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

        jPanel_top = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel_top_left = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel_top_center = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel_top_right = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel_center = new javax.swing.JPanel();
        accept = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        allow = new javax.swing.JButton();
        jPanel_buttom = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jPanel_top_left1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relationship/Attribute Handler");
        setAlwaysOnTop(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel_top.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_top.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_top.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(245, 245, 244));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel_top_left.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(0, 102, 153));
        jPanel5.setPreferredSize(new java.awt.Dimension(7, 82));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );

        jPanel_top_left.add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel6.setBackground(new java.awt.Color(254, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(35, 68));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );

        jPanel_top_left.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel_top_left, java.awt.BorderLayout.WEST);

        jPanel_top_center.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_top_center.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_top_center.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setText("<html>Imported Annotations have Attributes/Relationships outside of the current schema.  Choose whether you want to Allow them for this import only, Delete all unknown relationships/attributes, Accept them into the schema, or cancel the import.</html>");
        jPanel_top_center.add(jLabel2, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel_top_center, java.awt.BorderLayout.CENTER);

        jPanel_top_right.setBackground(new java.awt.Color(254, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/res/metacontact_online.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_top_rightLayout = new javax.swing.GroupLayout(jPanel_top_right);
        jPanel_top_right.setLayout(jPanel_top_rightLayout);
        jPanel_top_rightLayout.setHorizontalGroup(
            jPanel_top_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_top_rightLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );
        jPanel_top_rightLayout.setVerticalGroup(
            jPanel_top_rightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jPanel2.add(jPanel_top_right, java.awt.BorderLayout.EAST);

        jPanel_top.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel_top, java.awt.BorderLayout.NORTH);

        jPanel_center.setBackground(new java.awt.Color(240, 240, 241));
        jPanel_center.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 240, 241), 3));

        accept.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        accept.setText("Accept");
        accept.setToolTipText("<html>Accept these attributes/relationships into the schema.</html>");
        accept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptActionPerformed(evt);
            }
        });

        cancel.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        cancel.setText("Cancel");
        cancel.setToolTipText("<html>Cancel the import, and clear imported annotations.</html>");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        delete.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        delete.setText("Delete");
        delete.setToolTipText("<html> Delete the selected attributes/relationships from the annotations.<br> This only affects newly imported annotations.</html>");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        allow.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        allow.setText("Allow");
        allow.setToolTipText("<html>Allow the selected attributes/relationshpis to be imported<br> without adding them to the schema.</html>");
        allow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_centerLayout = new javax.swing.GroupLayout(jPanel_center);
        jPanel_center.setLayout(jPanel_centerLayout);
        jPanel_centerLayout.setHorizontalGroup(
            jPanel_centerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_centerLayout.createSequentialGroup()
                .addContainerGap(245, Short.MAX_VALUE)
                .addComponent(allow)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancel)
                .addGap(3, 3, 3))
        );
        jPanel_centerLayout.setVerticalGroup(
            jPanel_centerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_centerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cancel)
                .addComponent(accept)
                .addComponent(delete)
                .addComponent(allow))
        );

        getContentPane().add(jPanel_center, java.awt.BorderLayout.SOUTH);

        jPanel_buttom.setBackground(new java.awt.Color(254, 255, 255));
        jPanel_buttom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(254, 255, 255), 5));
        jPanel_buttom.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(254, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        list.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list);

        jTabbedPane1.addTab("Unkown Attributes/Relationships", jScrollPane1);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel_buttom.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel_top_left1.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(254, 255, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );

        jPanel_top_left1.add(jPanel7, java.awt.BorderLayout.EAST);

        jPanel8.setBackground(new java.awt.Color(254, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(35, 406));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );

        jPanel_top_left1.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel_buttom.add(jPanel_top_left1, java.awt.BorderLayout.WEST);

        getContentPane().add(jPanel_buttom, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //<editor-fold defaultstate="collapsed" desc="Event Methods">
    /**
     * The cancel button was pressed... delete all newly imported annotations and
     * dispose of this gui.
     * @param evt
     */
    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        Depot depot = new Depot();
        ArrayList<Annotation> annotations = depot.getAllAnnotations();
        //Loop through annotations to find newly imported ones and delete them.
        for(int i = 0; i< annotations.size(); i++)
        {
            Annotation annot = annotations.get(i);
            if(!annot.isVerified())
            {
                depot.deleteAnnotation(annot);
            }

        }
        //dispose gui
        finished();
    }//GEN-LAST:event_cancelActionPerformed

    /**
     * Mouse clicked on list
     * @param evt
     */
    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
        mouseClicked_onList(list);
    }//GEN-LAST:event_listMouseClicked
    
    /**
     * 'Accept' button pressed.  This must add all of the currently selected Attributes/Relationships to
     * the schema.
     * @param evt
     */
    private void acceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptActionPerformed
        updateListSelectionStatus();
        //env.Parameters.AttributeSchemas.addAll(actionAtts);
        
        
        resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
        try{
            for( String classname : actionAttsByClass.keySet() ){
                if(classname==null)
                    continue;
                Hashtable<String, HashSet> newatts =  actionAttsByClass.get( classname );
                if( newatts == null )
                    continue;
                
                AnnotationClass theclass = classdepot.getAnnotatedClass( classname );
                if( theclass == null)
                    continue;
                Vector<AttributeSchemaDef> atts = theclass.getAttributes();
                
                for(String attname : newatts.keySet()){
                    if(attname == null)
                        continue;
                    if( newatts.get( attname ) == null )
                        continue;
                    HashSet<String> values = newatts.get( attname );
                    
                    boolean found = false;
                    for(int i=0;i<atts.size();i++){
                        AttributeSchemaDef att = atts.get(i);
                        if(( att==null )||(att.getName()==null))
                            continue;
                        if( att.getName().trim().compareTo( attname.trim() ) == 0 ){
                            found = true;
                            Vector<String> oldvalues = att.getAllAllowedEntries();
                            oldvalues.addAll( values );
                            att.setAllowedEntries( oldvalues );
                            atts.set(i, att);
                            theclass.privateAttributes = atts;
                            break;
                        }
                    }
                    
                    if( !found ){
                        //Vector<String> oldvalues = new Vector<String>();
                        //oldvalues.addAll( values );
                        AttributeSchemaDef att = new AttributeSchemaDef();
                        att.setName(attname);
                        for(String value : values){
                            att.put( value );
                        }
                        theclass.privateAttributes.add(att);
                        
                    }
                }
                
                
                
            }
        }catch(Exception ex ){
            System.out.println( "fail to output ex" );
        }
        
        
        for(RelationshipDef s: actionRels)
        {
            env.Parameters.RelationshipSchemas.add(s);
        }
        
        if(listentry.size() == 0) {
            finished();
        }
    }//GEN-LAST:event_acceptActionPerformed

    /**
     * The 'Delete' button has been pressed.  We must delete any relationship/attribute that the
     * user currently has selected from all newly imported annotations.
     * @param evt
     */
    private void deleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteActionPerformed
    {//GEN-HEADEREND:event_deleteActionPerformed
        // annotation depot
        Depot depot = new Depot();

        updateListSelectionStatus();
        //Delete all unknown relatinoships/attributes
        for (Annotation annotation : depot.getAllAnnotations()) {
            //Annotation is unverified if it is still new
            if (!annotation.isVerified()) {
                //Make sure we have some ComplexRelationships before trying to delete them
                if (annotation.relationships != null) {
                    //Delete unknown relationships
                    for (int i = 0; i < annotation.relationships.size(); i++) {
                        AnnotationRelationship rel = annotation.relationships.get(i);

                        //If a selected relatinoship has the same name as the annotation then
                        //delete the relationship from the annotation
                        for (RelationshipDef newOnes : actionRels) {
                            if (newOnes.getName().equals(rel.getMentionSlotID())) {
                                //TODO: Only delete relationships that match regex being delted.
                                annotation.relationships.remove(i);
                                i--;
                            }
                        }
                    }
                }
                
                
                //Delete unknown attributes
                if ( annotation.attributes != null ) {
                    
                    if ( (annotation.annotationclass == null) || (annotation.annotationclass.trim().length() < 1)) {
                        break;
                    }

                    for (int i = 0; i < annotation.attributes.size(); i++) 
                    {
                        AnnotationAttributeDef att = annotation.attributes.get(i);

                        //If the attribute has a bad attribute.. just delete it... hopefully this never happens...
                        //but I get the feeling that it does
                        if (att.value == null || att.name == null) {
                            annotation.attributes.remove(i);
                            i--;
                            break;
                        } else {
                            //Loop through all Attributes to see if it matches an attribute in the
                            //current annotation
                            boolean found = false;
                            for (String classname : actionAttsByClass.keySet()) {
                                Hashtable<String, HashSet> atts = actionAttsByClass.get(classname);

                                if (annotation.annotationclass.trim().compareTo(classname.trim()) != 0) {
                                    continue;
                                } //TODO: Erase the following check and see if it is needed... don't think it is
                                
                                for( String an_attname : atts.keySet() ){
                                    HashSet thevalues = atts.get( an_attname );
                                    if( thevalues == null )
                                        continue;
                                    //If it is needed then need to see how thes uAtt's are being created
                                    if (an_attname == null || thevalues == null) {
                                        continue;
                                    } //If the names are equal and the value is contained then delete it.
                                    else if ( (att.name.trim().compareTo( 
                                        an_attname) == 0)  && (thevalues.contains(att.value) ) ) {
                                        found = true;
                                        break;
                                    }

                                }
                            }

                            if (found) {
                                //System.out.println("removed attribute: " + annotation.attributes.get(i).name + " = " + annotation.attributes.get(i).value );
                                annotation.attributes.remove(i);
                                i--;
                                continue;
                            }
                        }
                    }
                }
            }
        }
        //If the user has consumed all of the list then close the dialog.
        if (listentry.size() == 0) {
            finished();
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void allowActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_allowActionPerformed
    {//GEN-HEADEREND:event_allowActionPerformed
        updateListSelectionStatus();
        if(listentry.size() == 0)
            finished();
    }//GEN-LAST:event_allowActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        gui.listeneeDead();
    }//GEN-LAST:event_formWindowClosing
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Methods">
    public void listdisplay() {

        // remove old data
        //listentry = new Vector<Object>();
        //list.setListData(listentry);

        if (unknownRels == null && unknownAtts == null)
            return;

        list.setListData( listentry );
        list.setCellRenderer(new iListCellRenderer());

    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Call this when a mouse click occurs on the Jlist. Invert 'selected' variable
     * for the selected iListEntry.
     * @param thislist - the list that had a mouse click.
     */
    private void mouseClicked_onList(JList thislist) {


            iListEntry thisterm = (iListEntry)thislist.getSelectedValue();
            if(thisterm == null)
                return;
            thisterm.selected = !thisterm.selected;
            listdisplay();
    }
    
    private void updateListSelectionStatus()
    {
        this.actionAttsByClass.clear();
        actionRels.clear();
        for(int i = listentry.size() - 1 ; i >= 0; i--)
        {
            iListEntry entry = listentry.get(i);
            
            if(entry.selected)
            {
                if(entry.isUnknownRelationship)
                {
                    actionRels.add(new RelationshipDef(entry.term,entry.values.get(0)));
                    listentry.remove(i);
                    continue;
                    
                } else {
                    try {
                        String attributename = entry.term;
                        Vector<String> attributevalues = entry.values;
                        String classname = entry.classname;

                        HashSet<String> values = new HashSet<String>();
                        values.addAll(attributevalues);
                        
                        
                        // System.out.println("#### record to delete attribute 【" + attributename.trim() + "】 of class [" +  classname + "]");
                        
                        if(actionAttsByClass.containsKey( classname ) ){
                            Hashtable<String, HashSet> attributes = actionAttsByClass.get(classname);
                            attributes.put(attributename.trim(), values);
                        }else{
                            Hashtable<String, HashSet> attributes = new Hashtable<String, HashSet>();
                            attributes.put(attributename.trim(), values);
                            actionAttsByClass.put( classname, attributes );
                        }

                        listentry.remove(i);
                        continue;
                    } catch (Exception ex) {
                        System.out.println("error occurred while trying to record selected attributes on schema import dialog!" + ex.getMessage());
                    }
                }
            }
        }
        
        listdisplay();
    }
    private void finished()
    {
        gui.listeneeDead();
        this.dispose();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GUI Member Variables">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accept;
    private javax.swing.JButton allow;
    private javax.swing.JButton cancel;
    private javax.swing.JButton delete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel_buttom;
    private javax.swing.JPanel jPanel_center;
    private javax.swing.JPanel jPanel_top;
    private javax.swing.JPanel jPanel_top_center;
    private javax.swing.JPanel jPanel_top_left;
    private javax.swing.JPanel jPanel_top_left1;
    private javax.swing.JPanel jPanel_top_right;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables
//</editor-fold>
}


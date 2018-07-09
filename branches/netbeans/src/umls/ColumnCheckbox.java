/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package umls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.annotationClasses.Depot;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationAttributeDef;
import userInterface.GUI;

/**
 *
 * @author imed
 */
public class ColumnCheckbox extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

    JCheckBox renderButton;
    JCheckBox editButton;
    boolean thisvalue;
    JTable table;
    Annotation annotation;
    GUI gui;

    public ColumnCheckbox(JTable table, Annotation annotation, GUI gui) {
        super();
        this.table = table;
        this.annotation = annotation;
        this.gui = gui;
    }

    public void setRendererNEditor(TableColumn columnModel) {

        renderButton = new JCheckBox();
        renderButton.setText(null);

        editButton = new JCheckBox();
        editButton.setText(null);
        editButton.setFocusPainted(false);
        //editButton.setPreferredSize( new Dimension(70, 25));
        //editButton.setMaximumSize( new Dimension(70, 25));
        editButton.addActionListener(this );

        columnModel.setCellRenderer(this);
        columnModel.setCellEditor(this);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        if( this.annotation == null )
            return null;
                
        int realrow = table.convertRowIndexToModel(row);
        
        if (hasFocus) {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background "));
        } else if (isSelected) {
            renderButton.setForeground(Color.red);
            renderButton.setBackground(table.getSelectionBackground());
        } else {
            renderButton.setForeground(table.getForeground());
            renderButton.setBackground(UIManager.getColor("Button.background "));
        }

        boolean recorded = (Boolean) table.getModel().getValueAt(realrow, 2);;
        // System.out.println(row + " 选中" + copy );
        thisvalue = recorded;
        
        if( thisvalue ){
            //renderButton.setText( "chosen" );
            renderButton.setForeground( new Color(0x000000));
        }else{
            //renderButton.setText( "choose" );
            renderButton.setForeground( new Color(0xbdbdbd));
        }
        
        renderButton.setSelected( recorded );
        
        //(value == null) ? " " : value.toString());
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        //text = (value == null) ? " " : value.toString();

        // if an annotation is not designated, then the checkbox is invisible
        if (this.annotation == null) {
            return null;
        }

        boolean recorded = false;
        if (value == null) {
            recorded = false;
        } else {
            recorded = (Boolean) value;
        }


        
            //editButton.setText( "choosing" );
        editButton.setSelected( recorded );
                        
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return thisvalue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {                
        
        // if an annotation is not designated, then the checkbox is invisible
        if( this.annotation == null )
            return;
        
        int row = table.getSelectedRow();        
        if( row < 0)
            return;
        
        int realrow = table.convertRowIndexToModel(row);
        thisvalue = (Boolean) table.getModel().getValueAt(realrow, 2);
        
        // remove this previous recorded cui
        if( thisvalue == true ){
            try{
                String cui = (String) table.getModel().getValueAt(realrow, 0);
                if( removeCUI( cui ))
                    thisvalue = !thisvalue;
                else
                    commons.Tools.beep();
            }catch(Exception ex){
                
            }
        }
        // OR, record a new CUI 
        else {
            try{
                String cui = (String) table.getModel().getValueAt(realrow, 0);
                String concept = (String) table.getModel().getValueAt(realrow, 1);
                copyToAttribute( cui, concept );
                thisvalue = !thisvalue;
            }catch(Exception ex){
            }
        }
        
        
        table.getModel().setValueAt( thisvalue, realrow, 2 );
        
        //for( int i=0; i<table.getModel().getRowCount();i++ ){
        //            if(i==realrow)
        //        continue;
        //    table.getModel().setValueAt( false, realrow, 2 );
        //}
        
        
        
        
        //setSysClipboardText( "CUI: [" + cui + "];  LABEL: [" + concept + "]" );
        
        // 
        
        //if( this.annotation != null ){
        //    saveToCurrentAnnotation( annotation, "CUI: [" + cui + "];  LABEL: [" + concept + "]" );
        //}
        
        
        fireEditingStopped();
        //System.out.println( "\n" + e.getActionCommand() + "   :   " + table.getSelectedRow());
        //table.repaint();
    }
    
    /** remove a previous recorded CUI#. */
    private boolean removeCUI(String cui){
        if ((cui == null) || (cui.trim().length() < 1)) {
            return false;
        }

        if ((this.annotation != null) && (annotation.annotationclass != null)) {

            Depot classdepot = new Depot();
            //for ( : allClasses) {
            try {// record class names
                resultEditor.annotationClasses.AnnotationClass thisclass = classdepot.getAnnotatedClass(annotation.annotationclass);

                if (thisclass == null) {
                    return false;
                }

                Matcher matcher;
                Vector<AttributeSchemaDef> atts =  thisclass.getAttributes();
                for ( int j=atts.size()-1; j>=0;j-- ) {
                    AttributeSchemaDef att = atts.get(j);
                    if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                        continue;
                    }
                    if (att.isCUICodeNLabel && ( att.getAllAllowedEntries()!= null )) {
                        String regex = "CUI: \\[" + cui + "\\];";                   
                        
                        if(att.getAllowedEntries()==null)
                                continue;
                        for( int i=att.getAllowedEntries().size() - 1; i>= 0; i-- ){
                            if(att.getAllowedEntries().get(i) ==null)
                                continue;
                            matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( att.getAllowedEntries().get(i) );
                            boolean found = matcher.find();
                            if( found ){
                                att.removeValue( att.getAllowedEntries().elementAt(i) );   
                                atts.setElementAt(att, j);
                                thisclass.privateAttributes = atts;
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return false;
    }
    
     public static void setSysClipboardText(String writeMe) {  
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(writeMe);  
        clip.setContents(tText, null);  
    }

     /**add this cui and its label as a option into attributes whose "UMLS linking status" is true.*/
    private void copyToAttribute(String cui, String concept) {
        if(( cui == null )||(cui.trim().length()<1))
            return;
        if(( concept == null )||(concept.trim().length()<1))
            return;
        
        /*for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes()) {
            if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                continue;
            }
            if( att.UMLS linking status ){
                att.put("CUI: [" + cui + "];  LABEL: [" + concept + "]");
            }
        }*/
        
        // resultEditor.annotationClasses.Depot
        Depot classdepot = new Depot();
            //Vector<AnnotationClass> allClasses = classdepot.getAll();

            if(( this.annotation != null )&&(annotation.annotationclass != null)) {
                
                
            //for ( : allClasses) {
                try {// record class names
                    resultEditor.annotationClasses.AnnotationClass thisclass = classdepot.getAnnotatedClass(annotation.annotationclass);
                    
                    if( thisclass ==null )
                        return;

                    for (AttributeSchemaDef att : thisclass.getAttributes() ) {
                        if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                            continue;
                        }
                        if( att.isCUICodeNLabel ){
                            att.put("CUI: [" + cui + "];  LABEL: [" + concept + "]");
                        }else if( att.isCUICode ){
                            att.put( cui );
                        }else if( att.isCUILabel ){
                            att.put( concept );
                        }
                        
                    }
                }catch(Exception ex){
                    ex.printStackTrace();   
                }
            }
    }

    /*private void saveToCurrentAnnotation(Annotation annotation, String newCUICode) {
        if(annotation.attributes ==null)
            annotation.attributes = new Vector<AnnotationAttributeDef>();
        
        if(( annotation.annotationclass == null ) || (annotation.annotationclass.trim().length()<1)){
            System.out.println("error: this annotation didn't have class!!!");
            return;
        }
        
        
        try{
            HashSet<String> umlsAttributes = new HashSet<String>();
            
            // resultEditor.annotationClasses.Depot
            Depot classdepot = new Depot();
            Vector<AnnotationClass> allClasses = classdepot.getAll();
            
            AnnotationClass annclass = classdepot.getAnnotatedClass(annotation.annotationclass.trim());
            if( annclass == null )
                return;
            
            Vector<AttributeSchemaDef> privateAtts = annclass.getAttributes();
            if( privateAtts != null ){
                for( AttributeSchemaDef att : privateAtts  ){
                    if(att.isCode)
                    {
                        umlsAttributes.add( att.getName() );
                    }
                }
            }
            
            if( annclass.inheritsPublicAttributes ){
                for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes()) {
                    if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                        continue;
                    }
                    if( att.isCode ){
                        umlsAttributes.add( att.getName() );
                    }
                }
            }
            
            for(String umlsAttribute : umlsAttributes){    
                
                boolean found = false;
                
                for(int i=0; i<annotation.attributes.size();i++){
                    AnnotationAttributeDef annAttribute = annotation.attributes.get(i);
                    if(annAttribute==null)
                       continue;
                    
                    if(annAttribute.name == null)
                        continue;
                    
                    if(annAttribute.name.trim().compareTo( umlsAttribute.trim())==0){
                        annAttribute.value = newCUICode;
                        annotation.attributes.set(i, annAttribute);
                        found = true;
                        break;
                    }
                    
                    
                }
                
                if(!found){
                    AnnotationAttributeDef annAttribute = new AnnotationAttributeDef();
                    annAttribute.name = umlsAttribute.trim();
                    annAttribute.value = newCUICode;
                    annotation.attributes.add( annAttribute );
                }
            }
            //for (resultEditor.annotationClasses.AnnotationClass thisclass : allClasses) {
            //
            //    if( thisclass ==null )
            //        continue;
            //    
            //}
            
            gui.refresh();
            gui.updateAttributes( annotation );
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/
}
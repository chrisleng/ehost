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
import java.util.HashSet;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
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
public class ColumnButton extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {

    JButton renderButton;
    JButton editButton;
    boolean thisvalue;
    JTable table;
    Annotation annotation;
    GUI gui;

    public ColumnButton(JTable table, Annotation annotation, GUI gui) {
        super();
        this.table = table;
        this.annotation = annotation;
        this.gui = gui;
    }

    public void setRendererNEditor(TableColumn columnModel) {

        renderButton = new JButton();

        editButton = new JButton();
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

        boolean copy = (Boolean) table.getModel().getValueAt(realrow, 2);;
        // System.out.println(row + " 选中" + copy );
        thisvalue = copy;
        
        if( thisvalue ){
            renderButton.setText( "chosen" );
            renderButton.setForeground( new Color(0x000000));
        }else{
            
            renderButton.setText( "choose" );
            renderButton.setForeground( new Color(0xbdbdbd));
        }
        
        //(value == null) ? " " : value.toString());
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {
        //text = (value == null) ? " " : value.toString();
        
         boolean copy = false;
        if( value == null  )
            copy = false;
        else
            copy = (Boolean)value;
        
       
        
        
        if( this.annotation != null ){
            if(copy){
                editButton.setText( "chosen" );
                renderButton.setForeground( new Color(0x000000));
            }else{
                editButton.setText( "choose" );
                renderButton.setForeground( new Color(0xbdbdbd));
            }
        } else {
                editButton.setText( "copy" );
        }
                        
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return thisvalue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {                
        
        int row = table.getSelectedRow();        
        if( row < 0)
            return;
        
        int realrow = table.convertRowIndexToModel(row);
        thisvalue = (Boolean) table.getModel().getValueAt(realrow, 2);
        
        // if this button is not just clicked
        //if( !thisvalue ){
            // reset all to false
            int size = this.table.getModel().getRowCount();
            for(int i=0;i<size;i++){
                table.getModel().setValueAt( false, i, 2);
            }
            
            // and only this one to true
            table.getModel().setValueAt( true, realrow, 2);
            //table.getModel().setValueAt( true, realrow, 2);
            thisvalue = true;
        //}
        table.getModel().setValueAt( true, realrow, 2);
        
        
        //table.getModel().setValueAt( thisvalue, realrow, 3 );
        
        //for( int i=0; i<table.getModel().getRowCount();i++ ){
        //            if(i==realrow)
        //        continue;
        //    table.getModel().setValueAt( false, realrow, 3 );
        //}
        
        
        
        String cui = (String) table.getModel().getValueAt(realrow, 0);
        String concept = (String) table.getModel().getValueAt(realrow, 1);
        
        // copy the string to clipboard
        setSysClipboardText( "CUI: [" + cui + "];  LABEL: [" + concept + "]" );
        
        
        copyToAttribute( cui, concept );
        
        if( this.annotation != null ){
            saveToCurrentAnnotation( annotation, cui, concept );
        }
        
        
        fireEditingStopped();
        //System.out.println( "\n" + e.getActionCommand() + "   :   " + table.getSelectedRow());
        //table.repaint();
    }
    
     public static void setSysClipboardText(String writeMe) {  
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(writeMe);  
        clip.setContents(tText, null);  
    }

     /**add this cui and its label as a option into attributes whose "UMLSLINK" is true.*/
    private void copyToAttribute(String cui, String concept) {
        if(( cui == null )||(cui.trim().length()<1))
            return;
        if(( concept == null )||(concept.trim().length()<1))
            return;
        
        if( this.annotation != null ){
            //for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes()) {
            //    if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
            //        continue;
            //    }
            //    if( att.xx umls link status ){
            //        att.put("CUI: [" + cui + "];  LABEL: [" + concept + "]");
            //    }
            //}
        //}else{
        
            // resultEditor.annotationClasses.Depot
            Depot classdepot = new Depot();
            Vector<AnnotationClass> allClasses = classdepot.getAll();

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
                            att.put(concept);
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();   
                }
            }
            //}
        }
    }

    private void saveToCurrentAnnotation(Annotation annotation, String cUIcode, String cUILabel) {
        
        String cUI = "CUI: [" + cUIcode + "];  LABEL: [" + cUILabel + "]";
        
        if(annotation.attributes ==null)
            annotation.attributes = new Vector<AnnotationAttributeDef>();
        
        if(( annotation.annotationclass == null ) || (annotation.annotationclass.trim().length()<1)){
            System.out.println("error: this annotation didn't have class!!!");
            return;
        }
        
        
        try{
            HashSet<String> umlsAttributes_CUICODENLABEL = new HashSet<String>();
            HashSet<String> umlsAttributes_CUICODE = new HashSet<String>();
            HashSet<String> umlsAttributes_CUILABEL = new HashSet<String>();
            
            // resultEditor.annotationClasses.Depot
            Depot classdepot = new Depot();
            Vector<AnnotationClass> allClasses = classdepot.getAll();
            
            AnnotationClass annclass = classdepot.getAnnotatedClass(annotation.annotationclass.trim());
            if( annclass == null )
                return;
            
            Vector<AttributeSchemaDef> privateAtts = annclass.getAttributes();
            if( privateAtts != null ){
                for( AttributeSchemaDef att : privateAtts  ){
                    if(att.isCUICodeNLabel)                    
                        umlsAttributes_CUICODENLABEL.add( att.getName() );
                    if(att.isCUICode)                    
                        umlsAttributes_CUICODE.add( att.getName() );
                    if(att.isCUILabel)                    
                        umlsAttributes_CUILABEL.add( att.getName() );
                    
                }
            }
            
           
            // part 1
            for(String umlsAttribute : umlsAttributes_CUICODENLABEL){    
                
                boolean found = false;
                
                for(int i=0; i<annotation.attributes.size();i++){
                    AnnotationAttributeDef annAttribute = annotation.attributes.get(i);
                    if(annAttribute==null)
                       continue;
                    
                    if(annAttribute.name == null)
                        continue;
                    
                    if(annAttribute.name.trim().compareTo( umlsAttribute.trim())==0){
                        
                         
                        annAttribute.value = cUI;
                        annotation.attributes.set(i, annAttribute);
                        found = true;
                        break;
                    }
                    
                    
                }
                
                if(!found){
                    AnnotationAttributeDef annAttribute = new AnnotationAttributeDef();
                    annAttribute.name = umlsAttribute.trim();
                    annAttribute.value = cUI;
                    annotation.attributes.add( annAttribute );
                }
            }
            
            // part 2
            for(String umlsAttribute : umlsAttributes_CUICODE){    
                
                boolean found = false;
                
                for(int i=0; i<annotation.attributes.size();i++){
                    AnnotationAttributeDef annAttribute = annotation.attributes.get(i);
                    if(annAttribute==null)
                       continue;
                    
                    if(annAttribute.name == null)
                        continue;
                    
                    if(annAttribute.name.trim().compareTo( umlsAttribute.trim())==0){
                        
                         
                        annAttribute.value = cUIcode;
                        annotation.attributes.set(i, annAttribute);
                        found = true;
                        break;
                    }
                    
                    
                }
                
                if(!found){
                    AnnotationAttributeDef annAttribute = new AnnotationAttributeDef();
                    annAttribute.name = umlsAttribute.trim();
                    annAttribute.value = cUIcode;
                    annotation.attributes.add( annAttribute );
                }
            }
            
            // part 3
            for(String umlsAttribute : umlsAttributes_CUILABEL){    
                
                boolean found = false;
                
                for(int i=0; i<annotation.attributes.size();i++){
                    AnnotationAttributeDef annAttribute = annotation.attributes.get(i);
                    if(annAttribute==null)
                       continue;
                    
                    if(annAttribute.name == null)
                        continue;
                    
                    if(annAttribute.name.trim().compareTo( umlsAttribute.trim())==0){
                        
                         
                        annAttribute.value = cUILabel;
                        annotation.attributes.set(i, annAttribute);
                        found = true;
                        break;
                    }
                    
                    
                }
                
                if(!found){
                    AnnotationAttributeDef annAttribute = new AnnotationAttributeDef();
                    annAttribute.name = umlsAttribute.trim();
                    annAttribute.value = cUILabel;
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
    }
}
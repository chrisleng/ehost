/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.spanEdit;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import resultEditor.workSpace.WorkSet;

/**
 *
 * @author leng
 */
public class AnnotationClassChooser{
    protected static JPopupMenu popup;
    protected static userInterface.GUI __gui;
    protected static JTextComponent comp;
    protected static String currentAnnotationClass;

    /**the dialog we used to create/modify/delete classes.*/
    private resultEditor.annotationClasses.Manager classmanager = null;  

    /**class constructor*/
    public AnnotationClassChooser(JTextComponent comp, 
            userInterface.GUI _gui, String currentAnnotationClass){
        AnnotationClassChooser.comp = comp;
        AnnotationClassChooser.__gui = _gui;
        AnnotationClassChooser.currentAnnotationClass = currentAnnotationClass;
    }
    public AnnotationClassChooser(){
    }

    public void popupDialog(String annotation_text){

        resultEditor.annotationClasses.Depot depot = new resultEditor.annotationClasses.Depot();
        String[] annotationClassnames =  depot.getAnnotationClasssnamesString();
        int size = annotationClassnames.length; // numbers of classes


        // popupDialog the popmenu
        popup = new JPopupMenu();
        
        
        for(int i=0; i<size; i++){
            popup = buildpopmenu(popup, annotationClassnames[i], annotation_text);
        }
        if ( size > 0){
            JSeparator seperator = new JSeparator();
            popup.add(seperator);
        }
        popup = buildpopmenu(popup, "Build a new annotation category", annotation_text);




        popup.show(comp, 1, 1);
    }

    private JPopupMenu buildpopmenu(final JPopupMenu popup, final String annotationClassname, final String annotation_text){
        final ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                SaveAnnotationClassChange( annotationClassname, annotation_text );
            }
        };


        JMenuItem item;
        popup.add(item = new JMenuItem(annotationClassname, new ImageIcon("1.gif")));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        if(annotationClassname.compareTo("Build a new annotated class")==0){
            item.setBackground(Color.LIGHT_GRAY);
        }else if(currentAnnotationClass.compareTo(annotationClassname.trim())==0){
            item.setBackground(Color.LIGHT_GRAY);            
        }


        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
        return popup;
    }

    /**Save changes into memory if new annotation class designated via the
     * popup menu.*/
    private void SaveAnnotationClassChange(final String annotationClassname, String annotation_text ){

        if( annotationClassname ==  null )
            return;

        if( annotationClassname.compareTo("Build a new annotation category") ==  0 ){
            /*resultEditor.newClass.Creator creator = new resultEditor.newClass.Creator(eHOST_GUI);
            creator.setVisible(true);
            */
            if (classmanager == null || !classmanager.isActive()) {
                classmanager = new resultEditor.annotationClasses.Manager(__gui);
                classmanager.setVisible(true);
            }
            return;
        }

        if( annotationClassname.compareTo(AnnotationClassChooser.currentAnnotationClass) ==  0 )
            return;

        // display changes on screen
        ((JTextField)comp).setText(annotationClassname);

        // save changes
        save(annotationClassname, annotation_text);
    }

    public void applyJustBuiltAnnotatedClass(String annotationclass, String annotation_text){
        save(annotationclass, annotation_text);
    }
    
    private void save(String annotationclass, String annotation_text){

        resultEditor.workSpace.WorkSet.currentAnnotation.annotationclass = annotationclass;
        resultEditor.workSpace.WorkSet.currentAnnotation.creationDate = commons.OS.getCurrentDate();
        
        if( resultEditor.workSpace.WorkSet.currentAnnotation.hasAttribute()){
            resultEditor.workSpace.WorkSet.currentAnnotation.removeInvalidAttributes();
        }
        if( resultEditor.workSpace.WorkSet.currentAnnotation.hasRelationship()){
            resultEditor.workSpace.WorkSet.currentAnnotation.removeInvalidRelationships();
        }
        
        File current = WorkSet.getCurrentFile();
        if( current != null ){
            Article article = Depot.getArticleByFilename( current.getName() );
            if( article != null ){
                if(article.annotations!=null){
                    for( Annotation annotation : article.annotations )
                        if(annotation!=null)
                            annotation.removeInvalidRelationships( resultEditor.workSpace.WorkSet.currentAnnotation.uniqueIndex, annotationclass );
                }
            }
        }
        
        
        // set the default attribute value if needed.
        resultEditor.workSpace.WorkSet.currentAnnotation = setDefaultAttValue( resultEditor.workSpace.WorkSet.currentAnnotation, annotationclass );

        // after saved changes into memory
        __gui.display_repaintHighlighter();
        __gui.showAnnotationCategoriesInTreeView_selectCategory( annotationclass, annotation_text );
        __gui.showAnnotationCategoriesInTreeView_refresh();
        __gui.showValidPositionIndicators();
        __gui.refresh();
    }

    
    /**Set the default attribute value if needed after changing the class of 
     * current annotation.
     */
    private Annotation setDefaultAttValue(Annotation currentAnnotation, String classname ) {
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        return depot.setDefaultAttValue(currentAnnotation, classname);                
    }
    
    

}


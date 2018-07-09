

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.treeRelated;


import adjudication.SugarSeeder;
import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationRelationship;
import resultEditor.workSpace.WorkSet;
import userInterface.GUI;

/**
 *
 * @author leng
 */
 /**tree view node for annotated class*/
public class NodeOfRelAnnotation extends DefaultMutableTreeNode {
    //String name;
    //boolean selected;
    //Color c;
    //int amountofAnnotations, amountofAnnotationTypes;
    //boolean isSelected_toCreateAnnotation = false;
    Annotation annotation;
    boolean selected = false;
    public String relationshipname = null;
    public String filename = null;
    
    /**tell us whether this relationship is belong to current file
     * that are displayed on screen.
     * . */
    public boolean isCurrentFile = false;

    public NodeOfRelAnnotation( Annotation annotation, String relationshipname, String filename, boolean isCurrentFile ) {
        super();
        this.annotation = annotation;
        this.relationshipname = relationshipname;
        this.filename = filename;
        this.isCurrentFile = isCurrentFile;
    }

    //public Color getClassColor(){
    //    return c;
    //}

    //public void setFlag_isSelected_toCreateAnnotation( boolean flag ){
    //    isSelected_toCreateAnnotation = flag;
    //}

    //public boolean isSelected_toCreateAnnotation(){
    //    return isSelected_toCreateAnnotation;
    //}

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean newValue) {
        selected = newValue;
    }

    public String getDisplayText(){
        /*if ( this.text.contains("<html>"))
            return this.text;

        if ( this.amountofAnnotationTypes > 0 ){
            String displaytext = this.text;
            displaytext = "<html>" + displaytext;
            displaytext = displaytext + " [<b><font color=blue>"
                + this.amountofAnnotationTypes
                + "/"
                + this.amountofAnnotations
                + "</b></font>]</html>";
            return displaytext;
        }else{
            return this.text;
        }*/
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        //depot.getAnnotationByUnique(  , unique)
        String annotationtext = null;
        if((annotation.spanset == null ) || (annotation.spanset.isEmpty()) )
            annotationtext = "<font color=red>(SPANLESS)</font>";
        else
            annotationtext = annotation.annotationText;
        
        
        
        // get the linked relationship
        String linkedannotation = "";
        // You can't find the linked annotation
        // if the name is "null".
        if( relationshipname == null ){ 
            linkedannotation = "";
            
        } else if(annotation.relationships!=null){
            
            for(AnnotationRelationship relationship :  annotation.relationships){
                if(relationship.mentionSlotID == null )
                    continue;
                //System.out.println("1----------->"+relationship.mentionSlotID);
                if( relationship.mentionSlotID.trim().compareTo( this.relationshipname.trim() ) == 0 ){
                    if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ){
                        int originalLinkedAnnotation = relationship.linkedAnnotations.get(0).linkedAnnotationIndex;
                        Annotation adjudicatedAnnotation = getlinkedAdjudicatedAnnotation(originalLinkedAnnotation);
                        if(adjudicatedAnnotation!=null){
                            linkedannotation = adjudicatedAnnotation.annotationText;
                        }else
                            linkedannotation = relationship.linkedAnnotations.get(0).linkedAnnotationText;;
                    }
                    else{
                        linkedannotation = relationship.linkedAnnotations.get(0).linkedAnnotationText;
                    }
                    //System.out.println("2----------->"+linkedannotation);
                    break;
                }
            }
        }
        
        if ((linkedannotation == null)||(linkedannotation.trim().length()<1))
            linkedannotation = "<font color=red>(SPANLESS)</font>";
            
        if( this.isCurrentFile )    
            return "<html>" + annotationtext + "<font color = blue> ---> </font>" + linkedannotation +  "</html>";
        else
            return "<html><font color = gray>" + annotationtext + " ---> " + linkedannotation +  "</font></html>";
    }

    private Annotation getlinkedAdjudicatedAnnotation(int annotationUniqueIndex) {

        //String textOfLinkedAnnotation = null;
        try {


            resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            String path = WorkSet.getCurrentFile().getAbsolutePath();
            int offset = path.lastIndexOf(File.separatorChar);
            if (offset < 1) {
                return null;
            }
            String filename = path.substring(offset + 1, path.length());
            
            
            Annotation annotation = depot.getAnnotation( filename, annotationUniqueIndex);
            if(annotation==null)
                return null;
            

            
            

            if ((annotation.adjudicationAlias != null) && (!SugarSeeder.isAdjudicationAlias(
                    annotation.adjudicationAlias))) {

                String index = annotation.adjudicationAlias;
                Annotation ann2 = depot.getAnnotationByUnique(filename, Integer.valueOf(index));
                return ann2;
            } else {
                return annotation;
            }         

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    
    public String getText() {
        return getDisplayText();
    }

    //public int getAnnotationAmount(){
    //    return this.amountofAnnotations;
    //}

    //public int getAnnotationTypeAmount(){
    //    return this.amountofAnnotationTypes;
    //}

    @Override
    public String toString() {
        return getDisplayText();
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
    
    public Annotation getAnnotation(){
        return this.annotation;
    }
}



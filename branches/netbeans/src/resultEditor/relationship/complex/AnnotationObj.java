/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.complex;

import javax.swing.Icon;
import resultEditor.annotations.Annotation;
import userInterface.GUI;

/**
 *
 * @author Jianwei leng
 */
public class AnnotationObj {

    private Annotation ann;
    private Icon icon;


    public AnnotationObj(){
    }

    public AnnotationObj(Annotation ann, Icon icon) {
        this.ann = ann;
        this.icon = icon;
    }

    public Annotation getAnnotation() {
        return ann;
    }

    public String getText(){
        // get the annotator name of this annotation
        String annotator = ann.getAnnotator(); // usual 
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode ) // unusual
        {
            if( ann.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK )
                annotator = "ADJUDICATION";                        
        }
        
        return "<html>" + ann.annotationText + " <font color=gray>[" + ann.annotationclass + "] by " 
                + annotator 
                + "</font></html>";
    }



    public Icon getIcon() {
        return this.icon;
    }

    public void setAnnotation(Annotation ann) {
        this.ann = ann;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}

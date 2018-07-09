
package userInterface.structure;

import javax.swing.Icon;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.SpanDef;

/**
 *
 * @author leng
 */
public class SpanObj{

    private Annotation ann;
    private Icon icon;
    private SpanDef span;


    public SpanObj(){
    }

    public SpanObj(Annotation ann, SpanDef span, Icon icon) {
        this.ann = ann;
        this.span = span;
        this.icon = icon;
    }

    public Annotation getAnnotation() {
        return ann;
    }

    public String getText(){
        if(span==null)
            return null;
        if(span.text!=null)
            return span.start + " | " + span.end + " : " + span.text;
        else
            return span.start + " | " + span.end + " : ";
    }

    public SpanDef getSpan(){
        return span;
    }


    public Icon getIcon() {
        return this.icon;
    }

    public void setAnnotation(Annotation ann) {
        this.ann = ann;
    }

    public void setSpan(SpanDef span) {
        this.span = span;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}


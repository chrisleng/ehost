/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotations;

/**
 * This is the structure of span of annotation.
 *
 * @author Jianwei "Chris" leng, Jan 19, 2012
 */
public class SpanDef {

    /**the start point of the span.*/
    public int start = -1;
    /**the end point of the span.*/
    public int end = -1;

    public String text;


    public SpanDef(int start, int end){
        this.start = start;
            this.end = end;
    }

    public SpanDef(int start, int end, String text){
        this.start = start;
            this.end = end;
            this.text = text;
    }

    /**check whether two spans have same start point and end point.*/
    public boolean isEqual(SpanDef objSpan) {
        if ((this.start == objSpan.start)
                && (this.end == objSpan.end)) {
            return true;
        } else {
            return false;
        }
    }

    /**check whether two annotations are overlapping.
     *
     * NOTICE:
     * Two spans have 'exact' same start and end it NOT an overlap,
     * it's duplicate.
     */
    public boolean isOverlapping(SpanDef objSpan){
        if (objSpan==null)
            return false; // no overlap to a null span

        int a= this.start;
        int b= this.end;
        int c= objSpan.start;
        int d= objSpan.end;

        if((a==c)&&(b==d))
            return false;

        // c<=a<=b<=d
        if((c<=a)&&(a<b)&&(b<=d))
            return true;
        // a<=c<d<=b
        else if((a<=c)&&(c<d)&&(d<=b))
            return true;
        // a<=c<b<=d
        else if((a<=c)&&(c<b)&&(b<=d))
            return true;
        // c<=a<d<=b
        else if((c<=a)&&(a<d)&&(d<=b))
            return true;
        else if((a<=c)&&(c<d)&&(d<=b))
            return true;

        return false;
    }

    public SpanDef getOverlapping(SpanDef objSpan){
        if (objSpan==null)
            return null; // no overlap to a null span

        int a= this.start;
        int b= this.end;
        int c= objSpan.start;
        int d= objSpan.end;

        if((a==c)&&(b==d))
            return null;

        // c<=a<=b<=d
        if((c<=a)&&(a<b)&&(b<=d))
            return new SpanDef(a,b);
        // a<=c<d<=b
        else if((a<=c)&&(c<d)&&(d<=b))
            return new SpanDef(c,d);
        // a<=c<b<=d
        else if((a<=c)&&(c<b)&&(b<=d))
            return new SpanDef(c,b);
        // c<=a<d<=b
        else if((c<=a)&&(a<d)&&(d<=b))
            return new SpanDef(a,d);
        else if((a<=c)&&(c<d)&&(d<=b))
            return new SpanDef(c,d);

        return null;
    }
}

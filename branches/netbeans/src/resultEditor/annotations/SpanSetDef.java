/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotations;

import java.util.Vector;
import java.util.logging.Level;

/**
 * Class that designed to store and manage annotation spans. Usually, each
 * annotation only have one span, but some times,
 *
 * @author leng
 */
public class SpanSetDef {

    /**the is an list of spans. The first span will be the main span of these annotation */
    private Vector<SpanDef> spans = new Vector<SpanDef>();


    /**This method set the given span as the only span of this annotation*/
    public void setOnlySpan(int spanstart, int spanend) {
        spans.clear();
        this.addSpan(spanstart, spanend);
    }

    /**return the number of spans of current annotation.*/
    public int size() {
        return spans.size();
    }
    

    /**return a specific span by their index in the set of spans.*/
    public SpanDef getSpanAt(int index){
        if( spans == null )
            return null;

        if( index > size() -1 )
            return null;

        if( index < 0 )
            return null;
        
        return this.spans.get(index);
    }

    /**check whether the spans instances that contained in an given spanset
     * are all null objects.
     * The data structure of the "SpanSetDef" is "Vector<SpanSetDef>".
     */
    public boolean isAllNullObjects(SpanSetDef spanset){
        if(spanset == null)
            return true;

        for ( SpanDef span : spanset.spans ){
            if(span != null)
                return false;
        }
        
        return true;
    }

    /**check whether the spans instances that contained in an given spanset
     * are all null objects.
     * The data structure of the "SpanSetDef" is "Vector<SpanSetDef>".
     *
     * Note: 
     * This method is a overloading of method "isAllNullObjects(SpanSetDef
     * spanset)"
     */
    public boolean isAllNullObjects(){
        return isAllNullObjects(this);
    }

    /**Check the given spanset, compare it with current spanset,
     * and return false if there are not same to each other.
     * It's totally same as the method "public boolean equals(SpanSetDef)"
     * 
     * Notice: if any one of these spanset are null, all contains all null
     *          objects, the return will be "FALSE".
     *
     * @param   objSpans
     *          an instance of class "SpanSetDef". It's the given spanset that
     *          you want to compare it with you current span.
     * 
     */
    public boolean equals(SpanSetDef objSpans) throws Exception {
        return isDuplicates(objSpans);
    }
           
    /**Check to spanset, and return false if there are not same to each other.*/
    public boolean isDuplicates(SpanSetDef objSpans) {

        
        // neither this annotation-spans and the objective annotation-spans
        // can be null
        if ((spans == null) || (objSpans == null)) {
            log.LoggingToFile.log(Level.SEVERE,  "1201190443:: while you are comparing spans"
                    + " of two annotations. None of them can be null.");

            return false;
        }

        // if any of them contains all null objects, we return false;
        if ( this.isAllNullObjects() )
            return false;
        if ( objSpans.isAllNullObjects() )
            return false;

        // begin to get spans
        Vector<SpanDef> spans1 = this.spans;
        Vector<SpanDef> spans2 = objSpans.spans;
        if ((spans1 == null) || (spans2 == null)) {
            log.LoggingToFile.log(Level.SEVERE,  "1202231227:: compareing null items");
            return false;
        }

        // get size: number of spans in each spanset
        int size1= spans1.size(), size2 = spans2.size();

        // create two arrays
        SpanDef[] set1 = new SpanDef[size1];
        SpanDef[] set2 = new SpanDef[size2];

        // copy spans into these two arrays from two vectors
        for(int i=0; i<size1;i++){
            set1[i] = spans1.get(i);
        }
        for(int j=0; j<size2;j++){
            set2[j] = spans2.get(j);
        }

        
        if((set1==null)||(set2==null))
            return false;

        // compare and return restuls
        return isSame( set1, set2 );
    }

    

    /**return the number of non null span in this span set*/
    public int size_nonNull() {
        if (this.spans == null) {
            return 0;
        }

        int toReturn = 0;
        for (SpanDef span : this.spans) {
            if (span != null) {
                toReturn++;
            }
        }

        return toReturn;
    }

    /**return a string that contains information of all spans in format of html.
     * Such as "<html> span1start | span1end <p> span2start | span2end </html>"
     */
    public String toHTML(){
        String head = "<html>";
        String tail = "<html>";

        String body = "";
        if( this.spans != null ){
            boolean firstspan = true;  // indicate whether current span is
                                        // the first span in the list.
            
            for(SpanDef span : this.spans )
                if( spans != null ){
                    if( firstspan )
                        body = span.start + " | " + span.end;
                    else 
                        body = body + "<p>" + span.start + " | " + span.end;
                }
        }

        return head + body + tail;
    }

    /**Get the span start which has smallest value than others. If there is
     * only one span, return the start of that span. */
    public int getMinimumStart(){

        int smallest=0; boolean isFirst = true;
        
        for( SpanDef span : spans ){
            if( span != null ){
                if( isFirst ){
                    smallest = span.start;
                    isFirst = false;
                }else{
                    if( smallest > span.start )
                        smallest = span.start;
                }
            }
        }

        return smallest;
            
    }

    /**Get the span end which has biggest value than others. If there is
     * only one span, return the end of that span. */
    public int getMaximumEnd(){

        int maximum=0; boolean isFirst = true;

        for( SpanDef span : spans ){
            if( span != null ){
                if( isFirst ){
                    maximum = span.end;
                    isFirst = false;
                }else{
                    if( maximum < span.end )
                        maximum = span.end;
                }
            }
        }

        return maximum;

    }

    /**check whether two spansets are overlapping. **NOTICE** dulplicate is NOT
     * overlapping here.
     */
    public SpanSetDef getOverlapping(SpanSetDef otherSpanSet) {

        SpanSetDef overlappings = new SpanSetDef();
        
        if ((otherSpanSet == null) || (this.spans == null)) {
            return null;
        }

        // are they duplicates?
        try {
            if (this.equals(otherSpanSet)) {
                return null;
            }
        } catch (Exception ex) {

        }

        for (SpanDef span_this : this.spans) {
            if (span_this == null) {
                continue;
            }

            for (SpanDef span_obj : otherSpanSet.spans) {
                if (span_obj == null) {
                    continue;
                }

                SpanDef overlap = span_this.getOverlapping(span_obj);
                if ( overlap != null ) {
                    overlappings.addSpan(overlap.start, overlap.end);
                }

                if (span_this.isEqual(span_obj))
                    overlappings.addSpan(span_obj.start, span_obj.end);
            }
        }

        return overlappings;
    }

    /**check whether two spansets are overlapping. **NOTICE** dulplicate is NOT
     * overlapping here.
     */
    public boolean isOverlapping(SpanSetDef otherSpanSet) {

        if ((otherSpanSet == null) || (this.spans == null)) {
            return false;
        }

        // are they duplicates?
        try {
            if (this.equals(otherSpanSet)) {
                return false;
            }
        } catch (Exception ex) {
            
        }

        for (SpanDef span_this : this.spans) {
            if (span_this == null) {
                continue;
            }

            for (SpanDef span_obj : otherSpanSet.spans) {
                if (span_obj == null) {
                    continue;
                }

                if (span_this.isOverlapping(span_obj)) {
                    return true;
                }

                if (span_this.isEqual(span_obj))
                    return true;
            }
        }



        return false;
    }

    /**add a new span into a spanset for an annotation, whatever we have zero,
     * or one, or multiple spans in this spanset of the annotation.
     * .*/
    public void addSpan(int start, int end) {
        if(this.spans == null)
            spans = new Vector<SpanDef>();

        if(!contains(start, end)){
            spans.add(new SpanDef(start, end));
        }
    }

    /**add a new span into a spanset for an annotation, whatever we have zero,
     * or one, or multiple spans in this spanset of the annotation.
     * .*/
    public void addSpan(int start, int end, String text) {
        if(this.spans == null)
            spans = new Vector<SpanDef>();

        if(!contains(start, end)){
            spans.add(new SpanDef(start, end, text));
        }
    }

    public void removeSpan(int index){
        if((index >=0)&&(index<this.size()))
            spans.removeElementAt(index);
    }

    public String getAnnotationText(){
        if(size()<=0)
            return null;

        String annotationText = null;
        boolean isFirstRecord = true;

        for(SpanDef span : spans ){
            if(span == null)
                continue;
            
            if( isFirstRecord ){
                annotationText = span.text;
                isFirstRecord = false;
            }else{
                annotationText = annotationText + " ... " + span.text;
            }

        }

        return annotationText;
    }

    /**check whether we already have a span that have the span-start and the
     * span-end.
     */
    public boolean contains(int start, int end){
        if( this.spans == null )
            return false;
        if( this.spans.size() < 1 )
            return false;


        for(SpanDef span : spans){
            if(span==null)
                continue;
            if( (span.start == start) && (span.end == end) )
                return true;
        }

        return false;
        
    }

    /**check whether we already have the given span that have the same
     * span-start and the span-end.
     */
    public boolean contains(SpanDef span){
        if( span == null )
            return true;
        else return contains( span.start, span.end );
    }

    /**compare spans in two arrays are same or not. The basic rule is that we
     * replace the spans by null object in the array if we find that two spans
     * in different array are matched. And after we checked all spans in these
     * two arraeis, we can said we found difference if there still has any
     * non-null object.
     *
     * NOTICE: none of "set1" and "set2" can be null.
     */
    private boolean isSame(SpanDef[] set1, SpanDef[] set2){

        // check to find matches, and replace matches by null objects
        for(int i=0; i<set1.length; i++){

            SpanDef span_i = set1[i];
            if(span_i==null)
                continue;

            for(int j=0; j<set2.length; j++){
                SpanDef span_j = set2[j];
                if(span_j == null)
                    continue;

                if (span_i.isEqual(span_j)){
                    set1[i] = null;
                    set2[j] = null;
                    break;
                }
                    
            }
        }

        // go though these two arrays, return "false" if finding any non-null
        // object;
        // An non-null object after matching means this item only appear in
        // one spanset. So these two spans are different to each other and they
        // are NOT duplicates.
        for(int i=0; i<set1.length; i++){
            SpanDef span_i = set1[i];
            if(span_i!=null)
                return false;
        }

        for(int j=0; j<set2.length; j++){
            SpanDef span_j = set2[j];
            if(span_j!=null)
                return false;
        }
        
        return true;  // these two array of spans are same to each other.
                      // BUT They may have different sequence.
    }

    
    /**Report whether current spanset is empty or not, it returns ture while
     * current spanset is null, or size of spanset is smaller than 1.*/
    public boolean isEmpty() {
        
        if(this.spans == null ){
            spans = new Vector<SpanDef>();
            return true;
        }
    
        if( spans.size() < 1 )
            return true;
                
        return false;
    }

    /**get a deep copy of this instance. */
    public SpanSetDef getCopy() {
        SpanSetDef spanset = new SpanSetDef();
        if(spans!=null){
            for(SpanDef span : spans ){
                if( span == null )
                    continue;
                else{
                    spanset.spans.add(
                            new SpanDef( span.start, span.end, span.text )
                            );
                }
            }
        }
        return spanset;
    }


}

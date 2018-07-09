/*
 * This class is built to assemble a new Java Component which realize function
 * to show graphics path of complex relationship between annotations.
 */

package userInterface.txtScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.SpanDef;
import resultEditor.annotations.SpanSetDef;

/**
 * This class is built to assemble a new Java Component which realize function
 * to show graphics path of complex relationship between annotations.
 *
 * @author Jianwei Chris Leng @ Oct 11, 2010
 *         Division of Epidemiology, School of Medicine, University of Utah
 *
 */
public class TextScreen extends JTextPane{

    /**List used to store coordinates of annotations with complex relationships.*/
    protected ComplexRelationshipCoordinates coordinates;
        


    /**class constructor*/
    public TextScreen(){
        super();

        /**测试：可以用来取得行列，注意（这个行列是在原文章中的行列，而不是wrap过后的）*/
        /*this.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                int caretPosition = TextScreen.this.getCaretPosition();
                Element root = TextScreen.this.getDocument().getDefaultRootElement();
                int row = root.getElementIndex(caretPosition);
                int column = caretPosition -
                root.getElement(row).getStartOffset();
                System.out.println("Row : " + (row + 1));
                System.out.println("Column: " + (column + 1));
            }
        });
         */
    }


    /**paint method of custom component of text*/
    @Override
    public void paint(Graphics g1){

        try{
            super.paint(g1);

            drawSymbols_Rel( g1 );
            drawSymbols_Att( g1 );
            /**
            //==== only for test ====
            this.setComplexRelationsCoordinates(20, 28, 33, 38);
            this.setComplexRelationsCoordinates_extraEnds(400, 404);
             */

            drawComplexRelationship(g1);
            drawPath(g1);
            
            
            
        }catch(Exception ex){
        }
    }

    private void drawSymbols_Rel( Graphics g1 ){
        if( g1 == null )
            return;
        
        if( this.annotations_hasAtt == null )
            return;
        
        for( Annotation ann : annotations_hasAtt ){
            paintSymbol( g1, ann, "attribute") ;
            //System.out.println( "Att: " + ann.spanset.getSpanAt(0).start + ", " + ann.spanset.getSpanAt(0).end );
        }
        
    }
    
    private void drawSymbols_Att( Graphics g1 ){
        if( g1 == null )
            return;
        
        if( this.annotations_hasRel == null )
            return;
        
        for( Annotation ann : annotations_hasRel ){
            paintSymbol( g1, ann, "relationship") ;
            //System.out.println( "Rel: " + ann.spanset.getSpanAt(0).start + ", " + ann.spanset.getSpanAt(0).end );
        }
        
    }
    
    
    
    private void drawComplexRelationship(Graphics g1){
        
        if (coordinates==null)
            return;

        if (coordinates.EndAnnotations == null)
            return;
        if (coordinates.EndAnnotations.size() < 1)
            return;

        if (g1==null)
            return;

        try{
            String text;

            // checking data before drawing rectangle and text for spans 
            // of the source annotation
            if(coordinates.__coordinate_of_StartAnnotation==null)
                return;
            if(coordinates.__coordinate_of_StartAnnotation.spanset == null)
                return;
            
            int spanAmount_of_sourceAnnotation = coordinates.__coordinate_of_StartAnnotation.spanset.size();
            if( spanAmount_of_sourceAnnotation < 1 )
                return;
            
            // draw rectangle and text for spans of the source annotation
            for( int t=0; t < spanAmount_of_sourceAnnotation; t++){
                SpanDef span = coordinates.__coordinate_of_StartAnnotation.spanset.getSpanAt(t);
                if( span == null )
                    continue;
                
                text = getTextbyOffset(span.start, span.end);
                // draw first rectangle boarder for source annotation
                drawtab( g1, 
                        span.start, span.end, 
                        true, 
                        text,
                        coordinates.__coordinate_of_StartAnnotation.color,
                        "father" 
                        );
            }

            // draw rectangle boarders for corrlated annotations
            for( Coordinate linked: coordinates.EndAnnotations ){
                if ( linked == null )
                    continue;
                
                SpanSetDef spanset = linked.spanset;
                if(spanset == null)
                    continue;
                
                int spanSize_of_linkedAnnotation = spanset.size();
                
                for( int t = 0 ; t< spanSize_of_linkedAnnotation; t++ ){
                    SpanDef span = spanset.getSpanAt(t);
                    if( span == null )
                        return;
                    text = getTextbyOffset( span.start, span.end );
                    if( text == null )
                        continue;
                    
                    drawtab(g1, span.start, span.end, true, text, linked.color, null);
                    //System.out.println("draw ends x = " + linked.x + ", y = " + linked.y);
                }
                
            }
        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE,"error 1010132015:: fail to draw rectangle for annotations with complex relationships");
        }

        
    }

    public String getTextbyOffset(int spanstart, int spanend){
        String text = null;
        try{
            text = this.getDocument().getText(spanstart, spanend-spanstart);
            return text;
        }catch(Exception ex){

        }
        return text;
    }

    /**准备绘图： 路径*/
    private void drawPath(Graphics g1){
        try {
            if (coordinates == null) {
                return;
            }
            if (coordinates.EndAnnotations == null) {
                return;
            }
            if (coordinates.EndAnnotations.size() < 1) {
                return;
            }
            if (g1 == null) {
                return;
            }
            Point p0;
            int x0 = coordinates.__coordinate_of_StartAnnotation.spanset.getSpanAt(0).start;
            int y0 = coordinates.__coordinate_of_StartAnnotation.spanset.getSpanAt(0).end;
            Rectangle r0 = this.getUI().modelToView(this, x0);
            int cx0 = r0.x +4;
            int cy0 = r0.y +4;

            p0 = new Point(cx0, cy0);

            for(Coordinate c : coordinates.EndAnnotations){
                if(c==null) continue;
                
                int x = c.spanset.getSpanAt(0).start;
                Rectangle r = this.getUI().modelToView(this, x);
                int cx = r.x +4;
                int cy = r.y +4;
                Point p= new Point(cx, cy);

                drawPathLine(g1, p0, p);
            }

        } catch (Exception ex) {

        }


    }
    
    private void paintSymbol( Graphics g1, Annotation att, String type ){
        if( ( type == null ) || (type.trim().length()<1) || (g1 == null)
                || (att==null) )
            return;
        
        // ~~~~ 1 ~~~~  revursive for one situation that this annotation are 
        // layout on more than one line
        SpanSetDef spans = att.spanset;
        if( spans == null )
            return;
        
        SpanDef span = spans.getSpanAt(0);
        if( span == null )
            return;
        
        if( span.start < 0 )
            return;
        if( span.end < 0 )
            return;
        
        try{
                        
            // ~~~~ 1.1 ~~~~ get rectangle of the annotation
            Rectangle rect = this.getUI().modelToView(this, span.start);
            int x = rect.x;
            int y = rect.y; // get the Y coordinate of the upper-left corner of the Rectangle.
            //Rectangle rect2 = this.getUI().modelToView(this, att.spanend);
            //int y2 = rect2.y; // get the Y coordinate of the upper-left corner of the Rectangle.
            
            y=y-4;
            // ~~~~ 1.2 ~~~~ get 
            // this annotation are layout on more than one line, if the span 
            // start and the span end have different Y coordinate of their 
            // upper-left corner of the their rectangle.
            int symboltype = 0;
            
            if( type.compareTo( "attribute") == 0 ){
                symboltype = 1;
            }else
                symboltype = 0;
            
            
            if(symboltype == 0){
                g1.setColor(Color.black);
                g1.fillOval(x+1-1,    y+1-1, 6, 6);
                g1.setColor(Color.red);
                g1.fillOval(x-1,      y-1, 6, 6);                
                g1.fillOval(x+2-1,    y+2-1, 2, 2);
            }else{
                x=x+8;
                g1.setColor(Color.black);
                g1.fillOval(x+1-1,    y+1-1, 6, 6);
                g1.setColor(Color.green);
                g1.fillOval(x-1,      y-1, 6, 6);                
                g1.fillOval(x+2-1,    y+2-1, 2, 2);
            }
            

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, ex.getMessage() );
        }
        
    }
    

    /**绘图：路径*/
    private void drawPathLine(Graphics g1, Point startpoint, Point endpoint){
        // 取当前窗口尺寸
        // 当前视窗在总视窗中的位置，startOffset，endOffset, width, height
        int viewy1 = this.getVisibleRect().y;
        int viewy2 = this.getVisibleRect().y + this.getVisibleRect().height;
        //System.out.println("visible rect x = " + viewx);
        //System.out.println("visible rect y = " + viewy);

        // 默认线条高度
        int lineDefaultHeight = 26;
        int lineDefaultWidth = 40;
        
        g1.setColor(Color.black);
        BasicStroke b2=new BasicStroke(3.0f);
        BasicStroke b3=new BasicStroke(3.0f);
        Graphics2D g2d = ((Graphics2D)g1);
        g2d.setColor(new Color(255,153,0));
        g2d.setStroke(b3);

        // 类型1： 如果在一排
        if( Math.abs( startpoint.y - endpoint.y ) <= 4 ){
            // 上下都有足够空间，绘在上面
            int y;
            if((   (startpoint.y - lineDefaultHeight) > viewy1 )
                &&((startpoint.y + lineDefaultHeight) < viewy2)){
                y = (  startpoint.y < endpoint.y ?
                    (startpoint.y - lineDefaultHeight) : (endpoint.y - lineDefaultHeight));

            }
            // 上面有空间
            else if(   (startpoint.y - lineDefaultHeight) > viewy1 ) {
                y = (  startpoint.y < endpoint.y ?
                    (startpoint.y - lineDefaultHeight) : (endpoint.y - lineDefaultHeight));
                
            }
            else
            // 下面有空间
            {
                y = (  startpoint.y > endpoint.y ?
                    (startpoint.y + lineDefaultHeight) : (endpoint.y + lineDefaultHeight));
            }

            
            g2d.drawLine(startpoint.x, startpoint.y, startpoint.x, y);
            g2d.drawLine(endpoint.x, endpoint.y, endpoint.x, y);
            g2d.drawLine(startpoint.x, y, endpoint.x, y);

            
        }
        else
        // 类型2： 如果不在一排
        {
            // 判断左边是否有空间
            int x = (startpoint.x < endpoint.x? startpoint.x : endpoint.x);
            // 左边没有空间
            //if( x - lineDefaultWidth < 0 ){
                if(x == startpoint.x){
                    
                    g2d.drawLine( startpoint.x, startpoint.y, x, endpoint.y);
                    g2d.drawLine(   endpoint.x,   endpoint.y, x, endpoint.y);
                   
                }else{
                    x = endpoint.x;
                    
                    g2d.drawLine( startpoint.x, startpoint.y, x, startpoint.y);
                    g2d.drawLine(   endpoint.x,   endpoint.y, x, startpoint.y);
                    
                }
            //
            /*}
            else
            // 左边有绘图空间
            {
                x = x - lineDefaultWidth;
                g2d.drawLine(startpoint.x, startpoint.y, x, startpoint.y);
                g2d.drawLine(  endpoint.x,   endpoint.y, x,   endpoint.y);
                g2d.drawLine(x, startpoint.y, x, endpoint.y);
            }
            */


        }
    }


    /**
     * 绘图： 绘制第一个annotation周围的框框
     */
    /*public void paintFirstAnnotationBorader(Graphics g1, int startOffset, int endOffset){
        if (g1==null)
            return;

        // 找到当前文章的起始位置（0，文章长度）
        // start of doc
        int startoffset  = this.getDocument().getDefaultRootElement().getStartOffset();
        System.out.println("\nStart offset = " + startoffset);
        // end of doc // this.getDocument().getEndPosition()
        int endoffset  = this.getDocument().getDefaultRootElement().getEndOffset();
        System.out.println("end offset = " + endoffset);
        // 找到文章有多少个段落， 每一个段落就是从源文件中读取出来的一行
        // not row amount, it's just the amount of lines
        int elementcount=this.getDocument().getDefaultRootElement().getElementCount();
        System.out.println("element count = " + elementcount);

        // 找到在那一段
        int linesIndex = this.getDocument().getDefaultRootElement().getElementIndex(startOffset);
        System.out.println("linesIndext of x("+ startOffset +")= " + linesIndex);
        Element e = this.getDocument().getDefaultRootElement().getElement(linesIndex);

        // 找到该段的起始位置
        int s = e.getStartOffset();
        int ee = e.getEndOffset();
        System.out.println("start of this element = " + s + "; end of this element = " + ee );

        float ax = this.getAlignmentX();
        float ay = this.getAlignmentY();
        System.out.println( "alignment x = " + ax + "alignment x = " + ay);
        this.getCaretPosition();

        // 当前视窗在总视窗中的位置，startOffset，endOffset, width, height
        int viewx = this.getVisibleRect().x;
        int viewy = this.getVisibleRect().y;
        System.out.println("visible rect x = " + viewx);
        System.out.println("visible rect y = " + viewy);

        // draw rectangle for first(source) annotation
        drawRoundRect(g1, startOffset, endOffset);

    }*/

    private void drawRoundRect(Graphics g1, int startoffset, int endoffset){
        // 绘制源头框体
        try {
            // color for line
            Color linecolor_outer = new Color(153,153,204);
            Color linecolor = new Color(102,102,204);
            Color linecolor_inner = new Color(204,204,255);

            // get rectangle of the annotation
            Rectangle re = this.getUI().modelToView(this, startoffset);
            // coordinates (x1,y1) of the upper left point of the rectangle
            // view to this caret point
            int rect_upperleftX = re.x, rect_upperleftY = re.y;
            int rect_height = re.height;
            // coordinates (x2,y2) of the upper right point of the rectangle
            // view to this caret point
            re = this.getUI().modelToView(this, endoffset);
            int rect_upperrightX = re.x, rect_upperrightY = re.y;

            for(int i=0; i<4;i++){

                if(i==0) g1.setColor(linecolor_outer);
                else if(i==1) g1.setColor(linecolor);
                else if(i==2) g1.setColor(linecolor_inner);

                // opinion1: draw rect
                /*// lining: topleft - topright
                g1.drawLine(rect_upperleftX+i, rect_upperleftY+i, rect_upperrightX - i,
                        rect_upperleftY + i );

                // lining: lowerleft - lowerright
                g1.drawLine(rect_upperleftX+i, rect_upperleftY-i+rect_height, rect_upperrightX - i,
                        rect_upperleftY - i+rect_height );

                // lining: topleft - lowerleft
                g1.drawLine(rect_upperleftX+i, rect_upperleftY+i, rect_upperleftX+i,
                        rect_upperleftY - i + rect_height );

                // lining: topright - lowerright
                g1.drawLine(rect_upperrightX - i, rect_upperrightY + i, rect_upperrightX - i,
                        rect_upperrightY - i + rect_height );
                */

                // opinion 2: draw rect with round cornners
                g1.drawRoundRect(rect_upperleftX+i, rect_upperleftY+i, rect_upperrightX - rect_upperleftX-i*2, rect_height-i*2, 6, 6);
            }



            //System.out.println("start startOffset = " + re2.startOffset + ", endOffset = " + re2.endOffset + "; width = "+re2.width + ", height = " + re2.height);

        } catch (BadLocationException ex) {
            log.LoggingToFile.log( Level.SEVERE, ex.getMessage());
        }
    }

    
    /**
     * Draw rectangle(s) with annotation's class color to a given annotation's 
     * span start and span end. Usually we only need to draw one tab for one 
     * annotation, but while the annotation cross
     * 
     * @param   isUpwardTab
     *          true:  tab head to top
     *          fail:  tab head to bottom
     */
    private void drawtab(Graphics g1, int startoffset, int endoffset, boolean isUpwardTab, String text, Color color, String flag){

        // ~~~~ 1 ~~~~  revursive for one situation that this annotation are 
        // layout on more than one line
        try{
                        
            // ~~~~ 1.1 ~~~~ get rectangle of the annotation
            Rectangle rect1 = this.getUI().modelToView(this, startoffset);
            int y1 = rect1.y; // get the Y coordinate of the upper-left corner of the Rectangle.
            Rectangle rect2 = this.getUI().modelToView(this, endoffset);
            int y2 = rect2.y; // get the Y coordinate of the upper-left corner of the Rectangle.
            
            // ~~~~ 1.2 ~~~~ get 
            // this annotation are layout on more than one line, if the span 
            // start and the span end have different Y coordinate of their 
            // upper-left corner of the their rectangle.
            if( y1!=y2){
                for( int i=startoffset; i<=endoffset;i++){
                    Rectangle recttemp = this.getUI().modelToView(this, i);
                    int ytemp = recttemp.y;
                    if( ytemp == y1 )
                        continue;
                    else{
                        //System.out.println("i="+i+", start = " + startoffset + ", endoffset = "+ endoffset);
                        drawtab(g1, startoffset, i-1, isUpwardTab, text.substring(0, i-1-startoffset), color, flag);//
                        drawtab(g1, i, endoffset, isUpwardTab, text.substring(i-startoffset, text.length()), color, "dead");//
                        return;
                    }

                }
            }

        }catch(Exception ex){
            log.LoggingToFile.log( Level.SEVERE, ex.getMessage() );
        }

        
        // ~~~~ 2 ~~~~
        // This part is used to draw the rectangle area of a tab 
        try {
            // color for line
            //Color linecolor_outer = new Color(153,153,102);
            Color linecolor;
            
            if(( flag!=null)&&(flag.equals("father")))
                linecolor = new Color(204,0,0);
            else
                linecolor = new Color(0,0,204);
            
            Color linecolor_outer = linecolor;
            Color linecolor_inner = new Color(204,204,204);
            

            // get rectangle of the annotation
            Rectangle re = this.getUI().modelToView(this, startoffset);

            // coordinates (x1,y1) of the upper left point of the rectangle
            // view to this caret point
            int rect_upperleftX = re.x, rect_upperleftY = re.y;
            int rect_height = re.height;
            // coordinates (x2,y2) of the upper right point of the rectangle
            // view to this caret point
            re = this.getUI().modelToView(this, endoffset);
            int rect_upperrightX = re.x, rect_upperrightY = re.y;

            int extraheight = 3;

            // dirction of tab's head: to top or to bottom
            if( isUpwardTab ){

                Color fillcolor;
                fillcolor = ( color==null? new Color(204,204,204) : color);
                
                //g1.setColor( new Color(204,204,204) );
                //g1.fillRect(rect_upperleftX+3, rect_upperleftY-extraheight+3, 
                //        Math.abs(rect_upperrightX - rect_upperleftX)-3,
                //        rect_height+extraheight-3);

                //g1.setColor(fillcolor);
                //g1.fillRect(rect_upperleftX, rect_upperleftY - extraheight , rect_upperrightX - rect_upperleftX + 2, extraheight+4);

                //g1.setColor(linecolor_outer);
                //g1.drawRect(rect_upperleftX, rect_upperleftY - extraheight-1, rect_upperrightX - rect_upperleftX, rect_height+extraheight+2);
                                                
                g1.setColor( Color.WHITE );                
                g1.fillRect(rect_upperleftX, rect_upperleftY - extraheight , rect_upperrightX - rect_upperleftX + 2, re.height + 2);
                g1.setColor( linecolor );
                //g1.fillRoundRect(re.x, re.y, re.width, re.height, 10, 10);
                g1.fillRect(rect_upperleftX, rect_upperleftY - extraheight , rect_upperrightX - rect_upperleftX + 2, re.height + 2 );
                g1.setColor( color );
                g1.fillRect(rect_upperleftX+2, rect_upperleftY - extraheight + 2 , rect_upperrightX - rect_upperleftX -2, re.height - 2 );
                //g1.drawRect(+1, rect_upperleftY - extraheight, rect_upperrightX - rect_upperleftX-2, rect_height+extraheight-0);
                //.setColor(linecolor_inner);
                //g1.drawRect(rect_upperleftX+2, rect_upperleftY - extraheight+1, rect_upperrightX - rect_upperleftX-4, rect_height+extraheight-2);

                g1.setColor(Color.black);

                // draw point
                if(( flag!=null)&&(flag.equals("dead"))){
                    
                } else {

                    int cx = rect_upperleftX+2;
                    int cy = rect_upperleftY - extraheight+2;
                    g1.fillOval( cx, cy, 10, 10);

                    if(( flag!=null)&&(flag.equals("father")))
                        g1.setColor( Color.red );
                    else
                        g1.setColor( new Color(255,153,0) );
                    
                    g1.fillOval( cx, cy, 8, 8);
                }

                g1.setColor(Color.black);
                int elementindex = this.getDocument().getDefaultRootElement().getElementIndex(startoffset);
                Element e = this.getDocument().getDefaultRootElement().getElement(elementindex);
                Font font = this.getStyledDocument().getFont( e.getAttributes() );

                int r = color.getAlpha();
                int b = color.getBlue();
                int gg = color.getGreen();
                double grayLevel = r * 0.299 + gg * 0.587 + b * 0.114;
                Color textcolor =  grayLevel > 192 ? Color.BLACK : Color.white;
                
                g1.setFont( font );
                g1.setColor( textcolor );
                // draw text
                if((text!=null)&&(text.length()>0))
                    //g1.drawString(text, rect_upperleftX+2, rect_upperleftY + rect_height-2);
                    g1.drawString(text, rect_upperleftX, rect_upperleftY + re.height - 7 );
                
                // opinion1: draw rect
                /*

                // lining: lowerleft - lowerright
                g1.drawLine(rect_upperleftX+i, rect_upperleftY-i+rect_height, rect_upperrightX - i,
                        rect_upperleftY - i+rect_height );

                // lining: topleft - lowerleft
                g1.drawLine(rect_upperleftX+i, rect_upperleftY+i, rect_upperleftX+i,
                        rect_upperleftY - i + rect_height );

                // lining: topright - lowerright
                g1.drawLine(rect_upperrightX - i, rect_upperrightY + i, rect_upperrightX - i,
                        rect_upperrightY - i + rect_height );
                */

            }



            //System.out.println("start startOffset = " + re2.startOffset + ", endOffset = " + re2.endOffset + "; width = "+re2.width + ", height = " + re2.height);

        } catch (Exception ex) {
            log.LoggingToFile.log( Level.SEVERE, "Error 1010140012:: fail to draw tab" + ex.toString());
        }
    }

    /**
     * Remove all Complex Relationship Coordinate, then we can update screen
     * to remove the relationship graphics path from document viewer.
     */
    public void clearComplexRelationshipCoordinates()
    {
        if(this.isVisible())
        {
            this.coordinates = null;
            this.paint(this.getGraphics());
        }
    }

    /**This method is used to set the first path of a relationship between the 
     * given source annotation and the linked annotation.
     * 
     * In the class of "ComplexRelationshipCoordinates", which we used to store
     * information of the graph path, only one source annotation is allowed, but
     * you can have multiple linked annotations.
     * 
     * @param   SpanSetDef  _sourceAnnotationSpans
     *          the spans of the source Annotation.
     * 
     * @param   Color       _sourcecolor
     * 
            SpanSetDef  _linkedAnnotationSpans,
            Color       _linkedcolor
     */
    public void setComplexRelationsCoordinates(
            SpanSetDef  _sourceAnnotationSpans,
            Color       _sourcecolor, 
            SpanSetDef  _linkedAnnotationSpans,
            Color       _linkedcolor ){
        
        // validity checking
        if( ( _sourceAnnotationSpans == null ) || (_linkedAnnotationSpans == null) )
            return;        
        if( _sourceAnnotationSpans.size() < 1 )
            return;
        if( _linkedAnnotationSpans.size() < 1 ) 
            return;        
        
        // get primary span of the source annotation
        int source_start, source_end;
        SpanDef span_source = _sourceAnnotationSpans.getSpanAt(0);
        if ( span_source == null )
            return;
        source_start = span_source.start;
        source_end = span_source.end;
        
        // get primary span of the linked annotation
        int linked_start, linked_end;
        SpanDef span_linked = _linkedAnnotationSpans.getSpanAt(0);
        if ( span_linked == null )
            return;
        linked_start = span_linked.start;
        linked_end = span_linked.end;
        
        
        // make sure we have a non-null instance of ComplexRelationshipCoordinates
        if ((coordinates==null)||(!(coordinates instanceof ComplexRelationshipCoordinates )))
            coordinates = new ComplexRelationshipCoordinates();
        
        // set the first path of the     
        coordinates.setCoordinates(
                _sourceAnnotationSpans, 
                _sourcecolor, 
                _linkedAnnotationSpans, 
                _linkedcolor
                );
    }
    
    public void clearAttRels(){
        this.annotations_hasAtt.clear();
        this.annotations_hasRel.clear();
    }
    
    public void addRelAnnotation(Annotation annotation){
        if( annotation == null )
            return;
        if( annotation.relationships == null )
            return;
        if( annotation.relationships.size() < 1)
            return;
        
        this.annotations_hasRel.add(annotation);
    }
    
    public void addAttAnnotation(Annotation annotation){
        if( annotation == null )
            return;
        if( annotation.attributes == null )
            return;
        if( annotation.attributes.size() < 1)
            return;
        
        this.annotations_hasAtt.add(annotation);
    }

    public void setComplexRelationsCoordinates_extraEnds(
            SpanSetDef _Spans_of_linkedAnnotation, 
            Color color         // class color of the linked annotation
            ){
        
        // validity checking
        if ( _Spans_of_linkedAnnotation == null )
            return;
        if ( _Spans_of_linkedAnnotation.size() < 1 )
            return;
        
        // make sure the "coordinates" are not null
        if ((coordinates==null)||(!(coordinates instanceof ComplexRelationshipCoordinates )))
            return;
        
        // get primary span of the linked annotation
        int linked_start, linked_end;
        SpanDef span_linked = _Spans_of_linkedAnnotation.getSpanAt(0);
        if ( span_linked == null )
            return;
        linked_start = span_linked.start;
        linked_end = span_linked.end;
        
        coordinates.addExtraEndPoint( _Spans_of_linkedAnnotation , color);
    }


    /**list of annotations who have attributes. */
    protected ArrayList<Annotation> annotations_hasAtt = new ArrayList<Annotation>();
    
    /**list of annotations who have relationships. */
    protected ArrayList<Annotation> annotations_hasRel = new ArrayList<Annotation>();
    
    
}

/**
 * Class used to store coordinates of annotations who have complex 
 * relationships
 */
class ComplexRelationshipCoordinates{
    /** original annotation coordinates in current document, only one*/
    public Coordinate __coordinate_of_StartAnnotation = null;        
    
    /** destination annotations coordinates, one or more */
    public Vector<Coordinate> EndAnnotations = new Vector<Coordinate>();


    /**
     * Startpoint annotations Span: [x1, y1]
     */
    public void setCoordinates( 
            SpanSetDef _spanSet_of_StartAnnotation, 
            Color color1, 
            SpanSetDef _spanSet_of_LinkedAnnotation, 
            Color color2 ){
        
        // validity check to the spanset of the source annotation
        if( _spanSet_of_StartAnnotation == null )
            return;
        if( _spanSet_of_StartAnnotation.isEmpty() )
            return;
        
        SpanDef firstspan = _spanSet_of_StartAnnotation.getSpanAt(0);
        if( firstspan == null )
            return;
        
        
        if((firstspan.start<0)||(firstspan.start>=firstspan.end)||(firstspan.end<0)) {
            clear();
            return;
        }
        
        // record the spanset of the source annotation
        __coordinate_of_StartAnnotation = new Coordinate(_spanSet_of_StartAnnotation, color1);
                
        
        // validity check to the spanset of the linked annotation
        if( _spanSet_of_LinkedAnnotation == null )
            return;
        if( _spanSet_of_LinkedAnnotation.isEmpty() )
            return;
        
        
        // coordinates of annotation on start point        
        EndAnnotations.clear();
        EndAnnotations.add( new Coordinate( _spanSet_of_LinkedAnnotation, color2) );
    }

    public void addExtraEndPoint(SpanSetDef _spanSet_of_LinkedAnnotation, Color color){
        if(_spanSet_of_LinkedAnnotation == null)
            return;
        if( _spanSet_of_LinkedAnnotation.isEmpty() )
            return;

        if (__coordinate_of_StartAnnotation == null)
            return;

        EndAnnotations.add(new Coordinate(_spanSet_of_LinkedAnnotation, color));
        
    }

    public void clear(){
        __coordinate_of_StartAnnotation = null;
        EndAnnotations.clear();
    }

}

class Coordinate{
    SpanSetDef spanset = null;
    Color color;

    public Coordinate(SpanSetDef spanset, Color color){
        this.spanset = spanset;
        this.color = color;
    }

    public Coordinate(){
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.positionIndicator;

import resultEditor.annotations.Depot;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JLabel;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.SpanDef;
import userInterface.GUI;

/**
 *
 * @author Chris 2010-07-20 Jianwei Leng
 */
public class JPositionIndicator extends JLabel {
    /**position data*/
    private Vector<Position> positions = new Vector<Position>();
    /**length of current document*/
    private int documentlength;

    // predesignated canvas parameters
    private final int headblankHeight = 20;
    private final int tailblankHeight = 20;
    private final int indicatorHeight = 3;

    // indicator painter paraters:
    protected int canvasHeight, canvasWidth;
    protected int canvasTOP, canvasLEFT, canvasRIGHT, canvaBottom;
    protected int maxIndicatorAmount;

    private String annotationname = null;
    

    public JPositionIndicator(){
        super();
        super.setText( null );
    }

    @Override
    public void paint( Graphics g ){
        // paint frame
        super.paint( g );
        // paint indicators
        paintIndicators( g );
    }

    public void forcepaint(){
        Graphics g = this.getGraphics();
        this.paint( g );
    }



    /**indicator painter*/
    private void paintIndicators( Graphics g ){        
        if ( positions == null )
            return;

        try{
            // get canvas information
            preCalulation();

            // paint each indicator on the canvas
            for( Position position : positions ){
                if ( position == null )
                    continue;
                if ( position.end > this.documentlength )
                    continue;

                drawIndicator( g, position.start, position.end, position.color, this.documentlength );

            }
        }catch(Exception ex){
            System.out.println("ERROR 1203011641");
        }
    }

    public void paintArticle(String articlename){
        if ( ( articlename == null )||(articlename.trim().length() < 1)) {
            this.forcepaint();
            return;
        }

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if ( depot == null ){
            this.forcepaint();
            return;
        }
            

        resultEditor.annotations.Article article = null; 
        if( GUI.reviewmode == GUI.ReviewMode.adjudicationMode )
            article = adjudication.data.AdjudicationDepot.getArticleByFilename(articlename.trim());
        else
            article = Depot.getArticleByFilename(articlename.trim());
        
        if (( article == null )||( article.annotations == null)) {
            this.forcepaint();
            return;
        }
        for( resultEditor.annotations.Annotation annotation : article.annotations ){
            
            if ( annotation == null )
                continue;
            if (( annotation.annotationclass == null)||( annotation.annotationclass.trim().length() < 1 ))
                continue;
            if ( annotation.visible == false )
                continue;

            if(GUI.reviewmode == GUI.ReviewMode.adjudicationMode){
                if((annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK)
                &&(annotation.adjudicationStatus == Annotation.AdjudicationStatus.NON_MATCHES)){
                }
                        
            }

            if ( this.annotationname != null ){
                if( this.annotationname.trim().compareTo( annotation.annotationText.trim() ) != 0 ) {
                    //String classname1 = annotation.annotationclass.trim();
                    //Color color = ResultEditor.AnnotationClasses.Depot.getColor( classname1 );
                    //color = ( color == null ? Color.black : color);
                    // System.out.println("added indicator on " + annotation.spanstart +","+annotation.spanend);

                    //addIndicator( annotation.spanstart, annotation.spanend, color );
                    continue;
                }
            }

            String classname = annotation.annotationclass.trim();
            Color color = resultEditor.annotationClasses.Depot.getColor( classname  );
            color = ( color == null ? Color.black : color);
            // System.out.println("added indicator on " + annotation.spanstart +","+annotation.spanend);

            if(annotation.spanset!=null){
                int size = annotation.spanset.size();
                for(int t=0;t<size;t++){
                    SpanDef span = annotation.spanset.getSpanAt(t);
                    if(span==null)
                        continue;

                    addIndicator( span.start, span.end, color );
                }
            }
            
        }
    }

    private void drawIndicator( Graphics g, int start, int end, Color color, int doclength ){
        g.setColor(color);
        //System.out.println("max indicator amount =" + this.maxIndicatorAmount + " and start = " + start + " and length = " + doclength);
        int startpoint = (int)((this.maxIndicatorAmount *  start) / doclength);
        //System.out.println("Startpoint =" + startpoint);
        startpoint = ( (startpoint < 1)?  1: startpoint );
        int barTOP  = startpoint*this.indicatorHeight + this.headblankHeight;
        int barLEFT = 0;
        int barWidth = this.canvasWidth;
        //System.out.println("Startpoint/length = " + start +","+ doclength );
        int thin = (int)(( end - start )* this.maxIndicatorAmount*this.indicatorHeight/doclength);
        thin = ( thin < this.indicatorHeight ? this.indicatorHeight :thin);

        //System.out.println( "X=" + barLEFT +", y=" + barTOP + ", width= "+barWidth+", height=" + thin);
        
        g.fillRect( barLEFT, barTOP, barWidth, thin );
        g.setColor(new Color(204,204,204));
        g.drawLine( barLEFT, barTOP, barLEFT, barTOP+thin );
        g.setColor(this.getBackground());
        //g.drawLine(end, end, end, end);

        
    }

    /** get canvas information/parameters before starting painting.*/
    private void preCalulation(){
        int height = this.getHeight();
        int weight = this.getWidth();

        this.canvasHeight = height - this.headblankHeight - this.tailblankHeight;
        this.canvasWidth = weight;

        this.canvasTOP = headblankHeight + 0;
        this.canvasLEFT = 0;
        this.canvasRIGHT = weight - 1;
        this.canvaBottom = height - this.tailblankHeight;

        this.maxIndicatorAmount = (int)(this.canvasHeight / this.indicatorHeight - 1);

    }

    /**remove all indicatior data*/
    public void removeAllIndicators(){
        positions.clear();
    }

    /**add a new indicator*/
    public void addIndicator( int start, int end,  Color color ){
        if ( ( end > start )&&( start > 0 )) {
            if ( color == null )
                color = Color.black;
             positions.add( new Position( start, end, color ) );
        }
    }

    /**set the length of current docuement.*/
    public void setDocLength(int length){
        this.documentlength = length;
        //System.out.println("doc length = ------------ " + length );
    }

    public void setDesignatedAnnotation( String annotation ){
        if (( annotation == null )||(annotation.trim().length() < 1))
            this.annotationname = null;
        else this.annotationname = annotation.trim();
    }

    public void removeDesignatedAnnotation( ){
        this.annotationname = null;
    }
}



package adjudication.statusBar;

import commons.Tools;
import javax.swing.JLabel;
import resultEditor.Differences.IAADifference.ArticleDifference;
import resultEditor.Differences.IAADifference.Difference;
import resultEditor.Differences.IAADifference.Differences;

/**
 *
 * @author Chris Leng
 */
public class DiffCounter implements Comparable{
    private static JLabel label;
    private String filename;
    private static int count = -1;
    
    private userInterface.GUI gui;
    
    /**Keep tracking which difference is in processing, -1 means none difference is in processing. */
    private static int pointer = -1;
    
    public static int getPointer(){
        return pointer;
    }
    
    public static void accept(){
        
        count = count -1;
        
        if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
        
        pointer = pointer - 1;
        
        /*
        
            
        if( label != null ){
            label.setText( "<html><STRIKE><font color=gray> " + (pointer + 2) + " </font></STRIKE> of " + (count-1) + " )</html>" );
        }*/
        // System.out.println("point-1=" + pointer);
    }
    
    public DiffCounter(JLabel jlabel, String filename, userInterface.GUI gui){
        if(jlabel!=null) 
            this.label = jlabel;
        this.filename = filename;
        this.gui = gui;
    }

    public void reset() {
        
        try{
            ArticleDifference ad = Differences.getArticle(filename);
            if( ad == null )
                count = 0;
            else if( ad.differences != null ){
                ad.sort();
                count = ad.differences.size();                
                
            } else
                count = 0;
            
            if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
            pointer = -1;
            
        }catch(Exception ex){
        }
    }
    
    public void setSelected(int currentmouse) {
        try{
            ArticleDifference ad = Differences.getArticle(filename);
            if(ad==null)
                return;
            
            int count = ad.differences.size();
            ad.sort();

            if (currentmouse < 0) {
                if( count <= 0 )
                    label.setText( " ALL SOLVED " );
                else
                    label.setText( " " + count + " UNSOLVED" );
                return;
            }else{
                for( int i=0;i<count;i++ ){
                     Difference diff = ad.differences.get( i );
                    int start = diff.differenceStart, end = diff.differenceEnd;
                    if(( currentmouse >= start )&&(currentmouse<=end)){
                        int c = i + 1;
                        pointer = i;
                        if( count <= 0 )
                            label.setText( " ALL SOLVED " );
                        else
                            label.setText( " " + count + " UNSOLVED" );
                        return;
                    }
                }

            }
            
            if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public void goNext(){
        try {
            
            ArticleDifference ad = Differences.getArticle(filename);            
            count = ad.differences.size();
            ad.sort();
            
            if (pointer <= -1) {
                beep();
                pointer = 0;
            } else if (pointer >= count - 1) {
                beep();
                pointer = count - 1;
            } else {
                pointer = pointer + 1;
            }
            
            System.out.println("point = " + pointer);
            
            int c = pointer+1;
            if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
            
            // get the difference
            Difference diff = ad.differences.get( pointer );
            int start = diff.differenceStart, end = diff.differenceEnd;
            
            gui.diffjumper( start, end);
            
        } catch (Exception ex) {
        }
        
    }
    
    public void goPrevious(){
        try {
            
            // set the count
            ArticleDifference ad = Differences.getArticle(filename);
            count = ad.differences.size();
            ad.sort();
            
            if (pointer <= 0 ) {
                beep();
                pointer = 0;
            } else if (pointer == 0) {
                pointer = 0;
            } else  if (pointer > count - 1){
                beep();
                pointer = count - 1;
            } else {
                pointer = pointer-1;
            }
            
            int c = pointer+1;
            if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
            
            // get the difference
            Difference diff = ad.differences.get( pointer );
            int start = diff.differenceStart, end = diff.differenceEnd;
            
            gui.diffjumper( start, end);
            
        } catch (Exception ex) {
        }
    }
    
    private void beep(){
        Tools.beep();
    }

    public void accepted() {
        try {
            
            // set the count
            //ArticleDifference ad = Differences.getArticle(filename);
            //int thiscount = ad.differences.size();
            
            //if( count == thiscount )
            //    return;
            
            //if(thiscount==0){
            //    pointer = -1;
                
                // gui.diffjumper( -1, -1 );
                
            //    label.setText( " -- of 0 " );
            //    return;
            //}else{ 
            //    pointer = pointer + 1;
            //    if(( pointer < 0 ) || (pointer>thiscount-1))
            //    pointer = thiscount - 1;
            //}
            
                       
            
            //int c = pointer+1;
            //label.setText( " "+ c + " of " + thiscount + " " );
            
            // get the difference
            //Difference diff = ad.differences.get( pointer );
            //int start = diff.differenceStart, end = diff.differenceEnd;
            
            // gui.diffjumper( start, end);
            
        } catch (Exception ex) {
        }
    }

    @Override
    public int compareTo(Object t) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /** UNFINISHED FUNCTION */
    public void acceptALL() {
        try {
            
            // set the count
            ArticleDifference ad = Differences.getArticle(filename);
            int thiscount = ad.differences.size();
            
            if( count == thiscount )
                return;
            
            if(thiscount==0){
                pointer = -1;
                
                // gui.diffjumper( -1, -1 );
                
                if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
                return;
            }else{ 
                pointer = pointer + 1;
                if(( pointer < 0 ) || (pointer>thiscount-1))
                pointer = thiscount - 1;
            }
            
                       
            
            int c = pointer+1;
            if( count <= 0 )
                label.setText( " ALL SOLVED " );
            else
                label.setText( " " + count + " UNSOLVED" );
            
            // get the difference
            Difference diff = ad.differences.get( pointer );
            int start = diff.differenceStart, end = diff.differenceEnd;
            
            // gui.diffjumper( start, end);
            
        } catch (Exception ex) {
        }
    }


}

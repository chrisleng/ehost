/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.conflicts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author leng
 */
public class ConflictButtonPainter {
    protected JTextPane comp;
    protected static int[] conflictCursorPostions;

    public ConflictButtonPainter(JTextPane comp){
        this.comp = comp;
    }

    public void addButton(int _cursorPosition){
        
        // validity check to cursor position
        int doclength = comp.getDocument().getLength();
        if( _cursorPosition > doclength )
            return;
        
    }

    /**Get length of docment in the text panel.*/
    private int getDocLength(){
        return comp.getDocument().getLength();
    }

    /**@param  flag=0   request from pure textpane or annotations without
     * offset.<p>
     * @param  flag=1   request from pure textpane with offset.<p>
     */
    public static int getScreenOffset(int _cursorPosition, int _flag){
        if(conflictCursorPostions ==null)
            return 0;
        if(conflictCursorPostions.length < 1)
            return 0;

        // get a local array which sorted from low to high
        int size = conflictCursorPostions.length;
        int[] lowToHigh = new int[conflictCursorPostions.length];
        for(int i=0;i<size;i++){
            lowToHigh[i] = conflictCursorPostions[i];
        }

        // sort
        for(int i=0;i<size;i++){            
            for(int j=0;j<size;j++){
                if (lowToHigh[i]<lowToHigh[j]){
                    int c =lowToHigh[i];
                    lowToHigh[i] = lowToHigh[j];
                    lowToHigh[j] = c;
                }
            }
        }

        // get offset
        int offset = 0;
        for(int i=0;i<size;i++){
            if ((_flag==1)&&( _cursorPosition >= lowToHigh[i] + i )){
                offset++;
            } else if ((_flag==0)&&( _cursorPosition >= lowToHigh[i] )){
                offset++;
            }
        }
        return offset;
    }

    /**Show button after each protentional conflict word.*/
    public void showConflicts(File _file){
        String filename = _file.getName();

        // inital integer array
        // record cursor positions for all protential conflict words
        conflictCursorPostions = getConflictCursorPostions( filename );

        // resort the array
        conflictCursorPostions = resort_HighToLow(conflictCursorPostions);

        int size = conflictCursorPostions.length;

        // add buttons
        for(int i=0;i<size;i++){
            //System.out.println("conflictCursorPostions[" + i
            //        +"] = "+ conflictCursorPostions[i]);
            
            if ( conflictCursorPostions[i] > 0 )
                addbutton( conflictCursorPostions[i],  conflictCursorPostions[i] );
        }




    }



    /**Resor a integer array from low to high */
    private int[] resort_HighToLow(int[] _intarray){
        int length = _intarray.length;
        for(int i=0;i<length;i++){
            for(int j=0;j<length;j++){
                // switch if a>b
                if (_intarray[i]>_intarray[j]){
                    int c =_intarray[i];
                    _intarray[i] = _intarray[j];
                    _intarray[j] = c;
                }
            }
        }
        return _intarray;
    }

    /**Set conflict cursor position before draw buttons.*/
    private int[] getConflictCursorPostions( String _filename ){

        // start to find all conflicts to this text doc
        resultEditor.annotations.findConflicts.findConflict ConflictFinder =
                new resultEditor.annotations.findConflicts.findConflict(_filename);

        // get all protential conficts to this text file
        Vector<resultEditor.annotations.findConflicts.Range> allconflicts
                = ConflictFinder.get();

        // size 
        int size = allconflicts.size();

        // inital integer array
        int[] cursorPostions = new int[size];

        // max cursor position
        int maxCursorPosition = getDocLength() - 1;

        for(int i=0;i<size;i++){
            cursorPostions[i] 
                    = ( allconflicts.get(i).right <= maxCursorPosition ?
                        allconflicts.get(i).right: -1 );
        }

        return cursorPostions;
        
    }

    private void addbutton(int _cursorPosition, int _conflictSpanEnd){
        /*

            // add this string to end of logs on the Text Pane
            //;
            // Create and add the main document style
            StyleContext sc = new StyleContext();
            Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);

            final Style set2 = sc.addStyle("set2", defaultStyle);
            StyleConstants.setForeground(set2, Color.red);
            


                //StyleConstants.setForeground(set1, Color.black);//set color of font
                StyleConstants.setAlignment(set2, StyleConstants.ALIGN_CENTER);
*/
        

        Icon icon = userInterface.GUI.res_conflictIcon.getIcon();
        JButton button = new JButton();
        button.setIcon(icon);
        //button.setText("CONFLICT");
        button.setFont(null);
        button.setMargin(new Insets(0,0,0,0));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                Toolkit.getDefaultToolkit().beep();
            }
        });
        // force button size
        Dimension d = new Dimension();
        d.height = 23; d.width = 30;
        button.setMinimumSize(d);
        button.setMaximumSize(d);
        button.setPreferredSize(d);
        //comp.setCaretPosition(_cursorPosition);
        comp.insertComponent(button);
        

        DefaultStyledDocument doc = (DefaultStyledDocument)comp.getStyledDocument();
        //MutableAttributeSet set = new SimpleAttributeSet();
        StyleContext sc = new StyleContext();
        Style conflictbutton = sc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setSpaceBelow(conflictbutton, 0.0f);
        StyleConstants.setLineSpacing(conflictbutton, 0.0f);
        StyleConstants.setForeground(conflictbutton, Color.red);
        StyleConstants.setBackground(conflictbutton, Color.black);
        //StyleConstants.setAlignment(conflictbutton, StyleConstants.ALIGN_CENTER);
        doc.setLogicalStyle(_cursorPosition, conflictbutton);
        
    }
}

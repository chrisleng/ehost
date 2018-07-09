/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.positionIndicator;

import java.awt.Color;

/**
 *
 * @author Chris
 */
public class Position{
    int start;
    int end;
    Color color;

    public Position(){
    }

    public Position( int start, int end, Color color ){
        this.start = start;
        this.end = end;
        this.color = color;
    }
}

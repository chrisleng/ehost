/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.quickNLP;

/**
 *
 * @author Chris
 */
public class Interval {
    public int start;
    public int end;

    public Interval( int start, int end ){
        if ((start >=0)&&(end>=start)){
            this.start = start;
            this.end = end;
        }else
            return;
    }
}

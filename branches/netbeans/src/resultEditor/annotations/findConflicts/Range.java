/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations.findConflicts;

/**
 *
 * @author leng
 */
/**use to delivery catched annotation in a specific range*/
public class Range{
    public int left = -1; //default value: -1 means invalid
    public int right = -1;//default value: -1 means invalid

    // constructor
    public Range(int _left, int _right){
        left = _left;
        right = _right;
    }
    // constructor
    public Range(){
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmNegex;
/**
 *
 * @author VHASLCShens
 */
public class negTable {

    String[] word; // negation phrase
    int len; // length of list
    int[] type; // negation type (100=pseudo, 101=pre, 102=poss pre,

    // 103=post, 104=poss post, 105=conj, 200=special VA)
    negTable() {
        word = new String[20000];
        len = 0;
        type = new int[20000];
    }
}
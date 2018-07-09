/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thirdpartyOutput;

import java.util.Vector;

/**
 *
 * @author leng
 */
public class Annotations {
    public String annotationText = null;
    public String classname = null;
    public int locationStart = -1;
    public int locationEnd = -1;
    public int index = -1;
    public Vector<String> hasSlotMention = new Vector();
    public Vector<Slots> slots = new Vector<Slots>();
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.loadingNotes;

import java.awt.Color;

/**
 *
 * @author Chris
 */
public class ColoredFormat {
        // text of current paragraph
        String paragraphText;
        // which file does this paragraph belong to?
        String filename;
        // color of each character which will show in the text pane
        // length equal to size of paragraphText
        Color[] marks;
}

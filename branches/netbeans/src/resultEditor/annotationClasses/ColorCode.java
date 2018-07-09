/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import java.awt.Color;

/**
 *
 * @author Chris 5-18-2010 3:51 pm Tuesday Williams Building
 * @History 2010-07-07  rewrote class to let user designate color to class
 */
class ColorCode {
    String comments;
    Color  color;

    /**Constructor*/
    public ColorCode(){
    }
    /**Constructor*/
    public ColorCode(String comments, Color color){
        this.comments = comments;
        this.color = color;
    }
}

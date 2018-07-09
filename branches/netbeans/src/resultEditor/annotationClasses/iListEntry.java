
package resultEditor.annotationClasses;

import java.awt.Color;
import javax.swing.Icon;

/**New Class to bulid one kind of entry to show icon and text informations on
 * list.
 *
 * @author Jianwei Leng  2010-07-8
 */
public class iListEntry {
  private final String classname;
  private final Color color;
  private final Icon icon;

  public iListEntry(String title, Color color, Icon icon) {
    this.classname = title;
    this.icon = icon;
    this.color = color;

  }

  public String getTitle() {
    return classname;
  }

  public Icon getImage() {    
    return icon;
  }

  public Color getColor(){
    return color;
  }

  // Override standard toString method to give a useful result
  public String toString() {
    return classname;
  }
}
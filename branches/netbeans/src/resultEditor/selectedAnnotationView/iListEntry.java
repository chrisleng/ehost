
package resultEditor.selectedAnnotationView;

import java.awt.Color;
import javax.swing.Icon;
import resultEditor.annotations.Annotation;
//import javax.swing.Icon;

/**New Class to bulid one kind of entry to show icon and text informations on
 * list.
 *
 * @author Jianwei Leng  2010-07-8
 */
public class iListEntry {
  private final Annotation annotation;
  private final Color color;
  private final Icon icon;

  public iListEntry(Annotation annotation, Color color, Icon icon) {
    this.annotation = annotation;
    this.icon = icon;
    this.color = color;

  }

  public String getTitle() {
      String str = "<html>" +annotation.annotationText + "<br>-</html>";
      return str;
  }

  public Annotation getAnnotation(){
    return annotation;
  }

  public Icon getImage() {
    return icon;
  }

  public Color getColor(){
    return color;
  }

  // Override standard toString method to give a useful result
  public String toString() {
    return annotation.annotationText;
  }
}
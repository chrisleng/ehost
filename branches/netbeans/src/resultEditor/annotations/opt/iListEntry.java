
package resultEditor.annotations.opt;

import resultEditor.annotations.DupilcateAndGhost;

/**
 * New Class to bulid one kind of entry to show icon and text informations on
 * list of output annotations into xml files.
 *
 * @author Jianwei Leng  2010-06-29
 */
public class iListEntry {
  public DupilcateAndGhost duplicatesandghost;
  public boolean isCheckBoxSelected = true;

  public iListEntry(DupilcateAndGhost _duplicatesandghost) {
      this.duplicatesandghost = _duplicatesandghost;

  }

public String getTitle() {
    String str = "";
    if ( duplicatesandghost.type == 1 )
        str = "<html><b>Term: [<font color=blue><b>"
              + duplicatesandghost.referenceAnnotation.annotationText.trim()
              + "</b></font>], span( "
              + duplicatesandghost.referenceAnnotation.spanstart
              + ", "
              + duplicatesandghost.referenceAnnotation.spanend
              + ")<p> "
              + "<b><font color=red>Duplicates</font>: (<font color=blue>"
              + duplicatesandghost.duplicates.size()
              + "</font>)</b>"
              + "</html>";
              /*
              + "  (" + term.start + ", " + term.end + ")"
              + "<br><font color=blue>FORM</font> "
              + term.surroundtext.substring( 0, term.start_in_surroundtext )
              + "<font color=red>" + term.termtext + "</font>"
              + term.surroundtext.substring( term.end_in_surroundtext, term.surroundtext.length() )
              + "</html>";*/
    // classless
    else if ( duplicatesandghost.type == 4 )
        str = "<html><b>Term: [<font color=blue><b>"
              + duplicatesandghost.referenceAnnotation.annotationText.trim()
              + "</b></font>], span( "
              + duplicatesandghost.referenceAnnotation.spanstart
              + ", "
              + duplicatesandghost.referenceAnnotation.spanend
              + ")<p> "
              + "<b><font color=red>CLASSLESS</font></b>"
              + "</html>";

    // ghost annotation: spanless
    else if ( duplicatesandghost.type == 5 )
        str = "<html><b><font color=red>Spanless Annotation</font></b> with<p>"
              + "unique id = \""
              + duplicatesandghost.uniqueindex
              + "\"</html>";

    // ghost annotation: out of range
    else if ( duplicatesandghost.type == 6 )
        str = "<html><b>Term: [<font color=blue><b>"
              + duplicatesandghost.referenceAnnotation.annotationText.trim()
              + "</b></font>], <font color=red>span( "
              + duplicatesandghost.referenceAnnotation.spanstart
              + ", "
              + duplicatesandghost.referenceAnnotation.spanend
              + ")</font><p> "
              + "<b><font color=red>OUT OF RANGE</font></b>"
              + "</html>";


    return str;

}

  


    public void setCheckBoxStatus(boolean isCheckBoxSelected ){
        this.isCheckBoxSelected = isCheckBoxSelected;
    }

  // Override standard toString method to give a useful result
  public String toString() {
      return "file:["+duplicatesandghost.filename + "]; annotation index: " + duplicatesandghost.uniqueindex;
  }
}
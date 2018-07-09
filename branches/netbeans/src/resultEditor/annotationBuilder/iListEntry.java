
package resultEditor.annotationBuilder;

/**
 * New Class to bulid one kind of entry to show icon and text informations on
 * list of output annotations into xml files.
 *
 * @author Jianwei Leng  2010-06-29
 */
public class iListEntry {
  public Term term;
  public boolean isCheckBoxSelected = true;

  public iListEntry(Term term) {
      this.term = term;

  }

  public String getTitle() {
      String str = "<html>"
              + term.termtext
              + "  (" + term.start + ", " + term.end + ")"
              + "<br><font color=blue>FORM</font> "
              + term.surroundtext.substring( 0, term.start_in_surroundtext )
              + "<font color=red>" + term.termtext + "</font>"
              + term.surroundtext.substring( term.end_in_surroundtext, term.surroundtext.length() )
              + "</html>";
      return str;
  }

  


    public void setCheckBoxStatus(boolean isCheckBoxSelected ){
        this.isCheckBoxSelected = isCheckBoxSelected;
    }

  // Override standard toString method to give a useful result
  public String toString() {
      return term.termtext;
  }
}
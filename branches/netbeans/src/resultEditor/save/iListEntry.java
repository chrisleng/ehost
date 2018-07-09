
package resultEditor.save;

import javax.swing.Icon;

/**
 * New Class to bulid one kind of entry to show icon and text informations on
 * list of output annotations into xml files.
 *
 * @author Jianwei Leng  2010-06-29
 */
public class iListEntry {
  private String title;
  public String filename;
  public boolean isCheckBoxSelected = true;
  protected Icon notdone_icon, done_icon;

  /**0: unsave, 1: skipped: 2 saved*/
  public int status;

  public iListEntry(String filename, int annotationamount, Icon notdone, Icon done, int status) {
      this.filename = filename;
    this.title = "<html><b>" + filename;
    if ( status == 1 )
        this.title = this.title + " (Skipped) ";
    if ( status == 2 )
        this.title = this.title + " (Saved) ";

    this.title = this.title + "</b><br>"
               + "contains " + annotationamount + "(s) annotations."
               + "</html>";
    this.done_icon = done;
    this.notdone_icon = notdone;

  }

  public String getTitle() {
      return title;
  }

  public Icon getImage_done() {
      return this.done_icon;
  }

  public Icon getImage_notdone() {
      return this.notdone_icon;
  }

    public String getFilename(){
        return this.filename;
    }


    public void setCheckBoxStatus(boolean isCheckBoxSelected ){
        this.isCheckBoxSelected = isCheckBoxSelected;
    }

  // Override standard toString method to give a useful result
  public String toString() {
      return title;
  }
}
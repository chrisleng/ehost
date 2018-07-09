/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

/**
 *
 * @author leng
 */
public class CheckableItem {
    private String annotatorname;

    private boolean isSelected;

    public CheckableItem( String str ) {
      this.annotatorname = str;
      isSelected = false;
    }

    public CheckableItem( String str, boolean selected ) {
      this.annotatorname = str;
      isSelected = selected;
    }

    public void setSelected(boolean b) {
      isSelected = b;
    }

    public String getAnnotatorName(){
        return annotatorname;
    }

    public boolean isSelected() {
      return isSelected;
    }

    @Override
    public String toString() {
      return annotatorname;
    }
  }


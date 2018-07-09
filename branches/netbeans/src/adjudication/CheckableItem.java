package adjudication;

/**
 * This is a list item that is used to let user select/unselect annotators 
 * and classnames for later process to enter the adjudication mode.
 * 
 * @author Jianwei "Chris" Leng
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


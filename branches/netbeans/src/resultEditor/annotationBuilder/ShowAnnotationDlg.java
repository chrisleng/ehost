/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationBuilder;

/**
 *
 * @author leng
 */
public class ShowAnnotationDlg {
    private  userInterface.GUI eHOST_GUI;
    userInterface.AnnotationBuilder na;
    public ShowAnnotationDlg( userInterface.GUI eHOST_GUI){
        this.eHOST_GUI = eHOST_GUI;
    }

    /**Show dialog of creating a new annotation*/
    public void showDialog(int eHOSTx, int eHOSTy, int eHOSTweight, int eHOSTheight,
            String spantext, int start, int end){
        na = new userInterface.AnnotationBuilder(eHOST_GUI);
        int weight = na.getWidth();
        int height = na.getHeight();
        int x = eHOSTx + (int)((eHOSTweight - weight)/2);
        int y = eHOSTy + (int)((eHOSTheight - height)/2);
        na.setLocation(x, y);
        na.setValueofSelectedText(spantext, start, end);
        na.setVisible(true);
    }

    public void getFocus() {
        na.requestFocus();
    }

    public boolean isGood() {
        return na.isVisible();
    }
}

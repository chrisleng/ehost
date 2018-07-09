
package userInterface.structure;

import javax.swing.Icon;

/**
 *
 * @author leng
 */
public class FileObj{

    private String filename;
    private Icon icon;
    

    public FileObj(){
    }

    public FileObj(String filename, Icon icon) {
        this.filename = filename;
        this.icon = icon;      
    }

    public String getFileName() {
        return filename;
    }


    public Icon getIcon() {
        return this.icon;
    }

    public void setFileName(String attributename) {
        this.filename = attributename;
    }


    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public boolean isSelected = false;
}


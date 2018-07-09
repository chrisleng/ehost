/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotate.outDated;

import java.io.File;
import java.util.ArrayList;

/**
 * This class is a listObject for a list of files to be converted.  It will be used to
 * keep track of the state of each file.  Sometimes for a conversion multiple file types
 * are required; this class will keep track of any files that are missing for a conversion,
 * and set the color of the text(with the toString method) according to this.  Red means
 * that this file is missing some required files for conversion.  Black means it is good
 * to go.
 * @author Kyle
 */
public class listObject {

    //The actual file object that is in the list
    private File file;

    //The color of the String that will be displayed using the toString method
    private String color = "black";

    //The required files that this file is missing.
    private ArrayList<String> missingFiles = new ArrayList<String>();

    //optional - used for pins files when multiple text files are required for a singe
    //file conversion
    ArrayList<String> containedFiles = new ArrayList<String>();

    /**
     * Cpnstructor
     * @param file - the file that this listObject must maintain.
     */
    public listObject(File file)
    {
        this.file = file;
    }
    /**
     * This method will return a String representation of this object.  HTML tagging
     * is used to change the text color.
     * @return - string representing this object.
     */
    @Override
    public String toString()
    {
        String toReturn = "<html><font color = \""+color.toString()+"\">" +file.getName() +"</font></html>";
        return toReturn;
    }
    /**
     *
     * @return - the file object that this listObject is managing
     */
    public File getFile()
    {
        return file;
    }
    /**
     * Set the font color of this object
     * @param color - the color to set it to... will cause problems if this string
     * is not a standard color.
     */
    public void setFontColor(String color)
    {
        this.color = color;
    }
    /**
     * Add a string that contains the name of a missing file.
     * @param string - the name of the file.
     */
    public void addMissingFiles(String string)
    {
        missingFiles.add(string);
    }
    /**
     * Clear out the list of missing files.  This should be done right before
     * missing files are recalculated.
     */
    public void clearMissingFiles()
    {
        missingFiles = new ArrayList<String>();
    }
    /**
     * Get the missing files.
     * @return - All of the missing files - one missing file for each ArrayList index.
     */
    public ArrayList<String> getMissingFiles()
    {
        return missingFiles;
    }
    /**
     * Set the files that are contained in this file--- currently only used for
     * .pins files.
     *
     * @param toSet - An ArrayList where each entry is a file that is contained in this object.
     */
    public void setContainedFiles(ArrayList<String> toSet)
    {
        containedFiles = toSet;
    }
    /**
     * Get the list of files that are necessary for this file.
     * @return
     */
    public ArrayList<String> getContainedFiles()
    {
        return containedFiles;
    }

}

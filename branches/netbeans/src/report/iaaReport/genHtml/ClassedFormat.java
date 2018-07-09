/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.util.Vector;

/**
 *
 * @author leng
 */
/**format of matched or unmatched details separated by class*/

public class ClassedFormat {

    String classname;
    String filename;
    Vector<String> htmlcodes = new Vector<String>();

    public ClassedFormat( String classname, String annotatorName, boolean isMatches )
    {
        this.classname = classname;
        String newclassname = classname.trim();
        newclassname = newclassname.replaceAll(" ", "_");
        newclassname = newclassname.replaceAll(",", "_");
        newclassname = newclassname.replaceAll("=", "_");
        newclassname = newclassname.replaceAll("&", "_");
        newclassname = newclassname.replaceAll("!", "_");
        newclassname = newclassname.replaceAll("\\*", "_");
        newclassname = newclassname.replaceAll("@", "_");
        newclassname = newclassname.replaceAll("\\+", "_");
        
        if(isMatches)
            filename = "Matched-by-class-" + newclassname + ".html";
        else
            filename = annotatorName + "-UNMATCHED-by-class-" + newclassname + ".html";
    }
}
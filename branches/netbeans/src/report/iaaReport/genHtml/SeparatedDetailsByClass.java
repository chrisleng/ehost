/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport.genHtml;

import java.util.Vector;

/**
 * Depot of all matched or unmatched details separated by class
 *
 * @author leng
 */
public class SeparatedDetailsByClass {

    /**the static memoery used to keep record of classes which has unmatches to
     * a specific annotator.*/
    public static Vector<RegisterUnmatched> UNMATCHED = new Vector<RegisterUnmatched>();
    
    /**the static memoery used to keep record of classes which has matches to
     * an annotator.*/
    public static Vector<String> MATCHED = new Vector<String>();

    public static void registerUnMatchesClass(String annotatorName){
        for( ClassedFormat cf : separatedDetails)
        {
            UNMATCHED.add( new RegisterUnmatched(annotatorName, cf.classname));
        }
    }

    static void registerMatchesClass() {
        for( ClassedFormat cf : separatedDetails)
        {
            MATCHED.add( cf.classname );
        }
    }

    protected static Vector<ClassedFormat> separatedDetails = new Vector<ClassedFormat>();

    /**the annotator name which has be filtered out all special characters. It
     * usually is used to build the html names.
     */
    public static String annotatorName;

    static void setAnnotatorName(String annotatorName) {
        SeparatedDetailsByClass.annotatorName = annotatorName;
    }

    static boolean isNoData_ToNonMatches(String Annotator, String classname) {
        for(RegisterUnmatched ru : UNMATCHED)
        {
            if( (ru.annotator.trim().compareTo(Annotator.trim())==0)
              &&(ru.classname.trim().compareTo(classname.trim())==0) )
                return false;
        }

        return true;
    }

    static boolean isNoData_ToMatches(String classname) {
        for(String thisclassname : MATCHED)
        {
            if(thisclassname.trim().compareTo(classname.trim())==0)
                return false;
        }

        return true;
    }



    public SeparatedDetailsByClass(String annotatorName){
        SeparatedDetailsByClass.annotatorName = annotatorName;
    }

    /**remove all stored information*/
    public static  void clear(){
        separatedDetails.clear();
    }

    /**add a new line of HTML codes*/
    public static void addHtmlLine(String classname, Vector<String> htmlCodes, boolean isMatches)
    {
        int index = getClass( classname, isMatches);
        for(String code:htmlCodes)
        {
            separatedDetails.get(index).htmlcodes.add(code);
        }
    }

    private static int getClass(String classname, boolean isMatches)
    {

        for( int i=0; i<separatedDetails.size(); i++ )
        {
            ClassedFormat cf = separatedDetails.get(i);
            if(cf==null)
                continue;

            if(cf.classname.trim().compareTo(classname.trim())==0)
                return i;
        }

        separatedDetails.add(new ClassedFormat(classname, annotatorName, isMatches));
        return (separatedDetails.size() - 1);
    }
}

class RegisterUnmatched{
    
    String annotator;
    String classname;

    public RegisterUnmatched( String annotator, String classname )
    {
        this.annotator = annotator;
        this.classname = classname;
    }
}
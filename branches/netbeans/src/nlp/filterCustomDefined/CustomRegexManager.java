/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.filterCustomDefined;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jianwei Leng
 * @since May 22, 2010
 */
public class CustomRegexManager {
    private static ArrayList<CustomRegexFormat> CUSTOMREGULAREXPRESSION = new ArrayList<CustomRegexFormat>();

    /** clean arraylist*/
    public static void clear(){
        CUSTOMREGULAREXPRESSION.clear();
    }

    /** add a new record*/
    public static void add(String _customRegularExpression, String _classname, String _comment){
        CustomRegexFormat cre = new CustomRegexFormat();
        cre.customRegularExpression = _customRegularExpression;
        cre.classname = _classname;
        cre.comment = _comment;

        CUSTOMREGULAREXPRESSION.add(cre);
    }
    public static void add(String _customRegularExpression, String _classname){
        CustomRegexFormat cre = new CustomRegexFormat();
        cre.customRegularExpression = _customRegularExpression;
        cre.classname = _classname;
        cre.comment = null;

        CUSTOMREGULAREXPRESSION.add(cre);
    }

    public static void modify(int _index, String _customRegularExpression, String _classname, String _comment){
        CustomRegexFormat cre = new CustomRegexFormat();
        cre.customRegularExpression = _customRegularExpression;
        cre.classname = _classname;
        cre.comment = _comment;

        CUSTOMREGULAREXPRESSION.set(_index,cre);
    }

    public static int size(){
        return CUSTOMREGULAREXPRESSION.size();
    }

    public static CustomRegexFormat get(int _index){
        if (( _index > (size() -  1) )||(_index < 0))
            return null;
        else
            return CUSTOMREGULAREXPRESSION.get(_index);
    }

    /**check existing of a regular expression in the custom regular expression lib*/
    public static boolean existing(String _regularExpression){
        int size = size();
        
        if ( size < 1)
            return false;

        for(int i=0;i<size;i++){
            String regularExpression = CUSTOMREGULAREXPRESSION.get(i).customRegularExpression;
        
            if( _regularExpression.trim().compareTo(regularExpression.trim()) == 0)
                return true;
        }
        return false;
    }
    public static boolean existing(String _regularExpression, int _designatedIndex){
        int size = size();

        if ( size < 1)
            return false;

        //if ((_designatedIndex < 0)||(_designatedIndex > size - 1))
        //    return false;

        for(int i=0;i<size;i++){
            if (i==_designatedIndex)
                continue;
            String regularExpression = CUSTOMREGULAREXPRESSION.get(i).customRegularExpression;

            if( _regularExpression.trim().compareTo(regularExpression.trim()) == 0)
                return true;
        }
        return false;
    }

    public static Vector buildVectorForDisplay(){
        Vector<String> res = new Vector<String>();
        String comment;
        int size = size();
        for(int i=0;i<size;i++){
            String regularExpression = CUSTOMREGULAREXPRESSION.get(i).customRegularExpression.trim();
            String classname = CUSTOMREGULAREXPRESSION.get(i).classname.trim();
            if(CUSTOMREGULAREXPRESSION.get(i).comment == null)
                comment = "NULL";
            else
                comment = CUSTOMREGULAREXPRESSION.get(i).comment.toString();

            res.add(regularExpression +" -- "+ classname + " -- "+ comment);
        }

        return res;
    }

    /**delete a entry by index of the arraylist*/
    public static void delete(int _index){
        if (( _index > (size() -  1) )||(_index < 0))
            return;
        else
            CUSTOMREGULAREXPRESSION.remove(_index);
    }

    /** load from all saved from custom regular expression lib*/
    public static int loadFromLib(){
        int count = 0;
        String filename  = "customre.lib";
        try{
            BufferedReader lib
                = new BufferedReader( new FileReader( filename ));

            String st = lib.readLine();

            while (st != null) {
                String[] str = st.split("\t");

                if(str.length == 3){
                    String regularExpression = str[0];
                    String classname = str[1];
                    String comment = str[2];
                    if( regularExpression.trim().length() < 1){
                        st = lib.readLine();
                        continue;
                    }
                    if( classname.trim().length() < 1){
                        st = lib.readLine();
                        continue;
                    }
                    if( comment.trim().length() < 1){
                        st = lib.readLine();
                        continue;
                    }
                    if ( comment.toLowerCase().trim().compareTo("null") == 0 ){
                        add(regularExpression, classname);
                        count++;
                    }else{
                        add(regularExpression.trim(), classname.trim(), comment.trim() );
                        count++;
                    }
                }
                
                st = lib.readLine();
            }

            lib.close();

        }catch(Exception e){
            log.LoggingToFile.log( Level.SEVERE, "CustomRegularExpression.java a1 - fail to load custom regular expression from " + filename + "!!!" );
            logs.ShowLogs.printErrorLog("CustomRegularExpression.java a1 - fail to load custom regular expression from " + filename + "!!!");
            log.LoggingToFile.log( Level.SEVERE, "CustomRegularExpression.java a1 " + e.toString() );
        }
        return count;
    }

    public static String verifyARegularExpression(String _regularExpression){
        String errorInfo = null;

        String currsent = "I am a TESTer.";

        Matcher matcher;
        boolean match_found;
        try{
            matcher = Pattern.compile( _regularExpression, Pattern.CASE_INSENSITIVE ).matcher( currsent );
            match_found = matcher.find();
            while ( match_found )
            {
                int start = matcher.start();
                int end = matcher.end();
                match_found = matcher.find();
            }

        }catch(Exception e){
            return e.getMessage();
        }
        
        return errorInfo;
    }

    /** load from all saved from custom regular expression lib*/
    public static void saveTolib(){
        FileOutputStream out;
        PrintStream p;
        String filename  = "customre.lib";
        try {
            out = new FileOutputStream(filename);
            p = new PrintStream(out);

            int size = size();
            //System.out.println("----------------------> try to out put regular expression in custom lib");
            //System.out.println("----------------------> try to out put regular expression in custom lib: size=" + size);
            for(int i=0; i<size; i++){
                CustomRegexFormat cre = get(i);

                if(cre == null)
                    continue;
                if((cre.customRegularExpression == null)||(cre.customRegularExpression.trim().length() < 1))
                    continue;
                if((cre.classname == null)||(cre.classname.trim().length() < 1))
                    continue;
                String comment = cre.comment;
                if(comment==null){
                    comment = "null";
                }else
                    comment = cre.comment.trim();

                p.println( cre.customRegularExpression.trim()
                        +"\t"
                        + cre.classname.trim()
                        + "\t"
                        + comment );
                //System.out.println("----------------------> add new regular expression in custom lib");
                /*System.out.println( cre.customRegularExpression.trim()
                        +"\t"
                        + cre.classname.trim()
                        + "\t"
                        + comment );
                 */
            }

            p.close();
            out.close();

        } catch (Exception e) {
            log.LoggingToFile.log( Level.SEVERE, "CustomRegularExpression.java a02 - fail to write modification to custom regular expression lib - " + filename);
            logs.ShowLogs.printErrorLog("CustomRegularExpression.java a02 - fail to write modification to custom regular expression lib - " + filename);
            log.LoggingToFile.log( Level.SEVERE, "CustomRegularExpression.java a02 " + e.toString() );
            logs.ShowLogs.printErrorLog("CustomRegularExpression.java a02 " + e.toString() );
        }

    }
}

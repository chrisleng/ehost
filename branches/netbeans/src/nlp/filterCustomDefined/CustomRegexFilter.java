/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.filterCustomDefined;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Chris
 */
public class CustomRegexFilter {


    public ArrayList<CustomRegexResultFormat> catchTerms(String _paragraph, int _lengthOfProcessedParagraph){
        // prepare the custom regular expression arraylist
        int size = CustomRegexManager.size();
        if(size <= 0)
            CustomRegexManager.loadFromLib();

        size = CustomRegexManager.size();
        if(size <= 0)
            return null;

        // begin processing - use these regular expression for term catching
        ArrayList<CustomRegexResultFormat> results = process(_paragraph, _lengthOfProcessedParagraph);
        return results;
    }

    private ArrayList<CustomRegexResultFormat> process(String _paragraph, int _lengthOfProcessedParagraph){
        
        ArrayList<CustomRegexResultFormat> results = new ArrayList<CustomRegexResultFormat>();

        int size = CustomRegexManager.size();
        try{
            // run all regular expression to catch terms
            for(int i=0;i<size;i++){

                CustomRegexFormat cre = CustomRegexManager.get(i);
                if (cre == null)
                    continue;

                String regex = cre.customRegularExpression;
                String classname = cre.classname;

                // check validity of regex and classname
                if (regex==null) continue;
                if (classname==null) continue;


                boolean found = false;
                boolean termFound = false;

                if (( _paragraph != null )&&( _paragraph.trim().length() > 0 )) {
                    Matcher matcher;
                    matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher( _paragraph );
                    found = matcher.find();

                    while (found) {
                        int begin = matcher.start() ;
                        int end = matcher.end();
                        String terms = _paragraph.substring(begin, end);

                        CustomRegexResultFormat thisone = new CustomRegexResultFormat();
                        thisone.termText = terms;
                        thisone.classname = classname;
                        thisone.start = begin + _lengthOfProcessedParagraph;
                        thisone.end = end + _lengthOfProcessedParagraph;

                        log.LoggingToFile.log( Level.SEVERE,
                                "Custom Regular Expression - "
                                + classname
                                + ", "
                                + terms + " ,located on [" + begin + ", " + end + "]" );
                        logs.ShowLogs.printResults( "Custom Regular Expression - " + classname,
                                terms + " ,located on [" + begin + ", " + end + "]");

                        results.add(thisone);

                        found = matcher.find();
                    }

                }

            }
        }catch(Exception e){

        }

                
        return results;
    }
}

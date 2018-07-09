/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.filterDate;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jianwei Chris Leng, 2009-12-28 VA Salt Lake City
 */
public class getDates {                                //"(?i:((Jan|jan|Jan)(?:uary)|(FEB|Feb|feb)(?:ruary)|(MAR|Mar|mar)(?:ch)|(Apr|APR|apr)(?:il)|(MAY|May|may)|june|JUNE|June|July|JULY|july|(aug|AUG|Aug)(?:ust)|(sep|SEP|Sep)(?:tember)|(SEPT|Sept|sept)(?:ember)|(Oct|OCT|oct)(?:ober)|(Nov|nov|NOV)(?:ember)|(DEC|Dec|dec)(?:ember)))";

        private static final String PAT_MONTH  =       "(?i:((Jan|jan|JAN)(?:uary)|(Feb|feb|FEB)(?:ruary)|(Mar|mar|MAR)(?:ch)|(Apr|apr|APR)(?:il)|(May|may|MAY)|june|June|JUNE|July|july|JULY|(aug|Aug|AUG)(?:ust)|(SEP|sep|Sep)(?:tember)|(Sept|sept|SEPT)(?:ember)|(Oct|oct|OCT)(?:ober)|(Nov|NOV|nov)(?:ember)|(Dec|dec|DEC)(?:ember)))";
        private static final String MONTH_STRAYS2    =   "(?i:((Jan|jan|JAN)(?:uary)?|(Feb|feb|FEB)(?:ruary)?|(Mar|mar|MAR)(?:ch)?|(Apr|apr|APR)(?:il)?|(May|may|MAY)|june?|JUNE?|June?|july?|JULY?|July?|(aug|AUG|Aug)(?:ust)?|(sep|SEP|Sep)(?:tember)?|(Sept|SEPT|sept)(?:ember)?|(Oct|OCT|oct)(?:ober)?|(Nov|NOV|nov)(?:ember)?|(Dec|DEC|dec)(?:ember)?))";
        private static final String MONTH_STRAYS    = "(?i:\\b((Jan|jan|JAN)(?:uary)?|(Feb|feb|FEB)(?:ruary)?|(Mar|mar|MAR)(?:ch)?|(Apr|apr|APR)(?:il)?|(May|may|MAY)|june?|JUNE?|June?|July?|JULY?|july?|(aug|Aug|AUG)(?:ust)?|(sep|SEP|Sep)(?:tember)?|(Sept|SEPT|sept)(?:ember)?|(Oct|OCT|oct)(?:ober)?|(Nov|NOV|nov)(?:ember)?|(Dec|DEC|dec)(?:ember)?)\\b)";

        // ^ start; $ end; * >=0 times; + >=1 times; ? zero or one time;
        // [:punct:] [][!"#$%&'()*+,./:;<=>?@\^_`{|}~-]	Punctuation characters
        // [:alpha:]			[A-Za-z]
        // [:blank:]
        //:digit:]		\d	[0-9]	Digits
        //                      \D	[^0-9]	Non-digits
        private static final String[] date_regex = { //"(?:/|-|\\.)", //PAT_DATE_DELIM
                "( |:|,|;|.|!|@|\\?|#|\\$)(1|2)(\\d{3})( |,|.|\\?|;|:|\\?|#|\\$)", //1954
                "( |:|,|;|.|!|@|\\?|#|\\$)(1|2)(\\d{3})($)", //at the end of a line

                "(\\d{1,2}-\\d{1,2}-\\d{2,4})",// 12-23-2004
                "((1|2)\\d{3}-\\d{1,2}-\\d{1,2})", //2004-12-22
                "((1|2)\\d{3}-\\d{1,2}-\\d{1,2})( *)([@]?)( *)((\\d{1,2}:\\d{1,2}((:(\\d{2}))?))|(\\d{4}))( *)((am|pm|AM|PM)?)",

                "(?i:\\d{1,2}/\\d{1,2}/\\d{2,4})",// 12/23/2004 3/4/04
                "(?i:\\d{1,2}/\\d{1,2}/\\d{2,4}@\\d{1,2}:\\d{1,2})",// 12/23/2004@12:10
                "(?i:\\d{1,2}/\\d{1,2}/\\d{2,4}@\\d{4})",// 12/23/2004@1210
                "(?i:\\d{1,2}/\\d{1,2}/\\d{2,4}( +)\\d{1,2}:\\d{1,2})",// 12/23/2004 12:10
                "(?i:\\d{1,2}/\\d{1,2}/\\d{2,4}@\\d{1,2}\\d{1,2}) *(pm|am)",// 12/23/2004@12:10 pm
                //"(?i:\\d{1,2}/\\d{2,4})",// 12/2004 12/04 12/79 8/78
                "(( )+\\d{1,2}/\\d{2,4})", // 1/12 or 4/2008
                "(?i:\\d{1,2}\\.\\d{1,2}\\.\\d{2,4})",// 12.23.2004
                PAT_MONTH,
                MONTH_STRAYS,
                "(?i:\\d{1,2}/\\d{1,2}( *)-( *)\\d{1,2}/\\d{1,2})",  // 1/8-2/7
                "(\\d{1,2}[/\\-]"+PAT_MONTH+"[/\\-]\\d{2,4})", // 12-jan-2005
                "(?i:\\d{1,4}\\s"+PAT_MONTH+")", // 23 jan
                "(?i:"+PAT_MONTH+"\\.?(\\s+)?\\.?\\-?\\,?(\\s+)?\\d{1,2}(\\s+)?\\.?\\-?\\,?(\\s+)?\\d{1,4})",
          

                //PAT_MONTH+"( +)\\d{1,2}\\,( *)(\\d{2}|((1|2)(\\d{3})))", //jan 29,2004 jan 3,04
                "(?i:\\d{1,2}( *)"+PAT_MONTH+"( *)\\d{2,4})",// 12jan2006
                "(?i:\\b(jan(?:uary)?|feb(?:ruary)?|mar(?:ch)?|apr(?:il)?|june?|july?|aug(?:ust)?|sep(?:tember)?|september|oct(?:ober)?|nov(?:ember)?|dec(?:ember)?)\\b)",
                "(?i:"+MONTH_STRAYS+"\\.?\\,?(\\s+)?\\,?\\s?\\d{1,2})", // jan 23
                "(?i:"+MONTH_STRAYS+"\\.?\\,?(\\s+)?\\,?\\s?\\d{4})", // jan 2003
                "(?i:"+MONTH_STRAYS+"\\.?\\,?(\\s+)?\\,?\\s?\\d{1,2})(\\,*)( *)(\\d{2,4})", 
                // jan 23, 2004
                // May 23 2007
                "(?i:(\\[)( *)"+MONTH_STRAYS+"\\.?\\,?(\\s+)?\\,?\\s?\\d{1,2})(\\,*)( *)(\\d{2,4})( *)(\\])( *)(\\d{1,2}:\\d{2})((:\\d{2}){0,1})",
                // [may 23 2007] 12:43
                // [ may 23 2007 ] 12:43:34
                "(\\[)( *)"+MONTH_STRAYS+"(\\.?\\,?)( )?(\\d{1,2})(\\,*)( *)(\\d{2,4})( *)(\\])( *)(\\d{1,2}:\\d{2})((:\\d{2}){0,1})(( *(PM|AM|pm|am))?)",
                // [ may 23 2007 ] 12:43:34 am
                // [ may 23 2007 ] 12:43  PM
                "((\\d{1})|(\\d{2}))/\\d{2}/((\\d{2})|(\\d{4}))" + "((( *)@( *))|( +))" + "(\\d{1,2}:\\d{2})((:\\d{2})?)" + "(( *(pm|am|PM|AM))?)",
                "((\\d{1})|(\\d{2}))/\\d{2}/((\\d{2})|(\\d{4}))" + "((( *)@( *))|( +))" + "(\\d{4})",
                MONTH_STRAYS+"(\\.?\\,?)( )?(\\d{1,2})(\\,*)( *)((\\d{2})|(\\d{4}))( *)@( *)(\\d{1,2}:\\d{2})((:\\d{2}){0,1})(( *(PM|AM|pm|am))?)",
                "(?i:"+MONTH_STRAYS2+"\\.?\\,?(\\s+)?\\,?\\s?(\\d{1,2}))", // jan23 --
                "(?i:(\\d{1,2})\\.?\\,?\\s?"+MONTH_STRAYS2+"\\s)" // 08jan --
        };


    /**
     * Get the crude date word, storge in string[] array
     */
    private static ArrayList<ResultFormatOfDate> processSentence(String _currSentence){

        // ##1## define variables
        String regex;
        ArrayList<ResultFormatOfDate> foundDateTerm = new ArrayList<ResultFormatOfDate>();
        boolean match_found = false;
        boolean found_dates = false;
        int kind_of_date_regex = date_regex.length;

        // ##2## begin to run NLP on current sentence if it's not null.
        if ( _currSentence != null )
        {
            // ##2.1## use each regex expression to search date terms on current sentence
            for ( int regexIndex = 0; regexIndex < kind_of_date_regex; regexIndex++ )
            {
                // ##2.1.1## get current NLP expression
                regex = date_regex[regexIndex];
                Matcher matcher;

                // ##2.1.2## NLP
                matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher( _currSentence );
                match_found = matcher.find();
                if (match_found ) found_dates = true;

                // ##2.1.3## If found some date term
                while (match_found)
                {
                    int begin = matcher.start() ;
                    int end = matcher.end();

                    // ##2.1.3.1##  to some regex, so post process to found result
                    //              to currect them before saving

                    // ##2.1.3.1.1## to four digital years (at the end of a sentence)
                    if( (date_regex[regexIndex].compareTo( "( |:|,|;|.|!|@|\\?|#|\\$)(1|2)(\\d{3})($)" )) == 0 ){
                        String raw = matcher.group(0);
                        String mod = raw.substring(1,(raw.length()-1) );

                        char c_before = raw.charAt(0);

                        //System.out.println("\n\n1---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("1---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("["+_currSentence+"]");
                        //System.out.println("["+ raw+"]" + mod);
                        //System.out.println("before ["+c_before+"]");
                        

                        // to there results who have more than four characters appeared in ,
                        // let check the character just in front of these four digitals and
                        // make sure it's not a number
                        if( (c_before=='0')||(c_before=='1')||(c_before=='2')||(c_before=='3')
                                ||(c_before=='4')||(c_before=='5')||(c_before=='6')||(c_before=='7')
                                ||(c_before=='8')||(c_before=='9')){
                                match_found = matcher.find(begin + 1);
                                //System.out.println("--==droped==--\n");
                                continue;
                        }

                        ResultFormatOfDate df = new ResultFormatOfDate();
                        df.dateTermText  = mod;
                        df.start = begin ; // we add a space just while we send a sentence here
                        df.end  =  end - 1;

                        //System.out.println("--==saved==--\n");
                        foundDateTerm.add(df);


                    }
                    // ##2.1.3.1.2## to four digital years (not locate at the end of a sentence)
                    else if( (date_regex[regexIndex].compareTo( "( |:|,|;|.|!|@|\\?|#|\\$)(1|2)(\\d{3})( |,|.|\\?|;|:|\\?|#|\\$)" )) == 0 )
                    {
                        String raw = matcher.group(0);
                        String mod = raw.substring(1,(raw.length()-1) );

                        char c_before = raw.charAt(0);
                        char c_after = raw.charAt(raw.length() - 1 );
                        //System.out.println("2---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("["+_currSentence+"]");
                        //System.out.println("["+ raw+"]");
                        //System.out.println("before ["+c_before+"]");
                        //System.out.println("after ["+c_after+"]");

                        if( (c_before=='0')||(c_before=='1')||(c_before=='2')||(c_before=='3')
                                ||(c_before=='4')||(c_before=='5')||(c_before=='6')||(c_before=='7')
                                ||(c_before=='8')||(c_before=='9')){
                                match_found = matcher.find(begin + 1);
                                continue;
                        }

                        if( (c_after=='0')||(c_after=='1')||(c_after=='2')||(c_after=='3')
                                ||(c_after=='4')||(c_after=='5')||(c_after=='6')||(c_after=='7')
                                ||(c_after=='8')||(c_after=='9')){
                            match_found = matcher.find(begin + 1);
                            continue;
                        }


                        ResultFormatOfDate df = new ResultFormatOfDate();
                        df.dateTermText  = mod;
                        df.start = begin ; // we add a space just while we send a sentence here
                        df.end  =  end - 2;


                        foundDateTerm.add(df);


                    }
                    // ##2.1.3.1.3##  month-date-year
                    else if( date_regex[regexIndex].compareTo("(\\d{1,2}-\\d{1,2}-\\d{2,4})" ) == 0 )
                    {
                        String raw = matcher.group(0);

                        //System.out.println("\n\n3---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("current sentence = ["+_currSentence+"]");
                        //System.out.println("found = ["+ raw+"]");
                        //System.out.println("before ["+c_before+"]");
                        //System.out.println("after ["+c_after+"]");

                        //check char before and after this span
                        if( begin - 1 >= 0 ){
                            char c_before = _currSentence.charAt(begin-1);
                            
                            if( (c_before=='0')||(c_before=='1')||(c_before=='2')||(c_before=='3')
                                ||(c_before=='4')||(c_before=='5')||(c_before=='6')||(c_before=='7')
                                ||(c_before=='8')||(c_before=='9')){
                                match_found = matcher.find(begin + 1);
                                //System.out.println("--==droped==--");
                                continue;
                            }
                        }
                        if( end <= _currSentence.length() - 1){

                            char c_after = _currSentence.charAt(end);
                            if( (c_after=='0')||(c_after=='1')||(c_after=='2')||(c_after=='3')
                                    ||(c_after=='4')||(c_after=='5')||(c_after=='6')||(c_after=='7')
                                    ||(c_after=='8')||(c_after=='9')){
                                match_found = matcher.find(begin + 1);
                                //System.out.println("--==droped==--");
                                continue;
                            }
                        }

                        //int lastDashPosition = raw.lastIndexOf("-");
                        //String lastTwoorFourDigits = raw.substring( lastDashPosition, raw.length() );
                        //System.out.println("---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("---------------------->>>>>>>>>>>>>>>raw-" + raw);
                        //System.out.println("---------------------->>>>>>>>>>>>>>>lastTwoorFourDigits-" + lastTwoorFourDigits);

                        ResultFormatOfDate df = new ResultFormatOfDate();
                        df.dateTermText  = raw;
                        df.start = begin - 1 ; // we add a space just while we send a sentence here
                        df.end  =  end - 1 ;
                        foundDateTerm.add(df);
                        //System.out.println("--==saved==--");

                    }
                    // ##2.1.3.1.4##  year+month+date
                    else if (date_regex[regexIndex].compareTo("((1|2)\\d{3}-\\d{1,2}-\\d{1,2})") == 0) 
                    {
                        String raw = matcher.group(0);

                        //System.out.println("\n\n4---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("4---------------------->>>>>>>>>>>>>>>");
                        //System.out.println("current sentence = ["+_currSentence+"]");
                        //System.out.println("found = ["+ raw+"]");
                        //System.out.println("before ["+c_before+"]");
                        //System.out.println("after ["+c_after+"]");

                        //check char before and after this span
                        if( begin - 1 >= 0 ){
                            char c_before = _currSentence.charAt(begin-1);

                            if( isDigit(c_before) ){
                                match_found = matcher.find(begin + 1);
                                //System.out.println("--==droped==-- as before char is digit["+c_before+"].");
                                continue;
                            }
                        }
                        if( end <= _currSentence.length() - 1){

                            char c_after = _currSentence.charAt(end);
                            if( (isDigit(c_after))){
                                match_found = matcher.find(begin + 1);
                                //System.out.println("--==droped==-- as after char is digit["+c_after+"].");
                                continue;
                            }
                        }
                        
                        // save found
                        ResultFormatOfDate df = new ResultFormatOfDate();
                        df.dateTermText  = raw;
                        df.start = begin - 1 ; // we add a space just while we send a sentence here
                        df.end  =  end - 1 ;
                        foundDateTerm.add(df);
                        //System.out.println("--==saved==-- + [" + raw + "]");
                    }
                    else if( date_regex[regexIndex].compareTo("(( )+\\d{1,2}/\\d{2,4})" ) == 0 ){
                            // 1/12 or 4/2008

                            String str =  matcher.group(0);
                            if ((str==null)||(str.trim().length() < 1) ){
                                    match_found = matcher.find(begin + 1);
                                    continue;
                            }

                            //System.out.println("4---------------------->>>>>>>>>>>>>>>");

                            //remove all space in the front part of this term
                            // count the amount of spaces
                            char[] chr = str.toCharArray();
                            int count_space = 0;
                            for(int i =0; i < str.length(); i++){
                                    if( chr[i] != ' ')
                                            continue;
                                    else count_space++;
                            }
                            str = str.substring(count_space , str.length());

                            // cut term to head and tail; such as cut 12/03 to 12 and 03
                            int p = str.indexOf("/");
                            String heads = str.substring(0,p);
                            String tails = str.substring(p+1,str.length());

                            // System.out.println( "Current found = [" + str // matcher.group(0) + "]; HEAD = [" + heads+"]; TAIL = [" + tails +"]" );

                            // convert date/month/years from string to int
                            int iheads, itails;
                            iheads = Integer.valueOf(heads);
                            itails = Integer.valueOf(tails);

                            //if is 1/12 or 31/05 04/04  2/79
                            if ( ( heads.length() <= 2)&&( tails.length() <= 2) ){
                                    if (( itails > 0)&&(iheads > 0 )) {
                                        ResultFormatOfDate df = new ResultFormatOfDate();
                                        df.dateTermText  = str;
                                        df.start = begin - 1 + count_space ; // we add a space just while we send a sentence here
                                        df.end  =  end - 1 ;
                                        foundDateTerm.add(df);
                                    }
                            }else if( ( heads.length() <= 2)&&( tails.length() == 4) ){
                            //if is 1/12 or 31/1905 04/2004
                                    if (( itails <= 2100)&&(itails > 1000 )&&(iheads <= 12) ) {
                                        ResultFormatOfDate df = new ResultFormatOfDate();
                                        df.dateTermText  = str;
                                        df.start = begin - 1 + count_space ; // we add a space just while we send a sentence here
                                        df.end  =  end - 1 ;
                                        foundDateTerm.add(df);
                                    }
                            }

                        }else if(( date_regex[regexIndex].compareTo(PAT_MONTH) == 0 )
                            ||(date_regex[regexIndex].compareTo(MONTH_STRAYS) == 0 ) ){
                            String dateTermText  = matcher.group(0);
                            if(dateTermText.compareTo("may")!=0){
                                ResultFormatOfDate df = new ResultFormatOfDate();
                                df.dateTermText  = matcher.group(0);
                                df.start = begin - 1 ;
                                df.end  =  end - 1 ;
                                foundDateTerm.add(df);
                            }
                            //
                        } else {
                            ResultFormatOfDate df = new ResultFormatOfDate();
                            df.dateTermText  = matcher.group(0);
                            df.start = begin - 1 ;
                            df.end  =  end - 1 ;
                            foundDateTerm.add(df);
                        }

                        //System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$4" + matcher.toString());

                        match_found = matcher.find(begin + 1);
                    }

                }


        }else{
            return null;
        }

        //if( count == 1 ) return null;
        // if can found 'dates', return them
        if ( found_dates ) { return foundDateTerm; }

        return null;
    }

        // use space windows to filter out the repetitive date words
        private static ArrayList<ResultFormatOfDate> windowFilter(String _currSentence) {
            if ( _currSentence == null )
                return null;

            ArrayList<ResultFormatOfDate> dates = processSentence( _currSentence );
            if ( dates == null )
                return null;

            int size = dates.size();
            if ( size < 1 )
                return null;
                
                

                for ( int loops = 0; loops < size; loops++){
                    ResultFormatOfDate df = dates.get(loops);
                    if ( df.dateTermText == null )
                        continue;

                    int windows_begin = df.start ;
                    int windows_end = df.end;

                        if ( Math.abs( windows_end - windows_begin ) <= 1 ) {
                              df.dateTermText = null;
                              df.start = -1;
                              df.end = -1;
                              dates.set(loops, df);
                              continue;
                        }


                        for ( int subloops = 0; subloops < size; subloops++){
                                
                                if ( dates.get(subloops).dateTermText == null )
                                        continue;
                                if ( loops == subloops )
                                        continue;

                                int begin = dates.get(subloops).start;
                                int end = dates.get(subloops).end ;

                                if ( ( begin >= windows_begin  ) && ( end <= windows_end ) ) {
                                        //System.out.println(" begin(" + begin + ") >= windows_begin(" + windows_begin + ")");
                                        ResultFormatOfDate df2 = new ResultFormatOfDate();
                                        df2.dateTermText = null;
                                        df2.end = -1;
                                        df2.start = -1;
                                        dates.set(subloops, df2);
                                }
                        }

                }

                //System.out.println("[" + _currSentence + "]");
                
                //for ( int loops = size - 1; loops = 0; loops--){
                //        if ( dates[loops*3+1] != null )
                //        System.out.println("the " + loops + " records is [" + dates[loops*3+1] +"]  " + dates[loops*3+2] +", "+ dates[loops*3+3] );
                //}

                return dates;
        }

        public static ArrayList<ResultFormatOfDate> catchValidDateAndTime( String _currsent ){

                if ( _currsent == null )
                        return null;
                
                ArrayList<ResultFormatOfDate> datewords = windowFilter( _currsent );
                return datewords;
        }

        /**
         * Check a given character is a digital character or not.
         *
         * @param   numbercharacter
         *          The given character for testing.
         *
         * @return  false
         *          If the given character is not a digital character.
         *          true
         *          if given character is a digital character in range '0' to '9'
         */
        private static boolean isDigit(char numbercharacter){
            try{
                if(( numbercharacter >= '0' )&&(numbercharacter <= '9'))
                    return true;
                else
                    return false;
            }catch(Exception e){
                log.LoggingToFile.log( Level.SEVERE, "Error 1009150939 : error got while try to figure "
                        + "out whether a character is digital character!!!");
                return false;
            }

        }

}

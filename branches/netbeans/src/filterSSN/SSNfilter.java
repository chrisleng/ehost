/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package filterSSN;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jianwei Leng
 */
public class SSNfilter {

    private static final String[] _ssnRegex_ = {
        "\\d{3}-\\d{2}-\\d{4}",
        "\\d{9}",
        "\\d{3}( +)\\d{2}( +)\\d{4}",
        "([a-z]|(A-Z))\\d{4}"
        };

    public void SSNFinder( int _paragraphOverallIndex,
            String _thisParagraph,
            int _lengthOfProcessedParagraph,
            int _indexInCurrentFile) // index of sentence in current file, start from 1)
    {
         int kindsOfRegex = _ssnRegex_.length;

         if ( _thisParagraph == null ) {
                return;
         }

         for ( int regexIndex = 0; regexIndex < kindsOfRegex; regexIndex++ )
         {
                String regex = _ssnRegex_[regexIndex];
                Matcher matcher;
                boolean flag_MatchFound = false;

                matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher( _thisParagraph );
                flag_MatchFound = matcher.find();

                if ( flag_MatchFound ) {
                    while ( flag_MatchFound ) {
                        int begin = matcher.start() ;
                        int end = matcher.end();
                        String ssn = _thisParagraph.substring( begin, end );
                        
                        if( !checkAgainstCharacter(ssn, begin, end, _thisParagraph, regexIndex ) ){
                            flag_MatchFound = matcher.find( begin + 1 );
                            continue;
                        }

                        if( !checkValidity(ssn, regexIndex) ){
                            flag_MatchFound = matcher.find( begin + 1 );
                            continue;
                        }

                        String info = "found SSN - ["
                                + ssn
                                +"] - located at ["
                                + String.valueOf(begin + _lengthOfProcessedParagraph)
                                + ", " + String.valueOf(end + _lengthOfProcessedParagraph)
                                + "]";
                        log.LoggingToFile.log( Level.INFO, info );
                        logs.ShowLogs.printInfoLog( info );
                        
                        nlp.storageSpaceDraftLevel.StorageSpace.addSSNs(
                                _paragraphOverallIndex,
                                ssn, 
                                begin, end,
                                begin + _lengthOfProcessedParagraph,
                                end + _lengthOfProcessedParagraph);

                        flag_MatchFound = matcher.find( begin + 1 );
                    }
                }
         }
         
    }

    /**most of following rules get disabled as situation of VA*/
    /**
     * [1] no any first three digits are big than 772 or between 734 and 749
     *     as a valid SSN cannot have an area number between 734 and 749,
     *     or above 772, the highest area number which the Social Security
     *     Administration has allocated
     * [2] no any first three digits are 666
     *     as Numbers of the form 666-xx-####, probably due to the potential
     *     controversy
     * [3] no numbers from 987-65-4320 to 987-65-4329
     *     as they are reserved for use in advertisements
     * [4] no 078-05-1120
     *     as (078-05-1120) has been claimed by a total of over 40,000 people
     *     as their own
     * [5] no Numbers with all zeros in any digit group (000-xx-####, ###-00-####, ###-xx-0000)
     * [6] in VA, we like to consinder a inital character plus last four digits
     *     of SSN as a SSN
     */
    private boolean checkValidity( String _SSN, int _type ){

        

        
        /** @para _type
         *  0 - "\\d{3}-\\d{2}-\\d{4}",        --_SSN
         *  1 - "\\d{9}",                      --_SSN
         *  2 - "\\d{3}( +)\\d{2}( +)\\d{4}"   --_SSN
         */
        String firstThreeDigits, secondTwoDigits, lastFourDigits;
        int firstThree, secondTwo, lastFour;

        // *  0 - "\\d{3}-\\d{2}-\\d{4}",        --_SSN
        if( _type == 0){
            firstThreeDigits = _SSN.substring(0, 3);
            secondTwoDigits = _SSN.substring(4, 6);
            lastFourDigits = _SSN.substring(7, 11);
            
        // *  1 - "\\d{9}",                      --_SSN
        }else if( _type == 1){
            firstThreeDigits = _SSN.substring(0, 3);
            secondTwoDigits = _SSN.substring(3, 5);
            lastFourDigits = _SSN.substring(5, 9);
            
        }else if( _type == 2){
            String SSN = _SSN.replaceAll("( +)", "_");
            firstThreeDigits = SSN.substring(0, 3);
            secondTwoDigits = SSN.substring(4, 6);
            lastFourDigits = SSN.substring(7, 11);
        }else if( _type == 3){
            lastFourDigits = _SSN.substring(1, 5);
            if ( _SSN.compareTo("0000") == 0 )
                return false;

            return true;
        }else {
            return false;
        }


        
        //System.out.println("firstThreeDigits   --- ["+firstThreeDigits+"]");
        //System.out.println("secondTwoDigits    --- ["+secondTwoDigits+"]");
        //System.out.println("lastFourDigits     --- ["+lastFourDigits+"]");

        firstThree  = Integer.valueOf( firstThreeDigits );
        secondTwo   = Integer.valueOf( secondTwoDigits );
        lastFour    = Integer.valueOf( lastFourDigits );

        /**this sentence disable all rules, if you want to enable rules,
         * please disable this sentence*/
        return true;

        /*
        // [1] no any first three digits are big than 772 or between 734 and 749
        if ((firstThree >= 734)&&(firstThree <= 749))
            return false;
        if (firstThree >= 772)
            return false;
        
        // [2] no any first three digits are 666
        if (firstThree == 666)
            return false;

        // [3] no numbers from 987-65-4320 to 987-65-4329
        if ( ( firstThree == 987 )&&( secondTwo==65 ) ){
            if((lastFour>4320)&&(lastFour<4329))
                return false;
        }

        // [4] no 078-05-1120
        if ( ( firstThree == 78 )&&( secondTwo == 5 )&&( lastFour == 1120 ) )
            return false;

        // [5] no Numbers with all zeros in any digit group (000-xx-####, ###-00-####, ###-xx-0000)
        if(firstThreeDigits.compareTo("000")==0)
            return false;
        if(secondTwoDigits.compareTo("00")==0)
            return false;
        if(lastFourDigits.compareTo("0000")==0)
            return false;

        
        return true;
         */
    }

    /**
     * SSN format in ###-##-#### or ### ## #### or #########
     * any character next to SSN number CANNOT be a number from 0 to 9
     */
    private boolean checkAgainstCharacter( String _SSN, int _start, int _end, String _thisParagraph, int _type ){
        if( _type == 3){
            if( _end < ( _thisParagraph.length() - 1 ) ) {
                char c = _thisParagraph.charAt( _end );

                if( (c=='0')||(c=='1')||(c=='2')||(c=='3')||(c=='4')||(c=='5')
                        ||(c=='6')||(c=='7')||(c=='8')||(c=='9') ){
                    return false;
                }                
            }
            return true;
        }

        boolean vaild = false;
        if( _start > 0){
            char c = _thisParagraph.charAt( _start - 1 );
            
            if( (c=='0')||(c=='1')||(c=='2')||(c=='3')||(c=='4')||(c=='5')
                    ||(c=='6')||(c=='7')||(c=='8')||(c=='9') ){
                return false;
            }
        }
        if( _end < ( _thisParagraph.length() - 1 ) ) {
            char c = _thisParagraph.charAt( _end );            
            
            if( (c=='0')||(c=='1')||(c=='2')||(c=='3')||(c=='4')||(c=='5')
                    ||(c=='6')||(c=='7')||(c=='8')||(c=='9') ){
                return false;
            }
        }
        return true;
    }

}

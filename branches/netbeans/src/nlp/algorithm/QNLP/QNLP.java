/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.algorithm.QNLP;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jianwei Leng
 */
public class QNLP {

    private Vector<String> local_uterm = new Vector<String>();
    private Vector<Integer> positions = new Vector<Integer>();

    /**
     * By the content in this paragraph, find positions of pre-annotated 
     * concepts in the dictionaries.
     */
    private void findPositions( String _thisParagraph ){
        local_uterm.clear();
        positions.clear();

        if(( _thisParagraph == null )||(_thisParagraph.trim().length() < 1))
            return;

        String[] words = _thisParagraph.split(" ");
        if (words==null)
            return;

        // catch all uterms
        for( String word : words ){
            if (( word == null )||(word.trim().length() < 1))
                continue;

            word = word.trim();
            if ( word.length() <= 3 ) {
                saveLocalUterm( word );
            }else{
                saveLocalUterm( word.substring(0, 3));
            }
        }

        // use caught uterm to locate potential pre-annotated entries in the dictionary
        if( local_uterm == null)
            return;
        for( String uterm : local_uterm ){
            Vector<Integer> upositions = RouteMapDepot.getPosition(uterm);
            if ( upositions == null)
                continue;
            for(int i: upositions){
                positions.add(i);
                //System.out.print(" " + i + ",");
            }
        }
    }

    private void saveLocalUterm(String uterm){
        //System.out.println("--------"+uterm);
        if (!existed(uterm)){
            local_uterm.add(uterm);
        }
    }

    private boolean existed(String uterm){
        if( uterm == null )
            return true;
        if( local_uterm == null )
            return false;
        
        for( String term : local_uterm){
            if (term.equals(uterm))
                return true;
        }
        
        return false;
    }

    /**
     * Parmiary function of this class.
     */
    public void termFinder( int _paragraphOverallIndex,
            String _thisParagraph,
            int _offsetInFile,
            int _indexInCurrentFile) // index of sentence in current file, start from 1)
    {
        
        
        
        //int kindsOfRegex = RouteMapDepot.size();

         if ( _thisParagraph == null ) {
                return;
         }

        findPositions( _thisParagraph );

        if(( this.positions == null )||(this.positions.size() < 1))
            return;

        for(int i=0; i< positions.size();i++){
            int position = positions.get(i);
            DictionaryFormat df = DictionaryDepot.get(position);
            if ( df == null)
                continue;
            if(( df.term == null )||(df.category==null))
                continue;
            NLP(df, _paragraphOverallIndex,
                _thisParagraph,
                _offsetInFile,
                _indexInCurrentFile );
        }

        /*
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

                        Logs.ShowLogs.printInfoLog("found SSN - ["
                                + ssn
                                +"] - located at ["
                                + String.valueOf(begin + _offsetInFile)
                                + ", " + String.valueOf(end + _offsetInFile)
                                + "]");
                        
                        StorageSpaceDraftLevel.StorageSpace.addSSNs(
                                _paragraphOverallIndex,
                                ssn, 
                                begin, end,
                                begin + _offsetInFile,
                                end + _offsetInFile);

                        flag_MatchFound = matcher.find( begin + 1 );
                    }
                }
         } * */
         
    }

    private void NLP(DictionaryFormat _df,
            int _paragraphOverallIndex,
            String _thisParagraph,
            int _offsetInFile,
            int _indexInCurrentFile){
        Matcher matcher;
        boolean flag_MatchFound = false;

        if(_df == null)
            return;
        String regex = _df.term.trim();
        String category = _df.category.trim();
        try{
            matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher( " "+ _thisParagraph + " " );
            flag_MatchFound = matcher.find();
            while ( flag_MatchFound ) {
                int begin = matcher.start() + 1 ;
                int end = matcher.end() - 1;
                String term = _thisParagraph.substring( begin - 1, end - 1 );

                nlp.storageSpaceDraftLevel.StorageSpace.addConcept_QNLP(
                                        // overall index to show paragraph position in the arraylist
                                        _paragraphOverallIndex,
                                        // concept text
                                        term,
                                        begin + 1,
                                        end + 1,
                                        begin + _offsetInFile - 1,
                                        end + _offsetInFile - 1,
                                        category
                                        //end_span
                                        );
                flag_MatchFound = matcher.find( begin + 1 );
            }
        }catch(Exception ex){
        }

    }



}

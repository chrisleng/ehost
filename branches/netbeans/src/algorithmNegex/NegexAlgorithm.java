/*

* NegexAlgorithm.java
 *
 * Created on June 7, 2006, 4:07 PM
 * History: Thursday   04-24-2010  02:00 pm MST, change to a static method
 * History: Thursday   06-03-2010  09:32 am MST, add support to convert
 *          negation effect range from unit in word to unit in characters.
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package algorithmNegex;

/** *** Main File for NegexAlgorithm*** */
import commons.Constant;
import java.io.*;
import java.util.logging.Level;
import java.util.regex.*;


public class NegexAlgorithm {

	private String currsent, currnote, ptid, notenum;
        
	private int[] currsent_flag;
        private static int[][] currsent_flag_position;
	private BufferedReader negFile, termFile, inputReader;
        private BufferedReader conceptFile;
	private FileOutputStream out;
	private PrintStream p;
	static int saindex, cnindex;
	private String[] currsentw;
	private int[] currsentw_flag;
        private static int[][] currsentw_flag_position;
	//private termTable term, nterm;
        //private conceptTable concept, nconcept; // structure for concept words and amount
	private negTable negation, nnegation;
	static char[] sentarray;
	private static final int pseudo = 100, preneg = 101, posspre = 102,
			postneg = 103, posspost = 104, conj = 105;
        private static final int words_in_negation_phrase = -200;
	private static final int term_found = 1, term_negated = 2,
			term_possible = 3,  term_space = -99, wndsize = 8;       
	private int window_begin, window_end;
	private boolean found_term;
	int sent_index = 0 ;
	String[] sentence_array = null ;
	int neg_start_span = 0, neg_end_span = 0 ;
        private static boolean FLAG_NEED_TO_FIND_CONCEPTS, FLAG_NEED_TO_FIND_DATES;
        private boolean FLAG_OUTPUT_NEGATED = false, IS_THREE_COLUMNS = false;
        private static boolean ENABLED_LOG_OUTPUT_RegexResult = false;

        // flag: print the details of regular expression founds in method flag_keyword(...)
        // flag: _enabled == true
        public static void setLogPrintRegularExpressionDetails(boolean _ENABLED_or_NOT ){
                NegexAlgorithm.ENABLED_LOG_OUTPUT_RegexResult = _ENABLED_or_NOT;
        }
	// Function to read the list of negation keywords from negation.txt
	private void getneg() throws IOException {
		String line = this.negFile.readLine();
		while (line != null) {
			String[] st = line.split(",");
			negation.len++;
			nnegation.len++;
			negation.word[negation.len] = " " + st[0] + " ";
			// negation.word[negation.len] = st[0];
			negation.type[negation.len] = Integer.valueOf(st[1]).intValue();

			// replace " " in phrase with "_" to force a phrase to be 1 word
			nnegation.word[nnegation.len] = " "
					+ negation.word[negation.len].trim().replaceAll(" ", "_").toUpperCase()
                                        + " ";

			line = negFile.readLine();
		}
		negation.len++;
		nnegation.len++;

	}

        
        private void flag_keywords(String regex, int type, String replacement, int _weight,
                int _found_by_which_dictionary, int _found_by_which_entry) {

                Matcher matcher;
                boolean match_found;
                try{
                        matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( currsent );
                        match_found = matcher.find();

                        
                        
                        while ( match_found )
                        {
                                
                                int start = matcher.start();
                                int end = matcher.end();

                                char[] currsentchars = currsent.toCharArray();
                                //for(int ss = 0; ss < currsent.length(); ss++){
                                //        System.out.println("["+ss+"] = '"+ currsentchars[ss] +"'");
                                //}
                                //System.out.println("original start =" + start + ", original end = " + end );
                                while(( currsentchars[start] == ' ' )
                                        ||( currsentchars[start] == '_' )
                                        ||( currsentchars[start] == '(' )
                                        ||( currsentchars[start] == ')' )
                                        ||( currsentchars[start] == '[' )
                                        ||( currsentchars[start] == ']' )
                                        ){
                                     start++;
                                }
                                while( (currsentchars[end-1] == ' ')||(currsentchars[end-1] == '.')||(currsentchars[end-1] == '_')
                                        ||(currsentchars[end-1] == ',')
                                        ||(currsentchars[end-1] == ')')
                                        ||(currsentchars[end-1] == '(')
                                        ||(currsentchars[end-1] == '[')
                                        ||(currsentchars[end-1] == ']')
                                        ||(currsentchars[end-1] == ';'||(currsentchars[end-1] == '?')) ){
                                     end--;
                                }
                                

                                //System.out.println("fixed start =" + start + ", fixed end = " + end );

                                found_term = match_found;
                                /**System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<< currsent_flag_position"// ["+i+"] ---"
                                //        + String.valueOf( start )
                                //        + "/"+ String.valueOf( end ) + " | current found = " + matcher.toString()
                                //        + " - found [" + matcher.group(0) +"]"
                                //        );
                                */
                                // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<"+ currsent+"----------   " + regex + match_found);
                                int i = matcher.start() + 1;
                                currsent_flag[i] = type;

                                //pseudo = 100, preneg = 101, posspre = 102,
                                //postneg = 103, posspost = 104, conj = 105;
                                
                                int i_start = currsent_flag_position[i][0];
                                int i_end = currsent_flag_position[i][1];
                                if(( i_start == start )&&( end > i_end )){
                                        currsent_flag_position[i][0] = start;
                                        currsent_flag_position[i][1] = end;

                                        currsent_flag_position[i][2] = _weight;

                                        currsent_flag_position[i][3] = _found_by_which_dictionary;
                                        currsent_flag_position[i][4] = _found_by_which_entry;
                                        
                                }
                                if( i_start != start ){
                                        currsent_flag_position[i][0] = start;
                                        currsent_flag_position[i][1] = end;

                                        currsent_flag_position[i][2] = _weight;

                                        currsent_flag_position[i][3] = _found_by_which_dictionary;
                                        currsent_flag_position[i][4] = _found_by_which_entry;
                                }
                                
                                                
                                       
                                //}
                                match_found = matcher.find(i + 1);
                        }
                
                        if (( replacement != null )&&( match_found ))
                                currsent = matcher.replaceAll(replacement);
                        // above "replacement" are replaced " " by "_", if they have

                }catch(Exception e){
                        //Logs.ShowLogs.printErrorLog("INVALID ANNOTATOR EXPRESSION -  {" + regex + "}");
                        //Logs.ShowLogs.printErrorLog(e.toString());
                }
        }
        
        

	// Compare for an imperfect match between two words
	private void flag_currsentence( String st) {
		
		boolean found = false;
		found_term = false;

		// reset sentence string and flag array
		currsent = new String(st);
		currsent_flag = new int[st.length()];
                currsent_flag_position = new int[st.length()][5];
                
                
                  


                int cf_size = dictionaries.ConceptDictionaries.ConceptArray.size();
                dictionaries.ConceptDictionaryFormat cf;
                


                // flag all concept keywords
		int ii = 0;
                String regex = null, RAWregex = null;
                
                
                // exit if user did NOT ask for concept
                if(env.Parameters.NLPAssistant.OUTPUTCONCEPT){
                    try{
                        for(int c = 0; c< cf_size; c++){
                            cf = dictionaries.ConceptDictionaries.ConceptArray.get(c);
                            int size = cf.preprocessed_preannotated_Concept.size();
                            int weight = cf.weight;
                            //int size_fromRecord = nlp.switchpool.ConceptLib.amount_of_ValidPreAnnotatedConcepts;

                            //if (size_fromArray != size_fromRecord) size = size_fromArray;

                            for ( ii = 0; ii < size; ii++) {

                                regex = cf.preprocessed_preannotated_Concept.get(ii) ;
                                        
                                //flag_keywords(regex, term_found, RAWregex ); //search the term in current sentence
                                flag_keywords(regex, term_found, null, weight, c, ii );
                                        
                                // if found term in current sentence, "found_term = true"
                                if (found_term)
                                    found = true;

                            }
                        }
                    }catch(Exception e){
                        
                    }
                }


                // need to search for negative words, if found term or concept in current sentence
		if ((found)||(env.Parameters.NLPAssistant.OUTPUTNEGATION))
                {
                    // flag all negation keywords
                    int j = 0;

                    if (env.Parameters.NLPAssistant.OUTPUTNEGATION) {
                        for (j = 0; j < negation.len; j++) {
                            // check for negation keywords (with punctuation) and flag
                            regex = "[ |[\\p{Punct}&&[^_]]]" + negation.word[j].trim()
                                + "[[\\p{Punct}&&[^_]]| ]";
                            flag_keywords(regex, negation.type[j], nnegation.word[j], -1, -1, -1 );
                            // -1 mean invalid
                        }
                    }

                    // flag the space in a sentence to note the position of each word
                    int i = 0;
                    while (( i < currsent.length() ) && ( i != -1 ) ) {
                        currsent_flag[i] = term_space;
                        i = currsent.indexOf(" ", i + 1);
                    }

                    found_term = found;
		}
	}

    // change the word to its original form
    private String changeback(String t) {
            // System.out.println(t);
            t = t.trim().replaceAll("_", " ");
            t = t.replaceAll(":", " ").trim();
            // System.out.println(t);
            return t;
    }

    // Obtain the CUI for the given keyword
    /*
     * private String getcui(String kw) { String cui = ""; try { ResultSet rs =
     * CuiData.display(kw); rs.next(); if (rs == null) {
     * System.out.println("rs is null"); } if (rs != null) {
     * //System.out.println("rs is not null"); cui = rs.getString("CUI");
     * //System.out.println(cui); }
     *
     * if ((cui == null) || (cui.equals(""))) { cui = "no cui found"; } } catch
     * (Exception e) { System.out.println("Exception " + e + " occured"); }
     * //System.out.println("Cui found is " + cui); return cui; }
     */

    // Define the negation window if pre-negation
    private void pre_window(int i) {

            window_begin = i + 1;
            window_end = i + wndsize;
            if (window_end > currsentw.length - 1) {
                    window_end = currsentw.length - 1;
            }

            // if any negation phrase inside window, change window size to end
            // before negation phrase
            for (int j = window_begin; j <= window_end; j++) {
                    if (currsentw_flag[j] > 100) {
                            window_end = j - 1;
                            // System.out.println("window end " + window_end);
                            break;
                    }
            }
    }



    // Define the negation window if pre-negation
    private void pre_window_full(int i) {

            window_begin = i + 1;
            window_end = currsentw.length - 1;

            // if any negation phrase inside window, change window size to end
            // before negation phrase
            for (int j = window_begin; j <= window_end; j++) {
                    if (currsentw_flag[j] > 100) {
                            window_end = j - 1;
                            // System.out.println("window end " + window_end);
                            break;
                    }
            }
    }

    // Define the negation window if post-negation
    private void post_window(int i) {

            window_begin = i - wndsize;
            if (window_begin < 0) {
                    window_begin = 0;
            }

            // if window begin is within the previous window, change to begin after
            // the previous window
            if (window_begin <= window_end) {
                    window_begin = window_end + 1;
            }

            window_end = i - 1;

            // if any negation phrase inside window, change window size to begin
            // after negation phrase
            for (int j = window_begin; j <= window_end; j++) {
                    if (currsentw_flag[j] > 100) {
                            window_begin = j + 1;
                            break;
                    }
            }
    }

    private void post_window_full(int i) {

            window_begin = 0;

            // if window begin is within the previous window, change to begin after
            // the previous window
            if (window_begin <= window_end) {
                    window_begin = window_end + 1;
            }

            window_end = i - 1;

            // if any negation phrase inside window, change window size to begin
            // after negation phrase
            for (int j = window_begin; j <= window_end; j++) {
                    if (currsentw_flag[j] > 100) {
                            window_begin = j + 1;
                            break;
                    }
            }
    }

    // Flag keywords inside window as negated
    private void flag_neg(String negation, int _offset_for_multiplelines,
                    // lenth of all sentence before this one in current paragraph
                    int _length_before_currsent,
                    // show the paragraph overall index in all files
                    int _paragraphOverallIndex,
                    // length of all previous processed paragraph
                    int _length_of_processed_paragraph) {
            String cui, original;
            int termchk;

            //
            if( !FLAG_NEED_TO_FIND_CONCEPTS )
                    return;

            for (int j = window_begin; j <= window_end; j++) {
                    termchk = currsentw_flag[j];

                    // found concept in this window
                    if (termchk == term_found) {
                            original = changeback(currsentw[j]);
                            nlp.storageSpaceDraftLevel.StorageSpace.addConcepts(
                                    // overall index to show paragraph position in the arraylist
                                    _paragraphOverallIndex,
                                    // concept text
                                    currsent.substring(currsentw_flag_position[j][0], currsentw_flag_position[j][1]),
                                    currsentw_flag_position[j][0] + 1 + _length_before_currsent,
                                    currsentw_flag_position[j][1] + 1 + _length_before_currsent,
                                    currsentw_flag_position[j][0] + _length_before_currsent + _length_of_processed_paragraph -1,
                                    currsentw_flag_position[j][1] + _length_before_currsent + _length_of_processed_paragraph -1,
                                    currsentw_flag_position[j][2],
                                    currsentw_flag_position[j][3],
                                    currsentw_flag_position[j][4]
                                    );


                            logs.ShowLogs.printImportantInfoLog("found negation relationsip = " + original + "\t" + negation) ;
                            log.LoggingToFile.log( Level.FINE, "found negation relationsip = " + original + "\t" + negation );
                            currsentw_flag[j] = term_negated;
                    }
            }

    }

    // Flag keywords inside window as possible found
    private void flag_possible(String possible_phrases,
            int original_span_start,
            int original_span_end,
            // lenth of all sentence before this one in current paragraph
            int _length_before_currsent,
            // show the paragraph overall index in all files
            int _paragraphOverallIndex,
            // length of all previous processed paragraph
            int _length_of_processed_paragraph) {

            String cui, original;
            int termchk;

            if( !FLAG_NEED_TO_FIND_CONCEPTS )
                    return;


            // to all words appearing in current window( from windows begin to windows end )
            for (int j = window_begin; j <= window_end; j++) {
                    termchk = currsentw_flag[j];



                    // if it's a term
                    if (termchk == term_found) {

                            // following for output to screen
                            int sent_start = 0 ;
                            for (int small_j = 0; small_j < sent_index; small_j++)
                                    sent_start += sentence_array[small_j].length() + 2 ;

                            int start_span = sent_start ; //sentSpans.get(sent_index) ;

                            for (int small_i = 0; small_i < j ; small_i++)
                            {
                                    // add length of word plus one for the space that split it
                                    start_span += currsentw[small_i].length() + 1 ;
                                    // System.out.println("the words between current sentence" + currsentw[small_i]);
                            }
                            int end_span = start_span + currsentw[j].length() ;

                            // add span for uncounted whitespace at beginning of line
                            int toAdd = 0 ;
                            while (!sentence_array[sent_index].substring(toAdd).startsWith(sentence_array[sent_index].trim()))
                            {
                                    toAdd++ ;
                            }
                            start_span += toAdd ;
                            end_span += toAdd ;

                            original = changeback(currsentw[j]);

                            nlp.storageSpaceDraftLevel.StorageSpace.addConcepts(
                                    // overall index to show paragraph position in the arraylist
                                    _paragraphOverallIndex,
                                    // concept text
                                    currsent.substring(currsentw_flag_position[j][0], currsentw_flag_position[j][1]),
                                    currsentw_flag_position[j][0] + 1 + _length_before_currsent,
                                    currsentw_flag_position[j][1] + 1 + _length_before_currsent,
                                    currsentw_flag_position[j][0] + _length_before_currsent + _length_of_processed_paragraph -1,
                                    currsentw_flag_position[j][1] + _length_before_currsent + _length_of_processed_paragraph -1,
                                    currsentw_flag_position[j][2],
                                    currsentw_flag_position[j][3],
                                    currsentw_flag_position[j][4]
                                    //end_span
                                    );

                            // to all term found in current windows marked them as term_possible
                            currsentw_flag[j] = term_possible;
                    }
            }
    }

    /** The main NegexAlgorithm function */
    private void negexChapman(int _offsetInParagraph,
            int _offsetInFile,
            int _paragraphOverallIndex,
            int _length_of_previous_processsed_paragraphs) {

        window_begin = 0;
	window_end = 0;

        // to all words in current sentence, check their flag and
        // try to find concepts in their effective windows
        for (int i = 0; i < currsentw.length; i++) {
            String possible_phrase;
            switch (currsentw_flag[i]) {

                case preneg:
                        possible_phrase = currsent.substring( currsentw_flag_position[i][0],
                                currsentw_flag_position[i][1]);

                        pre_window_full(i);

                        neg_start_span = currsentw_flag_position[i][0]
                                + _offsetInParagraph  - 1 + _offsetInFile;
                        neg_end_span   = currsentw_flag_position[i][1]
                                + _offsetInParagraph - 1 + _offsetInFile;
                        //flag_neg(currsentw[i], offset_for_multiple_lines);

                        // get effective range of this negative phrase.
                        Table_range range = getEffectiveRange(window_begin, window_end, currsent);
                        // add offset
                        range.lowerside = range.lowerside + _offsetInParagraph
                                        + _offsetInFile;;
                        range.upperside = range.upperside + _offsetInParagraph
                                        + _offsetInFile;

                        // Add this negation terms into memory space
                        recordNegInfo(_paragraphOverallIndex,
                                Constant.NEGATED_NEGATION,
                                possible_phrase,
                                neg_start_span,
                                neg_end_span,
                                range.lowerside,
                                range.upperside);

                        flag_neg(possible_phrase,
                                _offsetInParagraph,
                                _offsetInFile,
                                _paragraphOverallIndex,
                                _length_of_previous_processsed_paragraphs);

                        i = window_end;
                        break;

                case posspre:
                        possible_phrase = currsent.substring( currsentw_flag_position[i][0], currsentw_flag_position[i][1]);
                        // pre_window(i);
                        pre_window_full(i);

                        // get effective range of this negative phrase.
                        range = getEffectiveRange(window_begin, window_end, currsent);
                        // add offset
                        range.lowerside = range.lowerside + _offsetInParagraph
                                        + _offsetInFile;;
                        range.upperside = range.upperside + _offsetInParagraph
                                        + _offsetInFile;

                        neg_start_span = currsentw_flag_position[i][0]
                                + _offsetInParagraph
                                + _offsetInFile
                                - 1;
                        neg_end_span   = currsentw_flag_position[i][1]
                                + _offsetInParagraph
                                + _offsetInFile
                                - 1;

                        // Add this negation terms into memory space
                        recordNegInfo(_paragraphOverallIndex,
                                Constant.POSSIBLE_NEGATION,
                                possible_phrase,
                                neg_start_span,
                                neg_end_span,
                                range.lowerside,
                                range.upperside);

                        flag_possible(possible_phrase, currsentw_flag_position[i][0], currsentw_flag_position[i][1],
                                _offsetInFile,
                                _paragraphOverallIndex, _length_of_previous_processsed_paragraphs);

                        i = window_end;
                        break;

                case postneg:
                        possible_phrase = currsent.substring( currsentw_flag_position[i][0], currsentw_flag_position[i][1]);
                        // post_window(i);
                        post_window_full(i);

                        // get effective range of this negative phrase.
                        range = getEffectiveRange(window_begin, window_end, currsent);
                        // add offset
                        range.lowerside = range.lowerside + _offsetInParagraph
                                        + _offsetInFile;;
                        range.upperside = range.upperside + _offsetInParagraph
                                        + _offsetInFile;

                        neg_start_span = currsentw_flag_position[i][0] + _offsetInParagraph - 1 + _offsetInFile;
                        neg_end_span   = currsentw_flag_position[i][1] + _offsetInParagraph - 1 + _offsetInFile;

                        // Add this negation terms into memory space
                        recordNegInfo(_paragraphOverallIndex,
                                Constant.NEGATED_NEGATION,
                                possible_phrase,
                                neg_start_span,
                                neg_end_span,
                                range.lowerside,
                                range.upperside);

                        flag_neg(possible_phrase, _offsetInParagraph, _offsetInFile,
                                _paragraphOverallIndex, _length_of_previous_processsed_paragraphs);

                        break;
                case posspost:
                        possible_phrase = currsent.substring( currsentw_flag_position[i][0], currsentw_flag_position[i][1]);
                        // post_window(i);
                        post_window_full(i);

                        // get effective range of this negative phrase.
                        range = getEffectiveRange(window_begin, window_end, currsent);
                        // add offset
                        range.lowerside = range.lowerside + _offsetInParagraph
                                        + _offsetInFile;;
                        range.upperside = range.upperside + _offsetInParagraph
                                        + _offsetInFile;

                        neg_start_span = currsentw_flag_position[i][0] + _offsetInParagraph - 1 + _offsetInFile;
                        neg_end_span   = currsentw_flag_position[i][1] + _offsetInParagraph - 1 + _offsetInFile;

                        // Add this negation terms into memory space
                        recordNegInfo(_paragraphOverallIndex,
                                Constant.POSSIBLE_NEGATION,
                                possible_phrase,
                                neg_start_span,
                                neg_end_span,
                                range.lowerside,
                                range.upperside);

                        //String post_possible_phrases = currsentw[i];
                        //flag_possible(post_possible_phrases);
                        flag_possible(possible_phrase, currsentw_flag_position[i][0], currsentw_flag_position[i][1],
                                _offsetInFile,
                                _paragraphOverallIndex, _length_of_previous_processsed_paragraphs);
                        break;
                default:
                        break;
            }
        }
    }


    /** obtain the effective window of this negative phrase.<p>
     * Unit is by character.<p>
     * The parameter:<p>
     * <tt>_window_begin</tt> and <tt>_window_end</tt>
     * shows the effective range of this negation with unit in words.
     * <p>
     * <tt>_currsent</tt> is string of the sentence you are handling.
     * <p>
     */
    private Table_range getEffectiveRange(int _window_begin, int _window_end,
            String _currsent){

        Table_range effectiverange = new Table_range();
        effectiverange.lowerside = 0;
        effectiverange.upperside = 0;

        String msgHead = "NegexAlgorithm.java - getEffectiveRange() - ";

        // validity check
        if(_currsent == null)
            return effectiverange;

        // validity check
        if(_currsent.length() < 1)
            return effectiverange;
        
        // validity check
        if(_window_begin > _window_end){
            String text = msgHead + "found wrong window of negation - ["
                    + _window_begin + " > " + _window_end + "]";
            log.LoggingToFile.log( Level.WARNING, text );
            logs.ShowLogs.printWarningLog(text);

            return effectiverange;
        }

        // define variables
        int windowBegin, windowEnd;

        // set the ranger cover whole of current sentence before processing
        windowBegin = 0;
        windowEnd = 0;
        int maxUpperSider = _currsent.length() - 1 ;

        // split current sentence by " "
        String[] sentw = currsentw; //_currsent.split(" ");
        int size = sentw.length;

        // validity check
        if(_window_end > (size - 1) ){
            _window_end = size - 1;
        }

        // validity check
        if((_window_begin < 0)||(_window_end < 0)){
            String text = msgHead + "found wrong window of negation - ["
                    + _window_begin + ", " + _window_end + "]";
            log.LoggingToFile.log( Level.WARNING, text );
            logs.ShowLogs.printWarningLog( text );
            return effectiverange;
        }

        // if a range in words is [x,y], that means we need to find
        // [ length of words of 0 to x -1, length of words of 0 to y ]
        // Example:
        // [word0][ ][word1][ ][word2][ ][word3][ ][word4][ ][word5][ ]
        // we ask for [2,3]
        // lower side
        if(_window_begin < 1){
            windowBegin = 0;
        }else{
            for(int i=0;i<_window_begin - 1;i++){
                windowBegin = windowBegin + sentw[i].length();
            }
            windowBegin = windowBegin + _window_begin;
        }
        //upper side
        for(int i=0;i<_window_end;i++){
            windowEnd = windowEnd + sentw[i].length();
        }
        windowEnd = windowEnd + _window_end;
        if (windowEnd > maxUpperSider)
            windowEnd = maxUpperSider;
        // value for return
        effectiverange.lowerside = windowBegin - 1;
        effectiverange.upperside = windowEnd - 1; 


        return effectiverange;
    }

    /**collect negation information into storage space, include negation
     * text, effect ranger, etc.
     * @return void
     */
    private void recordNegInfo(int _overallIndex_byParagraph, int _negationType,
            String _negationText, int _spanStart_InFile, int _spanEnd_InFile,
            int _window_begin, int _window_end){

        // show logs
        String result = "- ["+ _negationText
                    + "], located on [" + _spanStart_InFile + ", "
                    + _spanEnd_InFile + "] with effective range ["
                    + _window_begin+ ", " + _window_end +"]" ;
        log.LoggingToFile.log( Level.FINE, "== negation == " + result );
        logs.ShowLogs.printResults( "== negation == ", result );
        

        // call static method to record
        nlp.storageSpaceDraftLevel.StorageSpace.addNegation(
                _overallIndex_byParagraph,
                _negationType,
                _negationText, 
                _spanStart_InFile, // span start of the negation
                _spanEnd_InFile,   // span end of the negation
                _window_begin,// effective range - lower side
                _window_end   // Effective range - upper side
                );
    }

    private void chiefComplaintOnly() {
        boolean found_cc = false;
        int sent_begin, sent_end;
        Matcher matcher;
        String cc = "", chiefComplaint = "", rfv = "";

        matcher = Pattern.compile("CC:", Pattern.CASE_INSENSITIVE).matcher(currnote);
        found_cc = matcher.find();
        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 3);
            if (sent_end == -1)
                    sent_end = currnote.length() - 1;
            cc = currnote.substring(sent_begin, sent_end) + " ";
        }

        matcher = Pattern.compile("chief complaint", Pattern.CASE_INSENSITIVE).matcher(currnote);
        found_cc = matcher.find();

        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 16);
            if (sent_end == -1)
                    sent_end = currnote.length() - 1;
            chiefComplaint = currnote.substring(sent_begin, sent_end) + " ";
        }

        matcher = Pattern.compile("reason for visit", Pattern.CASE_INSENSITIVE).matcher(currnote);

        found_cc = matcher.find();
        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 17);
            if (sent_end == -1)
                    sent_end = currnote.length() - 1;
            // System.out.println(sent_begin + " " + sent_end);
            rfv = currnote.substring(sent_begin, sent_end) + " ";
        }

        currnote = cc + chiefComplaint + rfv;

    }


    private String replace_last_fullstop_with_space(String _currnote){
        char[] result = _currnote.toCharArray();

        //System.out.println("Current sentences = [" + String.valueOf( result, 0, result.length ) + "]");

        int pointer = _currnote.length() - 1;
        char current_char = result[ pointer ];

        // move pointer in front of all spaces in the tail
        while( current_char == ' ' ){
            current_char = result[ pointer ];
            pointer--;
        }

        // replace all fullstop at the tail with space(s)
        if(current_char == '.'){
            result[ pointer ] = ' ';
            pointer--;
            current_char = result[ pointer ];
        }

        // return the result string whose fullstops in the tail have been replaced with spaces
        return String.valueOf( result, 0, result.length );
    }



    /**enterance of negex algorithm, handle current paragraph
     * @return void
     */
    public void NegexFinder(
        // **** NEW PARAMETERS FOR DRAFT LEVEL STORAGE SPACE
        // overall index of all paragraphs in all files
        int _paragraphOverallIndex,
        // text of this paragraph
        String _textContentOfThisParagraph,
        // length of text of all processed paragraph before this paragraph
        int _lengthOfProcessedParagraph,
        // index of paragraph in current file, start from 1
        int _indexOfParagraphInThisFile,
        // flag: do we need to find concept terms?
        boolean _flag_implement_concepts_algorithm ) {
    

        // check flag before running
        if (( env.Parameters.NLPAssistant.OUTPUTCONCEPT == false)
                &&(env.Parameters.NLPAssistant.OUTPUTNEGATION == false)){
            return;
        }

        if( env.Parameters.NLPAssistant.OUTPUTCONCEPT){
            if (env.Parameters.NLPAssistant.QuickNLPEnabled)
                return;
        }
                
        //this.operateType = _operateType;
        FLAG_NEED_TO_FIND_CONCEPTS = env.Parameters.NLPAssistant.OUTPUTCONCEPT;
        //FLAG_NEED_TO_FIND_DATES = _flag_implement_dates_founder;
        //FLAG_OUTPUT_NEGATED = _flag_implement_negation_algorithm;
        //this.IS_THREE_COLUMNS = _isThreeColumnFile;

        boolean note_term_detected = false;
        String linein, cui;
        String[] line_split;

        try {
            
            // **** load negation words
            if( env.Parameters.NLPAssistant.OUTPUTNEGATION )
                negFile = new BufferedReader( new FileReader("phrases_chapman.txt"));

            negation = new negTable();
            nnegation = new negTable();
            negation.len = -1;
            nnegation.len = -1;

            // Initiate lists for negation term keywords
            if( env.Parameters.NLPAssistant.OUTPUTNEGATION )
                    getneg();

            note_term_detected = false;

            currnote = _textContentOfThisParagraph;
            currnote = currnote.replaceAll("zzzzzzzzzNEWLINEzzzzzzzzzzzzzzz", " ");

            // preprocess to texts before using method of regular expression
            currnote = Pattern.compile( "[0-9]\\.[0-9]" ).matcher( currnote ).replaceAll( "000" );
            currnote = replace_last_fullstop_with_space(currnote);


            // Split notes into sentence array where "." marks end of
            // sentence
            sentence_array = currnote.split("\\. ");
            //String[] cs = new String[1];
            //cs[0] = currnote;//.split("\\. ");
            // sentence_array = cs;

            sent_index = 0;
            //System.out.println("The length of current note sentence array is " + sentence_array.length);

            // **** loops to go over all sentences in this paragraph
            while ( sent_index < sentence_array.length ) {

                // show current sentences of this paragraph
                // we got this sentences by splitting paragraph with separator ". "
                logs.ShowLogs.printLog(" Current sentence is [" + sentence_array[sent_index] + "]");
                log.LoggingToFile.log( Level.FINER, " Current sentence is [" + sentence_array[sent_index] + "]" );

                // get length for all perivous sentences in this line.
                int length_before_currsent = 0;
                for(int lh=0; lh < sent_index; lh++ ){
                        length_before_currsent = length_before_currsent + sentence_array[lh].length();
                }
                // fix the length of previous sentences: add offset, 2, for the '.' you removed as split symble
                length_before_currsent = length_before_currsent + sent_index*2;


                // find 'date' related phrases and sent them to XML assembler
                //find_dates_phrases_and_export_to_XML( sentence_array, sent_index, length_before_currsent);

                // 1. check all phrases and marked those found concept phrases in current sentence
                // 2. inside method of flag_currsentence, program will exit before starting
                //    to dig out concept if user did not ask for that.
                flag_currsentence(" " + sentence_array[sent_index] + " ");

                // if found any concepts in current sentence
                //if( FLAG_NEED_TO_FIND_CONCEPTS ) {
                if ( found_term||(env.Parameters.NLPAssistant.OUTPUTNEGATION) ) {
                
                    
                    currsentw = currsent.trim().split(" ");
                    
                    if ( ( currsentw.length == 1 ) && ( currsentw[0].trim().length() < 1 ) ) {
                        sent_index++;
                        continue;
                        // continue to next loop if this sentences do no have any character
                    }

                    // set current sentence keyword flag
                    int i = 1, j = 0;
                    currsentw_flag = new int[currsentw.length];
                    currsentw_flag_position = new int[currsentw_flag.length][5];
                    

                    // unit in currsent is by characters, and unit in currsentw is by words
                    while (  ( i < currsent.length() ) && ( j < currsentw.length ) ) {

                        // ** flag the currsentw array for keywords **
                        if (currsent_flag[i - 1] == term_space) { //term_space = -99
                            // if phrase in position of i-1 of this sentence is a concept

                            while (currsent_flag[i] == term_space) {
                                i++;
                            }
                            while ( currsentw[j].trim().length() < 1 ) {
                                j++;
                            }

                            currsentw_flag[j] = currsent_flag[i];
                            currsentw_flag_position[j][0] = currsent_flag_position[i][0];
                            currsentw_flag_position[j][1] = currsent_flag_position[i][1];
                            currsentw_flag_position[j][2] = currsent_flag_position[i][2];
                            currsentw_flag_position[j][3] = currsent_flag_position[i][3];
                            currsentw_flag_position[j][4] = currsent_flag_position[i][4];

                            j++;
                        }

                        i++;
                    }


            negexChapman(_lengthOfProcessedParagraph, length_before_currsent, _paragraphOverallIndex, _lengthOfProcessedParagraph);


            for (i = 0; i < currsentw_flag.length; i++) {

                switch (currsentw_flag[i]) {

                    case term_found:

                        // find span of i-th word and add sentence_begin span
                        // get sentence span (the other way)
                        int sent_start = 0 ;

                        // System.out.println("currsentw_flag" + currsentw_flag[i]);
                        for (int small_j = 0; small_j < sent_index; small_j++)
                                sent_start += sentence_array[small_j].length() + 2 ;

                        int start_span = sent_start ; //sentSpans.get(sent_index) ;

                        for (int small_i = 0; small_i < i; small_i++)
                        {
                                // add length of word plus one for the space that split it
                                start_span += currsentw[small_i].length() + 1 ;
                        }
                        int end_span = start_span + currsentw[i].length() ;

                        // add span for uncounted whitespace at beginning of line
                        int toAdd = 0 ;
                        while (!sentence_array[sent_index].substring(toAdd).startsWith(sentence_array[sent_index].trim()))
                        {
                                toAdd++ ;

                        }
                        start_span += toAdd ;
                        end_span += toAdd ;

                        start_span += _lengthOfProcessedParagraph;
                        end_span += _lengthOfProcessedParagraph;

                        neg_start_span = currsentw_flag_position[i][0] + _lengthOfProcessedParagraph - 1 + length_before_currsent;
                        neg_end_span   = currsentw_flag_position[i][1] + _lengthOfProcessedParagraph - 1 + length_before_currsent;


                        nlp.storageSpaceDraftLevel.StorageSpace.addConcepts(
                                // overall index to show paragraph position in the arraylist
                                _paragraphOverallIndex,
                                // concept text
                                currsent.substring(currsentw_flag_position[i][0], currsentw_flag_position[i][1]),
                                currsentw_flag_position[i][0] + 1 + length_before_currsent,
                                currsentw_flag_position[i][1] + 1 + length_before_currsent,
                                currsentw_flag_position[i][0] + length_before_currsent+_lengthOfProcessedParagraph -1,
                                currsentw_flag_position[i][1] + length_before_currsent+_lengthOfProcessedParagraph -1,
                                currsentw_flag_position[i][2],
                                currsentw_flag_position[i][3],
                                currsentw_flag_position[i][4]
                                );

                        //Logs.ShowLogs.printErrorLog("==========================================");

                        note_term_detected = true;
                            break;
                        case term_negated:
                                note_term_detected = true;
                                break;
                        case term_possible:
                                note_term_detected = true;
                                break;
                        default:

                                break;
                    }

                }

            }
                                    //}
            sent_index++;  // count of sentences belongs to a line


            } // end loop after finishing all sentences in current line


            if (!note_term_detected) {
            //- p.println(notenum + "\t" + ptid + "\t\t\t\tno terms present\t\t\t\t");
            }

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "100 - Current note is  [" + currnote + "]");
            log.LoggingToFile.log(Level.SEVERE, "100 - Exception is " + e);
        }
    }
}
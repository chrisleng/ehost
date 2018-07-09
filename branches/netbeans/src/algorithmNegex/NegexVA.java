/*
 * NegexAlgorithm.java
 *
 * Created on June 7, 2006, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package algorithmNegex;
/**
 *
 * @author VHASLCSouthB
 */
/** *** Main File for Negex*** */


import java.io.*;
import java.util.regex.*;
import java.sql.ResultSet;

//
/*
class termTable {

    String[] word;
    int len;

    termTable() {
        word = new String[500];
        len = 0;
    }
}*/

/*
class negTable {

    String[] word; // negation phrase
    int len; // length of list
    int[] type; // negation type (100=pseudo, 101=pre, 102=poss pre,

    // 103=post, 104=poss post, 105=conj, 200=special VA)
    negTable() {
        word = new String[500];
        len = 0;
        type = new int[500];
    }
}*/

public class NegexVA {

    private String currsent,  currnote,  ptid,  notenum;
    private int[] currsent_flag;
    private BufferedReader negFile,  termFile,  inputReader;
    private FileOutputStream out;
    private PrintStream p;
    static int saindex,  cnindex;
    private String[] currsentw;
    private int[] currsentw_flag;
    //private nlp.switchpool.termTable term, nterm;
    private negTable negation,  nnegation;
    static char[] sentarray;
    private static final int pseudo = 100,  preneg = 101,  posspre = 102,  postneg = 103,  posspost = 104,  conj = 105,  vaneg = 300,  chronic = 400;
    private static final int term_found = 1,  term_negated = 2,  term_possible = 3,  term_va = 4,  term_chronic = 5,  term_space = -99,  wndsize = 8;
    private int window_begin,  window_end;
    private boolean found_term;

    // Function to read the list of negation keywords from negation.txt
    private void getneg() throws IOException {
        String line = this.negFile.readLine();
        while (line != null) {
            String[] st = line.split(",");
            negation.len++;
            nnegation.len++;
            negation.word[negation.len] = " " + st[0] + " ";
            //negation.word[negation.len] = st[0];
            negation.type[negation.len] = Integer.valueOf(st[1]).intValue();

            // replace " " in phrase with "_" to force a phrase to be 1 word
            nnegation.word[nnegation.len] = " " + negation.word[negation.len].trim().replaceAll(" ", "_").toUpperCase() + " ";
            //nnegation.word[nnegation.len] = negation.word[negation.len]
            //.replaceAll(" ", "_");

            line = negFile.readLine();
        }
        negation.len++;
        nnegation.len++;

    }

    // Function to read the list of keywords from terms.txt
    /*private void getterms() throws IOException {
        String st = termFile.readLine();
        while (st != null) {
            nterm.len++;
            term.len++;
            term.word.add( term.len, " " + st + " " );
            //term.word[term.len] =  st  ;

            // replace " " in phrase with "_" to force a phrase to be 1 word
            //nterm.word[term.len] = " " + term.word[term.len].trim().replaceAll(" ", "_").toUpperCase() + " ";
            nterm.word.add(term.len, (" " + term.word.get(term.len).trim().replaceAll(" ", "_").toUpperCase() + " ") );
            //nterm.word[term.len] = term.word[term.len].replaceAll(" ", "_");
            st = termFile.readLine();
        }

        term.len++;
        nterm.len++;
    }*/

    private void flag_keywords(String regex, int type, String replacement) {
        Matcher matcher;

        matcher = Pattern.compile(regex,
                Pattern.CASE_INSENSITIVE).matcher(currsent);
        boolean match_found = matcher.find();
        found_term = match_found;

        while (match_found) {
            // flag the keyword by noting the start position in the string
            // and its appropriate type
            int i = matcher.start() + 1;
            /*if (type == term_found) {
            System.out.println(i + replacement + " currsent is " + currsent);
            }*/
            currsent_flag[i] = type;
            match_found = matcher.find(i + 1);
        }
        currsent = matcher.replaceAll(replacement);
    }

    private void flag_keywordsNeg(String regex, int type, String replacement) {
        Matcher matcher;

        matcher = Pattern.compile(regex,
                Pattern.CASE_INSENSITIVE).matcher(currsent);
        boolean match_found = matcher.find();

        while (match_found) {
            // flag the keyword by noting the start position in the string
            // and its appropriate type
            int i = matcher.start() + 1;
            /*if (type == vaneg) {
            System.out.println(i + replacement + " currsent is " + currsent);
            }*/
            currsent_flag[i] = type;
            match_found = matcher.find(i + 1);
        }
        currsent = matcher.replaceAll(replacement);
    }

    // Compare for an imperfect match between two words
    private void flag_currsentence(String st) {
        String regex;
        boolean found = false;
        found_term = false;

        // reset sentence string and flag array
        currsent = new String(st);
        currsent_flag = new int[st.length()];

        int amount_of_concept_dictionaries = dictionaries.ConceptDictionaries.ConceptArray.size();
        dictionaries.ConceptDictionaryFormat cf;

        String PreProcessedTerm, RAWTerm;
        int length;

        for( int o=0;o<amount_of_concept_dictionaries; o++){
                cf = dictionaries.ConceptDictionaries.ConceptArray.get(o);

                length = cf.preprocessed_preannotated_Concept.size();

                // flag all concept keywords
                for (int i = 0; i < length; i++) {


                    PreProcessedTerm = cf.preprocessed_preannotated_Concept.get(i);
                    //RAWTerm = cf.original_preannotated_concept.get(i);

                    // check for term keywords (with punctuation) and flag
                    regex = "[ |[\\p{Punct}&&[^_:]]]" + PreProcessedTerm.trim() + "[ |[\\p{Punct}&&[^_:]]]";
                    flag_keywords(regex, term_found, null );

                    if (found_term) {
                        found = true;
                    }

                    // check for term keywords with : in front and flag
                    regex = " " + PreProcessedTerm.trim() + ":";
                    //flag_keywords(regex, term_found, " " + RAWTerm.trim() + ":");
                    flag_keywords(regex, term_found, null );

                    if (found_term) {
                        found = true;
                    }
                    found_term = found;
                }
        }


        if (found == true) {
                for( int o=0;o<amount_of_concept_dictionaries; o++){
                    cf = dictionaries.ConceptDictionaries.ConceptArray.get(o);
                    length = cf.preprocessed_preannotated_Concept.size();

                    for (int i = 0; i < length; i++) {
                        PreProcessedTerm = cf.preprocessed_preannotated_Concept.get(i);
                        //RAWTerm = cf.original_preannotated_concept.get(i);


                        regex = " " + PreProcessedTerm.trim() + ": no ";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_no ");


                        regex = " " + PreProcessedTerm.trim() + ": none/denies";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_none_denies");
                        //System.out.println(regex + " " + currsent);

                        regex = " " + PreProcessedTerm.trim() + ": none ";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_none ");
                        //System.out.println(currsent);

                        regex = " " + PreProcessedTerm.trim() + ": 0 ";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_0 ");

                        regex = " " + PreProcessedTerm.trim() + ": N/A ";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_N/A ");
                        //System.out.println(regex + " " + currsent);

                        regex = " " + PreProcessedTerm.trim() + ": NA ";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "_NA ");

                        regex = " " + PreProcessedTerm.trim() + " assessment:";
                        RAWTerm = regex;
                        flag_keywordsNeg(regex, term_va, RAWTerm + "assessment:");

                    }
                }

            // flag all negation keywords
            for (int i = 0; i < negation.len; i++) {

                // check for negation keywords (with punctuation) and flag
                regex = "[ |[\\p{Punct}&&[^_]]]" + negation.word[i].trim() + "[[\\p{Punct}&&[^_]]| ]";
                flag_keywordsNeg(regex, negation.type[i], nnegation.word[i]  );

                // check for special negation [ ]  ( )  (n)  (-)
                regex = " \\[ \\] ";
                flag_keywordsNeg(regex, vaneg, " [_] ");
                regex = " \\( \\) ";
                flag_keywordsNeg(regex, vaneg, " (_) ");
                regex = " \\(\\-\\) ";
                flag_keywordsNeg(regex, vaneg, " (-) ");
                regex = " \\(\\n\\) ";
                flag_keywordsNeg(regex, vaneg, " (n) ");
            }

        }


        // flag the space in a sentence to note the position of each word
        int i = 0;
        //System.out.println(currsent);
        while (i < currsent.length() & i != -1) {
            currsent_flag[i] = term_space;
            i = currsent.indexOf(" ", i + 1);
        }

    }

    // change the word to its original form
    private String changeback(String t) {
        //System.out.println(t);
        t = t.replaceAll("_", " ").trim();
        t = t.replaceAll(":", " ").trim();
        //System.out.println(t);
        return t;
    }

    // Obtain the CUI for the given keyword
    private String getcui(String kw) {
        String cui = "";
        try {
            ResultSet rs = CuiData.display(kw);
            rs.next();
            if (rs == null) {
                System.out.println("rs is null");
            }
            if (rs != null) {
                //System.out.println("rs is not null");
                cui = rs.getString("CUI");
            //System.out.println(cui);
            }

            if ((cui == null) || (cui.equals(""))) {
                cui = "no cui found";
            }
        } catch (Exception e) {
            System.out.println("Exception " + e + " occured");
        }
        //System.out.println("Cui found is " + cui);
        return cui;
    }


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
                //System.out.println("window end " + window_end);
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
                //System.out.println("window end " + window_end);
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
    private void flag_neg(String negation) {
        String cui, original;
        int termchk;
        for (int j = window_begin; j <= window_end; j++) {
            termchk = currsentw_flag[j];
//System.out.println("The term " + currsentw[j] + " has type " + currsentw_flag[j]);
            if (termchk == term_found) {
                //System.out.println("The term " + currsentw[j] + " which is at position " + j + " is negated");
                //System.out.println("Current sentence: " + currsent);
                original = changeback(currsentw[j]);
                cui = getcui(changeback(original));
                p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + original + "\t" + "negated" + "\t" + negation + "\t" + currsent.trim());
                currsentw_flag[j] = term_negated;
            }
        }

    }

    // Flag keywords inside window as possible
    private void flag_possible(String negation) {
        String cui, original;
        int termchk;
        for (int j = window_begin; j <= window_end; j++) {
            termchk = currsentw_flag[j];
            if (termchk == term_found) {
                original = changeback(currsentw[j]);
                cui = getcui(changeback(original));
                p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + original + "\t" + "possible" + "\t" + negation + "\t" + currsent.trim());
                currsentw_flag[j] = term_possible;
            }
        }
    }

    // Flag keywords inside window as possible
    private void flag_chronic(String negation) {
        String cui, original;
        int termchk;
        for (int j = window_begin; j <= window_end; j++) {
            termchk = currsentw_flag[j];
            if (termchk == term_found) {
                original = changeback(currsentw[j]);
                cui = getcui(changeback(original));
                p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + original + "\t" +
                        "non-acute" + "\t" + negation + "\t" + currsent.trim());
                currsentw_flag[j] = term_chronic;
            }
        }
    }

    // The main NegexAlgorithm function
    private void negexVA() {

        window_begin = 0;
        window_end = 0;

        for (int i = 0; i < currsentw.length; i++) {
            //if (currsentw_flag[i] > 0)
            //System.out.println("Current negation flag is " + currsentw[i] + " " + currsentw_flag[i]);
            switch (currsentw_flag[i]) {

                case preneg:
                    //pre_window(i);
                    pre_window_full(i);
                    flag_neg(currsentw[i]);
                    //System.out.println("Current negation flag is " + currsentw[i]);
                    i = window_end;
                    break;

                case posspre:
                    //pre_window(i);
                    pre_window_full(i);
                    flag_possible(currsentw[i]);
                    i = window_end;
                    break;

                case postneg:
                    //post_window(i);
                    post_window_full(i);
                    flag_neg(currsentw[i]);
                    break;

                case posspost:
                    //post_window(i);
                    post_window_full(i);
                    flag_possible(currsentw[i]);
                    break;

                case vaneg:
                    pre_window_full(i);
                    flag_possible(currsentw[i]);
                    i = window_end;
                    break;

                case chronic:
                    pre_window_full(i);
                    flag_chronic(currsentw[i]);
                    i = window_end;
                    break;
                default:
                    break;
            }
        }
    }

    private void chiefComplaintOnly() {
        boolean found_cc = false;
        int sent_begin, sent_end;
        Matcher matcher;
        String cc = "", chiefComplaint = "", rfv = "";

        matcher = Pattern.compile("CC:",
                Pattern.CASE_INSENSITIVE).matcher(currnote);
        found_cc = matcher.find();
        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 3);
            if (sent_end == -1) {
                sent_end = currnote.length() - 1;
            }
            cc = currnote.substring(sent_begin, sent_end) + " ";
        }


        matcher = Pattern.compile("chief complaint",
                Pattern.CASE_INSENSITIVE).matcher(currnote);
        found_cc = matcher.find();
        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 16);
            if (sent_end == -1) {
                sent_end = currnote.length() - 1;
            }
            chiefComplaint = currnote.substring(sent_begin, sent_end) + " ";
        }


        matcher = Pattern.compile("reason for visit",
                Pattern.CASE_INSENSITIVE).matcher(currnote);
        found_cc = matcher.find();
        if (found_cc) {
            sent_begin = matcher.start();
            sent_end = currnote.indexOf(":", sent_begin + 17);
            if (sent_end == -1) {
                sent_end = currnote.length() - 1;
            }
            System.out.println(sent_begin + " " + sent_end);
            rfv = currnote.substring(sent_begin, sent_end) + " ";
        }

        currnote = cc + chiefComplaint + rfv;

    }

    // Method for negexalgorithm
    public NegexVA(String inputFile) {
        boolean note_term_detected = false;
        String linein, cui;
        String[] line_split;

        try {

            String newst = "output_VA.txt";

            // Define input/output files
            negFile = new BufferedReader(
                    new FileReader("phrases_VA.txt"));
            termFile = new BufferedReader(new FileReader("phrases_term.txt"));
            inputReader = new BufferedReader(new FileReader(inputFile));
            out = new FileOutputStream(newst);
            p = new PrintStream(out);

            //term = new nlp.switchpool.termTable();
            //nterm = new termTable();
            negation = new negTable();
            nnegation = new negTable();

            //term.len = -1;
            //nterm.len = -1;

            //if(Dictionaries.ConceptDictionaries.amount_of_valid_preannotated_concepts == 0)
            //    Dictionaries.ConceptDictionaries.loadPreAnnotatedConcepts();

            negation.len = -1;
            nnegation.len = -1;

            // Initiate lists for negation term keywords
            getneg();
            //getterms();


            // loop to read over notes and process negex algorithm

            while ((linein = inputReader.readLine()) != null) {
                note_term_detected = false;
                // input file has format: notenum, ptid, currnote
                line_split = linein.split(" ", 3);
                if (line_split.length != 3) {
                    continue;
                } // skip to next loop if not all 3 items found


                currnote = line_split[2];
                if ((currnote == null) || (currnote.equals(""))) {
                    continue;
                } // skip to next loop if note is null
                ptid = line_split[1];
                notenum = line_split[0];
                //if (!notenum.equalsIgnoreCase("196")) continue;

                System.out.println("reached " + notenum);

                // Replace period following a digit to be 0
                // so it is not interpreted as end of sentence
                currnote = Pattern.compile("[0-9]\\.").matcher(currnote).replaceAll("0");

                // If looking for chief complaint only, uncomment this section
                //chiefComplaintOnly();
                //if (currnote.equals("")) {
                //    p.println(notenum + "\t" + ptid + "\t\t no CC present");
                //    continue;
                //}

                // Split notes into sentence array where "." marks end of sentence
                String[] sentence_array = currnote.split("\\. ");
                int sent_index = 0;
                //System.out.println("The length of current note sentence array is " + sentence_array.length);
                // loop to read over currnote
                while (sent_index < sentence_array.length) {
                    //System.out.println("The current sentence is " + sentence_array[sent_index]);
                    flag_currsentence(" " + sentence_array[sent_index] + " ");

                    currsentw = currsent.trim().split(" ");
                    if (currsentw.length == 1 & currsentw[0].equals("")) {
                        sent_index++;
                        continue;
                    }

                    currsentw_flag = new int[currsentw.length];
                    // set current sentence keyword flag
                    int i = 1;
                    int j = 0;
                    /*System.out.println(currsent_flag.length + " "+ currsentw.length);
                    for (int k=0; k < currsentw.length; k++){
                    System.out.println(k + " " + currsentw[k]);
                    }
                    System.out.println(currsent);*/
                    while (i < currsent.length() & j < currsentw.length) {

                        // flag the currsentw array for keywords
                        //System.out.println("The current flag position j i flag[j] is " + j +" "+ i + " " + currsent_flag[i] + " " + currsentw[j]);
                        if (currsent_flag[i - 1] == term_space) {
                            while (currsent_flag[i] == term_space) {
                                i++;
                            }
                            while (currsentw[j].equals("")) {
                                j++;
                            }

                            //System.out.println("The current flag[i] j i word is " + currsent_flag[i] +" "+ j + " " + i + " " + currsentw[j]);
                            //System.out.println("The current flag position j is " + j +" "+ currsentw.length);
                            currsentw_flag[j] = currsent_flag[i];
                            j++;
                        }
                        //System.out.println("The current flag position i is " + i + " " + currsent_flag[i]);
                        i++;
                    }

                    if (found_term) {
                        negexVA();

                        // System.out.println("printing sent array index is " + sent_index + "\n");

                        for (i = 0; i < currsentw_flag.length; i++) {
                            switch (currsentw_flag[i]) {
                                case term_found:
                                    cui = getcui(changeback(currsentw[i]));
                                    p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + currsentw[i] + "\t" + "found" + "\t\t" + currsent.trim());
                                    note_term_detected = true;
                                    break;
                                case term_negated:
                                    //cui = getcui(changeback(currsentw[i]));
                                    //p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + currsentw[i] + "\t" + "negated" + "\t" + currsent.trim());
                                    note_term_detected = true;
                                    break;
                                case term_possible:
                                    //cui = getcui(changeback(currsentw[i]));
                                    //p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + currsentw[i] + "\t" + "possible" + "\t" + currsent.trim());
                                    note_term_detected = true;
                                    break;
                                case term_va:
                                    cui = getcui(changeback(currsentw[i]));
                                    p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + currsentw[i] + "\t" + "removed" + "\t" + currsentw[i + 1] + "\t" + currsent.trim());
                                    note_term_detected = true;
                                    break;
                                case term_chronic:
                                    //cui = getcui(changeback(currsentw[i]));
                                    //p.println(notenum + "\t" + ptid + "\t" + cui + "\t" + currsentw[i] + "\t" + "removed" + "\t" + currsentw[i + 1] + "\t" + currsent.trim());
                                    note_term_detected = true;
                                    break;
                                default:

                                    break;
                            }

                        }
                    }

                    //p.println(notenum + "\t" + ptid + "\t\t no terms present");

                    sent_index++;
                }

                if (!note_term_detected) {
                    p.println(notenum + "\t" + ptid + "\t\t no terms present");
                }
            }
            inputReader.close();
            // fr.close();
            negFile.close();
            termFile.close();
            p.close();
        } catch (Exception e) {
            System.out.println("current note is  " + currnote);
// System.out.println("curr note length is " + currnote.length());
            System.out.println("Exception is " + e);
            e.printStackTrace();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package preAnnotation.pairs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class Dictionary {

    public final static String PAIR_DICTIONARY = "PronounDictionary-Leng.txt";

    /**space to store loaded pre-annotated terms for pairs checking;*/
    private static Vector<Entry> dictionary = new Vector<Entry>();


    public String getSubType(String annotationText){
        
        String to_return = null;

        if(annotationText==null)
            return null;

        if(annotationText.trim().length()<1)
            return null;

        if(( dictionary ==null )||(dictionary.size()<1))
            return null;

        for(Entry entry : dictionary){

            if((entry==null)||(entry.term==null)||(entry.subtype==null))
                continue;

            if(entry.term.trim().toLowerCase().compareTo(annotationText.trim().toLowerCase())==0)
                return entry.subtype;
        }

        return to_return;
    }
    /**load pre-annotated terms from the dictionary.*/
    public void loadDictionary(){
        
        log.LoggingToFile.log(Level.INFO, "[Pairs Dictionary] Start loading entries into memory.");

        // clear storage space before loading entry from dictionary
        clear();

        // load entries
        try{
            File dict = new File(PAIR_DICTIONARY);
            if((dict==null)||(!dict.exists())){
                log.LoggingToFile.log(Level.SEVERE, "error 1101260524:: could not get access to " +
                        "the dictionary for pre-annotated terms of pairs;");
                return;
            }

            BufferedReader dictbuff = new BufferedReader(new FileReader(dict));
            String line = dictbuff.readLine();

            while (line != null) {
                // System.out.println("current line is {"+line+"}");
                // extract structured term information from current line
                extractEntry(line);
                // read next line
                line = dictbuff.readLine();
            }
            
            dictbuff.close();
            
            // log: show how many entries have been loaded into memory
            log.LoggingToFile.log(Level.INFO, "[Pairs Dictionary] There are "
                    + size()
                    + " have been loaded into memory.");


        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "error " + ex.getMessage() );
        }
    }

    /**get the pre-annoated term from current line in the dictionary; and
     * divide it into severl pieces to fit the structure of class "entry";
     */
    private void extractEntry(String line){
        if((line==null)||(line.trim().length()<1))
            return;

        /**lines start with character '*' means this line is for comments*/
        if(line.charAt(0)=='*')
            return;

        line = line.replaceAll("[\t]+", "\t");
        String[] words = line.split("\t");
        if(words==null)
            return;

        if(words.length<2)
            return;

        // print out all entries imported from the dictionary
        //for(String s:words){
        //    System.out.println("["+s+"]");
        //}

        if(words.length==3){
            extractStructuredData(words);
        }
        else if(words.length==2){
            extractStructuredData2(words);
        }

    }

    private void extractStructuredData2(String[] words){
        for(int i=0;i<words.length;i++){
            if(words[i]==null)
                return;
            if(words[i].trim().length()<1)
                return;
        }

        String term = words[0].trim();
        String type = words[1].trim();
        String subtype = null;

        addEntry(new Entry(term, type, subtype));

    }

    private void extractStructuredData(String[] words){
        for(int i=0;i<words.length;i++){
            if(words[i]==null)
                return;
            if(words[i].trim().length()<1)
                return;
        }

        String term = words[0].trim();
        String type = words[1].trim();
        String subtype = words[2].trim();

        addEntry(new Entry(term, type, subtype));

    }

    private void addEntry(Entry entry)
    {
        if(entry==null)
            return;
        
        dictionary.add(entry);
    }

    public void close()
    {
        clear();
    }

    public void clear()
    {
        dictionary.clear();
    }



    public int size() {
        if(dictionary==null)        
            return 0;       

        return dictionary.size();
    }

    public Entry get(int i)
    {
        if((i<0)||(i>=size()))
            return null;

        return dictionary.get(i);
    }

}



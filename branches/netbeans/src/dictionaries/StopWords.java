/*****************************************************************
 * @author       Jianwei 'Chris' Leng
 * @created      Feb 2, 2010, 3:06 PM
 * @Organization University of Utah, Veteran Affairs Salt Lake City
 * @purpose:     
 *****************************************************************/
package dictionaries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class StopWords {
        public static ArrayList<String> STOPWORDs = new ArrayList<String>();
        public static int SIZE_OF_INVALID_CONCEPT_LIB = 0;
        private final static String libFILE = "phrases_invalid.txt";

        /**return amount of stop words in memory*/
        public static int size(){
            return STOPWORDs.size();
        }

        public static void LoadingStopWords(){
            if(env.Parameters.nlp_dictionary_proc.using_StopWords==false)
                return;
            
            try{
                        // empty these arraies before using
                        STOPWORDs.clear();
                        log.LoggingToFile.log(Level.INFO,"[eHOST INFO] Loading invalid concept lib ......");
                        // preannotatedConcepts lib file, nlp.switchpool.iniMSP.CONCEPT_LIB,
                        // is a 2 columns txt file, splitted by specific separator
                        
                        BufferedReader Lib = new BufferedReader( new FileReader( libFILE ));
                        
                        String st = Lib.readLine();
                                                                       
                        // go over the directionary and load entires into array
                        //String regex;
                        while  ( st != null ) {
                                //System.out.println("---" + st);
                                if ( st.trim().length() < 1 ){
                                        st = Lib.readLine();
                                        continue;
                                }

                                SIZE_OF_INVALID_CONCEPT_LIB++;
                                STOPWORDs.add( st );

                                st = Lib.readLine();
                        }
                        log.LoggingToFile.log(Level.INFO,"[eHOST INFO] Loaded valid concepts from file " + libFILE + " successfully");

                }catch(Exception e){
                        log.LoggingToFile.log(Level.SEVERE,"can not read invalid concepts from file: " + libFILE );
                        log.LoggingToFile.log(Level.SEVERE,e.getMessage() );
                        System.exit(99);
                }
        }
}
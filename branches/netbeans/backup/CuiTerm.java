/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negex_fob;
/**
 *
 * @author Jianwei Leng ( Chris )
 * @location: Williams Building, Level 1, Epidemiology Department
 * @history: Monday 11-16-2009  10:32 pm MST, First_Created_Time
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;


public class CuiTerm {
        public static Hashtable<String,String> CUITERMMemory;
        private final static String txtFileName_cuiterm = "phrases_term.txt";

        static { CUITERMMemory = new  Hashtable<String, String>(10, (float)0.8);  }
        static { getCuiTermTableinMemory(); }
        CuiCodeTable Cuicode = new CuiCodeTable();

        // check a pointed file existed or not
        private static boolean fileExist( String _filename ) {
                try{
                        File file = new File( _filename );
                        if ( file.exists() ) {
                                return true;
                        } else { return false; }
                }catch(Exception e) {
                        return false;
                }
        }

        private static void getCuiTermTableinMemory(){


                // ---------------------------- //
                // check file existed or not
                try{
                        if ( fileExist( txtFileName_cuiterm ) == false ) {
                                System.out.println("================================================================");
                                System.out.println("[CuiCode.java ] - CAN NOT open file " + txtFileName_cuiterm );
                                System.out.println("================================================================");
                                Exception e = new Exception();
                                throw e;
                        }
                }catch(Exception e){
                        // System.err.println( e );
                }

                // ---------------------------- //
                // try to open file and read cuiname-announcement table data
                // this file, should be a two-columns text file, divided by comma
                try{
                        String lineIn;
                        BufferedReader cuiANFileReader;
                        cuiANFileReader = new BufferedReader( new FileReader( txtFileName_cuiterm ));
                        String switches;

                        // read all line form the file
                        while( (lineIn = cuiANFileReader.readLine()) != null ){

                                // if this line started with '//' in the front, it's comment
                                if  ( (lineIn.charAt(0) == '/') && (lineIn.charAt(1) == '/') )
                                        continue;

                                String[] columns = lineIn.split("<--->", 2);
                                if (( columns[0] != null ) && (columns[1]) != null ) {
                                        // print out all CUI code - cui announce name on the screen
                                        //System.out.println("[" + columns[0] + "] --- [" + columns[1].trim() + "]");

                                        // if columns are not empty
                                        if (( columns[0].trim().length() > 1 ) && ( columns[1].trim().length() > 1 )){
                                                switches = columns[0].trim().replaceAll(" ", "_").toLowerCase();
                                                //System.out.println("[" + columns[0].trim() + "] --- [" + columns[1].trim() + "]");
                                                //System.out.println("[" + switches.toLowerCase() + "] --- [" + (String)columns[1].trim() + "]");
                                                CUITERMMemory.put( switches.toLowerCase(), (String)columns[1].trim());
                                        }
                                }
                        }



                }catch(Exception e){
                        System.out.println("[CuiUtilities.java] - reading cuiname-announcement table data from file " + txtFileName_cuiterm );
                        System.out.println("[CuiUtilities.java] - please check the file: " + txtFileName_cuiterm );
                        System.err.println(e);
                }
        }

}

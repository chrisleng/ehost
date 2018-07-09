/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.algorithm.QNLP;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author Chris Leng
 */
public class DictionaryDepot {
    protected final static String dictionaryFileName = "eHOST.idict";
    protected static Vector<DictionaryFormat> dictionary = new Vector<DictionaryFormat>();

    public static void clear(){
        dictionary.clear();
    }

    public static int size(){
        return dictionary.size();
    }
    public static DictionaryFormat get(int index){
        if((index<0)||(index>(dictionary.size()-1)))
            return null;

        return dictionary.get(index);

    }

    public static void load(){
        clear();
        String logstr = "\nStart loading indexed dictionary >>>>\n";
        log.LoggingToFile.log(Level.INFO, logstr );
        logs.ShowLogs.LogPrint(logstr, Color.blue);
        try {
            BufferedReader thisDictionary = new BufferedReader(new FileReader(dictionaryFileName));
            logstr = "Loading pre-annotated concept dictionary ......";
            log.LoggingToFile.log(Level.INFO, logstr );
            logs.ShowLogs.printImportantInfoLog( logstr );
            logstr = "each point means 1000 entries(80K/line):\n";
            log.LoggingToFile.log(Level.INFO, logstr );
            logs.ShowLogs.printImportantInfoLog( logstr );

            String st = thisDictionary.readLine();

            String[] sts; 
            String regex, category; 
            //boolean validity;
            int count = 0;

            while (st != null) {

                if(env.Parameters.NLPAssistant.STOPSign)
                    return;

                count++;
                if(count%1000 == 0)
                {
                    logs.ShowLogs.LogPrint(".", Color.blue);
                }
                    //System.out.print('.');
                if(count%80000 == 0)
                {
                    logstr = " - (" + String.valueOf(count) + ") \n";
                    logs.ShowLogs.LogPrint( logstr, Color.blue);
                    log.LoggingToFile.log(Level.FINE, logstr );
                }

                // split lines to 2 strings,
                // the concept and the comment(announced name of concept)
                sts = st.split( "<---->" );
                

                if ( sts.length == 2 ) {
                    if(( sts[0] == null )||(sts[0].trim().length() < 1)) {
                        dictionary.add(new DictionaryFormat(null, null) );
                        st = thisDictionary.readLine();
                        continue;
                    }
                    if(( sts[1] == null )||(sts[1].trim().length() < 1)) {
                        dictionary.add(new DictionaryFormat(null, null) );
                        st = thisDictionary.readLine();
                        continue;
                    }
                    regex = sts[0].trim();
                    category = sts[1].trim();


                    regex = regex.replaceAll("\\[", "\\\\[");
                    regex = regex.replaceAll("\\]", "\\\\]");
                    regex = regex.replaceAll("\\,", "\\\\,");
                    regex = regex.replaceAll("\\:", "\\\\:");
                    regex = regex.replaceAll("\\.", "\\\\.");
                    regex = regex.replaceAll("\\-", "\\\\-");
                    regex = regex.replaceAll("\\=", "\\\\=");
                    regex = regex.replaceAll("\\*", "\\\\*");
                    regex = regex.replaceAll("\\@", "\\\\@");
                    regex = regex.replaceAll("\\&", "\\\\&");
                    regex = regex.replaceAll("\\+", "\\\\+");
                    regex = regex.replaceAll("\\%", "\\\\%");
                    //regex = regex.replaceAll(" ", " " );
                    //regex = "[ |\\:|[\\p{Punct}&&[^_]]]" + regex + "[ |\\:|[\\p{Punct}&&[^_]]]";
                    regex = "[ |\\:|\\,|\\.]" + regex + "[ |\\:|\\,|\\.]";

                    

                    dictionary.add(new DictionaryFormat(regex, category) );
                    
                }else{
                    String errorinfo = "Error 1009060031:: wrong dictionary entry format!!! ";
                    logs.ShowLogs.printWarningLog( errorinfo );
                    log.LoggingToFile.log(Level.SEVERE, errorinfo);
                }

                st = thisDictionary.readLine();
            }

            thisDictionary.close();
        } catch (Exception ex) {
            String errorinfo = "Error 1009060027::" + ex.toString();
            log.LoggingToFile.log(Level.SEVERE, errorinfo);
            logs.ShowLogs.printImportantInfoLog( errorinfo );
        }
    }
    
}

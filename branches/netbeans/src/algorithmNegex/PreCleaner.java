/* PreCleaner.java
 * This program performs general normalization tasks such as removing 
 * unnecessary whitespace and changing all characters to lowercase.
 */

package algorithmNegex;


import java.io.*;
import java.util.regex.*;

public class PreCleaner {

    private PreCleaner() {}
    //	Preprocessing the given note text
    public static String preCleaning(String temp) {
        
        Pattern p;
        
        // replace all extra spaces with 1 space
        p = Pattern.compile(" {2,}");
        temp = p.matcher(temp).replaceAll(" ");
        
        // remove spaces within []
         p = Pattern.compile("\\[ +\\]");
         temp = p.matcher(temp).replaceAll(". [] ");
         
         // change to lower case
         temp = temp.toLowerCase();
        
        return temp;
    }
}

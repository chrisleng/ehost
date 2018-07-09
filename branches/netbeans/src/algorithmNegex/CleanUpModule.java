/*
 * clenaup.java
 *
 * Created on January 18, 2008
 * Last Modified on January 18, 2008
 * By Shuying Shen and Brett South
 */
package algorithmNegex;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.regex.*;

public class CleanUpModule {

//	Preprocessing the given note text
    private String normalize(String temp) {

        Pattern p;

        // add spaces after ] )and :
        p = Pattern.compile("\\]");
        temp = p.matcher(temp).replaceAll("] ");
        
        p = Pattern.compile("\\)");
        temp = p.matcher(temp).replaceAll(") ");
        
        p = Pattern.compile(":");
        temp = p.matcher(temp).replaceAll(": ");

        // replace all "," and extra spaces with 1 space
        p = Pattern.compile(" +");
        temp = p.matcher(temp).replaceAll(" ");

        return temp;
    }

    public CleanUpModule(String outputName) {

        FileOutputStream output;
        PrintStream p;
        ResultSet r;
        String currnote, ptid, notenum;

        try {

            output = new FileOutputStream(outputName);
            p = new PrintStream(output);

            String nno = "1874";
            //r = input.display(nno);
            r = new Data().display(nno);
            //int i = 0;
            while (r.next()) {

                currnote = "";
                currnote = r.getString("NOTETEXT");
                if ((currnote == null) || (currnote.equals(""))) {
                    continue;
                }
                ptid = r.getString("rnumber");
                notenum = r.getString("nnum");
                System.out.println("cleaning up " + notenum);
                //p.println(notenum+" "+ptid+" " + currnote +"\r\n");
                //currnote = normalize(currnote);
                p.println(notenum + " " + ptid + " " + currnote.replaceAll("\n", "zzzzzzzzzNEWLINEzzzzzzzzzzzzzzz"));
                //currnote. normalize(currnote);


            }
            r.close();
            p.close();
        } catch (Exception e) {
            log.LoggingToFile.log(Level.SEVERE, e + "Couldn't open file");

        }

    }
}




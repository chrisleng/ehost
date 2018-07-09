/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.algorithm.QNLP;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author Chris Leng, 2010 Sep 5
 */
public class RouteMapDepot {

    public static Hashtable routemap = new Hashtable();

    /**Clear data of route map for quick concept extraction algorithm.*/
    public static void clear(){
        routemap.clear();
    }

    /**
     * Return positions of potential pre-annotated concepts in the dictionary
     * to a designated uterm.
     */
    public static Vector<Integer> getPosition(String uterm){
        try{
            if ((uterm == null)||(uterm.trim().length() < 1))
                return null;
            return (Vector<Integer>) routemap.get(uterm);
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "Error 1009060001 : " + ex.toString());
            return null;
        }
    }

    /**
     * Return amount of entreis in route map.
     */
    public static int size(){
        return routemap.size();
    }

    /**Load data of route map for quick concept extraction algorithm.*/
    public static void load(){
        String filename = "eHOST.map";
        readRouteMapFile( filename );
    }

    /**
     * Read data from designate routemap file and extract them into memory
     * of route map.
     *
     * @param   filename
     *          The file name without absolutely path of the route map data file.
     */
    private static void readRouteMapFile(String filename){
        log.LoggingToFile.log(Level.INFO, "\nStart loading route map >>>>\n" );
        logs.ShowLogs.LogPrint("\nStart loading route map >>>>\n", Color.BLACK);

        BufferedReader routeMapFile;
        int count = 0;
        try {
            routeMapFile = new BufferedReader(new FileReader(filename));


            // ##1## read each line from the route map file
            String line = routeMapFile.readLine();
            String uterm = null, interval = null;
            while (line != null) {

                if(env.Parameters.NLPAssistant.STOPSign)
                    return;

                // processing indicator
                count++;
                if( count%1000 == 0){
                    logs.ShowLogs.LogPrint(".", Color.blue);
                    System.out.print(".");
                }
                if( count%80000 == 0) {
                    logs.ShowLogs.LogPrint( " - (" + String.valueOf(count) + ") \n", Color.blue);
                    log.LoggingToFile.log(Level.FINE, count + " loaded" );
                    System.out.print( ".\n");
                }


                // ##2.1## begin new uterm and its interval
                if (line.trim().length() < 1){

                    // save old uterm and interval info
                    //saveRouteMapEntry( uterm, interval );
                    
                    uterm = null; interval = null;

                    line = routeMapFile.readLine();
                    continue;
                }

                // ##2.2## continue to process current record
                if( uterm == null ) {

                    // ##2.2.1## new uterm found
                    uterm = line.trim();
                    line = routeMapFile.readLine();
                    continue;

                } else {

                   
                        interval = line;
                        saveRouteMapEntry( uterm, interval );
                        line = routeMapFile.readLine();
                        continue;
                }
                //line = routeMapFile.readLine();
            }
            logs.ShowLogs.LogPrint("\n"+count+" entries imported.\n", Color.BLACK);
            log.LoggingToFile.log(Level.INFO, count+" entries imported." );
            routeMapFile.close();

        } catch (Exception ex) {
            logs.ShowLogs.printErrorLog( "Error 1009051513: while try to open " +
                    "dictionary route map file: Details: " + ex.toString() );
            
            log.LoggingToFile.log(Level.SEVERE, "Error 1009051513: while try to open " +
                    "dictionary route map file: Details: " + ex.toString() );
        }
    }

    private static void saveRouteMapEntry(String uterm, String points_sentence){
        if ( (uterm == null) || ( points_sentence == null ))
            return;
        if ( points_sentence.trim().length() < 1 )
            return;
        if ( uterm.trim().length() < 1 )
            return;

        String[] points = points_sentence.split(",");

        if ( points.length <= 0 )
            return;


        for( int i=0; i<points.length; i++ ){
            try{
                String number_str = points[i];
                if (( number_str == null )||(number_str.trim().length() < 1))
                    continue;
                int position = Integer.valueOf( number_str.trim() );

                saveEntry( uterm.trim(), position );

            }catch(Exception ex){
                // error for transfer from str to number_str
                log.LoggingToFile.log(Level.SEVERE,"Error 1009051736: convert str to numbers: "
                        + ex.toString());
            }
        }

    }

    private static void saveEntry(String uterm, int position ){
        if (( uterm == null ) ||( uterm.trim().length() < 1))
                return;
        
        if ( existed( uterm ) ){
            updateExisted( uterm, position );
        }else{
            addNew( uterm, position );
        }
    }

    private static void updateExisted(String uterm, int position){
        //System.out.println("hello");
        Vector<Integer> positions = (Vector<Integer>) routemap.get(uterm);
        positions.add(position);

        //routemap.remove(uterm);
        //routemap.put(uterm, positions);
    }

    private static void addNew(String uterm, int position){
        Vector<Integer> positions = new Vector<Integer>();
        positions.add(position);
        routemap.put( uterm, positions );
    }

    //private void add(String term, int start, int end){
    //    routemap.put(term, new Interval(start, end));
    //}
    private static boolean existed(String uterm){
        if ( routemap == null )
            return false;
        if ( uterm == null )
            return true;

        if( routemap.containsKey(uterm) )
            return true;
        else
            return false;
    }
}

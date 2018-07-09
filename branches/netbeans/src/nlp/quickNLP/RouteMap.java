/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nlp.quickNLP;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author Chris
 */
public class RouteMap {
    protected Hashtable routemap = new Hashtable();
    protected Vector<Concept> concepts;
    protected final int KEYLENGTH = 3;
    protected InitDlg initdlg;

    public RouteMap(final Vector<Concept> concepts, InitDlg initdlg){
        this.concepts = concepts;
        this.initdlg = initdlg;

        buildRouteMap();
    }

    private void buildRouteMap(){
        routemap.clear();

        if( concepts == null )
            return;

        int size = concepts.size();

        int intervalstart=0, intervalend=0; String term = null, uterm = null;
        for(int i=0; i<size;i++){
            String currentterm = concepts.get(i).term;

            if(i%1000 == 0)
                initdlg.showInfo_noenter(".");
                        //System.out.print('.');
            if(i%80000 == 0)
                initdlg.showInfo_noenter( ".\n");

            // generate route map for unsorted dictionary
            if ( currentterm != null ) {
                String ucurrentterm = getTermUnique( currentterm );
                record( ucurrentterm, i );
            }

            /* this is function used to generate route map for sorted dictionary
             // if start with a new term
            if( (uterm==null) || (!uterm.equals(ucurrentterm)) ){
                // save results if not located on first row of the arraylist
                if( uterm != null ){
                    add(uterm, intervalstart, intervalend);
                    System.out.println(uterm + " -- " + intervalstart + ", " +  intervalend );
                }

                // start new
                intervalstart = i;
                intervalend = i;
                uterm = ucurrentterm;
            }
            else
            // if still in the interval of same terms as previous row
            {
                intervalend = i;
            }*/
        }
    }

    
    private String getTermUnique(String originalterm){
        String termUnique = originalterm;
        if (originalterm.length() > this.KEYLENGTH )
            termUnique = originalterm.substring(0, this.KEYLENGTH);

        char[] chars = termUnique.toCharArray();
        for( int i=0;i<chars.length; i++ ) {
            if ( chars[i] == ' ' )
                termUnique = originalterm.substring(0, i);
        }

        //System.out.println("["+originalterm+"] -> [" + termUnique + "]");
        return termUnique;
    }


    private void record(String uterm, int position){
        //System.out.println("["+uterm+"]" + " position = " + position);
        // existed uterm
        if( existed(uterm) ){
            //System.out.println("    - existed");

            updateExisted( uterm, position );
        }else
        // new uterm
        {
            //System.out.println("    - new");
            addNew( uterm, position );
        }
    }

    private void updateExisted(String uterm, int position){
        //System.out.println("hello");
        Vector<Integer> positions = (Vector<Integer>) routemap.get(uterm);
        positions.add(position);

        //routemap.remove(uterm);
        //routemap.put(uterm, positions);
    }

    private void addNew(String uterm, int position){
        Vector<Integer> positions = new Vector<Integer>();
        positions.add(position);
        routemap.put( uterm, positions );
    }

    //private void add(String term, int start, int end){
    //    routemap.put(term, new Interval(start, end));
    //}
    private boolean existed(String uterm){
        if ( routemap == null )
            return false;
        if ( uterm == null )
            return true;

        if( routemap.containsKey(uterm) )
            return true;
        else
            return false;
    }

    public void saveRouteMap(){
        if (routemap == null)
            return;

        FileOutputStream output, output1;
        try {

            output = new FileOutputStream("eHOST.map");
            PrintStream p = new PrintStream(output);

            //output1 = new FileOutputStream("eHOST.distribution");
            //PrintStream p1 = new PrintStream(output1);

            for(Iterator itr = routemap.keySet().iterator(); itr.hasNext();){
                String uterm = (String) itr.next();
            
                Vector<Integer> value = (Vector<Integer>) routemap.get(uterm);
                
                //int keylength = key.length();
                /*if( keylength > this.KEYLENGTH )
                    continue;
                else{
                    for( int i=0;i< 8 - keylength; i++ )
                        key = key + " ";
                }*/

                p.println(uterm);



                // sequence to a uterm
                //System.out.println("uterm [" + uterm + "] with sequence =" + value.size());
                //p1.println(value.size());
                String key = "";
                for( int i=0;i<value.size();i++){
                    
                    //if( value.size() > 100000 ){
                    //    if ( i%1000 == 0 )
                    //        System.out.println( "   - currrent in " + i);
                    //}
                    if ((i!=0)&&(i%10 != 0))
                        key += ", " + value.get(i).toString();                        
                    else   key = String.valueOf( value.get(i) );

                    if ((i== value.size() - 1)||( (i+1)%10 == 0 )){
                        p.println(key);
                        key = "";
                    }
                }


                p.println("\n");
            }

            p.close();
            //p1.close();

        } catch (Exception ex) {
            System.out.println("Error 1009021511 - " + ex.toString());
        }
        
        
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adjudication;

/**
 *
 * @author imed
 */
public class SugarSeeder {
    private static long seed = 1;
    
    public static String getSeed(){
        seed = seed + 1;
        return String.valueOf( seed );
    }
    
    public static boolean isAdjudicationAlias(String adjudicationAlias){
        if( adjudicationAlias == null )
            return false;
        
        int start = adjudicationAlias.indexOf( "Adjudicatino_Alias: " );
        if(( start < 0)||(start>=adjudicationAlias.length())){
            return false;
        }else
            return true;
        
        
                  
    }
}

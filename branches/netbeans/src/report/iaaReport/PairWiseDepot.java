/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class PairWiseDepot {

    /**constant*/
    public final static int SAMESPAN_AND_SAMECLASS = 1;

    /**static integer, which used to remember how many documents are involved in
     * this processing.*/
    public static int numbersOfArticles = 0;

    
    public static Vector<PairWiseAgreementRecord> depot_SameAll = new Vector<PairWiseAgreementRecord>();
    
    public static void removeAll(){
        depot_SameAll.clear();
        numbersOfArticles = 0;
    }


    public static void truepositivePlusOne(String annotator1, String annotator2, int steps){
        if(!isValidPair(annotator1, annotator2))
        {
            log.LoggingToFile.log(Level.SEVERE, "--------> fail to add one or more true postive record(s)!");
            return;
        }

        for(PairWiseAgreementRecord pairwise: depot_SameAll)
        {
            if(pairwise==null)
                continue;

            if((pairwise.gold_standard_set.trim().compareTo(annotator1.trim())==0)
                &&(pairwise.compared_set.trim().compareTo(annotator2.trim())==0))
            {
                pairwise.true_positive = pairwise.true_positive + steps;
            }
        }

    }

    public static void truepositivePlusOne(String annotator1, String annotator2){
        truepositivePlusOne(annotator1, annotator2, 1);
    }

    /**Two propose:
     * 1) count how many annotations in the gold standard set;
     * 2) init the pairWise record if it didn't get inited.
     */
    public static void countForGoldStandardSet(String annotator1, String annotator2) throws Exception{

        if(!isValidPair(annotator1, annotator2))
        {
            log.LoggingToFile.log(Level.SEVERE, "--------> fail to add 1");
            return;
        }

        boolean found = false;
        for(PairWiseAgreementRecord pairwise: depot_SameAll)
        {
            if(pairwise==null)
                continue;

            if((pairwise.gold_standard_set.trim().compareTo(annotator1.trim())==0)
                &&(pairwise.compared_set.trim().compareTo(annotator2.trim())==0))
            {
                pairwise.subTotal_GoldStandard = pairwise.subTotal_GoldStandard + 1;
                found = true;
            }
        }

        if(!found){
            throw new Exception("--------> 1108190216::fail to count the correct ammount of the annotations in the gold standard set!");
        }

    }


    /** 
     * Two propose of this method:
     * 
     * 1) set the pairwise.subTotal_Compare++ to count how many annotations 
     *    in the comparation set;
     * 2) initial the pairWise record if it didn't get inited.
     */
    public static void countForCompareSet(String annotator1, String annotator2) throws Exception{

        if(!isValidPair(annotator1, annotator2))
        {
            log.LoggingToFile.log(Level.SEVERE, "fail to add 2");
            return;
        }
        
        boolean found = false;
        for(PairWiseAgreementRecord pairwise: depot_SameAll)
        {
            if(pairwise==null)
                continue;

            if((pairwise.gold_standard_set.trim().compareTo(annotator1.trim())==0)
                &&(pairwise.compared_set.trim().compareTo(annotator2.trim())==0))
            {
                pairwise.subTotal_Compare = pairwise.subTotal_Compare + 1;
                found = true;
            }
        }

        if(!found){
            throw new Exception("--------> 1109151631::fail to count the correct ammount of the annotations in comparation set!");
        }

    }

    

    /**init a depot space to store a pair-wise information between two annotators.
     *
     * @param   annotator1
     *          The 1st annotator, whose data set will be considered as
     *          gold standard.
     *
     * @param   annotator2
     *          Whose data set will be compared to the gold standard, and then
     *          compared result will be the final output
     */
    public static void initAPairWiseRecord(String annotator1, String annotator2){

        // if current two annotators have at least one empty name, we show a log
        // message and stop
        if( !isValidPair(annotator1, annotator2))
        {
            // show WARNING message on screen

            //log to file
            log.LoggingToFile.log(Level.SEVERE, "user's current operation is trying" +
                    " to init a depot space for two annotators:\n"
                    + "annotator - gold standard : ["
                    + annotator1
                    + "]\nannotator - compared set : ["
                    + annotator2
                    + "]\nand at least one of them have an empty annotator name!!!");
            return;
        }

        try{
            depot_SameAll.add(new PairWiseAgreementRecord(annotator1, annotator2) );
        }catch(Exception ex){
            log.LoggingToFile.log(Level.SEVERE, "1108190629::"+ ex.getMessage() );
        }
    }

    public static boolean recordExists(String annotator1, String annotator2){
        
        // any one of these two annotators have an EMPTY name will cause this
        // method return a TRUE value.
        if(!isValidPair(annotator1, annotator2)){
            return true;
        }
        
        for(PairWiseAgreementRecord pair : depot_SameAll )
        {
            if(pair==null)
                continue;
            
            if((pair.gold_standard_set.trim().compareTo(annotator1.trim())==0)
               &&(pair.compared_set.trim().compareTo(annotator2.trim())==0))
                return true;
        }
        
        return false;
    }

    /**check whehter these two annotators' names are not null and their 
     * length are longer than 0.
     *      
     */
    public static boolean isValidPair(String annotator1, String annotator2){
        if((annotator1==null)||(annotator2==null))
            return false;
        if((annotator1.trim().length()<1)||(annotator2.trim().length()<1))
            return false;

        return true;
    }

    public static void completeForms()
    {
        for( PairWiseAgreementRecord pair : depot_SameAll )
        {
            pair.calculPairWiseAgreement();
        }
    }
    
    

}

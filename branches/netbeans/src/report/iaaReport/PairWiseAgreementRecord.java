/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

/**
 * This is a structure that used to record statistical information of agreements
 * and disagreements between two designated annotators, such as true-positive,
 * false-positive pairs between two annotators.
 * 
 * @author Jianwei Leng
 */
public class PairWiseAgreementRecord{

    /**annotator which we considered its dataset as gold standard*/
    public String gold_standard_set;

    /**annotator which used to compare with the gold standard*/
    public String compared_set;

    
    public int true_positive = 0;
    public int false_positives = 0;
    public int false_negatives = 0;

    /**indicates how many annotations belong in gold standard set, or belong
     * to the first annotator in current pair.
     */
    public int subTotal_GoldStandard = 0;
    public int subTotal_Compare = 0;

    public float precision = 0.0f;
    public float recall = 0.0f;
    public float f_score = 0.0f;

    public PairWiseAgreementRecord(String annotator1, String annotator2) throws Exception{
        
        if(!PairWiseDepot.isValidPair(annotator1, annotator2))
            throw new Exception("--------> 1108180622:: Fail to init a PairWise record!!!");

        this.gold_standard_set = annotator1.trim();
        this.compared_set = annotator2.trim();
    }

    public void calculPairWiseAgreement(){
        //printAll();
        this.false_positives = this.subTotal_GoldStandard - true_positive;
        this.false_negatives = this.subTotal_Compare - true_positive;

        precision = (float)this.true_positive/((float)this.true_positive+(float)this.false_positives);
        recall = (float)this.true_positive/((float)this.true_positive+(float)this.false_negatives);
        f_score = 2*recall*precision/(precision+recall);
    }

    /**print the statistical report on screen*/
    public void printAll(){
        System.out.println("\nsubtotal gold standard - " + subTotal_GoldStandard);
        System.out.println("subTotal_Compare - " + subTotal_Compare);
        System.out.println("true_positive - " + true_positive);

    }
}
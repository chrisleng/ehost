/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package report.iaaReport;

/**
 *
 * @author leng
 */
public class ClassAgreementRecord {
    
    public String classname;
    public float IAAscore;
    public int matches = 0;
    public int nonmatches = 0;

    public ClassAgreementRecord(String classname, boolean isMatched){
        this.classname = classname;
        if(isMatched)
            matches = 1;
        else
            nonmatches = 1;
    }

    public void completeRecord(){
        this.IAAscore = (float)matches/((float)matches + (float)nonmatches);
    }

}

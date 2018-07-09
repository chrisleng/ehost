/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.data;

/**
 *
 * @author imed
 */
public class Rule {
    public String ruleId;
    public String regEx;
    public String matchStrategy;
    public String matchType;
    public String confidence;

    Rule(String ruleId, String regEx, String matchStrategy, String matchType, String confidence) {
        this.ruleId = ruleId;
        this.regEx = regEx;
        this.matchStrategy = matchStrategy;
        this.matchType = matchType;
        this.confidence = confidence;
        
        
        
    }
}

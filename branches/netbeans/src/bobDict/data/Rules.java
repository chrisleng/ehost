/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.data;

import java.util.ArrayList;

/**
 *
 * @author imed
 */
public class Rules {

    public ArrayList<Rule> rules = new ArrayList<Rule>();
    
    
    public void addRule(String ruleId, String regEx, String matchStrategy, String matchType, String confidence) {
        if( regEx == null )
            return;
        if( regEx.trim().length()<1)
            return;
        
        if(exists( regEx ))
            return;
                
        
        Rule rule = new Rule(ruleId,  regEx,  matchStrategy,  matchType,  confidence );
        rules.add( rule );
        
    }
    
    /**report whether an rule is record, return true if it's recorded.*/
    public boolean exists(String regex){
        //if((ruleId==null)||(ruleId.trim().length()<1))
//            return false;
        
        if((regex==null)||(regex.trim().length()<1))
            return false;
        
        regex = regex.trim();
        //ruleId = ruleId.trim();
        
        for( Rule rule : rules ){
            if(rule==null)
                continue;
            if((rule.regEx!=null)&&(rule.regEx.trim().compareTo( regex ) == 0))
                return true;
            
  //          if((rule.ruleId!=null)&&(rule.ruleId.trim().compareTo( ruleId ) == 0))
    //            return true;
        }
        return false;
    }
    
    
    
}

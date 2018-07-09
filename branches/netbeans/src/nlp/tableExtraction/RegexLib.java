/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.tableExtraction;

/**
 * Regular Expressions that used to classify blocks.
 *
 * @author chris leng
 */
public class RegexLib {
    
    // basic number: 1.99 or 1. or .32 or 2
    // or basic numner + [11]
    private final static String number = "((\\d+\\.\\d+)|(\\d+\\.)|(\\.\\d+)|((\\d+)[%])|(\\d+))";
    // FVC  L   2.3 32
    private final static String regex_type1_fvc = "\\bFVC\\b([ ]+)([L])(([ ]+" + number + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    // FEV1 L 2.3 32
    private final static String regex_type1_fev1 
            = "\\bFEV1\\b([ ]+)([L])(([ ]+" + number + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    
    private final static String regex_type1_fev1fvc 
            = "\\bFEV1/FVC\\b([ ]+)([%]{0,1})(([ ]+" 
            + number 
            + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    
    private final static String regex_type1_pf 
            = "\\bPF\\b([ ]+)((L/SEC)|(l/sec))(([ ]+" 
            + number 
            + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    
    private final static String regex_type1_fef2575 
            = "\\bFEF(25[-]75)\\b([ ]+)((L/SEC)|(l/sec))(([ ]+" 
            + number 
            + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    
    private final static String columnheads 
            = "\\b(UNITS|units)([ ]+)(PRED|pred)([ ]+)(ACTUAL|actual)([ ]+)(%PRED|%pred)([ ]+)(PREV1|prev1)([ ]+)(PREV2|prev2)([ ]+)(CI|ci)";
    
    private final static String regex_type1_dlcosb 
            = "\\bDLCO[-]SB\\b([ ]+)([L])(([ ]+" 
            + number 
            + "(([ ]*)\\((\\d{1,2})%\\)){0,1})+)";
    
    
    /**regular expressions that used for classification.*/
    public final static String[][] regexlib_identification = { 
        
        // type 1：row
        { regex_type1_fvc,       "table.var.FVC"     },
        { regex_type1_fev1,      "table.var.FEV1"    },
        { regex_type1_pf,        "table.var.PF"      },
        { regex_type1_fev1fvc,   "table.var.fev1fvc" },
        { regex_type1_fef2575,   "table.var.fef2575" },
        { regex_type1_dlcosb,    "table.var.dlco_sb" },
        
        // table title
        { "\\bSTANDARD STUDY\\b", "table.title"     },
        { "\\bAFTER BRONCHODILATOR\\b", "table.title"},
        
        // table head
        { columnheads,            "table.columnheads" }
            
    };
}

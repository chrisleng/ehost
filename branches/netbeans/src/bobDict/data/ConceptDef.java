/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.data;

import java.util.HashMap;

/**
 *
 * @author imed
 */
public class ConceptDef {
    public String name;
    public String processAllRules;
    
    public Rules rules = new Rules();
    // name, type
    public AnnotationRule annotationrule;
            
    public ConceptDef(String name, String processAllRules){
        this.name = name;
        this.processAllRules = processAllRules;
    }
    
    public void setAnnotationRule(AnnotationRule annotationrule){
        if( annotationrule != null )
            this.annotationrule = annotationrule;
    }
    
    
    
}

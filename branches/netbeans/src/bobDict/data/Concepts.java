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
public class Concepts {
    public HashMap<String, ConceptDef> concepts = new HashMap<String, ConceptDef>();

    public void clear() {
        concepts.clear();
    }
    
    public void addConcept(String name, ConceptDef concept){
        if( name == null )
            return;
        
        if(name.trim().length()<1)
            return;
        
        if( concept == null )
            return;
        
        concepts.put(name.trim(), concept);
    }
    
    public void addConcept(String name, String processAllRules){
        if( name == null )
            return;
        
        if(name.trim().length()<1)
            return;                                
        
        concepts.put(name.trim(), new ConceptDef(name, processAllRules));
    }

    public void recordAnnotationRule(String conceptname, String id, String type, String groupBegin, String groupEnd) {
        ConceptDef concept = concepts.get(conceptname);
        if( concept == null ){
            return;
        }
        
        if((type==null)||(type.trim().length()<1))
            return;
        
        concept.annotationrule = new AnnotationRule( id, type, groupBegin, groupEnd);
    }

    public void recordAnnotationsetFeature(String conceptname, String setFeature_name, String setFeature_type) {
        ConceptDef concept = concepts.get(conceptname);
        if( concept == null ){
            return;
        }
        
        if((setFeature_name==null)||(setFeature_name.trim().length()<1))
            return;
        
        setFeature_name = setFeature_name.trim();
                
        
        if(setFeature_type!=null)
            setFeature_type =setFeature_type.trim();
        
        concept.annotationrule.addSetFeatures(setFeature_name, setFeature_type);
        
    }

    public void recordRule(String conceptname, String ruleId, String regEx, String matchStrategy, String matchType, String confidence) {
        ConceptDef concept = concepts.get(conceptname);
        if( concept == null ){
            return;
        }
        
        if((regEx==null)||(regEx.trim().length()<1))
            return;
        
        
        concept.rules.addRule(  ruleId,  regEx,  matchStrategy,  matchType,  confidence );
        
    }
    
    
}

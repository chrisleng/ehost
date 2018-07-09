/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bobDict.analyst;

import bobDict.data.dictionary;
import java.io.File;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author imed
 */
public class Loader {

    public void loadXML(File xmlfile){
        try{
                
            // init and constract doc object
            SAXBuilder sb = new SAXBuilder();            
            Document doc = sb.build( xmlfile );
            
            // get the root "conceptSet"
            Element conceptSet = doc.getRootElement(); // get element of root                                    
            if( conceptSet == null ){
                System.out.println("didn't find \"conceptSet\" node in the BoB dictionary.");
                return;
            }
                        
            // get namespace
            Namespace namespace = conceptSet.getNamespace();
            //System.out.println( namespace.toString() );
            
            // get root of variable set                                                           
            Element variables_root = conceptSet.getChild( "variables", namespace );
            if( variables_root == null ){
                System.out.println("didn't find \"variables\" node in the BoB dictionary.");
                return;
            }
            
            // get list of variable
            List variable_list = variables_root.getChildren("variable", namespace);
            if( variable_list == null ){
                System.out.println("didn't find any \"variable\" node in the BoB dictionary.");
            }else{
                // get variable one by one
                for(int i=0; i < variable_list.size(); i++ ){
                    Element element_variable = (Element) variable_list.get(i);
                    String name = element_variable.getAttributeValue("name");
                    String value = element_variable.getAttributeValue("value");
                    //System.out.println("name = " + name + "; value = " + value);
                    dictionary.variables.add(name, value);
                }
            }
            
            
            // get concept rule for all nodes of "concept"
            List list_concept = conceptSet.getChildren( "concept", namespace);
            
                
            if(( list_concept == null )||(list_concept.size()<1)){
                System.out.println(" no concept node is given in this BoB dictioanry!" );
                return;
            }else{
                
                // go though each concept
                for( int i=0; i< list_concept.size(); i++ ){
                    
                    // get basic information of the concept
                    Element element_concept = (Element) list_concept.get(i);
                    String name = element_concept.getAttributeValue("name");
                    String processAllRules = element_concept.getAttributeValue("processAllRules");
                    System.out.println("name = " + name + ";   processAllRules = " + processAllRules + "\n" );
                    dictionary.concepts.addConcept(name, processAllRules);
                    
                    // start getting rules of this concept
                    Element element_rulesRoot = element_concept.getChild("rules", namespace);
                    if( element_rulesRoot == null ){
                        System.out.println( "Concept[" + name + "] doesn't have any rules" );
                        continue;
                        
                    }else{
                        
                        // try to get details for each rule of this concept
                        List List_rule = element_rulesRoot.getChildren("rule", namespace);
                        if(( List_rule == null )||(List_rule.size()<1)){
                            System.out.println( "Concept[" + name + "] has 0 rule!" );
                            continue;
                        }else{
                            
                            // start to get details for each rule of this concept
                            for( int j=0; j< List_rule.size(); j++ ){
                                Element element_rule = (Element) List_rule.get(j);
                                String ruleId = element_rule.getAttributeValue("ruleId");
                                String regEx = element_rule.getAttributeValue("regEx");
                                // matchStrategy
                                String matchStrategy = element_rule.getAttributeValue("matchStrategy");
                                // matchType
                                String matchType = element_rule.getAttributeValue("matchType");
                                // confidence
                                String confidence = element_rule.getAttributeValue("confidence");                                
                                
                                dictionary.concepts.recordRule(name, ruleId, regEx, matchStrategy, matchType, confidence );
                            }
                        }
                        
                    }
                    // end of getting rules
                    
                    // start getting "createannotation"
                    Element element_createAnnotationsRoot = element_concept.getChild("createAnnotations", namespace);
                    if( element_createAnnotationsRoot == null ){
                        System.out.println( "Concept[" + name + "] doesn't have creating information" );
                        continue;                        
                    }else{
                        Element element_annotation = element_createAnnotationsRoot.getChild("annotation", namespace);
                        String id = element_annotation.getAttributeValue("id");
                        String type = element_annotation.getAttributeValue("type");
                        
                        Element element_begin = element_annotation.getChild("begin", namespace);
                        String groupBegin = element_begin.getAttributeValue("group");
                        
                        Element element_end = element_annotation.getChild("end", namespace);
                        String groupEnd = element_end.getAttributeValue("group");
                        
                        // record
                        dictionary.concepts.recordAnnotationRule( name, id, type, groupBegin, groupEnd );
                        
                        
                        List list_setFeature = element_annotation.getChildren("setFeature", namespace);
                        if( list_setFeature != null ){
                            for(int k=0;k<list_setFeature.size();k++){
                                Element element_setFeature = (Element) list_setFeature.get(k);
                                String setFeature_name = element_setFeature.getAttributeValue("name");
                                String setFeature_type = element_setFeature.getAttributeValue("type");
                                
                                // record
                                dictionary.concepts.recordAnnotationsetFeature(name, setFeature_name, setFeature_type);
                            }
                        }
                        
                    }
                    
                }
            }
            
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}

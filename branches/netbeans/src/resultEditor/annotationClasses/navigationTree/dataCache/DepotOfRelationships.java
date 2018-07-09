/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import java.util.HashMap;
import java.util.Vector;
import resultEditor.annotationClasses.navigationTree.treeRelated.NodeOfRelationship;
import resultEditor.annotations.Annotation;
import resultEditor.annotations.AnnotationRelationship;


/**
 *
 * @author imed
 */
public class DepotOfRelationships {
    
    /**A flag that tell us whether the branch of relationships on the navigation 
     * panel has been expanded or collapsed.*/
    public static boolean isExpanded = false;
    
    public static HashMap<String, Rel> rels = new HashMap<String, Rel>();
    
    public static int total = 0;
    
    public static void clear(){
        total=0;
        rels.clear();
    }
    
    public static void count(String name, Annotation annotation, String filename ){
        if( name == null )
            return;
        if( name.trim().length()<1 )
            return;
        
        if(annotation==null)
            return;
        
        name = name.trim();
        
        Rel rel = rels.get(  name );
        if( rel == null ){
            total++;
            rels.put( name, new Rel(name, annotation, filename)  );
        }else{
            total++;
            rel.add(annotation, filename);
            rels.put( name, rel );
        }
    }

    public static void count(Annotation thisAnnotation, Vector<AnnotationRelationship> relationships, String filename) {
        if( relationships == null )
            return;
        
        if( thisAnnotation == null )
            return;
        
        for( AnnotationRelationship relationship : relationships ){
            if( relationship == null )
                continue;
            if( relationship.mentionSlotID == null )
                continue;
            
            String name = relationship.mentionSlotID.trim();
            if( name.length() < 1 )
                continue;
            count( name , thisAnnotation, filename);
            // System.out.println( "rel["+name+"]->" + thisAnnotation.annotationText );
        }
    }

    public static int getTotal() {
        return total; 
    }

    /**mark a relationship node is expanded or collapsed.*/
    public static void setExpanded(NodeOfRelationship node_of_relationship, boolean isExpanded) {
        
        // validity checking of parameters
        if( node_of_relationship == null )
            return;
        if(( rels == null ) || (rels.size()<1))
            return;
        
        // get the name of the relationship
        String relname = node_of_relationship.getRelName();
        if(relname == null)
            return;
        
        Rel rel = rels.get( relname );
        if( rel != null ) {
            rel.isExpanded = isExpanded;
            rels.put(relname, rel);
        }
    }
    
}

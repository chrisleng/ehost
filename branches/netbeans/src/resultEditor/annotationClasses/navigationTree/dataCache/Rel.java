/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.annotationClasses.navigationTree.dataCache;

import java.util.ArrayList;
import resultEditor.annotations.Annotation;

/**
 *
 * @author imed
 */
public class Rel{
    
    /**A flag that tell us whether the node of a relationship on the navigation 
     * panel has been expanded or collapsed.*/
    public boolean isExpanded = false;
    
    /**The name of this annotation relationship.*/
    public String relname;   
    
    public int count = 0;
    
    /**Annotations who has this specific relationship.*/
    public ArrayList<RefAnnotation> refannotations = new ArrayList<RefAnnotation>();
    
    
    public Rel(String relname, Annotation annotation, String filename){
        this.relname = relname;
        count = 1;
        RefAnnotation refannotation = new RefAnnotation( annotation, filename );
        refannotations.add(refannotation);
    }
    
    //public Rel(String name, int count, Annotation annotation){
    //    this.name = name;
    //    this.count = count;
    //    annotations.add(annotation);
    //}
    
    public void add(Annotation annotation, String filename){
        if( annotation != null ){
            this.count++;
            RefAnnotation refannotation = new RefAnnotation( annotation, filename );
            refannotations.add(refannotation);
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resultEditor.relationship.complex;

import java.util.ArrayList;
import resultEditor.annotations.Annotation;

/**
 * A format used to store the result after checking possible relationships for 
 * two sets of given annotations. A method "relCheckStatus()" in class "GUI" is 
 * designed to check if we need to popup a dialog to handle complex status to 
 * create relationships. 
 * Before really creating a relationship, user usually need to click two times on the 
 * document viewer to select annotations. Two of these annotations will be selected as
 * the start point and end point as a pair annotations, so we can set up relationship 
 * between. We may get serveral annotation pairs and corresponsed relationships. This
 * is considered as a complex situation as we don't which one is user really want.
 * 
 * @author Jianwei Leng, March 21, 2012
 */
public class RelCheckResult{
    
    /**flag that user to */
    public boolean needpopup = false;
        
    /**they are all source annotations that are selected by user's previous 
     * click on the document viewer. One of them might be used to create a 
     * new relationship. It is in format of "Arraylist<Annotation>"; 
     * And some of them are "null".*/
    public ArrayList<Annotation> sourceAnnotations = new ArrayList<Annotation>(); 
        
    /**they are all destination annotations that are selected by user's  
     * current clicking on the document viewer. One of them might be used to 
     * create a new relationship. It is in format of "Arraylist<Annotation>"; 
     * And some of them are "null".*/
    public ArrayList<Annotation> objectAnnotations = new ArrayList<Annotation>(); 
    
    
    // ------------------------------------------------------ //
    // following variables only can be used during there is 
    // only two annotations can be paired with only one relationship.
    // ------------------------------------------------------ //
    
    /**if this flag is "true", it means there is only two annotations can be 
     * paired with only one relationship.
     * 
     * ***This variables only can be used during there is only two annotations 
     * can be paired with only one relationship. ***
     */
    public boolean only_one_possible_relationship = false;
    
    /**there is only two annotations can be paired with only one relationship* 
     * This is the annotation in the start point.
     * 
     * ***This variables only can be used during there is only two annotations 
     * can be paired with only one relationship. ***
     */
    public Annotation a;
     
    /**there is only two annotations can be paired with only one relationship* 
     * This is the annotation in the end point.
     * 
     * ***This variables only can be used during there is only two annotations 
     * can be paired with only one relationship. ***
     */
    public Annotation b;
}
    
    

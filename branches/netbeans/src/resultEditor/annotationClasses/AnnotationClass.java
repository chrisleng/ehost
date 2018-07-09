/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotationClasses;

import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotations.Annotation;

/**
 *
 * @author Chris 2010-07-18 4:14 pm , Division of Epidemiology, School of Medicine
 */
public class AnnotationClass {
    public String annotatedClassName;
    
    /**A short description of this class, and it will be displayed with the class
     * name on the navigation tree.
     */
    public String shortComment = null;
    
    /**description: long description about this class*/
    public String des = null;
    
    
    public Color  backgroundColor;
    public String source;

    /**Flag that indicates whether this class inherits the public attributes.*/
    public boolean inheritsPublicAttributes = true;

    /**In eHOST system, there are two kinds of annotation attributes, one is
     * public attributes that all categories of annotations can use it; the
     * other one is designated for specific category of annotations that only
     * some kind of annotation can have these attributes. This vector is
     * designed to store the second type of attributes.
     */
    public Vector<AttributeSchemaDef> privateAttributes = new Vector<AttributeSchemaDef>();

    /**
     * Node expanded or not. <p>All classes/markables get displayed as a node
     * in the navigator panel of result editor in eHOST. These nodes may have
     * children which have same classes/markables.
     */
    public boolean isNodeExpanded = false;
    public boolean isVisible = true;

    /**all subAnnotations belongs to this annotated class*/
    protected int annotationsCounter = 0;

    /**this is a vector which store all annotation uniques under current classname*/
    private Vector<AnnotationType> annotationUniques = new Vector<AnnotationType>();


    public AnnotationClass(){
    }

    public AnnotationClass(String annotatedClassName,
            Color backgroundColor,
            String source,
            boolean isVisible,
            boolean isNodeExpanded){
        
        this.annotatedClassName = annotatedClassName;
        this.backgroundColor = backgroundColor;
        this.source = source;

        this.isNodeExpanded = isNodeExpanded;
        this.isVisible = isVisible;
    }

    public void resetDepot_of_AnnotationTypes(){
        this.annotationUniques.clear();
    }
    public void resetAmount_of_AnnotationCount(){
        this.annotationsCounter = 0;
    }

    public int annotationTypesSize(){
        return annotationUniques.size();
    }

    

    public int getAnnotationAmount(){
        return this.annotationsCounter;
    }

    public int getAnnotationTypeAmount(){
        if ( this.annotationUniques == null )
            return 0;
        return this.annotationUniques.size();
    }

    private void annotationCountPlusPlus(){
        this.annotationsCounter = this.annotationsCounter + 1 ;
    }

    /**to an annotationText, record it if it's a new type; or count++ if existed.*/
    public void recordAnnotation( String annotationText, String filename, Annotation annotation ){
        if ( ( annotationText == null )
                ||( annotationText.trim().length() < 1 ) )
            return;

        annotationCountPlusPlus();

        if( this.isAnnotationTypeExisting( annotationText.trim() ) ){
            this.annotationTypePlus(
                    annotationText.trim(),
                    filename,
                    annotation );
        } else {
            AnnotationType at = new AnnotationType( 
                    annotationText.trim(),
                    1,
                    annotation.spanset.getSpanAt(0).start
                    );
            at.addAnnotation(filename, annotation);
            this.annotationUniques.add( at );
        }
    }

    public final Vector<AnnotationType> getAnnotationTypes(){
        sort();
        return this.annotationUniques;
    }

    /**sort annotation uniques by given sequence parameter*/
    public void sort(){
        Comparator comp_byCharacters = new AnnotationTypeComparator();
        Comparator comp_byLocations = new AnnotationTypeCOmparator_location();
        // use sequencey of characters
        if(!env.Parameters.ShowAnnotationUniquesInTree.isLocationSequence)
            Collections.sort( this.annotationUniques , comp_byCharacters);
        // use sequence of locaitons
        else
            Collections.sort( this.annotationUniques , comp_byLocations);
    }

    private void annotationTypePlus(String annotationText,
            String filename,
            Annotation annotation ){

        if ( ( annotationText == null )
                ||( annotationText.trim().length() < 1 ))
            return;

        for( AnnotationType annotationtype : annotationUniques ){
            if ( annotationtype == null )
                continue;
            if( annotationtype.name.compareTo( annotationText.trim() ) == 0 ){
                annotationtype.amount = annotationtype.amount + 1;
                annotationtype.addAnnotation(filename, annotation);
                return;
            }
        }
    }

    /**check existing of a type of annotationText. */
    private boolean isAnnotationTypeExisting( String annotation ){
        if ( ( annotation == null )||( annotation.trim().length() < 1 ))
            return false;

        for( AnnotationType annotationtype : annotationUniques ){
            if ( annotationtype == null )
                continue;
            if( annotationtype.name.compareTo( annotation.trim() ) == 0 ){
                return true;
            }
        }
        return false;
    }

    /**
     * Set flag to indicate node expanded or not. <p>
     * All classes/markables get displayed as a node in the navigator panel of
     * result editor in eHOST. These nodes may have children which have same
     * classes/markables.
     *
     * @param   flag
     *          The status of node of this class/markable you want to set. True
     *          means expanded; false means not expanded.
     */
    public void setExpansionStatus(boolean flag){
        this.isNodeExpanded = flag;
    }


    /**add or replace a record. If this attribtue is new, then just added it
     * to the end, or by alpha-number frequency; if it's existing, then
     * just replace the old record.
     */
    public void putAttribute(String attributeName, Vector<String> values){
        int size = this.privateAttributes.size();
        
        // if no the depot is empty, just add it w/o  sorting or comparsion.
        if(size==0) {
            AttributeSchemaDef att = new AttributeSchemaDef(attributeName, values);
            this.privateAttributes.add(att);
        }
        else{
            if( !hasAttributeName( attributeName ) ){
                AttributeSchemaDef att = new AttributeSchemaDef(attributeName, values);
                this.privateAttributes.add(att);
            }
        }        
    }


    /**check whether we already has an attribute called this name.*/
    public boolean hasAttributeName(String attributeName){
        if( ( attributeName == null )
            || ( attributeName.trim().length()<1 ) )
            return true;

        if ( this.getNumberOfAttributes() == 0 )
            return false;

        for(AttributeSchemaDef attribute : this.privateAttributes){
            if(attribute!=null){
                if(attribute.getName()!=null){
                    if(attribute.getName().trim().compareTo(attributeName.trim())==0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    

    public int getNumberOfAttributes(){
        return this.privateAttributes.size();
    }

    public AttributeSchemaDef getAttribute(int index){
        if( index < getNumberOfAttributes() ){
            return this.privateAttributes.elementAt(index);
        }

        return null;
    }

    public final Vector<AttributeSchemaDef> getAttributes(){
        return this.privateAttributes;
    }

    /**delete an attribute and its values by the given attribute name.*/
    public void removeAttribute(String attributeName) {
        if( ( attributeName == null ) ||
                (attributeName.trim().length() < 1 ) )
            return;
        
        if(hasAttributeName(attributeName)){
            for(int i=0; i<this.getNumberOfAttributes(); i++ ){
                if( this.privateAttributes.get(i).getName().trim().compareTo(attributeName.trim())==0 ){
                    this.privateAttributes.removeElementAt(i);
                    return;
                }
            }
        }
    }

    /**
     * @param   index
     *          It's the index of the attribute which we want to move up.
     *
     */
    public void moveUp_attribute(int index){
        if(index==0)
            return;
        if(index>this.privateAttributes.size()-1)
            return;

        AttributeSchemaDef temp = null;

        temp = this.privateAttributes.get(index-1);

        this.privateAttributes.setElementAt(
                this.privateAttributes.get(index),
                index-1);

        this.privateAttributes.setElementAt( temp, index );
    }

    public void moveDown_attribute(int selectedIndex) {

        if(selectedIndex>=this.privateAttributes.size()-1)
            return;

        AttributeSchemaDef temp = null;

        temp = this.privateAttributes.get(selectedIndex+1);

        this.privateAttributes.setElementAt(
                this.privateAttributes.get(selectedIndex),
                selectedIndex+1);

        this.privateAttributes.setElementAt( temp, selectedIndex );
    }
}

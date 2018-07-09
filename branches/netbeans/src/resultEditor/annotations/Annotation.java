package resultEditor.annotations;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import relationship.complex.dataTypes.RelationshipDef;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.relationship.iListable;

/**
 * This is the data structure of an annotation with its related attributes,
 * such as normal relationships and complex relationships and verifier
 * suggestions.
 *
 * @author Jianwei Leng, University Of Utah
 * @since  SDK 1.6
 * @since  2010-06-23
 *
 */
public class Annotation implements Comparable {

    /**The unique index that used to identify all imported or newly created annotation in eHOST result editor.*/
    public int uniqueIndex;
    public String uniqueIndexPhrase;
    /**Mention ID String, contains a instance name and a ID number.
     * ,such as: "SLC_HS_LENG_88765", here, "SLC_HS_LENG_" is the
     * instance name, and "88765" is the ID number.*/
    public String mentionid;
    /**Text content of this annotation*/
    public String annotationText;
    /**The Startpoint of this annotation in its original text source.*/
    public int spanstart;
    /**The endpoint of this annotation in its original text source.*/
    public int spanend;
    /**Annotator who created this annotation*/
    private String annotator = null;
    /**ID of annotator who created this annotation*/
    public String annotatorid;
    /**Annotated Class which this annotation belongs to. Such as annotation
     * words "X-ray" is an instance of class "Test" */
    public String annotationclass;
    /**The time while this annotation was created. Example: "Thu Jun 24 13:23:18 MDT 2010".*/
    public String creationDate;
    /**Comment string of this annotation.*/
    public String comments;
    /**flag used to indicate whether current annotation are same to all other
     * annotator's annotation*/
    public boolean is3WayMatched = true;
    /**Imported Verifier Suggestions , one or more Strings.*/
    public Vector<String> verifierSuggestion = new Vector<String>();
    /**Attributes of this annotation*/
    public Vector<AnnotationAttributeDef> attributes = new Vector<AnnotationAttributeDef>();
    /**complex Relationship of this annotation.*/
    public Vector<AnnotationRelationship> relationships = new Vector<AnnotationRelationship>();

    /**the is an list of spans. The first span will be the main span of these annotation */
    public SpanSetDef spanset = new SpanSetDef();
        
    /**Alias name of an annotation under the adjudication mode. If it start with 
     * string "Adjudication_Alias:", like "Adjudication_Alias: 0098", it means 
     * this annotation is in processing, such as there is found annotations, and 
     * we rejected one the other.
     * 
     * If it doesn't start with "Adjudication_Alias", and it's an unique index
     * of another annotation that replaced this annotation under the adjudication 
     * mode.
"     */
    public String adjudicationAlias = null;

    /**Assign an annotator name to this annotation. 
     * @param   annotatorname
     *          The name of an annotator who created this annotation.
     */
    public void setAnnotator(String annotatorname){
        if( annotatorname == null ){
            this.annotator = null;
            return;
        }
        
        if( annotatorname.trim().length() < 1 ){
            this.annotator = null;
            return;
        }
        
        this.annotator = annotatorname.trim();
                    
    }
    
    /**Get the annotator's name (Sometimes, annotator will be in format of 
     * "Oracle[Chris]", and this method only returns "Chris" back.). */
    public String getAnnotator(){
        return removeOracle( this.annotator );
    }
    
    /**Get the full annotator name (Sometimes, annotator will be in format of 
     * "Oracle[Chris]", and this method returns all this name back.). */
    public String getFullAnnotator(){
        return this.annotator;
    }
    
    /**Get the pure annotator name, for example return "Chris" 
     * if current annotator name is "Oracle[Chris]".
     * 
     * @param   annotatorFullName   The recorded annotator name of current 
     * annotation.
     */
    private String removeOracle(String annotatorFullName){
        
        if( annotatorFullName == null )
            return null;
        if( annotatorFullName.trim().length() < 1 )
            return null;
        
        // 1O 2R 3A 4C 5L 6E 7[ 8]
        if( annotatorFullName.trim().length() < 8 )
            return annotatorFullName;
        else{
            String first7 = annotatorFullName.substring(0, 7);
            first7 = first7.toLowerCase();
            if( first7.compareTo( "oracle[") == 0 ){
                return annotatorFullName.substring(7, annotator.length() - 1 );
            }else{
                return annotatorFullName;
            }
        }
        
        
        
    }
    
    /**This method is return the spans in text for listing in a html table.
     * 
     * Example 1: 
     * this annotation have two spans (5,80), (44,98)
     * The return will be "(5,80)<br>(44,98)"
     * 
     * Example 2: 
     * this annotation have just one span (5,80)
     * The return will be "(5,80)"
     * 
     * Example 3: 
     * this annotation have null or zero spans
     * The return will be ""
     */
    public String getSpansInText() {
        if ((this.spanset == null) || (spanset.size() < 1)) {
            return "";
        }

        String strToReturn = "";
        boolean isFirst = true;
        for (int spans = 0; spans < spanset.size(); spans++) {
            SpanDef span = spanset.getSpanAt(spans);
            if (span != null) {
                if (isFirst) {
                    strToReturn += "( " + span.start + "," + span.end + ")";
                    isFirst = false;
                } else {
                    strToReturn += "<br>( " + span.start + "," + span.end + ")";
                }
            }
        }        
        return strToReturn;
    }
    
    /**return true if this annotation has at least one valid attribute*/
    public boolean hasAttribute(){
        if( this.attributes == null )
            return false;
        
        for( AnnotationAttributeDef attribute : this.attributes ){
            if( attribute ==null )
                continue;
            
            if( ( attribute.name == null ) || (attribute.name.trim().length() < 1) )
                continue;
            
            if( ( attribute.value == null ) || (attribute.value.trim().length() < 1) 
                    || (attribute.value.trim().toLowerCase().compareTo("null") == 0 ) )
                continue;
            
            return true;
            
        }
        
        return false;
    }
    
    /**return true if this annotation has at least one valid relationship*/
    public boolean hasRelationship(){
        if( this.relationships == null )
            return false;
        
        for( AnnotationRelationship relationship : this.relationships ){
            if( relationship ==null )
                continue;
            
            if( ( relationship.getMentionSlotID() == null ) || (relationship.getMentionSlotID().trim().length() < 1) )
                continue;                        
            
            return true;
            
        }
        
        return false;
    }

    /**
     * Make a deep/new copy of current instance.
     */
    public Annotation getCopy() {
        
        // new instance
        Annotation annotation = new Annotation();
        
        // copy data
        annotation.uniqueIndex = this.uniqueIndex;
        annotation.uniqueIndexPhrase = this.uniqueIndexPhrase;
        annotation.mentionid = this.mentionid;
        annotation.annotationText = this.annotationText;   
        annotation.spanstart = this.spanstart;        
        annotation.spanend = this.spanend;        
        annotation.annotator = this.annotator;        
        annotation.annotatorid = this.annotatorid;                
        annotation.annotationclass = this.annotationclass;        
        annotation.creationDate = this.creationDate;        
        annotation.comments = this.comments;        
        // annotation.is3WayMatched = true;
        // Imported Verifier Suggestions , one or more Strings.
        // annotation.verifierSuggestion = new Vector<String>();
        
        // Attributes of this annotation
        // Vector<AnnotationAttributeDef> attributes = new Vector<AnnotationAttributeDef>();
        // annotation.attributes = this.attributes.getCopy();
        {
            if( this.attributes != null ) {
                for( AnnotationAttributeDef att : this.attributes ){
                    if( att == null )
                        continue;
                    AnnotationAttributeDef newatt = new AnnotationAttributeDef();
                    newatt.name = att.name;
                    newatt.value = att.value;
                    annotation.attributes.add( newatt );
                }
            }
        }
        
        // complex Relationship of this annotation.
        // Vector<AnnotationRelationship> relationships = new Vector<AnnotationRelationship>();
        // annotation.relationships = this.relationships.getCopy();
        {
            if( this.relationships != null ) {
                for( AnnotationRelationship rel : this.relationships ){
                    if( rel == null )
                        continue;                  
                    annotation.relationships.add( rel.getCopy() );
                }
            }
        }

        // the is an list of spans. The first span will be the main span of these annotation 
        // SpanSetDef spanset = new SpanSetDef();
        annotation.spanset = this.spanset.getCopy();
        
        
    /**Alias name of an annotation under the adjudication mode. If it start with 
     * string "Adjudication_Alias:", like "Adjudication_Alias: 0098", it means 
     * this annotation is in processing, such as there is found annotations, and 
     * we rejected one the other.
     * 
     * If it doesn't start with "Adjudication_Alias", and it's an unique index
     * of another annotation that replaced this annotation under the adjudication 
     * mode.
    "     */
        //public String adjudicationAlias = null;
        return annotation;
    }

    public void removeInvalidRelationships() {
        try{
            
            resultEditor.annotationClasses.Depot classdepot = new
                    resultEditor.annotationClasses.Depot();
            AnnotationClass annclass = classdepot.getAnnotatedClass(annotationclass);
            
            if( annclass == null )
                return;
            
             if((annotationclass==null)||(annotationclass.trim().length()<1))
                return;
             
            if(( relationships == null ) ||(relationships.size()<1))
                return;

            for(int i=relationships.size() - 1; i>=0;i--){
                AnnotationRelationship rel = relationships.get(i);
                if(rel==null)
                    continue;
                
                
                Vector<AnnotationRelationshipDef> arfs = rel.linkedAnnotations;
                boolean is1st = true;
                for(AnnotationRelationshipDef arf : arfs){
                    if( arf == null )
                        continue;
                    String linkedclass = arf.annotationClass;
                    if( env.Parameters.RelationshipSchemas.getPossibleRels(annotationclass, linkedclass) < 1 )
                    {
                        relationships.removeElementAt(i);
                        break;
                    }
                    
                    if( is1st ){
                        is1st = true;
                        String rulename = env.Parameters.RelationshipSchemas.getRelName(annotationclass, linkedclass);
                        if(rulename!=null){
                            rel.mentionSlotID = rulename;
                            relationships.setElementAt(rel, i);
                        }
                        
                    }
                }                
            }
            
            for(int i=relationships.size() - 1; i>=0;i--){
                AnnotationRelationship rel = relationships.get(i);
                if(rel==null)
                    continue;
                Vector<AnnotationAttributeDef> atts = rel.attributes;
                if(( atts == null )||(atts.size()<1))
                    continue;
                
                String relationshipname = rel.mentionSlotID;
                if(relationshipname==null)
                    continue;
                
                
                
                for(int s=atts.size()-1; s>=0;s--) {
                    AnnotationAttributeDef att = atts.get(s);
                    if(att==null)
                        continue;
                    
                    RelationshipDef relationship = env.Parameters.RelationshipSchemas.getRelationshipSchema( relationshipname );
                    
                    if(relationship==null)
                        continue;
                    
                    Vector<AttributeSchemaDef> privateatts = relationship.getAttributes().getAttributes();

                    boolean valid = false;


                    if (!valid) {
                        if (privateatts != null) {
                            for (AttributeSchemaDef privateatt : privateatts) {
                                if (privateatt == null) {
                                    continue;
                                }
                                if (privateatt.getName().compareTo(att.name) == 0) {
                                    valid = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!valid) {
                        atts.removeElementAt(i);
                    }

                }
                
            }
        }catch(Exception ex){
            System.out.print("fail to check and remove inlvalid relationships. Error message = " + ex.getMessage() );
        }
    }
    
    
    
    public void removeInvalidRelationships(int uniqueid, String classname) {
        try{
            
            resultEditor.annotationClasses.Depot classdepot = new
                    resultEditor.annotationClasses.Depot();
            AnnotationClass annclass = classdepot.getAnnotatedClass(annotationclass);
            
            if( annclass == null )
                return;
            
             if((annotationclass==null)||(annotationclass.trim().length()<1))
                return;
             
            if(( relationships == null ) ||(relationships.size()<1))
                return;

            for(int i=relationships.size() - 1; i>=0;i--){
                AnnotationRelationship rel = relationships.get(i);
                if(rel==null)
                    continue;
                
                
                Vector<AnnotationRelationshipDef> arfs = rel.linkedAnnotations;
                
                boolean is1st = true;
                for(AnnotationRelationshipDef arf : arfs){
                    if( arf == null )
                        continue;
                    if(arf.linkedAnnotationIndex!=uniqueid)
                        continue;
                    String linkedclass = arf.annotationClass;
                    if( env.Parameters.RelationshipSchemas.getPossibleRels(annotationclass, classname) < 1 )
                    {
                        relationships.removeElementAt(i);
                        break;
                    }
                    
                    if( is1st ){
                        is1st = true;
                        String rulename = env.Parameters.RelationshipSchemas.getRelName(annotationclass, classname);
                        if(rulename!=null){
                            rel.mentionSlotID = rulename;
                            relationships.setElementAt(rel, i);
                        }
                        
                    }
                }                
            }
            
            for(int i=relationships.size() - 1; i>=0;i--){
                AnnotationRelationship rel = relationships.get(i);
                if(rel==null)
                    continue;
                
                boolean found = false;
                for(AnnotationRelationshipDef arf : rel.linkedAnnotations){
                    if( arf == null )
                        continue;
                    if(arf.linkedAnnotationIndex==uniqueid)
                        found = true;
                }
                
                if( false )
                    continue;
                
                Vector<AnnotationAttributeDef> atts = rel.attributes;
                if(( atts == null )||(atts.size()<1))
                    continue;
                
                String relationshipname = rel.mentionSlotID;
                if(relationshipname==null)
                    continue;
                
                for(int s=atts.size()-1; s>=0;s--) {
                    AnnotationAttributeDef att = atts.get(s);
                    if(att==null)
                        continue;
                    
                    RelationshipDef relationship = env.Parameters.RelationshipSchemas.getRelationshipSchema( relationshipname );
                    if(relationship==null)
                        continue;
                    
                    Vector<AttributeSchemaDef> privateatts = relationship.getAttributes().getAttributes();

                    boolean valid = false;


                    if (!valid) {
                        if (privateatts != null) {
                            for (AttributeSchemaDef privateatt : privateatts) {
                                if (privateatt == null) {
                                    continue;
                                }
                                if (privateatt.getName().compareTo(att.name) == 0) {
                                    valid = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!valid) {
                        atts.removeElementAt(i);
                    }

                }
                
            }
        }catch(Exception ex){
            System.out.print("fail to check and remove inlvalid relationships. Error message = " + ex.getMessage() );
        }
    }
    
    
    
    public void removeInvalidAttributes() {
        try{
            if((annotationclass==null)||(annotationclass.trim().length()<1))
                return;
            
            if(( attributes == null ) ||(attributes.size()<1))
                return;
            
            resultEditor.annotationClasses.Depot classdepot = new
                    resultEditor.annotationClasses.Depot();
            AnnotationClass annclass = classdepot.getAnnotatedClass(annotationclass);
            
            if( annclass == null )
                return;
            Vector<AttributeSchemaDef> privateatts = annclass.privateAttributes;
            Vector<AttributeSchemaDef> publicatts = env.Parameters.AttributeSchemas.getAttributes();
            

            for(int i=attributes.size() - 1; i>=0;i--){
                AnnotationAttributeDef att = attributes.get(i);
                if(att==null) {
                    continue;
                }
                
                if((att.name == null)||(att.value==null)){
                    attributes.removeElementAt(i);
                    continue;
                }
                
                boolean valid = false;
                
                if(annclass.inheritsPublicAttributes){
                    if( publicatts!=null ) {
                        for( AttributeSchemaDef publicatt : publicatts ){
                            if(publicatt==null)
                                continue;
                            if( publicatt.getName().compareTo( att.name ) == 0 ){
                                valid = true;
                                break;
                            }
                        }
                    }
                }
                
                if(!valid){
                   if( privateatts!=null ) {
                        for( AttributeSchemaDef privateatt : privateatts ){
                            if(privateatt==null)
                                continue;
                            if( privateatt.getName().compareTo( att.name ) == 0 ){
                                valid = true;
                                break;
                            }
                        }
                    }
                }
                
                if(!valid){
                    attributes.removeElementAt(i);
                }
                
                
            }
        }catch(Exception ex){
            System.out.print("fail to check and remove inlvalid relationships. Error message = " + ex.getMessage() );
        }
    }
    

    /**Data type that use to indicate the adjudication status of an annotation*/
    public enum AdjudicationStatus {
        
        /**The annotation is excluded from the adjudication mode as its 
         * annotator or class isn't get selected by the user; it should 
         * be invisible in the adjudication mode.
         */
        EXCLUDED, 
        
        /***/
        MATCHES_OK, MATCHES_DLETED, NON_MATCHES, UNPROCESSED, NONMATCHES_DLETED
    };
    /**variable that use to indicate the adjudication status of an annotation,
     * default is "NOBODY", it means that this annotation will not be count
     * into subsequent adjudication processing;
     */
    public AdjudicationStatus adjudicationStatus = AdjudicationStatus.EXCLUDED;

    /**sometimes, we have have dead attributes here, such as attributes name
     * and values are both null or empty values. These dead attributes should
     * be erased.
     */
    public void cleanAttributes() {

        if (attributes == null) {
            attributes = new Vector<AnnotationAttributeDef>();
            return;
        }

        int size = attributes.size();
        if (size <= 0) {
            return;
        }

        for (int i = size - 1; i >= 0; i--) {
            AnnotationAttributeDef nr = attributes.get(i);
            if (nr == null) {
                attributes.removeElementAt(i);
                continue;
            }

            if (nr.name == null) {
                attributes.removeElementAt(i);
                continue;
            }

            if (nr.name.trim().length() < 1) {
                attributes.removeElementAt(i);
                continue;
            }

            //if (nr.value == null) {
            //    attributes.removeElementAt(i);
            //    continue;
            //}

            //if (nr.name.trim().length() < 1) {
            //    attributes.removeElementAt(i);
            //    continue;
            //}
            
        }

    }

    /**Get all relationships of current annotation in a string.
     *
     * @return  a string of all relationships of current annotation, usually
     *          is in following format:
     *              linked to "linking annotation text" with relationship: [
     *              relationship name]
     */
    public String getComplexRelationshipString() {
        if (relationships == null) {
            return null;
        }

        if (relationships.size() < 1) {
            return null;
        }

        String toString = "";

        for (AnnotationRelationship cr : relationships) {
            if (cr == null) {
                continue;
            }

            if (cr.linkedAnnotations == null) {
                continue;
            }

            for (AnnotationRelationshipDef ec : cr.linkedAnnotations) {
                if (ec == null) {
                    continue;
                }

                toString = toString + "linked to \"" + ec.linkedAnnotationText + "\" with relationship:[" + cr.mentionSlotID + "]<br>";
            }
        }

        if (toString.trim().length() < 1) {
            return null;
        }

        return toString;
    }

    /**Get all relationships of current annotation in a string.
     *
     * @return  a string of all relationships of current annotation, usually
     *          is in following format:
     *              linked to "linking annotation text" with relationship: [
     *              relationship name]
     */
    public String getAttributeString() {
        if (this.attributes == null) {
            return null;
        }

        if (attributes.size() < 1) {
            return null;
        }

        String toString = "";

        for (AnnotationAttributeDef nr : attributes) {
            if (nr == null) {
                continue;
            }

            if (nr.name == null) {
                continue;
            }

            toString = nr.name + " = " + nr.value;
        }

        if (toString.trim().length() < 1) {
            return null;
        }

        return toString;
    }
    public Vector<suggestion> verifierFound = new Vector<suggestion>();
    /**mention id will be generated while output annotation to xml*/
    public String outputmentionid;
    /** this annotation is visible or not, default value is true */
    public boolean visible = true;
    /** The attributes and relationships have been verified to be in the schema if this is true*/
    private boolean verified = false;
    // #### 89 #### following are all for IAA report system
    /**this flag is used to indicate whether one annotation has been marked as
     * "matched or unmatched" in the analysis system for IAA report. If it true,
     * it means we found a totally same annotation as this one but belong to
     * a different annotator.
     */
    public boolean isComparedAsMatched = false;
    /**this variable only can be accessed by method "setProcessed()",
     * "setUnProcessed()" and "isProcessed()". It is a flag to indicate whether
     * current annotation has been processed.
     */
    private boolean isMatchingAnalysisForIAAProcessed = false;

    public void setProcessed() {
        isMatchingAnalysisForIAAProcessed = true;
    }

    public void setUnProcessed() {
        isMatchingAnalysisForIAAProcessed = false;
    }

    public boolean isProcessed() {
        return isMatchingAnalysisForIAAProcessed;
    }
    // #### 89 #### end

    /** Default constructor.  */
    public Annotation() {
    }

    /**
     * This Constructor is used to create annotations that do not have relationhships or Verifier information.
     * (i.e.) dictionaries, .annotations files, etc.
     * @param mentionID
     * @param annotationText
     * @param spanstart
     * @param spanend
     * @param annotator
     * @param annotatorID
     * @param annotationClass
     * @param creationDate
     */
    public Annotation(String mentionID, String annotationText, int spanstart,
            int spanend, String annotator, String annotatorID,
            String annotationClass, String creationDate) {
        this.mentionid = mentionID;
        this.annotationText = annotationText;
//        this.spanstart = spanstart;
//        this.spanend = spanend;
        spanset.setOnlySpan(spanstart, spanend);
        this.annotator = annotator;
        this.annotatorid = annotatorID;
        this.annotationclass = annotationClass;
        this.creationDate = creationDate;

    }

    public AnnotationRelationship addComplex(String name, Annotation annot) {
        AnnotationRelationship rel = new AnnotationRelationship(name);
        rel.addLinked(new AnnotationRelationshipDef(annot));
        if (this.relationships == null) {
            this.relationships = new Vector<AnnotationRelationship>();
        }
        this.relationships.add(rel);
        return rel;
    }

    /**set visible flag to this annotation.*/
    public void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }

    @Override
    public String toString() {
        return annotationText.toLowerCase();
    }

    /**list all annotation texts. If there is just one span, just list it; But
     * if there are more than one spans, we need put them together by using " . "
     */
    public String getTexts(){
        return this.annotationText;
    }

    public int compareTo(Object object) {
        return this.annotationText.compareTo(object.toString());
    }

    /**quicly compare two annotations, if their basic attributes are same,
     * then return true, otherwise, return false.
     */
    public boolean quickcompareTo(Annotation ann, String filename) {
        boolean flag = false;

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        try {


            // if comparative object is null
            if (ann == null) {
                if (this == null) {
                    return true;
                } else {
                    return false;
                }
            }

            // comparing annotation text
            if (ann.annotationText == null) {
                if (this.annotationText != null) {
                    return false;
                }
            } else {
                if (this.annotationText == null) {
                    return false;
                } else if (this.annotationText.compareTo(ann.annotationText) != 0) {
                    return false;
                }
            }

            // comparing annotation spans
            if ( !ann.spanset.equals(this.spanset) )
                return false;

            // comparing annotation class
            if (ann.annotationclass == null) {
                if (this.annotationclass != null) {
                    return false;
                }
            } else {
                if (this.annotationclass == null) {
                    return false;
                } else if (this.annotationclass.compareTo(ann.annotationclass) != 0) {
                    return false;
                }
            }

            // comparing comments
            if (ann.comments == null) {
                if (this.comments != null) {
                    return false;
                }
            } else {
                if (this.comments == null) {
                    return false;
                } else if (this.comments.compareTo(ann.comments) != 0) {
                    return false;
                }
            }

            // complex relationship
            if (ann.relationships == null) {
                if (this.relationships != null) {
                    return false;
                }
            } else {
                if (this.relationships == null) {
                    return false;
                } else {
                    if (ann.relationships.size() != this.relationships.size()) {
                        return false;
                    }
                    // compare each sub item
                    for (int i = 0; i < ann.relationships.size(); i++) {
                        if (ann.relationships.get(i) == null) {
                            if (this.relationships.get(i) != null) {
                                return false;
                            }
                        } else {
                            if (this.relationships.get(i) == null) {
                                return false;
                            } else if (this.relationships.get(i).getMentionSlotID().compareTo(
                                    ann.relationships.get(i).getMentionSlotID()) != 0) {
                                return false;
                            } else {
                                int size = this.relationships.get(i).linkedAnnotations.size();
                                for (int j = 0; j < size; j++) {
                                    int uid2 = this.relationships.get(i).linkedAnnotations.get(j).linkedAnnotationIndex;
                                    int uid1 = ann.relationships.get(i).linkedAnnotations.get(j).linkedAnnotationIndex;

                                    if (uid2 == uid1) {
                                        continue;
                                    }
                                    Annotation u1;
                                    Annotation u2;
                                    u1 = depot.getAnnotationByUnique(filename, uid1);
                                    u2 = depot.getAnnotationByUnique(filename, uid2);
                                    if ((u1 == null) && (u2 == null)) {
                                        log.LoggingToFile.log(Level.SEVERE, "error 1101032050::1 could not found annotation by uid and filename");
                                        continue;
                                    }
                                    if ((u1 == null) || (u2 == null)) {
                                        log.LoggingToFile.log(Level.SEVERE, "error 1101032050::2 could not found annotation by uid and filename");
                                        continue;
                                    }

                                    //System.out.println("by uid u1="+u1.toString() + " / u2 ="+u2.toString());


//                                    if ((u1.spanstart != u2.spanstart) || (u1.spanend != u2.spanend)) {
                                    if ( !(u1.spanset.equals( u2.spanset)) ) {
                                        //System.out.println("u1="+u1.annotationText + "  u2="+ u2.annotationText);
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }


            // compare normal relationship / attributes
            if (ann.attributes == null) {
                if (this.attributes != null) {
                    return false;
                }
            } else {
                if (this.attributes == null) {
                    return false;
                } else {
                    int size1 = ann.attributes.size();
                    int size2 = this.attributes.size();
                    if (size1 != size2) {
                        return false;
                    } else {
                        for (int ir = 0; ir < size1; ir++) {
                            AnnotationAttributeDef nr1 = ann.attributes.get(ir);
                            AnnotationAttributeDef nr2 = this.attributes.get(ir);

                            if (nr1 == null) {
                                if (nr2 != null) {
                                    return false;
                                }
                            } else {
                                if (nr2 == null) {
                                    return false;
                                } else {
                                    if (nr1.name == null) {
                                        if (nr2.name != null) {
                                            return false;
                                        }
                                    } else {
                                        if (nr2.name == null) {
                                            return false;
                                        } else if (nr1.name.compareTo(nr2.name) != 0) {
                                            return false;
                                        }
                                    }



                                    if (nr1.value == null) {
                                        if (nr2.value != null) {
                                            return false;
                                        }
                                    } else {
                                        if (nr2.value == null) {
                                            return false;
                                        }
                                    }

                                    
                                    if (nr1.value.trim().compareTo( nr2.value.trim() ) != 0 ) {
                                        return false;
                                    }                                    
                                }
                            }
                        }
                    }

                }
            }

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1101032010:: fail to compare two annotations:"
                    + ex.toString());
            return false;
        }

        return true;
    }

    /**check whether two annotations are duplicates. chris Leng 5/26/2011*/
    public boolean isDuplicate(Annotation ann, String filename) {
        //boolean flag = false;

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        try {


            // if comparative object is null
            if (ann == null) {
                if (this == null) {
                    return true; // they are duplicates if all of them are null;
                } // Actually, THEY ARE GHOST
                else {
                    return false; // they are NOT duplicates
                }
            }

            // comparing annotation span
            if ( !ann.spanset.equals( this.spanset )) {
                return false;
            }

            // comparing annotation text
            if (ann.annotationText == null) {
                if (this.annotationText != null) {
                    return false;
                }
            } else {
                if (this.annotationText == null) {
                    return false;
                } else if (this.annotationText.compareTo(ann.annotationText) != 0) {
                    return false;
                }
            }



            // comparing annotation class
            if (env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameclasses) {
                if (ann.annotationclass == null) {
                    if (this.annotationclass != null) {
                        return false;  // all of their classes are null
                    }
                } else {
                    if (this.annotationclass == null) {
                        return false;
                    } else if (this.annotationclass.compareTo(ann.annotationclass) != 0) {
                        return false;
                    }
                }
            }


            //if((this.annotationText.compareTo("DATE")==0)&&(ann.annotationText.compareTo("DATE")==0))
            //    System.out.println("hello");

            // == DISABLED AS USER DON'T WANT TO COMPARE ANNOTATIONS BY COMMENTS
            // == JUNE 14, 2011
            // comparing comments
            /*if(env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samecomments)
            {
            // comment to one of them is null
            if((ann.comments==null)&&( this.comments!=null ))
            {
            if(this.comments.trim().length()>0)
            return false; // they are different, if comment of compared annotation
            // is null and comment of this annotation
            // is NOT "" or " ", etc;
            }

            // comment to one of them is null
            if((ann.comments!=null)&&( this.comments==null ))
            {
            if(ann.comments.trim().length()>0)
            return false; // Comment of this annotation
            // is NOT "" or " ", etc
            }


            if((ann.comments!=null)&&( this.comments!=null ))
            {
            if ( this.comments.trim().compareTo(ann.comments.trim()) != 0 )
            return false; // these two annotation are different if
            // they have different comments.
            }
            }*/
            // == DISABLED END


            // complex relationship
            if (env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_samerelationships) {
                if ((ann.relationships == null) && (this.relationships != null)) {
                    if (this.relationships.size() > 0) {
                        return false;
                    }
                }

                if ((ann.relationships != null) && (this.relationships == null)) {
                    if (ann.relationships.size() > 0) {
                        return false;
                    }
                }



                if ((ann.relationships != null) && (this.relationships != null)) {
                    // compare the amount of items to their relationships
                    if (ann.relationships.size() != this.relationships.size()) {
                        return false;
                    }

                    // compare each sub item for these items
                    try {

                        for (int i = 0; i < ann.relationships.size(); i++) {
                            if (ann.relationships.get(i) == null) {
                                if (this.relationships.get(i) != null) {
                                    return false;
                                }
                            } else {
                                if (this.relationships.get(i) == null) {
                                    return false;
                                } else if (this.relationships.get(i).getMentionSlotID().compareTo(
                                        ann.relationships.get(i).getMentionSlotID()) != 0) {
                                    return false;
                                } else {
                                    int size = this.relationships.get(i).linkedAnnotations.size();
                                    for (int j = 0; j < size; j++) {
                                        int uid2 = this.relationships.get(i).linkedAnnotations.get(j).linkedAnnotationIndex;
                                        int uid1 = ann.relationships.get(i).linkedAnnotations.get(j).linkedAnnotationIndex;

                                        if (uid2 == uid1) {
                                            continue;
                                        }
                                        Annotation u1;
                                        Annotation u2;
                                        u1 = depot.getAnnotationByUnique(filename, uid1);
                                        u2 = depot.getAnnotationByUnique(filename, uid2);
                                        if ((u1 == null) && (u2 == null)) {
                                            log.LoggingToFile.log(Level.SEVERE, "error 1101032050::1 could not found annotation by uid and filename");
                                            continue;
                                        }
                                        if ((u1 == null) || (u2 == null)) {
                                            log.LoggingToFile.log(Level.SEVERE, "error 1101032050::2 could not found annotation by uid and filename");
                                            continue;
                                        }

                                        //System.out.println("by uid u1="+u1.toString() + " / u2 ="+u2.toString());


                                        if ( !u1.spanset.equals( u2.spanset) ) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        log.LoggingToFile.log(Level.SEVERE, "error 1105261327::" + ex.toString()
                                + "\n annotation 1: " + ann.annotationText);
                    }
                }
            }



            // ### ### compare normal relationship / attributes
            if (env.Parameters.DuplicatesAndGhostSeeker.duplicates_with_sameattributes) {

                if ((ann.attributes == null) && (this.attributes != null)) {
                    if (this.attributes.size() > 0) {
                        return false;
                    }
                }

                if ((ann.attributes != null) && (this.attributes == null)) {
                    if (ann.attributes.size() > 0) {
                        return false;
                    }
                }

                if ((ann.attributes != null) && (this.attributes != null)) {
                    try {
                        int size1 = ann.attributes.size();
                        int size2 = this.attributes.size();
                        if (size1 != size2) {
                            return false;
                        } else {
                            for (int ir = 0; ir < size1; ir++) {
                                AnnotationAttributeDef nr1 = ann.attributes.get(ir);
                                AnnotationAttributeDef nr2 = this.attributes.get(ir);

                                if (nr1 == null) {
                                    if (nr2 != null) {
                                        return false;
                                    }
                                } else {
                                    if (nr2 == null) {
                                        return false;
                                    } else {
                                        if (nr1.name == null) {
                                            if (nr2.name != null) {
                                                return false;
                                            }
                                        } else {
                                            if (nr2.name == null) {
                                                return false;
                                            } else if (nr1.name.compareTo(nr2.name) != 0) {
                                                return false;
                                            }
                                        }



                                        if (nr1.value == null) {
                                            if (nr2.value != null) {
                                                return false;
                                            }
                                        } else {
                                            if (nr2.value == null) {
                                                return false;
                                            }
                                        }
                                        
                                        if ( nr1.value.trim().compareTo( nr2.value.trim() ) != 0) {
                                            return false;
                                        }

                                        
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        log.LoggingToFile.log(Level.SEVERE, "error 1105261349::" + ex.toString());
                    }
                }
            }

        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1105261357:: fail to compare two annotations:"
                    + ex.getMessage());
            return false;
        }

        return true;
    }

    /**(Chris June 23, 2011)
     * This method is designed to compare current annotation with another
     * assigned annotation.
     *
     * @return  true means these two annotations are equal; false means these
     *          two annotations are NOT equal.
     */
    @Override
    public boolean equals(Object object) {
        // #### 1 :: compare if one of them are null,
        // try to avoid errors by these steps before really starting comparing
        if ((this == null) && (object == null)) {
            return true;
        }
        if ((this == null) && (object != null)) {
            return false;
        }
        if ((this != null) && (object == null)) {
            return false;
        }

        // #### 2 :: if the object is not an instance of annotation, it's NOT
        // comparable to current instance as they belong to different classes.
        if (!(object instanceof Annotation)) {
            return false;
        }


        //#### 3 :: covert this object to instance of "Annotation" for
        // subsequent processing
        Annotation compareAgainst = (Annotation) object;


        //#### 4 :: comprea their annotation text
        //#### 4.1 :: if only one of their "annotaiontext" is NULL, they are
        //            different to each other. So we return false
        if ((this.annotationText != null) && (compareAgainst.annotationText == null)) {
            return false;
        }
        if ((this.annotationText == null) && (compareAgainst.annotationText != null)) {
            return false;
        }

        //#### 4.2 if both of they are not null
        if ((this.annotationText != null) && (compareAgainst.annotationText != null)) {
            if (!this.annotationText.toLowerCase().equals(compareAgainst.annotationText.toLowerCase())) {
                return false;
            }
        }


        //#### 5 compare
        if ((this.annotationText != null) && (compareAgainst.annotationText == null)) {
            return false;
        }

        if (!this.annotationclass.equals(compareAgainst.annotationclass)) {
            return false;
        }

        // compare "annotator"
        if (this.annotator == null) {
            if (compareAgainst.annotator != null) {
                return false;
            }
        } else if (!this.annotator.equals(compareAgainst.annotator)) {
            return false;
        }

        if (!this.creationDate.equals(compareAgainst.creationDate)) {
            return false;
        }

        try {
            if (!this.spanset.equals(compareAgainst.spanset)) {
                return false;
            }
        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "1201190527:: one of their" +
                    " spanset is null while compare two annotations.");
        }


        //}catch(Exception ex){
        //    return false;
        //}
        return true;
    }

    /**Prepare the list that will be listed on the editor. It contains the
     * attribute names and their values.*/
    public Vector<iListable> getAttributesForShow() {

        //Get attributes of this annotation
        Vector<iListable> list = new Vector<iListable>();
        if (attributes != null) {
            for (AnnotationAttributeDef rel : attributes) {
                list.add(rel);
            }
        }

        // Go through each attribute... if it is in the Annotation then get the
        // current value, and set that as the selected value else just add it
        // with no selected value.
        Vector<iListable> finalList = new Vector<iListable>();
        String classname = this.annotationclass;

        if ((classname == null) || (classname.trim().length() < 1)) {
            return null;
        }

        // get class of current annotation
        resultEditor.annotationClasses.Depot classdepot =
                new resultEditor.annotationClasses.Depot();
        AnnotationClass currentClass =
                classdepot.getAnnotatedClass(annotationclass);

        // if the class of this annotation is allowed to use public attributes:
        if (currentClass.inheritsPublicAttributes) {
            
            for (relationship.simple.dataTypes.AttributeSchemaDef attribute : env.Parameters.AttributeSchemas.getAttributes()) {
                boolean accountedFor = false;
                for (iListable listEntry : list) {
                    if ((((AnnotationAttributeDef) listEntry).name == null)
                            || (((AnnotationAttributeDef) listEntry).value == null)) {
                        continue;
                    }
                    if (listEntry.getSelectedItem(0).equals(attribute.getName())) {
                        accountedFor = true;
                        attribute.setString(1, listEntry.getSelectedItem(1));
                        finalList.add(attribute);
                    }
                }
                if (!accountedFor) {
                    finalList.add(attribute);
                }
            }

        }

        for (relationship.simple.dataTypes.AttributeSchemaDef attribute : currentClass.privateAttributes) {
            boolean accountedFor = false;
            for (iListable listEntry : list) {
                if ((((AnnotationAttributeDef) listEntry).name == null)
                        || (((AnnotationAttributeDef) listEntry).value == null)) {
                    continue;
                }
                if (listEntry.getSelectedItem(0).equals(attribute.getName())) {
                    accountedFor = true;
                    attribute.setString(1, listEntry.getSelectedItem(1));
                    finalList.add(attribute);
                }
            }
            if (!accountedFor) {
                finalList.add(attribute);
            }
        }



        //Get all Normal Relationships that Aren't in the current Schema
        if (attributes != null) {

            for (AnnotationAttributeDef attribute : attributes) {
                if (((AnnotationAttributeDef) attribute).name == null
                        || ((AnnotationAttributeDef) attribute).value == null) {
                    continue;
                }
                boolean accountedFor = false;
                for (relationship.simple.dataTypes.AttributeSchemaDef attributedef : currentClass.privateAttributes) {
                    if (attributedef.getName().equals(attribute.name)) {
                        accountedFor = true;
                    }
                }

                for (relationship.simple.dataTypes.AttributeSchemaDef attributedef : env.Parameters.AttributeSchemas.getAttributes()) {
                    if (attributedef.getName().equals( attribute.name)) {
                        accountedFor = true;
                    }
                }

                if (currentClass.inheritsPublicAttributes) {
                }

                if (!accountedFor) {
                    finalList.add(new relationship.simple.dataTypes.AttributeSchemaDef( attribute.name, attribute.value));
                }
            }
        }


        //Get all Normal Relationships that Aren't in the current Schema
        if (attributes != null) {
        }



        return finalList;
    }

    /**
     * @return the verified
     */
    public boolean isVerified() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}



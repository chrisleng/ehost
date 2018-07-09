/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.annotations;

import imports.importedXML.eXMLFile;
import java.io.File;
import java.util.Vector;

/**
 * import XML and PINS, then extract annotations from imported data.
 *
 * @author jianwei leng June 23, 2010 12:47am
 */
public class ImportAnnotation {

    /**read XML files and extract annotations from them.
     * @param XMLfiles  XML files for import and extraction.
     */
    public void XMLImporter(Vector XMLfiles){

        // validity check
        if( XMLfiles == null ) return;
        int size = XMLfiles.size();
        if ( size < 1 ) return;

        // handle each XML file in "XMLfiles"
        File f;
        for(int i=0;i<size;i++){

            try{
            f = (File)XMLfiles.get(i);

            // XML filename; example: 4.txt.knowtator.xml
            String filename = f.getName();

            // parsing the XML file and get information
            eXMLFile XMLfile = imports.ImportXML.readXMLContents( f );

            // assignate an unique annotaion index to each annotation in this article
            XMLfile = assignateAnnotationIndex( XMLfile );
                                   
            // extract annotations from this imported XML file
            XMLExtractor( XMLfile );
            }catch(Exception ex){
                System.out.println("Fail to reload annotation from a XML file!");
                System.out.println("Error Message:: " + ex.getMessage() );
            }
        }
    }



    /**
     * This method will extract annotaions from eXMLFiles.
     * @param XMLfile - the file to extract from.
     * @return
     */
    public void XMLExtractor(eXMLFile XMLfile){
        if(XMLfile == null)
            return;

        // XML filename; example: 4.txt.knowtator.xml
        String xmlfilename = XMLfile.filename.trim();
        // get file name of text source
        String filename = getXMLTextSource( xmlfilename );

        //Create an Article toReturn
        Article toReturn = new Article(filename);
        // make sure this article exists in annotation depot
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.articleInsurance(filename);
        //System.out.println(" + currentfile: [" + filename +"]");
        //boolean flag = false;
        //if(filename.trim().compareTo("record-3.txt")==0) {flag = true;System.out.println(" + got file record-3.txt"); }

        // get all XML annotation node of this XML file
        Vector<imports.importedXML.eAnnotationNode> annotations = XMLfile.annotations;
        //if(flag) System.out.println(" + + amount of annotations = " + annotations.size());
        // get all XML classmention node of this XML file
        Vector<imports.importedXML.eClassMention> classmentions = XMLfile.classMentions;
        //if(flag) System.out.println(" + + amount of classmentions = " + classmentions.size());
        // get all XML stringslot node of this XML file
        Vector<imports.importedXML.eStringSlotMention> stringslots =
                env.Parameters.AnnotationImportSetting.allowImportAttribute ?  XMLfile.stringSlotMentions: new Vector<imports.importedXML.eStringSlotMention>();
        //if(flag) System.out.println(" + + amount of stringslots = " + stringslots.size());
        // get all XML complexstringslot node of this XML file
        Vector<imports.importedXML.eComplexSlotMention> complexslotmentions =
                env.Parameters.AnnotationImportSetting.allowImportRelationship ? XMLfile.complexSlotMentions:new Vector<imports.importedXML.eComplexSlotMention>();
                
        //if(flag) System.out.println(" + + amount of complexslotmentions = " + complexslotmentions.size());

        // size of above vector
        int size_annotations   = annotations.size();
        int size_classmentions = classmentions.size();

        // go over all XML annotation node to extract information for annotation
        for(int i=0;i<size_annotations;i++) {

            // get a XML annotation node
            imports.importedXML.eAnnotationNode annotation = annotations.get(i);
            if ( annotation == null )   continue;

            // get annotation class
            String annotationClass = getAnnotationClass( annotation.mention_id , classmentions);
            // get normal relationship
            Vector<AnnotationAttributeDef> normalrelationship = searchNormalRelationship( annotation.mention_id, classmentions, stringslots );


            // get complex relationship
            Vector<AnnotationRelationship> complexRelationship = extractComplexRelationShip( annotation, annotations, classmentions, complexslotmentions );

            // ============================================================== //
            // - QUICK-INDEX1010091549
            // - here is used to check the imported annotation name and id for
            // - each imported annotations
            // System.out.println("annotator = [" + annotation.annotator + "]");
            // System.out.println("annotator.id = [" + annotation.annotator_id + "]");
            // ============================================================== //

                        
            boolean isProcessed = annotation.__isProcessed.toLowerCase().equals( "false" ) ? false : true;
            
            String adjudicationStatus = annotation.__adjudication_status;
            //System.out.println( annotation.__adjudication_status );
                    
            // record annotation information
            if( annotation.type == 5  ){
                recordAnnotationAdj( filename,
                    annotation.mention_id,
                    annotation.annotationText,
                    annotation.spanset,
                    annotationClass,
                    annotation.annotator,
                    annotation.annotator_id,
                    annotation.creationDate,
                    annotation.annotationComments, // all strings of annotation comment
                    annotation.verifierSuggestions, // insert verifier suggestion in comment
                    normalrelationship,
                    complexRelationship,
                    isProcessed,
                    adjudicationStatus,
                    annotation.uniqueIndex,
                    false // not verified
                );
            }else{
                recordAnnotation( filename,
                    annotation.mention_id,
                    annotation.annotationText,
                    annotation.spanset,
                    annotationClass,
                    annotation.annotator,
                    annotation.annotator_id,
                    annotation.creationDate,
                    annotation.annotationComments, // all strings of annotation comment
                    annotation.verifierSuggestions, // insert verifier suggestion in comment
                    normalrelationship,
                    complexRelationship,
                    isProcessed,
                    adjudicationStatus,
                    annotation.uniqueIndex,
                        false // not verified
                    
               );
            }
        }
    }

    /**
     * This method will extract annotaions from eXMLFiles.
     * @param XMLfile - the file to extract from.
     * @param record - true if you want to store the extracted information in static memory, false otherwise.
     * @return
     */
    public Article XMLExtractor(eXMLFile XMLfile, boolean record){
        if(XMLfile == null)
            return null;

        // XML filename; example: 4.txt.knowtator.xml
        String xmlfilename = XMLfile.filename.trim();
        // get file name of text source
        String filename = getXMLTextSource( xmlfilename );

        //Create an Article toReturn
        Article toReturn = new Article(filename);
        // make sure this article exists in annotation depot
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        if(record){
            depot.articleInsurance(filename);           
        }
        
        //System.out.println(" + currentfile: [" + filename +"]");


        // get all XML annotation node of this XML file
        Vector<imports.importedXML.eAnnotationNode> annotations = XMLfile.annotations;

        // get all XML classmention node of this XML file
        Vector<imports.importedXML.eClassMention> classmentions = XMLfile.classMentions;

        // get all XML stringslot node of this XML file
        Vector<imports.importedXML.eStringSlotMention> stringslots = XMLfile.stringSlotMentions;

        // get all XML complexstringslot node of this XML file
        Vector<imports.importedXML.eComplexSlotMention> complexslotmentions = XMLfile.complexSlotMentions;


        // size of above vector
        int size_annotations   = annotations.size();
        int size_classmentions = classmentions.size();

        // go over all XML annotation node to extract information for annotation
        for(int i=0;i<size_annotations;i++){

            // get a XML annotation node
            imports.importedXML.eAnnotationNode annotation = annotations.get(i);
            if ( annotation == null )   continue;

            // get annotation class
            String annotationClass = getAnnotationClass( annotation.mention_id , classmentions);
            // get normal relationship
            Vector<AnnotationAttributeDef> normalrelationship = searchNormalRelationship( annotation.mention_id, classmentions, stringslots );


            // get complex relationship
            Vector<AnnotationRelationship> complexRelationship = extractComplexRelationShip( annotation, annotations, classmentions, complexslotmentions );

            boolean isProcessed = annotation.__adjudication_status.toLowerCase().equals( "false" ) ? false : true;
            
            String adjudicationStatus = annotation.__adjudication_status;
            
            //System.out.println("annotator = [" + annotation.annotator + "]");
            //System.out.println("annotator.id = [" + annotation.annotator_id + "]");
            // record annotation information
            if(record) {
                recordAnnotation( filename,
                        annotation.mention_id,
                        annotation.annotationText,
                        annotation.spanset,
                        annotationClass,
                        annotation.annotator,
                        annotation.annotator_id,
                        annotation.creationDate,
                        annotation.annotationComments, // all strings of annotation comment
                        annotation.verifierSuggestions, // insert verifier suggestion in comment
                        normalrelationship,
                        complexRelationship,
                        isProcessed,
                        adjudicationStatus,
                        annotation.uniqueIndex,
                        false // not verified
                        );
            } else {
                Annotation toSet = new Annotation();
                
                toSet.mentionid = annotation.mention_id;
                toSet.annotationclass = annotationClass;
                toSet.annotationText = annotation.annotationText;
                toSet.comments = annotation.annotationComments;
                toSet.relationships = complexRelationship;
                toSet.attributes = normalrelationship;
                toSet.verifierSuggestion = annotation.verifierSuggestions;
                toSet.creationDate = annotation.creationDate;
                toSet.setAnnotator( annotation.annotator );
                toSet.annotatorid = annotation.annotator_id;
                toSet.spanset = annotation.spanset;                
                toSet.uniqueIndex = annotation.uniqueIndex;
                

                toReturn.addAnnotation(toSet);
            }
        }
        return toReturn;

    }


    
    /**Extract possible complex relationship(s) for current annotation.
     * @param   thisannotation  Current annotation in handling.
     * @param   annotations All XML annotation node of this XML file.
     */
    private Vector<AnnotationRelationship> extractComplexRelationShip(
            imports.importedXML.eAnnotationNode thisannotation,
            Vector<imports.importedXML.eAnnotationNode> annotations,
            Vector<imports.importedXML.eClassMention> classmentions,
            Vector<imports.importedXML.eComplexSlotMention> complexslotMentions
            ) {
        

        Vector<AnnotationRelationship>  complexrelationships = new Vector<AnnotationRelationship>();

        String mentionid = thisannotation.mention_id;

        //####-1- get possible hasSlotMention
        Vector<String> hasslotIDs = getHasSlotMentionIDs( mentionid, classmentions );
        if( ( hasslotIDs == null) || (hasslotIDs.size() < 1) )  
            return null;

        //System.out.println(" + To annotation-[" + thisannotation.annotationText + "]" + "has " + hasslotIDs.size() + " slots");
        //####-2- get all these slots by hasslotIDs
        for(String hasslotmentionid: hasslotIDs){
            //System.out.println(" - this slot id = ["+ hasslotmentionid + "]");

            //####-3- get its all complex relationships if it has
            imports.importedXML.eComplexSlotMention complexnode = getComplexNode( hasslotmentionid,  complexslotMentions );
            if ( complexnode != null ) {
                // 3.1 build the vector for recording, if has complex relationships
                


                String complexrelationshiptype = complexnode.mentionSLot_id;
                AnnotationRelationship complexrelationship =  new AnnotationRelationship(complexnode.mentionSLot_id);
                

                // get links(mentionid) to these linked annotations
                Vector<String> linkids = complexnode.complexSlotMentionValue_value;
                if( linkids != null ){
                    for(String linkid : linkids){
                        // to the mention id for linked annotation, get this annotation
                        imports.importedXML.eAnnotationNode linkedannotation = getLinkedAnnotationNode( linkid, annotations );
                        if( linkedannotation != null ){
                            int uniqueIndex = linkedannotation.uniqueIndex;                            

                            resultEditor.annotations.AnnotationRelationshipDef complex = new resultEditor.annotations.AnnotationRelationshipDef();
                            complex.linkedAnnotationIndex = uniqueIndex;
                            complex.linkedAnnotationText = linkedannotation.annotationText;
                            complex.mention = linkedannotation.mention_id;
                            complexrelationship.addLinked(complex);
                            complexrelationship.attributes.addAll( complexnode.attributes  );
                            
                        }
                    }
                }
                complexrelationships.add( complexrelationship );
            }
        }

        return complexrelationships;
    }

    
    /**In complex relationship, find linked annotation to a given link id number. */
    private imports.importedXML.eAnnotationNode getLinkedAnnotationNode(String linkid, Vector<imports.importedXML.eAnnotationNode> annotations){
        for( imports.importedXML.eAnnotationNode annotation: annotations ){
            if( annotation == null )    continue;
            if( annotation.mention_id.trim().compareTo(linkid.trim()) == 0 )
                return annotation;
        }
        return null;
    }


    /**To a given hasSlot Mention ID, try to get matched nodes of complex
     * relationship and extract complex relationship. */
    private imports.importedXML.eComplexSlotMention getComplexNode(
            String hasSlotMentionID,
            Vector<imports.importedXML.eComplexSlotMention> complexslotMentions ){
        
        for( imports.importedXML.eComplexSlotMention complexslotMention: complexslotMentions ) {
            if ( complexslotMention == null )   
                continue;
            String csm = complexslotMention.complexSlotMention;
            if(( csm == null )||(csm.trim().length()<1))
                continue;
            
            if(csm.trim().compareTo( hasSlotMentionID.trim() ) == 0){
                return complexslotMention;
            }
        }
        
        return null;
    }


    /**Get Normal relationship to a given annotaion*/
    private Vector<AnnotationAttributeDef> searchNormalRelationship( final String mentionid,
            final Vector<imports.importedXML.eClassMention> classmentions,
            final Vector<imports.importedXML.eStringSlotMention> stringslots
            )
    {
        
        // define for data gather and return
        Vector<AnnotationAttributeDef> toReturn = new Vector<AnnotationAttributeDef>();

        // validity check: to afferent parameter
        if ( ( classmentions == null ) || ( stringslots == null ) )
                return null;

        // get hasslotid if it has
        Vector<String> hasslotIDs  = getHasSlotMentionIDs( mentionid, classmentions );
        if ( hasslotIDs == null ) return null;
        
        // get normal relationships if we have
        for(String hasslotid: hasslotIDs ){
            AnnotationAttributeDef normalrelationship = getNormalRelationship( hasslotid, stringslots );
            if ( normalrelationship != null )
                toReturn.add(normalrelationship);
        }


        return toReturn;
    }


    /**to a given hasslotid from a classmention, get its normal relationship
     * name and value.
     */
    private AnnotationAttributeDef getNormalRelationship( String hasslotid, final Vector<imports.importedXML.eStringSlotMention> stringslots){
        AnnotationAttributeDef nr = new AnnotationAttributeDef();
        for(imports.importedXML.eStringSlotMention stringslot: stringslots){
            hasslotid = hasslotid.toLowerCase().trim();
            if( stringslot.stringSlotMention_id.toLowerCase().trim().compareTo( hasslotid ) == 0 ) {
                if(stringslot.mentionSLot_id == null || stringslot.mentionSLot_id.equals("")
                        ||stringslot.stringSlotMentionValue == null )
                    continue;
                nr.name = stringslot.mentionSLot_id.trim();
                nr.value = stringslot.stringSlotMentionValue;                
                return nr;
            }
        }
        return nr;
    }

    
    /**get all hasslot value to an annotation in its classmention*/
    private Vector<String> getHasSlotMentionIDs(String mentionid, final Vector<imports.importedXML.eClassMention> classmentions ){
        if( classmentions == null ) return null;
        if( classmentions.size() < 1 ) return null;     
        Vector<String> hasSlotmentionID = new Vector<String>();
        for(imports.importedXML.eClassMention classmention:classmentions) {
            String id = classmention.classMentionID.trim().toLowerCase();
            if ( mentionid.toLowerCase().trim().compareTo( id ) == 0 ) {
                return classmention.hasSlotMention_id;
            }
        }
        return hasSlotmentionID;
    }



    /**Get annotation class by a mentionid */
    private String getAnnotationClass(String mentionid, final Vector<imports.importedXML.eClassMention> classmentions){

        if(classmentions == null)
            return "";

        int size = classmentions.size();
        for( int i=0;i<size;i++ ){
            imports.importedXML.eClassMention classmention = classmentions.get(i);
            if ( classmention == null ) continue;

            // compare their id with the designated mentionid
            String id = classmention.classMentionID.trim().toLowerCase();
            if(id.compareTo(mentionid.trim().toLowerCase())==0)
                return classmention.mentionClassID.trim();
        }

        return "";
    }


    /**save annotation to annotation depot in resultEditor.annotations.Depot.
     * @param filename  This is the filename of the text source, it will be
     * used to located which article space in the depot the annotation will
     * be saved into.
     */
    private void recordAnnotation(String filename, String mentionid,
            String annotationText,
            SpanSetDef spanset,
            String annotationClass,
            String annotator, String annotatorid,
            String creationDate,
            String annotationComment,
            Vector<String> verifierSuggestions,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            boolean isProcessed,
            String adjudicationStatus,
            int uniqueindex,
            boolean isVerified
            ){
        // record into annotation depot
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        depot.recordAnnotation( 
                filename, 
                mentionid,
                annotationText,
                spanset,
                annotationClass,
                annotator, annotatorid,
                creationDate,
                annotationComment, verifierSuggestions,
                normalRelationships,
                complexRelationships,
                isProcessed,
                adjudicationStatus,
                uniqueindex,                
                isVerified
                );
    }


    private void recordAnnotationAdj( String filename, String mentionid,
            String annotationText,
            SpanSetDef spanset,
            String annotationClass,
            String annotator, String annotatorid,
            String creationDate,
            String annotationComment,
            Vector<String> verifierSuggestions,
            Vector<AnnotationAttributeDef> normalRelationships,
            Vector<AnnotationRelationship> complexRelationships,
            boolean isProcessed,
            String adjudicationStatus,
            int uniqueindex,
            boolean isVerified
            ){
        
        
        // record into annotation depot
        adjudication.data.AdjudicationDepot depotofAdj = new adjudication.data.AdjudicationDepot();
        depotofAdj.articleInsurance(filename);
        depotofAdj.recordAnnotation( 
                filename, 
                mentionid,
                annotationText,
                spanset,
                annotationClass,
                annotator, annotatorid,
                creationDate,
                annotationComment, verifierSuggestions,
                normalRelationships,
                complexRelationships,
                isProcessed,
                adjudicationStatus,
                uniqueindex
                );
    }
    
    /**Get file name of text source from a XML filename/
     * @param xmlfilename   filename of XML file; example: "4.txt.knowtator.xml"
     */
    private String getXMLTextSource( String xmlfilename ){
        String filename = xmlfilename.replaceAll("\\.knowtator\\.xml", " " ).trim();
        return filename;
    }



    // assignate an unique annotaion index to each annotation in raw imported xml data
    public eXMLFile assignateAnnotationIndex( eXMLFile XMLfile ){
        if( XMLfile.annotations == null )   
            return XMLfile;

        for( imports.importedXML.eAnnotationNode annotationnode: XMLfile.annotations ){
            if (annotationnode == null) continue;
            annotationnode.uniqueIndex = resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
        }
        
        return XMLfile;
    }

    
    
}

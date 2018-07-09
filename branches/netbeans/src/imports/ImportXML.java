/*
 * read annotation information from XML file and imported into memory.
 */

package imports;

import adjudication.parameters.Paras;
import imports.importedXML.eAnnotationNode;
import imports.importedXML.eStringSlotMention;
import imports.importedXML.eXMLFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import resultEditor.annotations.AnnotationAttributeDef;
import resultEditor.annotations.SpanSetDef;


/**
 * Import and handle annotation information from XML(knownotar format).<p>
 * 1. read and load annotations from XML file.<p>
 * 2. manage loaded annotations information in static vector.<p>
 * 3. append new annotations into memory.<p>
 *
 * @author Jianwei Leng
 */
public class ImportXML{

    // static vector: for return results of query from result editor
    //private static Vector<eQueryReturn> QueryReturns = new Vector<eQueryReturn>();

    // static memory all imported XML annotation informations
    private static Vector<eXMLFile> IMPORTEDXMLs  = new Vector<eXMLFile>();

    // index of latest selected annotation in list of multiple annotations
    // in result editor
    //private static int latestUsedIndex_inQueryReturns;

    // the text file latest got hown in result editor
    //private static File latestShowedTxtFile;





    /**clean all imported XML data which stored in the static memory in format
     * of arraylist, items are object of class "annotations"*/
    public static void cleanMemory(){
        IMPORTEDXMLs.clear();
    }

    //public static void setLatestUsedIndex_inQueryReturns(int _index){
    //    latestUsedIndex_inQueryReturns = _index;
    //}
    //public static int getLatestUsedIndex_inQueryReturns(){
    //    return latestUsedIndex_inQueryReturns;
    //}
    //public static String getLatestAccessedTxtFilename(){
    //    return latestShowedTxtFile.getName();
    //}
   // public static File getLatestAccessedTxtFile(){
   //     return latestShowedTxtFile;
    //}

    //public static Vector<eQueryReturn> getLatestQueryReturns(){
    //    return QueryReturns;
    //}




    /**Return how many xml files got imported. This is also the size of the
     * vector which storages imported xml information.
     * @return size type is "int"; How many xml files got imported. This is also
     * the size of the vector which storages imported xml information.
     */
    public static int size(){
        return IMPORTEDXMLs.size();
    }

    /**To an index number of arraylist "IMPORTEDXML", return its filename.<p>
     * @return  String  the simple file name
     *          null    while the arraylist is empty or the index
     *                  is out of array.<p>
     */
    public static String getFileName(int _index){
        if (_index > size() - 1)
            return null;
        eXMLFile qrs = IMPORTEDXMLs.get(_index);
        return qrs.filename;
    }
    public static imports.importedXML.eXMLFile getAllXMLData(int _index)
    {
        if (_index > size() - 1)
            return null;
        eXMLFile qrs = IMPORTEDXMLs.get(_index);
        return qrs;
    }

    /**return arraylist of all annotation elements to a specific file; we
     * choose the file here by its index.<p>
     */
    public static Vector<eAnnotationNode> getAnnotations(int _index){
        if (_index > size() - 1)
            return null;
        eXMLFile qr = IMPORTEDXMLs.get(_index);
        return qr.annotations;
    }

    /***/
    private static void recordAnnotation(eXMLFile _eas){
        IMPORTEDXMLs.add(_eas);
    }

    /** get adjudication setting */
    private static void getAdjudicationSetting(Element _root) {
        
        // get root of adjudication setting in the xml 
        Element elementAdj_root = _root.getChild("eHOST_Adjudication_Status");
        if (elementAdj_root != null) {
            
            // get entrance of all selected annotators
            Element elementAdj_annotators = elementAdj_root.getChild("Adjudication_Selected_Annotators");
            if (elementAdj_annotators != null) {
                
                // get all selected annotators
                List list_annotators = elementAdj_annotators.getChildren("Annotator");
                if (list_annotators != null) {

                    adjudication.parameters.Paras.removeAnnotators();
                    
                    for (int i = 0; i < list_annotators.size(); i++) {
                        Element annotator = (Element) list_annotators.get(i);
                        if (annotator != null) {
                            String annotatorname = annotator.getText();
                            if (annotatorname != null) {
                                adjudication.parameters.Paras.addAnnotator(annotatorname);
                            }
                        }
                    }
                }
            }
            
            // get entrance of all selected annotators
            Element elementAdj_classes = elementAdj_root.getChild("Adjudication_Selected_Classes");
            if (elementAdj_classes != null) {
                
                // get all selected annotators
                List list_classes = elementAdj_classes.getChildren("Class");
                if (list_classes != null) {

                    adjudication.parameters.Paras.removeClasses();
                    
                    for (int i = 0; i < list_classes.size(); i++) {
                        Element classname = (Element) list_classes.get(i);
                        if (classname != null) {
                            String str_classname = classname.getText();
                            if (str_classname != null) {
                                adjudication.parameters.Paras.addClass(str_classname);
                            }
                        }
                    }
                }
            }
            
            // get entrance of other adjudication settings
            Element elementAdj_others = elementAdj_root.getChild("Adjudication_Others");
            if (elementAdj_others != null) {
                
                Element elementAdj_others_CHECK_OVERLAPPED_SPANS = elementAdj_others.getChild("CHECK_OVERLAPPED_SPANS");
                if( elementAdj_others_CHECK_OVERLAPPED_SPANS != null ){
                    String str = elementAdj_others_CHECK_OVERLAPPED_SPANS.getText();
                    if( str != null ){
                        Paras.setAnnotationCheckingParameters("CHECK_OVERLAPPED_SPANS", str);
                    }
                }
                
                Element elementAdj_others_CHECK_ATTRIBUTES = elementAdj_others.getChild("CHECK_ATTRIBUTES");
                if( elementAdj_others_CHECK_ATTRIBUTES != null ){
                    String str = elementAdj_others_CHECK_ATTRIBUTES.getText();
                    if( str != null ){
                        Paras.setAnnotationCheckingParameters("CHECK_ATTRIBUTES", str);
                    }
                }
                
                Element elementAdj_others_CHECK_RELATIONSHIP = elementAdj_others.getChild("CHECK_RELATIONSHIP");
                if( elementAdj_others_CHECK_RELATIONSHIP != null ){
                    String str = elementAdj_others_CHECK_RELATIONSHIP.getText();
                    if( str != null ){
                        Paras.setAnnotationCheckingParameters("CHECK_RELATIONSHIP", str);
                    }
                }
                
                Element elementAdj_others_CHECK_CLASS = elementAdj_others.getChild("CHECK_CLASS");
                if( elementAdj_others_CHECK_CLASS != null ){
                    String str = elementAdj_others_CHECK_CLASS.getText();
                    if( str != null ){
                        Paras.setAnnotationCheckingParameters("CHECK_CLASS", str);
                    }
                }
                
                Element elementAdj_others_CHECK_COMMENT = elementAdj_others.getChild("CHECK_COMMENT");
                if( elementAdj_others_CHECK_COMMENT != null ){
                    String str = elementAdj_others_CHECK_COMMENT.getText();
                    if( str != null ){
                        Paras.setAnnotationCheckingParameters("CHECK_COMMENT", str);
                    }
                }
            }
        }
    }

    /**
     * to a given file in format of <code>File</code>, open it and read
     * nodes of element "annotation", nodes of element "classmention".<p>
     *
     * @return  null
     */
    public static eXMLFile readXMLContents( File _file ){
        if ( _file == null ) 
            return null;
        //Logs.ShowLogs.printInfoLog("openning xml file for annotation import: " + _file.getAbsolutePath() );

        eXMLFile eas = new eXMLFile();
        eas.absoluteFilename = _file.getAbsolutePath().trim();
        eas.filename = _file.getName().trim();

        //boolean debug = false;
        //if( eas.filename.trim().compareTo("record-3.txt.knowtator.xml")==0 ) debug = true;
        //if(debug) System.out.println(" = current xml file : [" + eas.filename + "]");

        String mentionSlot_id = "";

        try{
            //##-1- initialize for XML reader
            SAXBuilder sb = new SAXBuilder();
            if(sb==null){
                System.out.println("fail to create an instance of SAX Builder for XML importing.");
                return null;
            }
            
            //constract doc object
            Document doc = sb.build( _file );
            if( doc == null ){
                System.out.println("fail to create SAX Document for XML importing.");
                return null;
            }
            Element root = doc.getRootElement(); // get element of root

            
            getAdjudicationSetting( root );
            
            
            //##-2- get all node of annotations
            List list = root.getChildren("annotation");//get all element of
            
            if (list!=null) {

                for(int i=0;i<list.size();i++){
                    String spannedText = null, creationdate = null, annotatorID=null, annotatorValue=null;
                    Element annotations = (Element) list.get(i);
                    if( annotations != null ) {
                        Element mention = annotations.getChild("mention");
                        String mentionID = mention.getAttributeValue("id");

                        Element annotator = annotations.getChild("annotator");
                        if( annotator != null ){
                            annotatorID = annotator.getAttributeValue("id");
                            annotatorValue = annotator.getText();
                        }
                        

                        // get comment, it maybe includes one or more verifier suggestion.
                        String commentText = "";
                        Element comment = annotations.getChild("annotationComment");
                        if( comment != null ){
                            commentText = comment.getText();
                            //System.out.println(" - " + commentText);
                        }

                        // get adjudicationstatus
                        String adjudication_status = "NOBODY";
                        //Element element_adjudicationStatus = annotations.getChild("AdjudicationStatus");
                        //if( element_adjudicationStatus != null ){
                        //    adjudication_status = element_adjudicationStatus.getText();
                        //    //System.out.println(" - " + commentText);
                        //}
                        
                        //System.out.println(" adjudication_status = " + adjudication_status);
                        
                        // get adjudicationstatus
                        String isProcessed = "false";
                        //Element element_isProcessed = annotations.getChild("processed");
                        //if( element_isProcessed != null ){
                        //    isProcessed = element_isProcessed.getText();
                            
                        //}
                        //System.out.println("isprocessed = " + isProcessed );
                        
                        
                        // get spanset
                        SpanSetDef spanset = new SpanSetDef();
                        try{
                            List spans = annotations.getChildren("span");
                            if(spans!=null){
                                for(Object spanObj : spans){
                                    if(spanObj == null)
                                        continue;
                                    Element span = (Element) spanObj;
                                    String start = span.getAttributeValue("start");
                                    String end = span.getAttributeValue("end");

                                    int spanstart = Integer.valueOf(start);
                                    int spanend = Integer.valueOf(end);

                                    spanset.addSpan(spanstart, spanend);
                                }
                            }

                        }catch(Exception e){                            
                            logs.ShowLogs.printWarningLog("importXML.java lost a span position while load "
                                        + mentionID);
                        }

                        Element spannedTexts = annotations.getChild("spannedText");
                        if(spannedTexts != null) spannedText = spannedTexts.getText();

                        Element creationdates = annotations.getChild("creationDate");
                        if( creationdates != null ) creationdate = creationdates.getText();
                        

                        //Logs.ShowLogs.printInfoLog("mention id: " + mentionID );
                        //Logs.ShowLogs.printInfoLog("annotator id: " + annotatorID );
                        //Logs.ShowLogs.printInfoLog("annotator value: " + annotatorValue );
                        //Logs.ShowLogs.printInfoLog("span start: " + start );
                        //Logs.ShowLogs.printInfoLog("span end: " + end );
                        //Logs.ShowLogs.printInfoLog("spannedText: " + spannedText );
                        //Logs.ShowLogs.printInfoLog("-----------------------------------");

                        // System.out.println("spanset:"+spanset.toHTML());
                        // send into static memory
                        eAnnotationNode ea = new eAnnotationNode( mentionID, spannedText,
                                spanset,
                                annotatorValue, // annotator
                                annotatorID,
                                creationdate,
                                commentText,                                
                                isProcessed,adjudication_status 
                           
                                );

                        eas.annotations.add(ea);

                        //System.out.println("span start: " + start );
                        //System.out.println("span end: " + end );
                        //System.out.println("spannedText: " + spannedText );
                        //System.out.println("-----------------------------------");
                    }
                }
            }

            list = root.getChildren("adjudicating");//get all element of
            
            if (list!=null) {

                for(int i=0;i<list.size();i++){
                    String spannedText = null, creationdate = null, annotatorID=null, annotatorValue=null;
                    Element annotations = (Element) list.get(i);
                    if( annotations != null ) {
                        Element mention = annotations.getChild("mention");
                        String mentionID = mention.getAttributeValue("id");

                        Element annotator = annotations.getChild("annotator");
                        if( annotator != null ){
                            annotatorID = annotator.getAttributeValue("id");
                            annotatorValue = annotator.getText();
                        }

                        // get comment, it maybe includes one or more verifier suggestion.
                        String commentText = "";
                        Element comment = annotations.getChild("annotationComment");
                        if( comment != null ){
                            commentText = comment.getText();
                            //System.out.println(" - " + commentText);
                        }

                        // get adjudicationstatus
                        String adjudication_status = "NOBODY";
                        Element element_adjudicationStatus = annotations.getChild("AdjudicationStatus");
                        if( element_adjudicationStatus != null ){
                            adjudication_status = element_adjudicationStatus.getText();
                            //System.out.println(" - " + commentText);
                        }
                        
                        //System.out.println(" adjudication_status = " + adjudication_status);
                        
                        // get adjudicationstatus
                        String isProcessed = "false";
                        Element element_isProcessed = annotations.getChild("processed");
                        if( element_isProcessed != null ){
                            isProcessed = element_isProcessed.getText();
                            
                        }
                        //System.out.println("isprocessed = " + isProcessed );
                        
                        
                        // get spanset
                        SpanSetDef spanset = new SpanSetDef();
                        try{
                            List spans = annotations.getChildren("span");
                            if(spans!=null){
                                for(Object spanObj : spans){
                                    if(spanObj == null)
                                        continue;
                                    Element span = (Element) spanObj;
                                    String start = span.getAttributeValue("start");
                                    String end = span.getAttributeValue("end");

                                    int spanstart = Integer.valueOf(start);
                                    int spanend = Integer.valueOf(end);

                                    spanset.addSpan(spanstart, spanend);
                                }
                            }

                        }catch(Exception e){                            
                            logs.ShowLogs.printWarningLog("importXML.java lost a span position while load "
                                        + mentionID);
                        }

                        Element spannedTexts = annotations.getChild("spannedText");
                        if(spannedTexts != null) spannedText = spannedTexts.getText();

                        Element creationdates = annotations.getChild("creationDate");
                        if( creationdates != null ) creationdate = creationdates.getText();
                        
                        


                        //Logs.ShowLogs.printInfoLog("mention id: " + mentionID );
                        //Logs.ShowLogs.printInfoLog("annotator id: " + annotatorID );
                        //Logs.ShowLogs.printInfoLog("annotator value: " + annotatorValue );
                        //Logs.ShowLogs.printInfoLog("span start: " + start );
                        //Logs.ShowLogs.printInfoLog("span end: " + end );
                        //Logs.ShowLogs.printInfoLog("spannedText: " + spannedText );
                        //Logs.ShowLogs.printInfoLog("-----------------------------------");

                        // System.out.println("spanset:"+spanset.toHTML());
                        // send into static memory
                        eAnnotationNode ea = new eAnnotationNode( mentionID, spannedText,
                                spanset,
                                annotatorValue, // annotator
                                annotatorID,
                                creationdate,
                                commentText,                                
                                isProcessed,adjudication_status,
                                5
                                );

                        eas.annotations.add(ea);

                        //System.out.println("span start: " + start );
                        //System.out.println("span end: " + end );
                        //System.out.println("spannedText: " + spannedText );
                        //System.out.println("-----------------------------------");
                    }
                }
            }
            
            
           // if(debug) System.out.println(" = annotations imported!");

            // details of classmention
            list = root.getChildren("classMention");//get all element of

            if ( list != null )
            {
                for(int i=0;i<list.size();i++){
                    Element classMention = (Element) list.get(i);
                    String classMentionID = classMention.getAttributeValue("id");

                    Element mentionClass = classMention.getChild("mentionClass");
                    String mentionClassID = mentionClass.getAttributeValue("id");
                    String mentionClassText = mentionClass.getText();



                    //Logs.ShowLogs.printInfoLog("classmention id: "
                    //  + classMentionID.trim() );
                    //Logs.ShowLogs.printInfoLog("mentionClass id: "
                    //  + mentionClassID.trim() );
                    //Logs.ShowLogs.printInfoLog("mentionclass value: "
                    //  + mentionClassText.trim() );
                    //System.out.println("classmention id: " + classMentionID );
                    //System.out.println("classmention id: " + mentionClassID );
                    //System.out.println("classmention id: " + mentionClassText );
                    //Logs.ShowLogs.printInfoLog("--------------------------");

                    // send into static memory
                    imports.importedXML.eClassMention ecm = new imports.importedXML.eClassMention();
                    ecm.classMentionID = classMentionID.trim();
                    ecm.mentionClassID = mentionClassID.trim();
                    ecm.mentionClassText = mentionClassText.trim();

                    List hasSlotMentions = classMention.getChildren("hasSlotMention");
                    if ( hasSlotMentions != null ) {
                        int amountOfHasSlotMention = hasSlotMentions.size();
                        for(int j=0;j<amountOfHasSlotMention; j++){
                            Element hasSlotMention = (Element) hasSlotMentions.get(j);
                            String hasSlotMention_id = hasSlotMention.getAttributeValue("id");
                            //System.out.println(" - XMLreader - hasSlotMention_id - "+ hasSlotMention_id);
                            ecm.hasSlotMention_id.add(hasSlotMention_id);
                        }


                    }
                    eas.classMentions.add(ecm);

                }
            }

            try{
                // details of stringSlotMention
                list = root.getChildren("stringSlotMention");//get all element of
                if ( list != null ) {
                    for(int i=0;i<list.size();i++){
                        Element stringSlotMention = (Element) list.get(i);

                        String stringSlotMention_id = stringSlotMention.getAttributeValue("id");

                        Element mentionSlot = stringSlotMention.getChild("mentionSlot");
                        mentionSlot_id = mentionSlot.getAttributeValue("id");

                        List ele_stringSlotMentions = stringSlotMention.getChildren("stringSlotMentionValue");
                        if ( ele_stringSlotMentions != null ){

                            int sizestringslot = ele_stringSlotMentions.size();
                            if( sizestringslot > 0 ){
                                // send into static memory
                                eStringSlotMention ssm = new eStringSlotMention();
                                ssm.stringSlotMention_id = stringSlotMention_id;
                                ssm.mentionSLot_id = mentionSlot_id;
                                if( mentionSlot_id.trim().toLowerCase().compareTo( "suggestion" ) == 0 )
                                    continue;


                                for(int s=0;s<sizestringslot;s++){
                                    Element ele_stringSlotMention = (Element) ele_stringSlotMentions.get(s);
                                    String str_mentionSlot_id = ele_stringSlotMention.getAttributeValue("value");
                                    ssm.stringSlotMentionValue = str_mentionSlot_id;
                                }
                            eas.stringSlotMentions.add(ssm);
                            }
                        }




                        //Logs.ShowLogs.printInfoLog("classmention id: "
                        //  + classMentionID.trim() );
                        //Logs.ShowLogs.printInfoLog("mentionClass id: "
                        //  + mentionClassID.trim() );
                        //Logs.ShowLogs.printInfoLog("mentionclass value: "
                        //  + mentionClassText.trim() );
                        //System.out.println("classmention id: " + classMentionID );
                        //System.out.println("classmention id: " + mentionClassID );
                        //System.out.println("classmention id: " + mentionClassText );
                        //Logs.ShowLogs.printInfoLog("-------------------------");







                    }
                }
            }catch(Exception e){
                String errorinfo = "importXML.java - lost data" +
                        " while import xml annotations from file: "
                        + _file.getAbsolutePath();
                log.LoggingToFile.log( Level.SEVERE, errorinfo );                
            }

            try{
                // details of stringSlotMention
                list = root.getChildren("complexSlotMention");//get all element of
                String complexSlotMention_id, complexSlotMentionValue_value = "";
                if ( list != null ) {
                    for(int i=0;i<list.size();i++){
                        // get XML node of complex slot Mentiond
                        Element complexSlotMention = (Element) list.get(i);

                        // get complex slot Mentiond id of this XML node
                        complexSlotMention_id = complexSlotMention.getAttributeValue("id");

                        
                        // get mentionSLot id of this XML node
                        Element mentionSlot = complexSlotMention.getChild("mentionSlot");
                        if ( mentionSlot != null )
                            mentionSlot_id = mentionSlot.getAttributeValue("id");
                        
                        // get possible attributes on relationship
                        ArrayList<AnnotationAttributeDef> atts = new ArrayList<AnnotationAttributeDef>();
                        List attributes = complexSlotMention.getChildren("attribute");
                        for(int p = 0; p < attributes.size(); p++ ){
                            Element attribute = (Element) attributes.get(p);
                            String name = attribute.getAttributeValue("id");
                            String value = attribute.getText();
                            if(( name == null )||(name.trim().length()<1))
                                continue;
                            
                            if( value.trim().compareTo("null") == 0 )
                                value = null;
                            
                            atts.add( new AnnotationAttributeDef( name, value ));
                            
                        }

                        // get complex slot mention value of this XML node

                        List complexSlotMentionValues = complexSlotMention.getChildren("complexSlotMentionValue");

                        if( complexSlotMentionValues != null ){

                            int size_comp = complexSlotMentionValues.size();
                            // send into static memory
                            imports.importedXML.eComplexSlotMention thisComplexSlotMention = new imports.importedXML.eComplexSlotMention();
                            thisComplexSlotMention.complexSlotMention = complexSlotMention_id;
                            thisComplexSlotMention.mentionSLot_id = mentionSlot_id;

                            for(int k=0;k<size_comp;k++){
                                Element complexSlotMentionValue = (Element)  complexSlotMentionValues.get(k);
                                if ( complexSlotMentionValue == null )  continue;
                                complexSlotMentionValue_value = complexSlotMentionValue.getAttributeValue("value");
                                thisComplexSlotMention.complexSlotMentionValue_value.add( complexSlotMentionValue_value );

                                //System.out.println(" - complexSlotMention_id = " + complexSlotMention_id +"; " +mentionSlot_id);


                            }
                            thisComplexSlotMention.attributes = atts;
                             eas.complexSlotMentions.add(thisComplexSlotMention);

                        }
                        
                        
                        





                    }
                }
            } catch (Exception e) {
                String errorstr = "importXML.java - lost data"
                        + " while import xml annotations from file: "
                        + _file.getAbsolutePath();
                log.LoggingToFile.log(Level.WARNING, errorstr);
                logs.ShowLogs.printWarningLog(errorstr);
            }

            recordAnnotation(eas);

        } catch (Exception e) {
            // e.printStackTrace();
            log.LoggingToFile.log(Level.SEVERE, "importXML.java - 112" + e.getMessage());            
        }

        return eas;
    }
    /**
     * This method will return the index of the given filename in the IMPORTEDXMLs array.
     * @param filename - the name of the file
     * @return the index of the file name or -1 if not found.
     */
    public static int getIndex(String filename)
    {
        int size = IMPORTEDXMLs.size();
        filename = filename + ".knowtator.xml";
        filename = filename.trim().toLowerCase();
        for(int i = 0; i<size; i++)
        {
            eXMLFile eas = IMPORTEDXMLs.get(i);
            String easFilename = eas.filename.trim().toLowerCase();
            if(easFilename.compareTo(filename) == 0)
                return i;
        }
        return -1;
    }

    /*
    public static int changeAnntationClass(String filename, int _start,
            int _end, String _AnnotationClassname){
        int count=0;
        int size = IMPORTEDXMLs.size();

        String creationDate = commons.OS.getCurrentDate();
        eXMLFile eas;
        filename = filename + ".knowtator.xml";
        filename = filename.trim().toLowerCase();
        for(int i=0;i<size;i++){

            eas = IMPORTEDXMLs.get(i);
            String easFilename = eas.filename.trim().toLowerCase();

            // 1.get Annotations to this filename
            if(easFilename.compareTo(filename) == 0){
                Vector<eAnnotationNode> EAs = eas.annotations;
                int size_EAs = EAs.size();
                Vector<eClassMention> ECMs = eas.classMentions;
                int size_ECMs = ECMs.size();

                String mentionid;
                //for(int j=0;j<size_EAs;j++)
                int j=0;
                do{
                    int start = Integer.valueOf(EAs.get(j).span_start.trim());
                    int end = Integer.valueOf(EAs.get(j).span_end.trim());
                    // get annotation we want to modify
                    if((_start== start)&&(end==_end)){

                        mentionid = EAs.get(j).mention_id.trim();

                        for(int k=0;k<size_ECMs;k++){
                            eClassMention ecm = ECMs.get(k);
                            if(ecm.classMentionID.trim().compareTo(mentionid)==0){
                                ecm.mentionClassID = _AnnotationClassname;
                                count++;
                            }
                        }


                    }
                    j++;
                }while(j<size_EAs);

            }
        }

        AnnotationClassChange_inLatestQueryReturn(_start, _end, _AnnotationClassname,
                creationDate);

        return count;
    }*/

     /*public static void AnnotationClassChange_inLatestQueryReturn(
            int _start, int _end, String _annotationclassname,
            String _creationDate){
        int size = QueryReturns.size();

        for(int i=0;i<size;i++) {
            int start = Integer.valueOf(QueryReturns.get(i).spanStart.trim());
            int end = Integer.valueOf(QueryReturns.get(i).spanEnd.trim());
            if((_start==start)&&(end==_end)) {
                QueryReturns.get(i).mentionClassID = _annotationclassname;
            }
        }

    }*/





    /** span range change on user interface - for display sync. */
    //headextendtoLeft = 1, tailextendtoRight = 2,
    //headShortentoRight = 3, tailShortentoLeft =4;
    /*public static void AnnotationRangeSet_inLatestQueryReturn(
            int _start, int _end, int _type, String _creationdate,
            String _spanText){
        int size = QueryReturns.size();

        for(int i=0;i<size;i++) {
            int start = Integer.valueOf(QueryReturns.get(i).spanStart.trim());
            int end = Integer.valueOf(QueryReturns.get(i).spanEnd.trim());
            if((_start==start)&&(end==_end)) {
                start = ( _type == 1 ? (start-1) : (start) );
                end = ( _type == 2 ? (end+1) : end);
                start = ( _type == 3 ? (start+1) : (start) );
                end = ( _type == 4 ? (end-1) : end);
                QueryReturns.get(i).spanStart =  String.valueOf( start );
                QueryReturns.get(i).spanEnd = String.valueOf( end );
                QueryReturns.get(i).creationdate = _creationdate;
                QueryReturns.get(i).annotator = "eHOST";
                QueryReturns.get(i).spanText = _spanText;
            }
        }

    }*/


    /**return object = { spanstart, spanend, mentionClassID } to a specific
     * filename*/
    /*public static ArrayList getImportedXMLInfo(File _ListedTxtFile){
        ArrayList getImportedXMLDetails = new ArrayList();

        int size = IMPORTEDXMLs.size();


        eXMLFile eas;
        String knowtatorFilename = _ListedTxtFile.getName() + ".knowtator.xml";
        knowtatorFilename = knowtatorFilename.trim().toLowerCase();
        for(int i=0;i<size;i++){
            // 1.get Annotations to this filename
            eas = IMPORTEDXMLs.get(i);
            String easFilename = eas.filename.trim().toLowerCase();

            if(easFilename.compareTo(knowtatorFilename) == 0){

                // 2.get all annotation and classmention of this filename
                Vector<eAnnotationNode> EAs = eas.annotations;
                int size_EAs = EAs.size();
                Vector<eClassMention> ECMs = eas.classMentions;
                int size_ECMs = ECMs.size();

                // 3. to each annotation, get their classname
                for(int j =0 ; j<size_EAs;j++){

                    eAnnotationNode ea = EAs.get(j);

                    String mentionId = ea.mention_id.trim();

                    for(int k=0; k<size_ECMs ; k++ ){

                        eClassMention ecm = ECMs.get(k);

                        String classMentionId = ecm.classMentionID.trim();
                        if( mentionId.compareTo( classMentionId) == 0 ){
                            Object[] o = { ea.span_start,
                                ea.span_end, ecm.mentionClassID };
                            getImportedXMLDetails.add(o);
                        }

                    }

                }

            }
        }

        return getImportedXMLDetails;
    }
*/


    /*
    public static eXMLFile createXMLFile( File _file ){
        if ( _file == null )
            return null;

        Logs.ShowLogs.printInfoLog("open xml file for annotation import: "
                + _file.getAbsolutePath() );

        eXMLFile eas = new eXMLFile();
        eas.absoluteFilename = _file.getAbsolutePath().trim();
        eas.filename = _file.getName().trim();

        try{
            SAXBuilder sb=new SAXBuilder();

            //constract doc object
            Document doc = sb.build( _file.getAbsolutePath() );

            Element root=doc.getRootElement(); // get element of root

            // details of annotation
            List list = root.getChildren("annotation");//get all element of
            for(int i=0;i<list.size();i++){

                Element annotations = (Element) list.get(i);

                Element mention = annotations.getChild("mention");
                String mentionID = mention.getAttributeValue("id");

                Element annotator = annotations.getChild("annotator");
                String annotatorID = annotator.getAttributeValue("id");
                String annotatorValue = annotator.getText();

                Element span = annotations.getChild("span");
                String start = span.getAttributeValue("start");
                String end = span.getAttributeValue("end");

                Element spannedTexts = annotations.getChild("spannedText");
                String spannedText = spannedTexts.getText();

                Element creationdates = annotations.getChild("creationDate");
                String creationdate = creationdates.getText();


                //Logs.ShowLogs.printInfoLog("mention id: " + mentionID );
                //Logs.ShowLogs.printInfoLog("annotator id: " + annotatorID );
                //Logs.ShowLogs.printInfoLog("annotator value: " + annotatorValue );
                //Logs.ShowLogs.printInfoLog("span start: " + start );
                //Logs.ShowLogs.printInfoLog("span end: " + end );
                //Logs.ShowLogs.printInfoLog("spannedText: " + spannedText );
                //Logs.ShowLogs.printInfoLog("-----------------------------------");

                // send into static memory
                eAnnotationNode ea = new eAnnotationNode();
                ea.mention_id = mentionID;
                ea.annotator_id = annotatorID;
                ea.annotator = annotatorValue;
                ea.span_start = start;
                ea.span_end = end;
                ea.creationDate = creationdate;
                ea.annotationText = spannedText;
                eas.annotations.add(ea);

                //System.out.println("span start: " + start );
                //System.out.println("span end: " + end );
                //System.out.println("spannedText: " + spannedText );
                //System.out.println("-----------------------------------");
            }

            // details of classmention
            list = root.getChildren("classMention");//get all element of
            for(int i=0;i<list.size();i++){
                Element classMention = (Element) list.get(i);
                String classMentionID = classMention.getAttributeValue("id");

                Element mentionClass = classMention.getChild("mentionClass");
                String mentionClassID = mentionClass.getAttributeValue("id");
                String mentionClassText = mentionClass.getText();



                //Logs.ShowLogs.printInfoLog("classmention id: "
                //  + classMentionID.trim() );
                //Logs.ShowLogs.printInfoLog("mentionClass id: "
                //  + mentionClassID.trim() );
                //Logs.ShowLogs.printInfoLog("mentionclass value: "
                //  + mentionClassText.trim() );
                //System.out.println("classmention id: " + classMentionID );
                //System.out.println("classmention id: " + mentionClassID );
                //System.out.println("classmention id: " + mentionClassText );
                //Logs.ShowLogs.printInfoLog("--------------------------");

                // send into static memory
                Import.ImportedXML.eClassMention ecm =
                        new Import.ImportedXML.eClassMention();
                ecm.classMentionID = classMentionID.trim();
                ecm.mentionClassID = mentionClassID.trim();
                ecm.mentionClassText = mentionClassText.trim();

                List hasSlotMentions = mentionClass.getChildren("hasSlotMention");
                int amountOfHasSlotMention = hasSlotMentions.size();
                for(int j=0;j<amountOfHasSlotMention; j++){
                    Element hasSlotMention = (Element) hasSlotMentions.get(j);
                    String hasSlotMention_id
                            = hasSlotMention.getAttributeValue("id");
                    NodeHasSlotMention hsm = new NodeHasSlotMention();
                    hsm.hasSlotMention_id = hasSlotMention_id;
                    ecm.hasSlotMention_id.add(hsm);
                }

                eas.classMentions.add(ecm);
            }

            try{
                // details of stringSlotMention
                list = root.getChildren("stringSlotMention");//get all element of
                for(int i=0;i<list.size();i++){
                    Element stringSlotMention = (Element) list.get(i);

                    String stringSlotMention_id =
                            stringSlotMention.getAttributeValue("id");

                    Element mentionSlot =
                            stringSlotMention.getChild("mentionSlot");
                    String mentionSlot_id = mentionSlot.getAttributeValue("id");

                    Element ele_stringSlotMention =
                            stringSlotMention.getChild("stringSlotMentionValue");
                    String str_mentionSlot_id =
                            ele_stringSlotMention.getAttributeValue("value");



                    //Logs.ShowLogs.printInfoLog("classmention id: "
                    //  + classMentionID.trim() );
                    //Logs.ShowLogs.printInfoLog("mentionClass id: "
                    //  + mentionClassID.trim() );
                    //Logs.ShowLogs.printInfoLog("mentionclass value: "
                    //  + mentionClassText.trim() );
                    //System.out.println("classmention id: " + classMentionID );
                    //System.out.println("classmention id: " + mentionClassID );
                    //System.out.println("classmention id: " + mentionClassText );
                    //Logs.ShowLogs.printInfoLog("-------------------------");

                    // send into static memory
                    eStringSlotMention ssm = new eStringSlotMention();
                    ssm.stringSlotMention_id = stringSlotMention_id;
                    ssm.mentionSLot_id = mentionSlot_id;
                    ssm.stringSlotMentionValue_Value = str_mentionSlot_id;


                    eas.stringSlotMentions.add(ssm);


                }
            }catch(Exception e){
                Logs.ShowLogs.printWarningLog("importXML.java - lost data" +
                        " while import xml annotations from file: "
                        + _file.getAbsolutePath() );
            }

            return eas;

        }catch(Exception e){
            Logs.ShowLogs.printErrorLog(e.getLocalizedMessage());
            Logs.ShowLogs.printErrorLog(e.toString());
            return null;

        }
    }*/
}

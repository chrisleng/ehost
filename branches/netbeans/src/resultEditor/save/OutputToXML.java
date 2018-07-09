/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.save;

import adjudication.SugarSeeder;
import adjudication.parameters.Paras;
import resultEditor.annotations.Article;
import resultEditor.annotations.Depot;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import org.jdom.*;
import org.jdom.output.*;
import resultEditor.annotations.*;
import userInterface.GUI;

/**
 *
 * @author Chris
 */
public class OutputToXML {
    protected String textsourcefilename;
    protected String path;
    protected Article source = null;
    protected int latestUsedMentionID = 50000;
    protected String absolutepath;

    public OutputToXML( final String textsourcefilename, final String path, Article article){
         this.textsourcefilename = textsourcefilename;
         this.path = path;
         if(article != null)
             source = article;
    }

    /**user try "save" current work to xml files.*/
    public OutputToXML(){
    }

    public void directsave(final File txtfile)
    {
        // ##1##get text source filename
        this.textsourcefilename = txtfile.getName();
        if ( (textsourcefilename == null)||(textsourcefilename.trim().length()<1))
            return;
        
        // ##2## output to save folder
        latestUsedMentionID = getLatestUsedMentionID();
        log.LoggingToFile.log(Level.INFO," + now we are try to build xml for text source: ["+ txtfile.getAbsolutePath() + "]");

        File workspace = env.Parameters.WorkSpace.CurrentProject;
        String workspacepath = workspace.getAbsolutePath();

        String folder = "saved";
        
        //if(GUI.REVIEWMODE == GUI.ReviewMode.adjudicationMode)
        //    folder = "adjudication";

        String savedpath = workspacepath + File.separator + folder + File.separator;
        System.out.println("["+savedpath+"]");
        File savedFolder = new File(savedpath);

        prepareFolder(savedFolder);

        String xmlpath = savedpath + txtfile.getName().trim()
                + ".knowtator.xml";
        File XMLfile = new File(xmlpath);
        buildxml( XMLfile, false );

        env.Parameters.forceChangeLatestUsedMentionID(latestUsedMentionID);
        
        // ##2## output to "adjudication" folder if under the adjudication mode
        if(GUI.reviewmode == GUI.ReviewMode.adjudicationMode){
            latestUsedMentionID = getLatestUsedMentionID();
            log.LoggingToFile.log(Level.INFO," + now we are try to build xml for text source: ["+ txtfile.getAbsolutePath() + "] (Adjudicated Results)");
            
            folder = "adjudication";        
            savedpath = workspacepath + File.separator + folder + File.separator;
            System.out.println("["+savedpath+"]");
            prepareFolder( new File(savedpath) );
            xmlpath = savedpath + txtfile.getName().trim() + ".knowtator.xml";
            XMLfile = new File(xmlpath);
            
            // true: tell the method that we want to only output annotations whose status is matched_ok or annotator is "ADJUDICATION"
            buildxml( XMLfile, true ); 
            
            env.Parameters.forceChangeLatestUsedMentionID(latestUsedMentionID);
        }
    }

    private void prepareFolder(File folder){
        // if folder is a file
        if(folder.exists())
        {
           if(folder.isFile()){
               boolean success = folder.delete();
               if(success){
                    folder.mkdirs();
               }
           }
        }
        else
        {
            folder.mkdirs();
        }
    }

    private String gettextsourcename_byFile(File file){
        if (file==null)
            return null;
        String name = file.getName();
        name = name.toLowerCase().replaceAll(".knowtator.xml", " ");
        name = name.trim();

        return name;
    }

    public boolean write(){
       latestUsedMentionID = getLatestUsedMentionID();
       checkOutputFolder( path );
       String sepatator = ( env.Parameters.isUnixOS? "/":"\\" );
       String outputxmlfilename = path + sepatator + textsourcefilename + ".knowtator.xml";
       log.LoggingToFile.log(Level.INFO, " + now we are try to build xml: ["+ outputxmlfilename + "]");

       boolean done = buildxml( textsourcefilename, outputxmlfilename );

       env.Parameters.forceChangeLatestUsedMentionID(latestUsedMentionID);

       return done;
    }

    
    private boolean buildxml( String textsource, String outputAbsoulateFilename ){
         try{
            // initial the XML file and set the root node of the XML
            //System.out.println("annotationsStr is " + annotationsStr);
            Element root = new Element( "annotations" ); //
            //System.out.println("TextSource is " + TextSource);
            root.setAttribute( "textSource", textsource );
            Document Doc = new Document(root);

            /** add contents */
            root = addAnnotations( root, false );
            if( !SaveDialog.outputMainBodyOnly )
                root = adjudicationParameters( root );

            // **** output xml file to disk ****
            // XML storage processing: phycial writing
            Format format = Format.getCompactFormat();
            format.setEncoding("UTF-8"); // set XML encodeing
            format.setIndent("    ");
            XMLOutputter XMLOut = new XMLOutputter(format);
            // write to disk
            FileOutputStream XML = new FileOutputStream( outputAbsoulateFilename );
            XMLOut.output(Doc, XML);
            XML.close();

            // Screen Log
            log.LoggingToFile.log(Level.INFO,"Successfully genereted XML to - " + outputAbsoulateFilename );
            logs.ShowLogs.printImportantInfoLog("Successfully genereted XML to - " + outputAbsoulateFilename );
            return true;
         }
        catch(Exception exc){
            // exc.printStackTrace();
            log.LoggingToFile.log(Level.SEVERE, "WriteToXML.java - " + exc.toString());
            return false;
        }
    }

    /**
     * @param   is_outputing_adjudicated_annotations
     *          a boolean flag. If it's "true", it tells the method that we only 
     *          want to output annotations whose status is matched_ok or 
     *          annotator is "ADJUDICATION"
     */
    private boolean buildxml( File XMLfile, boolean is_outputing_adjudicated_annotations )
    {
         try{
            // initial the XML file and set the root node of the XML
            //System.out.println("annotationsStr is " + annotationsStr);
            Element root = new Element( "annotations" ); //
            //System.out.println("TextSource is " + TextSource);
            root.setAttribute( "textSource", this.textsourcefilename );
            

            /** add contents */
            root = addAnnotations( root, is_outputing_adjudicated_annotations );
            
            // do not output adjudicating annotation and adjudication parameter
            // while output annotation to the folder "ADJUDICATION"
            if(!is_outputing_adjudicated_annotations){
                root = addAdjudicatingAnnotations( root );        
                if(!SaveDialog.outputMainBodyOnly)
                    root = adjudicationParameters( root );
            }
            

            Document Doc = new Document(root);
            
            // **** output xml file to disk ****
            // XML storage processing: phycial writing
            Format format = Format.getCompactFormat();
            format.setEncoding("UTF-8"); // set XML encodeing
            format.setIndent("    ");
            XMLOutputter XMLOut = new XMLOutputter(format);
            // write to disk
            FileOutputStream XML = new FileOutputStream( XMLfile );
            System.out.println(XMLfile);
            XMLOut.output(Doc, XML);
            XML.close();

            // Screen Log
            log.LoggingToFile.log(Level.SEVERE, "Successfully genereted XML to - "
                    + XMLfile );
            return true;
         }
        catch(Exception exc){
            // exc.printStackTrace();
            log.LoggingToFile.log(Level.SEVERE, "WriteToXML.java - " + exc.toString());
            return false;
        }
    }

    
          
     
    /**
     * @param   is_outputing_adjudicated_annotations
     *          a boolean flag. If it's "true", it tells the method that we only 
     *          want to output annotations whose status is matched_ok or 
     *          annotator is "ADJUDICATION"
     */
    private Element addAnnotations(Element root, boolean is_outputing_adjudicated_annotations)
    {
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        Article article = null;
        if(source == null){
            if( is_outputing_adjudicated_annotations ){
                article = adjudication.data.AdjudicationDepot.getArticleByFilename(textsourcefilename);
            } else
                article = Depot.getArticleByFilename(textsourcefilename);
        }else
            article = source;

        if( article == null )
            return root;

        try
        {
            for( resultEditor.annotations.Annotation annotation: article.annotations ){
                latestUsedMentionID++;
                int mentionid = latestUsedMentionID;
                annotation.outputmentionid = "EHOST_Instance_"+ mentionid;
            }

            for(resultEditor.annotations.Annotation annotation: article.annotations)
            {
                if (is_outputing_adjudicated_annotations) {
                    if ((annotation.adjudicationStatus != Annotation.AdjudicationStatus.MATCHES_OK)
                            &&
                            (annotation.getFullAnnotator().compareTo("ADJUDICATION")!=0)) {
                        continue;
                    }
                }

                String annotator = "ADJUDICATION";
                if(!is_outputing_adjudicated_annotations)
                    annotator = annotation.getFullAnnotator();
                
                root = buildAnnotationNode(
                        is_outputing_adjudicated_annotations,
                        root,
                        annotation.outputmentionid,
                        annotation.annotationText,
                        annotation.annotationclass,
                        annotation.spanset,
                        annotation.creationDate,
                        annotation.comments,
                        annotator,
                        annotation.annotatorid,
                        annotation.attributes,
                        annotation.relationships,
                        annotation.verifierSuggestion,
                        annotation.verifierFound,
                        annotation,
                        false // false is default
                        );
            }
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "error 1102111931::fail to pack annotation to XML"
                    + e.toString() );
            return root;
        }
        

        return root;
    }

    /**
     * Record annotations that are working in the mirror memory for adjudication 
     * mode, so next time we can continue previous unfinished adjudication process.
     * 
     */
    private Element addAdjudicatingAnnotations(Element root )
    {
        // get 
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        Article article = adjudication.data.AdjudicationDepot.getArticleByFilename(textsourcefilename);                        

        if( article == null )
            return root;

        try
        {
            for( resultEditor.annotations.Annotation annotation: article.annotations ){
                latestUsedMentionID++;
                int mentionid = latestUsedMentionID;
                annotation.outputmentionid = "EHOST_Instance_"+ mentionid;
            }

            for(resultEditor.annotations.Annotation annotation: article.annotations)
            {                                
                
                root = buildAdjudicatingAnnotationNode(
                        // is_outputing_adjudicated_annotations,
                        root,
                        annotation.outputmentionid,
                        annotation.annotationText,
                        annotation.annotationclass,
                        annotation.spanset,
                        annotation.creationDate,
                        annotation.comments,
                        annotation.getFullAnnotator(),
                        annotation.annotatorid,
                        annotation.attributes,
                        annotation.relationships,
                        annotation.verifierSuggestion,
                        annotation.verifierFound,
                        annotation
                        );
            }
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "error 1102111931::fail to pack annotation to XML"
                    + e.toString() );
            return root;
        }
        

        return root;
    }

    /**record adjudication parameters*/
     private Element adjudicationParameters(Element root){
         Element adjudicationRoot = new Element("eHOST_Adjudication_Status");
         adjudicationRoot.setAttribute("version", "1.0");         
         
         // add xml branch for "selected annotators" for the adjudication mode
         adjudicationRoot = add_AdjudicationAnnotators( adjudicationRoot );   
         // add xml branch for "selected classes" for the adjudication mode
         adjudicationRoot = add_AdjudicationClasses( adjudicationRoot ); 
         // add xml branch for how we consider two annotations are same, and
         // which filed need to be compared.
         adjudicationRoot = add_AdjudicationOthers( adjudicationRoot );
         
         root.addContent(adjudicationRoot );         
         return root;            
     } 
     
     /**saved selected classes into the XML file for the adjudication mode*/
     private Element add_AdjudicationClasses(Element parent){
         Element element_classes = new Element("Adjudication_Selected_Classes");
         element_classes.setAttribute("version", "1.0");
         
         // get parameters         
         ArrayList<String> selectedClasses = adjudication.parameters.Paras.getClasses();
         
         if( selectedClasses != null ){
             for(String annotator : selectedClasses){
                 if(( annotator ==null ) || (annotator.trim().length() < 1) )
                     continue;
                Element element_class = new Element("Class");            
                element_class.setText( annotator.trim() );
                element_classes.addContent( element_class );
             }
         }
         
         parent.addContent( element_classes );
         
         return parent;
            
     } 
     
     /**saved selected annotators into the XML file for the adjudication mode*/
     private Element add_AdjudicationAnnotators(Element parent){
         Element annotators = new Element("Adjudication_Selected_Annotators");
         annotators.setAttribute("version", "1.0");
         
         // get parameters
         ArrayList<String> selectedAnnotators = adjudication.parameters.Paras.getAnnotators();         
         
         if( selectedAnnotators != null ){
             for(String annotator : selectedAnnotators){
                 if(( annotator ==null ) || (annotator.trim().length() < 1) )
                     continue;
                Element element_annotator = new Element("Annotator");            
                element_annotator.setText( annotator.trim() );
                annotators.addContent( element_annotator );
             }
         }
         
         parent.addContent( annotators );
         
         return parent;
            
     } 
     
     /**save other parameters for the adjudication mode*/
    private Element add_AdjudicationOthers(Element parent) {
        Element others = new Element("Adjudication_Others");
        
        // CHECK_OVERLAPPED_SPANS = false;        
        String str1 = Paras.CHECK_OVERLAPPED_SPANS? "true" : "false" ;
        Element element_para1 = new Element("CHECK_OVERLAPPED_SPANS");
        element_para1.setText( str1 );
        others.addContent(element_para1);
        
        // CHECK_ATTRIBUTES = false;
        String str2 = Paras.CHECK_ATTRIBUTES? "true" : "false" ;
        Element element_para2 = new Element("CHECK_ATTRIBUTES");
        element_para2.setText( str2 );
        others.addContent(element_para2);
        
        // CHECK_RELATIONSHIP = false; 
        String str3 = Paras.CHECK_RELATIONSHIP? "true" : "false" ;
        Element element_para3 = new Element("CHECK_RELATIONSHIP");
        element_para3.setText( str3 );
        others.addContent(element_para3);
        
        // CHECK_CLASS = false; 
        String str4 = Paras.CHECK_CLASS? "true" : "false" ;
        Element element_para4 = new Element("CHECK_CLASS");
        element_para4.setText( str4 );
        others.addContent(element_para4);
        
        // CHECK_COMMENT = false;
        String str5 = Paras.CHECK_COMMENT? "true" : "false" ;
        Element element_para5 = new Element("CHECK_COMMENT");
        element_para5.setText( str5 );
        others.addContent(element_para5);
        

        parent.addContent(others);

        return parent;

    }
     
    private Element buildAdjudicatingAnnotationNode( Element _root, String _MentionID,
            String _spanText, String _classname, SpanSetDef _spanset,
            String creationdate, String annotationComment,
            String _annotator, String annotatorid,
            Vector<resultEditor.annotations.AnnotationAttributeDef> attributes,
            Vector<resultEditor.annotations.AnnotationRelationship> relationships,
            Vector<String> verifierSuggestions, //suggestions extracted from annotation comment
            Vector<resultEditor.annotations.suggestion> verifierFounds,
            Annotation _annotation
            //Vector<String> _Slots
            ){
        return buildAnnotationNode( 
                false, 
                _root, 
                _MentionID,
                _spanText, 
                _classname, 
                _spanset,
                creationdate, 
                annotationComment,
                _annotator, 
                annotatorid,
                attributes,
                relationships,
                verifierSuggestions, //suggestions extracted from annotation comment
                verifierFounds,
                _annotation,
                true);
    }
    
    
    /**Build a XML node of annotations.
     * 
     * @param   outputAnnotationInMirrorMemeory
     *          Use to tell us whether this annotation is an annotation or a 
     *          mirror annotation that only exists on adjudication mode.
     *          If this is 'true', the annotation node should be 
     *          "adjudication_annotation", not "annotation".
     *          
     */
    private Element buildAnnotationNode(boolean is_outputing_adjudicated_annotations, Element _root, String _MentionID,
            String _spanText, String _classname, SpanSetDef _spanset,
            String creationdate, String annotationComment,
            String _annotator, String annotatorid,
            Vector<resultEditor.annotations.AnnotationAttributeDef> attributes,
            Vector<resultEditor.annotations.AnnotationRelationship> relationships,
            Vector<String> verifierSuggestions, //suggestions extracted from annotation comment
            Vector<resultEditor.annotations.suggestion> verifierFounds,
            Annotation _annotation,
            boolean outputAnnotationInMirrorMemeory 
            //Vector<String> _Slots
            )
    {
            Element root = _root;

            if(_classname==null){
                log.LoggingToFile.log(Level.SEVERE, "error 1102111947:: while building the XML node, classname is NULL");
                return root;
            }

            if((_MentionID==null)||(_MentionID.trim().length()<1)){
                log.LoggingToFile.log(Level.SEVERE, "error 1102111947:: while building the XML node, mention id is NULL");
                return root;
            }



            // -----------------------------------------------------elements
            
            Element annotation;
            if( outputAnnotationInMirrorMemeory )
                annotation = new Element("adjudicating");
            else 
                annotation = new Element("annotation");

            Element mention = new Element("mention");
            //System.out.println("mention id - " + annotationRecor ds.mentionId );
            mention.setAttribute("id",  _MentionID );
            //mention.setAttribute("id", "EHOST_0" );
            annotation.addContent(mention);

            Element annotator = new Element("annotator");
            //annotator.setAttribute("id", "EHOST_607");
            if(annotatorid==null) 
                annotatorid = "Anonymous";
            annotator.setAttribute("id", annotatorid);
            //System.out.println("annotator id - " + annotationRecords.annotatorId );
            //annotator.setAttribute("id", "EHOST_12");
            //System.out.println("annotator text - " + annotationRecords.annotator );
            if((_annotator==null)||(_annotator.trim().length()<1)){
                _annotator = "Anonymous";
            }
            annotator.setText(_annotator);
            annotation.addContent(annotator);

           if (_spanset != null) {
             if (_spanset.size() > 0) {
                for (int ispan = 0; ispan < _spanset.size(); ispan++) {
                    SpanDef span = _spanset.getSpanAt(ispan);
                    if(span==null)
                        continue;
                    Element span_element = new Element("span");
                    span_element.setAttribute("start", String.valueOf(span.start));
                    span_element.setAttribute("end", String.valueOf(span.end));
                    annotation.addContent(span_element);
                }
            }
        }

            if(_spanText==null)
                _spanText = "";
            annotation.addContent( new Element("spannedText").setText(_spanText));

            //#### handle comment and verifier suggestions here
            String commentstr = handleCommentAndVerifierSuggestions( annotationComment, verifierSuggestions, verifierFounds );            
            if ( commentstr != null ){
                if ( (commentstr.trim().length() > 0) && ( commentstr.replaceAll("\"", " ").trim().length() > 0 ) )
                    annotation.addContent( new Element("annotationComment").setText( commentstr ) );
            }
            
            


            if((creationdate==null)||(creationdate.trim().length()<1))
                creationdate = commons.OS.getCurrentDate();
            annotation.addContent( new Element("creationDate").setText( creationdate ));

            
            
            
            if(outputAnnotationInMirrorMemeory){                
                
                // record: annotation.isProcessed for adjudication mode
                String str = _annotation.isProcessed()? "true" : "false";
                Element isProcessed = new Element("processed");
                isProcessed.setText( str );
                annotation.addContent( isProcessed );
            
                // record: annotation.adjudicationStatus for adjudication mode
                str = _annotation.adjudicationStatus.toString();
                Element adjudicationStatus = new Element("AdjudicationStatus");
                adjudicationStatus.setText( str );
                annotation.addContent( adjudicationStatus );
            }
            
            
            root.addContent(annotation);
            // -----------------------------------------------------elements end

            
            
            // -----------------------------------------------------classmention begin
            // "classMention" data structure and get stored data in memory
            Element classMention = new Element("classMention");
            classMention.setAttribute("id", _MentionID );
            Element mentionClass = new Element( "mentionClass" );

            if((_classname==null)||(_classname.trim().length()<1))
                _classname = "UNKNOWN";
            mentionClass.setAttribute( "id", _classname );
            mentionClass.addContent( _spanText );

            //#### add has-string-slot in classmention for normal relationship
            try{
                if ((attributes!=null)&&(attributes.size()>0)){
                    //add this node <ClassMention> to xml tree
                    for( resultEditor.annotations.AnnotationAttributeDef att :  attributes){
                        if(att==null)
                            continue;
                        if(   (att.name==null)
                            ||(att.value==null)
                            ||(att.name.length()<1)
                            ||(att.value.length()<1)
                        )
                            continue;
                        
                        latestUsedMentionID++;
                        int mentiondid = latestUsedMentionID;
                        Element hasSlotMention = new Element("hasSlotMention");
                        hasSlotMention.setAttribute("id", "EHOST_Instance_" + mentiondid );
                        classMention.addContent(hasSlotMention);

                        Element slot = addstringSlots(
                                "EHOST_Instance_" + mentiondid,
                                att.name,
                                att.value
                                );
                        if (slot!= null) root.addContent(slot);
                    }
                }
            }catch(Exception e){log.LoggingToFile.log(Level.SEVERE, "1-"+e.toString());}

            if(SaveDialog.outputMainBodyOnly){
             classMention.addContent( mentionClass );
            root.addContent( classMention );
            return root;
            }
            
            //#### add has-string-slot in classmention for complex relationship
            try{
                if ( relationships!=null ){
                    //add this node <ClassMention> to xml tree
                    for( resultEditor.annotations.AnnotationRelationship complexrelationship :  relationships){
                        latestUsedMentionID++;
                        int cmentiondid = latestUsedMentionID;
                        Element hasSlotMention = new Element("hasSlotMention");
                        hasSlotMention.setAttribute("id", "EHOST_Instance_" + cmentiondid );
                        classMention.addContent(hasSlotMention);

                        Element cslot = addComplexSlots(
                                "EHOST_Instance_" + cmentiondid,
                                complexrelationship.getMentionSlotID(),
                                complexrelationship.getLinkedAnnotations(),
                                complexrelationship.attributes,
                                is_outputing_adjudicated_annotations,
                                outputAnnotationInMirrorMemeory
                                );
                        if (cslot!= null) 
                            root.addContent(cslot);
                    }
                }
            }catch(Exception e){log.LoggingToFile.log(Level.SEVERE, "2-"+e.toString());}

            // DISABLEDDISABLED
            if(env.Parameters.Output_XML.output_verify_suggestions_toXML){
                if(!hasSuggestions( verifierSuggestions, verifierFounds )){
                    latestUsedMentionID++;
                    int amentiondid = latestUsedMentionID;
                    Element hasSlotMention2 = new Element("hasSlotMention");
                    hasSlotMention2.setAttribute("id", "EHOST_Instance_" + amentiondid );
                    classMention.addContent(hasSlotMention2);

                    Element gslot = addZeroSuggestionSlot(
                                    "EHOST_Instance_" + amentiondid
                                    );
                    if (gslot!= null) root.addContent(gslot);
                }
            }


            
            
            
            
            
            
            classMention.addContent( mentionClass );
            root.addContent( classMention );
            return root;
    }

    private Element addZeroSuggestionSlot( String mentionid ){

        // "classMention" data structure and get stored data in memory
        Element stringSlotMention = new Element("stringSlotMention");
        stringSlotMention.setAttribute("id", mentionid );

        Element mentionSlot = new Element( "mentionSlot" );
        mentionSlot.setAttribute( "id", "suggestion" );
        stringSlotMention.addContent( mentionSlot );

        Element stringSlotMentionValue = new Element( "stringSlotMentionValue" );
        stringSlotMentionValue.setAttribute( "value", "0" );
        stringSlotMention.addContent( stringSlotMentionValue );


        return stringSlotMention;
    }


    private boolean hasSuggestions(  Vector<String> verifierSuggestions, //suggestions extracted from annotation comment
            Vector<resultEditor.annotations.suggestion> verifierFounds ) {

        Vector<String> suggestions = new Vector<String>();
        String commentstr = null;
        if( verifierSuggestions != null ){
            for( String suggestion: verifierSuggestions ){
                suggestions.add(suggestion.trim());
            }
        }
        if( verifierFounds != null ){
            for( resultEditor.annotations.suggestion suggestion: verifierFounds ){
                suggestions.add(suggestion.getExplanation().trim());
            }
        }

        suggestions =  removeRepetitive( suggestions );

        if (suggestions == null)
            return false;
        else{
            return ( suggestions.size() > 0? true: false);
        }

    }

    private String handleCommentAndVerifierSuggestions(
            String comment,
            Vector<String> verifierSuggestions, //suggestions extracted from annotation comment
            Vector<resultEditor.annotations.suggestion> verifierFounds ) {

        Vector<String> suggestions = new Vector<String>();
        String commentstr = null;
        if( verifierSuggestions != null ){
            for( String suggestion: verifierSuggestions ){
                suggestions.add(suggestion.trim());
            }
        }
        if( verifierFounds != null ){
            for( resultEditor.annotations.suggestion suggestion: verifierFounds ){
                suggestions.add(suggestion.getExplanation().trim());
            }
        }

        suggestions =  removeRepetitive( suggestions );

        
        // remvoe old instered suggestion
        if( comment != null ){
            comment = comment.replaceAll("(\\()" +
                "( *)" +
                "(VERIFIER_SUGGESTIONS)" +
                "( *)" +
                "(.*)" +
                "(\\))", " ");
            comment = comment.replaceAll("\"", " ");
        }else{
            comment = "";
        }


        String sug = "";
        if ((suggestions != null)&&( suggestions.size() > 0 )) {

            sug = "(VERIFIER_SUGGESTIONS ";
            for( String str: suggestions){
                sug = sug +  "[" + str.trim() + "] ";
            }
            sug = sug + ")";
        }

        return "\"" + sug + " " + comment + "\"";
    }

    private Vector<String> removeRepetitive(Vector<String> list){
        int size = list.size();
        for(int i=0;i<size;i++) {
            for(int j=0;j<size;j++) {
                if(i!=j){
                    if ( list.get(i).compareTo( list.get(j) ) == 0 ){
                        list.set(j, null);
                    }
                }
            }
        }
        Vector<String> newlist = new Vector<String>();
        for(String listentry: list){
            if (listentry != null)
                newlist.add(listentry);
        }

        return newlist;

    }

    private Element addComplexSlots(String mentionid,
            String id, Vector<resultEditor.annotations.AnnotationRelationshipDef> values,
            Vector<AnnotationAttributeDef> attributes,
            boolean is_outputing_adjudicated_annotations,
            boolean isADJDepot // tell us whether this annotation is came from the adjudication depot.
            ) {


        if ((id == null) || (values == null)) {
            return null;
        }

        // "classMention" data structure and get stored data in memory
        Element complexSlotMention = new Element("complexSlotMention");
        complexSlotMention.setAttribute("id", mentionid);


        // ---------------------------------------------------------
        Element mentionSlot = new Element("mentionSlot");
        mentionSlot.setAttribute("id", id);
        complexSlotMention.addContent(mentionSlot);

        // ---------------------------------------------------------

        if (attributes != null) {
            for (AnnotationAttributeDef attribute : attributes) {
                if (attribute == null) {
                    continue;
                }
                if ((attribute.name == null) || (attribute.name.trim().length() < 1)) {
                    continue;
                }

                Element att = new Element("attribute");
                att.setAttribute("id", attribute.name);
                if ((attribute.name == null) || (attribute.name.trim().length() < 1)) {
                    att.setText("null");
                } else {
                    att.setText(attribute.value);
                }
                complexSlotMention.addContent(att);
            }
        }

        // flag that tell use that at least one relationship found for this 
        // annotation.
        boolean found = false;
        // ---------------------------------------------------------
        for (resultEditor.annotations.AnnotationRelationshipDef value : values) {

                if(value == null)
                    return complexSlotMention;
                
                String linkedAnnotaionMentionID;
                
                if(  is_outputing_adjudicated_annotations  ){
                    linkedAnnotaionMentionID = getMentionID_byIndex2( value.linkedAnnotationIndex ) ;
                }else{
                    if(isADJDepot)
                        linkedAnnotaionMentionID = getMentionID_byIndex3( value.linkedAnnotationIndex ) ;
                    else
                        linkedAnnotaionMentionID = getMentionID_byIndex( value.linkedAnnotationIndex ) ;
                }
                
                
                if (( linkedAnnotaionMentionID == null )||(linkedAnnotaionMentionID.trim().length()<1))
                    continue; //return complexSlotMention;
                
                found = true;
                
                // if( is_outputing_adjudicated_annotations && (linkedAnnotaionMentionID.) )
                Element complexSlotMentionValue = new Element( "complexSlotMentionValue" );
                complexSlotMentionValue.setAttribute( "value", linkedAnnotaionMentionID  );
                System.out.println( "complexSlotMentionValue value = " + linkedAnnotaionMentionID );
                complexSlotMention.addContent( complexSlotMentionValue );
            }
        
        if( !found )
            return null;

        return complexSlotMention;
    }

    private String getMentionID_byIndex(int annotationIndex){
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        resultEditor.annotations.Article article = depot.getArticleByFilename(textsourcefilename);
        if (article == null)    return null;

        for( resultEditor.annotations.Annotation annotation: article.annotations){
            if (annotation.uniqueIndex == annotationIndex ){
                
                return annotation.outputmentionid;
            }

        }
        return null;

    }
    
    private String getMentionID_byIndex3(int annotationIndex){
        adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
        resultEditor.annotations.Article article = depotOfAdj.getArticleByFilename(textsourcefilename);
        if (article == null)    return null;

        for( resultEditor.annotations.Annotation annotation: article.annotations){
            if (annotation.uniqueIndex == annotationIndex ){
                
                return annotation.outputmentionid;
            }

        }
        return null;

    }
    
    private Annotation getAnnotation_byIndex(int annotationIndex){
        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
        resultEditor.annotations.Article article = depot.getArticleByFilename(textsourcefilename);
        if (article == null)    
            return null;

        for( resultEditor.annotations.Annotation annotation: article.annotations){
            if (annotation.uniqueIndex == annotationIndex ){
                
                return annotation;
            }

        }
        return null;

    }

    private Element addstringSlots( String mentionid,
            String type, String value){

    
            if(( type == null)||(value == null))
                return null;
            
            // "classMention" data structure and get stored data in memory
            Element stringSlotMention = new Element("stringSlotMention");
            stringSlotMention.setAttribute("id", mentionid );

            Element mentionSlot = new Element( "mentionSlot" );
            mentionSlot.setAttribute( "id", type );
            stringSlotMention.addContent( mentionSlot );

            
                if(value == null) return stringSlotMention;
                
                Element stringSlotMentionValue = new Element( "stringSlotMentionValue" );
                stringSlotMentionValue.setAttribute( "value", value );
                stringSlotMention.addContent( stringSlotMentionValue );
            

        return stringSlotMention;
    }

    private void checkOutputFolder(String path){
        File folder = new File(path);
        if (!folder.exists()){
            folder.mkdirs();
        }
    }

    private int getLatestUsedMentionID(){
        return env.Parameters.getLatestUsedMentionID();
    }
    
    /**
     * only used for to get mention id of annotations to generate adjudicated 
     * annotation in folder "ADJUDICATION".
     * 
     */
    private String getMentionID_byIndex2(int annotationIndex) {
        try {
            System.out.print("\n\nasked for [index=" + annotationIndex + "] = ");
            //resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
            adjudication.data.AdjudicationDepot depotOfAdj = new adjudication.data.AdjudicationDepot();
            resultEditor.annotations.Article article = depotOfAdj.getArticleByFilename(textsourcefilename);
            if (article == null) {
                return null;
            }

            for (resultEditor.annotations.Annotation annotation : article.annotations) {

                if (annotation.uniqueIndex == annotationIndex) {

                    //return  annotation.outputmentionid;
                    
                    System.out.print("; {" + annotation.outputmentionid + "} ");
                    // get the mapped annotation if the alias isn't null and isn't a simple index
                    if (annotation.adjudicationAlias != null) {
                        if (SugarSeeder.isAdjudicationAlias(annotation.adjudicationAlias)) {

                            System.out.print("; 1get[" + annotation.outputmentionid + "]");
                            return annotation.outputmentionid;

                        } else {
                            String mentionid = this.getMentionID_byIndex(Integer.valueOf(annotation.adjudicationAlias));
                            Annotation linkedannotation = getAnnotation_byIndex(Integer.valueOf(annotation.adjudicationAlias));
                            if (mentionid != null) {
                                // System.out.print("; 2get["+ mentionid +"]");
                                if (linkedannotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                                    return mentionid;
                                } else {
                                    return null;
                                }

                            } else {
                                // System.out.print("; 3get["+ annotation.outputmentionid +"]");
                                if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                                    return annotation.outputmentionid;
                                } else {
                                    return null;
                                }
                            }
                        }
                    } else {
                        // System.out.print("; 4get["+ annotation.outputmentionid +"]");
                        if (annotation.adjudicationStatus == Annotation.AdjudicationStatus.MATCHES_OK) {
                            return annotation.outputmentionid;
                        } else {
                            return null;
                        }

                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
}

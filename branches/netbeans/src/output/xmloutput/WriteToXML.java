/**
 * @(#)XMLMaker.java
 * Collect and assemble data to XML format
 *
 * @author  Jianwei Leng ( Chris )
            Williams Building, Level 1, Epidemiology Department
 * @history: Friday    06-04-2010  04:01 pm MST, output experiencer
 * @history: Tuesday   06-02-2010  08:01 am MST, add support to output negation terms
 * @history: Monday    10-20-2009  10:32 pm MST, First_Created_Time
 * @history: Thursday  11-05-2009  03:32 pm MST, Add function for negative concept of classmention
 * @history: Thursday  11-26-2009  17:00 pm MST, Modify for generalization of complex slot
 * @history: Tuesday   12-29-2009  14:06 pm MST, Modify for data/time words
 * @since:   Sunday    05-16-2009  05:31 am MST, redesign, structure based on XML node level
 *
 * 
 */

package output.xmloutput;

/* ---------------------------------------------
 * jdom.jar should be set in your classpath
 * If not, please download the source codes
 * and complie to get a compatible one
 * ---------------------------------------------*/

import nlp.storageSpaceDraftLevel.Table_ConceptAttributes;
import nlp.storageSpaceDraftLevel.Table_Experiencer;
import nlp.storageSpaceDraftLevel.Table_Negation;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import org.jdom.*; 
import org.jdom.output.*;

/**
 *
 * @author Chris
 */
public class WriteToXML {
    private String _outputPath_, _originalFilename_;
    private static int _countOfUnnamedFiles_ = 0;
    private final static String _SUFFIXNAME_ = ".knowtator.xml";

    private final static String backup_annotator_name = "Extensible_Human_Oracle_Suite_of_Tools";
    private final static String backup_annotator_id = "eHOST_001";
    private String annotator_name;
    private String annotator_id;

    public WriteToXML( String _xmlOutputPath, String _originalFilenameInAbsolutePath ){
            _outputPath_ = _xmlOutputPath;
            _originalFilename_ = _originalFilenameInAbsolutePath;

            try{
                String name = resultEditor.workSpace.WorkSet.current_annotator_name;
                String id = resultEditor.workSpace.WorkSet.current_annotator_id;

                if((name!=null)&&(name.trim().length()>0))
                    annotator_name = name.trim();
                else annotator_name = backup_annotator_name;

                if((id!=null)&&(id.trim().length()>0))
                    annotator_id = id.trim();
                else annotator_id = backup_annotator_id;

            }catch(Exception ex){

            }

    }


    /** extract data and assemble the xml into disk
     * <p>
     * <li>Source of extracted data:
     * <li>fiters:
     *   <ul>filter of SSN
     *   <p>filter of date and time
     *   <p>filter of custom regular expression</ul>
     * </li>
     * <li>Algorithm:
     * <ul>concept found by Negex Algorithm
     * <p>negation found by Negex Algorithm
     * <ul>
     * </li>
     * </li>
     *
     * @return <code>null</code>
     */
    public void writeToDisk(){

        String filename = setOutputFilename();
        logs.ShowLogs.printImportantInfoLog("XML output path: " + filename);

        String filenameWithoutPath = commons.Filesys.cleanFileName( _originalFilename_ ) + ".txt";
        if((filenameWithoutPath == null)||(filenameWithoutPath.trim().length() < 1)){
            log.LoggingToFile.log( Level.SEVERE, "LOST FILENAME!!! - err.writetoxml.java.102" );
            logs.ShowLogs.printErrorLog("LOST FILENAME!!! - err.writetoxml.java.102");
            return;
        }

        // XML Assembly and Storage
        try{
            // initial the XML file and set the root node of the XML
            //System.out.println("annotationsStr is " + annotationsStr);
            Element root = new Element( "annotations" ); //
            //System.out.println("TextSource is " + TextSource);
            root.setAttribute( "textSource", filenameWithoutPath );
            Document Doc = new Document(root);



            /** add contents */
            root = XMLNodesforConcepts(root);

            if(env.Parameters.NLPAssistant.OUTPUTSSN){
                root = XMLNodesforSSNs(root);
            }

            if(env.Parameters.NLPAssistant.OUTPUTDATE){
                root = XMLNodesforDates(root);
            }

            if(env.Parameters.NLPAssistant.OUTPUTCUSTOMREGEX){
                root = XMLNodesforTermsFoundByCustomRegex(root);
            }

            // assemble the branch of negations if necessary
            if(env.Parameters.NLPAssistant.OUTPUTNEGATION){
                root = XMLNodesforNegations(root);
            }

            // assemble the branch of experiencer if necessary
            if(env.Parameters.NLPAssistant.OUTPUTEXPERIENCER){
                root = XMLNodesforExperiencers(root);
            }

            // **** output xml file to disk ****
            // XML storage processing: phycial writing
            Format format = Format.getCompactFormat();
            format.setEncoding("UTF-8"); // set XML encodeing
            format.setIndent("    ");
            XMLOutputter XMLOut = new XMLOutputter(format);
            // write to disk
            XMLOut.output(Doc, new FileOutputStream( filename ));
            // Screen Log
            log.LoggingToFile.log( Level.FINE, "Successfully genereted XML to - " + filename );
            logs.ShowLogs.printImportantInfoLog("Successfully genereted XML to - " + filename );
        }
        catch(Exception exc){
            // exc.printStackTrace();
            log.LoggingToFile.log( Level.SEVERE, "error occurred in WriteToXML.java - "
                    + exc.toString() );
            logs.ShowLogs.printErrorLog("WriteToXML.java - " + exc.toString());
        }
    }

    /**Assemble new xml branch of experiencers onto a given xml element
     * <p>
     * <li>Parameter
     * <ul><code>Element</code> _root: parent element of coming new xml
     * branch</li>
     * @return <code>Element</code>, add current xml branch into the element
     * we got from parameter _root.<p>
     */
    private Element XMLNodesforExperiencers(Element _root){
        // variable defined for return
        Element root = _root;

        // get how many paragraph in the storage memory on draft level
        int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();

         // if filelist or memory is empty, do nothing
        if( amount_of_paragraph == 0 ){
            log.LoggingToFile.log( Level.SEVERE, "WriteToXML.java - "+
                    "XMLNodesforexperiencers() - empty file" );
            logs.ShowLogs.printErrorLog("WriteToXML.java - "+
                    "XMLNodesforexperiencers() - empty file");
            return root;
        }

        // amount of found phrases of experiencer
        int amount_of_found = 0;

        // **** 2) get all paragraph and found experiencer
        // and convert to format of
        for(int i = 0; i< amount_of_paragraph; i++){

            // get experiencer information
            ArrayList<Table_Experiencer> experiencers
                    = nlp.storageSpaceDraftLevel.StorageSpace.getExperiencer(i);

            if ( experiencers == null)
                    continue;

            // go over all phrases ofexperiencer in this arraylist to a paragraph
            int experiencerAmount = experiencers.size();
            for(int j=0; j<experiencerAmount; j++){

                Table_Experiencer experiencer
                        = experiencers.get(j);

                String info = "outputing to xml - experiencer: ["
                        + experiencer.expericnerText
                        + "] , located at [" + experiencer.spanstart
                        + ", " + experiencer.spanend + "]";

                log.LoggingToFile.log( Level.INFO, info );
                logs.ShowLogs.printInfoLog( info );


                // -----------------------------------------------------elements
                Element annotation = new Element("annotation");

                Element mention = new Element("mention");
                //System.out.println("mention id - " + annotationRecords.mentionId );
                mention.setAttribute("id", "EHOST_Instance_" + experiencer.mentionID );
                annotation.addContent(mention);

                Element annotator = new Element("annotator");
                annotator.setAttribute("id", annotator_id);
                //System.out.println("annotator id - " + annotationRecords.annotatorId );
                
                //System.out.println("annotator text - " + annotationRecords.annotator );
                annotator.setText(annotator_name);
                annotation.addContent(annotator);

                Element span = new Element("span");
                span.setAttribute("start", String.valueOf( experiencer.spanstart ));
                span.setAttribute("end", String.valueOf( experiencer.spanend ));
                annotation.addContent(span);

                annotation.addContent( new Element("spannedText").setText(experiencer.expericnerText));
                annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                root.addContent(annotation);
                // -----------------------------------------------------elements end


                // -----------------------------------------------------classmention begin
                // "classMention" data structure and get stored data in memory
                Element classMention = new Element("classMention");
                classMention.setAttribute("id", "EHOST_Instance_"+ experiencer.mentionID );
                Element mentionClass = new Element( "mentionClass" );
                mentionClass.setAttribute( "id", "experiencer" );
                mentionClass.addContent( experiencer.expericnerText );
                //add this node <ClassMention> to xml tree
                classMention.addContent( mentionClass );
                root.addContent( classMention );
                // -----------------------------------------------------classmention end

                amount_of_found++;
            }
        }
        log.LoggingToFile.log( Level.INFO, amount_of_found + " phrases of experiencer got found" );
        logs.ShowLogs.printImportantInfoLog( amount_of_found + " phrases of experiencer got found" );

        return root;
    }

    /**Assemble new xml branch of negations onto a given xml element
     * <p>
     * <li>Parameter
     * <ul><code>Element</code> _root: parent element of coming new xml
     * branch
     * </li>
     * @return <code>Element</code>, add current xml branch into the element
     * we got from parameter _root.
     */
    private Element XMLNodesforNegations(Element _root){
        Element root = _root;
        String errorstr ;

            // get how many paragraph in the storage memory on draft level
            int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();

             // if filelist or memory is empty, do nothing
            if( amount_of_paragraph == 0 ){
                errorstr = "empty file - err.writetoxml.java.010";
                log.LoggingToFile.log( Level.SEVERE, errorstr );
                logs.ShowLogs.printErrorLog( errorstr);
                return root;
            }

            // amount of found negation terms
            int amount_of_found = 0;

            // **** 2) get all paragraph and found negation details
            // and convert to format of
            for(int i = 0; i< amount_of_paragraph; i++){

                    // get negation information
                    ArrayList<Table_Negation> negations
                            = nlp.storageSpaceDraftLevel.StorageSpace.getNegations(i);

                    if ( negations == null)
                            continue;

                    // go over all negation records in this arraylist to a paragraph
                    int negationSize = negations.size();
                    for(int j=0; j<negationSize; j++){

                            Table_Negation negation
                                    = negations.get(j);

                            errorstr = "outputing to xml - negations: ["
                                    + negation.negationText
                                    + "] , located at [" + negation.spanStart_InFile
                                    + ", " + negation.spanEnd_InFile + "]";
                            log.LoggingToFile.log( Level.INFO, errorstr );
                            logs.ShowLogs.printInfoLog( errorstr );


                            // -----------------------------------------------------elements
                            Element annotation = new Element("annotation");

                            Element mention = new Element("mention");
                            //System.out.println("mention id - " + annotationRecords.mentionId );
                            mention.setAttribute("id", "EHOST_Instance_" + negation.mentionID );
                            
                            annotation.addContent(mention);

                            Element annotator = new Element("annotator");
                            annotator.setAttribute("id", annotator_id);
                            //System.out.println("annotator id - " + annotationRecords.annotatorId );
                            //System.out.println("annotator text - " + annotationRecords.annotator );
                            annotator.setText(annotator_name);
                            annotation.addContent(annotator);

                            Element span = new Element("span");
                            span.setAttribute("start", String.valueOf( negation.spanStart_InFile ));
                            span.setAttribute("end", String.valueOf( negation.spanEnd_InFile ));
                            annotation.addContent(span);

                            annotation.addContent( new Element("spannedText").setText(negation.negationText));
                            annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                            root.addContent(annotation);
                            // -----------------------------------------------------elements end


                            // -----------------------------------------------------classmention begin
                            // "classMention" data structure and get stored data in memory
                            Element classMention = new Element("classMention");
                            classMention.setAttribute("id", "EHOST_Instance_"+ negation.mentionID );
                            Element mentionClass = new Element( "mentionClass" );
                            mentionClass.setAttribute( "id", "negation" );
                            mentionClass.addContent( negation.negationText );
                            //add this node <ClassMention> to xml tree
                            classMention.addContent( mentionClass );
                            root.addContent( classMention );
                            // -----------------------------------------------------classmention end

                            amount_of_found++;


                    }
            }
            log.LoggingToFile.log( Level.INFO, amount_of_found + " valid negations terns got found." );
            logs.ShowLogs.printImportantInfoLog( amount_of_found + " valid negations terns got found" );

            return root;
    }

    private Element XMLNodesforTermsFoundByCustomRegex(Element _root){
            Element root = _root;

            // get how many paragraph in the storage memory of draft level
            int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();
             // if filelist or memory is empty, do nothing
            if( amount_of_paragraph == 0 ){
                log.LoggingToFile.log( Level.SEVERE,"empty file - err.writetoxml.java.005");
                logs.ShowLogs.printErrorLog("empty file - err.writetoxml.java.005");
                return root;
            }

            int amount_of_found = 0;

            // **** 2) get all paragraph and found informations from algorithms
            // and convert to format of
            for(int i = 0; i< amount_of_paragraph; i++)
            {
                    // ****3.1) prepare a instant in format of coloredFormat
                    //dialogsShowNotes.ColoredFormat thisColoringParagraph = new dialogsShowNotes.ColoredFormat();

                    // get concept information
                    ArrayList<nlp.storageSpaceDraftLevel.Table_CustomRegex> TermDetails
                            = nlp.storageSpaceDraftLevel.StorageSpace.getTermDetails_FoundByCustomRegex(i);

                    if ( TermDetails == null)
                            continue;

                    // ****4 get found concept details and mark color array
                    // by coordinates of concepts
                    int crSize = TermDetails.size();

                    for(int j=0; j<crSize; j++){

                            nlp.storageSpaceDraftLevel.Table_CustomRegex cr
                                    = (nlp.storageSpaceDraftLevel.Table_CustomRegex)TermDetails.get(j);
                            String info = "outputing to xml - terms found by custom regular expression: ["
                                    + cr.terms
                                    + "] ,  to class-["
                                    + cr.classname+
                                    "]; located at [" + cr.start
                                    + ", " + cr.end + "]";

                            log.LoggingToFile.log( Level.INFO, info );
                            logs.ShowLogs.printInfoLog( info );


                            // -----------------------------------------------------elements
                            Element annotation = new Element("annotation");

                            Element mention = new Element("mention");
                            //System.out.println("mention id - " + annotationRecords.mentionId );
                            mention.setAttribute("id", "EHOST_Instance_" + cr.mentionID );
                            annotation.addContent(mention);

                            Element annotator = new Element("annotator");
                            annotator.setAttribute("id", annotator_id);
                            //System.out.println("annotator id - " + annotationRecords.annotatorId );
                            //System.out.println("annotator text - " + annotationRecords.annotator );
                            annotator.setText(annotator_name);
                            annotation.addContent(annotator);

                            Element span = new Element("span");
                            span.setAttribute("start", String.valueOf( cr.start ));
                            span.setAttribute("end", String.valueOf( cr.end ));
                            annotation.addContent(span);

                            annotation.addContent( new Element("spannedText").setText(cr.terms));
                            annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                            root.addContent(annotation);
                            // -----------------------------------------------------elements end


                            // -----------------------------------------------------classmention begin
                            // "classMention" data structure and get stored data in memory
                            Element classMention = new Element("classMention");
                            classMention.setAttribute("id", "EHOST_Instance_"+ cr.mentionID );
                            Element mentionClass = new Element( "mentionClass" );
                            mentionClass.setAttribute( "id", cr.classname );
                            mentionClass.addContent( cr.terms );
                            //add this node <ClassMention> to xml tree
                            classMention.addContent( mentionClass );
                            root.addContent( classMention );
                            // -----------------------------------------------------classmention end

                            amount_of_found++;


                    }
            }

            log.LoggingToFile.log( Level.INFO, amount_of_found + " valid term(s) found by custom regular expression" );
            logs.ShowLogs.printImportantInfoLog( amount_of_found + " valid term(s) found by custom regular expression" );

            return root;
    }

    private Element XMLNodesforSSNs(Element _root){
            Element root = _root;

            // get how many paragraph in the storage memory of draft level
            int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();
             // if filelist or memory is empty, do nothing
            if( amount_of_paragraph == 0 ){
                log.LoggingToFile.log( Level.SEVERE, "empty file - err.writetoxml.java.004");
                logs.ShowLogs.printErrorLog("empty file - err.writetoxml.java.004");
                return root;
            }

            int amount_of_found = 0;

            // **** 2) get all paragraph and found informations from algorithms
            // and convert to format of
            for(int i = 0; i< amount_of_paragraph; i++){
                    // ****3.1) prepare a instant in format of coloredFormat
                    //dialogsShowNotes.ColoredFormat thisColoringParagraph = new dialogsShowNotes.ColoredFormat();

                    // get concept information
                    ArrayList<nlp.storageSpaceDraftLevel.Table_SSN> SSNDetails = nlp.storageSpaceDraftLevel.StorageSpace.getSSNDetails(i);

                    if ( SSNDetails == null)
                            continue;

                    // ****4 get found concept details and mark color array
                    // by coordinates of concepts
                    int ssnSize = SSNDetails.size();

                    for(int j=0; j<ssnSize; j++){

                            nlp.storageSpaceDraftLevel.Table_SSN SSN
                                    = (nlp.storageSpaceDraftLevel.Table_SSN)SSNDetails.get(j);

                            String infostr = "outputing to xml - SSN: ["
                                    + SSN.SSN
                                    + "] , located at [" + SSN.span_start_in_file
                                    + ", " + SSN.span_end_in_file + "]";
                            log.LoggingToFile.log( Level.INFO, infostr );
                            logs.ShowLogs.printInfoLog( infostr );


                            // -----------------------------------------------------elements
                            Element annotation = new Element("annotation");

                            Element mention = new Element("mention");
                            //System.out.println("mention id - " + annotationRecords.mentionId );
                            mention.setAttribute("id", "EHOST_Instance_" + SSN.mentionID );
                            annotation.addContent(mention);

                            Element annotator = new Element("annotator");
                            annotator.setAttribute("id", annotator_id);
                            //System.out.println("annotator id - " + annotationRecords.annotatorId );
                            //System.out.println("annotator text - " + annotationRecords.annotator );
                            annotator.setText(annotator_name);
                            annotation.addContent(annotator);

                            Element span = new Element("span");
                            span.setAttribute("start", String.valueOf( SSN.span_start_in_file ));
                            span.setAttribute("end", String.valueOf( SSN.span_end_in_file));
                            annotation.addContent(span);

                            annotation.addContent( new Element("spannedText").setText(SSN.SSN));
                            annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                            root.addContent(annotation);
                            // -----------------------------------------------------elements end


                            // -----------------------------------------------------classmention begin
                            // "classMention" data structure and get stored data in memory
                            Element classMention = new Element("classMention");
                            classMention.setAttribute("id", "EHOST_Instance_"+ SSN.mentionID );
                            Element mentionClass = new Element( "mentionClass" );
                            mentionClass.setAttribute( "id", "SSN" );
                            mentionClass.addContent( SSN.SSN );
                            //add this node <ClassMention> to xml tree
                            classMention.addContent( mentionClass );
                            root.addContent( classMention );
                            // -----------------------------------------------------classmention end

                            amount_of_found++;


                    }
            }

            log.LoggingToFile.log( Level.INFO,amount_of_found + " valid SSN found(s)" );
            logs.ShowLogs.printImportantInfoLog( amount_of_found + " valid SSN found(s)" );

            return root;
    }

    private Element XMLNodesforDates(Element _root){
            Element root = _root;

            // get how many paragraph in the storage memory of draft level
            int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();
             // if filelist or memory is empty, do nothing
            if( amount_of_paragraph == 0 ){
                    log.LoggingToFile.log( Level.SEVERE, "empty file - err.writetoxml.java.007" );
                    logs.ShowLogs.printErrorLog("empty file - err.writetoxml.java.007");
                    return root;
            }

            int amount_of_found = 0;

            // **** 2) get all paragraph and found informations from algorithms
            // and convert to format of
            for(int i = 0; i< amount_of_paragraph; i++){
                    // ****3.1) prepare a instant in format of coloredFormat
                    //dialogsShowNotes.ColoredFormat thisColoringParagraph = new dialogsShowNotes.ColoredFormat();

                    // get concept information
                    ArrayList<nlp.storageSpaceDraftLevel.Table_DateTime> dates
                            = nlp.storageSpaceDraftLevel.StorageSpace.getDates(i);

                    if ( dates == null)
                            continue;

                    // ****4 get found concept details and mark color array
                    // by coordinates of concepts
                    int dateSize = dates.size();

                    for(int j=0; j<dateSize; j++){

                            nlp.storageSpaceDraftLevel.Table_DateTime date
                                    = dates.get(j);

                            String infostr = "outputing to xml - dates: ["
                                    + date.termText
                                    + "] , located at [" + date.start
                                    + ", " + date.end + "]";
                            log.LoggingToFile.log( Level.INFO, infostr);
                            logs.ShowLogs.printInfoLog( infostr );


                            // -----------------------------------------------------elements
                            Element annotation = new Element("annotation");

                            Element mention = new Element("mention");
                            //System.out.println("mention id - " + annotationRecords.mentionId );
                            mention.setAttribute("id", "EHOST_Instance_" + date.mentionID );
                            annotation.addContent(mention);

                            Element annotator = new Element("annotator");
                            annotator.setAttribute("id", annotator_id);
                            //System.out.println("annotator id - " + annotationRecords.annotatorId );
                            //System.out.println("annotator text - " + annotationRecords.annotator );
                            annotator.setText(annotator_name);
                            annotation.addContent(annotator);

                            Element span = new Element("span");
                            span.setAttribute("start", String.valueOf( date.start ));
                            span.setAttribute("end", String.valueOf( date.end ));
                            annotation.addContent(span);

                            annotation.addContent( new Element("spannedText").setText(date.termText));
                            annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                            root.addContent(annotation);
                            // -----------------------------------------------------elements end


                            // -----------------------------------------------------classmention begin
                            // "classMention" data structure and get stored data in memory
                            Element classMention = new Element("classMention");
                            classMention.setAttribute("id", "EHOST_Instance_"+ date.mentionID );
                            Element mentionClass = new Element( "mentionClass" );
                            mentionClass.setAttribute( "id", "Date" );
                            mentionClass.addContent( date.termText );
                            //add this node <ClassMention> to xml tree
                            classMention.addContent( mentionClass );
                            root.addContent( classMention );
                            // -----------------------------------------------------classmention end

                            amount_of_found++;

                    }
            }

            log.LoggingToFile.log( Level.INFO, amount_of_found
                    + " valid date&time term found(s)" );
            logs.ShowLogs.printImportantInfoLog( amount_of_found
                    + " valid date&time term found(s)" );

            return root;
    }


    private Element XMLNodesforConcepts(Element _root){
        Element root = _root;

        // get how many paragraph in the storage memory of draft level
        int amount_of_paragraph = nlp.storageSpaceDraftLevel.StorageSpace.Length();
         // if filelist or memory is empty, do nothing
        if( amount_of_paragraph == 0 ){
                log.LoggingToFile.log( Level.SEVERE, "empty file - " +
                        "err.writetoxml.java.003");
                logs.ShowLogs.printErrorLog("empty file - " +
                        "err.writetoxml.java.003");
                return root;
        }

        int amount_of_found = 0;

        // **** 2) get all paragraph and found informations from algorithms
        // and convert to format of
        for(int i = 0; i< amount_of_paragraph; i++){
            // ****3.1) prepare a instant in format of coloredFormat
            //dialogsShowNotes.ColoredFormat thisColoringParagraph =
            // new dialogsShowNotes.ColoredFormat();

            // get concept information
            ArrayList conceptDetail = nlp.storageSpaceDraftLevel.StorageSpace
                    .getConceptDetails(i);

            if (conceptDetail == null)
                    continue;

            // ****4 get found concept details and mark color array
            // by coordinates of concepts
            int cdSize = conceptDetail.size();

            for(int j=0; j<cdSize; j++){

                nlp.storageSpaceDraftLevel.Table_Concept concepts
                        = (nlp.storageSpaceDraftLevel.Table_Concept)conceptDetail
                        .get(j);
                
                if( concepts.concept_text == null )
                        continue;
                int start = concepts.span_start_in_file;
                int end = concepts.span_end_in_file;
                String conceptText = concepts.concept_text;

                String infostr = "Negex_VA_V1.20.03222010"
                        + Integer.toString(start)
                        + Integer.toString(end)
                        + conceptText;
                log.LoggingToFile.log( Level.INFO, infostr );
                logs.ShowLogs.printInfoLog( infostr );


                // -----------------------------------------------------elements
                Element annotation = new Element("annotation");

                Element mention = new Element("mention");
                //System.out.println("mention id - " + annotationRecords.mentionId );
                mention.setAttribute("id", "EHOST_Instance_"+concepts.mentionID );
                annotation.addContent(mention);

                Element annotator = new Element("annotator");
                annotator.setAttribute("id", annotator_id);
                //System.out.println("annotator id - " + annotationRecords.annotatorId );
                //System.out.println("annotator text - " + annotationRecords.annotator );
                annotator.setText(annotator_name);
                annotation.addContent(annotator);

                Element span = new Element("span");
                span.setAttribute("start", String.valueOf( concepts.span_start_in_file ));
                span.setAttribute("end", String.valueOf( concepts.span_end_in_file));
                annotation.addContent(span);

                annotation.addContent( new Element("spannedText").setText(concepts.concept_text));
                annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

                root.addContent(annotation);
                // -----------------------------------------------------elements end


                // -----------------------------------------------------classmention begin
                // "classMention" data structure and get stored data in memory
                Element classMention = new Element("classMention");
                classMention.setAttribute("id", "EHOST_Instance_"+concepts.mentionID );
                Element mentionClass = new Element( "mentionClass" );
                mentionClass.setAttribute( "id", concepts.comment.trim() );
                mentionClass.addContent( concepts.concept_text );
                classMention.addContent( mentionClass );

                //hasSlotMention
                int hasSlotSize = concepts.attributes.size();
                for( int slot = 0; slot < hasSlotSize; slot++){
                    Table_ConceptAttributes ca = concepts.attributes.get(slot);
                    Element hasClassMention = new Element( "hasSlotMention" );
                    hasClassMention.setAttribute( "id",
                            "EHOST_Instance_"+ ca.hasClassMentionID );
                    classMention.addContent( hasClassMention );
                }


                root.addContent( classMention );


                for( int slot = 0; slot < hasSlotSize; slot++){
                    Table_ConceptAttributes ca = concepts.attributes.get(slot);
                    // "string slot mention"
                    Element stringSlotMention = new Element( "stringSlotMention" );
                    stringSlotMention.setAttribute( "id",
                            "EHOST_Instance_"+ ca.hasClassMentionID );
                    // "mentionSlot"
                    Element mentionSlot = new Element ("mentionSlot");
                    mentionSlot.setAttribute("id", ca.mentionSlotID);
                    stringSlotMention.addContent(mentionSlot);

                    //"stringSlotMentionValue"
                    Element stringSlotMentionValue = new Element ("stringSlotMentionValue");
                    stringSlotMentionValue.setAttribute("value", ca.slotMentionValue);
                    stringSlotMention.addContent(stringSlotMentionValue);

                    // end of "string slot mention"
                    root.addContent( stringSlotMention );
                }
                // -----------------------------------------------------classmention end

                amount_of_found++;

            }
        }

        log.LoggingToFile.log( Level.INFO,amount_of_found + " Concepts found(s)" );
        logs.ShowLogs.printImportantInfoLog( amount_of_found + " Concepts found(s)" );

        return root;
    }
            /*try{
                    int amount_of_paragraph = 0; // amount of paragraphs in the storage memory on draft level

                    logs.ShowLogs.printImportantInfoLog("Assembling XML output !!!");
                    logs.ShowLogs.printImportantInfoLog("XML: output path = " + _xml_output_path_ );
                    logs.ShowLogs.printImportantInfoLog("XML: processed clinical files = " + _amount_of_processed_files_ );
                    // get how many paragraph in the storage memory of draft level
                    amount_of_paragraph = StorageSpaceDraftLevel.StorageSpace.Length();
                    logs.ShowLogs.printImportantInfoLog("XML: " + amount_of_paragraph + "paragraph processed." );

                    // if filelist or memory is empty, do nothing
                    if( amount_of_paragraph == 0 ){
                            logs.ShowLogs.printErrorLog("empty file - err.output.java.001");
                            return;
                    }

                    // class of XML data collector
                    nlp.XMLMaker xmlDataManager  = new nlp.XMLMaker();
                    xmlDataManager.setOutputPath( _xml_output_path_ );
                    XMLMaker.LATEST_NEGATION_PHRASE_SPAN_END = 0;
                    XMLMaker.LATEST_NEGATION_PHRASE_SPAN_START = 0;


                    // how many files processed, how many xml file will be generated
                    //dialogsShowlogs.ShowLogs.printLog("XML confirm they got "+ _amount_of_processed_files_ + "processed files.");


                    String cleanFileName = commons.Filesys.cleanFileName( _original_filename_ );
                    xmlDataManager.setOutputFileName( cleanFileName );

                    // XML:: set XML output filename parameter
                    xmlDataManager.clean();



                    //}

                    return;

            }catch(Exception e){
                    logs.ShowLogs.printErrorLog( e.toString() );
            }
            return;*/



    // mac and windows operation system uses different path separator
    private String setDefaultPathByOSType(){
            String path;
            if( env.Parameters.isUnixOS ){
                    path = "./xmloutput/";      // for mac os system
            }else {path = ".\\xmloutput\\"; }   // for windows system

            return path;
    }

    // assemble output filename - just part of filename, w/o path
    public String assembleXMLFileName(String _cleanFileName, //_cleanFileName: no path, no suffix name, no point
            String _outputPath,
            String _fileExtendedAttributesName )
    {

        log.LoggingToFile.log( Level.CONFIG, "cleanfile/outputpath/fileextendname - [" + _cleanFileName
                + "] ,[" + _outputPath +"] ["
                + _fileExtendedAttributesName+ "]");
        System.out.println("cleanfile/outputpath/fileextendname - [" + _cleanFileName
                + "] ,[" + _outputPath +"] ["
                + _fileExtendedAttributesName+ "]");
        String XMLFileName = null;
            String sep;
            if( env.Parameters.isUnixOS ){
                sep = "/";
            }else{
                sep = "\\";
            }
            if ( _cleanFileName == null) {
                    _countOfUnnamedFiles_++;
                    XMLFileName = _outputPath + sep + "NoName" + _countOfUnnamedFiles_ + _fileExtendedAttributesName;
            }else{
                    String TextSource = _cleanFileName + ".txt";
                    XMLFileName = _outputPath + sep + TextSource + _fileExtendedAttributesName;
            }

            return XMLFileName;
    }

    private String setOutputFilename(){
            String filename;
            String defaultOutputPath = setDefaultPathByOSType();

            if ((_outputPath_ == null)||(_outputPath_.trim().length() < 1 ))
                    _outputPath_ = defaultOutputPath;

            String cleanFileName = commons.Filesys.cleanFileName( _originalFilename_ );

            filename = assembleXMLFileName( cleanFileName, _outputPath_, _SUFFIXNAME_ );

            return filename;
    }

    // get current date and time
    private String getCurrentDate(){
            Calendar rightnow = Calendar.getInstance();
            String CurrentDate = rightnow.getTime().toString();
            //System.out.println(CurrentDate);
            return CurrentDate;
    }
}

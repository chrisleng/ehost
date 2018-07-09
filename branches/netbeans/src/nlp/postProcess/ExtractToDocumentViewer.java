/**
 * Classname: ExtractToDocumentViewer
 *
 * Version information:
 *
 * Date:    2010/2011
 *
 */

package nlp.postProcess;

import java.io.File;
import java.util.ArrayList;
import nlp.storageSpaceDraftLevel.Table_CustomRegex;
import nlp.storageSpaceDraftLevel.Table_DateTime;
import resultEditor.annotationClasses.Depot;

/**
 *
 * @author leng
 */
public class ExtractToDocumentViewer {

    private static userInterface.GUI gui = null;

    public static void setGUI(userInterface.GUI gui){
        ExtractToDocumentViewer.gui = gui;
    }

    public void ShowOnDocumentViewer(){

        copytoDepot();

        // refresh screen
        //if(gui!=null){
        //    gui.refreshResultEditor();
        //}
    }

    private void saveConcepts( String filename, ArrayList _conceptDetailsInArrayList )
    {
            ArrayList modifiedResults = _conceptDetailsInArrayList;
            int size = _conceptDetailsInArrayList.size();



            // compare a entry result to others
            for(int i=0; i<size;i++)
            {
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_Concept cd
                    = (nlp.storageSpaceDraftLevel.Table_Concept) modifiedResults.get(i);

                resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
                //System.out.println("==  ADDING: to file: [" + filename + "]");
                //System.out.println("            text [" + regexResult.concept_text + "]" );
                //System.out.println("            text [" + regexResult.span_start_in_file + ", "+ regexResult.span_end_in_file +  "]" );
                //System.out.println();

                File f = new File(filename);


                // get the classname
                String classname;
                if(cd.foundclassname){
                    if((cd.classname!=null)&&(cd.classname.trim().length()>0)){
                        classname = cd.classname.trim();
                    }else{
                        classname = "unknown";
                    }
                }else{
                    try{
                        dictionaries.ConceptDictionaryFormat df
                            = dictionaries.ConceptDictionaries.ConceptArray.get(cd.found_by_which_dictionary);
                        classname = df.Comment.get(cd.found_by_which_entry);
                    }catch(Exception ex){
                        classname = "unknown";
                    }
                }
                
                
                
                // classname can't be "unknown"
                if( classname.compareTo( "unknown") == 0 )
                    continue;
                
                String time = commons.Tools.getTimeStamper();

                if(cd.concept_text!=null)
                    depot.addANewAnnotation(f.getName(), 
                        cd.concept_text,
                        cd.span_start_in_file,
                        cd.span_end_in_file,
                        time,
                        classname,
                        "eHOST",
                        "annotator-id",
                        null,
                        null,
                        cd.uniqueindex);
                
                 System.out.println( "file:["+f.getName()+"]: " + cd.uniqueindex );
                depot.setAttributeDefault( f.getName(), cd.uniqueindex );
            }




    }
    
    
    private void saveCustomRegex( String filename, ArrayList<Table_CustomRegex> _regexResults )
    {
            ArrayList<Table_CustomRegex> modifiedResults = _regexResults;
            int size = _regexResults.size();



            // compare a entry result to others
            for(int i=0; i<size;i++)
            {
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_CustomRegex regexResult
                    =  modifiedResults.get(i);

                resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
                //System.out.println("==  ADDING: to file: [" + filename + "]");
                //System.out.println("            text [" + regexResult.concept_text + "]" );
                //System.out.println("            text [" + regexResult.span_start_in_file + ", "+ regexResult.span_end_in_file +  "]" );
                //System.out.println();

                File f = new File(filename);


                // get the classname
                String classname = regexResult.classname;
                
                
                
                // classname can't be "unknown"
                if( (classname == null) || ( classname.compareTo( "unknown") == 0 ) )
                    continue;
                
                if( (regexResult.terms == null) || ( regexResult.terms.trim().length() < 1 ) )
                    continue;
                
                String time = commons.Tools.getTimeStamper();

                
                    depot.addANewAnnotation(f.getName(), 
                        regexResult.terms,
                        regexResult.start,
                        regexResult.end,
                        time,
                        classname,
                        "eHOST",
                        "annotator-id",
                        null,
                        null,
                        regexResult.uniqueindex);
                
                 System.out.println( "file:["+f.getName()+"]: " + regexResult.uniqueindex );
                depot.setAttributeDefault( f.getName(), regexResult.uniqueindex );
            }




    }
    
    private void saveSSN( String filename, ArrayList<nlp.storageSpaceDraftLevel.Table_SSN> ssns )
    {
        resultEditor.annotationClasses.Depot depot1 = new resultEditor.annotationClasses.Depot();
            if(depot1.getAnnotatedClass("SSN")==null){
                Depot.addElement("SSN", "NLP", 0,244,234 );                
            }
            
        
            ArrayList<nlp.storageSpaceDraftLevel.Table_SSN> ssndepot = ssns;
            int size = ssns.size();



            // compare a entry result to others
            for(int i=0; i<size;i++)
            {
                // *** get current concept result
                nlp.storageSpaceDraftLevel.Table_SSN ssn
                    =  ssndepot.get(i);

                resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
                //System.out.println("==  ADDING: to file: [" + filename + "]");
                //System.out.println("            text [" + regexResult.concept_text + "]" );
                //System.out.println("            text [" + regexResult.span_start_in_file + ", "+ regexResult.span_end_in_file +  "]" );
                //System.out.println();

                File f = new File(filename);


                // get the classname
                String classname = "SSN";
                
                
                
                // classname can't be "unknown"

                if( (ssn.SSN == null) || ( ssn.SSN.trim().length() < 1 ) )
                    continue;
                
                
                
                String time = commons.Tools.getTimeStamper();

                int latestUsedMentionID = env.Parameters.getLatestUsedMentionID();
                latestUsedMentionID++;
                env.Parameters.updateLatestUsedMentionID( latestUsedMentionID );
                
                    depot.addANewAnnotation(f.getName(), 
                        ssn.SSN,
                        ssn.span_start_in_file,
                        ssn.span_end_in_file,
                        time,
                        classname,
                        "eHOST",
                        "annotator-id",
                        null,
                        null,
                        latestUsedMentionID);
                
                 System.out.println( "file:["+f.getName()+"]: " +  latestUsedMentionID );
                 //depot.setAttributeDefault( f.getName(), ssn.uniqueindex );
            }

            
            
            
            

    }
    
    private void saveDate( String filename, ArrayList<Table_DateTime> dates )
    {
        resultEditor.annotationClasses.Depot depot1 = new resultEditor.annotationClasses.Depot();
            if(depot1.getAnnotatedClass("Date")==null){
                Depot.addElement("Date", "NLP", 100,244,234 );                
            }
            
            
            int size = dates.size();



            // compare a entry result to others
            for(int i=0; i<size;i++)
            {
                // *** get current concept result
                Table_DateTime date
                    =  dates.get(i);

                resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();
                //System.out.println("==  ADDING: to file: [" + filename + "]");
                //System.out.println("            text [" + regexResult.concept_text + "]" );
                //System.out.println("            text [" + regexResult.span_start_in_file + ", "+ regexResult.span_end_in_file +  "]" );
                //System.out.println();

                File f = new File(filename);


                // get the classname
                String classname = "Date";
                
                
                
                // classname can't be "unknown"

                if( (date.termText == null) || ( date.termText.trim().length() < 1 ) )
                    continue;
                
                
                
                String time = commons.Tools.getTimeStamper();

                int latestUsedMentionID = env.Parameters.getLatestUsedMentionID();
                latestUsedMentionID++;
                env.Parameters.updateLatestUsedMentionID( latestUsedMentionID );
                
                    depot.addANewAnnotation(f.getName(), 
                        date.termText,
                        date.start,
                        date.end,
                        time,
                        classname,
                        "eHOST",
                        "annotator-id",
                        null,
                        null,
                        latestUsedMentionID);
                
                 System.out.println( "file:["+f.getName()+"]: " +  latestUsedMentionID );
                 //depot.setAttributeDefault( f.getName(), ssn.uniqueindex );
            }


            

    }

    private void copytoDepot(){
        // find how many paragraph had been processed
        int number_of_paragraphs = nlp.storageSpaceDraftLevel.StorageSpace.Length();
        // if no paragraph got processed, exit from this function
        if(number_of_paragraphs <= 0)
            return;

        // **** to all records in the storage space, if they belong to the specific file
        // apply the concept result filter to it
        for(int i=0;i<number_of_paragraphs;i++)
        {
            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                = (nlp.storageSpaceDraftLevel.DraftLevelFormat)nlp.storageSpaceDraftLevel.StorageSpace.getParagraphInFormat(i);

            /** [1] assign mention id to concepts*/
            // if information belong to current file
            // apply the concept result filter
            ArrayList conceptsdetails = cef.ConceptDetails;
            // assign id for these concept results
            if((conceptsdetails!=null)&&(conceptsdetails.size()>0))
                saveConcepts(cef.filename, conceptsdetails);

            try{
                ArrayList<Table_CustomRegex> customRegex  = cef.CustomRegex;
                if((customRegex!=null)&&(customRegex.size()>0))
                    saveCustomRegex(cef.filename, customRegex);    
            }catch(Exception ex){
                ex.printStackTrace();
            }
            
            try{
            if( env.Parameters.NLPAssistant.OUTPUTSSN ){
                ArrayList<nlp.storageSpaceDraftLevel.Table_SSN> SSNDs = cef.SSNDetails;
                
                if((SSNDs!=null)&&(SSNDs.size()>0))
                    saveSSN(cef.filename, SSNDs);  
                
                // assign id for these concept results
                //ArrayList modifiedSSNArrayList = assignAndRecordMentionID_toSSN(SSNDs);
                //updateSSNs_into_StorageSpaceDraftLevel(i, modifiedSSNArrayList );
            }
            }catch(Exception ex){
                System.out.println("error:: failed to output ssn");
            }

            // [3] assign mention id to Date and time terms

            if( env.Parameters.NLPAssistant.OUTPUTDATE ){
                ArrayList<Table_DateTime> dates = cef.Dates;
                
                if((dates!=null)&&(dates.size()>0))
                    saveDate(cef.filename, dates); 
                // assign id for these concept results
                //ArrayList modifiedSSNArrayList = assignAndRecordMentionID_toDates(dates);
                //updateDates_into_StorageSpaceDraftLevel(i, modifiedSSNArrayList );
            }
/*
            // update these modified concept results to the paragraph
            // and update the paragraph in the storagespace in draft level
            updateConcepts_into_StorageSpaceDraftLevel(i, modifiedConceptArrayList);

            // [2] assign mention id to SSN

            

            // [4] assign mention id to terms found by filter of
              custom regular expression

            if(env.Parameters.OUTPUTCUSTOMREGEX){
                ArrayList<Table_CustomRegex> customRegexs = cef.CustomRegex;
                // assign id for these concept results
                ArrayList<Table_CustomRegex> modifiedCustomRegexArrayList
                        = assignAndRecordMentionID_toCustomRegexs(customRegexs);
                updateCustomRegexs_into_StorageSpaceDraftLevel(i, modifiedCustomRegexArrayList );
            }

            // [5] assign mention id to negation phrases

            if(env.Parameters.OUTPUTNEGATION){

                ArrayList<Table_Negation> negations = cef.Negations;
                // assign mention id for these negations
                assignMentionID_toNegations(negations);

            }

            // [6] assign mention id to experiencer

            if(env.Parameters.OUTPUTEXPERIENCER){
                ArrayList<Table_Experiencer> experiencer = cef.Experiencer;
                assignMentionID_toExperiencer(experiencer);
            }
            * 
            * 
            */
           


        }
    }
}

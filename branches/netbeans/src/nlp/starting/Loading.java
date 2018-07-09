package nlp.starting;


        


import algorithmNegex.NegexAlgorithm;
import nlp.postProcess.ExtractToDocumentViewer;
import annotate.gui.NLPcpu;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Vector;

/*
 * Load text from files and run filters and algorithms on them, then save all
 * found terms into XML files.
 *
 * @since Created on January 20, 2008, 11:09 AM
 * @author Jianwei Leng
 */
public class Loading
{
    private static String cleanNoteFileName, originalNoteFileName;
    private static boolean _flashScreen_;

    /** Creates a new instance of TextProcessing */
    public Loading()
    {
        cleanNoteFileName = "cleanednote_cxr.txt";
        originalNoteFileName = "input.txt";
    }

    public void DataCleanup(){

        //static CleanUpModule cleanup = new nlp.CleanUpModule(this.originalNoteFileName);
    }

    // *********************************** //
    // read content of files and imply the analysis by those nlp algorithm
    // 1. initial the data collector
    // 2. get filename and path from the static memory space and check their validities
    // 3. get contents from file, one each time
    //    do NLP algorithms and founders(this need to call another method)
    // %%%% design flow chart see FC.2
    public static void Loading_and_Processing(
            // flags: indicates what algorithm and nlp method are wanted
            //boolean _flag_implement_dates_founder,
            //boolean _flag_implement_negation_algorithm,
            // if each paragraph in this file contains patient-id and note-id,
            // we call this file is a 3 columns file
            // and we will use the note id as the output xml file name
            //boolean _isThreeColumnFile,
            // xml output path name, just the full path
            String _xmloutput_path )
    {
        resultEditor.runningStatus.Status status =
                new resultEditor.runningStatus.Status();

        // **** (1) init the mid storage memory space
        nlp.storageSpaceDraftLevel.StorageSpace.clean();

        resultEditor.runningStatus.Status.setStatus(2);

        // preload concepts if it still not be loaded yet before needed
        if(( env.Parameters.NLPAssistant.OUTPUTCONCEPT )&&(!env.Parameters.NLPAssistant.QuickNLPEnabled)){
            
            dictionaries.ConceptDictionaries.ConceptArray.clear();
            dictionaries.ConceptDictionaries.loadPreAnnotatedConcepts();
        }

        //System.out.println("@@@@@@ " +  Dictionaries.ConceptDictionaries.size());
        if (env.Parameters.NLPAssistant.OUTPUTCONCEPT) {
            if ((!env.Parameters.NLPAssistant.QuickNLPEnabled)
                    && (dictionaries.ConceptDictionaries.size() < 1)) {

                status.showErrorMsg("There is no valid record of your pre-annotated dictionary. Please check setting of your pre-annotation dictionaries.");
                return;
            }
        }
        

        if(env.Parameters.NLPAssistant.STOPSign)
            return;

        if (env.Parameters.NLPAssistant.OUTPUTCONCEPT) {
            if (env.Parameters.NLPAssistant.QuickNLPEnabled) {
                // load route map for quick NLP
                nlp.algorithm.QNLP.RouteMapDepot.clear();
                nlp.algorithm.QNLP.RouteMapDepot.load();

                if (nlp.algorithm.QNLP.RouteMapDepot.size() < 1) {
                    status.showErrorMsg("We can not find the route map for your current pre-annotated dictionaries to implement the fast NLP. Please check your settings.");
                    return;
                }

                // load dictionary
                nlp.algorithm.QNLP.DictionaryDepot.clear();
                nlp.algorithm.QNLP.DictionaryDepot.load();

                if (nlp.algorithm.QNLP.DictionaryDepot.size() < 1) {
                    status.showErrorMsg("We can not find the indexed pre-annotated dictionaries to implement the fast NLP. Please check your settings.");
                    return;
                }
            }
        }
       
        if(env.Parameters.NLPAssistant.STOPSign)
            return;
        
        resultEditor.runningStatus.Status.setStatus(3);
        // **** (2.1) check how many files in the waiting queue
        // if queue is empty, DO NOTHING
        int size = env.Parameters.corpus.LIST_ClinicalNotes.size();
        System.out.println("~~~~ NLP Cpu ~~~~:: we have " + size  + " files to process ..." );

        if ( size > 0 ){
                //System.out.println("There are " + size + " file(s) in current queue.");
                logs.ShowLogs.printImportantInfoLog( "There are " + size + " file(s) in current queue.");
        } else {
                logs.ShowLogs.printErrorLog("Do not find valid text file in current folder!!!");
                return;
        }


        File negative_term_directory = new File("phrases_chapman.txt");
        if(env.Parameters.NLPAssistant.OUTPUTNEGATION){
            if((negative_term_directory==null)||(!negative_term_directory.exists()))
            {
                status.showErrorMsg("Negation directory \"phrases_chapman.txt\" is missing!!! Please check your eHOST files.");
                return;

            }
        }


        NLPcpu.jProgressBar1.setMaximum(size*2);
        resultEditor.runningStatus.Status.setTotalFiles(size);

        

        // **** (2.2) get the filename list
        for(int i=0; i < size; i++)
        {

            if(env.Parameters.NLPAssistant.STOPSign)
                return;

            resultEditor.runningStatus.Status.setProcessedFiles(i);
            resultEditor.runningStatus.Status.addProcessedFilename(
            env.Parameters.corpus.LIST_ClinicalNotes.get(i).file );

            NLPcpu.jProgressBar1.setValue(i*2+1);
            // **** (1) init the mid storage memory space
            nlp.storageSpaceDraftLevel.StorageSpace.clean();

            // read file information from the array
            env.clinicalNoteList.CorpusStructure file = env.Parameters.corpus.LIST_ClinicalNotes.get(i);
            // if the filename empty, DO NOTHING
            if(( file == null )||(file.file == null))
                    continue;

            // screen log information
            logs.ShowLogs.printInfoLog("-------=======NEXT FILE=========---------");
            logs.ShowLogs.printNotice( "we are handling file: ["
                    + file.absoluteFilename + "]" );
            logs.ShowLogs.printNotice( "We are handling file - "
                            + String.valueOf(i+1)
                            + "/" + String.valueOf(size)
                            + "... ");

            // **** (3) got the text content of current file
            ArrayList paragraphTexts = GetContentFromFile( file.file.getAbsolutePath() );

            if(env.Parameters.NLPAssistant.STOPSign)
                return;

            resultEditor.runningStatus.Status.listCurrentCorpusFileName(file.file.getAbsolutePath());
            // **** (4)and run the analysis
            // **** (5) run subsequence processing.
            // i.e., output to txt file or xml
            RunAnalysis(
                    i+1, // which file it is, start form 1
                    file.file.getName(),  //fileanme-string
                    // object[], first is the text and second is the absolutely filename
                    paragraphTexts, // all paragraph text of the text-arralist,
                    //_flag_implement_concepts_algorithm,
                    //_flag_implement_dates_founder,
                    //_flag_implement_negation_algorithm,
                    //_isThreeColumnFile,
                    _xmloutput_path
                    );


        } // **** (3) loop end

        
        
        resultEditor.runningStatus.Status.setStatus(4);
        NLPcpu.jProgressBar1.setValue(size*2);
        resultEditor.runningStatus.Status.setProcessedFiles(size);
        resultEditor.runningStatus.Status.enablecombolist_counter();
        // clear concept dictionaries
        dictionaries.ConceptDictionaries.clear();

        // **** (7) ending
        nlp.algorithm.QNLP.RouteMapDepot.clear();
        nlp.algorithm.QNLP.DictionaryDepot.clear();
        
        resultEditor.runningStatus.Status.setStatus(5);

        // DONE: all algorithm reached their destination
        logs.ShowLogs.printNotice("=================================================");
        logs.ShowLogs.printNotice("=                         DONE                   =");
        logs.ShowLogs.printNotice("=================================================");

        

        resultEditor.runningStatus.Status.afterCompleteAllProcessing();
        
        

        return;
    }

    

        
    // get the text content from file
    private static ArrayList GetContentFromFile(
            // file: filename within absolute path
            String _inputFile)
    {
        if((_inputFile==null)||(_inputFile.trim().length()<1)) {
            System.out.println("#### ERROR #### :: 1106201412:: eHOST is trying to open a NULL as file to NLP assistant!!!");
            return null;
        }

        ArrayList texts = new ArrayList();

        // read all lines from file and put into the arraylist
        try{
            BufferedReader inputReader
                    = new BufferedReader(new FileReader(_inputFile));

            String linein;

            while ((linein = inputReader.readLine()) != null){
                Object[] s ={linein, _inputFile};
                texts.add(s);
            }

            inputReader.close();

            logs.ShowLogs.printImportantInfoLog("There are " + texts.size()
                    + " paragraph(s) in this file." );

        }catch(Exception e){
            logs.ShowLogs.printErrorLog("#### ERROR #### :: 201" + e);
            logs.ShowLogs.printErrorLog(
                    "#### ERROR #### :: 201 - Loading.java<-get_content_from_file()");
            return null;
        }

        return texts;
    }

        // ************************************************************** //
        // begin implement those algorithms and term founders to current clinical file
        // %%%% design flow chart - FC.3
        private static void RunAnalysis(
                // which file it is, start form 1
                int _indexOfFile,
                // file: filename within absolute path
                String _filename,
                // text content from the file
                ArrayList _paragraphTexts,
                // flags: indicates what algorithm and nlp method are wanted
                //boolean _flag_implement_concepts_algorithm,
                //boolean _flag_implement_dates_founder,
                //boolean _flag_implement_negation_algorithm,
                // if each paragraph in this file contains patient-id and note-id,
                // we call this file is a 3 columns file
                // and we will use the note id as the output xml file name
                //boolean _isThreeColumnFile,
                // xml output path name, just the full path
                String _xmloutput_path)
        {
                // overall index of all paragraph
                //int _paragraphOverallIndex;

                // ----------------------------------------------------------- /
                // if paragraphs in this file contains patient-id and note-id
                // we will consider each line in this file as an independent part
               /*if(_isThreeColumnFile){

                        logs.ShowLogs.printImportantInfoLog("User indicated that each line in notes(s) has patient id and note id.");
                        //nlp.NegexAlgorithm algorithm = new nlp.NegexAlgorithm(
                        //        _paragraphOverallIndex,
                        //        _file,
                        //        _flag_implement_concepts_algorithm,
                        //        _flag_implement_dates_founder,
                        //        _flag_implement_negation_algorithm,
                        //        _isThreeColumnFile,
                        //        _xmloutput_path );
                        
                        int size = _paragraphTexts.size();

                        
                        // to each line, we treat it as a independent file
                        for( int i=0; i<size; i++ ){
                                
                        }


                // ----------------------------------------------------------- /
                }else{
                 */
                    
                       // MUSTMUST
                       // this method need to be modificate to fit the this new design
                       // and move into step 5
                       //nlp.NegexAlgorithm algorithm = new nlp.NegexAlgorithm(
                       //         _paragraphOverallIndex,
                       //         _file,
                       //         _flag_implement_concepts_algorithm,
                       //         _flag_implement_dates_founder,
                       //         _flag_implement_negation_algorithm,
                       //         _isThreeColumnFile,
                       //         _xmloutput_path );

                        // get amount of paragraph in this file
                        if ((_paragraphTexts==null)||(_paragraphTexts.size()==0)){
                            System.out.println("~~~~ WARNING ~~~~ :: 1106201406 :: fail to found contents from current file. ");
                            return;
                        }

                        if ((_filename==null)||(_filename.trim().length()<1)){
                            System.out.println("~~~~ WARNING ~~~~ :: 1106201407 :: fail to get filename for NLP processing!!!. ");
                            return;
                        }

                        int size = _paragraphTexts.size();

                        //length of processed paragraph
                        int _offsetInFile = 0;

                        // ---- (1) init the mid storage memory space
                        //StorageSpaceDraftLevel.StorageSpace.clean();

                        // as no patient-id and note-id in this file,
                        // so we treat all lines here as a whole unit
                        // result generating only one output
                        for( int i=0; i<size; i++ )
                        {
                                

                                // ---- (2) get current paragraph
                                Object[] para = (Object[])_paragraphTexts.get(i);
                                String thisParagraph = para[0].toString();
                                String filename = para[1].toString();
                                int length_of_current_paragraph = thisParagraph.length();

                                // ---- (3) add record in mid storage memory for this paragraph
                                nlp.storageSpaceDraftLevel.StorageSpace.AddNewParagraph(
                                        thisParagraph, i+1, filename, _indexOfFile );

                                // show paragraph info in log
                                logs.ShowLogs.printLog("- content [" + thisParagraph + "]");
                                logs.ShowLogs.printInfoLog( "Algorithms are reaching paragraph of -> " + String.valueOf(i+1 ) +"/"+size );
                                // if this paragraph is a empty line, ignore it
                                if ( thisParagraph.trim().length() < 1 ){
                                        // record length of processed paragraph
                                        _offsetInFile += length_of_current_paragraph;
                                        // offset for the stop symbol
                                        _offsetInFile++;

                                        continue;
                                }

                                

                                // ---- (4) run all kind of NLP algorithm and filters
                                // ---- (4) and collect results
                                
                                int paragraphOverallIndex = nlp.storageSpaceDraftLevel.StorageSpace.GetParagraphOverallIndex();

                                if(env.Parameters.NLPAssistant.STOPSign)
                                return;

                                
                                // (4.1) concept 
                                // (4.2) negation phrase
                                // (4.3) complex concept-negation relationship


                                NegexAlgorithm Negex = new NegexAlgorithm();
                                Negex.NegexFinder(
                                        paragraphOverallIndex,
                                        thisParagraph,
                                        _offsetInFile,
                                        (i+1), // index of sentence in current file, start from 1
                                        env.Parameters.NLPAssistant.OUTPUTCONCEPT
                                         );
                                //System.out.println("\n\n\n0---------------------------------");
                                //    debugTools.Annotations.debug_echoResult();
                                
                                if(env.Parameters.NLPAssistant.STOPSign)
                                return;
                                if( env.Parameters.NLPAssistant.QuickNLPEnabled )
                                {
                                    // run algorithm
                                    nlp.algorithm.QNLP.QNLP Qnlp = new nlp.algorithm.QNLP.QNLP();
                                    Qnlp.termFinder(paragraphOverallIndex,
                                        thisParagraph,
                                        _offsetInFile,
                                        (i+1) // index of sentence in current file, start from 1);
                                        );
                                }

                                if(env.Parameters.NLPAssistant.STOPSign)
                                return;
                                if( env.Parameters.NLPAssistant.OUTPUTSSN ){
                                    filterSSN.SSNfilter SSN = new filterSSN.SSNfilter();
                                    SSN.SSNFinder(paragraphOverallIndex,
                                        thisParagraph,
                                        _offsetInFile,
                                        (i+1) // index of sentence in current file, start from 1);
                                        );
                                }
                                if(env.Parameters.NLPAssistant.STOPSign)
                                    return;


                                if( env.Parameters.NLPAssistant.OUTPUTDATE ){
                                    filterOfDateAndTime( thisParagraph, paragraphOverallIndex, _offsetInFile);
                                }
                                if(env.Parameters.NLPAssistant.STOPSign)
                                    return;

                                
                                if( env.Parameters.NLPAssistant.OUTPUTCUSTOMREGEX ){
                                    filterOfCustomRegex( thisParagraph, paragraphOverallIndex, _offsetInFile);
                                }                                
                                if(env.Parameters.NLPAssistant.STOPSign)
                                    return;


                                /** content algorithm, for attributes of temporality and experiencer*/
                                if( env.Parameters.NLPAssistant.OUTPUTTEMPORALITY || env.Parameters.NLPAssistant.OUTPUTEXPERIENCER){
                                    algorithmConText.ConTextAlgorithm ct
                                            = new algorithmConText.ConTextAlgorithm();
                                    ct.Context(thisParagraph, paragraphOverallIndex, _offsetInFile);
                                }
                                //AlgorithmConText.ConTextAlgorithm.ConText_Finder_Marker(
                                //        thisParagraph, // text of current paragraph
                                //        length_of_processed_paragraph, // for calculate position
                                //        (i+1) ); // which paragraph is this

                                // ---- (5) building relationship by the result in step 4
                                

                                // ---- (6) run output
                                //StorageSpaceDraftLevel.StorageSpace.printeverything();

                                // ---- (7) post-processing
                                // record length of processed paragraph
                                _offsetInFile += length_of_current_paragraph;
                                // offset for the stop symbol at the end of  each paragraph
                                _offsetInFile++;

                                //dialogsShowlogs.ShowLogs.printInfoLog(""-length_of_processed_paragraph);



                                

                            
                        }

                        

                        // ---- (8) flash the memory of color formated text and loading them on screen
                                //if(_flashScreen_){
                                    
                                //            ResultEditor.LoadingNotes.ShowNotes.ShowNote(_filename);
                                //}
                                    
                                    //System.out.println("\n\n\n1---------------------------------");
                                    //debugTools.Annotations.debug_echoResult();

                        if(env.Parameters.NLPAssistant.STOPSign)
                            return;
                        // **** <> filter all results of concept after analysising all paragraph of current file
                        // remove those have same range with lower weight than other results
                        nlp.postProcess.ConceptResultFilter cFilter
                                = new nlp.postProcess.ConceptResultFilter(_filename);
                        cFilter.conceptResultsFilter();

                        //S//ystem.out.println("\n\n\n1.1---------------------------------");
                        //            debugTools.Annotations.debug_echoResult();

                        if(env.Parameters.NLPAssistant.STOPSign)
                            return;
                        // assignment mention id for xml output
                        nlp.postProcess.AssignMentionID AM = new nlp.postProcess.AssignMentionID();
                        AM.assignMentionID();

                        
                        ExtractToDocumentViewer extractdoc = new ExtractToDocumentViewer();
                        extractdoc.ShowOnDocumentViewer();

                        //System.out.println("\n\n\n1.2---------------------------------");
                        //            debugTools.Annotations.debug_echoResult();


                        if(env.Parameters.NLPAssistant.STOPSign)
                            return;
                        // **** building relationships
                        // build concept-negation relationship
                        if(env.Parameters.NLPAssistant.OUTPUTNEGATION){
                            nlp.postProcess.relationships.Negation negationRelationship =
                                new nlp.postProcess.relationships.Negation();
                            negationRelationship.BuildRelationship();
                        }

                        //            System.out.println("\n\n\n1.3---------------------------------");
                         //           debugTools.Annotations.debug_echoResult();

                        if(env.Parameters.NLPAssistant.STOPSign)
                            return;
                        // indicate experiencer
                        if(env.Parameters.NLPAssistant.OUTPUTEXPERIENCER){
                            nlp.postProcess.relationships.Experiencer experiencer =
                                new nlp.postProcess.relationships.Experiencer();
                            experiencer.BuildRelationship();
                        }

                        //            System.out.println("\n\n\n1.4---------------------------------");
                        //            debugTools.Annotations.debug_echoResult();

                        if(env.Parameters.NLPAssistant.STOPSign)
                            return;
                        // indicate temporality of concepts in sentences
                        if(env.Parameters.NLPAssistant.OUTPUTTEMPORALITY){
                            nlp.postProcess.relationships.Temporality temporalityOrganizer =
                                new nlp.postProcess.relationships.Temporality();
                            temporalityOrganizer.BuildRelationship();
                        }

                        //System.out.println("\n\n\n2---------------------------------");
                        //            debugTools.Annotations.debug_echoResult();

                        // output our results to memory in result Editor
                        

                        // **** (6) export to XML
                        output.Output op = new output.Output(_xmloutput_path, size, _filename );
                        op.toXML();

                        // DONE: all algorithm reached their destination
                        logs.ShowLogs.printNotice("=================================================");
                        logs.ShowLogs.printNotice("=            Completed This File                =");
                        logs.ShowLogs.printNotice("=================================================\n\n");

                        // show the latest used metion id on gui dialog
                        int id = algorithmNegex.XMLMaker.getLatestMentionID();
                        env.Parameters.updateLatestUsedMentionID(id);
                        //UserInterface.GUI.show_latest_mention_id();

                        // save this number to ini file
                        config.system.SysConf.saveSystemConfigure();
                
                

        }

        public static void enableOrDisableScreen(boolean _flag){
                _flashScreen_ = _flag;
        }
       

        // return all found .txt filename to a given folder
        private String[] getTxtFile(String _path){
                String[] txtFiles = null;

                File file = new File(_path);

                if ( file.isDirectory() == true){
                        File[] t = file.listFiles();
                        char[] filename_char;
                        txtFiles = new String[t.length];
                        for (int i = 0; i < t.length; i++) {

                                txtFiles[i] = null;

                                if( t[i].isDirectory() == true )
                                        continue;

                                String filename = t[i].getName().toString();

                                filename_char = filename.toCharArray();
                                if ( filename_char[0] == '.' )
                                        continue;
                                
                                int begIndex = filename.lastIndexOf(".");
                                
                                if( begIndex < 1 )
                                        continue;
                                String suffix = filename.substring(begIndex + 1, filename.length());
                                if ( suffix.length() < 1 )
                                        continue;

                                if( suffix.equals("txt"))
                                        txtFiles[i] = filename;


                                //System.out.println(filename);
                                //System.out.println("begIndex: " + begIndex);
                                
                        }
                }else{
                        return null;
                }

            return txtFiles;
    }

    private static void  filterOfDateAndTime( String _paragraphText, int _paragraphOverallIndex, int _lengthOfProcessedParagraph){
        try{
            //ArrayList<FilterDate.ResultFormatOfDate> terms = getDates.catchValidDateAndTime( " " + _sentence_array[sent_index] + " " );
            ArrayList<nlp.filterDate.ResultFormatOfDate> dates = nlp.filterDate.getDates.catchValidDateAndTime(_paragraphText);

            if ( dates != null) {                
                int size = dates.size();
                for( int a = 0; a < size; a++ ){
                    if ( dates.get(a).dateTermText == null )
                        continue;

                    String dateTerm = dates.get(a).dateTermText;
                    int localStart = dates.get(a).start + 1;
                    int localEnd   = dates.get(a).end + 1;
                    int realSpanStart = _lengthOfProcessedParagraph + localStart;
                    int realSpanEnd   = _lengthOfProcessedParagraph + localEnd;

                    // record found valid terms and time terms into storage memory
                    // on draft level
                    nlp.storageSpaceDraftLevel.StorageSpace.addDateTerms(
                            _paragraphOverallIndex, dateTerm,
                            realSpanStart, realSpanEnd);
                    
                }
            }
        }catch(Exception e){
                logs.ShowLogs.printErrorLog(" 201 - dates finder ]" + e);
        }

    }

    private static void filterOfCustomRegex( String _paragraphText, int _paragraphOverallIndex, int _lengthOfProcessedParagraph){
        try{

            nlp.filterCustomDefined.CustomRegexFilter CREFilter
                    = new nlp.filterCustomDefined.CustomRegexFilter();

            // "terms" is the results we found by the custom defined regular 
            // expression lib, the "customre.lib".
            ArrayList<nlp.filterCustomDefined.CustomRegexResultFormat> terms
                    = CREFilter.catchTerms(_paragraphText, _lengthOfProcessedParagraph);

            if ( terms != null) {
                int size = terms.size();
                for( int a = 0; a < size; a++ ){
                    if ( terms.get(a).termText == null )
                        continue;

                    String term       = terms.get(a).termText;
                    String classname  = terms.get(a).classname;
                    int realSpanStart = terms.get(a).start;
                    int realSpanEnd   = terms.get(a).end;

                    if (     term == null) continue;
                    if (classname == null) continue;
                    

                    // record found valid terms and time terms into storage memory
                    // on draft level
                    nlp.storageSpaceDraftLevel.StorageSpace.addCustomRegex(
                            _paragraphOverallIndex, term, classname,
                            realSpanStart, realSpanEnd);

                }
            }
        }catch(Exception e){
                logs.ShowLogs.printErrorLog(" loading.java - filterOfCustomRegex() - 291 - filter of custom regular expression ]" + e);
        }

    }

}

/*
 * This is a storage space used to keep all materials we found from the clinical
 * notes, include original text content and results, such as found concept terms,
 * negation phrases, etc,.
 */

package nlp.storageSpaceDraftLevel;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author Jianwei Leng
 * @since 3-12 2010
 */
public class StorageSpace {
        private static ArrayList<DraftLevelFormat> DraftLevelStorageSpace;
        private static int Paragraph_Overall_Index_Count = 0;



        static{
                DraftLevelStorageSpace = new ArrayList<DraftLevelFormat>();
        }

        public static void clean(){
                DraftLevelStorageSpace.clear();
                Paragraph_Overall_Index_Count = 0;
        }

        // **** return how many records in current Crude Storage Space
        public static int Length(){
                int length = 0;
                try{
                        length = DraftLevelStorageSpace.size();

                }catch(Exception e){
                        return 0;
                }
                return length;

        }


    /** get content text of a specified paragraph
     * @param i     this is not the paragraph index and also not the paragraph
     *              index of whole memory
     *              it's just the index of the arraylist
     **/
    public static String getParagraphText(int i){
        String paragraphText = null;

        // check validity of number 'i'
        if( i > Length() - 1 )
                return null;

        // get specified record
        DraftLevelFormat cef
            = (DraftLevelFormat)DraftLevelStorageSpace.get(i);

        // get paragraph text
        paragraphText = cef.text_of_current_paragraph;
        return paragraphText;

    }


    /**To a designated index, it means a specific paragraph record with its
     * attributes, such as all found negations, concepts, ssns, dates, and
     * others found clinical terms.<br>
     * This method will find designated experiencer information of this
     * paragraph, and return it in an arraylist.<p>
     * <ul>
     * <li>param    i
     *     overall index of the paragraph(/line)
     * </ul>
     *
     * @return <code>ArrayList</code> <<code>ResultFormat_Negation</code> >,
     * arraylist of all negations in current paragraph(/line).
     */
    public static ArrayList<Table_Experiencer> getExperiencer(int i) {
        try{
            ArrayList<Table_Experiencer> experiencers;

            // check validity of number 'i'
            if( i > (Length() - 1) ){
                log.LoggingToFile.log(Level.INFO, "814 - storageSpace.java - ");
                logs.ShowLogs.printErrorLog("814 - storageSpace.java - ");
                return null;
            }

            // get specified record
            DraftLevelFormat cef
                = (DraftLevelFormat)DraftLevelStorageSpace.get(i);

            // get paragraph text
            experiencers = cef.Experiencer;

            return experiencers;

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE, "814.7 - storageSpace.java - "+ e.toString());
            logs.ShowLogs.printErrorLog("814.7 - storageSpace.java - "+ e.toString());
        }
        return null;
    }
    

    /**To a designated index, it means a specific paragraph record with its
     * attributes, such as all found negations, concepts, ssns, dates, and
     * others found clinical terms.<br>
     * This method will find designated information of this paragraph, and
     * return it in arraylist.
     *
     * <p>
     * <ul>
     * <li>param    i
     *     overall index of the paragraph(/line)
     * </ul>
     *
     * @return <code>ArrayList</code> <<code>ResultFormat_Negation</code> >,
     * arraylist of all negations in current paragraph(/line).
     */
    public static ArrayList<Table_Negation> getNegations(int i){
        try{
            ArrayList<Table_Negation> negations;

            // check validity of number 'i'
            if( i > (Length() - 1) ){
                log.LoggingToFile.log(Level.SEVERE, "811.1 - storageSpace.java - ");
                logs.ShowLogs.printErrorLog("811.1 - storageSpace.java - ");
                return null;
            }

            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

            // get paragraph text
            negations = cef.Negations;

            return negations;

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"812.1 - storageSpace.java - "+ e.toString());
            logs.ShowLogs.printErrorLog("812.1 - storageSpace.java - "+ e.toString());
        }
        return null;
    }

    public static ArrayList getConceptDetails(int i){
            try{
                    ArrayList cd;

                    // check validity of number 'i'
                    if( i > (Length() - 1) ){
                        log.LoggingToFile.log(Level.SEVERE,     "811.1 - storageSpace.java - ");
                        logs.ShowLogs.printErrorLog("811.1 - storageSpace.java - ");
                            return null;
                    }

                    // get specified record
                    nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                            = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

                    // get paragraph text
                    cd = cef.ConceptDetails;

                    return cd;

            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE, "811 - storageSpace.java - "+ e.toString());
                    logs.ShowLogs.printErrorLog("811 - storageSpace.java - "+ e.toString());
            }
            return null;
    }

    public static ArrayList<Table_SSN> getSSNDetails(int i){
            try{
                    ArrayList<Table_SSN> SSNs;

                    // check validity of number 'i'
                    if( i > (Length() - 1) ){
                        log.LoggingToFile.log(Level.SEVERE, "833.1 - storageSpace.java - ");
                            logs.ShowLogs.printErrorLog("833.1 - storageSpace.java - ");
                            return null;
                    }

                    // get specified record
                    nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                            = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

                    // get paragraph text
                    SSNs = cef.SSNDetails;

                    return SSNs;
            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE,"838 - storageSpace.java - "+ e.toString());
                    logs.ShowLogs.printErrorLog("838 - storageSpace.java - "+ e.toString());
            }
            return null;
    }

    public static ArrayList<Table_CustomRegex> getTermDetails_FoundByCustomRegex(int i) {
        try{
            ArrayList terms;

            // check validity of number 'i'
            if( i > (Length() - 1) ){
                log.LoggingToFile.log(Level.SEVERE,"833.8 - storageSpace.java - ");
                    logs.ShowLogs.printErrorLog("833.8 - storageSpace.java - ");
                    return null;
            }

            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

            // get paragraph text
            terms = cef.CustomRegex;

            return terms;
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"837 - storageSpace.java - "+ e.toString());
            logs.ShowLogs.printErrorLog("837 - storageSpace.java - "+ e.toString());
        }
        return null;
    }

    public static ArrayList<Table_DateTime> getDates(int i){
            try{
                    ArrayList dates;

                    // check validity of number 'i'
                    if( i > (Length() - 1) ){
                        log.LoggingToFile.log(Level.SEVERE,"833.8 - storageSpace.java - ");
                            logs.ShowLogs.printErrorLog("833.8 - storageSpace.java - ");
                            return null;
                    }

                    // get specified record
                    nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                            = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

                    // get paragraph text
                    dates = cef.Dates;

                    return dates;
            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE, "833 - storageSpace.java - "+ e.toString());
                    logs.ShowLogs.printErrorLog("833 - storageSpace.java - "+ e.toString());
            }
            return null;
    }


    // record modification of date and time(s) to a paragraph
    public static void UpdateDateDetails_toAParagraph( int _index, ArrayList _modifiedSSNDetails){
            int size = DraftLevelStorageSpace.size();
            if( _index > (size - 1) ){
                log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 819");
                    logs.ShowLogs.printErrorLog("StorageSpace.java 819");
                    return;
            }
            try{
                    DraftLevelFormat dlf = (DraftLevelFormat) DraftLevelStorageSpace.get(_index );
                    dlf.Dates = _modifiedSSNDetails;
                    DraftLevelStorageSpace.set(_index, dlf);
            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 820a9");
                    logs.ShowLogs.printErrorLog("StorageSpace.java 820a9");
            }
    }


    public static void UpdateCustomRegexResults_toDesignatedParagraph(int _index, ArrayList<Table_CustomRegex> _modifiedCustomRegexArrayList) {
        int size = DraftLevelStorageSpace.size();
        if( _index > (size - 1) )
        {
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 830");
                logs.ShowLogs.printErrorLog("StorageSpace.java 830");
                return;
        }
        try{
                DraftLevelFormat dlf = (DraftLevelFormat) DraftLevelStorageSpace.get(_index );
                dlf.CustomRegex = _modifiedCustomRegexArrayList;
                DraftLevelStorageSpace.set(_index, dlf);
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 830a10");
                logs.ShowLogs.printErrorLog("StorageSpace.java 830a10");
        }
    }


    // record modification of concepts to a paragraph
    public static void UpdateConcepsDetails_toAParagraph( int _index, ArrayList _modifiedConceptDetails){
            int size = DraftLevelStorageSpace.size();
            if( _index > (size - 1) ){
                log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 812");
                    logs.ShowLogs.printErrorLog("StorageSpace.java 812");
                    return;
            }
            try{
                    DraftLevelFormat dlf = (DraftLevelFormat) DraftLevelStorageSpace.get(_index );
                    dlf.ConceptDetails = _modifiedConceptDetails;
                    DraftLevelStorageSpace.set(_index, dlf);
            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 813");
                    logs.ShowLogs.printErrorLog("StorageSpace.java 813");
            }
    }

    // record modification of SSN(s) to a paragraph
    public static void UpdateSSNDetails_toAParagraph( int _index, ArrayList _modifiedSSNDetails){
        int size = DraftLevelStorageSpace.size();
        if( _index > (size - 1) ){
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 812");
            logs.ShowLogs.printErrorLog("StorageSpace.java 812");
            return;
        }
        try{
            DraftLevelFormat dlf = (DraftLevelFormat) DraftLevelStorageSpace.get(_index );
            dlf.SSNDetails = _modifiedSSNDetails;
            DraftLevelStorageSpace.set(_index, dlf);
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 820a");
            logs.ShowLogs.printErrorLog("StorageSpace.java 820a");
        }
    }

    public static nlp.storageSpaceDraftLevel.DraftLevelFormat getParagraphInFormat(int _index){
        int size = DraftLevelStorageSpace.size();
        if(_index > (size - 1) ){
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 814");
            logs.ShowLogs.printErrorLog("StorageSpace.java 814");
            return null;
        }
        try{
            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(_index);

            return cef;
        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"StorageSpace.java 815");
            logs.ShowLogs.printErrorLog("StorageSpace.java 815");
        }

        return null;

    }

    public static void printeverything(){
        int size = DraftLevelStorageSpace.size();
        for(int i=0;i<size;i++){
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);
            System.out.println("\n\n----------------------------------------");
            System.out.println(i+ "====== index    = " + i);
            System.out.println(i+ "====== filename = " + cef.filename);
            System.out.println(i+ "====== content  = " + cef.text_of_current_paragraph);
            System.out.println(i+ "====== realindex= " + cef.index);
            System.out.println(i+ "====== allindex = " + cef.paragraphOverallIndex);
            System.out.println(i+ "====== oldlength= " + cef.length_of_processed_paragraph);

            ArrayList cds = cef.ConceptDetails;
            int cds_size = cds.size();
            for(int c=0; c<cds_size; c++){
                    nlp.storageSpaceDraftLevel.Table_Concept cd = (nlp.storageSpaceDraftLevel.Table_Concept)cds.get(i);

            }
        }
    }

    // **** get content text of a specified paragraph
    public static String getFilename(int i){
            String filename = null;

            // check validity of number 'i'
            if( i > Length() - 1 )
                    return null;

            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

            // get paragraph text
            filename = cef.filename;

            return filename;
    }

    public static int getFileIndex(int i){
            int fileIndex = -1;

            // check validity of number 'i'
            if( i > Length() - 1 )
                    return -1;

            // get specified record
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef
                    = (nlp.storageSpaceDraftLevel.DraftLevelFormat)DraftLevelStorageSpace.get(i);

            // get paragraph text
            fileIndex = cef.fileindex;

            return fileIndex;
    }


    public static int GetParagraphOverallIndex(){
            return Paragraph_Overall_Index_Count;
    }
    // **** record paragraph
    // **** and alloc space for subsequence operations
    public static void AddNewParagraph(
            // content text of current paragraph
            String _paragraphText,
            // show which paragraph is this paragraph in the file
            int _index,
            // show which file does this paragraph belong to
            String _filename,
            // position of current file position, start from 1
            int _fileIndex
            // length of all paragraphs before this one
            // (included fullstop symbols)
            //int _length_of_processed_paragraphs
            )
    {
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef = new nlp.storageSpaceDraftLevel.DraftLevelFormat();

            cef.text_of_current_paragraph = _paragraphText;
            cef.filename = _filename;
            cef.fileindex = _fileIndex;

            Paragraph_Overall_Index_Count++;
            cef.paragraphOverallIndex = Paragraph_Overall_Index_Count;

            //cef.length_of_processed_paragraph = _length_of_processed_paragraphs;
            cef.index = _index;

            int size = _paragraphText.length();

            // alloc memory for arraies
            cef.flag_of_concept = new int[size];
            cef.flag_of_negation = new int[size];

            // initial value = -99
            for(int i=0; i<size; i++ ){
                    cef.flag_of_concept[i] = -99;
                    cef.flag_of_negation[i] = -99;
            }

            cef.ConceptDetails = new ArrayList();
            cef.SSNDetails = new ArrayList();



            DraftLevelStorageSpace.add(cef);
    }

    /** record found negation terms into the memory */
    public static void addNegation(
        int _overallIndex, // overall index to show paragraph position in the arraylist
        int _negationType,
        String _negationText, // negation text
        int _spanStart_InFile,
        int _spanEnd_InFile,
        int _window_begin,
        int _window_end){

        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat) DraftLevelStorageSpace.get( _overallIndex - 1 );
            if ( cef == null ) {
                log.LoggingToFile.log(Level.SEVERE,
                        "409 - can not get paragraph by overall index from draft level storage space" );
                logs.ShowLogs.printWarningLog(
                    "409 - can not get paragraph by overall index from draft level storage space" );
                return;
            }
                        

            // assemble data structure for storage
            nlp.storageSpaceDraftLevel.Table_Negation negation = new nlp.storageSpaceDraftLevel.Table_Negation();
            negation.negationText = _negationText;
            negation.spanStart_InFile = _spanStart_InFile;
            negation.spanEnd_InFile = _spanEnd_InFile;
            negation.type = _negationType;
            negation.effectiveRange_start = _window_begin;
            negation.effectiveRange_end = _window_end;


            cef.Negations.add(negation);

        }catch(Exception e){

        }
    }

    /** record found negation terms into the memory */
    public static void addExperiencer(
        int _overallIndex, // overall index to show paragraph position in the arraylist
        String _experiencerText, // negation text
        int _spanStart_InFile,
        int _spanEnd_InFile,
        int _effectiveRange_StartPoint,
        int _effectiveRange_EndPoint){

        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat)
                    DraftLevelStorageSpace.get( _overallIndex - 1 );

            if ( cef == null ) {
                log.LoggingToFile.log(Level.WARNING,
                        "StorageSpace.java - addExperiencer()" +
                    "- can not get paragraph by overall index from draft level storage space" );
                logs.ShowLogs.printWarningLog( "StorageSpace.java - addExperiencer()" +
                    "- can not get paragraph by overall index from draft level storage space" );
                return;
            }

            // assemble experiencer in format
            nlp.storageSpaceDraftLevel.Table_Experiencer e
                    = new nlp.storageSpaceDraftLevel.Table_Experiencer();
            e.expericnerText = _experiencerText;
            e.spanstart = _spanStart_InFile;
            e.spanend = _spanEnd_InFile;
            e.effectiveRangeStartPoint = _effectiveRange_StartPoint;
            e.effectiveRangeEndPoint = _effectiveRange_EndPoint;

            // add into arraylist for storage
            cef.Experiencer.add(e);

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,
                    "StorageSpace.java - addExperiencer()" +
                    "- lost found data in storage progress!!!" );
            logs.ShowLogs.printWarningLog( "StorageSpace.java - addExperiencer()" +
                    "- lost found data in storage progress!!!" );
        }
    }

    /** record temporality status to a specific sentence into the memory */
    public static void addTemporality(
        int _overallIndex, // overall index to show paragraph position in the arraylist
        String _temporalityStatus,
        int _effectiveRange_StartPoint,
        int _effectiveRange_EndPoint){

        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat)
                    DraftLevelStorageSpace.get( _overallIndex - 1 );

            if ( cef == null ) {
                log.LoggingToFile.log(Level.WARNING,
                        "StorageSpace.java - addTemporality()" +
                    "- can not get paragraph by overall index from draft level storage space" );
                logs.ShowLogs.printWarningLog( "StorageSpace.java - addTemporality()" +
                    "- can not get paragraph by overall index from draft level storage space" );
                return;
            }

            // assemble experiencer in format
            Table_Temporality t
                    = new Table_Temporality();
            t.temporalityStatus = _temporalityStatus;
            t.effectRangeStartPoint = _effectiveRange_StartPoint;
            t.effectRangeEndPoint = _effectiveRange_EndPoint;

            // add into arraylist for storage
            cef.Temporality.add(t);

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,
                    "StorageSpace.java - addTemporality()" +
                    "- lost found data in storage progress!!!" );
            logs.ShowLogs.printWarningLog( "StorageSpace.java - addTemporality()" +
                    "- lost found data in storage progress!!!" );
        }
    }

    /** record newly found SSN into the memory
     */
    public static void addSSNs(
        int _overallIndex, // overall index to show paragraph position in the arraylist
        String _SSN, // concept text
        int _span_start_in_the_paragraph,
        int _span_end_in_the_paragraph,
        int _span_start_in_file,
        int _span_end_in_file ){

        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat) DraftLevelStorageSpace.get( _overallIndex - 1 );
            if ( cef == null ) {
                log.LoggingToFile.log(Level.WARNING,
                        "401 - can not get paragraph by overall index from draft level storage space" );
                logs.ShowLogs.printWarningLog(
                    "401 - can not get paragraph by overall index from draft level storage space" );
                return;
            }

            resultEditor.runningStatus.Status.found_a_ssn();

            nlp.storageSpaceDraftLevel.Table_SSN SSND = new nlp.storageSpaceDraftLevel.Table_SSN();
            SSND.SSN = _SSN;
            SSND.span_start_in_the_paragraph = _span_start_in_the_paragraph;
            SSND.span_end_in_the_paragraph = _span_end_in_the_paragraph;
            SSND.span_start_in_file = _span_start_in_file;
            SSND.span_end_in_file = _span_end_in_file;

            cef.SSNDetails.add(SSND);

        }catch(Exception e){

        }
    }

    public static void addDateTerms(
        int _overallIndex, // overall index to show paragraph position in the arraylist
        String _dateTermText, // date and tiems
        int _span_start_in_file,
        int _span_end_in_file ){

        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat) DraftLevelStorageSpace.get( _overallIndex - 1 );
            if ( cef == null ) {
                log.LoggingToFile.log(Level.WARNING,
                        "404 - can not get paragraph by overall index from draft level storage space," +
                    "while try to add newly found date and time terms into storage memory" );
                logs.ShowLogs.printWarningLog(
                    "404 - can not get paragraph by overall index from draft level storage space," +
                    "while try to add newly found date and time terms into storage memory" );
                return;
            }

            resultEditor.runningStatus.Status.datefound();

            nlp.storageSpaceDraftLevel.Table_DateTime ddt = new nlp.storageSpaceDraftLevel.Table_DateTime();
            ddt.termText = _dateTermText;
            ddt.start = _span_start_in_file;
            ddt.end = _span_end_in_file;

            cef.Dates.add(ddt);

        }catch(Exception e){

        }
    }

    public static void addConcepts(
            int _overallIndex, // overall index to show paragraph position in the arraylist
            String _conceptText, // concept text
            int _span_start_in_the_paragraph,
            int _span_end_in_the_paragraph,
            int _span_start_in_file,
            int _span_end_in_file,
            int _weight,
            int _found_by_which_dictionary,
            int _found_by_which_entry

            ){
        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            try{

                //dialogsShowlogs.ShowLogs.printErrorLog( "overallindex = " + _overallIndex );
                //dialogsShowlogs.ShowLogs.printErrorLog( "size of memory = " + DraftLevelStorageSpace.size() );

                // locating

                cef = (DraftLevelFormat) DraftLevelStorageSpace.get( _overallIndex - 1 );

                if ( cef == null ) {
                    log.LoggingToFile.log(Level.WARNING,
                            "302 - can not get paragraph by overall index from draft level storage space" );
                        logs.ShowLogs.printWarningLog(
                                "302 - can not get paragraph by overall index from draft level storage space" );
                        return;
                }

                resultEditor.runningStatus.Status.conceptfound();

                // 2)add concept details as into the paragraph
                nlp.storageSpaceDraftLevel.Table_Concept concept = new nlp.storageSpaceDraftLevel.Table_Concept();
                concept.concept_text = _conceptText;
                concept.span_start_in_the_paragraph = _span_start_in_the_paragraph;
                concept.span_end_in_the_paragraph = _span_end_in_the_paragraph;
                concept.span_start_in_file = _span_start_in_file;
                concept.span_end_in_file = _span_end_in_file;
                concept.weight = _weight;
                concept.found_by_which_dictionary = _found_by_which_dictionary;
                concept.found_by_which_entry = _found_by_which_entry;
                concept.uniqueindex = getNewUniqueID();

                dictionaries.ConceptDictionaryFormat df
                        = dictionaries.ConceptDictionaries.ConceptArray.get(_found_by_which_dictionary);
                String comment = df.Comment.get(_found_by_which_entry);
                concept.comment = comment;

                cef.ConceptDetails.add(concept);

                // show concept details on screen (on log windows)
                String infostr = "[" + _conceptText + "]; paragraph location:["
                        + _span_start_in_the_paragraph + ", "
                        + _span_end_in_the_paragraph + "]; overall location: ["
                        + _span_start_in_file + ", "
                        + _span_end_in_file +
                        " - weight = " + _weight + "; comment = "+ comment + "]\n";
                log.LoggingToFile.log(Level.SEVERE, infostr );
                logs.ShowLogs.printResults(" =CONCEPT= : ", infostr );

            }catch(Exception e){
                log.LoggingToFile.log(Level.SEVERE,
                        "301 - can not get paragraph by overall index from draft level storage space" );
                // throw error infor if can not get data from draft level storage space
                logs.ShowLogs.printWarningLog(
                        "301 - can not get paragraph by overall index from draft level storage space" );
                logs.ShowLogs.printWarningLog( "      paragraph overall index = " + _overallIndex);
                logs.ShowLogs.printWarningLog( "      _conceptText = " + _conceptText );
                logs.ShowLogs.printWarningLog( "      _span_start_in_the_paragraph = " + _span_start_in_the_paragraph);
                logs.ShowLogs.printWarningLog( "      _span_end_in_the_paragraph = " + _span_end_in_the_paragraph);
                logs.ShowLogs.printWarningLog( "      _span_start_in_file = " + _span_start_in_file);
                logs.ShowLogs.printWarningLog( "      _span_end_in_file = " + _span_end_in_file);

            }

        }catch(Exception e){
                logs.ShowLogs.printWarningLog("addConcepts in class storagespace in package of storage space of draft level ");
                logs.ShowLogs.printWarningLog( e.toString());
        }
    }

    public static void addConcept_QNLP(
            int _overallIndex, // overall index to show paragraph position in the arraylist
            String _conceptText, // concept text
            int _span_start_in_the_paragraph,
            int _span_end_in_the_paragraph,
            int _span_start_in_file,
            int _span_end_in_file,
            String category
            ){
        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            try{

                //dialogsShowlogs.ShowLogs.printErrorLog( "overallindex = " + _overallIndex );
                //dialogsShowlogs.ShowLogs.printErrorLog( "size of memory = " + DraftLevelStorageSpace.size() );

                // locating

                cef = (DraftLevelFormat) DraftLevelStorageSpace.get( _overallIndex - 1 );

                if ( cef == null ) {
                    log.LoggingToFile.log(Level.WARNING,"302 - can not get paragraph by overall index from draft level storage space" );
                        logs.ShowLogs.printWarningLog(
                                "302 - can not get paragraph by overall index from draft level storage space" );
                        return;
                }

                resultEditor.runningStatus.Status.conceptfound();

                // 2)add concept details as into the paragraph
                nlp.storageSpaceDraftLevel.Table_Concept concept = new nlp.storageSpaceDraftLevel.Table_Concept();
                concept.concept_text = _conceptText;
                concept.span_start_in_the_paragraph = _span_start_in_the_paragraph;
                concept.span_end_in_the_paragraph = _span_end_in_the_paragraph;
                concept.span_start_in_file = _span_start_in_file;
                concept.span_end_in_file = _span_end_in_file;
                concept.uniqueindex = getNewUniqueID();
                //concept.weight = _weight;
                //concept.found_by_which_dictionary = _found_by_which_dictionary;
                //concept.found_by_which_entry = _found_by_which_entry;

                //Dictionaries.ConceptDictionaryFormat df
                //        = Dictionaries.ConceptDictionaries.ConceptArray.get(_found_by_which_dictionary);
                //String comment = category;
                concept.foundclassname = true;
                concept.classname = category;

                cef.ConceptDetails.add(concept);

                // show concept details on screen (on log windows)
                String infostr = "=CONCEPT= :" + "[" + _conceptText + "]; paragraph location:["
                        + _span_start_in_the_paragraph + ", "
                        + _span_end_in_the_paragraph + "]; overall location: ["
                        + _span_start_in_file + ", "
                        + _span_end_in_file + " @ "
                        + category + "]\n";

                log.LoggingToFile.log(Level.INFO, infostr );
                logs.ShowLogs.printResults( "=CONCEPT=", infostr );

            }catch(Exception e){
                String wraningstr = "301 - can not get paragraph by overall index from draft level storage space" 
                    + "      paragraph overall index = " + _overallIndex
                    +  "      _conceptText = " + _conceptText 
                    +  "      _span_start_in_the_paragraph = " + _span_start_in_the_paragraph
                    +  "      _span_end_in_the_paragraph = " + _span_end_in_the_paragraph
                    +  "      _span_start_in_file = " + _span_start_in_file
                    +  "      _span_end_in_file = " + _span_end_in_file;

                log.LoggingToFile.log(Level.SEVERE, wraningstr );
                // throw error infor if can not get data from draft level storage space
                logs.ShowLogs.printWarningLog( wraningstr );
                        

            }

        }catch(Exception e){
            log.LoggingToFile.log(Level.SEVERE,"addConcepts in class storagespace in package of storage space of draft level ");
                logs.ShowLogs.printWarningLog("addConcepts in class storagespace in package of storage space of draft level ");
                logs.ShowLogs.printWarningLog( e.toString());
        }
    }

    
    public static void addCustomRegex(int _paragraphOverallIndex, String term, String classname, int realSpanStart, int realSpanEnd) {
        try{
            // 1)locate where father paragraph is
            nlp.storageSpaceDraftLevel.DraftLevelFormat cef;
            cef = (nlp.storageSpaceDraftLevel.DraftLevelFormat) DraftLevelStorageSpace.get( _paragraphOverallIndex - 1 );
            if ( cef == null ) {
                log.LoggingToFile.log(Level.WARNING,
                        "405 - can not get paragraph by overall index from draft level storage space," +
                    "while try to add newly found terms by custom regular expression into storage memory" );
                logs.ShowLogs.printWarningLog(
                    "405 - can not get paragraph by overall index from draft level storage space," +
                    "while try to add newly found terms by custom regular expression into storage memory" );
                return;
            }

            resultEditor.runningStatus.Status.found_a_term_bycustomregex();

            nlp.storageSpaceDraftLevel.Table_CustomRegex cr = new nlp.storageSpaceDraftLevel.Table_CustomRegex();
            cr.terms = term;
            cr.classname = classname;
            cr.start = realSpanStart;
            cr.end = realSpanEnd;
            cr.uniqueindex = getNewUniqueID();

            cef.CustomRegex.add(cr);

        }catch(Exception e){

        }
    }




    private static int getNewUniqueID(){
        return resultEditor.annotations.AnnotationIndex.newAnnotationIndex();
    }
 


}

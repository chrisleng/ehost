/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package env;

import env.clinicalNoteList.CorpusStructure;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import relationship.complex.dataTypes.RelSchemaList;
import relationship.simple.dataTypes.AttributeList;

/**
 * This class is to build a static memory for all running parameters, which can
 * be used to record and track the running status of eHOST.
 *
 * @author Jianwei Chris Leng
 */
public class Parameters {

    /**
     * 标记：关于Navigation Panel和Oracle Function，说明我们工作在一个单个文件， 还是整个banrch.
     */
    public static boolean working_on_file = true;
    // env got from configure file
    public static String CONCEPT_LIB = null;
    public static String CONCEPT_LIB_SEPARTOR = null;
    public static String OUTPUT_CONTROL_CONCEPT_ENABLED = null;
    public static String COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES = null;

    public static class OracleStatus {

        public static boolean sysvisible = true;
        public static boolean visible = true;
    }
    /**
     * button of "diff" : This should be a public attribute of eHOST, not just
     * for one specific project only.
     */
    public static boolean EnableDiffButton = false;
    /**
     * record when is the last time that user clicked next/previous button to go
     * next or previous document
         *
     */
    public static long lastTimeToCallDocument = 0;
    /**
     * This is a flag that used to indicate whether user wants to quickly popup
     * the attribute editor window after 2 clicked on an annotation.
     */
    public static boolean enabled_displayAttributeEditor = false;
    // UMLS related --------------------------------------------------- //
    public static String umls_decryptedPassword = null;
    public static String umls_username = null;
    public static final byte[] umls_encryptorSalt = "12345678".getBytes();
    public static final String umls_encryptorPassword = "eHOST";
    public static final int umls_encryptorIterationCount = 100;

    public static class UMLSSetting {
        
        public static boolean UMLSfilterOn = false;

        public static boolean CPT = false;
        public static boolean HCPCS = false;
        public static boolean ICD10CM = false;
        public static boolean ICD10PCS = false;
        public static boolean LNC = false;
        public static boolean MEDLINEPLUS = false;
        public static boolean MeSH = false;
        public static boolean MedDRA = false;
        public static boolean RXNORM = false;
        public static boolean SCTSPA = false;
        public static boolean SCTUSX = false;
        public static boolean SNOMEDCT = false;
        public static boolean UMD = false;
    }

    public static class corpus {

        /**
         * env parameter: list of clinical files used in latest time.
         */
        public static Vector<CorpusStructure> LIST_ClinicalNotes = new Vector<CorpusStructure>();

        /**
         * Add a file into the list of text sources - selected clinical notes.
         * Check repetitive before adding.
         *
         * @param textfile The text source file which you want to add into
         * memory.
         */
        public static void addTextFile(File textfile) {

            // validity check
            if (textfile == null) {
                return;
            }

            if (!textfile.exists()) {
                return;
            }
            if (!textfile.canRead()) {
                return;
            }
            if (textfile.isDirectory()) {
                return;
            }

            // codes in following comment are using to check if current
            // textfile has an extension name ".txt"
            //if ( !isTxtFile( textfile ) )
            //    return;


            // repetitive check
            if (isTextFileRepetitive(textfile)) {
                return;
            }


            // first charact of the file name is . or ~, do nothing
            // "." or ".." means folder
            if (textfile.getName().toString().trim().charAt(0) == '.') {
                return;
            }
            // ~ means hidden system file
            if (textfile.getName().toString().trim().charAt(0) == '~') {
                return;
            }




            // assemble an instance clinicalnote
            CorpusStructure file = new CorpusStructure();
            file.file = textfile;
            file.absoluteFilename = textfile.getAbsolutePath();
            file.filename = textfile.getName();
            file.amountOfWords = commons.Filesys.countWordsInFile(textfile);

            LIST_ClinicalNotes.add(file);
        }

        /**
         * To a given text file, check whether it is a text file, whose suffix
         * name is ".txt" or ".TXT" or ".Txt".
         *
         * @param file The file you want to check its suffix name is text file
         * suffix name.
         */
        public static boolean isTxtFile(File file) {
            if (file == null) {
                System.out.println("1007291318::File is empty.");
                return false;
            }
            String filename = file.getName().toLowerCase().trim();
            if (filename.length() < 4) {
                return false;
            }

            int size = filename.length();
            char[] filename_chars = filename.toCharArray();
            if (filename_chars[size - 1] != 't') {
                return false;
            }
            if (filename_chars[size - 2] != 'x') {
                return false;
            }
            if (filename_chars[size - 3] != 't') {
                return false;
            }
            if (filename_chars[size - 4] != '.') {
                return false;
            }

            return true;
        }

        /**
         * Check repetitive of a specific textfile
         *
         * @param textfile The text source file which you want to add into
         * memory.
         */
        public static boolean isTextFileRepetitive(File textfile) {
            // return true, if it's null
            if (textfile == null) {
                return true;
            }

            for (env.clinicalNoteList.CorpusStructure clinicalnote : LIST_ClinicalNotes) {
                if (clinicalnote == null) {
                    continue;
                }
                if (clinicalnote.file.equals(textfile)) {
                    return true;
                }
            }

            return false;
        }

        /**
         * Add a file into the list of selected clinical notes.
         *
         * @param textfile type:File.
         * @param _amountOfWordsInFile type:int the amount of all words in this
         * file.
         */
        public static void AddFile(File _file, int _amountOfWordsInFile) {

            // validity check
            if (_file == null) {
                return;
            }

            // first charact of the file name is . or ~, do nothing
            // "." or ".." means folder
            if (_file.getName().toString().trim().charAt(0) == '.') {
                return;
            }
            // ~ means hidden system file
            if (_file.getName().toString().trim().charAt(0) == '~') {
                return;
            }

            // assemble an instance clinicalnote
            CorpusStructure file = new CorpusStructure();
            file.file = _file;
            file.absoluteFilename = _file.getAbsolutePath();
            file.filename = _file.getName();
            file.amountOfWords = _amountOfWordsInFile;

            LIST_ClinicalNotes.add(file);
        }

        public static int getSize() {
            return LIST_ClinicalNotes.size();
        }

        public static File[] getFiles() {
            int size = getSize();
            File[] files = new File[size];
            for (int i = 0; i < size; i++) {
                files[i] = getFile(i);
            }
            return files;
        }

        /**
         * return filename on position of index "_index".
         *
         * @param _index type: int<p> index of list of selected clinicel notes.
         * @return String the filename which is on the specific position of list
         * of selected clinical notes.
         */
        public static String getFileName(int _index) {
            if (_index > (getSize() - 1)) {
                return null;
            } else {
                return LIST_ClinicalNotes.get(_index).filename;
            }
        }

        public static File getFile(int _index) {
            try {
                if (_index > (getSize() - 1)) {
                    return null;
                } else {
                    return LIST_ClinicalNotes.get(_index).file;
                }
            } catch (Exception ex) {
                System.out.println("error 1103251322:: fail to get files "
                        + ex.getMessage());
                System.out.println("[eHOST] get(" + _index + ") failed");
                return null;
            }
        }

        /**
         * Get the absolute filename in a specific position of list of selected
         * clinical notes.
         *
         * @param _index type: int<p> index of list of selected clinicel
         * @return String absolute filename in a specific position of list of
         * selected clinical notes.
         */
        public static String getAbsoluteFileName(int _index) {
            if (_index > (getSize() - 1)) {
                return null;
            } else {

                return LIST_ClinicalNotes.get(_index).absoluteFilename;
            }
        }

        public static void RemoveInputFiles(int index) {
            LIST_ClinicalNotes.remove(index);
        }

        /**
         * Clear/empty the list of selected clinical notes
         */
        public static void RemoveAll() {
            LIST_ClinicalNotes.clear();
        }
    }
    public static ComparisonSettings verifierComparisonSettings = new ComparisonSettings();
    /**
     * Use to check amount of clinical files while first loading configure file.
     */
    public static boolean isFirstTimeLoadingConfigureFile = true;
    public static String currentMarkables_to_createAnnotation_by1Click = null;
    private static int LATEST_MENTION_ID;
    /**
     * @para filename - file name with absolute path
     * @para weight - if same all will be 0, otherwise big number means heavy
     * weight
     * @para description
     * @para separator
     * @para number_of_valid_entries
     */
    public static ArrayList PREANNOTATED_CONCEPT_DICTIONARIES;
    public static ArrayList<dictionaries.VerifierDictionaryFormat> VERIFIER_DICTIONARIES;
    public static AttributeList AttributeSchemas;
    //public static TreeSet<ComplexRel> ComplexRelationshipNames;
    public static RelSchemaList RelationshipSchemas;
    public static boolean Pre_Defined_Dictionary_DifferentWeight;
    // env detected on initial of eHOST
    public static boolean isUnixOS;
    /**
     * Flag used to control whether pop up the dialog of oracle similar words seeker
     */
    public static boolean oracleFunctionEnabled = false;
    /**
     * Flag used to control whether show graphics path between annotations for
     * complex relationships on document viewer.
     */
    public static final boolean enabled_GraphPath_Display = true;
    /**
     * Flag which used to turn on/off the display and function of difference matching
     */
    public static boolean enabled_Diff_Display = true;
    // GUI: which one got latest selected in the list of multiple annotations
    // in result editor
    public static int latestSelectedInListOfMultipleAnnotions = 0;

    // init
    static {
        PREANNOTATED_CONCEPT_DICTIONARIES = new ArrayList();
    }

    static {
        VERIFIER_DICTIONARIES = new ArrayList<dictionaries.VerifierDictionaryFormat>();
    }

    static {
        AttributeSchemas = new AttributeList();
    }
    //static{ ComplexRelationshipNames = new TreeSet<ComplexRel>();}

    static {
        RelationshipSchemas = new RelSchemaList();
    }

    /**
     * output/sync latest used mention id for output
     */
    public static int getLatestUsedMentionID() {
        return LATEST_MENTION_ID;
    }

    /**
     * record latest used mentiond id - in integer type;
     */
    public static void updateLatestUsedMentionID(int _newLatestUsedMentionID) {
        if (_newLatestUsedMentionID < 10000) {
            if (LATEST_MENTION_ID > 10000) {
                LATEST_MENTION_ID = LATEST_MENTION_ID + 1;
            } else {
                LATEST_MENTION_ID = 10000;
            }

        } else {
            if (LATEST_MENTION_ID > _newLatestUsedMentionID) {
                LATEST_MENTION_ID = LATEST_MENTION_ID + 1;
            } else {
                LATEST_MENTION_ID = _newLatestUsedMentionID;
            }
        }
    }

    /**
     * update latest used mention id without validity check.
     *
     * @param int _newLatestUsedMentionID<p> latest used mention id.
     */
    public static void forceChangeLatestUsedMentionID(int _newLatestUsedMentionID) {
        LATEST_MENTION_ID = _newLatestUsedMentionID;
    }

    public static class DifferenceMatching {

        public static boolean checkSameOverlappingSpan = false;
        /**
         * only show cross overlapping part of overlappings
         */
        public static boolean checkCrossSpan = true;
        public static boolean checkattributes = true;
        public static boolean checkatt_forComplex = true;
        public static boolean checkatt_forNormal = true;
        public static boolean checkatt_forClass = true;
        public static boolean checkatt_forSpan = true;
        public static boolean checkatt_forComment = true;
        public static boolean checkatt_forAnnotator = true;
    }

    /**
     * parameters using for creating new annotations.
     */
    public static class CreateAnnotation {

        /**
         * Flag that indicate whether we need to use exact span which user
         * slected on document viewer, or the span with corrected border by
         * detecting space or symbols, to build a new annotation. Default is false;
         */
        public static boolean buildAnnotation_usingExactSpan = false;
    }

    /**
     * Set of parameters for smart oracle module which can help you find similar
     * annotation in document
     */
    public static class Oracle {

        public static boolean search_matchWholeWord = true;
    }

    /**
     * Parameters corralated to annotation importing from XMLs
     */
    public static class AnnotationsImportingCorrelated {

        /**
         * list of filenames of imported xml
         */
        public static Vector<File> allImportedXMLs = new Vector<File>();

        /**
         * this is to return the value of vector of "allImportedXMLs".
         * Difference between return-value of this function and static value
         * "allImportedXMLs" is that the return-value of this fuction can make
         * sure there is no repetitive in its return.
         */
        public static Vector<File> getXMLList() {
            try {
                if (allImportedXMLs == null) {
                    return null;
                } else {
                    int size = allImportedXMLs.size();
                    for (int i = size - 1; i >= 0; i--) {
                        File f1 = allImportedXMLs.get(i);
                        if (f1 == null) {
                            continue;
                        }

                        System.out.println("file:[" + f1.getAbsolutePath() + "]");
                        for (int j = i; j >= 0; j--) {
                            File f2 = allImportedXMLs.get(j);
                            if (f2 == null) {
                                continue;
                            }

                            String str1 = f1.getAbsolutePath();
                            String str2 = f2.getAbsolutePath();

                            // if it finds any repetitive item in the vector, remove it
                            if ((str1.compareTo(str2) == 0) && (i != j)) {
                                allImportedXMLs.removeElementAt(i);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "error 1101021959:: fail to remove "
                        + "repetitive items in xml files.");
            }

            return allImportedXMLs;
        }
    }

    /**
     * Processes to dictionaries for NLP.
     */
    public static class nlp_dictionary_proc {

        /**
         * use dictionary of stop words to prevent unexpected annotations
         * occurred in the results of NLP by remove them from pre-annotated
         * concepts dictionaries.
         */
        public static boolean using_StopWords = false;
    }

    public static class Output_XML {

        public static boolean output_verify_suggestions_toXML = false;
    }

    /**
     * Here contains parameters about functions related to "workspace".
     */
    public static class WorkSpace {

        /**
         * Absolutely path of current workspace: Details: while record this in
         * configure file, blank space need to be replaced by symbol '='; and it
         * should be changed back while reload path of latest used workspace.
         */
        public static String WorkSpace_AbsolutelyPath = null;
        public static File CurrentProject = null;
    }

    public static class NLPAssistant {

        /**
         * <tt>OUTPUTCUSTOMREGEX</tt>: boolean, use to enable/disable filter of
         * custom regular expression
         */
        public static boolean OUTPUTCUSTOMREGEX = false;
        public static boolean Output_Dates = false;
        /**
         * <tt> OUTPUTNEGATION</tt>: boolean, enable/disable searching and
         * output of negation phrase
         */
        public static boolean OUTPUTNEGATION = false;
        /**
         * <tt>OUTPUTCONCEPT</tt>: boolean, enable/disable searching and output
         * of concept in negex algorithm
         */
        public static boolean OUTPUTCONCEPT = false;
        public static boolean OUTPUTSSN = false, OUTPUTDATE = false;
        /**
         * <tt>OUTPUTTEMPORALITY</tt>: boolean, enable/disable termporality in
         * context algorithm
            * <p>
         */
        public static boolean OUTPUTTEMPORALITY = false;
        /**
         * <tt>OUTPUTEXPERIENCER</tt>: boolean, enable/disable experiencer
         * method in context algorithm<p>
         */
        public static boolean OUTPUTEXPERIENCER = false;
        /**
         * Flag that indicate whether quick NLP algorithm is selected or not.
         */
        public static boolean QuickNLPEnabled = false;
        public static String outputpath = null;
        public static boolean STOPSign = false;
    }

    public static class Sysini {

        public static char[] functions = {'1', '0', '0', '0', '1', '1'};
    }

    /**
     * parameters to allow you control whether import annotation attributes,
     * relationships, or launching the handler dialog after annotatoin importing
     */
    public static class AnnotationImportSetting {

        public static boolean allowImportRelationship = true;
        public static boolean allowImportAttribute = true;
        public static boolean needSchemaHandler = false;
    }

    /**
     * Parameters related displaying annotation uniques in the tree on the
     * navigation panel
     */
    public static class ShowAnnotationUniquesInTree {

        /**
         * There are 2 sequences style to display annotation uniques in the tree
         * view on the navigation panel. One is by the sequence of characters,
         * the other one is by their location in the documents.
         *
         * if the value of this parameter is true, then eHOST will show
         * annotation uniques in sequence of their location, otherwise, in
         * sequence of characters.
         *
         */
        public static boolean isLocationSequence = false;
    }

    /**
     * Parameters used to indicates what kind of duplicates and ghost
     * annotations will be searched based on the checkboxes selected by user on
     * class "ghostandduplicatesSeeker.java" in package of
     * "ResultEditor.Annotations.Opt"
     */
    public static class DuplicatesAndGhostSeeker {

        /**
         * flag that show whether we check duplicates and ghofor all docs
         */
        public static boolean foralldocs = false;
        /**
         * duplicates must have same span
         */
        public static boolean duplicates_with_samespan = true;
        /**
         * duplicates must have same attributes
         */
        public static boolean duplicates_with_sameattributes = false;
        /**
         * duplicates must have same relationships
         */
        public static boolean duplicates_with_samerelationships = false;
        // == DISABLED AS USER DO NOT WANT TO COMPARE COMMENTS
        /**
         * duplicates must have same comments
         */
        //public static boolean duplicates_with_samecomments = false;
        // == DISABLED END ==
        // duplicates must have same classes
        public static boolean duplicates_with_sameclasses = false;
        //flag that for us to search ghost annotations
        /**
         * flag that for us to search ghost annotations without spans
         */
        public static boolean ghostannotation_spanless = false;
        /**
         * flag that for us to search ghost annotations without class/markable
         */
        public static boolean ghostannotation_classless = false;
        /**
         * flag that for us to search ghost annotations whose span is out of
         * current document
         */
        public static boolean ghostannotation_outofrange = false;
    }

    /**
     * parameter for handling quickly switch between recently used workspace
     */
    //public static class QuicklyWorkspaceSwitch{
    //    public static boolean isWorkingToAdd
    //}
    /**
     * Parameters that used during building relationships between selected two
     * annotations via editor, the original annotation can be selected on the
     * editor panel or the comparator panel.
     */
    public static class CreateRelationship {

        /**
         * A Flag that tell us whether user want to create relationship, and
         * annotation on which panel(editor or comparator panel) will be used to
         * create these relationship. To build a relationship, we need to select
         * two annotations: first one is the start and the second will be linked
         * to that one. It can be selected from the comparator panel.
         *
         * 0: stop building relationship; 1: build relationship from editor
         * panel; 2: build relationship from the comparator panel
         */
        public static int where = 0;
    }
}

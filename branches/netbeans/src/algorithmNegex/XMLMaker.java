package algorithmNegex;

/**
 * @(#)XMLMaker.java Collect and assemble data to XML format
 *
 * @author Jianwei Leng ( Chris ) @location: Williams Building, Level 1,
 * Epidemiology Department @history: Monday 10-20-2009 10:32 pm MST,
 * First_Created_Time @history: Thursday 11-05-2009 03:32 pm MST, Add function
 * for negative concept of classmention @history: Thursday 11-26-2009 17:00 pm
 * MST, Modify for generalization of complex slot @history: Tuesday 12-29-2009
 * 14:06 pm MST, Modify for data/time words
 */
/*
 * --------------------------------------------- 
 * jdom.jar should be set in your
 * classpath If not, please download the source codes and complie to get a
 * compatible one          
 * ---------------------------------------------
 */
import java.io.*;
import java.util.*;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import org.jdom.*;
import org.jdom.output.*;
import resultEditor.annotations.AnnotationAttributeDef;

// XML element: annotations and their contents 
class Annotations {

    String analyteAssignmentId;
    String mentionId, annotatorId, annotator;
    String spanStart, spanEnd, spanText;
    String creationDate;
    int groupindex = -1; // maping normal/concept term words and complexSlotMention
    // instance of Class ClassMention
    ArrayList<ClassMention> CMArray;
    boolean flag_negated = false;
    int type = 0;
    // 1: negation information
    // 2:normal term(span) information

    //CuiUtilities CuiUly = new CuiUtilities();
    //CUIAnnouncement CUIAName = new CUIAnnouncement();
    Annotations() {
        // CM - ClassMention
        CMArray = new ArrayList<ClassMention>();
    }

    // the return number is current offset for ID number increased
    int dataCollector(String _definedMentionClassText, int _currentMentionClassIdNumber, // parameters in this line are used to generate the IDs
            String annotatorId, String annotator,
            String spanStart, String spanEnd, String spanText, String creationDate,
            String stringSlotMentionValue, int _groupindex, int type, String _comment) {
        //currentMentionID = DefinedMentionClassText + (int)(count_of_annotations + mentionIRN);
        // STEP -=[1]=-
        // for variety of concepts:
        // build <annotation> branch and classMention branch
        this.mentionId = _definedMentionClassText + _currentMentionClassIdNumber;
        this.annotatorId = annotatorId;
        this.annotator = annotator;
        this.spanStart = spanStart;
        this.spanEnd = spanEnd;
        this.groupindex = _groupindex;
        if (type == 3) {
            this.spanText = spanText.replaceAll("_", " ");
        } else {
            this.spanText = spanText.toLowerCase().replaceAll("_", " ");
        }
        this.creationDate = creationDate;

        this.type = type;

        //System.out.println(">>>>>>>>>>>>>>>>>> dataCollector<-XMLMaker.java >>>>>>> type =" + type);
        // STEP -=[2]=-
        // build <classMention> Branch of XML
        // search the cui announce name
        String mentionClassID_CUI = _comment; //nlp.ConceptUtilities.getConceptAnnouncedName( spanText.toLowerCase().replaceAll("_", " ") );
        // if you can not found announced name for cui, use "concept" as "id" of <mentionClass>
        // if can not find it in the concept dictionary,
        if ((mentionClassID_CUI == null) && (type == 1)) {
            // do nothing, no change to the id counter, return 0
            // MUSTMUST you need to the laster record in arraylist of annotationsList
            log.LoggingToFile.log(Level.WARNING, "spantext: [" + spanText + "] could not reach its related announced name");
            logs.ShowLogs.printWarningLog("spantext: [" + spanText + "] could not reach its related announced name");

            // drop last records
            return -999; //means you need to drop this record
        }
        // build <classMention> Branch of XML
        ClassMention CMI = new ClassMention();
        //Assemble the content of xml branch: <classMention>
        CMI.classMentionId = mentionId;
        //CMI.groupIndex = this.groupindex;
        String parentsMentionID = this.mentionId;

        if ((type == 1) && (mentionClassID_CUI.length() < 1)) {
            CMI.mentionClassId = "concept";
        } else {
            CMI.mentionClassId = mentionClassID_CUI;
        }
        CMI.mentionClassId_ElementContent = spanText.toLowerCase();

        // UNTIL now, an regular xml branch of <classMention> are
        // ALMOST competed except "hasSlotMention id="

        String newMentionID;
        int id_increased = 0;


        // STEP -=[3]=-
        // to variety of concepts, switched by "type"
        // there have different amount of <hasSlotMention id ="" ></>
        // so here, we build:
        // (1)build matched "<hasMention Id=>....."
        // (2)record matched xml branch of <complexSlotMention> if have
        // (3)record matched xml branch of <stringSlotMention> if have
        //System.out.println(">>>>>>>>>>>>>>>1 " + spanText);
        switch (type) {

            // if not an negative word, (such as "not", "no", "no evidence", ...)
            case 1:
                // if CONCEPTS connected to Negative words
                // then matched <classmention> will have two "hasClassMention"
                if (stringSlotMentionValue.compareTo("negated") == 0) {
                    //System.out.println(">>>>>>>>>>>>>>>2 " + spanText);
                    //(1)two "hasClassMention"
                    // see (2.1) and (3.1)
                    //(2)record matched xml branch of <complexSlotMention> if have
                    //(2.1)                                        
                    CMI.hasComplexSlotMention = true;
                    id_increased++;
                    newMentionID = _definedMentionClassText
                            + (_currentMentionClassIdNumber + id_increased);
                    CMI.hasSlotMentionId_ArrayList.add(newMentionID);
                    CMI.addComplexSlotMention(newMentionID, _groupindex);
                    //(3)record matched xml branch of <stringSlotMention> if have
                    //(3.1)
                    id_increased++;
                    newMentionID = _definedMentionClassText
                            + (_currentMentionClassIdNumber + id_increased);
                    CMI.hasSlotMentionId_ArrayList.add(newMentionID);
                    CMI.hasSlot = true;
                    CMI.addStringSlotMention(newMentionID, "negation_slot", stringSlotMentionValue);

                    CMArray.add(CMI);
                } else if (stringSlotMentionValue.compareTo("possible") == 0) {
                    //negated found, possible, .....

                    //(1)two "hasClassMention"
                    // see (2.1) and (3.1)
                    //(2)record matched xml branch of <complexSlotMention> if have
                    //(2.1)

                    CMI.hasComplexSlotMention = true;
                    id_increased++;
                    newMentionID = _definedMentionClassText
                            + (_currentMentionClassIdNumber + id_increased);
                    CMI.hasSlotMentionId_ArrayList.add(newMentionID);
                    CMI.addComplexSlotMention(newMentionID, _groupindex);
                    //(3)record matched xml branch of <stringSlotMention> if have
                    //(3.1)
                    id_increased++;
                    newMentionID = _definedMentionClassText
                            + (_currentMentionClassIdNumber + id_increased);
                    CMI.hasSlotMentionId_ArrayList.add(newMentionID);
                    CMI.hasSlot = true;
                    CMI.addStringSlotMention(newMentionID, "negation_slot", stringSlotMentionValue);

                    CMArray.add(CMI);
                    //CMI.hasSlot = true;
                    //id_increased++;
                    //newMentionID = _definedMentionClassText
                    //                    + ( _currentMentionClassIdNumber + id_increased ) ;
                    //CMI.hasSlotMentionId_ArrayList.add( newMentionID );

                    //CMI.addStringSlotMention( newMentionID, "possible", "found");
                    //CMI.hasComplexSlotMention = false;
                    //CMArray.add( CMI );
                } else {
                    // <classMention> and <stringSlotMention> for other unclassificated concept
                    // such as just "found" term
                    CMI.hasSlot = false;
                    CMI.hasComplexSlotMention = false;
                    CMArray.add(CMI);
                }
                break;

            // 2:   if annotation is show an negative word, such as "not", "no", "no evidence"
            //      build a <classmention> branch
            //      as: <classmention> <mentionClass id="negation"> negation</> </>
            case 2:
                // ClassMention ID
                //ClassMention CMI2 = new ClassMention();
                CMI.classMentionId = mentionId;
                CMI.mentionClassId = "negation";
                CMI.mentionClassId_ElementContent = "negation";
                CMI.hasSlot = false;
                CMI.hasComplexSlotMention = false;
                CMI.groupIndex = _groupindex;
                CMArray.add(CMI);
                break;
            // if is "dates" words, it only have "annotations" branch and "classmention branch"
            case 3:
                // ClassMention ID
                //ClassMention CMI2 = new ClassMention();
                CMI.classMentionId = mentionId;
                CMI.mentionClassId = "Date";
                CMI.mentionClassId_ElementContent = "Date";
                CMI.hasSlot = false;
                CMI.hasComplexSlotMention = false;
                //CMI.groupIndex = _groupindex;
                CMArray.add(CMI);
                break;
        }
        return id_increased;
    }
    //static void setFlag(String Flag){ }
}

class ClassMention {

    String classMentionId;
    String mentionClassId;
    String mentionClassId_ElementContent;
    ArrayList<String> hasSlotMentionId_ArrayList;
    boolean hasSlot = false;
    boolean hasComplexSlotMention = false;
    ArrayList<StringSlotMention> SSMArrayList;
    ArrayList<ComplexSlotMention> CSMArrayList;
    int groupIndex = -1;

    ClassMention() {
        SSMArrayList = new ArrayList<StringSlotMention>();
        CSMArrayList = new ArrayList<ComplexSlotMention>();
        hasSlotMentionId_ArrayList = new ArrayList<String>();
    }

    // add a record about CM("ClassMention") set and matched SSM("String Slot Mention") data
    void addStringSlotMention(String _hasClassMention_id, String _memtionClassId, String _stringSlotMentionValue) {
        StringSlotMention SSM = new StringSlotMention();
        SSM.stringSlotMention_id = _hasClassMention_id;
        SSM.memtionClass_id = _memtionClassId;
        SSM.stringSlotMention_value = _stringSlotMentionValue;
        SSMArrayList.add(SSM);
    }

    void addComplexSlotMention(String _hasClassMention_id, int _groupindex){ //, ArrayList<AnnotationAttributeDef> attributes) {
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@"+_groupindex);
        ComplexSlotMention CSM = new ComplexSlotMention();
        //CSM.complexSlotMentionid = _complexSlotMentionid;
        //CSM.parentsMentionID = _parentsMentionID;
        CSM.complexSlotMentionid = _hasClassMention_id;
        CSM.mentionSlot = "link_negation";
        CSM.groupIndex = _groupindex;
        //CSM.attributes = attributes;

        CSMArrayList.add(CSM);
    }
}

//===================================================//
//  XML data collector and XML tree assembling       //
//===================================================//
public class XMLMaker {

    public static String OutputXMLFileName;
    public static String TextSource; // .txt filename of matched note record
    // temporary string, used to assemble "mention_id" and "annotator_id"
    private String currentMentionID;
    private String currentAnnotatorID;
    private static int count_of_annotations = 0;
    // private int count_of_classMention = 0;
    private static int mentionIRN; // Initial Random Number for mention id
    // private int maxMentionIRN; // Maximum / current mentionIRN
    // private static Hashtable annotationsTable; 
    private static ArrayList<Annotations> annotationsList;
    // constants
    private static final int mentionIdDigits = 5;
    private static final int annotatatorIdDigits = 3;
    private static final String annotationsStr = "annotations"; // XML elements
    private static final String fileExtendedAttributesName = ".knowtator.xml";
    private static String path;
    private static final String originalMentionID = "IDIS_ILI_VA_Instance_";
    private static final String originalAnnotatorID = "IDIS_ILI_VA_Instance_";
    private static short OFFSET = 0;
    private static int memory_groupIndex = 0;//, memory_groupIndex_neg_possible = 0;
    // record span position for latest found negtiion phrases
    // used in NegexAlgorithm to search concepts in its effective ranger/windows
    //public static int last_negspan_start = 0, last_negspan_end = 0;
    public static int LATEST_NEGATION_PHRASE_SPAN_START = 0, LATEST_NEGATION_PHRASE_SPAN_END = 0;
    // Flag: show the status of offset functions or not
    private static boolean flag_OffsetEnabled;
    // Flag: initial works done or not
    private static boolean flag_Initial;

    // return current status of offset enabled or not
    public boolean offsetEnabled() {
        if (flag_OffsetEnabled == true) {
            return true;
        } else {
            return false;
        }
    }

    public void setOutputPath(String _xmlouput_path) {
        if (commons.OS.isMacOS()) {
            XMLMaker.path = _xmlouput_path + "/";
        } else {
            XMLMaker.path = _xmlouput_path + "\\";
        }
        System.out.println(path);
        return;
    }

    // do offset to span start and end
    public String doOffset(String _spanPosition) {
        if (_spanPosition == null) {
            return "0";
        } else {
            try {
                int position = Integer.parseInt(_spanPosition);
                position = position + OFFSET;
                return String.valueOf(position);
            } catch (Exception e) {
                return _spanPosition;
            }
        }
    }

    // set Flag to indicate offset enabled or not
    public void offsetEnabled(boolean _flag_OffsetEnabled) {
        XMLMaker.flag_OffsetEnabled = _flag_OffsetEnabled;
    }

    // static codes
    static {
        // if its the first time to load this class
        // system will generate a mentioniIRN
        if (mentionIRN < 10000) {
            mentionIRN = randomNumber(mentionIdDigits);
        }
        // or you assign a mentionIRN
        //mentionIRN = 640643; // 03-24-2010 14:29
    }

    static {
        flag_OffsetEnabled = true;
    }

    static {
        annotationsList = new ArrayList<Annotations>();
    }
    // static { count_of_annotations = 0; }	

    // constractor
    public XMLMaker() {
        // initial 
        mentionIRN++;
        currentAnnotatorID = originalAnnotatorID + randomNumber(annotatatorIdDigits);
    }

    // to each original note, some values need to be initial or reset
    public void clean() {
        XMLMaker.annotationsList.clear();
        count_of_annotations = 0;
        XMLMaker.LATEST_NEGATION_PHRASE_SPAN_END = 0;
        XMLMaker.LATEST_NEGATION_PHRASE_SPAN_START = 0;
        //flag_OffsetEnabled = false;
        memory_groupIndex = 0;
        mentionIRN++;
        currentAnnotatorID = originalAnnotatorID + randomNumber(annotatatorIdDigits);
    }

    public static void set_mention_id_startpoint(int _id) {
        mentionIRN = _id;
    }
    // return the latest used mention id

    public static int getLatestMentionID() {
        return (count_of_annotations + mentionIRN + 1);
    }
    // collect the annotations data

    public void recordAnnotations(String _annotator, String _spanStart, String _spanEnd, String _spanText, String _comment, String _stringSlotMentionValue, int _memory_groupIndex) {
        currentMentionID = originalMentionID + (count_of_annotations + mentionIRN);
        //Annotations.Annotations(String _mentionId, String _annotatorId, String _annotatorValue, 
        //String _spanStart, String _spanEnd, String _creationDate)
        //System.out.println(">>>>>>>>>>>>>>>>>> recordAnnotations<-XMLMaker.java >>>>>>> _memory_groupIndex =" + _memory_groupIndex);
        Annotations records = new Annotations();

        int appended = 0;
        // do offset if we need
        if (flag_OffsetEnabled == true) {

            appended = records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    doOffset(_spanStart), doOffset(_spanEnd),
                    _spanText, getCurrentDate(), _stringSlotMentionValue, _memory_groupIndex, 1, _comment);
            if (appended > 0) {
                count_of_annotations = count_of_annotations + appended;
            }

        } else {
            appended = records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    _spanStart, _spanEnd, _spanText, getCurrentDate(), _stringSlotMentionValue, _memory_groupIndex, 1, _comment);	// 1:normal term(span) information
            if (appended > 0) {
                count_of_annotations = count_of_annotations + appended;
            }
        }
        //System.out.println("add date one time!!!!" +  currentMentionID + " - " + currentAnnotatorID + " - " + _annotator + 
        //	_spanStart + _spanEnd + _spanText + getCurrentDate());
        if (appended != -999) {
            annotationsList.add(records);
            count_of_annotations++;
        }

    }

    // collect the negative flag words, such as "no", "not", "no evidence" ...
    public void recordAnnotationsNegInfo(String _annotator, String _spanStart, String _spanEnd, String _spanText, int _memory_groupIndex) {
        currentMentionID = originalMentionID + (count_of_annotations + mentionIRN);
        //Annotations.Annotations(String _mentionId, String _annotatorId, String _annotatorValue, 
        //String _spanStart, String _spanEnd, String _creationDate)

        Annotations records = new Annotations();

        // do offset if we need
        if (flag_OffsetEnabled == true) {
            count_of_annotations = count_of_annotations
                    + records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    doOffset(_spanStart), doOffset(_spanEnd), _spanText, getCurrentDate(), "", _memory_groupIndex, 2, null); // 2: negation information
        } else {
            count_of_annotations = count_of_annotations
                    + records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    _spanStart, _spanEnd, _spanText, getCurrentDate(), "", _memory_groupIndex, 2, null); // 2: negation information
        }
        //System.out.println("add date one time!!!!" +  currentMentionID + " - " + currentAnnotatorID + " - " + _annotator + 
        //	_spanStart + _spanEnd + _spanText + getCurrentDate());
        annotationsList.add(records);
        count_of_annotations++;

    }

    // collect the negative flag words, such as "no", "not", "no evidence" ...
    public void recordDates(String _annotator, String _spanStart, String _spanEnd, String _spanText) {
        currentMentionID = originalMentionID + (count_of_annotations + mentionIRN);
        //Annotations.Annotations(String _mentionId, String _annotatorId, String _annotatorValue,
        //String _spanStart, String _spanEnd, String _creationDate)

        Annotations records = new Annotations();

        // do offset if we need
        if (flag_OffsetEnabled == true) {
            count_of_annotations = count_of_annotations
                    + records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    doOffset(_spanStart), doOffset(_spanEnd), _spanText, getCurrentDate(), "", 0, 3, null); // 3: dates
        } else {
            count_of_annotations = count_of_annotations
                    + records.dataCollector(originalMentionID, count_of_annotations + mentionIRN,
                    currentAnnotatorID, _annotator,
                    _spanStart, _spanEnd, _spanText, getCurrentDate(), "", 0, 3, null); // 3: dates
        }
        //System.out.println("add date one time!!!!" +  currentMentionID + " - " + currentAnnotatorID + " - " + _annotator +
        //	_spanStart + _spanEnd + _spanText + getCurrentDate());
        annotationsList.add(records);
        count_of_annotations++;

    }

    public void groupIndexPlusOne() {
        memory_groupIndex++;
    }

    //public void neg_possible_groupIndexPlusOne(){
    //        memory_groupIndex_neg_possible++;
    //}
    public int groupIndex() {
        return memory_groupIndex;
    }

    //public int get_neg_possible_groupIndex(){
    //        return memory_groupIndex_neg_possible;
    //}
    // generate a 'n'-digits random number for id
    static private int randomNumber(int n) {
        int intGet = 0;
        Random r = new Random();
        // max = the maximum integer of a n-digits number
        int max = (int) Math.floor(Math.pow(10.0, n)) - 1;

        // get an exact n-digits random number
        do {
            intGet = Math.abs(r.nextInt()) % max;
        } while ((String.valueOf(intGet).trim().length()) < n);

        return intGet;
    }

    // get current date and time
    private String getCurrentDate() {
        Calendar rightnow = Calendar.getInstance();
        String CurrentDate = rightnow.getTime().toString();
        //System.out.println(CurrentDate);
        return CurrentDate;
    }

    // set out put xml file name
    public void setOutputFileName(String _FileName) {
        // set the path by system
        setPath_By_OSType();

        if (_FileName == null) {
            OutputXMLFileName = path + "NoName" + fileExtendedAttributesName;
        } else {
            TextSource = _FileName + ".txt";
            OutputXMLFileName = path + TextSource + fileExtendedAttributesName;
        }
    }

    // assume the XML file and output
    public void outputXML() {



        mentionIRN = mentionIRN + count_of_annotations;
        // if not gave the output xml filename, use default "output.xml"
        if ((OutputXMLFileName == null)) {
            OutputXMLFileName = "output1.txt.knowtator.xml";
            log.LoggingToFile.log(Level.WARNING, "use default filename for XML output: output.txt.knowtator.xml");
            logs.ShowLogs.printWarningLog("use default filename for XML output: output.txt.knowtator.xml");
        }

        // XML Assembly and Storage
        try {
            // initial the XML file and set the root node of the XML
            //System.out.println("annotationsStr is " + annotationsStr);
            Element root = new Element(annotationsStr); // "annotations"
            //System.out.println("TextSource is " + TextSource);
            root.setAttribute("textSource", TextSource);
            Document Doc = new Document(root);

            // Traversing all data and assemble the XML sub branch
            // XML sub branch - "anotation"
            int length = annotationsList.size();

            // if could not get data for current statement, break out
            if (length < 1) {
                return;    //if no data need to be export to xml file, exit current method
            }

            // debug screen log
            //System.out.println("length of the arraylist = " + length);

            for (int i = 0; i < length; i++) {
                // elements
                Element annotation = new Element("annotation");

                Annotations annotationRecords = new Annotations();
                //System.out.println("annotations - " + annotationsList.get(i) );
                annotationRecords = (Annotations) annotationsList.get(i);

                Element mention = new Element("mention");
                //System.out.println("mention id - " + annotationRecords.mentionId );
                mention.setAttribute("id", annotationRecords.mentionId);
                //mention.setAttribute("id", "ISDS_ILI_shuying_Instance_0" );
                annotation.addContent(mention);

                Element annotator = new Element("annotator");
                annotator.setAttribute("id", annotationRecords.annotatorId);
                //System.out.println("annotator id - " + annotationRecords.annotatorId );
                //annotator.setAttribute("id", "ISDS_ILI_FeatureSets_clean_Instance_12");
                //System.out.println("annotator text - " + annotationRecords.annotator );
                annotator.setText(annotationRecords.annotator);
                annotation.addContent(annotator);

                Element span = new Element("span");
                span.setAttribute("start", annotationRecords.spanStart);
                span.setAttribute("end", annotationRecords.spanEnd);
                annotation.addContent(span);

                annotation.addContent(new Element("spannedText").setText(annotationRecords.spanText));
                annotation.addContent(new Element("creationDate").setText(annotationRecords.creationDate));

                root.addContent(annotation);

                //System.out.println( "==--== i/length = " + i +"/" + length);
            }

            // assemble sub XML branch of <classMention>
            for (int i = 0; i < length; i++) {
                Annotations annotationRecords = (Annotations) annotationsList.get(i); // data tree

                // if have matched "classMention" record
                if ((length > 0) & (annotationRecords.CMArray.size() > 0)) {
                    // numbers of "classMention"
                    int size = annotationRecords.CMArray.size();

                    // assemble the sub XML branch for all matched "classMention" records
                    for (int j = 0; j < size; j++) {
                        // "classMention" data structure and get stored data in memory
                        ClassMention CM = (ClassMention) annotationRecords.CMArray.get(j);

                        Element classMention = new Element("classMention");
                        classMention.setAttribute("id", CM.classMentionId);
                        Element mentionClass = new Element("mentionClass");
                        mentionClass.setAttribute("id", CM.mentionClassId);
                        mentionClass.addContent(CM.mentionClassId_ElementContent);
                        //add this node <ClassMention> to xml tree
                        classMention.addContent(mentionClass);

                        //if ( CM.hasSlot ){
                        int slotsAmount = CM.hasSlotMentionId_ArrayList.size();
                        if (slotsAmount > 0) {
                            for (int sa = 0; sa < slotsAmount; sa++) {
                                Element hasSlotMention = new Element("hasSlotMention");
                                hasSlotMention.setAttribute("id", (String) CM.hasSlotMentionId_ArrayList.get(sa));
                                classMention.addContent(hasSlotMention);
                            }
                        }
                        //}

                        root.addContent(classMention);


                        // assemble XML sub branch for "stringSlotMention"
                        int z_size = CM.SSMArrayList.size();
                        if (CM.hasSlot) //if ( z_size > 0 )
                        {
                            for (int z = 0; z < z_size; z++) {

                                //System.out.println( CM.SSMArrayList.get(z).memtionClass_id );
                                //System.out.println( CM.SSMArrayList.get(z).stringSlotMention_id );
                                //System.out.println( CM.SSMArrayList.get(z).stringSlotMention_value );

                                // get "stringSlotMention" data from the arraylist
                                StringSlotMention SSM = (StringSlotMention) CM.SSMArrayList.get(z);

                                // System.out.println( "=======================================================" );
                                // System.out.println( "id-" + CM.hasSlotMentionId + " id-" + SSM.memtionSlotId );
                                Element stringSlotMention = new Element("stringSlotMention");
                                stringSlotMention.setAttribute("id", SSM.stringSlotMention_id);

                                Element mentionSlotId = new Element("mentionSlot");
                                mentionSlotId.setAttribute("id", SSM.memtionClass_id);
                                stringSlotMention.addContent(mentionSlotId);

                                Element stringSlotMentionValue = new Element("stringSlotMentionValue");
                                stringSlotMentionValue.setAttribute("value", SSM.stringSlotMention_value);
                                stringSlotMention.addContent(stringSlotMentionValue);

                                //System.out.println( "==--== z/z-size" + z +"/" + z_size);

                                root.addContent(stringSlotMention);
                            }
                        }

                        if (CM.hasComplexSlotMention == true) {
                            int csm_size = CM.CSMArrayList.size();
                            if (csm_size < 1) {
                                continue;
                            }
                            for (int t = 0; t < csm_size; t++) {
                                ComplexSlotMention CSMA = (ComplexSlotMention) CM.CSMArrayList.get(t);

                                Element complexSlotMention = new Element("complexSlotMention");
                                complexSlotMention.setAttribute("id", CSMA.complexSlotMentionid);
                                //complexSlotMention.setAttribute("id", CSMA.parentsMentionID );

                                
                                String csmv = getComplexSlotMentionValue(CSMA.groupIndex);
                                if(( csmv == null )||(csmv.trim().length()<1))
                                    continue;
                                
                                ///System.out.println( "---------------------" + CSMA.groupIndex);
                                Element mentionSlotId = new Element("mentionSlot");
                                mentionSlotId.setAttribute("id", CSMA.mentionSlot);
                                complexSlotMention.addContent(mentionSlotId);
                                


                                Element complexSlotMentionValue = new Element("complexSlotMentionValue");
                                //System.out.println("CSMA.groupIndex = ["+ CSMA.groupIndex +"]");
                                //System.out.println("getComplexSlotMentionValue( CSMA.groupIndex ) = ["+ csmv +"]");
                                complexSlotMentionValue.setAttribute("value", csmv);
                                //System.out.println("CSMV complexSlotMentionValue value = " + csmv);
                                complexSlotMention.addContent(complexSlotMentionValue);


                                root.addContent(complexSlotMention);
                            }
                        }


                    }
                }

            }

            // XML storage processing: phycial writing
            Format format = Format.getCompactFormat();
            format.setEncoding("UTF-8"); // set XML encodeing
            format.setIndent("    ");
            XMLOutputter XMLOut = new XMLOutputter(format);
            // write to disk
            XMLOut.output(Doc, new FileOutputStream(OutputXMLFileName));

            // Screen Log
            logs.ShowLogs.printImportantInfoLog("Successfully genereted XML to - " + OutputXMLFileName);
            log.LoggingToFile.log(Level.INFO, "Successfully genereted XML to - " + OutputXMLFileName);
            randomNumber(2);
        } catch (Exception exc) {
            // exc.printStackTrace();
            logs.ShowLogs.printErrorLog("XMLMaker.java - " + exc.toString());
            log.LoggingToFile.log(Level.SEVERE, "XMLMaker.java - " + exc.toString());
        }
    }

    private String getComplexSlotMentionValue(int _groupIndex) {
        int length = annotationsList.size();
        // if could not get data for current statement, break out
        if (length < 1) {
            return "";
        }
        int size_annotationRecords;

        for (int i = 0; i < length; i++) {
            ///System.out.println("i="+i);
            Annotations annotationRecords = new Annotations();
            annotationRecords = (Annotations) annotationsList.get(i);

            size_annotationRecords = annotationRecords.CMArray.size();
            if (size_annotationRecords > 0) {
                for (int jj = 0; jj < size_annotationRecords; jj++) {

                    ClassMention CM = (ClassMention) annotationRecords.CMArray.get(jj);
                    //System.out.println("=========: " + CM.mentionClassId_ElementContent );
                    //System.out.println("=========: " + CM.groupIndex );
                    if (CM.groupIndex == _groupIndex) {
                        //System.out.println("^^^--^^^^^^^^^^^^^^^" +CM.classMentionId);
                        return CM.classMentionId;
                    }
                }
            }



        }

        // if above program can not find matched classmention id of the negative word
        return null;
    }

    // mac and windows operation system uses different path separator
    private void setPath_By_OSType() {
        if (env.Parameters.isUnixOS) {
            path = "./xmloutput/";      // for mac os system
        } else {
            path = ".\\xmloutput\\";
        }   // for windows system
    }
}

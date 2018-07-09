package thirdpartyOutput;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.Calendar;
import org.jdom.*;
import org.jdom.output.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author leng
 */
public class OutputXMLs {
    private static int INDEX = 10000;
    private Vector<Annotations> ORGANIZEDSPACE = new Vector<Annotations>();
    private String OriginalFilename = null;
    private String outputPath = null;
    
    
    // delete data before using to collect data for each clinical reports
    /*public static void clear(){
        ORGANIZEDSPACE.clear();
    }*/
    
    /** set filename(NOT absolute filename) of the original clinical report
     *@param _filename - TYPE: String or File
     *       (NOT absolute filename)
     */
    public void setSourceFilename(String _filename){
        OriginalFilename = _filename;
    }
    public void setSourceFilename(File _file){
        OriginalFilename = _file.getName();
    }
    
    

    /**add an annotation into the storage memory, it return a integer as index
     * if return value is smaller than 0, means failure
     *
     * @param _spanText the span text, TYPE: String
     * @param _spanStart, start postion of this span, TYPE: int
     */

    public int addAnnotation(String _spanText, String _classname, int _spanStart, int _spanEnd){
        Annotations annotation = new Annotations();
        annotation.annotationText = _spanText;
        annotation.classname = _classname;
        annotation.locationStart = _spanStart;
        annotation.locationEnd = _spanEnd;
        INDEX++;
        annotation.index = INDEX;
        

        ORGANIZEDSPACE.add(annotation);
        
        return (INDEX);
    }

    public int addSlots(int _indexOfFather, String _mentionSlotID, String _StringSlotMentionValue){
        int size = ORGANIZEDSPACE.size();
        for(int i = 0; i<size; i++){
            Annotations annotation = ORGANIZEDSPACE.get(i);

            // found annotation parents
            if (annotation.index == _indexOfFather){
                Slots slot = new Slots();
                INDEX++;
                slot.index = INDEX;
                slot.mentionSlotId = _mentionSlotID;
                slot.stringSlotMentionValue = _StringSlotMentionValue;
                annotation.slots.add(slot);
                annotation.hasSlotMention.add(String.valueOf(INDEX));
                ORGANIZEDSPACE.set(i, annotation);

                System.out.println("just add slot ["
                        + _mentionSlotID
                        +", "
                        + _StringSlotMentionValue
                        +", "
                        + INDEX
                        + "]  to annotation ["
                        + annotation.annotationText
                        + "]");
                return INDEX;
            }

        }

        return -1;
    }


    /** use to indicate the path for output the xml file 
     *  the real output xml absolute name should be:
     *  thispath + original filename(removed path) + ".xml"
     */
    public void setOutputPath(String _outputPath){
        outputPath = _outputPath;
    }

    /**write to xml file*/
    public void WriteToDisk(){
        outputXML();
    }

    private String outputFilename(){
        if( OriginalFilename == null)
            return null;

        return outputPath + "/" + OriginalFilename + ".xml";
    }

    // write to xml file
    private void outputXML(){

        // check: output path/filename
        String outputFilename = outputFilename();
        if (outputFilename == null){
            System.out.println("Error: outputXML.java - didn't indicate original filename!!!");
            return;
        }

        int size = size();

        // output annotation
        System.out.println("XML output path: " + outputFilename);


        // XML Assembly and Storage
        try{
                // initial the XML file and set the root node of the XML
                //System.out.println("annotationsStr is " + annotationsStr);
                Element root = new Element( "annotations" ) {};
                //System.out.println("TextSource is " + TextSource);
                root.setAttribute( "textSource", OriginalFilename );
                Document Doc = new Document(root);

                /** output xml file to disk */
                // XML storage processing: phycial writing
                Format format = Format.getCompactFormat();
                format.setEncoding("UTF-8"); // set XML encodeing
                format.setIndent("    ");
                XMLOutputter XMLOut = new XMLOutputter(format);

                for(int i=0; i<size; i++){
                    Annotations annotation = ORGANIZEDSPACE.get(i);
                    /** add contents */
                    root = BuildXMLNodesForAnnotations(root, annotation.index, 
                            annotation.annotationText, annotation.classname,
                            annotation.locationStart,
                            annotation.locationEnd, annotation.hasSlotMention );

                    //add slots

                    root = BuildXMLNodesForSlots(root, annotation.slots );
                    //int slotSize = annotation
                }

                // write to disk
                XMLOut.output(Doc, new FileOutputStream( outputFilename ));

        }catch(Exception e){
            System.out.println( e.toString() );
        }



    }

    private Element BuildXMLNodesForSlots(Element _root, Vector<Slots> _slotList){
        int size = _slotList.size();
        Element root = _root;

        for(int i=0; i< size;i++){

            Slots slot = _slotList.get(i);

            // "classMention" data structure and get stored data in memory
            Element stringSlotMention = new Element("stringSlotMention");
            stringSlotMention.setAttribute("id", "ISDS_ILI_VA_Instance_"+ slot.mentionSlotId );

            Element mentionSlot = new Element( "mentionSlot" );
            mentionSlot.setAttribute( "id", slot.mentionSlotId );
            stringSlotMention.addContent( mentionSlot );

            Element stringSlotMentionValue = new Element( "stringSlotMentionValue" );
            stringSlotMentionValue.setAttribute( "value", slot.stringSlotMentionValue );
            stringSlotMention.addContent( stringSlotMentionValue );



            root.addContent( stringSlotMention );



        }
        return root;
    }

    private Element BuildXMLNodesForAnnotations(Element _root, int _MentionID,
            String _spanText, String _classname, int _start, int _end,
            Vector<String> _Slots ){
            Element root = _root;

            // -----------------------------------------------------elements
            Element annotation = new Element("annotation");

            Element mention = new Element("mention");
            //System.out.println("mention id - " + annotationRecor ds.mentionId );
            mention.setAttribute("id", "ISDS_ILI_VA_Instance_3Party_"+ _MentionID );
            //mention.setAttribute("id", "ISDS_ILI_shuying_Instance_0" );
            annotation.addContent(mention);

            Element annotator = new Element("annotator");
            annotator.setAttribute("id", "ISDS_ILI_VA_Instance_607");
            //System.out.println("annotator id - " + annotationRecords.annotatorId );
            //annotator.setAttribute("id", "ISDS_ILI_FeatureSets_clean_Instance_12");
            //System.out.println("annotator text - " + annotationRecords.annotator );
            annotator.setText("3party");
            annotation.addContent(annotator);

            Element span = new Element("span");
            span.setAttribute("start", String.valueOf( _start ));
            span.setAttribute("end", String.valueOf( _end));
            annotation.addContent(span);

            annotation.addContent( new Element("spannedText").setText(_spanText));
            annotation.addContent( new Element("creationDate").setText( getCurrentDate() ));

            root.addContent(annotation);
            // -----------------------------------------------------elements end


            // -----------------------------------------------------classmention begin
            // "classMention" data structure and get stored data in memory
            Element classMention = new Element("classMention");
            classMention.setAttribute("id", "ISDS_ILI_VA_Instance_"+ _MentionID );
            Element mentionClass = new Element( "mentionClass" );
            mentionClass.setAttribute( "id", _classname );
            mentionClass.addContent( _spanText );
            //add this node <ClassMention> to xml tree
            int size = _Slots.size();
            for(int i=0;i<size;i++){
                Element hasSlotMention = new Element("hasSlotMention");
                hasSlotMention.setAttribute("id", "ISDS_ILI_VA_Instance_"+ _Slots.get(i) );
                classMention.addContent(hasSlotMention);
            }

            classMention.addContent( mentionClass );
            root.addContent( classMention );


            return root;
    }

    public int size(){
        return ORGANIZEDSPACE.size();
    }

    // get current date and time
    private String getCurrentDate(){
        Calendar rightnow = Calendar.getInstance();
        String CurrentDate = rightnow.getTime().toString();
        //System.out.println(CurrentDate);
        return CurrentDate;
    }
}

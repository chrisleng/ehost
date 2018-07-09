/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package resultEditor.io;

import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analysis the text block which extracted from a PINs file. Extract
 * informations into structured memory.
 *
 * @author Chris 2010-06-21
 */
public class BlockPraser {

    /** Example: batch1_Instance_0 */
    protected String mentionid;
    /** Example: "batch1_Instance_" of "batch1_Instance_0" */
    protected String mentionid_name;
    /** Example: "0" of "batch1_Instance_0" */
    protected int    mentionid_number;


    /** annotation class: got from knowtator_mention_class*/
    protected String knowtator_mention_class;
    /** index to anntation*/
    protected String knowtator_mention_annotation;
    /** index of father node*/
    protected String knowtator_annotated_mention;
    /** annotator index*/
    protected String knowtator_annotation_annotator;
    /** creation date of annotation*/
    protected String knowtator_annotation_creation_date;

    /** annotation span position in text*/
    protected String knowtator_annotation_span;

    /** annotation span start */
    protected int span_start = -1;
    /** annotation span end */
    protected int span_end = -1;

    /** annotation text */
    protected String knowtator_annotation_text;
    /** text source*/
    protected String knowtator_annotation_text_source;
    

    /** Example: "knowtator+class+mention" of "[batch2_Instance_0]
     * of  knowtator+class+mention" */
    protected String[] title;
    protected String titles;

    /**slot mention, can be considered as index which you can find a new
     * haslostmention which contains all details about the subslot. */
    protected Vector<String> knowtator_slot_mention = new Vector<String>();

    /** slot mention */
    protected String knowtator_mention_slot;
    /** slot mention value*/
    protected String knowtator_mention_slot_value;
    /**type of slot mention value, while true, type of slot mention is string;
     * Otherwiese, it will be one or more indexes linked to more information.
     * Each index string is surrounded with a pair of '[' and ']'.*/
    protected boolean knowtator_mention_slot_value_isString;
    /**slot mention value: str value*/
    protected Vector<String> knowtator_mention_slot_value_inString = new Vector<String>();
    /**slot mention value: index(es)*/
    protected Vector<String> knowtator_mention_slot_value_inIndex = new Vector<String>();

    protected String knowtator_annotation_annotator_firstname;
    protected String knowtator_annotation_annotator_lastname;
    protected String knowtator_annotator_id;
    protected String knowtator_annotator_team_name;

    /**knowtator_mentioned_in*/
    protected String knowtator_mentioned_in;

    /**constructor*/
    public BlockPraser(String block){

        // #1.get title and mentionid(name+number) and removed processed string
        block = getHeadline(block);
        //System.out.println("Block content after got and removed headline:\n" +
        //        block);
        if (block == null) return;
        if (block.trim().length()<1) return;

        // #2 get all entries
        Vector<String> entries = splittedByPairedBrackets(block);
        // validity check
        if(entries==null)   return;
        if(entries.size() < 1) return;

        // #3 split each entry into format "entryname"+"value"
        Vector entrydetails = getAttributes(entries);
        entries.clear(); // initiative memory release

        // #4 classified subsequent process
        // record value for different attibute
        valueCapture(entrydetails);
        entrydetails.clear(); // initiative memory release

        

    }


    /**
     * Got entries from text block. And these entries should be surrounded by
     * paired '(' and ')'.
     *
     * @param   block   Given text block came from PINs file, and headline
     * of it has got removed, and now it only has entries.
     *
     * @return  entries, such as "knowtator_mention_annotation [batch1_Instance_1]"
     * and "knowtator_mention_class Problem" got from following text<p>
     * (knowtator_mention_annotation [batch1_Instance_1])<p>
     * (knowtator_mention_class Problem)<p>
     * <p>
     *
     */
    private Vector splittedByPairedBrackets(String block){

        // validity check
        if(block == null)
            return null;
        if(block.trim().length() < 1)
            return null;

        block =  " " + block + " ";

        //System.out.println(block);

        // define value for subsequence processing
        int length = block.length();
        Vector entries = new Vector();

        // catch each entry
        int startpoint=0, endpoint=0;
        do{                        
            // get '(' and its paired ')'
            startpoint = block.indexOf( '(', startpoint);
            endpoint = block.indexOf(')', startpoint + 1);

            //Make sure there is nothing wrong with the starting parentheses
            //If there is skip it.
            if(!checkGood(block, startpoint, endpoint))
            {
                if(block.charAt(startpoint-1) != '\u0009')
                {
                    startpoint++;
                    log.LoggingToFile.log(Level.INFO, "Moved start");
                    continue;
                }
            }
            //Make sure there is nothing wrong with the ending paranetheses
            //If there is skip it.
            while(!checkGood(block, startpoint, endpoint))
            {
                if(block.charAt(endpoint+1) != '\u0009')
                {
                    endpoint = block.indexOf(')', endpoint + 1);
                    if(endpoint == -1)
                        break;
                    log.LoggingToFile.log(Level.INFO, "Moved end");
                }
            }
            //If we read in all blocks without fixing parentheses then print out bad block
            if (!checkGood(block, startpoint, endpoint))
            {
                log.LoggingToFile.log(Level.INFO, "bad block");
                return null;
            }
            
            
            //System.out.println("start = "+ startpoint+ ", " + "end = " + endpoint );

            // is this entry is valid
            if((startpoint>=0)&&(endpoint>startpoint)&&(endpoint<=length)){
                String entry = block.substring( startpoint + 1, endpoint );
                //System.out.println("This entry is :[" + entry + "]");
                entries.add(entry);
            }
            
            // break for a status: startpoint == -1
            if(startpoint<0)
                break;

            // new position to start catch in new loop
            startpoint++;
            
        }while(( startpoint>=0 )&&(endpoint>startpoint)
                &&(endpoint<=length));


        return entries;
    }
    private boolean checkGood(String block, int startpoint, int endpoint)
    {
            int startQuotation = block.indexOf("\"", startpoint);
            int endQuotation = block.lastIndexOf("\"", endpoint);
            //If there are no quotations then it's good
            if (startQuotation == -1 && endQuotation == -1)
            {
                return true;
            }
            //If there is more than one quotation then we should be good.
            else if (startQuotation != endQuotation)
            {
                return true;
            }
            return false;
         

    }
    /**Get headline of each text block, which got from a PINS file.
     * Headline here is the mentionid + title; And removed headline
     * from original string.
     *
     * @param   thisblock   text content of this block, the outmost bracket,
     * '(' and ')' have been removed.
     */
    private String getHeadline(String thisblock){
         Matcher matcher;
         boolean match_found;
         String regex =  " "
                 + "(\\[)(.*)(\\])"
                 + "( +)"
                 + "(of)"
                 + "( +)"
                 + "([a-z]+)"
                 + "((\\+([a-z]+))*)"
                 + " ";

         try{
             matcher = Pattern.compile( regex, Pattern.CASE_INSENSITIVE ).matcher( thisblock );
             match_found = matcher.find();

             if(match_found){
                 int start = matcher.start();
                 int end = matcher.end();

                 // get headline
                 String headline = thisblock.substring(start, end);                 

                 // get mentionid, title from the headline
                 headlineAnalysis(headline);
             }
             if(match_found){
                 // removd this headline from original string
                 thisblock = thisblock.replaceAll(regex, " ");
             }
         } catch (Exception ex){
             logs.ShowLogs.printErrorLog(ex.toString());
         }
         return thisblock;
    }


    /**extract mentionid, mentionid_name. mentionid_bumber and title from 
     * headline string.*/
    private void headlineAnalysis(String headline) {
        
        // get title
        getTitle(headline);

        // get mentionid
        int start = headline.indexOf('[');
        int end = headline.indexOf(']');
        this.mentionid = headline.substring(start+1, end);
        // get name and number of mentionid, and get title
        mentionIDSubdivide(mentionid);
        //System.out.println("mentionid = " + mentionid);

    }


    /**get title from headline of each text block, which was get from a
     * Pins file
     */
    private void getTitle(String headline){

        // consider words after " of " are all words of "title"
        int position = headline.toLowerCase().indexOf(" of ");        
        String titletext = headline.substring(position+3);
        this.titles = titletext;
        this.title = titletext.split("\\+");
        //System.out.println("all titles are - "+titles);
    }


    /** To a given mentiondid, get its mentionid number. Such as get "0"
     * from mentionid "batch1_Instance_0"*/
    private void mentionIDSubdivide(String mentionid){
        
        // #1 condition1: [006544894.txt] of  file+text+source
        if(this.titles.trim().toLowerCase().compareTo("file+text+source")== 0 ){
            this.mentionid_name = "";
            this.mentionid_number = -1;
            return;
        }

        // #2 condition2: [batch2_Instance_0] of  knowtator+class+mention
        int position = mentionid.lastIndexOf('_');

        // get mention id name
        this.mentionid_name = mentionid.substring(0, position+1).trim();
        //System.out.println("mentionid name = " + mentionid_name);

        // get mentiond id number
        String mentionid_number_str = mentionid.substring(position+1, mentionid.length()).trim();
        this.mentionid_number = Integer.valueOf(mentionid_number_str);
        //System.out.println("mentionid number = " + mentionid_number);

        
    }


    class Headline{
        String mentionid;
        String mentionid_name;
        int    mentionid_number;
        String title;
    }


    /**format of entrys which blong to a text block in PINs file.*/
    class EntryDetail{
        public String entryname;
        public String entryvalue;

        /**Constructor*/
        public EntryDetail(String entryname, String entryvalue){
            this.entryname = entryname;
            this.entryvalue =  entryvalue;
            //System.out.println("name = [" + this.entryname + "], value = ["
            //        + this.entryvalue + "];\n");
        }
    }


    /**Split entries and get entrydetail name and value for each entry.*/
    private Vector<EntryDetail> getAttributes(Vector<String>  entries){

        Vector entrydetails = new Vector<EntryDetail>();
        if (entries == null)
            return null;

        int size = entries.size();
        // go over each entry and split them into 2 part by regex "( +)"
        for(int i=0;i<size;i++){
            
            String[] fragments = entries.get(i).trim().split("( +)", 2);
            if( fragments == null)
                continue;
            if( fragments.length != 2 )
                continue;
            EntryDetail entrydetail = new
                    EntryDetail(fragments[0].trim(),fragments[1].trim());

            entrydetails.add(entrydetail);
        }
        return entrydetails;
    }


    /**capture necessay values and record them for future using.*/
    private void valueCapture(Vector<EntryDetail> entrydetails){
        //validity check
        if (entrydetails == null)
                return;

        int size = entrydetails.size();
        // go over each entry and try to transfer their value to macthed
        // structure in this class
        for(int i=0;i<size;i++){
            EntryDetail entrydetail = entrydetails.get(i);
            if ( entrydetail == null )
                continue;

            // extract value by type of entry and put value into related
            // value in this class
            classifiedProcess(entrydetail);
        }

    }
    
    /**to each entry, try do dig out valuable attibute information.
     * @param entrydetails  a entry which contains 2 strings. One is the 
     * "entry name", the other one is "entry value". <p>
     * Example: To a entry "knowtator_mention_class Problem", the entry name is 
     * "knowtator_mention_class" and the entry value is "Problem".
     */
    private void classifiedProcess(EntryDetail entrydetail){
        // validity checking
        if( entrydetail == null)
            return;
        if( (entrydetail.entryname == null)||(entrydetail.entryvalue== null))
            return;
        
        // get and set values
        if ( stringCompare(entrydetail.entryname, "knowtator_mention_class")) {
            // found annotation class
            setMentionClass(entrydetail.entryvalue);

        } else if(stringCompare(entrydetail.entryname, "knowtator_mention_annotation")) {
            // found annotation index
            setAnnotationIndex(entrydetail.entryvalue);

        } else if(stringCompare(entrydetail.entryname, "knowtator_slot_mention")) {
            // knowtator_slot_mention            
            setSlotMention(entrydetail.entryvalue);
            
        } else if(stringCompare(entrydetail.entryname, "knowtator_annotated_mention")) {
            // father node's mention id
            setFatherNode(entrydetail.entryvalue);
            //System.out.println(" = current slots string - " + entrydetail.entryvalue );
        } else if(stringCompare(entrydetail.entryname, "knowtator_annotation_annotator")) {
            // annotator index
            setAnnotatorIndex( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_creation_date")) {
            // annotation creation
            setCreationDate( entrydetail.entryvalue );
            
        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_span" )) {
            // get annotation span postion
            setSpanPosition( entrydetail.entryvalue );
            
        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_text" )) {
            // get annotation text
            setAnnotationText( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_text_source" )) {
            // get text source
            setTextSource( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_mention_slot" )) {
            // slot type
            setMentionSlotType( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_mentioned_in" )) {
            // knowtator_mentioned_in
            setMentionSlotMentionedIn( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_mention_slot_value" )) {
            // slot type value
            setmentionslotvalue( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_annotator_firstname" )) {
            // slot type value
            setannotatorfirstname( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_annotation_annotator_lastname" )) {
            // slot type value
            setannotatorlastname( entrydetail.entryvalue );

        } else if(stringCompare( entrydetail.entryname, "knowtator_annotator_id" )) {
            // slot type value
            setannotatorid( entrydetail.entryvalue );
        } else if(stringCompare( entrydetail.entryname, "knowtator_annotator_team_name" )) {
            // slot type value
            setSetAnnotatorid( entrydetail.entryvalue );
        }

        
    }

    private void setannotatorid(String value ){
        value = value.replaceAll("\"", " ").trim();
        this.knowtator_annotator_id = value;
    }
    private void setSetAnnotatorid(String value ){
        value = value.replaceAll("\"", " ").trim();
        this.knowtator_annotator_team_name = value;
    }
    
    private void setannotatorfirstname( String value ){
        value = value.replaceAll("\"", " ").trim();
        this.knowtator_annotation_annotator_firstname = value;
        //System.out.println("  - first name = " + value);
    }

    private void setannotatorlastname( String value ){
        value = value.replaceAll("\"", " ").trim();
        this.knowtator_annotation_annotator_lastname = value;
        //System.out.println("  - first name = " + value);
    }

    /**Get mention slot value. This value may have 2 kinds. One of them are
     * strings surrounded by a pair of quotation marks. And the other one may
     * be one or more index linked to more information.
     */
    private void setmentionslotvalue(String value){
        this.knowtator_mention_slot_value = value;
        this.knowtator_mention_slot_value = this.knowtator_mention_slot_value.replaceAll("\"", " ");
        this.knowtator_mention_slot_value = this.knowtator_mention_slot_value.replaceAll("\\[", " ");
        this.knowtator_mention_slot_value = this.knowtator_mention_slot_value.replaceAll("\\]", " ").trim();

        //System.out.println(" + mention slot value - [" + this.knowtator_mention_slot_value + "]");

        // if this contains one or more indexes which surrounded by paired '['
        // and ']'
        if(value.matches("(.*)\\[(.*)")&&value.matches("(.*)\\](.*)")) {

            // set flag
            this.knowtator_mention_slot_value_isString = false;

            // extract indexes
            int start =0, end = 0;
            int size = value.length();
            do{
                start = value.indexOf('[', start);
                end   = value.indexOf(']', start+1);
                //System.out.println("start, end = " + start + ", " + end);
                if ((start == -1) ||(end==-1))
                    break;


                if((start<end)&&(start >=0)&&(end<=size)){
                    String index = value.substring(start+1, end ).trim();
                    this.knowtator_mention_slot_value_inIndex.add(index);
                    //System.out.println(" - mention slot in index: [" + index+"]");
                }

                start++;

            }while((start<end)&&(start >=0)&&(end<=size));

        } else if(value.matches(".*\".*")) {
        // if the value is a single string

            // set flag
            this.knowtator_mention_slot_value_isString = true;

            // extract string values
            int start =0, end = 0;
            int size = value.length();
            do{
                start = value.indexOf('"', start );
                end   = value.indexOf('"', start+1 );
                if ( (start == -1) || (end == -1) )
                    break;

                if((start<end)&&(start >=0)&&(end<=size)) {
                    String string = value.substring(start+1, end).trim();
                    this.knowtator_mention_slot_value_inString.add(string);
                    //System.out.println(" - mention slot in string: [" + string+"]");
                }

                // different to other extractor, here start symbol and end symbol
                // are all quotation marks
                start = end + 1;

            }while((start >=0)&&(end<=size));
        }

        
    }



    /**get and record the slot mention type.*/
    private void setMentionSlotType(String mentionslot){
        mentionslot = mentionslot.replaceAll("\\[", " ");
        mentionslot = mentionslot.replaceAll("\\]", " ").trim();
        this.knowtator_mention_slot = mentionslot;
        //System.out.println(" - slot mention type - ["+ this.knowtator_mention_slot + "]");
    }

    /**get and record the index of slot mention mentiond id .*/
    private void setMentionSlotMentionedIn(String mentioned_in){
        mentioned_in = mentioned_in.replaceAll("\\[", " ");
        mentioned_in = mentioned_in.replaceAll("\\]", " ").trim();
        this.knowtator_mentioned_in = mentioned_in;
        //System.out.println(" - slot mention type - ["+ this.knowtator_mention_slot + "]");
    }




    /**set and record text source: such as a text fle*/
    private void setTextSource(String textsource){
        if (textsource == null)
            return;

        textsource =  textsource.replaceAll("\\[", " ");
        textsource =  textsource.replaceAll("\\]", " ");
        textsource =  textsource.replaceAll("\"", " ").trim();
        this.knowtator_annotation_text_source = textsource;
        //System.out.println(" - text source = ["+ textsource+"]");
    }


    /**Set and record annotation text.*/
    private void setAnnotationText(String annotationtext){
        if(annotationtext == null)
            return;

        annotationtext = annotationtext.replaceAll("\"", " ").trim();
        this.knowtator_annotation_text = annotationtext;
        //System.out.println(" - annotation text = ["+this.knowtator_annotation_text+"]");
    }


    /**get and record annotation span postion*/
    private void setSpanPosition(String spaninfo){
        if (spaninfo == null)
            return;

        // get span postion in string
        spaninfo = spaninfo.replaceAll("\"", " ").trim();
        this.knowtator_annotation_span = spaninfo;
        //System.out.println(" - span(str) = ["+this.knowtator_annotation_span+"]");

        // get span postion in integer
        String[] positions = spaninfo.split("\\|");
        if(positions.length != 2)
            return;
        int start = Integer.valueOf( positions[0] );
        int end = Integer.valueOf( positions[1] );

        this.span_start = start;
        this.span_end = end;

        //System.out.println(" - span(int) = "+ start + ", " +end+";");
    }

    /**set and record creation date*/
    private void setCreationDate(String creationdate){
        if (creationdate == null)
            return;

        creationdate = creationdate.replaceAll("\"", " " );
        creationdate = creationdate.trim();
        this.knowtator_annotation_creation_date = creationdate;
        //System.out.println(" - annotation creation date: [" + this.knowtator_annotation_creation_date
        //        + "]");

    }

    /**record annotatior information*/
    private void setAnnotatorIndex(String annotatorindex){
        annotatorindex = annotatorindex.replaceAll("\\["," ").trim();
        this.knowtator_annotation_annotator = annotatorindex.replaceAll("\\]"," ").trim();
    }
    /**record the mention id of father node, you can trace to the father node
     * by treating this index as a mention id.
     */
    private void setFatherNode(String fatherNodesMentionID){
        // remove '[' and ']'
        fatherNodesMentionID = fatherNodesMentionID.replaceAll("\\[", " ");
        fatherNodesMentionID = fatherNodesMentionID.replaceAll("\\]", " ");
        fatherNodesMentionID = fatherNodesMentionID.trim();
        this.knowtator_annotated_mention = fatherNodesMentionID;
        
        // System.out.println(" - knowtator annotated mention - [" + fatherNodesMentionID+"]");
    }

    /** Get and analysisi slot information to an annotation. One annotation
     * may have multiple slots.
     */
    private void setSlotMention(String slotMentions){
        if (slotMentions == null) return;
        int start = 0, end = 0, length = slotMentions.length();

        do{
            start = slotMentions.indexOf('[', start);
            end = slotMentions.indexOf(']', start+1);

            // if find paired '[' and ']'
            if((start>=0)&&(end>start)&&(end<=length)){
                String slotMention = slotMentions.substring(start+1, end);
                //System.out.println(" - slot : [" + slotMention + "]");
                this.knowtator_slot_mention.add( slotMention.trim() );
            }

            if ((start==-1)||(end == -1))
                break;
            start++;
        }while((start>=0)&&(end>start)&&(end<=length));

    }

    /**Record the index(a mentionid) for your to find annotation details*/
    private void setAnnotationIndex(String annotationIndex){
        // remove '[' and "]"
        annotationIndex = annotationIndex.replaceAll("\\[", " ");
        annotationIndex = annotationIndex.replaceAll("\\]", " ");
        this.knowtator_mention_annotation = annotationIndex.trim();
        //System.out.println(" - Annotation index - [" + this.knowtator_mention_annotation + "]");
    }
    
    /**Record the mention class of current annotation*/
    private void setMentionClass(String annotationClass){
        this.knowtator_mention_class = annotationClass.trim();
        //System.out.println(" - Annotation classname - " + this.knowtator_mention_class);
    }
    
    /**Compare two string after converted to lowcases and trimed
     * @param originalString    the String you want to compare it with 
     * an alternative string to check whehter they are same or not.
     * @param compareString     alternative string as the comparation object.
     * @return return true only while these two strings are same after removed 
     * extra blank spaces and converted into lowcases.
     */
    private boolean stringCompare(String originalString, String compareString){        
        originalString = originalString.trim().toLowerCase();
        compareString = compareString.trim().toLowerCase();
        if(originalString.compareTo(compareString)==0)
            return true;
        else return false;
    }


}

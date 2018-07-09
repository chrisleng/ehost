/*
 * (PinFile parser for pin import of result editor in eHOST)
 */

package resultEditor.io;


import resultEditor.annotations.Annotation;
import resultEditor.annotations.Article;
import java.io.File;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import preAnnotate.format.Annotator;


/**
 * (PinFile parser for pin import to result editor in eHOST)
 * This class is a pin file parser, which can recognine structure in this pin
 * file and output found infomations in format.<p>
 *
 * @author  Jianwei Leng, on Fri Jun 18 14:11:19 MDT 2010
 *          at Williams Building, Division of Epidemiology, University of Utah.
 *
 *          Jianwei Leng, on Fri Oct 09 17:29:00 MDT 2010, modified for
 *          annotator information of imported annotations.
 */
public class PinFile {

    private static int latestIndex = 0;
    
    /** pin file of current processing. */
    protected File pinfile;
    public Vector<Article> articles = new Vector<Article>();
    private Vector<PINAnnotator> annotators = new Vector<PINAnnotator>();

    /**
     * Cconstructor of class PinFile.
     * 
     * @param   pinfile
     *          Pin file of current processing. Format is 'File'.
     */
    public PinFile(File pinfile){
        this.pinfile = pinfile;
    }

    public static int givemeLatestIndex(){
        latestIndex = latestIndex + 1;
        return (latestIndex - 1);
    }

    @Override
    public String toString() {
        return pinfile.getName();
    }

    /**A parser to get formated data from pin file.*/
    public Vector<Article> startParsing(boolean toStatic){

        // for next pins file
        initlize();

        // Logs
        log.LoggingToFile.log(Level.INFO, "Extract annotations from PINs file: " + pinfile.getAbsolutePath());

        // validity checking: existing
        if(!pinfile.exists())
            return null;
        // validity checking: file or dictionary
        if(pinfile.isDirectory())
            return null;

        // ##1 split file into blocks, which surrounded with '(' and ')', and also
        // got surrounded by two blank lines.
        BlockReader blocks = new BlockReader(pinfile);
        if ( blocks == null    ) return null;
        if ( blocks.size() < 1 ) return null;
        initlize();

        // ##2 go over contents of each text block
        while(blocks.hasNext()) {

            // ##2.1 go over all splitted blocks of current pins file
            String thisblock = blocks.getNext();

            // #2.2 Get content of each text block
            // analysis text of this block and extract all information into
            // structured memory of annotations in depot;
            BlockPraser blockpraser = new BlockPraser(thisblock);

            // ##3 extract annotations and attributes from this parsed block
            informationOrganizer( blockpraser, pinfile );

        }

        // ##4 binding classname with PINS annotations
        classBinding();
        // ##4.1 to each annotations, figure who is their annotator
        annotatorBinding();

        // ##5 copy found pins annotations to annotation depot of result editor
        articles = annotationCopyer( toStatic );


        return( articles );        
    }


    /**Extract annotations and their attributes from current prased block.
     *
     * @param   prasedBlock
     *          Is the prased structured data from text of this block from current pins file.
     *
     * <p>Details see flow chart LENG-4001
     */
    private void informationOrganizer( BlockPraser prasedBlock, File pinfile){
        // validity check
        if ( prasedBlock == null ) return;
        if ( prasedBlock.titles == null )
            return;

        // ##<1>## get title
        String title = prasedBlock.titles.trim();
        if (title == null)    return;

        //System.out.println(" + title = [" + title + "]");

        // 2.1 if this block show text source name
        // show text source information
        // details or examples: #1 @ LENG-4001
        if( title.compareTo( "file+text+source" ) == 0 ){
            String textsourcefilename = prasedBlock.mentionid;
            //System.out.println("    - text source filename = [" + textsourcefilename + "]" );
            if (( textsourcefilename != null ) && (textsourcefilename.trim().length() > 0)) {
                resultEditor.workSpace.WorkSet.pinsFileSet_add( pinfile.getName().trim(), textsourcefilename);
            }
            return;
        }

        // knowtator+human+annotator: show identification information of annotator
        if( title.compareTo( "knowtator+human+annotator" ) == 0 ){
            String id = prasedBlock.mentionid.trim();
             if ( id != null ){
                //System.out.println("    - mentionid =["+ id + "]");
                String annotatorfirstname = prasedBlock.knowtator_annotation_annotator_firstname;
                //System.out.println("    - knowtator_annotation_annotator_firstname =["+ annotatorfirstname + "]");
                String annotatorid = prasedBlock.knowtator_annotator_id;
                String annotatorlastname = prasedBlock.knowtator_annotation_annotator_lastname;
                //if(annotatorid == null)
                //    annotatorid = "";
                //System.out.println("    - knowtator_annotator_id =["+ annotatorid + "]");
                PINAnnotatorDepot.add( id, annotatorfirstname, annotatorlastname, annotatorid );
             }
             return;
        }

        // get all slot mentions
        // annotation main node: knowtator+class+mention
        if( title.compareTo( "knowtator+class+mention" ) == 0 ) {
            String id = prasedBlock.mentionid.trim();
            if ( id != null ){
                //System.out.println("    - id =["+ id + "]");
                String classname = prasedBlock.knowtator_mention_class.trim() ;
                recordClassname( classname );

                //System.out.println("    - knowtator_mention_class =["+ classname + "]");
                String INDEX_knowtator_mention_annotation = prasedBlock.knowtator_mention_annotation.trim();
                //System.out.println("    - knowtator_mention_annotation =["+ INDEX_knowtator_mention_annotation + "]");
                Vector<String> slots = prasedBlock.knowtator_slot_mention;
                //for(String slot:slots) {
                //    System.out.println("    = - knowtator_slot_mention =["+ slot + "]");
                //}
                PINAnnotationAttributes.add(
                        // the index of current block
                        id,      
                        // class name: "knowtator_mention_class"
                        classname,
                        // id of current annotation: "knowtator_mention_annotation"
                        INDEX_knowtator_mention_annotation,   // index which bring you to get span information
                        // all slots of current annotation, "knowtator_slot_mention"
                        slots

                   );
            }
            return;
        }

        // knowtator+annotation
        // details and examples: #2 @ LENG-4001
        if( title.compareTo( "knowtator+annotation" ) == 0 ) {
            String id = prasedBlock.mentionid.trim();
            if ( id != null ){
                //System.out.println("    - id =["+ id + "]");

                // try to get annotator index
                String index_to_knowtator_annotation_annotator;
                if (prasedBlock.knowtator_annotation_annotator != null )
                    index_to_knowtator_annotation_annotator = prasedBlock.knowtator_annotation_annotator.trim();
                else
                    index_to_knowtator_annotation_annotator = "";
                //System.out.println("    - knowtator_annotation_annotator =["+ index_to_knowtator_annotation_annotator + "]");

                // try to get annotation creation date
                String knowtator_annotation_creation_date;
                if ( prasedBlock.knowtator_annotation_creation_date != null)
                    knowtator_annotation_creation_date = prasedBlock.knowtator_annotation_creation_date.trim();
                else
                    knowtator_annotation_creation_date = "";
                //System.out.println("    - knowtator_annotation_creation_date =["+ knowtator_annotation_creation_date + "]");
                
                int span_start = prasedBlock.span_start;
                int span_end = prasedBlock.span_end;
                String annotationtext = prasedBlock.knowtator_annotation_text;
                String textsource = prasedBlock.knowtator_annotation_text_source;
                PINSAnnotationDepot.add(
                        id,
                        index_to_knowtator_annotation_annotator,
                        knowtator_annotation_creation_date,
                        span_start, span_end,
                        annotationtext,
                        textsource,
                        prasedBlock.knowtator_annotated_mention);
            }
            return;
        }
        
        // annotator team name
        if( title.compareTo("knowtator+annotator+team") == 0) {
            String id = prasedBlock.mentionid.trim();
             if ( id != null ){
                //System.out.println("    - mentionid =["+ id + "]");
                String annotatorfirstname = prasedBlock.knowtator_annotator_team_name;
                //System.out.println("    - knowtator_annotation_annotator_firstname =["+ annotatorfirstname + "]");
                //String annotatorid = "";
                //System.out.println("    - knowtator_annotator_id =["+ annotatorid + "]");
                PINAnnotatorDepot.add( id, annotatorfirstname, null, null);
             }
            return;
        }

        // collect slot mention contents ( sample relationship)
        // knowtator+string+slot+mention
        if( title.compareTo("knowtator+string+slot+mention") == 0) {
            String id = prasedBlock.mentionid.trim();
            if ( id != null ){
                String slot = replaceControlSymbols( prasedBlock.knowtator_mention_slot );
                String slot_value = prasedBlock.knowtator_mention_slot_value;
                if(slot_value == null)
                    return;
                String mentioned_in = prasedBlock.knowtator_mentioned_in;
                SimpleSlotDepot.addSlot(id, slot, slot_value, mentioned_in);
                //System.out.println( " id= " + id + " ;" +
                //        "\n        - has slot = " +slot +
                //        "\n        - has slot_value = " + slot_value +
                //        "\n        - has mentioned_in = " + mentioned_in
                //        );
            }
            return;
        }

        // for complex relationships
        // knowtator+complex+slot+mention
        if( title.compareTo("knowtator+complex+slot+mention") == 0) {
            String id = prasedBlock.mentionid.trim();
             if ( id != null ){
                //System.out.println("    - mentionid =["+ id + "]");
                String knowtator_mention_slot = prasedBlock.knowtator_mention_slot;

                Vector<String> knowtator_mention_slot_value = prasedBlock.knowtator_mention_slot_value_inIndex;
                if(knowtator_mention_slot_value.size() == 0)
                    return;
                
                //String knowtator_mention_slot_value = prasedBlock.knowtator_mention_slot_value_inIndex;
                String knowtator_mentioned_in = prasedBlock.knowtator_mentioned_in;
                ComplexSlots.addSlot(id, knowtator_mention_slot, knowtator_mention_slot_value,
                        knowtator_mentioned_in );
             }
            return;
        }
    }



    /**empry static memory space each time before you start prasing a block of PINS files*/
    private void initlize(){
        PINAnnotatorDepot.clear();
        PINAnnotationAttributes.clear();
        PINSAnnotationDepot.clear();
        SimpleSlotDepot.clear();
        ComplexSlots.clear();
    }

    /**record newly found class name.*/
    private void recordClassname(String classname ){
        if ( classname == null )    return;
        if ( classname.trim().length() < 1 )    return;
        resultEditor.annotationClasses.Depot classnamedepot = new resultEditor.annotationClasses.Depot();
        classnamedepot.addElement(classname.trim(), "pins", null, true , false);
    }

    /**
     * ##4 a. binding classname with PINS annotations.
     *     b. send matched slot information to PINS annotations.
     *
     */
    private void classBinding(){
        int paa_size = PINSAnnotationDepot.size();
        int pa_size = PINAnnotationAttributes.size();

        if(( paa_size < 1)||(pa_size<1) )
            return;

        Vector<PINAnnotation> paas = PINSAnnotationDepot.getAll();
        Vector<PINAnnotationAttribute> pas = PINAnnotationAttributes.getAll();

        if(( paas == null )||( pas == null ))
            return;

        for( PINAnnotation currentAnnotation : paas ){
            if (currentAnnotation.id == null)
                continue;
            if (currentAnnotation.id.trim().length() < 1)
                continue;

            String classname = getClassname( currentAnnotation.id, pas );

            if ( (classname != null) && ( classname.trim().length() > 0 ) ) {
                currentAnnotation.setclassname( classname.trim() );
            } else {
                currentAnnotation.setclassname( null );
            }

            //String mention = getMention( currentAnnotation.id, pas );
            //currentAnnotation.annotationMention = mention;


            // get all relationship
            Vector<String> relationshipIndexes = getRelationships(currentAnnotation.id, pas);
            if ( relationshipIndexes != null ) {

                // To each index of slot, check simple slots and complex slots to find them
                for(String relationshipIndexe : relationshipIndexes ) {
                    boolean found = false;
                    // check simple slot
                    SimpleSlot thisslot = SimpleSlotDepot.getSlot( relationshipIndexe );
                    if ( thisslot != null ) {
                        currentAnnotation.setAnnotationSimpleSlot(
                                thisslot.knowtator_mention_slot ,
                                thisslot.knowtator_mention_slot_value
                                );
                        found = true;
                    }

                    // check complex slot
                    if( found == false){
                        ComplexSlot thiscomplexslot = ComplexSlots.getSlot( relationshipIndexe );

                        if ( thiscomplexslot != null ) {
                            if (   ( thiscomplexslot.knowtator_mention_slot_value != null ) ) {
                                for(String LinkedAnnotationIndex : thiscomplexslot.knowtator_mention_slot_value ){
                                if ( LinkedAnnotationIndex.trim().length() < 1) continue;
                                int uniqueindex  = -1;
                                String annotationtext = PINSAnnotationDepot.linkToAnotherAnnotation( LinkedAnnotationIndex.trim(), currentAnnotation.interSingle );
                                uniqueindex = PINSAnnotationDepot.linkToAnotherAnnotation( LinkedAnnotationIndex.trim() );
                                currentAnnotation.setAnnotationComplexSlot(
                                            annotationtext,
                                            thiscomplexslot.knowtator_mention_slot.trim(),
                                            uniqueindex);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * ##4 to each newly imported PINS annotations, figure out who is
     * its anotator.
     *
     */
    private void annotatorBinding(){

        try{            
            // validity checking
            int newannotations_amount = PINSAnnotationDepot.size();
            int annotators_amount = PINAnnotatorDepot.PIN_annotators_depot.size();

            if(( newannotations_amount < 1)||(annotators_amount<1) )
                return;

            Vector<PINAnnotation> newannotations = PINSAnnotationDepot.getAll();
            for(PINAnnotation currentAnnotation : newannotations ){
                if ( currentAnnotation == null )
                    continue;
                if ( currentAnnotation.index_to_knowtator_annotation_annotator == null)
                    continue;
                if ( currentAnnotation.index_to_knowtator_annotation_annotator.trim().length() < 1)
                    continue;
                
                // figure out its annotator
                String id = currentAnnotation.index_to_knowtator_annotation_annotator.trim();
                for( PINAnnotator annotatorinfo : PINAnnotatorDepot.PIN_annotators_depot){
                    if (( annotatorinfo == null )||(annotatorinfo.id == null))
                        continue;
                    if(annotatorinfo.id.trim().equals(id)){
                        currentAnnotation.annotator_first_name = annotatorinfo.annotator_first_name;
                        currentAnnotation.annotator_last_name = annotatorinfo.annotator_last_name;
                        currentAnnotation.annotator_id = annotatorinfo.annotatorid;
                        break;
                    }

                }
                //PINAnnotatorDepot.PIN_annotators_depot.
            }

        }catch(Exception ex){

        }

    }

    

    /**get class name in block of knowtator+class+mention by a given index from
     * block of knowtator+annotation
     */
    private String getClassname( String index, Vector<PINAnnotationAttribute> pas){
        //System.out.println("------- index = " + index);
        if ((index == null)||(pas == null))
            return null;
        for(PINAnnotationAttribute pinannotation: pas) {
            if ( pinannotation.INDEX_knowtator_mention_annotation == null )
                continue;
            
            if ( index.trim().compareTo( pinannotation.INDEX_knowtator_mention_annotation.trim() ) == 0 ) {
                //System.out.println("found matched annotated class:" + pinannotation.annotationclass);
                return pinannotation.annotationclass;
            }
        }
        return null;
    }

    private String getMention( String index, Vector<PINAnnotationAttribute> pas){
        //System.out.println("------- index = " + index);
        if ((index == null)||(pas == null))
            return null;
        for(PINAnnotationAttribute pinannotation: pas) {
            if ( pinannotation.INDEX_knowtator_mention_annotation == null )
                continue;

            if ( index.trim().compareTo( pinannotation.INDEX_knowtator_mention_annotation.trim() ) == 0 ) {
                //System.out.println("found matched annotated class:" + pinannotation.annotationclass);
                return pinannotation.INDEX_knowtator_mention_annotation;
            }
        }
        return null;
    }


    private Vector<String> getRelationships( String index, Vector<PINAnnotationAttribute> pas){
        //System.out.println("------- index = " + index);
        if ((index == null)||(pas == null))
            return null;
        for(PINAnnotationAttribute pinannotation: pas) {
            if ( pinannotation.INDEX_knowtator_mention_annotation == null )
                continue;

            if ( index.trim().compareTo( pinannotation.INDEX_knowtator_mention_annotation.trim() ) == 0 ) {
                //System.out.println("found matched annotated class:" + pinannotation.annotationclass);
                return pinannotation.INDEXES_knowtator_slot_mention;
            }
        }
        return null;
    }


    /** ##5 copy found pins annotations to annotation depot of result editor */
    private Vector<Article> annotationCopyer(boolean toStatic) {
        Vector<PINAnnotation> paas = PINSAnnotationDepot.getAll();
        annotators.addAll( PINAnnotatorDepot.PIN_annotators_depot );
        Vector<Article> toReturn = new Vector<Article>();
        if( paas == null ) return null;

        int paa_size = PINSAnnotationDepot.size();
        if( paa_size < 1) return null;

        String annotator_name, annotator_id;

        resultEditor.annotations.Depot depot = new resultEditor.annotations.Depot();

        for( PINAnnotation organized_imported_annotation : paas ){

            // validity checking
            if (organized_imported_annotation == null)
                continue;
            if (organized_imported_annotation.span_start < 0)
                continue;
            if (organized_imported_annotation.span_end < 0)
                continue;
            if ((organized_imported_annotation.annotationtext == null)
                 ||(organized_imported_annotation.annotationtext.trim().length() < 1))
                continue;
            if ((organized_imported_annotation.annotated_class == null)
                 ||(organized_imported_annotation.annotated_class.trim().length() < 1))
                continue;
            if (organized_imported_annotation.textsource == null)
                continue;


            if( toStatic ) {

                annotator_name = null;
                if((organized_imported_annotation.annotator_first_name == null)
                    &&(organized_imported_annotation.annotator_last_name == null)){
                    annotator_name = null;
                }else if(organized_imported_annotation.annotator_first_name == null){
                    annotator_name = organized_imported_annotation.annotator_last_name.trim();
                }else if(organized_imported_annotation.annotator_last_name == null){
                    annotator_name = organized_imported_annotation.annotator_first_name.trim();
                }else{
                    annotator_name = organized_imported_annotation.annotator_last_name.trim()
                            + ", "
                            + organized_imported_annotation.annotator_first_name.trim();
                }

                annotator_id = organized_imported_annotation.annotator_id;

                //System.out.println("@===========@ " + annotator_name + " / " + annotator_id);

                // ##<5.1>##  function 1 of 2: record this annotation into the annotation depot
                depot.addANewAnnotation(
                    organized_imported_annotation.textsource.trim() ,
                    organized_imported_annotation.annotationtext.trim(),
                    organized_imported_annotation.span_start,
                    organized_imported_annotation.span_end,
                    organized_imported_annotation.knowtator_annotation_creation_date.trim(),
                    organized_imported_annotation.annotated_class.trim(),
                    annotator_name, // annotator name
                    annotator_id, // annotator id
                    organized_imported_annotation.simpleRelationships,
                    organized_imported_annotation.complexRelationships,
                    organized_imported_annotation.uniqueIndex
                    );

            } else {
                String name = "";
                String id = "";
                for(PINAnnotator annotator: annotators)
                {
                    if(annotator.id.toLowerCase().trim().equals(organized_imported_annotation.index_to_knowtator_annotation_annotator.toLowerCase().trim()))
                    {
                        name = annotator.annotator_first_name;
                        id = annotator.annotatorid;
                    }
                }

                Annotation a = new Annotation(String.valueOf(env.Parameters.getLatestUsedMentionID()),
                        organized_imported_annotation.annotationtext.trim(),
                        organized_imported_annotation.span_start,
                    organized_imported_annotation.span_end,
                    name,
                    id,
                    organized_imported_annotation.annotated_class.trim(),
                    organized_imported_annotation.knowtator_annotation_creation_date.trim()
                    );

                boolean newOne = true;

                for(Article article: toReturn) {
                    if(article.filename.equals(organized_imported_annotation.textsource.trim())) {
                        article.addAnnotation(a);
                        newOne = false;
                    }
                }

                if(newOne) {
                    Article newArticle = new Article(organized_imported_annotation.textsource.trim());
                    newArticle.addAnnotation(a);
                    toReturn.add(newArticle);
                }
            }
            
        }

        return toReturn;

    }

    public HashSet<String> getAllClasses() {
        HashSet<String> classes = new HashSet<String>();
        for(Article article: articles) {
            for(Annotation annotation: article.annotations)
                classes.add(annotation.annotationclass);
        }
        return classes;
    }
    /**
     * Get all PIN_annotators_depot from this pinsFile.
     * @return
     */
    public HashSet<Annotator> getAllAnnotators() {
        HashSet<Annotator> theAnnotators = new HashSet<Annotator>();
        for(PINAnnotator annotator: annotators) {
             theAnnotators.add(new Annotator(annotator.annotator_first_name, annotator.annotatorid, annotator.id));
        }
        return theAnnotators;
    }

    public HashSet<String> getAllFileNames() {
        HashSet<String> fileNames = new HashSet<String>();
        for(Article article: articles) {
            fileNames.add(article.filename);
        }
        return fileNames;
    }

    /**
     * To a given string, remove PINS control symbols (such as '+') and convert
     * HEX to char while necessary. HEX usually are "%28" which can be replaced
     * by character '(' and "%29" which can be replaced by ')'.
     *
     * @param   afferentStr
     *          The afferent original which you want to remove control symbols,
     *          such as '+'. And replace HEX %28 and %29 to '(' and ')' respectively.
     */
    private String replaceControlSymbols( String afferentStr ){
        String efferentStr = afferentStr;

        if( afferentStr == null )
            return efferentStr;

        efferentStr = afferentStr.replaceAll( "\\+", " "  );
        efferentStr = efferentStr.replaceAll("%28", "(");
        efferentStr = efferentStr.replaceAll("%29", ")");

        return efferentStr;
    }

}































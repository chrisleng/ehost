/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package config.project;

import config.Block;
import java.util.Vector;
import relationship.simple.dataTypes.AttributeSchemaDef;
import relationship.complex.dataTypes.RelationshipDef;
import env.ComparisonSettings;
import env.Parameters;
import java.util.HashSet;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class ParameterGather {
    /**Load parameters into memory: In each of these splitted blocks,
     * parametername indicates the name of
     * a parameters, and all strings stored in the vector named "values" are
     * its values defined in repetitive formats.
     */
    public void load(Vector<Block> _blocks)
    {

        defaultvalue();

        if((_blocks==null)||(_blocks.size()<1))
            return;

        try{
            String str = null;

            for(Block block: _blocks)
            {

                if(block==null)
                    continue;
                

                if(block.parameterName.compareTo("CONCEPT_LIB")==0)
                {
                    getConceptLib(block);
                }
                
                if(block.parameterName.compareTo("COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES")==0)
                {
                    getComplexNoteFormat(block);
                }

                if(block.parameterName.compareTo("CONCEPT_LIB_SEPARATOR")==0)
                {
                    getConceptLibSeparator(block);
                }

                if(block.parameterName.compareTo("OUTPUT_CONTROL_CONCEPT_ENABLED")==0)
                {
                    getOutputControlConceptEnabled(block);
                }


                if(block.parameterName.compareTo("ORACLE_SEEKER_TO_SIMILAR_ANNOTATIONS_ENABLED")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                            env.Parameters.oracleFunctionEnabled = true;
                        else
                            env.Parameters.oracleFunctionEnabled = false;
                    
                }


                if(block.parameterName.compareTo("CREATE_ANNOTATION_BY_EXACT_SELECTED_SPAN")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                            env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = true;
                        else
                            env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = false;

                }

                if(block.parameterName.compareTo("ORACLE_NLP_for_WholeWord")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                            env.Parameters.Oracle.search_matchWholeWord = true;
                        else
                            env.Parameters.Oracle.search_matchWholeWord = false;

                }


                if(block.parameterName.compareTo("ENABLE_QUICKLY_ATTRIBUTE_EDITOR")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                            env.Parameters.enabled_displayAttributeEditor = true;
                        else
                            env.Parameters.enabled_displayAttributeEditor = false;

                }


                if(block.parameterName.compareTo("NLP_USE_STOPWORDS")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                            env.Parameters.nlp_dictionary_proc.using_StopWords = true;
                        else
                            env.Parameters.nlp_dictionary_proc.using_StopWords = false;

                }

                //if(block.parameterName.compareTo("GRAPHICPATH_ENABLED")==0)
                //{
                //    str = getOnlyOneTextValue(block);
                //    if((str!=null)&&(str.trim().compareTo("YES")==0))
                //        env.Parameters.enabled_GraphPath_Display = true;
                //    else
                //        env.Parameters.enabled_GraphPath_Display = false;

                //}


                if(block.parameterName.compareTo("DIFFERENCE_MATCHING_ENABLED")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                        env.Parameters.enabled_Diff_Display = true;
                    else
                        env.Parameters.enabled_Diff_Display = false;

                }


                // difference matching - check for cross overlap
                if(block.parameterName.compareTo("DIFFERENCE_MATCHING_CROSS")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                        env.Parameters.DifferenceMatching.checkCrossSpan = true;
                    else
                        env.Parameters.DifferenceMatching.checkCrossSpan = false;

                }

                // read and set the flag of multiple dictionaries weight
                // value = true, if there have different weight, string in ini = "TRUE"
                if(block.parameterName.compareTo("PRE_ANNOTATED_CONCEPT_DICTIONARIES_HAVE_DIFFERENT_WEIGHT")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("TRUE")==0))
                        env.Parameters.Pre_Defined_Dictionary_DifferentWeight = true;
                    else
                        env.Parameters.Pre_Defined_Dictionary_DifferentWeight = false;

                }

                
                // difference matching - check same span annotations
                if(block.parameterName.compareTo("DIFFERENCE_MATCHING_SAME")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                        env.Parameters.DifferenceMatching.checkSameOverlappingSpan = true;
                    else
                        env.Parameters.DifferenceMatching.checkSameOverlappingSpan = false;

                }
       

                // export verify suggestion to XML files if this value is true
                if(block.parameterName.compareTo("OUTPUT_SUGGESTIONS_TOXML")==0)
                {
                    str = getOnlyOneTextValue(block);
                    if((str!=null)&&(str.trim().compareTo("YES")==0))
                        env.Parameters.Output_XML.output_verify_suggestions_toXML = true;
                    else
                        env.Parameters.Output_XML.output_verify_suggestions_toXML = false;

                }


                // load input files used in pervious operation
                
                /*try{
                    if(block.parameterName.compareTo("INPUTFILES") == 0)
                    {

                        if((block!=null)&&(block.values!=null)&&(block.values.size()==3))
                        {

                            String filename = block.values.get(0);
                            String amountOfWord = block.values.get(1);
                            String absoluteFilename = block.values.get(2);
                            // load input files used in pervious operation for configuration

                            //assemble instance for ini

                            // add same content to the memory
                            env.ClinicalNoteList.CorpusStructure tcn = new env.ClinicalNoteList.CorpusStructure();
                            File file = new File(absoluteFilename);

                            // check amount of clinical files while first loading configure
                            // file.
                            if (env.Parameters.isFirstTimeLoadingConfigureFile) {
                                env.ClinicalNoteList.CorpusLib.addTextFile(file);
                            } else {
                                env.ClinicalNoteList.CorpusLib.AddFile(file, Integer.valueOf(amountOfWord.trim()));
                            }
                        }
                       
                                                      
                       
                    }
                }catch(Exception ex){
                    System.out.println( "error 1102110228::"+ex.toString());
                }*/


                // load pre-annotated concept dictionaries from the configure file
                /** @para filename - file name with absolute path
                 * @para weight - if same all will be 0, otherwise big number
                 * means heavy weight
                 * @para description
                 * @para separator
                 * @para number_of_valid_entries
                 */
                if(block.parameterName.compareTo("PRE_ANNOTATED_CONCEPT_DICTIONAIES")==0)
                {
                    getPreAnnotationConceptDictionaires(block);
                }

                
                if(block.parameterName.compareTo("VERIFIER_DICTIONARIES")==0)
                {
                    for(int i = 0; i< block.values.size(); i++)
                    {
                        str = block.values.get(i);
                        if(str!=null){
                            dictionaries.VerifierDictionaryFormat toAdd =
                                    new dictionaries.VerifierDictionaryFormat(str);
                            if (toAdd.checkGood())
                            {
                                // load input files used in pervious operation for configuration
                                env.Parameters.VERIFIER_DICTIONARIES.add(toAdd);
                                // add same content to the memory
                                // switchpool.InputFiles.AddInputFiles(s[0], s[1], s[2]);
                            }
                        }
                    }
                }

                if(block.parameterName.compareTo("VERIFIER_COMPARE_SETTINGS")==0)
                {
                    str = this.getOnlyOneTextValue(block);
                    if(str!=null){
                        ComparisonSettings settings = new ComparisonSettings(str);
                        env.Parameters.verifierComparisonSettings = settings;
                    }
                }


                if(block.parameterName.compareTo("RELATIONSHIPS")==0)
                {
                    if(block.values!=null)
                    {
                        for(String entry:block.values){
                            if((entry!=null)&&(entry.trim().length()>1))
                                Parameters.RelationshipSchemas.add(new RelationshipDef(entry));
                        }
                    }
                }


                
                try{
                    if(block.parameterName.compareTo("ATTRIBUTES")==0)
                    {
                        str = this.getOnlyOneTextValue(block);
                        if(str!=null){
                            String[] s = str.trim().split("@@@@");

                            for(String name: s)
                            {
                                String[] entries = name.split("---");
                                if(entries.length == 0)
                                    continue;
                                String type = entries[0];
                                Vector<String> allowable = new Vector<String>();
                                for(int i = 1; i< entries.length ; i++)
                                {
                                    if(!entries[i].equals(""))
                                    allowable.add(entries[i]);
                                }
                                Parameters.AttributeSchemas.Add(new AttributeSchemaDef(type, allowable));
                            }
                        }
                    }
                }catch(Exception ex){
                    log.LoggingToFile.log( Level.SEVERE,"error 1102110133::"+ex.toString());
                }


                //#### retrivel class information if current block is marked as "CLASS"
                if(block.parameterName.compareTo("CLASS")==0)
                {
                    try{
                        // got all strings for this "CLASS"
                        Vector<String> blockvalues = block.values;
                        if(blockvalues==null){
                            continue;
                        }

                        // check for number of lines: it is incomplete if
                        // number of lines in this block are less than 2 as
                        // a class must have a name and a designated background
                        // color
                        if(blockvalues.size()<2)
                            continue;

                        int number_of_lines = nonBlankLines( blockvalues );
                        if((number_of_lines<2)||(number_of_lines>3))
                            continue;

                        // retrival class information to depot of class(/markables)
                        getClassInfo( blockvalues );
                        
                        
                    }catch(Exception ex){
                        log.LoggingToFile.log( Level.SEVERE,"####ERROR#### 1107051148:: fail to "
                                + "load class informaiton from schema file."
                                + ex.toString());
                    }
                }
                
            }
        }catch(Exception ex){
            
        }
    }


    /**retrival class information from given block of strings which just got
     * loaded from the schema file, and save the just loaded class into depot
     * of classes(/markables);
     *
     * @param   _blockvalues
     *          the string block which contains a lot of strings and is in format
     *          of "vector<String>"
     */
    private void getClassInfo(Vector<String> _blockvalues){

        try{
            // don't need to check the parameter: _blockvalues
            // as method of nonBlankLines() already checked number
            // of valid non-blank lines in current block;
            // (we already have maked sure that number of non-blank lines
            // in this block are >= 2 and <=3)

            String classname = null;
            String color     = null;
            String source    = null;

            for(String str: _blockvalues) {

                // BUT we still need to void blank line if have
                if(str==null)
                    continue;
                if(str.trim().length()<1)
                    continue;

                if(classname==null){
                    classname = str.trim();
                }else{
                    if(color == null){
                        color = str.trim();
                    }else{
                        
                        if(source==null)
                            source = str.trim();
                        else
                            break;
                    }
                }
                
            }

            // After we got a complete record of a class, it should been
            // checked into the system.
            checkInClass(classname, color, source);

            //System.out.println("\n");
            //System.out.println("CLASS Name = " + classname);
            //System.out.println("CLASS color = " + color);
            //System.out.println("CLASS source = " + source);

        }catch(Exception ex){
            
        }
    }


    
    /**This method is used to check in the class you just loaded from the
     * schema configure file.
     *
     * @param   _classname
     *          the string of the name of this class which stored in schema file
     * @param   _color
     *          the string of the rgb color code of this class, usually it is
     *          written in "( r-value, g-value, b-balue)"
     *
     * @param   _classComment
     *          the string of comment to current class; it can be used to record
     *          where we first found this class, such as "from imported XML", or
     *          "generated by annotator "tom" ".
     */
    private void checkInClass(String _classname, String _color, String _classComment){
        if((_classname==null)||(_classname.trim().length()<1))
            return;

        if((_color==null)||(_color.trim().length()<1))
            return;

        int r=0,g=0,b=0;
        // try to get rbg color code
        if( (_color.contains("("))&&(_color.contains(")")) )
        {
            String colorstr = _color.replaceAll("\\(", " ");
            colorstr = colorstr.replaceAll("\\)", " ").trim();
            try{
                String[] rgbs = colorstr.split(",");
                if (rgbs.length == 3)
                {
                    r = Integer.valueOf( rgbs[0].trim() );
                    g = Integer.valueOf( rgbs[1].trim() );
                    b = Integer.valueOf( rgbs[2].trim() );
                }else{
                    return;
                }
            }catch(Exception e){ }

        } else {
            return;
        }

        resultEditor.annotationClasses.Depot.addElement( _classname.trim(), _classComment, r, g, b);
        
    }
    

    /**This method checks and return the number of found non-blank line for a
     * given string block which should be in format of "vector<String>".
     *
     * @return  number of non-blank lines in the given string block
     * @param   _input
     *          the string block which contains a lot of strings and is in format
     *          of "vector<String>"
     */
    private int nonBlankLines(Vector<String> _input){
        
        if(_input==null)
            return 0;

        if(!(_input instanceof Vector)){
            return 0;
        }

        int number_of_nonBlankLines = 0;
        
        for(Object o:_input)
        {
            if(!(o instanceof String)){
                continue;
            }

            String str = (String) o;
            if(str==null)
                continue;

            if(str.trim().length()>0)
                number_of_nonBlankLines++;

        }

        return number_of_nonBlankLines;

    }

    private void defaultvalue(){
        // clear all existing data before read from configure file
            //env.Parameters.LIST_ClinicalNotes.clear();
            env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.clear();
            env.Parameters.VERIFIER_DICTIONARIES.clear();
            env.Parameters.forceChangeLatestUsedMentionID(-1);
            env.Parameters.Pre_Defined_Dictionary_DifferentWeight = false;
            //env.Parameters.enabled_GraphPath_Display = true;
            env.Parameters.enabled_Diff_Display = true;
    }

    private void getPreAnnotationConceptDictionaires(Block block)
    {
        if(block==null)
            return;

        if(block.values==null)
            return;

        if(block.values.size()!=5)
            return;

        
        Object[] file =
            {
                block.values.get(0),
                block.values.get(1),
                block.values.get(2),
                block.values.get(3),
                block.values.get(4)
            };
        
        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.add(file);
    }


    private void getOutputControlConceptEnabled(Block block)
    {
        env.Parameters.OUTPUT_CONTROL_CONCEPT_ENABLED = getOnlyOneTextValue(block );
    }

    private void getConceptLibSeparator(Block block)
    {
        env.Parameters.CONCEPT_LIB_SEPARTOR = getOnlyOneTextValue(block );
    }

    private void getConceptLib(Block block)
    {
        env.Parameters.CONCEPT_LIB = getOnlyOneTextValue(block );        
    }
    
    private void getComplexNoteFormat(Block block)
    {
        env.Parameters.COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES = getOnlyOneTextValue(block );        
    }



    private String getOnlyOneTextValue(Block block)
    {
        try{
            
            if(block==null)
                return null;

            if(block.values.size()!=1)
                return null;

            if(block.values.get(0)==null)
                return null;

            if(block.values.get(0).trim().length()<1)
                return null;

            return  block.values.get(0).trim();

        }catch(Exception ex){
            return null;
        }

    }
}

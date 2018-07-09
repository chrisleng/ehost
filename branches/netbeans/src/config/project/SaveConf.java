package config.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import relationship.simple.dataTypes.AttributeSchemaDef;
import relationship.complex.dataTypes.RelationshipDef;
import env.Parameters;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author leng
 */
public class SaveConf {
    

    /**save configure to files.*/
    public void save(File _filename)
    {
        if((_filename==null))
        {
            log.LoggingToFile.log( Level.SEVERE,"error 1102100105::fail to get the output configure file name");
            return;
        }

        try
        {
            FileOutputStream output = new FileOutputStream(_filename);

            PrintStream p = new PrintStream(output);
            String outstr;

            //##1##comment
            p.println("// =================== These are comments ===================");
            p.println("// [1] All parameters are surround by [ and ]; ");
            p.println("// [2] Parameters are separated by one or more space line; ");
            p.println("// [3] Each parameter can have one or more values; ");
            p.println("// [4] Each value is one line; ");
            p.println("// [5] Values belong to one specific parameter should be ");
            p.println("//     connected lines, and no space line between them.");
            p.println("// ==========================================================");
            p.println("");


            //##2##CONCEPT_LIB
            /*p.println("[CONCEPT_LIB]");
            String annotatorName = ResultEditor.WorkSpace.WorkSet.current_annotator_name;
            if ((annotatorName == null)||(annotatorName.trim().length() < 1))
                 p.println("");
            else
                p.println(annotatorName.trim());
            p.println("");

             *
             */
            String str;

            // CONCEPT_LIB
            p.println("[CONCEPT_LIB]");
            str = env.Parameters.CONCEPT_LIB;
            if ((str == null)||(str.trim().length() < 1))
                 p.println("");
            else
                p.println(str.trim());
            p.println("");


            // CONCEPT_LIB_SEPARATOR
            p.println("[CONCEPT_LIB_SEPARTOR]");
            str = env.Parameters.CONCEPT_LIB_SEPARTOR;
            if ((str == null)||(str.trim().length() < 1))
                 p.println("");
            else
                p.println(str.trim());
            p.println("");


            // CONCEPT_LIB_SEPARATOR
            p.println("[OUTPUT_CONTROL_CONCEPT_ENABLED]");
            str = env.Parameters.OUTPUT_CONTROL_CONCEPT_ENABLED;
            if ((str == null)||(str.trim().length() < 1))
                 p.println("");
            else
                p.println(str.trim());
            p.println("");

            // COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES
            // $$$$strange one$$$$
            p.println("[COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES]");
            str = env.Parameters.COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES;
            if (str != null && str.trim().compareTo("YES") != 0)
                str = "NO";
            if (str == null)
                p.println("NO");
            else p.println(str.trim());                            
            p.println("");

            


            // flag to enable/disable the oracle seeker to find simlar annotations
            p.println("[ORACLE_SEEKER_TO_SIMILAR_ANNOTATIONS_ENABLED]");
            if ( env.Parameters.oracleFunctionEnabled)
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            // flag to enable/disable the oracle seeker to find simlar annotations
            p.println("[ENABLE_QUICKLY_ATTRIBUTE_EDITOR]");
            if ( env.Parameters.enabled_displayAttributeEditor)
                p.println("YES");
            else
                p.println("NO");
            p.println("");

            
            
            /**Flag that indicate whether we need to use exact span which user
             * slected on document viewer, or the span with corrected border by
             * detecting space or symbols, to build a new annotation.
             * Default is false;*/
            p.println("[CREATE_ANNOTATION_BY_EXACT_SELECTED_SPAN]");
            if ( env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            p.println("[ORACLE_NLP_for_WholeWord]");
            if ( env.Parameters.Oracle.search_matchWholeWord )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            p.println("[GRAPHICPATH_ENABLED]");
            if ( env.Parameters.enabled_GraphPath_Display )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            p.println("[DIFFERENCE_MATCHING_ENABLED]");
            if ( env.Parameters.enabled_Diff_Display )
                p.println("YES");
            else
                p.println("NO");
            p.println("");



            // parameters for difference matching
            p.println("[DIFFERENCE_MATCHING_CROSS]");
            if ( env.Parameters.DifferenceMatching.checkCrossSpan )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            p.println("[DIFFERENCE_MATCHING_SAME]");
            if ( env.Parameters.DifferenceMatching.checkSameOverlappingSpan )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            /**use dictionary of stop words to prevent unexpected annotations
             * occurred in the results of NLP by remove them from pre-annotated
             * concepts dictionaries.
             */
            p.println("[NLP_USE_STOPWORDS]");
            if ( env.Parameters.nlp_dictionary_proc.using_StopWords )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            /**export verify suggestions to XML or not*/
            p.println("[OUTPUT_SUGGESTIONS_TOXML]");
            if ( env.Parameters.Output_XML.output_verify_suggestions_toXML )
                p.println("YES");
            else
                p.println("NO");
            p.println("");


            // write to confile about the flag of multiple dictionaries weight
            // value = true, if there have different weight, string in ini = "TRUE"
            p.println("[PRE_ANNOTATED_CONCEPT_DICTIONARIES_HAVE_DIFFERENT_WEIGHT]");
            if ( env.Parameters.Pre_Defined_Dictionary_DifferentWeight )
                p.println("TRUE");
            else
                p.println("FALSE");
            p.println("");

           


            int size = 0 ;
            // input files in using
            
            
            size = env.Parameters.corpus.LIST_ClinicalNotes.size();
            if (size > 0)
            {
                for (int i = 0; i < size; i++)
                {
                    p.println("[INPUTFILES]");
                    p.println(env.Parameters.corpus.LIST_ClinicalNotes.get(i).filename);
                    str = String.valueOf(env.Parameters.corpus.LIST_ClinicalNotes.get(i).amountOfWords);
                    p.println(str);
                    p.println(env.Parameters.corpus.LIST_ClinicalNotes.get(i).absoluteFilename);
                    p.println();
                }
            }
            
            
            
            // save pre-annotated concept dictionaries from the configure file
            /** @para filename - file name with absolute path
             * @para weight - if same all will be 0, otherwise big number
             * means heavy weight
             * @para description
             * @para separator
             * @para number_of_valid_entries
             */
            try{
                size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
                if (size > 0)
                {
                    for (int i = 0; i < size; i++)
                    {
                        Object[] s = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                        p.println("[PRE_ANNOTATED_CONCEPT_DICTIONAIES]");
                        p.println(s[0]);
                        p.println(s[1]);
                        p.println(s[2]);
                        p.println(s[3]);
                        p.println(s[4]);
                        p.println("");
                    }
                }
            }catch(Exception ex){
                System.out.println("error 1102110153");
            }


            try{
                p.println("[ATTRIBUTES]");
                for(AttributeSchemaDef att: Parameters.AttributeSchemas.getAttributes())
                {
                   p.print("@@@@" + att.getName() + "---");
                   for(String entry: att.getAllowedEntries())
                   {
                      p.print(entry + "---");
                   }
                    //if(s != null && !s.equals(""))
                        //p.print("@@@@" + s);
                }
                p.println();
                
            }catch(Exception ex){
                System.out.println("error 1102110158");
            }

            try{
                p.println("[RELATIONSHIPS]");
                for(RelationshipDef s: Parameters.RelationshipSchemas.getRelationships())
                {

                    if(s != null && !s.getName().equals(""))
                        p.println(s.getWriteString());
                }
                p.println();
            }catch(Exception ex){
                System.out.println("error 1102110200");
            }

            try{
                p.println("[VERIFIER_DICTIONARIES]");
                size = env.Parameters.VERIFIER_DICTIONARIES.size();
                if (size > 0)
                {
                    for (int i = 0; i < size; i++)
                    {
                        dictionaries.VerifierDictionaryFormat toWrite = env.Parameters.VERIFIER_DICTIONARIES.get(i);
                        p.println(toWrite.toString());
                    }
                }
                p.println();
            }catch(Exception ex){
                System.out.println("error 1102110201");
            }

            try{
                p.println("[VERIFIER_COMPARE_SETTINGS]");
                p.println(env.Parameters.verifierComparisonSettings.configString());
                p.println();
            }catch(Exception ex){
                System.out.println("error 1102110202");
            }

            saveClassesSchema(p);
        

            // ##end## save and close
            p.close();

        }
        catch(Exception ex)
        {
            System.out.println("error 1102100106:: fail to save configure to disk."
                    + ex.toString() );
        }
    }


    /**save schema of classes into the configure file "projectschema.cfg"*/
    private void saveClassesSchema(PrintStream p){
        if (p==null)
        {
            log.LoggingToFile.log( Level.INFO, "~~~~ WARNING ~~~~ :: 1106291425 :: can not get"
                    + " access to printstream to write class information"
                    + " into the schema file of current project");
            return;
        }

        resultEditor.annotationClasses.Depot classdepot = new resultEditor.annotationClasses.Depot();
        Vector<resultEditor.annotationClasses.AnnotationClass> allClasses = classdepot.getAll();

        for( resultEditor.annotationClasses.AnnotationClass thisclass: allClasses ){
            try{
                String key = thisclass.annotatedClassName;
                if (( key == null )||(key.trim().length() < 1))
                    continue;
                
                p.println( "[CLASS]" );
                // output the name of class
                String outstr = key;
                p.println(outstr);
                // output the highlight color of this class
                if ( thisclass.backgroundColor != null ) {
                    p.println( "( " + thisclass.backgroundColor.getRed()
                            + ", " + thisclass.backgroundColor.getGreen()
                             + ", " + thisclass.backgroundColor.getBlue() + " )");
                } else {
                    p.println( "( -1, -1, -1 )");
                }

                p.println( thisclass.source + "\n" );
            }catch(Exception ex){
                log.LoggingToFile.log( Level.SEVERE,"~~~~ WARNING ~~~~ :: 1106291446 :: fail to"
                        + " save one class information into schema.");
            }
        }
    }
    
    


    
}

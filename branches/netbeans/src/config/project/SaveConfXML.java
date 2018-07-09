/*
 * SaveConfXML.java, Created on Dec 29, 2011
 */
package config.project;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import java.util.logging.Level;
import org.jdom.*;
import org.jdom.output.*;
import relationship.simple.dataTypes.AttributeSchemaDef;
import relationship.complex.dataTypes.RelationshipDef;
import resultEditor.annotationClasses.AnnotationClass;
import resultEditor.annotationClasses.Depot;

/**
 * This class has same function as the class "SaveConf.java". It's designed to
 * save configure information into a XML file for the specific project, usually
 * it's for your current project.
 *
 * @author      Jianwei "Chris" Leng
 * @version     1.0
 *
 * Created on Dec 29, 2011
 */
public class SaveConfXML {

    /**twin method of "saveConf". It's designed to output configure setting of
     * current project to a specific file. This one only have one parameter that
     * indicates where we should put these configure information into.
     *
     * @param   configureFile
     *          parameter that indicates where we should put these configure
     *          information into. It should be a XML file under the "config"
     *          folder of your curren eHOST project.
     */
    public void saveConf(File configureFile) throws Exception {
        saveConf(configureFile, null);
    }

    /**output configure setting of current project to a specific file.
     *
     * @param   configureFile
     *          parameter that indicates where we should put these configure
     *          information into. It should be a XML file under the "config"
     *          folder of your curren eHOST project.
     * 
     * @param   projectName
     *          the string of the name of your current eHOST project.
     */
    public void saveConf(File configureFile, String projectName) throws Exception {

        // validity checking
        if (configureFile == null) {
            throw new Exception("1112280506::null file name to output configure setting in XML!");
        }

        // create the XML head
        Element root = new Element("eHOST_Project_Configure"); //
        root.setAttribute("Version", "1.0");
        if ((projectName != null) && (projectName.trim().length() >= 1)) {
            root.setAttribute("project", projectName); // list the project name in XML if we have
        }
        Document doc_xml = new Document(root);

        // assemble the XML
        root = xmlAssembler(root);

        // **** output xml file to disk ****
        // XML storage processing: phycial writing
        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8"); // set XML encodeing
        format.setIndent("    ");
        XMLOutputter XMLOut = new XMLOutputter(format);
        // write to disk
        XMLOut.output(doc_xml, new FileOutputStream(configureFile));
    }

    /**Assembling each node for the XML document. It put each configure item
     * into format of XML node, and assemble the body of the XML document .
     *
     * @param   root
     *          the root node of the XML file
     */
    private Element xmlAssembler(Element root) {

        String str;

        // CONCEPT_LIB
        str = env.Parameters.CONCEPT_LIB;
        if ((str != null) && (str.trim().length() >= 1)) {
            Element conceptlib = new Element("Pre_Defined_Dictionary"); // CONCEPT_LIB
            conceptlib.setText(str.trim());
            root.addContent(conceptlib);
        }

        // CONCEPT_LIB_SEPARATOR
        // matching the item "[CONCEPT_LIB_SEPARTOR]" in INI configure file
        // (the old format)
        str = env.Parameters.CONCEPT_LIB_SEPARTOR;
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Separator_of_PreDefined_Dictionary"); // CONCEPT_LIB_SEPARTOR
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }


        // OUTPUT_CONTROL_CONCEPT_ENABLED
        str = env.Parameters.OUTPUT_CONTROL_CONCEPT_ENABLED;

        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Output_Control_Concept_Enabled"); // OUTPUT_CONTROL_CONCEPT_ENABLED
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES
        // these was used to extract each line as a clinical note from
        // a document, in which each line has an id number and contents.
        str = env.Parameters.COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES;
        if ((str != null) && (str.trim().compareTo("true") != 0)) {
            str = "false";
        }
        if (str == null) {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Handling_Text_Database"); // OUTPUT_CONTROL_CONCEPT_ENABLED
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // flag to enable/disable the oracle seeker to find simlar annotations
        if (env.Parameters.oracleFunctionEnabled) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("OracleFunction_Enabled"); // OUTPUT_CONTROL_CONCEPT_ENABLED
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // enabled_displayAttributeEditor
        if (env.Parameters.enabled_displayAttributeEditor) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("AttributeEditor_PopUp_Enabled");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }
        
        if (env.Parameters.OracleStatus.visible) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("OracleFunction"); 
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }
        

        // Flag that indicate whether we need to use exact span which user
        // slected on document viewer, or the span with corrected border by
        // detecting space or symbols, to build a new annotation.
        // Default is false.
        if (env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("AnnotationBuilder_Using_ExactSpan");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // to oracle function, this flag indicates whether we need to search
        // the whole word or just part of the span.
        // "[ORACLE_NLP_for_WholeWord]"
        if (env.Parameters.Oracle.search_matchWholeWord) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("OracleFunction_Using_WholeWord");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // should we show graphics path of relationship?
        // "[GRAPHICPATH_ENABLED]");
        if (env.Parameters.enabled_GraphPath_Display) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("GraphicAnnotationPath_Enabled");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // DIFFERENCE_MATCHING_ENABLED
        if (env.Parameters.enabled_Diff_Display) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Diff_Indicator_Enabled");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // DIFFERENCE_MATCHING_CROSS
        if (env.Parameters.DifferenceMatching.checkCrossSpan) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Diff_Indicator_Check_CrossSpan");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // "[DIFFERENCE_MATCHING_SAME]");
        if (env.Parameters.DifferenceMatching.checkSameOverlappingSpan) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Diff_Indicator_Check_Overlaps");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }


        // use dictionary of stop words to prevent unexpected annotations
        // occurred in the results of NLP by remove them from pre-annotated
        // concepts dictionaries.
        //
        // Old entry name in the ini format: "[NLP_USE_STOPWORDS]";
        if (env.Parameters.nlp_dictionary_proc.using_StopWords) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("StopWords_Enabled");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }


        // Export verify suggestions to XML or not.
        // Old entry name in the ini format: "[OUTPUT_SUGGESTIONS_TOXML]".
        if (env.Parameters.Output_XML.output_verify_suggestions_toXML) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Output_VerifySuggestions");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // Old entry name in the ini format:  "[PRE_ANNOTATED_CONCEPT_
        // DICTIONARIES_HAVE_DIFFERENT_WEIGHT]";
        if (env.Parameters.Pre_Defined_Dictionary_DifferentWeight) {
            str = "true";
        } else {
            str = "false";
        }
        if ((str != null) && (str.trim().length() >= 1)) {
            str = xmlStringProcessor(str);
            Element eleItem = new Element("Pre_Defined_Dictionary_DifferentWeight");
            eleItem.setText(str.trim());
            root.addContent(eleItem);
        }

        // Assembling the nodes of pre-defined dictionaries for the XML document
        // [PRE_ANNOTATED_CONCEPT_DICTIONAIES]
        root = xmlAssembler_addPreDicts(root);

        // Assembling the nodes of annotation-attributes for the XML document.
        root = xmlAssembler_addAttributes(root);

        // Assembling the nodes of Relationship Rules for the XML document.
        root = xmlAssembler_addRelationshipRules(root);

        // Assembling the nodes of classes
        root = xmlAssembler_addClasses(root);

        return root;
    }

    /**Assembling the nodes of pre-defined dictionaries for the XML document. 
     *
     * @param   root
     *          the root node of the XML file
     */
    private Element xmlAssembler_addPreDicts(Element root) {
        Element preDicts = new Element("PreAnnotated_Dictionaries");
        preDicts.setAttribute("Owner", "NLP_Assistant");
        int size = 0;
        // save pre-annotated concept dictionaries from the configure file

        //[PRE_ANNOTATED_CONCEPT_DICTIONAIES]
        try {
            size = env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    Object[] s = (Object[]) env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.get(i);
                    /*
                     *  filename    - file name with absolute path
                     *  weight      - if same all will be 0, otherwise big number
                     *              means heavy weight
                     *  description
                     *  separator
                     *  number_of_valid_entries
                     */
                    String filename = (String) s[0];
                    String weight = (String) s[1];
                    String description = (String) s[2];
                    String separator = (String) s[3];
                    String length = (String) s[4];

                    Element preDict = new Element("PreAnnotated_Dict");

                    // filename of the pre-annotated dictionary
                    if ((filename == null) || (filename.trim().length() < 1)) {
                        continue;
                    }
                    Element node_filename = new Element("Dictionary");
                    node_filename.setText(filename);
                    preDict.addContent(node_filename);

                    // weight of this dictionary
                    if ((weight != null) && (weight.trim().length() > 0)) {
                        Element node_weight = new Element("Weight");
                        node_weight.setText(weight);
                        preDict.addContent(node_weight);
                    }

                    // description of this dictionary
                    if ((description != null) && (description.trim().length() > 0)) {
                        Element node_description = new Element("Description");
                        node_description.setText(description);
                        preDict.addContent(node_description);
                    }

                    // separator of this dictionary
                    if ((description != null) && (description.trim().length() > 0)) {
                        Element node_separator = new Element("Separator");
                        separator = xmlStringProcessor(separator);
                        node_separator.setText(separator);
                        preDict.addContent(node_separator);
                    }

                    // description of this dictionary
                    if ((description != null) && (description.trim().length() > 0)) {
                        Element node_length = new Element("Number_of_Entries");
                        node_length.setText(length);
                        preDict.addContent(node_length);
                    }

                    // record this entry
                    preDicts.addContent(preDict);
                }
            }
        } catch (Exception ex) {
            System.out.println("error 1102110153");
        }

        // record changes
        root.addContent(preDicts);

        return root;
    }

    /**Assembling the nodes of annotation-attributes for the XML document.
     *
     * @param   root
     *          the root node of the XML file
     */
    private Element xmlAssembler_addAttributes(Element root) {
        Element attributes = new Element("attributeDefs");

        try {

            for (AttributeSchemaDef att : env.Parameters.AttributeSchemas.getAttributes()) {
                if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                    continue;
                }
                Element attribute = new Element("attributeDef");
                Element attributeName = new Element("Name");
                attributeName.setText(att.getName());
                attribute.addContent(attributeName);
                
                // NLabel for UMLS node
                Element attIsUMLS_1 = new Element("is_Linked_to_UMLS_CUICode_and_CUILabel");
                if( att.isCUICodeNLabel )
                    attIsUMLS_1.setText( "true" );
                else
                    attIsUMLS_1.setText( "false" );
                attribute.addContent(attIsUMLS_1);
                
                Element attIsUMLS_2 = new Element("is_Linked_to_UMLS_CUICode");
                if( att.isCUICode )
                    attIsUMLS_2.setText( "true" );
                else
                    attIsUMLS_2.setText( "false" );
                attribute.addContent(attIsUMLS_2);
                
                Element attIsUMLS_3 = new Element("is_Linked_to_UMLS_CUILabel");
                if( att.isCUILabel )
                    attIsUMLS_3.setText( "true" );
                else
                    attIsUMLS_3.setText( "false" );
                attribute.addContent(attIsUMLS_3);
                
                
                //
                if (att.hasDefaultValue()) {
                    Element attDefault = new Element("defaultValue");
                    attDefault.setText(att.getDefault());
                    attribute.addContent(attDefault);
                }
                
                
                
                


                for (String entry : att.getAllowedEntries()) {
                    if ((entry == null) || (entry.trim().length() < 1)) {
                        continue;
                    }

                    Element value = new Element("attributeDefOptionDef");
                    value.setText(entry);
                    attribute.addContent(value);
                }

                attributes.addContent(attribute);
            }


        } catch (Exception ex) {
            System.out.println("error 1102110158");
        }

        root.addContent(attributes);
        return root;
    }

    /**Assembling the nodes of relationship-rules for the XML document.
     *
     * @param   root
     *          the root node of the XML file
     */
    private Element xmlAssembler_addRelationshipRules(Element root) {
        Element rules = new Element("Relationship_Rules");

        try {

            for (RelationshipDef s : env.Parameters.RelationshipSchemas.getRelationships()) {

                if ((s == null) || (s.getName() == null) || (s.getName().trim().length() < 1)) {
                    continue;
                }

                Element rule = new Element("Relationship_Rule");
                Element rulename = new Element("Name");
                rulename.setText(s.getName());
                rule.addContent(rulename);



                Element definition = new Element("Definition");
                definition.setText(s.getAllowed());
                rule.addContent(definition);

                if (s.getAttributes() != null) {
                    if (s.getAttributes().getAttributes() != null) {
                        Element attributes = new Element("attributeDefs");
                        for( AttributeSchemaDef aad : s.getAttributes().getAttributes() )
                        {
                            if( aad == null )
                                continue;
                            if( ( aad.getName() == null ) || ( aad.getName().trim().length() < 1 ) )
                                continue;
                            
                            Element attribute = new Element("attributeDef");
                            Element attname = new Element("Name");
                            attname.setText(aad.getName());
                            attribute.addContent(attname);
                            
                            if (aad.hasDefaultValue()) {
                                Element attDefault = new Element("defaultValue");
                                attDefault.setText(aad.getDefault());
                                attribute.addContent(attDefault);
                            }
                            
                            // record option value (attributeDefOptionDef)
                            Vector<String> values = aad.getAllowedEntries();
                            if( values != null){
                                for(String value : values ){
                                    Element element_value = new Element("attributeDefOptionDef");
                                    element_value.setText( value );
                                    attribute.addContent( element_value );
                                }
                            }

                            // record this attribute into the list of attributes
                            attributes.addContent(attribute);
                        }
                        
                        // record these attributes
                        rule.addContent(attributes);
                    }
                }

                rules.addContent(rule);
            }


        } catch (Exception ex) {
            System.out.println("error 1102110158");
        }

        root.addContent(rules);
        return root;
    }

    /**Assembling the nodes of relationship-rules for the XML document.
     *
     * @param   root
     *          the root node of the XML file
     */
    private Element xmlAssembler_addClasses(Element root) {
        Element classes = new Element("classDefs");

        // resultEditor.annotationClasses.Depot
        Depot classdepot = new Depot();
        Vector<AnnotationClass> allClasses = classdepot.getAll();

        for (resultEditor.annotationClasses.AnnotationClass thisclass : allClasses) {
            try {
                
                // record class names
                String key = thisclass.annotatedClassName;
                if ((key == null) || (key.trim().length() < 1)) {
                    continue;
                }
                Element classnode = new Element("classDef"); // "[CLASS]"
                Element classname_node = new Element("Name"); // [CLASSNAME]
                classname_node.setText(key);
                classnode.addContent(classname_node);

                if(( thisclass.shortComment != null ) && ( thisclass.shortComment.trim().length() > 0) ){
                    Element ele_shortcomment = new Element("ShortComment");
                    ele_shortcomment.setText(  thisclass.shortComment.trim() );
                    classnode.addContent(ele_shortcomment);
                }
                
                if(( thisclass.des != null ) && ( thisclass.des.trim().length() > 0) ){
                    Element ele_des = new Element("DescriptionDoc");
                    ele_des.setText(  thisclass.des.trim() );
                    classnode.addContent(ele_des);
                }
                
                
                // output the highlight color of this class
                Element color_r = new Element("RGB_R");
                color_r.setText( String.valueOf( thisclass.backgroundColor.getRed() ) );
                classnode.addContent(color_r);
                Element color_g = new Element("RGB_G");
                color_g.setText( String.valueOf( thisclass.backgroundColor.getGreen() ) );
                classnode.addContent(color_g);
                Element color_b = new Element("RGB_B");
                color_b.setText( String.valueOf( thisclass.backgroundColor.getBlue() ) );
                classnode.addContent(color_b);
//                Element color_d = new Element("tests");
//                color_b.setText( "0x" + String.valueOf( thisclass.backgroundColor.getRed() )  );
//                classnode.addContent(color_d);

                Element element_inherit = new Element("InHerit_Public_Attributes");
                if( thisclass.inheritsPublicAttributes ){
                    element_inherit.setText("true");                    
                }else
                    element_inherit.setText("false");
                classnode.addContent(element_inherit);

                XMLassembler_addClassAttributes( thisclass, classnode );

                if( ( thisclass.source != null )
                        && ( thisclass.source.trim().length() > 0 ) ) {
                    Element source = new Element("Source");
                    source.setText( thisclass.source );
                    classnode.addContent( source );
                }

                classes.addContent( classnode );

            } catch (Exception ex) {
                log.LoggingToFile.log(Level.SEVERE, "~~~~ WARNING ~~~~ :: 1106291446 :: fail to"
                        + " save one class information into schema.");
            }
        }

        root.addContent(classes);
        return root;
    }

    public Element XMLassembler_addClassAttributes(
            resultEditor.annotationClasses.AnnotationClass thisclass,
            Element classnode){
        
        if( ( thisclass.privateAttributes != null ) &&
                ( thisclass.getNumberOfAttributes() > 0 ) ){

            for (AttributeSchemaDef att : thisclass.getAttributes() ) {
                if ((att.getName() == null) || (att.getName().trim().length() < 1)) {
                    continue;
                }

                Element attribute = new Element("attributeDef");
                Element attributeName = new Element("Name");
                attributeName.setText(att.getName());
                attribute.addContent(attributeName);
                
                Element attIsCUICodeNLabel = new Element("is_Linked_to_UMLS_CUICode_and_CUILabel");
                if( att.isCUICodeNLabel )
                    attIsCUICodeNLabel.setText("true");
                else
                    attIsCUICodeNLabel.setText("false");
                attribute.addContent(attIsCUICodeNLabel);
                
                Element attIsCUICode = new Element("is_Linked_to_UMLS_CUICode");
                if( att.isCUICode )
                    attIsCUICode.setText("true");
                else
                    attIsCUICode.setText("false");
                attribute.addContent(attIsCUICode);
                
                Element attIsCUILabel = new Element("is_Linked_to_UMLS_CUILabel");
                if( att.isCUILabel )
                    attIsCUILabel.setText("true");
                else
                    attIsCUILabel.setText("false");
                attribute.addContent(attIsCUILabel);
                
                if (att.hasDefaultValue()) {
                    Element attDefault = new Element("defaultValue");
                    attDefault.setText(att.getDefault());
                    attribute.addContent(attDefault);
                }


                for (String entry : att.getAllowedEntries()) {
                    if ((entry == null) || (entry.trim().length() < 1)) {
                        continue;
                    }

                    Element value = new Element("attributeDefOptionDef");
                    value.setText(entry);
                    attribute.addContent(value);
                }

                classnode.addContent(attribute);
            }
        }
        return classnode;
    }
    /**some text, such as a character of bracket,  can't be stored correct in
     * XML files. This method checks texts and use transfered meaning string to
     * replace.*/
    public String xmlStringProcessor(String text) {

        // String transferedMeaningCharacter = "＆LT;";

        if ((text == null) || (text.trim().length() < 1)) {
            return text;
        }
        text.replaceAll("<", "＆LT;<");
        text.replaceAll(">", "＆LT;>");

        return text;
    }

    /**encoding a Color data into hex coding string. */
    public static String toHexEncoding(Color color) {
        String R, G, B;
        StringBuffer sb = new StringBuffer();

        R = Integer.toHexString(color.getRed());
        G = Integer.toHexString(color.getGreen());
        B = Integer.toHexString(color.getBlue());

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        sb.append("0x");
        sb.append(R);
        sb.append(G);
        sb.append(B);

        return sb.toString();
    }
}

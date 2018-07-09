/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config.project;

import config.Block;
import env.Parameters;
import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import relationship.complex.dataTypes.RelationshipDef;
import relationship.simple.dataTypes.AttributeSchemaDef;
import resultEditor.annotationClasses.AnnotationClass;

/**
 *
 * @author leng
 */
public class ProjectConf {

    /**the file name(not absolutely) of the configure file to each project*/
    protected final String projectfilename = "projectschema.cfg";
    protected final String configXML = "projectschema.xml";
    /**Current project*/
    protected File __currentProject = null;

    /**class contracture*/
    public ProjectConf(File _project) {
        this.__currentProject = _project;
    }

    /**save configure to disk for current project*/
    public void saveConfigure() {
        try {
            // quit if curent project is unreachable
            if (configXML == null) {
                log.LoggingToFile.log(Level.SEVERE, "error 1102091124::fail to get the project");
                return;
            }

            // reach the config file to current project
            File toConfig_File = ensureProjectConfFile(__currentProject);
            if (toConfig_File == null) {
                log.LoggingToFile.log(Level.SEVERE, "error 1102091126A::fail to reach the config file to the project");
                return;
            }


            // write configure information into the XML
            SaveConfXML saveconfxml = new SaveConfXML();
            saveconfxml.saveConf(toConfig_File);

            // disable begin
            // 
            // Jianwei Leng, Dec 30, 2011.
            //
            // following codes were used to save configure information of current
            // project into a text file. It's disabled after we decided to switch
            // to the XML format.
            /*
            SaveConf saveconf = new SaveConf();
            saveconf.save(config);
             *
             */
            // disable end


        } catch (Exception ex) {
            // MUSTMUST
        }
    }

    public void rename() {

        try {

            if (!__currentProject.exists()) {
                return;
            }

            String str_configPath = __currentProject.getAbsolutePath() + File.separator + "config";

            File configPath = new File(str_configPath);

            if (!configPath.exists()) {
                return;
            }

            String oldConfigure = str_configPath + File.separator + this.projectfilename;

            File oldConfigureFile = new File(oldConfigure);

            if (oldConfigureFile.exists()) {
                oldConfigureFile.renameTo(new File(str_configPath + File.separator + "backup.cfg"));
            }


        } catch (Exception ex) {
        }

    }

    /**Load project setting variables of eHOST from disk to memory.*/
    public void loadConfigure() {
        // quit if curent project is unreachable
        if (__currentProject == null) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091124::fail to get the project");
            return;
        }

        // reach the config file to current project
        File config = getProjectConfFile(__currentProject, false);
        if (config == null) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091126::fail to reach the config file to the project");
            return;
        }
        // split configure file into blocks
        config.Extracter extracter = new config.Extracter(config);
        Vector<Block> blocks = extracter.getBlocks();

        // remove all current markables
        resultEditor.annotationClasses.Depot.clear();

        // load variable from block into memory       
        ParameterGather parameterloader = new ParameterGather();
        parameterloader.load(blocks);
    }

    /**Load project setting variables of eHOST from disk to memory.*/
    public void loadXmlConfigure() {


        // remove all current markables
        resultEditor.annotationClasses.Depot.clear();
        env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.clear();
        Parameters.AttributeSchemas.clear();
        Parameters.RelationshipSchemas.clear();

        // quit if curent project is unreachable
        if (__currentProject == null) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091124::fail to get the project");
            return;
        }

        // reach the config file to current project
        //System.out.println("__currentProject = " + __currentProject );
        File config = getProjectConfFile(__currentProject, true);
        //System.out.println("configure = " + config );
        
        if (config == null) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091126::fail to reach the config file to the project");
            return;
        }


        try {
            // initialize for XML reader
            SAXBuilder sb = new SAXBuilder();
            //constract doc object
            Document doc = sb.build(config);
            // get the root node of the xml tree
            Element root = doc.getRootElement(); // get element of root

            String value = null;

            // get parameter value of "Pre_Defined_Dictionary"
            env.Parameters.CONCEPT_LIB = null;
            Element conceptlib = root.getChild("Pre_Defined_Dictionary");
            if (conceptlib != null) {
                value = conceptlib.getValue();
                env.Parameters.CONCEPT_LIB = value;
            }

            // get parameter value of "env.Parameters.CONCEPT_LIB_SEPARTOR"
            env.Parameters.CONCEPT_LIB_SEPARTOR = null;
            Element eleItem = root.getChild("Separator_of_PreDefined_Dictionary");
            if (eleItem != null) {
                value = eleItem.getValue();
                env.Parameters.CONCEPT_LIB_SEPARTOR = value;
            }

            // OUTPUT_CONTROL_CONCEPT_ENABLED
            env.Parameters.OUTPUT_CONTROL_CONCEPT_ENABLED = null;
            Element occe = root.getChild("Output_Control_Concept_Enabled");
            if (occe != null) {
                value = occe.getValue();
                env.Parameters.OUTPUT_CONTROL_CONCEPT_ENABLED = value;
            }

            // COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES
            // these was used to extract each line as a clinical note from
            // a document, in which each line has an id number and contents.
            env.Parameters.COMPLEX_NOTES_FORMAT_IN_PTID_NOTEID_NOTES = "false";

            // flag to enable/disable the oracle seeker to find simlar annotations
            env.Parameters.oracleFunctionEnabled = false;
            Element ofeItem = root.getChild("OracleFunction_Enabled");
            if (ofeItem != null) {
                value = ofeItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.oracleFunctionEnabled = true;
                    }
                }
            }
            
            
            env.Parameters.OracleStatus.visible = true;
            Element ofItem = root.getChild("OracleFunction");
            if (ofItem != null) {
                value = ofItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("false") == 0) {
                        env.Parameters.OracleStatus.visible = false;
                    }
                }
            }

            // enabled_displayAttributeEditor
            env.Parameters.enabled_displayAttributeEditor = false;
            Element aepeItem = root.getChild("AttributeEditor_PopUp_Enabled");
            if (aepeItem != null) {
                value = aepeItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.enabled_displayAttributeEditor = true;
                    }
                }
            }

            // Flag that indicate whether we need to use exact span which user
            // slected on document viewer, or the span with corrected border by
            // detecting space or symbols, to build a new annotation.
            // Default is false.
            env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = false;
            Element abuesItem = root.getChild("AnnotationBuilder_Using_ExactSpan");
            if (abuesItem != null) {
                value = abuesItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.CreateAnnotation.buildAnnotation_usingExactSpan = true;
                    }
                }
            }

            // to oracle function, this flag indicates whether we need to search
            // the whole word or just part of the span.
            // "[ORACLE_NLP_for_WholeWord]"
            env.Parameters.Oracle.search_matchWholeWord = false;
            Element ofuwwItem = root.getChild("OracleFunction_Using_WholeWord");
            if (ofuwwItem != null) {
                value = ofuwwItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.Oracle.search_matchWholeWord = true;
                    }
                }
            }

            // should we show graphics path of relationship?
            // "[GRAPHICPATH_ENABLED]");
            //env.Parameters.enabled_GraphPath_Display = false;
            //Element gapeItem = root.getChild("GraphicAnnotationPath_Enabled");
            //if (gapeItem != null) {
            //    value = gapeItem.getValue();
            //    if ((value != null) && (value.trim().length() > 0)) {
            //        if (value.trim().compareTo("true") == 0) {
            //            //env.Parameters.enabled_GraphPath_Display = true;
            //        }
            //    }
            //}


            // DIFFERENCE_MATCHING_ENABLED
            env.Parameters.enabled_Diff_Display = false;
            Element dieItem = root.getChild("Diff_Indicator_Enabled");
            if (dieItem != null) {
                value = dieItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.enabled_Diff_Display = true;
                    }
                }
            }


            // DIFFERENCE_MATCHING_CROSS
            env.Parameters.DifferenceMatching.checkCrossSpan = false;
            Element diccItem = root.getChild("Diff_Indicator_Check_CrossSpan");
            if (diccItem != null) {
                value = diccItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.DifferenceMatching.checkCrossSpan = true;
                    }
                }
            }

            // "[DIFFERENCE_MATCHING_SAME]");
            env.Parameters.DifferenceMatching.checkSameOverlappingSpan = false;
            Element dicoItem = root.getChild("Diff_Indicator_Check_Overlaps");
            if (dicoItem != null) {
                value = dicoItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.DifferenceMatching.checkSameOverlappingSpan = true;
                    }
                }
            }

            // use dictionary of stop words to prevent unexpected annotations
            // occurred in the results of NLP by remove them from pre-annotated
            // concepts dictionaries.
            //
            // Old entry name in the ini format: "[NLP_USE_STOPWORDS]";
            env.Parameters.nlp_dictionary_proc.using_StopWords = false;
            Element sweItem = root.getChild("StopWords_Enabled");
            if (sweItem != null) {
                value = sweItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.nlp_dictionary_proc.using_StopWords = true;
                    }
                }
            }

            // Export verify suggestions to XML or not.
            // Old entry name in the ini format: "[OUTPUT_SUGGESTIONS_TOXML]".
            env.Parameters.Output_XML.output_verify_suggestions_toXML = false;
            Element ovsItem = root.getChild("Output_VerifySuggestions");
            if (ovsItem != null) {
                value = ovsItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.Output_XML.output_verify_suggestions_toXML = true;
                    }
                }
            }

            // Old entry name in the ini format:  "[PRE_ANNOTATED_CONCEPT_
            // DICTIONARIES_HAVE_DIFFERENT_WEIGHT]";
            env.Parameters.Pre_Defined_Dictionary_DifferentWeight = false;
            Element pdddwItem = root.getChild("Pre_Defined_Dictionary_DifferentWeight");
            if (pdddwItem != null) {
                value = pdddwItem.getValue();
                if ((value != null) && (value.trim().length() > 0)) {
                    if (value.trim().compareTo("true") == 0) {
                        env.Parameters.Pre_Defined_Dictionary_DifferentWeight = true;
                    }
                }
            }


            // get the nodes of pre-defined dictionaries for the XML document
            // [PRE_ANNOTATED_CONCEPT_DICTIONAIES]
            getParas_PreDicts(root);

            // get the nodes of annotation-attributes for the XML document.
            getParas_Attributes(root);

            // get the nodes of Relationship Rules for the XML document.
            getParas_RelationshipRules(root);

            // get the nodes of classes
            getParas_Classes(root);


        } catch (Exception ex) {
            log.LoggingToFile.log(Level.WARNING, "1201030855::" + ex.getLocalizedMessage());
        }


        // load variable from block into memory
        //ParameterGather parameterloader = new ParameterGather();
        //parameterloader.load( blocks );

    }

    /**This method is designed to get the pre-annotated dictionaries from
     * the configure file in format of XML.
     *
     * @param   root
     *          the root node of the xml configure file. 
     */
    private void getParas_PreDicts(Element root) throws Exception {


        if (root == null) {
            throw new Exception("1201030936:: the root node couldn't be null!");
        }

        List list = root.getChildren("PreAnnotated_Dictionaries");

        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Element dictionaries = (Element) list.get(i);
            if (dictionaries == null) {
                continue;
            }

            List dicts = dictionaries.getChildren("PreAnnotated_Dict");
            if (dicts == null) {
                continue;
            }

            for (int j = 0; j < dicts.size(); j++) {
                Element dict = (Element) dicts.get(j);
                if (dict == null) {
                    continue;
                }

                Element node_filename = dict.getChild("Dictionary");
                Element node_weight = dict.getChild("Weight");
                Element node_description = dict.getChild("Description");
                Element node_separator = dict.getChild("Separator");
                Element node_length = dict.getChild("Number_of_Entries");

                if ((node_filename == null) || (node_separator == null)) {
                    continue;
                }

                String str_filename = node_filename.getValue();
                String str_weight = node_weight.getValue();
                String str_description = node_description.getValue();
                String str_separator = node_separator.getValue();
                String str_length = node_length.getValue();

                Object[] file = {
                    str_filename,
                    str_weight,
                    str_description,
                    str_separator,
                    str_length
                };
                env.Parameters.PREANNOTATED_CONCEPT_DICTIONARIES.add(file);
            }
        }
    }

    /**This method is designed to get the annotation attributes from
     * the configure file in format of XML.
     *
     * @param   root
     *          the root node of the xml configure file.
     */
    private void getParas_Attributes(Element root) throws Exception {



        if (root == null) {
            throw new Exception("1201030951:: the root node couldn't be null!");
        }

        List lists = root.getChildren("attributeDefs");

        if (lists == null) {
            return;
        }

        for (int i = 0; i < lists.size(); i++) {
            Element dictionaries = (Element) lists.get(i);
            if (dictionaries == null) {
                continue;
            }

            List dicts = dictionaries.getChildren("attributeDef");
            if (dicts == null) {
                continue;
            }

            for (int j = 0; j < dicts.size(); j++) {
                Element element_attribute = (Element) dicts.get(j);
                if (element_attribute == null) {
                    continue;
                }

                Element element_attributeName = element_attribute.getChild("Name");
                if (element_attributeName == null) {
                    continue;
                }
                String attributeName = element_attributeName.getValue();
                if ((attributeName == null) || (attributeName.trim().length() < 1)) {
                    continue;
                }

                List element_values = element_attribute.getChildren("attributeDefOptionDef");
                if (element_values == null) {
                    continue;
                }
                
                boolean isCUICodeNLabel = false;
                Element element_isUMLSCodeNLabel = element_attribute.getChild("is_Linked_to_UMLS_CUICode_and_CUILabel");
                if( element_isUMLSCodeNLabel != null ){
                    String is = element_isUMLSCodeNLabel.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUICodeNLabel = true;
                    }
                        
                }
                
                boolean isCUICode = false;
                Element element_isUMLSCode = element_attribute.getChild("is_Linked_to_UMLS_CUICode");
                if( element_isUMLSCode != null ){
                    String is = element_isUMLSCode.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUICode = true;
                    }
                        
                }
                
                boolean isCUILabel = false;
                Element element_isUMLSLabel = element_attribute.getChild("is_Linked_to_UMLS_CUILabel");
                if( element_isUMLSLabel != null ){
                    String is = element_isUMLSLabel.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUILabel = true;
                    }
                        
                }
                
              

                String defaultvalue = null;
                Element element_defaultValue = element_attribute.getChild("defaultValue");
                if (element_defaultValue != null) {
                    defaultvalue = element_defaultValue.getText();
                    if (defaultvalue != null) {
                        if (defaultvalue.trim().length()<1) {
                            defaultvalue = null;
                        }
                        
                        if(defaultvalue.trim().toLowerCase().compareTo("null") == 0)
                            defaultvalue = null;
                    }
                }

                Vector<String> allowable = new Vector<String>();
                for (int k = 0; k < element_values.size(); k++) {
                    Element element_value = (Element) element_values.get(k);
                    if (element_value != null) {
                        String value = element_value.getValue();
                        if ((value != null) && (value.trim().length() > 0)) {
                            allowable.add(value);
                        }
                    }
                }
                
                AttributeSchemaDef attSchema = new AttributeSchemaDef(attributeName, allowable);
                if( defaultvalue != null )
                    attSchema.setDefaultValue( defaultvalue );
                attSchema.isCUICodeNLabel = isCUICodeNLabel;
                attSchema.isCUICode = isCUICode;
                attSchema.isCUILabel = isCUILabel;
                Parameters.AttributeSchemas.Add( attSchema );
            }
        }
    }

    /**This method is designed to get the annotation attributes from
     * the configure file in format of XML.
     *
     * @param   root
     *          the root node of the xml configure file.
     */
    private void getParas_RelationshipRules(Element root) throws Exception {



        if (root == null) {
            throw new Exception("1201031016:: the root node couldn't be null!");
        }

        List rulelists = root.getChildren("Relationship_Rules");

        if (rulelists == null) {
            return;
        }

        for (int i = 0; i < rulelists.size(); i++) {
            Element rulesList = (Element) rulelists.get(i);
            if (rulesList == null) {
                continue;
            }

            List rules = rulesList.getChildren("Relationship_Rule");
            if (rules == null) {
                continue;
            }

            for (int j = 0; j < rules.size(); j++) {
                Element element_rule = (Element) rules.get(j);
                if (element_rule == null) {
                    continue;
                }
                Element element_rulename = element_rule.getChild("Name");
                Element element_ruledef = element_rule.getChild("Definition");

                if ((element_rulename == null) || (element_ruledef == null)) {
                    continue;
                }

                String rulename = element_rulename.getValue();
                String ruledef = element_ruledef.getValue();
                if ((rulename == null) || (rulename.trim().length() < 1)) {
                    continue;
                }
                if ((ruledef == null) || (ruledef.trim().length() < 1)) {
                    continue;
                }

                RelationshipDef relationship = new RelationshipDef(rulename, ruledef);


                relationship = extractAttributesOnRelationship(relationship, element_rule);

                Parameters.RelationshipSchemas.add(relationship);

            }
        }
    }

    private RelationshipDef extractAttributesOnRelationship(
            RelationshipDef relationship,
            Element element_rule) {
// attribtues on relationship" "attributeDefs" 
        Element attsroot = element_rule.getChild("attributeDefs");
        if (attsroot == null) {
            return relationship;
        }

        List attroot = attsroot.getChildren("attributeDef");
        if (attroot == null) {
            return relationship;
        }

        for (int t = 0; t < attroot.size(); t++) {
            Element att = (Element) attroot.get(t);
            if (att == null) {
                continue;
            }

            // get the name of this attribtue of relationship
            Element element_attname = att.getChild("Name");
            if (element_attname == null) {
                continue;
            }
            if ((element_attname.getText() == null) || (element_attname.getText().trim().length() < 1)) {
                continue;
            }

            List atts = att.getChildren("attributeDefOptionDef");
            if (atts == null) {
                continue;
            }
            
            String defaultvalue = null;
                Element element_defaultValue = att.getChild("defaultValue");
                if (element_defaultValue != null) {
                    defaultvalue = element_defaultValue.getText();
                    if (defaultvalue != null) {
                        if (defaultvalue.trim().length()<1) {
                            defaultvalue = null;
                        }
                        
                        if(defaultvalue.trim().toLowerCase().compareTo("null") == 0)
                            defaultvalue = null;
                    }
                }

            AttributeSchemaDef attschema = new AttributeSchemaDef();
            attschema.setName(element_attname.getText().trim());
            if( defaultvalue != null )
                attschema.setDefaultValue( defaultvalue );

            Vector<String> values = new Vector<String>();
            for (int h = 0; h < atts.size(); h++) {
                if (atts.get(h) == null) {
                    continue;
                }
                Element attvalue = (Element) atts.get(h);
                if (attvalue == null) {
                    continue;
                }
                if (attvalue.getText() == null) {
                    continue;
                }
                if (attvalue.getText().trim().length() < 1) {
                    continue;
                }

                values.add(attvalue.getText().trim());
            }
            if (values.size() > 0) {
                attschema.setAllowedEntries(values);
            }

            relationship.getAttributes().Add(attschema);
        }
        return relationship;
    }

    /**This method is designed to get the annotation classes from
     * the configure file in format of XML.
     *
     * @param   root
     *          the root node of the xml configure file.
     */
    private void getParas_Classes(Element root) throws Exception {



        if (root == null) {
            throw new Exception("1201031027:: the root node couldn't be null!");
        }

        List classlists = root.getChildren("classDefs");

        if (classlists == null) {
            return;
        }

        for (int i = 0; i < classlists.size(); i++) {
            Element classesList = (Element) classlists.get(i);
            if (classesList == null) {
                continue;
            }

            List classes = classesList.getChildren("classDef");
            if (classes == null) {
                continue;
            }

            for (int j = 0; j < classes.size(); j++) {
                Element element_class = (Element) classes.get(j);
                if (element_class == null) {
                    continue;
                }
                Element element_classname = element_class.getChild("Name");
                Element element_colorR = element_class.getChild("RGB_R");
                Element element_colorG = element_class.getChild("RGB_G");
                Element element_colorB = element_class.getChild("RGB_B");
                Element element_source = element_class.getChild("Source");
                Element element_shortComment = element_class.getChild("ShortComment");
                Element element_des = element_class.getChild("DescriptionDoc");
                Element element_isInherit = element_class.getChild("InHerit_Public_Attributes");

                if (element_classname == null) {
                    continue;
                }

                Vector<AttributeSchemaDef> privateAttributes = getAttributes( element_class );

                String classname = element_classname.getValue();

                String colorR = null,
                        colorB = null,
                        colorG = null,
                        source = null;
                
                String shortcomment = null;
                String desdoc = null;

                if (element_colorR != null) {
                    colorR = element_colorR.getValue();
                }
                if (element_colorG != null) {
                    colorG = element_colorG.getValue();
                }
                if (element_colorB != null) {
                    colorB = element_colorB.getValue();
                }
                if (element_source != null) {
                    source = element_source.getValue();
                }
                
                if( element_shortComment != null ){
                    shortcomment = element_shortComment.getValue();
                }
                if( element_des != null ){
                    desdoc = element_des.getValue();
                }

                if ((classname == null) || (classname.trim().length() < 1)) {
                    continue;
                }

                boolean isInherit = true;
                if( element_isInherit != null ){
                    String isInherit_str = element_isInherit.getText();
                    if( ( isInherit_str != null ) && ( isInherit_str.trim().length() > 0 ) ){
                        if( isInherit_str.trim().compareTo("false") == 0 )
                            isInherit = false;
                    }
                }

                //System.out.println("COLOR:: ---------> value22: " + classname + colorR + colorG + colorB + source);
                // checkInClass(classname, colorR, colorG, colorB, source, privateAttributes, isInherit );
                checkInClass(classname, colorR, colorG, colorB, source, privateAttributes, shortcomment, desdoc, isInherit );

            }
        }
    }

    public Vector<AttributeSchemaDef> getAttributes(Element element_class) {
        Vector<AttributeSchemaDef> toAttributes = new Vector<AttributeSchemaDef>();
        List attributes_list = element_class.getChildren("attributeDef");
        if (attributes_list != null) {

            // to each attribute
            for (Object obj : attributes_list) {

                // get name of this attribute
                Element element_attribute = (Element) obj;
                String name = element_attribute.getChildText("Name");
                if ((name == null) || (name.trim().length() < 1)) {
                    continue;
                }
                
                 
                  
                 
                boolean isCUICodeNLabel = false;
                Element element_isUMLSCode_1 = element_attribute.getChild("is_Linked_to_UMLS_CUICode_and_CUILabel");
                if( element_isUMLSCode_1 != null ){
                    String is = element_isUMLSCode_1.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUICodeNLabel = true;
                    }
                        
                }
                boolean isCUICode = false;
                Element element_isUMLSCode_2 = element_attribute.getChild("is_Linked_to_UMLS_CUICode");
                if( element_isUMLSCode_2 != null ){
                    String is = element_isUMLSCode_2.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUICode = true;
                    }
                        
                }
                boolean isCUILabel = false;
                Element element_isUMLSCode_3 = element_attribute.getChild("is_Linked_to_UMLS_CUILabel");
                if( element_isUMLSCode_3 != null ){
                    String is = element_isUMLSCode_3.getText();
                    if( (is!=null)&&(is.trim().length()>0) ){
                        if ( is.trim().compareTo( "true" ) == 0 )
                            isCUILabel = true;
                    }
                        
                }

                // get values
                Vector<String> values = new Vector<String>();
                List list_values = element_attribute.getChildren("attributeDefOptionDef");
                if (list_values != null) {                        
                    for (Object obj_value : list_values )  {
                        Element element_value = (Element) obj_value;
                        if(element_value == null)
                            continue;
                        String value = element_value.getText();
                        values.add(value);
                    }
                }
                
                String defaultvalue = null;
                Element element_defaultValue = element_attribute.getChild("defaultValue");
                if (element_defaultValue != null) {
                    defaultvalue = element_defaultValue.getText();
                    if (defaultvalue != null) {
                        if (defaultvalue.trim().length()<1) {
                            defaultvalue = null;
                        }
                        
                        if(defaultvalue.trim().toLowerCase().compareTo("null") == 0)
                            defaultvalue = null;
                    }
                }
                
                

                
                
                 AttributeSchemaDef attSchema = new AttributeSchemaDef(name, values);
                 attSchema.isCUICodeNLabel = isCUICodeNLabel;
                 attSchema.isCUICode = isCUICode;
                 attSchema.isCUILabel = isCUILabel;
                if( defaultvalue != null )
                    attSchema.setDefaultValue( defaultvalue );
                toAttributes.add( attSchema );
            }
            
        }
        return toAttributes;
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
    private void checkInClass(
            String _classname,
            String _colorR,
            String _colorG,
            String _colorB, 
            String _classComment,
            Vector<AttributeSchemaDef> privateAttributes,
            String shortcomment,
            String desdoc,
            boolean isInherit) {

        if ((_classname == null) || (_classname.trim().length() < 1)) {
            return;
        }

        if ((_colorR == null) || (_colorR.trim().length() < 1)) {
            _colorR = "0";
        }
        if ((_colorG == null) || (_colorG.trim().length() < 1)) {
            _colorG = "0";
        }
        if ((_colorB == null) || (_colorB.trim().length() < 1)) {
            _colorB = "0";
        }

        int r = 0, g = 0, b = 0;
        try {
            // try to get rbg color code
            r = Integer.valueOf(_colorR);
            g = Integer.valueOf(_colorG);
            b = Integer.valueOf(_colorB);
        } catch (Exception ex) {
            r = 0;
            g = 0;
            b = 0;
        }

        resultEditor.annotationClasses.Depot.addElement(
                _classname.trim(),
                _classComment,
                r,
                g,
                b, shortcomment, desdoc );

        resultEditor.annotationClasses.Depot classdepot =
                new resultEditor.annotationClasses.Depot();
        AnnotationClass ac = classdepot.getAnnotatedClass(_classname.trim());
        if(ac!=null){
            ac.privateAttributes.addAll(privateAttributes);
            ac.inheritsPublicAttributes = isInherit;
        }

    }

    private File ensureProjectConfFile(File _project) {
        try {
            // ##1## this is ensure that this project folder exists
            if (!projectFolderExists(_project)) {
                return null;
            }

            // ##2## this is to ensure there is a folder called "config" under
            // current project
            if (!configFolderExists(_project)) {
                String projectpath = _project.getAbsolutePath();
                String conffolderpath = projectpath + File.separator + "config";
                File conffolder = new File(conffolderpath);
                conffolder.mkdirs();
            }


            // ##3## get the config name
            String projectpath2 = _project.getAbsolutePath();
            String configfolder = projectpath2 + File.separator + "config";
            if ((configfolder == null) || (configfolder.trim().length() < 1)) {
                return null;
            }

            String configfile = configfolder + File.separator + this.configXML;

            File configure = new File(configfile);

            return configure;


        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091140:: fail to ensure the exists "
                    + "of configure file to current project."
                    + ex.toString());
        }

        return null;
    }

    public boolean foundOldConfigureFile() {
        try {

            if (!__currentProject.exists()) {
                return false;
            }

            String str_configPath = __currentProject.getAbsolutePath() + File.separator + "config";

            File configPath = new File(str_configPath);

            if (!configPath.exists()) {
                return false;
            }

            String potentialXML = str_configPath + File.separator + this.projectfilename;

            File xmlConfig = new File(potentialXML);

            return xmlConfig.exists();

        } catch (Exception ex) {
            return false;
        }
    }

    private File getProjectConfFile(File _project, boolean isXML) {

        try {
            
            // System.out.println("project=" + _project.getAbsolutePath() );

            // ##1## this is ensure that this project folder exists
            if (!projectFolderExists(_project)) {
                return null;
            }

            // ##2## this is to ensure there is a folder called "config" under
            // current project
            if (!configFolderExists(_project)) {
                return null;
            }

            // ##3## get the config name
            String configfolder = this.getConfigFolderName(_project);
            if ((configfolder == null) || (configfolder.trim().length() < 1)) {
                return null;
            }

            String configfile = configfolder + File.separator + this.projectfilename;
            if (isXML) {
                configfile = configfolder + File.separator + this.configXML;
            }

            File configure = new File(configfile);

            if (configure.exists()) {
                return configure;
            }


        } catch (Exception ex) {
            log.LoggingToFile.log(Level.SEVERE, "error 1102091041:: fail to get configure file to current project."
                    + ex.toString());
        }
        return null;
    }

    private boolean projectFolderExists(File _project) {
        try {
            // if the project is not exist
            if (_project == null) {
                return false;
            }

            if (!_project.exists()) {
                return false;
            }

            //if this project is not a directory
            if (!_project.isDirectory()) {
                return false;
            }

            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    private boolean configFolderExists(File _project) {

        try {
            String projectpath = _project.getAbsolutePath();
            String conffolderpath = projectpath + File.separator + "config";
            File conffolder = new File(conffolderpath);

            return conffolder.exists();

        } catch (Exception ex) {
            return false;
        }
    }

    private String getConfigFolderName(File _project) {
        String configpath = null;
        String projectpath = _project.getAbsolutePath();
        configpath = projectpath + File.separator + "config";

        return configpath;
    }
}

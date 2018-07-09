package gov.va.ehat;

import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.AnalyteDocument;
import gov.va.ehat.model.annotationAdminSchema.AnnotationAdminSchema;
import java.io.*;
import java.text.ParseException;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EHostToAnnotAdminTranslator {
	
	public static AnnotationAdminSchema schema;
	public static String annotAdminUserID;
	public static String annotAdminAnalyteRefID;
	public static String annotAdminAssignmentID;
	private static String annotatorName = "";
	private static String annotatorID;

	/**
	 * This routine will work on ONE analyte XML document from eHost at a time.
	 * @param knowtatorXMLFile - The saved XML file in eHost.
	 * @param annotationAdminSchemaServiceResponse - Raw text coming back from the REST request to AnnotationAdmin for the schema used. This expects to be given XML with a root element of <getSchema method='response'>
	 * @param annotationAdminAnalyteID - AnnotationAdmin's ID for the analyte document
	 * @param resultFile - Placeholder (file does NOT exist) for the resultant file. Can be null.
	 * @return - Returns raw text representing the generated XML.
	 * @throws TranslationException - If either XML cannot be parsed or the documents cannot be found.
	 */
	public static String convertAnnotationsInKnowtatorXMLToAnnotationAdminSubmitAnnotationsXML(
			File knowtatorXMLFile, 
			String annotationAdminSchemaServiceResponse, 
			String annotationAdminAssignmentID, 
			String annotationAdminAnalyteID, 
			String annotationAdminUserID, 
			File resultFile) throws TranslationException
	{
		annotAdminAssignmentID = annotationAdminAssignmentID;
		annotAdminUserID = annotationAdminUserID;
		annotAdminAnalyteRefID = annotationAdminAnalyteID;
		String rslt = null;
				
		SAXBuilder builder = new SAXBuilder();
		try {
			Document knowtatorDoc = builder.build(knowtatorXMLFile);
			if(knowtatorDoc==null)
			{
				throw new TranslationException("Knowtator XML Document could not be read: "+knowtatorXMLFile.getAbsolutePath());
			}
			Element eHostDocRoot = knowtatorDoc.getRootElement();
			if(eHostDocRoot==null)
			{
				throw new TranslationException("Knowtator XML Document could not be read: "+knowtatorXMLFile.getAbsolutePath());
			}
			StringReader annotationAdminSchemaServiceReader = new StringReader(annotationAdminSchemaServiceResponse);
			Document annotSchemaDoc = builder.build(annotationAdminSchemaServiceReader);
			if(annotSchemaDoc==null)
			{
				throw new TranslationException("Annotation Schema XML Response text could not be read: "+annotationAdminSchemaServiceResponse);
			}
			Element annotSchemaDocRoot = annotSchemaDoc.getRootElement();
			if(annotSchemaDocRoot==null)
			{
				throw new TranslationException("Knowtator XML Document could not be read: "+knowtatorXMLFile.getAbsolutePath());
			}
			
			buildSchemasFromAnnotationAdminSchemaServiceResponse(annotSchemaDocRoot);
			if(schema == null)
			{
				throw new TranslationException("I could not find any schemas within the given String: "+annotationAdminSchemaServiceResponse);
			}

			AnalyteDocument adoc = new AnalyteDocument();
			
			adoc.populateFromKnowtatorXMLRoot(eHostDocRoot, annotAdminAnalyteRefID, "" + schema.getId(), annotationAdminUserID);
			
			Document annotationAdminSubmitDoc = new Document();
			
			/*
			 * Wrap the XML for the analyte doc inside of a submit request.
			 */
			Element submitElement = new Element("submitAnnotations");
			submitElement.setAttribute("method","request");
			Namespace nmspace = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
			submitElement.addNamespaceDeclaration(nmspace);
			annotationAdminSubmitDoc.setRootElement(submitElement);
			
			submitElement.addContent(adoc.getAnnotationAdminRootElement(annotAdminAssignmentID));

//			annotationAdminSubmitDoc.setRootElement(adoc.getAnnotationAdminRootElement());
			
			Format format = Format.getCompactFormat();
			format.setEncoding("UTF-8"); // set XML encodeing
			format.setIndent("    ");
			XMLOutputter XMLOut = new XMLOutputter(format);
			
			rslt = XMLOut.outputString(annotationAdminSubmitDoc);
			
			/*
			 * Save the final XML to the file provided.
			 */
			if(resultFile!=null)
			{
				if(resultFile.exists())
				{
					if(!resultFile.delete())
					{
						throw new TranslationException("Could not remove old file: "+resultFile.getAbsolutePath());
					}
				}

				if(resultFile.createNewFile())
				{
					// write to disk
					XMLOut.output(annotationAdminSubmitDoc, new FileOutputStream( resultFile ));
				}
				else
				{

				}
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rslt;
	}
	
	public static String convertAnnotationAdminXMLToEHostXML(
			String annotationAdminAnnotationResponse, 
			String annotationAdminSchemaServiceResponse, 
			String eHostAnnotatorUserName, 
			String eHostAnnotatorUserID,
			File eHostDestinationFile) throws TranslationException
	{
		annotatorID = eHostAnnotatorUserID;
		annotatorName = eHostAnnotatorUserName;
		
		String rslt = null;
		
		SAXBuilder builder = new SAXBuilder();
		try {

			StringReader annotationAdminSchemaServiceReader = new StringReader(annotationAdminSchemaServiceResponse);
			Document annotSchemaDoc = builder.build(annotationAdminSchemaServiceReader);
			if(annotSchemaDoc==null)
			{
				throw new TranslationException("Annotation Schema XML Response text could not be read: "+annotationAdminSchemaServiceResponse);
			}

			Element annotSchemaDocRoot = annotSchemaDoc.getRootElement();
			if(annotSchemaDocRoot==null)
			{
				throw new TranslationException("Annotation Schema could not be read: "+annotationAdminSchemaServiceResponse);
			}
			
			buildSchemasFromAnnotationAdminSchemaServiceResponse(annotSchemaDocRoot);
			
			StringReader annotationAdminAnnotationServiceReader = new StringReader(annotationAdminAnnotationResponse);

			Document annotationXMLServiceResponse = builder.build(annotationAdminAnnotationServiceReader);
			if(annotationXMLServiceResponse==null)
			{
				throw new TranslationException("Annotation Schema XML Response text could not be read: "+annotationAdminAnnotationResponse);
			}
			Element annotationAdminAnnotationsDocRoot = annotationXMLServiceResponse.getRootElement();
			if(annotationAdminAnnotationsDocRoot.getName().equalsIgnoreCase("getAnnotations"))
			{
				annotationAdminAnnotationsDocRoot = annotationAdminAnnotationsDocRoot.getChild("annotations");
			}
			
			if(!annotationAdminAnnotationsDocRoot.getName().equalsIgnoreCase("annotations"))
			{
				throw new TranslationException("Annotations XML could not be read. Expecting top-level <annotations> element.");
			}
			/*
			 * Per Roman, this will be done for only one analyte at a time.
			 */
			AnalyteDocument adoc = new AnalyteDocument();
			
			adoc.populateFromAnnotationAdminXMLRoot(annotationAdminAnnotationsDocRoot);
			
			Element rootElement = adoc.getKnowtatorRootElementAfterBuildingFromAnnotationAdminXML();
			

			Document eHostResultDoc = new Document();				

			eHostResultDoc.setRootElement(rootElement);
			
			Format format = Format.getCompactFormat();
			format.setEncoding("UTF-8"); // set XML encodeing
			format.setIndent("    ");
			XMLOutputter XMLOut = new XMLOutputter(format);
			
			rslt = XMLOut.outputString(eHostResultDoc);
			/*
			 * Save the final XML to the file provided.
			 */
			if(eHostDestinationFile!=null)
			{
				if(eHostDestinationFile.exists())
				{
					if(!eHostDestinationFile.delete())
					{
						throw new TranslationException("Could not remove old file: "+eHostDestinationFile.getAbsolutePath());
					}
				}

				if(eHostDestinationFile.createNewFile())
				{
					// write to disk
					XMLOut.output(eHostResultDoc, new FileOutputStream(eHostDestinationFile));
				}
				else
				{

				}
			}
						
		} 
		catch (JDOMException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return rslt;
	}

	private static void buildSchemasFromAnnotationAdminSchemaServiceResponse(Element annotSchemaDocRoot) throws DataConversionException {
		if(!annotSchemaDocRoot.getName().equalsIgnoreCase("schema"))
		{
			annotSchemaDocRoot = annotSchemaDocRoot.getChild("schema");
		}
		
		if(annotSchemaDocRoot!=null)
		{
			schema = new AnnotationAdminSchema();
			schema.populateFromAnnotationAdminSchemaXMLElement(annotSchemaDocRoot);
		}
	}
	
	public static void main(String[] args)
	{
		try {
			runTests();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TranslationException e) {
			e.printStackTrace();
		}
	}
	
	private static void runTests() throws IOException, TranslationException
	{
		run_test_EHost_To_AnnotAdmin("eHostWithAttributes.xml", "getSchema.xml", "test_result.xml");
		run_test_AnnotAdmin_To_EHost("crDiscrimAnnotations.xml", "crDiscrimSchema.xml", "test_result_deux.xml");
	}
	
	private static void run_test_AnnotAdmin_To_EHost(String annotAdminFile, String annotAdminSchemaFile, String resultFile) throws TranslationException, IOException
	{
		File annotAdminXMLFile = new File(annotAdminFile);
		File eHostResultFile = new File(resultFile);
		
		if(eHostResultFile.exists())
		{
			if(!eHostResultFile.delete())
			{
				System.err.println("Test cannot run. File can't be deleted: "+eHostResultFile.getAbsolutePath());
			}
		}
		
		File schemaFile = new File(annotAdminSchemaFile);
		
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(schemaFile));
		int i = 0;
		while((i = reader.read()) >= 0)
		{
			builder.append((char)i);
		}
		reader.close();
		String serviceResponseText = builder.toString().trim();
		
		builder = new StringBuilder();
		reader = new InputStreamReader(new FileInputStream(annotAdminXMLFile));
		i = 0;
		while((i = reader.read()) >= 0)
		{
			builder.append((char)i);
		}
		reader.close();
		String crXML = builder.toString().trim();
/*
 * 
			String annotationAdminAnnotationResponse, 
			String annotationAdminSchemaServiceResponse, 
			String eHostAnnotatorUserName, 
			String eHostAnnotatorUserID,
			File eHostDestinationFile) throws TranslationException
 */
		convertAnnotationAdminXMLToEHostXML(crXML, serviceResponseText, "Tyler", "2", eHostResultFile);
	}

	private static void run_test_EHost_To_AnnotAdmin(String ehostFile, String annotAdminSchemaFile, String resultFile) throws IOException, TranslationException {

		File eHostXMLFile = new File(ehostFile);
		File annotationsResultFile = new File(resultFile);
		
		if(annotationsResultFile.exists())
		{
			if(!annotationsResultFile.delete())
			{
				System.err.println("Test cannot run. File can't be deleted: "+annotationsResultFile.getAbsolutePath());
			}
		}
		
		File serviceRespFile = new File(annotAdminSchemaFile);
		
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(serviceRespFile));
		int i = 0;
		while((i = reader.read()) >= 0)
		{
			builder.append((char)i);
		}
		reader.close();
		String serviceResponseText = builder.toString().trim();

		convertAnnotationsInKnowtatorXMLToAnnotationAdminSubmitAnnotationsXML(eHostXMLFile, serviceResponseText, "2001", "301", "201", annotationsResultFile);
	}

	public static String getAnnotatorName() {
		return annotatorName;
	}

	public static String getAnnotatorID() {
		// TODO Auto-generated method stub
		return annotatorID;
	}
}

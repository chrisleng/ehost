package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.ClassDef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.Element;

public abstract class Annotation {
	private String annotAdminTemporaryNumericalId = null;
	protected String annotAdminSchemaTextName = null;
	
	public String getAnnotAdminSchemaTextName() {
		return annotAdminSchemaTextName;
	}

	public String getAnnotAdminTemporaryNumericalId() {
		return annotAdminTemporaryNumericalId;
	}

	private Integer eHostTempId = null;
	void addClassReference(ClassDef cdef, String refValue) {
		AnnotationClassReference ref = new AnnotationClassReference();
		ref.setClassDefinition(cdef);
		ref.setValue(refValue);
		classRefs.add(ref);
	}
	
	public ArrayList<AnnotationClassReference> getClassRefs() {
		return classRefs;
	}
	
	public void setClassRefs(ArrayList<AnnotationClassReference> classRefs) {
		this.classRefs = classRefs;
	}

	private ArrayList<AnnotationClassReference> classRefs = new ArrayList<AnnotationClassReference>();
	private ArrayList<Span> spans = new ArrayList<Span>();
	private Date creationDate = null;
	/**
	 * AnnotationAdmin user ID
	 */
	private String userId = "";
	/**
	 * AnnotationAdmin analyteId
	 */
	private Integer analyteId = 0;
	private ArrayList<AnnotationAttribute> referencedAttributes = new ArrayList<AnnotationAttribute>();
	private Element generatedEHostXMLElement;
	
	public abstract int getAnnotationAdminTypeId();

	private static Integer nextId = Integer.valueOf(0);
	public static Integer getNextTempID() {
		return ++nextId;
	}

	protected static SimpleDateFormat getEHostSDF() {
		if(ehostDF==null)
		{
			ehostDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		}
		return ehostDF;
	}

	public Integer getEHostTempId() {
		if(eHostTempId == null || eHostTempId.equals(Integer.valueOf(0)))
		{
			Integer nextID = getNextTempID();
			/*
			 * Add processing to ensure it is a multiple of 10? Seems like that is what the submitAnnotations routine wants?
			 */
			while(!nextID.toString().endsWith("0"))
			{
				nextID = getNextTempID();
			}
			eHostTempId = nextID;
		}
		return eHostTempId;
	}

	private static SimpleDateFormat ehostDF = null;

	public Element getAnnotationAdminElement() {
		/*
		 * In this parent class we can build the standard <annotation> element and the <annotationAnalyteRefs> element.
		 */		
		Element annotationElement = new Element("annotation");
		annotationElement.setAttribute("tempid", getEHostTempId().toString());

		Element schemaElement = new Element("schema");
		schemaElement.setAttribute("id", EHostToAnnotAdminTranslator.schema.getId().toString());
		annotationElement.addContent(schemaElement);

		Date cd = getCreationDate();
		if(cd==null)
		{
			cd = GregorianCalendar.getInstance().getTime();
			setCreationDate(cd);
		}
		if(cd!=null)
		{
			Element creationDateElement = new Element("creationDate");
			creationDateElement.setText(DateUtil.getAnnotationAdminSDF().format(getCreationDate()));
			annotationElement.addContent(creationDateElement);
		}

		Element userIDElement = new Element("user");
		userIDElement.setAttribute("id",EHostToAnnotAdminTranslator.annotAdminUserID);
		annotationElement.addContent(userIDElement);
		
		Element annotationAnalyteRefs = new Element("annotationAnalyteRefs");	
		Element annotationAnalyteRefElement = new Element("annotationAnalyteRef");
		annotationAnalyteRefElement.setAttribute("tempid", getNextTempID().toString());
		Element analyteIdElement = new Element("analyte");
		analyteIdElement.setAttribute("id",EHostToAnnotAdminTranslator.annotAdminAnalyteRefID);
		annotationAnalyteRefElement.addContent(analyteIdElement);
		Element spansElement = getSpansElement();
		if(spansElement!=null)
		{
			annotationAnalyteRefElement.addContent(spansElement);
		}
		annotationAnalyteRefs.addContent(annotationAnalyteRefElement);		
		annotationElement.addContent(annotationAnalyteRefs);
		
		/*
		 * And finally, delegate to the child classes via the abstract method to get their particular flavor of the "features" element.
		 */
		annotationElement.addContent(getAnnotationAdminFeaturesElement());
		
		return annotationElement;
	}
	
	/**
	 * Pass in the <annotation> element and the AnalyteDocument that has thus far collected other annotations by ID. The AnalyteDocument contains annotations mapped by their ID's so they can be referenced in any dependencies in this element's features.
	 * @param al - <annotation> element
	 * @param doc - document built thus far, containing all possible dependencies for this analyte's features.
	 * @throws ParseException 
	 * @throws TranslationException 
	 */
	@SuppressWarnings("unchecked") 
	void readFromAnnotationAdminElement(Element al, AnalyteDocument doc) throws ParseException, TranslationException
	{
		annotAdminTemporaryNumericalId = al.getAttributeValue("id");

		Element creationDate = al.getChild("creationDate");
		if(creationDate!=null)
		{
			this.creationDate = DateUtil.getAnnotationAdminSDF().parse(creationDate.getValue());
		}
		Element user = al.getChild("user");
		if(user!=null)
		{
			userId = user.getValue();
		}

		Element annotationAnalyteRefs = al.getChild("annotationAnalyteRefs");
		if(annotationAnalyteRefs!=null)
		{
			Element annotationAnalyteRef = annotationAnalyteRefs.getChild("annotationAnalyteRef");
			if(annotationAnalyteRef!=null)
			{
				Element analyte = annotationAnalyteRef.getChild("analyte");
				analyteId = Integer.parseInt(analyte.getAttributeValue("id"));
				Element multipleSpanElements = annotationAnalyteRef.getChild("spans");
				if(multipleSpanElements!=null)
				{
					List<Element> spanElements = multipleSpanElements.getChildren("span");
					for(Element spanElement: spanElements)
					{
						Span span = new Span();
						span.setSpannedText(spanElement.getChildText("spannedText"));
						span.setStartOffset(Integer.parseInt(spanElement.getChild("startOffset").getValue()));
						span.setEndOffset(Integer.parseInt(spanElement.getChild("endOffset").getValue()));
						spans.add(span);
					}
				}
			}
		}
		
		Element featuresElement = al.getChild("features");
		if(featuresElement!=null)
		{
			Element featureElement = featuresElement.getChild("feature");
			if(featureElement!=null)
			{
				Element nameElement = featureElement.getChild("name");
				if(nameElement!=null)
				{
					annotAdminSchemaTextName = nameElement.getValue();
					/*
					 * Delegate subclass-specific bits to subclass implementation:
					 */
					readFromAnnotationAdminFeaturesElement(al, doc);
				}
			}
		}
	}
	
	/**
	 * CHART READER XML -- TO -- JAVA OBJECT
	 * Build the rest of the annotation (class, attribute, or relationship) from the features. 
	 * @param al - <annotation> element (features should be a child under this)
	 * @param doc - AnalyteDocument for the sake of having references to Annotations by ID as needed.
	 * @throws TranslationException 
	 */
	public abstract void readFromAnnotationAdminFeaturesElement(Element al, AnalyteDocument doc) throws TranslationException;
	
	/**
	 * JAVA OBJECT -- TO -- CHART READER XML
	 * @return
	 */
	public abstract Element getAnnotationAdminFeaturesElement();
	
	public Element getSpansElement() {
		if(spans.isEmpty())
		{
			return null;
		}
		Element spansElement = new Element("spans");
		for(Span span: spans)
		{
			Element spanElement = span.getAnnotationAdminElement();			
			spansElement.addContent(spanElement);
		}
		return spansElement;
	}

	public ArrayList<Span> getSpans() {
		return spans;
	}

	public void setSpans(ArrayList<Span> spans) {
		this.spans = spans;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAnalyteId() {
		return analyteId;
	}

	public void setAnalyteId(Integer analyteId) {
		this.analyteId = analyteId;
	}

	public void addReferencedAttribute(AnnotationAttribute att) {
		referencedAttributes.add(att);
	}
	
	public ArrayList<AnnotationAttribute> getReferencedAttributes()
	{
		return referencedAttributes;
	}

	public void setGeneratedEHostXMLElement(Element el) {
		generatedEHostXMLElement = el;
	}

	public Element getGeneratedEHostXMLElement() {
		return generatedEHostXMLElement;
	}

}

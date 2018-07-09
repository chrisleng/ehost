package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.model.annotationAdminSchema.DefaultDef;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class AdminAnnotation extends AdminObject
{
	protected Date creationDate = null;
	protected String userId;
	protected Double interAnnotatorAgreement = new Double(1.0);
	protected String analyteId; // We do not need to save annotationAnalyteRefs (like in the admin model), because an eHost annotation only references on document.  We just save one analyte id and its spans.
	protected List<Span> spans = new ArrayList<Span>();
	protected List<AdminFeature> features = new ArrayList<AdminFeature>();
	
	public AdminAnnotation(String mentionId, int type, Integer tempId, String analyteId, String schemaId, String userId)
	{
		super(mentionId, type, tempId, schemaId);
		this.analyteId = analyteId;
		this.userId = userId;
	}
	
	public Element getSchemaRefElement()
	{
		Element schemaRefElement = new Element("schemaRef");
		schemaRefElement.setAttribute("tempid", getNextTempId().toString());
		String uriStr = "schema:" + getSchemaId();
		if(schemaRefObj != null)
		{
			switch(type)
			{
				case AnnotationAdminConst.ANNOTATION_TYPE_CLASSIFICATION:
					uriStr += ";classDef:" + schemaRefObj.getAnnotationAdminId();
					break;
				case AnnotationAdminConst.ANNOTATION_TYPE_CLASSREL:
					uriStr += ";classRelDef:" + schemaRefObj.getAnnotationAdminId();
					break;
			}
		}
		schemaRefElement.setAttribute("uri", uriStr);
		schemaRefElement.setText(getName());
		return schemaRefElement;
	}
	
	public void populateFromEHostXMLElement(Element annotationElement) throws DataConversionException
	{
		if(annotationElement != null)
		{
			Element mentionElement = annotationElement.getChild("mention");		
			setMentionId(mentionElement.getAttributeValue("id"));
			
			Element spanElement = annotationElement.getChild("span");
			Span span = new Span();
			span.setStartOffset(spanElement.getAttribute("start").getIntValue());
			span.setEndOffset(spanElement.getAttribute("end").getIntValue());
			
			Element spannedTextElement = annotationElement.getChild("spannedText");
			span.setSpannedText(spannedTextElement.getValue());
			
			getSpans().add(span);
			
			Element creationDateElement = annotationElement.getChild("creationDate");
			String creationDateText = creationDateElement.getValue();
			Date creationDate = null;
			try {
				creationDate = DateUtil.getKnowtatorSDF().parse(creationDateText);
			} catch (ParseException e) {
				e.printStackTrace();
				creationDate = GregorianCalendar.getInstance().getTime();
			}
			setCreationDate(creationDate);
		}
	}

	public Element getAnnotationAdminElement()
	{
		Element annotationElement = new Element("annotation");
		annotationElement.setAttribute("tempid", getTempId().toString());
		
		Element nameElement = new Element("name");
		nameElement.setText(getName());
		annotationElement.addContent(nameElement);

		Element schemaElement = new Element("schema");
		schemaElement.setAttribute("id", getSchemaId());
		annotationElement.addContent(schemaElement);

		Date cd = getCreationDate();
		if(cd == null)
		{
			cd = GregorianCalendar.getInstance().getTime();
			setCreationDate(cd);
		}
		if(cd != null)
		{
			Element creationDateElement = new Element("creationDate");
			creationDateElement.setText(DateUtil.getAnnotationAdminSDF().format(getCreationDate()));
			annotationElement.addContent(creationDateElement);
		}

		Element userElement = new Element("user");
		userElement.setAttribute("id", getUserId());
		annotationElement.addContent(userElement);
		
		Element interAnnotatorAgreementElement = new Element("interAnnotatorAgreement");
		interAnnotatorAgreementElement.setText(getInterAnnotatorAgreement().toString());
		annotationElement.addContent(interAnnotatorAgreementElement);

		annotationElement.addContent(getSchemaRefElement());

		Element annotationAnalyteRefs = new Element("annotationAnalyteRefs");	
		Element annotationAnalyteRefElement = new Element("annotationAnalyteRef");
		annotationAnalyteRefElement.setAttribute("tempid", getNextTempId().toString());
		Element analyteIdElement = new Element("analyte");
		analyteIdElement.setAttribute("id", getAnalyteId());
		annotationAnalyteRefElement.addContent(analyteIdElement);
		Element spansElement = getSpansElement();
		if(spansElement!=null)
		{
			annotationAnalyteRefElement.addContent(spansElement);
		}
		annotationAnalyteRefs.addContent(annotationAnalyteRefElement);		
		annotationElement.addContent(annotationAnalyteRefs);

		annotationElement.addContent(getFeaturesElement());
		
		return annotationElement;
	}
	
	public Element getSpansElement()
	{
		Element spansElement = new Element("spans");
		for(Span span: spans)
		{
			Element spanElement = span.getAnnotationAdminElement();			
			spansElement.addContent(spanElement);
		}
		return spansElement;
	}

	public Element getFeaturesElement()
	{
		Element featuresElement = new Element("features");
		for(AdminFeature feature: features)
		{
			Element featureElement = feature.getAnnotationAdminElement();
			featuresElement.addContent(featureElement);
		}

		return featuresElement;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<Span> getSpans() {
		return spans;
	}

	public void setSpans(List<Span> spans) {
		this.spans = spans;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAnalyteId() {
		return analyteId;
	}

	public void setAnalyteId(String analyteId) {
		this.analyteId = analyteId;
	}

	public List<AdminFeature> getFeatures() {
		return features;
	}

	public void setFeatures(List<AdminFeature> features) {
		this.features = features;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getInterAnnotatorAgreement() {
		return interAnnotatorAgreement;
	}

	public void setInterAnnotatorAgreement(Double interAnnotatorAgreement) {
		this.interAnnotatorAgreement = interAnnotatorAgreement;
	}
}

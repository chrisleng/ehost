package gov.va.ehat.model;

import gov.va.ehat.EHostToAnnotAdminTranslator;
import gov.va.ehat.exception.TranslationException;
import gov.va.ehat.model.annotationAdminSchema.DefaultDef;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import org.jdom.DataConversionException;
import org.jdom.Element;

public class KnowtatorAnnotation extends KnowtatorObject
{
	protected String annotatorId;
	protected String annotator = "";
	protected Long spanStart = new Long(0);
	protected Long spanEnd = new Long(0);
	protected String spannedText = "";
	protected Date creationDate = null;
	protected Boolean processed = false;
	protected String adjudicationStatus = "EXCLUDED";
	protected List<KnowtatorObject> slotMentions = new ArrayList<KnowtatorObject>();
	
	protected String schemaRefObjName; // For use in constructing complexSlotMention - this is the mentionSlotId for those.
	
	protected Long leftAnnotationAdminId; // Used after all annotations have been processed to find a complexSlotMention's left annotation to add the complexSlotMention to its slotMentions list.
	
	public KnowtatorAnnotation(String mentionId, Long adminId, int type)
	{
		super(mentionId, adminId, type);
	}
	
	public void populateFromAnnotationAdminXMLElement(Element annotationElement) throws DataConversionException, TranslationException
	{
		if(annotationElement != null)
		{
			Element nameElement = annotationElement.getChild("name");
			String name = nameElement.getValue();
			
			Element schemaElement = annotationElement.getChild("schema");
			String schemaId = schemaElement.getAttributeValue("id");
			
			Element userElement = annotationElement.getChild("user");
			String userId = userElement.getAttributeValue("id");
			annotatorId = userId;
                        this.annotator = userElement.getText();
                        log.LoggingToFile.log(Level.SEVERE, "Annotator from Server=["+ this.annotator + "]");
                        
			
			Element interAnnotatorAgreementElement = annotationElement.getChild("interAnnotatorAgreement");
			Double interAnnotatorAgreement = new Double(interAnnotatorAgreementElement.getValue());
			
			Element schemaRefElement = annotationElement.getChild("schemaRef");
			String schemaRefObjName = schemaRefElement.getValue();
			DefaultDef schemaRefObj = EHostToAnnotAdminTranslator.schema.getClassDefsByName().get(schemaRefObjName);
			if(schemaRefObj == null)
			{
				schemaRefObj = EHostToAnnotAdminTranslator.schema.getClassRelDefsByName().get(schemaRefObjName);
			}
			setSchemaRefObjName(schemaRefObj.getName());
			
			Element annotationAnalyteRefsElement = annotationElement.getChild("annotationAnalyteRefs");
			Element annotationAnalyteRefElement  = annotationAnalyteRefsElement.getChild("annotationAnalyteRef");
			Element analyteElement = annotationAnalyteRefElement.getChild("analyte");
			String analyteId  = analyteElement.getAttributeValue("id");
			
			Element spansElement = annotationAnalyteRefElement.getChild("spans");
			Element spanElement  = spansElement.getChild("span");
			if(spanElement != null)
			{
				Element filterElement  = spanElement.getChild("filter");
				String filter = filterElement.getValue(); // Not used in ehost yet.
				
				Element startOffsetElement = spanElement.getChild("startOffset");
				String startOffsetStr  = startOffsetElement.getValue();
				Long startOffset = new Long(startOffsetStr);
				spanStart = startOffset;
				
				Element endOffsetElement = spanElement.getChild("endOffset");
				String endOffsetStr  = endOffsetElement.getValue();
				Long endOffset = new Long(endOffsetStr);
				spanEnd = endOffset;

				Element spannedTextElement = spanElement.getChild("spannedText");
				String spannedText  = spannedTextElement.getValue();
				this.spannedText = spannedText;	
                                log.LoggingToFile.log(Level.SEVERE, "SpanText=["+ this.spannedText + "] " + spanStart + ", " + spanEnd );
                                
			}
			Element creationDateElement = annotationElement.getChild("creationDate");
			String creationDateStr = creationDateElement.getValue();
			Date creationDate = GregorianCalendar.getInstance().getTime();
			try {
				creationDate = DateUtil.getAnnotationAdminSDF().parse(creationDateStr);
			} catch (ParseException e) {
				throw new TranslationException("Cannot translate creationDate " + creationDateStr);
			}
			this.creationDate = creationDate;
		}
	}

	public Element getAnnotationElement()
	{
		Element annotationElement = new Element("annotation");
		
		Element mentionElement = new Element("mention");
		mentionElement.setAttribute("id", getMentionId());
		annotationElement.addContent(mentionElement);
		
		Element annotatorElement = new Element("annotator");
		annotatorElement.setAttribute("id", getAnnotatorId());
		annotatorElement.setText(getAnnotator());
		annotationElement.addContent(annotatorElement);
		
		Element spanElement = new Element("span");
		spanElement.setAttribute("start", getSpanStart().toString());
		spanElement.setAttribute("end", getSpanEnd().toString());
		annotationElement.addContent(spanElement);
		
		Element spannedTextElement = new Element("spannedText");
		spannedTextElement.setText(getSpannedText());
		annotationElement.addContent(spannedTextElement);
		
		Date cd = getCreationDate();
		if(cd == null)
		{
			cd = GregorianCalendar.getInstance().getTime();
			setCreationDate(cd);
		}
		if(cd != null)
		{
			Element creationDateElement = new Element("creationDate");
			creationDateElement.setText(DateUtil.getKnowtatorSDF().format(getCreationDate()));
			annotationElement.addContent(creationDateElement);
		}

		Element processedElement = new Element("processed");
		processedElement.setText(getProcessed().toString());
		annotationElement.addContent(processedElement);
		
		Element adjudicationStatusElement = new Element("adjudication_status");
		adjudicationStatusElement.setText(getAdjudicationStatus());
		annotationElement.addContent(adjudicationStatusElement);
		
		return annotationElement;
	}

	public String getAnnotatorId() {
		return annotatorId;
	}

	public void setAnnotatorId(String annotatorId) {
		this.annotatorId = annotatorId;
	}

	public String getAnnotator() {
		return annotator;
	}

	public void setAnnotator(String annotator) {
		this.annotator = annotator;
	}

	public Long getSpanStart() {
		return spanStart;
	}

	public void setSpanStart(Long spanStart) {
		this.spanStart = spanStart;
	}

	public Long getSpanEnd() {
		return spanEnd;
	}

	public void setSpanEnd(Long spanEnd) {
		this.spanEnd = spanEnd;
	}

	public String getSpannedText() {
		return spannedText;
	}

	public void setSpannedText(String spannedText) {
		this.spannedText = spannedText;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

	public String getAdjudicationStatus() {
		return adjudicationStatus;
	}

	public void setAdjudicationStatus(String adjudicationStatus) {
		this.adjudicationStatus = adjudicationStatus;
	}

	public String getSchemaRefObjName() {
		return schemaRefObjName;
	}

	public void setSchemaRefObjName(String schemaRefObjName) {
		this.schemaRefObjName = schemaRefObjName;
	}

	public List<KnowtatorObject> getSlotMentions() {
		return slotMentions;
	}

	public void setSlotMentions(List<KnowtatorObject> slotMentions) {
		this.slotMentions = slotMentions;
	}

	public Long getLeftAnnotationAdminId() {
		return leftAnnotationAdminId;
	}

	public void setLeftAnnotationAdminId(Long leftAnnotationAdminId) {
		this.leftAnnotationAdminId = leftAnnotationAdminId;
	}
}

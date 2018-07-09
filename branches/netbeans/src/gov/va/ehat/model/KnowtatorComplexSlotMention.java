package gov.va.ehat.model;

import java.util.List;

import gov.va.ehat.exception.TranslationException;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class KnowtatorComplexSlotMention extends KnowtatorObject
{
	protected String mentionSlotId;
	protected String complexSlotMentionValue;
	
	protected Long relAnnotationAdminId; // Used to add this slot to the leftAnnotaion's slot mentions list after all annotations are processed.  The relAnnotaiton saves the leftAnnotationAdminId.
	protected Long rightAnnotationAdminId; // Used to add this slot to the rightAnnotation's slot mentions list after all annotations are processed.
	
	public KnowtatorComplexSlotMention(String mentionId)
	{
		super(mentionId, null, 0);  // No adminid or type for this object.
	}
	
	public void populateFromFeatureXMLElement(Element featureElement) throws DataConversionException, TranslationException
	{
		if(featureElement != null)
		{
			Element nameElement = featureElement.getChild("name");
			String name = nameElement.getValue();
			
			Element elementsElement = featureElement.getChild("elements");
			Element elementElement = elementsElement.getChild("element");
			Element valueElement = elementElement.getChild("value");
			String valueStr = valueElement.getValue();
			Long leftAnnotationAdminId = new Long(valueStr);
			setRightAnnotationAdminId(leftAnnotationAdminId);
		}
	}

	public Element getComplexSlotMentionElement()
	{
		Element complexSlotMentionElement = new Element("complexSlotMention");
		complexSlotMentionElement.setAttribute("id", getMentionId());
		
		Element mentionSlotElement = new Element("mentionSlot");
		mentionSlotElement.setAttribute("id", getMentionSlotId());
		complexSlotMentionElement.addContent(mentionSlotElement);
		
                String complexSlotMentionValue = getComplexSlotMentionValue();
                //if(( complexSlotMentionValue == null ) ||(complexSlotMentionValue.trim().length()<1))
                //    return null;
		Element complexSlotMentionValueElement = new Element("complexSlotMentionValue");
		complexSlotMentionValueElement.setAttribute("value", complexSlotMentionValue );
		complexSlotMentionElement.addContent(complexSlotMentionValueElement);
				
		return complexSlotMentionElement;
	}

	public String getMentionSlotId() {
		return mentionSlotId;
	}

	public void setMentionSlotId(String mentionSlotId) {
		this.mentionSlotId = mentionSlotId;
	}

	public String getComplexSlotMentionValue() {
		return complexSlotMentionValue;
	}

	public void setComplexSlotMentionValue(String complexSlotMentionValue) {
		this.complexSlotMentionValue = complexSlotMentionValue;
	}

	public Long getRightAnnotationAdminId() {
		return rightAnnotationAdminId;
	}

	public void setRightAnnotationAdminId(Long rightAnnotationAdminId) {
		this.rightAnnotationAdminId = rightAnnotationAdminId;
	}

	public Long getRelAnnotationAdminId() {
		return relAnnotationAdminId;
	}

	public void setRelAnnotationAdminId(Long relAnnotationAdminId) {
		this.relAnnotationAdminId = relAnnotationAdminId;
	}
}

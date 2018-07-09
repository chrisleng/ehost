package gov.va.ehat.model;

import gov.va.ehat.exception.TranslationException;

import org.jdom.DataConversionException;
import org.jdom.Element;

public class KnowtatorStringSlotMention extends KnowtatorObject
{
	protected String mentionSlotId;
	protected String stringSlotMentionValue;
	
	public KnowtatorStringSlotMention(String mentionId)
	{
		super(mentionId, null, 0);  // No adminid or type for this object.
	}
	
	public void populateFromFeatureXMLElement(Element featureElement) throws DataConversionException, TranslationException
	{
		if(featureElement != null)
		{
			Element nameElement = featureElement.getChild("name");
			String name = nameElement.getValue();
			
			Element schemaRefElement = featureElement.getChild("schemaRef");
			String schemaRefObjName = schemaRefElement.getValue();
			mentionSlotId = schemaRefObjName;
			
			Element elementsElement = featureElement.getChild("elements");
			Element elementElement = elementsElement.getChild("element");
			Element valueElement = elementElement.getChild("value");
			String value = valueElement.getValue();
			// TBD - For FEATURE_TYPE_ATTRIBUTE_OPTION, the value that should be saved is the schemaRefId of the attributeDefOptionDef chosen.
			this.stringSlotMentionValue = value;
		}
	}

	public Element getStringSlotMentionElement()
	{
		Element stringSlotMentionElement = new Element("stringSlotMention");
		stringSlotMentionElement.setAttribute("id", getMentionId());
		
		Element mentionSlotElement = new Element("mentionSlot");
		mentionSlotElement.setAttribute("id", getMentionSlotId());
		stringSlotMentionElement.addContent(mentionSlotElement);
		
		Element stringSlotMentionValueElement = new Element("stringSlotMentionValue");
		stringSlotMentionValueElement.setAttribute("value", getStringSlotMentionValue());
		stringSlotMentionElement.addContent(stringSlotMentionValueElement);
				
		return stringSlotMentionElement;
	}

	public String getMentionSlotId() {
		return mentionSlotId;
	}

	public void setMentionSlotId(String mentionSlotId) {
		this.mentionSlotId = mentionSlotId;
	}

	public String getStringSlotMentionValue() {
		return stringSlotMentionValue;
	}

	public void setStringSlotMentionValue(String stringSlotMentionValue) {
		this.stringSlotMentionValue = stringSlotMentionValue;
	}
}

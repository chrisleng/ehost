package gov.va.ehat.model;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class KnowtatorClassMention extends KnowtatorObject
{
	protected List<KnowtatorObject> hasSlotMentions = new ArrayList<KnowtatorObject>(); // of classMentions like stringSlotMention and complexSlotMention
	protected String mentionClassId;
	protected String mentionClassValue;
	
	public KnowtatorClassMention(String mentionId)
	{
		super(mentionId, null, 0);  // No adminid or type for this object.
	}
	
	public Element getClassMentionElement()
	{
		Element classMentionElement = new Element("classMention");
		classMentionElement.setAttribute("id", getMentionId());
		
		for(KnowtatorObject classMention: hasSlotMentions)
		{
			Element hasSlotMention = new Element("hasSlotMention");
			hasSlotMention.setAttribute("id", classMention.getMentionId());
			classMentionElement.addContent(hasSlotMention);
		}
		
		Element mentionClassElement = new Element("mentionClass");
		mentionClassElement.setAttribute("id", getMentionClassId());
		mentionClassElement.setText(getMentionClassValue());
		classMentionElement.addContent(mentionClassElement);

		return classMentionElement;
	}

	public List<KnowtatorObject> getHasSlotMentions() {
		return hasSlotMentions;
	}

	public void setHasSlotMentions(List<KnowtatorObject> hasSlotMentions) {
		this.hasSlotMentions = hasSlotMentions;
	}

	public String getMentionClassId() {
		return mentionClassId;
	}

	public void setMentionClassId(String mentionClassId) {
		this.mentionClassId = mentionClassId;
	}

	public String getMentionClassValue() {
		return mentionClassValue;
	}

	public void setMentionClassValue(String mentionClassValue) {
		this.mentionClassValue = mentionClassValue;
	}
}

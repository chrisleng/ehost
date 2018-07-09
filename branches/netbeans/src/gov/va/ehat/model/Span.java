package gov.va.ehat.model;

import org.jdom.Element;

public class Span {
	private Integer startOffset;
	private Integer endOffset;
	private String spannedText;
	public Integer getStartOffset() {
		return startOffset;
	}
	public void setStartOffset(Integer startOffset) {
		this.startOffset = startOffset;
	}
	public Integer getEndOffset() {
		return endOffset;
	}
	public void setEndOffset(Integer endOffset) {
		this.endOffset = endOffset;
	}
	public String getSpannedText() {
		return spannedText;
	}
	public void setSpannedText(String spannedText) {
		this.spannedText = spannedText;
	}
	public Element getAnnotationAdminElement() {
		Element el = new Element("span");
		Element filterElement = new Element("filter"); // Not used yet in eHost.
		el.setAttribute("tempid",AdminObject.getNextTempId().toString());
		Element startOffsetElement = new Element("startOffset");
		startOffsetElement.setText(startOffset.toString());
		Element endOffsetElement = new Element("endOffset");
		endOffsetElement.setText(endOffset.toString());
		Element spannedTextElement = new Element("spannedText");
		spannedTextElement.setText(spannedText);
		el.addContent(filterElement);
		el.addContent(startOffsetElement);
		el.addContent(endOffsetElement);
		el.addContent(spannedTextElement);
		return el;
	}
}

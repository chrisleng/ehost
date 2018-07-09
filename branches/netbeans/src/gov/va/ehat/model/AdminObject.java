package gov.va.ehat.model;

import gov.va.ehat.model.annotationAdminSchema.DefaultDef;

public abstract class AdminObject
{
	private static Integer nextTempId = Integer.valueOf(1);
	
	public static Integer getNextTempId()
	{
		return nextTempId++;
	}
	
	protected String mentionId;
	protected Integer tempId = new Integer(0);
	protected int type;
	protected String name;
	protected String schemaId;
	protected DefaultDef schemaRefObj;

	public AdminObject(String mentionId, int type, Integer tempId, String schemaId) {
		super();
		this.mentionId = mentionId;
		this.type = type;
		this.tempId = tempId;
		this.schemaId = schemaId;
	}

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public String getMentionId() {
		return mentionId;
	}

	public void setMentionId(String mentionId) {
		this.mentionId = mentionId;
	}

	public String getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public DefaultDef getSchemaRefObj() {
		return schemaRefObj;
	}

	public void setSchemaRefObj(DefaultDef schemaRefObj) {
		this.schemaRefObj = schemaRefObj;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

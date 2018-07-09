package gov.va.ehat.model;

import gov.va.ehat.model.annotationAdminSchema.DefaultDef;

public abstract class KnowtatorObject {
	private static Integer nextMentionIdNum = Integer.valueOf(1);
	
	public static Integer getNextMentionIdNum()
	{
		return nextMentionIdNum++;
	}
	
	public static String getNextMentionId()
	{
		return "EHOST_Instance_" + getNextMentionIdNum();
	}
	
	protected String mentionId;
	protected Long adminId;
	protected int type;
	protected String name;
	protected DefaultDef schemaRefObj;

	public KnowtatorObject(String mentionId, Long adminId, int type) {
		super();
		this.mentionId = mentionId;
		this.adminId = adminId;
		this.type = type;
	}

	public String getMentionId() {
		return mentionId;
	}

	public void setMentionId(String mentionId) {
		this.mentionId = mentionId;
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

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
}

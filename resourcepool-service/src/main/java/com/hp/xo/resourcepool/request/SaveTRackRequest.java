package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class SaveTRackRequest extends BaseEntityRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8233373481677914244L;

	@Parameter(name = "id", type = FieldType.LONG, description = "")
	private Long id;

	@Parameter(name = "name", type = FieldType.STRING, description = "")
	private String name;
	
	@Parameter(name = "serialnumber", type = FieldType.STRING, description = "")
	private String serialnumber;

	@Parameter(name = "uuid", type = FieldType.STRING, description = "")
	private String uuid;

	@Parameter(name = "status", type = FieldType.STRING, description = "")
	private Integer status;

	@Parameter(name = "saveStatus", type = FieldType.STRING, description = "")
	private Integer saveStatus;

	@Parameter(name = "oaip", type = FieldType.STRING, description = "")
	private String oaip;
	
	@Parameter(name = "type", type = FieldType.STRING, description = "")
	private String type;
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SaveTRackRequest(){
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public String getOaip() {
		return oaip;
	}

	public void setOaip(String oaip) {
		this.oaip = oaip;
	}

}

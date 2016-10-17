package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class ListTHostRequest extends BaseListRequest {

	@Parameter(name = "id", type = FieldType.LONG)
	private Long id;

	@Parameter(name = "hostName", type = FieldType.STRING)
	private String hostName;

	@Parameter(name = "saveStatus", type = FieldType.INTEGER)
	private Integer saveStatus;

	@Parameter(name = "type", type = FieldType.STRING)
	private String type;

	@Parameter(name = "resourcepoolid", type = FieldType.STRING)
	private String resourcePoolId;

	@Parameter(name = "status", type = FieldType.INTEGER)
	private Integer status;
	
	@Parameter(name = "serialnumber", type = FieldType.STRING)
	private String serialnumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public String getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(String resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	
}

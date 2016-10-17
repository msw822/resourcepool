package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class ListTRackRequest extends BaseListRequest {
	
	@Parameter(name="id",type=FieldType.LONG)
	private Long id;
	
	@Parameter(name="name",type=FieldType.STRING)
	private String name;
	
	@Parameter(name="saveStatus",type=FieldType.INTEGER)
	private Integer saveStatus;
	
	@Parameter(name="status",type=FieldType.INTEGER)
	private Integer status;
	
	@Parameter(name = "serialnumber", type = FieldType.STRING)
	private String serialnumber;
	

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

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	
}

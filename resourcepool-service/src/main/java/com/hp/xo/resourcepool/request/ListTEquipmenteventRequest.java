package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class ListTEquipmenteventRequest extends BaseListRequest {

	@Parameter(name="serialnumber", type=FieldType.STRING, description="")
	private String serialnumber;

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	
	@Parameter(name="eventstatus", type=FieldType.INTEGER, description="")
	private Integer eventstatus;

	public Integer getEventstatus() {
		return eventstatus;
	}

	public void setEventstatus(Integer eventstatus) {
		this.eventstatus = eventstatus;
	}
	
	
	
	
}

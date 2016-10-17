package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class ListWorkOrderReportRequest extends BaseListRequest {

	@Parameter(name = "workOrderType", type = FieldType.INTEGER, description = "")
	private Integer workOrderType;

	@Parameter(name = "workOrderTypeName", type = FieldType.STRING, description = "")
	private String workOrderTypeName;

	@Parameter(name = "status", type = FieldType.INTEGER, description = "")
	private Integer status;

	@Parameter(name = "startDate", type = FieldType.STRING, description = "")
	private String startDate;

	@Parameter(name = "endDate", type = FieldType.STRING, description = "")
	private String endDate;

	public Integer getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(Integer workOrderType) {
		this.workOrderType = workOrderType;
	}

	public String getWorkOrderTypeName() {
		return workOrderTypeName;
	}

	public void setWorkOrderTypeName(String workOrderTypeName) {
		this.workOrderTypeName = workOrderTypeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}

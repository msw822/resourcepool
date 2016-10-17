package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class PhysicsHostRequest extends BuessionListRequest{
    @Parameter(name="hostTypeStatus", type=FieldType.STRING, description="")
	private String hostTypeStatus;
    @Parameter(name="hostName", type=FieldType.STRING, description="")
  	private String hostName;
    @Parameter(name="orderId", type=FieldType.STRING, description="")
  	private String orderId;
    @Parameter(name="queryType", type=FieldType.STRING, description="")
  	private String queryType;
 	@Parameter(name="projectId", type=FieldType.STRING, description="")
  	private String projectId;
    
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostTypeStatus() {
		return hostTypeStatus;
	}

	public void setHostTypeStatus(String hostTypeStatus) {
		this.hostTypeStatus = hostTypeStatus;
	}
	public String getQueryType() {
			return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
}

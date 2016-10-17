package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class DimResourceTreeRequest extends BaseEntityRequest {
	@Parameter(name = "resourceId", type = FieldType.STRING, description = "")
	private String resourceId;

	@Parameter(name = "type", type = FieldType.STRING, description = "")
	private String type;

	@Parameter(name = "preResourceId", type = FieldType.STRING, description = "")
	private String preResourceId;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPreResourceId() {
		return preResourceId;
	}

	public void setPreResourceId(String preResourceId) {
		this.preResourceId = preResourceId;
	}

}

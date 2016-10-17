package com.hp.xo.resourcepool.request;

import java.util.Date;

import com.hp.xo.resourcepool.model.Parameter;

public abstract class BaseEntityRequest extends BaseRequest {
	@Parameter(name = "createdBy", type = FieldType.STRING)
	protected String createdBy;
	@Parameter(name = "createdOn", type = FieldType.DATE)
	protected Date createdOn;
	@Parameter(name = "modifiedBy", type = FieldType.STRING)
	protected String modifiedBy;
	@Parameter(name = "modifiedOn", type = FieldType.DATE)
	protected Date modifiedOn;

    // ///////////////////////////////////////////////////
    // ///////////////// Accessors ///////////////////////
    // ///////////////////////////////////////////////////

    public BaseEntityRequest() {
    	super();
    }

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	
}

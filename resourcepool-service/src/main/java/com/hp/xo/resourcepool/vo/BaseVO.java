package com.hp.xo.resourcepool.vo;

import java.io.Serializable;

import com.hp.xo.resourcepool.model.ResponseObject;


public abstract class BaseVO implements ResponseObject, Serializable {
	private static final long serialVersionUID = 3064655260099858115L;
	
	private transient String responseName;
    private transient String objectName;
    protected String jobId;
    private Integer jobStatus;
    
    public BaseVO() {
    	super();
    }
    
    public String getResponseName() {
		if (null == this.responseName) {
			return this.getClass().getSimpleName().toLowerCase();
		}
        return responseName;
    }

    public void setResponseName(String responseName) {
        this.responseName = responseName;
    }

    public String getObjectName() {
		if (null == this.objectName) {
			return this.getClass().getSimpleName().toLowerCase()+"obj";
		}
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }
}

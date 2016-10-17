package com.hp.xo.resourcepool.response;


public abstract class BaseResponse implements ResponseObject {
    private transient String responseName;
    private transient String objectName;
    protected String jobId;
    private Integer jobStatus;
    
    public BaseResponse() {
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

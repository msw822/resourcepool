package com.hp.xo.resourcepool.vo;

public class WorkOrderReportVO extends BaseVO {

	private static final long serialVersionUID = 2213517242966326014L;
	private Object[] obj;

	public Object[] getObj() {
		return obj;
	}

	public void setObj(Object[] obj) {
		this.obj = obj;
	}
	private transient String responseName;
    private transient String objectName;
    protected String jobId;
    private Integer jobStatus;
    
    
    public String getResponseName() {
		
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

package com.hp.xo.resourcepool.response;

public interface ResponseObject {
    String getResponseName();
    void setResponseName(String name);
    String getObjectName();
    void setObjectName(String name);
    String getJobId();
    void setJobId(String jobId);
    Integer getJobStatus();
    void setJobStatus(Integer jobStatus);
}

package com.hp.xo.resourcepool.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.wsclient.CloudStackApiWebClient;


@Service(value="genericCloudServerManager")
public class GenericCloudServerManagerImpl {
    protected final Logger log = Logger.getLogger(this.getClass().getName());
    @Autowired
    private CloudStackApiWebClient cloudStackApiWebClient;
    
    public GenericCloudServerManagerImpl() { 
    } 
    
    public  Response get(Map<String, Object[]> params, boolean isSecret) { 
    	Response resp = null;
        Object[] responseObj = params.get("response");
        String responseType = (String) responseObj[0];		
        resp = cloudStackApiWebClient.request(CloudStackApiWebClient.METHOD_TYPE_GET, params, isSecret);
        resp.setType(responseType);
        return resp;  
    } 
    
    public  Response get(Map<String, Object[]> params) { 
    	return this.get(params, true);
    }
    
    public  Response post(Map<String, Object[]> params) { 
    	return this.post(params, true);
    }
    
    
    public  Response post(Map<String, Object[]> params, boolean isSecret) { 
    	Response resp = null;
        Object[] responseObj = params.get("response");
        String responseType = (String) responseObj[0];        
        resp = cloudStackApiWebClient.request(CloudStackApiWebClient.METHOD_TYPE_POST, params, isSecret);
    	resp.setType(responseType);
        return resp;  
    } 

	public CloudStackApiWebClient getCloudStackApiWebClient() {
		return cloudStackApiWebClient;
	}

	public void setCloudStackApiWebClient(CloudStackApiWebClient cloudStackApiWebClient) {
		this.cloudStackApiWebClient = cloudStackApiWebClient;
	}
}

package com.hp.xo.resourcepool.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.ServiceConstants;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.DimResource;
import com.hp.xo.resourcepool.model.ResourcePool;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.DimResourceTreeRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.schedule.service.DimResourceManager;
import com.hp.xo.resourcepool.service.ResourcePoolManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.web.action.core.BaseAction;


public class ResourcePoolAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5364087239920275888L;
	
	@Autowired
	private ResourcePoolManager resourcePoolManager;
	
	@Autowired
	private DimResourceManager dimResourceManager;
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	
	
	public void setResourcePoolManager(ResourcePoolManager resourcePoolManager) {
		this.resourcePoolManager = resourcePoolManager;
	}

	
	
	
	public String computeResource(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		String jsoncallback =   this.request.getParameter("jsoncallback");
		jsoncallback = jsoncallback==null || "".equals(jsoncallback)?"jsoncallback":jsoncallback;
		
		Map<String,Object[]> param = new HashMap();
		param.putAll(this.requestParams);
		String resultStr = resourcePoolManager.computeResource(param, resourcePoolId);
		
		if(!"jsoncallback".equals(jsoncallback))
		{
			resultStr = jsoncallback+"("+resultStr+")";
		}
		
		Response responseObj = new Response();
		responseObj.setResponseString(resultStr);
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(responseType);
		writeResponse(responseObj);
		return NONE;
	}
	//按人员(账号)统计20141230
	public String accountresource(){
		String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		String jsoncallback =   this.request.getParameter("jsoncallback");
		jsoncallback = jsoncallback==null || "".equals(jsoncallback)?"jsoncallback":jsoncallback;
		Map<String,Object[]> param = new HashMap();
		param.putAll(this.requestParams);
		String resultStr = resourcePoolManager.accountResource(param, resourcePoolId);

		if( !"jsoncallback".equals(jsoncallback) )
		{
			resultStr = jsoncallback+"("+resultStr+")";
		}
		
		
		Response responseObj = new Response();
		responseObj.setResponseString(resultStr);
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(responseType);
		writeResponse(responseObj);
		return NONE;
	}
	    //按业务系统统计20141230
		public String operationresource(){
			String resourcePoolId = requestParams.get("resourcePoolId")==null ? null : (String) requestParams.get("resourcePoolId")[0];
			String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
			
			Map<String,Object[]> param = new HashMap();
			param.putAll(this.requestParams);
			String resultStr = resourcePoolManager.operationResource(param, resourcePoolId);
			
			Response responseObj = new Response();
			responseObj.setResponseString(resultStr);
			responseObj.setStatusCode(HttpStatus.SC_OK);
			responseObj.setType(responseType);
			writeResponse(responseObj);
			return NONE;
		}
}

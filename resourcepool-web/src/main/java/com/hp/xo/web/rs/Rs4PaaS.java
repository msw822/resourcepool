/*package com.hp.xo.web.rs;

import java.util.Date;
import java.util.LinkedHashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.cmsz.cloudplatform.model.PaasDestoryObj;
import com.cmsz.cloudplatform.service.PaasDestoryObjManager;
import com.cmsz.cloudplatform.utils.ServiceOptionUtil;
import com.cmsz.cloudplatform.web.action.ControlAction;
import com.hp.xo.resourcepool.utils.SpringUtil;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

@Path("/rs4paas")
public class Rs4PaaS {
	private static final Logger log = Logger.getLogger(Rs4PaaS.class.getName());
	@GET
	@Path("/handleRequest")
	@Produces(MediaType.TEXT_PLAIN)//APPLICATION_JSON
	public String handleRequest(@QueryParam("requestParams")String requestParams){
		int len=requestParams.length();
		requestParams=requestParams.substring(1, len-1);
		String[] requestParamArr=requestParams.split("~");
		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		RsRequestHandler handler=new RsRequestHandler();
		for(int i=0;i<requestParamArr.length;i++){
			String[] requestParamPair=requestParamArr[i].split("=");
			param.put(requestParamPair[0], requestParamPair[1]);
		}
		
		String username=param.get("username");
		String password=param.get("password");
		String command =param.get("command");
		//call the login interface and verify the username and password and then get the userId.
		String loginResponse=handler.BuildLogin(username, password);
		if(loginResponse.indexOf("errorcode")>-1){
			return loginResponse;
		}
		
		JSONObject jo = JSONObject.fromObject(loginResponse);
		String userid=(String)JSONObject.fromObject(jo.getString("loginresponse")).get("userid");
		
		//get apikey and secretkey via userid.
	//	String tokenResponse=handler.BuildRegisterUserKeys(userid);
		String userStr=handler.BuildListUsers(userid);
		if(userStr.indexOf("errorcode")>-1){
			return userStr;
		}
		
		System.out.println("tokenreposneis  ===="+userStr);
		//String userid=(String)JSONObject.fromObject(jo.getString("loginresponse")).get("userid");
		//JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(tokenResponse).getString("listusersresponse")).getString("user")).getJSONArray(key)
		JSONObject listusersresponseJSON=JSONObject.fromObject(userStr);
		String listusersresponseStr =listusersresponseJSON.getString("listusersresponse");
		JSONObject jo3=JSONObject.fromObject(listusersresponseStr);
		JSONArray userArr=jo3.getJSONArray("user");
	    JSONObject userJson=userArr.getJSONObject(0);
	    String apikey=userJson.getString("apikey");
	    String secretkey=userJson.getString("secretkey");
		
		param.put("apikey", apikey);
		if("getCredentials".equalsIgnoreCase(command)){
			String templateId=param.get("templateId");
			String credentials=handler.getCredentialsByTemplateId(templateId);
			return credentials;
		}
		
		else if("listServiceOfferings".equalsIgnoreCase(command)){
			param=handler.buildListServiceOfferings(param);
					
		}
		else if("listDiskOfferings".equalsIgnoreCase(command)){
			param=handler.buildListDiskOfferings(param);
		}
		else if("listNetworks".equalsIgnoreCase(command)){
			param=handler.buildListNetworks(param);
			
		}
		else if("listTemplates".equalsIgnoreCase(command)){
			param=handler.buildListTemplates(param);
			
		}
		else if("createProject".equalsIgnoreCase(command)){
			param=handler.buildCreateProject(param);
		}
		else if("deleteProject".equalsIgnoreCase(command)){
			param=handler.buildDeleteProject(param);
			
		}
		else if("deployVirtualMachine".equalsIgnoreCase(command)){
			param=handler.buildDeployVirtualMachine(param);
			
		}
		else if("startVirtualMachine".equalsIgnoreCase(command)){
			param=handler.buildStartVirtualMachine(param);
			
		}
		else if("stopVirtualMachine".equalsIgnoreCase(command)){
			param=handler.buildStopVirtualMachine(param);
			
		}
		else if("destroyVirtualMachine".equalsIgnoreCase(command)){
			param=handler.buildDestroyVirtualMachine(param);
			
		}
		else if("listVirtualMachines".equalsIgnoreCase(command)){
			param=handler.buildListVirtualMachines(param);
		}
		
		else if("memoryUsageRatio".equalsIgnoreCase(command)){
			String instanceId=param.get("id");
			long startTime=Long.parseLong(param.get("startTime"));
			long endTime=Long.parseLong(param.get("endTime"));
			String memoryUsageRation=handler.getMemoryUsageRatio(instanceId,startTime,endTime);
			return memoryUsageRation;
				}
		else if("diskUsageRatio".equalsIgnoreCase(command)){
			String instanceId=param.get("id");
			long startTime=Long.parseLong(param.get("startTime"));
			long endTime=Long.parseLong(param.get("endTime"));
			String diskUsageRation=handler.getDiskUsageRatio(instanceId,startTime,endTime);
			return diskUsageRation;
		}
		
		String signature=handler.getSignature(param,secretkey);
		param.put("signature", signature);
		
		String reqUrl = handler.buildRequestUrl( param);
		if (log.isDebugEnabled()) {
			log.debug("reqUrl >>> " + reqUrl);
		}
		String responseStr=RsRequestHandler.doHttpGet(reqUrl);
		
		if(responseStr.indexOf("errorcode")>0){
			if("deleteProject".equalsIgnoreCase(command) || "destroyVirtualMachine".equalsIgnoreCase(command) ){
				PaasDestoryObj po = new PaasDestoryObj();
				po.setCommand(command);
				po.setCreatedBy("admin");
				po.setCreatedOn(new Date());
				po.setExeResult(PaasDestoryObj.RESULT_FAIL);
				po.setFailReason(responseStr);
				//paas调用时，如果返回结果是失败，放入到后台定时处理表中去，让iaas平台自动去处理paas失败的操作
				if("deleteProject".equalsIgnoreCase(command)){
					po.setObjType(PaasDestoryObj.TYPE_PROJECT);
					po.setObjId(param.get("id"));
					if(param.get("name")!=null){
						po.setObjectName(param.get("name"));
					}
				}else if("destroyVirtualMachine".equalsIgnoreCase(command)){
					po.setObjType(PaasDestoryObj.TYPE_VM);
					po.setObjId(param.get("id"));
					if(param.get("name")!=null){
						po.setObjectName(param.get("name"));
					}
				}
				ApplicationContext applicationContext = SpringUtil.getApplicationContext();
				PaasDestoryObjManager  paasDestoryObjManager = (PaasDestoryObjManager) applicationContext.getBean("paasDestoryObjManager");
				paasDestoryObjManager.save(po);
			}
		}
		return responseStr;
		
	}
	
} */
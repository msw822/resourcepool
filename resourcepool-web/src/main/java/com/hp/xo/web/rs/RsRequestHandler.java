/*package com.hp.xo.web.rs;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.cmsz.cloudplatform.model.DiskPerformance;
import com.cmsz.cloudplatform.model.MemoryPerformance;
import com.cmsz.cloudplatform.model.TemplateAccount;
import com.cmsz.cloudplatform.model.TemplateCredentials;
import com.cmsz.cloudplatform.model.response.ErrorResponse;
import com.cmsz.cloudplatform.service.TemplateAccountManager;
import com.hp.util.SpringUtil;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;




public class RsRequestHandler {
    public static final Logger log = Logger.getLogger(RsRequestHandler.class.getName());

    static String unSecureUrl = ServiceOptionUtil.obtainCloudStackApiUnSecretUrl();
    static String secureUrl   = ServiceOptionUtil.obtainCloudStackApiSecretUrl();
	static String domainid=ServiceOptionUtil.obtainDomainId4PaaS();
	static String domainPath=ServiceOptionUtil.obtainDomainPath4PaaS();
	static String zoneid=ServiceOptionUtil.obtainZoneId4PaaS();
	static String account=ServiceOptionUtil.obtainAccount4PaaS();
	static String username=ServiceOptionUtil.obtainUsername4PaaS();
	static String projecId = ServiceOptionUtil.obtainProjectId4PaaS();
	
	

	public  String buildRequestUrl( LinkedHashMap<String, String> param) {
		StringBuilder sb = new StringBuilder();
		sb.append(secureUrl);
		sb.append("?");
	
		for (String key : param.keySet()) {
			sb.append("&").append(key).append("=").append(param.get(key));
		}

		return sb.toString();
	}
	
	public static  String doHttpGet(String reqUrl) {
		    Response result = new Response(); 
	        
	        HttpUriRequest httpReq = null;
	        HttpResponse httpResp = null;
	        httpReq = new HttpGet(reqUrl);
	    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
	        
	        try {
				httpResp = httpclient.execute(httpReq);
				
		        if (null != httpResp) {
		        	HttpEntity entityResp = httpResp.getEntity();
		        	
		        	result.setResponseString(EntityUtils.toString(entityResp, "UTF-8"));
					result.setStatusCode(httpResp.getStatusLine().getStatusCode());
					
		        if (log.isDebugEnabled()) {
		    	        Header[] hds = httpResp.getAllHeaders();
		    	        if (null != hds) {
			    	        for (Header hd : hds) {
			    	        	log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response headName[" + hd.getName() + "] headValue[" + hd.getValue() + "]");
			    	        }
		        		} else {
		        			log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response header is empty");
		        		}
		    	        
		            }
		        	
		        }

	    	
	         } catch (Exception e) {
	        	log.error(e.getMessage(), e);
	        	ErrorResponse er=new ErrorResponse();
	        	er.setErrorCode(-1);
	        	er.setErrorText(e.getMessage());
	        	HashMap<String,ErrorResponse> map=new HashMap<String,ErrorResponse>();
	        	map.put("errorresponse", er);
	        	JSONObject json=JSONObject.fromObject(map);
	        	return json.toString();
	        	
	        	//TODO throwing and handling
	         } finally {
	        	 httpclient.getConnectionManager().shutdown();
	         }
	        	 
	    	
	    	if (log.isDebugEnabled()) {
	    		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": httpclient.execute(new HttpRequest(" + reqUrl + ") respone -> " + result);
	    	}
	        	
	        	
	    
		  return result.getResponseString();
		
	
		}
	
	
	
	public static class Response {
		private String responseString = null;
		private int statusCode = 0;
		
		public Response() {
			super();
		}


		public String getResponseString() {
			return responseString;
		}

		public void setResponseString(String responseString) {
			this.responseString = responseString;
		}


		public int getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(int statusCode) {
			this.statusCode = statusCode;
		}

		@Override
		public String toString() {
			return "Response [responseString="  + responseString + ", statusCode="
					+ statusCode+ "]";
		}

	
	}
	
	public String BuildLogin(String username,String password){
		if(!RsRequestHandler.username.equalsIgnoreCase(username)){
			ErrorResponse er=new ErrorResponse();
			er.setErrorCode(-2);
			er.setErrorText("the username is not correct.Please contack IaaS admin to get the user that assigned to PaaS");
		    HashMap<String,ErrorResponse> map=new HashMap<String,ErrorResponse>();
		     map.put("errorresponse", er);
			JSONObject json = JSONObject.fromObject(map);
			String ers=json.toString();
			return ers;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(secureUrl);
		sb.append("?");
		sb.append("&").append("command").append("=").append("login");
		sb.append("&").append("username").append("=").append(username);
		sb.append("&").append("password").append("=").append(password);
		sb.append("&").append("domain").append("=").append(RsRequestHandler.domainPath);
		sb.append("&").append("response").append("=").append("json");
		
		String loginResponse=doHttpGet(sb.toString());
		return loginResponse;
		
	}
	
	public String BuildListUsers(String userid){
		
		StringBuilder sb = new StringBuilder();
		sb.append(unSecureUrl);
		sb.append("?");
		sb.append("&").append("command").append("=").append("listUsers");
		sb.append("&").append("id").append("=").append(userid);
		sb.append("&").append("response").append("=").append("json");
		
		return doHttpGet(sb.toString());
		
		
	}
	public String BuildRegisterUserKeys(String userId){
		StringBuilder sb = new StringBuilder();
		sb=new StringBuilder();
		sb.append(unSecureUrl);
		sb.append("?");
		sb.append("&").append("id").append("=").append(userId);
		sb.append("&").append("command").append("=").append("registerUserKeys");
		sb.append("&").append("response").append("=").append("json");
		String tokenResponse=doHttpGet(sb.toString());
		return tokenResponse;
		
	}
	public String getCredentialsByTemplateId(String templateId){
		
		TemplateCredentials credentials=new TemplateCredentials();
		credentials.setTemplateId(templateId);
		ApplicationContext applicationContext = SpringUtil.getApplicationContext();
		
		TemplateAccountManager templateAccountManager = (TemplateAccountManager)applicationContext.getBean("templateAccountManager");
		List<TemplateAccount> list= templateAccountManager.getByTemplateId(templateId);
		for(TemplateAccount ta:list){
			String acconutName=ta.getAccountName();
			String accountType=ta.getAccountType();
			String accountPassWord=ta.getPassword();
			
			if("0".equalsIgnoreCase(accountType)){
			credentials.setOspasswd(accountPassWord);
			credentials.setOsuserName(acconutName);
			}
			if("1".equalsIgnoreCase(accountType)){
			credentials.setFtppasswd(accountPassWord);
			credentials.setFtpuserName(acconutName);
			}
		}
		
		//String userName="username";
		//String ospasswd="ospasswd";
		//String ftppasswd="ftppasswd";  
		
		
		credentials.setFtppasswd("ftppassword");
		credentials.setOspasswd("ospassword");
		credentials.setFtpuserName("ftpadmin");
		credentials.setOsuserName("osadmin");
		
		HashMap<String, TemplateCredentials> map = new HashMap<String, TemplateCredentials>();
		map.put("getCredentialsResponse", credentials);
		JSONObject json = JSONObject.fromObject(map);
		String cred=json.toString();
		return cred;
	}
	
	public String getMemoryUsageRatio(String instanceId,long startTime,long endTime){
		MemoryPerformance mp=new MemoryPerformance();
		mp.setInstanceId(instanceId);
		mp.setMemoryUsageRatio("50%");
        mp.setTime(endTime);
        
		HashMap<String, MemoryPerformance> map = new HashMap<String, MemoryPerformance>();
		map.put("memoryUsageRatioResponse", mp);
		JSONObject json = JSONObject.fromObject(map);
		String mps=json.toString();
		return mps;
			
				}
	public String getDiskUsageRatio(String  instanceId,long startTime,long endTime){
		DiskPerformance dp=new DiskPerformance();
		dp.setInstanceId(instanceId);
		dp.setDiskUsageRatio("50%");
		dp.setDiskioread("10");
		dp.setDiskiowrite("20");
		dp.setDiskkbsread("30");
		dp.setDiskkbswrite("40");
		dp.setTime(endTime);
		HashMap<String, DiskPerformance> map = new HashMap<String, DiskPerformance>();
		map.put("diskUsageRatioResponse", dp);
		JSONObject json = JSONObject.fromObject(map);
		String mps=json.toString();
		return mps;
		
	}
	
	
	String getSignature(Map<String, String> param,String secretKey) {
		SortedMap<String, String> need2Sign = new TreeMap<String, String>();
		for (String s : param.keySet()) {
			need2Sign.put(s.toLowerCase(), param.get(s).toLowerCase()
					.replaceAll("[ +]", "%20"));
		}
		StringBuilder sb2 = new StringBuilder();
		for (String s : need2Sign.keySet()) {
			sb2.append(s).append("=").append(need2Sign.get(s)).append("&");
		}
		sb2.deleteCharAt(sb2.length() - 1);
		String signature = HmacSha.encodEncrypt(sb2.toString().toLowerCase(),
				secretKey);

		return signature;
	}
	
	
	//http://192.168.22.80:8080/client/api?command=login&username=admin&password=password&domain=/
	//http://192.168.22.80:8080/client/api?&command=login&username=admin&password=passworld&domain=/&response=json
	//http://192.168.22.80:8096/client/api?id=31f8f717-6302-11e3-89ad-005056941242&response=json&command=registerUserKeys&signature=null
	//http://localhost:8888/client/rest/rs4paas/handleRequest?requestParams=(command=listServiceOfferings~response=xml~username=admin~password=password)
	
	public  LinkedHashMap<String, String> buildListServiceOfferings( LinkedHashMap<String, String> param) {
		param.put("domainid", RsRequestHandler.domainid);
		return param;
	}
	public  LinkedHashMap<String, String> buildListDiskOfferings( LinkedHashMap<String, String> param) {
		param.put("domainid", RsRequestHandler.domainid);
		return param;
	}
	public  LinkedHashMap<String, String> buildListNetworks( LinkedHashMap<String, String> param) {
		param.put("zoneid", RsRequestHandler.zoneid);
		param.put("domainid", RsRequestHandler.domainid);
		param.put("account", RsRequestHandler.account);
		return param;
	}
	public  LinkedHashMap<String, String> buildListTemplates( LinkedHashMap<String, String> param) {
		//param.put("domainid", RsRequestHandler.domainid);
		//param.put("account", RsRequestHandler.account);
		param.put("zoneid", RsRequestHandler.zoneid);
		return param;
	}
	public  LinkedHashMap<String, String> buildCreateProject( LinkedHashMap<String, String> param) {
		param.put("domainid", RsRequestHandler.domainid);
		param.put("account", RsRequestHandler.account);
		
		return param;
	}
	public  LinkedHashMap<String, String> buildDeleteProject( LinkedHashMap<String, String> param) {
		return param;
	}
	public  LinkedHashMap<String, String> buildDeployVirtualMachine( LinkedHashMap<String, String> param) {
		param.put("zoneid", RsRequestHandler.zoneid);
		param.put("domainid", RsRequestHandler.domainid);
		param.put("account", RsRequestHandler.account);
		if(StringUtils.isBlank(param.get("projectid"))){
			param.put("projectid", RsRequestHandler.projecId);
		}
		return param;
	}
	public  LinkedHashMap<String, String> buildStartVirtualMachine( LinkedHashMap<String, String> param) {
		return param;
	}
	public  LinkedHashMap<String, String> buildStopVirtualMachine( LinkedHashMap<String, String> param) {
		return param;
	}
	public  LinkedHashMap<String, String> buildDestroyVirtualMachine( LinkedHashMap<String, String> param) {
		return param;
	}
	public  LinkedHashMap<String, String> buildListVirtualMachines( LinkedHashMap<String, String> param) {
		param.put("zoneid", RsRequestHandler.zoneid);
		param.put("domainid", RsRequestHandler.domainid);
		param.put("account", RsRequestHandler.account);
		return param;
	}
	

}

class HmacSha {

	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";

	static String encodEncrypt(String encryptText, String encryptKey) {
		return encode2Base64(encrypt(encryptText, encryptKey));
	}

	static byte[] encrypt(String encryptText, String encryptKey) {
		try {
			byte[] data = encryptKey.getBytes(ENCODING);
			SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
			Mac mac = Mac.getInstance(MAC_NAME);
			mac.init(secretKey);

			byte[] text = encryptText.getBytes(ENCODING);
			return mac.doFinal(text);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static String encode2Base64(byte[] content) {
		Base64 base64 = new Base64();
		byte[] tmp = base64.encode(content);
		return new String(tmp).replaceAll("[/]", "%2f")
				.replaceAll("[=]", "%3d").replaceAll("[+]", "%2b");
	}
	
	
}*/
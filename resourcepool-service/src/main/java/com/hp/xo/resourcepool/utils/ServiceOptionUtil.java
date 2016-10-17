package com.hp.xo.resourcepool.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hp.xo.resourcepool.configuration.impl.AppConfigMgr;


public class ServiceOptionUtil {

	private static final String CLOUDSTACK_MANGEMENT_API_SECRET_URL = "cloudstack.mangement.api.secret.url";
	private static final String CLOUDSTACK_MANGEMENT_API_UNSECRET_URL = "cloudstack.mangement.api.unsecret.url";
	private static final String CLOUDSTACK_API_COMMAND_PARAM_ORDER_TO_FIRST_PREFIX = "cloudstack.api.command.param.order.to.first";
	private static final String DOMAINID_4_PAAS="domainid.4.paas";
	private static final String DOMAINPATH_4_PAAS="domainpath.4.paas";
	private static final String ZONEID_4_PAAS="zoneid.4.paas";
	private static final String ACCOUNT_4_PAAS="account.4.paas";
	private static final String USERNAME_4_PAAS="username.4.paas";
	private static final String CLOUDSTACK_ROOT_USERNAME = "cloudStack.root.username";
	private static final String PROJECT_ID_4_PAAS = "projectid.4.paas";
	private static final String CLOUDSTACK_ROOT_PASSWORD = "cloudStack.root.password";
	private static final String CLOUDSTACK_ROOT_USERID = "cloudStack.root.userid";
	private static final String ENCODE_RESPONSE = "encode.response";
	
	private static final String  INSTANCE_DUE_NOTICE_DAYS= "instance.due.notice.days";

	
	private static final AppConfigMgr appConfig = AppConfigMgr.getInstance();	
	
	private ServiceOptionUtil() {

	}
	
	
	
	
	
	public static String obtainCloudStackApiSecretUrl() {
		String spacetime = appConfig.getValue(CLOUDSTACK_MANGEMENT_API_SECRET_URL);
				
		return spacetime;
	}
	
	public static String obtainCloudStackApiUnSecretUrl() {
		String spacetime = appConfig.getValue(CLOUDSTACK_MANGEMENT_API_UNSECRET_URL);
				
		return spacetime;
	}
	
	public static String obtainDomainId4PaaS(){
		String domainId = appConfig.getValue(DOMAINID_4_PAAS);
		return domainId;
		
	}
	public static String obtainDomainPath4PaaS(){
		String domainId = appConfig.getValue(DOMAINPATH_4_PAAS);
		return domainId;
		
	}
	public static String obtainZoneId4PaaS(){
		String domainId = appConfig.getValue(ZONEID_4_PAAS);
		return domainId;
		
	}
	public static String obtainAccount4PaaS(){
		String domainId = appConfig.getValue(ACCOUNT_4_PAAS);
		return domainId;
		
	}
	public static String obtainUsername4PaaS(){
		String domainId = appConfig.getValue(USERNAME_4_PAAS);
		return domainId;
		
	}
	
	public static String obtainCloudStackUsername(){
		String username = appConfig.getValue(CLOUDSTACK_ROOT_USERNAME);
		return username;
	}
	
	public static String obtainCloudStackPassword(){
		String password = appConfig.getValue(CLOUDSTACK_ROOT_PASSWORD);
		return password;
	}
	public static String obtainCloudStackUserId(){
		String password = appConfig.getValue(CLOUDSTACK_ROOT_USERID);
		return password;
	}
	
	public static String obtainProjectId4PaaS(){
		String projectName = appConfig.getValue(PROJECT_ID_4_PAAS);
		return projectName;
	}
	public static String getValue(String key,String defaultValue){
		String values = appConfig.getValue(key);
		if(StringUtils.isBlank(values)){
			return defaultValue;
		}
		return values;
	}
	
	
	
	

	

	
	public static Map<String, String> obtainCommandParamOrderToFirstMap() {
		Map<String, String> result = new HashMap<String, String>();

		Map<String, String> map = appConfig.getValuesAsMap(CLOUDSTACK_API_COMMAND_PARAM_ORDER_TO_FIRST_PREFIX);
		if (null != result) {
			for (String key : map.keySet()) {
				String cmdKey = key.substring(CLOUDSTACK_API_COMMAND_PARAM_ORDER_TO_FIRST_PREFIX.length()+1);
				if (StringUtils.isNotBlank(cmdKey)) {
					result.put(cmdKey.trim().toLowerCase(), map.get(key));
				}
			}
		}
		return result;
	}
	
	public static Boolean obtainEncodeResponse(){
		Boolean value = appConfig.getBooleanValue(ENCODE_RESPONSE);
		if (null == value) {
			value = Boolean.FALSE;
		}
		
		return value;
	}
	
	public static void main(String[] args) {
		String test = CLOUDSTACK_API_COMMAND_PARAM_ORDER_TO_FIRST_PREFIX+ ".listUsers";
		System.out.println(test.substring(CLOUDSTACK_API_COMMAND_PARAM_ORDER_TO_FIRST_PREFIX.length() + 1));
		
		System.out.println(obtainEncodeResponse());
	}
	
	
	
	/*public static String obtainCloudStackApiSecretUrl() {
		String spacetime = appConfig.getValue(CLOUDSTACK_MANGEMENT_API_SECRET_URL);
				
		return spacetime;
	}*/
	
}


package com.hp.xo.resourcepool.web.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.ServiceConstants;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.LogConstants;
import com.hp.xo.resourcepool.web.action.core.BaseAction;

public class LoginAction extends BaseAction {
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private LogManager logManager;

	public String execute() {
		log.debug("----------------------------");
        log.debug("---------------------------- login");
        log.debug("----------------------------");
		return login();
	}

	private String login() {

		Response loginResponse = genericCloudServerManager.post(this.requestParams);

		// resp.setType(responseType);

		JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
		boolean isLogin = true;
		try {
			jo.getString("loginresponse");
		} catch (JSONException e) {
			log.warn("login info is error, " + loginResponse.getResponseString());
			isLogin = false;
			//记录登录失败日志
			logManager.log(this.request.getRemoteHost(), LogConstants.AUTH, LogConstants.LOGIN, requestParams.get("username")[0].toString(), "用户[" + requestParams.get("username")[0].toString() + "]登录成功。", LogConstants.FAIL, loginResponse.getResponseString());
		}
		
		this.writeResponse(loginResponse);
		

	
		if (isLogin) {
			
			ActiveUser au = new ActiveUser();
			
			// resp.setSessionKey((String)JSONObject.fromObject(jo.getString("loginresponse")).get("sessionkey"));
			loginResponse.setUserid((String) JSONObject.fromObject(jo.getString("loginresponse")).get("userid"));
			loginResponse.setDomainid((String) JSONObject.fromObject(jo.getString("loginresponse")).get("domainid"));
			au.setUserid(loginResponse.getUserid());
			au.setLoginId((String) JSONObject.fromObject(jo.getString("loginresponse")).get("username"));
			au.setClientIP(request.getRemoteHost());
			au.setDefaultLang(request.getLocale().getLanguage());
			//记录成功登录日志
			logManager.log(this.request.getRemoteHost(), LogConstants.AUTH, LogConstants.LOGIN, au.getLoginId(), "用户[" + au.getLoginId() + "]登录成功。", LogConstants.SUCCESS, "");
			Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
			listUsersParams.put("command", new Object[] { "listUsers" });
			listUsersParams.put("response", new Object[] { "json" });
			listUsersParams.put("id", new Object[] { loginResponse.getUserid() });
			
			Response listUsersResponse = genericCloudServerManager.get(listUsersParams, false);
			
			if (log.isDebugEnabled()) {
				log.debug("listUsersResponse >>> " + listUsersResponse);
			}
			
			String usersresponse = listUsersResponse.getResponseString();
			if (StringUtils.isNotBlank(usersresponse)) {
				jo = JSONObject.fromObject(usersresponse);
					jo = JSONObject.fromObject(jo.getString("listusersresponse"));
					JSONArray jos = jo.getJSONArray("user");
					jo = JSONObject.fromObject(jos.get(0));
					int role = ActiveUser.USER;
					try {
						role = jo.getInt("accounttype");
					} catch (Exception e) {
						role = ActiveUser.USER;
					}
					au.setRole(role);
					
					au.setUserName(jo.getString("username"));

					String apikey = null;
					try {
						apikey = jo.getString("apikey");
					} catch (Exception e) {
						apikey = null;
					}

					String accountid = null;
					try {
						accountid = jo.getString("accountid");
						au.setAccountid(accountid);
					} catch (Exception e) {
						accountid = null;
					}
					
					String account = null;
					try {
						account = jo.getString("account");
						au.setAccount(account);
					} catch (Exception e) {
						account = null;
					}
					
					String domainid = null;
					try {
						domainid = jo.getString("domainid");
						au.setDomainid(domainid);
					} catch (Exception e) {
						domainid = null;
					}
					
					if (StringUtils.isNotBlank(apikey)) {
						String secretkey = jo.getString("secretkey");

						au.setApikey(apikey);
						au.setSecretkey(secretkey);
						
						if (log.isDebugEnabled()) {
							log.debug("apikey:" + apikey);
							log.debug("secretkey:" + secretkey);
						}
					} else {
						Map<String, Object[]> apiKeyParams = new HashMap<String, Object[]>();
						apiKeyParams.put("command", new Object[] { "registerUserKeys" });
						apiKeyParams.put("response", new Object[] { "json" });
						apiKeyParams.put("id", new Object[] { loginResponse.getUserid() });

						Response registerUserKeysResponse = genericCloudServerManager.get(apiKeyParams, false);

						if (log.isDebugEnabled()) {
							log.debug("registerUserKeysResponse >>> " + registerUserKeysResponse);
						}

						String apikeyresponse = registerUserKeysResponse.getResponseString();

						if (StringUtils.isNotBlank(apikeyresponse)) {
							jo = JSONObject.fromObject(apikeyresponse);
							jo = JSONObject.fromObject(jo.getString("registeruserkeysresponse"));

							String userKeysString = jo.getString("userkeys");

							if (StringUtils.isNotBlank(userKeysString)) {
								jo = JSONObject.fromObject(userKeysString);
								apikey = jo.getString("apikey");
								String secretkey = jo.getString("secretkey");

								au.setApikey(apikey);
								au.setSecretkey(secretkey);
								
								if (log.isDebugEnabled()) {
									log.debug("apikey:" + apikey);
									log.debug("secretkey:" + secretkey);
								}
							} else {
								log.error("ERROR::apikeyresponse:" + apikeyresponse);
								this.writeResponse(registerUserKeysResponse);
							}
						}
					}
					
					
				
			}
						
			Map<String, Object[]> listdomainsparams = new HashMap<String, Object[]>();
			listdomainsparams.put("command", new Object[] { "listDomains" });
			listdomainsparams.put("response", new Object[] { "json" });
			listdomainsparams.put("id", new Object[] { loginResponse.getDomainid()});
			
			Response listdomainsresponse = genericCloudServerManager.get(listdomainsparams, false);
			
			if (log.isDebugEnabled()) {
				log.debug("listUsersResponse >>> " + listUsersResponse);
			}
			
			String domainresponse = listdomainsresponse.getResponseString();
			if (StringUtils.isNotBlank(domainresponse)) {
				jo = JSONObject.fromObject(domainresponse);
					jo = JSONObject.fromObject(jo.getString("listdomainsresponse"));
					JSONArray jos = jo.getJSONArray("domain");
					jo = JSONObject.fromObject(jos.get(0));
					String path = null;
					String path2=null;
					try {
						path = jo.getString("path");
					} catch (Exception e) {
						path = null;
					}
					if(path.lastIndexOf("/")!=-1){
						path2=path.substring(path.lastIndexOf("/")+1, path.length());
					}else{
						path2=path;
					}
					au.setPath(path);
					au.setPath2(path2);
			}
			
			this.request.getSession(true).setAttribute(ServiceConstants.ACTIVE_USER, au);
		}

		return NONE;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}
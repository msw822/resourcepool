package com.hp.xo.resourcepool.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;

public abstract class AbstractSchedule {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private GenericCloudServerManagerImpl genericCloudServerManager;	
	private Response loginCloudStack() {
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("command", new Object[] { "login" });
		param.put("username",new Object[] { ServiceOptionUtil.obtainCloudStackUsername() });
		param.put("password",new Object[] { ServiceOptionUtil.obtainCloudStackPassword() });
		param.put("response", new Object[] { "json" });
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;

	}
	public Map<String ,Object[] > getCloudStackParams()throws BuessionException {
		Response loginResponse = loginCloudStack();
		JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
		String userId = "";
		String sessionkey = "";
		try {
			jo = JSONObject.fromObject(jo.getString("loginresponse"));
			userId = jo.getString("userid");
			sessionkey = jo.getString("sessionkey");
		} catch (JSONException e) {
			log.error("login info is error, "+ loginResponse.getResponseString());
			throw new BuessionException(e.getMessage());
		}

		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("id", new Object[] { userId });

		Response listUsersResponse = genericCloudServerManager.get(listUsersParams, false);

		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			jo = JSONObject.fromObject(listUsersResponse.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
		Map<String, Object[]> cloudStackParams = new HashMap<String, Object[]>();
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("sessionkey", new Object[] { sessionkey });
		cloudStackParams.put("response", new Object[] { "json" });
		return cloudStackParams;
	}	
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}

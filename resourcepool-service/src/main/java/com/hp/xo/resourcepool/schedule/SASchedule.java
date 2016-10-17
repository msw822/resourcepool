package com.hp.xo.resourcepool.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.rmi.RmiNotifyServiceBuilder;
import com.hp.xo.resourcepool.rmi.RmiServiceClientFactory;
import com.hp.xo.resourcepool.schedule.service.ScheduleService;
import com.hp.xo.resourcepool.schedule.service.impl.SAAsyncHelper;
import com.hp.xo.resourcepool.schedule.service.impl.SACapabilityHelper;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.service.impl.WorkOrderJobManagerImpl;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;
import com.hp.xo.uip.cmdb.service.CmdbService;

@Service(value = "saSchedule")
// @Transactional(propagation=Propagation.REQUIRED)
public class SASchedule {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private SAAsyncHelper saAsyncHelper;
	@Autowired
	private SACapabilityHelper saCapabilityHelper;
	@Autowired
	private HostManagerService hostManagerService;
	@Autowired
	private WorkOrderJobManagerImpl workOrderJobManager;
	@Autowired
	private ScheduleService virtualMachineManager;
	
	public void work() {
		//System.out.println("-----------------------------------服务开通  begin--------------------------------------------------------");
		active();	
		System.out.println("-----------------------------------服务开通  end--------------------------------------------------------");
	}
	public void expire() throws BuessionException {
		saAsyncHelper.expire(getCloudStackParams());
	}
	private Response loginCloudStack() {
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("command", new Object[] { "login" });
		param.put("username",new Object[] { ServiceOptionUtil.obtainCloudStackUsername() });
		param.put("password",new Object[] { ServiceOptionUtil.obtainCloudStackPassword() });
		param.put("response", new Object[] { "json" });
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;

	}
	
	public void active() {
		Response loginResponse = loginCloudStack();
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		if(log.isDebugEnabled()){
			log.debug(loginResponse.toString());
		}
		JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
		String userId = "";
		String sessionkey = "";
		try {
			jo = JSONObject.fromObject(jo.getString("loginresponse"));
			userId = jo.getString("userid");
			sessionkey = jo.getString("sessionkey");
		} catch (JSONException e) {
			log.error("login info is error, "+ loginResponse.getResponseString());
			return;
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

		saCapabilityHelper.active(cloudStackParams);
		saAsyncHelper.active(cloudStackParams);
		//noticeRmiTest();
		//cmdbTestAdd();
		//cmdbTestRemove();
		//cmdbTestUpdate();
	}
	public Map<String, Object[]> getCloudStackParams(){
		Response loginResponse = loginCloudStack();
		if(log.isDebugEnabled()){
			log.debug(loginResponse.toString());
		}
		JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
		String userId = "";
		String sessionkey = "";
		try {
			jo = JSONObject.fromObject(jo.getString("loginresponse"));
			userId = jo.getString("userid");
			sessionkey = jo.getString("sessionkey");
		} catch (JSONException e) {
			log.error("login info is error, "+ loginResponse.getResponseString());
			return null;
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
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-service.xml");
		SASchedule saSchedule = (SASchedule) applicationContext.getBean("saSchedule");
		//saSchedule.active();
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
	public SAAsyncHelper getSaAsyncHelper() {
		return saAsyncHelper;
	}
	public void setSaAsyncHelper(SAAsyncHelper saAsyncHelper) {
		this.saAsyncHelper = saAsyncHelper;
	}
	public SACapabilityHelper getSaCapabilityHelper() {
		return saCapabilityHelper;
	}
	public void setSaCapabilityHelper(SACapabilityHelper saCapabilityHelper) {
		this.saCapabilityHelper = saCapabilityHelper;
	}
	public WorkOrderJobManagerImpl getWorkOrderJobManager() {
		return workOrderJobManager;
	}
	public void setWorkOrderJobManager(WorkOrderJobManagerImpl workOrderJobManager) {
		this.workOrderJobManager = workOrderJobManager;
	}
	public ScheduleService getVirtualMachineManager() {
		return virtualMachineManager;
	}
	public void setVirtualMachineManager(ScheduleService virtualMachineManager) {
		this.virtualMachineManager = virtualMachineManager;
	}
	public void cmdbTestAdd(){
//		JSONArray arrHosts=new JSONArray();
//    	JSONObject host=new JSONObject();
//    	host.put("name", "test-name");
//    	host.put("displayname", "test-displayname");
//    	//host.put("domain", "test-domain");
//    	host.put("instancename", "test-instanceName");
//    	host.put("hostname", "host1"); 
//    	host.put("memory","test-memory"); 
//    	host.put("cpunumber", "test-cpunumber"); 
//    	host.put("hypervisor", "test-hypervisor");
//    	host.put("state", "test-state");
//    	host.put("zonename", "test-zonename");    	   	
//    	host.put("ipaddress", "test-192.168.0.1"); 
//    	arrHosts.add(host);
//    	if(1==1)
//    	hostManagerService.saveVirtualCItoCMDB(arrHosts);
		JSONArray arrProject=new JSONArray();
    	JSONObject project=new JSONObject();
    	project.put("name", "test-name");
    	project.put("displayname", "test-displayname");
    	arrProject.add(project);
    	//if(1==1)
    	//hostManagerService.saveProjectCmdb(arrProject);
	}
	public void cmdbTestRemove(){
		JSONArray virtualMachineNames = new  JSONArray();
		JSONObject virtualMachine=new JSONObject();
		virtualMachine.put("instancename","test-instanceName");
		virtualMachine.put("name", "test-name");
		virtualMachineNames.add(virtualMachine);
    	if(1==1)
    	hostManagerService.deleteVirtualCItoCMDB(virtualMachineNames);
	}
	public void cmdbTestUpdate(){
		JSONObject json=new JSONObject();
		json.put("name", "test-name");
		json.put("hostname","cloudstack-mgt.hp.com");
		json.put("instancename","test-instanceName");
    	try {
			JSONObject jobresult=virtualMachineManager.migrateVirtualMachineCmdb(json);
		} catch (BuessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void noticeRmiTest(){
		RmiNotifyServiceBuilder build =new RmiNotifyServiceBuilder();
		build.addNotifyToDB(1, "test@hp.com", "你的id为的处理成功！",  "你的处理成功！详情请登录资源池管理平台查看！", null, null, null);
	}
	
}

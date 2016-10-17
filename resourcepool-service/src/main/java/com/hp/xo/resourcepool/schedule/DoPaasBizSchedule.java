package com.hp.xo.resourcepool.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.PaasDestoryObj;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.schedule.service.PaasDestoryObjManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;

@Service(value = "doPaasBizSchedule")
public class DoPaasBizSchedule {

	private static Logger logger = LoggerFactory
			.getLogger(DoPaasBizSchedule.class);

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	
	static String JOBSTATUS_PENDING="0";
	static String JOBSTATUS_SUCCEED="1";
	static String JOBSTATUS_FAIL="2";

	@Autowired
	private PaasDestoryObjManager paasDestoryObjManager;

	private Response loginCloudStack() {
		Map<String, Object[]> param = new HashMap<String, Object[]>();
		param.put("command", new Object[] { "login" });
		param.put("username",
				new Object[] { ServiceOptionUtil.obtainCloudStackUsername() });
		param.put("password",
				new Object[] { ServiceOptionUtil.obtainCloudStackPassword() });
		param.put("response", new Object[] { "json" });
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;
	}

	public void work() {

		logger.info("-----------------------------------处理PAAS操作失败的接口  begin--------------------------------------------------------");

		PaasDestoryObj po = new PaasDestoryObj();
		po.setExeResult(PaasDestoryObj.RESULT_FAIL);
		List<PaasDestoryObj> list = (List<PaasDestoryObj>) paasDestoryObjManager
				.findByExample(po);
		if (list != null && list.size() > 0) {

			Map<String, Object[]> cloudStackParams = initCloudStackParam();
			System.out.println(cloudStackParams);
			for (PaasDestoryObj obj : list) {
				// genericCloudServerManager.
				Map<String,Object[]> inputParams = new HashMap<String,Object[]>();
				inputParams.putAll(cloudStackParams);
				String paramStr = obj.getParamstr();
				if(StringUtils.isBlank(paramStr)){
					paramStr = "";
				}
				String[] params = paramStr.split("&");
				for(String p : params){
					String[] kvs = p.split("=");
					if(kvs.length==2){
						inputParams.put(kvs[0], new Object[]{kvs[1]});
					}
				}
				inputParams.put("command", new Object[]{obj.getCommand()});
				System.out.println(inputParams);
				try{
					Response response = genericCloudServerManager.get(inputParams);
					String responseStr = response.getResponseString();
					String responseHead = "";
					if(obj.getCommand().equalsIgnoreCase("deleteProject")){
						responseHead = "deleteprojectresponse";
					}else if(obj.getCommand().equalsIgnoreCase("destroyVirtualMachine")){
						responseHead = "destroyVirtualmachineresponse";
					}
					logger.info(response.getResponseString());
					JSONObject JSONObj = JSONObject
							.fromObject(response.getResponseString());
					
					if(response.getResponseString().indexOf("errorcode")>-1){
						logger.error(responseStr);
					}else{
						String commandresponseStr=JSONObj.getString(responseHead);
						JSONObject commandresponseObj=JSONObject.fromObject(commandresponseStr);
						this.jobDeal(cloudStackParams, commandresponseObj, obj);
					}
					
				}catch(Exception ex){
					ex.printStackTrace();
					logger.error(ex.getMessage());
				}
			}
			
		}
		logger.info("-----------------------------------处理PAAS操作失败的接口  end--------------------------------------------------------");
	}
	
	private void jobDeal(Map<String, Object[]> cloudStackParams,JSONObject commandresponseObj,PaasDestoryObj paasDestoryObj){
		String jobid =commandresponseObj.getString("jobid");
		cloudStackParams.put("command", new Object[]{"queryAsyncJobResult"});
		cloudStackParams.put("jobId", new Object[]{jobid});

		String jobstatus=this.JOBSTATUS_PENDING;
		JSONObject resultJSONObj=null;
				while(jobstatus.equalsIgnoreCase(this.JOBSTATUS_PENDING)){
					try{
						System.out.println("sleeping now");
						Thread.sleep(3000);
					}catch(Exception e){e.printStackTrace();}
					Response queryasyncjobresult2 = genericCloudServerManager
							.get(cloudStackParams);
					JSONObject queryasyncjobresultJSONObj2 = JSONObject
							.fromObject(queryasyncjobresult2.getResponseString());
					
					String queryasynjobresultStr2=queryasyncjobresultJSONObj2.toString();
						   JSONObject queryasynjobresultJSONObj2=JSONObject.fromObject(queryasynjobresultStr2);
							String resultStr2=queryasynjobresultJSONObj2.getString("queryasyncjobresultresponse");
							 resultJSONObj=JSONObject.fromObject(resultStr2);
							jobstatus=resultJSONObj.getString("jobstatus");
				}
				
				if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_SUCCEED)){
					paasDestoryObj.setExeResult(PaasDestoryObj.RESULT_SUCCESS);
					paasDestoryObj.setModifiedBy("admin");
					paasDestoryObj.setModifiedOn(new Date());
					paasDestoryObjManager.save(paasDestoryObj);
				}else if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_FAIL)){
					String jobresultcode=resultJSONObj.getString("jobresultcode");
					paasDestoryObj.setExeResult(PaasDestoryObj.RESULT_FAIL);
					paasDestoryObj.setModifiedBy("admin");
					paasDestoryObj.setModifiedOn(new Date());
					paasDestoryObj.setFailReason(resultJSONObj.toString());
					paasDestoryObjManager.save(paasDestoryObj);
					logger.error(resultJSONObj.toString());
				}
				
	}

	private Map<String, Object[]> initCloudStackParam() {
		Response loginResponse = loginCloudStack();
		if (loginResponse != null
				&& StringUtils.isNotBlank(loginResponse.getResponseString())) {
			JSONObject jo = JSONObject.fromObject(loginResponse
					.getResponseString());
			String userId = "";
			String sessionkey = "";
			try {
				jo = JSONObject.fromObject(jo.getString("loginresponse"));
				userId = jo.getString("userid");
				sessionkey = jo.getString("sessionkey");
			} catch (JSONException e) {
				logger.error("login info is error, "
						+ loginResponse.getResponseString());
				return null;
			}
			// String sessionKey = jo.getString("sessionkey");

			Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
			listUsersParams.put("command", new Object[] { "listUsers" });
			listUsersParams.put("response", new Object[] { "json" });
			listUsersParams.put("id", new Object[] { userId });

			Response listUsersResponse = genericCloudServerManager.get(
					listUsersParams, false);

			String apikey = "";
			String secretkey = "";
			if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
				jo = JSONObject.fromObject(listUsersResponse
						.getResponseString());
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
		} else {
			return null;
		}
	}

}

package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.service.Global4Alog;
import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.schedule.service.SAManager;
import com.hp.xo.resourcepool.schedule.service.ScheduleService;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderJobManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.DateUtil;
import com.hp.xo.resourcepool.utils.LogConstants;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;

public abstract class AbstracServiceImpl implements ScheduleService{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private WorkOrderManager orderManager = null;	
	@Autowired
	private WorkItemManager itemManager = null;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private ProvisionAttributeManager provisionAttributeManager = null;
	@Autowired
	private LogManager logManager;
	@Autowired
	private WorkOrderJobManager orderJobManager;
	
	 
	static String JOBSTATUS_SUCCEED="1";
	static String JOBSTATUS_PENDING="0";
	static String JOBSTATUS_FAIL="2";
	public LinkedHashMap<String,Object[]> cloneCloudStackParams(Map<String, Object[]> cloudStackParams){
		 Object[] apikey=cloudStackParams.get("apikey");
		 Object[] secretkey=cloudStackParams.get("secretkey");
		 Object[] sessionkey=cloudStackParams.get("sessionkey");
		 Object[] response=cloudStackParams.get("response");
		 LinkedHashMap<String,Object[]>  cloneCloudStackParams=new  LinkedHashMap<String,Object[]>();
		 cloneCloudStackParams.put("apikey", apikey);
		 cloneCloudStackParams.put("secretkey",secretkey);
		 cloneCloudStackParams.put("sessionkey", sessionkey);
		 cloneCloudStackParams.put("response", response);
		 return cloneCloudStackParams;
	}	
	
	public JSONArray getDestroyOrders(){
		List<Integer> typeList=new ArrayList<Integer>();
//		typeList.add(WorkOrder.TYPE_NEWPROJECTAPPLICATION);
		typeList.add(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE);//虚拟机
		typeList.add(WorkOrder.TYPE_SCALEVIRTUALMACHINE);//计算方案
		typeList.add(WorkOrder.TYPE_NEWVOLUME);//卷
	
		List<Integer> statusList=new ArrayList<Integer>();
		statusList.add(WorkOrder.STATUS_PROVISIONSUCCEED);
		List<WorkOrder> workOrderList=orderManager.listWorkOrderByTypeAndStatus(typeList, statusList);
		JSONArray jsonArray=new JSONArray();
		for(WorkOrder order:workOrderList){
			JSONObject json=getInfoByOrder(order);
			jsonArray.add(json);
		}
		return jsonArray;
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean destroy(JSONArray arr,Map<String, Object[]> cloudStackParams)throws BuessionException{
		if(arr==null || arr.isEmpty()){
			return false;
		}
		for(int index=0;index<arr.size();index++ ){
			JSONObject json  = arr.getJSONObject(index);
			destroyInstance(json,cloudStackParams);		
		}
		return true;
		
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean destroy(Map<String, Object[]> cloudStackParams)throws BuessionException{
		JSONArray jsonArray=this.getDestroyOrders();
		if(jsonArray==null || jsonArray.isEmpty()){
			return false;
		}
		for(int index=0;index<jsonArray.size();index++ ){
			JSONObject json  = jsonArray.getJSONObject(index);
			destroyInstance(json,cloudStackParams);		
		}
		return true;
		
	}
	public JSONObject destroyInstance(JSONObject json,Map<String, Object[]> cloudStackParams)throws BuessionException{
		JSONObject jobresult=new JSONObject();  
		jobresult.put("jobstatus",HttpStatus.SC_PAYMENT_REQUIRED);
		jobresult.put("jobstatus",false);
		jobresult.put("message", "删除虚拟机实例失败");
		if(json.isEmpty()){
			return jobresult;
		}
		Map<String,String> specificParams= this.getOrderTypeSpecificListParams();
		String commandName=specificParams.get("commandName");
		String responseHead=specificParams.get("responseHead");	
		Map<String, Object[]> cloneCloudStackParams=this.cloneCloudStackParams(cloudStackParams);
		putUserCredentialParams(cloneCloudStackParams, json);
		putProvisionListAttributes(cloneCloudStackParams, json);		
		cloneCloudStackParams.put("command", new Object[]{commandName});
		Response response = genericCloudServerManager.get(cloneCloudStackParams);
		JSONObject jsonObj = JSONObject.fromObject(response.getResponseString());
		jsonObj = JSONObject.fromObject(jsonObj.getString(responseHead));		
		String commandresponseStr=jsonObj.toString();
		if(commandresponseStr.indexOf("errorcode")>-1){
			jobresult.put("message", "删除虚拟机实例失败,虚拟机不存在");
			if(log.isDebugEnabled()){
				for(String key:cloudStackParams.keySet()){
					log.debug(key+" value is "+cloudStackParams.get(key)[0]);
				}
			}
			JSONObject createprojectresponseObj=JSONObject.fromObject(commandresponseStr);
			String errorcode =createprojectresponseObj.getString("errorcode");
			String errortext =createprojectresponseObj.getString("errortext");
			if(log.isInfoEnabled()){					
				log.info("处理失败, 失败原因："+errortext);
			}
			return jobresult;
		}
		JSONArray jos = jsonObj.getJSONArray(specificParams.get("jobResultresponseHead"));
		JSONObject instanceInfo=JSONObject.fromObject(jos.get(0));		
		if(instanceInfo==null || instanceInfo.isEmpty()){
			return jobresult;
		}		
		specificParams= this.getOrderTypeSpecificParams();
		commandName=specificParams.get("commandName");
		responseHead=specificParams.get("responseHead");	
		cloneCloudStackParams=this.cloneCloudStackParams(cloudStackParams);
		putUserCredentialParams(cloneCloudStackParams, json);
		putProvisionAttributes(cloneCloudStackParams, json);		
		cloneCloudStackParams.put("command", new Object[]{commandName});
		response = genericCloudServerManager.get(cloneCloudStackParams);
		
		jsonObj = JSONObject.fromObject(response.getResponseString());
		final String resultResponseStr=jsonObj.getString(responseHead);
		final Map<String, Object[]> destroyCloudStackParams=cloudStackParams;
		final JSONObject vmInstance=instanceInfo;
		final Map<String,String> vmSpecificParams=specificParams;
//		Thread deleteCMDBThread=new Thread(){
//			public void run() {
//				if(resultResponseStr.indexOf("errorcode")>-1){
//					if(log.isDebugEnabled()){
//						for(String key:destroyCloudStackParams.keySet()){
//							log.debug(key+" value is "+destroyCloudStackParams.get(key)[0]);
//						}
//					}
//					JSONObject createprojectresponseObj=JSONObject.fromObject(resultResponseStr);
//					String errorcode =createprojectresponseObj.getString("errorcode");
//					String errortext =createprojectresponseObj.getString("errortext");
//					if(log.isInfoEnabled()){					
//						log.info("处理虚拟机CMDB信息失败, 失败原因："+errortext);
//					}
//					
//				}		
//				jobDeal(destroyCloudStackParams,vmSpecificParams,vmInstance,JSONObject.fromObject(resultResponseStr));
//				if(log.isInfoEnabled()){					
//					log.info("成功处理虚拟机CMDB信息");
//				}			
//		    } 
//		};
//		deleteCMDBThread.start();
		if(jobDeal(destroyCloudStackParams,vmSpecificParams,vmInstance,JSONObject.fromObject(resultResponseStr))){
			jobresult.put("jobstatus",true);
			jobresult.put("message", "成功删除虚拟机实例");
		}
		
		logManager.log("",LogConstants.RESOURCE_RECYCLE,  LogConstants.DELETE, ServiceOptionUtil.obtainCloudStackUsername(), "删除/回收实例",jobresult.getBoolean("jobstatus")?LogConstants.SUCCESS:LogConstants.FAIL, jobresult.getString("message"));
		
		// 4A纳入日志 -- change by lixinqi
		Global4Alog global4Alog=new Global4Alog();
		global4Alog.setIdentityName("4AZYCLog");
		global4Alog.setResourceKind("1");
		global4Alog.setResourceCode("HANGZYC");
		global4Alog.setIdrCreationTime(DateUtil.getNowSystemTimeStr());
		global4Alog.setOperateTime(DateUtil.getNowSystemTimeStr());
		global4Alog.setOpTypeId("1-HAZYC-10019");
		global4Alog.setOpTypeName("资源池管理平台虚机管理");
		global4Alog.setOpLevelId("3");
		global4Alog.setOperateResult("0");
		global4Alog.setClientAddress("");
		StringBuilder utilLogStr = new StringBuilder("");
		utilLogStr.append("clientIP-客户端调用IP：").append("");
		utilLogStr.append("，module-模块：").append(LogConstants.RESOURCE_RECYCLE);
		utilLogStr.append("，operation-操作：").append(LogConstants.DELETE);
		utilLogStr.append("，username-操作用户名：").append(ServiceOptionUtil.obtainCloudStackUsername());
		utilLogStr.append("，content-操作内容：删除/回收实例");
		utilLogStr.append("，result-操作结果：").append(jobresult.getBoolean("jobstatus")?LogConstants.SUCCESS:LogConstants.FAIL);
		utilLogStr.append("，info-操作结果原因描述：").append(jobresult.getString("message"));
		global4Alog.setOperateContent("删除/回收虚拟机实例！"+utilLogStr.toString());
		
		return jobresult;
	}
	public boolean jobDeal(Map<String, Object[]> cloudStackParams,Map<String,String> specificParams,JSONObject instanceInfo,JSONObject commandresponseStr){
		String jobid =commandresponseStr.getString("jobid");
		cloudStackParams.put("command", new Object[]{"queryAsyncJobResult"});
		cloudStackParams.put("jobId", new Object[]{jobid});
		String jobstatus=this.JOBSTATUS_PENDING;
		String jobInstanceId =instanceInfo.getString("id");
		JSONObject resultJSONObj=null;	
		
		while(jobstatus.equalsIgnoreCase(this.JOBSTATUS_PENDING)){
			try{
				Thread.sleep(3000);
			}catch(Exception e){
				e.printStackTrace();
			}
			Response queryasyncjobresult2 = genericCloudServerManager.get(cloudStackParams);
			String queryasynjobresultStr2=queryasyncjobresult2.getResponseString();
			if(queryasynjobresultStr2.contains("null")){
				queryasynjobresultStr2=queryasynjobresultStr2.replaceAll("null", "kkkk");
			}
		    JSONObject queryasynjobresultJSONObj2=JSONObject.fromObject(queryasynjobresultStr2);
			String resultStr2=queryasynjobresultJSONObj2.getString("queryasyncjobresultresponse");
			resultJSONObj=JSONObject.fromObject(resultStr2);
			jobstatus=resultJSONObj.getString("jobstatus");
		}
				
		if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_SUCCEED)){
			List<WorkOrderJob> list	= orderJobManager.findWorkOrderJobByInstanceId(jobInstanceId);
			for(WorkOrderJob workOrderjob :list){
				WorkOrder order =orderManager.get(workOrderjob.getWorkOrderId());
				order.setStatus(WorkOrder.STATUS_INSTANCES_END);
				orderManager.save(order);
			}
			jobSucceedDeal(cloudStackParams, instanceInfo, resultJSONObj, specificParams);
			jobSucceedPost(cloudStackParams,instanceInfo,resultJSONObj,specificParams);
		}else if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_FAIL)){
			String jobresultcode=resultJSONObj.getString("jobresultcode");
			String jobresult=resultJSONObj.getString("jobresult");
			log.info("处理失败, 失败原因："+jobresult);
		
		}
		return jobstatus.equalsIgnoreCase(this.JOBSTATUS_SUCCEED);
	}
	/**
	 * 订单相关的处理
	 * @param cloudStackParams
	 * @param workOrder
	 * @param resultJSONObj
	 * @param woSpecificParams
	 */
	public void jobSucceedDeal(Map<String, Object[]> cloudStackParams,JSONObject json, JSONObject resultJSONObj,Map<String, String> woSpecificParams){
		
	}
	/**
	 * cmdb相关的处理
	 * @param cloudStackParams
	 * @param workOrder
	 * @param resultJSONObj
	 * @param woSpecificParams
	 */
	public boolean jobSucceedPost(Map<String, Object[]> cloudStackParams,JSONObject json, JSONObject resultJSONObj,Map<String, String> woSpecificParams){
		return true;
	}
	/**
	 * 设置属性信息
	 * @param cloudStackParams
	 * @param json
	 */
	public void putProvisionAttributes(Map<String, Object[]> cloudStackParams,JSONObject json){
		
	}
	public void putProvisionListAttributes(Map<String, Object[]> cloudStackParams,JSONObject json){
		
	}
	/**
	 * 设置权限信息
	 * @param cloudStackParams
	 * @param json
	 */
	public void putUserCredentialParams(Map<String, Object[]> cloudStackParams,JSONObject json) {
		this.putUserCredentialParams(cloudStackParams,ServiceOptionUtil.obtainCloudStackUserId());
	}
	protected void putUserCredentialParams(	Map<String, Object[]> cloudStackParams, String userId) {
		Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
		listUsersParams.put("command", new Object[] { "listUsers" });
		listUsersParams.put("response", new Object[] { "json" });
		listUsersParams.put("accounttype",new Object[] { String.valueOf(ActiveUser.ADMIN.intValue()) });// 帐户类型
		Response listUsersResponse = genericCloudServerManager.get(listUsersParams, false);
		if(log.isDebugEnabled()){
			log.debug("userid is " + userId);
			log.debug("response is " + listUsersResponse.toString());
		}
		String apikey = "";
		String secretkey = "";
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			JSONObject jo = JSONObject.fromObject(listUsersResponse.getResponseString());
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			apikey = jo.getString("apikey");
			secretkey = jo.getString("secretkey");
		}
		cloudStackParams = new HashMap<String, Object[]>();
		cloudStackParams.put("apikey", new Object[] { apikey });
		cloudStackParams.put("secretkey", new Object[] { secretkey });
		cloudStackParams.put("response", new Object[] { "json" });
	}
	protected abstract JSONObject getInfoByOrder(WorkOrder order);
	public abstract Map<String, String> getOrderTypeSpecificListParams();
	public abstract Map<String, String> getOrderTypeSpecificParams();

	
	public WorkOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public WorkItemManager getItemManager() {
		return itemManager;
	}
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
	public ProvisionAttributeManager getProvisionAttributeManager() {
		return provisionAttributeManager;
	}
	public void setProvisionAttributeManager(ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	public LogManager getLogManager() {
		return logManager;
	}
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
	
}

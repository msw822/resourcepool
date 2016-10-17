package com.hp.xo.resourcepool.schedule.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
























import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.service.Global4Alog;
import com.hp.service.GlobalLogUtil;
import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.rmi.RmiNotifyServiceBuilder;
import com.hp.xo.resourcepool.schedule.service.SAManager;
import com.hp.xo.resourcepool.service.GlobalSettingManager;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.DateUtil;
import com.hp.xo.resourcepool.utils.LogConstants;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;
import com.hp.xo.resourcepool.utils.StringUtil;
@Service(value="saAsyncHelper")
//@Transactional(propagation=Propagation.REQUIRED)
public class SAAsyncHelper {
	private final Logger log = Logger.getLogger(this.getClass());
	static String JOBSTATUS_PENDING="0";
	static String JOBSTATUS_SUCCEED="1";
	static String JOBSTATUS_FAIL="2";
	static int EXPIRE_NOTICE_DAYS = 20;//订单到期提前多少天通知
	static int EXPIRE_DESTORY_DAYS = -3; //订单过期几天销毁
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private SAManager saNewProjectImpl;
	@Autowired
	private SAManager saNewVolumeImpl;
	@Autowired
	private SAManager saResizeVolumeImpl;
	@Autowired
	private SAManager saDeployVMImpl;
	@Autowired
    private SAManager saScaleVMImpl;
	@Autowired
	private WorkItemManager itemManager;
	@Autowired
	private WorkOrderManager orderManager;	
	@Autowired
	private VirtualMachineManagerImpl virtualMachineManager;	
	@Autowired
	private LogManager logManager;
	
	@Autowired
	private GlobalSettingManager globalSettingManager;
	
	public LinkedHashMap<String,Object[]> refreshCloudStackParams(Map<String, Object[]> cloudStackParams){
		 Object[] apikey=cloudStackParams.get("apikey");
		 Object[] secretkey=cloudStackParams.get("secretkey");
		 Object[] sessionkey=cloudStackParams.get("sessionkey");
		 Object[] response=cloudStackParams.get("response");
		 LinkedHashMap<String,Object[]>  cloudStackParams2=new  LinkedHashMap<String,Object[]>();
		cloudStackParams2.put("apikey", apikey);
		cloudStackParams2.put("secretkey",secretkey);
		cloudStackParams2.put("sessionkey", sessionkey);
		cloudStackParams2.put("response", response);
		return cloudStackParams2;
	}
	
	
	public void active(Map<String,Object[]> cloudStackParams){
	
		List<Integer> typeList=new ArrayList<Integer>();
		typeList.add(WorkOrder.TYPE_NEWPROJECTAPPLICATION);
		typeList.add(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE);//虚拟机
		typeList.add(WorkOrder.TYPE_SCALEVIRTUALMACHINE);//计算方案
		typeList.add(WorkOrder.TYPE_NEWVOLUME);//卷
		typeList.add(WorkOrder.TYPE_RESIZEVOLUME);
		
		List<Integer> statusList=new ArrayList<Integer>();
		statusList.add(WorkOrder.STATUS_APPROVED);
		List<WorkOrder> workOrderList=orderManager.listWorkOrderByTypeAndStatus(typeList, statusList);
		for(WorkOrder workOrder:workOrderList){
			workOrder.setStatus(WorkOrder.STATUS_PROVISIONING);
			orderManager.save(workOrder);
			SAManager saManager=this.getSAManager(workOrder.getWorkOrderType());
			Map<String,String> woSpecificParams =saManager.getOrderTypeSpecificParams();
			String commandName=woSpecificParams.get("commandName");
			String responseHead=woSpecificParams.get("responseHead");		
			cloudStackParams=this.refreshCloudStackParams(cloudStackParams);
			saManager.putUserCredentialParams(cloudStackParams, workOrder);			
			saManager.getProvisionAttributes(cloudStackParams, workOrder);
			cloudStackParams.put("command", new Object[]{commandName});
			Response response = genericCloudServerManager.get(cloudStackParams);
			JSONObject JSONObj = JSONObject.fromObject(response.getResponseString());
			String commandresponseStr=JSONObj.getString(responseHead);
		
			// 4A纳入日志 -- change by lixinqi
    		Global4Alog global4Alog=new Global4Alog();
    		global4Alog.setIdentityName("4AZYCLog");
    		global4Alog.setResourceKind("1");
    		global4Alog.setResourceCode("HANGZYC");
    		global4Alog.setIdrCreationTime(DateUtil.getNowSystemTimeStr());
    		global4Alog.setOperateTime(DateUtil.getNowSystemTimeStr());
    		global4Alog.setOpTypeId("1-HAZYC-10018");
    		global4Alog.setOpTypeName("资源池管理平台订单管理");
    		global4Alog.setOpLevelId("3");
    		global4Alog.setOperateResult("0");
    		global4Alog.setClientAddress("");
    		
			if(commandresponseStr.indexOf("errorcode")>-1){
				if(log.isDebugEnabled()){
					for(String key:cloudStackParams.keySet()){
						log.debug(key+" value is "+cloudStackParams.get(key)[0]);
					}
				}			
				JSONObject createprojectresponseObj=JSONObject.fromObject(commandresponseStr);
				String errorcode =createprojectresponseObj.getString("errorcode");
				String errortext =createprojectresponseObj.getString("errortext");
				workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
				workOrder.setErrorCode(errorcode);
				workOrder.setErrorText(errortext);
				orderManager.save(workOrder);
				logManager.log("",LogConstants.RESOURCE_CREATE,  LogConstants.CREATE, ServiceOptionUtil.obtainCloudStackUsername(), "订单处理：类型："+workOrder.getWorkOrderTypeName() +" id:"+workOrder.getId(),LogConstants.FAIL, errortext);
//				System.out.print("处理失败"+workOrder.getApplierName()+"申请的订单类型："+workOrder.getWorkOrderType()+" 订单号："+workOrder.getId()+" 失败原因："+errortext);
				if(log.isInfoEnabled()){					
					log.info("处理失败"+workOrder.getApplierName()+"申请的订单类型："+workOrder.getWorkOrderType()+" 订单号："+workOrder.getId()+" 失败原因："+errortext);
				}	
				
				//邮件通知处理失败
				String email = "";
				cloudStackParams.put("command", new Object[]{"listUsers"});
				cloudStackParams.put("id", new Object[]{workOrder.getApplierId()});
				Response listUsersResponse = genericCloudServerManager.get(cloudStackParams);	
				if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
					JSONObject js = JSONObject.fromObject(listUsersResponse.getResponseString());	
					JSONObject	jo = JSONObject.fromObject(js);
					jo = JSONObject.fromObject(jo.getString("listusersresponse"));
					JSONArray jos = jo.getJSONArray("user");
					jo = JSONObject.fromObject(jos.get(0));
					email = jo.getString("email");
				}
				if(StringUtil.isNotEmpty(email)){
					RmiNotifyServiceBuilder build =new RmiNotifyServiceBuilder();
					build.addNotifyToDB(1, email, "你的id为"+workOrder.getId()+"的"+workOrder.getWorkOrderTypeName()+"处理失败！",  "你的"+workOrder.getWorkOrderTypeName()+"处理失败！原因："+errorcode+",详情请登录资源池管理平台查看！", null, null, null);
				}
				// 4A纳入日志 -- change by lixinqi
				StringBuilder utilLogStr = new StringBuilder("");
				utilLogStr.append("clientIP-客户端调用IP：").append("");
				utilLogStr.append("，module-模块：").append(LogConstants.RESOURCE_CREATE);
				utilLogStr.append("，operation-操作：").append(LogConstants.CREATE);
				utilLogStr.append("，username-操作用户名：").append(ServiceOptionUtil.obtainCloudStackUsername());
				utilLogStr.append("，content-操作内容：{订单处理类型：").append(workOrder.getWorkOrderTypeName()).append("，订单处理id：").append(workOrder.getId()).append("}");
        		utilLogStr.append("，result-操作结果：").append(LogConstants.FAIL);
        		utilLogStr.append("，info-操作结果原因描述：").append(errortext);
				global4Alog.setOperateContent("处理订单失败！"+utilLogStr.toString());
				
			}else{				
				JSONObject commandresponseObj=JSONObject.fromObject(commandresponseStr);
				this.jobDeal(cloudStackParams,woSpecificParams, workOrder, commandresponseObj,saManager);				
//				System.out.print("成功处理"+workOrder.getApplierName()+"申请的订单类型："+workOrder.getWorkOrderType()+" 订单号："+workOrder.getId());
				if(log.isInfoEnabled()){					
					log.info("成功处理"+workOrder.getApplierName()+"申请的订单类型："+workOrder.getWorkOrderType()+" 订单号："+workOrder.getId());
				}
				logManager.log("",LogConstants.RESOURCE_CREATE,  LogConstants.CREATE, ServiceOptionUtil.obtainCloudStackUsername(), "订单处理：类型："+workOrder.getWorkOrderTypeName() +" id:"+workOrder.getId(),LogConstants.SUCCESS,"成功处理" );
				
				// 4A纳入日志 -- change by lixinqi
				StringBuilder utilLogStr = new StringBuilder("");
				utilLogStr.append("clientIP-客户端调用IP：").append("");
				utilLogStr.append("，module-模块：").append(LogConstants.RESOURCE_CREATE);
				utilLogStr.append("，operation-操作：").append(LogConstants.CREATE);
				utilLogStr.append("，username-操作用户名：").append(ServiceOptionUtil.obtainCloudStackUsername());
				utilLogStr.append("，content-操作内容：{订单处理类型：").append(workOrder.getWorkOrderTypeName()).append("，订单处理id：").append(workOrder.getId()).append("}");
        		utilLogStr.append("，result-操作结果：").append(LogConstants.SUCCESS);
        		utilLogStr.append("，info-操作结果原因描述：").append("成功处理");
				global4Alog.setOperateContent("处理订单成功！"+utilLogStr.toString());
			}
			
    		GlobalLogUtil.getInstance().addGlobal4Alog(global4Alog);
		}
//		System.out.print("成功处理"+workOrderList.size()+"条数据");
		if(log.isInfoEnabled()){					
			log.info("成功处理"+workOrderList.size()+"条数据");
		}	
	}
	public void expire(Map<String,Object[]> cloudStackParams) throws BuessionException{	
		long dd = Long.valueOf(globalSettingManager.getValueByKey("dueNoticeDay"));
		List<Integer> typeList=new ArrayList<Integer>();
		typeList.add(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE);//实例申请
		List<Integer> statusList1=new ArrayList<Integer>();
		statusList1.add(WorkOrder.STATUS_PROVISIONSUCCEED);//处理成功
		List<WorkOrder> workOrderList1=orderManager.listWorkOrderByTypeAndStatus(typeList, statusList1);
		for(WorkOrder workOrder:workOrderList1){
			if(workOrder.getWorkorder_due_date()!=null){
				Date date = workOrder.getWorkorder_due_date();
				long days = (date.getTime()-System.currentTimeMillis())/(3600 * 24 * 1000);
				//String dueNoticeDay = ServiceOptionUtil.getValue("instance.due.notice.days", "18");
				long dueNoticeDays = Long.valueOf(globalSettingManager.getValueByKey("dueNoticeDay"));
				if(days == dueNoticeDays || days == 1){
					expireNotice(cloudStackParams, workOrder,days);
				}
			}
		}
		List<Integer> statusList2=new ArrayList<Integer>();
		statusList2.add(WorkOrder.STATUS_INSTANCES_RECYCLE);//状态待回收
		List<WorkOrder> workOrderList2=orderManager.listWorkOrderByTypeAndStatus(typeList, statusList2);
		String instanceId="";
	
			for(WorkOrder workOrder:workOrderList2){
				List<WorkOrderJob> workOrderJobList = workOrder.getWorkOrderJob();
				if(!workOrderJobList.isEmpty()){
					instanceId = workOrderJobList.get(0).getJobInstanceId();
					boolean jobSuccess = expireDestory(cloudStackParams,instanceId);
					if(jobSuccess){
//						workOrder.setStatus(WorkOrder.STATUS_INSTANCES_END);
//						orderManager.save(workOrder);
						System.out.println("删除成功");
					}else{
						System.out.println("删除失败");
					}
				}else{
					System.out.println("没有实例");
				}
			}
		
	}
	private boolean expireDestory(Map<String, Object[]> cloudStackParams,String instanceId) throws BuessionException {
		
		JSONObject json=new JSONObject();
		json.put("virtualMachineId", instanceId);
		json.put("expunge",true);
		JSONObject jobresult = virtualMachineManager.destroyInstance(json, cloudStackParams);
		return jobresult.getBoolean("jobstatus");		
	}		
	


	private void expireNotice(Map<String, Object[]> cloudStackParams,
			WorkOrder workOrder,long days) {
		// TODO Auto-generated method stub
		String email = "";
		cloudStackParams.put("command", new Object[]{"listUsers"});
		cloudStackParams.put("id", new Object[]{workOrder.getApplierId()});
		Response listUsersResponse = genericCloudServerManager.get(cloudStackParams);	
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			JSONObject JSONObj = JSONObject.fromObject(listUsersResponse.getResponseString());	
			JSONObject	jo = JSONObject.fromObject(JSONObj);
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			email = jo.getString("email");
		}
		System.out.println("email:---------" + email);
		if(StringUtil.isNotEmpty(email)){
			RmiNotifyServiceBuilder build =new RmiNotifyServiceBuilder();
			build.addNotifyToDB(1, email, "你申请的虚机还有"+days+"天就要过期,如你不及时续订,将会被回收", "详情请登录资源池平台查看!", null, null, null);
		}
	}


	public void jobDeal(Map<String, Object[]> cloudStackParams,Map<String,String> woSpecificParams,WorkOrder workOrder,JSONObject commandresponseObj,SAManager saManager ){
		cloudStackParams=this.refreshCloudStackParams(cloudStackParams);
		String jobid =commandresponseObj.getString("jobid");
		cloudStackParams.put("command", new Object[]{"queryAsyncJobResult"});
		cloudStackParams.put("jobId", new Object[]{jobid});
		String jobstatus=this.JOBSTATUS_PENDING;
		JSONObject resultJSONObj=null;
		while(jobstatus.equalsIgnoreCase(this.JOBSTATUS_PENDING)){
			try{
				Thread.sleep(3000);
			}catch(Exception e){
				e.printStackTrace();
			}
			Response queryasyncjobresult2 = genericCloudServerManager.get(cloudStackParams);
			JSONObject queryasyncjobresultJSONObj2 = JSONObject.fromObject(queryasyncjobresult2.getResponseString());			
			String queryasynjobresultStr2=queryasyncjobresultJSONObj2.toString();
		    JSONObject queryasynjobresultJSONObj2=JSONObject.fromObject(queryasynjobresultStr2);
			String resultStr2=queryasynjobresultJSONObj2.getString("queryasyncjobresultresponse");
			resultJSONObj=JSONObject.fromObject(resultStr2);
			jobstatus=resultJSONObj.getString("jobstatus");
		}
		//email通知订单处理情况
		String email = "";
		cloudStackParams.put("command", new Object[]{"listUsers"});
		cloudStackParams.put("id", new Object[]{workOrder.getApplierId()});
		Response listUsersResponse = genericCloudServerManager.get(cloudStackParams);	
		if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
			JSONObject JSONObj = JSONObject.fromObject(listUsersResponse.getResponseString());	
			JSONObject	jo = JSONObject.fromObject(JSONObj);
			jo = JSONObject.fromObject(jo.getString("listusersresponse"));
			JSONArray jos = jo.getJSONArray("user");
			jo = JSONObject.fromObject(jos.get(0));
			if(jo.containsKey("email"));
				email = jo.getString("email");
		}
		if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_SUCCEED)){
		
			if(StringUtil.isNotEmpty(email)){
				RmiNotifyServiceBuilder build =new RmiNotifyServiceBuilder();
				build.addNotifyToDB(1, email, "你的id为"+workOrder.getId()+"的"+workOrder.getWorkOrderTypeName()+"处理成功！",  "你的"+workOrder.getWorkOrderTypeName()+"处理成功！详情请登录资源池管理平台查看！", null, null, null);
			}
			saManager.jobSucceedDeal(cloudStackParams, workOrder, resultJSONObj, woSpecificParams);
			saManager.jobSucceedPost(cloudStackParams,workOrder,resultJSONObj,woSpecificParams);
		}else if(jobstatus.equalsIgnoreCase(this.JOBSTATUS_FAIL)){
			String jobresultcode=resultJSONObj.getString("jobresultcode");
			String jobresult=resultJSONObj.getString("jobresult");
			workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
			workOrder.setErrorCode(jobresultcode);
			workOrder.setErrorText(jobresult);
			orderManager.save(workOrder);
			if(StringUtil.isNotEmpty(email)){
				RmiNotifyServiceBuilder build =new RmiNotifyServiceBuilder();
				build.addNotifyToDB(1, email, "你的id为"+workOrder.getId()+"的"+workOrder.getWorkOrderTypeName()+"处理失败！",  "你的"+workOrder.getWorkOrderTypeName()+"处理失败！原因："+jobresultcode+",详情请登录资源池管理平台查看！", null, null, null);
			}
		
		}
				
	}
	
	public SAManager getSAManager(int workOrderType){
		if(workOrderType==WorkOrder.TYPE_NEWPROJECTAPPLICATION) return saNewProjectImpl;
		else if(workOrderType==WorkOrder.TYPE_DEPLOYVIRTUALMACHINE) return saDeployVMImpl;
		else if(workOrderType==WorkOrder.TYPE_SCALEVIRTUALMACHINE) return saScaleVMImpl;
		else if(workOrderType==WorkOrder.TYPE_NEWVOLUME) return saNewVolumeImpl;
		else if(workOrderType==WorkOrder.TYPE_RESIZEVOLUME) return saResizeVolumeImpl;
		else return null;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
	
	public SAManager getSaNewProjectImpl() {
		return saNewProjectImpl;
	}
	
	public void setSaNewProjectImpl(SAManager saNewProjectImpl) {
		this.saNewProjectImpl = saNewProjectImpl;
	}
	
	public SAManager getSaNewVolumeImpl() {
		return saNewVolumeImpl;
	}
	
	public void setSaNewVolumeImpl(SAManager saNewVolumeImpl) {
		this.saNewVolumeImpl = saNewVolumeImpl;
	}
	
	public SAManager getSaDeployVMImpl() {
		return saDeployVMImpl;
	}
	
	public void setSaDeployVMImpl(SAManager saDeployVMImpl) {
		this.saDeployVMImpl = saDeployVMImpl;
	}
	
	public SAManager getSaScaleVMImpl() {
		return saScaleVMImpl;
	}
	
	public void setSaScaleVMImpl(SAManager saScaleVMImpl) {
		this.saScaleVMImpl = saScaleVMImpl;
	}
	
	public WorkItemManager getItemManager() {
		return itemManager;
	}
	
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}
	
	public WorkOrderManager getOrderManager() {
		return orderManager;
	}
	
	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}


	public VirtualMachineManagerImpl getVirtualMachineManager() {
		return virtualMachineManager;
	}


	public void setVirtualMachineManager(
			VirtualMachineManagerImpl virtualMachineManager) {
		this.virtualMachineManager = virtualMachineManager;
	}


	public LogManager getLogManager() {
		return logManager;
	}


	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}


	public GlobalSettingManager getGlobalSettingManager() {
		return globalSettingManager;
	}


	public void setGlobalSettingManager(GlobalSettingManager globalSettingManager) {
		this.globalSettingManager = globalSettingManager;
	}
	
	
}

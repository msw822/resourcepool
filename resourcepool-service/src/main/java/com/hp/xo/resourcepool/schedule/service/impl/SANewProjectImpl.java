package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.ResourcePoolPermission;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.rmi.RmiPhysicsHostBuilder;
import com.hp.xo.resourcepool.schedule.SASchedule;
import com.hp.xo.resourcepool.schedule.service.ResourcePoolPermissionManager;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;

@Service(value="saNewProjectImpl")
public class SANewProjectImpl extends AbstractSAManager {
	private final Logger log = LoggerFactory.getLogger(SANewProjectImpl.class);
	static String PRODUCTIONPOOL="R_01";
	static String TESTINGPOOL="R_02";
	static String ALLPOOL="all";	
	static String RESOURCEPOOLPERMISSIONPERMIT="1";
	static String RESOURCEPOOLPERMISSIONREJECT="0";
	static int OWNERTYPEPROJECT=3;

//	@Autowired
	private ResourcePoolPermissionManager resourcePoolPermissionManager;
	
	@Autowired
	private WorkOrderManager orderManager=null;
	@Autowired
	private WorkItemManager itemManager=null;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private HostManagerService hostManagerService;
	
	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		Map<String,String> woSpecificParams =new LinkedHashMap();
		woSpecificParams.put("commandName", "createProject");
		woSpecificParams.put("responseHead", "createprojectresponse");
		woSpecificParams.put("jobResultresponseHead", "project");
		return woSpecificParams; 
	}
	
	public void saveResourcePoolPermission(Map<String, Object[]> cloudStackParams,String projectid,String projectname,WorkOrder workOrder){
		ResourcePoolPermission p=new ResourcePoolPermission();
		p.setOwnerId(projectid);
		p.setOwnerName(projectname);
		p.setOwnerType(this.OWNERTYPEPROJECT);
		String resourcePoolId="";
		WorkItem wiRP=new WorkItem();
		wiRP.setWorkOrderId(workOrder.getId());
		wiRP.setAttributeName("resourcePoolId");
		
		List<WorkItem> wiRPl=(List<WorkItem>) itemManager.findByExample(wiRP);
		Collections.sort(wiRPl, new Comparator(){
			public int compare(Object o1, Object o2) {  
		        WorkItem s1=(WorkItem)o1;  
		        WorkItem s2=(WorkItem)o2;  
		        
		        if(s1.getStep()<s2.getStep()){  
		            return -1;  
		        }  
		        if(s1.getStep()>s2.getStep()){  
		            return 1;  
		        }  
		        return 0;  
		    }  
		});  
		cloudStackParams =this.refreshCloudStackParams(cloudStackParams);
		for(WorkItem wi:wiRPl){
			resourcePoolId=wi.getAttributeValue();
		}
		
		if(this.PRODUCTIONPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONPERMIT));
			p.setTestingPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONREJECT));	
		}
		else if(this.TESTINGPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONREJECT));
			p.setTestingPool(Integer.parseInt(this.RESOURCEPOOLPERMISSIONPERMIT));	
		}
		else if(this.ALLPOOL.equalsIgnoreCase(resourcePoolId)){
			p.setProductionPool(Integer.parseInt(RESOURCEPOOLPERMISSIONPERMIT));
			p.setTestingPool(Integer.parseInt(RESOURCEPOOLPERMISSIONPERMIT));	
			
		}
		p.setCreatedBy("admin");
		p.setCreatedOn(new Date());
		
		resourcePoolPermissionManager.save(p);
	
		
		
	}
	
	public void updateResourceLimit(Map<String, Object[]> cloudStackParams,String projectid,WorkOrder workOrder){
		LinkedHashMap<String,String> map=this.getCapabilityMap();
		for(String capabilityName:map.keySet()){
			WorkItem wiExample=new WorkItem();
			wiExample.setWorkOrderId(workOrder.getId());
			wiExample.setAttributeName(capabilityName);
			//wiExample.setStep(2);
			
			List<WorkItem> wil=(List<WorkItem>) itemManager.findByExample(wiExample);
			Collections.sort(wil, new Comparator(){
				public int compare(Object o1, Object o2) {  
			        WorkItem s1=(WorkItem)o1;  
			        WorkItem s2=(WorkItem)o2;  
			        
			        if(s1.getStep()<s2.getStep()){  
			            return -1;  
			        }  
			        if(s1.getStep()>s2.getStep()){  
			            return 1;  
			        }  
			        return 0;  
			    }  
			});  
			cloudStackParams =this.refreshCloudStackParams(cloudStackParams);
			for(WorkItem wi:wil){
				String attributeValue=wi.getAttributeValue();
				cloudStackParams.put("max", new Object[]{attributeValue});
			}
		
			cloudStackParams.put("command", new Object[]{"updateResourceLimit"});
			cloudStackParams.put("resourcetype", new Object[]{map.get(capabilityName)});
			cloudStackParams.put("projectid", new Object[]{projectid});
			
			Response updateLimitResponse = genericCloudServerManager.get(cloudStackParams);
			JSONObject updateLimitResponseJSONObj = JSONObject.fromObject(updateLimitResponse.getResponseString());
			
			
			String updateresourcelimitresponseStr=updateLimitResponseJSONObj.getString("updateresourcelimitresponse");
			if(updateresourcelimitresponseStr.indexOf("errorcode")>-1){
				JSONObject updateresourcelimitresponseObj=JSONObject.fromObject(updateresourcelimitresponseStr);
				String errorcode =updateresourcelimitresponseObj.getString("errorcode");
				String errortext =updateresourcelimitresponseObj.getString("errortext");
				workOrder.setStatus(WorkOrder.STATUS_PROVISONFAIL);//处理失败
				orderManager.save(workOrder);
				saveWorkItem(workOrder.getId(),3,"errorcode",errorcode);
				saveWorkItem(workOrder.getId(),3,"errortext",errortext);
				
				this.refreshCloudStackParams(cloudStackParams);
				cloudStackParams.put("command", new Object[]{"deleteProject"});
				cloudStackParams.put("id", new Object[]{projectid});
				Response deleteProjectResponse = genericCloudServerManager.get(cloudStackParams);				
				JSONObject deleteProjectResponseSSS = JSONObject.fromObject(deleteProjectResponse.getResponseString());
				
				break;
	
			}
		}
	}

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
	public LinkedHashMap<String, String> getCapabilityMap(){
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		map.put("instance", "0");
		map.put("ip", "1");
		map.put("volume", "2");
		map.put("snapshot", "3");
		map.put("template", "4");
		map.put("network", "6");
		map.put("vpc", "7");
		map.put("cpu", "8");
		map.put("memory", "9");
		map.put("primaryStorage", "10");
		map.put("secondaryStorage", "11");			
		return map;
		
	}
	public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams){
		System.out.println("command succeeded");
		JSONObject jobresultJO=resultJSONObj.getJSONObject("jobresult");
		String jobResultresponseHead=woSpecificParams.get("jobResultresponseHead");

		JSONObject resultHeadJO=jobresultJO.getJSONObject(jobResultresponseHead);
		String id=resultHeadJO.getString("id");
		String projectname=resultHeadJO.getString("name");
		//this.saveResourcePoolPermission(cloudStackParams, id, projectname, workOrder);
		workOrder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);//成功
		orderManager.save(workOrder);
		saveWorkItem(workOrder.getId(),3,"ID",id);//id改为ID edit by@ma 10-20
		this.updateResourceLimit(cloudStackParams, id,  workOrder);			
	}
	/**
     * 订单后处理
     * @param cloudStackParams
     * @param workOrder
     * @return
     */
    public boolean jobSucceedPost(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject commandresponseObj,Map<String, String> woSpecificParams){

    	JSONArray arrProject=new JSONArray();
    	JSONObject project=new JSONObject();
    	JSONObject jobresultJO = commandresponseObj.getJSONObject("jobresult");
		String jobResultresponseHead = woSpecificParams.get("jobResultresponseHead");
		JSONObject resultHeadJO = jobresultJO.getJSONObject(jobResultresponseHead);
		Map<String,String> mapMaping=RmiPhysicsHostBuilder.getProjectMaping();
		for(Iterator<String> itor= mapMaping.keySet().iterator();itor.hasNext();){
			String key=itor.next().toString();
			if(resultHeadJO.containsKey(key))
				project.put(mapMaping.get(key), resultHeadJO.get(key));				
		}
		arrProject.add(project);
	 	try{
    		hostManagerService.saveProjectCmdb(arrProject);
        	return true;
    	}catch(ServiceException er){
    		log.error(er.getMessage(),er);
    		return false;
    	}
    }
  
	public void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder){			
		this.putUserCredentialParams(cloudStackParams,ServiceOptionUtil.obtainCloudStackUserId());
		cloudStackParams.put("domainid", new Object[]{workOrder.getDomainId()});
		cloudStackParams.put("account", new Object[]{workOrder.getAccount()});
	}
	public ResourcePoolPermissionManager getResourcePoolPermissionManager() {
		return resourcePoolPermissionManager;
	}
	public void setResourcePoolPermissionManager(
			ResourcePoolPermissionManager resourcePoolPermissionManager) {
		this.resourcePoolPermissionManager = resourcePoolPermissionManager;
	}

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
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}

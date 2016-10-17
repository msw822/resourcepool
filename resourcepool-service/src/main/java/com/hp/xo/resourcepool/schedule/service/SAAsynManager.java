package com.hp.xo.resourcepool.schedule.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.service.impl.WorkOrderJobManagerImpl;

public abstract class SAAsynManager implements SAManager {
	static int WORKORDER_TYPE_NEWVOLUME=7;
	private WorkOrderManager orderManager=null;
	@Autowired
	private WorkItemManager itemManager=null;
	public WorkItemManager getItemManager() {
		return itemManager;
	}
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}
	public WorkOrderManager getOrderManager() {
		return orderManager;
	}
	@Autowired
	public void setOrderManager(WorkOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	private ProvisionAttributeManager provisionAttributeManager=null;
	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	public ProvisionAttributeManager getProvisionAttributeManager() {
		return provisionAttributeManager;
	}
	@Autowired
	public void setProvisionAttributeManager(
			ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
	
		this.genericCloudServerManager = genericCloudServerManager;
	}
	@Autowired
	private WorkOrderJobManagerImpl workOrderJobManager;
	
	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		
		return null;
	};

	public List<WorkItem> getCapObjWorkItem(long workOrderId){return null;}
    public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder){
    	
    	List<WorkItem> workItemList=workOrder.getWorkItems();
		Collections.sort(workItemList, new Comparator(){
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
		//将工单的开通属性放入MAP中。
		for(WorkItem wi:workItemList){
			ProvisionAttribute pa=new ProvisionAttribute();
			pa.setWorkOrderType(workOrder.getWorkOrderType());
			List paList=provisionAttributeManager.findByExample(pa);
			Iterator iterPa=paList.iterator();
			while(iterPa.hasNext()){
				ProvisionAttribute pa1=(ProvisionAttribute)iterPa.next();
				if(pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())){
					String attributeName=wi.getAttributeName();
					String attributeValue=wi.getAttributeValue();
					if(null!=attributeValue&&!"".equalsIgnoreCase(attributeValue.trim())){
						attributeName=attributeName.trim();
						attributeValue=attributeValue.trim();
						cloudStackParams.put(attributeName, new Object[]{attributeValue});
						/*if(workOrder.getWorkOrderType()==this.WORKORDER_TYPE_NEWVOLUME&&"virtualmachineid".equalsIgnoreCase(attributeName)){
					         this.putZoneId(cloudStackParams, attributeValue);
						}else {
							cloudStackParams.put(attributeName, new Object[]{attributeValue});
						}*/
					}
					
				}
			}
         }
		
		return cloudStackParams;
	};
	  public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams){

			System.out.println("command succeeded");
			JSONObject jobresultJO=resultJSONObj.getJSONObject("jobresult");
			String jobResultresponseHead=woSpecificParams.get("jobResultresponseHead");

			JSONObject resultHeadJO=jobresultJO.getJSONObject(jobResultresponseHead);
			String id=resultHeadJO.getString("id");
			
			
			workOrder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);//成功
			orderManager.save(workOrder);
			//saveWorkItem(workOrder.getId(),3,"id",id); 
			saveWorkItem(workOrder.getId(),3,"ID",id); //edit by@ma 10/16 id改为ID
			WorkOrderJob orderJob =  new WorkOrderJob();
			orderJob.setWorkOrderId(workOrder.getId());
			orderJob.setJobInstanceId(id);
			orderJob.setJobInstanceName(resultHeadJO.getString("name"));
			WorkOrderJob dd =  workOrderJobManager.save(orderJob);
		
}
	  public void saveWorkItem(long workOrderId,int step,String attributeName,String attributeValue){
			WorkItem wi=new WorkItem();
			wi.setWorkOrderId(workOrderId);
			wi.setStep(3);
			wi.setAttributeName(attributeName);
			wi.setAttributeValue(attributeValue);
			wi.setDescript("inserted by service active module");
			wi.setCreatedBy("admin");
			wi.setCreatedOn(new Date());
			wi.setModifiedBy("admin");
			wi.setModifiedOn(new Date());
			itemManager.save(wi);
			
		}
		static int WORKORDERSTATUS_PROVISIONSUCCEED=5;
		public WorkOrderJobManagerImpl getWorkOrderJobManager() {
			return workOrderJobManager;
		}
		public void setWorkOrderJobManager(WorkOrderJobManagerImpl workOrderJobManager) {
			this.workOrderJobManager = workOrderJobManager;
		}		
}

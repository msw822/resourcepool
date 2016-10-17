package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;
@Service(value="saProjectLimitImpl")

public class SAProjectLimitImpl  extends AbstractSAManager {
	public WorkItemManager getItemManager() {
		return itemManager;
	}
	@Autowired
	public void setItemManager(WorkItemManager itemManager) {
		this.itemManager = itemManager;
	}

	private WorkItemManager itemManager=null;
		

	
    public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder){
    	String projectid="";
    	WorkItem wiExample=new WorkItem();
		wiExample.setWorkOrderId(workOrder.getId());
		wiExample.setAttributeName("projectid");
		List<WorkItem> wiList=(List<WorkItem>) itemManager.findByExample(wiExample);
		for(WorkItem wi:wiList){
			projectid=wi.getAttributeValue();
			//this.updateResourceLimit2(cloudStackParams, wi.getAttributeValue(), "", "", workOrder);
		}
    	cloudStackParams.put("projectid", new Object[]{projectid});
    	return cloudStackParams;
    }
	public void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder){

		this.putUserCredentialParams(cloudStackParams,ServiceOptionUtil.obtainCloudStackUserId());
	}
}

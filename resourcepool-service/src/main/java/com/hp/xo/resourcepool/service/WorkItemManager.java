package com.hp.xo.resourcepool.service;

import javax.servlet.http.HttpServletRequest;

import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.request.SaveOrderRequest;
import com.hp.xo.resourcepool.response.EntityResponse;



public interface WorkItemManager extends GenericManager<WorkItem,Long>{
	
	/**
	 * @author zhouwenb
	 * @return 
	 */
	
	public EntityResponse<WorkItem> saveItem(SaveOrderRequest req,Long id,HttpServletRequest request);
/*	public void saveWorkItem(long workOrderId, int step, String attributeName,
			String attributeValue);*/
}

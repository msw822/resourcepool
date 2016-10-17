package com.hp.xo.resourcepool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.request.ListWorkOrderRequest;
import com.hp.xo.resourcepool.request.SaveOrderRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ListResponse;



public interface WorkOrderManager extends GenericManager<WorkOrder,Long>{
	
	/**
	 * @author zhouwenb
	 * @return 
	 */
	
	public EntityResponse<WorkOrder> saveOrder(SaveOrderRequest request,ActiveUser user,HttpServletRequest req);
	
	/**
	 * @author Li Manxin
	 * @param request
	 * @return
	 */
	ListResponse<WorkOrder> listWorkOrder(ListWorkOrderRequest request);
	
	/***
	 * 检查状态
	 */
	WorkOrder checkStatus(int key,String name,String value);

	public List<WorkOrder> listWorkOrderByTypeAndStatus(List<Integer> typeList,List<Integer> statusList);
	
	public WorkOrder findById(Long id);
	

}

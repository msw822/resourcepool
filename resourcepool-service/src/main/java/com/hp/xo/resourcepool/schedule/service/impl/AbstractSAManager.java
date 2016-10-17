package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.schedule.service.SAManager;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.service.impl.WorkOrderJobManagerImpl;

public abstract class AbstractSAManager implements SAManager {
	private final Logger log = Logger.getLogger(this.getClass());
	protected String ignoreFields = "applyreason";
	@Autowired
	private WorkOrderManager orderManager = null;
	@Autowired
	private WorkItemManager itemManager = null;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private ProvisionAttributeManager provisionAttributeManager = null;
	@Autowired
	private WorkOrderJobManagerImpl workOrderJobManager;
	
	@Override
	public Map<String, String> getOrderTypeSpecificParams() {
		return null;
	};

	public List<WorkItem> getCapObjWorkItem(long workOrderId) {
		return null;
	}
	public void putUserCredentialParams(Map<String, Object[]> cloudStackParams,WorkOrder workOrder) {
		this.putUserCredentialParams(cloudStackParams, workOrder.getApplierId());
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

	public Map<String, Object[]> getProvisionAttributes(Map<String, Object[]> cloudStackParams, WorkOrder workOrder) {
		List<WorkItem> workItemList = workOrder.getWorkItems();
		Collections.sort(workItemList, new Comparator() {
			public int compare(Object o1, Object o2) {
				WorkItem s1 = (WorkItem) o1;
				WorkItem s2 = (WorkItem) o2;
				if (s1.getStep() < s2.getStep()) {
					return -1;
				}
				if (s1.getStep() > s2.getStep()) {
					return 1;
				}
				return 0;
			}
		});
		// 将工单的开通属性放入MAP中。
		for (WorkItem wi : workItemList) {
			ProvisionAttribute pa = new ProvisionAttribute();
			pa.setWorkOrderType(workOrder.getWorkOrderType());
			List paList = provisionAttributeManager.findByExample(pa);
			Iterator iterPa = paList.iterator();
			while (iterPa.hasNext()) {
				ProvisionAttribute pa1 = (ProvisionAttribute) iterPa.next();
				if (pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())) {
					String attributeName = wi.getAttributeName();
					String attributeValue = wi.getAttributeValue();
					if (null != attributeValue	&& !"".equalsIgnoreCase(attributeValue.trim())) {
						attributeName = attributeName.trim();
						attributeValue = attributeValue.trim();
						cloudStackParams.put(attributeName,new Object[] { attributeValue });
					}
				}
			}
		}

		return cloudStackParams;
	};

	public void jobSucceedDeal(Map<String, Object[]> cloudStackParams,WorkOrder workOrder, JSONObject resultJSONObj,Map<String, String> woSpecificParams) {
		JSONObject jobresultJO = resultJSONObj.getJSONObject("jobresult");
		String jobResultresponseHead = woSpecificParams.get("jobResultresponseHead");
		JSONObject resultHeadJO = jobresultJO.getJSONObject(jobResultresponseHead);
		String id = resultHeadJO.getString("id");
		String name = resultHeadJO.getString("name");
		workOrder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);// 成功
		orderManager.save(workOrder);
		//saveWorkItem(workOrder.getId(), 3, "id", id);
		saveWorkItem(workOrder.getId(), 3, "ID", id); //eidt by@ma 10/16 把id改为ID
		WorkOrderJob orderJob =  new WorkOrderJob();
		orderJob.setWorkOrderId(workOrder.getId());
		orderJob.setJobInstanceId(id);
		orderJob.setJobInstanceName(name);
		WorkOrderJob dd =  workOrderJobManager.save(orderJob);
	}
	public void saveWorkItem(long workOrderId, int step, String attributeName,String attributeValue) {
		WorkItem wi = new WorkItem();
		wi.setWorkOrderId(workOrderId);
		wi.setStep(step);
		wi.setAttributeName(attributeName);
		wi.setAttributeValue(attributeValue);
		wi.setDescript("inserted by service active module");
		wi.setCreatedBy("admin");
		wi.setCreatedOn(new Date());
		wi.setModifiedBy("admin");
		wi.setModifiedOn(new Date());
		itemManager.save(wi);
	}
	/**
     * 订单后处理
     * @param cloudStackParams
     * @param workOrder
     * @return
     */
    public boolean jobSucceedPost(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject commandresponseObj,Map<String, String> woSpecificParams){
    	return true;
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

	public ProvisionAttributeManager getProvisionAttributeManager() {
		return provisionAttributeManager;
	}

	public void setProvisionAttributeManager(ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}

	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	public WorkOrderJobManagerImpl getWorkOrderJobManager() {
		return workOrderJobManager;
	}

	public void setWorkOrderJobManager(WorkOrderJobManagerImpl workOrderJobManager) {
		this.workOrderJobManager = workOrderJobManager;
	}
	
}

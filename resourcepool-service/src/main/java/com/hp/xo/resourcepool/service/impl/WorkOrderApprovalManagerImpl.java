package com.hp.xo.resourcepool.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.ResourceDao;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.DbConfig;
import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.Resource;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.service.DbConfigManager;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderApprovalManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.utils.LogConstants;

//import com.cmsz.cloudplatform.dao.ResourceDao;
//import com.cmsz.cloudplatform.model.ActiveUser;
//import com.cmsz.cloudplatform.model.ProvisionAttribute;
//import com.cmsz.cloudplatform.model.Resource;
//import com.cmsz.cloudplatform.model.WorkItem;
//import com.cmsz.cloudplatform.model.WorkOrder;
//import com.cmsz.cloudplatform.service.LogManager;
//import com.cmsz.cloudplatform.service.ProvisionAttributeManager;
//import com.cmsz.cloudplatform.service.WorkItemManager;
//import com.cmsz.cloudplatform.service.WorkOrderApprovalManager;
//import com.cmsz.cloudplatform.service.WorkOrderManager;
//import com.cmsz.cloudplatform.utils.LogConstants;
//import com.hp.config.model.DbConfig;
//import com.hp.config.service.DbConfigManager;

//GenericCloudServerManagerImpl
@Service(value="workOrderApprovalManager")
public class WorkOrderApprovalManagerImpl implements WorkOrderApprovalManager {
	

	@Autowired
	private LogManager logManager;

	private WorkOrderManager workOrderManager;
	private WorkItemManager workItemManager;
	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	public void setWorkOrderManager(WorkOrderManager workOrderManager) {
		this.workOrderManager = workOrderManager;
	}

	@Autowired
	public void setWorkItemManager(WorkItemManager workItemManager) {
		this.workItemManager = workItemManager;
	}

	@Autowired
	private DbConfigManager dbconfigManager;
	
	private ProvisionAttributeManager provisionAttributeManager;
	

	
	
	private static Map<String,String> workOrderTypeStr = null;
	private static Map<String,String> workOrderTypeKey = null;
	
	@Autowired
	public void setProvisionAttributeManager(
			ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}

	@Override
	public void approve(Map<String,Object[]> attributeItem, ActiveUser user, Long workOrderId,
			int approveResult, String approveDesc) {

		DbConfig example = new DbConfig();
		example.setKey("workorder_status%");
		List<DbConfig> configs = (List<DbConfig>) dbconfigManager
				.findByExample(example);
		WorkOrder worder = workOrderManager.get(workOrderId);
		worder.setModifiedBy(user.getUserid());
		worder.setModifiedOn(new Date());
		
		worder.setDescript(approveDesc);
		worder.setApproveId(user.getUserid());
		worder.setApproveName(user.getLoginId());
		worder.setApproveResult(approveResult);
		worder.setApproveDesc(approveDesc);
		String result = "",info="";
		
		if (workOrderTypeKey == null) {
			workOrderTypeKey = new HashMap<String, String>();
			DbConfig texample = new DbConfig();
			texample.setKey("workorder_type%");
			List<DbConfig> tconfigs = (List<DbConfig>) dbconfigManager.findByExample(texample);
			for (int i = 0; tconfigs != null && i < tconfigs.size(); ++i) {
				workOrderTypeKey.put(tconfigs.get(i).getValue(), tconfigs.get(i).getKey());
			}
		}
		
		if (approveResult == this.AGREE) {// 审批通过
//			String key = null;
//			if (worder.getWorkOrderType() != null) {
//				key = workOrderTypeKey.get(String.valueOf(worder.getWorkOrderType().intValue()));
//			}
			worder.setStatus(WorkOrder.STATUS_APPROVED);// 审批通过待处理			
			result = "审批通过";
			if (WorkOrder.TYPE_DEPLOYVIRTUALMACHINE == worder.getWorkOrderType().intValue()){//add by@ma 
					Calendar cal = Calendar.getInstance();
					cal.setTime(worder.getModifiedOn());
					int months = Integer.parseInt(attributeItem.get("apply-time-length")[0].toString());
					cal.add(Calendar.MONTH,months);
					Date date=cal.getTime();
					worder.setWorkorder_due_date(date);
			}else if(WorkOrder.TYPE_EXTENDORDER == worder.getWorkOrderType().intValue()){//add by@ma 
				Calendar cal = Calendar.getInstance();
				Long originalOrderId = Long.parseLong(attributeItem.get("originalOrder")[0].toString());
				WorkOrder originalOrder = workOrderManager.get(originalOrderId);
				cal.setTime(originalOrder.getWorkorder_due_date());
				int months = Integer.parseInt(attributeItem.get("extendMonths")[0].toString());
				cal.add(Calendar.MONTH,months);
				Date work_due_date=cal.getTime();
				originalOrder.setWorkorder_due_date(work_due_date);
				workOrderManager.save(originalOrder);
				worder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);
			}
			/*if ((WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(key)) || WorkOrder.KEY_HP_MINICOMPUTER_RESOURCES_APPLICATION.equals(key)) {
				worder.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED); // X86物理资源申请和HP小型机资源申请工单审批通过,工单处理完成
				
				// X86物理资源申请和HP小型机资源申请工单审批通过，保存申请信息供资源统计使用
				Resource resource = new Resource();
				resource.setType(WorkOrder.KEY_X86_PHYSICAL_RESOURCES_APPLICATION.equals(key) ? Resource.RESOURCE_TYPE_X86 : Resource.RESOURCE_TYPE_HPVM);
				if (attributeItem != null) {
					Object[] resourcePoolId = (Object[]) attributeItem.get("resourcePoolId");
					if(resourcePoolId != null && resourcePoolId.length > 0 && resourcePoolId[0] != null) {
						resource.setResourcepoolid(resourcePoolId[0].toString());
					} else {
						resource.setResourcepoolid("");
					}
					
					Object[] cpu = (Object[]) attributeItem.get("cpu");
					if(cpu != null && cpu.length > 0 && cpu[0] != null) {
						resource.setCpu(cpu[0].toString());
					} else {
						resource.setCpu("");
					}
					
					Object[] memory = (Object[]) attributeItem.get("memory");
					if(memory != null && memory.length > 0 && memory[0] != null) {
						resource.setMemory(memory[0].toString());
					} else {
						resource.setMemory("");
					}
					
					
					Object[] unit = (Object[]) attributeItem.get("unit");
					if(unit != null && unit.length > 0 && unit[0] != null) {
						resource.setAmount(Integer.parseInt(unit[0].toString()));
					} else {
						resource.setAmount(0);
					}
					
				}
				resource.setCreatedBy(user.getLoginId());
				resource.setCreatedOn(new Date());
				resource.setModifiedBy(resource.getCreatedBy());
				resource.setModifiedOn(resource.getCreatedOn());
				resourceDao.save(resource);
			} */			
		} else {// 审批不通过
			worder.setStatus(WorkOrder.STATUS_REJECTED);
			result = "审批不通过";
			info  = approveDesc;
//			if(WorkOrder.TYPE_EXTENDORDER == worder.getWorkOrderType().intValue()){//add by@ma 
//				Long originalOrderId = Long.parseLong(attributeItem.get("originalOrder")[0].toString());
//				WorkOrder originalOrder = workOrderManager.get(originalOrderId);
//			}
		}

		//更新Order审批状态
		workOrderManager.save(worder);
		
		if(workOrderTypeStr==null){
			workOrderTypeStr = new HashMap<String,String>();
			DbConfig texample = new DbConfig();
			texample.setKey("workorder_type%");
			List<DbConfig> tconfigs = (List<DbConfig>) dbconfigManager
					.findByExample(texample);
			for(int i=0;tconfigs!=null && i<tconfigs.size();++i){
				workOrderTypeStr.put(tconfigs.get(i).getValue(), tconfigs.get(i).getDescription());
			}
			
		}
		logManager.log(user.getClientIP(), LogConstants.SERVICE_AUDIT, LogConstants.ACTION_APPROVE, user.getLoginId(), "用户[" + user.getLoginId() + "]审批["+workOrderTypeStr.get(String.valueOf(worder.getWorkOrderType().intValue()))+"]工单,工单id:"+worder.getId().longValue(),result, info);
		//添加workItem
		WorkItem workItem = null;
		Iterator<Entry<String,Object[]>> it = attributeItem.entrySet().iterator();
		ProvisionAttribute  pa = new ProvisionAttribute();
		pa.setWorkOrderType(worder.getWorkOrderType());
		List<ProvisionAttribute> pas = (List<ProvisionAttribute>) provisionAttributeManager.findByExample(pa);
		List<String> attributeName = new ArrayList<String>();
		for(ProvisionAttribute tpa : pas){
			attributeName.add(tpa.getAttributeName());
		}
		while(it.hasNext()){
			Entry<String,Object[]> entry = it.next();
			if(!attributeName.contains(entry.getKey())){
				continue;
			}
			workItem = new WorkItem();
			workItem.setAttributeName(entry.getKey());
			String temp =entry.getValue()[0].toString();
			temp = temp.equals("undefined")?"":temp;
			workItem.setAttributeValue(temp);
			workItem.setCreatedBy(worder.getCreatedBy());
			workItem.setCreatedOn(worder.getCreatedOn());
			workItem.setModifiedBy(user.getLoginId());
			workItem.setModifiedOn(new Date());
			workItem.setStep(2);
			workItem.setWorkOrderId(workOrderId);
			this.workItemManager.save(workItem);
		}
		

	}

}

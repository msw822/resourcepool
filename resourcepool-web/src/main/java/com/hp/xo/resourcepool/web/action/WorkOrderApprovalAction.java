package com.hp.xo.resourcepool.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.WorkOrderApprovalManager;
import com.hp.xo.resourcepool.web.action.core.BaseAction;

//import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
//import com.cmsz.cloudplatform.model.response.Response;
//import com.cmsz.cloudplatform.service.WorkOrderApprovalManager;
//import com.cmsz.cloudplatform.web.core.BaseAction;
//import com.hp.config.service.impl.DbConfigMgr;
@Transactional
public class WorkOrderApprovalAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5397154542139501303L;
	
	
	private WorkOrderApprovalManager workOrderApprovalManager;

	public String approveWorkOrder(){
		
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.putAll(this.requestParams);
		
		int approveResult = Integer.parseInt(params.get("approveResult")[0].toString());
		String approveDesc = params.get("approveDesc")[0].toString();
		Long workOrderId = Long.parseLong(params.get("workOrderId")[0].toString());
		
		params.remove("approveResult");
		params.remove("approveDesc");
		params.remove("workOrderId");

		this.workOrderApprovalManager.approve(params,this.activeUser,workOrderId, approveResult, approveDesc);

		Response response = this.transformResponse("SUCCESS", "json", HttpStatus.SC_OK);
		writeResponse(response);
		return NONE;
	}
	
	@Autowired
	public void setWorkOrderApprovalManager(
			WorkOrderApprovalManager workOrderApprovalManager) {
		this.workOrderApprovalManager = workOrderApprovalManager;
	}
}

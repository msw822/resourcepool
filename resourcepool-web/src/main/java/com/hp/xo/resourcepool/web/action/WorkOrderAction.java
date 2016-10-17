package com.hp.xo.resourcepool.web.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderRelate;
import com.hp.xo.resourcepool.request.ListWorkOrderRequest;
import com.hp.xo.resourcepool.request.SaveOrderRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;
import com.hp.xo.resourcepool.service.WorkItemManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.service.impl.AttributeValueManagerFactory;
import com.hp.xo.resourcepool.utils.DateUtil;
import com.hp.xo.resourcepool.utils.LogConstants;
import com.hp.xo.resourcepool.utils.Page;
import com.hp.xo.resourcepool.web.action.core.BaseAction;



public class WorkOrderAction extends BaseAction{

	/**
	 * @author zhouwenb
	 * 提交工单
	 */
	@Autowired
	
	private LogManager logManager;
	
	private static final long serialVersionUID = 1032232232L;
	
	private WorkOrderManager orderManager=null;
	
	private HostManagerService hostManagerService;
	
	@Autowired
	public void setHostManagerService(HostManagerService hostManagerService) {
		this.hostManagerService = hostManagerService;
	}

	private WorkOrder order=null;

	public WorkOrderAction(){
		super();
	}
	
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

	public WorkOrder getOrder() {
		return order;
	}

	public void setOrder(WorkOrder order) {
		this.order = order;
	}
	
	public String save(){
		String msg = "SUCCESS";
		SaveOrderRequest request = (SaveOrderRequest) this.wrapRequest(new SaveOrderRequest());
		EntityResponse<WorkOrder> entityResponse = this.orderManager.saveOrder(request,this.activeUser,this.request);
		//EntityResponse<WorkItem> entityResponse2 = this.itemManager.saveItem(request,entityResponse.getEntity().getId(),this.request);
		Response response = this.transformResponse(msg, "json", HttpStatus.SC_OK);
		logManager.log(this.request.getRemoteHost(), LogConstants.SERVICE_VM, LogConstants.CREATE,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]申请["+entityResponse.getEntity().getWorkOrderTypeName()+"工单,工单id："+entityResponse.getEntity().getId().longValue(), LogConstants.SUCCESS, msg);
//		logManager.log(this.request.getRemoteHost(), LogConstants.SERVICE_VM, LogConstants.CREATE, this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]申请["+workOrderTypeStr.get(String.valueOf(worder.getWorkOrderType().intValue()))+"]工单,工单id:"+worder.getId().longValue(),LogConstants.SUCCESS, msg);
		writeResponse(response);
		 
		return NONE;
	}
	public String list(){
		ListWorkOrderRequest request = (ListWorkOrderRequest) this.wrapRequest(new ListWorkOrderRequest());
		
		ListResponse<WorkOrder> response = orderManager.listWorkOrder(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("workOrders", response.getResponses());
		this.renderText(jsonPage.toString());
		return NONE;
	}
	
	/**
	 * add cancel order @ma
	 * 
	 **/
	public String cancel(){
		String workOrderId =  request.getParameter("id");		
		WorkOrder order= orderManager.get(Long.valueOf(workOrderId));
		if (order.getStatus()==WorkOrder.STATUS_WAITINGFORAPPROVAL){
			order.setStatus(WorkOrder.STATUS_INSTANCES_END);
			orderManager.save(order);
			logManager.log(this.request.getRemoteHost(), LogConstants.SERVICE_VM, LogConstants.CANCEL,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]取消["+order.getWorkOrderTypeName()+"]订单id："+ order.getId().longValue(), LogConstants.SUCCESS, "取消成功");
		}
		else if (order.getStatus()==WorkOrder.STATUS_PROVISIONSUCCEED && order.getWorkOrderType() == WorkOrder.TYPE_DEPLOYVIRTUALMACHINE){
			order.setStatus(WorkOrder.STATUS_INSTANCES_RECYCLE);
			orderManager.save(order);
			logManager.log(this.request.getRemoteHost(), LogConstants.SERVICE_VM, LogConstants.CANCEL,this.activeUser.getLoginId(), "用户[" + this.activeUser.getLoginId() + "]取消["+order.getWorkOrderTypeName()+"]订单id："+ order.getId().longValue(), LogConstants.SUCCESS, "取消成功");
		}		
		return NONE;
	}
	public String recycle(){
		String message = "";
		int success = 1;
		String workOrderId =  request.getParameter("id");		
		WorkOrder order= orderManager.get(Long.valueOf(workOrderId));
		JSONObject jsonObj = new JSONObject();
		if (WorkOrder.STATUS_PROVISIONSUCCEED == order.getStatus() && WorkOrder.TYPE_DEPLOYVIRTUALMACHINE == order.getWorkOrderType()){
			if(DateUtil.isOverDueDate(order.getWorkorder_due_date())){
				message = "回收成功";
				List<WorkOrderRelate> list =  order.getWorkOrderRelate();
				order.setStatus(WorkOrder.STATUS_INSTANCES_RECYCLE);
				for(WorkOrderRelate wr:list){
					if(WorkOrder.STATUS_WAITINGFORAPPROVAL == orderManager.get(wr.getRelateWorkOrderId()).getStatus()){
						message = "该订单有未审批续订订单暂不能回收";
						success=0;
						order.setStatus(WorkOrder.STATUS_PROVISIONSUCCEED);
						break;
					}
				}				
				orderManager.save(order);				
			}
			else{
				success = 0;
				message = "没到期不能回收";
			}
		}		
		jsonObj.put("message",message);
		jsonObj.put("success", success);
		this.renderText(jsonObj.toString());
		return NONE;
	}
	private ProvisionAttributeManager provisionAttributeManager;
	
	@Autowired
	public void setProvisionAttributeManager(
			ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	
	
	public String getAttribute(){
		String workordertype = this.requestParams.containsKey("workordertype")?(String)requestParams.get("workordertype")[0]:"-1";
		ProvisionAttribute  pa = new ProvisionAttribute();
		pa.setWorkOrderType(Integer.parseInt(workordertype));
		List<ProvisionAttribute> pas = (List<ProvisionAttribute>) provisionAttributeManager.findByExample(pa);
		JSONObject jsonObj = new JSONObject();
		Collections.sort(pas);
		jsonObj.put("fields",pas);
		this.renderText(jsonObj.toString());
		return NONE;
	}
	
	public String getExtval(){
		String workordertype = this.requestParams.containsKey("workordertype")?(String)requestParams.get("workordertype")[0]:"-1";
		String attributeName = this.requestParams.containsKey("attributename")?(String)requestParams.get("attributename")[0]:"";
		ProvisionAttributeValueManager attributeValManager = AttributeValueManagerFactory.createManager(Integer.parseInt(workordertype));
		String sessionkey = requestParams.containsKey("sessionkey")?(String)requestParams.get("sessionkey")[0]:"";
		Map<String,Object> result = attributeValManager.getExtval(attributeName, sessionkey, this.requestParams);
		JSONObject jsonObj = null;
		JSONArray array = new JSONArray();
		if(result!=null && result.size()>0){
			Iterator<Entry<String,Object>> iterator =  result.entrySet().iterator();
			while(iterator.hasNext()){
				jsonObj = new JSONObject();
				Entry<String,Object> keyValue = iterator.next();
				jsonObj.put("id", keyValue.getKey());
				if(keyValue.getValue() instanceof String){
					jsonObj.put("description",keyValue.getValue());
				}else{
					if(keyValue.getValue() instanceof JSONObject){
						if(workordertype.equalsIgnoreCase(String.valueOf(WorkOrder.TYPE_NEWVOLUME))){
							jsonObj.put("description", ((JSONObject)keyValue.getValue()).getString("displaytext"));
							jsonObj.put("iscustomized", ((JSONObject)keyValue.getValue()).getBoolean("iscustomized")?1:0);
						}else if(workordertype.equalsIgnoreCase(String.valueOf(WorkOrder.TYPE_DEPLOYVIRTUALMACHINE))){
							jsonObj.put("description", ((JSONObject)keyValue.getValue()).getString("displaytext"));
							jsonObj.put("iscustomized", ((JSONObject)keyValue.getValue()).getBoolean("iscustomized")?1:0);
						}else if(workordertype.equalsIgnoreCase(String.valueOf(WorkOrder.TYPE_RESIZEVOLUME))){
							jsonObj.put("description", ((JSONObject)keyValue.getValue()).getString("displaytext"));
							jsonObj.put("iscustomized", ((JSONObject)keyValue.getValue()).getBoolean("iscustomized")?1:0);
						}
					}
				}
				
				array.add(jsonObj);
			}
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("keyValues", array);
		this.renderText(resultJson.toString());
		return NONE;
	}
	
	
	public String checkStatus(){
		int key=Integer.parseInt(request.getParameter("workordertype"));
		WorkOrder order=new WorkOrder();
		if(key==2){
			order=this.orderManager.checkStatus(2,"projectid",request.getParameter("projectid"));
		}else if(key==3){
			order=this.orderManager.checkStatus(3,"domainId",request.getParameter("domainId"));
		}else if(key==4){
			order=this.orderManager.checkStatus(4,"account",request.getParameter("account"));
		}else if(key==6){
			order=this.orderManager.checkStatus(6,"vmid",request.getParameter("vmid"));
		}
		JSONObject resultJson = new JSONObject();
		resultJson.put("status", order.getStatus());
		this.renderText(resultJson.toString());
		return NONE;
	}
	
	
	public String  savePhysicsHostAssignment(){
		WorkOrder order=new WorkOrder();
		JSONObject resultJson = new JSONObject();
		String orderId = request.getParameter("orderId");
		String isVirtualHost ="0";
		String physicsName = request.getParameter("physicsName");
		String physicsType = request.getParameter("physicsType");
		String owner = this.activeUser.getUserName();
		String ownerName = this.activeUser.getUserid();
		String orderTypeId = request.getParameter("orderTypeId");
		String orderTypeName = request.getParameter("orderTypeName");
		String status = request.getParameter("status");
		resultJson.put("orderId",orderId);
		resultJson.put("isVirtualHost", isVirtualHost);
		resultJson.put("physicsName", physicsName);
		resultJson.put("physicsType", physicsType);
		resultJson.put("owner", this.activeUser.getUserName());
		resultJson.put("ownerName", this.activeUser.getUserName());
		//写死
		resultJson.put("orderTypeId", "8");
		resultJson.put("orderTypeName", "物理机申请");
		
		hostManagerService.savePhysicsHostAssignment(resultJson.toString(), this.activeUser);
		
		if(!status.equals("5"))
		{
			
			order =  orderManager.findById(Long.parseLong(orderId));
			 
			if(order!=null)
			{
				order.setStatus(5);
				orderManager.save(order);
			}
			
		}
		
		this.renderText(resultJson.toString());
		
		return NONE;
	}
	
 
	
	public String  saveEnabledPhysicsInstanceByOrderid(){
		JSONObject resultJson = new JSONObject();
		String orderId = request.getParameter("orderId");
		resultJson.put("orderId",orderId);
		hostManagerService.saveEnabledPhysicsInstanceByOrderid(orderId);
		this.renderText(resultJson.toString());
		return NONE;
	}	
}

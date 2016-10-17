package com.hp.xo.resourcepool.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.ApproveRuleDao;
import com.hp.xo.resourcepool.dao.AttributeDao;
import com.hp.xo.resourcepool.dao.WorkItemDao;
import com.hp.xo.resourcepool.dao.WorkOrderDao;
import com.hp.xo.resourcepool.dao.WorkOrderRelateDao;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.ApproveRule;
import com.hp.xo.resourcepool.model.PhysicsInstances;
import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.model.WorkOrderRelate;
import com.hp.xo.resourcepool.request.ListWorkOrderRequest;
import com.hp.xo.resourcepool.request.SaveOrderRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.WorkOrderManager;
import com.hp.xo.resourcepool.utils.LogConstants;

/*import com.cmsz.cloudplatform.dao.ApproveRuleDao;
import com.cmsz.cloudplatform.dao.AttributeDao;
import com.cmsz.cloudplatform.dao.WorkItemDao;
import com.cmsz.cloudplatform.dao.WorkOrderDao;
import com.cmsz.cloudplatform.model.ActiveUser;
import com.cmsz.cloudplatform.model.ApproveRule;
import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.cmsz.cloudplatform.model.WorkItem;
import com.cmsz.cloudplatform.model.WorkOrder;
import com.cmsz.cloudplatform.model.request.ListWorkOrderRequest;
import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
import com.cmsz.cloudplatform.model.response.EntityResponse;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.WorkOrderManager;
import com.hp.core.service.impl.GenericManagerImpl;*/
@Service(value="orderManager")
@Transactional(propagation=Propagation.REQUIRED)
public class WorkOrderManagerImpl extends GenericManagerImpl<WorkOrder,Long> implements WorkOrderManager{
	/**
	 * @author zhouwenb
	 */
	private WorkOrderDao orderDao;
	public WorkOrderManagerImpl(){
		super();
	}
	
	public WorkOrderManagerImpl(WorkOrderDao orderDao){
		super(orderDao);
		this.orderDao = orderDao;
	}
	
	@Autowired
	public void setOrderDao(WorkOrderDao orderDao) {
		this.dao = orderDao;
		this.orderDao = orderDao;
	}
	
	private WorkItemDao itemDao;
	@Autowired
	public void setOrderDao(WorkItemDao itemDao) {
		this.itemDao = itemDao;
	}
	private AttributeDao attributeDao;
	@Autowired
	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}
	private ApproveRuleDao approveRuleDao;
	@Autowired
	public void setAttributeDao(ApproveRuleDao approveRuleDao) {
		this.approveRuleDao = approveRuleDao;
	}
	private WorkOrderRelateDao orderRelateDao;	
	@Autowired
	public void setOrderRelateDao(WorkOrderRelateDao orderRelateDao){
		this.orderRelateDao  = orderRelateDao;
	}
	@Override
	public EntityResponse<WorkOrder> saveOrder(SaveOrderRequest request,ActiveUser user,HttpServletRequest req) {
		
//		if(StringUtils.isNotBlank(request.getResourcePoolId())){
//			ApproveRule example=new ApproveRule();
//			example.setWorkOrderType(request.getWorkOrderType());
//			example.setPoolId(request.getResourcePoolId());
//			//List<ApproveRule> list=this.approveRuleDao.listApproveRule(example);
//			List<ApproveRule> list = this.approveRuleDao.listApproveRule(example);
// 			if(list.size()>0){
//				request.setStatus(Integer.parseInt(list.get(0).getType()));
//			}
//		}
		EntityResponse<WorkOrder> result = null;
		WorkOrder entity = new WorkOrder();
		BeanUtils.copyProperties(request, entity);
		entity.setAccount(user.getAccount());
		entity.setAccountId(user.getAccountid());
		if(request.getWorkOrderType()==3||request.getWorkOrderType()==4){
			entity.setDomainId(req.getParameter("domainId"));
		}else{
			entity.setDomainId(user.getDomainid());
		}
		entity.setCreatedBy(request.getLoginId());
		entity.setCreatedOn(new Date());
		entity.setModifiedBy(request.getLoginId());
		entity.setModifiedOn(new Date());
		entity.setApplierId(request.getUserid());
		entity.setApplierName(request.getLoginId());
		entity.setApplierAbsDomain(user.getPath());
		entity.setApplierRelDomain(user.getPath2());
		if(request.getStatus()==WorkOrder.STATUS_APPROVED){
			entity.setApproveResult(1);
			entity.setApproveDesc("自动审批");
		}else{
			entity.setApproveResult(0);
		}
		entity = this.orderDao.save(entity);		
		
		ProvisionAttribute p=new ProvisionAttribute();
		p.setWorkOrderType(request.getWorkOrderType());
		List<ProvisionAttribute> list=this.attributeDao.findWorkOrderListByExample(p);
		for(int i=0;i<list.size();i++){
			WorkItem entity2 = new WorkItem();
			BeanUtils.copyProperties(request, entity2);
			entity2.setCreatedBy(request.getLoginId());
			entity2.setCreatedOn(new Date());
			entity2.setModifiedBy(request.getLoginId());
			entity2.setModifiedOn(new Date());
			if(request.getStatus()==WorkOrder.STATUS_APPROVED){
				entity2.setStep(2);
			}else{
				entity2.setStep(1);
			}
			entity2.setWorkOrderId(entity.getId());
			entity2.setAttributeName(list.get(i).getAttributeName());
			if(StringUtils.isNotBlank(req.getParameter(list.get(i).getAttributeName()))){
				entity2.setAttributeValue(req.getParameter(list.get(i).getAttributeName()));
			}else{
				entity2.setAttributeValue("");
			}
			entity2 = this.itemDao.save(entity2);
		}
		result = new EntityResponse<WorkOrder>();
		result.setEntity(entity);
		
		if(request.getWorkOrderType()== WorkOrder.TYPE_EXTENDORDER){//save WorkOrderRelate
			WorkOrderRelate orderRelate = new WorkOrderRelate();
			if(StringUtils.isNotBlank(req.getParameter("originalOrder"))){				
				Long originalOrderId = Long.parseLong(req.getParameter("originalOrder"));
				orderRelate.setWorkOrderId(originalOrderId);
			}
			orderRelate.setRelateWorkOrderId(entity.getId());
			orderRelateDao.save(orderRelate);
		}
		
		return result;
	}
	
	
	@Autowired
	private  GenericCloudServerManagerImpl genericCloudServerManager;
	
	public ListResponse<WorkOrder> listWorkOrder(ListWorkOrderRequest request){
		ListResponse<WorkOrder> result = new ListResponse<WorkOrder>();
		List<WorkOrder> list = null;
		Integer count = 0;
		
		if (null != request.getId()) {
			WorkOrder example = new WorkOrder();
			example.setId(request.getId());
			list = (List<WorkOrder>) orderDao.findWorkOrderList(request, 1, 1);
			//list = (List<WorkOrder>) orderDao.findByExample(example);
			if(list!=null){
				count = list.size();
			}
		} else {
			
		
		//admin 1
    	//domainadmin 2
    	//user 0
		if(request.getRole().intValue()==ActiveUser.DOMAINADMIN){
			Map<String,Object[]> params = new HashMap<String,Object[]>();
			params.put("command", new Object[]{"listUsers"});
			params.put("apikey",new Object[]{request.getApikey()});
			params.put("secretkey",new Object[]{request.getSecretkey()});
			//params.put("account",new Object[]{request.getAccount()});
			params.put("domainid",new Object[]{request.getDomainid()});
			params.put("response", new Object[]{"json"});
			Response response = genericCloudServerManager.get(params);
			System.out.println("HttpStatus.OK.value()="+HttpStatus.SC_OK);
			if(response!=null && response.getStatusCode()==HttpStatus.SC_OK &&  StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listusersresponse")){
					json = json.getJSONObject("listusersresponse");
					if(json.containsKey("count") && json.getInt("count")>0 && json.containsKey("user")){
						JSONArray jsonArray = json.getJSONArray("user");
						String[] users = new String[jsonArray.size()];
						for(int i= 0; i<jsonArray.size();++i){
							users[i]=jsonArray.getJSONObject(i).getString("username");
						}
						request.setApplyUsers(users);
					}
				}
			}
			/*{ "listaccountsresponse" : { "count":23 ,"account" : [  {"id":"c3c700f0-7d3b-11e3-bf84-005056941242","name":"admin","accounttype":1,"domainid":"67f97989-7d3b-11e3-bf84-005056941242","domain":"ROOT","vmlimit":"Unlimited","vmtotal":18,"vmavailable":"Unlimi*/
			}else if(request.getRole().intValue()==ActiveUser.USER){
				request.setApplyUsers(new String[]{request.getUserName()});
			}else if(request.getRole().intValue() == ActiveUser.ADMIN){
				//所有用户，不加条件限制
			}
		if (StringUtils.isNotBlank(request.getKeyword())) {
			request.setCreateBy(request.getKeyword());
		}
		list = (List<WorkOrder>) orderDao.findWorkOrderList(request, request.getPage(), request.getPagesize());
		count = orderDao.countByExample(request);
			
	}
		
		
		result.setCount(count);
		result.setResponses(list);
		return result;
	}
	@Transactional(readOnly =true)
	public List<WorkOrder> listWorkOrderByTypeAndStatus(List<Integer> typeList,List<Integer> statusList){
		return orderDao.listWorkOrderByTypeAndStatus(typeList,statusList);
		
	}
	
	@Override
	public WorkOrder checkStatus(int key,String name,String value) {
		return this.orderDao.checkStatus(key,name,value);
	}

	@Override
	public WorkOrder findById(Long id) {
		
		ListWorkOrderRequest o = new ListWorkOrderRequest();
		
		o.setId(id);
		List<WorkOrder> result = orderDao.findWorkOrderList(o, 1, 1);
		
		if(result.size()!=0)
		{
			return result.get(0);
		}
		
		return null;
	}

 
	
}

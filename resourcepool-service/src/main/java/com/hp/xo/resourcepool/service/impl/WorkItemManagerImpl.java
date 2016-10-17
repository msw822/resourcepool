package com.hp.xo.resourcepool.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.AttributeDao;
import com.hp.xo.resourcepool.dao.WorkItemDao;
import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.request.SaveOrderRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.service.WorkItemManager;

//import com.cmsz.cloudplatform.dao.AttributeDao;
//import com.cmsz.cloudplatform.dao.WorkItemDao;
//import com.cmsz.cloudplatform.model.ProvisionAttribute;
//import com.cmsz.cloudplatform.model.WorkItem;
//import com.cmsz.cloudplatform.model.request.SaveOrderRequest;
//import com.cmsz.cloudplatform.model.response.EntityResponse;
//import com.cmsz.cloudplatform.service.WorkItemManager;
//import com.hp.core.service.impl.GenericManagerImpl;
@Service(value="itemManager")
public class WorkItemManagerImpl extends GenericManagerImpl<WorkItem,Long> implements WorkItemManager{
	/**
	 * @author zhouwenb
	 */
	private WorkItemDao itemDao;
	public WorkItemManagerImpl(){
		super();
	}
	
	public WorkItemManagerImpl(WorkItemDao itemDao){
		super(itemDao);
		this.itemDao = itemDao;
	}
	
	@Autowired
	public void setItemDao(WorkItemDao itemDao) {
		this.dao = itemDao;
		this.itemDao = itemDao;
	}
	
	private AttributeDao attributeDao;
	@Autowired
	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}
	
	@Override
	public EntityResponse<WorkItem> saveItem(SaveOrderRequest request,Long id,HttpServletRequest req) {
		EntityResponse<WorkItem> result = null;
		ProvisionAttribute p=new ProvisionAttribute();
		p.setWorkOrderType(request.getWorkOrderType());
		List<ProvisionAttribute> list=this.attributeDao.findWorkOrderListByExample(p);
		for(int i=0;i<list.size();i++){
			WorkItem entity = new WorkItem();
			BeanUtils.copyProperties(request, entity);
			entity.setCreatedBy(request.getLoginId());
			entity.setCreatedOn(new Date());
			entity.setModifiedBy(request.getLoginId());
			entity.setModifiedOn(new Date());
			entity.setStep(1);
			entity.setWorkOrderId(id);
			entity.setAttributeName(list.get(i).getAttributeName());
			if(StringUtils.isNotBlank(req.getParameter(list.get(i).getAttributeName()))){
				entity.setAttributeValue(req.getParameter(list.get(i).getAttributeName()));
				entity = this.itemDao.save(entity);
			}
			/*if(i==list.size()-1){
				result = new EntityResponse<WorkItem>();
				result.setEntity(entity);
			}*/
		}
		return result;
		
	}
	/*@Override
	public void saveWorkItem(long workOrderId, int step, String attributeName,
			String attributeValue) {
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
		this.save(wi);

	}*/
	
}

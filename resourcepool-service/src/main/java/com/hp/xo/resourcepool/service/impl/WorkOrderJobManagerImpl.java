package com.hp.xo.resourcepool.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hp.xo.resourcepool.dao.WorkOrderJobDao;
import com.hp.xo.resourcepool.model.WorkOrderJob;
import com.hp.xo.resourcepool.service.WorkOrderJobManager;

@Service(value="orderJobManager")
@Transactional(propagation=Propagation.REQUIRED)
public class WorkOrderJobManagerImpl extends GenericManagerImpl<WorkOrderJob,Long> implements WorkOrderJobManager{
	
	private WorkOrderJobDao orderJobDao;
	
	public WorkOrderJobManagerImpl(){
		super();
	}
	public WorkOrderJobManagerImpl(WorkOrderJobDao orderJobDao){
		super(orderJobDao);
		this.orderJobDao = orderJobDao;
	}
	@Override
	public List<WorkOrderJob> findWorkOrderJobByInstanceId(String instanceId) {
		return orderJobDao.findWorkOrderJobByInstanceId(instanceId);
	}
	
	@Autowired
	public void setOrderJobDao(WorkOrderJobDao orderJobDao) {
		this.dao = orderJobDao;
		this.orderJobDao = orderJobDao;
	}
	
}

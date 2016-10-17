package com.hp.xo.resourcepool.dao;


import java.util.List;

import com.hp.xo.resourcepool.exception.ServiceException;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.PhysicsInstances;
import com.hp.xo.resourcepool.request.BuessionListRequest;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;

public interface PhysicsInstanceDao extends CommonDao{
	/**
	 * 获得已经分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstancesAssignmented(BuessionListRequest request)throws ServiceException;
	
	/**
	 * 获得物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstances(PhysicsHostRequest request)throws ServiceException;
	
	/**
	 * 获得已经分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstancesAssignmented(BuessionListRequest request,int pageNumber, int pageSize)throws ServiceException;
	/**
	 * 获得已经分配的物理机器的记录条数
	 * @return
	 * @throws ServiceException
	 */
	public long getPhysicsInstancesAssignmentedCount(BuessionListRequest request)throws ServiceException;
}

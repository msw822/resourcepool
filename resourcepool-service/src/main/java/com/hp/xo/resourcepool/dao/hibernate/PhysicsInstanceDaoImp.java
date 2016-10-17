package com.hp.xo.resourcepool.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.PhysicsInstanceDao;
import com.hp.xo.resourcepool.exception.ServiceException;
import com.hp.xo.resourcepool.model.PhysicsInstances;
import com.hp.xo.resourcepool.request.BuessionListRequest;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;
import com.hp.xo.utils.common.CollectionUtil;
@Repository("physicsInstanceDao")
public class PhysicsInstanceDaoImp extends CommonDaoImpl implements PhysicsInstanceDao{
	public PhysicsInstanceDaoImp(Class<PhysicsInstances> clazz) {
		super(clazz);
	}

	public PhysicsInstanceDaoImp(){
		super(PhysicsInstances.class);
	}
	/**
	 * 获得已经分配的物理机器的HQL
	 * @return
	 * @throws ServiceException
	 */
	
	public String[] getPhysicsInstancesAssignmentedHql(String[] users){
		StringBuffer selectTable=new StringBuffer("from com.hp.xo.resourcepool.model.PhysicsInstances t where 1=1 ");
		StringBuffer orderHql=new StringBuffer(" order by created_on desc");
		StringBuffer whereHql=new StringBuffer(" and t.isAllocated='1' and t.isValid='1' ");	
		if(users!=null && users.length>0){
			String condition=CollectionUtil.getBatchCondition(users,"owner",CollectionUtil.BATCH_CONDITON_IN_FLAG);
			whereHql.append(condition);
		}
		StringBuffer selectHql=new StringBuffer(selectTable);
		selectHql.append(whereHql).append(orderHql);
		StringBuffer countHql=new StringBuffer("select count(t) "+selectTable);
		countHql.append(whereHql);
		return new String[]{selectHql.toString(),countHql.toString()};
	}
	/**
	 * 获得已经分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstancesAssignmented(BuessionListRequest request)throws ServiceException{
		try{			
			String[] arrSQL=this.getPhysicsInstancesAssignmentedHql(request.getAccountIds());
			String[] paramters=new String[]{};			
			List list=this.findListByHql(arrSQL[0], paramters);
			List<PhysicsInstances> physicsInstances=(List<PhysicsInstances>)list;	
			return physicsInstances;

			
		}catch(Exception er){
			log.error("获取物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
		
	}
	/**
	 * 获得物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstances(PhysicsHostRequest request)throws ServiceException{
		try{
			StringBuffer selectTable=new StringBuffer("from com.hp.xo.resourcepool.model.PhysicsInstances t where 1=1 ");
			StringBuffer orderHql=new StringBuffer(" order by created_on desc");
			StringBuffer whereHql=new StringBuffer(" and t.isAllocated='1' and t.isValid='1' ");
			List<String> paramers=new ArrayList<String>();
			if(StringUtils.isNotBlank(request.getOrderId())){
				whereHql.append(" and t.orderId=?");
				paramers.add(request.getOrderId());
			}
			String[] users=request.getUserIds();
			if(users!=null && users.length>0){
				String condition=CollectionUtil.getBatchCondition(users,"owner",CollectionUtil.BATCH_CONDITON_IN_FLAG);
				whereHql.append(condition);
			}
			StringBuffer selectHql=new StringBuffer(selectTable);
			selectHql.append(whereHql).append(orderHql);
			StringBuffer countHql=new StringBuffer("select count(t) "+selectTable);
			countHql.append(whereHql);

			Object[] arrParamters=(Object[])CollectionUtil.list2Array(paramers);
			//String [] strarr = (String[])arrParamters;
			List list=this.findListByHql(selectHql.toString(), arrParamters);
			List<PhysicsInstances> physicsInstances=(List<PhysicsInstances>)list;	
			return physicsInstances;

			 
		}catch(Exception er){
			log.error("获取物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
		
	}
	/**
	 * 获得已经分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsInstances> getPhysicsInstancesAssignmented(BuessionListRequest request,int pageNumber, int pageSize)throws ServiceException{
//		ListResponse<PhysicsInstances> result = new ListResponse<PhysicsInstances>();
		try{
			String[] arrUserIds=request.getUserIds();
			String[] arrSQL=this.getPhysicsInstancesAssignmentedHql(arrUserIds);
			String[] paramters=new String[]{}; 
			List list=this.findListByHql(arrSQL[0], paramters, pageNumber, pageSize);
			List<PhysicsInstances> physicsInstances=(List<PhysicsInstances>)list;	
			return physicsInstances;

		}catch(Exception er){
			log.error("获取物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
		
	}
	/**
	 * 获得已经分配的物理机器的记录条数
	 * @return
	 * @throws ServiceException
	 */
	public long getPhysicsInstancesAssignmentedCount(BuessionListRequest request)throws ServiceException{
		try{
			String[] arrUserIds=request.getUserIds();
			String[] arrSQL=this.getPhysicsInstancesAssignmentedHql(arrUserIds);
			String[] paramters=new String[]{}; 
			long count=this.getCountByHql(arrSQL[1], paramters);
			return count;			
		}catch(Exception er){
			log.error("获取物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
		
	}


}
 
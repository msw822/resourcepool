package com.hp.xo.resourcepool.service;

import java.util.Map;

import com.hp.xo.resourcepool.model.ResourcePool;



public interface ResourcePoolManager extends GenericManager<ResourcePool, Integer> {
	
	
	/**
	 * 查询统计信息
	 * @param cloudStackParam CloudStack命令参数 key包括 
	 * @return
	 */
	String computeResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	

	/**
	 * 按人员(账号)统计
	 * @param cloudStackParam CloudStack命令参数 key包括 
	 * @return
	 */
	String accountResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	
	/**
	 * 按业务系统统计
	 * @param cloudStackParam CloudStack命令参数 key包括 
	 * @return
	 */
	String operationResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	
}

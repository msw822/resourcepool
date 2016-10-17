package com.hp.xo.resourcepool.schedule.service;

import java.util.Map;

import com.hp.xo.resourcepool.model.ResourcePool;
import com.hp.xo.resourcepool.service.GenericManager;

public interface ResourcePoolManager extends GenericManager<ResourcePool, Integer> {
	/**
	 * 配置ZONE
	 * @param resourcePoolId
	 * @param username TODO
	 * @param zoneId
	 */
	void configZone(String resourcePoolId,String username, String...zoneId);
	
	/**
	 * 查询己配置的二级资源池  ZONE
	 * @param cloudStackParam  CloudStack命令参数
	 * @param resourcePoolId   一级资源池ID
	 * @return 二级资源池JSON字符串
	 */
	String listSubResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	
	/**
	 * 查询可配置的二级资源池  ZONE
	 * @param cloudStackParam
	 * @param resourcePoolId
	 * @return
	 */
	String listAvailableResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	
	/**
	 * 查询统计信息
	 * @param cloudStackParam CloudStack命令参数 key包括 
	 * @return
	 */
	String computeResource(Map<String,Object[]> cloudStackParam,String resourcePoolId);
	
	/**
	 * 删除一级资源池中的ZONE
	 * @param resourcePoolId
	 * @param zoneId
	 */
	void removeRelation(String resourcePoolId,String zoneId);
	
	
	/**
	 * 根据ZONEID 得到所属的一级资源池ID
	 * @param zoneId
	 * @return
	 */
	String getRelResourcePoolByZoneId(String zoneId);
	
	/**
	 *获取主机的数量 
	 * @return
	 */
	int getHostCount(String type);
	
}

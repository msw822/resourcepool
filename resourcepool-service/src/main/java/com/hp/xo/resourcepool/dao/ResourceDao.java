package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.Resource;

//import com.cmsz.cloudplatform.model.Resource;
//import com.hp.core.dao.GenericDao;

public interface ResourceDao extends GenericDao<Resource, Long> {

	/**
	 * 资源申请统计
	 * @param type 资源类型
	 * @param resourcePoolId 资源池ID
	 * @return
	 */
	public List<Object> applicationTotalResource(Integer type, String resourcePoolId);
	
	/**
	 * 主机资源统计
	 * 
	 * @param type
	 *            主机资源类型
	 * @param resourcePoolId
	 *            资源池ID
	 * @return
	 */
	public List<Object> hostTotalResource(Integer type, String resourcePoolId);
	
	/**
	 * hpvm资源统计
	 * 
	 * @return
	 */
	public List<Object> hpvmTotalResource();
}

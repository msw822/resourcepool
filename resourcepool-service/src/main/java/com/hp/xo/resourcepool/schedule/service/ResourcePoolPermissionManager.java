package com.hp.xo.resourcepool.schedule.service;

import java.util.Map;

import com.hp.xo.resourcepool.dto.ResourcePoolPermissionVO;
import com.hp.xo.resourcepool.model.DbConfig;
import com.hp.xo.resourcepool.model.ResourcePoolPermission;
import com.hp.xo.resourcepool.request.ResourcePoolPermissionRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.GenericManager;

public interface ResourcePoolPermissionManager extends GenericManager<ResourcePoolPermission, Long> {

	/**
	 * 
	 * @return
	 */
	public ListResponse<DbConfig> getResourcePoolRelatedObjectType();

	/**
	 * save resource pool permission
	 * 
	 * @param request
	 * @return
	 */
	public EntityResponse<ResourcePoolPermission> saveResourcePoolPermission(ResourcePoolPermissionRequest request);

	/**
	 * get resource pool permission
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 */
	public ListResponse<ResourcePoolPermissionVO> getResourcePoolPermissions(ResourcePoolPermissionRequest request,
			Map<String, Object[]> requestParams);
	
	public ResourcePoolPermission getPool(Integer type,String value);
}

package com.hp.xo.resourcepool.schedule.service;

import com.hp.xo.resourcepool.model.DimResource;
import com.hp.xo.resourcepool.request.DimResourceTreeRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.GenericManager;

public interface DimResourceManager extends GenericManager<DimResource, Long> {
	/**
	 * 同步数据  资源表
	 */
	void synchronizeData();
	
	/**
	 * 
	 * @param drtr
	 * @return
	 */
	public ListResponse<DimResource> getDimResourceTree(DimResourceTreeRequest drtr);
}

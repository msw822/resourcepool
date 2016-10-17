package com.hp.xo.resourcepool.service;


import java.util.Map;

import com.hp.xo.resourcepool.model.DimResource;
import com.hp.xo.resourcepool.request.ListTopDataRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.vo.TopDataVO;



public interface TopDataManager extends GenericManager<DimResource, Long> {

	/**
	 * 
	 * @param request
	 * @param requestParams
	 * @return
	 */
	public ListResponse<TopDataVO> getTopData(ListTopDataRequest request, Map<String, Object[]> requestParams);
}

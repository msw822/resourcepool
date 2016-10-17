package com.hp.xo.resourcepool.service;

import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.vo.WorkOrderReportVO;
import com.hp.xo.resourcepool.request.ListWorkOrderReportRequest;
import com.hp.xo.resourcepool.response.ListResponse;


public interface WorkOrderReportManager extends GenericManager<WorkOrder, Long> {

	/**
	 * 
	 * @param req
	 * @return
	 */
	public ListResponse<WorkOrderReportVO> getWorkOrderReportData(ListWorkOrderReportRequest req);
}

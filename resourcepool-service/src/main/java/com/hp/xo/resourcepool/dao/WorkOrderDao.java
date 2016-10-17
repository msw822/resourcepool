package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.request.ListWorkOrderReportRequest;
import com.hp.xo.resourcepool.request.ListWorkOrderRequest;

public interface WorkOrderDao extends GenericDao<WorkOrder, Long> {

	/**
	 * @author Li Manxin
	 * @param example
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	List<WorkOrder> findWorkOrderList(final ListWorkOrderRequest example,
			final int pageSize, final int pageNumber);

	/**
	 * 
	 * @param req
	 * @return
	 */
	List<Object> findWorkOrderReport(ListWorkOrderReportRequest req);

	/***
	 * 检查状态
	 */
	WorkOrder checkStatus(int key, String name, String value);

	/**
	 * 查询总记录数
	 * 
	 * @param req
	 * @return
	 */
	Integer countByExample(ListWorkOrderRequest req);

	public List<WorkOrder> listWorkOrderByTypeAndStatus(List<Integer> typeList,
			List<Integer> statusList);
}

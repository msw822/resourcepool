package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.WorkOrderRelate;

public interface WorkOrderRelateDao extends GenericDao<WorkOrderRelate, Long> {
	public List<WorkOrderRelate> findWorkOrderRelateByWorkOrderId(Long  workOrderId);
}

package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.WorkOrderJob;

public interface WorkOrderJobDao extends GenericDao<WorkOrderJob, Long>{
	public List<WorkOrderJob> findWorkOrderJobByInstanceId(String instanceId);
}

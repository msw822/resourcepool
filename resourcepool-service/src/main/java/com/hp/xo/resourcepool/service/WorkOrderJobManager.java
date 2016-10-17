package com.hp.xo.resourcepool.service;

import java.util.List;

import com.hp.xo.resourcepool.model.WorkOrderJob;

public interface WorkOrderJobManager extends GenericManager<WorkOrderJob, Long> {

	public List<WorkOrderJob> findWorkOrderJobByInstanceId(String instanceId);
}

package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.WorkOrderJobDao;
import com.hp.xo.resourcepool.model.WorkOrderJob;
@Repository("orderJobDao")
public class WorkOrderJobDaoImpl extends GenericDaoImpl<WorkOrderJob, Long>  implements WorkOrderJobDao{

	public WorkOrderJobDaoImpl(Class<WorkOrderJob> persistentClass) {
		super(persistentClass);
	}
	public WorkOrderJobDaoImpl(){
		super(WorkOrderJob.class);
	}
	public List<WorkOrderJob> findWorkOrderJobByInstanceId(String instanceId){
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkOrderJob.class);
		criteria.add(Restrictions.eq("jobInstanceId", instanceId));
		List<WorkOrderJob> result = (List<WorkOrderJob>) findByCriteria(criteria);
		return result;		
	}
}

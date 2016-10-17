package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.WorkOrderRelateDao;
import com.hp.xo.resourcepool.model.WorkOrderRelate;
@Repository("orderRelateDao")
public class WorkOrderRelateDaoImpl extends GenericDaoImpl<WorkOrderRelate, Long> implements WorkOrderRelateDao{

	public WorkOrderRelateDaoImpl(Class<WorkOrderRelate> persistentClass) {
		super(persistentClass);
	}
	public WorkOrderRelateDaoImpl(){
		super(WorkOrderRelate.class);
	}
	public List<WorkOrderRelate> findWorkOrderRelateByWorkOrderId(Long  workOrderId){
		DetachedCriteria criteria = DetachedCriteria.forClass(WorkOrderRelate.class);
		criteria.add(Restrictions.eq("workOrderId", workOrderId));
		List<WorkOrderRelate> result = (List<WorkOrderRelate>) findByCriteria(criteria);
		return result;		
	}
}

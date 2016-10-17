package com.hp.xo.resourcepool.dao.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.WorkItemDao;
import com.hp.xo.resourcepool.model.WorkItem;
@Repository("itemDao")
public class WorkItemDaoImpl extends GenericDaoImpl<WorkItem, Long>  implements WorkItemDao{

	public WorkItemDaoImpl(Class<WorkItem> clazz) {
		super(clazz);
	}

	public WorkItemDaoImpl(){
		super(WorkItem.class);
	}
}

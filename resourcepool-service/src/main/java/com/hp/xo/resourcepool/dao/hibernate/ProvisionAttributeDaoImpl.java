package com.hp.xo.resourcepool.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.ProvisionAttributeDao;
import com.hp.xo.resourcepool.model.ProvisionAttribute;

@Repository("provisionAttributeDao")
public class ProvisionAttributeDaoImpl extends GenericDaoImpl<ProvisionAttribute, Long> implements
		ProvisionAttributeDao {
	public ProvisionAttributeDaoImpl(){
		super(ProvisionAttribute.class);
	}
	
	
	public ProvisionAttributeDaoImpl(Class<ProvisionAttribute> clazz) {
		super(clazz);
	}

}

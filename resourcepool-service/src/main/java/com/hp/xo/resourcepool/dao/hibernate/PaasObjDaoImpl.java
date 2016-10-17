package com.hp.xo.resourcepool.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.PaasObjDao;
import com.hp.xo.resourcepool.model.PaasDestoryObj;


@Repository("paasObjDao")
public class PaasObjDaoImpl extends GenericDaoImpl<PaasDestoryObj, Long> implements PaasObjDao {

	public PaasObjDaoImpl(Class<PaasDestoryObj> persistentClass) {
		super(persistentClass);
	}
	
	public PaasObjDaoImpl() {
		super(PaasDestoryObj.class);
	}

}

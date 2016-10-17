package com.hp.xo.resourcepool.schedule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.PaasObjDao;
import com.hp.xo.resourcepool.model.PaasDestoryObj;
import com.hp.xo.resourcepool.schedule.service.PaasDestoryObjManager;
import com.hp.xo.resourcepool.service.impl.GenericManagerImpl;
@Service(value="paasDestoryObjManager")
public class PaasDestoryObjManagerImpl extends GenericManagerImpl<PaasDestoryObj, Long>
		implements PaasDestoryObjManager {
	
	private PaasObjDao paasObjDao;
	
	public PaasDestoryObjManagerImpl() {
		super();
	}
	
	
	public PaasDestoryObjManagerImpl(PaasObjDao paasObjDao) {
		super(paasObjDao);
		this.paasObjDao = paasObjDao;
	}
	
	@Autowired
	public void setPaasObjDao(PaasObjDao paasObjDao) {
		this.paasObjDao = paasObjDao;
		this.dao = paasObjDao;
	}
}

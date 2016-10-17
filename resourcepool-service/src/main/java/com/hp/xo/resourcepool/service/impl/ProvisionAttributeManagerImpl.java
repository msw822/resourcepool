package com.hp.xo.resourcepool.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.ProvisionAttributeDao;
import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;

@Service(value = "provisionAttributeManager")
public class ProvisionAttributeManagerImpl extends
		GenericManagerImpl<ProvisionAttribute, Long> implements
		ProvisionAttributeManager {
	private ProvisionAttributeDao provisionAttributeDao;

	@Autowired
	public void setProvisionAttributeDao(
			ProvisionAttributeDao provisionAttributeDao) {
		this.provisionAttributeDao = provisionAttributeDao;
		this.dao = provisionAttributeDao;
	}

	public ProvisionAttributeManagerImpl() {
		super();
	}

	public ProvisionAttributeManagerImpl(
			ProvisionAttributeDao provisionAttributeDao) {
		super(provisionAttributeDao);
		this.dao = provisionAttributeDao;
	}

}

package com.hp.xo.resourcepool.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.DbConfigDao;
import com.hp.xo.resourcepool.model.DbConfig;
import com.hp.xo.resourcepool.service.DbConfigManager;



/**
 * 
 * @author Zhefang Chen
 *
 */
@Transactional(propagation=Propagation.REQUIRES_NEW)
@Service(value="dbConfigManager")
public class DbConfigManagerImpl extends GenericManagerImpl<DbConfig, Long> implements DbConfigManager  {
	private DbConfigDao dbConfigDao;
	
	@Autowired
	public void setDbConfigDao(DbConfigDao dbConfigDao) {
		this.dbConfigDao = dbConfigDao;
		this.dao = dbConfigDao;
	}

	public DbConfigManagerImpl() {
		super();
	}
	
    public DbConfigManagerImpl(DbConfigDao dbConfigDao) {
        super(dbConfigDao);
        this.dbConfigDao = dbConfigDao;
        this.dao = dbConfigDao;
    }

	public DbConfig getDbConfigByKey(String key) {
		return this.dbConfigDao.getDbConfigByKey(key);
	}
}

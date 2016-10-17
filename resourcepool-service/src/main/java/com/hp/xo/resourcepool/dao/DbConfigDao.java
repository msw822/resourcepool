package com.hp.xo.resourcepool.dao;

import com.hp.xo.resourcepool.model.DbConfig;



/**
 * 
 * @author Zhefang Chen
 *
 */
public interface DbConfigDao extends GenericDao<DbConfig, Long> {

	DbConfig getDbConfigByKey(String key);
}

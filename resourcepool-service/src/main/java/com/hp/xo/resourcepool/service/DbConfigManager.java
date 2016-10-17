package com.hp.xo.resourcepool.service;


import com.hp.xo.resourcepool.service.GenericManager;
import com.hp.xo.resourcepool.model.DbConfig;

/**
 * 
 * @author Zhefang Chen
 *
 */
public interface DbConfigManager extends GenericManager<DbConfig, Long>  {
	DbConfig getDbConfigByKey(String key);

}

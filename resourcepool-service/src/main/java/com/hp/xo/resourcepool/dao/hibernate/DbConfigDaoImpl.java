package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.hp.xo.resourcepool.dao.DbConfigDao;
import com.hp.xo.resourcepool.model.DbConfig;


/**
 * 
 * @author Zhefang Chen
 *
 */
@Repository("dbConfigDao")
public class DbConfigDaoImpl  extends GenericDaoImpl<DbConfig, Long> implements DbConfigDao {
	public DbConfigDaoImpl() {
		super(DbConfig.class);
	}
	
	public DbConfig getDbConfigByKey(String key) {
		DbConfig result = null;
		
		List cfgs = getSession().createCriteria(DbConfig.class).add(Restrictions.eq("key", key)).list();
        if (cfgs.isEmpty()) {
            result = null;
        } else {
            result= (DbConfig) cfgs.get(0);
        }
		
		return result;
	}
	
}

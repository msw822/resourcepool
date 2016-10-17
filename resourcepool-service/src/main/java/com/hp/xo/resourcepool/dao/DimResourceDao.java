package com.hp.xo.resourcepool.dao;

import com.hp.xo.resourcepool.model.DimResource;

public interface DimResourceDao extends GenericDao<DimResource,Long> {
	void deleteAll();
}

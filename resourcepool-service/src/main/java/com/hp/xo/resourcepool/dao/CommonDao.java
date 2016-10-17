package com.hp.xo.resourcepool.dao;

import java.io.Serializable;
import java.util.List;

public interface CommonDao extends GenericDao{
    public List<Serializable> findListByHql(String hql,Object[] paramters,int pageNumber, int pageSize);
    public List<Serializable> findListByHql(String hql,Object[] paramters);
    public long getCountByHql(String hql,Object[] paramters);
}

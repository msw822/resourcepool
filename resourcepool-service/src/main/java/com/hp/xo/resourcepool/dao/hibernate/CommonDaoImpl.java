package com.hp.xo.resourcepool.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

import com.hp.xo.resourcepool.dao.CommonDao;

public class CommonDaoImpl extends GenericDaoImpl implements CommonDao  {
	public CommonDaoImpl(Class clazz) {
		super(clazz);
	}
	public List<Serializable> findListByHql(String hql,Object[] paramters, int pageNumber, int pageSize) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		for(int i=0;i<paramters.length;i++){
			Object obj=paramters[i];
			query.setParameter(i, obj);
		}
		if (pageNumber >= 0) {
			final int start = pageSize * (pageNumber - 1);
			query.setFirstResult(start);
		}
		if (pageSize > 0) {
			query.setMaxResults(pageSize);
		}	
		return (List<Serializable>) query.list();
	}
	public List<Serializable> findListByHql(String hql,Object[] paramters){
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		for(int i=0;i<paramters.length;i++){
			Object obj=paramters[i];
			query.setParameter(i, obj);
		}
		return (List<Serializable>) query.list();
	}
	
	public long getCountByHql(String hql,Object[] paramters) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		for(int i=0;i<paramters.length;i++){
			query.setParameter(i, paramters[i]);			
		}
		return (Long) query.uniqueResult();
	}
}

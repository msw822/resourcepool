package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.GlobalSettingDao;
import com.hp.xo.resourcepool.model.GlobalSetting;
import com.hp.xo.resourcepool.request.ListGlobalSettingRequest;

@Repository("globalSettingDao")
public class GlobalSettingDaoImpl extends GenericDaoImpl<GlobalSetting, Long> implements
		GlobalSettingDao {
	
	public GlobalSettingDaoImpl(Class<GlobalSetting> persistentClass) {
		super(persistentClass);
	}

	public GlobalSettingDaoImpl() {
		super(GlobalSetting.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GlobalSetting> listUserDefined(
			ListGlobalSettingRequest example, int pageSize, int pageNumber) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(GlobalSetting.class);
		List<GlobalSetting> result = (List<GlobalSetting>) findByCriteria(criteria, pageSize, pageNumber);
		return result;
	}

	@Override
	public Integer countByExample(ListGlobalSettingRequest request) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GlobalSetting.class);
		
		return this.countByCriteria(criteria);
	}

	
	@Override
	public void updateBySQLQuery(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public List<?> findListBySql(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}

}

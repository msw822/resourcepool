package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.GlobalSetting;
import com.hp.xo.resourcepool.request.ListGlobalSettingRequest;

/**
 * 全局设置
 * GlobalSettingDao
 * @author lixinqi
 *
 */
public interface GlobalSettingDao extends GenericDao<GlobalSetting, Long> {
	
	/**
	 * 查—列表—ByExample
	 * @param example
	 * @param pageSize
	 * @param pageNumber
	 * @return
	 */
	List<GlobalSetting> listUserDefined(final ListGlobalSettingRequest example,
			final int pageSize, final int pageNumber);
	
	/**
	 * 查—列表—BySQL
	 * @param sql
	 * @return
	 */
	List<?> findListBySql(String sql);
	
	/**
	 * 查—总记录数—ByExample
	 * @param request
	 * @return
	 */
	Integer countByExample(ListGlobalSettingRequest request);
	
	/**
	 * 改—BySqlQuery
	 * @param sql
	 */
	void updateBySQLQuery(String sql);
	
}

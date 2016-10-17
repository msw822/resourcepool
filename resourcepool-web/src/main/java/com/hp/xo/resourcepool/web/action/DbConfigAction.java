package com.hp.xo.resourcepool.web.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.DbConfigDao;
import com.hp.xo.resourcepool.model.DbConfig;
import com.hp.xo.resourcepool.web.action.core.BaseAction;


@Transactional
public class DbConfigAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1143427426418106590L;
	private String configKey = "";
	@Autowired
	private DbConfigDao dbConfigDao;

	public String getConfigKey() {
		return configKey;
	}
	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public void setDbConfigDao(DbConfigDao dbConfigDao) {
		this.dbConfigDao = dbConfigDao;
	}
	
	public String listConfig() {
		//dbconfigManager = WebApplicationContextUtils.getWebApplicationContext(ServletActionContext.getServletContext()).getBean(DbConfigManager.class);
		DbConfig example = new DbConfig();
		example.setKey(configKey);
		List<DbConfig> configs = (List<DbConfig>) dbConfigDao.findByExample(example);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("configList", configs);
		this.renderText(jsonObj.toString()); 
		return NONE;
	}
}

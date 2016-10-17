/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.xo.resourcepool.configuration.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.Constants;
import com.hp.xo.resourcepool.configuration.AbstractConfigManager;
import com.hp.xo.resourcepool.utils.StringUtil;

/**
 * System configuration property manager
 * 
 * @author john
 *
 */
public final class AppConfigMgr extends AbstractConfigManager {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(this.getClass());
	private static final String	CONFIG_PATH					= "app.config.path";	
	private static final String	CONFIG_FILE					= "app.config.file";
	private static final String	DEFAULT_CONFIG_PROPERTIES	= "config.properties";
	private static final String	CONFIG_PREFIX				= "config";
	private static final char	KEY_SEPARATOR				= '.';
//	private static final String SYS_FILE 					= "system.properties";
//	private static final String APP_FILE	 				= "app.properties";
//	private static final String PATH_SEPARATOR				= "/";
//	private static final String APP_NAME 					= "appname";
//	private static final String APP_ENVIROMENT				= "app.environment";
//	private static final String CONSTANT_PREFIX				= "constant";
//	private static Map<String, String> CONSTANTS			= new HashMap<String, String>();
	private static AppConfigMgr	configFactory				= null;

	static {
		configFactory = new AppConfigMgr();
		configFactory.load();
	}

	private AppConfigMgr() {
		super();
	}

	/**
	 * Obtain system configuration manager singleton instance
	 * 
	 * @return System configuration manager
	 */
	public static AppConfigMgr getInstance() {
		return configFactory;
	}
	public void load() {
		PropertyConfigMgr propsConfig = null;
		String fileName = null;
		
		// try to get the config properties name from environment level
		String envPropsPath = System.getenv(CONFIG_PATH);
		String envPropsName = System.getenv(CONFIG_FILE);
		// try to get the config properties name from JVM level
		String jvmPropsPath = System.getProperty(CONFIG_PATH);
		String jvmPropsName = System.getProperty(CONFIG_FILE);
		// using default config properties name
		if (!StringUtil.isNullString(envPropsName)) {
			if(log.isInfoEnabled()){
				log.info("Found environment variable [ " + envPropsPath + " ].");
				log.info("Found environment variable [ " + envPropsName + " ].");
			}
			
			filePath = envPropsPath;
			fileName = envPropsName;
			
		} else if (!StringUtil.isNullString(jvmPropsName)) {
			if(log.isInfoEnabled()){
				log.info("Found JVM parameter -D [ " + jvmPropsPath + " ].");
				log.info("Found JVM parameter -D [ " + jvmPropsName + " ].");
			}			
			filePath = jvmPropsPath;
			fileName = jvmPropsName;

		} else {
//			filePath = ClassLoaderUtil.getResource(DEFAULT_CONFIG_PROPERTIES, SysConfigMgr.class).;
			fileName = DEFAULT_CONFIG_PROPERTIES;
						
		}
		
//		if (false == filePath.endsWith(PATH_SEPARATOR)) {
//			filePath = filePath + PATH_SEPARATOR;
//			
//		} 
		if (true == fileName.startsWith(Constants.FILE_SEP)) {
			fileName = fileName.substring(1);
		}
		propsConfig = new PropertyConfigMgr(fileName);		
		propsConfig.load();
				
		this._config = propsConfig.getConfigData();
		
		Iterator<String> _ci = this._config.keySet().iterator();
		Map<String, String> _rcfg = new HashMap<String, String>();
		while (_ci.hasNext()) {
			String _key = _ci.next();
			String _value = propsConfig.getValue(_key);
//			_value = replaceConstants(_value);	
			if(log.isDebugEnabled()){
				log.debug("config key: " + _key + " value: " + _value);
			}			
			_rcfg.put(_key, _value);
		}		
		this._config = _rcfg;
		
		String[] keys = (String[]) this._config.keySet().toArray(new String[this._config.size()]);
		for (String key : keys) {
			if (key.startsWith(CONFIG_PREFIX)) {
				propsConfig = new PropertyConfigMgr(getContextFilePath(key));
				propsConfig.load();
				int pos = key.indexOf(KEY_SEPARATOR);
				String propPrefix = key.substring(pos + 1, key.length());

				Iterator<String> _itr = propsConfig.getConfigData().keySet().iterator();
				while (_itr.hasNext()) {
					String _key = _itr.next();
					String _value = propsConfig.getValue(_key);
//					_value = replaceConstants(_value);
					if(log.isDebugEnabled()){
						log.debug("config-" + propPrefix + " key: " + propPrefix + KEY_SEPARATOR + _key + " value: " + _value);
					}					
					this._config.put(propPrefix + KEY_SEPARATOR + _key, _value);
				}
			}
		}
	}

	public void reload() {
		load();
	}

	@Override
	public String getEncryptedValue(String key) {
		try {			
			String value = this.getValue(key);
			return null ;//StringUtil.decrypt(value);
		} catch (Exception e) {
			throw new RuntimeException(e);			
		}
	}
	
	public static void main(String[] args) {
	//	String rowcount = AppConfigMgr.getInstance().getValue(AppConfigKey.DATATABLE_ROWCOUNT);
	//	System.out.println(rowcount);
		
	}
}

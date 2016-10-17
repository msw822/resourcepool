/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.xo.resourcepool.configuration;


import java.util.Date;
import java.util.Map;

/**
 * Configuration interface
 * 
 * @author Zhefang Chen
 *
 */
public interface Config {
	
	/**
	 * Only use by Properites file 
	 * @return
	 */
	String getContextPath();

	/**
	 * Only use by Properites file 
	 * @param fileName
	 * @return
	 */
	String getContextFilePath(final String key) ;
	
	/**
	 * Return configuration value as String.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return String value of the configuration property
	 */
	String getValue(String key);

	/**
	 * Return encrypted configuration value as String.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Decrypted value of the configuration property in String type
	 */
	String getEncryptedValue(String key);

	/**
	 * Return configuration value as Integer
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Integer value of the configuration property
	 */
	Integer getIntegerValue(String key);

	/**
	 * Return configuration value as Long.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Long value of the configuration property
	 */
	Long getLongValue(String key);

	/**
	 * Return configuration value as Float.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Float value of the configuration property
	 */
	Float getFloatValue(String key);

	/**
	 * Return configuration value as Date.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Date value of the configuration property
	 */
	Date getDateValue(String key);

	/**
	 * Return configuration timestamp as Date.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Date value of the configuration property
	 */
	Date getTimestampValue(String key);

	/**
	 * Return configuration value as Boolean.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Boolean value of the configuration property
	 */
	Boolean getBooleanValue(String key);

	/**
	 * Return configuration value as Double.
	 * 
	 * @param key
	 *            Configuration property key
	 * @return Double value of the configuration property
	 */
	Double getDoubleValue(String key);

	/**
	 * Return configuration value as Map
	 * 
	 * @param prefix
	 *            Configuration property key prefix
	 * @return Configuration value as a Map object
	 */
	Map<String, String> getValuesAsMap(String prefix);

	/**
	 * Return a copy of the configuration data in Map format.
	 * 
	 * @return Configuration data as Map
	 */
	Map<String, String> getConfigData();

}

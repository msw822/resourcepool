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

/**
 * Configuration manager interface
 * 
 * @author Zhefang Chen
 * 
 */
public interface ConfigManager {

	/**
	 * Load the configuration information from the data source.
	 */
	void load();

	/**
	 * Reload the configuration information from the data source.
	 */
	void reload();

}

/*
 * Copyright (c) 2009 Hutchison Global Communications Limited,
 *
 * All Rights Reserved.
 * This document contains proprietary information that shall be
 * distributed or routed only within HGC, and its authorized
 * clients, except with written permission of HGC.
 *
 */
package com.hp.xo.resourcepool.service;

import com.hp.xo.resourcepool.model.ActiveUser;



/**
 * ActiveUser aware , Provide to interceptor obtain BaseAction activeUser attribute  
 * 
 * @author Zhefang Chen
 *
 */
public interface ActiveUserAware {
	public void setActiveUser( ActiveUser user );
}

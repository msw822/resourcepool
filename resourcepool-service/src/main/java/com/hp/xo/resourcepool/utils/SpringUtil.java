package com.hp.xo.resourcepool.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtil {
	
	private static ApplicationContext applicationContext = null;
	
	public static void setApplicationContext(final ApplicationContext ac) {
		if (null == applicationContext) {
			applicationContext = ac;
		}
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}

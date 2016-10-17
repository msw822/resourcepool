package com.hp.xo.resourcepool.web.action.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 
 * Struts2 interceptor base class
 * 
 * @author john
 *
 */
public abstract class BaseInterceptor extends AbstractInterceptor {
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 */
	public BaseInterceptor() {
		
	}
	protected void debug(Object msg) {
		log.debug(this.getClass()+" - " + msg);
	} 
//	
//	protected void auditTrail(BaseUser user) {
//		AuditTrail at = new AuditTrailImpl();
//		at.setUser(user);		
//		auditLog.log(at);	
//	}	
//	
//	protected String getMethodName(Exception e) { 
//		return e.getStackTrace()[0].getMethodName();
//	}
//	
	protected void info(Object msg) {
		log.info(this.getClass()+" - " + msg);
	}
	
	protected HttpServletRequest getRequest() {
		HttpServletRequest request = (HttpServletRequest)ActionContext.
		getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		return request;
	}
}

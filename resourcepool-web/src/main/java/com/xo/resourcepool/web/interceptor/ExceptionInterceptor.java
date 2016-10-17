package com.xo.resourcepool.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.hp.xo.resourcepool.exception.ServiceException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
/**
 * @author Li Manxin
 */
public class ExceptionInterceptor implements Interceptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7386650935494684313L;

	public void destroy() {

	}

	public void init() {

	}

	public String intercept(ActionInvocation arg0) throws Exception {
		
		HttpServletResponse response = ServletActionContext.getResponse();
		PrintWriter writer = response.getWriter();
		try{
			arg0.invoke();
		}catch(ServiceException ex){
			
		}
		return null;
	}

}

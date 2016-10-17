package com.xo.resourcepool.web.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.ServiceConstants;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.web.action.core.BaseAction;
import com.hp.xo.resourcepool.web.action.core.BaseInterceptor;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @author john
 *
 */
public class AuthorizationInterceptor extends BaseInterceptor {
    private final Logger log = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 9087616558026470925L;
	
	public AuthorizationInterceptor() {
		super();	
		
	}


	@Override
	public String intercept(ActionInvocation actionInvocation) throws Exception {
		if (log.isDebugEnabled()) {
			HttpServletRequest request = this.getRequest();
			StringBuilder sb = new StringBuilder();
			sb.append("Request Method: " ).append(request.getMethod()).append("\n");
			sb.append("Request Protocol: " ).append(request.getProtocol()).append("\n");
			sb.append("Request QueryString: ").append(request.getQueryString()).append("\n");
			sb.append("Request ContextPath: ").append(request.getContextPath()).append("\n");
			sb.append("Request ContentType: ").append(request.getContentType()).append("\n");
			sb.append("Request PathInfo: ").append(request.getPathInfo()).append("\n");
			sb.append("Request URL: ").append(request.getRequestURL()).append("\n");
			sb.append("Request URI: ").append(request.getRequestURI()).append("\n");
			sb.append("Request ServletPath: ").append(request.getServletPath()).append("\n");
			sb.append("Request Scheme: ").append(request.getScheme()).append("\n");
			sb.append("Request Locale: ").append(request.getLocale()).append("\n");
			sb.append("Request CharacterEncoding: ").append(request.getCharacterEncoding()).append("\n");
			sb.append("Request LocalAddr: ").append(request.getLocalAddr()).append("\n");
			sb.append("Request LocalName: ").append(request.getLocalName()).append("\n");
			sb.append("Request LocalPort: ").append(request.getLocalPort()).append("\n");
			sb.append("Request RemoteAddr: ").append(request.getRemoteAddr()).append("\n");
			sb.append("Request RemoteHost: ").append(request.getRemoteHost()).append("\n");
			sb.append("Request RemotePort: ").append(request.getRemotePort()).append("\n");
			sb.append("Request RemoteUser: ").append(request.getRemoteUser()).append("\n");
			sb.append("Request SessionId: ").append(request.getRequestedSessionId()).append("\n");
			sb.append("Request AuthType: ").append(request.getAuthType()).append("\n");
			sb.append("Request PathTranslated: ").append(request.getPathTranslated()).append("\n");
			
		    Enumeration<String> headerNames = request.getHeaderNames();
		    while(headerNames.hasMoreElements()) {
		      String headerName = (String)headerNames.nextElement();
		      sb.append("headerName " ).append(headerName + ":").append(request.getHeader(headerName)).append("\n");		     
		    } 
		    
		    Cookie[] cks = request.getCookies();
		    if (cks != null) {
		    	for(Cookie ck : cks) {
		    		sb.append("cookie  " + ck.getName()+  ": " ).append(ck.getValue()).append("\n");
		    	}
		    }
		    
		    Enumeration<String> attributeNames = request.getAttributeNames();
		    if (attributeNames != null) {
			    while(attributeNames.hasMoreElements()) {
			      String attributeName = (String)attributeNames.nextElement();
			      sb.append("attributeName " ).append(attributeName + ":").append(request.getAttribute(attributeName)).append("\n");		     
			    }
		    } else {
		    	sb.append("attributeNames is null" ).append("\n");
		    }
		    
		    Enumeration<String> parameterNames = request.getParameterNames();
		    if (parameterNames != null) {
			    while(parameterNames.hasMoreElements()) {
			      String parameterName = (String)parameterNames.nextElement();
			      sb.append("parameterName " ).append(parameterName + ":").append(request.getParameter(parameterName)).append("\n");		     
			    }
		    } else {
		    	sb.append("parameterNames is null" ).append("\n");
		    }
		    
		    Enumeration<String> sessionAttributeNames = request.getSession().getAttributeNames();
		    if (sessionAttributeNames != null) {
			    while(sessionAttributeNames.hasMoreElements()) {
			      String sessionAttributeName = (String)sessionAttributeNames.nextElement();
			      if (ServiceConstants.ACTIVE_USER.equalsIgnoreCase(sessionAttributeName)) {
			    	  sb.append("sessionAttributeName " ).append(sessionAttributeName + ":").append(request.getSession().getAttribute(sessionAttributeName) != null ? "value is not null" : "value is null").append("\n");
			      } else {
			    	  sb.append("sessionAttributeName " ).append(sessionAttributeName + ":").append(request.getSession().getAttribute(sessionAttributeName)).append("\n");
			      }
			    }
		    } else {
		    	sb.append("sessionAttributeNames is null" ).append("\n");
		    }
		    	    
		    
			debug(sb.toString());

		}
		
		ActionSupport action = (ActionSupport) actionInvocation.getAction();
		Map<String, Object> session = actionInvocation.getInvocationContext().getSession();		
		ActiveUser user = (ActiveUser) session.get(ServiceConstants.ACTIVE_USER);
		ActiveUser cuser = new ActiveUser();
		if (null != user) {
		    BeanUtils.copyProperties(cuser, user); 
		    
			if (action instanceof BaseAction) {
				BaseAction currentAction = (BaseAction)action;
				currentAction.setActiveUser(cuser);

				Map<String, Object[]> requestParams = new HashMap<String, Object[]>();
				
				requestParams.putAll(this.getRequest().getParameterMap());
				currentAction.utf8Fixup(this.getRequest(), requestParams);
				
				Object[] keyObj = requestParams.get("apikey");
			    if (null == keyObj || keyObj.length < 1) {
			    	if (StringUtils.isNotEmpty(cuser.getApikey())) {
			    		requestParams.put("apikey", new Object[]{cuser.getApikey()});
			    	}
			    }
				
				keyObj = requestParams.get("secretkey");
			    if (null == keyObj || keyObj.length < 1) {
			    	if (StringUtils.isNotEmpty(cuser.getSecretkey())) {
			    		requestParams.put("secretkey", new Object[]{cuser.getSecretkey()});
			    	}
			    }
			    if(log.isDebugEnabled()){
			    	log.debug("user:" + user);
				    log.debug("cuser:" + cuser);
				    log.debug("apikey:" + cuser.getApikey());
				    log.debug("secretkey:" + cuser.getSecretkey());
			    }
			    currentAction.setRequestParams(requestParams);
			}
			
		} else {
			cuser.setLoginId("unloginuser");			
			cuser.setClientIP(getRequest().getRemoteHost());
			cuser.setDefaultLang(getRequest().getLocale().getCountry());
			
			if (action instanceof BaseAction) {
				BaseAction currentAction = (BaseAction)action;
				currentAction.setActiveUser(cuser);

				Map<String, Object[]> requestParams = new HashMap<String, Object[]>();
				
				requestParams.putAll(this.getRequest().getParameterMap());
				currentAction.utf8Fixup(this.getRequest(), requestParams);
				
				currentAction.setRequestParams(requestParams);
			}
			
		}

		
		// Check Session contain the USER
		if (log.isDebugEnabled()) {
			debug("user is " + ((null!=user)?user.getUserid():"") + " in session");
		}
//		
		
		return actionInvocation.invoke();
		
	}
	

}

package com.hp.xo.resourcepool.web.action.core;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.BeanUtils;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.Request;
import com.hp.xo.resourcepool.response.ResponseObject;
import com.hp.xo.resourcepool.utils.Page;
import com.hp.xo.resourcepool.utils.RequestWrapper;
import com.hp.xo.resourcepool.utils.ResponseSerializer;
import com.opensymphony.xwork2.ActionSupport;
import com.hp.xo.resourcepool.service.ActiveUserAware;
import com.hp.xo.resourcepool.service.RequestParamsAware;

public abstract class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ActiveUserAware, RequestParamsAware {
	private static final long serialVersionUID = -5473246055094516821L;
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	public static final String STATUS_SUCCESS="SUCCESS";
	public static final String STATUS_FAILURE="FAILURE";
	
	protected Map<String, Object> session = null;
	protected HttpServletRequest request = null;
	protected ActiveUser activeUser = null; 
	protected Map<String, Object[]> requestParams = null;
	
	public void setRequestParams(Map<String, Object[]> requestParams) {
		this.requestParams = requestParams;
	}

	public BaseAction() {
		super();
	}
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
		
	}
	
	public void setActiveUser(ActiveUser activeUser) {
		this.activeUser = activeUser;
		
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	public void utf8Fixup(HttpServletRequest req, Map<String, Object[]> params) {
		if (req.getQueryString() == null) {
			return;
		}
		String[] paramsInQueryString = req.getQueryString().split("&");
		if (paramsInQueryString != null) {
			for (String param : paramsInQueryString) {
				String[] paramTokens = param.split("=", 2);
				if (paramTokens != null && paramTokens.length == 2) {
					String name = paramTokens[0];
					String value = paramTokens[1];
					try {
						name = URLDecoder.decode(name, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					try {
						value = URLDecoder.decode(value, "UTF-8");
					} catch (UnsupportedEncodingException e) {
					}
					params.put(name, new String[] { value });
				} else {
					log.debug("Invalid parameter in URL found. param: " + param);
				}
			}
		}
	}
	
	protected void writeResponse(Response response) {
		HttpServletResponse resp = ServletActionContext.getResponse(); 
		try {
			if ("json".equalsIgnoreCase(response.getType())) {
				resp.setContentType("text/plain; charset=UTF-8");
			} else {
				resp.setContentType("text/xml; charset=UTF-8");
			}
			resp.setStatus(response.getStatusCode());
			resp.getWriter().print(response.getResponseString());
			resp.getWriter().flush();

		} catch (IOException ioex) {
			if (log.isTraceEnabled()) {
				log.trace("exception writing response: " + ioex);
			}
		} catch (Exception ex) {
			if (!(ex instanceof IllegalStateException)) {
				log.error("unknown exception writing api response", ex);
			}
		}
	}
	
	public void renderText(String text) {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("UTF-8");
		try {
			resp.getWriter().write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void writeResponse(ResponseObject response) {
		 
		Response resp = new Response();
		String type = "json";
		String responseString = ResponseSerializer.toSerializedString(response, type);
		int status = HttpStatus.SC_OK;
		
		if (StringUtils.isEmpty(response.getResponseName())) {
			response.setResponseName(response.getClass().getSimpleName().toLowerCase());
		}

		resp.setStatusCode(status);
		resp.setUserid(this.activeUser.getUserid());
		resp.setResponseString(responseString);
		resp.setType(type);
		
		this.writeResponse(resp);
	}
	
	protected   Response transformResponse(Object responseData, String responseType, int responseHTTPCode){
		 Response result = new  Response();
		Map m = new HashMap();
		m.put("root", responseData);
		JSONObject jsonObj = JSONObject.fromObject(m);
		result.setResponseString(jsonObj.toString());
		result.setStatusCode(responseHTTPCode);
		result.setType(responseType);
		return result;
	}
	
	protected Request wrapRequest(Request request) {
		BeanUtils.copyProperties(this.activeUser, request);
		RequestWrapper.getInstance().wrapRequest2(request, this.requestParams);
		
		return request;
	}
	
	/**
	 * 组装jqGrid分页信息参数
	 * 
	 * @throws JSONException
	 * */
	protected JSONObject getJsonPage(Page page) throws JSONException {

		JSONObject jsonPage = new JSONObject();
//------------update by liqiang 
		jsonPage.put("page", page.getPageNo());
		jsonPage.put("total",page.getTotalPages());
		jsonPage.put("records", page.getTotalPages());
		return jsonPage;
	}

}

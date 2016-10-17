package com.hp.xo.resourcepool.web.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.utils.Page;
import com.hp.xo.resourcepool.web.action.core.BaseAction;
@Transactional
public class PhysicsHostAction extends BaseAction{
	private static final long serialVersionUID = 1L;
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	@Autowired
	private HostManagerService hostManagerService=null;
	@Autowired	
	private LogManager logManager;
	public String listPhysicsHostCapacity(){
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		PhysicsHostRequest request = (PhysicsHostRequest) this.wrapRequest(new PhysicsHostRequest());
		JSONObject result =hostManagerService.getPhysicsHostsCapacity(request);		
		Response responseObj = new Response();
		responseObj.setResponseString(result.toString());
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(responseType);
		writeResponse(responseObj);
		return NONE;
	}
	public String listPhysicsHostStatus(){
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		PhysicsHostRequest request = (PhysicsHostRequest) this.wrapRequest(new PhysicsHostRequest());
		JSONObject result =hostManagerService.listPhysicsHostStatus(request);			
		Response responseObj = new Response();
		responseObj.setResponseString(result.toString());
		responseObj.setStatusCode(HttpStatus.SC_OK);
		responseObj.setType(responseType);
		writeResponse(responseObj);
		return NONE;
	}
	
	@SuppressWarnings("rawtypes")
	public String listPhysicsHost(){
//		HttpServletRequest requesta = (HttpServletRequest)ActionContext.getContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		PhysicsHostRequest request = (PhysicsHostRequest) this.wrapRequest(new PhysicsHostRequest());		
		ListResponse<PhysicsHost> response = hostManagerService.getPhysicsHost(request);		
		List<PhysicsHost> ordered = hostManagerService.getPhysicsHostWithOrderid(request.getOrderId());	
		List<PhysicsHost> list = response.getResponses();		
		for(PhysicsHost host:ordered){ 
			host.setHold0("isAssignmente");			
			list.add(host);			
		}
		
		Collections.sort(list, new Comparator<PhysicsHost>(){
			@Override
			public int compare(PhysicsHost o1, PhysicsHost o2) {				
				if(o1.getHold0()!=null){
					return -1;
				}
				return 1;
			}
		});		
		response.setResponses(list);
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("physicsHosts", response.getResponses());
		if(log.isInfoEnabled()){
			log.info(jsonPage.toString());
		}
		this.renderText(jsonPage.toString());
		return NONE;
	}
	public String listPhysicsByOrderId(){
		
		PhysicsHostRequest request = (PhysicsHostRequest) this.wrapRequest(new PhysicsHostRequest());
		
		List<PhysicsHost> response = hostManagerService.getPhysicsHostWithOrderid(request.getOrderId());
		
		JSONObject json = new JSONObject();
		
		json.put("ordered", response);
		
		this.renderText(json.toString());
		
		return NONE;		
	}
	public String physicsHostInfo() {
		String hostname = requestParams.get("hostname") != null ? requestParams.get("hostname")[0].toString() : "";
		String type = requestParams.get("type") != null ? requestParams.get("type")[0].toString() : "";
		PhysicsHost host = hostManagerService.getPhysicsHost(hostname);
		JSONObject jsonHost = new JSONObject();
		jsonHost.put("host", host);
		if(log.isInfoEnabled()){
			log.info(jsonHost.toString());
		}
		this.renderText(jsonHost.toString());
		return NONE;
	}
	public HostManagerService getHostManagerService() {
		return hostManagerService;
	}
	public void setHostManagerService(HostManagerService hostManagerService) {
		this.hostManagerService = hostManagerService;
	}
	public LogManager getLogManager() {
		return logManager;
	}
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}
}

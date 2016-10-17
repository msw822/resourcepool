package com.hp.xo.resourcepool.web.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.web.action.core.BaseAction;

public class LogoutAction extends BaseAction {
	private static final long serialVersionUID = -4334736909537757982L;
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	
	public String execute() {
		log.debug("----------------------------");
        log.debug("---------------------------- logout");
        log.debug("----------------------------");
		return logout();
	}

	private String logout() {
		Response loginResponse = genericCloudServerManager.get(this.requestParams);

		this.writeResponse(loginResponse);		
		this.request.getSession().invalidate();		

		return NONE;
	}

	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}

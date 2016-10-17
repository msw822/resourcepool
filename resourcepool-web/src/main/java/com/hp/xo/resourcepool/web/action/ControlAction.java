package com.hp.xo.resourcepool.web.action;

import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.web.action.core.BaseAction;


public class ControlAction extends BaseAction {
	
	private static final long serialVersionUID = 6088453089914883547L;
	private static final Logger log = Logger.getLogger(ControlAction.class.getName());
	protected static final String COMMAND = "command";
	protected static final String DEFAULT = "generic";
	protected static final String CMSZ = "cmsz";
	protected static final String CMSZ_YES = "yes";
	
	public String execute() {
		String forward = NONE;
		
		Object[] cmszObj = this.requestParams.get(CMSZ);
		Object[] commandObj = this.request.getParameterMap().get(COMMAND);
		String command = (String) commandObj[0];
		if (null == cmszObj || cmszObj.length < 1) {
			if ("login".equalsIgnoreCase(command) || "logout".equalsIgnoreCase(command)) {
				forward = command;
			} else {
				forward = DEFAULT;
			}
			
			
		} else {
			String cmsz = (String) cmszObj[0];
			if (CMSZ_YES.equalsIgnoreCase(cmsz)) {
				
				forward = command;
			} else {
				forward = DEFAULT;
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("forward >>> " + forward);
		}
		
		return forward;
	}
	
}

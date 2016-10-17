package com.hp.xo.resourcepool.web.action;


import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.VirtualMachineProjectRequest;
import com.hp.xo.resourcepool.request.VirtualMachineRequest;
import com.hp.xo.resourcepool.schedule.VirtualMachineSchedule;
import com.hp.xo.resourcepool.schedule.service.ScheduleService;
import com.hp.xo.resourcepool.web.action.core.BaseAction;
@Transactional
public class VirtualMachineAction  extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private ScheduleService virtualMachineManager;
	public String destroyVirtualMachine(){
		try{
			VirtualMachineSchedule virtualMachineSchedule=new VirtualMachineSchedule();
			virtualMachineSchedule.setVirtualMachineManager(virtualMachineManager);
			virtualMachineSchedule.setGenericCloudServerManager(virtualMachineManager.getGenericCloudServerManager());
			VirtualMachineRequest buessionRequest=(VirtualMachineRequest)this.wrapRequest(new VirtualMachineRequest());
			Map<String, Object[]> cloudStackParams=virtualMachineSchedule.getCloudStackParams();
			JSONObject json=new JSONObject();
			json.put("virtualMachineId", buessionRequest.getId());
			json.put("expunge",buessionRequest.isExpunge());
			JSONObject jobresult=virtualMachineManager.destroyInstance(json, cloudStackParams);			
			this.renderText(jobresult.toString());
			
		}catch(BuessionException er){
			log.error(er.getMessage(),er);
		}
		
		return NONE;
	}
	public String migrateVirtualMachineCmdb(){
		try{
			VirtualMachineRequest buessionRequest=(VirtualMachineRequest)this.wrapRequest(new VirtualMachineRequest());
			JSONObject json=new JSONObject();
			json.put("name", buessionRequest.getName());
			json.put("hostname", buessionRequest.getHostname());
			json.put("instancename",buessionRequest.getInstancename());
			JSONObject jobresult=virtualMachineManager.migrateVirtualMachineCmdb(json);			
			this.renderText(jobresult.toString());
			
		}catch(BuessionException er){
			log.error(er.getMessage(),er);
		}
		
		return NONE;
	}
	
	public String updateProjectVirtualMachineCmdb(){
		try{
			VirtualMachineProjectRequest buessionRequest=(VirtualMachineProjectRequest)this.wrapRequest(new VirtualMachineProjectRequest());
			JSONObject json=new JSONObject();
			json.put("projectName", buessionRequest.getProjectName());
			json.put("newProjectName", buessionRequest.getNewProjectName());
			json.put("instancename",buessionRequest.getInstancename());
			JSONObject jobresult=virtualMachineManager.updateProjectVirtualMachineCmdb(json);			
			this.renderText(jobresult.toString());
			
		}catch(BuessionException er){
			log.error(er.getMessage(),er);
		}
		
		return NONE;
	}
	public ScheduleService getVirtualMachineManager() {
		return virtualMachineManager;
	}
	public void setVirtualMachineManager(ScheduleService virtualMachineManager) {
		this.virtualMachineManager = virtualMachineManager;
	}
}

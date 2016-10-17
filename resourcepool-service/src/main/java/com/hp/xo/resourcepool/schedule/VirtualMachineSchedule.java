package com.hp.xo.resourcepool.schedule;

import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.schedule.service.ScheduleService;

@Service(value = "virtualMachineSchedule")
public class VirtualMachineSchedule extends AbstractSchedule{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ScheduleService virtualMachineManager;
	public boolean destroyVirtualMachine() {
		log.info("-----------------------------------服务开通  begin--------------------------------------------------------");
		try{
			this.setGenericCloudServerManager(virtualMachineManager.getGenericCloudServerManager());
			Map<String, Object[]> cloudStackParams=this.getCloudStackParams();
//			virtualMachineManager.destroy(cloudStackParams);	
			JSONObject json=new JSONObject();
			json.put("virtualMachineId", "c009d4ae-7564-43bf-9511-bd36710b072e");
			virtualMachineManager.destroyInstance(json, cloudStackParams);
			
		}catch(BuessionException er){
			log.error(er.getMessage(), er);
			return false;
		}
		log.info("-----------------------------------服务开通  end--------------------------------------------------------");
		return true;
	}
	public ScheduleService getVirtualMachineManager() {
		return virtualMachineManager;
	}
	public void setVirtualMachineManager(ScheduleService virtualMachineManager) {
		this.virtualMachineManager = virtualMachineManager;
	}
	
}

package com.hp.xo.resourcepool.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.schedule.service.DimResourceManager;
@Service(value = "resourceSchedule")
public class ResourceSchedule {
	private final Logger log = LoggerFactory.getLogger(ResourceSchedule.class);

	@Autowired
	private DimResourceManager dimResourceManager = null; 
	
	public void work(){
		
		log.info("-----------------------------------资源同步  begin--------------------------------------------------------");
		dimResourceManager.synchronizeData();
		log.info("-----------------------------------资源同步  end--------------------------------------------------------");;
		
		
	}
}

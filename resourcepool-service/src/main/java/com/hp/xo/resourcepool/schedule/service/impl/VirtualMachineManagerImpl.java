package com.hp.xo.resourcepool.schedule.service.impl;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.schedule.service.ScheduleService;
import com.hp.xo.resourcepool.service.HostManagerService;
@Service(value="virtualMachineManager")
public class VirtualMachineManagerImpl extends AbstracServiceImpl implements ScheduleService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private HostManagerService hostManagerService;
	@Override
	public Map<String, String> getOrderTypeSpecificListParams(){
		Map<String, String> woSpecificParams = new LinkedHashMap();
		woSpecificParams.put("commandName", "listVirtualMachines");
		woSpecificParams.put("responseHead", "listvirtualmachinesresponse");
		woSpecificParams.put("jobResultresponseHead", "virtualmachine");
		return woSpecificParams;
	}
	@Override
	public Map<String, String> getOrderTypeSpecificParams(){
		Map<String, String> woSpecificParams = new LinkedHashMap();
		woSpecificParams.put("commandName", "destroyVirtualMachine");
		woSpecificParams.put("responseHead", "destroyvirtualmachineresponse");
		woSpecificParams.put("jobResultresponseHead", "virtualmachine");
		return woSpecificParams;
		
	}
	
	@Override
	public void putProvisionListAttributes(Map<String, Object[]> cloudStackParams,JSONObject json){
		cloudStackParams.put("id", new Object[]{json.getString("virtualMachineId")});
	}
	@Override
	public void putProvisionAttributes(Map<String, Object[]> cloudStackParams,JSONObject json){
		cloudStackParams.put("id", new Object[]{json.getString("virtualMachineId")});
		if(json.get("expunge")!=null){
			cloudStackParams.put("expunge", new Boolean[]{Boolean.valueOf(json.get("expunge").toString())});
		}else{
			cloudStackParams.put("expunge", new Boolean[]{true});
		}
		
	}
	
	@Override
	public JSONObject getInfoByOrder(WorkOrder order){
		WorkItem example = new WorkItem();
		example.setWorkOrderId(order.getId());
		example.setStep(3);
		List<WorkItem> items=(List<WorkItem>)this.getOrderManager().findByExample(example);
		if(items==null ||items.isEmpty()){
			return null;
		}else{
			WorkItem item=items.get(0);
			JSONObject json=new JSONObject();
			json.put("virtualMachineId", item.getAttributeValue());
			return json;
		}		
	}
	
	/**
	 * cmdb相关的处理
	 * @param cloudStackParams
	 * @param workOrder
	 * @param resultJSONObj
	 * @param woSpecificParams
	 */
	public boolean jobSucceedPost(Map<String, Object[]> cloudStackParams,JSONObject instanceInfo, JSONObject resultJSONObj,Map<String, String> specificParams){
		String virtualMachineName=instanceInfo.getString("instancename");
		String name=instanceInfo.getString("name");
		JSONArray virtualMachines = new  JSONArray();
		JSONObject virtualMachine=new JSONObject();
		virtualMachine.put("instancename", virtualMachineName);
		virtualMachine.put("name", name);
		if(instanceInfo.containsKey("project")){
			virtualMachine.put("project",instanceInfo.getString("project"));
		}else{
			virtualMachine.put("project","default");
		}
		virtualMachines.add(virtualMachine);
		try{
			Response rmiResponse=hostManagerService.deleteVirtualCItoCMDB(virtualMachines);
			if(rmiResponse.getStatusCode()!=HttpStatus.SC_OK){
				log.info(rmiResponse.getResponseString());
			}
			return true;
		}catch(ServiceException er){
			log.error(er.getMessage(), er);
			return false;
		}
		
		
	}
	public HostManagerService getHostManagerService() {
		return hostManagerService;
	}
	public void setHostManagerService(HostManagerService hostManagerService) {
		this.hostManagerService = hostManagerService;
	}
	@Override
	public JSONObject migrateVirtualMachineCmdb(JSONObject json)
			throws BuessionException {
		// TODO Auto-generated method stub
		Response rmiResponse=hostManagerService.migrateVirtualMachineCmdb(json);
		return null;
	}
	@Override
	public JSONObject updateProjectVirtualMachineCmdb(JSONObject json)
			throws BuessionException {
		// TODO Auto-generated method stub
		Response rmiResponse=hostManagerService.updateProjectVirtualMachineCmdb(json);
		return null;
	}
	
	
}

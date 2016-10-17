package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.rmi.RmiPhysicsHostBuilder;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.StringUtil;
import com.hp.xo.uip.cmdb.domain.CiAttribute;

@Service(value = "saDeployVMImpl")
public class SADeployVMImpl extends AbstractSAManager {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;
	@Autowired
	private HostManagerService hostManagerService;
	@Override
	public Map<String, String> getOrderTypeSpecificParams() {
		Map<String, String> woSpecificParams = new LinkedHashMap();
		woSpecificParams.put("commandName", "deployVirtualMachine");
		woSpecificParams.put("responseHead", "deployvirtualmachineresponse");
		woSpecificParams.put("jobResultresponseHead", "virtualmachine");
		return woSpecificParams;
	}
	
	public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder){    	
	 List<WorkItem> workItemList=workOrder.getWorkItems();
		Collections.sort(workItemList, new Comparator(){
			public int compare(Object o1, Object o2) {  
		        WorkItem s1=(WorkItem)o1;  
		        WorkItem s2=(WorkItem)o2;  
		        
		        if(s1.getStep()<s2.getStep()){  
		            return -1;  
		        }  
		        if(s1.getStep()>s2.getStep()){  
		            return 1;  
		        }  
		        return 0;  
		    }  
		});  
		//将工单的开通属性放入MAP中。
		for(WorkItem wi:workItemList){
			ProvisionAttribute pa=new ProvisionAttribute();
			pa.setWorkOrderType(workOrder.getWorkOrderType());
			List paList=getProvisionAttributeManager().findByExample(pa);
			Iterator iterPa=paList.iterator();
			while(iterPa.hasNext()){
				ProvisionAttribute pa1=(ProvisionAttribute)iterPa.next();
				if(pa1.getAttributeName().equalsIgnoreCase(ignoreFields)){
					continue;
				}
				String attributeName=wi.getAttributeName();
				if(pa1.getAttributeName().equalsIgnoreCase(wi.getAttributeName())){
					String attributeValue=wi.getAttributeValue();
					if(null!=attributeValue&&!"".equalsIgnoreCase(attributeValue.trim())){
						attributeName=attributeName.trim();
						attributeValue=attributeValue.trim();
						cloudStackParams.put(attributeName, new Object[]{attributeValue});
					}					
				}
			}
         }
		//projectid = -1 表示是默认视图
		if(!cloudStackParams.containsKey("projectid") || "-1".equals(cloudStackParams.get("projectid")[0].toString())){
			cloudStackParams.put("account", new Object[]{workOrder.getAccount()});
			cloudStackParams.put("domainid", new Object[]{workOrder.getDomainId()});
		}		
		//删除diskofferingid
		if(cloudStackParams.containsKey("diskSize") && cloudStackParams.get("diskSize").length>0 && cloudStackParams.get("diskSize")[0]!=null
				&& StringUtils.isNotBlank(cloudStackParams.get("diskSize")[0].toString())){
			cloudStackParams.put("size", cloudStackParams.get("diskSize"));
			cloudStackParams.remove("diskSize");
		}
		//删除networkids
		if(cloudStackParams.containsKey("networkids")){
			if(cloudStackParams.get("networkids")[0]==null||"undefined".equals(cloudStackParams.get("networkids")[0].toString())){
				cloudStackParams.remove("networkids");
			}else if(1==1){
				//待添加 高级zone和普通zone区别
			}			
		}
		//处理securitygroupids
		if(cloudStackParams.containsKey("securitygroupids")){
			String securitygroupids=(String)cloudStackParams.get("securitygroupids")[0];
			if("undefined".equals(securitygroupids)|| StringUtils.isBlank(securitygroupids)){
				cloudStackParams.remove("securitygroupids");				
			}
		}
		//处理affinitygroupids
		if(cloudStackParams.containsKey("affinitygroupids")){
			String affinitygroupids=(String)cloudStackParams.get("affinitygroupids")[0];
			if("undefined".equals(affinitygroupids)|| StringUtils.isBlank(affinitygroupids)){
				cloudStackParams.remove("affinitygroupids");				
			}
		}
		
		return cloudStackParams;
	}
	/**
     * 订单后处理
     * @param cloudStackParams
     * @param workOrder
     * @return
     */
    public boolean jobSucceedPost(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject commandresponseObj,Map<String, String> woSpecificParams){
    	JSONArray arrHosts=new JSONArray();
    	JSONObject host=new JSONObject();
    	JSONObject jobresultJO = commandresponseObj.getJSONObject("jobresult");
		String jobResultresponseHead = woSpecificParams.get("jobResultresponseHead");
		JSONObject resultHeadJO = jobresultJO.getJSONObject(jobResultresponseHead);
//    	host.put("id", resultHeadJO.get("id"));
//    	host.put("name", resultHeadJO.get("name"));
//    	host.put("displayName", resultHeadJO.get("displayname"));
//    	host.put("instanceName", resultHeadJO.get("instancename"));
//    	host.put("hostId", resultHeadJO.get("hostid"));
//    	host.put("hostname", resultHeadJO.get("hostname")); 
//    	host.put("memory", resultHeadJO.get("memory")); 
//    	host.put("cpuNumber", resultHeadJO.get("cpunumber")); 
//    	host.put("hyperVisor", resultHeadJO.get("hypervisor"));
//    	host.put("state", resultHeadJO.get("state"));
//    	host.put("zoneId", resultHeadJO.get("zoneid"));
//    	host.put("zoneName", resultHeadJO.get("zonename"));    	
//    	host.put("domain", resultHeadJO.get("domain"));
		//RmiPhysicsHostBuilder rb = new RmiPhysicsHostBuilder();
		
		Map<String,String> mapMaping=RmiPhysicsHostBuilder.getVirtualHostMaping();
		for(Iterator<String> itor= mapMaping.keySet().iterator();itor.hasNext();){
			String key=itor.next().toString();
			if(resultHeadJO.containsKey(key)){
				host.put(mapMaping.get(key), resultHeadJO.get(key));
			}
			else{
				if(key.equalsIgnoreCase("project")){
				host.put(mapMaping.get(key), "default");
				}
			}
		}
    	JSONArray  arrNetWork= resultHeadJO.getJSONArray("nic");
    	if(arrNetWork!=null && arrNetWork.size()>0){
    		for (int i = 0; i < arrNetWork.size(); i++) {
        		JSONObject jo = (JSONObject) arrNetWork.get(i);
        		host.put(mapMaping.get("ipaddress"), jo.get("ipaddress"));  
        	}   
    	}else{
    		host.put(mapMaping.get("ipaddress"), "N/A");  
    	}  
    	arrHosts.add(host);
    	try{
    		hostManagerService.saveVirtualCItoCMDB(arrHosts);
        	return true;
    	}catch(ServiceException er){
    		log.error(er.getMessage(),er);
    		return false;
    	}
    	
    }
    public HostManagerService getHostManagerService() {
		return hostManagerService;
	}
	public void setHostManagerService(HostManagerService hostManagerService) {
		this.hostManagerService = hostManagerService;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}

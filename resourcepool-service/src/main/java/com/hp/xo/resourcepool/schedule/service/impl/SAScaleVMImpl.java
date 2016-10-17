package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.rmi.RmiPhysicsHostBuilder;
import com.hp.xo.resourcepool.service.HostManagerService;


@Service(value="saScaleVMImpl")
public class SAScaleVMImpl extends AbstractSAManager {
	private final Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private HostManagerService hostManagerService;
	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		Map<String,String> woSpecificParams =new LinkedHashMap();
		woSpecificParams.put("commandName", "scaleVirtualMachine");
		woSpecificParams.put("responseHead", "scalevirtualmachineresponse");
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
					String attributeName=wi.getAttributeName();
					
					if(attributeName.equalsIgnoreCase("vmid")){
						attributeName = "id";
					}
					if(attributeName.equalsIgnoreCase(ignoreFields)){
						continue;
					}
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
			
			return cloudStackParams;
		};
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
	    	hostManagerService.scaleVirtualMachineCmdb(resultHeadJO);
	        return true;
	    }	
}
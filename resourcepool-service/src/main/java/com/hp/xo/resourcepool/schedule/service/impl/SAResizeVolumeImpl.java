package com.hp.xo.resourcepool.schedule.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.ProvisionAttribute;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkItem;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.schedule.SASchedule;
import com.hp.xo.resourcepool.service.ProvisionAttributeManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
@Service(value="saResizeVolumeImpl")
public class SAResizeVolumeImpl extends AbstractSAManager {
	private final Logger log = LoggerFactory.getLogger(SASchedule.class);
	@Autowired
	private ProvisionAttributeManager provisionAttributeManager=null;	
	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	@Override
	public Map<String,String> getOrderTypeSpecificParams(){
		Map<String,String> woSpecificParams =new LinkedHashMap();
		woSpecificParams.put("commandName", "resizeVolume");
		woSpecificParams.put("responseHead", "resizevolumeresponse");
		woSpecificParams.put("jobResultresponseHead", "volume");
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
			List paList=provisionAttributeManager.findByExample(pa);
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
			if(cloudStackParams.containsKey("diskSize") && cloudStackParams.get("diskSize").length>0 && cloudStackParams.get("diskSize")[0]!=null
					&& StringUtils.isNotBlank(cloudStackParams.get("diskSize")[0].toString())){
				cloudStackParams.put("size", cloudStackParams.get("diskSize"));
				cloudStackParams.remove("diskSize");
			}
			if(cloudStackParams.containsKey("diskid") && cloudStackParams.get("diskid").length>0 && cloudStackParams.get("diskid")[0]!=null
					&& StringUtils.isNotBlank(cloudStackParams.get("diskid")[0].toString())){
				cloudStackParams.put("id", cloudStackParams.get("diskid"));
				cloudStackParams.remove("diskid");
			}
         }
		return cloudStackParams;
	};
	public ProvisionAttributeManager getProvisionAttributeManager() {
		return provisionAttributeManager;
	}
	
	public void setProvisionAttributeManager(ProvisionAttributeManager provisionAttributeManager) {
		this.provisionAttributeManager = provisionAttributeManager;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager) {
	
		this.genericCloudServerManager = genericCloudServerManager;
	}
	
}

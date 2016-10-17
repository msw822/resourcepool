package com.hp.xo.resourcepool.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;

//import com.cmsz.cloudplatform.model.response.Response;
//import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;
@Service("scaleVMAttributeValueManager")
public class ScaleVMAttributeValueManagerImpl extends
		GenericCloudServerManagerImpl implements ProvisionAttributeValueManager {
	
	/**虚机*/
	private String EXTERNAL_VMID  ="vmid";
	
	/**服务方案*/
	private String EXTERNAL_SERVICEOFFERINGID="serviceofferingid";
	
	@Override
	public Map<String, Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.put("sessionkey", new Object[]{sessionkey});
		params.putAll(valueParam);
		
		if(EXTERNAL_VMID.equalsIgnoreCase(attributeName)){
			params.put("command", new Object[]{"listVirtualMachines"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listvirtualmachinesresponse")){
					json = json.getJSONObject("listvirtualmachinesresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("virtualmachine");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("instancename"));
						}
					}
				}
			}
		}else if(EXTERNAL_SERVICEOFFERINGID.equalsIgnoreCase(attributeName)){
			params.put("command", new Object[]{"listServiceOfferings"});
			Response response = this.get(params);
			params.put("issystem", new Object[]{"false"});//issystem=false 计算方案，issystem=true 系统方案，
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listserviceofferingsresponse")){
					json = json.getJSONObject("listserviceofferingsresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("serviceoffering");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
		}
		return resultMap;
	}

}

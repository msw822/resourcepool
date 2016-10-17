package com.hp.xo.resourcepool.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;

/*import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;*/
@Service("newVolumeAttributeValueManager")
public class NewVolumeAttributeValueManagerImpl extends
		GenericCloudServerManagerImpl implements ProvisionAttributeValueManager {
	
	/**虚机*/
	private String EXTERNAL_VIRTUALMACHINEID ="virtualmachineid"; 
	
	/**数据磁盘方案*/
	private String EXTERNAL_DISKOFFERINGID = "diskOfferingId";
	
	@Override
	public Map<String, Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam) {
Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.put("sessionkey", new Object[]{sessionkey});
		params.putAll(valueParam);
		
		if(EXTERNAL_VIRTUALMACHINEID.equalsIgnoreCase(attributeName)){
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
		}else if(EXTERNAL_DISKOFFERINGID.equalsIgnoreCase(attributeName)){
			params.put("command", new Object[]{"listDiskOfferings"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listdiskofferingsresponse")){
					json = json.getJSONObject("listdiskofferingsresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("diskoffering");
						//resultMap.put("root", jsonArray);
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							//resultMap.put(json.getString("id"),json.getString("displaytext"));
							resultMap.put(json.getString("id"),json);
						}
					}
				}
			}
		}
		return resultMap;	
	}

}

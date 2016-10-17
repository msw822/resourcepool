package com.hp.xo.resourcepool.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;

@Service("domainLimitAttributeValueManager")
public class DomainLimitAttributeValueManagerImpl extends GenericCloudServerManagerImpl implements
		ProvisionAttributeValueManager {
	
	private String EXTERNAL_ATTRIBUTE1 ="domainId";
	
	/*@Autowired
	private ResourcePoolManager resourcePoolManager;*/

	@Override
	public Map<String, Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		if(EXTERNAL_ATTRIBUTE1.equals(attributeName)){//domainId
			Map<String,Object[]> params =  new HashMap<String,Object[]>();
			params.putAll(valueParam);
			params.put("sessionkey", new Object[]{sessionkey});
			params.put("command", new Object[]{"listDomains"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject jsonObject = JSONObject.fromObject(response.getResponseString());
				if(jsonObject.containsKey("listdomainsresponse")){
					jsonObject = jsonObject.getJSONObject("listdomainsresponse");
					if(jsonObject.containsKey("domain")&& jsonObject.getJSONArray("domain").isArray()){
						JSONArray jsonArray = jsonObject.getJSONArray("domain");
						if(jsonArray.size()>0){
							jsonObject = (JSONObject)jsonArray.get(0);
							resultMap.put(jsonObject.getString("id"),jsonObject.getString("name"));
						}
					}
				}
			}
		}
		return resultMap;
	}

}

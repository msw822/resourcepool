package com.hp.xo.resourcepool.service.impl;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;


@Service("projectLimitAttributeValueManager")
public class ProjectLimitAttributeValueManagerImpl extends
		GenericCloudServerManagerImpl implements ProvisionAttributeValueManager {

	
	
	/**项目名称*/
	private String EXTERNAL_PROJECTID = "projectid";
	
	
	@Override
	public Map<String, Object> getExtval(String attributeName,
			String sessionkey, Map<String, Object[]> valueParam) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.put("sessionkey", new Object[]{sessionkey});
		params.putAll(valueParam);
		
		if(EXTERNAL_PROJECTID.equalsIgnoreCase(attributeName)){
			params.put("command", new Object[]{"listProjects"});
			//params.put("id",valueParam.get("id"));
			//params.put("response", new Object[]{"json"});
			params.put("listall", new Object[]{"true"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listprojectsresponse")){
					json = json.getJSONObject("listprojectsresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("project");
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

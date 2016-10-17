package com.hp.xo.resourcepool.web.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.request.ListBaremetalHostRequest;
import com.hp.xo.resourcepool.request.ListWorkOrderRequest;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.utils.Page;
import com.hp.xo.resourcepool.web.action.core.BaseAction;
@Transactional
public class BaremetalHostAction extends BaseAction{
	@Autowired
	private  GenericCloudServerManagerImpl genericCloudServerManager;
	public String list(){
		ListBaremetalHostRequest request = (ListBaremetalHostRequest) this.wrapRequest(new ListBaremetalHostRequest());
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.put("response", new Object[]{"json"});
		params.put("apikey",new Object[]{request.getApikey()});
		params.put("secretkey",new Object[]{request.getSecretkey()});
		params.put("command", new Object[]{"listHosts"});
		if(request.getHypervisor()!=null){			
			params.put("hypervisor", new Object[]{request.getHypervisor()});
		}
		if(request.getZoneid()!=null){			
			params.put("zoneid", new Object[]{request.getZoneid()});
		}
		if(request.getResourcestate()!=null){			
			params.put("resourcestate", new Object[]{request.getResourcestate()});
		}
		Response response = genericCloudServerManager.get(params);
		JSONObject JSONObj = JSONObject.fromObject(response.getResponseString());				
		String commandresponseStr=JSONObj.getString("listhostsresponse");
		JSONObject listhostsresponse = JSONObject.fromObject(commandresponseStr);
		if(listhostsresponse.containsKey("count") && listhostsresponse.getInt("count")>0){
			JSONArray array = listhostsresponse.getJSONArray("host");
			int size =array.size();
			JSONArray returnArray = new JSONArray();
			for (int i = 0; i < size; i++) {
				JSONObject jo = (JSONObject) array.get(i);
				params.put("command", new Object[]{"listVirtualMachines"});
				params.put("hostid", new Object[]{jo.get("id")});
				Response listVirtualMachinesResponse = genericCloudServerManager.get(params);						
				if(listVirtualMachinesResponse!=null){
						JSONObject jsonResponse = JSONObject.fromObject(listVirtualMachinesResponse.getResponseString());
						String jsonStr=jsonResponse.getString("listvirtualmachinesresponse");
						JSONObject json = JSONObject.fromObject(jsonStr);					
						if(!(json.containsKey("count") && json.getInt("count")>0)){
							returnArray.add(array.get(i));
						}
				}
			}
			listhostsresponse.put("count", returnArray.size());
			listhostsresponse.put("host", returnArray);
			JSONObj.put("listhostsresponse", listhostsresponse);
		}
		System.out.println("sss");
		this.renderText(JSONObj.toString());
		return NONE;
	}
		
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
	
	
}

package com.hp.xo.resourcepool.schedule.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.DimResourceDao;
import com.hp.xo.resourcepool.model.DimResource;
import com.hp.xo.resourcepool.model.ResourcePool;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.DimResourceTreeRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.schedule.service.DimResourceManager;
import com.hp.xo.resourcepool.schedule.service.ResourcePoolManager;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;
import com.hp.xo.resourcepool.service.impl.GenericManagerImpl;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;

@Service("dimResourceManager")
public class DimResourceManagerImpl extends GenericManagerImpl<DimResource, Long> implements DimResourceManager {
	
	@Autowired
	private DimResourceDao dimResourceDao;
	
	public DimResourceManagerImpl() {
		super();
	}
	
	
	public DimResourceManagerImpl(DimResourceDao dimResourceDao) {
		super(dimResourceDao);
		this.dimResourceDao = dimResourceDao;
	}
	
	public void setDimResourceDao(DimResourceDao dimResourceDao) {
		this.dimResourceDao = dimResourceDao;
		this.dao = dimResourceDao;
	}

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

//	@Autowired
	private ResourcePoolManager resourcePoolManager;
	
	private Response loginCloudStack() {
		//String secureUrl = ServiceOptionUtil.obtainCloudStackApiSecretUrl();
		Map<String,Object[]> param = new HashMap<String,Object[]>();
		param.put("command", new Object[]{"login"});
		param.put("username", new Object[]{ServiceOptionUtil.obtainCloudStackUsername()});
		param.put("password", new Object[]{ServiceOptionUtil.obtainCloudStackPassword()});
		param.put("response", new Object[]{"json"});
		Response loginResponse = genericCloudServerManager.post(param);
		return loginResponse;



		/*boolean isLogin = true;
		
		if(loginResponse!=null && StringUtils.isNotBlank(loginResponse.getResponseString()) ){
			JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
			try {
				jo.getString("loginresponse");
			} catch (JSONException e) {
				log.warn("login info is error, " + loginResponse.getResponseString());
				isLogin = false;
			}
		}
		
		return "";
*/
	}

	/*public static void main(String[] args){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		System.out.println( applicationContext ); 
		System.out.println(applicationContext.getBean("resourceSchedule"));
		DimResourceManager dimResourceManager  = (DimResourceManager) applicationContext.getBean("dimResourceManager");
		dimResourceManager.synchronizeData();
	}*/
	@Override
	public void synchronizeData() {

		// call the login interface and verify the username and password and
		// then get the userId.
		Response loginResponse = loginCloudStack();
		
		if(loginResponse!=null && StringUtils.isNotBlank(loginResponse.getResponseString()) ){
			JSONObject jo = JSONObject.fromObject(loginResponse.getResponseString());
			String userId = "";
			String sessionkey = "";
			try {
				jo = JSONObject.fromObject(jo.getString("loginresponse"));
				userId = jo.getString("userid");
				sessionkey = jo.getString("sessionkey");
			} catch (JSONException e) {
				log.error("login info is error, " + loginResponse.getResponseString());
				return ;
			}
			//String sessionKey = jo.getString("sessionkey");

			Map<String, Object[]> listUsersParams = new HashMap<String, Object[]>();
			listUsersParams.put("command", new Object[] { "listUsers" });
			listUsersParams.put("response", new Object[] { "json" });
			listUsersParams.put("id", new Object[] { userId });
			
			Response listUsersResponse = genericCloudServerManager.get(listUsersParams, false);
			
			String apikey = "";
			String secretkey = "";
			if (StringUtils.isNotBlank(listUsersResponse.getResponseString())) {
				jo = JSONObject.fromObject(listUsersResponse.getResponseString());
				jo = JSONObject.fromObject(jo.getString("listusersresponse"));
				JSONArray jos = jo.getJSONArray("user");
				jo = JSONObject.fromObject(jos.get(0));
				apikey = jo.getString("apikey");
				secretkey = jo.getString("secretkey");
			}
			Map<String,Object[]> cloudStackParams = new HashMap<String,Object[]>();
			cloudStackParams.put("apikey", new Object[]{apikey});
			cloudStackParams.put("secretkey", new Object[]{secretkey});
			cloudStackParams.put("sessionkey", new Object[]{sessionkey});
			cloudStackParams.put("response", new Object[]{"json"});
			//cloudStackParams.put("command", new Object[]{"listZones"});
			
			List<DimResource> result = new ArrayList<DimResource>();
			
			/*
				resourcetype.resoucepool	1	一级资源池
				resourcetype.zone	2	zone
				resourcetype.pod	3	pod
				resourcetype.cluster	4	集群
				resourcetype.host	5	主机
				resourcetype.primarystorage	6	主存储
				resourcetype.secondarystorage	7	辅助存储

			 */
			String resourcePoolType = "1";
			String zoneType = "2";
			String podType = "3";
			String clusterType = "4";
			String hostType = "5";
			//查询一级资源池
			List<ResourcePool> list = resourcePoolManager.getAll();
			
			//记录Hypervisor
			Map<String,String> hypervisorMap  = new HashMap<String,String>();
			
			
			
			cloudStackParams.put("command", new Object[]{"listClusters"});
			Response listClustersResponse = genericCloudServerManager.get(cloudStackParams);
			if(listClustersResponse!=null && StringUtils.isNotBlank(listClustersResponse.getResponseString())){
				JSONObject listClustersJson = JSONObject.fromObject(listClustersResponse.getResponseString());
				if(listClustersJson.getJSONObject("listclustersresponse").containsKey("count")){
					JSONArray clustersArray = listClustersJson.getJSONObject("listclustersresponse").getInt("count")>0?listClustersJson.getJSONObject("listclustersresponse").getJSONArray("cluster"):new JSONArray();
					for(int j = 0; j<clustersArray.size();++j){
						String clusterId = clustersArray.getJSONObject(j).getString("id");
						String podId = clustersArray.getJSONObject(j).getString("podid");
						DimResource clusterDimResource = initDimResource();
						clusterDimResource.setResourceId(clusterId);
						clusterDimResource.setPreResourceId(podId);
						clusterDimResource.setType(clusterType);
						clusterDimResource.setName(clustersArray.getJSONObject(j).getString("name"));
						clusterDimResource.setDescript(" Cluster ");
						clusterDimResource.setHypervisor(clustersArray.getJSONObject(j).getString("hypervisortype"));
						if(!hypervisorMap.containsKey(podId)){
							hypervisorMap.put(podId, clustersArray.getJSONObject(j).getString("hypervisortype"));
						}else{
							if(!hypervisorMap.get(podId).equals(clustersArray.getJSONObject(j).getString("hypervisortype"))){
								hypervisorMap.put(podId, hypervisorMap.get(podId)+","+clustersArray.getJSONObject(j).getString("hypervisortype"));
							}
						}
						result.add(clusterDimResource);
					}
				}
			}
				
			cloudStackParams.put("command", new Object[]{"listPods"});
			//cloudStackParams.put("zoneid", new Object[]{"zoneId"});
			Response listPodsResponse = genericCloudServerManager.get(cloudStackParams);
			if(listPodsResponse!=null && StringUtils.isNotBlank(listPodsResponse.getResponseString())){
				JSONObject listPodsJson = JSONObject.fromObject(listPodsResponse.getResponseString());
				if(listPodsJson.getJSONObject("listpodsresponse").containsKey("count")){
					JSONArray podsArray = listPodsJson.getJSONObject("listpodsresponse").getInt("count")>0?listPodsJson.getJSONObject("listpodsresponse").getJSONArray("pod"):new JSONArray();
					for(int j = 0; j<podsArray.size();++j){
						String podId = podsArray.getJSONObject(j).getString("id");
						String zoneId = podsArray.getJSONObject(j).getString("zoneid");
						DimResource podDimResource = initDimResource();
						podDimResource.setResourceId(podId);
						podDimResource.setPreResourceId(zoneId);
						podDimResource.setType(podType);
						podDimResource.setName(podsArray.getJSONObject(j).getString("name"));
						podDimResource.setDescript(" Pod ");
						podDimResource.setHypervisor(hypervisorMap.get(podId));
						if(!hypervisorMap.containsKey(zoneId)){
							hypervisorMap.put(zoneId, podDimResource.getHypervisor());
						}/*else{
							if(!hypervisorMap.get(zoneId).equals(clustersArray.getJSONObject(j).getString("hypervisortype"))){
								hypervisorMap.put(zoneId, hypervisorMap.get(podId)+","+clustersArray.getJSONObject(j).getString("hypervisortype"));
							}
						}*/
						result.add(podDimResource);
					}
				}
			}
			
			for(ResourcePool resourcePool : list){
				DimResource dimResource = initDimResource();
				dimResource.setResourceId(resourcePool.getResourcePoolId());
				dimResource.setPreResourceId("");
				dimResource.setType(resourcePoolType);
				dimResource.setName(resourcePool.getName());
				dimResource.setDescript(resourcePool.getDesc());
				result.add(dimResource);
				// List Zones from cloudStack API
				cloudStackParams.put("command", new Object[]{"listZones"});
				String subResources  = resourcePoolManager.listSubResource(cloudStackParams, resourcePool.getResourcePoolId());
				JSONObject jsonObj = JSONObject.fromObject(subResources);
				if(jsonObj.getJSONObject("listzonesresponse").containsKey("count")){
					JSONArray arrays = jsonObj.getJSONObject("listzonesresponse").getInt("count")>0?jsonObj.getJSONObject("listzonesresponse").getJSONArray("zone"):new JSONArray();
					for(int i=0;i<arrays.size();++i){
						String zoneId = arrays.getJSONObject(i).getString("id");
						DimResource zoneDimResource = initDimResource();
						zoneDimResource.setResourceId(zoneId);
						zoneDimResource.setPreResourceId(resourcePool.getResourcePoolId());
						zoneDimResource.setType(zoneType);
						zoneDimResource.setName(arrays.getJSONObject(i).getString("name"));
						zoneDimResource.setDescript(arrays.getJSONObject(i).containsKey("description")?arrays.getJSONObject(i).getString("description"):" Zone ");
						zoneDimResource.setHypervisor(hypervisorMap.get(zoneId));
						dimResource.setHypervisor(zoneDimResource.getHypervisor());
						result.add(zoneDimResource);
					}
				}
			}
			
			
			cloudStackParams.put("command", new Object[]{"listHosts"});
			cloudStackParams.put("type", new Object[]{"Routing"});
			Response listHostsResponse = genericCloudServerManager.get(cloudStackParams);
			if(listHostsResponse!=null && StringUtils.isNotBlank(listHostsResponse.getResponseString())){
				JSONObject listHostsJson = JSONObject.fromObject(listHostsResponse.getResponseString());
				if(listHostsJson.getJSONObject("listhostsresponse").containsKey("count")){
					JSONArray hostsArray = listHostsJson.getJSONObject("listhostsresponse").getInt("count")>0?listHostsJson.getJSONObject("listhostsresponse").getJSONArray("host"): new JSONArray();
					for(int j = 0; j<hostsArray.size();++j){
						String hostId = hostsArray.getJSONObject(j).getString("id");
						String clusterId = hostsArray.getJSONObject(j).getString("clusterid");
						DimResource hostDimResource = initDimResource();
						hostDimResource.setResourceId(hostId);
						hostDimResource.setPreResourceId(clusterId);
						hostDimResource.setType(hostType);
						hostDimResource.setName(hostsArray.getJSONObject(j).getString("name"));
						hostDimResource.setDescript(" Host ");
						hostDimResource.setHypervisor(hostsArray.getJSONObject(j).getString("hypervisor"));
						result.add(hostDimResource);
					}
				}
			}
			if(result.size()>0){
				dimResourceDao.deleteAll();
				for(int i=0;i<result.size(); ++i ){
					dimResourceDao.save(result.get(i));
				}
				DimResource example = new DimResource();
				example.setType(clusterType);
				List<DimResource> clusterList = (List<DimResource>) dimResourceDao.findByExample(example);
				/*for(int i=0;clusterList!=null&&i<clusterList.size();++i){
					DimResource dr = clusterList.get(i);
					dr.getHypervisor()
				}*/
			}
		}
	}
	
	
	private DimResource initDimResource(){
		DimResource dimResource = new DimResource();
		dimResource.setCreatedBy("admin");
		dimResource.setCreatedOn(new Date());
		dimResource.setModifiedBy("admin");
		dimResource.setModifiedOn(new Date());
		return dimResource;
	}

	private DimResource generateFromCloudStack(Map<String,Object[]> cloudStackParam){
		DimResource dimResource = new DimResource();
		Response response =  genericCloudServerManager.get(cloudStackParam,true);
		if (response!=null && StringUtils.isNotBlank(response.getResponseString())) {
			JSONObject jsonObj = JSONObject.fromObject(response.getResponseString());
			JSONArray jsoArray = jsonObj.getJSONArray("virtualmachine");
			
		}
		return dimResource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResponse<DimResource> getDimResourceTree(DimResourceTreeRequest drtr) {
		ListResponse<DimResource> response = new ListResponse<DimResource>();
		DimResource exampleEntity = new DimResource();
		exampleEntity.setResourceId(drtr.getResourceId());
		exampleEntity.setType(drtr.getType());
		exampleEntity.setPreResourceId(drtr.getPreResourceId());
		response.setResponses((List<DimResource>) dimResourceDao.findByExample(exampleEntity));
		return response;
	}
}

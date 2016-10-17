package com.hp.xo.resourcepool.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;
import com.hp.xo.resourcepool.utils.StringUtil;

/*import com.cmsz.cloudplatform.model.ResourcePool;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;
import com.cmsz.cloudplatform.service.ResourcePoolManager;*/
@Service("deployVMAttributeValueManager")
public class DeployVMAttributeValueManagerImpl extends
		GenericCloudServerManagerImpl implements ProvisionAttributeValueManager {

	/**一级资源池*/
	private String EXTERNAL_RESOURCEPOOLID = "resourcePoolId";
	/**计算方案*/
	private String EXTERNAL_SERVICEOFFERINGID = "serviceofferingid";
	/**模板*/
	private String EXTERNAL_TEMPLATEID = "templateid";
	/**区域*/
	private String EXTERNAL_ZONEID = "zoneid";
	/**数据磁盘方案*/
	private String EXTERNAL_DISKOFFERINGID = "diskofferingid";
	/**虚拟机管理程序*/
	private String EXTERNAL_HYPERVISOR = "hypervisor";
	/**网络*/
	private String EXTERNAL_NEWWORKIDS = "networkids";
	
	/**项目*/
	private String EXTERNAL_PROJECTID = "projectid";
	
	//add@by @ma 添加安全组和关联性组 
	/**安全组*/
	private String EXTERNAL_SECURITYGROUPIDS = "securitygroupids";
	
	/**关联性组*/
	private String EXTERNAL_AFFINITYGROUPIDS = "affinitygroupids";
	
/*	@Autowired
	private ResourcePoolManager resourcePoolManager;*/
	
	
	@Override
	public Map<String, Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam) {

		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		Map<String,Object[]> params = new HashMap<String,Object[]>();
		params.put("sessionkey", new Object[]{sessionkey});
		params.putAll(valueParam);
		
//		if(EXTERNAL_RESOURCEPOOLID.equalsIgnoreCase(attributeName)){
//			List<ResourcePool> pools = resourcePoolManager.getAll();
//			for(int i=0 ; i < pools.size() ; ++ i){
//				ResourcePool p = pools.get(i);
//				resultMap.put(p.getResourcePoolId(),p.getName());
//			}
//			resultMap.put("all", "所有资源池");
//		}
		 if(EXTERNAL_SERVICEOFFERINGID.equalsIgnoreCase(attributeName)){
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
		}else if(EXTERNAL_TEMPLATEID.equals(attributeName)){//listTemplates
			params.put("command", new Object[]{"listTemplates"});
			//params.put("listall", new Object[]{true});
			//featured : templates that have been marked as featured and public.
			params.put("templatefilter", new Object[]{"featured"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listtemplatesresponse")){
					json = json.getJSONObject("listtemplatesresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("template");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
			
			//community : templates that have been marked as public but not featured.
			params.put("templatefilter", new Object[]{"community"});
			response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listtemplatesresponse")){
					json = json.getJSONObject("listtemplatesresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("template");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
			
			//sharedexecutable : templates ready to be deployed that have been granted to the calling user by another user.
			params.put("templatefilter", new Object[]{"selfexecutable"});
			response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listtemplatesresponse")){
					json = json.getJSONObject("listtemplatesresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("template");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
		}else if(EXTERNAL_ZONEID.equals(attributeName)){
			String resourcePoolId = "";
			if(params.containsKey("resourcePoolId")){
				if(params.get("resourcePoolId")!=null && params.get("resourcePoolId").length>0){
					resourcePoolId = (String)params.get("resourcePoolId")[0];
				}
			}
			params.put("command", new Object[]{"listZones"});
			String responseString = "";
			if(StringUtils.isBlank(resourcePoolId)){
				Response response = this.get(params);
				if(response!=null && response.getStatusCode()==HttpStatus.SC_OK){
					responseString = response.getResponseString();
				}
			}
//			else{
//				responseString = resourcePoolManager.listSubResource(params, resourcePoolId);
//			}
			
			
			if(responseString!=null && StringUtils.isNotBlank(responseString)){
				JSONObject json = JSONObject.fromObject(responseString);
				if(json.containsKey("listzonesresponse")){
					json = json.getJSONObject("listzonesresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("zone");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
		}else if(EXTERNAL_DISKOFFERINGID.equals(attributeName)){//磁盘方案
			/*params.put("command", new Object[]{"listDiskOfferings"});
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listdiskofferingsresponse")){
					json = json.getJSONObject("listdiskofferingsresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("diskoffering");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}*/
			
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
					if(params.containsKey("id")){
						Object[] idValue = params.get("id");
						if(StringUtil.isNullString(idValue[0].toString())){//如果为空说明选择的是无磁盘方案，直接返回无
							JSONObject tempJson = new JSONObject();
							tempJson.put("displaytext", "无");
							tempJson.put("iscustomized", false);
							Map<String,Object> resultMapidnull = new HashMap<String,Object>();
							resultMapidnull.put("", tempJson);
							return resultMapidnull;
						}
					}else{
						JSONObject tempJson = new JSONObject();
						tempJson.put("displaytext", "无");
						tempJson.put("iscustomized", false);
						resultMap.put("", tempJson);
					}
				}
			}
			
		}else if(EXTERNAL_NEWWORKIDS.equals(attributeName)){//listNetworks
			/*
			 * account	admin
canusefordeploy	true
command	listNetworks
domainid	67f97989-7d3b-11e3-bf84-005056941242
response	json
sessionkey	rvGx0Gg9EEIA956R45JtLAlKvb8=
zoneId	bcdf98b8-33ba-48a9-b785-6be289c5ff10
			 * */
			params.put("command", new Object[]{"listNetworks"});
			params.put("canusefordeploy", new Object[]{"true"});
			/*
			zoneId=bcdf98b8-33ba-48a9-b785-6be289c5ff10&


					canusefordeploy=true&

					domainid=67f97989-7d3b-11e3-bf84-005056941242&

					account*/
			Response response = this.get(params);
			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listnetworksresponse")){
					json = json.getJSONObject("listnetworksresponse");
					if(json.containsKey("count")&& json.getInt("count")>0){
						JSONArray jsonArray = json.getJSONArray("network");
						for(int i=0;i<jsonArray.size();++i){
							json = (JSONObject)jsonArray.get(i);
							resultMap.put(json.getString("id"),json.getString("name"));
						}
					}
				}
			}
		}else if(EXTERNAL_PROJECTID.equalsIgnoreCase(attributeName)){//projectid
			params.put("command", new Object[]{"listProjects"});
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
							resultMap.put(json.getString("id"),json.getString("displaytext"));
						}
					}
				}
			}
		}else if(EXTERNAL_SECURITYGROUPIDS.equalsIgnoreCase(attributeName)){//添加by@ma安全组 securitygroupids
//			params.put("command", new Object[]{"listSecurityGroups"});
//			Response response = this.get(params);
//			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
//				JSONObject json = JSONObject.fromObject(response.getResponseString());
//				System.out.println("安全组---------------------------------"+attributeName);
//				if(json.containsKey("listsecuritygroupsresponse")){
//					json = json.getJSONObject("listsecuritygroupsresponse");
//					if(json.containsKey("count")&& json.getInt("count")>0){
//						JSONArray jsonArray = json.getJSONArray("securitygroup");
//						for(int i=0;i<jsonArray.size();++i){
//							json = (JSONObject)jsonArray.get(i);
//							resultMap.put(json.getString("id"),json.getString("name"));
//						}
//					}
//				}
//			}
			Response response = new Response();
			params.put("command", new Object[]{"listSecurityGroups"});
			if(params.containsKey("id")){	
					String securitygroupName="";
					String ids = (String)params.get("id")[0];
					if(StringUtils.isNotBlank(ids)){
						String[] listSecurityGroupIds = ((String)params.get("id")[0]).split(",");
						for(int i = 0; i < listSecurityGroupIds.length; i++ ){
							params.put("id",new Object[]{listSecurityGroupIds[i]});
							Response res = this.get(params);
							if(res!=null && StringUtils.isNotBlank(res.getResponseString())){
								JSONObject json = JSONObject.fromObject(res.getResponseString());
								if(json.containsKey("listsecuritygroupsresponse")){
									json = json.getJSONObject("listsecuritygroupsresponse");
									if(json.containsKey("count")&& json.getInt("count")>0){
										JSONArray jsonArray = json.getJSONArray("securitygroup");
										json = (JSONObject)jsonArray.get(0);
										securitygroupName = securitygroupName +  "  " +json.getString("name");									
									}									
								}
							}
						}
						resultMap.put(ids,securitygroupName);
					}					
			}else{
				 response = this.get(params);
				if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
					JSONObject json = JSONObject.fromObject(response.getResponseString());
					System.out.println("安全组---------------------------------"+attributeName);
					if(json.containsKey("listsecuritygroupsresponse")){
						json = json.getJSONObject("listsecuritygroupsresponse");
						if(json.containsKey("count")&& json.getInt("count")>0){
							JSONArray jsonArray = json.getJSONArray("securitygroup");
							for(int i=0;i<jsonArray.size();++i){
								json = (JSONObject)jsonArray.get(i);
								resultMap.put(json.getString("id"),json.getString("name"));
							}
						}
					}
				}
				
			}
//			params.put("command", new Object[]{"listSecurityGroups"});
//			response = this.get(params);
//			if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
//				JSONObject json = JSONObject.fromObject(response.getResponseString());
//				System.out.println("安全组---------------------------------"+attributeName);
//				if(json.containsKey("listsecuritygroupsresponse")){
//					json = json.getJSONObject("listsecuritygroupsresponse");
//					if(json.containsKey("count")&& json.getInt("count")>0){
//						JSONArray jsonArray = json.getJSONArray("securitygroup");
//						for(int i=0;i<jsonArray.size();++i){
//							json = (JSONObject)jsonArray.get(i);
//							resultMap.put(json.getString("id"),json.getString("name"));
//						}
//					}
//				}
//			}
		}else if(EXTERNAL_AFFINITYGROUPIDS.equalsIgnoreCase(attributeName)){
			params.put("command", new Object[]{"listAffinityGroups"});
			if(params.containsKey("id")){
				String ids = (String)params.get("id")[0];
				if(StringUtils.isNotBlank(ids)){
					String affinitygroupName="";
					String[] listSecurityGroupIds = ((String)params.get("id")[0]).split(",");
					for(int i = 0; i < listSecurityGroupIds.length; i++ ){
						params.put("id",new Object[]{listSecurityGroupIds[i]});
						Response res = this.get(params);
						if(res!=null && StringUtils.isNotBlank(res.getResponseString())){
							JSONObject json = JSONObject.fromObject(res.getResponseString());
							if(json.containsKey("listaffinitygroupsresponse")){
								json = json.getJSONObject("listaffinitygroupsresponse");
								if(json.containsKey("count")&& json.getInt("count")>0){
									JSONArray jsonArray = json.getJSONArray("affinitygroup");
									json = (JSONObject)jsonArray.get(0);
									affinitygroupName = affinitygroupName +  "  " +json.getString("name");									
								}
								
								
							}
						}
					}
					resultMap.put(ids,affinitygroupName);
				}										
			}else{
				Response response = this.get(params);
				if(response!=null && StringUtils.isNotBlank(response.getResponseString())){
					JSONObject json = JSONObject.fromObject(response.getResponseString());
					System.out.println("关联性组---------------------------------"+attributeName);				
					if(json.containsKey("listaffinitygroupsresponse")){
						json = json.getJSONObject("listaffinitygroupsresponse");
						if(json.containsKey("count")&& json.getInt("count")>0){
							JSONArray jsonArray = json.getJSONArray("affinitygroup");
							for(int i=0;i<jsonArray.size();++i){
								json = (JSONObject)jsonArray.get(i);
								resultMap.put(json.getString("id"),json.getString("name"));
							}						
						}					
					}
				}
			}
		}		 
		return resultMap;
	}

}

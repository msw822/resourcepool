package com.hp.xo.resourcepool.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.DimResourceDao;
import com.hp.xo.resourcepool.model.DimResource;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.ListTopDataRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.TopDataManager;
import com.hp.xo.resourcepool.utils.StringUtil;
import com.hp.xo.resourcepool.vo.TopDataVO;

/*import com.cmsz.cloudplatform.dao.DimResourceDao;
import com.cmsz.cloudplatform.model.DimResource;
import com.cmsz.cloudplatform.model.request.ListTopDataRequest;
import com.cmsz.cloudplatform.model.response.ListResponse;
import com.cmsz.cloudplatform.model.response.Response;
import com.cmsz.cloudplatform.model.vo.TopDataVO;
import com.cmsz.cloudplatform.service.TopDataManager;
import com.cmsz.cloudplatform.utils.StringUtils;
import com.hp.core.service.impl.GenericManagerImpl;*/

@Service("topDataManager")
public class TopDataManagerImpl extends GenericManagerImpl<DimResource, Long> implements TopDataManager {

	@Autowired
	private GenericCloudServerManagerImpl genericCloudServerManager;

	private DimResourceDao dimResourceDao;

	public TopDataManagerImpl(DimResourceDao dimResourceDao) {
		super(dimResourceDao);
		this.dimResourceDao = dimResourceDao;
	}

	@Autowired
	public void setDimResourceDao(DimResourceDao dimResourceDao) {
		this.dimResourceDao = dimResourceDao;
		this.dao = dimResourceDao;
	}

	public TopDataManagerImpl() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResponse<TopDataVO> getTopData(ListTopDataRequest request, Map<String, Object[]> requestParams) {
		ListResponse<TopDataVO> response = new ListResponse<TopDataVO>();
		if (request == null) {
			return response;
		}
		response.setResponses(new ArrayList<TopDataVO>());

		// List<String> resoucePools = request.getResoucePools();// 一级池
		List<String> zones = request.getZones();// 二级池
		List<String> pods = request.getPods();// pod
		List<String> clusters = request.getClusters();// cluster
		List<String> hosts = request.getHosts();// 主机
		String target = request.getTarget();

		List<Response> responseList = new ArrayList<Response>();
//		if (!CollectionUtils.isEmpty(hosts)) {
//			//return response;
//		} else 
		if (hosts != null && hosts.size() == 1) {
			requestParams.put(HOST_ID, new Object[] { hosts.get(0) });
			responseList.add(genericCloudServerManager.get(requestParams));
		} else if (pods != null && pods.size() == 1) {
			requestParams.put(POD_ID, new Object[] { pods.get(0) });
			responseList.add(genericCloudServerManager.get(requestParams));
		} else if (zones != null && zones.size() == 1) {
			requestParams.put(ZONE_ID, new Object[] { zones.get(0) });
			responseList.add(genericCloudServerManager.get(requestParams));
		} else {
			responseList.add(genericCloudServerManager.get(requestParams));
		}

//		DimResource exampleEntity = new DimResource();
//		List<DimResource> dimResourceList = (List<DimResource>) dimResourceDao.findByExample(exampleEntity);
		List<TopDataVO> topDataVOList = new ArrayList<TopDataVO>();

		for (Response resp : responseList) {
			JSONObject json = JSONObject.fromObject(resp.getResponseString());
			if (json != null) {
				JSONObject jsonObj = (JSONObject) json.get(LIST_VIRTUAL_MACHINES_RESPONSE);

				if (jsonObj != null) {
					List<JSONObject> jsonObjList = (List<JSONObject>) jsonObj.get(VIRTUAL_MACHINE);

					if (CollectionUtils.isNotEmpty(jsonObjList)) {
						for (JSONObject jsobj : jsonObjList) {
							// 判断主机是否存在查询列表中
//							if (!hosts.contains(StringUtil.getJsonString(jsobj, HOST_ID))) {
//								continue;
//							}

							TopDataVO topDataVO = new TopDataVO();
							topDataVO.setTarget(target);
							topDataVO.setName(StringUtil.getJsonString(jsobj, NAME));
							topDataVO.setDisplayName(StringUtil.getJsonString(jsobj, DISPLAY_NAME));

							// ip地址(nic拼接)
							List<JSONObject> jsonNicList = (List<JSONObject>) jsobj.get(NIC);
							if (CollectionUtils.isNotEmpty(jsonNicList)) {
								List<String> nicList = new ArrayList<String>();
								for (JSONObject jsonNic : jsonNicList) {
									if (StringUtil.getJsonString(jsonNic, IP_ADDRESS) != null) {
										nicList.add(StringUtil.getJsonString(jsonNic, IP_ADDRESS));
									}
								}
								if (nicList != null) {
									if (nicList.size() == 1) {
										topDataVO.setIpAddress(nicList.get(0));
									} else {
										topDataVO.setIpAddress(nicList.toString());
									}
								}
							}

							// type = "5"
							topDataVO.setHost(StringUtil.getJsonString(jsobj, HOST_NAME));

							// type = "4"
							// topDataVO.setCluster(cluster);
							//initTopData(topDataVO, "4", dimResourceList, jsobj);

							// type = "3"
							// topDataVO.setPod(pod);
						//	initTopData(topDataVO, "3", dimResourceList, jsobj);

							// type = "2"
							topDataVO.setZone(StringUtil.getJsonString(jsobj, ZONE_NAME));

							// type = "1"
							// topDataVO.setResoucePool(resoucePool);
						//	initTopData(topDataVO, "1", dimResourceList, jsobj);

							// KVM XenServer
							String hypervisor = StringUtil.getJsonString(jsobj, HYPERVISOR);
							topDataVO.setHypervisor(hypervisor);

							if (CPU_NUMBER.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, CPU_NUMBER));
								topDataVO.setCpuSpeed(StringUtil.getJsonString(jsobj, CPU_SPEED));
							} else if (CPU_USED.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, CPU_USED));
							} else if (NET_WORK_KBS_READ.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, NET_WORK_KBS_READ));
							} else if (NET_WORK_KBS_WRITE.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, NET_WORK_KBS_WRITE));
							} else if (DISK_KBS_READ.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, DISK_KBS_READ));
							} else if (DISK_KBS_WRITE.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, DISK_KBS_WRITE));
							} else if (DISK_IO_READ.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, DISK_IO_READ));
							} else if (DISK_IO_WRITE.equals(target)) {
								topDataVO.setValue(StringUtil.getJsonString(jsobj, DISK_IO_WRITE));
							}

							topDataVOList.add(topDataVO);
						}
					}
				}
			}
		}

		Collections.sort(topDataVOList);
		if (topDataVOList != null && topDataVOList.size() > 600) {
			response.setResponses(topDataVOList.subList(0, 600));
		} else {
			response.setResponses(topDataVOList);
		}

		return response;
	}

	private void initTopData(TopDataVO topDataVO, String preType, List<DimResource> dimResourceList, JSONObject jsobj) {
		if (dimResourceList == null) {
			return;
		}
		
		String resourceId = null;
		String type = null;
		if ("4".equals(preType)) {
			type = "5";
			resourceId = StringUtil.getJsonString(jsobj, HOST_ID);
		} else if ("3".equals(preType)) {
			type = "4";
			resourceId = StringUtil.getJsonString(jsobj, CLUSTER_ID);
		} else if ("1".equals(preType)) {
			type = "2";
			resourceId = StringUtil.getJsonString(jsobj, ZONE_ID);
		}

		for (DimResource dimResource : dimResourceList) {
			if (dimResource != null && type.equals(dimResource.getType()) && resourceId != null && resourceId.equals(dimResource.getResourceId())) {

				String preResourceId = dimResource.getPreResourceId();
				for (DimResource preDimResource : dimResourceList) {
					if (preDimResource != null && preType.equals(preDimResource.getType()) && preResourceId != null
							&& preResourceId.equals(preDimResource.getResourceId())) {
						if ("4".equals(preType)) {
							topDataVO.setCluster(preDimResource.getName());
							jsobj.put(CLUSTER_ID, preDimResource.getResourceId());
						} else if ("3".equals(preType)) {
							topDataVO.setPod(preDimResource.getName());
						} 
//						else if ("1".equals(preType)) {
//							topDataVO.setResoucePool(preDimResource.getName());
//						}
						return;
					}
				}
			}
		}
	}

	protected static final String ZONE_ID = "zoneid";
	protected static final String POD_ID = "podid";
	protected static final String CLUSTER_ID = "clusterid";
	protected static final String HOST_ID = "hostid";

	protected static final String LIST_VIRTUAL_MACHINES_RESPONSE = "listvirtualmachinesresponse";
	protected static final String VIRTUAL_MACHINE = "virtualmachine";

	protected static final String ID = "id";
	protected static final String NAME = "name";
	protected static final String DISPLAY_NAME = "displayname";
	protected static final String ZONE_NAME = "zonename";
	protected static final String HOST_NAME = "hostname";

	protected static final String NIC = "nic";
	protected static final String IP_ADDRESS = "ipaddress";

	protected static final String HYPERVISOR = "hypervisor";

	protected static final String CPU_NUMBER = "cpunumber";
	protected static final String CPU_USED = "cpuused";
	protected static final String NET_WORK_KBS_READ = "networkkbsread";
	protected static final String NET_WORK_KBS_WRITE = "networkkbswrite";
	protected static final String DISK_KBS_READ = "diskkbsread";
	protected static final String DISK_KBS_WRITE = "diskkbswrite";
	protected static final String DISK_IO_READ = "diskioread";
	protected static final String DISK_IO_WRITE = "diskiowrite";
	protected static final String CPU_SPEED = "cpuspeed";

	protected static final String STATE = "state";
	protected static final String STOPPED = "Stopped";

	protected static final String KVM = "KVM";
	protected static final String XEN_SERVER = "XenServer";

}

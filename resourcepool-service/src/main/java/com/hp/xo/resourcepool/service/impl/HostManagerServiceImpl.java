package com.hp.xo.resourcepool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.PhysicsInstanceDao;
import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.enumtype.PhysicsHostStatus;
import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.exception.ServiceException;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.model.PhysicsInstances;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.BuessionListRequest;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.rmi.AbstractPhysicsHostBuilder;
import com.hp.xo.resourcepool.rmi.RmiPhysicsHostBuilder;
import com.hp.xo.resourcepool.service.HostManagerService;
import com.hp.xo.utils.common.CollectionUtil;

@Service(value="hostManagerService")
//@Transactional(propagation=Propagation.REQUIRED)
public class HostManagerServiceImpl extends GenericManagerImpl<Log,Long>implements HostManagerService{
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	@Autowired
	private  PhysicsInstanceDao physicsInstanceDao;
	@Autowired
	private  GenericCloudServerManagerImpl genericCloudServerManager;
	private Map<String,PhysicsInstances> getPhysicsInstancesFormat(List<PhysicsInstances> physicsInstances){
		try{	
			final Map physicsInstanceMap=new ConcurrentHashMap();
			List<String> physicsInstanceList = (List<String>) CollectionUtils.collect(physicsInstances, new Transformer() {
	                public Object transform(Object arg0) {
	                	PhysicsInstances instance= (PhysicsInstances) arg0;
	                	if(StringUtils.isNoneBlank(instance.getPhysicsName())){
	                		physicsInstanceMap.put(instance.getPhysicsName(), instance);
		                    return instance.getPhysicsName();
	                	}else{
	                		return null;
	                	}
	                	
	                }
	            });
		     return physicsInstanceMap;
		}catch(Exception er){
			log.error("获取物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
	}
	/**
	 * 所有物理机列表
	 * @return
	 * @throws BuessionException
	 */
	private List<PhysicsHost> getCMDBPhysicsHost()throws BuessionException{
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();				
		List<PhysicsHost> listPhysicsHost=builder.buildHostDataSource();			
		listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
            public boolean evaluate(Object arg0) {
            	PhysicsHost physicsHost = (PhysicsHost) arg0;
            	if(StringUtils.isNoneBlank(physicsHost.getHostName())){
            		return true;
            	}else{
            		return false;
            	}
            	
            }
        });		
		return listPhysicsHost;
	}
	/**
	 * 所有物理机列表
	 * @return
	 * @throws BuessionException
	 */
	private List<PhysicsHost> getCMDBPhysicsHostWithOrderInfo(PhysicsHostRequest request)throws BuessionException{
		List<PhysicsHost> listPhysicsHost=getCMDBPhysicsHost();
		List<PhysicsInstances> physicsInstances=physicsInstanceDao.getPhysicsInstances(request);	
		final Map<String,PhysicsInstances> physicsInstanceMap=getPhysicsInstancesFormat(physicsInstances);
		listPhysicsHost=(List<PhysicsHost>)CollectionUtils.collect(listPhysicsHost, new Transformer() {
            public Object transform(Object arg0) {
            	PhysicsHost host= (PhysicsHost) arg0;
            	if("1".equals(host.getIsVirtual())) {
            		host.setHostTypeStatus(PhysicsHostStatus.VIRTUALED.toString());
            	}else{
            		if(physicsInstanceMap.containsKey(host.getHostName())){
                		PhysicsInstances instance=physicsInstanceMap.get(host.getHostName());
                		host.setHostTypeStatus(PhysicsHostStatus.ASSIGNMENTESD.toString());
                		host.setOwner(instance.getOwner());
                		host.setOwnerName(instance.getOwnerName());
                	}else{
                		host.setHostTypeStatus(PhysicsHostStatus.UNASSIGNMENTE.toString());
                	}
            	}
            	return host;
            }
        });
		return listPhysicsHost;
	}
	/**
	 * 根据订单号获得物理机器列表
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<PhysicsHost> getPhysicsHostWithOrderid(String orderId)throws ServiceException{
		try{			
			PhysicsHostRequest request=new PhysicsHostRequest();
			request.setOrderId(orderId);
			List<PhysicsInstances> physicsInstances=physicsInstanceDao.getPhysicsInstances(request);
			final Map<String,PhysicsInstances> physicsInstanceMap=getPhysicsInstancesFormat(physicsInstances);			
			List<PhysicsHost> listPhysicsHost=getCMDBPhysicsHost();
			listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
                public boolean evaluate(Object arg0) {
                	PhysicsHost physicsHost = (PhysicsHost) arg0;
                	return physicsInstanceMap.containsKey(physicsHost.getHostName()); 
                }
            });	
			return listPhysicsHost;
		}catch(Exception er){			
			throw new ServiceException(er.getMessage());
		}catch(BuessionException er){			
			throw new ServiceException(er.getMessage());
		}
	}
	/**
	 * 作废分配的物理机
	 * @param orderId
	 * @return
	 * @throws ServiceException
	 */
	public boolean saveEnabledPhysicsInstanceByOrderid(String orderId)throws ServiceException{
		try{	
			PhysicsHostRequest request=new PhysicsHostRequest();
			request.setOrderId(orderId);
			List<PhysicsInstances> physicsInstances=physicsInstanceDao.getPhysicsInstances(request);
			if(physicsInstances.size()<0){
				throw new ServiceException("没有找到订单号："+orderId+" 分配的物理机");
			}
			for(PhysicsInstances instance:physicsInstances){
				instance.setIsValid("0");
				physicsInstanceDao.save(instance);
			}
			return true;
			
		}catch(Exception er){			
			throw new ServiceException(er.getMessage());
		}
	}
	
	/**
	 * 获得包含关联信息的物理机器列表
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<PhysicsHost> getPhysicsHostWithAssociate(PhysicsHostRequest request)throws ServiceException{
		try{			
							
			List<PhysicsHost> listPhysicsHost=getCMDBPhysicsHostWithOrderInfo(request);			
			listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
                public boolean evaluate(Object arg0) {
                	PhysicsHost physicsHost = (PhysicsHost) arg0;               
                	return PhysicsHostStatus.ASSIGNMENTESD.toString().equals(physicsHost.getHostTypeStatus());
                }
            });	
			return listPhysicsHost;
		}catch(Exception er){			
			throw new ServiceException(er.getMessage());
		}catch(BuessionException er){			
			throw new ServiceException(er.getMessage());
		}
	}	
	/**
	 * 获得未分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<PhysicsHost> getPhysicsHostUnAssignmented()throws ServiceException{
		try{
			List<PhysicsHost> listPhysicsHost=getCMDBPhysicsHostWithOrderInfo(new PhysicsHostRequest());			
			listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
                public boolean evaluate(Object arg0) {
                	PhysicsHost physicsHost = (PhysicsHost) arg0;               
                	return PhysicsHostStatus.UNASSIGNMENTE.toString().equals(physicsHost.getHostTypeStatus());
                }
            });	
			return listPhysicsHost;
		}catch(Exception er){			
			throw new ServiceException(er.getMessage());
		}catch(BuessionException er){			
			throw new ServiceException(er.getMessage());
		}
	}
	@Transactional(readOnly = true)
	public JSONObject listPhysicsHostStatus(PhysicsHostRequest request)throws ServiceException{
		if("project".equalsIgnoreCase(request.getQueryType())){
			Map<String,Object[]> params = new HashMap<String,Object[]>();
			params.put("command", new Object[]{"listProjectAccounts"});
			params.put("apikey",new Object[]{request.getApikey()});
			params.put("secretkey",new Object[]{request.getSecretkey()});
			params.put("projectid",new Object[]{request.getProjectId()});
//			params.put("domainid",new Object[]{request.getDomainid()});
			params.put("response", new Object[]{"json"});
			Response response = genericCloudServerManager.get(params);
			if(log.isInfoEnabled()){
				log.info("listProjectAccounts HttpStatus.OK.value()="+HttpStatus.SC_OK);
			}
			if(response!=null && response.getStatusCode()==HttpStatus.SC_OK &&  StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listprojectaccountsresponse")){
					json = json.getJSONObject("listprojectaccountsresponse");
					if(json.containsKey("count") && json.getInt("count")>0 && json.containsKey("projectaccount")){
						JSONArray jsonArray = json.getJSONArray("projectaccount");
						String[] userIds = new String[jsonArray.size()];
						for(int i= 0; i<jsonArray.size();++i){
							JSONObject jsonAccount=jsonArray.getJSONObject(i);
							JSONArray userArray=jsonAccount.getJSONArray("user");
							for(int index=0;index<userArray.size();index++){
								userIds[i]=userArray.getJSONObject(index).getString("id");
							}

						}
						request.setUserIds(userIds);
					}
				}
			}
		}		
		int running=0;
		int stopped=0;
		int total=0;
		List<PhysicsHost> userdPhysicsHost=this.getPhysicsHostWithAssociate(request);		
		for(PhysicsHost host:userdPhysicsHost){
			total++;
			if("on".equalsIgnoreCase(host.getCpuStatus())){
				running++;
			}else{
				stopped++;
			}			
		}
		JSONObject capacity =new JSONObject();
		capacity.put("runningHosts",running);
		capacity.put("stoppedHosts", stopped);
		capacity.put("totalHosts", running+stopped);
		JSONObject jsonMap =new JSONObject();
		jsonMap.put("response","json");
		jsonMap.put("count", total);
		jsonMap.put("capacity", capacity.toString());
		JSONObject jsonStr = new JSONObject();
		jsonStr.put("listcapacityresponse", jsonMap.toString());	
		return jsonStr;
	}
	
	@Transactional(readOnly = true)
	public JSONObject getPhysicsHostsCapacity(PhysicsHostRequest request)throws ServiceException{
		JSONArray capacity=new JSONArray();
		try{			
			List<PhysicsHost> listPhysicsHost=getCMDBPhysicsHost();
			List<PhysicsHost> userdPhysicsHost=this.getPhysicsHostWithAssociate(new PhysicsHostRequest());
			long totalCpuNumber=0;
			long useredCpuNumber=0;
			long totalMemorySize=0;
			long useredMemorySize=0;
			long totalDiskSize=0;
			long useredDiskSize=0;
			for(PhysicsHost host:listPhysicsHost){
				if(host.getCpuNumber()!=null){
					totalCpuNumber=totalCpuNumber+host.getCpuNumber();					
				}
				if(host.getPhysicsMemory()!=null){
					totalMemorySize=totalMemorySize+host.getPhysicsMemory();				
				}
				if(host.getfDiskTotal()!=null){
					totalDiskSize=totalDiskSize+host.getfDiskTotal();	
				}
				
			}
			for(PhysicsHost host:userdPhysicsHost){
				if(host.getCpuNumber()!=null){
					useredCpuNumber=totalCpuNumber+host.getCpuNumber();		
				}
				if(host.getPhysicsMemory()!=null){
					useredMemorySize=totalMemorySize+host.getPhysicsMemory();			
				}
				if(host.getfDiskTotal()!=null){
					useredDiskSize=totalDiskSize+host.getfDiskTotal();	
				}
			}
//			Map<String,JSONObject> result = new HashMap<String,JSONObject>();
			JSONObject jsonPage =new JSONObject();			
			jsonPage.put("type","1");
			jsonPage.put("zoneId","zoneId");
			jsonPage.put("zoneName","物理机容量");
			jsonPage.put("describe","CPU");
			jsonPage.element("capacityused",new Long(useredCpuNumber));
			jsonPage.element("capacitytotal",new Long(totalCpuNumber));
			jsonPage.element("percentused",""+Math.round((jsonPage.optLong("capacityused")*1.0D*10000/jsonPage.optLong("capacitytotal")))*0.01);
			capacity.add(jsonPage);
			
			jsonPage =new JSONObject();
			jsonPage.put("type","0");
			jsonPage.put("describe","内存容量");
			jsonPage.put("zoneId","zoneId");
			jsonPage.put("zoneName","物理机容量");
			jsonPage.element("capacityused",new Long(useredMemorySize));
			jsonPage.element("capacitytotal",new Long(totalMemorySize));
			jsonPage.element("percentused",""+Math.round((jsonPage.optLong("capacityused")*1.0D*10000/jsonPage.optLong("capacitytotal")))*0.01);

			capacity.add(jsonPage);
			
			jsonPage =new JSONObject();
			jsonPage.put("type","2");
			jsonPage.put("describe","硬盘存储");
			jsonPage.put("zoneId","zoneId");
			jsonPage.put("zoneName","物理机容量");
			jsonPage.element("capacityused",new Long(useredDiskSize));
			jsonPage.element("capacitytotal",new Long(totalDiskSize));
			jsonPage.element("percentused",""+Math.round((jsonPage.optLong("capacityused")*1.0D*10000/jsonPage.optLong("capacitytotal")))*0.01);
			capacity.add(jsonPage);
			
			JSONObject jsonMap =new JSONObject();
			jsonMap.put("count",capacity.size());
			jsonMap.put("capacity", capacity.toString());
			JSONObject jsonStr = new JSONObject();
			jsonStr.put("listcapacityresponse", jsonMap.toString());		
			return jsonStr;
		}catch(BuessionException er){
			throw new ServiceException(er.getMessage());
		}
		
	}
	
	
	/**
	 * 获得物理机器列表
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public ListResponse<PhysicsHost> getPhysicsHost(PhysicsHostRequest request)throws ServiceException{
		//admin 1
    	//domainadmin 2
    	//user 0
		if(request.getRole().intValue()==ActiveUser.DOMAINADMIN){
			Map<String,Object[]> params = new HashMap<String,Object[]>();
			params.put("command", new Object[]{"listUsers"});
			params.put("apikey",new Object[]{request.getApikey()});
			params.put("secretkey",new Object[]{request.getSecretkey()});
//			params.put("account",new Object[]{request.getAccount()});
			params.put("domainid",new Object[]{request.getDomainid()});
			params.put("response", new Object[]{"json"});
			Response response = genericCloudServerManager.get(params);
			if(log.isInfoEnabled()){
				log.info("listUsers HttpStatus.OK.value()="+HttpStatus.SC_OK);
			}
			if(response!=null && response.getStatusCode()==HttpStatus.SC_OK &&  StringUtils.isNotBlank(response.getResponseString())){
				JSONObject json = JSONObject.fromObject(response.getResponseString());
				if(json.containsKey("listusersresponse")){
					json = json.getJSONObject("listusersresponse");
					if(json.containsKey("count") && json.getInt("count")>0 && json.containsKey("user")){
						JSONArray jsonArray = json.getJSONArray("user");
						String[] users = new String[jsonArray.size()];
						for(int i= 0; i<jsonArray.size();++i){
							users[i]=jsonArray.getJSONObject(i).getString("id");
						}
						request.setUserIds(users);
					}
				}
			}
			
			}else if(request.getRole().intValue()==ActiveUser.USER){
				request.setUserIds(new String[]{request.getUserid()});
			}else if(request.getRole().intValue() == ActiveUser.ADMIN){
				//所有用户，不加条件限制
			}
		
		try{
			List<PhysicsHost> listPhysicsHost=new ArrayList<PhysicsHost>();
			listPhysicsHost=getCMDBPhysicsHostWithOrderInfo(new PhysicsHostRequest());				
			if(StringUtils.isNoneBlank(request.getHostName())){
				final String hostName=request.getHostName().trim();
				listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
	                public boolean evaluate(Object arg0) {
	                	PhysicsHost physicsHost = (PhysicsHost) arg0;
	                	return physicsHost.getHostName().contains(hostName);
	                	
	                }
	            });	
			}else if(PhysicsHostStatus.ASSIGNMENTESD.toString().equals(request.getHostTypeStatus())){
				listPhysicsHost=getPhysicsHostWithAssociate(request);				
			}else if(PhysicsHostStatus.UNASSIGNMENTE.toString().equals(request.getHostTypeStatus())){
				listPhysicsHost=getPhysicsHostUnAssignmented();
			}else if(PhysicsHostStatus.VIRTUALED.toString().equals(request.getHostTypeStatus())){
				listPhysicsHost=getCMDBPhysicsHostWithOrderInfo(new PhysicsHostRequest());
				listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
	                public boolean evaluate(Object arg0) {
	                	PhysicsHost physicsHost = (PhysicsHost) arg0;
	                	return PhysicsHostStatus.VIRTUALED.toString().equals(physicsHost.getHostTypeStatus());
	                }
	            });	
			}else if("mine".equals(request.getHostTypeStatus())){
				final String currentUserId=request.getUserid();
				listPhysicsHost=getPhysicsHostWithAssociate(request);
				listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
	                public boolean evaluate(Object arg0) {
	                	PhysicsHost physicsHost = (PhysicsHost) arg0;
	                	return physicsHost.getOwner().equalsIgnoreCase(currentUserId);
	                }
	            });	
				
			}
			ListResponse<PhysicsHost> result= new ListResponse<PhysicsHost>();
			result.setCount(new Integer(String.valueOf(listPhysicsHost.size())));			
			List<?> list=CollectionUtil.getSubListByPage(listPhysicsHost,request.getPage(),request.getPagesize());
			List<PhysicsHost> subPhysicsHost=(List<PhysicsHost>)list;
			result.setResponses(listPhysicsHost);
			return result;
		}catch(ServiceException  er){
			throw new ServiceException(er.getMessage());
		}catch(BuessionException er){
			throw new ServiceException(er.getMessage());
		}catch(Exception er){
			throw new ServiceException(er.getMessage());
		}
		
	}
	
	@Transactional(readOnly = true)
	public PhysicsHost getPhysicsHost(final String hostname)throws ServiceException{
		try{					
			List<PhysicsHost> listPhysicsHost=this.getCMDBPhysicsHost();
			listPhysicsHost = (List<PhysicsHost>) CollectionUtils.select(listPhysicsHost,new Predicate() {
                public boolean evaluate(Object arg0) {
                	PhysicsHost physicsHost = (PhysicsHost) arg0;
                	if(physicsHost.getHostName().equals(hostname)){
                		return true;
                	}else{
                		return false;
                	}
                }
            });
			if(listPhysicsHost.isEmpty()){
				return new PhysicsHost();
			}else{
				return listPhysicsHost.get(0);
			}
			
		}catch(Exception er){			
			throw new ServiceException(er.getMessage());
		}catch(BuessionException er){			
			throw new ServiceException(er.getMessage());
		}
		
	}
	
	public boolean  isExist(String hostName){
		PhysicsInstances p = new PhysicsInstances();		
		p.setPhysicsName(hostName);		
		List<PhysicsInstances> instances = physicsInstanceDao.findByExample(p);		
		if(instances==null)
			return false;		
		return instances.size()==0?false:true;
	}
	
	
	/**
	 * 
	 * @param orderinfo 参数格式：{"orderId"：'1111',"isVirtualHost":"1 虚拟机 0 物理机 -1 未知",
	 * "physicsName":"physicsName值" ,"physicsType":"physicsName值", 
	 * "orderTypeId":"orderTypeId值","orderTypeName":"orderTypeName值",
	 * "owner":"userId","ownerName":"ownerName值"}
	 * @param  owner
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean savePhysicsHostAssignment(String strOrderinfo,ActiveUser owner)throws ServiceException{
		if(log.isInfoEnabled()){
			log.info("传递来的创建物理机实例申请单信息："+strOrderinfo);
		}
		PhysicsInstances instance=null;//new PhysicsInstances();
		try{
			 JSONObject orderinfo= JSONObject.fromObject(strOrderinfo);
			 JsonConfig config=new JsonConfig();
			 config.setRootClass(PhysicsInstances.class);
			 instance=(PhysicsInstances)JSONObject.toBean(orderinfo,config);
		 }catch(Exception er){
				log.error("创建物理机实例申请单信息格式有误：", er);
				throw new ServiceException(er.getMessage());
		 }
		 
//		 instance.setPhysicsName(orderinfo.getString("physicsName"));
//		 instance.setPhysicsType(orderinfo.getString("physicsType"));
//		 instance.setOwner(orderinfo.getString("owner"));
//		 instance.setOwnerName(orderinfo.getString("ownerName"));
//		 instance.setOrderId(orderinfo.getString("orderId"));
		 instance.setIsAllocated("1");
		 instance.setIsValid("1");
		 instance.setCreatedBy(owner.getUserName());
		 instance.setCreatedOn(new Date());
		 instance.setModifiedBy(owner.getUserName());
		 instance.setModifiedOn(new Date());
		try{
			
			return physicsInstanceDao.save(instance)!=null;
			
		}catch(Exception er){
			log.error("添加物理机分配信息出现错误：", er);
			throw new ServiceException(er.getMessage());
		}
	}
	/**
	 * 
	 * @param host 参数格式：{
	 * "id"：'1111',"name":"name值","displayname":"displayname值" , 
	 * "hostid":"hostid值","hostname":"hostname值",
	 * "account":"admin"}
	 * @param  owner
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Response saveVirtualCItoCMDB(JSONArray arrHosts)throws ServiceException{
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try{
			List<PhysicsHost> listPhysicsHost=builder.buildHostDataSource();
			final Map<String,PhysicsHost> mapPhysicsHost=new ConcurrentHashMap<String,PhysicsHost>();
			List<String> mapPhysicsHostName = new ArrayList<String>();
//			List<String> mapPhysicsHostName=(List<String>)CollectionUtils.collect(listPhysicsHost, new Transformer() {
//                public Object transform(Object arg0) {
//                	PhysicsHost host= (PhysicsHost) arg0;
//                	mapPhysicsHost.put(host.getHostName(), host);
//                    return host.getHostName();
//                }
//            });
			for(PhysicsHost physicshost :listPhysicsHost){
				mapPhysicsHostName.add(physicshost.getHostName());
				mapPhysicsHost.put(physicshost.getHostName(),physicshost);
			}
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();
			JSONArray result=builder.saveCmdbVirtualHost(arrHosts,mapPhysicsHost);
			for(int index=0;index<result.size();index++ ){
				JSONObject json  = result.getJSONObject(index);
				if(json.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(json.getString("errMessage"));
				}
			}
			respone.setStatusCode(resultValue?HttpStatus.SC_OK:HttpStatus.SC_PAYMENT_REQUIRED);
			respone.setResponseString(sbMessage.toString());
		}catch(BuessionException er){
			log.error("保存CMDB失败",er);
			throw new ServiceException(er.getMessage());
		}
		return respone;
		
	}
	/**
	 * 删除CMDB中虚拟机相关信息
	 * @param host 参数格式：{
	 * "name":"name值","instancename":"instancename值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public Response deleteVirtualCItoCMDB(JSONArray virtualMachines)throws ServiceException{
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try{
			
			List<PhysicsHost> listPhysicsHost=builder.buildHostDataSource();
			final Map<String,PhysicsHost> mapPhysicsHost=new ConcurrentHashMap<String,PhysicsHost>();
			List<String> mapPhysicsHostName = new ArrayList<String>();
//			List<String> mapPhysicsHostName=(List<String>)CollectionUtils.collect(listPhysicsHost, new Transformer() {
//                public Object transform(Object arg0) {
//                	PhysicsHost host= (PhysicsHost) arg0;
//                	mapPhysicsHost.put(host.getHostName(), host);
//                    return host.getHostName();
//                }
//            });
			for(PhysicsHost physicshost :listPhysicsHost){
				mapPhysicsHostName.add(physicshost.getHostName());
				mapPhysicsHost.put(physicshost.getHostName(),physicshost);
			}
			JSONArray result=builder.deleteCmdbVirtualMachines(virtualMachines, mapPhysicsHost);
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();			
			for(int index=0;index<result.size();index++ ){
				JSONObject json  = result.getJSONObject(index);
				if(json.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(json.getString("errMessage"));
				}
			}
			respone.setStatusCode(resultValue?HttpStatus.SC_OK:HttpStatus.SC_PAYMENT_REQUIRED);
			respone.setResponseString(sbMessage.toString());
		}catch(BuessionException er){
			log.error("删除CMDB失败",er);
			throw new ServiceException(er.getMessage());
		}
		return respone;
		
	}
	@Override
	public Response migrateVirtualMachineCmdb(JSONObject json)
			throws ServiceException {
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try {
			JSONArray result=builder.migrateVirtualMachineCmdb(json);
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();			
			for(int index=0;index<result.size();index++ ){
				JSONObject jn  = result.getJSONObject(index);
				if(jn.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(jn.getString("errMessage"));
				}
			}
			respone.setStatusCode(resultValue?HttpStatus.SC_OK:HttpStatus.SC_PAYMENT_REQUIRED);
			respone.setResponseString(sbMessage.toString());
		} catch (BuessionException e) {
			log.error("保存CMDB失败",e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}
	@Override
	public Response scaleVirtualMachineCmdb(JSONObject json)
			throws ServiceException {
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try {
			JSONArray result=builder.scaleVirtualMachineCmdb(json);
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();			
			for(int index=0;index<result.size();index++ ){
				JSONObject jn  = result.getJSONObject(index);
				if(jn.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(jn.getString("errMessage"));
				}
			}
		} catch (BuessionException e) {
			log.error("保存CMDB失败",e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}	
	@Override
	public Response saveProjectCmdb(JSONArray arrayProject) throws ServiceException {

		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try {
			JSONArray result=builder.saveProjectCmdb(arrayProject);
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();			
			for(int index=0;index<result.size();index++ ){
				JSONObject jn  = result.getJSONObject(index);
				if(jn.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(jn.getString("errMessage"));
				}
			}
		} catch (BuessionException e) {
			log.error("保存CMDB失败",e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	
	}
	@Override
	public Response updateProjectVirtualMachineCmdb(JSONObject json)
			throws ServiceException {
		AbstractPhysicsHostBuilder builder=new RmiPhysicsHostBuilder();		
		Response respone=new Response();
		try {
			JSONArray result=builder.updateProjectVirtualMachineCmdb(json);
			boolean resultValue=true;
			StringBuffer sbMessage=new StringBuffer();			
			for(int index=0;index<result.size();index++ ){
				JSONObject jn  = result.getJSONObject(index);
				if(jn.getInt("status")!=HttpStatus.SC_OK){
					resultValue=false;
					sbMessage.append(jn.getString("errMessage"));
				}
			}
		} catch (BuessionException e) {
			log.error("保存CMDB失败",e);
			throw new ServiceException(e.getMessage());
		}
		return null;
	}	
	public PhysicsInstanceDao getPhysicsInstanceDao() {
		return physicsInstanceDao;
	}
	public void setPhysicsInstanceDao(PhysicsInstanceDao physicsInstanceDao) {
		this.physicsInstanceDao = physicsInstanceDao;
	}
	public GenericCloudServerManagerImpl getGenericCloudServerManager() {
		return genericCloudServerManager;
	}
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}
}

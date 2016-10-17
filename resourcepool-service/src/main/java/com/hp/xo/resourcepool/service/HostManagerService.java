package com.hp.xo.resourcepool.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.exception.ServiceException;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.request.PhysicsHostRequest;
import com.hp.xo.resourcepool.response.ListResponse;

public interface HostManagerService {
	/**
	 * 获得未分配的物理机器
	 * @returns
	 * @throws ServiceException
	 */
	//public JSONObject getPhysicsHostUnAssignment()throws ServiceException;
	/**
	 * 获得物理机的使用情况
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public JSONObject getPhysicsHostsCapacity(PhysicsHostRequest request)throws ServiceException;
	/**
	 * 获得分配物理机的状态情况
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public JSONObject listPhysicsHostStatus(PhysicsHostRequest request)throws ServiceException;
	/**
	 * 获得已经分配的物理机器
	 * @return
	 * @throws ServiceException
	 */
	public ListResponse<PhysicsHost> getPhysicsHost(PhysicsHostRequest request)throws ServiceException;
	
	/**
	 * 根据订单号获得物理机器列表
	 * @return
	 * @throws ServiceException
	 */
	public List<PhysicsHost> getPhysicsHostWithOrderid(String orderId)throws ServiceException;
	/**
	 * 作废分配的物理机
	 * @param orderId
	 * @return
	 * @throws ServiceException
	 */
	public boolean saveEnabledPhysicsInstanceByOrderid(String orderId)throws ServiceException;
	
	
	public PhysicsHost getPhysicsHost(String hostname)throws ServiceException;
	
	/**
	 * 获得未分配的物理机器
	 * @returnss
	 * @throws ServiceException
	 */
	public List<PhysicsHost> getPhysicsHostUnAssignmented()throws ServiceException;
	
	/**
	 * 
	 * @param orderinfo 参数格式：{"orderId"：'1111',"isVirtualHost":"1 虚拟机 0 物理机 -1 未知",
	 * "physicsName":"physicsName值" ,"physicsType":"physicsName值", 
	 * "orderTypeId":"orderTypeId值","orderTypeName":"orderTypeName值",
	 * "owner":"userId","ownerName":"ownerName值"}
	 * @param owner 用户
	 * @return
	 * @throws ServiceException
	 */
	public boolean savePhysicsHostAssignment(String strOrderinfo,ActiveUser owner)throws ServiceException;
 
	
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
	public Response saveVirtualCItoCMDB(JSONArray arrHosts)throws ServiceException;
	
	
	/**
	 * 删除CMDB中虚拟机相关信息
	 * @param host 参数格式：{
	 * "name":"name值","instancename":"instancename值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	public Response deleteVirtualCItoCMDB(JSONArray virtualMachines)throws ServiceException;
	/**
	 * update CMDB中虚拟机与物理机的关系 用于对虚机迁移主机时候update cmdb用
	 * @param json 参数格式：{
	 * "hostname":"hostname值","instancename":"instancename值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	public Response migrateVirtualMachineCmdb(JSONObject json)throws ServiceException;
	
	/**
	 * update CMDB中虚拟机 用于对虚机更改服务方案调用
	 * @param json 参数格式：{
	 * "hostname":"hostname值","instancename":"instancename值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	public Response scaleVirtualMachineCmdb(JSONObject json)throws ServiceException;
	
	
	
	
	
	/**
	 * add CMDB中业务系统 用于新增业务系统调用
	 * @param json 参数格式：{
	 * "hostname":"hostname值","instancename":"instancename值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	public Response saveProjectCmdb(JSONArray arrayProject)throws ServiceException;
	
	
	
	
	/**
	 * 分配业务系统 CMDB中虚拟机与业务系统关系随之改变 用于分配业务系统调用
	 * @param json 参数格式：{
	 * "projectName":"projectName值","instancename":"instancename值","newProjectName":"newProjectName值"
	 * }
	 * @return
	 * @throws ServiceException
	 */
	public Response updateProjectVirtualMachineCmdb(JSONObject json)throws ServiceException;
}

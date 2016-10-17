package com.hp.xo.resourcepool.schedule.service;

import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.service.impl.GenericCloudServerManagerImpl;

public interface ScheduleService {
	/**
	 * 销毁实例
	 * @param arr
	 * @return
	 * @throws BuessionException
	 */
	public boolean destroy(JSONArray arr,Map<String, Object[]> cloudStackParams)throws BuessionException;
	/**
	 * 销毁实例
	 * @param cloudStackParams
	 * @return
	 * @throws BuessionException
	 */
	public boolean destroy(Map<String, Object[]> cloudStackParams)throws BuessionException;
	/**
	 * 销毁实例
	 * @param json
	 * @param cloudStackParams
	 * @return
	 * @throws BuessionException
	 */
	public JSONObject destroyInstance(JSONObject json,Map<String, Object[]> cloudStackParams)throws BuessionException;
	/**
	 * 迁移虚机到其它主机cmdb关系处理
	 * @param json
	 * @return
	 * @throws BuessionException
	 */
	public JSONObject migrateVirtualMachineCmdb(JSONObject json)throws BuessionException;
	
	
	/**
	 * 分配虚机到其它业务系统
	 * @param json
	 * @return
	 * @throws BuessionException
	 */
	public JSONObject updateProjectVirtualMachineCmdb(JSONObject json)throws BuessionException;
	
	public GenericCloudServerManagerImpl getGenericCloudServerManager();
	public void setGenericCloudServerManager(GenericCloudServerManagerImpl genericCloudServerManager);
	
}

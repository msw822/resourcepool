package com.hp.xo.resourcepool.service;

import java.util.Map;

import com.hp.xo.resourcepool.model.GlobalSetting;
import com.hp.xo.resourcepool.request.ListGlobalSettingRequest;
import com.hp.xo.resourcepool.response.ListResponse;

/**
 * 全局设置—自定义设置 Service
 * GlobalSettingManager Service
 * @author lixinqi
 *
 */
public interface GlobalSettingManager extends GenericManager<GlobalSetting, Long> {
	
	/**
	 * 列表 
	 * @param request
	 * @return
	 */
	ListResponse<GlobalSetting> listUserDefined(ListGlobalSettingRequest request);
	
	/**
	 * 更新
	 * @param requestParams
	 */
	void updateUserDefined(Map<String,Object[]> requestParams);
	
	/**
	 * 取值
	 * @param key
	 * @return	字符串
	 */
	String getValueByKey(String key);
	
	/**
	 * 取值
	 * @param key
	 * @return	布尔类型
	 */
	boolean getBooleanValueByKey(String key);
	
	/**
	 * 取值
	 * @param key
	 * @return	数字类型
	 */
	Number getNumberValueByKey(String key);
	
	/**
	 * 取值
	 * @param key
	 * @return	解密后的数据
	 */
	String getDecryptValueByKey(String key);
	
	/**
	 * <p>加密或解密数据。
	 * <p>加密请传入参数：mode=Cipher.ENCRYPT_MODE
	 * <p>解密请传入参数：mode=Cipher.DECRYPT_MODE
	 * @param datasource
	 * @param password
	 * @param mode
	 * @return
	 */
	byte[] des(byte[] datasource, String password, int mode);

}

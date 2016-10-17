package com.hp.xo.resourcepool.service.impl;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sun.misc.BASE64Decoder;

import com.hp.xo.resourcepool.dao.GlobalSettingDao;
import com.hp.xo.resourcepool.model.GlobalSetting;
import com.hp.xo.resourcepool.request.ListGlobalSettingRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.GlobalSettingManager;
import com.ibm.icu.text.NumberFormat;

@SuppressWarnings("restriction")
@Service(value="globalSettingManager")
@Transactional(propagation=Propagation.REQUIRED)
public class GlobalSettingManagerImpl extends GenericManagerImpl<GlobalSetting, Long>
		implements GlobalSettingManager {
	
	@Autowired
	private GlobalSettingDao globalSettingDao;

	@Override
	public ListResponse<GlobalSetting> listUserDefined(ListGlobalSettingRequest request) {
		ListResponse<GlobalSetting> result = new ListResponse<GlobalSetting>();
		List<GlobalSetting> list = null;
		Integer count = 0;
		
		list = globalSettingDao.listUserDefined(request, request.getPage(), request.getPagesize());
		count = globalSettingDao.countByExample(request);
		
		result.setResponses(list);
		result.setCount(count);
		return result;
	}

	@Override
	public void updateUserDefined(Map<String,Object[]> requestParams) {
		String name = (String) requestParams.get("name")[0];
		String value = (String) requestParams.get("value")[0];
		String sql = "update t_globalsetting set value = '%s' where name = '%s' ";
		globalSettingDao.updateBySQLQuery(String.format(sql, value, name));
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getValueByKey(String key) {
		String sql = "select value from t_globalsetting where key = '%s' ";
		List<String> list = (List<String>) globalSettingDao.findListBySql(String.format(sql, key));
		return list.get(0);
	}
			
	@SuppressWarnings("null")
	@Override
	public boolean getBooleanValueByKey(String key) {
		if(GlobalSetting.true_.equalsIgnoreCase(getValueByKey(key))){
			return true;
		}else if(GlobalSetting.false_.equalsIgnoreCase(getValueByKey(key))){
			return false;
		}else{
			try {
				throw new Exception("数值不是true或false");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return (Boolean) null;
		}
	}

	@Override
	public Number getNumberValueByKey(String key) {
		try {
			return NumberFormat.getInstance().parse(getValueByKey(key));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDecryptValueByKey(String key) {
		BASE64Decoder dec=new BASE64Decoder();
		byte[] util = null;
		try {
			util = dec.decodeBuffer(getValueByKey(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] result = des(util, GlobalSetting.password_, Cipher.DECRYPT_MODE);
		return new String(result);
	}

	@Override
	public byte[] des(byte[] datasource, String password, int mode) {
		try{
	        SecureRandom random = new SecureRandom();
	        
	        DESKeySpec desKey = new DESKeySpec(password.getBytes());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        SecretKey securekey = keyFactory.generateSecret(desKey);
	        
	        Cipher cipher = Cipher.getInstance("DES");
	        cipher.init(mode, securekey, random);
	        
	        return cipher.doFinal(datasource);
        }catch(Throwable e){
                e.printStackTrace();
        }
		
        return null;
	}
	
	


}

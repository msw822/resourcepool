package com.hp.xo.resourcepool.web.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import sun.misc.BASE64Encoder;

import com.hp.xo.resourcepool.model.GlobalSetting;
import com.hp.xo.resourcepool.request.ListGlobalSettingRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.GlobalSettingManager;
import com.hp.xo.resourcepool.utils.Page;
import com.hp.xo.resourcepool.web.action.core.BaseAction;

/**
 * 全局设置Action
 * @author lixinqi
 *
 */
@SuppressWarnings("restriction")
public class GlobalSettingAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static boolean isDateTypeError ;
	
	@Autowired
	private GlobalSettingManager globalSettingManager;

	/**
	 * 全局设置列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String listUserDefined(){
		ListGlobalSettingRequest request = (ListGlobalSettingRequest) this.wrapRequest(new ListGlobalSettingRequest());
		ListResponse<GlobalSetting> response = globalSettingManager.listUserDefined(request);
		
		//测试能否取值
		/*String userEmail = globalSettingManager.getValueByKey("user.email");
		boolean saveCookie = globalSettingManager.getBooleanValueByKey("cookie.isSaveCookie");
		boolean cleanCookie = globalSettingManager.getBooleanValueByKey("cookie.isCleanCookie");
		Number session = globalSettingManager.getNumberValueByKey("session.timeOut");
		String account = globalSettingManager.getValueByKey("mail.from.address");
		String username = globalSettingManager.getValueByKey("mail.username");
		String password = globalSettingManager.getDecryptValueByKey("mail.password");*/
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("listGlobalSetting", response.getResponses());
		if(isDateTypeError){
			jsonPage.put("isDateTypeError", "true");
		}
		this.renderText(jsonPage.toString());
		
		return NONE;
	}
	
	public String updateUserDefined(){
		//校验数据类型是否正确
		isDateTypeError = false;
		String valueType = (String) this.requestParams.get("valueType")[0];
		String value = (String) this.requestParams.get("value")[0];
		if(GlobalSetting.JAVA_LANG_BOOLEAN.equalsIgnoreCase(valueType)){
			if( !GlobalSetting.true_.equalsIgnoreCase(value) 
					&& !GlobalSetting.false_.equalsIgnoreCase(value) ){
				isDateTypeError = true;
				return listUserDefined();
			}
		}else if(GlobalSetting.JAVA_LANG_NUMBER.equalsIgnoreCase(valueType)){
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(value);
			if( !isNum.matches() ){
				isDateTypeError = true;
				return listUserDefined(); 
			}
		}else if(GlobalSetting.ENCRYPTION.equalsIgnoreCase(valueType)){
			byte[] encryptValue = globalSettingManager.des(value.getBytes(), GlobalSetting.password_, Cipher.ENCRYPT_MODE);
			BASE64Encoder enc=new BASE64Encoder();
			this.requestParams.put("value", new String[]{enc.encode(encryptValue)});
		}
		
		globalSettingManager.updateUserDefined(this.requestParams);
		
		return listUserDefined();
	}

	
}

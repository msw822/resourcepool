package com.hp.xo.resourcepool.request;

public interface Request {
	String getUserid();
	void setUserid(String userid);
	String getApikey();
	void setApikey(String apikey);
	String getSecretkey();
	void setSecretkey(String secretkey);
	String getClientIP();
	void setClientIP(String clientIP);
	String getDefaultLang();
	void setDefaultLang(String defaultLang);
	String getLoginId();
	void setLoginId(String loginId);
}

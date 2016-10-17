package com.hp.xo.resourcepool.model;

public class ActiveUser {
	/**超级管理员 1*/
	public static Integer ADMIN = 1;
	/**域管理员 2*/
	public static Integer DOMAINADMIN = 2;
	/**用户 0*/
	public static Integer USER = 0;
	private String userid = null;
	private String loginId = null;
	private String apikey = null;
	private String secretkey = null;
	private String clientIP = null;
	private String defaultLang = null;
	private String path=null;
	private String path2=null;
	private String account=null;
	private String accountid=null;
	private String domainid=null;
	/**登录用户名*/
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 1   admin
	 * 2   domainAdmin
	 * 0   user
	 **/
	private Integer role =  -1;
	
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	
	public String getDefaultLang() {
		return defaultLang;
	}
	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath2() {
		return path2;
	}
	public void setPath2(String path2) {
		this.path2 = path2;
	}
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccountid() {
		return accountid;
	}
	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}
	public String getDomainid() {
		return domainid;
	}
	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}
	@Override
	public String toString() {
		return "ActiveUser [userid=" + userid + ", loginId=" + loginId
				+ ", apikey=" + apikey + ", secretkey=" + secretkey
				+ ", clientIP=" + clientIP + ", defaultLang=" + defaultLang
				+ ", path=" + path + ", path2=" + path2 + ", account="
				+ account + ", accountid=" + accountid + ", domainid="
				+ domainid + "]";
	}
}

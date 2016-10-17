package com.hp.xo.resourcepool.request;

import java.util.Date;

import com.hp.xo.resourcepool.model.Parameter;

public class ListLogRequest extends BaseListRequest {

	private String account  ="";
	
	private String domainid = "";
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	@Parameter(name="id",type=FieldType.LONG)
	private Long id;
	
	/**
	 * 日志对应的操作者
	 */
	@Parameter(name="loginName",type=FieldType.STRING)
	private String loginName;// 用户名
	
	@Parameter(name="module",type=FieldType.STRING)
	private String module;// 所属模块
	
	@Parameter(name="operation",type=FieldType.STRING)
	private String operation;// 操作(登录、登出、添加、修改、删除、接口调用)

	@Parameter(name = "startDate", type = FieldType.DATE, description = "")
	private Date startDate;

	@Parameter(name = "endDate", type = FieldType.DATE, description = "")
	private Date endDate;
	
	/**
	 *  指定数组用户的日志
	 */
	private String[] operationUsers;
	
	/**登录用户名*/
	private String userName = "";

	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String[] getOperationUsers() {
		return operationUsers;
	}

	public void setOperationUsers(String[] operationUsers) {
		this.operationUsers = operationUsers;
	}

	/**
	 * 角色
	 * 1   admin
	 * 2   domainAdmin
	 * 0   user
	 */
	private Integer role;
	

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

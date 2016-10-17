package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class BuessionListRequest extends BaseListRequest{
	@Parameter(name=ApiConstants.ID, type=FieldType.STRING, description="List by ID.")
    private String id;
	@Parameter(name="status", type=FieldType.INTEGER, description="")
	private Integer status;
	@Parameter(name ="createBy",type=FieldType.STRING)
	private String createBy;
	/**
	 * 角色
	 * 1   admin
	 * 2   domainAdmin
	 * 0   user
	 */
	private Integer role;
	private String domainid;

	private String[] userIds;	
	
	/**
	 *  申请用户名
	 */
	private String[] userNames;
	
	private String[] accountIds;	
	/**
	 *  申请用户名
	 */
	private String[] accountNames;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getDomainid() {
		return domainid;
	}
	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}
	public String[] getUserIds() {
		return userIds;
	}
	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}
	public String[] getUserNames() {
		return userNames;
	}
	public void setUserNames(String[] userNames) {
		this.userNames = userNames;
	}
	public String[] getAccountIds() {
		return accountIds;
	}
	public void setAccountIds(String[] accountIds) {
		this.accountIds = accountIds;
	}
	public String[] getAccountNames() {
		return accountNames;
	}
	public void setAccountNames(String[] accountNames) {
		this.accountNames = accountNames;
	}
}

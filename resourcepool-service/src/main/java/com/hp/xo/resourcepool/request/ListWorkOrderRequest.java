package com.hp.xo.resourcepool.request;

import java.util.Date;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class ListWorkOrderRequest extends BaseListRequest {

	@Parameter(name=ApiConstants.ID, type=FieldType.LONG, description="List by ID.")
    private Long id;
    
	@Parameter(name="WORKORDER_TYPE", type=FieldType.INTEGER, description="")
	private Integer workOrderType;
    
    @Parameter(name="status", type=FieldType.INTEGER, description="")
	private Integer status;
    
    @Parameter(name ="createBy",type=FieldType.STRING)
    private String createBy;
    
    @Parameter(name ="step",type=FieldType.INTEGER)
    private Integer step;
    
   public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Parameter(name="startDate", type=FieldType.DATE, description="")
	private Date startDate;
   
	@Parameter(name="endDate", type=FieldType.DATE, description="")
    private Date endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setWorkOrderType(Integer workOrderType) {
		this.workOrderType = workOrderType;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWorkOrderType() {
		return workOrderType;
	}

	public Integer getStatus() {
		return status;
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

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
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
	
	private String domainid = "";

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}
	
	
	/**
	 *  申请用户名
	 */
	private String[] applyUsers;

	public String[] getApplyUsers() {
		return applyUsers;
	}

	public void setApplyUsers(String[] applyUsers) {
		this.applyUsers = applyUsers;
	}
	
	
	/**登录用户名*/
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	//add @ma below
	@Parameter(name="isOverDue", type=FieldType.INTEGER, description="")
	private Integer isOverDue;

	public Integer getIsOverDue() {
		return isOverDue;
	}

	public void setIsOverDue(Integer isOverDue) {
		this.isOverDue = isOverDue;
	}

	@Parameter(name="instanceId", type=FieldType.STRING, description="")
	private String instanceId;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}


	
	
	
}

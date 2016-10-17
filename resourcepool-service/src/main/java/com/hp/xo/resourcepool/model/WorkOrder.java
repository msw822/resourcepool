package com.hp.xo.resourcepool.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "T_WORKORDER")
public class WorkOrder extends BaseEntity {

	/**处理失败  6*/
	public static int STATUS_PROVISONFAIL = 6;
	/**处理成功  5*/
	public static int STATUS_PROVISIONSUCCEED = 5;
	/**审批通过正在处理   4*/
	public static int STATUS_PROVISIONING = 4;
	/**审批未通过  3*/
	public static int STATUS_REJECTED = 3;
	/**审批通过待处理  2*/
	public static int STATUS_APPROVED = 2;
	/**待审批 1*/
	public static int STATUS_WAITINGFORAPPROVAL = 1;
	
	/**待回收*/
	public static int STATUS_INSTANCES_RECYCLE = 7;  
	/**已结束*/
	public static int STATUS_INSTANCES_END = 8; 
	
	public static int TYPE_NEWPROJECTAPPLICATION = 1;
	public static int TYPE_PROJECTLIMITAPPLICATION = 2;
	public static int TYPE_DOMAINLIMITAPPLICATION = 3;
	public static int TYPE_ACCOUNTLIMITAPPLICATION = 4;
	public static int TYPE_DEPLOYVIRTUALMACHINE = 5;
	public static int TYPE_SCALEVIRTUALMACHINE = 6;
	public static int TYPE_NEWVOLUME = 7;
	public static int TYPE_EXTENDORDER = 8;
	public static int TYPE_RESIZEVOLUME = 9;
	public static String KEY_X86_PHYSICAL_RESOURCES_APPLICATION = "workorder_type.X86PhysicalResourcesApplication";
	public static String KEY_HP_MINICOMPUTER_RESOURCES_APPLICATION = "workorder_type.HPMinicomputerResourcesApplication";
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1954983368006338624L;

	@SequenceGenerator(sequenceName = "T_WORKORDER_ID_SEQ", name = "T_WORKORDER_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_WORKORDER_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "PK_WORKORDER_ID", nullable = false)
	private Long id = null;

	@Column(name = "WORKORDER_TYPE")
	private Integer workOrderType;

	@Column(name = "ACCOUNT_ID")
	private String accountId;

	@Column(name = "DOMAIN_ID")
	private String domainId;

	@Column(name = "APPLIER_ID")
	private String applierId;

	@Column(name = "APPLIER_NAME")
	private String applierName;

	@Column(name = "APPROVER_ID")
	private String approveId;

	@Column(name = "APPROVER_NAME")
	private String approveName;

	@Column(name = "APPLIER_ABSOLUTE_DOMAIN")
	private String applierAbsDomain;

	@Column(name = "APPLIER_RELATIVE_DOMAIN")
	private String applierRelDomain;

	@Column(name = "STATUS")
	private Integer status;

	@Column(name = "DESCRIPT")
	private String descript;
	
	@Column(name = "APPROVE_DESC")
	private String approveDesc;
	
	@Column(name = "APPROVE_RESULT")
	private Integer approveResult; 
	
	@Column(name = "ERROR_CODE")
	private String errorCode;

	@Column(name = "ERROR_TEXT")
	private String errorText;
	
	//add column workorder_due_date @ma
	@Column(name = "WORKORDER_DUE_DATE")
	private Date workorder_due_date;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getWorkOrderType() {
		return workOrderType;
	}

	public void setWorkOrderType(Integer workOrderType) {
		this.workOrderType = workOrderType;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getApplierId() {
		return applierId;
	}

	public void setApplierId(String applierId) {
		this.applierId = applierId;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}

	public String getApproveName() {
		return approveName;
	}

	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}

	public String getApplierAbsDomain() {
		return applierAbsDomain;
	}

	public void setApplierAbsDomain(String applierAbsDomain) {
		this.applierAbsDomain = applierAbsDomain;
	}

	public String getApplierRelDomain() {
		return applierRelDomain;
	}

	public void setApplierRelDomain(String applierRelDomain) {
		this.applierRelDomain = applierRelDomain;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	

	public String getApproveDesc() {
		return approveDesc;
	}

	public void setApproveDesc(String approveDesc) {
		this.approveDesc = approveDesc;
	}

	public Integer getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(Integer approveResult) {
		this.approveResult = approveResult;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	

	public Date getWorkorder_due_date() {
		return workorder_due_date;
	}

	public void setWorkorder_due_date(Date workorder_due_date) {
		this.workorder_due_date = workorder_due_date;
	}

	@Override
	public String toString() {
		return String
				.format("WorkOrder [id=%s, workOrderType=%s, accountId=%s, domainId=%s, applierId=%s, applierName=%s, approveId=%s, approveName=%s, applierAbsDomain=%s, applierRelDomain=%s, status=%s, descript=%s, approveDesc=%s, approveResult=%s, workItems=%s]",
						id, workOrderType, accountId, domainId, applierId,
						applierName, approveId, approveName, applierAbsDomain,
						applierRelDomain, status, descript, approveDesc,
						approveResult, workItems);
	}


	@OneToMany(targetEntity = WorkItem.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_WORKORDER_ID", nullable = true)
	@Fetch(FetchMode.SELECT)
	//@JoinTable(joinColumns = @JoinColumn(name = "FK_WORKORDER_ID", nullable = true))
	private List<WorkItem> workItems;

	public List<WorkItem> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(List<WorkItem> workItems) {
		this.workItems = workItems;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "ACCOUNT")
	private String account;
	
	@OneToMany(targetEntity = WorkOrderJob.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKORDER_ID", nullable = true)
	@Fetch(FetchMode.SELECT)
	private List<WorkOrderJob> workOrderJob;

	public List<WorkOrderJob> getWorkOrderJob() {
		return workOrderJob;
	}

	public void setWorkOrderJob(List<WorkOrderJob> workOrderJob) {
		this.workOrderJob = workOrderJob;
	}
	
	@OneToMany(targetEntity = WorkOrderRelate.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "WORKORDER_ID", nullable = true)
	@Fetch(FetchMode.SELECT)
	private List<WorkOrderRelate> workOrderRelate;

	public List<WorkOrderRelate> getWorkOrderRelate() {
		return workOrderRelate;
	}

	public void setWorkOrderRelate(List<WorkOrderRelate> workOrderRelate) {
		this.workOrderRelate = workOrderRelate;
	}
	
	public String getWorkOrderTypeName() {
		String typeName = "";
		switch(this.workOrderType){
			case 1:
				typeName = "业务系统申请";
				break;
			case 2:
				typeName = "业务系统扩容申请";
				break;
			case 3:
				typeName = "域扩容申请";
				break;
			case 4:
				typeName = "账户扩容申请";
				break;
			case 5:
				typeName = "实例申请";
				break;
			case 6:
				typeName = "变更服务方案申请";
				break;
			case 7:
				typeName = "新加卷申请";
				break;
			case 8:
				typeName = "实例续订申请";
				break;
		}
		return typeName;
	}
	
}

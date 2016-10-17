package com.hp.xo.resourcepool.dto;

public class ResourcePoolPermissionVO extends BaseVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer ownerType;

	private String ownerId;

	private String ownerName;

	private Boolean productionPool;

	private Boolean testingPool;

	// listAccounts -> listaccountsresponse -> account
	// id name accounttype domain
	private String accountName;// 账户名称
	private String accountType;// 账户角色
	private String accountDomain;// 账户域

	// listDomains listAll=true -> listdomainsresponse -> domain
	// id name path
	private String domainId;// 域ID
	private String domainName;// 域名称
	private String domainPath;// 域完整路径

	// listProjects -> listprojectsresponse -> project
	// id name displaytext domain account
	private String projectName;// 项目名称
	private String projectDisplayText;// 项目显示名称
	private String projectDomain;// 项目域
	private String projectAccount;// 所有者账户

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(Integer ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Boolean getProductionPool() {
		return productionPool;
	}

	public void setProductionPool(Boolean productionPool) {
		this.productionPool = productionPool;
	}

	public Boolean getTestingPool() {
		return testingPool;
	}

	public void setTestingPool(Boolean testingPool) {
		this.testingPool = testingPool;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountDomain() {
		return accountDomain;
	}

	public void setAccountDomain(String accountDomain) {
		this.accountDomain = accountDomain;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getDomainPath() {
		return domainPath;
	}

	public void setDomainPath(String domainPath) {
		this.domainPath = domainPath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDisplayText() {
		return projectDisplayText;
	}

	public void setProjectDisplayText(String projectDisplayText) {
		this.projectDisplayText = projectDisplayText;
	}

	public String getProjectDomain() {
		return projectDomain;
	}

	public void setProjectDomain(String projectDomain) {
		this.projectDomain = projectDomain;
	}

	public String getProjectAccount() {
		return projectAccount;
	}

	public void setProjectAccount(String projectAccount) {
		this.projectAccount = projectAccount;
	}

}

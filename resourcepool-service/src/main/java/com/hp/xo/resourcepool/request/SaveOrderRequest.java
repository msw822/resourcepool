package com.hp.xo.resourcepool.request;


import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class SaveOrderRequest extends BaseEntityRequest {
    @Parameter(name=ApiConstants.ID, type=FieldType.LONG, description="List by ID.")
    private Long id;
    @Parameter(name="workOrderType", type=FieldType.INTEGER, description="")
	private Integer workOrderType;
    @Parameter(name="accountId", type=FieldType.STRING, description="")
	private String accountId;
    @Parameter(name="domainId", type=FieldType.STRING, description="")
	private String domainId;
    @Parameter(name="applierId", type=FieldType.STRING, description="")
	private String applierId;
    @Parameter(name="applierName", type=FieldType.STRING, description="")
	private String applierName;
    @Parameter(name="approveId", type=FieldType.STRING, description="")
	private String approveId;
    @Parameter(name="approveName", type=FieldType.STRING, description="")
	private String approveName;
    @Parameter(name="applierAbsDomain", type=FieldType.STRING, description="")
	private String applierAbsDomain;
    @Parameter(name="applierRelDomain", type=FieldType.STRING, description="")
	private String applierRelDomain;
    @Parameter(name="status", type=FieldType.INTEGER, description="")
	private Integer status;
    @Parameter(name="descript", type=FieldType.STRING, description="")
	private String descript;
    
    @Parameter(name = "approveDesc",type=FieldType.STRING)
	private String approveDesc;
    
	
    @Parameter(name = "approveResult",type=FieldType.INTEGER)
	private Integer approveResult = null ; 
    
    /**
     * 
     */
    @Parameter(name="name", type=FieldType.STRING, description="")
	private String name;
    @Parameter(name="displaytext", type=FieldType.STRING, description="")
	private String displaytext;
    @Parameter(name="instance", type=FieldType.STRING, description="")
	private String instance;
    @Parameter(name="ip", type=FieldType.STRING, description="")
	private String ip;
    @Parameter(name="volume", type=FieldType.STRING, description="")
	private String volume;
    @Parameter(name="snapshot", type=FieldType.STRING, description="")
	private String snapshot;
    @Parameter(name="template", type=FieldType.STRING, description="")
	private String template;
    @Parameter(name="network", type=FieldType.STRING, description="")
	private String network;
    @Parameter(name="vpc", type=FieldType.STRING, description="")
	private String vpc;
    @Parameter(name="cpu", type=FieldType.STRING, description="")
	private String cpu;
    @Parameter(name="memory", type=FieldType.STRING, description="")
	private String memory;
    @Parameter(name="primaryStorage", type=FieldType.STRING, description="")
	private String primaryStorage;
    @Parameter(name="secondaryStorage", type=FieldType.STRING, description="")
	private String secondaryStorage;
    @Parameter(name="applyReason", type=FieldType.STRING, description="")
	private String applyReason;
    @Parameter(name="resourcePoolId", type=FieldType.STRING, description="")
	private String resourcePoolId;
    
    @Parameter(name="projectid", type=FieldType.STRING, description="")
	private String projectid;
    
    @Parameter(name="account", type=FieldType.STRING, description="")
	private String account;
    
    
    @Parameter(name="templateid", type=FieldType.STRING, description="")
	private String templateid;
    @Parameter(name="zoneid", type=FieldType.STRING, description="")
	private String zoneid;
    @Parameter(name="displayname", type=FieldType.STRING, description="")
	private String displayname;
    @Parameter(name="group", type=FieldType.STRING, description="")
	private String group;
    @Parameter(name="hypervisor", type=FieldType.STRING, description="")
	private String hypervisor;
    @Parameter(name="networkids", type=FieldType.STRING, description="")
	private String networkids;
    
    @Parameter(name="vmid", type=FieldType.STRING, description="")
   	private String vmid;
    @Parameter(name="serviceofferingid", type=FieldType.STRING, description="")
   	private String serviceofferingid;
    
    
    @Parameter(name="virtualmachineid", type=FieldType.STRING, description="")
   	private String virtualmachineid;
    public String getDiskSize() {
		return diskSize;
	}
	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}
	@Parameter(name="diskOfferingId", type=FieldType.STRING, description="")
   	private String diskOfferingId;
    @Parameter(name="diskSize", type=FieldType.STRING, description="")
   	private String diskSize;
    @Parameter(name="iscustomized", type=FieldType.INTEGER, description="")
   	private String iscustomized;
    
	public Long getId() {
		return id;
	}
	public String getIscustomized() {
		return iscustomized;
	}
	public void setIscustomized(String iscustomized) {
		this.iscustomized = iscustomized;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getWorkOrderType() {
		return workOrderType;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplaytext() {
		return displaytext;
	}
	public void setDisplaytext(String displaytext) {
		this.displaytext = displaytext;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getSnapshot() {
		return snapshot;
	}
	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	public String getVpc() {
		return vpc;
	}
	public void setVpc(String vpc) {
		this.vpc = vpc;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getPrimaryStorage() {
		return primaryStorage;
	}
	public void setPrimaryStorage(String primaryStorage) {
		this.primaryStorage = primaryStorage;
	}
	public String getSecondaryStorage() {
		return secondaryStorage;
	}
	public void setSecondaryStorage(String secondaryStorage) {
		this.secondaryStorage = secondaryStorage;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public String getResourcePoolId() {
		return resourcePoolId;
	}
	public void setResourcePoolId(String resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getVmid() {
		return vmid;
	}
	public void setVmid(String vmid) {
		this.vmid = vmid;
	}
	public String getServiceofferingid() {
		return serviceofferingid;
	}
	public void setServiceofferingid(String serviceofferingid) {
		this.serviceofferingid = serviceofferingid;
	}
	public String getVirtualmachineid() {
		return virtualmachineid;
	}
	public void setVirtualmachineid(String virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
	}
	public String getDiskOfferingId() {
		return diskOfferingId;
	}
	public void setDiskOfferingId(String diskOfferingId) {
		this.diskOfferingId = diskOfferingId;
	}
	public String getTemplateid() {
		return templateid;
	}
	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getHypervisor() {
		return hypervisor;
	}
	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}
	public String getNetworkids() {
		return networkids;
	}
	public void setNetworkids(String networkids) {
		this.networkids = networkids;
	}
	
	
}


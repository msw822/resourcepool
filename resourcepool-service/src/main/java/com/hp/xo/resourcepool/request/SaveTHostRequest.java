package com.hp.xo.resourcepool.request;



import com.hp.xo.resourcepool.model.Parameter;

public class SaveTHostRequest extends BaseEntityRequest {

	@Parameter(name = "id", type = FieldType.LONG, description = "")
	private Long id;

	@Parameter(name = "manufacturer", type = FieldType.STRING, description = "")
	private String manufacturer;

	@Parameter(name = "productname", type = FieldType.STRING, description = "")
	private String productname;

	@Parameter(name = "serialnumber", type = FieldType.STRING, description = "")
	private String serialnumber;

	@Parameter(name = "servername", type = FieldType.STRING, description = "")
	private String servername;

	@Parameter(name = "cpucount", type = FieldType.INTEGER, description = "")
	private Integer cpucount;

	@Parameter(name = "cpucores", type = FieldType.INTEGER, description = "")
	private Integer cpucores;

	@Parameter(name = "cputype", type = FieldType.STRING, description = "")
	private String cputype;

	@Parameter(name = "memory", type = FieldType.INTEGER, description = "")
	private Integer memory;

	@Parameter(name = "status", type = FieldType.INTEGER, description = "")
	private Integer status;

	@Parameter(name = "saveStatus", type = FieldType.INTEGER, description = "")
	private Integer saveStatus;

	@Parameter(name = "type", type = FieldType.INTEGER, description = "")
	private Integer type;

	@Parameter(name = "nic", type = FieldType.INTEGER, description = "")
	private Integer nic;


	@Parameter(name = "hostname", type = FieldType.STRING, description = "")
	private String hostname;

	@Parameter(name = "resourcepoolid", type = FieldType.STRING, description = "")
	private String resourcepoolid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public Integer getCpucount() {
		return cpucount;
	}

	public void setCpucount(Integer cpucount) {
		this.cpucount = cpucount;
	}

	public Integer getCpucores() {
		return cpucores;
	}

	public void setCpucores(Integer cpucores) {
		this.cpucores = cpucores;
	}

	public String getCputype() {
		return cputype;
	}

	public void setCputype(String cputype) {
		this.cputype = cputype;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getNic() {
		return nic;
	}

	public void setNic(Integer nic) {
		this.nic = nic;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getResourcepoolid() {
		return resourcepoolid;
	}

	public void setResourcepoolid(String resourcepoolid) {
		this.resourcepoolid = resourcepoolid;
	}
	
	
	
}

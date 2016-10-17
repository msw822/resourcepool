package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class ResourcePoolPermissionRequest extends BaseEntityRequest {
	@Parameter(name = ApiConstants.ID, type = FieldType.LONG, description = "")
	private Long id;

	@Parameter(name = "ownerType", type = FieldType.INTEGER, description = "")
	private Integer ownerType;

	@Parameter(name = "ownerId", type = FieldType.STRING, description = "")
	private String ownerId;

	@Parameter(name = "ownerName", type = FieldType.STRING, description = "")
	private String ownerName;

	@Parameter(name = "productionPool", type = FieldType.BOOLEAN, description = "")
	private Boolean productionPool;

	@Parameter(name = "testingPool", type = FieldType.BOOLEAN, description = "")
	private Boolean testingPool;

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

}

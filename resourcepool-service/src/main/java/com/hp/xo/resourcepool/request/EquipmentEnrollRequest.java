package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class EquipmentEnrollRequest extends BaseEntityRequest {
	@Parameter(name = ApiConstants.ID, type = FieldType.LONG, description = "")
	private Long id;

	@Parameter(name = "targetType", type = FieldType.SHORT, description = "")
	private Short targetType;

	@Parameter(name = "ip", type = FieldType.STRING, description = "")
	private String ip;

	@Parameter(name = "descript", type = FieldType.STRING, description = "")
	private String descript;

	@Parameter(name = "available", type = FieldType.SHORT, description = "")
	private Short available;

	@Parameter(name = "keyword", type = FieldType.STRING, description = "")
	private String keyword;
	@Parameter(name = "page", type = FieldType.INTEGER, description = "")
	private Integer page;
	@Parameter(name = "pageSize", type = FieldType.INTEGER, description = "")
	private Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getTargetType() {
		return targetType;
	}

	public void setTargetType(Short targetType) {
		this.targetType = targetType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Short getAvailable() {
		return available;
	}

	public void setAvailable(Short available) {
		this.available = available;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}

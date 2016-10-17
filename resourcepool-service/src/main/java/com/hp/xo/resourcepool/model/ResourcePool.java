package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity(name="T_ResourcePool")
public class ResourcePool extends BaseEntity {

	@Id
	@Column(name="PK_T_ResourcePool_ID")
	private Integer id;
	
	@Column(name="resourcepool_id")
	private String resourcePoolId;
	
	public String getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(String resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	@Column(name="name")
	private String name;
	
	@Column(name="descript")
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourcePool [id=");
		builder.append(id);
		builder.append(", resourceId=");
		builder.append(resourcePoolId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", desc=");
		builder.append(desc);
		builder.append("]");
		return builder.toString();
	}
	
}

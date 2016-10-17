package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
@Entity(name="T_DIM_RESOURCE")
public class DimResource extends BaseEntity {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -8248735802314271177L;
	@SequenceGenerator(sequenceName="T_DIM_RESOURCE_ID_SEQ",name="T_DIM_RESOURCE_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator="T_DIM_RESOURCE_ID_SEQ_GEN",strategy=GenerationType.SEQUENCE)
	@Column(name="PK_T_DIM_RESOURCE_ID",nullable=false)
	private Long id = null;
	
	@Column(name="Resource_Id")
	private String resourceId;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="Pre_Resource_Id")
	private String preResourceId;
	
	@Column(name="DESCRIPT")
	private String descript;
	
	@Column(name="type")
	private String type;
	
	/**表明虚拟化类型。可能有三种值：“kvm”,"vmware","kvm,vmware"*/
	@Column(name="hypervisor")
	private String hypervisor;

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public String getPreResourceId() {
		return preResourceId;
	}

	
	public void setPreResourceId(String preResourceId) {
		this.preResourceId = preResourceId;
	}

	public String getDescript() {
		return descript;
	}

	
	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DimResource [id=");
		builder.append(id);
		builder.append(", resourceId=");
		builder.append(resourceId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", preResourceId=");
		builder.append(preResourceId);
		builder.append(", descript=");
		builder.append(descript);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}

	
}

package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_resourcepool_permission")
public class ResourcePoolPermission extends BaseEntity {
	private static final long serialVersionUID = -2231724652555839818L;
	@SequenceGenerator(sequenceName = "t_resourcepool_perm_id_seq", name = "t_resourcepool_perm_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_resourcepool_perm_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "pk_resoucepool_permission_id")
	private Long id;

	// '拥有者类型。
	// 数据保存在app_config表中。
	// 其中key的前缀为resoucepool_related_object_type
	@Column(name = "owner_type")
	private Integer ownerType;

	@Column(name = "owner_id")
	private String ownerId;

	@Column(name = "owner_name")
	private String ownerName;

	// '数据保存在app_config表中。
	// 其中key为resourcepool_permission
	@Column(name = "productionpool")
	private Integer productionPool;

	// '拥有者类型。
	// 数据保存在app_config表中。
	// 其中key的前缀为resourcepool_permission';
	@Column(name = "testingpool")
	private Integer testingPool;

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

	public Integer getProductionPool() {
		return productionPool;
	}

	public void setProductionPool(Integer productionPool) {
		this.productionPool = productionPool;
	}

	public Integer getTestingPool() {
		return testingPool;
	}

	public void setTestingPool(Integer testingPool) {
		this.testingPool = testingPool;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((ownerName == null) ? 0 : ownerName.hashCode());
		result = prime * result + ((ownerType == null) ? 0 : ownerType.hashCode());
		result = prime * result + ((productionPool == null) ? 0 : productionPool.hashCode());
		result = prime * result + ((testingPool == null) ? 0 : testingPool.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourcePoolPermission other = (ResourcePoolPermission) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		if (ownerType == null) {
			if (other.ownerType != null)
				return false;
		} else if (!ownerType.equals(other.ownerType))
			return false;
		if (productionPool == null) {
			if (other.productionPool != null)
				return false;
		} else if (!productionPool.equals(other.productionPool))
			return false;
		if (testingPool == null) {
			if (other.testingPool != null)
				return false;
		} else if (!testingPool.equals(other.testingPool))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResourcePoolPermission [id=" + id + ", ownerType=" + ownerType + ", ownerId=" + ownerId + ", ownerName=" + ownerName
				+ ", productionPool=" + productionPool + ", testingPool=" + testingPool + "]; " + super.toString();
	}

}

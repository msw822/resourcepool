package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "PHYSICS_INSTANCES")
public class PhysicsInstances extends BaseEntity {
	private static final long serialVersionUID = 1606205145046074804L;
	// 0：未处理
	// 1：己处理
	public static final Integer SAVE_STATUS_DEAL = 1;

	public static final Integer SAVE_STATUS_UNDEAL = 0;
	@Id
	@GenericGenerator(name="systemUUID",strategy="uuid")
	@GeneratedValue(generator="systemUUID")
	@Column(name = "ID", unique=true ,insertable = true, updatable = true, nullable = false)
	private String id;

	@Column(name = "PHYSICS_NAME")
	private String physicsName;

	@Column(name = "PHYSICS_TYPE")
	private String physicsType;

	@Column(name = "IS_ALLOCATED")
	private String isAllocated;

	@Column(name = "OWNER_NAME")
	private String ownerName;
	
	@Column(name = "OWNER")
	private String owner;

	@Column(name = "DOMIAN")
	private Integer domian;
	
	@Column(name = "IS_VALID")
	private String isValid;


	@Column(name = "ORDER_ID")
	private String orderId;
	
	@Column(name = "ORDER_TYPE_ID")
	private String orderTypeId;
	
	@Column(name = "ORDER_TYPE_NAME")
	private String orderTypeName;
	
	@Column(name = "COMMENTS")
	private String comments;

	@Transient
	private String ip;
	
	@Transient
	private int hashCode = Integer.MIN_VALUE;


	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhysicsName() {
		return physicsName;
	}

	public void setPhysicsName(String physicsName) {
		this.physicsName = physicsName;
	}

	public String getPhysicsType() {
		return physicsType;
	}

	public void setPhysicsType(String physicsType) {
		this.physicsType = physicsType;
	}

	public String getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(String isAllocated) {
		this.isAllocated = isAllocated;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getDomian() {
		return domian;
	}

	public void setDomian(Integer domian) {
		this.domian = domian;
	}
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(String orderTypeId) {
		this.orderTypeId = orderTypeId;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	@Override
	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId()) return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) return false;
		if (!(obj instanceof PhysicsInstances)) return false;
		else {
			PhysicsInstances physicsInstances = (PhysicsInstances) obj;
			if (null == this.getId() || null == physicsInstances.getId()) return false;
			else return (this.getId().equals(physicsInstances.getId()));
		}
	}

	@Override
	public String toString() {
		return "PhysicsInstances [id=" + id + ", physicsName=" + physicsName +"]";
	}

}
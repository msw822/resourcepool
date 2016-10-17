package com.hp.xo.resourcepool.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


//import com.hp.core.model.BaseEntity;

@Entity(name = "T_APPROVERULE_CONFIG")
public class ApproveRule extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1954983368006338624L;

	@SequenceGenerator(sequenceName = "T_APPROVERULE_CONFIG_ID_SEQ", name = "T_APPROVERULE_CONFIG_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_APPROVERULE_CONFIG_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "PK_APPROVERULE_CONFIG_ID", nullable = false)
	private Long id = null;

	@Column(name = "WORKORDER_TYPE")
	private Integer workOrderType;

	@Column(name = "RESOURCEPOOL_ID")
	private String poolId;

	@Column(name = "APPROVE_TYPE")
	private String type;

	@Column(name = "NEG_APPROVE_DURA")
	private String approveDura;

	@Override
	public String toString() {
		return "ApproveRule [id=" + id + ", workOrderType=" + workOrderType
				+ ", poolId=" + poolId + ", type=" + type + ", approveDura="
				+ approveDura + ", firstRole=" + firstRole + ", secondRole="
				+ secondRole + ", desc=" + desc + "]";
	}

	@Column(name = "LEVEL_FIR_APPROVE_ROLE")
	private String firstRole;

	@Column(name = "LEVEL_SEC_APPROVE_ROLE")
	private String secondRole;

	@Column(name = "DESCRIPT")
	private String desc;

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

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApproveDura() {
		return approveDura;
	}

	public void setApproveDura(String approveDura) {
		this.approveDura = approveDura;
	}

	public String getFirstRole() {
		return firstRole;
	}

	public void setFirstRole(String firstRole) {
		this.firstRole = firstRole;
	}

	public String getSecondRole() {
		return secondRole;
	}

	public void setSecondRole(String secondRole) {
		this.secondRole = secondRole;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	

}

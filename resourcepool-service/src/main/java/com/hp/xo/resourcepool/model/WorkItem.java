package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "T_WORKITEM")
public class WorkItem extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8738031424910400869L;
	/*
	 * PK_WORKITEM_ID NUMBER NOT NULL, FK_WORKORDER_ID NUMBER NOT NULL, STEP INT
	 * NOT NULL, ATTRIBUTE_NAME VARCHAR2(200) NOT NULL, ATTRIBUTE_VALUE
	 * VARCHAR2(200) NOT NULL, DESCRIPT VARCHAR2(200) NOT NULL, CREATED_BY
	 * VARCHAR2(200) NOT NULL, CREATED_ON TIMESTAMP(6) DEFAULT SYSTIMESTAMP NOT
	 * NULL, MODIFIED_BY VARCHAR2(200) NULL, MODIFIED_ON
	 */
	@SequenceGenerator(sequenceName = "T_WORKITEM_ID_SEQ", name = "T_WORKITEM_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_WORKITEM_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "PK_WORKITEM_ID", nullable = false)
	private Long id = null;

	@Column(name = "FK_WORKORDER_ID")
	private Long workOrderId;

	@Column(name = "STEP")
	private Integer step;

	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;

	@Column(name = "ATTRIBUTE_VALUE")
	private String attributeValue;

	@Column(name = "DESCRIPT")
	private String descript;

	@Override
	public String toString() {
		return "WorkItem [id=" + id + ", workOrderId=" + workOrderId
				+ ", step=" + step + ", attributeName=" + attributeName
				+ ", attributeValue=" + attributeValue + ", descript="
				+ descript + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", modifiedBy=" + modifiedBy + ", modifiedOn="
				+ modifiedOn + ", jobId=" + jobId + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	
}

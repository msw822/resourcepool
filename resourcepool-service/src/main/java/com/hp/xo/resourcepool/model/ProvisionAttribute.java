package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@Entity
@Table(name = "T_PROVISION_ATTRIBUTE")
public class ProvisionAttribute extends BaseEntity implements Comparable {
	
	public static String ENUM_TYPE = "enum";
	public static String TEXT_TYPE = "text";
	public static String INT_TYPE = "int";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6281539381083217287L;

	@SequenceGenerator(sequenceName = "T_PROVISION_ATTRIBUTE_ID_SEQ", name = "T_PROVISION_ATTRIBUTE_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_PROVISION_ATTRIBUTE_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "PK_PROVISION_ATTRIBUTE_ID", nullable = false)
	private Long id = null;

	@Column(name = "WORKORDER_TYPE")
	private Integer workOrderType;

	@Column(name = "ATTRIBUTE_NAME")
	private String attributeName;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "ALIGNTO")
	private String alignto;

	@Column(name = "VISIBLE")
	private Integer visible;

	@Column(name = "EXTERNALVALUE")
	private Integer externalValue;

	@Column(name = "EDITABLE")
	private Integer editable;

	@Column(name = "SEQUENCE")
	private Integer sequence;

	@Column(name = "DESCRIPT1")
	private String displayName;

	@Column(name = "DESCRIPT2")
	private String descript;

	@Override
	public String toString() {
		return String
				.format("ProvisionAttribute [id=%s, workOrderType=%s, attributeName=%s, type=%s, alignto=%s, visible=%s, externalValue=%s, editable=%s, sequence=%s, displayName=%s, descript=%s, createdBy=%s, createdOn=%s, modifiedBy=%s, modifiedOn=%s, jobId=%s]",
						id, workOrderType, attributeName, type, alignto,
						visible, externalValue, editable, sequence,
						displayName, descript, createdBy, createdOn,
						modifiedBy, modifiedOn, jobId);
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof ProvisionAttribute){
			Integer seq = ((ProvisionAttribute)o).sequence;
			return this.sequence-seq;
		}else{
			throw new RuntimeException("非ProvisionAttribute实例，无法compare");
		}
	}

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

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlignto() {
		return alignto;
	}

	public void setAlignto(String alignto) {
		this.alignto = alignto;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getExternalValue() {
		return externalValue;
	}

	public void setExternalValue(Integer externalValue) {
		this.externalValue = externalValue;
	}

	public Integer getEditable() {
		return editable;
	}

	public void setEditable(Integer editable) {
		this.editable = editable;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	/**是否必填项*/
	@Column(name = "REQUIRED")
	private Integer required;

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}
	
	
	
}

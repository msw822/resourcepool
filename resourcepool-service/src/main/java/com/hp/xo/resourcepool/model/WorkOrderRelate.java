package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "T_WORKORDER_RELATE")
public class WorkOrderRelate {

	
	@SequenceGenerator(sequenceName = "T_WORKORDER_RELATE_ID_SEQ", name = "T_WORKORDER_RELATE_ID_SEQ_GEN")	
	@Id
	@GeneratedValue(generator = "T_WORKORDER_RELATE_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id = null;
		
	@Column(name = "WORKORDER_ID")
	private Long workOrderId;
	
	@Column(name = "RELATE_WORKORDER_ID")
	private Long relateWorkOrderId;

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

	public Long getRelateWorkOrderId() {
		return relateWorkOrderId;
	}

	public void setRelateWorkOrderId(Long relateWorkOrderId) {
		this.relateWorkOrderId = relateWorkOrderId;
	}


	
	
	


}

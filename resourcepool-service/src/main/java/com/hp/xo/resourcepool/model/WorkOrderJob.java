package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity(name = "T_WORKORDER_JOB")
public class WorkOrderJob {
	
	@SequenceGenerator(sequenceName = "T_WORKORDERJOB_ID_SEQ", name = "T_WORKORDERJOB_ID_SEQ_GEN")	
	@Id
	@GeneratedValue(generator = "T_WORKORDERJOB_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id = null;
		
	@Column(name = "WORKORDER_ID")
	private Long workOrderId;
	
	@Column(name = "JOBINSTANCE_ID")
	private String jobInstanceId;
	
	@Column(name = "JOBINSTANCE_NAME")
	private String jobInstanceName;
	
	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public String getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(String jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
		
	public String getJobInstanceName() {
		return jobInstanceName;
	}

	public void setJobInstanceName(String jobInstanceName) {
		this.jobInstanceName = jobInstanceName;
	}

}

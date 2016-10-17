package com.hp.xo.resourcepool.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "T_RESOURCE")
public class Resource extends BaseEntity implements Serializable {

	public static int RESOURCE_TYPE_X86 = 1;
	public static int RESOURCE_TYPE_HPVM = 2;

	private static final long serialVersionUID = 970181462177970752L;

	@SequenceGenerator(sequenceName = "T_RESOURCE_ID_SEQ", name = "T_RESOURCE_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_RESOURCE_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name = "t_resource_id", nullable = false)
	private Long id;

	private String cpu;// CPU数量
	private String memory;
	private int type;// 资源类型： 1：X86物理机 2：HPVM
	private int amount;// X86 或虚机台数
	private String resourcepoolid;// 资源池ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getResourcepoolid() {
		return resourcepoolid;
	}

	public void setResourcepoolid(String resourcepoolid) {
		this.resourcepoolid = resourcepoolid;
	}

}

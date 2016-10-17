package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "T_EQUIPMENTENROLL")
public class TEquipmentenroll extends BaseEntity {
	private static final long serialVersionUID = 1667901537985346320L;

	@SequenceGenerator(sequenceName = "t_equipmentenroll_id_seq", name = "t_equipmentenroll_id_seq_gen")
	@Id
	@GeneratedValue(generator = "t_equipmentenroll_id_seq_gen", strategy = GenerationType.AUTO)
	@Column(name = "PK_EQUENROLL_ID")
	private Long id;

	@Column(name = "TARGET_TYPE")
	private Short targetType;

	@Column(name = "IP")
	private String ip;

	@Column(name = "DESCRIPT")
	private String descript;

	@Column(name = "AVAILABLE")
	private Short available;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getTargetType() {
		return targetType;
	}

	public void setTargetType(Short targetType) {
		this.targetType = targetType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Short getAvailable() {
		return available;
	}

	public void setAvailable(Short available) {
		this.available = available;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((available == null) ? 0 : available.hashCode());
		result = prime * result + ((descript == null) ? 0 : descript.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((targetType == null) ? 0 : targetType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TEquipmentenroll other = (TEquipmentenroll) obj;
		if (available == null) {
			if (other.available != null)
				return false;
		} else if (!available.equals(other.available))
			return false;
		if (descript == null) {
			if (other.descript != null)
				return false;
		} else if (!descript.equals(other.descript))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (targetType == null) {
			if (other.targetType != null)
				return false;
		} else if (!targetType.equals(other.targetType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TEquipmentenroll [id=" + id + ", targetType=" + targetType + ", ip=" + ip + ", descript=" + descript
				+ ", available=" + available + "]";
	}

}
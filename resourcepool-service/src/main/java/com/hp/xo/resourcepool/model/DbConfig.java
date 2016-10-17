package com.hp.xo.resourcepool.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hp.xo.resourcepool.model.BaseEntity;

/**
 * 
 * @author Zhefang Chen
 *
 */
@Entity
@Table(name = "app_config")
public class DbConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@SequenceGenerator(sequenceName="APP_CONFIG_ID_SEQ",name="APP_CONFIG_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator="APP_CONFIG_ID_SEQ_GEN",strategy=GenerationType.SEQUENCE)
	@Column(name="pk_app_config_id")
	private Long id;
	
    @Column(name = "config_key", nullable = false, length = 255)
	private String key;
	
    @Column(name = "config_value", nullable = true, length = 255)
	private String value;
    
    @Column(name = "description", nullable = true, length = 255)
	private String description;

    public DbConfig() {
    	super();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		DbConfig other = (DbConfig) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public String toString() {
		return "DbConfig [description=" + description + ", id=" + id + ", key="
				+ key + ", value=" + value + "]";
	}
}

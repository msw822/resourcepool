package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * GlobalSetting POJO
 * @author lixinqi
 *
 */
@Entity(name = "T_GLOBALSETTING")
public class GlobalSetting extends BaseEntity {

	private static final long serialVersionUID = -7253734482720579311L;
	/**
	 * <p>自定义的数据类型：用来表示value的真实的数据类型
	 * <p>ps：枚举数据库表字段value_type
	 */
	public static final String JAVA_LANG_BOOLEAN = "java.lang.Boolean";		// 布尔类型
	public static final String JAVA_LANG_NUMBER = "java.lang.Number";		// 数字类型
	public static final String JAVA_LANG_STRING = "java.lang.String";				// 字符串类型
	public static final String ENCRYPTION = "Encryption";									// 加密类型

	public static final String true_ =  "true";
	public static final String false_ =  "false";
	public static final String password_ = "15263748";
	
	@SequenceGenerator(sequenceName = "T_GLOBALSETTING_ID_SEQ", name = "T_GLOBALSETTING_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator = "T_GLOBALSETTING_ID_SEQ_GEN", strategy = GenerationType.AUTO)
	@Column(name="PK_ID",nullable=false)
	private Long id;
	
	@Column(name="NAME")
	private String name;
	@Column(name="KEY")
	private String key;
	@Column(name="VALUE")
	private String value;
	@Column(name="VALUE_TYPE")
	private String valueType;
	@Column(name="DEFAULT")
	private String defaultValue;
	@Column(name="MODEL")
	private String model;
	@Column(name="SCOPE")
	private String scope;
	@Column(name="DESCRIPTION")
	private String description;
	
	@Override
	public String toString() {
		return "GlobalSetting [id=" + id + ", name=" + name + ", key=" + key
				+ ", value=" + value + ", valueType=" + valueType
				+ ", defaultValue=" + defaultValue + ", model=" + model
				+ ", scope=" + scope + ", description=" + description + "]";
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	
}

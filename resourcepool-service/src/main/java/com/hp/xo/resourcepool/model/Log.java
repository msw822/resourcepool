package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity(name="T_LOG")
public class Log extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3374753195272163499L;
	
	@SequenceGenerator(sequenceName="T_LOG_ID_SEQ",name="T_LOG_ID_SEQ_GEN")
	@Id
	@GeneratedValue(generator="T_LOG_ID_SEQ_GEN",strategy=GenerationType.SEQUENCE)
	@Column(name="PK_T_LOG_ID",nullable=false)
	private Long id;
	
	private String loginName;// 用户名
	private String content;// 日志内容
	private String operation;// 操作(登录、登出、添加、修改、删除、接口调用)
	private String info;// 信息
	@Column(name="result")
	private String result;// 成功/失败
	@Column(name="module")
	private String module;// 所属模块
	@Column(name="ip")
	private String ip;// 客户端IP
	private String targetId;// 日志对象id,暂时提供给策略使用
	
	public Log() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Log(String loginName, String targetId, String content, String operation, String info, String result,
			String module, String ip) {

		this.loginName = loginName;
		this.targetId = targetId;
		this.content = content;
		this.operation = operation;
		this.info = info;
		this.result = result;
		this.module = module;
		this.ip = ip;
	}

	public Log(String loginName, String content, String operation, String info, String result, String module, String ip) {

		this.loginName = loginName;
		this.content = content;
		this.operation = operation;
		this.info = info;
		this.result = result;
		this.module = module;
		this.ip = ip;
	}

	public String getIp() {

		return ip;
	}
	
	public void setIp(String ip) {

		this.ip = ip;
	}

	@Column
	public String getInfo() {

		return info;
	}

	public void setInfo(String info) {

		this.info = info;
	}

	public String getResult() {

		return result;
	}

	public void setResult(String result) {

		this.result = result;
	}

	public String getLoginName() {

		return loginName == null ? "N/A" : loginName;
	}

	public void setLoginName(String loginName) {

		this.loginName = loginName;
	}

	public String getModule() {

		return module;
	}

	public void setModule(String module) {

		this.module = module;
	}

	public String getContent() {

		return content;
	}

	public void setContent(String content) {

		this.content = content;
	}

	public String getOperation() {

		return operation;
	}

	public void setOperation(String operation) {

		this.operation = operation;
	}

	public String getTargetId() {

		return targetId;
	}

	public void setTargetId(String targetId) {

		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return "Log [id=" + id + ", loginName=" + loginName + ", content="
				+ content + ", operation=" + operation + ", info=" + info
				+ ", result=" + result + ", module=" + module + ", ip=" + ip
				+ ", targetId=" + targetId + "]";
	}
	

}

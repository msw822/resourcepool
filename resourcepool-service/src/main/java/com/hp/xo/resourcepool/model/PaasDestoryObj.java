package com.hp.xo.resourcepool.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
@Entity(name = "T_PAAS_DESTORY_OBJ")
public class PaasDestoryObj extends BaseEntity {
	
	
	/**调用失败*/
	public static int RESULT_FAIL = 1;
	/**调用成功*/
	public static int RESULT_SUCCESS = 2;
	
	/**project*/
	public static int TYPE_PROJECT = 1;
	/** vm */
	public static int TYPE_VM = 2;

	@SequenceGenerator(sequenceName = "T_PAASOBJ_ID_SEQ", name = "T_PAASOBJ_ID_SEQ_gen")
	@Id
	@GeneratedValue(generator = "T_PAASOBJ_ID_SEQ_gen", strategy = GenerationType.AUTO)
	@Column(name = "T_PASSDESTORYOBJ_ID")
	private Long id;
	
	
	@Column(name = "OBJNAME")
	private String objName;
	
	@Column(name = "OBJTYPE")
	private int objType;
	
	@Column(name = "COMMAND")
	private String command;
	
	@Column(name="PARAMSTR")
	private String paramstr;
	
	
	public String getParamstr() {
		return paramstr;
	}

	public void setParamstr(String paramstr) {
		this.paramstr = paramstr;
	}

	@Column(name = "OBJID")
	private String objId;
	
	@Column(name = "EXE_RESULT")
	private int exeResult;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public int getExeResult() {
		return exeResult;
	}

	public void setExeResult(int exeResult) {
		this.exeResult = exeResult;
	}
	
	
	@Column(name = "FAILREASON")
	private String failReason;


	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
	
	
	
	
	
}

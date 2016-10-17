package com.hp.xo.resourcepool.request;

import java.util.Date;

import com.hp.xo.resourcepool.ApiConstants;
import com.hp.xo.resourcepool.model.Parameter;

public class ListPropertyRequest extends BaseListRequest {
	@Parameter(name = ApiConstants.ID, type = FieldType.LONG, description = "List by ID.")
	private Long id;
	@Parameter(name = "type", type = FieldType.STRING, description = "")
	private String type;
	@Parameter(name = "vendor", type = FieldType.STRING, description = "")
	private String vendor;
	@Parameter(name = "model", type = FieldType.STRING, description = "")
	private String model;
	@Parameter(name = "code", type = FieldType.STRING, description = "")
	private String code;
	@Parameter(name = "start_date", type = FieldType.DATE, description = "")
	private Date start_date;
	@Parameter(name = "end_date", type = FieldType.DATE, description = "")
	private Date end_date;
	@Parameter(name = "cost", type = FieldType.STRING, description = "")
	private String cost;
	@Parameter(name = "u_bit", type = FieldType.STRING, description = "")
	private String u_bit;
	@Parameter(name = "u_high", type = FieldType.STRING, description = "")
	private String u_high;
	@Parameter(name = "position", type = FieldType.STRING, description = "")
	private String position;
	@Parameter(name = "position_desc", type = FieldType.STRING, description = "")
	private String position_desc;
	@Parameter(name = "serial_num", type = FieldType.STRING, description = "")
	private String serial_num;
	@Parameter(name = "owner", type = FieldType.STRING, description = "")
	private String owner;
	@Parameter(name = "contractInfo", type = FieldType.STRING, description = "")
	private String contractInfo;
	@Parameter(name = "service_period", type = FieldType.STRING, description = "")
	private String service_period;
	@Parameter(name = "expire_date", type = FieldType.DATE, description = "")
	private Date expire_date;
	@Parameter(name = "status", type = FieldType.STRING, description = "")
	private String status;
	@Parameter(name = "cpu_account", type = FieldType.STRING, description = "")
	private String cpu_account;
	@Parameter(name = "memory_size", type = FieldType.STRING, description = "")
	private String memory_size;
	@Parameter(name = "hdd_size", type = FieldType.STRING, description = "")
	private String hdd_size;

	@Parameter(name = "section", type = FieldType.STRING, description = "")
	private String section;

	/*
	 * private Date createdate; private Date modifydate;
	 */
	/*
	 * private String belong_to; private Integer check_point; private Integer
	 * using; private String machine_cabinet; private String os_name_chn;
	 * private String os_name_eng; private String power; private String room;
	 */
	/*
	 * public Date getCreatedate() { return createdate; }
	 * 
	 * public void setCreatedate(Date createdate) { this.createdate =
	 * createdate; }
	 * 
	 * public Date getModifydate() { return modifydate; }
	 * 
	 * public void setModifydate(Date modifydate) { this.modifydate =
	 * modifydate; }
	 */

	public String getU_bit() {
		return u_bit;
	}

	public void setU_bit(String u_bit) {
		this.u_bit = u_bit;
	}

	public String getU_high() {
		return u_high;
	}

	public void setU_high(String u_high) {
		this.u_high = u_high;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCpu_account() {
		return cpu_account;
	}

	public void setCpu_account(String cpu_account) {
		this.cpu_account = cpu_account;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getHdd_size() {
		return hdd_size;
	}

	public void setHdd_size(String hdd_size) {
		this.hdd_size = hdd_size;
	}

	/*
	 * public String getBelong_to() { return belong_to; }
	 * 
	 * public void setBelong_to(String belong_to) { this.belong_to = belong_to;
	 * } public Integer getCheck_point() { return check_point; }
	 * 
	 * public void setCheck_point(Integer check_point) { this.check_point =
	 * check_point; }
	 * 
	 * public Integer getUsing() { return using; }
	 * 
	 * public void setUsing(Integer using) { this.using = using; }
	 * 
	 * public String getMachine_cabinet() { return machine_cabinet; }
	 * 
	 * public void setMachine_cabinet(String machine_cabinet) {
	 * this.machine_cabinet = machine_cabinet; }
	 * 
	 * public String getOs_name_chn() { return os_name_chn; }
	 * 
	 * public void setOs_name_chn(String os_name_chn) { this.os_name_chn =
	 * os_name_chn; }
	 * 
	 * public String getOs_name_eng() { return os_name_eng; }
	 * 
	 * public void setOs_name_eng(String os_name_eng) { this.os_name_eng =
	 * os_name_eng; }
	 * 
	 * public String getPower() { return power; }
	 * 
	 * public void setPower(String power) { this.power = power; }
	 * 
	 * public String getRoom() { return room; }
	 * 
	 * public void setRoom(String room) { this.room = room; }
	 */

	public String getMemory_size() {
		return memory_size;
	}

	public void setMemory_size(String memory_size) {
		this.memory_size = memory_size;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * added by steven 云计算二期按照需求新增字段
	 * 
	 * @return
	 */
	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition_desc() {
		return position_desc;
	}

	public void setPosition_desc(String position_desc) {
		this.position_desc = position_desc;
	}

	public String getContractInfo() {
		return contractInfo;
	}

	public void setContractInfo(String contractInfo) {
		this.contractInfo = contractInfo;
	}

	public String getService_period() {
		return service_period;
	}

	public void setService_period(String service_period) {
		this.service_period = service_period;
	}

	public Date getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}

	public ListPropertyRequest() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	@Override
	public String toString() {
		return "ListPropertyRequest [id=" + id + ", type=" + type + ", vendor=" + vendor + ", model=" + model + ", code=" + code + ", start_date="
				+ start_date + ", end_date=" + end_date + ", cost=" + cost + ", u_bit=" + u_bit + ", u_high=" + u_high + ", position=" + position
				+ ", position_desc=" + position_desc + ", serial_num=" + serial_num + ", owner=" + owner + ", contractInfo=" + contractInfo
				+ ", service_period=" + service_period + ", expire_date=" + expire_date + ", status=" + status + ", cpu_account=" + cpu_account
				+ ", memory_size=" + memory_size + ", hdd_size=" + hdd_size + "]; " + super.toString();
	}

}

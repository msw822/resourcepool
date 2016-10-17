package com.hp.xo.resourcepool.dto;

import java.io.Serializable;
import java.util.Date;
/**
 * 物理主机地址
 * @author dujun
 *
 */
public class PhysicsHost implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	/**
	 * 名称
	 */
	private String hostName;
	/**
	 * 显示名称
	 */
	private String displayName;
	
	/**
	 * 操作系统
	 */
	private String osCaption ;
	/**
	 * 操作系统名称
	 */
	private String osVersion ;
	private String status;
	/**
	 * 最近修改者
	 */
	private String updator;
	/**
	 * 最近更新时间
	 */
	private Date updateTime;
	private String type;
	private Long cpuNumber;
	private String cpuDescription;
	/**
	 * 物理内存大小
	 */
	private Long physicsMemory;
	/**
	 * 虚拟内存大小
	 */
	private Long virtualMemory;
	/**
	 * IP地址
	 */
	private String ipAddr;
	

	/**
	 * 网络信息
	 */
	private String netAddr;
	
	
	private String cpuStatus;
	/**
	 * 磁盘文件大小
	 */
	private Long fDiskTotal;
	
	
	/**
	 * 主机位置
	 */
	private String hostAddr;
	/**
	 * 物理机逻辑状态
	 */
	private String hostTypeStatus;	
	/**
	 * 是否虚拟化
	 */
	private String isVirtual="0";
	
	/**
	 * 类型
	 */
	private String derivedFrom;
	
	private String owner;
	private String ownerName;
	
	//备用信息
	private String hold0;
	private String hold1;
	private String hold2;
	private String hold3;
	private String hold4;
	private String hold5;
	private String hold6;
	private String hold7;
	private String hold8;
	private String hold9;
	private String hold10;
	private String hold12;
	private String hold13;
	private String hold14;
	private String hold15;
	private String hold16;
	private String hold17;
	private String hold18;
	private String hold19;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Long getCpuNumber() {
		return cpuNumber;
	}
	public void setCpuNumber(Long cpuNumber) {
		this.cpuNumber = cpuNumber;
	}
	public String getOsCaption() {
		return osCaption;
	}
	public void setOsCaption(String osCaption) {
		this.osCaption = osCaption;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getCpuDescription() {
		return cpuDescription;
	}
	public void setCpuDescription(String cpuDescription) {
		this.cpuDescription = cpuDescription;
	}
	public Long getPhysicsMemory() {
		return physicsMemory;
	}
	public void setPhysicsMemory(Long physicsMemory) {
		this.physicsMemory = physicsMemory;
	}
	public Long getVirtualMemory() {
		return virtualMemory;
	}
	public void setVirtualMemory(Long virtualMemory) {
		this.virtualMemory = virtualMemory;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getCpuStatus() {
		return cpuStatus;
	}
	public void setCpuStatus(String cpuStatus) {
		this.cpuStatus = cpuStatus;
	}
	public Long getfDiskTotal() {
		return fDiskTotal;
	}
	public void setfDiskTotal(Long fDiskTotal) {
		this.fDiskTotal = fDiskTotal;
	}
	public String getNetAddr() {
		return netAddr;
	}
	public void setNetAddr(String netAddr) {
		this.netAddr = netAddr;
	}
	public String getHostAddr() {
		return hostAddr;
	}
	public void setHostAddr(String hostAddr) {
		this.hostAddr = hostAddr;
	}
	public String getIsVirtual() {
		return isVirtual;
	}
	public void setIsVirtual(String isVirtual) {
		this.isVirtual = isVirtual;
	}
	public String getDerivedFrom() {
		return derivedFrom;
	}
	public void setDerivedFrom(String derivedFrom) {
		this.derivedFrom = derivedFrom;
	}	
	public String getHostTypeStatus() {
		return hostTypeStatus;
	}
	public void setHostTypeStatus(String hostTypeStatus) {
		this.hostTypeStatus = hostTypeStatus;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getHold0() {
		return hold0;
	}
	public void setHold0(String hold0) {
		this.hold0 = hold0;
	}
	public String getHold1() {
		return hold1;
	}
	public void setHold1(String hold1) {
		this.hold1 = hold1;
	}
	public String getHold2() {
		return hold2;
	}
	public void setHold2(String hold2) {
		this.hold2 = hold2;
	}
	public String getHold3() {
		return hold3;
	}
	public void setHold3(String hold3) {
		this.hold3 = hold3;
	}
	public String getHold4() {
		return hold4;
	}
	public void setHold4(String hold4) {
		this.hold4 = hold4;
	}
	public String getHold5() {
		return hold5;
	}
	public void setHold5(String hold5) {
		this.hold5 = hold5;
	}
	public String getHold6() {
		return hold6;
	}
	public void setHold6(String hold6) {
		this.hold6 = hold6;
	}
	public String getHold7() {
		return hold7;
	}
	public void setHold7(String hold7) {
		this.hold7 = hold7;
	}
	public String getHold8() {
		return hold8;
	}
	public void setHold8(String hold8) {
		this.hold8 = hold8;
	}
	public String getHold9() {
		return hold9;
	}
	public void setHold9(String hold9) {
		this.hold9 = hold9;
	}
	public String getHold10() {
		return hold10;
	}
	public void setHold10(String hold10) {
		this.hold10 = hold10;
	}
	public String getHold12() {
		return hold12;
	}
	public void setHold12(String hold12) {
		this.hold12 = hold12;
	}
	public String getHold13() {
		return hold13;
	}
	public void setHold13(String hold13) {
		this.hold13 = hold13;
	}
	public String getHold14() {
		return hold14;
	}
	public void setHold14(String hold14) {
		this.hold14 = hold14;
	}
	public String getHold15() {
		return hold15;
	}
	public void setHold15(String hold15) {
		this.hold15 = hold15;
	}
	public String getHold16() {
		return hold16;
	}
	public void setHold16(String hold16) {
		this.hold16 = hold16;
	}
	public String getHold17() {
		return hold17;
	}
	public void setHold17(String hold17) {
		this.hold17 = hold17;
	}
	public String getHold18() {
		return hold18;
	}
	public void setHold18(String hold18) {
		this.hold18 = hold18;
	}
	public String getHold19() {
		return hold19;
	}
	public void setHold19(String hold19) {
		this.hold19 = hold19;
	}	
}

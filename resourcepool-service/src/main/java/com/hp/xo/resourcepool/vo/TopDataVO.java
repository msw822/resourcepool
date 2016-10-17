package com.hp.xo.resourcepool.vo;

import com.hp.xo.resourcepool.utils.StringUtil;


public class TopDataVO extends BaseVO implements Comparable<TopDataVO> {

	private static final long serialVersionUID = 7904386920623047277L;
	private String name;
	private String instanceName;
	private String displayName;// 虚机名称

	private String ipAddress;// IP地址
	//private String resoucePool;// 一级池
	private String zone;// 二级池
	private String host;// 主机名

	// 查询指标：CPU总量，CPU利用率，网络读取量，网络写入量，
	// 网络读取（字节），网络写入（字节），网络读取（IO），网络写入（IO）
	// [后四个指标只有KVM的虚机才能采集到]
	private String value; // 值

	// pod,cluster
	private String pod;
	private String cluster;
	private String hypervisor;
	private String target;

	private String cpuSpeed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

/*	public String getResoucePool() {
		return resoucePool;
	}

	public void setResoucePool(String resoucePool) {
		this.resoucePool = resoucePool;
	}*/

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPod() {
		return pod;
	}

	public void setPod(String pod) {
		this.pod = pod;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCpuSpeed() {
		return cpuSpeed;
	}

	public void setCpuSpeed(String cpuSpeed) {
		this.cpuSpeed = cpuSpeed;
	}

	@Override
	public int compareTo(TopDataVO o) {
		try {
			if (o != null && StringUtil.isNotEmpty(o.value) && StringUtil.isNotEmpty(this.value)) {
				if ("cpunumber".equals(target)) {
					return new Float(Integer.valueOf(o.value) * Float.valueOf(o.cpuSpeed)).compareTo(new Float(Integer.valueOf(this.value)
							* Float.valueOf(this.cpuSpeed)));
				} else if ("cpuused".equals(target)) {
					return Float.valueOf(o.value.replace("%", "")).compareTo(Float.valueOf(this.value.replace("%", "")));
				} else {
					return Integer.valueOf(o.value).compareTo(Integer.valueOf(this.value));
				}
			} else if (o != null && StringUtil.isNotEmpty(o.value) && StringUtil.isNullString(this.value)) {
				return 1;
			} else if (o != null && StringUtil.isNullString(o.value) && StringUtil.isNotEmpty(this.value)) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.err.println("TOPN统计值[" + o.value + "]与[" + this.value + "]比较时发生异常：");
			return 0;
		}
	}

}


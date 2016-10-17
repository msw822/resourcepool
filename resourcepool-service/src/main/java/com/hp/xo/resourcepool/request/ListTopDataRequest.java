package com.hp.xo.resourcepool.request;

import java.util.List;

import com.hp.xo.resourcepool.model.Parameter;

public class ListTopDataRequest extends BaseListRequest {

	@Parameter(name = "target", type = FieldType.STRING, description = "")
	private String target;

	private List<String> resoucePools;// 一级池
	private List<String> zones;// 二级池
	private List<String> pods;// pod
	private List<String> clusters;// cluster
	private List<String> hosts;// 主机

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public List<String> getResoucePools() {
		return resoucePools;
	}

	public void setResoucePools(List<String> resoucePools) {
		this.resoucePools = resoucePools;
	}

	public List<String> getZones() {
		return zones;
	}

	public void setZones(List<String> zones) {
		this.zones = zones;
	}

	public List<String> getPods() {
		return pods;
	}

	public void setPods(List<String> pods) {
		this.pods = pods;
	}

	public List<String> getClusters() {
		return clusters;
	}

	public void setClusters(List<String> clusters) {
		this.clusters = clusters;
	}

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

}

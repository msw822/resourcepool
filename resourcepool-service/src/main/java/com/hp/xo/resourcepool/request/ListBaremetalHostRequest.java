package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;

public class ListBaremetalHostRequest extends BuessionListRequest{
    @Parameter(name="resourcestate", type=FieldType.STRING, description="resourcestate")
	private String resourcestate;
    @Parameter(name="hypervisor", type=FieldType.STRING, description="hypervisor")
  	private String hypervisor;
    @Parameter(name="zoneid", type=FieldType.STRING, description="zoneid")
    private String zoneid;
	public String getResourcestate() {
		return resourcestate;
	}
	public void setResourcestate(String resourcestate) {
		this.resourcestate = resourcestate;
	}
	public String getHypervisor() {
		return hypervisor;
	}
	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}
	public String getZoneid() {
		return zoneid;
	}
	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

}

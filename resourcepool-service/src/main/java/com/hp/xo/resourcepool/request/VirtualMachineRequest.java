package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;
import com.hp.xo.resourcepool.request.BaseRequest.FieldType;

public class VirtualMachineRequest extends BuessionListRequest{
	@Parameter(name="expunge", type=FieldType.BOOLEAN, description="")
	private boolean expunge;

	@Parameter(name="hostname", type=FieldType.STRING, description="主机名")
	private String hostname;
	
	@Parameter(name="instancename", type=FieldType.STRING, description="虚拟机内部名")
	private String instancename;
	
	@Parameter(name="name", type=FieldType.STRING, description="虚拟机名")
	private String name;

	public boolean isExpunge() {
		return expunge;
	}

	public void setExpunge(boolean expunge) {
		this.expunge = expunge;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getInstancename() {
		return instancename;
	}

	public void setInstancename(String instancename) {
		this.instancename = instancename;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

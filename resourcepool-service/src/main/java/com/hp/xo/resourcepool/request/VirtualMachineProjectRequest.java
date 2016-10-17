package com.hp.xo.resourcepool.request;

import com.hp.xo.resourcepool.model.Parameter;
import com.hp.xo.resourcepool.request.BaseRequest.FieldType;

public class VirtualMachineProjectRequest extends BuessionListRequest{

	
	@Parameter(name="instancename", type=FieldType.STRING, description="虚拟机内部名")
	private String instancename;
	

	@Parameter(name="projectName", type=FieldType.STRING, description="原业务系统名称")
	private String projectName;
	
	@Parameter(name="newProjectName", type=FieldType.STRING, description="新业务系统名称")
	private String newProjectName;

	public String getInstancename() {
		return instancename;
	}

	public void setInstancename(String instancename) {
		this.instancename = instancename;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getNewProjectName() {
		return newProjectName;
	}

	public void setNewProjectName(String newProjectName) {
		this.newProjectName = newProjectName;
	}
	
	
	
}

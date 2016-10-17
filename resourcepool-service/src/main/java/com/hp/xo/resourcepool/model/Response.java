package com.hp.xo.resourcepool.model;

public class Response {
	private String type = null;
	private String sessionKey = null;
	private String responseString = null;
	private int statusCode = 0;
	private String userid = null;
	private String domainid=null;
	
	public Response() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}


	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}


	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public String toString() {
		return "Response [type=" + type + ", sessionKey=" + sessionKey
				+ ", responseString=" + responseString + ", statusCode="
				+ statusCode + ", userid=" + userid + ", domainid=" + domainid
				+ "]";
	}
	
}

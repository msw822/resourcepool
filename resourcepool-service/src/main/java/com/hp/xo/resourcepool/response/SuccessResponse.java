package com.hp.xo.resourcepool.response;

import com.google.gson.annotations.SerializedName;
import com.hp.xo.resourcepool.model.Param;

public class SuccessResponse extends BaseResponse {
	@SerializedName("success")
	@Param(description = "true if operation is executed successfully")
	private Boolean success = true;

	@SerializedName("displaytext")
	@Param(description = "any text associated with the success or failure")
	private String displayText;

	public SuccessResponse() {
		super();
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public SuccessResponse(String responseName) {
		super.setResponseName(responseName);
	}
}

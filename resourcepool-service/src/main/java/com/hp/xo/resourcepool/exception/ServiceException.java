package com.hp.xo.resourcepool.exception;
/**
 * 
 * @author Li Manxin
 *
 */
public class ServiceException extends CloudRuntimeException {
	
	public ServiceException(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4378186651089257315L;

	private String errorCode = "";
	
	private String errorMsg = "";

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public ServiceException(String errorMsg,String errorCode){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	
}

package com.hp.xo.resourcepool.exception;

public class CloudRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 924121548700590919L;

	public CloudRuntimeException() {
		super();
	}
	
	public CloudRuntimeException(String message) {
		super(message);
	}
	
	public CloudRuntimeException(String message, Exception exception) {
		super(message, exception);
	}
}

package com.hp.xo.resourcepool.exception;

public class BuessionException extends Throwable{
	private static final long serialVersionUID = 1752151198269325597L;
	public BuessionException(){
		super(); 
	}
	public BuessionException(String  message){
		super(message); 
	}
	public BuessionException(Throwable  ex){
		super(ex); 
	}
	public BuessionException(String message,Throwable  ex){
		super(message,ex); 
	}
	
}

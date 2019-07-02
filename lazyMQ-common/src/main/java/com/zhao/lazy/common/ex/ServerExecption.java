package com.zhao.lazy.common.ex;

public class ServerExecption extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServerExecption() {super();}
	
	public ServerExecption(String message , Throwable th) {super(message, th);}
	
	public ServerExecption(String message) {super(message);}
}

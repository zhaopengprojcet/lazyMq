package com.zhao.lazy.common.ex;

public class ServerExecption extends RuntimeException {

	public ServerExecption() {super();}
	
	public ServerExecption(String message , Throwable th) {super(message, th);}
	
	public ServerExecption(String message) {super(message);}
}

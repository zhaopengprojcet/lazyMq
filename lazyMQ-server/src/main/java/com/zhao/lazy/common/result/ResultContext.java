package com.zhao.lazy.common.result;

import com.alibaba.fastjson.JSON;

public class ResultContext {

	private int code;
	private String message;
	
	public ResultContext() {}
	public ResultContext(int code , String message) {
		this.code = code;
		this.message = message;
	}
	
	public String toJson() {
		return JSON.toJSONString(this);
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}

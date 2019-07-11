package com.zhao.lazy.common.result;

import com.alibaba.fastjson.JSON;

public class ResponseContext {

	private int code ;
    private String msg ;
    private int total ;
    private Object rows ;
    
    public ResponseContext() {}
    public ResponseContext(int total , Object data) {
    	this.code = total > 0 ? 0 : 1;
    	this.total = total;
    	this.rows = data;
    }
    
    public ResponseContext(int code , String msg) {
    	this.code = code;
    	this.msg = msg;
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
    
    
}

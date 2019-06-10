package com.zhao.lazy.client.em;

public enum LazySendType {

	chart(1 , "chart") ,
	group(2 , "group") ;
	
	LazySendType(int code,String value){
        this.code = code;
        this.value = value;
    }
    private String value;
    private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}

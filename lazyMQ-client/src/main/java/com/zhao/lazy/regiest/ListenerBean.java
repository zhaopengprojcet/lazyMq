package com.zhao.lazy.regiest;

import java.lang.reflect.Method;

public class ListenerBean {

	private Class cls;
	
	private Method met;
	
	private String springBeanName;
	
	public ListenerBean() {}
	public ListenerBean(Class cls , Method met , String springBeanName) {
		this.cls = cls ;
		this.met = met;
		this.springBeanName = springBeanName;
	}

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}

	public Method getMet() {
		return met;
	}

	public void setMet(Method met) {
		this.met = met;
	}
	public String getSpringBeanName() {
		return springBeanName;
	}
	public void setSpringBeanName(String springBeanName) {
		this.springBeanName = springBeanName;
	}
	
	
}

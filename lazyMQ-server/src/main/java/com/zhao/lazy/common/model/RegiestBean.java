package com.zhao.lazy.common.model;

import java.util.List;
import java.util.Map;

/**
 * 注册消息体
 * add by zhao of 2019年5月24日
 *
 * 功能描述：
 */
public class RegiestBean {

	private String userName;
	private String password;
	private Map<String, String> regiestServices; // topicGroup --> topic
	private String host;
	private int port;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Map<String, String> getRegiestServices() {
		return regiestServices;
	}
	public void setRegiestServices(Map<String, String> regiestServices) {
		this.regiestServices = regiestServices;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}

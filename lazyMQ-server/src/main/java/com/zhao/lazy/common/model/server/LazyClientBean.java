package com.zhao.lazy.common.model.server;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.model.RegiestBean;

/**
 * 注册服务客户端
 * add by zhao of 2019年5月30日
 *
 * 功能描述：
 */
public class LazyClientBean {

	private String regiestKey;
	private String host;
	private int port;
	private String userName;
	private String password;
	private long regiestTime;
	
	
	public LazyClientBean() {
	}
	
	public LazyClientBean(RegiestBean bean) {
		this.host = bean.getHost();
		this.port = bean.getPort();
		this.userName = bean.getUserName();
		this.password = bean.getPassword();
		this.regiestKey = DigestUtils.md5Hex(JSON.toJSONString(this));
		this.regiestTime = new Date().getTime();
	}
	
	
	
	public String getRegiestKey() {
		return regiestKey;
	}

	public void setRegiestKey(String regiestKey) {
		this.regiestKey = regiestKey;
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
	public long getRegiestTime() {
		return regiestTime;
	}
	public void setRegiestTime(long regiestTime) {
		this.regiestTime = regiestTime;
	}
	
	
}

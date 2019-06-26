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

	private String regiestKey; //注册key ,通过账户和密码验证后为此次注册生成的唯一key , 发送mq到服务器需要使用key作为身份标识
	private String host;//域名   http://192.168.0.1
	private int port;//端口号
	private String userName;//账号
	private String password;//密码
	private long regiestTime;//注册时间 
	
	
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

package com.zhao.lazy.common.model;

/**
 * 请求消息体
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class MessageBean {

	private String regiestKey;
	private String body;
	private String topicName;
	private long sendTime;
	private int sendType;//1单点 2广播
	
	
	public String getRegiestKey() {
		return regiestKey;
	}
	public void setRegiestKey(String regiestKey) {
		this.regiestKey = regiestKey;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	
}

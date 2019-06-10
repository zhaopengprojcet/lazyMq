package com.zhao.lazy.regiest;

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
	
	public MessageBean(String body , String topicName , int sendType) {
		this.body = body;
		this.topicName = topicName;
		this.sendType = (sendType == 1 || sendType == 2) ? sendType : 1;
		this.sendTime = System.currentTimeMillis();
	}



	public String getRegiestKey() {
		return regiestKey;
	}
	protected void setRegiestKey(String regiestKey) {
		this.regiestKey = regiestKey;
	}
	public String getBody() {
		return body;
	}
	protected void setBody(String body) {
		this.body = body;
	}
	public String getTopicName() {
		return topicName;
	}
	protected void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public long getSendTime() {
		return sendTime;
	}
	protected void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public int getSendType() {
		return sendType;
	}
	protected void setSendType(int sendType) {
		this.sendType = sendType;
	}
	
}

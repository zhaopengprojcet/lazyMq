package com.zhao.lazy.common.model.server;

public class LazyMqDiscardedBean {

	private String messageId;
	private String body;
	private String topicName;
	private long sendTime;
	private long createTime;
	private long inDisTime;
	private int sendType;
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getInDisTime() {
		return inDisTime;
	}
	public void setInDisTime(long inDisTime) {
		this.inDisTime = inDisTime;
	}
	
	public int getSendType() {
		return sendType;
	}
	public void setSendType(int sendType) {
		this.sendType = sendType;
	}
	@Override
	public String toString() {
		return messageId;
	}
	
}

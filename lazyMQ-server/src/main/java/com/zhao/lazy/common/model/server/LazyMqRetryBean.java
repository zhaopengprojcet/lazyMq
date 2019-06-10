package com.zhao.lazy.common.model.server;

/**
 * MQ存储消息实体
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class LazyMqRetryBean {

	private String messageId;
	private String body;
	private String topicName;
	private long sendTime;
	private long createTime;
	private long lastSendTime;
	private long nextSendTime;
	private int sendCount;
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
	public long getLastSendTime() {
		return lastSendTime;
	}
	public void setLastSendTime(long lastSendTime) {
		this.lastSendTime = lastSendTime;
	}
	public long getNextSendTime() {
		return nextSendTime;
	}
	public void setNextSendTime(long nextSendTime) {
		this.nextSendTime = nextSendTime;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
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

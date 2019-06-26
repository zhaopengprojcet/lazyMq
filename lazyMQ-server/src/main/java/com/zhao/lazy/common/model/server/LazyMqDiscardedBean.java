package com.zhao.lazy.common.model.server;

public class LazyMqDiscardedBean {

	private String messageId;//消息唯一编号
	private String body;//消息体
	private String topicName;
	private String groupName;
	private long sendTime;//客户端推送时间
	private long inDisTime;//进入死信时间
	private int sendType;//推送类型
	private String requestUrl;//死信消息推送地址 ,同一个消息可能推送到多个服务，某一个服务推送进入死信后记录对应请求
	
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	@Override
	public String toString() {
		return messageId;
	}
	
}

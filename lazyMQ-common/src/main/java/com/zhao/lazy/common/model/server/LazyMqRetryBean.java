package com.zhao.lazy.common.model.server;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.util.RetryTimeUtil;

/**
 * MQ存储消息实体
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class LazyMqRetryBean {

	private String messageId;//消息唯一编号
	private String body;//内容
	private String topicName;
	private String groupName;
	private long sendTime;//客户端发送时间
	private long createTime;//重试队列接收时间
	private long lastSendTime;//最后一次推送时间
	private long nextSendTime;//下一次推送时间
	private long thisRetryTime;//当前重试间隔时间
	private int sendCount;//发送次数
	private int sendType;//发送类型
	private String requestUrl;//发送地址
	
	public LazyMqRetryBean loadMqBean(LazyMqBean message , String groupName , String requestUrl) {
		this.body = message.getBody();
		this.topicName = message.getTopicName();
		this.groupName = groupName;
		this.sendTime = message.getSendTime();
		this.sendType = message.getSendType();
		this.requestUrl = requestUrl;
		this.messageId = message.getMessageId();
		this.createTime = System.currentTimeMillis();
		this.lastSendTime = 0l;
		this.nextSendTime = RetryTimeUtil.getNextTime(createTime, 0);
		this.thisRetryTime = RetryTimeUtil.getNextTimeOff(0);
		this.sendCount = 1;
		return this;
	}
	
	public LazyMqRetryBean loadRetryBean(LazyMqRetryBean message) {
		this.body = message.getBody();
		this.topicName = message.getTopicName();
		this.groupName = message.getGroupName();
		this.sendTime = message.getSendTime();
		this.sendType = message.getSendType();
		this.requestUrl = message.getRequestUrl();
		this.messageId = message.getMessageId();
		this.createTime = message.getCreateTime();
		this.lastSendTime = System.currentTimeMillis();
		this.nextSendTime = RetryTimeUtil.getNextTime(this.lastSendTime, message.getThisRetryTime());
		this.thisRetryTime = RetryTimeUtil.getNextTimeOff(message.getThisRetryTime());
		this.sendCount = message.getSendCount() + 1;
		return this;
	}
	
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
	public long getThisRetryTime() {
		return thisRetryTime;
	}
	public void setThisRetryTime(long thisRetryTime) {
		this.thisRetryTime = thisRetryTime;
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

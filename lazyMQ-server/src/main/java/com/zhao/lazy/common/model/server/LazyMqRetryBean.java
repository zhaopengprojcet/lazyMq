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

	private String messageId;
	private String body;
	private String topicName;
	private String groupName;
	private long sendTime;
	private long createTime;
	private long lastSendTime;
	private long nextSendTime;
	private int sendCount;
	private int sendType;
	private String requestUrl;
	
	public LazyMqRetryBean loadMqBean(LazyMqBean message , String groupName , String requestUrl) {
		this.body = message.getBody();
		this.topicName = message.getTopicName();
		this.groupName = groupName;
		this.sendTime = message.getSendTime();
		this.sendType = message.getSendType();
		this.requestUrl = requestUrl;
		this.messageId = DigestUtils.md5Hex(JSON.toJSONString(this));
		this.createTime = System.currentTimeMillis();
		this.lastSendTime = 0l;
		this.nextSendTime = RetryTimeUtil.getNextTime(createTime, 0);
		this.sendCount = 1;
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

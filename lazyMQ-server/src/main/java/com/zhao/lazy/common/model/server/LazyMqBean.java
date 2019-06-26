package com.zhao.lazy.common.model.server;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.util.RandomUtils;

/**
 * MQ存储消息实体
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class LazyMqBean {

	private String messageId;//消息唯一编号
	private String body;//消息内容
	private String topicName;
	private long sendTime;//客户端发送时间
	private int sendType;//推送类型
	private long createTime;//服务器收取时间
	
	
	public LazyMqBean() {
	}
	
	
	public LazyMqBean(MessageBean bean) {
		this.body = bean.getBody();
		this.topicName = bean.getTopicName();
		this.sendTime = bean.getSendTime();
		this.sendType = bean.getSendType();
		this.messageId = DigestUtils.md5Hex(JSON.toJSONString(this));
		this.createTime = new Date().getTime();
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

package com.zhao.lazy.service.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.service.MqService;

/**
 * 同步消息处理
 * 保证消息接收的完整性
 * add by zhao of 2019年6月26日
 *
 * 功能描述：
 */
@ConditionalOnProperty(name="mq.db.auto" ,havingValue = "synchro")
@Service("mqService")
public class MqSynchroServiceImpl extends BaseMqService implements MqService {

	@Override
	public void startDBinit() {
		loadSynchroThread();
	}

	@Transactional
	@Override
	public ResultContext clientRegiest(RegiestBean regiest) {
		LazyClientBean client = new LazyClientBean(regiest);
		int insert = sqlUtil.insertLazyClientBean(regiest, client);
		if(insert > 0) {
			return ServerAttributeUtil.addRegiestToList(regiest.getRegiestServices(), client);
		}
		return new ResultContext(-1, "insert client error");
	}

	@Transactional
	@Override
	public ResultContext pushMessage(MessageBean message) {
		LazyMqBean lmb = new LazyMqBean(message);
		int insert = sqlUtil.insertLazyMqBean(lmb);
		if(insert > 0) {
			return ServerAttributeUtil.addMessageToWaitSendQueue(lmb) ? new ResultContext(0, lmb.getMessageId()) : new ResultContext(-1, "duplication");
		}
		return new ResultContext(-1, "duplication");
	}

	
}

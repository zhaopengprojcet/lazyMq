package com.zhao.lazy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhao.lazy.common.ex.ServerExecption;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqliteUtil;
import com.zhao.lazy.service.MqService;

@ConditionalOnProperty(name="mq.db.auto" ,havingValue = "synchro")
@Service("mqService")
public class MqSynchroServiceImpl implements MqService {

	@Autowired
	private SqliteUtil sqliteUtil;
	@Autowired
	private ServerAttributeUtil serverAttributeUtil;
	
	
	
	@Override
	public void startDBinit() {
	}

	@Transactional
	@Override
	public ResultContext clientRegiest(RegiestBean regiest) {
		LazyClientBean client = new LazyClientBean(regiest);
		int insert = sqliteUtil.insertLazyClientBean(regiest, client);
		if(insert > 0) {
			return serverAttributeUtil.addRegiestToList(regiest.getRegiestServices(), client);
		}
		return new ResultContext(-1, "insert client error");
	}

	@Transactional
	@Override
	public ResultContext pushMessage(MessageBean message) {
		LazyMqBean lmb = new LazyMqBean(message);
		int insert = sqliteUtil.insertLazyMqBean(lmb);
		if(insert > 0) {
			return serverAttributeUtil.addMessageToWaitSendQueue(lmb) ? new ResultContext(0, lmb.getMessageId()) : new ResultContext(-1, "duplication");
		}
		return new ResultContext(-1, "duplication");
	}

	
}

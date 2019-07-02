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
 * 异步消息处理
 * 保证执行的效率
 * add by zhao of 2019年6月26日
 *
 * 功能描述：
 */
@ConditionalOnProperty(name="mq.db.auto" ,havingValue = "asynchronous")
@Service("mqService")
public class MqAsynchronousServiceImpl extends BaseMqService implements MqService {

	@Override
	public void startDBinit() {
		loadAsynchronousThread();
	}
	
	/**
	 * 注册客户端属于低并发
	 * 不需要将数据入库置为队列处理
	 */
	@Transactional
	@Override
	public ResultContext clientRegiest(RegiestBean regiest) {
		LazyClientBean client = new LazyClientBean(regiest);
		int insert = sqlUtil.insertLazyClientBean(regiest, client);
		if(insert > 0) {
			return ServerAttributeUtil.addRegiestToList(regiest.getRegiestServices(), client);
		}
		return new ResultContext(-1, "duplication");
	}

	/**
	 * 队列处理入库
	 */
	@Override
	public ResultContext pushMessage(MessageBean message) {
		LazyMqBean lmb = new LazyMqBean(message);
		return ServerAttributeUtil.addMessageToWaitSendQueueAndDBqueue(lmb) ? new ResultContext(0, lmb.getMessageId()) : new ResultContext(-1, "duplication");
	}
}

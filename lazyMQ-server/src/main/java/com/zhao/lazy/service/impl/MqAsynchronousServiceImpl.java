package com.zhao.lazy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONArray;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.LogUtil;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqliteUtil;
import com.zhao.lazy.service.MqService;


@ConditionalOnProperty(name="mq.db.auto" ,havingValue = "asynchronous")
@Service("mqService")
public class MqAsynchronousServiceImpl implements MqService {

	@Autowired
	private SqliteUtil sqliteUtil;
	@Autowired
	private ServerAttributeUtil serverAttributeUtil;
	
	private static Thread dbThread;
	
	@Override
	public void startDBinit() {
		if(dbThread == null) {
			dbThread = new Thread() {
				@Override
				public void run() {
					try {
						while(true) {
							List<LazyMqBean> beans = serverAttributeUtil.offWaitSendQueueAndDBqueue(1500);
							
							if(!CollectionUtils.isEmpty(beans)) {
								sqliteUtil.batchInsertLazyMqBean(beans);
							}
							sleep(2000);
						}
					} catch (Exception e) {
						e.printStackTrace();
						LogUtil.error("异步消息导入DB异常", e);
					}
				}
			};
		}
		dbThread.start();
	}
	
	
	@Transactional
	@Override
	public ResultContext clientRegiest(RegiestBean regiest) {
		LazyClientBean client = new LazyClientBean(regiest);
		int insert = sqliteUtil.insertLazyClientBean(regiest, client);
		if(insert > 0) {
			return serverAttributeUtil.addRegiestToList(regiest.getRegiestServices(), client);
		}
		return new ResultContext(-1, "duplication");
	}

	@Transactional
	@Override
	public ResultContext pushMessage(MessageBean message) {
		LazyMqBean lmb = new LazyMqBean(message);
		return serverAttributeUtil.addMessageToWaitSendQueueAndDBqueue(lmb) ? new ResultContext(0, lmb.getMessageId()) : new ResultContext(-1, "duplication");
	}
}

package com.zhao.lazy.service.impl;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.model.server.LazyMqDiscardedBean;
import com.zhao.lazy.common.model.server.LazyMqRetryBean;
import com.zhao.lazy.common.util.LogUtil;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqliteUtil;
import com.zhao.lazy.common.util.ThreadSysUtil;

@Component
public class BaseMqService {
	@Autowired
	protected SqliteUtil sqliteUtil;
	//等待队列
	private static Thread waitDbThread;
	//重试队列
	private static Thread retryThread;
	//死信队列
	private static Thread discardedThread;
	/**
	 * 设置为同步时需要开启的任务
	* add by zhao of 2019年6月26日
	 */
	public void loadSynchroThread() {
		loadretryDbThread();
		loadDiscardedDbThread();
	}
	
	/**
	 * 设置为异步时需要开启的任务
	* add by zhao of 2019年6月26日
	 */
	public void loadAsynchronousThread() {
		loadWaitDbThread();
		loadretryDbThread();
		loadDiscardedDbThread();
	}
	
	/**
	 * 启动等待队列入库线程
	* add by zhao of 2019年6月26日
	 */
	private void loadWaitDbThread() {
		if(waitDbThread == null) {
			waitDbThread = new Thread() {
				@Override
				public void run() {
					
						while(true) {
							List<LazyMqBean> beans = new ArrayList<LazyMqBean>();
							try {
								beans = ServerAttributeUtil.offWaitSendQueueAndDBqueue(500);
								
								if(!CollectionUtils.isEmpty(beans)) {
									try {
										sqliteUtil.batchInsertLazyMqBean(beans);
									} catch (Exception e) {
										for (LazyMqBean lazyMqBean : beans) {
											ServerAttributeUtil.pushWaitSendQueueAndDBqueue(lazyMqBean);
										}
										throw new ServerException("insert wait db sql error", e);
									}
								}
								sleep(2000);
							} catch (Exception e) {
								e.printStackTrace();
								LogUtil.error("mq inster wait db error", e);
							}
						}
				}
			};
			ThreadSysUtil.execute(waitDbThread);
		}
	}
	
	/**
	 * 启动重试队列入库线程
	* add by zhao of 2019年6月26日
	 */
	private void loadretryDbThread() {
		if(retryThread == null) {
			retryThread = new Thread() {
				@Override
				public void run() {
					
						while(true) {
							List<LazyMqRetryBean> beans = new ArrayList<LazyMqRetryBean>();
							try {
								beans = ServerAttributeUtil.offRetrySendQueueAndDBqueue(500);
								
								if(!CollectionUtils.isEmpty(beans)) {
									try {
										offWaitToRetry(beans);
									} catch (Exception e) {
										for (LazyMqRetryBean lazyRetryMqBean : beans) {
											ServerAttributeUtil.pushRetrySendQueueAndDBqueue(lazyRetryMqBean);
										}
										throw new ServerException("insert retry db sql error", e);
									}
								}
								sleep(2000);
							} catch (Exception e) {
								e.printStackTrace();
								LogUtil.error("mq inster retry db error", e);
							}
						}
				}
			};
			ThreadSysUtil.execute(retryThread);
		}
	}
	
	/**
	 * 启动死信队列入库线程
	* add by zhao of 2019年6月26日
	 */
	private void loadDiscardedDbThread() {
		if(discardedThread == null) {
			discardedThread = new Thread() {
				@Override
				public void run() {
					
						while(true) {
							List<LazyMqDiscardedBean> beans = new ArrayList<LazyMqDiscardedBean>();
							try {
								beans = ServerAttributeUtil.popDiscardedQueue(500);
								
								if(!CollectionUtils.isEmpty(beans)) {
									try {
										offRetryToDis(beans);
									} catch (Exception e) {
										for (LazyMqDiscardedBean lazyMqDiscardedBean : beans) {
											ServerAttributeUtil.pushDiscardedQueue(lazyMqDiscardedBean);
										}
										throw new ServerException("insert discarded db sql error", e);
									}
								}
								sleep(2000);
							} catch (Exception e) {
								e.printStackTrace();
								LogUtil.error("mq inster discarded db error", e);
							}
						}
				}
			};
			ThreadSysUtil.execute(retryThread);
		}
	}
	
	/**
	 * 拷贝到重试
	 * 删除等待队列表数据
	* add by zhao of 2019年6月26日
	* 
	* 声明函数包裹2个独立事务的更新函数为一个事务
	 */
	@Transactional
	private void offWaitToRetry(List<LazyMqRetryBean> beans) {
		int update = sqliteUtil.insertLazyMqRetryBean(beans);
		if(update == beans.size()) {
			List<String> messageIds = beans.stream().map(LazyMqRetryBean::getMessageId).collect(Collectors.toList());
			sqliteUtil.deleteLazyMqBean(messageIds);
		}
	}
	
	/**
	 * 死信入库与删除重试
	* add by zhao of 2019年6月26日
	* 
	* 声明函数包裹2个独立事务的更新函数为一个事务
	 */
	@Transactional
	private void offRetryToDis(List<LazyMqDiscardedBean> beans) {
		int update = sqliteUtil.insertLazyMqDiscardedBean(beans);
		if(update == beans.size()) {
			List<String> messageIds = beans.stream().map(LazyMqDiscardedBean::getMessageId).collect(Collectors.toList());
			sqliteUtil.deleteRetryMqBean(messageIds);
		}
	}
}

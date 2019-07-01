package com.zhao.lazy.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.model.server.LazyMqDiscardedBean;
import com.zhao.lazy.common.model.server.LazyMqRetryBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.msg.SendMsgThread;
import com.zhao.lazy.common.util.queue.ZFifoQueue;
import com.zhao.lazy.common.util.queue.impl.CacheQueue;
import com.zhao.lazy.common.util.queue.impl.CacheQueueForMq;

/**
 * 服务启动后缓存的全局参数
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class ServerAttributeUtil {

	private static Log log = LogFactory.getLog("");
	
	public ServerAttributeUtil() {
	}
	
	/**
	 * 请求的账号和密码
	 */
	private static ConcurrentHashMap<String, String> lazyUser = new ConcurrentHashMap<String, String>();
	
	/**
	 * 已注册key
	 */
	private static Set<String> lazyRegiestKey = Collections.newSetFromMap(new ConcurrentHashMap<String , Boolean>());
	
	/**
	 * 等待发送DB队列    使用异步处理时启用
	 * queue
	 */
	private static ZFifoQueue<LazyMqBean> waitSendDBQueue = new CacheQueueForMq<LazyMqBean>();
	/**
	 * 等待发送队列
	 * topic ->  group ->queue
	 */
	private static ConcurrentHashMap<String ,ConcurrentHashMap<String, ZFifoQueue<LazyMqBean>>> waitSendQueue = new ConcurrentHashMap<String,  ConcurrentHashMap<String,ZFifoQueue<LazyMqBean>>>();
	/**
	 * 重试发送DB队列
	 * queue
	 */
	private static ZFifoQueue<LazyMqRetryBean> retrySendDBQueue = new CacheQueueForMq<LazyMqRetryBean>();
	/**
	 * 重试发送队列
	 */
	private static ConcurrentHashMap<String ,ConcurrentHashMap<String, ZFifoQueue<LazyMqRetryBean>>> retrySendQueue = new ConcurrentHashMap<String ,ConcurrentHashMap<String, ZFifoQueue<LazyMqRetryBean>>>();
	/**
	 * 死信队列
	 */
	private static ZFifoQueue<LazyMqDiscardedBean> discardedQueue = new CacheQueueForMq<LazyMqDiscardedBean>();
	/**
	 * 成功发送message   id队列
	 */
	private static ConcurrentHashMap<String, CacheQueue<String>> successQueue = new ConcurrentHashMap<String ,CacheQueue<String>>();
	/**
	 * 客户端注册内容
	 * topic->[groupName->[client]]
	 */
	public static ConcurrentHashMap<String, ConcurrentHashMap<String, List<LazyClientBean>>> topicGroupCache = new ConcurrentHashMap<String, ConcurrentHashMap<String,List<LazyClientBean>>>();
	
	/**
	 * 初始化
	* add by zhao of 2019年5月23日
	 */
	public void init(SqliteUtil sqliteUtil) {
		//请求账户
		List<Map<String, Object>> users = sqliteUtil.queryReqiestUser();
		if(!CollectionUtils.isEmpty(users)) {
			for (Map<String, Object> map : users) {
				lazyUser.put(map.get("username").toString(), map.get("password").toString());
			}
			log.info("load lazy users ["+lazyUser+"]");
		}
		
		//成功通道
		successQueue.put("waitSendQueue", new CacheQueue<String>());
		successQueue.put("retrySendQueue", new CacheQueue<String>());
		
		//成功通道处理线程
		ThreadSysUtil.execute(new SendMsgThread().new SuccessQueueThread("waitSendQueue"));
		ThreadSysUtil.execute(new SendMsgThread().new SuccessQueueThread("retrySendQueue"));
	}
	
	
	//-------------------------------------  等待队列
	/**
	 * 加入指定消息待发送队列
	* add by zhao of 2019年5月24日
	 */
	public static boolean addMessageToWaitSendQueue(LazyMqBean message , String groupName) {
		log.info("push mq to waitSendQueue \n" + JSON.toJSONString(message));
		return waitSendQueue.get(message.getTopicName()).get(groupName).flush(message);
	}
	/**
	 * 加入消息待发送队列
	* add by zhao of 2019年5月24日
	 */
	public static boolean addMessageToWaitSendQueue(LazyMqBean message) {
		log.info("push mq to waitSendQueue \n" + JSON.toJSONString(message));
		for(String key : waitSendQueue.get(message.getTopicName()).keySet()) {
			waitSendQueue.get(message.getTopicName()).get(key).flush(message);
		}
		return true;
	}
	
	/**
	 * 加入消息待发送队列 与拷贝DB 队列
	* add by zhao of 2019年5月24日
	 */
	public static boolean addMessageToWaitSendQueueAndDBqueue(LazyMqBean message) {
		if(addMessageToWaitSendQueue(message)) {
			return pushWaitSendQueueAndDBqueue(message);
		}
		return false;
	}
	
	/**
	 * 等待 出队列
	* add by zhao of 2019年6月10日
	 */
	public static LazyMqBean offWaitSendQueue(String topicName , String group) {
		return waitSendQueue.get(topicName).get(group).pop();
	}
	
	/**
	 *  等待 长度
	* add by zhao of 2019年6月10日
	 */
	public static long offWaitSendQueueSize(String topicName , String group) {
		return waitSendQueue.get(topicName).get(group).size();
	}
	
	/**
	 * tipic 存在
	* add by zhao of 2019年6月17日
	 */
	public static boolean hasTopic(String topic) {
		return waitSendQueue.containsKey(topic);
	}
	
	/**
	 * 取出等待拷贝队列
	* add by zhao of 2019年6月6日
	 */
	public static List<LazyMqBean> offWaitSendQueueAndDBqueue(int size) {
		return waitSendDBQueue.popList(size);
	}
	
	/**
	 * 放入等待拷贝队列
	* add by zhao of 2019年6月6日
	 */
	public static boolean pushWaitSendQueueAndDBqueue(LazyMqBean message) {
		return waitSendDBQueue.flush(message);
	}
	
	//-------------------------------------  重发队列
	
	/**
	 *  重发 长度
	* add by zhao of 2019年6月10日
	 */
	public static long offRetrySendQueueSize(String topicName , String group) {
		return retrySendQueue.get(topicName).get(group).size();
	}
	
	/**
	 * 放入重发队列
	* add by zhao of 2019年6月17日
	 */
	public static boolean pushRetrySendQueue(LazyMqBean message , String groupName , String requestUrl) {
		LazyMqRetryBean rb = new LazyMqRetryBean().loadMqBean(message, groupName, requestUrl);
		retrySendDBQueue.flush(rb);
		return retrySendQueue.get(message.getTopicName()).get(groupName).flush(rb);
	}
	
	/**
	 * 放入重发队列
	* add by zhao of 2019年6月17日
	 */
	public static boolean pushRetrySendQueue(LazyMqRetryBean message) {
		return retrySendQueue.get(message.getTopicName()).get(message.getGroupName()).flush(message);
	}
	
	/**
	 * 放入重试拷贝队列
	* add by zhao of 2019年6月6日
	 */
	public static boolean pushRetrySendQueueAndDBqueue(LazyMqRetryBean message) {
		return retrySendDBQueue.flush(message);
	}
	
	/**
	 * 重试 出队列
	* add by zhao of 2019年6月10日
	 */
	public static LazyMqRetryBean offRetrySendQueue(String topicName , String group) {
		return retrySendQueue.get(topicName).get(group).pop();
	}
	
	/**
	 * 取出重发拷贝队列
	* add by zhao of 2019年6月6日
	 */
	public static List<LazyMqRetryBean> offRetrySendQueueAndDBqueue(int size) {
		return retrySendDBQueue.popList(size);
	}
	
	//-------------------------------------  成功队列
	public static boolean pushSuccessQueue(String queueName , String messageId) {
		return successQueue.get(queueName).flush(messageId);
	}
	
	/**
	 *  批量出队列
	* add by zhao of 2019年6月17日
	 */
	public static List<String> popSuccessQueue(String queueName , int size) {
		return successQueue.get(queueName).popList(size);
	}
	
	//-------------------------------------  死信队列
	/**
	 * 放入死信队列
	* add by zhao of 2019年6月17日
	 */
	public static boolean pushDiscardedQueue(LazyMqRetryBean message) {
		LazyMqDiscardedBean rb = new LazyMqDiscardedBean().loadMqBean(message);
		return discardedQueue.flush(rb);
	}
	
	/**
	 * 放入死信队列
	* add by zhao of 2019年6月6日
	 */
	public static boolean pushDiscardedQueue(LazyMqDiscardedBean message) {
		return discardedQueue.flush(message);
	}
	
	/**
	 *  批量出队列
	* add by zhao of 2019年6月17日
	 */
	public static List<LazyMqDiscardedBean> popDiscardedQueue(int size) {
		return discardedQueue.popList(size);
	}
	
	/**
	 * 加入注册客户端
	* add by zhao of 2019年5月30日
	 */
	public static ResultContext addRegiestToList(Map<String, String> topicGroup , LazyClientBean bean) {
		if(topicGroup == null || topicGroup.isEmpty()) {
			return new ResultContext(-1, "must set topic");
		}
		
		synchronized(topicGroupCache) {
			if(lazyRegiestKey.contains(bean.getRegiestKey())) { //提供界面做已注册服务的主动注销处理 , 心跳监测 被动注销处理
				return new ResultContext(-1, "duplicate registration");
			}
			for(Entry<String, String> entry :topicGroup.entrySet()) {
				if(!topicGroupCache.containsKey(entry.getKey()) || !topicGroupCache.get(entry.getKey()).containsKey(entry.getValue())) {
					ThreadSysUtil.execute(new SendMsgThread().new WaitSendQueueThread(entry.getKey() ,entry.getValue())); //开启等待、重试、死信队列处理线程
					ThreadSysUtil.execute(new SendMsgThread().new RetrySendQueueThread(entry.getKey() ,entry.getValue()));
					//ThreadSysUtil.execute(new SendMsgThread().new DiscardedQueueThread(entry.getKey() ,entry.getValue()));
				}
				if(!waitSendQueue.containsKey(entry.getValue())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqBean>> hash = new ConcurrentHashMap<String, ZFifoQueue<LazyMqBean>>();
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqBean>());
					waitSendQueue.put(entry.getValue(), hash);
				}
				else if(!waitSendQueue.get(entry.getValue()).containsKey(entry.getKey())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqBean>> hash = waitSendQueue.get(entry.getValue());
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqBean>());
					waitSendQueue.put(entry.getValue(), hash);
				}
				if(!retrySendQueue.containsKey(entry.getValue())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqRetryBean>> hash = new ConcurrentHashMap<String, ZFifoQueue<LazyMqRetryBean>>();
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqRetryBean>());
					retrySendQueue.put(entry.getValue(), hash);
				}
				else if(!retrySendQueue.get(entry.getValue()).containsKey(entry.getKey())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqRetryBean>> hash = retrySendQueue.get(entry.getValue());
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqRetryBean>());
					retrySendQueue.put(entry.getValue(), hash);
				}
				/*
				if(!discardedQueue.containsKey(entry.getValue())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqDiscardedBean>> hash = new ConcurrentHashMap<String, ZFifoQueue<LazyMqDiscardedBean>>();
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqDiscardedBean>());
					discardedQueue.put(entry.getValue(), hash);
				}
				else if(!discardedQueue.get(entry.getValue()).containsKey(entry.getKey())) {
					ConcurrentHashMap<String, ZFifoQueue<LazyMqDiscardedBean>> hash = discardedQueue.get(entry.getValue());
					hash.put(entry.getKey(), new CacheQueueForMq<LazyMqDiscardedBean>());
					discardedQueue.put(entry.getValue(), hash);
				}
				*/
			}
			
			lazyRegiestKey.add(bean.getRegiestKey());
			for (Map.Entry<String, String> entry : topicGroup.entrySet()) {
				ConcurrentHashMap<String, List<LazyClientBean>> group = null;
			  if(topicGroupCache.containsKey(entry.getKey())) {
				  group = topicGroupCache.get(entry.getKey());
			  }
			  else {
				  group = new ConcurrentHashMap<String, List<LazyClientBean>>();
			  }
			  List<LazyClientBean> beans = null;
			  if(group.containsKey(entry.getValue())) {
				  beans = group.get(entry.getValue());
			  }
			  else {
				  beans = new ArrayList<LazyClientBean>();
			  }
			  LazyClientBean lcb = new LazyClientBean();
			  BeanUtils.copyProperties(bean, lcb);
			  beans.add(lcb);
			  group.put(entry.getValue(), beans);
			  topicGroupCache.put(entry.getKey(), group); 
			}
			return new ResultContext(0, bean.getRegiestKey());
		}
	}
	
	/**
	 * 效验请求账号合法性
	* add by zhao of 2019年5月23日
	 */
	public static boolean lazyUserCheck(String user , String password) {
		if(StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
			return false;
		}
 		if(!lazyUser.containsKey(user)) {
			return false;
		}
		if(!lazyUser.get(user).equals(DigestUtils.md5Hex(password))) {
			return false;
		}
		return true;
	}
	
	/**
	 * 注册键合法性
	* add by zhao of 2019年5月30日
	 */
	public static boolean lazyRegiestKeyCheck(String regiestKey) {
		if(StringUtils.isBlank(regiestKey)) {
			return false;
		}
 		if(!lazyRegiestKey.contains(regiestKey)) {
			return false;
		}
		return true;
	}
}

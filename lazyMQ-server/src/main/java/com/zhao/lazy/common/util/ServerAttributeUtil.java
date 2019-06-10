package com.zhao.lazy.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.model.server.LazyMqDiscardedBean;
import com.zhao.lazy.common.model.server.LazyMqRetryBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.queue.ZFifoQueue;
import com.zhao.lazy.common.util.queue.impl.CacheQueueForMq;

/**
 * 服务启动后缓存的全局参数
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
@Component("serverAttributeUtil")
public class ServerAttributeUtil {

	private Log log = LogFactory.getLog("");
	
	public ServerAttributeUtil() {
	}
	
	@Autowired
	private SqliteUtil sqliteUtil;
	/**
	 * 请求的账号和密码
	 */
	private static Map<String, String> lazyUser = new HashMap<String, String>();
	
	/**
	 * 已注册key
	 */
	private static Set<String> lazyRegiestKey = new HashSet<String>();
	
	/**
	 * 等待发送DB队列
	 * topicName --> queue
	 */
	private static ZFifoQueue<LazyMqBean> waitSendDBQueue = new CacheQueueForMq<LazyMqBean>();
	/**
	 * 等待发送队列
	 * topicName --> queue
	 */
	private static ZFifoQueue<LazyMqBean> waitSendQueue = new CacheQueueForMq<LazyMqBean>();
	/**
	 * 重试发送队列
	 */
	private static ZFifoQueue<LazyMqRetryBean> retrySendQueue = new CacheQueueForMq<LazyMqRetryBean>();
	/**
	 * 死信队列
	 */
	private static ZFifoQueue<LazyMqDiscardedBean> discardedQueue = new CacheQueueForMq<LazyMqDiscardedBean>();
	
	/**
	 * 客户端注册内容
	 * topic->[groupName->[client]]
	 */
	private static Map<String, Map<String, List<LazyClientBean>>> topicGroupCache = new HashMap<String, Map<String,List<LazyClientBean>>>();
	
	/**
	 * 初始化
	* add by zhao of 2019年5月23日
	 */
	public void init() {
		List<Map<String, Object>> users = sqliteUtil.queryReqiestUser();
		if(!CollectionUtils.isEmpty(users)) {
			for (Map<String, Object> map : users) {
				lazyUser.put(map.get("username").toString(), map.get("password").toString());
			}
			log.info("load lazy users ["+lazyUser+"]");
		}
	}
	
	/**
	 * 加入消息待发送队列
	* add by zhao of 2019年5月24日
	 */
	public boolean addMessageToWaitSendQueue(LazyMqBean message) {
		log.info("push mq to waitSendQueue \n" + JSON.toJSONString(message));
		return waitSendQueue.flush(message);
	}
	
	/**
	 * 加入消息待发送队列 与拷贝DB 队列
	* add by zhao of 2019年5月24日
	 */
	public boolean addMessageToWaitSendQueueAndDBqueue(LazyMqBean message) {
		if(waitSendDBQueue.flush(message)) {
			return addMessageToWaitSendQueue(message);
		}
		return false;
	}
	
	/**
	 * 取出拷贝队列
	* add by zhao of 2019年6月6日
	 */
	public List<LazyMqBean> offWaitSendQueueAndDBqueue(int size) {
		return waitSendDBQueue.popList(size);
	}
	
	/**
	 * 加入注册客户端
	* add by zhao of 2019年5月30日
	 */
	public ResultContext addRegiestToList(Map<String, String> topicGroup , LazyClientBean bean) {
		if(topicGroup == null || topicGroup.isEmpty()) {
			return new ResultContext(-1, "must set topic");
		}
		
		synchronized(topicGroupCache) {
			if(lazyRegiestKey.contains(bean.getRegiestKey())) {
				return new ResultContext(-1, "duplicate registration");
			}
			lazyRegiestKey.add(bean.getRegiestKey());
			for (Map.Entry<String, String> entry : topicGroup.entrySet()) {
				Map<String, List<LazyClientBean>> group = null;
			  if(topicGroupCache.containsKey(entry.getKey())) {
				  group = topicGroupCache.get(entry.getKey());
			  }
			  else {
				  group = new HashMap<String, List<LazyClientBean>>();
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
		if(!lazyUser.get(user).equals(password)) {
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

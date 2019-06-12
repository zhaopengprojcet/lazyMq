package com.zhao.lazy.common.util.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.util.HttpUtil;
import com.zhao.lazy.common.util.LogUtil;
import com.zhao.lazy.common.util.ServerAttributeUtil;

public class SendMsgThread {

	private static ConcurrentHashMap<String, ConcurrentHashMap<String, BaseThread>> runThreads = new ConcurrentHashMap<String, ConcurrentHashMap<String, BaseThread>>();//监听组topic线程
	
	/**
	 * 移除没有注册的线程任务
	* add by zhao of 2019年6月11日
	 */
	public static void showdownThread(String queueName ,String key) {
		if(runThreads.contains(queueName) && runThreads.get(queueName).contains(key)) {
			BaseThread bt = runThreads.get(queueName).get(key);
			if(bt != null) {
				bt.exit = true;
				runThreads.get(queueName).remove(key);
			}
		}
	}
	
	public static void logRunThreadsInfo() {
		StringBuffer sbf = new StringBuffer("---------------------------------------");
		for(Entry<String, ConcurrentHashMap<String, BaseThread>> keys :runThreads.entrySet()) {
			sbf.append("\n[ "+keys.getKey() + " ]");
			int i = 1;
			Set<String> thMap = keys.getValue().keySet();
			for(String threads : thMap) {
				sbf.append("\n   " + i + ". " + threads);
				i++;
			}
			sbf.append("\n\n");
		}
		sbf.append("\n---------------------------------------");
		LogUtil.info(sbf.toString());
	}
	
	public static List<LazyClientBean> getTopicGroupClients(String groupName ,String topicName , int type) {
		List<LazyClientBean> pushClients = new ArrayList<LazyClientBean>();
		if(!ServerAttributeUtil.topicGroupCache.containsKey(groupName)) {
			return pushClients;
		}
		Map<String, List<LazyClientBean>> topicClients = ServerAttributeUtil.topicGroupCache.get(groupName);
		if(topicClients.containsKey(topicName)) {
			 if(type == 2) { // 广播
				 pushClients.addAll(topicClients.get(topicName));
			 }
			 else { //单点
				 pushClients.add(topicClients.get(topicName).get(((int)Math.random() * topicClients.get(topicName).size())));
			 }
		}
		return pushClients;
	}
	
	public static synchronized void checkRunThreadKey(String queueName) {
		if(!runThreads.containsKey(queueName)) {
			runThreads.put(queueName, new ConcurrentHashMap<String, BaseThread>());
		}
	}
	
	public class BaseThread {
		public boolean exit = false;
		public String topicName;
		public String groupName;
		public String queueName;
	}
	
	/**
	 * 待发送队列处理线程
	 * add by zhao of 2019年6月12日
	 *
	 * 功能描述：
	 */
	public class WaitSendQueueThread extends BaseThread  implements Runnable {
		
		
		public WaitSendQueueThread() {}
		public WaitSendQueueThread(String _groupName , String _topicName) {
			topicName = _topicName;
			groupName = _groupName;
			queueName = "waitSendQueue";
			SendMsgThread.checkRunThreadKey(queueName);
			runThreads.get(queueName).put(groupName + "_" + topicName, this);//加入监听
			SendMsgThread.logRunThreadsInfo();
		}
		
		@Override
		public void run() {
			while(!exit) {
				if(ServerAttributeUtil.topicGroupCache.containsKey(groupName) && ServerAttributeUtil.offWaitSendQueueSize(topicName , groupName) > 0) {
					LazyMqBean message = ServerAttributeUtil.offWaitSendQueue(topicName , groupName);
					try {
						if(message != null) {
							List<LazyClientBean> clients = getTopicGroupClients(groupName ,topicName, message.getSendType());
							if(!CollectionUtils.isEmpty(clients)) {
								JSONObject request = new JSONObject();
								request.put("_sc", message.getBody());
								request.put("_gp", groupName);
								Map<String, String> value = new HashMap<String, String>();
								value.put("_mc", request.toJSONString());
								for (LazyClientBean lazyClientBean : clients) {
									JSONObject result = HttpUtil.post("http://" + lazyClientBean.getHost() + ":" + lazyClientBean.getPort() + "/lazy/call", value);
									System.out.println("【"+groupName+"】【"+topicName+"】发送返回-------------》" + result.toJSONString());
								}
							}
							else {
								ServerAttributeUtil.addMessageToWaitSendQueue(message);
							}
						}
						else {
							Thread.sleep(2000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public class RetrySendQueueThread extends BaseThread  implements Runnable {

		public RetrySendQueueThread() {}
		public RetrySendQueueThread(String _groupName , String _topicName) {
			topicName = _topicName;
			groupName = _groupName;
			queueName = "retrySendQueue";
			SendMsgThread.checkRunThreadKey(queueName);
			runThreads.get(queueName).put(groupName + "_" + topicName, this);//加入监听
			SendMsgThread.logRunThreadsInfo();
		}
		
		@Override
		public void run() {
		}
		
	}
	
	public class DiscardedQueueThread extends BaseThread  implements Runnable {

		public DiscardedQueueThread() {}
		public DiscardedQueueThread(String _groupName , String _topicName) {
			topicName = _topicName;
			groupName = _groupName;
			queueName = "discardedQueue";
			SendMsgThread.checkRunThreadKey(queueName);
			runThreads.get(queueName).put(groupName + "_" + topicName, this);//加入监听
			SendMsgThread.logRunThreadsInfo();
		}
		
		@Override
		public void run() {
		}
		
	}
}

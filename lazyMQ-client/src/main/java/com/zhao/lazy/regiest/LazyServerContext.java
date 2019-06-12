package com.zhao.lazy.regiest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhao.lazy.client.LazyBean;
import com.zhao.lazy.client.LazyMethod;
import com.zhao.lazy.common.AesUtil;
import com.zhao.lazy.common.HttpUtil;
import com.zhao.lazy.common.JarFileUtil;
import com.zhao.lazy.server.ServerJetty;

public class LazyServerContext {

	private static ServerJetty server ;
	private static Log log = LogFactory.getLog("LazyRegiest service load ");
	private static ConcurrentHashMap<String, Method> listeners = new ConcurrentHashMap<String, Method>();
	private static ConcurrentHashMap<String, String> regiests = new ConcurrentHashMap<String, String>();
	private static String regiestToken = null;
	private static String regiestKey = null;
	private static String regiestHost = null;
	
	public static Method getMqMethod(String groupTopic) {
		return listeners.get(groupTopic);
	}
	
	/**
	 * 注册客户端
	* add by zhao of 2019年6月4日
	* @param host mq服务器地址   ex   http://127.0.0.1:8000
	* @param port 客户端端口号   ex    1000
	* @param packageName 注册服务扫描注册包名   ex  com
	* @param userName 服务器账户    ex admin
	* @param password 服务器密码    ex  123456
	* @param key 服务器通讯加密键   ex xxxxxxx
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void regiest(String host ,int port , String packageName , String userName , String password , String key) {
		//加载本地注册监听服务
		if(port < 1) {
			log.error("init lazyMQ client error [port wrong]");
			return ;
		}
		if(StringUtils.isBlank(key)) {
			log.error("init lazyMQ client error [key wrong]");
			return ;
		}
		if(StringUtils.isBlank(host)) {
			log.error("init lazyMQ client error [host wrong]");
			return ;
		}
		regiestHost = host;
		regiestKey = key;
		List<String> classNames = JarFileUtil.getClassName(packageName);
		if(classNames != null && classNames.size() > 0) {
			for (String cn : classNames) {
				try {
					Class cl = Class.forName(cn);
					if(cl.isAnnotationPresent(LazyBean.class)) {
						Method[] method = cl.getDeclaredMethods();
						if(method != null && method.length > 0) {
							for (Method mt : method) {
								if(mt.isAnnotationPresent(LazyMethod.class)) {
									LazyMethod lm = mt.getAnnotation(LazyMethod.class);
									String groupTopic = cn.substring(0, cn.lastIndexOf(".")) + "_" + lm.topic();
									
									if(regiests.containsKey(groupTopic)) {
										break;
									}
									else {
										regiests.put(groupTopic, lm.topic());
										listeners.put(groupTopic, mt);
									}
								}
							}
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			if(regiests.isEmpty()) {
				log.error("no lazyMQ listener[no LazyMethod]");
				return;
			}
			log.info("----------------------------------------------------------------" +
					 "\n lazy service [" + regiests + "]   \n listeners [" + listeners + "]" + 
					 "\n----------------------------------------------------------------");
			
			RegiestBean regiestBean = new RegiestBean();
			regiestBean.setUserName(userName);
			regiestBean.setPassword(password);
			regiestBean.setPort(port);
			regiestBean.setRegiestServices(regiests);
			try {
				String _jc = AesUtil.encrypt(JSON.toJSONString(regiestBean), regiestKey);
				Map<String, String> value = new HashMap<String, String>();
				value.put("_jc", _jc);
				JSONObject result = HttpUtil.post(host + "/server/regiest.wcc", value);
				if(result != null) {
					if(result.containsKey("code")) {
						String message = result.getString("message");
						if(result.getIntValue("code") == 0) {
							regiestToken = message;
							log.info("regiest lazyclient success ["+message+"]");
						}
						else {
							log.error("regiest lazyclient error ["+message+"]");
							return ;
						}
					}
					else {
						log.error("regiest lazyclient error ["+result.toString()+"]");
						return ;
					}
				}
				else {
					log.error("regiest lazyclient error [no result]");
					return ;
				}
			} catch (Exception e) {
				log.error("regiest lazyclient error [request error]");
				e.printStackTrace();
				return;
			}
		}
		else {
			log.error("no lazyMQ listener[no LazyMethod]");
			return;
		}
		
		//启动jetty监听mq推送
		
		if(server == null) {
			server = new ServerJetty(port);
		}
		server.start();
	}
	
	/**
	 * 推送消息到mq服务器
	* add by zhao of 2019年6月4日
	* @param messageBean 消息实体
	 */
	public static void pushMQBean(MessageBean messageBean) {
		if(server == null) {
			log.error("push mq error[must init server befor push]");
			return;
		}
		if(regiestToken == null) {
			log.error("push mq error[has no token]");
			return;
		}
		messageBean.setRegiestKey(regiestToken);
		
		if(StringUtils.isBlank(messageBean.getBody()) || StringUtils.isBlank(messageBean.getTopicName())) {
			log.error("push mq error[no body or no topic set]");
			return;
		}
		
		try {
			String _jc = AesUtil.encrypt(JSON.toJSONString(messageBean), regiestKey);
			Map<String, String> value = new HashMap<String, String>();
			value.put("_jc", _jc);
			JSONObject result = HttpUtil.post(regiestHost + "/server/msgPush.wcc", value);
			if(result != null) {
				if(result.containsKey("code")) {
					String message = result.getString("message");
					if(result.getIntValue("code") == 0) {
						log.info("push mq success ["+message+"]");
					}
					else {
						log.error("push mq error ["+message+"]");
						return ;
					}
				}
				else {
					log.error("push mq error ["+result.toString()+"]");
					return ;
				}
			}
			else {
				log.error("push mq error [no result]");
				return ;
			}
		} catch (Exception e) {
			log.error("push mq error [request error]");
			e.printStackTrace();
			return;
		}
	}
}

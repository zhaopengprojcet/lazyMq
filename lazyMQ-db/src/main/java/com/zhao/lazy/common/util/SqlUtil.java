package com.zhao.lazy.common.util;

import java.util.List;
import java.util.Map;

import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.model.server.LazyMqDiscardedBean;
import com.zhao.lazy.common.model.server.LazyMqRetryBean;

public interface SqlUtil {

	/**
	 * 查看消息是否重复
	* add by zhao of 2019年5月30日
	 */
	public boolean lazyMqExist(String messageId);
	/**
	 * 加入待发送队列
	* add by zhao of 2019年5月24日
	 */
	public int insertLazyMqBean(LazyMqBean messageBean);
	/**
	 * 加入待发送队列
	* add by zhao of 2019年5月24日
	 */
	public int batchInsertLazyMqBean(List<LazyMqBean> messageBeans);
	/**
	 * 删除待发送消息
	* add by zhao of 2019年6月20日
	 */
	public int deleteLazyMqBean(List<String> messageIds);
	/**
	 * 加入重试队列
	* add by zhao of 2019年5月24日
	 */
	public int insertLazyMqRetryBean(List<LazyMqRetryBean> messageBeans);
	/**
	 * 删除重试消息
	* add by zhao of 2019年6月20日
	 */
	public int deleteRetryMqBean(List<String> messageIds);
	/**
	 * 加入死信队列
	* add by zhao of 2019年5月24日
	 */
	public int insertLazyMqDiscardedBean(List<LazyMqDiscardedBean> messageBeans);
	
	/**
	 * 加入历史注册客户端
	* add by zhao of 2019年5月30日
	 */
	public int insertLazyClientBean(RegiestBean clientBean , LazyClientBean lazy);
	/**
	 * 新增账号
	* add by zhao of 2019年6月3日
	 */
	public int insertRegiestUser(String username , String pass , String desc);
	/**
	 * 查询账号
	* add by zhao of 2019年6月3日
	 */
	public List<Map<String, Object>> queryReqiestUser();
}

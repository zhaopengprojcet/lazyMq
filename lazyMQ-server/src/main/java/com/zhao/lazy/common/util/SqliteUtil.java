package com.zhao.lazy.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.model.server.LazyClientBean;
import com.zhao.lazy.common.model.server.LazyMqBean;
import com.zhao.lazy.common.model.server.LazyMqDiscardedBean;
import com.zhao.lazy.common.model.server.LazyMqRetryBean;

@Component("sqliteUtil")
public class SqliteUtil {

	@Autowired
	private JdbcTemplate updateJdbcTemplate;
	@Autowired
	private JdbcTemplate readJdbcTemplate;
	
	
	
	
	private final String QUERY_MQ_EXIST = "select count(*) as _count from lazy_mq where messageId = ?";
	
	/**
	 * 查看消息是否重复
	* add by zhao of 2019年5月30日
	 */
	public boolean lazyMqExist(String messageId) {
		Map<String, Object> query = readJdbcTemplate.queryForMap(QUERY_MQ_EXIST, messageId);
		if(query.containsKey("_count")) {
			if(Integer.parseInt(query.get("_count").toString()) < 1) {
				return false;
			} 
		}
		return true;
	}
	
	
	private final String INSERT_LAZY_MQ = "insert into lazy_mq(id,messageId , body ,topicName ,sendTime ,createTime ,sendType) select ?,?,?,?,?,?,?  where not exists(select 1 from lazy_mq where messageId = ?)";
	/**
	 * 加入待发送队列
	* add by zhao of 2019年5月24日
	 */
	@Transactional
	public int insertLazyMqBean(LazyMqBean messageBean) {
		return updateJdbcTemplate.update(INSERT_LAZY_MQ, 
					RandomUtils.getPrimaryKey() ,
					messageBean.getMessageId() ,
					messageBean.getBody() ,
					messageBean.getTopicName() ,
					messageBean.getSendTime() + "",
					messageBean.getCreateTime() +"",
					messageBean.getSendType() ,
					messageBean.getMessageId()
				);
	}
	
	
	private final String BATCH_INSERT_LAZY_MQ = "insert into lazy_mq(id,messageId , body ,topicName ,sendTime ,createTime ,sendType) values(?,?,?,?,?,?,?)";
	/**
	 * 加入待发送队列
	* add by zhao of 2019年5月24日
	 */
	@Transactional
	public int batchInsertLazyMqBean(List<LazyMqBean> messageBeans) {
		if(!CollectionUtils.isEmpty(messageBeans)) {
			Object[] pras = new Object[messageBeans.size() * 7];
			int index = 0;
			for (LazyMqBean lazyMqBean : messageBeans) {
				pras[index++] = RandomUtils.getPrimaryKey();
				pras[index++] = lazyMqBean.getMessageId() ;
				pras[index++] = lazyMqBean.getBody()  ;
				pras[index++] = lazyMqBean.getTopicName() ;
				pras[index++] = lazyMqBean.getSendTime() + "" ;
				pras[index++] = lazyMqBean.getCreateTime() + "";
				pras[index++] = lazyMqBean.getSendType();
			}
			StringBuffer updateSql = new StringBuffer(BATCH_INSERT_LAZY_MQ);
			for(int i = 0 ; i < messageBeans.size() - 1 ; i++) {
				updateSql.append(",(?,?,?,?,?,?,?)");
			}
			int updatedCountArray = updateJdbcTemplate.update(updateSql.toString(), pras);
			return updatedCountArray; 
		}
		return 0;
	}
	
	private final String BATCH_DELETE_LAZY_MQ = "delete from lazy_mq where messageId in ( ? ";
	/**
	 * 删除已成功消息
	* add by zhao of 2019年6月20日
	 */
	@Transactional
	public int deleteSuccessLazyMqBean(List<String> messageIds) {
		if(!CollectionUtils.isEmpty(messageIds)) {
			StringBuffer deleteSql = new StringBuffer(BATCH_DELETE_LAZY_MQ);
			Object[] pras = new Object[messageIds.size()];
			for(int i = 0 ; i < messageIds.size() ; i++) {
				pras[i] = messageIds.get(i);
				if(i < messageIds.size() - 2) {
					deleteSql.append(" , ?");
				}
			}
			deleteSql.append(" ) ");
			int updatedCountArray = updateJdbcTemplate.update(deleteSql.toString(), pras);
			return updatedCountArray; 
		}
		return 0;
	}
	
	
	private final String BATCH_INSERT_LAZY_RETRY_MQ = "insert into lazy_retry_mq(id,messageId , body ,topicName ,sendTime ,createTime , lastSendTime , nextSendTime ,thisRetryTime, sendCount,sendType) values(?,?,?,?,?,?,?,?,?,?)";
	/**
	 * 加入重试队列
	* add by zhao of 2019年5月24日
	 */
	@Transactional
	public int insertLazyMqRetryBean(List<LazyMqRetryBean> messageBeans) {
		if(!CollectionUtils.isEmpty(messageBeans)) {
			Object[] pras = new Object[messageBeans.size() * 11];
			int index = 0;
			for (LazyMqRetryBean lazyRetryBean : messageBeans) {
				pras[index++] = RandomUtils.getPrimaryKey();
				pras[index++] = lazyRetryBean.getMessageId() ;
				pras[index++] = lazyRetryBean.getBody() ;
				pras[index++] = lazyRetryBean.getTopicName() ;
				pras[index++] = lazyRetryBean.getSendTime() + "";
				pras[index++] = lazyRetryBean.getCreateTime() +"";
				pras[index++] = lazyRetryBean.getLastSendTime() + "";
				pras[index++] = lazyRetryBean.getNextSendTime() + "" ;
				pras[index++] = lazyRetryBean.getThisRetryTime() + "" ;
				pras[index++] = lazyRetryBean.getSendCount();
				pras[index++] = lazyRetryBean.getSendType();
			}
			StringBuffer updateSql = new StringBuffer(BATCH_INSERT_LAZY_RETRY_MQ);
			for(int i = 0 ; i < messageBeans.size() - 2 ; i++) {
				updateSql.append(",(?,?,?,?,?,?,?)");
			}
			int updatedCountArray = updateJdbcTemplate.update(updateSql.toString(), pras);
			return updatedCountArray; 
		}
		return 0;
	}
	
	private final String BATCH_DELETE_RETRY_MQ = "delete from lazy_retry_mq where messageId in ( ? ";
	/**
	 * 删除已成功消息
	* add by zhao of 2019年6月20日
	 */
	@Transactional
	public int deleteSuccessRetryMqBean(List<String> messageIds) {
		if(!CollectionUtils.isEmpty(messageIds)) {
			StringBuffer deleteSql = new StringBuffer(BATCH_DELETE_RETRY_MQ);
			Object[] pras = new Object[messageIds.size()];
			for(int i = 0 ; i < messageIds.size() ; i++) {
				pras[i] = messageIds.get(i);
				if(i < messageIds.size() - 2) {
					deleteSql.append(" , ?");
				}
			}
			deleteSql.append(" ) ");
			int updatedCountArray = updateJdbcTemplate.update(deleteSql.toString(), pras);
			return updatedCountArray; 
		}
		return 0;
	}
	
	private final String INSERT_LAZY_DISCARDED_MQ = "insert into lazy_mq(id,messageId , body ,topicName ,sendTime  , inDisTime,sendType) values(?,?,?,?,?,?)";
	/**
	 * 加入死信队列
	* add by zhao of 2019年5月24日
	 */
	@Transactional
	public int insertLazyMqDiscardedBean(LazyMqDiscardedBean messageBean) {
		return updateJdbcTemplate.update(INSERT_LAZY_DISCARDED_MQ, 
				RandomUtils.getPrimaryKey() ,
				messageBean.getMessageId() ,
				messageBean.getBody() ,
				messageBean.getTopicName() ,
				messageBean.getSendTime() + "",
				messageBean.getInDisTime() + "",
				messageBean.getSendType()
			);
	}
	
	
	private final String INSERT_REGIEST_CLIENT = "insert into lazy_regiest_client(id , topicName ,host ,port ,regiestKey , regiestTime ,username) values(?,?,?,?,?,?,?)";
	
	/**
	 * 加入历史注册客户端
	* add by zhao of 2019年5月30日
	 */
	@Transactional
	public int insertLazyClientBean(RegiestBean clientBean , LazyClientBean lazy) {
		return updateJdbcTemplate.update(INSERT_REGIEST_CLIENT, 
				RandomUtils.getPrimaryKey() ,
				JSON.toJSONString(clientBean.getRegiestServices()) ,
				clientBean.getHost() ,
				clientBean.getPort() ,
				lazy.getRegiestKey() ,
				lazy.getRegiestTime() + "" ,
				lazy.getUserName()
			);
	}
	
	private final String INSERT_REGIEST_USER = "insert into lazy_user(id , username ,password ,userdesc) values(?,?,?,?)";
	
	/**
	 * 新增账号
	* add by zhao of 2019年6月3日
	 */
	@Transactional
	public int insertRegiestUser(String username , String pass , String desc) {
		return updateJdbcTemplate.update(INSERT_REGIEST_USER, 
				RandomUtils.getPrimaryKey() ,
				username ,
				DigestUtils.md5Hex(pass) ,
				desc
			);
	}
	
	private final String QUERY_REGIEST_USER = "select * from lazy_user ";
	
	/**
	 * 查询账号
	* add by zhao of 2019年6月3日
	 */
	public List<Map<String, Object>> queryReqiestUser() {
		return readJdbcTemplate.queryForList(QUERY_REGIEST_USER);
	}
}

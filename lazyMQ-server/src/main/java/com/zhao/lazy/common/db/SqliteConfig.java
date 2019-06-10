package com.zhao.lazy.common.db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zhao.lazy.common.util.ConfigPropertiesUtil;

@SpringBootConfiguration
public class SqliteConfig {

	@Bean("updateDataSource")
	public BasicDataSource updateDataSource() {
		BasicDataSource pool = new BasicDataSource();
        try {
            pool.setDriverClassName(ConfigPropertiesUtil.instance().getPropertiesVal("mq.db.driver"));
            pool.setUrl(ConfigPropertiesUtil.instance().getPropertiesVal("mq.db.url"));
            pool.setInitialSize(1);
            pool.setMaxIdle(1);
            pool.setMinIdle(1);
            pool.setMaxTotal(1);
        } catch (Exception e) {
        	throw new RuntimeException("初始化sqlite失败", e);
		}
        return pool;
    }
	
	
	@Bean("readDataSource")
	public BasicDataSource readDataSource() {
		BasicDataSource pool = new BasicDataSource();
        try {
            pool.setDriverClassName(ConfigPropertiesUtil.instance().getPropertiesVal("mq.db.driver"));
            pool.setUrl(ConfigPropertiesUtil.instance().getPropertiesVal("mq.db.url"));
            pool.setInitialSize(20);
            pool.setMaxIdle(100);
            pool.setMinIdle(20);
            pool.setMaxTotal(100);
            
        } catch (Exception e) {
        	throw new RuntimeException("初始化sqlite失败", e);
		}
        return pool;
    }
	
	@Bean("updateJdbcTemplate")
	public JdbcTemplate updateJdbcTemplate() {
		return new JdbcTemplate(updateDataSource());
	}
	
	@Bean("readJdbcTemplate")
	public JdbcTemplate readJdbcTemplate() {
		return new JdbcTemplate(readDataSource());
	}
	
}

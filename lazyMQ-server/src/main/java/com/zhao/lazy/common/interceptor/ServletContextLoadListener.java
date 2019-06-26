package com.zhao.lazy.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqliteUtil;
import com.zhao.lazy.service.MqService;

@Component
public class ServletContextLoadListener implements ApplicationRunner{

	@Autowired
	ServerAttributeUtil serverAttributeUtil;
	@Autowired
	MqService mqService;
	@Autowired
	SqliteUtil sqliteUtil;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		serverAttributeUtil.init(sqliteUtil);
		
		mqService.startDBinit();
	}

}

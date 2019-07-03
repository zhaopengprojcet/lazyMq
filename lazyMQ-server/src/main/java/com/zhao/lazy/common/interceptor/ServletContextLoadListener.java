package com.zhao.lazy.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqlUtil;
import com.zhao.lazy.common.util.sqlite.SqliteUtil;
import com.zhao.lazy.service.MqService;

@Component
public class ServletContextLoadListener implements ApplicationRunner{

	@Autowired
	MqService mqService;
	@Autowired
	SqlUtil sqlUtil;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		ServerAttributeUtil.init(sqlUtil);
		
		mqService.startDBinit();
	}

}

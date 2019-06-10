package com.zhao.lazy.service;

import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.result.ResultContext;

public interface MqService {

	ResultContext clientRegiest(RegiestBean regiest);
	
	ResultContext pushMessage(MessageBean message);
	
	void startDBinit();
}

package com.zhao.lazy.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.annotation.PushMessageBean;
import com.zhao.lazy.common.annotation.PushRegiestBean;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.util.AesUtil;
import com.zhao.lazy.common.util.ConfigPropertiesUtil;
import com.zhao.lazy.common.util.IpUtil;
import com.zhao.lazy.common.util.RandomUtils;
import com.zhao.lazy.service.MqService;

/**
 * 请求处理
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
@RestController
public class MqController {
	@Autowired
	private MqService mqService;
	
	@PushRegiestBean
	@RequestMapping(value = "/server/regiest.wcc" ,method = RequestMethod.POST)
	public String regiest(@RequestParam("_jc")String _jc , HttpServletRequest request) {
		_jc = AesUtil.decrypt(_jc, ConfigPropertiesUtil.instance().getPropertiesVal("mq.aes.key"));
		RegiestBean regiesteBean = JSON.parseObject(_jc, RegiestBean.class);
		regiesteBean.setHost(IpUtil.getIpAddr(request));
		return mqService.clientRegiest(regiesteBean).toJson();
	}
	
	@PushMessageBean
	@RequestMapping(value = "/server/msgPush.wcc" ,method = RequestMethod.POST)
	public String pushMsg(@RequestParam("_jc")String _jc) {
		_jc = AesUtil.decrypt(_jc, ConfigPropertiesUtil.instance().getPropertiesVal("mq.aes.key"));
		MessageBean message = JSON.parseObject(_jc, MessageBean.class);
		return mqService.pushMessage(message).toJson();
	}
}

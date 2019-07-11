package com.zhao.lazy.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * console 界面控制器
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
@Controller
@RequestMapping("/console")
public class MqConsoleController {
	
	/**
	 * 主页
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/index.html")
	public String index() {
		return "index";
	}
	
	/**
	 * 账号相关
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/user/index.html")
	public String users() {
		return "user/index";
	}
	
	/**
	 * 注册信息相关
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/client/index.html")
	public String regiestClient() {
		return "client/index";
	}
	
	/**
	 * 等待队列相关
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/mq/wait/index.html")
	public String waitQueue() {
		return "wait/index";
	}
	
	/**
	 * 重试队列相关
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/mq/retry/index.html")
	public String retryQueue() {
		return "retry/index";
	}
	
	/**
	 * 死信队列相关
	* add by zhao of 2019年7月3日
	 */
	@RequestMapping("/mq/discarded/index.html")
	public String discardedQueue() {
		return "discarded/index";
	}
	
}

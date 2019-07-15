package com.zhao.lazy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhao.lazy.common.result.ResponseContext;
import com.zhao.lazy.common.util.ServerAttributeUtil;

@Controller
@RequestMapping("/console/mq/wait")
public class WaitSendQueueController {

	@ResponseBody
	@RequestMapping("/list.html")
	public String list() {
		return ServerAttributeUtil.waitSednQueueTreeGrid().toJSONString();
	}
	
	@RequestMapping("/delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="regiestKey" , defaultValue = "" , required = true)String regiestKey) {
		boolean remove = ServerAttributeUtil.removeRegiestClient(regiestKey);
		if(remove) {
			return new ResponseContext(1, "客户端已移出注册列表").toJson();
		}
		return new ResponseContext(1, "当前已注册客户端列表未找到对应客户端").toJson();
	}
}

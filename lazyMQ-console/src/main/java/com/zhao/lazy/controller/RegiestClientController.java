package com.zhao.lazy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhao.lazy.common.result.ResponseContext;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqlUtil;

@Controller
@RequestMapping("/console/client")
public class RegiestClientController {

	@Autowired
	private SqlUtil sqlUtil;
	
	@ResponseBody
	@RequestMapping("/list.html")
	public String list(@RequestParam(value="page" , defaultValue = "1" , required = false) int page ,
					   @RequestParam(value="rows" , defaultValue = "10" , required = false) int limit) {
		return new ResponseContext(sqlUtil.queryReqiestClientPageCount(), ServerAttributeUtil.checkClientIsOnLine(sqlUtil.queryReqiestClientPage(page, limit))).toJson();
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

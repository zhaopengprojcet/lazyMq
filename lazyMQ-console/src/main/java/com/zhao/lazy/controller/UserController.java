package com.zhao.lazy.controller;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhao.lazy.common.result.ResponseContext;
import com.zhao.lazy.common.util.ServerAttributeUtil;
import com.zhao.lazy.common.util.SqlUtil;

@Controller
@RequestMapping("/console/user")
public class UserController {

	@Autowired
	private SqlUtil sqlUtil;
	
	@ResponseBody
	@RequestMapping("/list.html")
	public String list(@RequestParam(value="page" , defaultValue = "1" , required = false) int page ,
					   @RequestParam(value="rows" , defaultValue = "10" , required = false) int limit) {
		return new ResponseContext(sqlUtil.queryReqiestUserPageCount(), sqlUtil.queryReqiestUserPage(page, limit)).toJson();
	}
	
	@RequestMapping("/update.html")
	public String update(@RequestParam(value="id",defaultValue="",required=false)String id , Model model) {
		if(!StringUtils.isBlank(id)) {
			Map<String, Object> user = this.sqlUtil.queryUserById(id);
			user.put("password", "");
			model.addAttribute("update_data_map", user);
		}
		return "user/update";
	}
	
	@RequestMapping("/save.html")
	@ResponseBody
	public String save(@RequestParam(value="id" , defaultValue = "" , required = false)String id , 
					   @RequestParam(value="username" , defaultValue = "" , required = false)String username , 
					   @RequestParam(value="password" , defaultValue = "" , required = false)String password , 
					   @RequestParam(value="userdesc" , defaultValue = "" , required = false)String userdesc) {
		if(StringUtils.isBlank(username) || (StringUtils.isBlank(id) && StringUtils.isBlank(password))) {//新账号需要密码，旧账号无密码则认为不修改，否则修改
			return new ResponseContext(-1, "缺少提交参数").toJson();
		}
		if(StringUtils.isBlank(id)) {//新增
			if(this.sqlUtil.queryUserConutByName(username) > 0) {
				return new ResponseContext(-1, "账号已存在").toJson();
			}
			int add = this.sqlUtil.insertRegiestUser(username, password, userdesc);
			if(add > 0) {
				if(!StringUtils.isBlank(password)) {
					ServerAttributeUtil.updateUser(username, password);//更新账号过滤缓存
				}
				return new ResponseContext(1, "处理成功").toJson();
			}
			else {
				return new ResponseContext(-1, "新增账号失败").toJson();
			}
		}
		else {//更新
			int update = this.sqlUtil.updateUser(id, password, userdesc);
			if(update > 0) {
				Map<String, Object> user = this.sqlUtil.queryUserById(id);
				ServerAttributeUtil.updateUser(user.get("username").toString(), password);//更新账号过滤缓存
				return new ResponseContext(1, "处理成功").toJson();
			}
			else {
				return new ResponseContext(-1, "更新账号失败").toJson();
			}
		}
	}
	
	@RequestMapping("/delete.html")
	@ResponseBody
	public String delete(@RequestParam(value="id" , defaultValue = "" , required = true)String id) {
		Map<String, Object> user = this.sqlUtil.queryUserById(id);
		if(user != null) {
			int delete = this.sqlUtil.deleteUser(id);
			if(delete > 0) {
				ServerAttributeUtil.deleteUser(user.get("username").toString());
				return new ResponseContext(1, "处理成功").toJson();
			}
		}
		return new ResponseContext(-1, "没有可删除数据").toJson();
	}
}

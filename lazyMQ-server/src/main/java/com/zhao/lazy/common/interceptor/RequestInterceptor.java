package com.zhao.lazy.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.zhao.lazy.common.annotation.PushMessageBean;
import com.zhao.lazy.common.annotation.PushRegiestBean;
import com.zhao.lazy.common.model.MessageBean;
import com.zhao.lazy.common.model.RegiestBean;
import com.zhao.lazy.common.result.ResultContext;
import com.zhao.lazy.common.util.AesUtil;
import com.zhao.lazy.common.util.ConfigPropertiesUtil;
import com.zhao.lazy.common.util.DateUtil;
import com.zhao.lazy.common.util.ServerAttributeUtil;

/**
 * 请求拦截处理
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class RequestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String parameter = request.getParameter("_jc");
		
		//-------------注册注销类函数请求----------------------------开始
		
    	if(StringUtils.isBlank(parameter)) {
    		noPowersRest(response ,"拒绝服务【1090】");
    		return false;
    	}
    	try {
    		parameter = AesUtil.decrypt(parameter, ConfigPropertiesUtil.instance().getPropertiesVal("mq.aes.key"));
		} catch (Exception e) {
			noPowersRest(response ,"拒绝服务【1096】");
    		return false;
		}
    	if(StringUtils.isBlank(parameter)) {
    		noPowersRest(response ,"拒绝服务【1091】");
    		return false;
    	}
    	if(!(parameter.startsWith("{") && parameter.endsWith("}"))) {
    		noPowersRest(response ,"拒绝服务【1092】");
    		return false;
    	}
    	//-------------注册注销类函数请求----------------------------结束
    	
    	//-------------消息push类函数请求----------------------------开始
    	
		if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            //请求消息类
            if(method.hasMethodAnnotation(PushMessageBean.class)) {
            	MessageBean messageBean = null;
            	try {
            		messageBean = JSON.parseObject(parameter, MessageBean.class);
				} catch (Exception e) {
					noPowersRest(response ,"拒绝服务【1093】");
					return false;
				}
            	System.out.println("----------------->"+messageBean.getTopicName());
            	if(!ServerAttributeUtil.hasTopic(messageBean.getTopicName())) {
            		noPowersRest(response ,"拒绝服务【1096】");
					return false;
            	}
            	if(!ServerAttributeUtil.lazyRegiestKeyCheck(messageBean.getRegiestKey())) {
            		noPowersRest(response ,"拒绝服务【1095】");
					return false;
            	}
            	if(messageBean.getSendTime() < DateUtil.getBeforTime(5, TimeUnit.MINUTES) || messageBean.getSendTime() > DateUtil.getAfterTime(5, TimeUnit.MINUTES)) {
            		noPowersRest(response ,"拒绝服务【1094】");
					return false;
            	}
            	return true;
            }
            //注册服务类
            else if(method.hasMethodAnnotation(PushRegiestBean.class)) {
            	RegiestBean regiesteBean = null;
            	try {
            		regiesteBean = JSON.parseObject(parameter, RegiestBean.class);
				} catch (Exception e) {
					noPowersRest(response ,"拒绝服务【1081】");
					return false;
				}
            	if(!ServerAttributeUtil.lazyUserCheck(regiesteBean.getUserName(), regiesteBean.getPassword())) {
            		noPowersRest(response ,"拒绝服务【1082】");
					return false;
            	}
            	if(CollectionUtils.isEmpty(regiesteBean.getRegiestServices())) {
            		noPowersRest(response ,"拒绝服务【1083】");
					return false;
            	}
            	if(regiesteBean.getPort() < 1) {
            		noPowersRest(response ,"拒绝服务【1085】");
					return false;
            	}
            	return true;
            }
		}
		//-------------消息push类函数请求----------------------------结束
		
		noPowersRest(response ,"拒绝服务【-1000】");
		return false;
	}
	
	private void noPowersRest(HttpServletResponse response , String message) {
		PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(new ResultContext(-1 , message).toJson());
        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
	}
}

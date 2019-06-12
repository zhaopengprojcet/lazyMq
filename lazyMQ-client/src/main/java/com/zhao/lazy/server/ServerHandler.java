package com.zhao.lazy.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhao.lazy.regiest.LazyServerContext;

/**
 * 监听服务端发送的MQ消息
 * add by zhao of 2019年6月4日
 *
 * 功能描述：
 */
public class ServerHandler extends AbstractHandler {

	private final String REQUEST_URL = "/lazy/call";
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("utf-8");
        baseRequest.setHandled(true);
        PrintWriter out = response.getWriter();
        if(target.equals(REQUEST_URL)) {
        	String paramers = request.getParameter("_mc");
        	JSONObject json = JSON.parseObject(paramers);
        	Method method = LazyServerContext.getMqMethod(json.getString("_gp"));
        	if(method != null) {
        		System.out.println(method.getName());
        		response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"code\":1,\"message\":\"success\"}");
                return;
        	}
        } 
        response.setStatus(HttpServletResponse.SC_OK);
        out.print("{\"code\":-1,\"message\":\"error\"}");
	}

}

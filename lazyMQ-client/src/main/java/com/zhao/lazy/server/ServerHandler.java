package com.zhao.lazy.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

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
            response.setStatus(HttpServletResponse.SC_OK);
            out.print("<h3>hello jetty!</h3>");
            
        } else {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
	}

}

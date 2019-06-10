package com.zhao.lazy.common.ex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.zhao.lazy.common.result.ResultContext;


/**
 * 异常处理
 * @author zhao
 *
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler implements HandlerExceptionResolver ,Ordered{
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ex.printStackTrace();
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.addStaticAttribute("code", -1);
		view.addStaticAttribute("message", "服务器处理异常");
		return new ModelAndView(view);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
	@ExceptionHandler(value=RuntimeException.class)
	public String otherExceptionHandler(RuntimeException runtimeException) {
		runtimeException.printStackTrace();
		return new ResultContext(-1, "服务器处理异常").toJson();
	}
	
	@ExceptionHandler(value=ServerExecption.class)
	public String serverExceptionHandler(ServerExecption serverExecption) {
		serverExecption.printStackTrace();
		return new ResultContext(-1, serverExecption.getMessage()).toJson();
	}
}

package com.zhao.lazy.common.ex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RestController
public class RestErrorController implements ErrorController{
	
	@RequestMapping(value = "/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.addStaticAttribute("code", -1);
		view.addStaticAttribute("message", "服务器处理异常");
        return new ModelAndView(view);
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
}

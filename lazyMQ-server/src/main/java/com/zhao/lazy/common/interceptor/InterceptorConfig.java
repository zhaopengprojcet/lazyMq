package com.zhao.lazy.common.interceptor;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public HandlerInterceptor getInterceptor() {
		return new RequestInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getInterceptor())
		.addPathPatterns("/")
		.addPathPatterns("/**")
		;
	}
}

package com.zhao.lazy.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zhao.lazy.client.em.LazySendType;

/**
 * 声明当前函数用于处理监听的对应消息
 * add by zhao of 2019年6月4日
 *
 * 功能描述：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LazyMethod {

	String topic(); 
}

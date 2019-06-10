package com.zhao.lazy.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  定义方法参数结构应该为 请求消息体
 *  com.zhao.lazy.common.model.MessageBean
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushMessageBean {

}

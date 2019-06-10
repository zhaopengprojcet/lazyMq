package com.zhao.lazy.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 统一日志处理
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class LogUtil {

	private static Log logger = LogFactory.getLog(LogUtil.class);
	/**
	 * 设置日志是否控制台打印
	 */
	public static boolean consoleLog = true;
	
	public static void info(String message) {
		if(consoleLog) {
			logger.info(message);
		}
	}
	
	public static void error(String message) {
		if(consoleLog) {
			logger.error(message);
		}
	}
	
	public static void error(String message , Throwable th) {
		if(consoleLog) {
			logger.error(message ,th);
		}
	}
}

package com.zhao.lazy.common.util;

/**
 *  重试队列重试时间设置
 * add by zhao of 2019年6月17日
 *
 * 功能描述：
 */
public class RetryTimeUtil {
	/**
	 * 时间间隔设置
	 */
	private static long[] TIME_RETRY = { // ms
		5000 , 10000 , 30000 , 60000 , 120000 , 300000 , 600000 , 1200000 , 3600000 , 7200000
	};
	
	/**
	 * 获取下一次尝试时间
	* add by zhao of 2019年6月17日
	 */
	public static long getNextTime(long thisTime , long retryTime) {
		if(retryTime == 0) {
			return thisTime + TIME_RETRY[0];
		}
		for(int i = 0 ; i < TIME_RETRY.length ; i++) {
			if(TIME_RETRY[i] == retryTime && (i < TIME_RETRY.length - 1)) {
				return thisTime + TIME_RETRY[i + 1];
			}
		}
		return -1;
	}
}

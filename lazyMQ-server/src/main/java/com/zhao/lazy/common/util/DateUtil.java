package com.zhao.lazy.common.util;

import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static long getBeforTime(long time , TimeUnit unit) {
		if(unit == TimeUnit.MILLISECONDS) {
			return System.currentTimeMillis() - time;
		}
		else if(unit == TimeUnit.SECONDS) {
			return System.currentTimeMillis() - time * 1000;
		}
		else if(unit == TimeUnit.MINUTES) {
			return System.currentTimeMillis() - time * 1000 * 60;
		}
		else if(unit == TimeUnit.HOURS) {
			return System.currentTimeMillis() - time * 1000 * 60 * 60;
		}
		else if(unit == TimeUnit.DAYS) {
			return System.currentTimeMillis() - time * 1000 * 60 * 60 * 24;
		}
		return -1;
	}
	
	public static long getAfterTime(long time , TimeUnit unit) {
		if(unit == TimeUnit.MILLISECONDS) {
			return System.currentTimeMillis() + time;
		}
		else if(unit == TimeUnit.SECONDS) {
			return System.currentTimeMillis() + time * 1000;
		}
		else if(unit == TimeUnit.MINUTES) {
			return System.currentTimeMillis() + time * 1000 * 60;
		}
		else if(unit == TimeUnit.HOURS) {
			return System.currentTimeMillis() + time * 1000 * 60 * 60;
		}
		else if(unit == TimeUnit.DAYS) {
			return System.currentTimeMillis() + time * 1000 * 60 * 60 * 24;
		}
		return -1;
	}
}

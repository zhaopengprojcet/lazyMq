package com.zhao.lazy.common.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 公共线程池
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class ThreadSysUtil {

	private static LinkedBlockingQueue<Runnable> lbq = new LinkedBlockingQueue<Runnable>();
	private static ThreadPoolExecutor fixedThreadPool = new ThreadPoolExecutor(20, 200,
            0L, TimeUnit.MILLISECONDS,
            lbq);
	
	public static void execute(Thread thread) {
		fixedThreadPool.execute(thread);
		LogUtil.info("新线程任务加入执行,当前任务数【"+(fixedThreadPool.getActiveCount())+"】,池内总线程数【"+fixedThreadPool.getPoolSize()+"】缓存队列长度【"+lbq.size()+"】");
	}
}

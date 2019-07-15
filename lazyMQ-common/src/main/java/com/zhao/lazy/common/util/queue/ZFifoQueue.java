package com.zhao.lazy.common.util.queue;

import java.util.List;

/**
 * 先入先出队列
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public interface ZFifoQueue<E> {

	/**
	 * 出队列 并返回出值
	 * @return
	 */
	E pop();
	
	/**
	 * 批量出队列
	* add by zhao of 2019年6月10日
	 */
	List<E> popList(int size);
	/**
	 * 入队列
	 * @param model
	 * @return
	 */
	boolean flush(E model);
	
	/**
	 * 队列长度
	 * @return
	 */
	long size();
	
	/**
	 * 清空队列
	 * @return
	 */
	boolean clean();
	
	/**
	 * 取全部，非出队
	* add by zhao of 2019年7月15日
	 */
	List<Object> readAll();
}

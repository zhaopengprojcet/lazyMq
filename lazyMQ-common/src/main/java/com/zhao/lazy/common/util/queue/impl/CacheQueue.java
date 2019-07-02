package com.zhao.lazy.common.util.queue.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.zhao.lazy.common.util.queue.ZFifoQueue;
/**
 * 系统运行时缓存队列
 * add by zhao of 2019年5月23日
 *
 * 功能描述：
 */
public class CacheQueue<E> implements ZFifoQueue<E>{

	private Object[] datas ;
	private int size = 0;
	private final int DEFAULT_SIZE = 10;
	public CacheQueue() {
		checkNull();
	}
	
	/**
	 * 顺序取  从下标0开始
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E pop() {
		synchronized (datas) {
			if(datas == null) return null;
			if(size < 1) return null;
			Object model = null;
			model = datas[0];
			System.arraycopy(datas, 1, datas, 0,
	                size - 1);
			datas[--size] = null;
			return (E) model;
		}
	}

	
	
	@Override
	public List<E> popList(int popSize) {
		synchronized (datas) {
			if(datas == null) return null;
			if(size < 1) return null;
			List<E> result = null;
			if(size < popSize) {
				result = new ArrayList<E>(((List<E>)Arrays.asList(datas)));
				size = 0;
				result.removeAll(Collections.singleton(null)); 
				datas = new Object[DEFAULT_SIZE];
				return result;
			}
			else {
				Object[] outDatas = new Object[popSize];
				System.arraycopy(datas, 0, outDatas, 0, popSize);
				Object[] srcDatas = new Object[size - popSize];
				System.arraycopy(datas, popSize, srcDatas, 0,
						size - popSize);
				datas = srcDatas;
				size = size - popSize;
				result = new ArrayList<E>((List<E>)Arrays.asList(outDatas));
				result.removeAll(Collections.singleton(null)); 
				return result;
			}
		}
	}

	/**
	 * 顺序加入 从下标0开始
	 */
	@Override
	public boolean flush(E model) {
		synchronized (datas) {
			checkNull();
			checkDataSize();
			datas[size++] = model;
		}
		return true;
	}

	@Override
	public long size() {
		return size;
	}

	@Override
	public boolean clean() {
		datas = null;
		size = 0;
		return true;
	}

	/**
	 * 检查数据长度是否满足继续添加新元素
	 */
	private void checkDataSize() {
		if(datas.length == size) {
			int newSize = size + size / 2 ;
			datas = Arrays.copyOf(datas, newSize);
			return;
		}
	}
	
	/**
	 * 检查数据空
	 */
	private void checkNull() {
		if(datas == null) {
			datas = new Object[this.DEFAULT_SIZE];
			size = 0;
		}
	}

	
}

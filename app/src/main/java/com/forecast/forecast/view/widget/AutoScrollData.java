package com.forecast.forecast.view.widget;

/**
 * 自动滚动TextView的数据
 * 
 * @param <T>
 */
public interface AutoScrollData<T> {

	/**
	 * * 获取标题
	 * 
	 * @param data
	 * @return
	 */
    String getTextTitle(T data);

	/**
	 * 获取内容
	 * 
	 * @param data
	 * @return
	 */
    String getTextInfo(T data);

}

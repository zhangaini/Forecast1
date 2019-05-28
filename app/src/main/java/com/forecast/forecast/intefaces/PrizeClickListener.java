package com.forecast.forecast.intefaces;

/**
 * 描述：
 * 创建作者： ""
 * 创建时间： 2017/5/6 10:52
 **/
public interface PrizeClickListener<T> {
    void setOnItemClickListener(int position, T t, String name);

    void setOnViewClickListener(T t);
}

package com.forecast.forecast.adapters.base;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 通用适配器
 */
public class BaseRVAdapter<T> extends BaseRVAdapterWithCustomHolder<T, BaseViewHolder> {

    public BaseRVAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
//        setLoadMoreView(new CustomLoadMoreView());
    }

    @Override
    protected void convert(BaseViewHolder mHolder, T bean) {
    }
}
package com.forecast.forecast.adapters.base;

import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.forecast.forecast.view.widget.CustomLoadMoreView;

import java.util.List;


/**
 * 带自定义viewHolder的通用适配器
 * 主要用于既需要自定义 ViewHolder 的同时又需要 "加载更多" 的适配器
 * 在其增加 "加载更多" 行布局后,基类适配器{@link BaseQuickAdapter}中 {@link BaseQuickAdapter#getLoadingView(ViewGroup)} 和 {@link BaseQuickAdapter#onCreateDefViewHolder(ViewGroup, int)} 均调用了 {@link BaseQuickAdapter#createBaseViewHolder(View)}
 * 重写 {@link BaseQuickAdapter#createBaseViewHolder(View)} 后会导致适配器把 "加载更多" 行布局也当作普通数据行列表,需要注意
 * 总之:
 * 继承本适配器后,只需要指定所需的BaseViewHolder子类 V 即可使用自定义holder;
 * 同时,不要重写 {@link BaseQuickAdapter#createBaseViewHolder(View)};
 *
 * @param <T> item布局对应的数据实体类
 * @param <V> 自定义viewHolder,只填写常规item行布局对应的holder即可
 */
public class BaseRVAdapterWithCustomHolder<T, V extends BaseViewHolder> extends BaseQuickAdapter<T, V> {

    public BaseRVAdapterWithCustomHolder(int layoutResId, List<T> data) {
        super(layoutResId, data);
        setLoadMoreView(new CustomLoadMoreView());
    }

    @Override
    protected void convert(V mHolder, T bean) {
    }
}
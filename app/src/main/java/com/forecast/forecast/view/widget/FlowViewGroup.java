package com.forecast.forecast.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自动换行view group
 * Created by 洪少鹏 on 2017/10/17.
 */
public class FlowViewGroup extends ViewGroup {

    //如果使用频繁可以抽出来作为自定义的控件的参数
    private final static int VIEW_MARGIN = 15;
    private int jiange = 20;//按钮之间的间隔

    public FlowViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int stages = 1;
        int stageHeight = 0;
        int stageWidth = 0;

        int wholeWidth = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            // measure
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            stageWidth += (child.getMeasuredWidth() + VIEW_MARGIN);
            stageHeight = child.getMeasuredHeight();
            if (stageWidth >= wholeWidth) {
                stages++;
                //reset stageWidth
                stageWidth = child.getMeasuredWidth();
            }

        }
        int wholeHeight = (stageHeight + jiange) * (stages);

        setMeasuredDimension(resolveSize(wholeWidth, widthMeasureSpec),
                resolveSize(wholeHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

        final int count = getChildCount();
        int row = 0;
        int lengthX = arg1;    // 子view的右边和parent 的距离
        int lengthY = arg2;    //子view bottom位置和parent 的距离
        for (int i = 0; i < count; i++) {
            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            if (i == 0) {
                lengthX += width + VIEW_MARGIN;//第一个的时候不需要加
            } else {
                lengthX += width + VIEW_MARGIN + jiange;//按钮之间的间隔
            }
            if (row == 0) {
                // lengthY =  height + arg2 ;
                lengthY = height + jiange;
            } else {
                lengthY = (row) * (height + jiange) + height + jiange;
            }
            //如果一行画不完，就换行
            if (lengthX > arg3) {
                lengthX = width + VIEW_MARGIN + arg1;
                row++;
                lengthY = (row) * (height + jiange) + height + jiange;
            }
            child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
        }
    }
}
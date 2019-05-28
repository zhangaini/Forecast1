package com.forecast.forecast.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 描述：
 * 创建作者： 吴奶强
 * 创建时间： 2018/9/27
 */


public class EllipsizingTextView extends TextView {

    private static final String ELLIPSIS = "...";

    private boolean isStale;
    private boolean programmaticChange;
    private String fullText;
    private int maxLength = -1;

    public EllipsizingTextView(Context context) {
        super(context);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 外部通过调用setMaxLength方法设置显示的最多字数为maxLength
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength(){
        return maxLength;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text.toString();
            isStale = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }

    private void resetText() {
        int len = fullText.length();
        String workingText = fullText;
        if (maxLength != -1) {
            if (len > maxLength) {
                workingText = fullText.substring(0, maxLength).trim();
                workingText = workingText + ELLIPSIS;
            }
        }
        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                setText(workingText);
            } finally {
                programmaticChange = false;
            }
        }
        isStale = false;
    }

}

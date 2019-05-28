package com.forecast.forecast.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.forecast.forecast.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;


public class DialogLoading {
    private static final String TAG = "DialogLoading";
    public static final int HOR = 1;
    public static final int VER = 2;

    public static AlertDialog alertDialog;

    public static void showDialog(Context context) {
        showDialog(context, true);
    }

    public static void showDialogVer(Context context, @DrawableRes int drawableId, @StringRes int strId) {
        showDialog(context, drawableId, strId, true, VER);
        if (alertDialog != null && alertDialog.isShowing()) {
            changeAnimation(true);
        }
    }

    public static void showDialog(Context context, boolean cancelable) {
        showDialog(context, 0, R.string.progress_loading, cancelable, HOR);
    }

    private static void changeAnimation(boolean isStart) {
        View view = alertDialog.findViewById(R.id.dialog_icon);
        if (view != null && view instanceof ImageView && ((ImageView) view).getDrawable() instanceof AnimationDrawable) {
            if (isStart) {
                Log.d(TAG, "changeAnimation: start");
                ((AnimationDrawable) ((ImageView) view).getDrawable()).start();
            } else {
                Log.d(TAG, "changeAnimation: stop");
                ((AnimationDrawable) ((ImageView) view).getDrawable()).stop();
            }
        }
    }

    public static void showDialog(Context context, int drawableId, int resId, boolean canCancel, int orientation) {
        cancelDialog();
        alertDialog = createDialog(context, drawableId, resId, canCancel, orientation);

        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.show();
            } catch (Exception e) {
                Log.e(TAG, "showDialog: show error", e);
            }
        }
    }

    public static void cancelDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            try {
                changeAnimation(false);
                alertDialog.cancel();
                alertDialog = null;
            } catch (Exception e) {
                Log.e(TAG, "cancelDialog: dismiss error", e);
            }
        }
    }

    private static AlertDialog createDialog(Context context, int drawableId, int id, boolean cancelable, int orientation) {
        switch (orientation) {
            case VER:
                return createVerticalDialog(context, drawableId, context.getString(id), cancelable);
            case HOR:
            default:
                return createHorizontalDialog(context, context.getString(id), cancelable);

        }
    }

    private static AlertDialog createHorizontalDialog(Context context, String content, boolean cancelable) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        MaterialProgressBar progress = new MaterialProgressBar(context);
        progress.setId(R.id.dialog_icon);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(context, 50), dip2px(context, 50));
        params.setMargins(dip2px(context, 20), dip2px(context, 20), dip2px(context, 20), dip2px(context, 20));
        layout.addView(progress, params);

        TextView textView = new TextView(context);
        textView.setId(R.id.dialog_text);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(context, 20), 0, 0, 0);
        layout.addView(textView, params);
        textView.setTextSize(16);
        textView.setText(content);

        return new AlertDialog.Builder(context).setCancelable(cancelable).setView(layout).create();
    }

    private static AlertDialog createVerticalDialog(Context context, @DrawableRes int drawableRes, String content, boolean cancelable) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView imageView = new ImageView(context);
        imageView.setId(R.id.dialog_icon);
        imageView.setImageResource(drawableRes);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(context, 50), dip2px(context, 50));
        params.setMargins(0, dip2px(context, 30), 0, dip2px(context, 12));
        layout.addView(imageView, params);

        TextView textView = new TextView(context);
        textView.setId(R.id.dialog_text);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dip2px(context, 30));
        layout.addView(textView, params);
        textView.setTextSize(16);
        textView.setText(content);

        return new AlertDialog.Builder(context).setCancelable(cancelable).setView(layout).create();
    }

    private static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static boolean isShowing() {
        return alertDialog != null && alertDialog.isShowing();
    }
}

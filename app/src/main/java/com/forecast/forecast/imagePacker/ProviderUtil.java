package com.forecast.forecast.imagePacker;

import android.content.Context;

/**
 * 用于解决provider冲突的util
 *
 * 创建时间： 2017/12/11.
 * 作者：吴奶强
 * 邮箱：549064368@qq.com
 */

public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}

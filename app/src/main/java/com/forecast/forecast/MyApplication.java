package com.forecast.forecast;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * 创建作者：
 * 创建时间：
 **/

public class MyApplication extends Application {


    private static MyApplication mInstance;

    public static String APPID = "1ec025d7f7196c02e5aeb8977abe1695";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //集成测试，正式试用时要设为false

        Bmob.initialize(this,APPID);



        BmobConfig config = new BmobConfig.Builder(this)
                //设置APPID
                .setApplicationId(APPID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5500)
                .build();
        Bmob.initialize(config);

    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;
    }



}

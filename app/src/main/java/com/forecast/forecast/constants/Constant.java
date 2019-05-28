package com.forecast.forecast.constants;

import android.os.Environment;

/**
 * Created by Administrator on 2017/2/21.
 */

public class Constant {

    //角色id
    public static final String USERID = "USERID";

    //类型
    public static final String TYPE ="TYPE";

    //登录名
    public static final String LOGINNAME = "LOGINNAME";


    //登录id
    public static final String LOGINID = "LOGINID";


    /* 渠道使用的key,这里使用的是友盟的key */
    public static final String CHANNAL_KEY = "";


    /* 下载保存路径 */
    public static final String SAVEAPPFILEPATH = Environment.getExternalStorageDirectory()
            + "/" + "exam/exam.apk";
    /*保存的二维码路径*/
    public static final String ERWEIMA = Environment.getExternalStorageDirectory() + "/image";


    public final static int PHOTO_CAMERA = 10; // 拍照
    public final static int PHOTO_ALBUM = 11;// 相册


    public final static String RMB = "¥";


    public final static String EVEBUSCONTACT="EVEBUSCONTACT";

    public final static String EVEBUSSTEP="EVEBUSSTEP";

    public final static String EVEBUSRATECLAM="EVEBUSRATECLAM";

    public final static String EVEBUSRATEHIGH="EVEBUSRATEHIGH";

    public final static String EVEBUSRATEUP="EVEBUSRATEUP";

    public final static String EVEBUSRATETIME="EVEBUSRATETIME";

}

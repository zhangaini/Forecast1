package com.forecast.forecast.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.forecast.forecast.MyApplication;
import com.forecast.forecast.constants.Constant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 系统工具类
 *
 * @author ruhui
 * @time 2017/2/21 16:34
 **/

public class Tool {

    /**
     * 键盘在显示状态
     */
    public static final String KEYBOARD_SHOW = "keyboard_show";
    /**
     * 键盘隐藏状态
     */
    public static final String KEYBOARD_HIDE = "keyboard_hide";

    /**
     * 获取友盟渠道名称
     *
     * @param ctx
     * @return
     */
    public static String getChannelName(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                    }

                    channelName = applicationInfo.metaData.getString(Constant.CHANNAL_KEY);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取手机版本号
     *
     * @param context
     * @param isVersionCode 是否是数字
     * @return
     */
    public static String getAppVersion(Context context, boolean isVersionCode) {
        String resoult = "";
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        resoult = packInfo.versionName;
        if (isVersionCode) {
            resoult = String.valueOf(packInfo.versionCode);
        }
        return resoult;
    }

    /**
     * 判断两对象是否相同
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(Object a, Object b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    public static void hideInputMethod(Context context, View view) {
        try {
            InputMethodManager manager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示键盘
     */
    public static void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) MyApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        //这里给它设置了弹出的时间，
        imm.toggleSoftInput(1000, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param date
     * @return 格式化 yyyy-MM-dd
     */
    public static String formatSimpleDateX(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    /**
     * @param date
     * @return 格式化 yyyy-MM-dd
     */
    public static String formatSimpleDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar g = Calendar.getInstance();
        g.setTime(date);
        g.add(Calendar.MONTH, -1);
        Date date1 = g.getTime();
        return format.format(date1);
    }


    /**
     * @param date
     * @return 格式化 yyyy-MM-dd 00:10:03
     */
    public static String formatSimpleDate2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }

    /**
     * @param date
     * @return 格式化 yyyy-MM-dd 00:10
     */
    public static String formatSimpleDate3(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar g = Calendar.getInstance();
        g.setTime(date);
        g.add(Calendar.MONTH, -1);
        Date date1 = g.getTime();
        return format.format(date1);
    }

    /**
     * @param date
     * @return 格式化 yyyy-MM-dd
     */
    public static String formatSimpleDate1(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar g = Calendar.getInstance();
        g.setTime(date);
        g.add(Calendar.MONTH, -1);
        Date date1 = g.getTime();
        return format.format(date1);
    }

    /**
     * 银行卡的有效期
     *
     * @param date
     * @return
     */
    public static String formatCardTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format(date);
    }


    /**
     * @param date
     * @return 获取年
     */
    public static String formatGetyear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * @return String
     * @throws
     * @title: checkKeyboardStatus
     * @description: 判断软键盘的状态
     */
    public static String checkKeyboardStatus(Activity activity) {
        final View v = activity.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            return KEYBOARD_SHOW;
        }
        return KEYBOARD_HIDE;
    }

    /**
     * @describe:隐藏软键盘
     */
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            String status = Tool.checkKeyboardStatus(activity);
            if (!Tool.KEYBOARD_HIDE.equals(status)) {
                // 隐藏软键盘
                InputMethodManager manager =
                        (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    /**
     * @describe:手动调用软键盘
     */
    public static void showKeyboard(Activity activity) {

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        InputMethodManager manager =
                (InputMethodManager) activity.getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public static void showKeyboard(View view, Activity activity) {
        InputMethodManager manager =
                (InputMethodManager) activity.getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 格式化 2位小数
     *
     * @param number
     * @return
     */
    public static String formatPrice(double number) {
        String preNum = "";
        String numStr = String.valueOf(number);
        if (numStr.indexOf(".") < 0) {
            preNum = numStr + ".00";
        } else {
            preNum = numStr.substring(0, numStr.indexOf("."));
            String lastNum = numStr.substring(numStr.indexOf(".") + 1);
            if (lastNum.length() >= 2) {
                lastNum = lastNum.substring(0, 2);
                preNum = preNum + "." + lastNum;
            } else {
                preNum = preNum + "." + lastNum + "0";
            }
        }
        return preNum;
    }

    /**
     * 去除小数点 2位小数
     *
     * @param price
     * @return
     */
    public static String wipePoint(String price) {
        String preNum = "";
        String[] split = price.split(".");
        preNum = split[0];
        return preNum;
    }


    /**
     * 格式化 2位小数
     *
     * @param number
     * @return
     */
    public static String formatPrice(String number) {
        if ("".equals(number) || TextUtils.isEmpty(number)) {
            number = "0";
        }
        String preNum = "";
        String numStr = String.valueOf(number);
        if (numStr.indexOf(".") < 0) {
            preNum = numStr + ".00";
        } else {
            preNum = numStr.substring(0, numStr.indexOf("."));
            String lastNum = numStr.substring(numStr.indexOf(".") + 1);
            if (lastNum.length() >= 2) {
                lastNum = lastNum.substring(0, 2);
                preNum = preNum + "." + lastNum;
            } else {
                preNum = preNum + "." + lastNum + "0";
            }
        }
        return preNum;
    }

    public static String getGson(Object t) {
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        return gb.create().toJson(t);
    }

    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }


    /**
     * 判断是否是0
     *
     * @param RMB
     * @return
     */
    public static boolean isZero(String RMB) {
        return RMB.equals(Constant.RMB + "0") || RMB.equals(Constant.RMB + "0.0") || RMB.equals(Constant.RMB + "0.00")
                || RMB.equals("0") || RMB.equals("0.0") || RMB.equals("0.00");

    }

    //md5加密
    public static String getMd5(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            // 获得密文
            byte b[] = md.digest();
            // 把密文转换成十六进制的字符串形式
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取手机型号
     */
    public static String getMODEL() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     */
    public static String getBrand() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIP(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    /**
     * 是否是手机号码
     *
     * @param mobiles
     * @return
     */
    public static boolean checkPhoneNum(String mobiles) {
        Pattern p = Pattern.compile("^((1[0-9]))\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }


    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


//    public static void showShare(Context context,String title,String url,String content,String comment,String pictureUrl) {
//        //初始化ShareSDK
//        ShareSDK.initSDK(context);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
//        oks.setTitle(title);
//        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(content);
//        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl(pictureUrl);
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(comment);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(context.getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.show(context);
//    }

    /**
     * 车源的时间格式
     */
    public static String getTimeFormat(String date) {
        String newyear = "", mouth = "", day = "", hour = "", minute = "";
        if (date.indexOf("-") > 0) {
            newyear = date.substring(0, date.indexOf("-"));
            date = date.substring(date.indexOf("-") + 1);
            if (date.indexOf("-") > 0) {
                mouth = date.substring(0, date.indexOf("-"));
                date = date.substring(date.indexOf("-") + 1);
                if (date.indexOf(" ") > 0) {
                    day = date.substring(0, date.indexOf(" "));
                    date = date.substring(date.indexOf(" ") + 1);
                    if (date.indexOf(":") > 0) {
                        hour = date.substring(0, date.indexOf(":"));
                        date = date.substring(date.indexOf(":") + 1);
                        if (date.indexOf(":") > 0) {
                            minute = date.substring(0, date.indexOf(":"));
                        }
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(newyear)) {
            if ((Integer.valueOf(newyear) < getYear())) {
                return newyear + "年" + mouth + "月";
            } else if (Integer.valueOf(newyear) == getYear()) {
                if (!TextUtils.isEmpty(day) && !TextUtils.isEmpty(mouth) && !TextUtils.isEmpty(day)) {
                    int m = Integer.valueOf(mouth);
                    int h = getMonth();
                    if (Integer.valueOf(mouth) < getMonth()) {
                        return mouth + "月" + day + "日";
                    } else if ((Integer.valueOf(mouth) == getMonth())) {
                        if (Integer.valueOf(day) < getDay()) {
                            return mouth + "月" + day + "日";
                        } else if (Integer.valueOf(day) == getDay()) {
                            int hout = getHour();
                            int houtp = getHour() - Integer.valueOf(hour);
                            if (houtp > 1 && houtp < 24) {
                                return hour + " : " + minute;
                            } else {
                                return "刚刚";
                            }
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                } else {
                    return newyear + "年";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }


    public void getTimeByCalendar() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month = cal.get(Calendar.MONTH);//获取月份
        int day = cal.get(Calendar.DATE);//获取日
        int hour = cal.get(Calendar.HOUR);//小时
        int minute = cal.get(Calendar.MINUTE);//分
        int second = cal.get(Calendar.SECOND);//秒
        int WeekOfYear = cal.get(Calendar.DAY_OF_WEEK);//一周的第几天
    }

    /**
     * 获取年
     */
    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        return year;
    }

    /**
     * 获取年
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取月
     */
    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);//获取月份
        return month + 1;
    }

    /**
     * 获取日
     */
    public static int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);//获取日
    }

    /**
     * 获取时
     */
    public static int getHour() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);//小时
    }

    /**
     * 获取分
     */
    public static int getMinute() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);//分
    }





    /**
     * 返回定义的相册路径
     */
    public static File getFilePath() {
        File DatalDir = Environment.getExternalStorageDirectory();
        File myDir = new File(DatalDir, "/DCIM/Camera");
        myDir.mkdirs();
        String mDirectoryname = DatalDir.toString() + "/DCIM/Camera";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hhmmss", Locale.SIMPLIFIED_CHINESE);
        File tempfile = new File(mDirectoryname, sdf.format(new Date()) + ".jpg");
        if (tempfile.isFile())
            tempfile.delete();
        return tempfile;
    }

    public static String trim(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 输入金额界面
     *
     * @param money 输入的金额
     * @param str 输入的字符
     * @return
     */
    public static String formatInputPrice(String money, String str) {
        if (str.equals(".")) {
            if (!money.contains(".")) {
                //没有小数点
                money += str;
            }
        } else if (str.equals("sc")) {
            //删除
            if (Double.valueOf(money) == 0) {
                money = "0";
            } else if (money.indexOf(".") == money.length() - 1) {
                if (Double.valueOf(money) == 0) {
                    money = "0";
                } else {
                    money = money.substring(0, money.length() - 2);
                }
            } else {
                money = money.substring(0, money.length() - 1);
            }
        } else {
            if (money.equals("0.")) {
                if (!str.equals(".")) {
                    money += str;
                }
            } else {
                if (Double.valueOf(money) == 0) {
                    money = str;
                } else {
                    money += str;
                }
            }
        }
        if (TextUtils.isEmpty(money) || Double.valueOf(money) == 0) {
            if (money.equals("0.")) {
                if (!str.equals(".")) {
                    if (!str.equals("sc")) {
                        money += str;
                    } else {
                        money = "0";
                    }
                }
            } else {
                money = "0";
            }
            return money;
        }
//        money = formatPrice(money);
        return money;
    }



    /**
     * 保留两位小数
     */
    public static String format2Money(String money) {
        if (money.indexOf(".") > 0 && money.length() > money.indexOf(".") + 3) {
            money = money.substring(0, money.indexOf(".") + 3);
        }
        return money;
    }

    /**
     * 比较时间
     */
    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    /**
     * @param
     * @return
     * @描述 —— 格式化日期对象
     */
    public static Date date2date(Date date) {
        //"yyyy/MM/dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String str = sdf.format(date);
        try {
            date = sdf.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    /**
     * @param dateString
     * @return
     * @描述 —— 字符串转换成时间对象
     */
    public static Date string2date(String dateString) {
        Date formateDate = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            formateDate = format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return formateDate;
    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 以友好的方式显示时间
     */
    public static String getFriendlyTime(String dateString) {
        Date time = string2date(dateString);
        //获取time距离当前的秒数
        int ct = (int) ((System.currentTimeMillis() - time.getTime()) / 1000);
        if (ct > 0 && ct < 60) {
            return "刚刚";
        }
        if (ct >= 60 && ct < 3600) {
            return Math.max(ct / 60, 1) + "分钟前";
        }
        if (ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if (ct >= 86400 && ct < 172800) { //86400 * 2
            return "昨天";
        }
        return dateString;
    }

    /**
     * 获取屏幕大小
     * <p>无</p>
     *
     * @param context 设备上下文
     * @return 返回屏幕大小
     */
    public static Point getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new Point(screenWidth, screenHeight);
    }


    public static List<Point> get16Position(Context context) {
        //112
        Point scPoint = getDisplayMetrics(context);
        List<Point> temp = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Point point = new Point();
            int x = (scPoint.x / 8) * ((i % 4) * 2);
            int y = (scPoint.x / 8) * ((i / 4) * 2);
            if (i / 4 == 0) {
                y = Utils.dip2px(context, 113 + 30);
            } else if (i / 4 == 1) {
                y = Utils.dip2px(context, 113 + 30 * 3);
            } else if (i / 4 == 2) {
                y = Utils.dip2px(context, 113 + 30 * 5);
            } else if (i / 4 == 3) {
                y = Utils.dip2px(context, 113 + 30 * 7);
            }
            point.set(x, y);
            temp.add(point);
        }

        return temp;
    }


    /**
     * date2比date1多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2)   //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else    //不同年
        {
            System.out.println("判断day2 - day1 : " + (day2 - day1));
            return day2 - day1;
        }
    }


    /**
     * date2比date1多的天数
     *
     * @param date1
     * @return
     */
    public static boolean differentDays(int date1) {
        Date date2 = new Date();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        if (day2 - date1 > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static int nowDay() {
        Date date2 = new Date();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
        return day2;
    }
}
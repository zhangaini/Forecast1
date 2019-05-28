package gllibrary.utils;

public class GLConstants {
	/**
	 * 固定值
	 */
	//内网测试 192.168.0.25
	public static  String ServerUrl= "http://php.javaframework.cn";

	//外网正式
	//public static  String ServerUrl= "https://caipiao.goodluckchina.net";
	public static  String URL=ServerUrl+"";

	public static final boolean debug = true;//判断是否打印日志
	public static final int ACT_LOGIN = 0x000021;
	public static final int ACT_CHAGE = 0x000023;
	public static final int ACT_REFRESH = 0x000022;
	public static final int CODE_SCAN = 0x000031;
	public static final int CODE_ADDRESS = 0x000032;
}

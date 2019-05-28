package gllibrary.utils;

import android.util.Log;


public class GLLogUtil {


	/*public static void setDebug(boolean debug) {
		Constants.debug = debug;
	}*/

	public static void d(String tag, String msg) {
		if (GLConstants.debug)
			Log.d(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (GLConstants.debug)
			Log.e(tag, msg);
	}

	public static void i(String tag, String msg) {
		if (GLConstants.debug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (GLConstants.debug)
			Log.v(tag, msg);
	}

	public static void w(String tag, String msg) {
		if (GLConstants.debug)
			Log.v(tag, msg);
	}
}

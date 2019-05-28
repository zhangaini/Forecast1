package gllibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 图片加载工具类
 * 
 * @version 1.0
 * 
 */
public abstract class GLToolImage {
	public static File file;

	/**
	 * 先保存到本地再广播到图库
	 * */
	public static void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(),
				"code");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = "qrcode"+ System.currentTimeMillis() + ".jpg";
		file = new File(appDir, fileName);

		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			Uri localUri = Uri.fromFile(file);
			Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
			context.sendBroadcast(localIntent);
			Log.e("sdfas",file.getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		// 其次把文件插入到系统图库
//		try {
//			MediaStore.Images.Media.insertImage(context.getContentResolver(),
//					file.getAbsolutePath(), fileName, null);
//
//			// 最后通知图库更新
//			context.sendBroadcast(new Intent(
//					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"
//							+ file)));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		try {
//			MediaStore.Images.Media.insertImage(context.getContentResolver(),
//					file.getAbsolutePath(), fileName, null);
//			// 最后通知图库更新
//			Log.e("sdfas",file.getPath());
//			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

	}



}

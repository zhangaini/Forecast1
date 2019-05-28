package com.forecast.forecast.utils;

import android.os.Environment;

import com.forecast.forecast.MyApplication;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
	public static final String CACHE = "cache";
	public static final String IMG = "img";
	public static final String ROOT = "tuoken";
	/**
	 * 获取图片的缓存的路径
	 * @return
	 */
	public static File getImgDir(){
		return getDir(IMG);
		
	}
	/**
	 * 获取缓存路径
	 * @return
	 */
	public static File getCacheDir() {
		return getDir(CACHE);
	}
	public static File getDir(String cache) {
		StringBuilder path = new StringBuilder();
		if (isSDAvailable()) {
			path.append(Environment.getExternalStorageDirectory().getAbsolutePath());
			path.append(File.separator);
			path.append(ROOT);
			path.append(File.separator);
			path.append(cache);
			
		}else{
			File filesDir = MyApplication.getInstance().getCacheDir();    //  cache  getFileDir file
			path.append(filesDir.getAbsolutePath());
			path.append(File.separator);
			path.append(cache);
		}
		File file = new File(path.toString());
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();// 创建文件夹
		}
		return file;

	}

	private static boolean isSDAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file
					+ "' does not exist");
		}
		return new FileInputStream(file);
	}

	public static FileOutputStream openOutputStream(File file)
			throws IOException {
		return openOutputStream(file, false);
	}

	public static FileOutputStream openOutputStream(File file, boolean append)
			throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file
						+ "' cannot be written to");
			}
		} else {
			File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent
							+ "' could not be created");
				}
			}
		}
		return new FileOutputStream(file, append);
	}

	public static void closeQuietly(InputStream input) {
		closeQuietly((Closeable) input);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}


}

package io.viva.httpbase.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class FileUtils {

	/**
	 * @param file
	 * @return 目录的大小
	 */
	public static int getDirectorySize(File file) {
		if (file.listFiles() == null) {
			return 0;
		}
		int i = 0;
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				i += getDirectorySize(f);
			} else {
				i = (int) (i + f.length());
			}
		}
		return i;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public static void copyFile(File srcFile, File destFile) {
		if ((!srcFile.isFile()) || (!destFile.isFile())) {
			return;
		}
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream(srcFile);
			if (!destFile.exists()) {
				if (!destFile.getParentFile().exists()) {
					destFile.mkdirs();
				}
				destFile.createNewFile();
			}
			fileOutputStream = new FileOutputStream(destFile);
			byte[] arrayOfByte = new byte[10 * 1024];
			int i = 0;
			while ((i = fileInputStream.read(arrayOfByte)) != -1) {
				fileOutputStream.write(arrayOfByte, 0, i);
			}
			fileOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
				if (fileInputStream != null) {
					fileInputStream.close();
					fileInputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 拷贝目录
	 * 
	 * @param srcFile
	 * @param destFile
	 */
	public static void copyDir(File srcFile, File destFile) {
		if ((!srcFile.isDirectory()) || (!destFile.isDirectory())) {
			return;
		}
		if (!destFile.exists()) {
			destFile.mkdirs();
		}
		for (File f : srcFile.listFiles()) {
			if (f.isDirectory()) {
				copyDir(f, new File(destFile, f.getName()));
			} else {
				copyFile(f, new File(destFile, f.getName()));
			}
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param file
	 */
	public static void removeDir(File file) {
		if (file.exists()) {
			for (File f : file.listFiles()) {
				if (f.isDirectory()) {
					removeDir(f);
				} else {
					f.delete();
				}
			}
			file.delete();
		}
	}

	/**
	 * 读取raw数据
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static String rawRead(Context context, int resId) {
		String str = null;
		InputStream inputStream = null;
		try {
			inputStream = context.getResources().openRawResource(resId);
			int i = inputStream.available();
			byte[] arrayOfByte = new byte[i];
			inputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 读取assets数据
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readAssets(Context context, String fileName) {
		String str = null;
		InputStream inputStream = null;
		try {
			inputStream = context.getResources().getAssets().open(fileName);
			int i = inputStream.available();
			byte[] arrayOfByte = new byte[i];
			inputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * 拷贝asset文件到文件系统
	 * 
	 * @param context
	 * @param assetFile
	 * @param destFile
	 * @return
	 */
	public static boolean copyFileFromAssets(Context context, String assetFile, String destFile) {
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStream = context.getAssets().open(assetFile);
			File f = new File(destFile);
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();
			fileOutputStream = new FileOutputStream(f);
			byte[] arrayOfByte = new byte[5120];
			int i = 0;
			while ((i = inputStream.read(arrayOfByte)) != -1) {
				fileOutputStream.write(arrayOfByte, 0, i);
			}
			fileOutputStream.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 从sdcard中读取文件内容
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static String sdcardRead(Context context, String filePath) {
		String str = null;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(filePath);
			int i = fileInputStream.available();
			byte[] arrayOfByte = new byte[i];
			fileInputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
					fileInputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return str;
	}

	/**
	 * @param file
	 * @return 文件/文件夹可用空间
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getUsableSpace(File file) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return file.getUsableSpace();
		}
		StatFs statFs = new StatFs(file.getPath());
		return statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();
	}

	/**
	 * @param context
	 * @return sdcard缓存目录
	 */
	public static File getExternalCacheDir(Context context) {
		if (Build.VERSION.SDK_INT >= 8) {
			return context.getExternalCacheDir();
		}
		String str = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + str);
	}

	/**
	 * @return 外部存储是否移除
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
}

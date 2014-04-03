package io.viva.httpbase.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class FileUtils {
	public static int getDirectorySize(File paramFile) {
		if (paramFile.listFiles() == null) {
			return 0;
		}
		int i = 0;
		for (File localFile : paramFile.listFiles()) {
			if (localFile.isDirectory()) {
				i += getDirectorySize(localFile);
			} else {
				i = (int) (i + localFile.length());
			}
		}
		return i;
	}

	public static void copyFile(File paramFile1, File paramFile2) {
		if ((!paramFile1.isFile()) || (!paramFile2.isFile())) {
			return;
		}
		FileInputStream localFileInputStream = null;
		FileOutputStream localFileOutputStream = null;
		try {
			localFileInputStream = new FileInputStream(paramFile1);
			if (!paramFile2.exists()) {
				if (!paramFile2.getParentFile().exists()) {
					paramFile2.mkdirs();
				}
				paramFile2.createNewFile();
			}
			localFileOutputStream = new FileOutputStream(paramFile2);
			byte[] arrayOfByte = new byte[10240];
			int i = 0;
			while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
				localFileOutputStream.write(arrayOfByte, 0, i);
			}
			localFileOutputStream.flush();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		} finally {
			try {
				if (localFileOutputStream != null) {
					localFileOutputStream.close();
					localFileOutputStream = null;
				}
				if (localFileInputStream != null) {
					localFileInputStream.close();
					localFileInputStream = null;
				}
			} catch (IOException localIOException4) {
				localIOException4.printStackTrace();
			}
		}
	}

	public static void copyDir(File paramFile1, File paramFile2) {
		if ((!paramFile1.isDirectory()) || (!paramFile2.isDirectory())) {
			return;
		}
		if (!paramFile2.exists()) {
			paramFile2.mkdirs();
		}
		for (File localFile : paramFile1.listFiles()) {
			if (localFile.isDirectory()) {
				copyDir(localFile, new File(paramFile2, localFile.getName()));
			} else {
				copyFile(localFile, new File(paramFile2, localFile.getName()));
			}
		}
	}

	public static void removeDir(File paramFile) {
		if (paramFile.exists()) {
			for (File localFile : paramFile.listFiles()) {
				if (localFile.isDirectory()) {
					removeDir(localFile);
				} else {
					localFile.delete();
				}
			}
			paramFile.delete();
		}
	}

	public static String rawRead(Context paramContext, int paramInt) {
		String str = null;
		InputStream localInputStream = null;
		try {
			localInputStream = paramContext.getResources().openRawResource(paramInt);
			int i = localInputStream.available();
			byte[] arrayOfByte = new byte[i];
			localInputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "utf-8");
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			try {
				if (localInputStream != null) {
					localInputStream.close();
					localInputStream = null;
				}
			} catch (IOException localIOException3) {
				localIOException3.printStackTrace();
			}
		}
		return str;
	}

	public static String readAssets(Context paramContext, String paramString) {
		String str = null;
		InputStream localInputStream = null;
		try {
			localInputStream = paramContext.getResources().getAssets().open(paramString);
			int i = localInputStream.available();
			byte[] arrayOfByte = new byte[i];
			localInputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "utf-8");
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			try {
				if (localInputStream != null) {
					localInputStream.close();
					localInputStream = null;
				}
			} catch (IOException localIOException3) {
				localIOException3.printStackTrace();
			}
		}
		return str;
	}

	public static boolean copyFileFromAssets(Context paramContext, String paramString1, String paramString2) {
		InputStream localInputStream = null;
		FileOutputStream localFileOutputStream = null;
		try {
			localInputStream = paramContext.getAssets().open(paramString1);
			File localFile = new File(paramString2);
			if (localFile.exists()) {
				localFile.delete();
			}
			localFile.createNewFile();
			localFileOutputStream = new FileOutputStream(localFile);
			byte[] arrayOfByte = new byte[5120];
			int i = 0;
			while ((i = localInputStream.read(arrayOfByte)) != -1) {
				localFileOutputStream.write(arrayOfByte, 0, i);
			}
			localFileOutputStream.flush();
			boolean bool = true;
			return bool;
		} catch (IOException localIOException1) {
			localIOException1.printStackTrace();
		} finally {
			try {
				if (localFileOutputStream != null) {
					localFileOutputStream.close();
					localFileOutputStream = null;
				}
				if (localInputStream != null) {
					localInputStream.close();
					localInputStream = null;
				}
			} catch (IOException localIOException4) {
				localIOException4.printStackTrace();
			}
		}
		return false;
	}

	public static String sdcardRead(Context paramContext, String paramString) {
		String str = null;
		FileInputStream localFileInputStream = null;
		try {
			localFileInputStream = new FileInputStream(paramString);
			int i = localFileInputStream.available();
			byte[] arrayOfByte = new byte[i];
			localFileInputStream.read(arrayOfByte);
			str = EncodingUtils.getString(arrayOfByte, "UTF-8");
		} catch (Exception localException) {
			localException.printStackTrace();
		} finally {
			try {
				if (localFileInputStream != null) {
					localFileInputStream.close();
					localFileInputStream = null;
				}
			} catch (IOException localIOException3) {
				localIOException3.printStackTrace();
			}
		}
		return str;
	}

	public static long getUsableSpace(File paramFile) {
		if (Build.VERSION.SDK_INT >= 9) {
			return paramFile.getUsableSpace();
		}
		StatFs localStatFs = new StatFs(paramFile.getPath());
		return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
	}

	public static File getExternalCacheDir(Context paramContext) {
		if (Build.VERSION.SDK_INT >= 8) {
			return paramContext.getExternalCacheDir();
		}
		String str = "/Android/data/" + paramContext.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + str);
	}

	public static boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= 9) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}
}

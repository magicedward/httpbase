package io.viva.httpbase.info;

import io.viva.httpbase.utils.FileUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

public class BaseAppInfo {
	public static final int MAX_ROOT_SIZE = 100 * 1024 * 1024;
	public static final String ROOT_DIR = "viva";
	private static String appRootPath;
	private static String imagePath;
	public static int versionCode = 0;
	public static String versionName = "0.0.1";
	public static boolean isDebug = true;

	public static void init(Context context) {
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		int i = applicationInfo.flags;
		if ((i & 0x2) != 0) {
			isDebug = true;
		} else {
			isDebug = false;
		}
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			versionName = packageInfo.versionName;
			versionCode = packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param context
	 * @return 当前是否运行在后台
	 */
	public static boolean isAppBackRun(Context context) {
		ActivityManager activityManger = (ActivityManager) context.getSystemService("activity");
		List<RunningTaskInfo> list = activityManger.getRunningTasks(1);
		return (list.size() <= 0) || (!context.getPackageName().equals(((ActivityManager.RunningTaskInfo) list.get(0)).topActivity.getPackageName()));
	}

	/**
	 * @param context
	 * @return 当前是否运行在后台
	 */
	public static boolean isAppForegroundRun(Context context) {
		ActivityManager activityManger = (ActivityManager) context.getSystemService("activity");
		List<RunningAppProcessInfo> list = activityManger.getRunningAppProcesses();
		if (list == null) {
			return false;
		}
		Iterator<RunningAppProcessInfo> it = list.iterator();
		while (it.hasNext()) {
			ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) it.next();
			if ((runningAppProcessInfo.processName.equals(context.getApplicationContext().getPackageName()))
					&& (runningAppProcessInfo.importance == runningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param context
	 * @return 应用的数据存储目录
	 */
	public static String getRootPath(Context context) {
		if (appRootPath == null) {
			appRootPath = Environment.getExternalStorageDirectory().getPath() + File.pathSeparator + ROOT_DIR + File.pathSeparator + context.getPackageName() + File.pathSeparator;
			File file = new File(appRootPath);
			if (file.exists()) {
				if (FileUtils.getDirectorySize(file) > MAX_ROOT_SIZE) {
					FileUtils.removeDir(file);
					if (!file.mkdirs()) {
						appRootPath = null;
					}
				}
			} else if (!file.mkdirs()) {
				appRootPath = null;
			}
			if (appRootPath == null) {
				File f = context.getDir("images", Context.MODE_PRIVATE);
				appRootPath = f.getAbsolutePath();
			}
		}
		return appRootPath;
	}

	/**
	 * @param context
	 * @return 应用的图片缓存目录
	 */
	public static String getImagePath(Context context) {
		if (imagePath == null) {
			imagePath = getRootPath(context) + "images/";
			File file = new File(imagePath);
			if ((!file.exists()) && (!file.mkdirs())) {
				imagePath = null;
			}
		}
		return imagePath;
	}

	/**
	 * @return sdcard卡可用大小
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getSDCardAvailable() {
		File file = Environment.getExternalStorageDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long l1 = statFs.getBlockSizeLong();
		long l2 = statFs.getAvailableBlocksLong();
		return l2 * l1;
	}

	/**
	 * @return 应用程序目录可用大小
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getAvailableDataSize() {
		File file = Environment.getDataDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long l1 = statFs.getBlockSizeLong();
		long l2 = statFs.getAvailableBlocksLong();
		return l2 * l1;
	}

	/**
	 * @return 应用程序数据（包括缓存）总大小
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public static long getTotalDataSize() {
		File file = Environment.getDataDirectory();
		StatFs statFs = new StatFs(file.getPath());
		long l1 = statFs.getBlockSizeLong();
		long l2 = statFs.getBlockCountLong();
		return l2 * l1;
	}

	/**
	 * @return sdcard存储是否已满
	 */
	public static boolean isSDCardFull() {
		return getSDCardAvailable() == 0L;
	}

	/**
	 * 移除缓存目录所有缓存
	 */
	public void releasAll() {
		if (appRootPath != null) {
			File file = new File(appRootPath);
			if ((file.exists()) && (file.isDirectory())) {
				FileUtils.removeDir(file);
			}
		}
	}

	public static String getUserAgent(Context context) {
		return "SYS=Android;IP=" + MobileInfo.getIp() + ";NT=" + getNetworkType(context) + ";PT=" + MobileInfo.getOperatorNetworkType(context) + ";PN="
				+ MobileInfo.getOperatorCode(context);
	}

	/**
	 * @param context
	 * @return 活动的网络类型
	 */
	public static int getNetworkType(Context context) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
		NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.isAvailable())) {
			return localNetworkInfo.getType();
		}
		return -9999;
	}
}
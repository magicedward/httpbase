package io.viva.httpbase.info;

import io.viva.httpbase.utils.FileUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	public static void init(Context paramContext) {
		ApplicationInfo localApplicationInfo = paramContext.getApplicationInfo();
		int i = localApplicationInfo.flags;
		if ((i & 0x2) != 0) {
			isDebug = true;
		} else {
			isDebug = false;
		}
		PackageManager localPackageManager = paramContext.getPackageManager();
		try {
			PackageInfo localPackageInfo = localPackageManager.getPackageInfo(paramContext.getPackageName(), 16384);
			versionName = localPackageInfo.versionName;
			versionCode = localPackageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
	}

	public static boolean isAppBackRun(Context paramContext) {
		ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
		List<RunningTaskInfo> localList = localActivityManager.getRunningTasks(1);
		return (localList.size() <= 0) || (!paramContext.getPackageName().equals(((ActivityManager.RunningTaskInfo) localList.get(0)).topActivity.getPackageName()));
	}

	public static boolean isAppForegroundRun(Context paramContext) {
		ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
		List<RunningAppProcessInfo> localList = localActivityManager.getRunningAppProcesses();
		if (localList == null) {
			return false;
		}
		Iterator<RunningAppProcessInfo> localIterator = localList.iterator();
		while (localIterator.hasNext()) {
			ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) localIterator.next();
			if ((localRunningAppProcessInfo.processName.equals(paramContext.getApplicationContext().getPackageName()))
					&& (localRunningAppProcessInfo.importance == localRunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
				return true;
			}
		}
		return false;
	}

	public static String getRootPath(Context paramContext) {
		if (appRootPath == null) {
			appRootPath = Environment.getExternalStorageDirectory().getPath() + "/" + ROOT_DIR + "/" + paramContext.getPackageName() + "/";
			File localFile1 = new File(appRootPath);
			if (localFile1.exists()) {
				if (FileUtils.getDirectorySize(localFile1) > MAX_ROOT_SIZE) {
					FileUtils.removeDir(localFile1);
					if (!localFile1.mkdirs()) {
						appRootPath = null;
					}
				}
			} else if (!localFile1.mkdirs()) {
				appRootPath = null;
			}
			if (appRootPath == null) {
				File localFile2 = paramContext.getDir("images", Context.MODE_WORLD_WRITEABLE);
				appRootPath = localFile2.getAbsolutePath();
			}
		}
		return appRootPath;
	}

	public static String getImagePath(Context paramContext) {
		if (imagePath == null) {
			imagePath = getRootPath(paramContext) + "images/";
			File localFile = new File(imagePath);
			if ((!localFile.exists()) && (!localFile.mkdirs())) {
				imagePath = null;
			}
		}
		return imagePath;
	}

	public static long getSDCardAvailable() {
		File localFile = Environment.getExternalStorageDirectory();
		StatFs localStatFs = new StatFs(localFile.getPath());
		long l1 = localStatFs.getBlockSize();
		long l2 = localStatFs.getAvailableBlocks();
		return l2 * l1;
	}

	public static long getAvailableDataSize() {
		File localFile = Environment.getDataDirectory();
		StatFs localStatFs = new StatFs(localFile.getPath());
		long l1 = localStatFs.getBlockSize();
		long l2 = localStatFs.getAvailableBlocks();
		return l2 * l1;
	}

	public static long getTotalDataSize() {
		File localFile = Environment.getDataDirectory();
		StatFs localStatFs = new StatFs(localFile.getPath());
		long l1 = localStatFs.getBlockSize();
		long l2 = localStatFs.getBlockCount();
		return l2 * l1;
	}

	public static boolean isSDCardFull() {
		return getSDCardAvailable() == 0L;
	}

	public void releasAll() {
		if (appRootPath != null) {
			File localFile = new File(appRootPath);
			if ((localFile.exists()) && (localFile.isDirectory())) {
				FileUtils.removeDir(localFile);
			}
		}
	}

	public static String getUserAgent(Context paramContext) {
		return "SYS=Android;IP=" + MobileInfo.getIp() + ";NT=" + getNetworkType(paramContext) + ";PT=" + MobileInfo.getOperatorNetworkType(paramContext) + ";PN="
				+ MobileInfo.getOperatorCode(paramContext);
	}

	public static int getNetworkType(Context paramContext) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
		NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.isAvailable())) {
			return localNetworkInfo.getType();
		}
		return -9999;
	}
}
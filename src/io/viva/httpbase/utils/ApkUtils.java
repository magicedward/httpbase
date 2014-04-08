package io.viva.httpbase.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ApkUtils {
	
	/**
	 * @param context
	 * @param packageName 包名
	 * @return
	 */
	public static boolean checkApkExist(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> list = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : list) {
			if (packageInfo.packageName.equalsIgnoreCase(packageName)) {
				return true;
			}
		}
		return false;
	}
}

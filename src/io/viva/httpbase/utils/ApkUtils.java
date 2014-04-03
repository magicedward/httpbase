package io.viva.httpbase.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class ApkUtils {
	public static boolean checkApkExist(Context paramContext, String paramString) {
		PackageManager localPackageManager = paramContext.getPackageManager();
		List<PackageInfo> localList = localPackageManager.getInstalledPackages(0);
		for (int i = 0; i < localList.size(); i++) {
			PackageInfo localPackageInfo = (PackageInfo) localList.get(i);
			if (localPackageInfo.packageName.equalsIgnoreCase(paramString)) {
				return true;
			}
		}
		return false;
	}
}

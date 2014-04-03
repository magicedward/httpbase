package io.viva.httpbase.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class SystemUtils {
	public static final String TAG = "SystemUtils";

	private boolean isSystemIdle() {
		Runtime localRuntime = Runtime.getRuntime();
		long l1 = localRuntime.maxMemory();
		long l2 = localRuntime.totalMemory();
		long l3 = localRuntime.freeMemory();
		System.out.println("MaxMemoery:" + l1);
		System.out.println("AllocatedMemory:" + l2);
		System.out.println("freeMemeroy:" + l3);
		return true;
	}

	public static void chmod(String paramString1, String paramString2) {
		try {
			String str = "chmod " + paramString1 + " " + paramString2;
			Runtime localRuntime = Runtime.getRuntime();
			Process localProcess = localRuntime.exec(str);
			if (localProcess != null) {
				Log.i("SystemUtils", str + " ok!");
			}
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public static boolean rootCommand(String paramString) {
		Process localProcess = null;
		DataOutputStream localDataOutputStream = null;
		try {
			localProcess = Runtime.getRuntime().exec("su");
			localDataOutputStream = new DataOutputStream(localProcess.getOutputStream());
			localDataOutputStream.writeBytes(paramString + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();
			localProcess.waitFor();
		} catch (Exception localException2) {
			boolean bool = false;
			return bool;
		} finally {
			try {
				if (localDataOutputStream != null) {
					localDataOutputStream.close();
				}
				localProcess.destroy();
			} catch (Exception localException4) {
			}
		}
		return true;
	}

	public static ActivityManager.MemoryInfo getMemoryInfo(Context paramContext) {
		ActivityManager localActivityManager = (ActivityManager) paramContext.getSystemService("activity");
		ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
		localActivityManager.getMemoryInfo(localMemoryInfo);
		return localMemoryInfo;
	}

	public static int getCpuUseAngle() throws Exception {
		File localFile = new File("/proc/stat");
		BufferedReader localBufferedReader = null;
		localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));
		StringTokenizer localStringTokenizer = new StringTokenizer(localBufferedReader.readLine());
		localStringTokenizer.nextToken();
		long l1 = Long.parseLong(localStringTokenizer.nextToken());
		long l2 = Long.parseLong(localStringTokenizer.nextToken());
		long l3 = Long.parseLong(localStringTokenizer.nextToken());
		long l4 = Long.parseLong(localStringTokenizer.nextToken());
		long l5 = Long.parseLong(localStringTokenizer.nextToken());
		long l6 = Long.parseLong(localStringTokenizer.nextToken());
		long l7 = Long.parseLong(localStringTokenizer.nextToken());
		localBufferedReader.close();
		long l8 = l1 + l2 + l3 + l5 + l6 + l7;
		long l9 = l8 + l4;
		Thread.sleep(1000L);
		localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));
		localStringTokenizer = new StringTokenizer(localBufferedReader.readLine());
		localStringTokenizer.nextToken();
		long l10 = Long.parseLong(localStringTokenizer.nextToken());
		long l11 = Long.parseLong(localStringTokenizer.nextToken());
		long l12 = Long.parseLong(localStringTokenizer.nextToken());
		long l13 = Long.parseLong(localStringTokenizer.nextToken());
		long l14 = Long.parseLong(localStringTokenizer.nextToken());
		long l15 = Long.parseLong(localStringTokenizer.nextToken());
		long l16 = Long.parseLong(localStringTokenizer.nextToken());
		long l17 = l10 + l11 + l12 + l14 + l15 + l16;
		long l18 = l17 + l13;
		if (null != localBufferedReader) {
			try {
				localBufferedReader.close();
			} catch (Exception localException) {
			}
			localBufferedReader = null;
		}
		return (int) ((l17 - l8) / (l18 - l9) * 100L);
	}
}
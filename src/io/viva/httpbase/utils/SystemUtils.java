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

	/**
	 * @return 系统是否空闲
	 */
	public static boolean isSystemIdle() {
		Runtime rt = Runtime.getRuntime();
		long l1 = rt.maxMemory();
		long l2 = rt.totalMemory();
		long l3 = rt.freeMemory();
		System.out.println("MaxMemoery:" + l1);
		System.out.println("AllocatedMemory:" + l2);
		System.out.println("freeMemeroy:" + l3);
		return true;
	}

	/**
	 * chmod
	 * @param mode
	 * @param filePath
	 */
	public static void chmod(String mode, String filePath) {
		try {
			String str = "chmod " + mode + " " + filePath;
			Runtime rt = Runtime.getRuntime();
			Process p = rt.exec(str);
			if (p != null) {
				Log.i("SystemUtils", str + " ok!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行root命令
	 * @param cmdStr
	 * @return
	 */
	public static boolean rootCommand(String cmdStr) {
		Process process = null;
		DataOutputStream dataOutputStream = null;
		try {
			process = Runtime.getRuntime().exec("su");
			dataOutputStream = new DataOutputStream(process.getOutputStream());
			dataOutputStream.writeBytes(cmdStr + "\n");
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 获取内存信息
	 * @param context
	 * @return
	 */
	public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService("activity");
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		return memoryInfo;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static int getCpuUseAngle() throws Exception {
		File file = new File("/proc/stat");
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringTokenizer stringTokenizer = new StringTokenizer(bufferedReader.readLine());
		stringTokenizer.nextToken();
		long l1 = Long.parseLong(stringTokenizer.nextToken());
		long l2 = Long.parseLong(stringTokenizer.nextToken());
		long l3 = Long.parseLong(stringTokenizer.nextToken());
		long l4 = Long.parseLong(stringTokenizer.nextToken());
		long l5 = Long.parseLong(stringTokenizer.nextToken());
		long l6 = Long.parseLong(stringTokenizer.nextToken());
		long l7 = Long.parseLong(stringTokenizer.nextToken());
		bufferedReader.close();
		long l8 = l1 + l2 + l3 + l5 + l6 + l7;
		long l9 = l8 + l4;
		Thread.sleep(1000L);
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		stringTokenizer = new StringTokenizer(bufferedReader.readLine());
		stringTokenizer.nextToken();
		long l10 = Long.parseLong(stringTokenizer.nextToken());
		long l11 = Long.parseLong(stringTokenizer.nextToken());
		long l12 = Long.parseLong(stringTokenizer.nextToken());
		long l13 = Long.parseLong(stringTokenizer.nextToken());
		long l14 = Long.parseLong(stringTokenizer.nextToken());
		long l15 = Long.parseLong(stringTokenizer.nextToken());
		long l16 = Long.parseLong(stringTokenizer.nextToken());
		long l17 = l10 + l11 + l12 + l14 + l15 + l16;
		long l18 = l17 + l13;
		if (null != bufferedReader) {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			bufferedReader = null;
		}
		return (int) ((l17 - l8) / (l18 - l9) * 100L);
	}
}
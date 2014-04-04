package io.viva.base.info;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class MobileInfo {
	private static DisplayMetrics dm = null;

	public static String getOperatorName(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getApplicationContext().getSystemService("phone");
		return localTelephonyManager.getNetworkOperatorName();
	}

	public static String getOperatorCode(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
		return localTelephonyManager.getNetworkOperator();
	}

	public static int getOperatorNetworkType(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
		return localTelephonyManager.getNetworkType();
	}

	private static DisplayMetrics getDm(Context paramContext) {
		if (dm == null) {
			WindowManager localWindowManager = (WindowManager) paramContext.getSystemService("window");
			Display localDisplay = localWindowManager.getDefaultDisplay();
			dm = new DisplayMetrics();
			localDisplay.getMetrics(dm);
		}
		return dm;
	}

	public static int getScreenWidthDip(Context paramContext) {
		return (int) (getDm(paramContext).widthPixels / getDm(paramContext).density + 0.5F);
	}

	public static int getScreenHightDip(Context paramContext) {
		return (int) (getDm(paramContext).heightPixels / getDm(paramContext).density + 0.5F);
	}

	public static float getDensity(Context paramContext) {
		return getDm(paramContext).density;
	}

	public static float getDensityDpi(Context paramContext) {
		return getDm(paramContext).densityDpi;
	}

	public static int dip2px(Context paramContext, float paramFloat) {
		return (int) (paramFloat * getDensity(paramContext) + 0.5F);
	}

	public static float px2dip(Context paramContext, int paramInt) {
		return (int) (paramInt / getDensity(paramContext) + 0.5F);
	}

	public static float px2sp(Context paramContext, int paramInt) {
		return (int) (paramInt / getDm(paramContext).scaledDensity + 0.5F);
	}

	public static int sp2px(Context paramContext, float paramFloat) {
		return (int) (paramFloat * getDm(paramContext).scaledDensity + 0.5F);
	}

	public static int getScreenWidthPx(Context paramContext) {
		return getDm(paramContext).widthPixels;
	}

	public static int getScreenHeightPx(Context paramContext) {
		return getDm(paramContext).heightPixels;
	}

	public static String getScreenResolution(Context paramContext) {
		return getDm(paramContext).widthPixels + "x" + getDm(paramContext).heightPixels;
	}

	public static String getDeviceName() {
		return Build.MANUFACTURER + " : " + Build.MODEL;
	}

	public static String getImei(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
		String str = localTelephonyManager.getDeviceId();
		if ((str == null) || ("".equals(str.trim()))) {
			File localFile = new File(Environment.getExternalStorageDirectory() + "/pwmob/", "INSTALLATION");
			try {
				Object localObject;
				if (!localFile.exists()) {
					str = UUID.randomUUID().toString();
					localObject = new DataOutputStream(new FileOutputStream(localFile));
					((DataOutputStream) localObject).writeUTF(str);
					((DataOutputStream) localObject).close();
				} else {
					localObject = new DataInputStream(new FileInputStream(localFile));
					str = ((DataInputStream) localObject).readUTF();
					((DataInputStream) localObject).close();
				}
			} catch (Exception localException) {
				throw new RuntimeException(localException);
			}
		}
		return str;
	}

	public static String getFormattedKernelVersion() {
		try {
			BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/version"), 256);
			String str;
			try {
				str = localBufferedReader.readLine();
			} finally {
				localBufferedReader.close();
			}
			Pattern localPattern = Pattern
					.compile("\\w+\\s+\\w+\\s+([^\\s]+)\\s+\\(([^\\s@]+(?:@[^\\s.]+)?)[^)]*\\)\\s+\\((?:[^(]*\\([^)]*\\))?[^)]*\\)\\s+([^\\s]+)\\s+(?:PREEMPT\\s+)?(.+)");
			Matcher localMatcher = localPattern.matcher(str);
			if (!localMatcher.matches()) {
				return "Unavailable";
			}
			if (localMatcher.groupCount() < 4) {
				return "Unavailable";
			}
			return localMatcher.group(1) + "\n" + localMatcher.group(2) + " " + localMatcher.group(3) + "\n" + localMatcher.group(4);
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return "Unavailable";
	}

	public static String getTotalMemory() {
		String str1 = "/proc/meminfo";
		long l = 0L;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8 * 1024);
			String str2 = localBufferedReader.readLine();
			String[] arrayOfString = str2.split("\\s+");
			l = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return String.valueOf(l);
	}

	public static String getUUId(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
		String str1 = "" + localTelephonyManager.getDeviceId();
		String str2 = "" + localTelephonyManager.getSimSerialNumber();
		String str3 = "" + Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
		UUID localUUID = new UUID(str3.hashCode(), str1.hashCode() << 32 | str2.hashCode());
		return localUUID.toString();
	}

	public static String getIp() {
		String str = null;
		try {
			Enumeration<NetworkInterface> localEnumeration1 = NetworkInterface.getNetworkInterfaces();
			while (localEnumeration1.hasMoreElements()) {
				NetworkInterface localNetworkInterface = localEnumeration1.nextElement();
				Enumeration<InetAddress> localEnumeration2 = localNetworkInterface.getInetAddresses();
				while (localEnumeration2.hasMoreElements()) {
					InetAddress localInetAddress = localEnumeration2.nextElement();
					if (!localInetAddress.isLoopbackAddress()) {
						str = localInetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException localSocketException) {
			localSocketException.printStackTrace();
		}
		return str;
	}
}
package io.viva.httpbase.info;

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

	/**
	 * @param context
	 * @return 移动网络运营商的名字
	 */
	public static String getOperatorName(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
		return telephonyManager.getNetworkOperatorName();
	}

	/**
	 * @param context
	 * @return 运营商网络代码
	 */
	public static String getOperatorCode(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
		return telephonyManager.getNetworkOperator();
	}

	/**
	 * Returns a constant indicating the radio technology (network type)
	 * currently in use on the device for data transmission.
	 * 
	 * @return the network type
	 * 
	 * @see #NETWORK_TYPE_UNKNOWN
	 * @see #NETWORK_TYPE_GPRS
	 * @see #NETWORK_TYPE_EDGE
	 * @see #NETWORK_TYPE_UMTS
	 * @see #NETWORK_TYPE_HSDPA
	 * @see #NETWORK_TYPE_HSUPA
	 * @see #NETWORK_TYPE_HSPA
	 * @see #NETWORK_TYPE_CDMA
	 * @see #NETWORK_TYPE_EVDO_0
	 * @see #NETWORK_TYPE_EVDO_A
	 * @see #NETWORK_TYPE_EVDO_B
	 * @see #NETWORK_TYPE_1xRTT
	 * @see #NETWORK_TYPE_IDEN
	 * @see #NETWORK_TYPE_LTE
	 * @see #NETWORK_TYPE_EHRPD
	 * @see #NETWORK_TYPE_HSPAP
	 * 
	 */
	public static int getOperatorNetworkType(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
		return telephonyManager.getNetworkType();
	}

	private static DisplayMetrics getDm(Context context) {
		if (dm == null) {
			WindowManager windowManager = (WindowManager) context.getSystemService("window");
			Display localDisplay = windowManager.getDefaultDisplay();
			dm = new DisplayMetrics();
			localDisplay.getMetrics(dm);
		}
		return dm;
	}

	/**
	 * @param context
	 * @return 屏幕宽度dip
	 */
	public static int getScreenWidthDip(Context context) {
		return (int) (getDm(context).widthPixels / getDm(context).density + 0.5F);
	}

	/**
	 * @param context
	 * @return 屏幕高度dip
	 */
	public static int getScreenHightDip(Context context) {
		return (int) (getDm(context).heightPixels / getDm(context).density + 0.5F);
	}

	public static float getDensity(Context context) {
		return getDm(context).density;
	}

	public static float getDensityDpi(Context context) {
		return getDm(context).densityDpi;
	}

	public static int dip2px(Context context, float paramFloat) {
		return (int) (paramFloat * getDensity(context) + 0.5F);
	}

	public static float px2dip(Context context, int paramInt) {
		return (int) (paramInt / getDensity(context) + 0.5F);
	}

	public static float px2sp(Context context, int paramInt) {
		return (int) (paramInt / getDm(context).scaledDensity + 0.5F);
	}

	public static int sp2px(Context context, float paramFloat) {
		return (int) (paramFloat * getDm(context).scaledDensity + 0.5F);
	}

	/**
	 * @param context
	 * @return 屏幕宽度px
	 */
	public static int getScreenWidthPx(Context context) {
		return getDm(context).widthPixels;
	}

	/**
	 * @param context
	 * @return 屏幕高度px
	 */
	public static int getScreenHeightPx(Context context) {
		return getDm(context).heightPixels;
	}

	/**
	 * @param context
	 * @return 屏幕分辨率
	 */
	public static String getScreenResolution(Context context) {
		return getDm(context).widthPixels + "x" + getDm(context).heightPixels;
	}

	/**
	 * @return 设备名称
	 */
	public static String getDeviceName() {
		return Build.MANUFACTURER + " : " + Build.MODEL;
	}

	/**
	 * @param context
	 * @return IMEI号
	 */
	public static String getImei(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
		String str = telephonyManager.getDeviceId();
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

	/**
	 * @return 内核版本号
	 */
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

	/**
	 * @return 内存大小
	 */
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

	public static String getUUId(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
		String str1 = "" + telephonyManager.getDeviceId();
		String str2 = "" + telephonyManager.getSimSerialNumber();
		String str3 = "" + Settings.Secure.getString(context.getContentResolver(), "android_id");
		UUID localUUID = new UUID(str3.hashCode(), str1.hashCode() << 32 | str2.hashCode());
		return localUUID.toString();
	}

	/**
	 * @return IP地址
	 */
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
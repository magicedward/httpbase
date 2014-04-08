package io.viva.httpbase.net;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class Tools {
	public static final String TAG = "lib_http";

	public static String getInterfaceName(NetworkInterface paramNetworkInterface) {
		return paramNetworkInterface.getName().toString();
	}

	public static String getIPAddress(NetworkInterface paramNetworkInterface) {
		String str = "";
		Enumeration<InetAddress> localEnumeration = paramNetworkInterface.getInetAddresses();
		while (localEnumeration.hasMoreElements()) {
			InetAddress localInetAddress = localEnumeration.nextElement();
			str = localInetAddress.getHostAddress();
		}
		return str;
	}

	public static NetworkInterface getInternetInterface() {
		try {
			Enumeration<NetworkInterface> localEnumeration = NetworkInterface.getNetworkInterfaces();
			while (localEnumeration.hasMoreElements()) {
				NetworkInterface localNetworkInterface = localEnumeration.nextElement();
				if (!localNetworkInterface.equals(NetworkInterface.getByName("lo"))) {
					return localNetworkInterface;
				}
			}
		} catch (SocketException localSocketException) {
			Log.e(TAG, localSocketException.toString());
		}
		return null;
	}

	public static String join(String paramString, String[] paramArrayOfString) {
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < paramArrayOfString.length - 1; i++) {
			localStringBuffer.append(paramArrayOfString[i]);
			localStringBuffer.append(paramString);
		}
		localStringBuffer.append(paramArrayOfString[(paramArrayOfString.length - 1)]);
		return localStringBuffer.toString();
	}

	public static String insertCommas(String paramString) {
		if (paramString.length() < 4)
			return paramString;
		return insertCommas(paramString.substring(0, paramString.length() - 3)) + "," + paramString.substring(paramString.length() - 3, paramString.length());
	}

	public static String getTodaysDate() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar localCalendar = Calendar.getInstance();
		return localSimpleDateFormat.format(localCalendar.getTime());
	}

	public static String getTodaysTime() {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
		return localSimpleDateFormat.format(localCalendar.getTime());
	}

	public static BufferedReader readFromWeb(String paramString) throws Exception {
		return new BufferedReader(new InputStreamReader(new URL(paramString).openStream()));
	}

	public static String lastLine(String paramString) {
		String localObject1 = null;
		long l1 = 200L;
		RandomAccessFile localRandomAccessFile = null;
		try {
			localRandomAccessFile = new RandomAccessFile(paramString, "r");
			try {
				long l2 = localRandomAccessFile.length();
				if (l2 > l1) {
					localRandomAccessFile.seek(l2 - l1);
				}
				String str = "";
				while ((str = localRandomAccessFile.readLine()) != null) {
					localObject1 = str;
				}
			} catch (IOException localIOException3) {
				Log.e(TAG, localIOException3.toString());
			}
		} catch (FileNotFoundException localFileNotFoundException) {
			Log.e(TAG, localFileNotFoundException.toString());
		} finally {
			try {
				localRandomAccessFile.close();
			} catch (IOException localIOException4) {
				Log.e(TAG, localIOException4.toString());
			}
		}
		return localObject1;
	}
}
package io.viva.httpbase.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetInfoAdapter {
	public static final String TAG = "lib_http";
	private static Map<String, String> netMap = new HashMap<String, String>();
	private static Map<Integer, String> phoneType = new HashMap<Integer, String>();
	private static Map<Integer, String> networkType = new HashMap<Integer, String>();
	private boolean netExists = false;
	private boolean wifiConnected = false;
	private boolean mobileConnected = false;
	private boolean ethConnected = false;
	private boolean isRoaming = false;
	private String n_a = "n/a";
	private String strUnknown = "Unknown";

	public NetInfoAdapter(Context paramContext) {
		phoneType.put(Integer.valueOf(0), "None");
		phoneType.put(Integer.valueOf(1), "GSM");
		phoneType.put(Integer.valueOf(2), "CDMA");
		networkType.put(Integer.valueOf(0), this.strUnknown);
		networkType.put(Integer.valueOf(1), "GPRS");
		networkType.put(Integer.valueOf(2), "EDGE");
		networkType.put(Integer.valueOf(3), "UMTS");
		networkType.put(Integer.valueOf(4), "CDMA");
		networkType.put(Integer.valueOf(5), "EVDO_0");
		networkType.put(Integer.valueOf(6), "EVDO_A");
		networkType.put(Integer.valueOf(7), "1xRTT");
		networkType.put(Integer.valueOf(8), "HSDPA");
		networkType.put(Integer.valueOf(9), "HSUPA");
		networkType.put(Integer.valueOf(10), "HSPA");
		networkType.put(Integer.valueOf(11), "IDEN");
		netMap.put("state", "");
		netMap.put("interface", "");
		netMap.put("type", "");
		netMap.put("netID", "");
		netMap.put("roaming", "");
		netMap.put("ip", "");
		netMap.put("bgdata", "");
		netMap.put("data_activity", this.n_a);
		netMap.put("cell_location", this.n_a);
		netMap.put("cell_type", this.n_a);
		netMap.put("Phone_type", this.n_a);
		this.netExists = false;
		this.wifiConnected = false;
		this.mobileConnected = false;
		this.isRoaming = false;
		TelephonyManager localTelephonyManager = null;
		try {
			Log.v("lib_http", "GET TelephonyManager");
			localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
		} catch (Exception localException1) {
			Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter");
			Log.w("lib_http", "Cannot get telephony service! except: " + localException1.getMessage());
			localTelephonyManager = null;
		}
		if (localTelephonyManager != null) {
			try {
				int i = 0;
				i = localTelephonyManager.getDataActivity();
				netMap.put("data_activity", Integer.toString(i));
			} catch (Exception localException2) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter2");
				Log.w("lib_http", "Cannot get telephony data activity, except: " + localException2.getMessage());
			}
			try {
				String str1 = null;
				if (localTelephonyManager.getCellLocation() != null) {
					str1 = localTelephonyManager.getCellLocation().toString();
				}
				netMap.put("cell_location", str1);
			} catch (Exception localException3) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter3");
				Log.w("lib_http", "Cannot get telephony cell location, except: " + localException3.getMessage());
			}
			try {
				netMap.put("cell_type", getNetworkType(Integer.valueOf(localTelephonyManager.getNetworkType())));
			} catch (Exception localException4) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter4");
				Log.w("lib_http", "Cannot get telephony cell type, except: " + localException4.getMessage());
			}
			try {
				netMap.put("phone_type", getPhoneType(Integer.valueOf(localTelephonyManager.getPhoneType())));
			} catch (Exception localException5) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter5");
				Log.w("lib_http", "Cannot get telephony phone type, except: " + localException5.getMessage());
			}
		}
		ConnectivityManager localConnectivityManager = null;
		try {
			Log.v("lib_http", "GET ConnectivityManager");
			localConnectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
		} catch (Exception localException6) {
			Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter6");
			Log.w("lib_http", "Cannot get connectivity service! except: " + localException6.getMessage());
			localConnectivityManager = null;
		}
		NetworkInfo localNetworkInfo = null;
		if (null != localConnectivityManager)
			try {
				localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
			} catch (Exception localException7) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter7");
				Log.w("lib_http", "Cannot get active network info! except: " + localException7.getMessage());
				localNetworkInfo = null;
			}
		Log.v("lib_http", "check connection states");
		int j = 0;
		try {
			j = (localNetworkInfo != null) && (localNetworkInfo.isConnected()) ? 1 : 0;
		} catch (Exception localException8) {
			Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter8");
		}
		if (j != 0) {
			Log.v("lib_http", "Network is connected");
			this.netExists = true;
			netMap.put("state", "connected");
			WifiManager localWifiManager = null;
			try {
				localWifiManager = (WifiManager) paramContext.getSystemService("wifi");
			} catch (Exception localException9) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter9");
				Log.w("lib_http", "Cannot get Wifi service! except: " + localException9.getMessage());
				localWifiManager = null;
			}
			NetworkInterface localNetworkInterface = getInternetInterface();
			if (null != localNetworkInterface) {
				try {
					netMap.put("interface", localNetworkInterface.getName());
					netMap.put("ip", getIPAddress(localNetworkInterface));
				} catch (Exception localException10) {
					Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter10");
					Log.w("lib_http", localException10.toString());
				}
			}
			String str2 = "";
			try {
				str2 = localNetworkInfo.getTypeName();
				Log.v("lib_http", "Connection type is " + str2);
			} catch (Exception localException11) {
				Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter11");
				Log.w("lib_http", localException11.toString());
			}
			WifiInfo localObject;
			if ((null != localWifiManager) && (localWifiManager.isWifiEnabled())) {
				Log.v("lib_http", "Wifi connected");
				netMap.put("type", "net_type_wifi");
				localObject = null;
				this.wifiConnected = true;
				try {
					localObject = localWifiManager.getConnectionInfo();
				} catch (Exception localException15) {
					Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter12");
					localObject = null;
				}
				if (null != localObject)
					try {
						netMap.put("netID", ((WifiInfo) localObject).getSSID());
						netMap.put("speed", Integer.toString(((WifiInfo) localObject).getLinkSpeed()) + "Mbps");
					} catch (Exception localException16) {
						Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter13");
					}
			} else {
				if (str2.equalsIgnoreCase("MOBILE")) {
					this.mobileConnected = true;
					Log.v("lib_http", "Mobile connected");
					netMap.put("type", "net_type_mobile");
				} else if (str2.equalsIgnoreCase("ETH")) {
					this.ethConnected = true;
					Log.v("lib_http", "Ethernet connected");
					netMap.put("type", "net_type_ethernet");
				} else {
					Log.v("lib_http", "Unknown/unsupported network type");
					netMap.put("type", str2 + " net_type_unsupported");
				}
				try {
					String extraInfo = localNetworkInfo.getExtraInfo();
					netMap.put("netID", extraInfo);
				} catch (Exception localException12) {
					Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter14");
				}
				try {
					netMap.put("bgdata", localConnectivityManager.getBackgroundDataSetting() ? "permitted" : "denied");
					Log.v("lib_http", "bgdata: " + (String) netMap.get("bgdata"));
				} catch (Exception localException13) {
					Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter15");
				}
				try {
					this.isRoaming = localNetworkInfo.isRoaming();
				} catch (Exception localException14) {
					Log.v("lib_http", "NetInfoAdapter.NetInfoAdapter16");
					this.isRoaming = false;
				}
				if (this.isRoaming)
					netMap.put("roaming", "roaming_yes");
				else
					netMap.put("roaming", "roaming_no");
			}
		} else {
			netMap.put("state", "not_connected");
			netMap.put("dns", "");
		}
	}

	public String getPhoneType(Integer paramInteger) {
		if (phoneType.containsKey(paramInteger)) {
			return (String) phoneType.get(paramInteger);
		}
		return this.strUnknown;
	}

	public String getNetworkType(Integer paramInteger) {
		if (networkType.containsKey(paramInteger)) {
			return (String) networkType.get(paramInteger);
		}
		return this.strUnknown;
	}

	public String getInfo(String paramString) {
		return exists(paramString) ? (String) netMap.get(paramString) : "";
	}

	public boolean exists(String paramString) {
		return netMap.containsKey(paramString);
	}

	public boolean isConnected() {
		return this.netExists;
	}

	public boolean isEthConnected() {
		return this.ethConnected;
	}

	public boolean isWifiConnected() {
		return this.wifiConnected;
	}

	public boolean isMobileConnected() {
		return this.mobileConnected;
	}

	private static String getIPAddress(NetworkInterface paramNetworkInterface) {
		String str = "";
		Enumeration<InetAddress> localEnumeration = paramNetworkInterface.getInetAddresses();
		while (localEnumeration.hasMoreElements()) {
			InetAddress localInetAddress = localEnumeration.nextElement();
			str = localInetAddress.getHostAddress();
		}
		return str;
	}

	private static NetworkInterface getInternetInterface() {
		try {
			Enumeration<NetworkInterface> localEnumeration = NetworkInterface.getNetworkInterfaces();
			while (localEnumeration.hasMoreElements()) {
				NetworkInterface localNetworkInterface = localEnumeration.nextElement();
				if (!localNetworkInterface.equals(NetworkInterface.getByName("lo"))) {
					return localNetworkInterface;
				}
			}
		} catch (SocketException localSocketException) {
			Log.e("lib_http", "getInternetInterface ERROR:" + localSocketException.toString());
		} catch (Exception localException) {
			Log.v("lib_http", "NetInfoAdapter.getInternetInterface");
			Log.e("lib_http", "getInternetInterface ERROR:" + localException.toString());
		}
		return null;
	}
}
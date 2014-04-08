package io.viva.httpbase.net;

import io.viva.httpbase.exception.NoNetworkException;

import java.util.HashSet;
import java.util.Iterator;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkManager implements NetworkMonitor.INetworkCtrListener {
	public static final String TAG = "NetworkManager";
	private static NetworkManager networkManager = null;
	private Context applicationContext;
	private boolean isConnected = true;
	private NetConnectionType currentConnectionType = NetConnectionType.none;
	private NetConnectionType lastConnectionType = NetConnectionType.none;
	private NetworkMonitor mNetworkMonitor;
	private HashSet<INetworkListener> listenerSet = new HashSet<INetworkListener>();
	public static final int UNCONNECTED = -9999;

	public static NetworkManager instance() {
		if (null == networkManager) {
			networkManager = new NetworkManager();
		}
		return networkManager;
	}

	public void init(Context paramContext) {
		init(paramContext, null);
	}

	public void init(Context paramContext, NoNetworkException.NoNetworkHanler paramNoNetworkHanler) {
		this.applicationContext = paramContext;
		this.mNetworkMonitor = new NetworkMonitor(this);
		this.applicationContext.registerReceiver(this.mNetworkMonitor, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		NoNetworkException.setNoNetworkHanler(paramNoNetworkHanler);
	}

	public void release() {
		this.applicationContext.unregisterReceiver(this.mNetworkMonitor);
	}

	public void registerStateChangedListener(INetworkListener paramINetworkListener) {
		this.listenerSet.add(paramINetworkListener);
		Log.i("NetworkManager", "registerStateChangedListener, size:" + this.listenerSet.size());
	}

	public void unregisterStateChangedListener(INetworkListener paramINetworkListener) {
		this.listenerSet.remove(paramINetworkListener);
		Log.i("NetworkManager", "unregisterStateChangedListener, size:" + this.listenerSet.size());
	}

	public void setNetworkConnectedStatus(boolean paramBoolean, NetConnectionType paramNetConnectionType) {
		this.isConnected = paramBoolean;
		NetConnectionType localNetConnectionType = this.currentConnectionType;
		this.lastConnectionType = this.currentConnectionType;
		if (paramBoolean) {
			this.currentConnectionType = paramNetConnectionType;
		} else {
			this.currentConnectionType = NetConnectionType.none;
		}
		if (localNetConnectionType != paramNetConnectionType) {
			Log.i("http", "========setNetworkConnectedStatus===============" + localNetConnectionType + "---->" + paramNetConnectionType);
			Iterator<INetworkListener> localIterator = this.listenerSet.iterator();
			while (localIterator.hasNext()) {
				INetworkListener localINetworkListener = localIterator.next();
				localINetworkListener.onNetworkChanged(paramBoolean, localNetConnectionType, paramNetConnectionType);
			}
		}
	}

	public boolean isNetworkConnected() {
		return this.isConnected;
	}

	public Context getApplicationContext() {
		return this.applicationContext;
	}

	public static int getNetworkType(Context paramContext) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext.getSystemService("connectivity");
		NetworkInfo localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()) && (localNetworkInfo.isAvailable())) {
			return localNetworkInfo.getType();
		}
		return UNCONNECTED;
	}

	public static boolean isNetworkAvailable(Context paramContext) {
		return UNCONNECTED != getNetworkType(paramContext);
	}

	public static interface INetworkListener {
		public abstract void onNetworkChanged(boolean paramBoolean, NetConnectionType paramNetConnectionType1, NetConnectionType paramNetConnectionType2);
	}
}
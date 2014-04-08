package io.viva.httpbase.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class NetworkMonitor extends BroadcastReceiver {
	public static final String TAG = "lib_http";
	private final int ON_NETWORK_DISCONNECTED = 100;
	private Context mContext = null;
	private Runnable mRunnable = null;
	private Handler mHandler = null;
	private boolean ENABLE = true;
	private INetworkCtrListener iNetworkCtrListener;

	public NetworkMonitor(INetworkCtrListener paramINetworkCtrListener) {
		this.iNetworkCtrListener = paramINetworkCtrListener;
	}

	public void onReceive(Context paramContext, Intent paramIntent) {
		Log.d("lib_http", "NetworkMonitor.onReceive");
		try {
			this.mContext = paramContext;
			NetInfoAdapter localNetInfoAdapter = new NetInfoAdapter(this.mContext);
			if (localNetInfoAdapter.isConnected()) {
				String str = "NetworkMonitor: connected " + localNetInfoAdapter.getInfo("type");
				if (localNetInfoAdapter.exists("netID")) {
					str = str + " " + localNetInfoAdapter.getInfo("netID");
				}
				if (localNetInfoAdapter.exists("speed")) {
					str = str + " " + localNetInfoAdapter.getInfo("speed");
				}
				Log.v("lib_http", str);
				if (this.ENABLE) {
					String[] arrayOfString = new LableMap().getLableList();
					int i = 2;
					Log.v("lib_http",
							Tools.join(
									",",
									new String[] { Tools.getTodaysDate(), Tools.getTodaysTime(), localNetInfoAdapter.getInfo(arrayOfString[(i++)]),
											localNetInfoAdapter.getInfo(arrayOfString[(i++)]), localNetInfoAdapter.getInfo(arrayOfString[(i++)]),
											localNetInfoAdapter.getInfo(arrayOfString[(i++)]), localNetInfoAdapter.getInfo(arrayOfString[(i++)]),
											localNetInfoAdapter.getInfo(arrayOfString[(i++)]), localNetInfoAdapter.getInfo(arrayOfString[(i++)]),
											localNetInfoAdapter.getInfo(arrayOfString[(i++)]), localNetInfoAdapter.getInfo(arrayOfString[(i++)]),
											localNetInfoAdapter.getInfo(arrayOfString[(i++)]) }));
				}
				if (localNetInfoAdapter.isWifiConnected()) {
					this.iNetworkCtrListener.setNetworkConnectedStatus(true, NetConnectionType.wifi);
				} else if (localNetInfoAdapter.isMobileConnected()) {
					this.iNetworkCtrListener.setNetworkConnectedStatus(true, NetConnectionType.mobile);
				} else if (localNetInfoAdapter.isEthConnected()) {
					this.iNetworkCtrListener.setNetworkConnectedStatus(true, NetConnectionType.ether);
				} else if (localNetInfoAdapter.isConnected()) {
					this.iNetworkCtrListener.setNetworkConnectedStatus(true, NetConnectionType.ether);
				}
				localNetInfoAdapter = null;
			} else {
				Log.v("lib_http", "NetworkMonitor: not connected");
				if (null == this.mHandler)
					this.mHandler = new Handler() {
						public void handleMessage(Message paramAnonymousMessage) {
							switch (paramAnonymousMessage.what) {
							case ON_NETWORK_DISCONNECTED:
								NetConnectionType localNetConnectionType = NetworkMonitor.this.getConnectionType();
								if (localNetConnectionType == NetConnectionType.none) {
									Log.i("lib_http", "Local disconnected, stop conference");
									removeCallbacks(NetworkMonitor.this.mRunnable);
									NetworkMonitor.this.mRunnable = null;
									NetworkMonitor.this.iNetworkCtrListener.setNetworkConnectedStatus(false, NetConnectionType.none);
								} else {
									Log.i("lib_http", "Connection re-build.");
									removeCallbacks(NetworkMonitor.this.mRunnable);
									NetworkMonitor.this.iNetworkCtrListener.setNetworkConnectedStatus(true, localNetConnectionType);
									NetworkMonitor.this.mRunnable = null;
								}
								break;
							}
						}
					};
				if (null == this.mRunnable)
					this.mRunnable = new Runnable() {
						public void run() {
							NetworkMonitor.this.mHandler.obtainMessage(ON_NETWORK_DISCONNECTED).sendToTarget();
						}
					};
				this.mHandler.postDelayed(this.mRunnable, 1000L);
			}
		} catch (Exception localException) {
			Log.v("lib_http", "NetworkMonitor.onReceive");
			Log.e("lib_http", "NetworkMonitor.onReceive, failed: " + localException.getMessage());
		}
	}

	public NetConnectionType getConnectionType() {
		boolean bool = true;
		NetConnectionType localNetConnectionType = NetConnectionType.none;
		Log.d("lib_http", "check network status");
		try {
			NetInfoAdapter localNetInfoAdapter = new NetInfoAdapter(this.mContext);
			bool = localNetInfoAdapter.isConnected();
			if (localNetInfoAdapter.isWifiConnected()) {
				localNetConnectionType = NetConnectionType.wifi;
			} else if (localNetInfoAdapter.isMobileConnected()) {
				localNetConnectionType = NetConnectionType.mobile;
			} else if (localNetInfoAdapter.isEthConnected()) {
				localNetConnectionType = NetConnectionType.ether;
			} else {
				localNetConnectionType = NetConnectionType.none;
			}
		} catch (Exception localException) {
			Log.v("lib_http", "NetworkMonitor.getConnectionType");
			Log.e("lib_http", "NetworkMonitor.isConnected, failed: " + localException.getMessage());
			bool = false;
			localNetConnectionType = NetConnectionType.none;
		}
		if (bool) {
			return localNetConnectionType;
		}
		return NetConnectionType.none;
	}

	public static interface INetworkCtrListener {
		public void setNetworkConnectedStatus(boolean paramBoolean, NetConnectionType paramNetConnectionType);
	}
}
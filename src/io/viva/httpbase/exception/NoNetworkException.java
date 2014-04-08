package io.viva.httpbase.exception;

import android.content.Context;
import android.widget.Toast;

public class NoNetworkException extends BaseException {

	private static final long serialVersionUID = 1L;
	public static final String NETWORK_UNCONNECTED = "未连接网络，请检查网络设置";
	public static NoNetworkHanler mNoNetworkHanler;

	public NoNetworkException() {
	}

	public NoNetworkException(String paramString) {
		super(paramString);
	}

	public NoNetworkException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public NoNetworkException(int paramInt, String paramString, Throwable paramThrowable) {
		super(paramInt, paramString, paramThrowable);
	}

	public NoNetworkException(int paramInt, String paramString) {
		super(paramInt, paramString);
	}

	public NoNetworkException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

	public boolean handle(Context paramContext) {
		if (mNoNetworkHanler != null) {
			return mNoNetworkHanler.handle(paramContext);
		}
		Toast.makeText(paramContext, "未连接网络，请检查网络设置", Toast.LENGTH_SHORT).show();
		return true;
	}

	public static void setNoNetworkHanler(NoNetworkHanler paramNoNetworkHanler) {
		mNoNetworkHanler = paramNoNetworkHanler;
	}

	public static interface NoNetworkHanler {
		public abstract boolean handle(Context paramContext);
	}
}
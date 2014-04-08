package io.viva.httpbase.exception;

import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.conn.HttpHostConnectException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ExceptionManager {
	public static UnknownExceptionHandler unknownExceptionHandler;

	public static boolean handleException(Context paramContext, Exception paramException) {
		if (paramException == null) {
			return false;
		}
		Log.w("ExceptionManager", "ExceptionManager", paramException);
		if ((paramException instanceof HttpRequestException)) {
			return ((HttpRequestException) paramException).handle(paramContext);
		}
		if ((paramException instanceof UnknownHostException)) {
			Toast.makeText(paramContext, "请求出错,域名无法解析", Toast.LENGTH_LONG).show();
			return true;
		}
		if ((paramException instanceof InterruptedIOException)) {
			Toast.makeText(paramContext, "服务器连接超时，请检查网络设置", Toast.LENGTH_LONG).show();
			return true;
		}
		if ((paramException instanceof HttpHostConnectException)) {
			HttpHostConnectException localHttpHostConnectException = (HttpHostConnectException) paramException;
			Toast.makeText(paramContext, "服务器连接出错:" + localHttpHostConnectException.getHost().getHostName(), Toast.LENGTH_LONG).show();
			return true;
		}
		if ((paramException instanceof SocketException)) {
			Toast.makeText(paramContext, "网络数据传输出错", Toast.LENGTH_LONG).show();
			return true;
		}
		if ((paramException instanceof BaseException)) {
			return ((BaseException) paramException).handle(paramContext);
		}
		if (unknownExceptionHandler != null) {
			unknownExceptionHandler.handle(paramException);
		}
		return false;
	}

	public static interface UnknownExceptionHandler {
		public abstract void handle(Exception paramException);
	}
}